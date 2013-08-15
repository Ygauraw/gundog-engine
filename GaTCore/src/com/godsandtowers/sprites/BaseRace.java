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

public class BaseRace implements Upgradeable, Externalizable {

	public static BaseRace getBaseRace() {
		return new BaseRace(2000f, .55f, .3f, // slowDuration, slowFactor, creatureEvadePercentage,
				500f, 1.1f, 1.1f, // stunDuration, towerAttackRateModifier, creatureSpeedModifier,
				1.1f, 1.1f, 1.1f, // towerDamageModifier, towerDefenseModifier, creatureDefenseModifier,
				1f, .05f, 1.25f, // splashRadius,splashStrengthFade, creatureDamageModifier,
				1000, .05f, .05f,// drainDuration, drainPercentage, healPercentage,
				.1f, 1.25f, .1f// killPercentage, creatureHealthModifier, resurrectPercentage
		);
	}

	// ICE MODS
	private static final float SLOW_DURATION_UPGRADE_RATE = 1000;
	private static final float SLOW_FACTOR_UPGRADE_RATE = .05f;
	private static final float CREATURE_EVADE_PERCENTAGE_UPGRADE_RATE = .05f;
	private float slowDuration;
	private float slowFactor;
	private float creatureEvadePercentage;
	private int slowDurationUpgradeCount = 1;
	private int slowFactorUpgradeCount = 1;
	private int creatureEvadePercentageUpgradeCount = 1;

	// WIND MODS
	private static final float STUN_DURATION_UPGRADE_RATE = 250f;
	private static final float TOWER_ATTACK_RATE_MODIFIER_UPGRADE_RATE = .025f;
	private static final float CREATURE_SPEED_MODIFIER_UPGRADE_RATE = .025f;
	private float stunDuration;
	private float towerAttackRateModifier;
	private float creatureSpeedModifier;
	private int stunDurationUpgradeCount = 1;
	private int towerAttackRateModifierUpgradeCount = 1;
	private int creatureSpeedModifierUpgradeCount = 1;

	// EARTH MODS
	private static final float TOWER_DAMAGE_MODIFIER_UPGRADE_RATE = .025f;
	private static final float TOWER_DEFENSE_MODIFIER_UPGRADE_RATE = .025f;
	private static final float CREATURE_DEFENSE_MODIFIER_UPGRADE_RATE = .025f;
	private float towerDamageModifier;
	private float towerDefenseModifier;
	private float creatureDefenseModifier;
	private int towerDamageModifierUpgradeCount = 1;
	private int towerDefenseModifierUpgradeCount = 1;
	private int creatureDefenseModifierUpgradeCount = 1;

	// FIRE MODS
	private static final float SPLASH_RADIUS_UPGRADE_RATE = .05f;
	private static final float SPLASH_STRENGTH_FADE_UPGRADE_RATE = .025f;
	private static final float CREATURE_DAMAGE_MODIFIER_UPGRADE_RATE = .05f;
	private float splashRadius;
	private float splashStrengthFade;
	private float creatureDamageModifier;
	private int splashRadiusUpgradeCount = 1;
	private int splashStrengthFadeUpgradeCount = 1;
	private int creatureDamageModifierUpgradeCount = 1;

	// LIFE MODS
	private static final float DRAIN_DURATION_UPGRADE_RATE = 100;
	private static final float DRAIN_PERCENTAGE_UPGRADE_RATE = .025f;
	private static final float HEAL_PERCENTAGE_UPGRADE_RATE = .025f;
	private float drainDuration;
	private float drainPercentage;
	private float healPercentage;
	private int drainDurationUpgradeCount = 1;
	private int drainPercentageUpgradeCount = 1;
	private int healPercentageUpgradeCount = 1;

	// DEATH MODS
	private static final float KILL_PERCENTAGE_UPGRADE_RATE = .025f;
	private static final float CREATURE_HEALTH_MODIFIER_UPGRADE_RATE = .05f;
	private static final float RESURRECT_PERCENTAGE_UPGRADE_RATE = .025f;
	private float killPercentage;
	private float creatureHealthModifier;
	private float resurrectPercentage;
	private int killPercentageUpgradeCount = 1;
	private int creatureHealthModifierUpgradeCount = 1;
	private int resurrectPercentageUpgradeCount = 1;

