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
package com.godsandtowers.graphics.game.layouts;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.godsandtowers.R;
import com.godsandtowers.graphics.game.BitmapCache;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.util.FastMath;

public class ScrollableLayout implements ILayout {
	private Activity activity;
	private HorizontalScrollView scrollView;

	public ScrollableLayout(Activity activity) {
		this.activity = activity;

	}

	protected void init(View[] data) {

		Bitmap moneyIcon = BitmapCache.getBitmap(R.drawable.icon_game);

		int imageWidth = moneyIcon.getWidth() + Modules.PREFERENCES.get(TDWPreferences.PADDING, 0);
		float imageHeight = moneyIcon.getHeight() + Modules.PREFERENCES.get(TDWPreferences.PADDING, 0);
		float height = Modules.PREFERENCES.get(TDWPreferences.HEIGHT, 0) - imageHeight * 2;
		float length = data.length;
		int rowNumber = FastMath.floor(height / imageHeight);
		int columnNumber = FastMath.ceil(length / rowNumber);

		int current = 0;

		scrollView = new HorizontalScrollView(activity);
		scrollView.setHorizontalScrollBarEnabled(false);
		scrollView.setPadding(imageWidth, 0, imageWidth, 0);
		LinearLayout horizontal = new LinearLayout(activity);
		horizontal.setOrientation(LinearLayout.HORIZONTAL);
		horizontal.setGravity(Gravity.CENTER);
		for (int i = 0; i < columnNumber; i++) {
			LinearLayout layout = new LinearLayout(activity);
			layout.setOrientation(LinearLayout.VERTICAL);
			for (int j = 0; j < rowNumber && current < data.length; j++) {
				layout.addView(data[current++]);
			}
			horizontal.addView(layout);
		}
		scrollView.addView(horizontal);
	}

	@Override
	public void attach() {
		RelativeLayout middle = (RelativeLayout) activity.findViewById(R.id.centerLayout);
		middle.removeAllViews();
		if (scrollView != null)
			middle.addView(scrollView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		middle.bringToFront();

	}

}
