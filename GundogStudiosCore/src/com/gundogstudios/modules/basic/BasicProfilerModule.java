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

import com.gundogstudios.modules.Modules;
import com.gundogstudios.modules.ProfilerModule;

public class BasicProfilerModule implements ProfilerModule {

	private static final String TAG = "Profiler";

	private static final int FPS_REFRESH_RATE = 1000;

	private long previousLogicFPSTime = 0;

	private long previousRenderFPSTime = 0;

	private int logicFPS = 0;

	private int renderFPS = 0;

	public BasicProfilerModule() {

	}

	@Override
	public void updateRenderFPS() {
		renderFPS++;
		long currentTime = System.currentTimeMillis();

		if (currentTime - previousRenderFPSTime >= FPS_REFRESH_RATE) {
			Modules.LOG.info(TAG, "R.FPS " + renderFPS);
			renderFPS = 0;
			previousRenderFPSTime = currentTime;
		}

	}

	@Override
	public void updateLogicFPS() {
		logicFPS++;
		long currentTime = System.currentTimeMillis();

		if (currentTime - previousLogicFPSTime >= FPS_REFRESH_RATE) {
			long free = Runtime.getRuntime().freeMemory() / 1024;
			long total = Runtime.getRuntime().totalMemory() / 1024;
			long max = Runtime.getRuntime().maxMemory() / 1048576;
			Modules.LOG.info(TAG, "L.FPS " + logicFPS + " Ram: f" + free + "KB t" + total + "KB m" + max + "MB");
			logicFPS = 0;
			previousLogicFPSTime = currentTime;
		}
	}

}
