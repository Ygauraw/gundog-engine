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

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.godsandtowers.core.GameEngine;
import com.godsandtowers.core.GameInfo;
import com.godsandtowers.core.HostGameEngine;
import com.godsandtowers.graphics.game.GameLayoutManager;
import com.godsandtowers.messaging.GLMessageProcessor;
import com.godsandtowers.messaging.LogicHandler;
import com.godsandtowers.messaging.LogicMessageProcessor;
import com.gundogstudios.modules.AssetModule;
import com.gundogstudios.modules.Modules;

public class GameStateManager {

	private static final String TAG = "GameStateManager";
	private ExecutorService gameEngineExecutor;
	private Future<Boolean> future;
	private GameEngine engine;
	private Activity activity;
	private GameLayoutManager gameLayoutManager;

	public GameStateManager(Activity activity) {
		this.activity = activity;
		gameEngineExecutor = Executors.newSingleThreadExecutor();
	}

	private GameLayoutManager getGameLayoutManager() {
		if (gameLayoutManager == null) {
			gameLayoutManager = new GameLayoutManager(activity);
		}
		return gameLayoutManager;
	}

	public void clearGL() {
		getGameLayoutManager().clearGL();
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return getGameLayoutManager().onKeyUp(keyCode, event);
	}

	public boolean canRun() {
		return engine != null;
	}

	public boolean isRunning() {
		return future != null && !future.isDone();
	}

	public void init(GameEngine engine) {
		this.engine = engine;
		getGameLayoutManager().setPlayers(engine.getGameInfo());
		getGameLayoutManager().setDefaults();
		Modules.MESSENGER.register(LogicMessageProcessor.ID, new LogicHandler(engine));
		AssetModule assetModule = Modules.ASSETS;
		Modules.LOG.info(TAG, "Deleting previous file " + assetModule.delete("", MainActivity.SAVE_GAME_FILENAME));

	}

	public void tryLoad(Activity activity, Bundle savedInstanceState) {
		Modules.LOG.info(TAG, "tryLoad");
		GameInfo info = null;

		AssetModule assetModule = Modules.ASSETS;
		try {
			InputStream fin = assetModule.openInput("", MainActivity.SAVE_GAME_FILENAME);
			ObjectInputStream oin = new ObjectInputStream(fin);
			long start = System.currentTimeMillis();
			info = (GameInfo) oin.readObject();
			Modules.LOG.info(TAG, "Took " + (System.currentTimeMillis() - start) + " to load the game");
			fin.close();
		} catch (Exception e) {
			Modules.LOG.info(
					TAG,
					"Failed to load previous save, deleting file "
							+ assetModule.delete("", MainActivity.SAVE_GAME_FILENAME));
		}

		if (info != null) {
			HostGameEngine engine = new HostGameEngine(info, null);
			init(engine);
		}
	}

	public void saveInstanceState(Bundle outState) {
		Modules.LOG.info(TAG, "onSaveInstanceState");
		if (isRunning()) {
			pause();
		}
	}

	public void resume() {
		Modules.LOG.info(TAG, "resume");
		if (future == null || future.isDone())
			future = gameEngineExecutor.submit(engine, true);
		getGameLayoutManager().onResume();
	}

	public void pause() {
		Modules.LOG.info(TAG, "pause");
		getGameLayoutManager().onPause();
		if (isRunning()) {
			try {
				synchronized (engine) {
					if (!engine.hasExited() && !engine.isPaused()) {
						engine.setPaused(true);
						engine.wait();
					}
				}
			} catch (InterruptedException e) {
				Modules.LOG.info(TAG, "Failed to pause game due to interruption\n" + e.toString());
			}
		}
	}

	public void save() {
		Modules.LOG.info(TAG, "save");
		pause();
		if (isRunning() && engine.isSaveable()) {
			try {
				AssetModule assetModule = Modules.ASSETS;
				OutputStream fout = assetModule.openOutput("", MainActivity.SAVE_GAME_FILENAME);
				ObjectOutputStream oout = new ObjectOutputStream(fout);
				long start = System.currentTimeMillis();
				oout.writeObject(engine.getGameInfo());
				Modules.LOG.info(TAG, "Took " + (System.currentTimeMillis() - start) + " to save the game");
				fout.close();
			} catch (IOException e) {
				Modules.LOG.info(TAG, "Failed to save game due to ioexception\n" + e.toString());
			}
		}
	}

	public void shutdownNow() {
		getGameLayoutManager().onPause();
		if (engine != null)
			engine.quitGame();
		if (gameEngineExecutor != null)
			gameEngineExecutor.shutdownNow();
		Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.SHUTDOWN);
	}

	public void shutdown() {
		Modules.LOG.info(TAG, "shutdown");
		getGameLayoutManager().onPause();
		if (isRunning()) {
			try {
				synchronized (engine) {
					if (!engine.hasExited()) {
						engine.quitGame();
						engine.wait();
					}
				}
			} catch (InterruptedException e) {
				Modules.LOG.info(TAG, "Failed to shutdown game engine due to interruption\n" + e.toString());
			}
		}

		engine = null;
	}

}