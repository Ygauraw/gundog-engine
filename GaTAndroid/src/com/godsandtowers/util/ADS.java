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
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.gundogstudios.util.FastMath;

public class ADS {
	private static boolean displayObtrusiveADS = true;
	private static Activity activity;

	public static void setActivity(Activity activity) {
		ADS.activity = activity;
	}

	public static void removeObtrusiveADs() {
		displayObtrusiveADS = false;
	}

	public static void placeObtrusiveADMobAD(ViewGroup viewGroup) {
		if (displayObtrusiveADS) {
			placeADMobAd(viewGroup);
		}
	}

	public static void placeADMobAd(ViewGroup viewGroup) {
		Resources resources = activity.getResources();
		int width = FastMath.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 320,
				resources.getDisplayMetrics()));
		int height = FastMath.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50,
				resources.getDisplayMetrics()));
		LayoutParams params = new LayoutParams(width, height);
		placeADMobAd(viewGroup, params);
	}

	public static void placeADMobAd(ViewGroup viewGroup, LayoutParams params) {
		AdView adView = new AdView(activity, AdSize.BANNER, "c89f8c998cb3499c");
		AdRequest adRequest = new AdRequest();
		if (params == null)
			viewGroup.addView(adView);
		else
			viewGroup.addView(adView, params);
		adView.loadAd(adRequest);
	}
}
