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
package com.godsandtowers.core;

import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.hash.TIntHashSet;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.godsandtowers.core.networking.PlayerSnapshot;
import com.godsandtowers.core.networking.RemoteNetworkManager;
import com.godsandtowers.core.networking.Snapshot;
import com.godsandtowers.core.networking.SpriteSnapshot;
import com.godsandtowers.messaging.ApplicationMessageProcessor;
import com.godsandtowers.messaging.GLMessageProcessor;
import com.godsandtowers.messaging.ViewMessageProcessor;
import com.godsandtowers.sprites.BuildingSphere;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.sprites.Tower;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.gl.Sprite;
import com.gundogstudios.modules.Modules;

public class RemoteGameEngine implements GameEngine {

	private static final String TAG = "RemoteGameEngine";
	private TIntObjectHashMap<SpriteSnapshot> playerOneSprites;
	private TIntObjectHashMap<SpriteSnapshot> playerTwoSprites;
	private TIntObjectHashMap<SpriteSnapshot> playerOneGridSquareSprites;
	private TIntObjectHashMap<SpriteSnapshot> playerTwoGridSquareSprites;

	private int timeSinceLastRefresh;
	private long lastSnapshotTimestamp;
	private long overSlept;
	private long gameStart;
	private boolean loading;
	private boolean paused;
	private boolean exited;
	private boolean started;
	private boolean quit;
	private boolean finished;
	private GameInfo gameInfo;
	private int gridSquareIDLimit;
	private ExecutorService executor;
	private RemoteNetworkManager networkManager;

	public RemoteGameEngine(GameInfo gameInfo) {
		this.gameInfo = gameInfo;
		timeSinceLastRefresh = 0;
		lastSnapshotTimestamp = 0;
		overSlept = 0;
		playerOneSprites = new TIntObjectHashMap<SpriteSnapshot>(500);
		playerTwoSprites = new TIntObjectHashMap<SpriteSnapshot>(500);
		playerOneGridSquareSprites = new TIntObjectHashMap<SpriteSnapshot>(500);
		playerTwoGridSquareSprites = new TIntObjectHashMap<SpriteSnapshot>(500);
		quit = false;

		gridSquareIDLimit = addGridSquares(gameInfo.getPlayer(0));
		int tmp = addGridSquares(gameInfo.getPlayer(1));

		if (tmp > gridSquareIDLimit)
			gridSquareIDLimit = tmp;

		networkManager = new RemoteNetworkManager(gameInfo);
		executor = Executors.newSingleThreadExecutor();
		executor.execute(networkManager);

	}

	private int addGridSquares(Player player) {
		TIntObjectHashMap<SpriteSnapshot> map = getGridSquareSprites(player.getID());
		int max = 0;
		for (Sprite sprite : player.getGrid().getAllGridSquares()) {
			map.put(sprite.getID(), new SpriteSnapshot(sprite));
			if (sprite.getID() > max)
				max = sprite.getID();
		}
		return max;
	}

	@Override
	public void buyCreature(int player, String name) {
		networkManager.buyCreature(player, name);
	}

	@Override
	public void buyTower(int player, String name, float x, float y) {
		networkManager.buyTower(player, name, x, y);
	}

	@Override
	public void upgradeTower(int player, float x, float y) {
		networkManager.upgradeTower(player, x, y);
	}

	@Override
	public void sellTower(int player, float x, float y) {
		networkManager.sellTower(player, x, y);
	}

	@Override
	public void buildAllTowers(int player) {
		networkManager.buildAllTowers(player);
	}

	@Override
	public void buildTower(int player, String name) {
		networkManager.buildTower(player, name);
	}

	@Override
	public void cancelBuildTowers(int player) {
		networkManager.cancelBuildTowers(player);
	}

	@Override
	public void gridTouchCommand(int player, int col, int row) {
		networkManager.gridTouchCommand(player, col, row);
	}

	public void executeSpecial(int player, String name) {
		networkManager.executeSpecial(player, name);
	}

	@Override
	public void autoBuyCreature(int player, String name) {
		networkManager.autoBuyCreature(player, name);
	}

	@Override
	public void maxUpgradeTower(int player, float x, float y) {
		networkManager.maxUpgradeTower(player, x, y);
	}

