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
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.godsandtowers.R;
import com.godsandtowers.graphics.game.BitmapCache;
import com.godsandtowers.util.ADS;
import com.godsandtowers.util.Constants;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.Modules;

public class NewsLayout implements ILayout {
	private LinearLayout newsLayout;
	private String[][] news;
	private Activity activity;

	public NewsLayout(Activity activity) {
		this.activity = activity;
	}

	private LinearLayout getLayout() {
		if (newsLayout == null) {
			newsLayout = new LinearLayout(activity);
			newsLayout.setOrientation(LinearLayout.VERTICAL);
			refresh();
		}
		return newsLayout;
	}

	private void refresh() {
		newsLayout.removeAllViews();

		ADS.placeObtrusiveADMobAD(newsLayout);

		Typeface font = (Typeface) Modules.PREFERENCES.get(TDWPreferences.TEXT_FONT, Typeface.DEFAULT);
		int color = Modules.PREFERENCES.get(TDWPreferences.BUTTON_COLOR, Color.WHITE);

		LinearLayout titleLayout = new LinearLayout(activity);
		titleLayout.setOrientation(LinearLayout.VERTICAL);
		titleLayout.setGravity(Gravity.CENTER);

		ImageView logo = new ImageView(activity);
		logo.setImageBitmap(BitmapCache.getBitmap(R.drawable.game_logo));
		titleLayout.addView(logo);

		TextView title = new TextView(activity);
		title.setText(activity.getResources().getText(R.string.main_version) + " " + Constants.APP_VERSION);
		title.setTypeface(font);
		title.setTextColor(Color.LTGRAY);
		title.setTextSize(10);
		title.setGravity(Gravity.CENTER);
		titleLayout.addView(title);
		newsLayout.addView(titleLayout);

		ImageView seperator = new ImageView(activity);
		seperator.setImageBitmap(BitmapCache.getBitmap(R.drawable.menu_seperator));
		newsLayout.addView(seperator);
		if (news != null) {
			for (int i = 0; i < news.length; i++) {

				LinearLayout articleLayout = new LinearLayout(activity);
				articleLayout.setOrientation(LinearLayout.VERTICAL);
				TextView newsTitle = new TextView(activity);
				newsTitle.setText(news[i][0]);
				newsTitle.setTypeface(font);
				newsTitle.setTextColor(color);
				newsTitle.setTextSize(16);
				articleLayout.addView(newsTitle);
				TextView newsText = new TextView(activity);
				newsText.setText(news[i][1]);
				newsText.setTypeface(font);
				newsText.setTextColor(color);
				newsText.setTextSize(12);
				articleLayout.addView(newsText);
				seperator = new ImageView(activity);
				seperator.setImageBitmap(BitmapCache.getBitmap(R.drawable.menu_seperator));
				articleLayout.addView(seperator);
				newsLayout.addView(articleLayout);
			}
		} else {
			LinearLayout articleLayout = new LinearLayout(activity);
			articleLayout.setOrientation(LinearLayout.VERTICAL);
			articleLayout.setBackgroundResource(R.drawable.menu_text_background);
			TextView newsTitle = new TextView(activity);
			newsTitle.setText(R.string.main_no_news);
			newsTitle.setTypeface(font);
			newsTitle.setTextColor(color);
			newsTitle.setTextSize(16);
			articleLayout.addView(newsTitle);
			TextView newsText = new TextView(activity);
			newsText.setText(R.string.main_news_server_down);
			newsText.setTypeface(font);
			newsText.setTextColor(color);
			newsText.setTextSize(12);
			articleLayout.addView(newsText);
			seperator = new ImageView(activity);
			seperator.setImageBitmap(BitmapCache.getBitmap(R.drawable.menu_seperator));
			articleLayout.addView(seperator);
			newsLayout.addView(articleLayout);
		}
		ADS.placeADMobAd(newsLayout);
	}

	public synchronized void setNews(String[][] news) {
		this.news = news;
		refresh();
	}

	@Override
	public synchronized void attach(LinearLayout baseLayout) {
		baseLayout.addView(getLayout());
	}

}
