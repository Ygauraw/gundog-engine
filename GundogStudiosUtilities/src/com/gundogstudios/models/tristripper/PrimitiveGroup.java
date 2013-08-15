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
package com.gundogstudios.models.tristripper;

public class PrimitiveGroup {

	public static final int PT_LIST = 0;
	public static final int PT_STRIP = 1;
	public static final int PT_FAN = 2;

	public int type;
	public int[] indices;
	public int numIndices;

	public PrimitiveGroup() {
		type = PT_STRIP;
	}

	public String getTypeString() {
		switch (type) {
		case PT_LIST:
			return "list";
		case PT_STRIP:
			return "strip";
		case PT_FAN:
			return "fan";
		default:
			return "????";
		}
	}

	public String toString() {
		return getTypeString() + " : " + numIndices;
	}

	public String getFullInfo() {
		if (type != PT_STRIP)
			return toString();

		int[] stripLengths = new int[numIndices];

		int prev = -1;
		int length = -1;
		for (int i = 0; i < numIndices; i++) {
			if (indices[i] == prev) {
				stripLengths[length]++;
				length = -1;
				prev = -1;
			} else {
				prev = indices[i];
				length++;
			}
		}
		stripLengths[length]++;

		StringBuffer sb = new StringBuffer();
		sb.append("Strip:").append(numIndices).append("\n");
		for (int i = 0; i < stripLengths.length; i++) {
			if (stripLengths[i] > 0) {
				sb.append(i).append("->").append(stripLengths[i]).append("\n");
			}
		}
		return sb.toString();
	}

}
