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
package com.godsandtowers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.godsandtowers.core.GameInfo;
import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.sprites.BaseCreature;
import com.godsandtowers.sprites.BasePlayer;
import com.godsandtowers.sprites.BaseRace;
import com.godsandtowers.sprites.BaseTower;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.sprites.Races;
import com.gundogstudios.modules.DesktopAssets;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.modules.SystemLogger;
import com.gundogstudios.modules.basic.BasicPreferenceModule;
import com.gundogstudios.modules.basic.EmptyProfilerModule;

public class GeneticBalancer {

	private static final BasicPreferenceModule PREFERENCE_MODULE = new BasicPreferenceModule();
	private static final CustomMessageModule MESSAGE_MODULE = new CustomMessageModule();
	private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(10);
	private static final int STARTING_LEVEL = 10;
	private static final int MAX_LEVEL = 100000;
	private static final float IDEAL_ENTROPY = .0000001f;
	private static final String FILE_NAME = "output/towers.txt";
	private static final int MAX_MUTATIONS = 16;
	private static final int SAVE_MUTATIONS = 4;
	private static final int RUNS_PER_LEVEL = 10;

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		long initialLoad = System.currentTimeMillis();

		Modules.LOG = new SystemLogger();
		Modules.MESSENGER = MESSAGE_MODULE;
		Modules.PREFERENCES = PREFERENCE_MODULE;
		Modules.PROFILER = new EmptyProfilerModule();
		// PREFERENCE_MODULE.put(TDWPreferences.GAME_ENGINE_SPEED, 3);
		Modules.ASSETS = new DesktopAssets();

		Mutation[] mutations = new Mutation[MAX_MUTATIONS];
		Mutation m = new Mutation();
		float lowestEntropy = m.run();

		for (int i = 0; i < mutations.length; i++) {
			mutations[i] = new Mutation(m);
		}

		while (lowestEntropy > IDEAL_ENTROPY) {
			long runStart = System.currentTimeMillis();
			for (Mutation mutation : mutations) {
				mutation.mutate();
				float current = mutation.run();
				if (current < lowestEntropy) {
					saveMutation(mutation);
					lowestEntropy = current;
				}
			}

			Arrays.sort(mutations);

			for (int i = SAVE_MUTATIONS; i < mutations.length; i++) {
				mutations[i].replace(mutations[i % SAVE_MUTATIONS]);
			}

			System.out.println("Lowest entropy " + lowestEntropy + " in " + (System.currentTimeMillis() - runStart)
					+ " ms");
		}

		THREAD_POOL.shutdown();

