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
package com.godsandtowers.sprites;

import com.gundogstudios.modules.Modules;

public class IDGenerator {
	private static final int MAX_SPRITE_ID = 0x40000000;
	private static int CURRENT_SPRITE_ID = 1;

	public static int getNextID() {
		if (CURRENT_SPRITE_ID >= MAX_SPRITE_ID) {
			CURRENT_SPRITE_ID = 1;
			Modules.LOG.warn("IDGenerator", "Reached max ID count, starting again at 0");
		}

		return CURRENT_SPRITE_ID++;
	}
}
