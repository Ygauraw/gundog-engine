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

public class BaseTower implements Upgradeable, Externalizable {

	// ICE
	public static final String WATER_CANNON = "water_cannon";
	public static final String WATER_CATAPULT = "water_catapult";
	public static final String WATER_PILLAR = "water_pillar";
	public static final String WATER_EMITTER = "water_emitter";

	// WIND
	public static final String LIGHTNING_CANNON = "lightning_cannon";
	public static final String GRIFFIN_STATUE = "griffin_statue";
	public static final String LIGHTNING_PILLAR = "lightning_pillar";
	public static final String LIGHTNING_EMITTER = "lightning_emitter";

	// EARTH
	public static final String ROCK_CANNON = "rock_cannon";
	public static final String STONE_CATAPULT = "stone_catapult";
	public static final String EARTH_PILLAR = "earth_pillar";
	public static final String DIRT_EMITTER = "dirt_emitter";

	// FIRE
	public static final String FLAME_CANNON = "flame_cannon";
	public static final String FLAME_CATAPULT = "flame_catapult";
	public static final String FIRE_PILLAR = "fire_pillar";
	public static final String FIRE_EMITTER = "fire_emitter";

	// LIFE
	public static final String ENERGY_CANNON = "energy_cannon";
	public static final String ANGEL_STATUE = "angel_statue";
	public static final String LIFE_PILLAR = "life_pillar";
	public static final String HOLY_WATER_EMITTER = "holy_water_emitter";

	// DEATH
	public static final String POISON_CANNON = "poison_cannon";
	public static final String GARGOYLE_STATUE = "gargoyle_statue";
	public static final String UNDEAD_PILLAR = "undead_pillar";
	public static final String TOXIC_GAS_EMITTER = "toxic_gas_emitter";

	// COMBO
	public static final String FLAME_WHIP = "flame_whip";
	public static final String WATER_WHIP = "water_whip";
	public static final String TENTICAL_WHIP = "tentical_whip";
	public static final String FLAME_CRYSTALLIZER = "flame_crystallizer";
	public static final String ICE_CRYSTALLIZER = "ice_crystallizer";
	public static final String EARTH_CRYSTALLIZER = "earth_crystallizer";
	public static final String LIGHTNING_BLADE = "lightning_blade";
	public static final String ICE_BLADE = "ice_blade";
	public static final String FLAME_BLADE = "flame_blade";
	public static final String EARTH_BALLISTA = "earth_ballista";
	public static final String ICE_BALLISTA = "ice_ballista";
	public static final String FLAME_BALLISTA = "flame_ballista";

