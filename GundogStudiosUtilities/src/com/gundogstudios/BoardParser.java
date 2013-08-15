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
package com.gundogstudios;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BoardParser {

	private static final String PATH = "boards/";
	private static final String SEPERATOR = ",";

	public static void main(String[] args) throws IOException {

		System.out.println("package com.godsandtowers.core.grid;");

		System.out.println("import java.util.HashMap;");

		System.out.println("public class Boards {");

		System.out.println("public static final String FOREST = \"forest\";");
		System.out.println("public static final String WINTER_FOREST = \"winter_forest\";");
		System.out.println("public static final String DESERT = \"desert\";");
		System.out.println("public static final String[] TILESETS = { FOREST, WINTER_FOREST, DESERT };");

		ArrayList<String> allBoards = new ArrayList<String>();
		for (String boardName : new File(PATH).list()) {
			InputStream stream = new FileInputStream(PATH + boardName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

			boardName = boardName.substring(0, boardName.length() - 4);
			allBoards.add(boardName.toUpperCase());

			String[] header = reader.readLine().split(SEPERATOR);
			String tileset = header[0];

			System.out.println("private static final Board " + boardName.toUpperCase() + " = new Board(\"" + boardName
					+ "\"," + tileset.toUpperCase() + ",new String[][]{");

			int row = Integer.parseInt(header[1]);
			int column = Integer.parseInt(header[2]);

			for (int r = 0; r < row; r++) {
				System.out.print("{");
				String[] line = reader.readLine().split(SEPERATOR);
				for (int c = 0; c < column; c++) {
					System.out.print("\"" + line[c] + "\",");
				}
				System.out.println("},");
			}

			System.out.println("});\n");
			stream.close();
		}

		System.out.print("public static final Board[] ALL_BOARDS = {");
		for (String board : allBoards) {
			System.out.print(board + ",");
		}
		System.out.println("};");

		System.out.println("private static final HashMap<String, Board> boards;");
		System.out.println("	static {");
		System.out.println("    boards = new HashMap<String, Board>();");

		System.out.println("	for (Board board : ALL_BOARDS) {");
		System.out.println("		boards.put(board.getName(), board);");
		System.out.println("	}");

		System.out.println("	}");

		System.out.println("	public static Board getBoard(String name) {");
		System.out.println("		return boards.get(name);");
		System.out.println("	}");

		System.out.println("\n\n}");
	}

}
