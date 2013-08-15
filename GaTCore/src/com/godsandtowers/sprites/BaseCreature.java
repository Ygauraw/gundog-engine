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
import java.util.Collection;

import com.gundogstudios.util.FastMath;

public class BaseCreature implements Upgradeable, Externalizable {

	// ICE
	public static final String FROZEN_SOLDIER = "frozen_soldier";
	public static final String FROZEN_SOLDIER_HORSEMAN = "frozen_soldier_horseman";
	public static final String POLAR_BEAR = "polar_bear";
	public static final String ICE_GOLEM = "ice_golem";
	public static final String DRAKE = "drake";

	// WIND
	public static final String HUMAN_ARCHER = "human_archer";
	public static final String HUMAN_ARCHER_HORSEMAN = "human_archer_horseman";
	public static final String PANDA_BEAR = "panda_bear";
	public static final String LIGHTNING_GOLEM = "lightning_golem";
	public static final String DRAGON = "dragon";

	// EARTH
	public static final String ELVISH_ARCHER = "elvish_archer";
	public static final String ELVISH_ARCHER_HORSEMAN = "elvish_archer_horseman";
	public static final String GRIZZLY_BEAR = "grizzly_bear";
	public static final String EARTH_GOLEM = "earth_golem";
	public static final String EAGLE = "eagle";

	// FIRE
	public static final String MUMMY = "mummy";
	public static final String MUMMY_HORSEMAN = "mummy_horseman";
	public static final String FIERY_BEAR = "fiery_bear";
	public static final String FIRE_GOLEM = "fire_golem";
	public static final String FALCON = "falcon";

	// LIFE
	public static final String ZEALOT = "zealot";
	public static final String ZEALOT_HORSEMAN = "zealot_horseman";
	public static final String SERAPHIM = "seraphim";
	public static final String LIFE_GOLEM = "life_golem";
	public static final String ANGEL = "angel";

	// DEATH
	public static final String ZOMBIE = "zombie";
	public static final String ZOMBIE_HORSEMAN = "zombie_horseman";
	public static final String SUCCUBUS = "succubus";
	public static final String UNDEAD_GOLEM = "undead_golem";
	public static final String FALLEN_ANGEL = "fallen_angel";

	// COMBO
	public static final String ELF_WIZARD = "elf_wizard";
	public static final String HUMAN_MAGE = "human_mage";
	public static final String HUMAN_NECROMANCER = "human_necromancer";
	public static final String TROLL = "troll";
	public static final String DWARF = "dwarf";
	public static final String KOBOLD = "kobold";
	public static final String HALFLING = "halfling";
	public static final String GNOME = "gnome";
	public static final String GHOUL = "ghoul";
	public static final String HUMAN_WITCH = "human_witch";
	public static final String FEMALE_NECROMANCER = "female_necromancer";
	public static final String FEMALE_ELF_MAGE = "female_elf_mage";

