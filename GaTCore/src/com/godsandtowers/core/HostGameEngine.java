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

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.godsandtowers.core.commands.AutoBuyCreatureCommand;
import com.godsandtowers.core.commands.BuildAllTowersCommand;
import com.godsandtowers.core.commands.BuildTowerCommand;
import com.godsandtowers.core.commands.BuyCreatureCommand;
import com.godsandtowers.core.commands.BuyTowerCommand;
import com.godsandtowers.core.commands.CancelAutoBuyCreatureCommand;
import com.godsandtowers.core.commands.CancelBuildTowersCommand;
import com.godsandtowers.core.commands.ExecuteSpecialCommand;
import com.godsandtowers.core.commands.GridTouchCommand;
import com.godsandtowers.core.commands.MaxUpgradeTowerCommand;
import com.godsandtowers.core.commands.SellTowerCommand;
import com.godsandtowers.core.commands.UpgradeTowerCommand;
import com.godsandtowers.core.networking.HostNetworkManager;
import com.godsandtowers.messaging.ApplicationMessageProcessor;
import com.godsandtowers.messaging.GLMessageProcessor;
import com.godsandtowers.messaging.ViewMessageProcessor;
import com.godsandtowers.sprites.Creature;
import com.godsandtowers.sprites.MovingProjectile;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.sprites.Projectile;
import com.godsandtowers.sprites.Tower;
import com.godsandtowers.util.Constants;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.gl.Sprite;
import com.gundogstudios.modules.Modules;

public class HostGameEngine implements GameEngine {
	private static final String TAG = "HostGameEngine";

	private static final int REFRESH_RATE = 500;// ms

	private LinkedBlockingQueue<Runnable> commands;

	private GridTouchListener[] gridTouchListener;

	private Player[] players;
	private boolean loading;
	private boolean paused;
	private boolean quit;
	private boolean exited;
	private boolean started;
	private boolean finished;
	private long overSlept;
	private long lastUpdate;
	private int timeSinceLastRefresh;

	private GameInfo gameInfo;
	private HostNetworkManager networkManager;
	private ExecutorService executor;

	public HostGameEngine(GameInfo gameInfo, HostNetworkManager networkManager) {
		if (networkManager != null) {
			this.networkManager = networkManager;
			executor = Executors.newSingleThreadExecutor();
			executor.execute(networkManager);
		}
		this.players = gameInfo.getPlayers();
		this.gameInfo = gameInfo;
		this.gridTouchListener = new GridTouchListener[players.length];

		for (int i = 0; i < players.length; i++) {
			gridTouchListener[i] = new GridTouchListener(players[i]);
		}

		loading = true;
		paused = true;
		quit = false;
		started = false;
		exited = false;
		finished = false;
		overSlept = 0;
		lastUpdate = 0;
		timeSinceLastRefresh = 0;
		commands = new LinkedBlockingQueue<Runnable>();
	}

	public void upgradeTower(int player, float x, float y) {
		commands.add(new UpgradeTowerCommand(gameInfo, players[player], x, y));
		gridTouchListener[player].cancelUpgrading();
	}

	public void sellTower(int player, float x, float y) {
		commands.add(new SellTowerCommand(gameInfo, players[player], x, y));
	}

	public void buyTower(int player, String name, float x, float y) {
		commands.add(new BuyTowerCommand(gameInfo, players[player], name, x, y));
	}

	public void buyCreature(int player, String name) {
		commands.add(new BuyCreatureCommand(gameInfo, players[player], players[(player + 1) % players.length], name));
	}

	public void executeSpecial(int player, String name) {
		commands.add(new ExecuteSpecialCommand(players[player], players[(player + 1) % players.length], name));
	}

	public void buildAllTowers(int player) {
		commands.add(new BuildAllTowersCommand(gridTouchListener[player]));
	}

	public void buildTower(int player, String name) {
		commands.add(new BuildTowerCommand(gridTouchListener[player], name));
	}

	public void cancelBuildTowers(int player) {
		commands.add(new CancelBuildTowersCommand(gridTouchListener[player]));
	}

	public void gridTouchCommand(int player, int col, int row) {
		commands.add(new GridTouchCommand(gridTouchListener[player], col, row));
	}

	public void autoBuyCreature(int player, String name) {
		commands.add(new AutoBuyCreatureCommand(players[player], name));
	}

	public void maxUpgradeTower(int player, float x, float y) {
		commands.add(new MaxUpgradeTowerCommand(gameInfo, players[player], x, y));
		gridTouchListener[player].cancelUpgrading();
	}

	public void cancelAutoBuyCreature(int player) {
		commands.add(new CancelAutoBuyCreatureCommand(players[player]));
	}

	public void launchUpgradeView(int player, Tower tower) {
		if (player == gameInfo.getLocalPlayerID()) {
			Modules.MESSENGER.submit(ViewMessageProcessor.ID, ViewMessageProcessor.UPGRADE_TOWER, tower);
		} else {
			networkManager.sendUpgradeView(player, tower);
		}
	}

	public void processResults(int winnerID) {
		throw new RuntimeException("ProcessResults called on HostGameEngine, someone is hacking the code");
	}

