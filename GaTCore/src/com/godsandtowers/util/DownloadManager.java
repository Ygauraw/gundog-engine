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
package com.godsandtowers.util;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import com.gundogstudios.modules.Modules;

public class DownloadManager {
	private static final int FETCHER_THREADS = 2;
	private static final String TAG = "DownloadManager";
	private static final int SUCCESSFUL = Integer.MIN_VALUE;
	private static final String HOST_NAME = "http://godsandtowers.appspot.com/";
	private static final String[][] ASSETS = {

	{ "boards/", "basic.csv" }, { "boards/", "basic_icon.jpg" }, { "boards/", "close_call.csv" },
			{ "boards/", "close_call_icon.jpg" }, { "boards/", "confusion.csv" }, { "boards/", "confusion_icon.jpg" },
			{ "boards/", "decisions.csv" }, { "boards/", "decisions_icon.jpg" }, { "boards/", "four_square.csv" },
			{ "boards/", "four_square_icon.jpg" }, { "boards/", "oasis.csv" }, { "boards/", "oasis_icon.jpg" },
			{ "boards/", "open_field.csv" }, { "boards/", "open_field_icon.jpg" },
			{ "boards/", "path_to_freedom.csv" }, { "boards/", "path_to_freedom_icon.jpg" },
			{ "boards/", "rocky_mountain.csv" }, { "boards/", "rocky_mountain_icon.jpg" }, { "boards/", "rush.csv" },
			{ "boards/", "rush_icon.jpg" }, { "boards/", "simple.csv" }, { "boards/", "simple_icon.jpg" },
			{ "boards/", "three_way.csv" }, { "boards/", "three_way_icon.jpg" }, { "models/", "ArcherHorsemen.gs1" },
			{ "models/", "Archers.gs1" }, { "models/", "ArrowProjectiles.gs1" }, { "models/", "Ballistas.gs1" },
			{ "models/", "BallProjectiles.gs1" }, { "models/", "Base.gs1" }, { "models/", "Bears.gs1" },
			{ "models/", "Birds.gs1" }, { "models/", "Blade.gs1" }, { "models/", "Cactus.gs1" },
			{ "models/", "Cannons.gs1" }, { "models/", "Catapults.gs1" }, { "models/", "Crystallizers.gs1" },
			{ "models/", "DeadHorsemen.gs1" }, { "models/", "DeadSoldiers.gs1" }, { "models/", "Dragons.gs1" },
			{ "models/", "Emitters.gs1" }, { "models/", "FemaleAngels.gs1" }, { "models/", "FemaleMages.gs1" },
			{ "models/", "Golems.gs1" }, { "models/", "Horsemen.gs1" }, { "models/", "Horses.gs1" },
			{ "models/", "HumanoidBeasts.gs1" }, { "models/", "Humanoids.gs1" }, { "models/", "Mages.gs1" },
			{ "models/", "MaleAngels.gs1" }, { "models/", "Pillars.gs1" }, { "models/", "Rock.gs1" },
			{ "models/", "Rubble.gs1" }, { "models/", "Soldiers.gs1" }, { "models/", "Statues.gs1" },
			{ "models/", "Tree.gs1" }, { "models/", "WallCorner.gs1" }, { "models/", "WallSide.gs1" },
			{ "models/", "Whip.gs1" }, { "music/", "10_intro.mp3" }, { "music/", "10_main.mp3" },
			{ "music/", "1_intro.mp3" }, { "music/", "1_main.mp3" }, { "music/", "2_intro.mp3" },
			{ "music/", "2_main.mp3" }, { "music/", "3_intro.mp3" }, { "music/", "3_main.mp3" },
			{ "music/", "4_intro.mp3" }, { "music/", "4_main.mp3" }, { "music/", "5_intro.mp3" },
			{ "music/", "5_main.mp3" }, { "music/", "6_intro.mp3" }, { "music/", "6_main.mp3" },
			{ "music/", "7_intro.mp3" }, { "music/", "7_main.mp3" }, { "music/", "8_intro.mp3" },
			{ "music/", "8_main.mp3" }, { "music/", "9_intro.mp3" }, { "music/", "9_main.mp3" },
			{ "music/", "theme_intro.mp3" }, { "music/", "theme_main.mp3" }, { "textures/", "angel.png" },
			{ "textures/", "angel_statue.png" }, { "textures/", "background_death.jpg" },
			{ "textures/", "background_earth.jpg" }, { "textures/", "background_fire.jpg" },
			{ "textures/", "background_ice.jpg" }, { "textures/", "background_life.jpg" },
			{ "textures/", "background_wind.jpg" }, { "textures/", "dead_horse.png" },
			{ "textures/", "desert_ground.jpg" }, { "textures/", "desert_props.png" },
			{ "textures/", "desert_wall.jpg" }, { "textures/", "dirt_emitter.png" }, { "textures/", "dragon.png" },
			{ "textures/", "drake.png" }, { "textures/", "dwarf.png" }, { "textures/", "eagle.png" },
			{ "textures/", "earth_ballista.png" }, { "textures/", "earth_ball_projectile.png" },
			{ "textures/", "earth_crystallizer.jpg" }, { "textures/", "earth_golem.jpg" },
			{ "textures/", "earth_pillar.jpg" }, { "textures/", "earth_rubble.jpg" },
			{ "textures/", "elf_wizard.png" }, { "textures/", "elvish_archer.png" },
			{ "textures/", "energy_cannon.jpg" }, { "textures/", "falcon.png" }, { "textures/", "fallen_angel.png" },
			{ "textures/", "female_elf_mage.png" }, { "textures/", "female_necromancer.png" },
			{ "textures/", "fiery_bear.jpg" }, { "textures/", "fire_ball_projectile.png" },
			{ "textures/", "fire_emitter.png" }, { "textures/", "fire_golem.jpg" }, { "textures/", "fire_pillar.jpg" },
			{ "textures/", "fire_rubble.jpg" }, { "textures/", "flame_ballista.png" },
			{ "textures/", "flame_blade.png" }, { "textures/", "flame_cannon.png" },
			{ "textures/", "flame_catapult.png" }, { "textures/", "flame_crystallizer.jpg" },
			{ "textures/", "flame_whip.png" }, { "textures/", "forest_ground.jpg" },
			{ "textures/", "forest_props.png" }, { "textures/", "forest_wall.jpg" },
			{ "textures/", "frozen_horse.png" }, { "textures/", "frozen_soldier.png" },
			{ "textures/", "gargoyle_statue.png" }, { "textures/", "ghoul.png" }, { "textures/", "gnome.png" },
			{ "textures/", "griffin_statue.png" }, { "textures/", "grizzly_bear.jpg" },
			{ "textures/", "halfling.png" }, { "textures/", "holy_water_emitter.jpg" }, { "textures/", "horse.png" },
			{ "textures/", "human_archer.png" }, { "textures/", "human_mage.png" },
			{ "textures/", "human_necromancer.png" }, { "textures/", "human_witch.png" },
			{ "textures/", "ice_ballista.png" }, { "textures/", "ice_ball_projectile.png" },
			{ "textures/", "ice_blade.png" }, { "textures/", "ice_crystallizer.jpg" },
			{ "textures/", "ice_golem.jpg" }, { "textures/", "kobold.png" },
			{ "textures/", "life_ball_projectile.png" }, { "textures/", "life_golem.jpg" },
			{ "textures/", "life_pillar.jpg" }, { "textures/", "life_rubble.jpg" },
			{ "textures/", "lightning_blade.png" }, { "textures/", "lightning_cannon.png" },
			{ "textures/", "lightning_emitter.png" }, { "textures/", "lightning_golem.jpg" },
			{ "textures/", "lightning_pillar.jpg" }, { "textures/", "mummy.png" }, { "textures/", "panda_bear.jpg" },
			{ "textures/", "poison_cannon.png" }, { "textures/", "polar_bear.jpg" },
			{ "textures/", "rock_cannon.png" }, { "textures/", "seraphim.png" }, { "textures/", "sky_day.jpg" },
			{ "textures/", "sky_night.jpg" }, { "textures/", "stone_catapult.png" }, { "textures/", "succubus.png" },
			{ "textures/", "tentical_whip.png" }, { "textures/", "toxic_gas_emitter.jpg" },
			{ "textures/", "troll.png" }, { "textures/", "undead_ball_projectile.jpg" },
			{ "textures/", "undead_golem.jpg" }, { "textures/", "undead_pillar.jpg" },
			{ "textures/", "undead_rubble.jpg" }, { "textures/", "wall_side.jpg" },
			{ "textures/", "water_cannon.png" }, { "textures/", "water_catapult.png" },
			{ "textures/", "water_emitter.png" }, { "textures/", "water_pillar.jpg" },
			{ "textures/", "water_rubble.jpg" }, { "textures/", "water_whip.png" },
			{ "textures/", "wind_ball_projectile.jpg" }, { "textures/", "wind_rubble.jpg" },
			{ "textures/", "winter_forest_ground.jpg" }, { "textures/", "winter_forest_props.png" },
			{ "textures/", "winter_forest_wall.jpg" }, { "textures/", "zealot.png" }, { "textures/", "zombie.png" },

	};
	private static final long[] ASSET_SIZES = {

	189, 9897, 136, 9101, 275, 8669, 212, 9159, 275, 8833, 228, 8323, 180, 9158, 292, 10505, 182, 8178, 196, 9906, 182,
			7992, 273, 10512, 292528, 292528, 438, 85224, 2114, 2564, 208592, 269906, 70310, 2810, 44418, 85962, 91780,
			258630, 227362, 297496, 10640, 252398, 287756, 171784, 257452, 262152, 317646, 343788, 292116, 212582,
			7458, 2372, 65786, 286086, 81678, 13002, 3566, 966, 151566, 387072, 1120256, 387072, 1120256, 247808,
			1476608, 247808, 1968128, 131072, 1282048, 131072, 1282048, 124928, 1476608, 315392, 1433600, 282624,
			1120256, 247808, 1476608, 32768, 739328, 336169, 445412, 128340, 70188, 73455, 73468, 131514, 70193,
			404764, 57403, 436573, 66573, 371145, 445306, 431645, 324589, 389047, 378028, 138379, 50864, 36448, 53786,
			17790, 329652, 383968, 56098, 415637, 308224, 336794, 320578, 49483, 126134, 335807, 44910, 65285, 14589,
			359339, 403766, 345040, 390106, 40720, 374329, 163731, 431092, 68905, 501918, 553702, 352448, 284317,
			352186, 406563, 66215, 304283, 58381, 378399, 383731, 380555, 290630, 307534, 408192, 144920, 437823,
			44299, 56876, 352539, 147845, 54001, 66898, 18190, 455979, 426567, 381167, 48491, 64668, 501662, 72751,
			341532, 90012, 376659, 284352, 17896, 13656, 391301, 275637, 452414, 47541, 248888, 18507, 37970, 49837,
			14389, 119803, 412638, 444927, 411892, 65070, 18712, 399156, 23419, 17995, 100299, 425440, 67205, 420137,
			391808,

	};
	public static final long TOTAL_SIZE = 46975565;