	@Override
	public void cancelAutoBuyCreature(int player) {
		networkManager.cancelAutoBuyCreature(player);

	}

	@Override
	public void launchUpgradeView(int player, Tower tower) {
		if (player == gameInfo.getLocalPlayerID()) {
			Modules.MESSENGER.submit(ViewMessageProcessor.ID, ViewMessageProcessor.UPGRADE_TOWER, tower);
		}
	}

	@Override
	public void processResults(int winnerID) {
		gameInfo.setWinner(winnerID);
		finished = true;
		quit = true;
	}

	@Override
	public void run() {
		try {
			gameStart = System.currentTimeMillis();
			lastSnapshotTimestamp = System.currentTimeMillis();
			while (!quit) {
				long start = System.currentTimeMillis();
				started = true;
				if (!paused && !loading) {
					nextTurn();
				} else {
					synchronized (this) {
						this.notifyAll();
					}

					try {
						Thread.sleep(REFRESH_RATE);
					} catch (InterruptedException e) {
						Modules.LOG.error(TAG, "interrupted during sleeping: " + e.toString());
					}
				}

				timeSinceLastRefresh += System.currentTimeMillis() - start;
				if (timeSinceLastRefresh >= REFRESH_RATE) {
					Modules.MESSENGER.submit(ViewMessageProcessor.ID, ViewMessageProcessor.REFRESH);
					timeSinceLastRefresh = 0;
				}

			}
			Modules.LOG.info(TAG, "thread stopping cleanly");

		} catch (Exception e) {
			e.printStackTrace();
			Modules.LOG.error(TAG, e.toString());
			Modules.MESSENGER.submit(ApplicationMessageProcessor.ID, ApplicationMessageProcessor.GAME_ERROR, e);
		} finally {
			gameInfo.setLength(System.currentTimeMillis() - gameStart);
			Modules.LOG.info(TAG, "EXITING");
			synchronized (this) {
				exited = true;
				this.notifyAll();
			}
			if (networkManager != null) {
				networkManager.shutDown();
				executor.shutdownNow();
			}
			if (finished) {
				Modules.MESSENGER.submit(ApplicationMessageProcessor.ID, ApplicationMessageProcessor.GAME_COMPLETED,
						gameInfo);
			}
		}
	}

	private void nextTurn() throws InterruptedException {
		long start = System.currentTimeMillis();

		Snapshot snapshot = networkManager.getLatestSnapshot();

		if (snapshot != null && snapshot.getTimeStamp() > lastSnapshotTimestamp) {
			lastSnapshotTimestamp = snapshot.getTimeStamp();
			processSnapshot(snapshot);
		}

		if (System.currentTimeMillis() - lastSnapshotTimestamp > DISCONNECT_TIMEOUT) {
			finished = true;
			quit = true;
			gameInfo.setWon(false);
			gameInfo.setDisconnected(true);
		}

		Modules.PROFILER.updateLogicFPS();

		long totalTime = System.currentTimeMillis() - start;
		long sleepTime = getTickInterval() - totalTime - overSlept;

		start = System.currentTimeMillis();
		if (sleepTime > 0) {
			Thread.sleep(sleepTime);
		} else {
			Modules.LOG.warn(TAG, "Processing time took: " + totalTime);
		}

		overSlept = System.currentTimeMillis() - start - sleepTime;
	}

