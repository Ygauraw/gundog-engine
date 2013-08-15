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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.godsandtowers.R;
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
import com.godsandtowers.sprites.Races;
import com.godsandtowers.util.ADS;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.Modules;

public class NewGameLayout implements ILayout {

	private LinearLayout newGameLayout;
	private int speed;
	private PlayerStats player;
	private int gameType;
	private String boardName;

	public NewGameLayout(final PlayerStats player, final int gameType, String boardName) {
		this.player = player;
		this.gameType = gameType;
		this.boardName = boardName;
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

	private int getText(Context context, final EditText textBox) {
		String text = textBox.getText().toString();
		try {
			int value = Integer.parseInt(text);
			if (value > 0)
				return value;
		} catch (NumberFormatException e) {

		}
		Toast toast = Toast.makeText(context, R.string.newgame_invalid_text, Toast.LENGTH_LONG);
		toast.show();
		return Integer.MIN_VALUE;
	}

	public LinearLayout getLayout(final Context context) {
		if (newGameLayout == null) {
			Typeface font = (Typeface) Modules.PREFERENCES.get(TDWPreferences.TEXT_FONT, Typeface.DEFAULT);
			newGameLayout = new LinearLayout(context);
			newGameLayout.setOrientation(LinearLayout.VERTICAL);

			ADS.placeObtrusiveADMobAD(newGameLayout);
			TextView racesTextView = new TextView(context);
			racesTextView.setText(R.string.newgame_yourRace);
			racesTextView.setTypeface(font);
			newGameLayout.addView(racesTextView);

			final RaceSelectionGallery humanRaces = new RaceSelectionGallery(context, Races.ALL_RACES,
					new RaceSelectionGallery.BasicSelector(context, player));
			int races = Modules.PREFERENCES.get(TDWPreferences.PLAYER_RACE, 0);
			if (Races.asArray(races).length <= 1 || player.areMultipleRacesUnlocked())
				humanRaces.setSelections(races);

			newGameLayout.addView(humanRaces);

			TextView levelText = new TextView(context);
			levelText.setText(R.string.newgame_opponentsLevel);
			levelText.setTypeface(font);
			newGameLayout.addView(levelText);

			final EditText opponentsLevel = new EditText(context);
			opponentsLevel.setText("" + Modules.PREFERENCES.get(TDWPreferences.LEVEL, 1));
			opponentsLevel.setTypeface(font);
			newGameLayout.addView(opponentsLevel);

			racesTextView = new TextView(context);
			racesTextView.setText(R.string.newgame_opponentsRace);
			racesTextView.setTypeface(font);
			newGameLayout.addView(racesTextView);

			final RaceSelectionGallery computerRaces = new RaceSelectionGallery(context, Races.ALL_RACES,
					new RaceSelectionGallery.BasicSelector(context, player));
			races = Modules.PREFERENCES.get(TDWPreferences.COMPUTER_RACE, 0);
			if (Races.asArray(races).length <= 1 || player.areMultipleRacesUnlocked())
				computerRaces.setSelections(races);
			newGameLayout.addView(computerRaces);

			Spinner speedSpinner = generateSpinner(context, new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					setGameSpeed(arg2);
					Modules.PREFERENCES.put(TDWPreferences.GAME_SPEED, arg2);
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			}, R.string.newgame_speed, new int[] { R.string.newgame_slow, R.string.newgame_normal,
					R.string.newgame_fast, });
			int s = Modules.PREFERENCES.get(TDWPreferences.GAME_SPEED, GameInfo.NORMAL);
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
					Board board;

					if (boardName != null)
						board = Boards.getBoard(boardName);
					else
						board = Board.generateRandom();

					int races = Races.getRaces(humanRaces.getSelections());
					Modules.PREFERENCES.put(TDWPreferences.PLAYER_RACE, races);
					if (races == 0) {
						Toast toast = Toast.makeText(context, R.string.newgame_minHumanRaces, Toast.LENGTH_LONG);
						toast.show();
						return;
					}
					Grid humanGrid = new Grid(board);
					Player human = new Player(0, player, races, humanGrid);

					int level = getText(context, opponentsLevel);
					if (level <= 0)
						return;

					Modules.PREFERENCES.put(TDWPreferences.LEVEL, level);

					PlayerStats stats = new PlayerStats(level);

					races = Races.getRaces(computerRaces.getSelections());
					Modules.PREFERENCES.put(TDWPreferences.COMPUTER_RACE, races);
					if (races == 0) {
						Toast toast = Toast.makeText(context, R.string.newgame_minComputerRaces, Toast.LENGTH_LONG);
						toast.show();
						return;
					}
					Grid computerGrid = new Grid(board);

					Player computer;
					int waves;
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

					GameInfo gameInfo = new GameInfo(0, players, speed, gameType, waves, board);
					HostGameEngine engine = new HostGameEngine(gameInfo, null);

					Modules.MESSENGER.submit(ApplicationMessageProcessor.ID, ApplicationMessageProcessor.ATTACH_GAME,
							engine);

				}
			});
			newGameLayout.addView(startButton);
			ADS.placeADMobAd(newGameLayout);
		}
		return newGameLayout;
	}

	@Override
	public void attach(LinearLayout baseLayout) {
		baseLayout.addView(getLayout(baseLayout.getContext()));
	}

}
