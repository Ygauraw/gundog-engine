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

public interface GLMessageProcessor extends MessageProcessor {
	public static final int ID = 1;

	public static final int ADD_SPRITE = 0;
	public static final int REMOVE_SPRITE = 1;
	public static final int ADD_TRANSPARENT_SPRITE = 2;
	public static final int REMOVE_TRANSPARENT_SPRITE = 3;
	public static final int PREVIOUS_BOARD = 7;
	public static final int NEXT_BOARD = 8;
	public static final int ZOOM_IN = 9;
	public static final int ZOOM_OUT = 10;
	public static final int ROTATE_UP = 11;
	public static final int ROTATE_DOWN = 12;
	public static final int RESET_VIEW = 13;
	public static final int ADD_SPRITE_BATCH = 14;
	public static final int REMOVE_SPRITE_BATCH = 15;
	public static final int ADD_TRANSPARENT_SPRITE_BATCH = 16;
	public static final int REMOVE_TRANSPARENT_SPRITE_BATCH = 17;
	public static final int UPDATE_TRANSLATION = 18;
	public static final int SHUTDOWN = 19;
}