	public BaseRace() {
	}

	public BaseRace(BaseRace baseRace) {
		this.slowDuration = baseRace.slowDuration;
		this.slowFactor = baseRace.slowFactor;
		this.creatureEvadePercentage = baseRace.creatureEvadePercentage;
		this.stunDuration = baseRace.stunDuration;
		this.towerAttackRateModifier = baseRace.towerAttackRateModifier;
		this.creatureSpeedModifier = baseRace.creatureSpeedModifier;
		this.towerDamageModifier = baseRace.towerDamageModifier;
		this.towerDefenseModifier = baseRace.towerDefenseModifier;
		this.creatureDefenseModifier = baseRace.creatureDefenseModifier;
		this.splashRadius = baseRace.splashRadius;
		this.splashStrengthFade = baseRace.splashStrengthFade;
		this.creatureDamageModifier = baseRace.creatureDamageModifier;
		this.drainDuration = baseRace.drainDuration;
		this.drainPercentage = baseRace.drainPercentage;
		this.healPercentage = baseRace.healPercentage;
		this.killPercentage = baseRace.killPercentage;
		this.creatureHealthModifier = baseRace.creatureHealthModifier;
		this.resurrectPercentage = baseRace.resurrectPercentage;
	}

	public BaseRace(float slowDuration, float slowFactor, float creatureEvadePercentage, float stunDuration,
			float towerAttackRateModifier, float creatureSpeedModifier, float towerDamageModifier,
			float towerDefenseModifier, float creatureDefenseModifier, float splashRadius, float splashStrengthFade,
			float creatureDamageModifier, float drainDuration, float drainPercentage, float healPercentage,
			float killPercentage, float creatureHealthModifier, float resurrectPercentage) {
		this.slowDuration = slowDuration;
		this.slowFactor = slowFactor;
		this.creatureEvadePercentage = creatureEvadePercentage;
		this.stunDuration = stunDuration;
		this.towerAttackRateModifier = towerAttackRateModifier;
		this.creatureSpeedModifier = creatureSpeedModifier;
		this.towerDamageModifier = towerDamageModifier;
		this.towerDefenseModifier = towerDefenseModifier;
		this.creatureDefenseModifier = creatureDefenseModifier;
		this.splashRadius = splashRadius;
		this.splashStrengthFade = splashStrengthFade;
		this.creatureDamageModifier = creatureDamageModifier;
		this.drainDuration = drainDuration;
		this.drainPercentage = drainPercentage;
		this.healPercentage = healPercentage;
		this.killPercentage = killPercentage;
		this.creatureHealthModifier = creatureHealthModifier;
		this.resurrectPercentage = resurrectPercentage;
	}

	@Override
	public String toString() {
		return slowDuration + "f," + slowFactor + "f," + creatureEvadePercentage + "f," + stunDuration + "f,"
				+ towerAttackRateModifier + "f," + creatureSpeedModifier + "f," + towerDamageModifier + "f,"
				+ towerDefenseModifier + "f," + creatureDefenseModifier + "f," + splashRadius + "f,"
				+ splashStrengthFade + "f," + creatureDamageModifier + "f," + drainDuration + "f," + drainPercentage
				+ "f," + healPercentage + "f," + killPercentage + "f," + creatureHealthModifier + "f,"
				+ resurrectPercentage + "f";
	}

	public float getSplashRadius() {
		return splashRadius;
	}

	public float getSplashStrengthFade() {
		return splashStrengthFade;
	}

	public float getCreatureDamageModifier() {
		return creatureDamageModifier;
	}

	public float getSlowDuration() {
		return slowDuration;
	}

	public float getSlowFactor() {
		return slowFactor;
	}

	public float getCreatureEvadePercentage() {
		return creatureEvadePercentage;
	}

	public float getStunDuration() {
		return stunDuration;
	}

	public float getTowerAttackRateModifier() {
		return towerAttackRateModifier;
	}

	public float getCreatureSpeedModifier() {
		return creatureSpeedModifier;
	}

