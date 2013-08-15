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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.godsandtowers.R;
import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.graphics.game.BitmapCache;
import com.godsandtowers.sprites.BaseCreature;
import com.godsandtowers.sprites.BaseTower;
import com.godsandtowers.sprites.Races;
import com.godsandtowers.util.ADS;
import com.godsandtowers.util.ResourceUtilities;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.util.FastMath;

public class AlmanacRightLayout implements ILayout {
	private static final String STORY = "story";
	private static final float NOT_SELECTED = .75f;
	private static final float SELECTED = 1f;
	private LinearLayout upgradeLayout;
	private LinearLayout selectionLayout;
	private AlmanacLeftLayout leftLayout;
	private Activity activity;
	private PlayerStats stats;
	private View lastView;

	public AlmanacRightLayout(Activity activity, AlmanacLeftLayout leftLayout, PlayerStats stats) {
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
		selectionLayout.setBackgroundResource(R.drawable.menu_text_background);

		updateSelection();
		selectionLayout.addView(getTextView(STORY));
		upgradeLayout.addView(selectionLayout);
	}

	private void updateSelection() {

		ArrayList<BaseCreature> creatures = new ArrayList<BaseCreature>();

		for (BaseCreature creature : stats.getBaseCreatures()) {
			if (creature.isUnlocked()) {
				creatures.add(creature);
			}
		}
		Collections.sort(creatures, new Comparator<BaseCreature>() {

			@Override
			public int compare(BaseCreature lhs, BaseCreature rhs) {
				int race = lhs.getRaces() - rhs.getRaces();
				if (race == 0)
					return FastMath.round(lhs.getCost() - rhs.getCost());
				else
					return race;
			}
		});

		ArrayList<BaseTower> towers = new ArrayList<BaseTower>();

		for (BaseTower tower : stats.getBaseTowers()) {
			if (tower.isUnlocked()) {
				towers.add(tower);
			}
		}

		Collections.sort(towers, new Comparator<BaseTower>() {

			@Override
			public int compare(BaseTower lhs, BaseTower rhs) {
				int race = lhs.getRaces() - rhs.getRaces();
				if (race == 0)
					return FastMath.round(lhs.getCost() - rhs.getCost());
				else
					return race;
			}
		});

		ArrayList<Info> bios = new ArrayList<Info>();

		for (int race : Races.ALL_RACES) {
			bios.add(new RaceInfo(Races.getName(race)));
		}

		for (BaseCreature baseCreature : creatures) {
			bios.add(new CreatureInfo(baseCreature));
		}

		for (BaseTower baseTower : towers) {
			bios.add(new TowerInfo(baseTower));
		}

		generateLayout(upgradeLayout, bios);

	}

	private void generateLayout(LinearLayout wrapper, final ArrayList<Info> bios) {

		Gallery gallery = new Gallery(activity);
		gallery.setAdapter(new ImageAdapter(bios));
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if (lastView != null) {
					((ImageView) lastView).setAlpha(NOT_SELECTED);
				}

				selectionLayout.removeAllViews();
				leftLayout.setModel(null);

				if (view != lastView) {
					((ImageView) view).setAlpha(SELECTED);
					Info info = bios.get(position);
					String name = info.getName();

					if (info.hasModel()) {
						leftLayout.setModel(name);
					}

					selectionLayout.addView(getTextView(name));
					selectionLayout.addView(getRaceView(info.getRace()));

					ADS.placeADMobAd(selectionLayout);
					lastView = view;
				} else {
					lastView = null;
					selectionLayout.addView(getTextView(STORY));
				}
			}
		});
		wrapper.addView(gallery);

		ImageView seperator = new ImageView(activity);
		seperator.setImageBitmap(BitmapCache.getBitmap(R.drawable.menu_seperator));
		wrapper.addView(seperator);

	}

	private LinearLayout getRaceView(int races) {

		LinearLayout layout = new LinearLayout(activity);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setGravity(Gravity.CENTER);

		for (int race : Races.asArray(races)) {
			ImageView image = new ImageView(activity);
			Bitmap bitmap = BitmapCache.getBitmap(ResourceUtilities.getIconID(Races.getName(race)));
			image.setImageBitmap(bitmap);
			layout.addView(image);
		}
		return layout;
	}

	private TextView getTextView(String name) {
		TextView jobView = new TextView(activity);
		jobView.setText(ResourceUtilities.getString("almanac_" + name));
		jobView.setTypeface((Typeface) Modules.PREFERENCES.get(TDWPreferences.TEXT_FONT, Typeface.DEFAULT));
		jobView.setTextColor(Color.LTGRAY);
		jobView.setTextSize(15);
		// jobView.setGravity(Gravity.CENTER);
		return jobView;
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

		public ImageAdapter(ArrayList<Info> imageIDs) {
			images = new ImageView[imageIDs.size()];
			for (int i = 0; i < images.length; i++) {
				images[i] = new ImageView(activity);

				Bitmap bitmap = BitmapCache.getBitmap(imageIDs.get(i).getIconID());

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

	private class RaceInfo implements Info {
		private String race;

		public RaceInfo(String race) {
			this.race = race;
		}

		@Override
		public String getName() {
			return race;
		}

		@Override
		public int getIconID() {
			return ResourceUtilities.getIconID(race);
		}

		@Override
		public boolean hasModel() {
			return false;
		}

		@Override
		public int getRace() {
			return Races.getRaces(race);
		}
	}

	private class TowerInfo implements Info {
		private BaseTower tower;

		public TowerInfo(BaseTower creature) {
			this.tower = creature;
		}

		@Override
		public String getName() {
			return tower.getName();
		}

		@Override
		public int getIconID() {
			return ResourceUtilities.getIconID(tower);
		}

		@Override
		public boolean hasModel() {
			return true;
		}

		@Override
		public int getRace() {
			return tower.getRaces();
		}
	}

	private class CreatureInfo implements Info {
		private BaseCreature creature;

		public CreatureInfo(BaseCreature creature) {
			this.creature = creature;
		}

		@Override
		public String getName() {
			return creature.getName();
		}

		@Override
		public int getIconID() {
			return ResourceUtilities.getIconID(creature);
		}

		@Override
		public boolean hasModel() {
			return true;
		}

		@Override
		public int getRace() {
			return creature.getRaces();
		}

	}

	private interface Info {
		public String getName();

		public int getIconID();

		public boolean hasModel();

		public int getRace();
	}

}
