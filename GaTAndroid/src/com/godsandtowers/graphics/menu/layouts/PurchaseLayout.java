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

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.godsandtowers.R;
import com.godsandtowers.billing.PurchaseItem;
import com.godsandtowers.graphics.game.BitmapCache;
import com.godsandtowers.sprites.Races;
import com.godsandtowers.util.ADS;
import com.godsandtowers.util.ResourceUtilities;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.modules.PurchaseModule;

public class PurchaseLayout implements ILayout {
	private LinearLayout purchaseLayout;

	public PurchaseLayout() {
	}

	private LinearLayout getLayout(final Context context) {
		if (purchaseLayout == null) {
			purchaseLayout = new LinearLayout(context);
			purchaseLayout.setOrientation(LinearLayout.VERTICAL);
			purchaseLayout.setBackgroundResource(R.drawable.menu_text_background);

			ADS.placeObtrusiveADMobAD(purchaseLayout);

			Typeface typeFace = (Typeface) Modules.PREFERENCES.get(TDWPreferences.BUTTON_FONT, Typeface.DEFAULT);

			for (PurchaseItem item : PurchaseItem.getPurchaseItems()) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
				LinearLayout itemLayout = (LinearLayout) inflater.inflate(R.layout.purchaselayout, null);

				TextView button = (TextView) itemLayout.findViewById(R.id.purchase_button);
				button.setTypeface(typeFace);
				button.setTextSize(20);

				TextView title = (TextView) itemLayout.findViewById(R.id.purchase_title);
				String payload = "";

				try {
					payload = item.getPayload();
					payload = (payload != null) ? " "
							+ ResourceUtilities.getString(Races.getName(Integer.parseInt(payload))) : "";
				} catch (Exception e) {

				}

				title.setText(ResourceUtilities.getString(item.getPurchaseTitle()) + payload);
				title.setTextSize(16);

				TextView desciption = (TextView) itemLayout.findViewById(R.id.purchase_description);
				desciption.setText(ResourceUtilities.getString(item.getPurchaseDescription()));
				desciption.setTextSize(12);

				final PurchaseItem purchasedItem = item;
				itemLayout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Toast toast = Toast.makeText(context,
								ResourceUtilities.getString(purchasedItem.getPurchaseTitle()), Toast.LENGTH_LONG);
						toast.show();
						PurchaseModule purchases = Modules.PURCHASER;
						purchases.purchase(purchasedItem.getItemID(), purchasedItem.getPayload());

					}
				});

				purchaseLayout.addView(itemLayout);

				ImageView seperator = new ImageView(context);
				seperator.setImageBitmap(BitmapCache.getBitmap(R.drawable.menu_seperator));
				purchaseLayout.addView(seperator);
			}
			ADS.placeADMobAd(purchaseLayout);
		}
		return purchaseLayout;
	}

	@Override
	public void attach(LinearLayout baseLayout) {
		baseLayout.addView(getLayout(baseLayout.getContext()));
	}

}