	public float getTowerDamageModifier() {
		return towerDamageModifier;
	}

	public float getTowerDefenseModifier() {
		return towerDefenseModifier;
	}

	public float getCreatureDefenseModifier() {
		return creatureDefenseModifier;
	}

	public float getDrainDuration() {
		return drainDuration;
	}

	public float getDrainPercentage() {
		return drainPercentage;
	}

	public float getHealPercentage() {
		return healPercentage;
	}

	public float getKillPercentage() {
		return killPercentage;
	}

	public float getCreatureHealthModifier() {
		return creatureHealthModifier;
	}

	public float getResurrectPercentage() {
		return resurrectPercentage;
	}

	private static final int SPLASH_RADIUS_ID = 1;
	private static final int SPLASH_STRENGTH_FADE_ID = 2;
	private static final int CREATURE_DAMAGE_MODIFIER_ID = 3;
	private static final int SLOW_DURATION_ID = 4;
	private static final int SLOW_FACTOR_ID = 5;
	private static final int CREATURE_EVADE_PERCENTAGE_ID = 6;
	private static final int STUN_DURATION_ID = 7;
	private static final int TOWER_ATTACK_RATE_MODIFIER_ID = 8;
	private static final int CREATURE_SPEED_MODIFIER_ID = 9;
	private static final int TOWER_DAMAGE_MODIFIER_ID = 10;
	private static final int TOWER_DEFENSE_MODIFIER_ID = 11;
	private static final int CREATURE_DEFENSE_MODIFIER_ID = 12;
	private static final int DRAIN_DURATION_ID = 13;
	private static final int DRAIN_PERCENTAGE_ID = 14;
	private static final int HEAL_PERCENTAGE_ID = 15;
	private static final int KILL_PERCENTAGE_ID = 16;
	private static final int CREATURE_HEALTH_MODIFIER_ID = 17;
	private static final int RESURRECT_PERCENTAGE_ID = 18;

	private static final String SPLASH_RADIUS = "stat_splash_radius";
	private static final String SPLASH_STRENGTH_FADE = "stat_splash_strength_fade";
	private static final String CREATURE_DAMAGE_MODIFIER = "stat_creature_damage_modifier";
	private static final String SLOW_DURATION = "stat_slow_duration";
	private static final String SLOW_FACTOR = "stat_slow_factor";
	private static final String CREATURE_EVADE_PERCENTAGE = "stat_creature_evade_percentage";
	private static final String STUN_DURATION = "stat_stun_duration";
	private static final String TOWER_ATTACK_RATE_MODIFIER = "stat_tower_attack_rate_modifier";
	private static final String CREATURE_SPEED_MODIFIER = "stat_creature_speed_modifier";
	private static final String TOWER_DAMAGE_MODIFIER = "stat_tower_damage_modifier";
	private static final String TOWER_DEFENSE_MODIFIER = "stat_tower_defense_modifier";
	private static final String CREATURE_DEFENSE_MODIFIER = "stat_creature_defense_modifier";
	private static final String DRAIN_DURATION = "stat_drain_duration";
	private static final String DRAIN_PERCENTAGE = "stat_drain_percentage";
	private static final String HEAL_PERCENTAGE = "stat_heal_percentage";
	private static final String KILL_PERCENTAGE = "stat_kill_percentage";
	private static final String CREATURE_HEALTH_MODIFIER = "stat_creature_health_modifier";
	private static final String RESURRECT_PERCENTAGE = "stat_resurrect_percentage";

	public int[] getUpgradeIDs(int race) {
		switch (race) {
		case Races.ICE:
			return new int[] { SLOW_DURATION_ID, SLOW_FACTOR_ID, CREATURE_EVADE_PERCENTAGE_ID };
		case Races.WIND:
			return new int[] { STUN_DURATION_ID, TOWER_ATTACK_RATE_MODIFIER_ID, CREATURE_SPEED_MODIFIER_ID };
		case Races.EARTH:
			return new int[] { TOWER_DAMAGE_MODIFIER_ID, TOWER_DEFENSE_MODIFIER_ID, CREATURE_DEFENSE_MODIFIER_ID };
		case Races.FIRE:
			return new int[] { SPLASH_RADIUS_ID, SPLASH_STRENGTH_FADE_ID, CREATURE_DAMAGE_MODIFIER_ID };
		case Races.DEATH:
			return new int[] { KILL_PERCENTAGE_ID, CREATURE_HEALTH_MODIFIER_ID, RESURRECT_PERCENTAGE_ID };
		case Races.LIFE:
			return new int[] { DRAIN_DURATION_ID, DRAIN_PERCENTAGE_ID, HEAL_PERCENTAGE_ID };
		default:
			throw new RuntimeException("Unknown race in Base Race get Upgrade IDs: " + race);
		}
	}

