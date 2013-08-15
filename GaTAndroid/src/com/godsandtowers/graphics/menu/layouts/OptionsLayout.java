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
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

import com.godsandtowers.R;
import com.godsandtowers.core.GameEngine;
import com.godsandtowers.core.HostGameEngine;
import com.godsandtowers.graphics.game.BitmapCache;
import com.godsandtowers.messaging.ApplicationMessageProcessor;
import com.godsandtowers.util.ADS;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.gl.ModelUtils;
import com.gundogstudios.modules.Modules;

public class OptionsLayout implements ILayout {
	private LinearLayout optionsLayout;

	public OptionsLayout() {

	}

	private void flipSoundState(Button button) {
		boolean soundState = Modules.PREFERENCES.get(TDWPreferences.SOUND, true);
		if (soundState) {
			button.setText(R.string.options_soundOff);
			Modules.PREFERENCES.put(TDWPreferences.SOUND, false);
			Modules.AUDIO.pause();
		} else {
			button.setText(R.string.options_soundOn);
			Modules.PREFERENCES.put(TDWPreferences.SOUND, true);
			Modules.AUDIO.play("theme", true);
		}
	}

	private int getNextQuality(int quality) {
		switch (quality) {
		case ModelUtils.HIGH_QUALITY:
			return ModelUtils.LOW_QUALITY;
		case ModelUtils.MEDIUM_QUALITY:
			return ModelUtils.HIGH_QUALITY;
		case ModelUtils.LOW_QUALITY:
		default:
			return ModelUtils.MEDIUM_QUALITY;
		}
	}

	private String getQualityString(Context context, int type, int level) {
		Resources resources = context.getResources();
		String base = resources.getString(type) + ": ";
		switch (level) {
		case ModelUtils.HIGH_QUALITY:
			return base + resources.getString(R.string.options_high).toUpperCase();
		case ModelUtils.MEDIUM_QUALITY:
			return base + resources.getString(R.string.options_medium).toUpperCase();
		case ModelUtils.LOW_QUALITY:
		default:
			return base + resources.getString(R.string.options_low).toUpperCase();
		}
	}

	private int getNextSpeed(int speed) {
		switch (speed) {
		case HostGameEngine.FAST:
			return HostGameEngine.SLOW;
		case HostGameEngine.NORMAL:
			return HostGameEngine.FAST;
		case HostGameEngine.SLOW:
		default:
			return HostGameEngine.NORMAL;
		}
	}

	private String getSpeedString(Context context, int type, int level) {
		Resources resources = context.getResources();
		String base = resources.getString(type) + ": ";
		switch (level) {
		case HostGameEngine.FAST:
			return base + resources.getString(R.string.options_fast).toUpperCase();
		case HostGameEngine.NORMAL:
			return base + resources.getString(R.string.options_normal).toUpperCase();
		case HostGameEngine.SLOW:
		default:
			return base + resources.getString(R.string.options_slow).toUpperCase();
		}
	}

