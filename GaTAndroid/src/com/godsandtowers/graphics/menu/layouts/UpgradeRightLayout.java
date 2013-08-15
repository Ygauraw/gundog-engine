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

import java.text.NumberFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.godsandtowers.R;
import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.graphics.game.BitmapCache;
import com.godsandtowers.graphics.menu.layouts.RaceSelectionGallery.Selector;
import com.godsandtowers.sprites.BaseCreature;
import com.godsandtowers.sprites.BaseTower;
import com.godsandtowers.sprites.Races;
import com.godsandtowers.sprites.Upgradeable;
import com.godsandtowers.util.ADS;
import com.godsandtowers.util.ResourceUtilities;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.Modules;

public class UpgradeRightLayout implements ILayout {
	private static final float NOT_SELECTED = .75f;
	private static final float SELECTED = 1f;
	private LinearLayout upgradeLayout;
	private LinearLayout selectionLayout;
	private UpgradeLeftLayout leftLayout;
	private View lastView;
	private Activity activity;
	private PlayerStats stats;

	public UpgradeRightLayout(final Activity activity, UpgradeLeftLayout leftLayout, PlayerStats stats) {
		this.activity = activity;
		this.stats = stats;
		this.leftLayout = leftLayout;
	}

	public void reset() {
		upgradeLayout = new LinearLayout(activity);
		upgradeLayout.setOrientation(LinearLayout.VERTICAL);

		ADS.placeObtrusiveADMobAD(upgradeLayout);
		selectionLayout = new LinearLayout(activity);
		selectionLayout.setOrientation(LinearLayout.VERTICAL);

		Gallery gallery = generateRaceSelectionLayout();
		upgradeLayout.addView(gallery);
		ImageView seperator = new ImageView(activity);
		seperator.setImageBitmap(BitmapCache.getBitmap(R.drawable.menu_seperator));
		upgradeLayout.addView(seperator);

		upgradeLayout.addView(selectionLayout);

	}

	private Gallery generateRaceSelectionLayout() {

		Selector selector = new Selector() {

			@Override
			public void onSelection(boolean[] selections) {
				selectionLayout.removeAllViews();
				updateSelection(selections);
			}
		};
		RaceSelectionGallery gallery = new RaceSelectionGallery(activity, Races.ALL_RACES, selector);

		updateSelection(gallery.getSelections());
		return gallery;
	}

	private void updateSelection(boolean[] racesSelected) {

		int[] r = Races.asArray(Races.getRaces(racesSelected));
		if (r.length == 0) {
			// Only show player stats when no races are selected
			displayUpgrades(selectionLayout, stats.getBasePlayer(), stats.getBasePlayer().getUpgradeIDs(),
					Races.ALL_RACES);
			return;
		} else if (r.length == 1) {
			// Only show race stats when only 1 race is selected
			displayUpgrades(selectionLayout, stats.getBaseRace(), stats.getBaseRace().getUpgradeIDs(r[0]), r);

			ImageView seperator = new ImageView(activity);
			seperator.setImageBitmap(BitmapCache.getBitmap(R.drawable.menu_seperator));
			selectionLayout.addView(seperator);
		}

		ArrayList<Upgradeable> upgradeables = new ArrayList<Upgradeable>();
		ArrayList<Integer> iconIDs = new ArrayList<Integer>();
		ArrayList<int[]> races = new ArrayList<int[]>();

		for (BaseCreature creature : stats.getBaseCreatures()) {
			if (creature.isUnlocked() && Races.compareRaces(r, creature.getRaces())) {
				int iconID = ResourceUtilities.getIconID(creature);
				iconIDs.add(iconID);
				upgradeables.add(creature);
				races.add(Races.asArray(creature.getRaces()));
			}
		}
		for (BaseTower tower : stats.getBaseTowers()) {
			if (tower.isUnlocked() && Races.compareRaces(r, tower.getRaces())) {
				int iconID = ResourceUtilities.getIconID(tower);
				iconIDs.add(iconID);
				upgradeables.add(tower);
				races.add(Races.asArray(tower.getRaces()));
			}
		}

		generateLayout(selectionLayout, upgradeables, iconIDs, races);

	}

