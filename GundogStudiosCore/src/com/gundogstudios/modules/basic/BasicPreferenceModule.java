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
package com.gundogstudios.modules.basic;

import java.util.HashMap;

import com.gundogstudios.modules.PreferenceModule;

public class BasicPreferenceModule implements PreferenceModule {

	private HashMap<String, Object> preferences;

	public BasicPreferenceModule() {
		preferences = new HashMap<String, Object>();
	}

	@Override
	public int get(String name, int defaultValue) {
		Object o = preferences.get(name);
		if (o == null)
			return defaultValue;
		return (Integer) o;
	}

	@Override
	public long get(String name, long defaultValue) {
		Object o = preferences.get(name);
		if (o == null)
			return defaultValue;
		return (Long) o;
	}

	@Override
	public boolean get(String name, boolean defaultValue) {
		Object o = preferences.get(name);
		if (o == null)
			return defaultValue;
		return (Boolean) o;
	}

	@Override
	public Object get(String name, Object defaultValue) {
		Object o = preferences.get(name);
		if (o == null)
			return defaultValue;
		return o;
	}

	@Override
	public void put(String name, int value) {
		preferences.put(name, value);
	}

	@Override
	public void put(String name, boolean value) {
		preferences.put(name, value);
	}

	@Override
	public void put(String name, Object value) {
		preferences.put(name, value);
	}

	@Override
	public void reset() {
		preferences.clear();
	}
}
