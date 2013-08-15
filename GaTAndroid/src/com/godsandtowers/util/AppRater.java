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

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.godsandtowers.R;
import com.gundogstudios.modules.Modules;

public class AppRater {

	private final static int DAYS_UNTIL_PROMPT = 3;
	private final static int LAUNCHES_UNTIL_PROMPT = 7;
	private final static long ONE_DAY = 24l * 60l * 60l * 1000l;

	public static void launchRating(final Context context, final String URI) {
		boolean rated = Modules.PREFERENCES.get(TDWPreferences.RATED, false);

		if (rated == true) {
			return;
		}

		int launches = Modules.PREFERENCES.get(TDWPreferences.LAUNCHES, 0);

		Modules.PREFERENCES.put(TDWPreferences.LAUNCHES, launches + 1);

		long firstLaunch = Modules.PREFERENCES.get(TDWPreferences.FIRST_LAUNCH, 0);

		if (firstLaunch == 0) {
			firstLaunch = System.currentTimeMillis();
			Modules.PREFERENCES.put(TDWPreferences.FIRST_LAUNCH, firstLaunch);
		}

		if (launches < LAUNCHES_UNTIL_PROMPT
				|| System.currentTimeMillis() < (firstLaunch + DAYS_UNTIL_PROMPT * ONE_DAY)) {
			return;
		}

		final Dialog dialog = new Dialog(context);
		Resources resources = context.getResources();

		String appName = resources.getText(R.string.app_name).toString();
		String rateDialog = String.format(resources.getText(R.string.main_rate_dialog).toString(), appName);
		String rateNow = String.format(resources.getText(R.string.main_rate_now).toString(), appName);
		String rateLater = resources.getText(R.string.main_rate_later).toString();
		String rateNever = resources.getText(R.string.main_rate_never).toString();

		dialog.setTitle(rateNow);

		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);

		TextView tv = new TextView(context);
		tv.setText(rateDialog);
		tv.setWidth(240);
		tv.setPadding(4, 0, 4, 10);
		layout.addView(tv);

		Button b1 = new Button(context);
		b1.setText(rateNow);
		b1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Modules.PREFERENCES.put(TDWPreferences.RATED, true);
				context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URI)));
				dialog.dismiss();
			}
		});
		layout.addView(b1);

		Button b2 = new Button(context);
		b2.setText(rateLater);
		b2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Modules.PREFERENCES.put(TDWPreferences.LAUNCHES, 0);
				dialog.dismiss();
			}
		});
		layout.addView(b2);

		Button b3 = new Button(context);
		b3.setText(rateNever);
		b3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Modules.PREFERENCES.put(TDWPreferences.RATED, true);
				dialog.dismiss();
			}
		});
		layout.addView(b3);

		dialog.setContentView(layout);
		dialog.show();
	}
}