	@Override
	public void run() {

		try {
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
			if (!Constants.BALANCING)
				Modules.LOG.info(TAG, "thread stopping cleanly");

		} catch (Exception e) {
			e.printStackTrace();
			Modules.LOG.error(TAG, e.toString());
			Modules.MESSENGER.submit(ApplicationMessageProcessor.ID, ApplicationMessageProcessor.GAME_ERROR, e);
		} finally {
			if (!Constants.BALANCING)
				Modules.LOG.info(TAG, "EXITING");
			synchronized (this) {
				exited = true;
				this.notifyAll();
			}
			if (networkManager != null) {
				if (finished)
					networkManager.sendGameResults(gameInfo);
				networkManager.shutDown();
				executor.shutdown();
				try {
					executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				executor.shutdownNow();
			}
			if (finished) {
				if (!Constants.BALANCING)
					Modules.MESSENGER.submit(ApplicationMessageProcessor.ID,
							ApplicationMessageProcessor.GAME_COMPLETED, gameInfo);
			}
		}

	}

	private void nextTurn() throws InterruptedException {
		long start = System.currentTimeMillis();
		int timePassed = (int) (start - lastUpdate);

		if ((timePassed < 0 || timePassed > 1000) || Constants.BALANCING)
			timePassed = 50;

		lastUpdate = start;

		boolean addIncome = gameInfo.update(timePassed);

		boolean opponentsAlive = false;
		boolean playerAlive = false;
		for (int i = 0; i < players.length; i++) {
			Player player = players[i];
			int id = player.getID();
			if (gameInfo.getGameType() == GameInfo.DEFENSE && gameInfo.getCurrentWave() >= gameInfo.getMaxWaves()) {
				if (id != gameInfo.getLocalPlayerID()) {
					opponentsAlive = true;
					continue;
				} else {
					if (player.getGrid().getCreatures().size() == 0) {
						finished = true;
						quit = true;
						gameInfo.setWon(true);
						break;
					}
				}
			}

			String autoBuyCreatureName = player.getAutoBuyCreatureName();
			if (autoBuyCreatureName != null && player.timeToAutoBuy(timePassed)) {
				new BuyCreatureCommand(gameInfo, players[id], players[(id + 1) % players.length], autoBuyCreatureName)
						.run();
			}

			if (addIncome)
				player.addIncome(gameInfo.getCurrentWave());

			player.nextTurn(timePassed, gameInfo.getTimeBetweenWaves());

			ArrayList<Sprite> removeDead = player.updateDyingSprites(timePassed);

			for (Sprite sprite : removeDead) {
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.REMOVE_SPRITE, id, sprite);
			}

			ArrayList<Creature> creaturesReachedFinished = player.moveCreatures(timePassed);

			for (Creature creature : creaturesReachedFinished) {
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.REMOVE_SPRITE, id, creature);
			}

			if (player.getLife() <= 0)
				continue;

			if (i != gameInfo.getLocalPlayerID())
				opponentsAlive = true;
			else
				playerAlive = true;

			ArrayList<Projectile> newProjectiles = new ArrayList<Projectile>();
			ArrayList<Creature> creaturesKilled = player.attackCreatures(timePassed, newProjectiles);

			for (Projectile projectile : newProjectiles) {
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.ADD_SPRITE, id, projectile);
			}

			gameInfo.creaturesKilled(id, creaturesKilled);

			ArrayList<MovingProjectile> uselessProjectiles = new ArrayList<MovingProjectile>();
			creaturesKilled = player.moveProjectiles(timePassed, uselessProjectiles);

			for (MovingProjectile projectile : uselessProjectiles) {
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.REMOVE_SPRITE, id, projectile);
			}

			gameInfo.creaturesKilled(id, creaturesKilled);

			ArrayList<Tower> towersDestroyed = player.attackTowers(timePassed);

			id = (id > 0) ? id - 1 : players.length - 1;
			gameInfo.towersDestroyed(id, towersDestroyed);

		}

		if (!opponentsAlive) {
			finished = true;
			quit = true;
			gameInfo.setWon(true);
		} else if (!playerAlive) {
			finished = true;
			quit = true;
			gameInfo.setWon(false);
		}

		if (networkManager != null) {
			if (System.currentTimeMillis() - networkManager.getTimeSinceLastHeartbeat() > DISCONNECT_TIMEOUT) {
				finished = true;
				quit = true;
				gameInfo.setWon(false);
				gameInfo.setDisconnected(true);
			} else {
				long snapshotTime = System.currentTimeMillis();
				networkManager.sendSnapshot(start);
				snapshotTime = System.currentTimeMillis() - snapshotTime;
				if (snapshotTime > 10) {
					Modules.LOG.warn(TAG, "Snapshot transmit time: " + snapshotTime);
				}
			}
		}

		Runnable command;
		while ((command = commands.poll()) != null)
			command.run();

		if (!Constants.BALANCING) {
			// for (int i = 0; i < players.length; i++) {
			// if (players[i].getGrid().checkState()) {
			// ModuleManager.LOG.warn(TAG, "Player " + i + " has incorrect board state");
			// }
			// }

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
	}

	@Override
	public String toString() {
		return "Wave: " + gameInfo.getCurrentWave() + " Player1:  " + players[0].getLife() + " Player2: "
				+ players[1].getLife();
	}

	public void setAttacking(int player, boolean attacking) {
		players[player].setAttacking(attacking);
	}

	public GameInfo getGameInfo() {
		return gameInfo;
	}

	public boolean hasStarted() {
		return started;
	}

	public boolean hasExited() {
		return exited;
	}

	public boolean isPaused() {
		return paused;
	}

	public boolean isLoading() {
		return loading;
	}

	public boolean isSaveable() {
		return networkManager == null;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
		// prevents User and Computer from submitting commands while the game is paused
		commands.clear();
	}

	public void setLoading(boolean loading) {
		this.loading = loading;
	}

	public void quitGame() {
		quit = true;
		if (networkManager != null)
			executor.shutdownNow();
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
