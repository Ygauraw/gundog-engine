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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.godsandtowers.R;
import com.godsandtowers.billing.PurchaseItem;
import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.graphics.game.BitmapCache;
import com.godsandtowers.sprites.Races;
import com.godsandtowers.util.ResourceUtilities;
import com.gundogstudios.modules.Modules;

public class RaceSelectionGallery extends Gallery {
	private static final float NOT_SELECTED = .5f;
	private static final float SELECTED = 1f;
	private boolean[] selections;
	private ImageView[] images;
	private Context context;

	public RaceSelectionGallery(Context context, int[] races, final Selector selector) {
		super(context);
		this.context = context;
		this.selections = new boolean[races.length];
		int[] ids = new int[races.length];
		for (int i = 0; i < races.length; i++) {
			ids[i] = ResourceUtilities.getIconID(Races.getName(races[i]));
		}
		super.setAdapter(new ImageAdapter(ids));
		super.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selections[position] = !selections[position];

				selector.onSelection(selections);

				for (int i = 0; i < selections.length; i++) {
					images[i].setAlpha((selections[i] ? SELECTED : NOT_SELECTED));
				}
			}
		});

	}

	public void reset() {
		for (int i = 0; i < selections.length; i++) {
			selections[i] = false;
			images[i].setAlpha(NOT_SELECTED);
		}
	}

	public void setSelections(int races) {
		selections = Races.getRacesSelected(races);
		for (int i = 0; i < selections.length; i++) {
			if (selections[i])
				images[i].setAlpha(SELECTED);
			else
				images[i].setAlpha(NOT_SELECTED);
		}
	}

	public boolean[] getSelections() {
		return selections;
	}

	private class ImageAdapter extends BaseAdapter {

		public ImageAdapter(int[] imageIDs) {
			images = new ImageView[imageIDs.length];
			for (int i = 0; i < images.length; i++) {
				images[i] = new ImageView(context);
				Bitmap bitmap = BitmapCache.getBitmap(imageIDs[i]);
				int width = (int) (bitmap.getWidth() * 1.5);
				int height = bitmap.getHeight();
				images[i].setLayoutParams(new Gallery.LayoutParams(width, height));
				images[i].setImageBitmap(bitmap);
				images[i].setAlpha(NOT_SELECTED);
			}
		}

		public int getCount() {
			return images.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			return images[position];
		}

	}

	public static class BasicSelector implements Selector {
		private PlayerStats player;
		private Context context;

		public BasicSelector(Context context, PlayerStats player) {
			this.player = player;
			this.context = context;
		}

		@Override
		public void onSelection(boolean[] selections) {
			int count = 0;
			for (boolean b : selections) {
				count += (b ? 1 : 0);
			}
			if (!player.areMultipleRacesUnlocked() && count > 1) {
				for (int i = 0; i < selections.length; i++)
					selections[i] = false;
				purchase(context, PurchaseItem.MULTIPLE_RACES, R.string.purchase_multiple_races);
			}
			int races = Races.getRaces(selections);

			if (!player.areAllRacesUnlocked() && count > 0 && races - Races.ICE != 0) {
				for (int i = 0; i < selections.length; i++)
					selections[i] = false;
				purchase(context, PurchaseItem.MULTIPLE_RACES, R.string.purchase_all_races);
			}
		}

		private void purchase(Context context, final PurchaseItem purchaseItem, int id) {

			AlertDialog a = new AlertDialog(context) {

			};
			a.setTitle(id);
			DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					if (which == AlertDialog.BUTTON_POSITIVE) {
						Modules.PURCHASER.purchase(purchaseItem.getItemID(), purchaseItem.getPayload());
					}

				}
			};
			a.setButton(AlertDialog.BUTTON_POSITIVE, context.getText(R.string.purchase_purchase), listener);
			a.setButton(AlertDialog.BUTTON_NEGATIVE, context.getText(R.string.purchase_cancel), listener);
			a.show();
		}

	}

	public static interface Selector {
		public void onSelection(boolean[] selections);
	}

}
