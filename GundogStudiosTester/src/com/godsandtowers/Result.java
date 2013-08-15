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
package com.godsandtowers;

public class Result {
	public int racesOne;
	public int levelOne;
	public int racesTwo;
	public int levelTwo;
	public int wins = 0;
	public int losses = 0;
	public int draws = 0;

	public Result(int racesOne, int levelOne, int racesTwo, int levelTwo) {
		this.racesOne = racesOne;
		this.levelOne = levelOne;
		this.racesTwo = racesTwo;
		this.levelTwo = levelTwo;
	}

}