	@Override
	public String getName() {
		return "BaseRace";
	}

	@Override
	public String getUpgradeName(int id) {

		switch (id) {
		case SPLASH_RADIUS_ID:
			return SPLASH_RADIUS;
		case SPLASH_STRENGTH_FADE_ID:
			return SPLASH_STRENGTH_FADE;
		case CREATURE_DAMAGE_MODIFIER_ID:
			return CREATURE_DAMAGE_MODIFIER;
		case SLOW_DURATION_ID:
			return SLOW_DURATION;
		case SLOW_FACTOR_ID:
			return SLOW_FACTOR;
		case CREATURE_EVADE_PERCENTAGE_ID:
			return CREATURE_EVADE_PERCENTAGE;
		case STUN_DURATION_ID:
			return STUN_DURATION;
		case TOWER_ATTACK_RATE_MODIFIER_ID:
			return TOWER_ATTACK_RATE_MODIFIER;
		case CREATURE_SPEED_MODIFIER_ID:
			return CREATURE_SPEED_MODIFIER;
		case TOWER_DAMAGE_MODIFIER_ID:
			return TOWER_DAMAGE_MODIFIER;
		case TOWER_DEFENSE_MODIFIER_ID:
			return TOWER_DEFENSE_MODIFIER;
		case CREATURE_DEFENSE_MODIFIER_ID:
			return CREATURE_DEFENSE_MODIFIER;
		case DRAIN_PERCENTAGE_ID:
			return DRAIN_PERCENTAGE;
		case DRAIN_DURATION_ID:
			return DRAIN_DURATION;
		case HEAL_PERCENTAGE_ID:
			return HEAL_PERCENTAGE;
		case KILL_PERCENTAGE_ID:
			return KILL_PERCENTAGE;
		case CREATURE_HEALTH_MODIFIER_ID:
			return CREATURE_HEALTH_MODIFIER;
		case RESURRECT_PERCENTAGE_ID:
			return RESURRECT_PERCENTAGE;
		default:
			throw new RuntimeException("Unknown getUpgradeCost ID: " + id);
		}
	}

	@Override
	public int[] getUpgradeIDs() {
		return new int[] {
				// FIRE
				SPLASH_RADIUS_ID, SPLASH_STRENGTH_FADE_ID, CREATURE_DAMAGE_MODIFIER_ID,
				// ICE
				SLOW_DURATION_ID, SLOW_FACTOR_ID, CREATURE_EVADE_PERCENTAGE_ID,
				// WIND
				STUN_DURATION_ID, TOWER_ATTACK_RATE_MODIFIER_ID, CREATURE_SPEED_MODIFIER_ID,
				// EARTH
				TOWER_DAMAGE_MODIFIER_ID, TOWER_DEFENSE_MODIFIER_ID, CREATURE_DEFENSE_MODIFIER_ID,
				// LIFE
				DRAIN_DURATION_ID, DRAIN_PERCENTAGE_ID, HEAL_PERCENTAGE_ID,
				// DEATH
				KILL_PERCENTAGE_ID, CREATURE_HEALTH_MODIFIER_ID, RESURRECT_PERCENTAGE_ID };
	}

