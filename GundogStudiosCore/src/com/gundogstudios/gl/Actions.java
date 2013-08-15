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
package com.gundogstudios.gl;

public class Actions {
	public static final int IDLE = 1;
	public static final int ATTACK = 2;
	public static final int MOVE = 4;
	public static final int DEATH = 8;
	public static final int ALL = IDLE | ATTACK | MOVE | DEATH;

	public static int combineActions(int... actions) {
		int value = 0;
		for (int action : actions) {
			value |= action;
		}
		return value;
	}

	public static boolean isAction(int combinedAction, int action) {
		return (combinedAction & action) - action == 0;
	}

	public static boolean isIdle(int action) {
		return (IDLE & action) > 0;
	}

	public static boolean isAttack(int action) {
		return (ATTACK & action) > 0;
	}

	public static boolean isMove(int action) {
		return (MOVE & action) > 0;
	}

	public static boolean isDeath(int action) {
		return (DEATH & action) > 0;
	}

	public static String toString(int action) {
		String text = " ";
		if ((action & IDLE) > 0)
			text += "IDLE ";
		if ((action & ATTACK) > 0)
			text += "ATTACK ";
		if ((action & MOVE) > 0)
			text += "MOVE ";
		if ((action & DEATH) > 0)
			text += "DEATH ";

		return text;
	}
}
