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
package com.godsandtowers.achievements;

import static com.godsandtowers.achievements.Achievement.BRONZE;
import static com.godsandtowers.achievements.Achievement.GOLD;
import static com.godsandtowers.achievements.Achievement.PLATINUM;
import static com.godsandtowers.achievements.Achievement.SILVER;
import static com.godsandtowers.campaigns.CampaignLevel.EASY;
import static com.godsandtowers.campaigns.CampaignLevel.EXPERT;
import static com.godsandtowers.campaigns.CampaignLevel.HARD;
import static com.godsandtowers.campaigns.CampaignLevel.MEDIUM;
import static com.godsandtowers.sprites.BaseCreature.ANGEL;
import static com.godsandtowers.sprites.BaseCreature.DRAGON;
import static com.godsandtowers.sprites.BaseCreature.DRAKE;
import static com.godsandtowers.sprites.BaseCreature.EAGLE;
import static com.godsandtowers.sprites.BaseCreature.EARTH_GOLEM;
import static com.godsandtowers.sprites.BaseCreature.ELVISH_ARCHER_HORSEMAN;
import static com.godsandtowers.sprites.BaseCreature.FALCON;
import static com.godsandtowers.sprites.BaseCreature.FALLEN_ANGEL;
import static com.godsandtowers.sprites.BaseCreature.FIERY_BEAR;
import static com.godsandtowers.sprites.BaseCreature.FIRE_GOLEM;
import static com.godsandtowers.sprites.BaseCreature.FROZEN_SOLDIER_HORSEMAN;
import static com.godsandtowers.sprites.BaseCreature.GRIZZLY_BEAR;
import static com.godsandtowers.sprites.BaseCreature.HUMAN_ARCHER_HORSEMAN;
import static com.godsandtowers.sprites.BaseCreature.ICE_GOLEM;
import static com.godsandtowers.sprites.BaseCreature.LIFE_GOLEM;
import static com.godsandtowers.sprites.BaseCreature.LIGHTNING_GOLEM;
import static com.godsandtowers.sprites.BaseCreature.MUMMY_HORSEMAN;
import static com.godsandtowers.sprites.BaseCreature.PANDA_BEAR;
import static com.godsandtowers.sprites.BaseCreature.POLAR_BEAR;
import static com.godsandtowers.sprites.BaseCreature.SERAPHIM;
import static com.godsandtowers.sprites.BaseCreature.SUCCUBUS;
import static com.godsandtowers.sprites.BaseCreature.UNDEAD_GOLEM;
import static com.godsandtowers.sprites.BaseCreature.ZEALOT_HORSEMAN;
import static com.godsandtowers.sprites.BaseCreature.ZOMBIE_HORSEMAN;
import static com.godsandtowers.sprites.BaseTower.ANGEL_STATUE;
import static com.godsandtowers.sprites.BaseTower.DIRT_EMITTER;
import static com.godsandtowers.sprites.BaseTower.EARTH_PILLAR;
import static com.godsandtowers.sprites.BaseTower.FIRE_EMITTER;
import static com.godsandtowers.sprites.BaseTower.FIRE_PILLAR;
import static com.godsandtowers.sprites.BaseTower.FLAME_CATAPULT;
import static com.godsandtowers.sprites.BaseTower.GARGOYLE_STATUE;
import static com.godsandtowers.sprites.BaseTower.GRIFFIN_STATUE;
import static com.godsandtowers.sprites.BaseTower.HOLY_WATER_EMITTER;
import static com.godsandtowers.sprites.BaseTower.LIFE_PILLAR;
import static com.godsandtowers.sprites.BaseTower.LIGHTNING_EMITTER;
import static com.godsandtowers.sprites.BaseTower.LIGHTNING_PILLAR;
import static com.godsandtowers.sprites.BaseTower.STONE_CATAPULT;
import static com.godsandtowers.sprites.BaseTower.TOXIC_GAS_EMITTER;
import static com.godsandtowers.sprites.BaseTower.UNDEAD_PILLAR;
import static com.godsandtowers.sprites.BaseTower.WATER_CATAPULT;
import static com.godsandtowers.sprites.BaseTower.WATER_EMITTER;
import static com.godsandtowers.sprites.BaseTower.WATER_PILLAR;
import static com.godsandtowers.sprites.Races.DEATH;
import static com.godsandtowers.sprites.Races.EARTH;
import static com.godsandtowers.sprites.Races.FIRE;
import static com.godsandtowers.sprites.Races.ICE;
import static com.godsandtowers.sprites.Races.LIFE;
import static com.godsandtowers.sprites.Races.WIND;
import static com.godsandtowers.sprites.Races.getName;

public class Achievements {
	private static final int[] UNLOCK_WINS = { 2, 4, 6, 8, 10, 12, 16 };
	private static final int BRONZE_WINS = 5;
	private static final int SILVER_WINS = 25;
	private static final int GOLD_WINS = 100;
	private static final int PLATINUM_WINS = 1000;

