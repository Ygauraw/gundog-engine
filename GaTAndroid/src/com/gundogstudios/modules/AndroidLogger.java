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

import android.util.Log;

public class AndroidLogger implements LogModule {

	public AndroidLogger() {

	}

	@Override
	public void debug(String tag, String message) {
		Log.d(tag, message);
	}

	@Override
	public void error(String tag, String message) {
		Log.e(tag, message);
	}

	@Override
	public void info(String tag, String message) {
		Log.i(tag, message);
	}

	@Override
	public void verbose(String tag, String message) {
		Log.v(tag, message);
	}

	@Override
	public void warn(String tag, String message) {
		Log.w(tag, message);
	}

}