	private static final BaseTower[] BASE_TOWERS = {
			new BaseTower(WATER_CANNON, 1, 5.0f, 6.0f, 20.0f, 1.0f, 3000.0f, 3f, true, true, false, false, true),
			new BaseTower(ENERGY_CANNON, 16, 6.0f, 6.0f, 20.0f, 1.0f, 3000.0f, 3f, true, true, false, false, true),
			new BaseTower(POISON_CANNON, 32, 7.0f, 6.0f, 20.0f, 1.0f, 3000.0f, 3f, true, true, false, false, true),
			new BaseTower(LIGHTNING_CANNON, 2, 8.0f, 6.0f, 20.0f, 1.0f, 3000.0f, 3f, true, true, false, false, true),
			new BaseTower(ROCK_CANNON, 4, 9.0f, 5.0f, 20.0f, 1.0f, 4000.0f, 3f, true, true, false, false, true),
			new BaseTower(FLAME_CANNON, 8, 10.0f, 3.0f, 20.0f, 1.0f, 4000.0f, 3f, true, true, false, false, true),
			new BaseTower(DIRT_EMITTER, 4, 11.0f, 2.0f, 40.0f, 1.0f, 4000.0f, 1.5f, true, false, true, true, true),
			new BaseTower(FIRE_EMITTER, 8, 12.0f, 3.0f, 40.0f, 1.0f, 4000.0f, 1.5f, true, false, true, true, true),
			new BaseTower(HOLY_WATER_EMITTER, 16, 13.0f, 6.0f, 40.0f, 1.0f, 4000.0f, 1.5f, true, false, true, true,
					true),
			new BaseTower(TOXIC_GAS_EMITTER, 32, 14.0f, 6.0f, 40.0f, 1.0f, 4000.0f, 1.5f, true, false, true, true, true),
			new BaseTower(WATER_EMITTER, 1, 15.0f, 6.0f, 40.0f, 1.0f, 4000.0f, 1.5f, true, false, true, true, true),
			new BaseTower(LIGHTNING_EMITTER, 2, 16.0f, 6.0f, 40.0f, 1.0f, 8000.0f, 1.5f, true, false, true, true, true),
			new BaseTower(TENTICAL_WHIP, 34, 17.0f, 50.0f, 60.0f, 2.0f, 2000.0f, 1.5f, true, false, false, true, true),
			new BaseTower(WATER_WHIP, 3, 18.0f, 50.0f, 60.0f, 2.0f, 2000.0f, 1.5f, true, false, false, true, true),
			new BaseTower(FLAME_WHIP, 10, 19.0f, 50.0f, 60.0f, 2.0f, 2000.0f, 1.5f, true, false, false, true, true),
			new BaseTower(ICE_CRYSTALLIZER, 17, 20.0f, 40.0f, 60.0f, 2.0f, 3000.0f, 3.0f, true, true, false, false,
					true),
			new BaseTower(EARTH_CRYSTALLIZER, 20, 21.0f, 55.0f, 60.0f, 2.0f, 3000.0f, 3.0f, true, true, false, false,
					true),
			new BaseTower(FLAME_CRYSTALLIZER, 24, 22.0f, 25.0f, 60.0f, 2.0f, 3000.0f, 3.0f, true, true, false, false,
					true),
			new BaseTower(GRIFFIN_STATUE, 2, 23.0f, 24.0f, 80.0f, 2.0f, 3000.0f, 1.5f, true, false, false, true, true),
			new BaseTower(ANGEL_STATUE, 16, 24.0f, 24.0f, 80.0f, 2.0f, 2000.0f, 1.5f, true, false, false, true, true),
			new BaseTower(GARGOYLE_STATUE, 32, 25.0f, 25.0f, 80.0f, 2.0f, 2000.0f, 1.5f, true, false, false, true, true),
			new BaseTower(WATER_CATAPULT, 1, 26.0f, 52.0f, 80.0f, 1.0f, 3000.0f, 5f, true, false, false, false, true),
			new BaseTower(STONE_CATAPULT, 4, 27.0f, 20.0f, 80.0f, 1.0f, 4000.0f, 5f, true, false, false, false, true),
			new BaseTower(FLAME_CATAPULT, 8, 28.0f, 14.0f, 80.0f, 1.0f, 4000.0f, 5f, true, false, false, false, true),
			new BaseTower(ICE_BLADE, 5, 29.0f, 90.0f, 100.0f, 2.0f, 2000.0f, 1.5f, true, false, false, true, true),
			new BaseTower(FLAME_BLADE, 12, 30.0f, 60.0f, 100.0f, 2.0f, 2000.0f, 1.5f, true, false, false, true, true),
			new BaseTower(LIGHTNING_BLADE, 18, 31.0f, 70.0f, 100.0f, 2.0f, 2000.0f, 1.5f, true, false, false, true,
					true),
			new BaseTower(EARTH_BALLISTA, 36, 32.0f, 150.0f, 100.0f, 1.0f, 4000.0f, 5.0f, true, false, false, false,
					true),
			new BaseTower(ICE_BALLISTA, 33, 33.0f, 90.0f, 100.0f, 1.0f, 4000.0f, 5.0f, true, false, false, false, true),
			new BaseTower(FLAME_BALLISTA, 40, 34.0f, 70.0f, 100.0f, 1.0f, 4000.0f, 5.0f, true, false, false, false,
					true),
			new BaseTower(WATER_PILLAR, 1, 35.0f, 60.0f, 120.0f, 2.0f, 3000.0f, 3.0f, true, true, false, false, true),
			new BaseTower(LIGHTNING_PILLAR, 2, 36.0f, 30.0f, 120.0f, 2.0f, 3000.0f, 3.0f, true, true, false, false,
					true),
			new BaseTower(LIFE_PILLAR, 16, 37.0f, 30.0f, 120.0f, 2.0f, 3000.0f, 3.0f, true, true, false, false, true),
			new BaseTower(UNDEAD_PILLAR, 32, 38.0f, 30.0f, 120.0f, 2.0f, 3000.0f, 3.0f, true, true, false, false, true),
			new BaseTower(EARTH_PILLAR, 4, 39.0f, 25.0f, 120.0f, 2.0f, 3000.0f, 3.0f, true, true, false, false, true),
			new BaseTower(FIRE_PILLAR, 8, 40.0f, 10.0f, 120.0f, 2.0f, 3000.0f, 3.0f, true, true, false, false, true), };

	public static BaseTower createUpgrade(BaseTower tower) {
		BaseTower upgrade = new BaseTower(tower);
		upgrade.cost *= 2;
		upgrade.damage *= 2;
		upgrade.health *= 2;
		upgrade.defense *= 1.25;
		upgrade.level += 1;
		upgrade.calculatePower();
		return upgrade;
	}

