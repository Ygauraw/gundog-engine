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
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.godsandtowers.billing.PurchaseItem;
import com.godsandtowers.graphics.game.BitmapCache;
import com.godsandtowers.messaging.LogicMessageProcessor;
import com.godsandtowers.sprites.BaseSpecial;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.sprites.Race;
import com.godsandtowers.util.ResourceUtilities;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.util.FastMath;

public class CenterSpecialLayout extends ScrollableLayout {

	private Activity activity;

	public CenterSpecialLayout(final Activity activity, final Player player) {
		super(activity);
		this.activity = activity;

		final Race playerRace = player.getRace();
		BaseSpecial[] specials = player.getPlayerStats().getSpecials();

		LinearLayout[] dataLayout = new LinearLayout[specials.length];

		int padding = Modules.PREFERENCES.get(TDWPreferences.PADDING, 0) / 2;
		int current = 0;
		for (int i = 0; i < specials.length; i++) {
			dataLayout[current] = new LinearLayout(activity);
			dataLayout[current].setOrientation(LinearLayout.HORIZONTAL);
			dataLayout[current].setGravity(Gravity.CENTER_VERTICAL);
			dataLayout[current].setPadding(padding, padding, padding, padding);
			final BaseSpecial special = specials[i];

			final ImageView image = new ImageView(activity);
			Bitmap bm = BitmapCache.getBitmap(ResourceUtilities.getIconID(special.getName()));
			image.setImageBitmap(bm);
			image.setPadding(0, 0, padding, 0);

			if (!playerRace.isRaces(special.getRaces()))
				image.setAlpha(.5f);

			dataLayout[current].addView(image);

			final TextView text = new TextView(activity);
			text.setTextColor(Color.WHITE);
			text.setTypeface((Typeface) Modules.PREFERENCES.get(TDWPreferences.TEXT_FONT, Typeface.DEFAULT));
			text.setTextSize(20);
			text.setBackgroundColor(Color.argb(128, 0, 0, 0));
			final NumberFormat formatter = NumberFormat.getInstance();
			formatter.setMaximumFractionDigits(0);
			text.setText(" " + formatter.format(FastMath.ceil(special.getCount())) + " ");
			dataLayout[current].addView(text);
			image.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (special.getCount() > 0 && playerRace.isRaces(special.getRaces())) {
						Modules.MESSENGER.submit(LogicMessageProcessor.ID, LogicMessageProcessor.EXECUTE_SPECIAL,
								player.getID(), special.getName());
						text.setText(" " + formatter.format(FastMath.ceil(special.getCount() - 1)) + " ");
					} else {
						purchase();
					}
				}
			});

			image.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					Toast toast = Toast.makeText(activity,
							ResourceUtilities.getInfo(special) + " " + special.getCount(), Toast.LENGTH_LONG);
					toast.show();
					return true;
				}
			});
			current++;
		}
		super.init(dataLayout);
	}

	private void purchase() {

		AlertDialog a = new AlertDialog(activity) {

		};
		a.setTitle(R.string.purchase_special_100);
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				if (which == AlertDialog.BUTTON_POSITIVE) {
					Modules.PURCHASER.purchase(PurchaseItem.SPECIAL_100.getItemID(),
							PurchaseItem.SPECIAL_100.getPayload());
				}

			}
		};
		a.setButton(AlertDialog.BUTTON_POSITIVE, activity.getText(R.string.purchase_purchase), listener);
		a.setButton(AlertDialog.BUTTON_NEGATIVE, activity.getText(R.string.purchase_cancel), listener);
		a.show();
	}

}