	private HashMap<String, Integer> assets;
	private AtomicLong currentAssetSize;
	private ExecutorService executor;
	private ArrayList<Future<Integer>> futures;

	private String FILE_SYSTEM_PATH;

	public DownloadManager(String systemPath) {

		// TODO use "/Android/data/com.godsandtowers/" not "TDW" for the release version of the game so that all data
		// is deleted after uninstall
		FILE_SYSTEM_PATH = systemPath + "/Gods And Towers/assets/";
		currentAssetSize = new AtomicLong(0);
		executor = Executors.newFixedThreadPool(FETCHER_THREADS);
		assets = new HashMap<String, Integer>();
		futures = new ArrayList<Future<Integer>>();

	}

	private long verifyAsset(File file) {
		long fileSize = 0;
		File[] files = file.listFiles();
		if (files != null) {
			for (File f : files) {
				fileSize += verifyAsset(f);
			}
		} else {
			Integer loc = assets.remove(file.getName());
			if (loc != null) {
				fileSize = file.length();
				if (fileSize != ASSET_SIZES[loc]) {
					assets.put(file.getName(), loc);
					Modules.LOG.info(TAG, "Asset: " + file.getName() + " has incorrect filesize, it is " + fileSize
							+ " and should be " + ASSET_SIZES[loc]);
					file.delete();
					return 0;
				}
			} else {
				Modules.LOG.info(TAG, "Unknown asset file: " + file.getName());
			}

		}
		return fileSize;
	}