	public static BaseTower[] getBaseTowers() {
		BaseTower[] newList = new BaseTower[BASE_TOWERS.length];

		for (int j = 0; j < BASE_TOWERS.length; j++) {
			newList[j] = new BaseTower(BASE_TOWERS[j]);

		}
		return newList;
	};

	public static BaseTower getBaseTower(BaseTower[] towers, String name) {
		for (BaseTower tower : towers) {
			if (tower.getName().equals(name))
				return tower;
		}
		return null;
	}

	// upgradeable stats
	private static final float HEALTH_UPGRADE_RATE = 2f;
	private static final float DEFENSE_UPGRADE_RATE = 1.25f;
	private static final float DAMAGE_UPGRADE_RATE = 2f;
	private static final float ATTACK_RATE_UPGRADE_RATE = .9f;
	private float damage;
	private float health;
	private float attackRate;
	private float defense;
	private int healthUpgradeCount = 1;
	private int defenseUpgradeCount = 1;
	private int damageUpgradeCount = 1;
	private int attackRateUpgradeCount = 1;
	private int totalUpgradeCount = 1;
	private int level = 1;

	// other stats
	private boolean unlocked;
	private String name;
	private int races;
	private float attackRange;
	private boolean attacksGround;
	private boolean attacksAir;
	private boolean attacksAllInRange;
	private boolean attacksInstantly;
	private float cost;
	private float power;

	public BaseTower() {
	}

	public BaseTower(BaseTower tower) {
		this(tower.name, tower.races, tower.cost, tower.damage, tower.health, tower.defense, tower.attackRate,
				tower.attackRange, tower.attacksGround, tower.attacksAir, tower.attacksAllInRange,
				tower.attacksInstantly, tower.unlocked);
		this.level = tower.level;
		this.totalUpgradeCount = tower.totalUpgradeCount;
		this.power = tower.power;
	}

	public BaseTower(String name, int races, float cost, float damage, float health, float defense, float attackRate,
			float attackRange, boolean attacksGround, boolean attacksAir, boolean attacksAllInRange,
			boolean attacksInstantly, boolean unlocked) {
		this.name = name;
		this.races = races;
		this.cost = cost;
		this.damage = damage;
		this.health = health;
		this.attackRate = attackRate;
		this.attackRange = attackRange;
		this.defense = defense;
		this.attacksGround = attacksGround;
		this.attacksAir = attacksAir;
		this.attacksAllInRange = attacksAllInRange;
		this.attacksInstantly = attacksInstantly;
		this.unlocked = unlocked;
		calculatePower();
	}

	private void calculatePower() {
		power = totalUpgradeCount * cost / 100f;
	}

	public float getPower() {
		return power;
	}

	public int getTotalUpgradeCount() {
		return totalUpgradeCount;
	}

	public int getLevel() {
		return level;
	}

	public boolean attacksAllInRange() {
		return attacksAllInRange;
	}

	public boolean attacksInstantly() {
		return attacksInstantly;
	}

	public int getRaces() {
		return races;
	}

	public boolean isUnlocked() {
		return true;// unlocked;
	}

	public void setUnlocked(boolean unlocked) {
		this.unlocked = unlocked;
	}

	public float getDamage() {
		return damage;
	}

	public float getHealth() {
		return health;
	}

	public float getAttackRate() {
		return attackRate;
	}

	public float getAttackRange() {
		return attackRange;
	}

	public float getDefense() {
		return defense;
	}

	public boolean attacksGround() {
		return attacksGround;
	}

	public boolean attacksAir() {
		return attacksAir;
	}

	public float getCost() {
		return cost;
	}

	public void setDamage(float damage) {
		this.damage = damage;
	}

	public void setAttackRate(float attackRate) {
		this.attackRate = attackRate;
	}

	public void setAttackRange(float attackRange) {
		this.attackRange = attackRange;
	}

	@Override
	public String toString() {
		return name.toUpperCase() + "," + races + "," + cost + "f," + damage + "f," + health + "f," + defense + "f,"
				+ attackRate + "f," + attackRange + "f," + attacksGround + "," + attacksAir + "," + attacksAllInRange
				+ "," + attacksInstantly + "," + unlocked + "," + power;
	}

	private static final int HEALTH_ID = 1;
	private static final int DEFENSE_ID = 2;
	private static final int DAMAGE_ID = 3;
	private static final int ATTACK_RATE_ID = 4;
	private static final String HEALTH = "stat_health";
	private static final String DEFENSE = "stat_defense";
	private static final String DAMAGE = "stat_damage";
	private static final String ATTACK_RATE = "stat_attack_rate";

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getUpgradeName(int id) {
		switch (id) {
		case HEALTH_ID:
			return HEALTH;
		case DEFENSE_ID:
			return DEFENSE;
		case DAMAGE_ID:
			return DAMAGE;
		case ATTACK_RATE_ID:
			return ATTACK_RATE;
		default:
			throw new RuntimeException("Unknown getUpgradeCost ID: " + id);
		}
	}