	private LinearLayout getLayout(final Context context) {
		if (optionsLayout == null) {

			optionsLayout = new LinearLayout(context);
			optionsLayout.setOrientation(LinearLayout.VERTICAL);

			ADS.placeObtrusiveADMobAD(optionsLayout);
			Typeface typeFace = (Typeface) Modules.PREFERENCES.get(TDWPreferences.BUTTON_FONT, Typeface.DEFAULT);
			int color = Modules.PREFERENCES.get(TDWPreferences.BUTTON_COLOR, Color.GRAY);

			Button button = new Button(context);
			BitmapDrawable drawable = new BitmapDrawable(context.getResources(),
					BitmapCache.getBitmap(R.drawable.menu_options_button));
			button.setBackgroundDrawable(drawable);
			button.setText(R.string.options_reset);
			button.setTypeface(typeFace);
			button.setTextColor(color);
			button.setTextSize(20);
			button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Modules.MESSENGER.submit(ApplicationMessageProcessor.ID, ApplicationMessageProcessor.RESET);
				}
			});

			optionsLayout.addView(button);

			button = new Button(context);
			drawable = new BitmapDrawable(context.getResources(), BitmapCache.getBitmap(R.drawable.menu_options_button));
			button.setBackgroundDrawable(drawable);
			button.setText(R.string.options_assets);
			button.setTypeface(typeFace);
			button.setTextColor(color);
			button.setTextSize(20);
			button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Modules.MESSENGER.submit(ApplicationMessageProcessor.ID, ApplicationMessageProcessor.CLEAR_ASSETS);
				}
			});

			optionsLayout.addView(button);

			final Button sound = new Button(context);
			drawable = new BitmapDrawable(context.getResources(), BitmapCache.getBitmap(R.drawable.menu_options_button));
			sound.setBackgroundDrawable(drawable);
			sound.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			if (Modules.PREFERENCES.get(TDWPreferences.SOUND, true)) {
				sound.setText(R.string.options_soundOn);
			} else {
				sound.setText(R.string.options_soundOff);
			}

			sound.setTypeface(typeFace);
			sound.setTextColor(color);
			sound.setTextSize(20);
			sound.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					flipSoundState(sound);
				}
			});
			optionsLayout.addView(sound);

			final Button textureButton = new Button(context);
			drawable = new BitmapDrawable(context.getResources(), BitmapCache.getBitmap(R.drawable.menu_options_button));
			textureButton.setBackgroundDrawable(drawable);
			textureButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			textureButton.setText(getQualityString(context, R.string.options_textureQuality,
					ModelUtils.getTextureQuality()));
			textureButton.setTypeface(typeFace);
			textureButton.setTextColor(color);
			textureButton.setTextSize(20);
			textureButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int quality = getNextQuality(ModelUtils.getTextureQuality());
					Modules.PREFERENCES.put(ModelUtils.TEXTURE_QUALITY, quality);
					textureButton.setText(getQualityString(context, R.string.options_textureQuality, quality));
				}
			});

			optionsLayout.addView(textureButton);

			final Button meshButton = new Button(context);
			drawable = new BitmapDrawable(context.getResources(), BitmapCache.getBitmap(R.drawable.menu_options_button));
			meshButton.setBackgroundDrawable(drawable);
			meshButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			meshButton.setText(getQualityString(context, R.string.options_meshQuality, ModelUtils.getMeshQuality()));
			meshButton.setTypeface(typeFace);
			meshButton.setTextColor(color);
			meshButton.setTextSize(20);
			meshButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int quality = getNextQuality(ModelUtils.getMeshQuality());
					Modules.PREFERENCES.put(ModelUtils.MESH_QUALITY, quality);
					meshButton.setText(getQualityString(context, R.string.options_meshQuality, quality));
				}
			});

			optionsLayout.addView(meshButton);

			final Button logicTickIntervalButton = new Button(context);
			drawable = new BitmapDrawable(context.getResources(), BitmapCache.getBitmap(R.drawable.menu_options_button));
			logicTickIntervalButton.setBackgroundDrawable(drawable);
			logicTickIntervalButton.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			logicTickIntervalButton.setText(getSpeedString(context, R.string.options_engineSpeed,
					Modules.PREFERENCES.get(TDWPreferences.GAME_ENGINE_SPEED, GameEngine.FAST)));
			logicTickIntervalButton.setTypeface(typeFace);
			logicTickIntervalButton.setTextColor(color);
			logicTickIntervalButton.setTextSize(20);
			logicTickIntervalButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int speed = getNextSpeed(Modules.PREFERENCES.get(TDWPreferences.GAME_ENGINE_SPEED, GameEngine.FAST));
					Modules.PREFERENCES.put(TDWPreferences.GAME_ENGINE_SPEED, speed);
					logicTickIntervalButton.setText(getSpeedString(context, R.string.options_engineSpeed, speed));
				}
			});

			optionsLayout.addView(logicTickIntervalButton);
			ADS.placeADMobAd(optionsLayout);
		}
		return optionsLayout;
	}

	@Override
	public void attach(LinearLayout baseLayout) {
		baseLayout.addView(getLayout(baseLayout.getContext()));
	}

}