	@Override
	public long getUpgradeCost(int id) {
		switch (id) {
		case SPLASH_RADIUS_ID:
			return Math.round(Math.pow(10, splashRadiusUpgradeCount));
		case SPLASH_STRENGTH_FADE_ID:
			return Math.round(Math.pow(10, splashStrengthFadeUpgradeCount));
		case CREATURE_DAMAGE_MODIFIER_ID:
			return Math.round(Math.pow(10, creatureDamageModifierUpgradeCount));
		case SLOW_DURATION_ID:
			return Math.round(Math.pow(10, slowDurationUpgradeCount));
		case SLOW_FACTOR_ID:
			return Math.round(Math.pow(10, slowFactorUpgradeCount));
		case CREATURE_EVADE_PERCENTAGE_ID:
			return Math.round(Math.pow(10, creatureEvadePercentageUpgradeCount));
		case STUN_DURATION_ID:
			return Math.round(Math.pow(10, stunDurationUpgradeCount));
		case TOWER_ATTACK_RATE_MODIFIER_ID:
			return Math.round(Math.pow(10, towerAttackRateModifierUpgradeCount));
		case CREATURE_SPEED_MODIFIER_ID:
			return Math.round(Math.pow(10, creatureSpeedModifierUpgradeCount));
		case TOWER_DAMAGE_MODIFIER_ID:
			return Math.round(Math.pow(10, towerDamageModifierUpgradeCount));
		case TOWER_DEFENSE_MODIFIER_ID:
			return Math.round(Math.pow(10, towerDefenseModifierUpgradeCount));
		case CREATURE_DEFENSE_MODIFIER_ID:
			return Math.round(Math.pow(10, creatureDefenseModifierUpgradeCount));
		case DRAIN_PERCENTAGE_ID:
			return Math.round(Math.pow(10, drainPercentageUpgradeCount));
		case DRAIN_DURATION_ID:
			return Math.round(Math.pow(10, drainDurationUpgradeCount));
		case HEAL_PERCENTAGE_ID:
			return Math.round(Math.pow(10, healPercentageUpgradeCount));
		case KILL_PERCENTAGE_ID:
			return Math.round(Math.pow(10, killPercentageUpgradeCount));
		case CREATURE_HEALTH_MODIFIER_ID:
			return Math.round(Math.pow(10, creatureHealthModifierUpgradeCount));
		case RESURRECT_PERCENTAGE_ID:
			return Math.round(Math.pow(10, resurrectPercentageUpgradeCount));
		default:
			throw new RuntimeException("Unknown getUpgradeCost ID: " + id);
		}
	}

	@Override
	public float getBaseValue(int id) {

		switch (id) {
		case SPLASH_RADIUS_ID:
			return splashRadius;
		case SPLASH_STRENGTH_FADE_ID:
			return splashStrengthFade;
		case CREATURE_DAMAGE_MODIFIER_ID:
			return creatureDamageModifier;
		case SLOW_DURATION_ID:
			return slowDuration;
		case SLOW_FACTOR_ID:
			return slowFactor;
		case CREATURE_EVADE_PERCENTAGE_ID:
			return creatureEvadePercentage;
		case STUN_DURATION_ID:
			return stunDuration;
		case TOWER_ATTACK_RATE_MODIFIER_ID:
			return towerAttackRateModifier;
		case CREATURE_SPEED_MODIFIER_ID:
			return creatureSpeedModifier;
		case TOWER_DAMAGE_MODIFIER_ID:
			return towerDamageModifier;
		case TOWER_DEFENSE_MODIFIER_ID:
			return towerDefenseModifier;
		case CREATURE_DEFENSE_MODIFIER_ID:
			return creatureDefenseModifier;
		case DRAIN_PERCENTAGE_ID:
			return drainPercentage;
		case DRAIN_DURATION_ID:
			return drainDuration;
		case HEAL_PERCENTAGE_ID:
			return healPercentage;
		case KILL_PERCENTAGE_ID:
			return killPercentage;
		case CREATURE_HEALTH_MODIFIER_ID:
			return creatureHealthModifier;
		case RESURRECT_PERCENTAGE_ID:
			return resurrectPercentage;
		default:
			throw new RuntimeException("Unknown getBaseValue ID: " + id);
		}
	}

