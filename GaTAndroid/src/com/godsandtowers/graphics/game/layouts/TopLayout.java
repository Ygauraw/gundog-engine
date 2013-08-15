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
package com.godsandtowers.graphics.game.layouts;

import java.text.NumberFormat;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.godsandtowers.R;
import com.godsandtowers.core.GameInfo;
import com.godsandtowers.graphics.game.BitmapCache;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.Modules;

public class TopLayout {
	private TextView lifeText;
	private TextView moneyText;
	private TextView incomeText;
	private TextView waveText;
	private TextView counterText;
	private Player[] players;
	private GameInfo gameInfo;
	private int currentPlayer = 0;
	private Activity activity;

	public TopLayout(Activity activity) {
		this.activity = activity;
		int padding = Modules.PREFERENCES.get(TDWPreferences.HEIGHT, 0) / 100;
		RelativeLayout top = (RelativeLayout) activity.findViewById(R.id.topLayout);
		top.bringToFront();
		top.setBackgroundResource(R.drawable.topmenu_background);

		LinearLayout left = (LinearLayout) activity.findViewById(R.id.topLeftLayout);

		ImageView lifeImage = new ImageView(activity);
		lifeImage.setImageBitmap(BitmapCache.getBitmap(R.drawable.topmenu_life));
		left.addView(lifeImage, 0);

		Typeface typeFace = (Typeface) Modules.PREFERENCES.get(TDWPreferences.BUTTON_FONT, Typeface.DEFAULT);

		lifeText = new TextView(activity);
		lifeText.setTypeface(typeFace);
		lifeText.setTextColor(Color.WHITE);
		lifeText.setTextSize(20);
		lifeText.setPadding(padding, padding, padding, padding);
		left.addView(lifeText, 1);

		ImageView moneyImage = new ImageView(activity);
		moneyImage.setImageBitmap(BitmapCache.getBitmap(R.drawable.topmenu_money));
		left.addView(moneyImage, 2);

		moneyText = new TextView(activity);
		moneyText.setTypeface(typeFace);
		moneyText.setTextSize(20);
		moneyText.setTextColor(Color.WHITE);
		moneyText.setPadding(padding, padding, padding, padding);
		left.addView(moneyText, 3);

		left.bringToFront();

		LinearLayout center = (LinearLayout) activity.findViewById(R.id.topMiddleLayout);

		waveText = new TextView(activity);
		waveText.setTypeface(typeFace);
		waveText.setTextSize(20);
		waveText.setTextColor(Color.WHITE);
		waveText.setPadding(padding, padding, padding, padding);
		center.addView(waveText, 0);

		center.bringToFront();

		LinearLayout right = (LinearLayout) activity.findViewById(R.id.topRightLayout);

		incomeText = new TextView(activity);
		incomeText.setTypeface(typeFace);
		incomeText.setTextSize(20);
		incomeText.setTextColor(Color.WHITE);
		incomeText.setPadding(padding, padding, padding, padding);
		right.addView(incomeText, 0);

		moneyImage = new ImageView(activity);
		moneyImage.setImageBitmap(BitmapCache.getBitmap(R.drawable.topmenu_money));
		right.addView(moneyImage, 1);

		counterText = new TextView(activity);
		counterText.setTypeface(typeFace);
		counterText.setTextSize(20);
		counterText.setTextColor(Color.WHITE);
		counterText.setPadding(padding, padding, padding, padding);
		right.addView(counterText, 2);

		right.bringToFront();
	}

	public void setPlayers(GameInfo gameInfo) {
		this.players = gameInfo.getPlayers();
		this.gameInfo = gameInfo;
		currentPlayer = gameInfo.getLocalPlayerID();
	}

	public void previousPlayer() {
		if (--currentPlayer < 0)
			currentPlayer = players.length - 1;
		showSwitch();
	}

	private void showSwitch() {
		Resources resources = activity.getResources();
		Toast.makeText(
				activity,
				(currentPlayer == gameInfo.getLocalPlayerID()) ? resources.getString(R.string.game_you) : resources
						.getString(R.string.game_enemy), Toast.LENGTH_SHORT).show();
	}

	public void nextPlayer() {
		if (++currentPlayer > players.length - 1)
			currentPlayer = 0;
		showSwitch();
	}

	public void reset() {
		currentPlayer = gameInfo.getLocalPlayerID();
		refresh();
	}

	public void refresh() {
		Resources resources = activity.getResources();
		if (players == null)
			return;
		Player player = players[currentPlayer];

		NumberFormat formatter = NumberFormat.getInstance();
		formatter.setMaximumFractionDigits(0);
		String gold = formatter.format(Math.floor(player.getGold()));
		String income = formatter.format(Math.floor(player.getIncome()));
		String life = formatter.format(player.getLife());
		lifeText.setText(life);
		moneyText.setText(gold);
		waveText.setText(resources.getString(R.string.game_wave) + " " + gameInfo.getCurrentWave());
		incomeText.setText(resources.getString(R.string.game_plus) + income + " ");
		counterText.setText(resources.getString(R.string.game_in) + " " + (gameInfo.getTimeUntilNextWave() / 1000)
				+ resources.getString(R.string.game_seconds));
	}

}
