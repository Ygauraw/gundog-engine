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
package com.godsandtowers.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;
import android.widget.Toast;

import com.godsandtowers.R;
import com.gundogstudios.modules.AndroidPreferences;
import com.gundogstudios.modules.Modules;

public class TDWAndroidPreferences extends AndroidPreferences implements TDWPreferences {
	private static final String TOWER_DEFENSE_WARS = "TOWERDEFENSEWARS";

	public TDWAndroidPreferences(Activity activity) {
		super(activity.getSharedPreferences(TOWER_DEFENSE_WARS, Context.MODE_PRIVATE));

		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			Modules.LOG.error("TDWAndroidPreferences", "External storage is not mounted");
			Toast toast = Toast.makeText(activity, R.string.main_external_storage, Toast.LENGTH_LONG);
			toast.show();
			activity.finish();
		}

		Typeface buttonFont = Typeface.createFromAsset(activity.getAssets(), BUTTON_FONT);
		Typeface textFont = Typeface.createFromAsset(activity.getAssets(), TEXT_FONT);
		int buttonColor = Color.rgb(201, 179, 132);
		int textColor = Color.rgb(211, 179, 136);
		int width = activity.getResources().getDisplayMetrics().widthPixels;
		int height = activity.getResources().getDisplayMetrics().heightPixels;
		// int density = activity.getResources().getDisplayMetrics().densityDpi;
		// System.out.println("Density: " + density);
		if (height > width) {
			int tmp = width;
			width = height;
			height = tmp;
		}
		if (android.os.Build.MODEL.equals("Kindle Fire")) {
			height -= 20;
			// System.out.println("Device is a Kindle Fire, removing softmenu space from height ");
		} else {
			// System.out.println("Device is not a Kindle Fire: " + android.os.Build.MODEL);
		}
		super.put(WIDTH, width);
		super.put(HEIGHT, height);
		super.put(BUTTON_FONT, buttonFont);
		super.put(TEXT_FONT, textFont);
		super.put(BUTTON_COLOR, buttonColor);
		super.put(TEXT_COLOR, textColor);
		super.put(PADDING, width / 50);
	}

}
