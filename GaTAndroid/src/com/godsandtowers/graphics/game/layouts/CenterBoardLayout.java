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
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.godsandtowers.R;
import com.godsandtowers.core.GameInfo;
import com.godsandtowers.graphics.game.BitmapCache;
import com.godsandtowers.messaging.GLMessageProcessor;
import com.gundogstudios.modules.Modules;

public class CenterBoardLayout implements ILayout {
	private Activity activity;
	private ImageView left;
	private ImageView right;
	private GameInfo gameInfo;

	public CenterBoardLayout(Activity activity, final TopLayout top) {
		this.activity = activity;

		left = new ImageView(activity);
		left.setImageBitmap(BitmapCache.getBitmap(R.drawable.button_back));
		left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.PREVIOUS_BOARD);
				top.previousPlayer();
			}
		});
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		left.setLayoutParams(params);

		right = new ImageView(activity);
		right.setImageBitmap(BitmapCache.getBitmap(R.drawable.button_forward));
		right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.NEXT_BOARD);
				top.nextPlayer();
			}
		});
		params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		right.setLayoutParams(params);
	}

	public void setPlayer(GameInfo gameInfo) {
		this.gameInfo = gameInfo;
	}

	@Override
	public void attach() {

		RelativeLayout middle = (RelativeLayout) activity.findViewById(R.id.centerLayout);
		middle.removeAllViews();

		if (gameInfo.getGameType() != GameInfo.DEFENSE) {
			middle.addView(left);
			middle.addView(right);
		}
		middle.bringToFront();

	}
}
