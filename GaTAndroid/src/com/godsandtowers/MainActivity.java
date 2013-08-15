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
import java.io.OutputStream;

import org.restlet.resource.ClientResource;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.godsandtowers.core.GameEngine;
import com.godsandtowers.core.GameInfo;
import com.godsandtowers.core.PlayerSaver;
import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.core.networking.ApplicationInfo;
import com.godsandtowers.core.networking.GetAppInfoResource;
import com.godsandtowers.graphics.game.BitmapCache;
import com.godsandtowers.graphics.menu.MenuLayoutManager;
import com.godsandtowers.messaging.ApplicationHandler;
import com.godsandtowers.messaging.ApplicationMessageProcessor;
import com.godsandtowers.util.ADS;
import com.godsandtowers.util.AppRater;
import com.godsandtowers.util.Constants;
import com.godsandtowers.util.ResourceUtilities;
import com.godsandtowers.util.TDWAndroidPreferences;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.gl.ModelUtils;
import com.gundogstudios.modules.AndroidAssets;
import com.gundogstudios.modules.AndroidAudio;
import com.gundogstudios.modules.AndroidGLES11;
import com.gundogstudios.modules.AndroidLogger;
import com.gundogstudios.modules.AndroidReporter;
import com.gundogstudios.modules.AssetModule;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.modules.PurchaseModule;
import com.gundogstudios.modules.basic.BasicMessageModule;
import com.gundogstudios.modules.basic.BasicNetworkModule;
import com.gundogstudios.modules.basic.BasicProfilerModule;
import com.gundogstudios.modules.basic.EmptyLogger;
import com.gundogstudios.modules.basic.EmptyProfilerModule;
import com.gundogstudios.util.FastMath;

public abstract class MainActivity extends Activity {

	protected abstract void initPurchaser();

	private static final String TAG = "MainActivity";

	public static final String GAME_RESULTS = "GameResults";
	public static final String PLAYER_STATS = "PlayerStats";
	public static final String SAVE_STATS_FILENAME = "stats.gat";
	public static final String SAVE_GAME_FILENAME = "game.gat";

	private PlayerStats stats;
	private GameStateManager game;
	private MenuLayoutManager menu;
	private boolean firstLoad = true;

	public void clearGL() {
		game.clearGL();
	}

	public void displayGame() {

		if (Modules.PREFERENCES.get(TDWPreferences.SOUND, true)) {
			int song = FastMath.round(FastMath.random(1, 10));
			Modules.AUDIO.play("" + song, true);
		}
		game.resume();
		RelativeLayout gameLayout = (RelativeLayout) super.findViewById(R.id.gameLayout);
		gameLayout.setVisibility(View.VISIBLE);
		LinearLayout menuLayout = (LinearLayout) super.findViewById(R.id.menuLayout);
		menuLayout.setVisibility(View.GONE);
		// This code is key to ensure that the normal Views receive the button commands, not the GLView.
		// Without this the back button and the menu buttons will not work
		gameLayout.setFocusable(true);
		gameLayout.setFocusableInTouchMode(true);
		gameLayout.requestFocus();
	}

	public void displayMenu() {
		if (Modules.PREFERENCES.get(TDWPreferences.SOUND, true)) {
			Modules.AUDIO.play("theme", true);
		}

		menu.reset();
		game.pause();
		displayResume();
		RelativeLayout gameLayout = (RelativeLayout) super.findViewById(R.id.gameLayout);
		gameLayout.setVisibility(View.GONE);

		LinearLayout menuLayout = (LinearLayout) super.findViewById(R.id.menuLayout);
		menuLayout.setVisibility(View.VISIBLE);

		// This code is key to ensure that the normal Views receive the button commands, not the GLView.
		// Without this the back button and the menu buttons will not work
		menuLayout.setFocusable(true);
		menuLayout.setFocusableInTouchMode(true);
		menuLayout.requestFocus();
		displayLogo();

	}