	private static final BaseCreature[] BASE_CREATURES = {
			new BaseCreature(ZOMBIE, 32, 5.0f, 2400.0f, 1.0f, 1.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(MUMMY, 8, 6.0f, 2400.0f, 1.0f, 1.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(FROZEN_SOLDIER, 1, 7.0f, 2200.0f, 1.5f, 1.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(ZEALOT, 16, 8.0f, 2200.0f, 1.5f, 1.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(HUMAN_ARCHER, 2, 9.0f, 2000.0f, 1.0f, 1.0f, 2000.0f, 3f, false, true),
			new BaseCreature(ELVISH_ARCHER, 4, 10.0f, 2000.0f, 1.0f, 1.0f, 2000.0f, 3f, false, true),
			new BaseCreature(GHOUL, 34, 12.0f, 2200.0f, 1.0f, 2.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(GNOME, 24, 14.0f, 2200.0f, 1.0f, 2.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(HALFLING, 3, 16.0f, 2000.0f, 1.0f, 2.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(KOBOLD, 5, 18.0f, 2000.0f, 1.0f, 2.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(DWARF, 20, 20.0f, 2400.0f, 1.5f, 2.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(TROLL, 36, 22.0f, 2400.0f, 1.5f, 2.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(ZOMBIE_HORSEMAN, 32, 25.0f, 1500.0f, 1.0f, 4.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(MUMMY_HORSEMAN, 8, 28.0f, 1500.0f, 1.0f, 4.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(FROZEN_SOLDIER_HORSEMAN, 1, 31.0f, 1400.0f, 1.5f, 4.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(ZEALOT_HORSEMAN, 16, 34.0f, 1400.0f, 1.5f, 4.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(HUMAN_ARCHER_HORSEMAN, 2, 37.0f, 1300.0f, 1.0f, 4.0f, 2000.0f, 3f, false, true),
			new BaseCreature(ELVISH_ARCHER_HORSEMAN, 4, 40.0f, 1300.0f, 1.0f, 4.0f, 2000.0f, 3f, false, true),
			new BaseCreature(HUMAN_WITCH, 17, 44.0f, 2400.0f, 1.0f, 7.0f, 2000.0f, 3f, false, true),
			new BaseCreature(HUMAN_MAGE, 10, 48.0f, 2400.0f, 1.0f, 7.0f, 2000.0f, 3f, false, true),
			new BaseCreature(HUMAN_NECROMANCER, 40, 52.0f, 2300.0f, 1.0f, 7.0f, 2000.0f, 3f, false, true),
			new BaseCreature(FEMALE_NECROMANCER, 33, 56.0f, 2300.0f, 1.0f, 7.0f, 2000.0f, 3f, false, true),
			new BaseCreature(FEMALE_ELF_MAGE, 12, 60.0f, 2200.0f, 1.0f, 7.0f, 2000.0f, 3f, false, true),
			new BaseCreature(ELF_WIZARD, 18, 64.0f, 2200.0f, 1.0f, 7.0f, 2000.0f, 3f, false, true),
			new BaseCreature(PANDA_BEAR, 2, 69.0f, 1800.0f, 2.0f, 11.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(GRIZZLY_BEAR, 4, 74.0f, 1800.0f, 2.0f, 11.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(POLAR_BEAR, 1, 79.0f, 1800.0f, 2.0f, 11.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(FIERY_BEAR, 8, 84.0f, 1800.0f, 2.0f, 11.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(SERAPHIM, 16, 89.0f, 2000.0f, 2.0f, 11.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(SUCCUBUS, 32, 94.0f, 2000.0f, 2.0f, 11.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(UNDEAD_GOLEM, 32, 100.0f, 2600.0f, 4.0f, 16.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(LIGHTNING_GOLEM, 2, 106.0f, 2600.0f, 4.0f, 16.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(LIFE_GOLEM, 16, 112.0f, 2600.0f, 4.0f, 16.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(FIRE_GOLEM, 8, 118.0f, 2600.0f, 4.0f, 16.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(ICE_GOLEM, 1, 124.0f, 2600.0f, 4.0f, 16.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(EARTH_GOLEM, 4, 130.0f, 2600.0f, 4.0f, 16.0f, 2000.0f, 1.25f, false, true),
			new BaseCreature(FALCON, 8, 137.0f, 2000.0f, 1.0f, 21.0f, 2000.0f, 1.25f, true, true),
			new BaseCreature(EAGLE, 4, 144.0f, 2000.0f, 1.0f, 21.0f, 2000.0f, 1.25f, true, true),
			new BaseCreature(DRAKE, 1, 151.0f, 2400.0f, 2.0f, 21.0f, 2000.0f, 3f, true, true),
			new BaseCreature(DRAGON, 2, 158.0f, 2400.0f, 2.0f, 21.0f, 2000.0f, 3f, true, true),
			new BaseCreature(FALLEN_ANGEL, 32, 165.0f, 2200.0f, 3.0f, 21.0f, 2000.0f, 1.25f, true, true),
			new BaseCreature(ANGEL, 16, 172.0f, 2200.0f, 3.0f, 21.0f, 2000.0f, 1.25f, true, true), };

	public static Collection<BaseCreature> getUpgradedCreatures(Collection<BaseCreature> creatures) {
		ArrayList<BaseCreature> upgrades = new ArrayList<BaseCreature>(creatures.size());
		for (BaseCreature creature : creatures) {
			BaseCreature upgrade = new BaseCreature(creature);
			upgrade.cost *= 2;
			upgrade.income = creature.getIncome() * 1.9f;
			upgrade.damage *= 2f;
			upgrade.health *= 2;
			upgrade.defense *= 1.25f;
			upgrade.level += 1;
			upgrade.calculatePower();
			upgrades.add(upgrade);
		}
		return upgrades;
	};

	public static BaseCreature[] getBaseCreatures() {
		BaseCreature[] newList = new BaseCreature[BASE_CREATURES.length];

		for (int j = 0; j < BASE_CREATURES.length; j++) {
			newList[j] = new BaseCreature(BASE_CREATURES[j]);
		}

		return newList;
	};

	public static BaseCreature getBaseCreature(BaseCreature[] creatures, String name) {
		for (BaseCreature creature : creatures) {
			if (creature.getName().equals(name))
				return creature;
		}
		return null;
	}

	// upgradeable stats
	private static final float HEALTH_UPGRADE_RATE = 2f;
	private static final float SPEED_UPGRADE_RATE = .9f;
	private static final float DEFENSE_UPGRADE_RATE = 1.25f;
	private static final float DAMAGE_UPGRADE_RATE = 1.5f;
	private static final float ATTACK_RATE_UPGRADE_RATE = .9f;
	private float health;
	private float speed;
	private float defense;
	private float damage;
	private float attackRate;
	private int healthUpgradeCount = 1;
	private int speedUpgradeCount = 1;
	private int defenseUpgradeCount = 1;
	private int damageUpgradeCount = 1;
	private int attackRateUpgradeCount = 1;
	private int totalUpgradeCount = 1;
	private int level = 1;

	// other stats
	private boolean unlocked;
	private String name;
	private int races;
	private float power;
	private float cost;
	private float income;
	private float attackRange;
	private boolean air;

	public BaseCreature() {
	}

	public BaseCreature(BaseCreature creature) {
		this(creature.name, creature.races, creature.cost, creature.speed, creature.defense, creature.damage,
				creature.attackRate, creature.attackRange, creature.air, creature.unlocked);
		this.level = creature.level;
		this.totalUpgradeCount = creature.totalUpgradeCount;
		this.power = creature.power;
		this.health = creature.health;
	}

	private BaseCreature(String name, int races, float cost, float speed, float defense, float damage,
			float attackRate, float attackRange, boolean air, boolean unlocked) {
		this.name = name;
		this.races = races;
		this.cost = FastMath.ceil(cost);
		this.income = (cost / 5) / ((air) ? 2 : 1);
		this.health = cost / ((air) ? 2 : 1);
		this.speed = speed;
		this.defense = defense;
		this.damage = damage;
		this.attackRate = attackRate;
		this.attackRange = attackRange;
		this.air = air;
		this.unlocked = unlocked;
		calculatePower();
	}

	@Override
	public String toString() {
		return "BaseCreature [health=" + health + ", speed=" + speed + ", defense=" + defense + ", damage=" + damage
				+ ", attackRate=" + attackRate + ", healthUpgradeCount=" + healthUpgradeCount + ", speedUpgradeCount="
				+ speedUpgradeCount + ", defenseUpgradeCount=" + defenseUpgradeCount + ", damageUpgradeCount="
				+ damageUpgradeCount + ", attackRateUpgradeCount=" + attackRateUpgradeCount + ", totalUpgradeCount="
				+ totalUpgradeCount + ", level=" + level + ", unlocked=" + unlocked + ", name=" + name + ", races="
				+ races + ", cost=" + cost + ", income=" + income + ", attackRange=" + attackRange + ", air=" + air
				+ "]";
	}

	private void calculatePower() {
		power = (level * totalUpgradeCount * cost) / 100f;
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

	public boolean isUnlocked() {
		return true;// unlocked;
	}

	public void setUnlocked(boolean unlocked) {
		this.unlocked = unlocked;
	}

	public int getRaces() {
		return races;
	}

	public float getHealth() {
		return health;
	}

	public float getSpeed() {
		return speed;
	}

	public float getDefense() {
		return defense;
	}

	public float getDamage() {
		return damage;
	}

	public float getAttackRate() {
		return attackRate;
	}

	public float getAttackRange() {
		return attackRange;
	}

	public float getCost() {
		return cost;
	}

	public float getIncome() {
		return income;
	}

	public boolean isAir() {
		return air;
	}

	private static final int HEALTH_ID = 0;
	private static final int DEFENSE_ID = 1;
	private static final int DAMAGE_ID = 2;
	private static final int ATTACK_RATE_ID = 3;
	private static final int SPEED_ID = 4;
	private static final String HEALTH = "stat_health";
	private static final String DEFENSE = "stat_defense";
	private static final String DAMAGE = "stat_damage";
	private static final String ATTACK_RATE = "stat_attack_rate";
	private static final String SPEED = "stat_speed";

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getUpgradeName(int id) {
		switch (id) {
		case HEALTH_ID:
			return HEALTH;
		case SPEED_ID:
			return SPEED;
		case DEFENSE_ID:
			return DEFENSE;
		case DAMAGE_ID:
			return DAMAGE;
		case ATTACK_RATE_ID:
			return ATTACK_RATE;
		default:
			throw new RuntimeException("Unknown upgrade ID: " + id);
		}
	}

	@Override
	public int[] getUpgradeIDs() {
		return new int[] { HEALTH_ID, SPEED_ID, DEFENSE_ID, DAMAGE_ID, ATTACK_RATE_ID };
	}

	@Override
	public long getUpgradeCost(int id) {
		switch (id) {
		case HEALTH_ID:
			return (long) Math.pow(5, healthUpgradeCount);
		case SPEED_ID:
			return (long) Math.pow(5, speedUpgradeCount);
		case DEFENSE_ID:
			return (long) Math.pow(5, defenseUpgradeCount);
		case DAMAGE_ID:
			return (long) Math.pow(5, damageUpgradeCount);
		case ATTACK_RATE_ID:
			if (attackRate <= ATTACK_RATE_UPGRADE_RATE)
				return 0l;
			return (long) Math.pow(5, attackRateUpgradeCount);
		default:
			throw new RuntimeException("Unknown upgrade ID: " + id);
		}
	}

	@Override
	public float getBaseValue(int id) {
		switch (id) {
		case HEALTH_ID:
			return health;
		case SPEED_ID:
			return speed;
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
		case SPEED_ID:
			return speed * SPEED_UPGRADE_RATE;
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
		case SPEED_ID:
			speed *= SPEED_UPGRADE_RATE;
			speedUpgradeCount++;
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
		case SPEED_ID:
			return speedUpgradeCount;
		case DEFENSE_ID:
			return defenseUpgradeCount;
		case DAMAGE_ID:
			return damageUpgradeCount;
		case ATTACK_RATE_ID:
			return attackRateUpgradeCount;
		default:
			throw new RuntimeException("Unknown getUpgradeCount ID: " + id);
		}
	}

	@Override
	public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
		name = (String) input.readObject();
		races = input.readInt();
		cost = input.readFloat();
		power = input.readFloat();
		income = input.readFloat();
		air = input.readBoolean();
		unlocked = input.readBoolean();
		health = input.readFloat();
		speed = input.readFloat();
		defense = input.readFloat();
		damage = input.readFloat();
		attackRate = input.readFloat();
		attackRange = input.readFloat();
		healthUpgradeCount = input.readInt();
		speedUpgradeCount = input.readInt();
		defenseUpgradeCount = input.readInt();
		damageUpgradeCount = input.readInt();
		attackRateUpgradeCount = input.readInt();
		level = input.readInt();
		totalUpgradeCount = input.readInt();
	}

	@Override
	public void writeExternal(ObjectOutput output) throws IOException {
		output.writeObject(name);
		output.writeInt(races);
		output.writeFloat(cost);
		output.writeFloat(power);
		output.writeFloat(income);
		output.writeBoolean(air);
		output.writeBoolean(unlocked);
		output.writeFloat(health);
		output.writeFloat(speed);
		output.writeFloat(defense);
		output.writeFloat(damage);
		output.writeFloat(attackRate);
		output.writeFloat(attackRange);
		output.writeInt(healthUpgradeCount);
		output.writeInt(speedUpgradeCount);
		output.writeInt(defenseUpgradeCount);
		output.writeInt(damageUpgradeCount);
		output.writeInt(attackRateUpgradeCount);
		output.writeInt(level);
		output.writeInt(totalUpgradeCount);
	}

}
