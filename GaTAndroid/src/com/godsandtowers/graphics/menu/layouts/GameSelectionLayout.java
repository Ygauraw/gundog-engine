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
package com.godsandtowers.graphics.menu.layouts;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.godsandtowers.R;
import com.godsandtowers.core.GameInfo;
import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.core.TutorialGameEngine;
import com.godsandtowers.core.grid.Board;
import com.godsandtowers.core.grid.Boards;
import com.godsandtowers.core.grid.Grid;
import com.godsandtowers.graphics.game.BitmapCache;
import com.godsandtowers.graphics.menu.MenuLayoutManager;
import com.godsandtowers.messaging.ApplicationMessageProcessor;
import com.godsandtowers.sprites.AIPlayer;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.sprites.Races;
import com.godsandtowers.util.ADS;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.Modules;

public class GameSelectionLayout implements ILayout {
	private LinearLayout gameSelectionLayout;
	private CampaignLayout campaignLayout;
	private MenuLayoutManager manager;
	private PlayerStats stats;
	private Activity activity;
	private boolean onlinePlayEnabled;

	public GameSelectionLayout(Activity activity, MenuLayoutManager manager, PlayerStats stats) {
		this.activity = activity;
		this.manager = manager;
		this.stats = stats;
		this.onlinePlayEnabled = false;
	}

	public void setOnlinePlay(boolean onlinePlayEnabled) {
		this.onlinePlayEnabled = onlinePlayEnabled;
	}

	public void reset() {
		if (campaignLayout != null) {
			campaignLayout.reset();
		}
	}

	public LinearLayout getLayout() {
		if (gameSelectionLayout == null) {

			gameSelectionLayout = new LinearLayout(activity);
			gameSelectionLayout.setOrientation(LinearLayout.VERTICAL);

			ADS.placeObtrusiveADMobAD(gameSelectionLayout);
			Typeface typeFace = (Typeface) Modules.PREFERENCES.get(TDWPreferences.BUTTON_FONT, Typeface.DEFAULT);
			int color = Modules.PREFERENCES.get(TDWPreferences.BUTTON_COLOR, Color.WHITE);

			campaignLayout = new CampaignLayout(activity, manager, stats);
			Button button = new Button(activity);
			BitmapDrawable drawable = new BitmapDrawable(activity.getResources(),
					BitmapCache.getBitmap(R.drawable.menu_options_button));
			button.setBackgroundDrawable(drawable);
			button.setText(R.string.newgame_tutorial);
			button.setTypeface(typeFace);
			button.setTextColor(color);
			button.setTextSize(20);
			button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Board board = Boards.ALL_BOARDS[0];

					Grid humanGrid = new Grid(board);
					Player human = new Player(0, stats, Races.ICE, humanGrid);

					PlayerStats stats = new PlayerStats(1);
					Grid computerGrid = new Grid(board);
					Player computer = new AIPlayer(1, stats, Races.EARTH, computerGrid, true, true);

					Player[] players = new Player[] { human, computer };

					GameInfo gameInfo = new GameInfo(0, players, GameInfo.FAST, GameInfo.TUTORIAL,
							GameInfo.BATTLE_WAVES, board);
					TutorialGameEngine engine = new TutorialGameEngine(activity, gameInfo);

					Modules.MESSENGER.submit(ApplicationMessageProcessor.ID, ApplicationMessageProcessor.ATTACH_GAME,
							engine);
				}
			});

			gameSelectionLayout.addView(button);

			button = new Button(activity);
			drawable = new BitmapDrawable(activity.getResources(),
					BitmapCache.getBitmap(R.drawable.menu_options_button));
			button.setBackgroundDrawable(drawable);
			button.setText(R.string.newgame_campaign);
			button.setTypeface(typeFace);
			button.setTextColor(color);
			button.setTextSize(20);
			button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					manager.attachRightLayout(campaignLayout);
				}
			});

			gameSelectionLayout.addView(button);

			button = new Button(activity);
			drawable = new BitmapDrawable(activity.getResources(),
					BitmapCache.getBitmap(R.drawable.menu_options_button));
			button.setBackgroundDrawable(drawable);
			button.setText(R.string.newgame_defense);
			button.setTypeface(typeFace);
			button.setTextColor(color);
			button.setTextSize(20);
			button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					BoardSelectionLayout defenseLayout = new BoardSelectionLayout(manager, stats, GameInfo.DEFENSE,
							false);
					manager.attachRightLayout(defenseLayout);
				}
			});

			gameSelectionLayout.addView(button);

			button = new Button(activity);
			drawable = new BitmapDrawable(activity.getResources(),
					BitmapCache.getBitmap(R.drawable.menu_options_button));
			button.setBackgroundDrawable(drawable);
			button.setText(R.string.newgame_battle);
			button.setTypeface(typeFace);
			button.setTextColor(color);
			button.setTextSize(20);
			button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					BoardSelectionLayout battleLayout = new BoardSelectionLayout(manager, stats, GameInfo.BATTLE, false);
					manager.attachRightLayout(battleLayout);
				}
			});

			gameSelectionLayout.addView(button);

			button = new Button(activity);
			drawable = new BitmapDrawable(activity.getResources(),
					BitmapCache.getBitmap(R.drawable.menu_options_button));
			button.setBackgroundDrawable(drawable);
			button.setText(R.string.newgame_online);
			button.setTypeface(typeFace);
			button.setTextColor(color);
			button.setTextSize(20);
			button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (onlinePlayEnabled) {
						BoardSelectionLayout battleLayout = new BoardSelectionLayout(manager, stats, GameInfo.BATTLE,
								true);
						manager.attachRightLayout(battleLayout);
					} else {
						Toast toast = Toast.makeText(activity, activity.getText(R.string.main_latest_version),
								Toast.LENGTH_LONG);
						toast.show();
					}
				}
			});

			gameSelectionLayout.addView(button);
			ADS.placeADMobAd(gameSelectionLayout);
		}
		return gameSelectionLayout;
	}

	@Override
	public void attach(LinearLayout baseLayout) {
		baseLayout.addView(getLayout());
	}

}
