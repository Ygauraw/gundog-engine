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
package com.gundogstudios.modules;

import java.util.HashMap;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AndroidPreferences implements PreferenceModule {

	private HashMap<String, Object> tempPreferences;
	private SharedPreferences preferences;

	public AndroidPreferences(SharedPreferences preferences) {
		this.preferences = preferences;
		this.tempPreferences = new HashMap<String, Object>();
	}

	@Override
	public int get(String name, int defaultValue) {
		return preferences.getInt(name, defaultValue);
	}

	@Override
	public long get(String name, long defaultValue) {
		return preferences.getLong(name, defaultValue);
	}

	@Override
	public boolean get(String name, boolean defaultValue) {
		return preferences.getBoolean(name, defaultValue);
	}

	@Override
	public Object get(String name, Object defaultValue) {
		Object o = tempPreferences.get(name);
		if (o == null)
			return defaultValue;
		return o;
	}

	@Override
	public void put(String name, int value) {
		Editor editor = preferences.edit();
		editor.putInt(name, value);
		editor.commit();
	}

	@Override
	public void put(String name, boolean value) {
		Editor editor = preferences.edit();
		editor.putBoolean(name, value);
		editor.commit();
	}

	@Override
	public void put(String name, Object value) {
		tempPreferences.put(name, value);
	}

	@Override
	public void reset() {
		tempPreferences.clear();
		preferences.edit().clear().commit();
	}

}
