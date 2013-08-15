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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.godsandtowers.R;
import com.godsandtowers.graphics.game.BitmapCache;
import com.godsandtowers.graphics.game.GameLayoutManager;
import com.godsandtowers.messaging.LogicMessageProcessor;
import com.godsandtowers.sprites.BaseTower;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.util.ResourceUtilities;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.util.FastMath;

public class CenterTowerLayout extends ScrollableLayout {

	private Activity activity;
	private CenterLayout layout;
	private GameLayoutManager manager;

	public CenterTowerLayout(Activity activity, CenterLayout layout, GameLayoutManager manager) {
		super(activity);
		this.activity = activity;
		this.layout = layout;
		this.manager = manager;
	}

	public void setPlayer(final Player player) {
		int padding = Modules.PREFERENCES.get(TDWPreferences.PADDING, 0) / 2;
		BaseTower[] towers = player.getSortedBaseTowers();

		LinearLayout[] dataLayout = new LinearLayout[towers.length];

		Bitmap bitmap = BitmapCache.getBitmap(R.drawable.bottommenu_cost);

		int current = 0;
		for (BaseTower tower : towers) {
			final BaseTower baseTower = tower;
			dataLayout[current] = new LinearLayout(activity);
			dataLayout[current].setOrientation(LinearLayout.HORIZONTAL);
			dataLayout[current].setGravity(Gravity.CENTER_VERTICAL);
			dataLayout[current].setPadding(padding, padding, padding, padding);

			ImageView image = new ImageView(activity);
			Bitmap bm = BitmapCache.getBitmap(ResourceUtilities.getIconID(baseTower));
			image.setImageBitmap(bm);

			if (!baseTower.isUnlocked()) {
				image.setAlpha(.5f);
			}

			final BaseTower tow = tower;
			image.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (baseTower.isUnlocked()) {
						Modules.MESSENGER.submit(LogicMessageProcessor.ID, LogicMessageProcessor.BUILD_TOWER,
								player.getID(), baseTower.getName());
						layout.attachBoardLayout();
						manager.setTower(tow);
					}
				}
			});
			image.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					Toast toast = Toast.makeText(activity, ResourceUtilities.getInfo(baseTower), Toast.LENGTH_LONG);
					toast.show();
					return true;
				}
			});

			LinearLayout subLayout = new LinearLayout(activity);
			subLayout.setOrientation(LinearLayout.HORIZONTAL);
			subLayout.setGravity(Gravity.CENTER_VERTICAL);
			subLayout.setBackgroundColor(Color.argb(128, 0, 0, 0));

			TextView text = new TextView(activity);
			text.setTextColor(Color.WHITE);
			text.setTypeface((Typeface) Modules.PREFERENCES.get(TDWPreferences.TEXT_FONT, Typeface.DEFAULT));
			text.setTextSize(20);
			NumberFormat formatter = NumberFormat.getInstance();
			formatter.setMaximumFractionDigits(0);
			text.setText(" " + formatter.format(FastMath.ceil(baseTower.getCost())) + " ");

			ImageView money = new ImageView(activity);
			money.setImageBitmap(bitmap);

			if (!baseTower.isUnlocked())
				money.setAlpha(.5f);

			dataLayout[current].addView(image);
			subLayout.addView(text);
			subLayout.addView(money);
			dataLayout[current].addView(subLayout);

			current++;
		}

		super.init(dataLayout);
	}
}