	private void generateLayout(LinearLayout wrapper, final ArrayList<Upgradeable> upgradeables,
			ArrayList<Integer> imageIDs, final ArrayList<int[]> races) {

		final LinearLayout upgrades = new LinearLayout(activity);
		upgrades.setOrientation(LinearLayout.VERTICAL);

		Gallery gallery = new Gallery(activity);
		gallery.setAdapter(new ImageAdapter(imageIDs));
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if (lastView != null) {
					((ImageView) lastView).setAlpha(NOT_SELECTED);
				}
				upgrades.removeAllViews();

				if (view != lastView) {
					((ImageView) view).setAlpha(SELECTED);
					Upgradeable upgradeable = upgradeables.get(position);
					int[] r = races.get(position);
					displayUpgrades(upgrades, upgradeable, upgradeable.getUpgradeIDs(), r);

					ADS.placeADMobAd(upgrades);

					lastView = view;
				} else {
					lastView = null;
				}
			}
		});
		wrapper.addView(gallery);

		ImageView seperator = new ImageView(activity);
		seperator.setImageBitmap(BitmapCache.getBitmap(R.drawable.menu_seperator));
		wrapper.addView(seperator);
		wrapper.addView(upgrades);

	}

	private void displayUpgrades(LinearLayout layout, final Upgradeable upgradable, int[] upgradeableIDs,
			final int[] races) {
		int heightPadding = Modules.PREFERENCES.get(TDWPreferences.HEIGHT, 0) / 20;
		int widthPadding = Modules.PREFERENCES.get(TDWPreferences.WIDTH, 0) / 25;
		final Resources resources = activity.getResources();
		for (int nameID : upgradeableIDs) {
			RelativeLayout insideLayout = new RelativeLayout(activity);
			insideLayout.setBackgroundResource(R.drawable.menu_upgrade_button);
			insideLayout.setPadding(0, heightPadding, 0, heightPadding);
			insideLayout.setGravity(Gravity.CENTER);

			ImageView image = new ImageView(activity);
			final String name = upgradable.getUpgradeName(nameID);
			int iconID = ResourceUtilities.getIconID(name);
			image.setImageBitmap(BitmapCache.getBitmap(iconID));
			image.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					Toast.makeText(activity, ResourceUtilities.getString(name), Toast.LENGTH_SHORT).show();
					return true;
				}
			});
			image.setPadding(widthPadding, 0, 0, 0);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			insideLayout.addView(image, params);

			final int id = nameID;
			final TextView nameView = new TextView(activity);
			nameView.setText(getText(resources, upgradable, id));
			nameView.setPadding(widthPadding, 0, widthPadding, 0);
			params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			insideLayout.addView(nameView, params);

			ImageView purchase = new ImageView(activity);
			purchase.setImageBitmap(BitmapCache.getBitmap(R.drawable.bottommenu_upgrade));

			purchase.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					long cost = upgradable.getUpgradeCost(id);
					boolean hasEnoughXP = true;

					for (int race : races) {
						hasEnoughXP &= stats.getRaceXP(race) >= cost;
						if (!hasEnoughXP) {
							CharSequence text = activity.getResources().getText(R.string.upgrade_notEnoughXP);
							Toast toast = Toast.makeText(
									activity,
									text + " " + ResourceUtilities.getString(Races.getName(race)) + " "
											+ stats.getRaceXP(race) + " / " + cost, Toast.LENGTH_SHORT);
							toast.show();
						}
					}
					if (hasEnoughXP) {

						for (int race : races)
							stats.decreaseXP(race, cost);

						upgradable.upgrade(id);
						leftLayout.refresh();
						nameView.setText(getText(resources, upgradable, id));
					}
				}
			});
			purchase.setPadding(0, 0, widthPadding, 0);
			params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			insideLayout.addView(purchase, params);

			layout.addView(insideLayout);
		}

	}

	private String getText(Resources resources, Upgradeable upgradable, int id) {
		NumberFormat formatter = NumberFormat.getInstance();
		formatter.setMaximumFractionDigits(2);
		String base = formatter.format(upgradable.getBaseValue(id));
		String upgraded = formatter.format(upgradable.getUpgradedValue(id));
		String cost = formatter.format(upgradable.getUpgradeCost(id));
		return base + " --> " + upgraded + "\n" + cost + " " + resources.getText(R.string.upgrade_XP);
	}

	@Override
	public void attach(LinearLayout baseLayout) {
		if (upgradeLayout == null) {
			reset();
		}
		baseLayout.addView(upgradeLayout);
	}

	private class ImageAdapter extends BaseAdapter {

		private ImageView[] images;

		public ImageAdapter(ArrayList<Integer> imageIDs) {
			images = new ImageView[imageIDs.size()];
			for (int i = 0; i < images.length; i++) {
				images[i] = new ImageView(activity);
				Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), imageIDs.get(i));

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

}
