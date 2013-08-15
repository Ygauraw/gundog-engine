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
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.godsandtowers.R;
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
import com.godsandtowers.graphics.game.BitmapCache;
import com.godsandtowers.messaging.ApplicationMessageProcessor;
import com.godsandtowers.messaging.GLMessageProcessor;
import com.godsandtowers.messaging.ViewMessageProcessor;
import com.godsandtowers.sprites.BaseCreature;
import com.godsandtowers.sprites.BaseSpecial;
import com.godsandtowers.sprites.Creature;
import com.godsandtowers.sprites.MovingProjectile;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.sprites.Projectile;
import com.godsandtowers.sprites.Tower;
import com.godsandtowers.sprites.Upgradeable;
import com.godsandtowers.util.ResourceUtilities;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.gl.Sprite;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.util.FastMath;

public class TutorialGameEngine implements GameEngine {
	private static final String TAG = "TutorialGameEngine";

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
	private Activity activity;

	private Queue<Task> tasks;
	private long timeOfLastTask;

	public TutorialGameEngine(Activity activity, GameInfo gameInfo) {
		this.activity = activity;
		this.tasks = new LinkedList<TutorialGameEngine.Task>();
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
		populateTaskList();
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

	public void launchUpgradeView(int player, Tower tower) {
		if (player == gameInfo.getLocalPlayerID())
			Modules.MESSENGER.submit(ViewMessageProcessor.ID, ViewMessageProcessor.UPGRADE_TOWER, tower);
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

	public void processResults(int winnerID) {
		throw new RuntimeException("ProcessResults called on HostGameEngine, someone is hacking the code");
	}

	@Override
	public void run() {

		try {
			timeOfLastTask = System.currentTimeMillis();
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
			Modules.LOG.info(TAG, "EXITING");
			synchronized (this) {
				exited = true;
				this.notifyAll();
			}

			if (finished) {
				Modules.MESSENGER.submit(ApplicationMessageProcessor.ID, ApplicationMessageProcessor.GAME_COMPLETED,
						gameInfo);
			}
		}

	}

	private void nextTurn() throws InterruptedException {
		Task task = tasks.peek();
		if (task == null) {
			finished = true;
			quit = true;
			return;
		} else {
			if (task.displayWhenReady()) {
				tasks.remove();
			}
		}

		long start = System.currentTimeMillis();
		int timePassed = (int) (start - lastUpdate);

		if (timePassed < 0 || timePassed > 1000)
			timePassed = 50;

		lastUpdate = start;

		boolean addIncome = gameInfo.update(timePassed);

		for (int i = 0; i < players.length; i++) {
			Player player = players[i];
			int id = player.getID();

			String autoBuyCreatureName = player.getAutoBuyCreatureName();
			if (autoBuyCreatureName != null) {
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
			if (player.getLife() < 1)
				player.setLife(20);

			for (Creature creature : creaturesReachedFinished) {
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.REMOVE_SPRITE, id, creature);
			}

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

		Runnable command;
		while ((command = commands.poll()) != null)
			command.run();

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
		return false;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
		// prevents User and Computer from submitting commands while the game is paused
		commands.clear();
		Task task = tasks.peek();
		task.cancel();
	}

	public void setLoading(boolean loading) {
		this.loading = loading;
	}

	public void quitGame() {
		quit = true;
	}

	private Player getHuman() {
		return players[0];
	}

	private Player getComputer() {
		return players[1];
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

	private void populateTaskList() {
		Task task;

		task = new SimpleTask(generateDialog(0, R.string.tutorial_intro));
		tasks.add(task);

		task = new SimpleTask(generateDialog(0, R.string.tutorial_game_area));
		tasks.add(task);

		task = new WaitTask(generateDialog(0, R.string.tutorial_top_menu, R.drawable.topmenu_life,
				R.string.tutorial_top_menu_life, R.drawable.topmenu_money, R.string.tutorial_top_menu_gold,
				R.drawable.topmenu_money, R.string.tutorial_top_menu_income), 10000);
		tasks.add(task);

		task = new WaitTask(generateDialog(0, R.string.tutorial_bottom_menu, R.drawable.bottommenu_towers,
				R.string.tutorial_bottom_menu_tower, R.drawable.bottommenu_creatures,
				R.string.tutorial_bottom_menu_creature, R.drawable.bottommenu_specials,
				R.string.tutorial_bottom_menu_special, R.drawable.bottommenu_options,
				R.string.tutorial_bottom_menu_option, R.drawable.bottommenu_attack,
				R.string.tutorial_bottom_menu_attacking, R.drawable.bottommenu_pause,
				R.string.tutorial_bottom_menu_pause), 5000);
		tasks.add(task);

		task = new WaitTask(generateDialog(0, R.string.tutorial_buying_tower, R.drawable.bottommenu_towers,
				R.string.tutorial_bottom_menu_tower, R.drawable.icon_water_cannon, R.string.tutorial_buying_tower_pick,
				R.drawable.bottommenu_cancel, R.string.tutorial_buying_tower_cancel), 5000);
		tasks.add(task);

		task = new WaitTask(generateDialog(0, R.string.tutorial_buying_tower_selected, R.drawable.bottommenu_buy,
				R.string.tutorial_buying_tower_purchase, R.drawable.bottommenu_cancel,
				R.string.tutorial_buying_tower_cancel), 5000);

		task = new TowerTask(generateDialog(0, R.string.tutorial_build_maze), 1);
		tasks.add(task);

		task = new TowerTask(generateDialog(0, R.string.tutorial_upgrade_tower), 10);
		tasks.add(task);

		task = new UpgradeTask(generateDialog(0, R.string.tutorial_upgrade_tower_selected,
				R.drawable.icon_water_cannon, R.string.tutorial_upgrade_tower_purchase, R.drawable.bottommenu_buy,
				R.string.tutorial_upgrade_tower_sell, R.drawable.bottommenu_cancel,
				R.string.tutorial_upgrade_tower_cancel, R.drawable.icon_water_cannon,
				R.string.tutorial_upgrade_tower_long_press));
		tasks.add(task);

		task = new WaitTask(generateDialog(0, R.string.tutorial_side_buttons, R.drawable.button_back,
				R.string.tutorial_side_buttons_pic), 5000);
		tasks.add(task);

		task = new WaitTask(generateDialog(0, R.string.tutorial_buying_creature, R.drawable.bottommenu_creatures,
				R.string.tutorial_bottom_menu_creature, R.drawable.icon_frozen_soldier,
				R.string.tutorial_buying_creature_purchase, R.drawable.bottommenu_cancel,
				R.string.tutorial_buying_creature_cancel, R.drawable.icon_frozen_soldier,
				R.string.tutorial_buying_creature_long_press), 10000);
		tasks.add(task);

		task = new CreatureTask(generateDialog(0, R.string.tutorial_attacking, R.drawable.bottommenu_attack,
				R.string.tutorial_attacking_button, R.drawable.bottommenu_move, R.string.tutorial_attacking_move), 5);
		tasks.add(task);

		task = new AttackingTask(generateDialog(0, R.string.tutorial_specials, R.drawable.icon_special_damage,
				R.string.tutorial_specials_damage, R.drawable.icon_special_heal, R.string.tutorial_specials_heal,
				R.drawable.icon_special_kill, R.string.tutorial_specials_kill, R.drawable.icon_special_slow,
				R.string.tutorial_specials_slow, R.drawable.icon_special_speed, R.string.tutorial_specials_speed,
				R.drawable.icon_special_stun, R.string.tutorial_specials_stun));
		tasks.add(task);

		task = new SpecialTask(generateDialog(0, R.string.tutorial_options_menu, R.drawable.option_zoomin,
				R.string.tutorial_options_menu_zoomin, R.drawable.option_zoomout,
				R.string.tutorial_options_menu_zoomout, R.drawable.option_yrotate_cw,
				R.string.tutorial_options_menu_rotateup, R.drawable.option_yrotate_ccw,
				R.string.tutorial_options_menu_rotatedown, R.drawable.option_resetview,
				R.string.tutorial_options_menu_default, R.drawable.option_play, R.string.tutorial_options_menu_play,
				R.drawable.option_pause, R.string.tutorial_options_menu_pause));
		tasks.add(task);

		task = new WaitTask(generateUpgradingDialog(), 10000);
		tasks.add(task);

		task = new WaitTask(generateDialog(0, R.string.tutorial_completion), 10000);
		tasks.add(task);
	}

	private TutorialDialog generateUpgradingDialog() {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		ids.add(0);
		ids.add(R.string.tutorial_upgrading);
		Player player = players[0];

		addUpgradeable(ids, player.getBasePlayer());

		addUpgradeable(ids, player.getRace().getBaseRace());

		addUpgradeable(ids, player.getCreature(BaseCreature.FROZEN_SOLDIER));

		int[] idArray = new int[ids.size()];
		for (int i = 0; i < ids.size(); i++) {
			idArray[i] = ids.get(i);
		}
		return generateDialog(idArray);
	}

	private void addUpgradeable(ArrayList<Integer> ids, Upgradeable upgradeable) {
		for (int nameID : upgradeable.getUpgradeIDs()) {
			final String name = upgradeable.getUpgradeName(nameID);
			int iconID = ResourceUtilities.getIconID(name);
			int stringID = ResourceUtilities.getStringID(name);
			ids.add(iconID);
			ids.add(stringID);
		}
	}

	private TutorialDialog generateDialog(int... ids) {
		int[][] format = new int[ids.length / 2][2];
		for (int i = 0; i < ids.length; i++) {
			format[FastMath.floor(i / 2)][i % 2] = ids[i];
		}
		return new TutorialDialog(format);
	}

	private class SpecialTask extends Task {
		public SpecialTask(TutorialDialog dialog) {
			super(dialog);
		}

		@Override
		protected boolean completed() {
			for (BaseSpecial special : getHuman().getPlayerStats().getSpecials()) {
				if (special.getName().equals(BaseSpecial.SLOW_CREATURES))
					return startingSpecialCount >= special.getCount();
			}
			return false;
		}
	}

	private int startingSpecialCount;

	private class AttackingTask extends Task {
		public AttackingTask(TutorialDialog dialog) {
			super(dialog);
		}

		@Override
		protected boolean completed() {
			if (getHuman().isAttacking()) {
				for (BaseSpecial special : getHuman().getPlayerStats().getSpecials()) {
					if (special.getName().equals(BaseSpecial.SLOW_CREATURES)) {
						startingSpecialCount = special.getCount();
						special.upgrade(1);
					}
				}
				return true;
			}
			return false;
		}
	}

	private class UpgradeTask extends Task {
		public UpgradeTask(TutorialDialog dialog) {
			super(dialog);
		}

		@Override
		protected boolean completed() {
			return getHuman().getGrid().getBuildingTowers().size() != getHuman().getGrid().getBuildingSpheres().size();
		}
	}

	private class CreatureTask extends Task {
		private int count;

		public CreatureTask(TutorialDialog dialog, int count) {
			super(dialog);
			this.count = count;
		}

		@Override
		protected boolean completed() {
			return getComputer().getGrid().getCreatures().size() >= count;
		}
	}

	private class TowerTask extends Task {
		private int count;

		public TowerTask(TutorialDialog dialog, int count) {
			super(dialog);
			this.count = count;
		}

		@Override
		protected boolean completed() {
			return getHuman().getGrid().getTowers().size() >= count;
		}
	}

	private class SimpleTask extends Task {
		public SimpleTask(TutorialDialog dialog) {
			super(dialog);
		}

		@Override
		protected boolean completed() {
			return true;
		}
	}

	private class WaitTask extends Task {
		public long waitTime;

		public WaitTask(TutorialDialog dialog, long waitTime) {
			super(dialog);
			this.waitTime = waitTime;
		}

		@Override
		protected boolean completed() {
			return (System.currentTimeMillis() - timeOfLastTask) > waitTime;
		}
	}

	private abstract class Task {

		private TutorialDialog dialog;

		public Task(TutorialDialog dialog) {
			this.dialog = dialog;
		}

		public void cancel() {
			dialog.cancel();
		}

		protected abstract boolean completed();

		public boolean displayWhenReady() {
			if (!completed())
				return false;

			dialog.show();

			return true;
		}
	}

	private class TutorialDialog {
		private AlertDialog dialog;
		private CountDownLatch latch;
		private int[][] ids;

		public TutorialDialog(int[][] ids) {
			this.ids = ids;
			this.dialog = null;
		}

		public void cancel() {
			if (dialog != null) {
				dialog.dismiss();
				latch.countDown();
			}
		}

		public void show() {
			if (ids == null)
				return;

			latch = new CountDownLatch(1);
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ScrollView scrollView = new ScrollView(activity);
					scrollView.setScrollbarFadingEnabled(false);
					LinearLayout outsideLayout = new LinearLayout(activity);
					outsideLayout.setOrientation(LinearLayout.VERTICAL);
					outsideLayout.setGravity(Gravity.CENTER);

					for (int[] idPair : ids) {
						LinearLayout insideLayout = new LinearLayout(activity);
						insideLayout.setOrientation(LinearLayout.HORIZONTAL);
						if (idPair[0] != 0) {
							ImageView image = new ImageView(activity);
							image.setImageResource(idPair[0]);
							insideLayout.addView(image);
						}

						TextView text = new TextView(activity);
						text.setText(idPair[1]);
						text.setPadding(5, 0, 5, 0);
						insideLayout.addView(text);

						outsideLayout.addView(insideLayout);
						ImageView seperator = new ImageView(activity);
						seperator.setImageBitmap(BitmapCache.getBitmap(R.drawable.menu_seperator));
						outsideLayout.addView(seperator);
					}

					scrollView.addView(outsideLayout);
					AlertDialog.Builder builder = new AlertDialog.Builder(activity);
					builder.setView(scrollView);
					builder.setCancelable(false);
					builder.setNeutralButton(R.string.tutorial_continue, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							latch.countDown();
						}
					});
					dialog = builder.create();

					dialog.show();
				}
			});
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
				finished = true;
			} finally {
				timeOfLastTask = System.currentTimeMillis();
			}

		}

	}

}
