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

public interface ViewMessageProcessor extends MessageProcessor {
	public static final int ID = 3;

	public static final int REFRESH = 0;
	public static final int UPGRADE_TOWER = 1;
	public static final int DISPLAY_VIEW_MENU = 2;
	public static final int TURN_OFF_ATTACKING = 3;

}