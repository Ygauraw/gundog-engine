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
package com.godsandtowers.core.grid;

import java.util.HashMap;

public class Boards {
	public static final String FOREST = "forest";
	public static final String WINTER_FOREST = "winter_forest";
	public static final String DESERT = "desert";
	public static final String[] TILESETS = { FOREST, WINTER_FOREST, DESERT };
	private static final Board BASIC = new Board("basic", WINTER_FOREST, new String[][] {
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "S", "E", "E", "E", "E", "E", "E", "E", "E", "E", "F", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", }, });

	private static final Board CLOSE_CALL = new Board("close_call", FOREST, new String[][] {
			{ "E", "E", "E", "E", "E", "S", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "F", "E", "E", "E", "E", "E", }, });

	private static final Board CONFUSION = new Board("confusion", DESERT, new String[][] {
			{ "S", "E", "E", "E", "E", "T", "E", "E", "E", "E", "S", },
			{ "E", "E", "E", "E", "E", "T", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "T", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "T", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "F", "T", "F", "E", "E", "E", "E", },
			{ "T", "T", "T", "T", "T", "T", "T", "T", "T", "T", "T", },
			{ "E", "E", "E", "E", "F", "T", "F", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "T", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "T", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "T", "E", "E", "E", "E", "E", },
			{ "S", "E", "E", "E", "E", "T", "E", "E", "E", "E", "S", }, });

	private static final Board DECISIONS = new Board("decisions", FOREST, new String[][] {
			{ "S", "T", "E", "E", "E", "E", "E", "E", "E", "E", "E", "T", "S", },
			{ "E", "T", "E", "E", "E", "E", "E", "E", "E", "E", "E", "T", "E", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "R", "F", "R", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "E", "T", "E", "E", "E", "E", "E", "E", "E", "E", "E", "T", "E", },
			{ "S", "T", "E", "E", "E", "E", "E", "E", "E", "E", "E", "T", "S", }, });

	private static final Board FOUR_SQUARE = new Board("four_square", FOREST, new String[][] {
			{ "S", "E", "E", "E", "E", "E", "E", "E", "E", "E", "S", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "F", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "S", "E", "E", "E", "E", "E", "E", "E", "E", "E", "S", }, });

	private static final Board OASIS = new Board("oasis", DESERT, new String[][] {
			{ "E", "E", "E", "E", "E", "E", "E", "T", "E", "E", "F", },
			{ "E", "E", "E", "T", "E", "E", "E", "T", "E", "E", "E", },
			{ "E", "E", "E", "T", "E", "E", "E", "T", "E", "E", "E", },
			{ "E", "E", "E", "T", "E", "E", "E", "T", "E", "E", "E", },
			{ "E", "E", "E", "T", "E", "E", "E", "T", "E", "E", "E", },
			{ "E", "E", "E", "T", "E", "E", "E", "T", "E", "E", "E", },
			{ "E", "E", "E", "T", "E", "E", "E", "T", "E", "E", "E", },
			{ "E", "E", "E", "T", "E", "E", "E", "T", "E", "E", "E", },
			{ "S", "E", "E", "T", "E", "E", "E", "E", "E", "E", "E", }, });

	private static final Board OPEN_FIELD = new Board("open_field", FOREST, new String[][] {
			{ "S", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "F", },
			{ "S", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "F", },
			{ "S", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "F", },
			{ "S", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "F", },
			{ "S", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "F", }, });

	private static final Board PATH_TO_FREEDOM = new Board("path_to_freedom", WINTER_FOREST, new String[][] {
			{ "S", "E", "T", "E", "E", "E", "E", "E", "R", "E", "E", "E", "E", "E", },
			{ "E", "E", "T", "E", "E", "R", "E", "E", "R", "E", "E", "R", "E", "E", },
			{ "E", "E", "R", "E", "E", "R", "E", "E", "R", "E", "E", "R", "E", "E", },
			{ "E", "E", "R", "E", "E", "R", "E", "E", "R", "E", "E", "R", "E", "E", },
			{ "E", "E", "R", "E", "E", "R", "E", "E", "R", "E", "E", "R", "E", "E", },
			{ "E", "E", "R", "E", "E", "R", "E", "E", "R", "E", "E", "R", "E", "E", },
			{ "E", "E", "R", "E", "E", "R", "E", "E", "R", "E", "E", "R", "E", "E", },
			{ "E", "E", "R", "E", "E", "R", "E", "E", "R", "E", "E", "T", "E", "E", },
			{ "E", "E", "E", "E", "E", "R", "E", "E", "E", "E", "E", "T", "E", "F", }, });

	private static final Board ROCKY_MOUNTAIN = new Board("rocky_mountain", DESERT, new String[][] {
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "E", "R", "E", "E", "E", "R", "E", "E", "E", "R", "E", },
			{ "E", "E", "E", "R", "E", "E", "E", "R", "E", "E", "E", },
			{ "S", "R", "E", "E", "E", "R", "E", "E", "E", "R", "F", },
			{ "E", "E", "E", "R", "E", "E", "E", "R", "E", "E", "E", },
			{ "E", "R", "E", "E", "E", "R", "E", "E", "E", "R", "E", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", }, });

	private static final Board RUSH = new Board("rush", WINTER_FOREST, new String[][] {
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", }, { "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", }, { "E", "E", "E", "R", "T", "R", "E", "E", "E", },
			{ "E", "E", "E", "S", "T", "F", "E", "E", "E", }, { "E", "E", "E", "R", "T", "R", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", }, { "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", }, });

	private static final Board SIMPLE = new Board("simple", DESERT, new String[][] {
			{ "S", "R", "E", "E", "E", "E", "E", "E", "E", "T", "F", },
			{ "E", "R", "E", "E", "E", "E", "E", "E", "E", "T", "E", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "E", "R", "E", "E", "E", "E", "E", "E", "E", "T", "E", },
			{ "S", "R", "E", "E", "E", "E", "E", "E", "E", "T", "F", }, });

	private static final Board THREE_WAY = new Board("three_way", WINTER_FOREST, new String[][] {
			{ "S", "E", "E", "E", "E", "E", "E", "E", "E", "E", "T", "E", "F", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "T", "E", "E", },
			{ "T", "T", "R", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "R", "R", "R", },
			{ "S", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "F", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "R", "R", "R", },
			{ "T", "T", "R", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", },
			{ "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "T", "E", "E", },
			{ "S", "E", "E", "E", "E", "E", "E", "E", "E", "E", "T", "E", "F", }, });

	public static final Board[] ALL_BOARDS = { BASIC, CLOSE_CALL, CONFUSION, DECISIONS, FOUR_SQUARE, OASIS, OPEN_FIELD,
			PATH_TO_FREEDOM, ROCKY_MOUNTAIN, RUSH, SIMPLE, THREE_WAY, };
	private static final HashMap<String, Board> boards;
	static {
		boards = new HashMap<String, Board>();
		for (Board board : ALL_BOARDS) {
			boards.put(board.getName(), board);
		}
	}

	public static Board getBoard(String name) {
		return boards.get(name);
	}

}
