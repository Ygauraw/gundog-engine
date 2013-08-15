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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.godsandtowers.R;
import com.godsandtowers.campaigns.CampaignLevel;
import com.godsandtowers.core.GameInfo;
import com.godsandtowers.core.HostGameEngine;
import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.core.grid.Board;
import com.godsandtowers.core.grid.Boards;
import com.godsandtowers.core.grid.Grid;
import com.godsandtowers.graphics.game.BitmapCache;
import com.godsandtowers.messaging.ApplicationMessageProcessor;
import com.godsandtowers.sprites.AIPlayer;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.util.ADS;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.Modules;

public class CampaignLevelLayout implements ILayout {

	private LinearLayout newGameLayout;
	private int speed;
	private int difficulty;

	public CampaignLevelLayout(final Context context, final PlayerStats player, final CampaignLevel level) {

		newGameLayout = new LinearLayout(context);
		newGameLayout.setOrientation(LinearLayout.VERTICAL);

		ADS.placeObtrusiveADMobAD(newGameLayout);

		Spinner levelSpinner = generateSpinner(context, new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				setDifficulty(arg2);
				Modules.PREFERENCES.put(TDWPreferences.CAMPAIGN_DIFFICULTY, arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		}, R.string.newgame_difficulty, new int[] { R.string.newgame_easy, R.string.newgame_medium,
				R.string.newgame_hard, R.string.newgame_expert, });
		int s = Modules.PREFERENCES.get(TDWPreferences.CAMPAIGN_DIFFICULTY, 0);
		setDifficulty(s);
		levelSpinner.setSelection(s >= 4 ? 0 : s);

		newGameLayout.addView(levelSpinner);

		Spinner speedSpinner = generateSpinner(context, new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				setGameSpeed(arg2);
				Modules.PREFERENCES.put(TDWPreferences.GAME_SPEED, arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		}, R.string.newgame_speed, new int[] { R.string.newgame_slow, R.string.newgame_normal, R.string.newgame_fast, });
		s = Modules.PREFERENCES.get(TDWPreferences.GAME_SPEED, GameInfo.NORMAL);

		setGameSpeed(s);
		speedSpinner.setSelection(s >= 3 ? 0 : s);

		newGameLayout.addView(speedSpinner);

		Button startButton = new Button(context);
		BitmapDrawable drawable = new BitmapDrawable(context.getResources(),
				BitmapCache.getBitmap(R.drawable.menu_options_button));
		startButton.setBackgroundDrawable(drawable);
		startButton.setText(R.string.newgame_startGame);
		startButton.setTypeface((Typeface) Modules.PREFERENCES.get(TDWPreferences.BUTTON_FONT, Typeface.DEFAULT));
		startButton.setTextColor(Color.WHITE);
		startButton.setTextSize(20);
		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int computersLevel = difficulty;
				int races = level.getPlayerRaces();
				Board board = Boards.getBoard(level.getBoard());
				Grid humanGrid = new Grid(board);
				Player human = new Player(0, player, races, humanGrid);

				PlayerStats stats = new PlayerStats(computersLevel);
				races = level.getComputerRaces();
				Grid computerGrid = new Grid(board);

				Player computer;
				int waves;
				int gameType = level.getGameType();
				switch (gameType) {
				case GameInfo.DEFENSE:
					computer = new AIPlayer(1, stats, races, computerGrid, false, true);
					waves = GameInfo.NORMAL_WAVES;
					break;
				case GameInfo.BATTLE:
				default:
					computer = new AIPlayer(1, stats, races, computerGrid, true, true);
					waves = GameInfo.BATTLE_WAVES;
					break;
				}

				Player[] players = new Player[] { human, computer };

				GameInfo gameInfo = new GameInfo(0, players, speed, gameType, waves, level, board);

				HostGameEngine engine = new HostGameEngine(gameInfo, null);
				Modules.MESSENGER.submit(ApplicationMessageProcessor.ID, ApplicationMessageProcessor.ATTACH_GAME,
						engine);

			}
		});
		newGameLayout.addView(startButton);

		ADS.placeADMobAd(newGameLayout);
	}

	private void setDifficulty(int position) {
		switch (position) {
		case 0:
			difficulty = CampaignLevel.EASY;
			break;
		case 1:
			difficulty = CampaignLevel.MEDIUM;
			break;
		case 2:
			difficulty = CampaignLevel.HARD;
			break;
		case 3:
		default:
			difficulty = CampaignLevel.EXPERT;
			break;
		}
	}

	private void setGameSpeed(int position) {
		switch (position) {
		case 0:
			speed = GameInfo.SLOW;
			break;
		case 1:
			speed = GameInfo.NORMAL;
			break;
		case 2:
		default:
			speed = GameInfo.FAST;
			break;
		}
	}

	private Spinner generateSpinner(Context context, OnItemSelectedListener listener, int description, int[] choices) {
		String[] mStrings = new String[choices.length];
		Resources resources = context.getResources();
		for (int i = 0; i < mStrings.length; i++) {
			mStrings[i] = resources.getString(description) + ": " + resources.getString(choices[i]).toUpperCase();
		}

		Spinner speedSpinner = new Spinner(context);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, mStrings);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		speedSpinner.setAdapter(adapter);
		speedSpinner.setOnItemSelectedListener(listener);
		return speedSpinner;
	}

	@Override
	public void attach(LinearLayout baseLayout) {
		baseLayout.addView(newGameLayout);
	}

}
