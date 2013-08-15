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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;

import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.core.grid.Grid;
import com.godsandtowers.messaging.ViewMessageProcessor;
import com.gundogstudios.gl.Sprite;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.util.FastMath;

public class Player implements Externalizable {

	private static final int AUTO_BUY_DELAY = 1000;// ms
	protected static final float COST_VS_INCOME_UPGRADE_FACTOR = 4f;

	protected int id;
	protected PlayerStats playerStats;

	protected float income;
	protected float gold;
	protected float life;
	protected float costUpgradeThreshold;
	protected int creatureLevel;

	protected boolean attacking;

	protected Race race;
	protected Grid grid;

	protected BaseTower[] sortedTowers;
	protected BaseCreature[] sortedCreatures;
	protected HashMap<Integer, HashMap<String, BaseTower>> towers;
	protected HashMap<String, BaseCreature> creatures;
	protected HashMap<String, BaseSpecial> specials;
	protected String autoBuyCreatureName;
	private int autoBuyCooldown;

	public Player() {
	}

	public Player(int id, PlayerStats playerStats, int races, Grid grid) {
		this.playerStats = playerStats;
		this.id = id;
		this.grid = grid;
		this.race = new Race(playerStats.getBaseRace(), races);
		this.attacking = false;
		this.creatureLevel = 1;
		this.autoBuyCreatureName = null;
		autoBuyCooldown = 0;
		this.income = playerStats.getBasePlayer().getStartingIncome();
		this.gold = playerStats.getBasePlayer().getStartingGold();
		this.life = playerStats.getBasePlayer().getStartingLife();

		float maxCost = 0;

		this.creatures = new HashMap<String, BaseCreature>();
		for (BaseCreature creature : playerStats.getBaseCreatures()) {
			if (!Races.isRaces(race.getRaces(), creature.getRaces())) {
				continue;
			}
			float cost = creature.getCost();
			if (creature.isUnlocked() && cost > maxCost)
				maxCost = cost;
			creatures.put(creature.getName(), creature);
		}

		costUpgradeThreshold = maxCost * COST_VS_INCOME_UPGRADE_FACTOR;

		this.towers = new HashMap<Integer, HashMap<String, BaseTower>>();
		HashMap<String, BaseTower> towerMap = new HashMap<String, BaseTower>();
		for (BaseTower tower : playerStats.getBaseTowers()) {
			if (!race.isRaces(tower.getRaces())) {
				continue;
			}
			towerMap.put(tower.getName(), tower);
		}
		towers.put(1, towerMap);

		specials = new HashMap<String, BaseSpecial>();
		for (BaseSpecial baseSpecial : playerStats.getSpecials()) {
			specials.put(baseSpecial.getName(), baseSpecial);
		}

	}

	public BaseSpecial getSpecial(String name) {
		return specials.get(name);
	}

	public boolean timeToAutoBuy(int timePassed) {
		autoBuyCooldown -= timePassed;
		if (autoBuyCooldown <= 0) {
			autoBuyCooldown += AUTO_BUY_DELAY;
			return true;
		} else {
			return false;
		}

	}

	public String getAutoBuyCreatureName() {
		return autoBuyCreatureName;
	}

	public void cancelAutoBuyCreature() {
		this.autoBuyCreatureName = null;
	}

	public void setAutoBuyCreature(String name) {
		this.autoBuyCreatureName = name;
	}

	public void setIncome(float income) {
		this.income = income;
	}

	public void setGold(float gold) {
		this.gold = gold;
	}

	public void setLife(float life) {
		this.life = life;
	}

	public void setCreatureLevel(int creatureLevel) {
		this.creatureLevel = creatureLevel;
	}

	public int getCreatureLevel() {
		return creatureLevel;
	}

	public float getCostUpgradeThreshold() {
		return costUpgradeThreshold;
	}

