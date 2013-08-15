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
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.godsandtowers.R;
import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.sprites.Races;
import com.godsandtowers.util.ResourceUtilities;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.Modules;

public class UpgradeLeftLayout implements ILayout {

	private int padding;
	private LinearLayout upgradeLayout;

	private TextView[] levelText;
	private Activity activity;
	private PlayerStats stats;

	public UpgradeLeftLayout(final Activity activity, PlayerStats stats) {
		this.activity = activity;
		this.stats = stats;
		this.padding = Modules.PREFERENCES.get(TDWPreferences.HEIGHT, 0) / 100;
	}

	public void reset() {
		upgradeLayout = new LinearLayout(activity);
		upgradeLayout.setOrientation(LinearLayout.VERTICAL);
		upgradeLayout.setGravity(Gravity.CENTER);
		int[] races = Races.ALL_RACES;
		levelText = new TextView[races.length];
		for (int i = 0; i < races.length; i++) {
			levelText[i] = generateRaceLayouts(races[i]);
		}
	}

	private TextView generateRaceLayouts(int race) {

		LinearLayout raceLayout = new LinearLayout(activity);
		raceLayout.setOrientation(LinearLayout.HORIZONTAL);

		raceLayout.setPadding(padding, padding, padding, padding);
		raceLayout.setOrientation(LinearLayout.HORIZONTAL);
		raceLayout.setGravity(Gravity.CENTER);

		ImageView image = new ImageView(activity);
		int imageID = ResourceUtilities.getIconID(Races.getName(race));
		image.setImageResource(imageID);
		raceLayout.addView(image);

		TextView levelText = new TextView(activity);
		levelText.setText("   " + stats.getRaceXP(race));
		raceLayout.addView(levelText);
		upgradeLayout.addView(raceLayout);
		return levelText;
	}

	@Override
	public void attach(LinearLayout baseLayout) {
		if (upgradeLayout == null)
			reset();
		baseLayout.addView(upgradeLayout);
		ScrollView scrollLayout = (ScrollView) activity.findViewById(R.id.scrollableLeftLayout);
		scrollLayout.setBackgroundResource(0);
	}

	public void refresh() {
		if (upgradeLayout == null)
			reset();
		int[] races = Races.ALL_RACES;
		for (int i = 0; i < races.length; i++) {
			levelText[i].setText("   " + stats.getRaceXP(races[i]));
		}
	}

}
