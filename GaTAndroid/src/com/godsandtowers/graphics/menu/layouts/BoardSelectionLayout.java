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
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.godsandtowers.R;
import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.core.grid.Board;
import com.godsandtowers.core.grid.Boards;
import com.godsandtowers.graphics.game.BitmapCache;
import com.godsandtowers.graphics.menu.MenuLayoutManager;
import com.godsandtowers.util.ADS;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.Modules;

public class BoardSelectionLayout implements ILayout {

	private LinearLayout boardLayout;
	private PlayerStats player;
	private MenuLayoutManager manager;
	private int gameType;
	private boolean online;

	public BoardSelectionLayout(MenuLayoutManager manager, PlayerStats player, int gameType, boolean online) {
		this.player = player;
		this.manager = manager;
		this.gameType = gameType;
		this.online = online;
	}

	public void reset() {
	}

	private LinearLayout getLayout(final Context context) {
		if (boardLayout == null) {
			boardLayout = new LinearLayout(context);
			boardLayout.setOrientation(LinearLayout.VERTICAL);
			boardLayout.setGravity(Gravity.CENTER);

			ADS.placeObtrusiveADMobAD(boardLayout);
			for (Board board : Boards.ALL_BOARDS) {

				final String boardName = board.getName();
				ImageView image = new ImageView(context);
				image.setImageBitmap(BitmapCache.getBitmap("boards/", boardName + "_icon"));
				image.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (online) {
							OnlineGameLayout layout = new OnlineGameLayout(player, boardName);
							manager.attachRightLayout(layout);
						} else {
							NewGameLayout layout = new NewGameLayout(player, gameType, boardName);
							manager.attachRightLayout(layout);
						}
					}
				});
				boardLayout.addView(image);

				ImageView seperator = new ImageView(context);
				seperator.setImageBitmap(BitmapCache.getBitmap(R.drawable.menu_seperator));
				boardLayout.addView(seperator);

			}

			Typeface typeFace = (Typeface) Modules.PREFERENCES.get(TDWPreferences.BUTTON_FONT, Typeface.DEFAULT);
			int color = Modules.PREFERENCES.get(TDWPreferences.BUTTON_COLOR, Color.WHITE);

			Button button = new Button(context);
			BitmapDrawable drawable = new BitmapDrawable(context.getResources(),
					BitmapCache.getBitmap(R.drawable.menu_options_button));
			button.setBackgroundDrawable(drawable);
			button.setText(R.string.newgame_random);
			button.setTypeface(typeFace);
			button.setTextColor(color);
			button.setTextSize(30f);
			button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (online) {
						OnlineGameLayout layout = new OnlineGameLayout(player, null);
						manager.attachRightLayout(layout);
					} else {
						NewGameLayout layout = new NewGameLayout(player, gameType, null);
						manager.attachRightLayout(layout);
					}
				}
			});
			boardLayout.addView(button);
			ADS.placeADMobAd(boardLayout);
		}
		return boardLayout;
	}

	@Override
	public void attach(LinearLayout baseLayout) {
		baseLayout.addView(getLayout(baseLayout.getContext()));
	}

}
