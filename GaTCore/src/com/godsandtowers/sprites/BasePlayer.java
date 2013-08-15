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

public class BasePlayer implements Upgradeable, Externalizable {

	public static final int STARTING_INCOME = 10;

	public static BasePlayer getBasePlayer() {
		return new BasePlayer();
	}

	// Upgradeable stats
	public static final int GOLD_UPGRADE_RATE = 10;
	public static final int LIFE_UPGRADE_RATE = 1;
	private float startingGold = 20;
	private float startingLife = 20;
	private int startingGoldUpgradeCount = 1;
	private int startingLifeUpgradeCount = 1;

	public BasePlayer() {
	}

	public BasePlayer(BasePlayer basePlayer) {
		this.startingGold = basePlayer.startingGold;
		this.startingLife = basePlayer.startingLife;
	}

	public BasePlayer(float startingGold, float startingLife) {
		this.startingGold = startingGold;
		this.startingLife = startingLife;
	}

	@Override
	public String toString() {
		return "BasePlayer [startingGold=" + startingGold + ", startingLife=" + startingLife
				+ ", startingGoldUpgradeCount=" + startingGoldUpgradeCount + ", startingLifeUpgradeCount="
				+ startingLifeUpgradeCount + "]";
	}

	public int getStartingIncome() {
		return STARTING_INCOME;
	}

	public float getStartingGold() {
		return startingGold;
	}

	public float getStartingLife() {
		return startingLife;
	}

	private static final int STARTING_GOLD_ID = 0;
	private static final int STARTING_LIFE_ID = 1;
	private static final String STARTING_GOLD = "stat_gold";
	private static final String STARTING_LIFE = "stat_life";

	@Override
	public String getName() {
		return "BasePlayer";
	}

	@Override
	public String getUpgradeName(int id) {
		switch (id) {
		case STARTING_GOLD_ID:
			return STARTING_GOLD;
		case STARTING_LIFE_ID:
			return STARTING_LIFE;
		default:
			throw new RuntimeException("Unknown getUpgradeCost ID: " + id);
		}
	}

	@Override
	public int[] getUpgradeIDs() {
		return new int[] { STARTING_GOLD_ID, STARTING_LIFE_ID };
	}

	@Override
	public long getUpgradeCost(int id) {
		switch (id) {
		case STARTING_GOLD_ID:
			return (long) Math.pow(100, startingGoldUpgradeCount);
		case STARTING_LIFE_ID:
			return (long) Math.pow(100, startingLifeUpgradeCount);
		default:
			throw new RuntimeException("Unknown getUpgradeCost ID: " + id);
		}
	}

	@Override
	public float getBaseValue(int id) {
		switch (id) {
		case STARTING_GOLD_ID:
			return startingGold;
		case STARTING_LIFE_ID:
			return startingLife;
		default:
			throw new RuntimeException("Unknown getBaseValue ID: " + id);
		}
	}

	@Override
	public float getUpgradedValue(int id) {
		switch (id) {
		case STARTING_GOLD_ID:
			return startingGold + GOLD_UPGRADE_RATE;
		case STARTING_LIFE_ID:
			return startingLife + LIFE_UPGRADE_RATE;
		default:
			throw new RuntimeException("Unknown getUpgradedValue ID: " + id);
		}
	}

	@Override
	public void upgrade(int id) {
		switch (id) {
		case STARTING_GOLD_ID:
			startingGold += GOLD_UPGRADE_RATE;
			startingGoldUpgradeCount++;
			break;
		case STARTING_LIFE_ID:
			startingLife += LIFE_UPGRADE_RATE;
			startingLifeUpgradeCount++;
			break;
		default:
			throw new RuntimeException("Unknown upgrade ID: " + id);
		}

	}

	@Override
	public int getUpgradeCount(int id) {
		switch (id) {
		case STARTING_GOLD_ID:
			return startingGoldUpgradeCount;
		case STARTING_LIFE_ID:
			return startingLifeUpgradeCount;
		default:
			throw new RuntimeException("Unknown getBaseValue ID: " + id);
		}
	}

	@Override
	public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
		startingGold = input.readFloat();
		startingLife = input.readFloat();
		startingGoldUpgradeCount = input.readInt();
		startingLifeUpgradeCount = input.readInt();
	}

	@Override
	public void writeExternal(ObjectOutput output) throws IOException {
		output.writeFloat(startingGold);
		output.writeFloat(startingLife);
		output.writeInt(startingGoldUpgradeCount);
		output.writeInt(startingLifeUpgradeCount);
	}

}
