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

import java.util.ArrayList;
import java.util.concurrent.Callable;

import com.godsandtowers.core.GameInfo;
import com.godsandtowers.core.HostGameEngine;
import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.core.grid.Board;
import com.godsandtowers.core.grid.Boards;
import com.godsandtowers.core.grid.Grid;
import com.godsandtowers.core.grid.GridSquare;
import com.godsandtowers.sprites.AIPlayer;
import com.godsandtowers.sprites.Player;

public class GameCallable implements Callable<GameInfo> {
	private static final int MAX_WAVES = 10000;
	private static Board board;
	private static ArrayList<GridSquare> buildOrder;

	static {
		board = Boards.ALL_BOARDS[0];
		Grid grid = new Grid(board);
		buildOrder = grid.calculateBuildSquares();
	}

	private CustomMessageModule handler;
	private PlayerStats baseStats;
	private int racesOne;
	private int levelOne;
	private int racesTwo;
	private int levelTwo;

	public GameCallable(CustomMessageModule handler, int racesOne, int levelOne, int racesTwo, int levelTwo) {
		this(handler, null, racesOne, levelOne, racesTwo, levelTwo);
	}

	public GameCallable(CustomMessageModule handler, PlayerStats baseStats, int racesOne, int levelOne, int racesTwo,
			int levelTwo) {
		this.handler = handler;
		this.baseStats = baseStats;
		this.racesOne = racesOne;
		this.levelOne = levelOne;
		this.racesTwo = racesTwo;
		this.levelTwo = levelTwo;
	}

	@Override
	public GameInfo call() throws Exception {
		Grid gridOne = new Grid(board);
		PlayerStats oneStats = (baseStats == null) ? new PlayerStats(levelOne) : new PlayerStats(levelOne, baseStats);
		AIPlayer playerOne = new AIPlayer(0, oneStats, racesOne, gridOne, true, true, buildOrder);

		Grid gridTwo = new Grid(board);
		PlayerStats twoStats = (baseStats == null) ? new PlayerStats(levelTwo) : new PlayerStats(levelTwo, baseStats);
		AIPlayer playerTwo = new AIPlayer(1, twoStats, racesTwo, gridTwo, true, true, buildOrder);

		int timeUntilNextWave = 5000;
		GameInfo info = new GameInfo(0, new Player[] { playerOne, playerTwo }, timeUntilNextWave, GameInfo.BATTLE,
				MAX_WAVES, board);

		HostGameEngine engine = new HostGameEngine(info, null);

		int id = handler.addEngine(engine);
		playerOne.setGameID(id);
		playerTwo.setGameID(id);

		engine.setPaused(false);
		engine.setLoading(false);

		engine.run();
		handler.removeEngine(id);

		return info;
	}
}