	@Override
	public int[] getUpgradeIDs() {
		return new int[] { HEALTH_ID, DEFENSE_ID, DAMAGE_ID, ATTACK_RATE_ID };
	}

	@Override
	public long getUpgradeCost(int id) {
		switch (id) {
		case HEALTH_ID:
			return (long) Math.pow(5, healthUpgradeCount);
		case DEFENSE_ID:
			return (long) Math.pow(5, defenseUpgradeCount);
		case DAMAGE_ID:
			return (long) Math.pow(5, damageUpgradeCount);
		case ATTACK_RATE_ID:
			return (long) Math.pow(5, attackRateUpgradeCount);
		default:
			throw new RuntimeException("Unknown getUpgradeCost ID: " + id);
		}
	}

	@Override
	public float getBaseValue(int id) {
		switch (id) {
		case HEALTH_ID:
			return health;
		case DEFENSE_ID:
			return defense;
		case DAMAGE_ID:
			return damage;
		case ATTACK_RATE_ID:
			return attackRate;
		default:
			throw new RuntimeException("Unknown getBaseValue ID: " + id);
		}
	}

	@Override
	public float getUpgradedValue(int id) {
		switch (id) {
		case HEALTH_ID:
			return health * HEALTH_UPGRADE_RATE;
		case DEFENSE_ID:
			return defense * DEFENSE_UPGRADE_RATE;
		case DAMAGE_ID:
			return damage * DAMAGE_UPGRADE_RATE;
		case ATTACK_RATE_ID:
			return attackRate * ATTACK_RATE_UPGRADE_RATE;
		default:
			throw new RuntimeException("Unknown getUpgradedValue ID: " + id);
		}
	}

	@Override
	public void upgrade(int id) {
		switch (id) {
		case HEALTH_ID:
			health *= HEALTH_UPGRADE_RATE;
			healthUpgradeCount++;
			break;
		case DEFENSE_ID:
			defense *= DEFENSE_UPGRADE_RATE;
			defenseUpgradeCount++;
			break;
		case DAMAGE_ID:
			damage *= DAMAGE_UPGRADE_RATE;
			damageUpgradeCount++;
			break;
		case ATTACK_RATE_ID:
			attackRate *= ATTACK_RATE_UPGRADE_RATE;
			attackRateUpgradeCount++;
			break;
		default:
			throw new RuntimeException("Unknown upgrade ID: " + id);
		}
		totalUpgradeCount++;
	}

	@Override
	public int getUpgradeCount(int id) {
		switch (id) {
		case HEALTH_ID:
			return healthUpgradeCount;
		case DEFENSE_ID:
			return defenseUpgradeCount;
		case DAMAGE_ID:
			return damageUpgradeCount;
		case ATTACK_RATE_ID:
			return attackRateUpgradeCount;
		default:
			throw new RuntimeException("Unknown getBaseValue ID: " + id);
		}
	}

	@Override
	public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
		name = (String) input.readObject();
		unlocked = input.readBoolean();
		races = input.readInt();
		damage = input.readFloat();
		health = input.readFloat();
		attackRate = input.readFloat();
		defense = input.readFloat();
		attackRange = input.readFloat();
		attacksGround = input.readBoolean();
		attacksAir = input.readBoolean();
		attacksAllInRange = input.readBoolean();
		attacksInstantly = input.readBoolean();
		cost = input.readFloat();
		power = input.readFloat();
		level = input.readInt();
		healthUpgradeCount = input.readInt();
		defenseUpgradeCount = input.readInt();
		damageUpgradeCount = input.readInt();
		attackRateUpgradeCount = input.readInt();
		totalUpgradeCount = input.readInt();
	}

	@Override
	public void writeExternal(ObjectOutput output) throws IOException {
		output.writeObject(name);
		output.writeBoolean(unlocked);
		output.writeInt(races);
		output.writeFloat(damage);
		output.writeFloat(health);
		output.writeFloat(attackRate);
		output.writeFloat(defense);
		output.writeFloat(attackRange);
		output.writeBoolean(attacksGround);
		output.writeBoolean(attacksAir);
		output.writeBoolean(attacksAllInRange);
		output.writeBoolean(attacksInstantly);
		output.writeFloat(cost);
		output.writeFloat(power);
		output.writeInt(level);
		output.writeInt(healthUpgradeCount);
		output.writeInt(defenseUpgradeCount);
		output.writeInt(damageUpgradeCount);
		output.writeInt(attackRateUpgradeCount);
		output.writeInt(totalUpgradeCount);
	}

}
