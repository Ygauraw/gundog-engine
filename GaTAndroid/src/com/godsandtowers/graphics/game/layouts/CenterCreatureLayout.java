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
import com.godsandtowers.sprites.BaseCreature;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.util.ResourceUtilities;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.util.FastMath;

public class CenterCreatureLayout extends ScrollableLayout {

	private Activity activity;
	private Player player;
	private int level;
	private GameLayoutManager manager;

	public CenterCreatureLayout(Activity activity, GameLayoutManager manager) {
		super(activity);
		this.manager = manager;
		this.activity = activity;
		level = 1;
	}

	public void setPlayer(final Player player) {
		this.player = player;
		BaseCreature[] creatures = player.getSortedBaseCreatures();

		LinearLayout[] dataLayout = new LinearLayout[creatures.length];

		Bitmap bitmap = BitmapCache.getBitmap(R.drawable.bottommenu_cost);

		int padding = Modules.PREFERENCES.get(TDWPreferences.PADDING, 0) / 2;
		int current = 0;
		for (BaseCreature creature : creatures) {
			final BaseCreature baseCreature = creature;
			dataLayout[current] = new LinearLayout(activity);
			dataLayout[current].setOrientation(LinearLayout.HORIZONTAL);
			dataLayout[current].setGravity(Gravity.CENTER_VERTICAL);
			dataLayout[current].setPadding(padding, padding, padding, padding);

			ImageView image = new ImageView(activity);
			Bitmap bm = BitmapCache.getBitmap(ResourceUtilities.getIconID(baseCreature));
			image.setImageBitmap(bm);

			if (!baseCreature.isUnlocked())
				image.setAlpha(.5f);

			final BaseCreature cre = creature;
			image.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (baseCreature.isUnlocked()) {
						Modules.MESSENGER.submit(LogicMessageProcessor.ID, LogicMessageProcessor.BUY_CREATURE,
								player.getID(), baseCreature.getName());
						manager.setCreature(cre);

					}

					if (level != player.getCreatureLevel()) {
						attach();
					}
				}
			});

			final LinearLayout subLayout = new LinearLayout(activity);
			subLayout.setOrientation(LinearLayout.HORIZONTAL);
			subLayout.setGravity(Gravity.CENTER_VERTICAL);

			final int autoBuyColor = Color.argb(128, 0, 128, 0);
			final int regularColor = Color.argb(128, 0, 0, 0);
			if (player.getAutoBuyCreatureName() != null
					&& player.getAutoBuyCreatureName().equals(baseCreature.getName())) {
				subLayout.setBackgroundColor(autoBuyColor);
			} else {
				subLayout.setBackgroundColor(regularColor);

			}

			TextView text = new TextView(activity);
			text.setTextColor(Color.WHITE);
			text.setTypeface((Typeface) Modules.PREFERENCES.get(TDWPreferences.TEXT_FONT, Typeface.DEFAULT));
			text.setTextSize(20);
			NumberFormat formatter = NumberFormat.getInstance();
			formatter.setMaximumFractionDigits(0);
			text.setText(" " + formatter.format(FastMath.ceil(baseCreature.getCost())) + " ");

			ImageView money = new ImageView(activity);
			money.setImageBitmap(bitmap);

			if (!creature.isUnlocked())
				money.setAlpha(.5f);

			dataLayout[current].addView(image);
			subLayout.addView(text);
			subLayout.addView(money);
			dataLayout[current].addView(subLayout);

			image.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					if (player.getAutoBuyCreatureName() != null
							&& player.getAutoBuyCreatureName().equals(baseCreature.getName())) {
						if (autoBuyLayout != null)
							autoBuyLayout.setBackgroundColor(regularColor);
						Modules.MESSENGER.submit(LogicMessageProcessor.ID,
								LogicMessageProcessor.CANCEL_AUTO_BUY_CREATURE, player.getID());
						manager.setCreature(cre);
						Toast toast = Toast.makeText(activity, R.string.game_disable_auto_purchase, Toast.LENGTH_LONG);
						toast.show();
					} else if (baseCreature.isUnlocked()) {
						if (autoBuyLayout != null)
							autoBuyLayout.setBackgroundColor(regularColor);
						autoBuyLayout = subLayout;
						autoBuyLayout.setBackgroundColor(autoBuyColor);
						Modules.MESSENGER.submit(LogicMessageProcessor.ID, LogicMessageProcessor.AUTO_BUY_CREATURE,
								player.getID(), baseCreature.getName());
						manager.setCreature(cre);
						Toast toast = Toast.makeText(activity, R.string.game_enable_auto_purchase, Toast.LENGTH_LONG);
						toast.show();
					}
					if (level != player.getCreatureLevel()) {
						attach();
					}
					return true;
				}
			});

			current++;
		}
		super.init(dataLayout);
	}

	private LinearLayout autoBuyLayout;

	@Override
	public void attach() {
		if (level != player.getCreatureLevel()) {
			level = player.getCreatureLevel();
			Toast.makeText(activity, activity.getResources().getText(R.string.game_creature_level) + " " + level,
					Toast.LENGTH_SHORT).show();
			setPlayer(player);
		}
		super.attach();
	}
}
