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
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.godsandtowers.R;
import com.godsandtowers.graphics.game.BitmapCache;
import com.godsandtowers.messaging.LogicMessageProcessor;
import com.godsandtowers.sprites.BaseCreature;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.Modules;

public class BottomCreatureLayout implements ILayout {
	private Activity activity;
	private ImageView cancelImage;
	private Player player;

	public BottomCreatureLayout(Activity activity, final BottomLayout bottomLayout, final CenterLayout centerLayout) {
		this.activity = activity;
		int padding = Modules.PREFERENCES.get(TDWPreferences.PADDING, 0) / 2;
		cancelImage = new ImageView(activity);
		cancelImage.setImageBitmap(BitmapCache.getBitmap(R.drawable.bottommenu_cancel));
		cancelImage.setPadding(padding, 0, padding, 0);
		cancelImage.setClickable(true);
		cancelImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Modules.MESSENGER.submit(LogicMessageProcessor.ID, LogicMessageProcessor.CANCEL_BUILD_TOWERS,
						player.getID());
				bottomLayout.attachDefaultLayout();
				centerLayout.attachBoardLayout();
			}
		});
	}

	@Override
	public void attach() {
		RelativeLayout center = (RelativeLayout) activity.findViewById(R.id.bottomLayout);
		center.bringToFront();

		LinearLayout left = (LinearLayout) activity.findViewById(R.id.bottomLeftLayout);
		left.setGravity(Gravity.CENTER_VERTICAL);
		left.removeAllViews();

		left.addView(cancelImage);
		left.bringToFront();

		LinearLayout right = (LinearLayout) activity.findViewById(R.id.bottomRightLayout);
		right.removeAllViews();
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setCreature(BaseCreature creature) {
		float textSize = 20f;
		int backgroundColor = Color.argb(128, 0, 0, 0);
		int widthPadding = Modules.PREFERENCES.get(TDWPreferences.WIDTH, 0) / 50;
		NumberFormat formatter = NumberFormat.getInstance();
		formatter.setMaximumFractionDigits(2);
		LinearLayout right = (LinearLayout) activity.findViewById(R.id.bottomRightLayout);
		right.setGravity(Gravity.CENTER);
		right.removeAllViews();

		ImageView image = new ImageView(activity);
		image.setImageBitmap(BitmapCache.getBitmap(R.drawable.icon_stat_health));
		right.addView(image);

		TextView text = new TextView(activity);
		text.setTextSize(textSize);
		text.setPadding(widthPadding, 0, widthPadding, 0);
		text.setText(formatter.format(creature.getHealth()));
		text.setBackgroundColor(backgroundColor);
		right.addView(text);

		image = new ImageView(activity);
		image.setImageBitmap(BitmapCache.getBitmap(R.drawable.icon_stat_speed));
		right.addView(image);

		text = new TextView(activity);
		text.setTextSize(textSize);
		text.setPadding(widthPadding, 0, widthPadding, 0);
		text.setText(formatter.format(creature.getSpeed()));
		text.setBackgroundColor(backgroundColor);
		right.addView(text);

	}

}
