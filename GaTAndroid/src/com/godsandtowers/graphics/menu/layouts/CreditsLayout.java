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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.godsandtowers.R;
import com.godsandtowers.util.ADS;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.Modules;

public class CreditsLayout implements ILayout {
	private LinearLayout creditsLayout;

	public CreditsLayout() {

	}

	private void addText(Context context, int job, int name, int hyperlink) {

		TextView jobView = new TextView(context);
		jobView.setText(job);
		jobView.setTextColor(Color.LTGRAY);
		jobView.setTextSize(15);
		jobView.setGravity(Gravity.CENTER);
		creditsLayout.addView(jobView);

		Resources resources = context.getResources();

		String text;
		if (hyperlink == 0) {
			text = resources.getText(name).toString();
		} else {
			text = resources.getText(name) + " - <a href=\"" + resources.getText(hyperlink) + "\">"
					+ resources.getText(R.string.credits_url) + "</a>";
		}
		Spanned html = Html.fromHtml(text);
		TextView nameView = new TextView(context);
		nameView.setText(html);
		nameView.setTypeface((Typeface) Modules.PREFERENCES.get(TDWPreferences.TEXT_FONT, Typeface.DEFAULT));
		nameView.setTextColor(Color.LTGRAY);
		nameView.setTextSize(15);
		nameView.setMovementMethod(LinkMovementMethod.getInstance());
		nameView.setGravity(Gravity.CENTER);
		creditsLayout.addView(nameView);

		TextView blankView = new TextView(context);
		blankView.setText("\n");
		blankView.setTextColor(Color.LTGRAY);
		blankView.setTextSize(15);
		blankView.setGravity(Gravity.CENTER);
		creditsLayout.addView(blankView);
	}

	private LinearLayout getLayout(Context context) {
		if (creditsLayout == null) {
			creditsLayout = new LinearLayout(context);
			creditsLayout.setOrientation(LinearLayout.VERTICAL);
			creditsLayout.setBackgroundResource(R.drawable.menu_text_background);

			ADS.placeObtrusiveADMobAD(creditsLayout);
			addText(context, R.string.credits_production, R.string.credits_production_name,
					R.string.credits_production_website);
			addText(context, R.string.credits_programming, R.string.credits_programming_name,
					R.string.credits_programming_website);
			addText(context, R.string.credits_models, R.string.credits_models_name, R.string.credits_models_website);
			addText(context, R.string.credits_pixelart, R.string.credits_pixelart_name,
					R.string.credits_pixelart_website);
			addText(context, R.string.credits_music, R.string.credits_music_name, R.string.credits_music_website);
			addText(context, R.string.credits_story, R.string.credits_story_name, R.string.credits_story_website);
			// addText(context, R.string.credits_trailer, R.string.credits_trailer_name,
			// R.string.credits_trailer_website);
			addText(context, R.string.credits_website, R.string.credits_website_name, R.string.credits_website_website);
			// addText(context, R.string.credits_translations, R.string.credits_translations_names, 0);
			addText(context, R.string.credits_special_thanks, R.string.credits_special_thanks_names, 0);

			TextView memory = new TextView(context);
			memory.setText(R.string.credits_inmemoryof);
			memory.setTypeface((Typeface) Modules.PREFERENCES.get(TDWPreferences.TEXT_FONT, Typeface.DEFAULT));
			memory.setTextColor(Color.LTGRAY);
			memory.setTextSize(15);
			memory.setMovementMethod(LinkMovementMethod.getInstance());
			memory.setGravity(Gravity.CENTER);
			creditsLayout.addView(memory);

			ADS.placeADMobAd(creditsLayout);
		}
		return creditsLayout;
	}

	@Override
	public void attach(LinearLayout baseLayout) {
		baseLayout.addView(getLayout(baseLayout.getContext()));
	}

}
