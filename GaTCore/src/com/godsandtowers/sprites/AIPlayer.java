/**
 * Copyright (C) 2013 Gundog Studios LLC.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.godsandtowers.sprites;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.core.grid.Grid;
import com.godsandtowers.core.grid.GridSquare;
import com.godsandtowers.messaging.LogicMessageProcessor;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.util.FastMath;

public class AIPlayer extends Player {
	private static final int LOST_LIFE_DELAY = 20000;
	private static final int MOVE_DELAY = 500;

	private boolean defends;
	private boolean attacks;
	private float aggressiveness;
	private ArrayList<GridSquare> buildOrder;
	private int moveDelay;
	private float lastTurnsLife;
	private long lostLifeDelay;

	// this is ONLY for testing, it uses the arg2 of a message handler to store which game engine it is calling
	// this is a complete hack and will be removed when use of googles message handling service is taken out of the code
	private int gameID = 0;

	public void setGameID(int id) {
		this.gameID = id;
	}

	public AIPlayer() {
	}

	public AIPlayer(int id, PlayerStats playerStats, int races, Grid grid, boolean defends, boolean attacks) {
		this(id, playerStats, races, grid, defends, attacks, null);
	}

	public AIPlayer(int id, PlayerStats playerStats, int races, Grid grid, boolean defends, boolean attacks,
			ArrayList<GridSquare> buildOrder) {
		super(id, playerStats, races, grid);
		this.defends = defends;
		this.attacks = attacks;
		this.aggressiveness = FastMath.random() + .5f;
		this.buildOrder = buildOrder;

		if (defends && buildOrder == null) {
			long start = System.currentTimeMillis();
			this.buildOrder = super.grid.calculateBuildSquares();
			Modules.LOG.info("AIPlayer", "TOOK " + (System.currentTimeMillis() - start) + " to calculate build order");
		}
		spendXP();
		setCostUpgradeThreshold();
		lastTurnsLife = super.getLife();
		lostLifeDelay = 0;
	}

	private void spendXP() {
		// TODO account for race experience
		long xp = playerStats.getTotalXP();

		xp -= purchase(playerStats.getBasePlayer(), xp / 2);

		xp *= Races.ALL_RACES.length / 2; // # of races with the experience

		xp -= purchase(playerStats.getBaseRace(), xp / 3);

		Collection<BaseTower> towers = super.getBaseTowers();
		if (defends) {
			long spendXP = xp / towers.size() / (attacks ? 2 : 1);
			for (BaseTower tower : towers) {
				tower.setUnlocked(true);
				xp -= purchase(tower, spendXP);
			}
		}
		if (attacks) {
			Collection<BaseCreature> creatures = super.getBaseCreatures();
			long spendXP = xp / creatures.size();
			for (BaseCreature creature : creatures) {
				creature.setUnlocked(true);
				purchase(creature, spendXP);
			}
		}
	}

	public void setCostUpgradeThreshold() {

		float maxCost = 0;

		for (BaseCreature creature : creatures.values()) {

			float cost = creature.getCost();
			if (cost > maxCost)
				maxCost = cost;
		}

		super.costUpgradeThreshold = maxCost * COST_VS_INCOME_UPGRADE_FACTOR;

	}

	private long purchase(Upgradeable upgradable, long xp) {
		long spentXP = 0;
		int[] ids = upgradable.getUpgradeIDs();
		long perID = xp / ids.length;
		for (int id : ids) {
			long cost = upgradable.getUpgradeCost(id);
			long currXP = perID;
			while (cost > 0 && currXP >= cost) {
				currXP -= cost;
				spentXP += cost;
				upgradable.upgrade(id);
				cost = upgradable.getUpgradeCost(id);
			}
		}

		return spentXP;
	}

	@Override
	public void nextTurn(int timePassed, int timeBetweenWaves) {
		super.nextTurn(timePassed, timeBetweenWaves);

		moveDelay -= timePassed;

		if (moveDelay <= 0) {
			if (calculateMoves(timePassed))
				moveDelay += MOVE_DELAY;
			else
				moveDelay = 0;
		}
	}

	public void increaseIncome(float amount) {
		if (defends) {
			super.increaseIncome(amount / 2);
		}
	}

	public void addIncome(int currentWave) {
		if (!defends) {
			super.income += ((float) currentWave) / 10f;
		}
		super.addIncome(currentWave);
	}

	public boolean calculateMoves(int timePassed) {

		if (defends && attacks) {
			if (lastTurnsLife != super.getLife()) {
				lostLifeDelay += LOST_LIFE_DELAY;
			}
			lastTurnsLife = super.getLife();

			lostLifeDelay -= timePassed;
			if (lostLifeDelay <= 0) {
				lostLifeDelay = 0;
			} else {
				return buyOrUpgrade(timePassed);
			}

			float creaturePower = calculateCreaturePower();
			float towerPower = calculateTowerPower() * aggressiveness;

			// only do one thing a turn to make the computer more "human"
			if (creaturePower > towerPower) {
				return buyOrUpgrade(timePassed);
			} else {
				return buyCreature(timePassed);
			}
		} else if (attacks) {
			return buyRandomCreature(timePassed);
		} else if (defends) {
			return buyOrUpgrade(timePassed);
		} else {
			return false;
		}

	}

	private boolean buyOrUpgrade(long timePassed) {
		float remainingGold = gold;
		BaseTower[] sorted = super.getSortedBaseTowers();

		boolean successful = buyTower(sorted, remainingGold);

		if (!successful)
			successful = upgradeTower(remainingGold);

		return successful;
	}

	private float calculateCreaturePower() {
		float creaturePower = 0f;
		for (Creature creature : super.grid.getCreatures()) {
			creaturePower += creature.getPower();
		}
		return creaturePower;
	}

	private float calculateTowerPower() {
		float towerPower = 0f;
		for (Tower tower : super.grid.getTowers()) {
			towerPower += tower.getPower();
		}
		return towerPower;
	}

	private boolean upgradeTower(float remainingGold) {

		Collection<Tower> towers = grid.getTowers();
		int num = FastMath.floor(FastMath.random() * towers.size());

		Iterator<Tower> iter = towers.iterator();
		Tower oldTower = null;
		while (num-- > 0)
			oldTower = iter.next();

		if (oldTower == null)
			return false;

		if (oldTower.getCost() <= remainingGold) {
			Modules.MESSENGER.submit(LogicMessageProcessor.ID, LogicMessageProcessor.UPGRADE_TOWER, id,
					oldTower.getX(), oldTower.getY(), gameID);
			return true;
		}
		return false;
	}

	private boolean buyTower(BaseTower[] sorted, float remainingGold) {
		for (GridSquare square : buildOrder) {

			if (!square.canPlaceTower()) {
				continue;
			}
			int num = FastMath.floor(FastMath.random() * sorted.length);
			BaseTower tower = sorted[num];

			if (tower.getCost() <= remainingGold) {
				Modules.MESSENGER.submit(LogicMessageProcessor.ID, LogicMessageProcessor.BUY_TOWER, id,
						tower.getName(), square.getX(), square.getY(), gameID);
				return true;
			}
			return false;
		}
		return false;
	}

	private boolean buyRandomCreature(long timePassed) {
		float lastGoldAmount = 0;
		float remainingGold = gold;
		BaseCreature[] creatures = super.getSortedBaseCreatures();
		int count = creatures.length;
		lastGoldAmount = remainingGold;
		while (lastGoldAmount == remainingGold && count >= 0) {
			int loc = FastMath.floor(FastMath.random() * creatures.length);
			BaseCreature creature = creatures[loc];

			if (remainingGold >= creature.getCost() && timePassed > 0) {
				Modules.MESSENGER.submit(LogicMessageProcessor.ID, LogicMessageProcessor.BUY_CREATURE, id,
						creature.getName(), gameID);
				return true;
			}

			count--;
		}
		return false;
	}

	private boolean buyCreature(long timePassed) {
		BaseCreature[] creatures = super.getSortedBaseCreatures();

		for (int i = creatures.length - 1; i >= 0; i--) {
			BaseCreature creature = creatures[i];
			if (gold >= creature.getCost()) {
				Modules.MESSENGER.submit(LogicMessageProcessor.ID, LogicMessageProcessor.BUY_CREATURE, id,
						creature.getName(), gameID);

				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
		super.readExternal(input);
		defends = input.readBoolean();
		attacks = input.readBoolean();
		aggressiveness = input.readFloat();
		moveDelay = input.readInt();
		buildOrder = (ArrayList<GridSquare>) input.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput output) throws IOException {
		super.writeExternal(output);
		output.writeBoolean(defends);
		output.writeBoolean(attacks);
		output.writeFloat(aggressiveness);
		output.writeInt(moveDelay);
		output.writeObject(buildOrder);
	}

}
