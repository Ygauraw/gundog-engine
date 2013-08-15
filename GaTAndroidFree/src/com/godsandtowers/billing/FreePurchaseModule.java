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
package com.godsandtowers.billing;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.godsandtowers.core.PlayerStats;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.modules.PurchaseModule;

public class FreePurchaseModule implements PurchaseModule {

	private static final boolean AMAZON = true;

	private Activity activity;
	private PlayerStats stats;

	public FreePurchaseModule() {

	}

	@Override
	public void init(Object... args) {
		this.activity = (Activity) args[0];
		this.stats = (PlayerStats) args[1];
		stats.setMultipleRaces(false);
	}

	@Override
	public void restoreTransactions() {

	}

	@Override
	public void purchase(String itemId, String payload) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			if (AMAZON) {
				intent.setData(Uri
						.parse("http://www.amazon.com/gp/mas/dl/android?p=com.godsandtowers.amazon&ref=mas_pm_gods_and_towers_3d_tower_defense"));
			} else {
				intent.setData(Uri.parse("market://details?id=com.godsandtowers.google"));
			}
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			activity.startActivity(intent);
		} catch (Exception e) {
			Modules.LOG.error("FreePurchaseModule", "Failed to launch app store due to: " + e.toString());
		}
	}

	@Override
	public void purchased(String itemId, String payload) {

	}

	@Override
	public void destroy() {

	}

}