	public void shutdown() {
		executor.shutdownNow();
	}

	public synchronized boolean verifyAllAssets() {
		assets.clear();

		for (int i = 0; i < ASSETS.length; i++) {
			assets.put(ASSETS[i][1], i);
		}

		currentAssetSize.set(verifyAsset(new File(FILE_SYSTEM_PATH)));

		if (assets.size() > 0) {
			Modules.LOG.info(TAG, "Still waiting to download " + assets.size() + " assets out of " + ASSETS.length);
			return false;
		}
		if (currentAssetSize.get() != TOTAL_SIZE) {
			Modules.LOG.error(TAG, "CurrentAssetSize " + currentAssetSize + " does not match total size " + TOTAL_SIZE);
			return false;
		}
		return true;
	}

	public synchronized void downloadMissingAssets() {
		futures.clear();
		for (String asset : assets.keySet()) {
			Integer loc = assets.get(asset);
			Future<Integer> submit = executor.submit(new Downloader(loc, HOST_NAME));
			futures.add(submit);
		}
	}

	public synchronized boolean downloadComplete() {
		for (Future<Integer> future : futures) {
			if (!future.isDone())
				return false;

		}
		return true;
	}

	public synchronized boolean downloadFailed() {
		for (Future<Integer> future : futures) {
			try {
				int result = future.get();
				if (result != SUCCESSFUL)
					return true;
			} catch (InterruptedException e) {
				Modules.LOG.error(TAG, e.toString());
				return true;
			} catch (ExecutionException e) {
				Modules.LOG.error(TAG, e.toString());
				return true;
			}
		}
		return false;
	}