	@Override
	public float getUpgradedValue(int id) {

		switch (id) {
		case SPLASH_RADIUS_ID:
			return splashRadius + SPLASH_RADIUS_UPGRADE_RATE;
		case SPLASH_STRENGTH_FADE_ID:
			return splashStrengthFade + SPLASH_STRENGTH_FADE_UPGRADE_RATE;
		case CREATURE_DAMAGE_MODIFIER_ID:
			return creatureDamageModifier + CREATURE_DAMAGE_MODIFIER_UPGRADE_RATE;
		case SLOW_DURATION_ID:
			return slowDuration + SLOW_DURATION_UPGRADE_RATE;
		case SLOW_FACTOR_ID:
			return slowFactor - SLOW_FACTOR_UPGRADE_RATE;
		case CREATURE_EVADE_PERCENTAGE_ID:
			return creatureEvadePercentage + CREATURE_EVADE_PERCENTAGE_UPGRADE_RATE;
		case STUN_DURATION_ID:
			return stunDuration + STUN_DURATION_UPGRADE_RATE;
		case TOWER_ATTACK_RATE_MODIFIER_ID:
			return towerAttackRateModifier + TOWER_ATTACK_RATE_MODIFIER_UPGRADE_RATE;
		case CREATURE_SPEED_MODIFIER_ID:
			return creatureSpeedModifier + CREATURE_SPEED_MODIFIER_UPGRADE_RATE;
		case TOWER_DAMAGE_MODIFIER_ID:
			return towerDamageModifier + TOWER_DAMAGE_MODIFIER_UPGRADE_RATE;
		case TOWER_DEFENSE_MODIFIER_ID:
			return towerDefenseModifier + TOWER_DEFENSE_MODIFIER_UPGRADE_RATE;
		case CREATURE_DEFENSE_MODIFIER_ID:
			return creatureDefenseModifier + CREATURE_DEFENSE_MODIFIER_UPGRADE_RATE;
		case DRAIN_PERCENTAGE_ID:
			return drainPercentage + DRAIN_PERCENTAGE_UPGRADE_RATE;
		case DRAIN_DURATION_ID:
			return drainDuration + DRAIN_DURATION_UPGRADE_RATE;
		case HEAL_PERCENTAGE_ID:
			return healPercentage + HEAL_PERCENTAGE_UPGRADE_RATE;
		case KILL_PERCENTAGE_ID:
			return killPercentage + KILL_PERCENTAGE_UPGRADE_RATE;
		case CREATURE_HEALTH_MODIFIER_ID:
			return creatureHealthModifier + CREATURE_HEALTH_MODIFIER_UPGRADE_RATE;
		case RESURRECT_PERCENTAGE_ID:
			return resurrectPercentage + RESURRECT_PERCENTAGE_UPGRADE_RATE;
		default:
			throw new RuntimeException("Unknown getUpgradedValue ID: " + id);
		}
	}

