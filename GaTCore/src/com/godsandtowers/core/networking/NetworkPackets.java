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
package com.godsandtowers.core.networking;

public interface NetworkPackets {

	public static final int HOST_PORT = 8425;
	public static final int REMOTE_PORT = 8426;

	public static final int SNAPSHOT = 2;
	public static final int BUY_CREATURE = 3;
	public static final int BUY_TOWER = 4;
	public static final int UPGRADE_TOWER = 5;
	public static final int SELL_TOWER = 6;
	public static final int BUILD_ALL_TOWERS = 7;
	public static final int BUILD_TOWER = 8;
	public static final int CANCEL_BUILD_TOWERS = 9;
	public static final int GRID_TOUCH_COMMAND = 10;
	public static final int HEART_BEAT = 11;
	public static final int EXECUTE_SPECIAL = 12;
	public static final int LAUNCH_UPGRADE_VIEW = 13;
	public static final int RESULTS = 14;
	public static final int MAX_UPGRADE_TOWER = 15;
	public static final int AUTO_BUY_CREATURE = 16;
	public static final int CANCEL_AUTO_BUY_CREATURE = 17;
}