	public synchronized long getCurrentSize() {
		return currentAssetSize.get();
	}

	public synchronized long getTotalSize() {
		return TOTAL_SIZE;
	}

	private class Downloader implements Callable<Integer> {
		private int loc;
		private String hostname;

		public Downloader(int loc, String hostname) {
			this.loc = loc;
			this.hostname = hostname;
		}

		@Override
		public Integer call() {
			try {
				long startTime = System.currentTimeMillis();

				String basePath = ASSETS[loc][0];
				String fileName = ASSETS[loc][1];

				String urlName = hostname + "assets/" + basePath + fileName;

				Modules.LOG.info(TAG, "Downloading " + urlName);

				String path = FILE_SYSTEM_PATH + basePath;
				new File(path).mkdirs();
				path += fileName;
				Modules.LOG.info(TAG, "Storing " + fileName + " at " + path);
				FileOutputStream stream = new FileOutputStream(path, false);
				FileChannel fileChannel = stream.getChannel();

				URL url = new URL(urlName);
				URLConnection openConnection = url.openConnection();
				openConnection.setUseCaches(false);
				openConnection.setDoOutput(false);
				ReadableByteChannel inChannel = Channels.newChannel(openConnection.getInputStream());

				long read = 0;
				long pos = 0;
				while ((read = fileChannel.transferFrom(inChannel, pos, ASSET_SIZES[loc])) >= 0
						&& pos < ASSET_SIZES[loc]) {
					pos += read;
					currentAssetSize.addAndGet(read);
				}
				inChannel.close();
				fileChannel.close();
				stream.close();

				File file = new File(path);
				if (ASSET_SIZES[loc] != file.length()) {
					file.delete();
					Modules.LOG.error(TAG, "Failed to download " + fileName + " correctly");
					return loc;
				}

				Modules.LOG.info(TAG, "wrote file in " + (System.currentTimeMillis() - startTime) + " msec");
				return SUCCESSFUL;
			} catch (Exception e) {
				Modules.LOG.error(TAG, "Error downloading file: " + e.toString());
				return 0;
			}
		}

	}
}
