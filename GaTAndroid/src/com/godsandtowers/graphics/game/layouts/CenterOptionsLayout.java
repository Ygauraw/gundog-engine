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

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.godsandtowers.R;
import com.godsandtowers.graphics.game.BitmapCache;
import com.godsandtowers.messaging.GLMessageProcessor;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.util.FastMath;

public class CenterOptionsLayout extends ScrollableLayout {

	public CenterOptionsLayout(Activity activity) {
		super(activity);

		ArrayList<ImageView> views = new ArrayList<ImageView>();

		ImageView zoomin = new ImageView(activity);
		zoomin.setImageBitmap(BitmapCache.getBitmap(R.drawable.option_zoomin));
		int padding = Modules.PREFERENCES.get(TDWPreferences.PADDING, 0);
		zoomin.setPadding(padding, padding, padding, padding);
		zoomin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.ZOOM_IN);
			}
		});
		views.add(zoomin);

		ImageView zoomout = new ImageView(activity);
		zoomout.setImageBitmap(BitmapCache.getBitmap(R.drawable.option_zoomout));
		zoomout.setPadding(padding, padding, padding, padding);
		zoomout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.ZOOM_OUT);
			}
		});
		views.add(zoomout);

		ImageView rotateup = new ImageView(activity);
		rotateup.setImageBitmap(BitmapCache.getBitmap(R.drawable.option_yrotate_cw));
		rotateup.setPadding(padding, padding, padding, padding);
		rotateup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.ROTATE_UP);
			}
		});
		views.add(rotateup);

		ImageView rotatedown = new ImageView(activity);
		rotatedown.setImageBitmap(BitmapCache.getBitmap(R.drawable.option_yrotate_ccw));
		rotatedown.setPadding(padding, padding, padding, padding);
		rotatedown.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.ROTATE_DOWN);
			}
		});
		views.add(rotatedown);

		ImageView reset = new ImageView(activity);

		reset.setImageBitmap(BitmapCache.getBitmap(R.drawable.option_resetview));
		reset.setPadding(padding, padding, padding, padding);
		reset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.RESET_VIEW);
			}
		});
		views.add(reset);

		ImageView play = new ImageView(activity);
		play.setImageBitmap(BitmapCache.getBitmap(R.drawable.option_play));
		play.setPadding(padding, padding, padding, padding);
		play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Modules.PREFERENCES.put(TDWPreferences.SOUND, true);
				int song = FastMath.round(FastMath.random(1, 10));
				Modules.AUDIO.play("" + song, true);
			}
		});
		views.add(play);

		ImageView pause = new ImageView(activity);
		pause.setImageBitmap(BitmapCache.getBitmap(R.drawable.option_pause));
		pause.setPadding(padding, padding, padding, padding);
		pause.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Modules.PREFERENCES.put(TDWPreferences.SOUND, true);
				Modules.AUDIO.pause();
			}
		});
		views.add(pause);

		super.init(views.toArray(new ImageView[views.size()]));

	}

}