	@Override
	public void upgrade(int id) {
		switch (id) {
		case SPLASH_RADIUS_ID:
			splashRadius += SPLASH_RADIUS_UPGRADE_RATE;
			splashRadiusUpgradeCount++;
			break;
		case SPLASH_STRENGTH_FADE_ID:
			splashStrengthFade += SPLASH_STRENGTH_FADE_UPGRADE_RATE;
			splashStrengthFadeUpgradeCount++;
			break;
		case CREATURE_DAMAGE_MODIFIER_ID:
			creatureDamageModifier += CREATURE_DAMAGE_MODIFIER_UPGRADE_RATE;
			creatureDamageModifierUpgradeCount++;
			break;
		case SLOW_DURATION_ID:
			slowDuration += SLOW_DURATION_UPGRADE_RATE;
			slowDurationUpgradeCount++;
			break;
		case SLOW_FACTOR_ID:
			slowFactor -= SLOW_FACTOR_UPGRADE_RATE;
			slowFactorUpgradeCount++;
			break;
		case CREATURE_EVADE_PERCENTAGE_ID:
			creatureEvadePercentage += CREATURE_EVADE_PERCENTAGE_UPGRADE_RATE;
			creatureEvadePercentageUpgradeCount++;
			break;
		case STUN_DURATION_ID:
			stunDuration += STUN_DURATION_UPGRADE_RATE;
			stunDurationUpgradeCount++;
			break;
		case TOWER_ATTACK_RATE_MODIFIER_ID:
			towerAttackRateModifier += TOWER_ATTACK_RATE_MODIFIER_UPGRADE_RATE;
			towerAttackRateModifierUpgradeCount++;
			break;
		case CREATURE_SPEED_MODIFIER_ID:
			creatureSpeedModifier += CREATURE_SPEED_MODIFIER_UPGRADE_RATE;
			creatureSpeedModifierUpgradeCount++;
			break;
		case TOWER_DAMAGE_MODIFIER_ID:
			towerDamageModifier += TOWER_DAMAGE_MODIFIER_UPGRADE_RATE;
			towerDamageModifierUpgradeCount++;
			break;
		case TOWER_DEFENSE_MODIFIER_ID:
			towerDefenseModifier += TOWER_DEFENSE_MODIFIER_UPGRADE_RATE;
			towerDefenseModifierUpgradeCount++;
			break;
		case CREATURE_DEFENSE_MODIFIER_ID:
			creatureDefenseModifier += CREATURE_DEFENSE_MODIFIER_UPGRADE_RATE;
			creatureDefenseModifierUpgradeCount++;
			break;
		case DRAIN_PERCENTAGE_ID:
			drainPercentage += DRAIN_PERCENTAGE_UPGRADE_RATE;
			drainPercentageUpgradeCount++;
			break;
		case DRAIN_DURATION_ID:
			drainDuration += DRAIN_DURATION_UPGRADE_RATE;
			drainDurationUpgradeCount++;
			break;
		case HEAL_PERCENTAGE_ID:
			healPercentage += HEAL_PERCENTAGE_UPGRADE_RATE;
			healPercentageUpgradeCount++;
			break;
		case KILL_PERCENTAGE_ID:
			killPercentage += KILL_PERCENTAGE_UPGRADE_RATE;
			killPercentageUpgradeCount++;
			break;
		case CREATURE_HEALTH_MODIFIER_ID:
			creatureHealthModifier += CREATURE_HEALTH_MODIFIER_UPGRADE_RATE;
			creatureHealthModifierUpgradeCount++;
			break;
		case RESURRECT_PERCENTAGE_ID:
			resurrectPercentage += RESURRECT_PERCENTAGE_UPGRADE_RATE;
			resurrectPercentageUpgradeCount++;
			break;
		default:
			throw new RuntimeException("Unknown upgrade ID: " + id);
		}

	}

	@Override
	public int getUpgradeCount(int id) {
		switch (id) {
		case SPLASH_RADIUS_ID:
			return splashRadiusUpgradeCount;
		case SPLASH_STRENGTH_FADE_ID:
			return splashStrengthFadeUpgradeCount;
		case CREATURE_DAMAGE_MODIFIER_ID:
			return creatureDamageModifierUpgradeCount;
		case SLOW_DURATION_ID:
			return slowDurationUpgradeCount;
		case SLOW_FACTOR_ID:
			return slowFactorUpgradeCount;
		case CREATURE_EVADE_PERCENTAGE_ID:
			return creatureEvadePercentageUpgradeCount;
		case STUN_DURATION_ID:
			return stunDurationUpgradeCount;
		case TOWER_ATTACK_RATE_MODIFIER_ID:
			return towerAttackRateModifierUpgradeCount;
		case CREATURE_SPEED_MODIFIER_ID:
			return creatureSpeedModifierUpgradeCount;
		case TOWER_DAMAGE_MODIFIER_ID:
			return towerDamageModifierUpgradeCount;
		case TOWER_DEFENSE_MODIFIER_ID:
			return towerDefenseModifierUpgradeCount;
		case CREATURE_DEFENSE_MODIFIER_ID:
			return creatureDefenseModifierUpgradeCount;
		case DRAIN_PERCENTAGE_ID:
			return drainPercentageUpgradeCount;
		case DRAIN_DURATION_ID:
			return drainDurationUpgradeCount;
		case HEAL_PERCENTAGE_ID:
			return healPercentageUpgradeCount;
		case KILL_PERCENTAGE_ID:
			return killPercentageUpgradeCount;
		case CREATURE_HEALTH_MODIFIER_ID:
			return creatureHealthModifierUpgradeCount;
		case RESURRECT_PERCENTAGE_ID:
			return resurrectPercentageUpgradeCount;
		default:
			throw new RuntimeException("Unknown getBaseValue ID: " + id);
		}
	}

