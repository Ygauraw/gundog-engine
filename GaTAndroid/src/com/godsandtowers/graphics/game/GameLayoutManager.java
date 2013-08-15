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
package com.godsandtowers.graphics.game;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.godsandtowers.R;
import com.godsandtowers.core.GameInfo;
import com.godsandtowers.graphics.game.layouts.BottomLayout;
import com.godsandtowers.graphics.game.layouts.CenterLayout;
import com.godsandtowers.graphics.game.layouts.TopLayout;
import com.godsandtowers.messaging.LogicMessageProcessor;
import com.godsandtowers.messaging.ViewHandler;
import com.godsandtowers.sprites.BaseCreature;
import com.godsandtowers.sprites.BaseTower;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.Modules;

public class GameLayoutManager {
	private TopLayout top;
	private CenterLayout center;
	private BottomLayout bottom;
	private GameView gameView;
	private Activity activity;
	private GameInfo gameInfo;

	public GameLayoutManager(Activity activity) {
		this.activity = activity;
		top = new TopLayout(activity);
		center = new CenterLayout(activity, this, top);
		bottom = new BottomLayout(activity, center);

		Modules.MESSENGER.register(ViewHandler.ID, new ViewHandler(activity, top, center, bottom));
	}

	public void setPlayers(GameInfo gameInfo) {
		clearGL();

		this.gameInfo = gameInfo;

		top.setPlayers(gameInfo);
		center.setPlayers(gameInfo);
		bottom.setPlayers(gameInfo);
	}

	public void setTower(BaseTower tower) {
		bottom.setTower(tower);
	}

	public void setCreature(BaseCreature creature) {
		bottom.setCreature(creature);
	}

	public void setDefaults() {
		center.attachBoardLayout();
		bottom.attachDefaultLayout();
	}

	public void onPause() {
		if (gameView != null)
			clearGL();

		bottom.onPause();
	}

	public void clearGL() {
		// this happens when user opens the almanac during a paused game. need to free up the opengl context
		if (gameView != null) {
			gameView.onResume();
			gameView.release();
			gameView.onPause();
			RelativeLayout main = (RelativeLayout) activity.findViewById(R.id.gameLayout);
			main.removeView(gameView);
			gameView = null;
		}
	}

	public void onResume() {
		if (gameView != null) {
			gameView.onResume();
		} else if (gameInfo != null) {

			// this happens when user opens the almanac during a paused game. need to free up the opengl context
			gameView = new GameView(activity, gameInfo);
			RelativeLayout main = (RelativeLayout) activity.findViewById(R.id.gameLayout);
			main.addView(gameView, 0);

			gameView.requestRender();
			// glView.setVisibility(View.INVISIBLE);

			final RelativeLayout topLayout = (RelativeLayout) activity.findViewById(R.id.topLayout);
			topLayout.setVisibility(View.INVISIBLE);

			final RelativeLayout bottomLayout = (RelativeLayout) activity.findViewById(R.id.bottomLayout);
			bottomLayout.setVisibility(View.INVISIBLE);

			final RelativeLayout centerLayout = (RelativeLayout) activity.findViewById(R.id.centerLayout);
			centerLayout.setVisibility(View.INVISIBLE);

			RelativeLayout gameLayout = (RelativeLayout) activity.findViewById(R.id.gameLayout);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);

			final TextView text = new TextView(activity);
			text.setTypeface((Typeface) Modules.PREFERENCES.get(TDWPreferences.TEXT_FONT, Typeface.DEFAULT));
			text.setTextSize(60);
			text.setGravity(Gravity.CENTER);
			text.setText(R.string.motto);
			text.setTextColor(Modules.PREFERENCES.get(TDWPreferences.TEXT_COLOR, Color.GRAY));

			text.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					text.setClickable(false);
					text.setVisibility(View.GONE);
					// glView.setVisibility(View.VISIBLE);
					topLayout.setVisibility(View.VISIBLE);
					bottomLayout.setVisibility(View.VISIBLE);
					centerLayout.setVisibility(View.VISIBLE);

					top.reset();
					bottom.onResume();
					gameView.requestRender();

					Modules.MESSENGER.submit(LogicMessageProcessor.ID, LogicMessageProcessor.PLAY);
				}
			});

			gameLayout.addView(text, params);
		} else {
			Modules.LOG.error("GameLayoutManager", "RESUMING GAME WITH NO PLAYERS");
			return;
		}
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (gameView != null)
			return gameView.onKeyUp(keyCode, event);
		else
			return false;
	}

}