	public boolean isAttacking() {
		return attacking;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public int getID() {
		return id;
	}

	public void nextTurn(int timePassed, int timeBetweenWaves) {
		if (income > costUpgradeThreshold)
			upgradeCreatures();
		if (attacking) {
			float temp = timePassed;
			temp /= timeBetweenWaves;
			temp *= income;
			gold -= temp;
			if (gold <= 0) {
				attacking = false;
				gold = 0;

				Modules.MESSENGER.submit(ViewMessageProcessor.ID, ViewMessageProcessor.TURN_OFF_ATTACKING);
			}
		}
	}

	public Race getRace() {
		return race;
	}

	public Grid getGrid() {
		return grid;
	}

	public float getIncome() {
		return income;
	}

	public float getGold() {
		return gold;
	}

	public float getLife() {
		return life;
	}

	public ArrayList<Creature> moveCreatures(int timePassed) {
		ArrayList<Creature> remove = grid.moveCreatures(timePassed);
		life -= remove.size();
		if (life < 0.0f)
			life = 0.0f;
		return remove;
	}

	public ArrayList<Sprite> updateDyingSprites(int timePassed) {
		return grid.updateDyingSprites(timePassed);
	}

	private void addGoldFromKills(ArrayList<Creature> creaturesKilled) {
		for (Creature creature : creaturesKilled) {
			gold += creature.getIncome();
		}
	}

	public ArrayList<Creature> attackCreatures(int timePassed, ArrayList<Projectile> newProjectiles) {
		ArrayList<Creature> creaturesKilled = grid.attackCreatures(timePassed, newProjectiles);
		addGoldFromKills(creaturesKilled);
		return creaturesKilled;
	}

	public ArrayList<Tower> attackTowers(int timePassed) {
		return grid.attackTowers(timePassed);
	}

	public ArrayList<Creature> moveProjectiles(int timePassed, ArrayList<MovingProjectile> uselessProjectiles) {
		ArrayList<Creature> creaturesKilled = grid.moveProjectiles(timePassed, uselessProjectiles);
		addGoldFromKills(creaturesKilled);
		return creaturesKilled;
	}

	public void addIncome(int currentWave) {
		gold += income;
	}

	public void increaseIncome(float amount) {
		float sum = income + amount;
		if (sum < playerStats.getBasePlayer().getStartingIncome())
			income = playerStats.getBasePlayer().getStartingIncome();
		else
			income = sum;
	}

	public void decreaseGold(float amount) {
		gold -= amount;
	}

	public void increaseGold(float amount) {
		gold += amount;
	}

	public PlayerStats getPlayerStats() {
		return playerStats;
	}

	public BasePlayer getBasePlayer() {
		return playerStats.getBasePlayer();
	}

	public BaseCreature getCreature(String name) {
		return creatures.get(name);
	}

	public BaseTower getTower(String name) {
		return towers.get(1).get(name);
	}

	public Collection<BaseCreature> getBaseCreatures() {
		return creatures.values();
	}

	public BaseCreature[] getSortedBaseCreatures() {
		if (sortedCreatures == null) {
			Collection<BaseCreature> collection = creatures.values();
			sortedCreatures = collection.toArray(new BaseCreature[collection.size()]);
			Arrays.sort(sortedCreatures, new Comparator<BaseCreature>() {
				@Override
				public int compare(BaseCreature o1, BaseCreature o2) {

					int cost = FastMath.floor(o1.getCost() - o2.getCost());
					if (cost != 0) {
						return cost;
					}

					cost = o1.getRaces() - o2.getRaces();
					if (cost != 0) {
						return cost;
					}
					return o1.getName().compareTo(o2.getName());
				}
			});
		}
		return sortedCreatures;
	}

	public Collection<BaseTower> getBaseTowers() {
		return towers.get(1).values();
	}

	public BaseTower[] getSortedBaseTowers() {
		if (sortedTowers == null) {
			Collection<BaseTower> collection = towers.get(1).values();
			sortedTowers = collection.toArray(new BaseTower[collection.size()]);
			Arrays.sort(sortedTowers, new Comparator<BaseTower>() {
				@Override
				public int compare(BaseTower o1, BaseTower o2) {
					int cost = FastMath.floor(o1.getCost() - o2.getCost());
					if (cost != 0) {
						return cost;
					}

					cost = o1.getRaces() - o2.getRaces();
					if (cost != 0) {
						return cost;
					}
					return o1.getName().compareTo(o2.getName());
				}
			});
		}
		return sortedTowers;
	}

	public void upgradeCreatures() {
		Collection<BaseCreature> upgraded = BaseCreature.getUpgradedCreatures(creatures.values());
		float maxCost = 0;
		for (BaseCreature creature : upgraded) {
			float cost = creature.getCost();
			if (creature.isUnlocked() && cost > maxCost)
				maxCost = cost;
			creatures.put(creature.getName(), creature);
		}
		costUpgradeThreshold = maxCost * COST_VS_INCOME_UPGRADE_FACTOR;
		creatureLevel++;
		sortedCreatures = null;
	}

	public BaseTower getUpgrade(BaseTower tower) {
		int level = tower.getLevel() + 1;
		HashMap<String, BaseTower> towerMap = towers.get(level);
		if (towerMap == null) {
			towerMap = new HashMap<String, BaseTower>();
			towers.put(level, towerMap);
		}
		String name = tower.getName();
		BaseTower upgraded = towerMap.get(name);
		if (upgraded == null) {
			upgraded = BaseTower.createUpgrade(tower);
			towerMap.put(name, upgraded);
		}
		return upgraded;
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", income=" + income + ", gold=" + gold + ", life=" + life + ", creatureLevel="
				+ creatureLevel + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
		id = input.readInt();
		playerStats = (PlayerStats) input.readObject();
		income = input.readFloat();
		gold = input.readFloat();
		life = input.readFloat();
		costUpgradeThreshold = input.readFloat();
		creatureLevel = input.readInt();
		race = (Race) input.readObject();
		grid = (Grid) input.readObject();
		towers = (HashMap<Integer, HashMap<String, BaseTower>>) input.readObject();
		creatures = (HashMap<String, BaseCreature>) input.readObject();
		specials = (HashMap<String, BaseSpecial>) input.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput output) throws IOException {
		output.writeInt(id);
		output.writeObject(playerStats);
		output.writeFloat(income);
		output.writeFloat(gold);
		output.writeFloat(life);
		output.writeFloat(costUpgradeThreshold);
		output.writeInt(creatureLevel);
		output.writeObject(race);
		output.writeObject(grid);
		output.writeObject(towers);
		output.writeObject(creatures);
		output.writeObject(specials);
	}
}
