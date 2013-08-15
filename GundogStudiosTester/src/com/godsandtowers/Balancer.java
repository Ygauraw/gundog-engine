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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.godsandtowers.core.GameInfo;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.sprites.Races;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.DesktopAssets;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.modules.SystemLogger;
import com.gundogstudios.modules.basic.BasicPreferenceModule;
import com.gundogstudios.modules.basic.EmptyProfilerModule;

public class Balancer {
	private static final boolean SAME_RACES = false;
	private static final boolean DIFFERENT_LEVELS = false;
	private static final int NUM_THREADS = 10;
	private static final int STARTING_LEVEL = 10;
	private static final int RUNS_PER_LEVEL = 10;
	private static final int MAX_LEVEL = 100000;
	private static final String FILE_NAME = "output/results.csv";
	private static final CustomMessageModule MESSAGE_MODULE = new CustomMessageModule();
	private static final BasicPreferenceModule PREFERENCE_MODULE = new BasicPreferenceModule();

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		long start = System.currentTimeMillis();
		Modules.LOG = new SystemLogger();
		Modules.MESSENGER = MESSAGE_MODULE;
		Modules.PREFERENCES = PREFERENCE_MODULE;
		Modules.PROFILER = new EmptyProfilerModule();
		PREFERENCE_MODULE.put(TDWPreferences.GAME_ENGINE_SPEED, 3);
		Modules.ASSETS = new DesktopAssets();

		ExecutorService threadPool = Executors.newFixedThreadPool(NUM_THREADS);

		BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME));

		LinkedList<Future<GameInfo>> futures = submitFutures(threadPool);

		ArrayList<Result> results = generateResults(writer, futures);

		writeResults(writer, results);

		calculateEntropy(writer, results);

		writer.close();

		threadPool.shutdown();
		System.out.println("Took " + (System.currentTimeMillis() - start) + " ms to run");
	}

	private static void writeTitle(BufferedWriter writer) throws IOException {
		String date = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(
				new Date(System.currentTimeMillis()));
		writer.append(date + ",");
		for (int levelTwo = STARTING_LEVEL; levelTwo <= MAX_LEVEL; levelTwo *= 10) {
			for (int racesTwo : Races.ALL_RACES) {
				writer.append(Races.toString(racesTwo) + "_" + levelTwo + ",");
			}
		}
		writer.append("\n");
	}

	private static LinkedList<Future<GameInfo>> submitFutures(ExecutorService threadPool) {
		LinkedList<Future<GameInfo>> futures = new LinkedList<Future<GameInfo>>();

		for (int levelOne = STARTING_LEVEL; levelOne <= MAX_LEVEL; levelOne *= 10) {
			for (int racesOne : Races.ALL_RACES) {
				for (int levelTwo = STARTING_LEVEL; levelTwo <= MAX_LEVEL; levelTwo *= 10) {
					for (int racesTwo : Races.ALL_RACES) {
						if ((racesOne == racesTwo && !SAME_RACES) || (levelOne != levelTwo && !DIFFERENT_LEVELS))
							continue;
						for (int i = 0; i < RUNS_PER_LEVEL; i++) {
							GameCallable callable = new GameCallable(MESSAGE_MODULE, racesOne, levelOne, racesTwo,
									levelTwo);
							Future<GameInfo> future = threadPool.submit(callable);
							futures.add(future);
						}
					}
				}
			}
		}
		System.out.println("Submitted " + futures.size() + " futures");
		return futures;
	}

	private static ArrayList<Result> generateResults(BufferedWriter writer, LinkedList<Future<GameInfo>> futures)
			throws IOException, InterruptedException, ExecutionException {
		ArrayList<Result> results = new ArrayList<Result>();

		writeTitle(writer);

		for (int levelOne = STARTING_LEVEL; levelOne <= MAX_LEVEL; levelOne *= 10) {
			for (int racesOne : Races.ALL_RACES) {
				writer.append(Races.toString(racesOne) + "_" + levelOne + ",");
				for (int levelTwo = STARTING_LEVEL; levelTwo <= MAX_LEVEL; levelTwo *= 10) {
					for (int racesTwo : Races.ALL_RACES) {
						if ((racesOne == racesTwo && !SAME_RACES) || (levelOne != levelTwo && !DIFFERENT_LEVELS)) {
							writer.append(",");
							continue;
						}
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
						writer.append(result.wins + "-" + result.losses + "-" + result.draws + ",");
						// System.out.println(Races.toString(racesOne) + "_" + levelOne + " vs "
						// + Races.toString(racesTwo) + "_" + levelTwo + " results: " + result.wins + "-"
						// + result.losses + "-" + result.draws);
					}
				}
				writer.append("\n");
			}
		}
		return results;
	}

	private static void writeResults(BufferedWriter writer, ArrayList<Result> results) throws IOException {
		writer.append("\n\n");

		for (int races : Races.ALL_RACES) {
			for (int levelOne = STARTING_LEVEL; levelOne <= MAX_LEVEL; levelOne *= 10) {
				writer.append(Races.toString(races) + "_" + levelOne + ",");
				int wins = 0;
				int losses = 0;
				int draws = 0;
				for (Result result : results) {
					if (races == result.racesOne && levelOne == result.levelOne) {
						wins += result.wins;
						losses += result.losses;
						draws += result.draws;
					}
				}
				writer.append(wins + "-" + losses + "-" + draws + ",");
				// System.out.println(Races.toString(races) + "_" + levelOne + " vs " + levelTwo + " results: " +
				// wins
				// + "-" + losses + "-" + draws);

				writer.append("\n");
			}
		}
	}

	private static void calculateEntropy(BufferedWriter writer, ArrayList<Result> results) throws IOException {
		float entropy = 0;

		for (int races : Races.ALL_RACES) {
			float wins = 0;
			float losses = 0;
			float draws = 0;
			for (Result result : results) {
				if (result.levelOne == result.levelTwo) {
					if (races == result.racesOne) {
						wins += result.wins;
						losses += result.losses;
						draws += result.draws;
					} else if (races == result.racesTwo) {
						wins += result.losses;
						losses += result.wins;
						draws += result.draws;
					}
				}
			}
			float winPercentage = (wins) / (wins + losses);
			System.out.println(Races.toString(races) + " - " + winPercentage + " " + wins + "-" + losses + "-" + draws);

			entropy += Math.pow(winPercentage - .5f, 2);
		}
		entropy = (float) Math.sqrt(entropy);
		System.out.println("Total entropy for the game: " + entropy);
	}

}