	public static Achievement[] getAchievements() {
		return new Achievement[] {
				// ICE
				new UnlockAchievement(FROZEN_SOLDIER_HORSEMAN, BRONZE, UNLOCK_WINS[0]),
				new UnlockAchievement(WATER_CATAPULT, BRONZE, UNLOCK_WINS[1]),
				new UnlockAchievement(POLAR_BEAR, BRONZE, UNLOCK_WINS[2]),
				new UnlockAchievement(ICE_GOLEM, BRONZE, UNLOCK_WINS[3]),
				new UnlockAchievement(WATER_PILLAR, BRONZE, UNLOCK_WINS[4]),
				new UnlockAchievement(DRAKE, BRONZE, UNLOCK_WINS[5]),
				new UnlockAchievement(WATER_EMITTER, BRONZE, UNLOCK_WINS[6]),
				new CompleteRaceAchievement(getName(ICE), SILVER, ICE),
				new CampaignAchievement(getName(ICE), BRONZE, ICE, EASY),
				new CampaignAchievement(getName(ICE), SILVER, ICE, MEDIUM),
				new CampaignAchievement(getName(ICE), GOLD, ICE, HARD),
				new CampaignAchievement(getName(ICE), PLATINUM, ICE, EXPERT),
				new BattleAchievement(getName(ICE), BRONZE, ICE, BRONZE_WINS),
				new BattleAchievement(getName(ICE), SILVER, ICE, SILVER_WINS),
				new BattleAchievement(getName(ICE), GOLD, ICE, GOLD_WINS),
				new BattleAchievement(getName(ICE), PLATINUM, ICE, PLATINUM_WINS),
				// WIND
				new UnlockAchievement(HUMAN_ARCHER_HORSEMAN, BRONZE, UNLOCK_WINS[0]),
				new UnlockAchievement(GRIFFIN_STATUE, BRONZE, UNLOCK_WINS[1]),
				new UnlockAchievement(PANDA_BEAR, BRONZE, UNLOCK_WINS[2]),
				new UnlockAchievement(LIGHTNING_GOLEM, BRONZE, UNLOCK_WINS[3]),
				new UnlockAchievement(LIGHTNING_PILLAR, BRONZE, UNLOCK_WINS[4]),
				new UnlockAchievement(DRAGON, BRONZE, UNLOCK_WINS[5]),
				new UnlockAchievement(LIGHTNING_EMITTER, BRONZE, UNLOCK_WINS[6]),
				new CompleteRaceAchievement(getName(WIND), SILVER, WIND),
				new CampaignAchievement(getName(WIND), BRONZE, WIND, EASY),
				new CampaignAchievement(getName(WIND), SILVER, WIND, MEDIUM),
				new CampaignAchievement(getName(WIND), GOLD, WIND, HARD),
				new CampaignAchievement(getName(WIND), PLATINUM, WIND, EXPERT),
				new BattleAchievement(getName(WIND), BRONZE, WIND, BRONZE_WINS),
				new BattleAchievement(getName(WIND), SILVER, WIND, SILVER_WINS),
				new BattleAchievement(getName(WIND), GOLD, WIND, GOLD_WINS),
				new BattleAchievement(getName(WIND), PLATINUM, WIND, PLATINUM_WINS),
				// EARTH
				new UnlockAchievement(ELVISH_ARCHER_HORSEMAN, BRONZE, UNLOCK_WINS[0]),
				new UnlockAchievement(STONE_CATAPULT, BRONZE, UNLOCK_WINS[1]),
				new UnlockAchievement(GRIZZLY_BEAR, BRONZE, UNLOCK_WINS[2]),
				new UnlockAchievement(EARTH_GOLEM, BRONZE, UNLOCK_WINS[3]),
				new UnlockAchievement(EARTH_PILLAR, BRONZE, UNLOCK_WINS[4]),
				new UnlockAchievement(EAGLE, BRONZE, UNLOCK_WINS[5]),
				new UnlockAchievement(DIRT_EMITTER, BRONZE, UNLOCK_WINS[6]),
				new CompleteRaceAchievement(getName(EARTH), SILVER, EARTH),
				new CampaignAchievement(getName(EARTH), BRONZE, EARTH, EASY),
				new CampaignAchievement(getName(EARTH), SILVER, EARTH, MEDIUM),
				new CampaignAchievement(getName(EARTH), GOLD, EARTH, HARD),
				new CampaignAchievement(getName(EARTH), PLATINUM, EARTH, EXPERT),
				new BattleAchievement(getName(EARTH), BRONZE, EARTH, BRONZE_WINS),
				new BattleAchievement(getName(EARTH), SILVER, EARTH, SILVER_WINS),
				new BattleAchievement(getName(EARTH), GOLD, EARTH, GOLD_WINS),
				new BattleAchievement(getName(EARTH), PLATINUM, EARTH, PLATINUM_WINS),
				// FIRE
				new UnlockAchievement(MUMMY_HORSEMAN, BRONZE, UNLOCK_WINS[0]),
				new UnlockAchievement(FLAME_CATAPULT, BRONZE, UNLOCK_WINS[1]),
				new UnlockAchievement(FIERY_BEAR, BRONZE, UNLOCK_WINS[2]),
				new UnlockAchievement(FIRE_GOLEM, BRONZE, UNLOCK_WINS[3]),
				new UnlockAchievement(FIRE_PILLAR, BRONZE, UNLOCK_WINS[4]),
				new UnlockAchievement(FALCON, BRONZE, UNLOCK_WINS[5]),
				new UnlockAchievement(FIRE_EMITTER, BRONZE, UNLOCK_WINS[6]),
				new CompleteRaceAchievement(getName(FIRE), SILVER, FIRE),
				new CampaignAchievement(getName(FIRE), BRONZE, FIRE, EASY),
				new CampaignAchievement(getName(FIRE), SILVER, FIRE, MEDIUM),
				new CampaignAchievement(getName(FIRE), GOLD, FIRE, HARD),
				new CampaignAchievement(getName(FIRE), PLATINUM, FIRE, EXPERT),
				new BattleAchievement(getName(FIRE), BRONZE, FIRE, BRONZE_WINS),
				new BattleAchievement(getName(FIRE), SILVER, FIRE, SILVER_WINS),
				new BattleAchievement(getName(FIRE), GOLD, FIRE, GOLD_WINS),
				new BattleAchievement(getName(FIRE), PLATINUM, FIRE, PLATINUM_WINS),
				// LIFE
				new UnlockAchievement(ZEALOT_HORSEMAN, BRONZE, UNLOCK_WINS[0]),
				new UnlockAchievement(ANGEL_STATUE, BRONZE, UNLOCK_WINS[1]),
				new UnlockAchievement(SERAPHIM, BRONZE, UNLOCK_WINS[2]),
				new UnlockAchievement(LIFE_GOLEM, BRONZE, UNLOCK_WINS[3]),
				new UnlockAchievement(LIFE_PILLAR, BRONZE, UNLOCK_WINS[4]),
				new UnlockAchievement(ANGEL, BRONZE, UNLOCK_WINS[5]),
				new UnlockAchievement(HOLY_WATER_EMITTER, BRONZE, UNLOCK_WINS[6]),
				new CompleteRaceAchievement(getName(LIFE), SILVER, LIFE),
				new CampaignAchievement(getName(LIFE), BRONZE, LIFE, EASY),
				new CampaignAchievement(getName(LIFE), SILVER, LIFE, MEDIUM),
				new CampaignAchievement(getName(LIFE), GOLD, LIFE, HARD),
				new CampaignAchievement(getName(LIFE), PLATINUM, LIFE, EXPERT),
				new BattleAchievement(getName(LIFE), BRONZE, LIFE, BRONZE_WINS),
				new BattleAchievement(getName(LIFE), SILVER, LIFE, SILVER_WINS),
				new BattleAchievement(getName(LIFE), GOLD, LIFE, GOLD_WINS),
				new BattleAchievement(getName(LIFE), PLATINUM, LIFE, PLATINUM_WINS),
				// DEATH
				new UnlockAchievement(ZOMBIE_HORSEMAN, BRONZE, UNLOCK_WINS[0]),
				new UnlockAchievement(GARGOYLE_STATUE, BRONZE, UNLOCK_WINS[1]),
				new UnlockAchievement(SUCCUBUS, BRONZE, UNLOCK_WINS[2]),
				new UnlockAchievement(UNDEAD_GOLEM, BRONZE, UNLOCK_WINS[3]),
				new UnlockAchievement(UNDEAD_PILLAR, BRONZE, UNLOCK_WINS[4]),
				new UnlockAchievement(FALLEN_ANGEL, BRONZE, UNLOCK_WINS[5]),
				new UnlockAchievement(TOXIC_GAS_EMITTER, BRONZE, UNLOCK_WINS[6]),
				new CompleteRaceAchievement(getName(DEATH), SILVER, DEATH),
				new CampaignAchievement(getName(DEATH), BRONZE, DEATH, EASY),
				new CampaignAchievement(getName(DEATH), SILVER, DEATH, MEDIUM),
				new CampaignAchievement(getName(DEATH), GOLD, DEATH, HARD),
				new CampaignAchievement(getName(DEATH), PLATINUM, DEATH, EXPERT),
				new BattleAchievement(getName(DEATH), BRONZE, DEATH, BRONZE_WINS),
				new BattleAchievement(getName(DEATH), SILVER, DEATH, SILVER_WINS),
				new BattleAchievement(getName(DEATH), GOLD, DEATH, GOLD_WINS),
				new BattleAchievement(getName(DEATH), PLATINUM, DEATH, PLATINUM_WINS),

		};
	};

}