	private void processSnapshot(Snapshot snapshot) {

		int currentWave = snapshot.getCurrentWave();
		gameInfo.setCurrentWave(currentWave);
		gameInfo.setTimeUntilNextWave(snapshot.getTimeUntilNextWave());
		for (PlayerSnapshot playerSnapshot : snapshot.getPlayerSnapshots()) {
			final int playerID = playerSnapshot.getId();
			final TIntObjectHashMap<SpriteSnapshot> sprites = getSprites(playerID);
			final TIntObjectHashMap<SpriteSnapshot> permanentSprites = getGridSquareSprites(playerID);

			Player player = gameInfo.getPlayer(playerID);
			player.setCreatureLevel(playerSnapshot.getCreatureLevel());
			player.setGold(playerSnapshot.getGold());
			player.setIncome(playerSnapshot.getIncome());
			player.setLife(playerSnapshot.getLife());

			PlayerInfo playerInfo = gameInfo.getPlayerInfo(playerID);
			playerInfo.setScore(playerSnapshot.getScore());
			playerInfo.setLife(playerSnapshot.getLife(), currentWave);
			playerInfo.setIncome(playerSnapshot.getIncome(), currentWave);
			playerInfo.setDefensivePower(playerSnapshot.getDefensivePower(), currentWave);
			playerInfo.setOffensivePower(playerSnapshot.getOffensivePower(), currentWave);

			ArrayList<Sprite> newSprites = new ArrayList<Sprite>();
			TIntHashSet removeSpriteIDs = new TIntHashSet(sprites.keys());
			for (SpriteSnapshot newSprite : playerSnapshot.getSprites()) {
				int id = newSprite.getID();
				SpriteSnapshot oldSprite = sprites.get(id);
				if (oldSprite == null) {
					oldSprite = newSprite;
					sprites.put(id, newSprite);
					if (newSprite.getModel() != null) {
						if (newSprite.isTransparent()) {
							if (newSprite.getModel().equals(BuildingSphere.NAME)) {
								// TODO this is an ugly hack, fix me eventually
								float scale = ((float) newSprite.getLevel()) / 10f;
								Modules.LOG.warn(TAG, "Sphere scale is: " + scale);
								Modules.MESSENGER.submit(GLMessageProcessor.ID,
										GLMessageProcessor.ADD_TRANSPARENT_SPRITE, playerID, newSprite, scale);
							} else {
								Modules.MESSENGER.submit(GLMessageProcessor.ID,
										GLMessageProcessor.ADD_TRANSPARENT_SPRITE, playerID, newSprite);
							}
						} else {
							newSprites.add(newSprite);
						}
					}
				} else {
					oldSprite.update(newSprite);
				}
				removeSpriteIDs.remove(id);

				int targetID = oldSprite.getTargetID();
				if (targetID != 0) {
					SpriteSnapshot target;
					if (targetID <= gridSquareIDLimit) {
						target = permanentSprites.get(targetID);
					} else {
						target = sprites.get(targetID);
					}
					oldSprite.setTarget(target);
				}
			}

			Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.ADD_SPRITE_BATCH, playerID, newSprites);

			final ArrayList<Sprite> removeBatch = new ArrayList<Sprite>(removeSpriteIDs.size());
			removeSpriteIDs.forEach(new TIntProcedure() {

				@Override
				public boolean execute(int value) {
					SpriteSnapshot sprite = sprites.remove(value);
					if (sprite.isTransparent())
						Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.REMOVE_TRANSPARENT_SPRITE,
								playerID, sprite);
					else
						removeBatch.add(sprite);
					return true;
				}
			});
			Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.REMOVE_SPRITE_BATCH, playerID,
					removeBatch);

		}

	}

	@Override
	public void setAttacking(int player, boolean attacking) {
		gameInfo.getPlayer(player).setAttacking(attacking);
	}

	@Override
	public GameInfo getGameInfo() {
		return gameInfo;
	}

	@Override
	public boolean hasStarted() {
		return started;
	}

	@Override
	public boolean hasExited() {
		return exited;
	}

	@Override
	public boolean isPaused() {
		return paused;
	}

	@Override
	public boolean isLoading() {
		return loading;
	}

	@Override
	public boolean isSaveable() {
		return false;
	}

	@Override
	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	@Override
	public void setLoading(boolean loading) {
		this.loading = loading;
	}

	@Override
	public void quitGame() {
		quit = false;
		executor.shutdownNow();
	}

	private TIntObjectHashMap<SpriteSnapshot> getSprites(int playerID) {
		if (playerID == 0)
			return playerOneSprites;
		else
			return playerTwoSprites;
	}

	private TIntObjectHashMap<SpriteSnapshot> getGridSquareSprites(int playerID) {
		if (playerID == 0)
			return playerOneGridSquareSprites;
		else
			return playerTwoGridSquareSprites;
	}

	private int getTickInterval() {
		int speed = Modules.PREFERENCES.get(TDWPreferences.GAME_ENGINE_SPEED, FAST);
		switch (speed) {
		case SLOW:
			return 200;
		case NORMAL:
			return 100;
		case FAST:
			return 50;
		default:
			return 0;
		}
	}

}