		System.out.println("Took " + (System.currentTimeMillis() - initialLoad) + " ms to run");
	}

	private static void saveMutation(Mutation mutation) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true));

		writer.append(mutation.toString());

		writer.close();
	}

	private static class Mutation implements Comparable<Mutation> {
		private Entropy entropy;
		private BasePlayer basePlayer;
		private BaseRace baseRace;
		private BaseTower[] baseTowers;
		private BaseCreature[] baseCreatures;
		private ArrayList<Result> results;

		public Mutation() {
			entropy = null;
			results = null;
			basePlayer = BasePlayer.getBasePlayer();
			baseRace = BaseRace.getBaseRace();
			baseTowers = BaseTower.getBaseTowers();
			baseCreatures = BaseCreature.getBaseCreatures();
		}

		public Mutation(Mutation mutation) {
			baseTowers = new BaseTower[mutation.baseTowers.length];
			baseCreatures = new BaseCreature[mutation.baseCreatures.length];
			replace(mutation);
		}

		public void replace(Mutation mutation) {
			entropy = mutation.entropy;
			results = mutation.results;
			basePlayer = new BasePlayer(mutation.basePlayer);
			baseRace = new BaseRace(mutation.baseRace);

			for (int i = 0; i < baseTowers.length; i++) {
				baseTowers[i] = new BaseTower(mutation.baseTowers[i]);
			}

			for (int i = 0; i < baseCreatures.length; i++) {
				baseCreatures[i] = new BaseCreature(mutation.baseCreatures[i]);
			}
		}

		public float run() throws InterruptedException, ExecutionException {
			long start = System.currentTimeMillis();

			LinkedList<Future<GameInfo>> futures = submitFutures();

			results = generateResults(futures);

			entropy = new Entropy(results);
			System.out.println("Entropy was " + entropy.getEntropy() + " in " + (System.currentTimeMillis() - start)
					+ "\n");
			return entropy.getEntropy();
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("\n");
			builder.append("Entropy: ");
			builder.append(entropy.getEntropy());
			builder.append("\n");
			for (int race : Races.ALL_RACES) {
				builder.append(Races.toString(race));
				builder.append(" - ");
				builder.append(entropy.getRaceWinPercentage(race));
				builder.append("\n");
			}
			builder.append("\n");
			for (BaseTower tower : baseTowers) {
				builder.append("new BaseTower(");
				builder.append(tower);
				builder.append("),\n");
			}
			builder.append("\n");

			builder.append("new BaseRace(");
			builder.append(baseRace);
			builder.append(");\n");

			appendResults(builder);
			return builder.toString();
		}

		private void appendResults(StringBuilder builder) {
			builder.append("\n\n");
			builder.append("race,");
			for (int level = 1; level <= MAX_LEVEL; level *= 10) {
				builder.append("level_" + level + ",");
			}
			builder.append("\n");
			for (int races : Races.ALL_RACES) {
				for (int levelOne = STARTING_LEVEL; levelOne <= MAX_LEVEL; levelOne *= 10) {
					builder.append(Races.toString(races) + "_" + levelOne + ",");
					for (int levelTwo = STARTING_LEVEL; levelTwo <= MAX_LEVEL; levelTwo *= 10) {
						int wins = 0;
						int losses = 0;
						int draws = 0;
						for (Result result : results) {
							if (races == result.racesOne && levelOne == result.levelOne && levelTwo == result.levelTwo) {
								wins += result.wins;
								losses += result.losses;
								draws += result.draws;
							}
						}
						builder.append(wins + "-" + losses + "-" + draws + ",");
					}
					builder.append("\n");
				}
			}
		}

		private LinkedList<Future<GameInfo>> submitFutures() {
			LinkedList<Future<GameInfo>> futures = new LinkedList<Future<GameInfo>>();

			PlayerStats stats = new PlayerStats(1, basePlayer, baseRace, baseTowers, baseCreatures);
			for (int levelOne = STARTING_LEVEL; levelOne <= MAX_LEVEL; levelOne *= 10) {
				for (int racesOne : Races.ALL_RACES) {
					for (int levelTwo = STARTING_LEVEL; levelTwo <= MAX_LEVEL; levelTwo *= 10) {
						for (int racesTwo : Races.ALL_RACES) {
							if (racesOne == racesTwo || levelOne != levelTwo)
								continue;
							for (int i = 0; i < RUNS_PER_LEVEL; i++) {
								GameCallable callable = new GameCallable(MESSAGE_MODULE, stats, racesOne, levelOne,
										racesTwo, levelTwo);
								Future<GameInfo> future = THREAD_POOL.submit(callable);
								futures.add(future);
							}
						}
					}
				}
			}
			return futures;
		}

		private ArrayList<Result> generateResults(LinkedList<Future<GameInfo>> futures) throws InterruptedException,
				ExecutionException {
			ArrayList<Result> results = new ArrayList<Result>();
			for (int levelOne = STARTING_LEVEL; levelOne <= MAX_LEVEL; levelOne *= 10) {
				for (int racesOne : Races.ALL_RACES) {
					for (int levelTwo = STARTING_LEVEL; levelTwo <= MAX_LEVEL; levelTwo *= 10) {
						for (int racesTwo : Races.ALL_RACES) {
							if (racesOne == racesTwo || levelOne != levelTwo)
								continue;
							Result result = new Result(racesOne, levelOne, racesTwo, levelTwo);
							for (int i = 0; i < RUNS_PER_LEVEL; i++) {
								Future<GameInfo> future = futures.removeFirst();
								GameInfo gameInfo = future.get();
								Player[] players = gameInfo.getPlayers();
								if (players[0].getLife() > 0 && players[1].getLife() <= 0) {
									result.wins++;
								} else if (players[0].getLife() <= 0 && players[1].getLife() > 0) {
									result.losses++;
								} else {
									result.draws++;
								}
							}
							results.add(result);
						}
					}
				}
			}
			return results;
		}

		public void mutate() {

			float percentage = entropy.getRaceWinPercentage(Races.ICE) - .5f;
			float slowDuration = baseRace.getSlowDuration() * (float) (1f - (Math.random() * percentage));
			float slowFactor = baseRace.getSlowFactor() * (float) (1f - (Math.random() * percentage * -1f));
			float creatureEvadePercentage = baseRace.getCreatureEvadePercentage()
					* (float) (1f - (Math.random() * percentage));

			percentage = entropy.getRaceWinPercentage(Races.WIND) - .5f;
			float stunDuration = baseRace.getStunDuration() * (float) (1f - (Math.random() * percentage));
			float towerAttackRateModifier = baseRace.getTowerAttackRateModifier()
					* (float) (1f - (Math.random() * percentage));
			float creatureSpeedModifier = baseRace.getCreatureSpeedModifier()
					* (float) (1f - (Math.random() * percentage));

			percentage = entropy.getRaceWinPercentage(Races.EARTH) - .5f;
			float towerDamageModifier = baseRace.getTowerDamageModifier() * (float) (1f - (Math.random() * percentage));
			float towerDefenseModifier = baseRace.getTowerDefenseModifier()
					* (float) (1f - (Math.random() * percentage));
			float creatureDefenseModifier = baseRace.getCreatureDefenseModifier()
					* (float) (1f - (Math.random() * percentage));

			percentage = entropy.getRaceWinPercentage(Races.FIRE) - .5f;
			float splashRadius = baseRace.getSplashRadius() * (float) (1f - (Math.random() * percentage));
			float splashStrengthFade = baseRace.getSplashStrengthFade() * (float) (1f - (Math.random() * percentage));
			float creatureDamageModifier = baseRace.getCreatureDamageModifier()
					* (float) (1f - (Math.random() * percentage));

			percentage = entropy.getRaceWinPercentage(Races.LIFE) - .5f;
			float drainDuration = baseRace.getDrainDuration() * (float) (1f - (Math.random() * percentage));
			float drainPercentage = baseRace.getDrainPercentage() * (float) (1f - (Math.random() * percentage));
			float healPercentage = baseRace.getHealPercentage() * (float) (1f - (Math.random() * percentage));

			percentage = entropy.getRaceWinPercentage(Races.DEATH) - .5f;
			float killPercentage = baseRace.getKillPercentage() * (float) (1f - (Math.random() * percentage));
			float creatureHealthModifier = baseRace.getCreatureHealthModifier()
					* (float) (1f - (Math.random() * percentage));
			float resurrectPercentage = baseRace.getResurrectPercentage() * (float) (1f - (Math.random() * percentage));

			baseRace = new BaseRace(slowDuration, slowFactor, creatureEvadePercentage, stunDuration,
					towerAttackRateModifier, creatureSpeedModifier, towerDamageModifier, towerDefenseModifier,
					creatureDefenseModifier, splashRadius, splashStrengthFade, creatureDamageModifier, drainDuration,
					drainPercentage, healPercentage, killPercentage, creatureHealthModifier, resurrectPercentage);

			for (int i = 0; i < baseTowers.length; i++) {

				String name = baseTowers[i].getName();
				int races = baseTowers[i].getRaces();

				if (Races.getNumRaces(races) > 1)
					continue;

				percentage = entropy.getRaceWinPercentage(races) - .5f;

				float factor = (float) (Math.random() * percentage);
				float damage = baseTowers[i].getDamage() * (1 - factor);

				factor = (float) (Math.random() * percentage) * -1f;
				float attackRate = baseTowers[i].getAttackRate() * (1 - factor);

				float cost = baseTowers[i].getCost();
				float health = baseTowers[i].getHealth();
				float attackRange = baseTowers[i].getAttackRange();
				float defense = baseTowers[i].getDefense();
				boolean attacksGround = baseTowers[i].attacksGround();
				boolean attacksAir = baseTowers[i].attacksAir();
				boolean attacksAllInRange = baseTowers[i].attacksAllInRange();
				boolean attacksInstantly = baseTowers[i].attacksInstantly();
				boolean unlocked = baseTowers[i].isUnlocked();

				baseTowers[i] = new BaseTower(name, races, cost, damage, health, defense, attackRate, attackRange,
						attacksGround, attacksAir, attacksAllInRange, attacksInstantly, unlocked);
			}
		}

		@Override
		public int compareTo(Mutation o) {
			return Float.floatToIntBits(o.entropy.getEntropy()) == Float.floatToIntBits(entropy.getEntropy()) ? 0
					: entropy.getEntropy() > o.entropy.getEntropy() ? 1 : -1;
		}

	}

	private static class Entropy {
		private HashMap<Integer, Float> raceWinPercentage = new HashMap<Integer, Float>();
		private float entropy = 0f;

		public Entropy(ArrayList<Result> results) {
			for (int races : Races.ALL_RACES) {
				float wins = 0;
				float losses = 0;
				// float draws = 0;
				for (Result result : results) {
					if (result.levelOne == result.levelTwo) {
						if (races == result.racesOne) {
							wins += result.wins;
							losses += result.losses;
							// draws += result.draws;
						} else if (races == result.racesTwo) {
							wins += result.losses;
							losses += result.wins;
							// draws += result.draws;
						}
					}
				}
				float winPercentage = (wins) / (wins + losses);
				raceWinPercentage.put(races, winPercentage);
				System.out.println(Races.toString(races) + " - " + winPercentage + " " + wins + "-" + losses);

				entropy += Math.pow(winPercentage - .5f, 2);
			}
		}

		public float getRaceWinPercentage(int race) {
			return raceWinPercentage.get(race);
		}

		public float getEntropy() {
			return entropy;
		}

	}
}
