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
package com.godsandtowers.messaging;

import com.gundogstudios.modules.MessageModule.MessageProcessor;

public interface LogicMessageProcessor extends MessageProcessor {

	public static final int ID = 2;

	public static final int BUY_TOWER = 0;
	public static final int PAUSE = 1;
	public static final int PLAY = 2;
	public static final int BUY_CREATURE = 3;
	public static final int UPGRADE_TOWER = 4;
	public static final int SELL_TOWER = 5;
	public static final int GL_FINISHED_LOADING = 6;
	public static final int EXECUTE_SPECIAL = 7;
	public static final int BUILD_TOWER = 13;
	public static final int BUILD_ALL_TOWERS = 14;
	public static final int CANCEL_BUILD_TOWERS = 15;
	public static final int GRID_TOUCHED = 16;
	public static final int ATTACKING = 17;
	public static final int LAUNCH_UPGRADE_VIEW = 18;
	public static final int PROCESS_RESULTS = 19;
	public static final int AUTO_BUY_CREATURE = 20;
	public static final int MAX_UPGRADE_TOWER = 21;
	public static final int CANCEL_AUTO_BUY_CREATURE = 22;

}