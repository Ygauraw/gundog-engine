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

import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.godsandtowers.MainActivity;
import com.godsandtowers.R;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.Modules;

public class AlmanacLeftLayout implements ILayout {

	private AlmanacModelView modelView;
	private MainActivity activity;

	public AlmanacLeftLayout(MainActivity activity) {
		this.activity = activity;
	}

	public void reset(LinearLayout baseLayout) {
		if (modelView != null) {
			modelView.release();
			modelView.onPause();
			baseLayout.removeView(modelView);
			modelView = null;
		}
	}

	public void setModel(String name) {
		modelView.setModel(name);
	}

	@Override
	public void attach(LinearLayout baseLayout) {
		activity.clearGL();
		ScrollView scrollLayout = (ScrollView) activity.findViewById(R.id.scrollableLeftLayout);
		scrollLayout.setBackgroundResource(0);

		this.modelView = new AlmanacModelView(activity);

		int buttonWidth = (int) (.275 * Modules.PREFERENCES.get(TDWPreferences.WIDTH, 0));
		int buttonHeight = (int) (.65 * Modules.PREFERENCES.get(TDWPreferences.HEIGHT, 0));
		modelView.setLayoutParams(new LayoutParams(buttonWidth, buttonHeight));
		// modelView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		int width = Modules.PREFERENCES.get(TDWPreferences.WIDTH, 0);
		int padding = (int) (.02 * width);
		modelView.setPadding(padding, padding, padding, padding);
		modelView.setBackgroundResource(R.drawable.menu_almanac_frame);
		baseLayout.addView(modelView, new LayoutParams(buttonWidth, buttonHeight));
		modelView.bringToFront();
		modelView.onResume();
	}

}
