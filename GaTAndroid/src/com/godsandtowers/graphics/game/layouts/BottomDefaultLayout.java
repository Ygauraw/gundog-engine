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

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.godsandtowers.R;
import com.godsandtowers.core.GameInfo;
import com.godsandtowers.graphics.game.BitmapCache;
import com.godsandtowers.messaging.LogicMessageProcessor;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.Modules;

public class BottomDefaultLayout implements ILayout {
	private Activity activity;
	private ImageView towerImage;
	private ImageView creatureImage;
	private ImageView specialImage;
	private ImageView optionsImage;
	private ImageView playImage;
	private ImageView attackImage;
	private boolean paused = true;
	private boolean attack = false;

	public BottomDefaultLayout(Activity activity, final BottomLayout bottomLayout, final CenterLayout centerLayout) {
		this.activity = activity;

		int padding = Modules.PREFERENCES.get(TDWPreferences.PADDING, 0) / 2;
		towerImage = new ImageView(activity);
		towerImage.setImageBitmap(BitmapCache.getBitmap(R.drawable.bottommenu_towers));
		towerImage.setPadding(padding, 0, padding, 0);
		towerImage.setClickable(true);
		towerImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				centerLayout.attachTowerLayout();
				bottomLayout.attachTowerLayout();
			}
		});

		creatureImage = new ImageView(activity);
		creatureImage.setImageBitmap(BitmapCache.getBitmap(R.drawable.bottommenu_creatures));
		creatureImage.setPadding(padding, 0, padding, 0);
		creatureImage.setClickable(true);
		creatureImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				centerLayout.attachCreatureLayout();
				bottomLayout.attachCreatureLayout();
			}
		});

		specialImage = new ImageView(activity);
		specialImage.setImageBitmap(BitmapCache.getBitmap(R.drawable.bottommenu_specials));
		specialImage.setPadding(padding, 0, padding, 0);
		specialImage.setClickable(true);
		specialImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				centerLayout.attachSpecialLayout();
				bottomLayout.attachCancelLayout();
			}
		});

		optionsImage = new ImageView(activity);
		optionsImage.setImageBitmap(BitmapCache.getBitmap(R.drawable.bottommenu_options));
		optionsImage.setPadding(padding, 0, padding, 0);
		optionsImage.setClickable(true);
		optionsImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				centerLayout.attachOptionsLayout();
				bottomLayout.attachCancelLayout();
			}
		});

		attackImage = new ImageView(activity);
		attackImage.setImageBitmap(BitmapCache.getBitmap(R.drawable.bottommenu_attack));
		attackImage.setPadding(padding, 0, padding, 0);
		attackImage.setClickable(true);

		playImage = new ImageView(activity);
		playImage.setImageBitmap(BitmapCache.getBitmap(R.drawable.bottommenu_play));
		playImage.setPadding(padding, 0, padding, 0);
		playImage.setClickable(true);
		playImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!paused) {
					playImage.setImageBitmap(BitmapCache.getBitmap(R.drawable.bottommenu_play));
					Modules.MESSENGER.submit(LogicMessageProcessor.ID, LogicMessageProcessor.PAUSE);
				} else {
					playImage.setImageBitmap(BitmapCache.getBitmap(R.drawable.bottommenu_pause));
					Modules.MESSENGER.submit(LogicMessageProcessor.ID, LogicMessageProcessor.PLAY);
				}
				paused = !paused;
			}
		});
	}

	public void setPlayers(final GameInfo gameInfo) {
		final Player[] players = gameInfo.getPlayers();
		attackImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				attack = !attack;
				if (attack) {
					attackImage.setImageBitmap(BitmapCache.getBitmap(R.drawable.bottommenu_move));
				} else {
					attackImage.setImageBitmap(BitmapCache.getBitmap(R.drawable.bottommenu_attack));
				}
				players[gameInfo.getLocalPlayerID()].setAttacking(attack);
			}
		});

		if (gameInfo.getGameType() == GameInfo.DEFENSE) {
			creatureImage.setVisibility(View.GONE);
			attackImage.setVisibility(View.GONE);
		} else {
			creatureImage.setVisibility(View.VISIBLE);
			attackImage.setVisibility(View.VISIBLE);

		}
	}

	public void setAttacking(boolean attacking) {
		attack = attacking;
		if (attack) {
			attackImage.setImageBitmap(BitmapCache.getBitmap(R.drawable.bottommenu_move));
		} else {
			attackImage.setImageBitmap(BitmapCache.getBitmap(R.drawable.bottommenu_attack));
		}
	}

	@Override
	public void attach() {

		RelativeLayout center = (RelativeLayout) activity.findViewById(R.id.bottomLayout);
		center.bringToFront();

		LinearLayout left = (LinearLayout) activity.findViewById(R.id.bottomLeftLayout);
		left.removeAllViews();

		left.addView(towerImage);
		left.addView(creatureImage);
		left.addView(specialImage);
		left.bringToFront();

		LinearLayout right = (LinearLayout) activity.findViewById(R.id.bottomRightLayout);
		right.removeAllViews();

		right.addView(optionsImage);
		right.addView(attackImage);
		right.addView(playImage);
		right.bringToFront();
	}

	public void onResume() {
		paused = false;
		playImage.setImageBitmap(BitmapCache.getBitmap(R.drawable.bottommenu_pause));
	}

	public void onPause() {
		paused = true;
		playImage.setImageBitmap(BitmapCache.getBitmap(R.drawable.bottommenu_play));
	}

}
