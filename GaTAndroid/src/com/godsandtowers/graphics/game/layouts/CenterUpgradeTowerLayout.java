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
import com.godsandtowers.sprites.Tower;
import com.godsandtowers.util.ResourceUtilities;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.util.FastMath;

public class CenterUpgradeTowerLayout extends ScrollableLayout {

	public CenterUpgradeTowerLayout(final Tower originalTower, final Player player, final Activity activity,
			final GameLayoutManager manager) {
		super(activity);
		int padding = Modules.PREFERENCES.get(TDWPreferences.PADDING, 0) / 2;

		final BaseTower tower = player.getUpgrade(originalTower.getBaseTower());

		LinearLayout[] dataLayout = new LinearLayout[2];

		dataLayout[0] = new LinearLayout(activity);
		dataLayout[0].setOrientation(LinearLayout.HORIZONTAL);
		dataLayout[0].setGravity(Gravity.CENTER_VERTICAL);
		dataLayout[0].setPadding(padding, padding, padding, padding);

		LinearLayout subLayout = new LinearLayout(activity);
		subLayout.setOrientation(LinearLayout.HORIZONTAL);
		subLayout.setGravity(Gravity.CENTER_VERTICAL);
		subLayout.setBackgroundColor(Color.argb(128, 0, 0, 0));

		ImageView image = new ImageView(activity);
		Bitmap bm = BitmapCache.getBitmap(R.drawable.bottommenu_buy);
		image.setImageBitmap(bm);
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Modules.MESSENGER.submit(LogicMessageProcessor.ID, LogicMessageProcessor.SELL_TOWER, player.getID(),
						originalTower.getX(), originalTower.getY());
				manager.setDefaults();
			}

		});

		TextView text = new TextView(activity);
		text.setTextColor(Color.WHITE);
		text.setTypeface((Typeface) Modules.PREFERENCES.get(TDWPreferences.TEXT_FONT, Typeface.DEFAULT));
		text.setTextSize(20);
		NumberFormat formatter = NumberFormat.getInstance();
		formatter.setMaximumFractionDigits(0);
		text.setText(" " + formatter.format(FastMath.ceil(originalTower.getSellValue())) + " ");

		ImageView money = new ImageView(activity);
		money.setImageBitmap(BitmapCache.getBitmap(R.drawable.bottommenu_cost));

		dataLayout[0].addView(image);
		subLayout.addView(text);
		subLayout.addView(money);
		dataLayout[0].addView(subLayout);

		dataLayout[1] = new LinearLayout(activity);
		dataLayout[1].setOrientation(LinearLayout.HORIZONTAL);
		dataLayout[1].setGravity(Gravity.CENTER_VERTICAL);
		dataLayout[1].setPadding(padding, padding, padding, padding);

		image = new ImageView(activity);
		bm = BitmapCache.getBitmap(ResourceUtilities.getIconID(tower));
		image.setImageBitmap(bm);

		image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Modules.MESSENGER.submit(LogicMessageProcessor.ID, LogicMessageProcessor.UPGRADE_TOWER, player.getID(),
						originalTower.getX(), originalTower.getY());
				manager.setDefaults();

			}
		});

		image.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				Modules.MESSENGER.submit(LogicMessageProcessor.ID, LogicMessageProcessor.MAX_UPGRADE_TOWER,
						player.getID(), originalTower.getX(), originalTower.getY());
				manager.setDefaults();
				Toast toast = Toast.makeText(activity, R.string.game_max_upgrade, Toast.LENGTH_LONG);
				toast.show();
				return true;
			}
		});

		subLayout = new LinearLayout(activity);
		subLayout.setOrientation(LinearLayout.HORIZONTAL);
		subLayout.setGravity(Gravity.CENTER_VERTICAL);
		subLayout.setBackgroundColor(Color.argb(128, 0, 0, 0));

		text = new TextView(activity);
		text.setTextColor(Color.WHITE);
		text.setTypeface((Typeface) Modules.PREFERENCES.get(TDWPreferences.TEXT_FONT, Typeface.DEFAULT));
		text.setTextSize(20);
		text.setText(" " + formatter.format(FastMath.ceil(originalTower.getCost())) + " ");

		money = new ImageView(activity);
		money.setImageBitmap(BitmapCache.getBitmap(R.drawable.bottommenu_cost));

		subLayout.addView(text);
		subLayout.addView(money);

		dataLayout[1].addView(image);
		dataLayout[1].addView(subLayout);

		super.init(dataLayout);
	}

}
