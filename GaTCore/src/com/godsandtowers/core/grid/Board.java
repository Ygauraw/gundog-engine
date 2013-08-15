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

import java.io.BufferedReader;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.gundogstudios.modules.Modules;
import com.gundogstudios.util.FastMath;

public class Board implements Externalizable {
	private static final String PATH = "boards/";
	private static final String SEPERATOR = ",";
	private Square[][] squares;
	private String baseTileset;
	private String name;

	public Board() {
	}

	protected Board(String name, String baseTileset, String[][] boardText) {
		this.name = name;
		this.baseTileset = baseTileset;
		this.squares = parse(baseTileset, boardText);
	}

	public static Board generateRandom() {
		Board board;
		Grid grid;
		do {
			board = createBoard();
			grid = new Grid(board);
		} while (!grid.checkSquares());

		return board;
	}

	private static Board createBoard() {

		Board board = new Board();

		board.baseTileset = Boards.TILESETS[FastMath.random(0, Boards.TILESETS.length - 1)];

		int row = (int) (FastMath.random() * 20 + 7);
		int column = (int) (FastMath.random() * 20 + 7);

		if (row > column) {
			int tmp = row;
			row = column;
			column = tmp;
		}

		board.squares = new Square[row][column];

		for (int r = 0; r < row; r++) {
			for (int c = 0; c < column; c++) {
				board.squares[r][c] = new Square(r, c);
			}
		}

		float starts = 1;
		do {
			int c = (int) (FastMath.random() * 3);
			int r = (int) (FastMath.random() * row);
			board.squares[r][c].setStart();
		} while ((FastMath.random() < (1f / ++starts)));

		float finishes = 1;
		do {
			int c = column - 1 - (int) (FastMath.random() * 3);
			int r = (int) (FastMath.random() * row);
			board.squares[r][c].setFinish();
		} while ((FastMath.random() < (1f / ++finishes)));

		float count;

		count = 1;
		do {
			int c = (int) (FastMath.random() * column);
			int r = (int) (FastMath.random() * row);

			if (board.squares[r][c].getModel() != Square.EMPTY)
				continue;

			board.squares[r][c].setAttackRateBonus();
		} while ((FastMath.random() < (1f / ++count) * 1.5f));

		count = 1;
		do {
			int c = (int) (FastMath.random() * column);
			int r = (int) (FastMath.random() * row);

			if (board.squares[r][c].getModel() != Square.EMPTY)
				continue;

			board.squares[r][c].setDamageBonus();
		} while ((FastMath.random() < (1f / ++count) * 1.5f));

		count = 1;
		do {
			int c = (int) (FastMath.random() * column);
			int r = (int) (FastMath.random() * row);

			if (board.squares[r][c].getModel() != Square.EMPTY)
				continue;

			board.squares[r][c].setRangeBonus();
		} while ((FastMath.random() < (1f / ++count) * 1.5f));

		count = 1;
		do {
			int c = (int) (FastMath.random() * column);
			int r = (int) (FastMath.random() * row);

			if (board.squares[r][c].getModel() != Square.EMPTY)
				continue;

			board.squares[r][c].setTree(board.baseTileset);
		} while ((FastMath.random() < (1f / ++count) * 2f));

		count = 1;
		do {
			int c = (int) (FastMath.random() * column);
			int r = (int) (FastMath.random() * row);

			if (board.squares[r][c].getModel() != Square.EMPTY)
				continue;

			board.squares[r][c].setRock(board.baseTileset);
		} while ((FastMath.random() < (1f / ++count) * 2f));
		return board;
	}

	private static Square[][] parse(String baseTileset, String[][] boardText) {

		int rows = boardText.length;
		int columns = boardText[0].length;
		Square[][] squares = new Square[rows][columns];

		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				squares[r][c] = new Square(r, c);
				squares[r][c].load(baseTileset, boardText[r][c]);
			}
		}

		if (rows > columns) {
			Square[][] newSquares = new Square[columns][rows];
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < columns; c++) {
					newSquares[c][r] = squares[r][c];
					newSquares[c][r].swap();
				}
			}
		}
		return squares;

	}

	public static Board load(String boardName) {
		try {
			Board board = new Board();
			InputStream stream = Modules.ASSETS.openInput(PATH, boardName);
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

			String[] header = reader.readLine().split(SEPERATOR);
			board.baseTileset = header[0];
			int row = Integer.parseInt(header[1]);
			int column = Integer.parseInt(header[2]);

			String[][] boardText = new String[row][column];

			for (int r = 0; r < row; r++) {
				String[] line = reader.readLine().split(SEPERATOR);
				for (int c = 0; c < column; c++) {
					boardText[r][c] = line[c];
				}
			}

			board.squares = parse(board.baseTileset, boardText);
			stream.close();
			return board;
		} catch (IOException e) {
			Modules.LOG.error("Board", "Failed to load board: " + boardName + " - " + e.getMessage());
			return null;
		}
	}

	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		for (int r = 0; r < getRows(); r++) {
			output.append("\n");
			for (int c = 0; c < getColumns(); c++) {
				output.append(squares[r][c] + ",");
			}
		}

		output.append("\n");
		return output.toString();
	}

	public String getBackgroundTextureName() {
		return baseTileset + "_ground";
	}

	public String getWallTextureName() {
		return baseTileset + "_wall";
	}

	public String getName() {
		return name;
	}

	public int getRows() {
		return squares.length;
	}

	public int getColumns() {
		return squares[0].length;
	}

	public Square getSquare(int row, int column) {
		return squares[row][column];
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(baseTileset);
		out.writeObject(name);
		out.writeObject(squares);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		baseTileset = (String) in.readObject();
		name = (String) in.readObject();
		squares = (Square[][]) in.readObject();
	}
}