	@Override
	public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
		splashRadius = input.readFloat();
		splashStrengthFade = input.readFloat();
		creatureDamageModifier = input.readFloat();
		splashRadiusUpgradeCount = input.readInt();
		splashStrengthFadeUpgradeCount = input.readInt();
		creatureDamageModifierUpgradeCount = input.readInt();

		slowDuration = input.readFloat();
		slowFactor = input.readFloat();
		creatureEvadePercentage = input.readFloat();
		slowDurationUpgradeCount = input.readInt();
		slowFactorUpgradeCount = input.readInt();
		creatureEvadePercentageUpgradeCount = input.readInt();

		stunDuration = input.readFloat();
		towerAttackRateModifier = input.readFloat();
		creatureSpeedModifier = input.readFloat();
		stunDurationUpgradeCount = input.readInt();
		towerAttackRateModifierUpgradeCount = input.readInt();
		creatureSpeedModifierUpgradeCount = input.readInt();

		towerDamageModifier = input.readFloat();
		towerDefenseModifier = input.readFloat();
		creatureDefenseModifier = input.readFloat();
		towerDamageModifierUpgradeCount = input.readInt();
		towerDefenseModifierUpgradeCount = input.readInt();
		creatureDefenseModifierUpgradeCount = input.readInt();

		drainDuration = input.readFloat();
		drainPercentage = input.readFloat();
		healPercentage = input.readFloat();
		drainDurationUpgradeCount = input.readInt();
		drainPercentageUpgradeCount = input.readInt();
		healPercentageUpgradeCount = input.readInt();

		killPercentage = input.readFloat();
		creatureHealthModifier = input.readFloat();
		resurrectPercentage = input.readFloat();
		killPercentageUpgradeCount = input.readInt();
		creatureHealthModifierUpgradeCount = input.readInt();
		resurrectPercentageUpgradeCount = input.readInt();
	}

	@Override
	public void writeExternal(ObjectOutput output) throws IOException {
		output.writeFloat(splashRadius);
		output.writeFloat(splashStrengthFade);
		output.writeFloat(creatureDamageModifier);
		output.writeInt(splashRadiusUpgradeCount);
		output.writeInt(splashStrengthFadeUpgradeCount);
		output.writeInt(creatureDamageModifierUpgradeCount);

		output.writeFloat(slowDuration);
		output.writeFloat(slowFactor);
		output.writeFloat(creatureEvadePercentage);
		output.writeInt(slowDurationUpgradeCount);
		output.writeInt(slowFactorUpgradeCount);
		output.writeInt(creatureEvadePercentageUpgradeCount);

		output.writeFloat(stunDuration);
		output.writeFloat(towerAttackRateModifier);
		output.writeFloat(creatureSpeedModifier);
		output.writeInt(stunDurationUpgradeCount);
		output.writeInt(towerAttackRateModifierUpgradeCount);
		output.writeInt(creatureSpeedModifierUpgradeCount);

		output.writeFloat(towerDamageModifier);
		output.writeFloat(towerDefenseModifier);
		output.writeFloat(creatureDefenseModifier);
		output.writeInt(towerDamageModifierUpgradeCount);
		output.writeInt(towerDefenseModifierUpgradeCount);
		output.writeInt(creatureDefenseModifierUpgradeCount);

		output.writeFloat(drainDuration);
		output.writeFloat(drainPercentage);
		output.writeFloat(healPercentage);
		output.writeInt(drainDurationUpgradeCount);
		output.writeInt(drainPercentageUpgradeCount);
		output.writeInt(healPercentageUpgradeCount);

		output.writeFloat(killPercentage);
		output.writeFloat(creatureHealthModifier);
		output.writeFloat(resurrectPercentage);
		output.writeInt(killPercentageUpgradeCount);
		output.writeInt(creatureHealthModifierUpgradeCount);
		output.writeInt(resurrectPercentageUpgradeCount);

	}

}