	private void displayLogo() {
		final ScrollView scrollableLeftLayout = (ScrollView) super.findViewById(R.id.scrollableLeftLayout);
		final ScrollView scrollableRightLayout = (ScrollView) super.findViewById(R.id.scrollableRightLayout);
		final LinearLayout logoLayout = (LinearLayout) super.findViewById(R.id.logo_layout);
		if (firstLoad) {
			scrollableLeftLayout.setVisibility(View.GONE);
			scrollableRightLayout.setVisibility(View.GONE);
			logoLayout.setVisibility(View.VISIBLE);

			BitmapDrawable drawable = new BitmapDrawable(this.getResources(),
					BitmapCache.getBitmap(R.drawable.gundog_studios_logo));
			logoLayout.setBackgroundDrawable(drawable);
			AlphaAnimation animation = new AlphaAnimation(0f, 1f);
			animation.setDuration(5000);
			final Activity activity = this;
			animation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					scrollableLeftLayout.setVisibility(View.VISIBLE);
					scrollableRightLayout.setVisibility(View.VISIBLE);
					logoLayout.setVisibility(View.GONE);
					firstLoad = false;

					LinearLayout main = (LinearLayout) activity.findViewById(R.id.menuLayout);
					BitmapDrawable drawable = new BitmapDrawable(activity.getResources(), BitmapCache
							.getBitmap(R.drawable.menu_background));
					main.setBackgroundDrawable(drawable);
				}
			});
			logoLayout.startAnimation(animation);
			logoLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					scrollableLeftLayout.setVisibility(View.VISIBLE);
					scrollableRightLayout.setVisibility(View.VISIBLE);
					logoLayout.setVisibility(View.GONE);
					logoLayout.setAnimation(null);
					firstLoad = false;

					LinearLayout main = (LinearLayout) activity.findViewById(R.id.menuLayout);
					BitmapDrawable drawable = new BitmapDrawable(activity.getResources(), BitmapCache
							.getBitmap(R.drawable.menu_background));
					main.setBackgroundDrawable(drawable);

				}
			});
		} else {
			scrollableLeftLayout.setVisibility(View.VISIBLE);
			scrollableRightLayout.setVisibility(View.VISIBLE);
			logoLayout.setVisibility(View.GONE);
		}
	}

	public void newGame(GameEngine engine) {
		game.shutdown();
		game.init(engine);
		displayGame();
	}

	public void gameError(Exception e) {
		e.printStackTrace();
		Modules.LOG.error(TAG, "error in game engine " + e.toString());
		game.shutdown();
		displayMenu();
	}

	public void gameFinished(GameInfo gameInfo) {
		Modules.LOG.info(TAG, "Game Finished " + gameInfo.getLength());
		game.shutdown();
		gameInfo.setNoXP(super.getPackageName().equals("com.godsandtowers.free"));
		stats.update(gameInfo);
		save();

		RelativeLayout gameLayout = (RelativeLayout) super.findViewById(R.id.gameLayout);
		gameLayout.setVisibility(View.GONE);
		if (GameInfo.TUTORIAL != gameInfo.getGameType()) {
			Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
			gameInfo.removePlayers();
			intent.putExtra(GAME_RESULTS, gameInfo);
			super.startActivity(intent);
		} else {
			menu.reset();
			displayMenu();
		}

	}

	public void reset() {

		AlertDialog a = new AlertDialog(this) {

		};
		a.setTitle(R.string.options_resetPlayer);
		final MainActivity activity = this;
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				if (which == AlertDialog.BUTTON_POSITIVE) {
					game.shutdown();
					AssetModule assetModule = Modules.ASSETS;
					Modules.LOG.info(TAG,
							"Deleting previous game file " + assetModule.delete("", MainActivity.SAVE_STATS_FILENAME));
					Modules.LOG.info(TAG,
							"Deleting previous stats file " + assetModule.delete("", MainActivity.SAVE_GAME_FILENAME));

					Toast toast = Toast.makeText(activity, activity.getText(R.string.manager_playerReset),
							Toast.LENGTH_LONG);
					toast.show();
					stats.reset();
					menu.reset();
					Modules.PURCHASER.restoreTransactions();
					displayMenu();
				}

			}
		};
		a.setButton(AlertDialog.BUTTON_POSITIVE, this.getText(R.string.options_reset), listener);
		a.setButton(AlertDialog.BUTTON_NEGATIVE, this.getText(R.string.options_cancel), listener);
		a.show();
	}

	public void clearAssets() {

		AlertDialog a = new AlertDialog(this) {

		};
		a.setTitle(R.string.options_assetClear);
		final MainActivity activity = this;
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				if (which == AlertDialog.BUTTON_POSITIVE) {
					// ((AndroidAssets) Modules.ASSETS).deleteAllAssets();
					Toast toast = Toast.makeText(activity, R.string.options_clearing, Toast.LENGTH_LONG);
					toast.show();
					activity.finish();
				}

			}
		};
		a.setButton(AlertDialog.BUTTON_POSITIVE, this.getText(R.string.options_clear), listener);
		a.setButton(AlertDialog.BUTTON_NEGATIVE, this.getText(R.string.options_cancel), listener);
		a.show();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Modules.LOG.info(TAG, "onConfigurationChanged");
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {

		if (KeyEvent.KEYCODE_BACK == keyCode) {
			onBackPressed();
			return true;
		}
		if (game.isRunning()) {
			return game.onKeyUp(keyCode, event);
		}
		return true;
	}

	public void onBackPressed() {
		// super.onBackPressed();
		Modules.LOG.info(TAG, "On Back Pressed");
		RelativeLayout gameLayout = (RelativeLayout) super.findViewById(R.id.gameLayout);
		if (gameLayout.isShown()) {
			game.pause();
			displayMenu();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.setProperty("java.net.preferIPv6Addresses", "false");
		super.onCreate(savedInstanceState);

		super.setContentView(R.layout.mainlayout);
		super.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		ADS.setActivity(this);

		ResourceUtilities.init(super.getResources());
		BitmapCache.init(super.getResources());

		Modules.LOG = Constants.TESTING ? new AndroidLogger() : new EmptyLogger();
		Modules.AUDIO = new AndroidAudio(this.getAssets());
		Modules.ASSETS = new AndroidAssets(this.getAssets());
		initPurchaser();
		Modules.GL = new AndroidGLES11();
		Modules.PROFILER = Constants.TESTING ? new BasicProfilerModule() : new EmptyProfilerModule();
		Modules.MESSENGER = new BasicMessageModule();
		Modules.PREFERENCES = new TDWAndroidPreferences(this);
		Modules.REPORTER = new AndroidReporter();
		Modules.NETWORKING = new BasicNetworkModule();

		Modules.LOG.info(TAG, "onCreate " + super.getPackageName());

		Modules.MESSENGER.register(ApplicationMessageProcessor.ID, new ApplicationHandler(this));

		if (savedInstanceState != null) {
			Modules.LOG.info(TAG, "Trying to load from savedInstanceState");
			stats = (PlayerStats) savedInstanceState.getSerializable(PLAYER_STATS);
		}

		if (stats == null) {
			Modules.LOG.info(TAG, "Trying to load from new file");
			AssetModule assetModule = Modules.ASSETS;
			try {
				InputStream fin = assetModule.openInput("", SAVE_STATS_FILENAME);
				stats = PlayerSaver.load(fin);
				fin.close();
			} catch (Exception e) {
				Modules.LOG.info(TAG,
						"Failed to load previous save, deleting file " + assetModule.delete("", SAVE_STATS_FILENAME));
			}
		}

		if (stats == null) {
			Modules.LOG.info(TAG, "Unable to find previous player stats, creating new player");
			stats = new PlayerStats(0);
		}

		menu = new MenuLayoutManager(this, stats);

		checkAppInfo();

		game = new GameStateManager(this);

		try {
			game.tryLoad(this, savedInstanceState);
		} catch (Exception e) {
			Modules.REPORTER.report(e);
			Modules.LOG.info(TAG, "Failed to load game engine due to " + e.getLocalizedMessage());
		}
		String URI = this.getResources().getText(R.string.app_uri).toString();
		AppRater.launchRating(this, URI);
	}

	private void checkAppInfo() {
		final Activity activity = this;
		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					ClientResource prepareGameClient = new ClientResource("http://" + Constants.RESTLET_HOSTNAME + ":"
							+ Constants.RESTLET_PORT + Constants.RESTLET_GETINFO);
					prepareGameClient.setRequestEntityBuffering(true);
					GetAppInfoResource prepareGameResource = prepareGameClient.wrap(GetAppInfoResource.class);
					Modules.LOG.info(TAG, "sending getAppInfo requests");
					final ApplicationInfo appInfo = prepareGameResource.getLatestInfo();
					if (appInfo == null) {
						Modules.LOG.error(TAG, "getLatestInfo returned null, but no error");
					} else {
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								menu.setNews(appInfo.getNews());
								try {
									int currentVersion = Integer.parseInt(Constants.APP_VERSION.replace(".", ""));
									int serverVersion = Integer.parseInt(appInfo.getAppVersion().replace(".", ""));
									menu.setOnlinePlay(currentVersion >= serverVersion);
								} catch (Exception e) {
									Modules.LOG.error(TAG, "error setting online play");
								}
							}
						});
					}
				} catch (Exception e) {

					Modules.LOG.error(TAG, "Failed to retrieve latest application info due to " + e.getMessage());
				}
			}
		}).start();

	}

	public void lowerGraphics() {
		final Activity activity = this;
		if (game.isRunning())
			game.shutdownNow();
		super.runOnUiThread(new Runnable() {
			Dialog dialog;

			@Override
			public void run() {
				ScrollView scrollView = new ScrollView(activity);
				scrollView.setScrollbarFadingEnabled(false);
				LinearLayout outsideLayout = new LinearLayout(activity);
				outsideLayout.setOrientation(LinearLayout.VERTICAL);

				TextView text = new TextView(activity);
				text.setText(R.string.main_graphics);

				outsideLayout.addView(text);

				scrollView.addView(outsideLayout);
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setView(scrollView);
				builder.setCancelable(false);
				builder.setNeutralButton(R.string.tutorial_continue, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						ModelUtils.decreaseTextureQuality();
						long free = Runtime.getRuntime().freeMemory() / 1024;
						long total = Runtime.getRuntime().totalMemory() / 1024;
						long max = Runtime.getRuntime().maxMemory() / 1048576;
						Modules.REPORTER.report(new RuntimeException("Application quit due to OOM: Ram: f" + free
								+ "KB t" + total + "KB m" + max + "MB"));
						activity.finish();
					}
				});
				dialog = builder.create();

				dialog.show();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		Modules.LOG.info(TAG, "onResume");
		PurchaseModule purchaseModule = Modules.PURCHASER;
		purchaseModule.init(this, stats);
		purchaseModule.restoreTransactions();
		displayResume();
		displayMenu();
	}

	private void displayResume() {
		if (game.canRun()) {
			menu.showResume();
		} else {
			menu.hideResume();
		}
		menu.attachNews();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Modules.LOG.info(TAG, "onSaveInstanceState");
		outState.putSerializable(PLAYER_STATS, stats);
		game.saveInstanceState(outState);
	}

	@Override
	public void onPause() {
		super.onPause();
		Modules.LOG.info(TAG, "onPause");
		Modules.AUDIO.shutdown();
		save();
	}

	private void save() {
		try {
			AssetModule assetModule = Modules.ASSETS;
			OutputStream fout = assetModule.openOutput("", SAVE_STATS_FILENAME);
			PlayerSaver.write(stats, fout);
			fout.close();
		} catch (IOException e) {
			Modules.LOG.info(TAG, "Failed to save game due to ioexception\n" + e.toString());
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Modules.LOG.info(TAG, "onDestroy");
		Modules.NETWORKING.disconnect();
		Modules.PURCHASER.destroy();
		game.save();
		game.shutdown();
	}

	@Override
	public CharSequence onCreateDescription() {
		return getText(R.string.app_description);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Modules.LOG.info(TAG, "Activity returned with resultCode " + resultCode);

		displayMenu();
	}

}