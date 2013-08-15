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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.core.grid.Board;
import com.godsandtowers.core.grid.Boards;
import com.godsandtowers.core.grid.Grid;
import com.godsandtowers.core.grid.GridSquare;
import com.godsandtowers.sprites.AIPlayer;
import com.godsandtowers.sprites.BaseCreature;
import com.godsandtowers.sprites.BaseTower;
import com.godsandtowers.sprites.Creature;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.sprites.Races;
import com.godsandtowers.sprites.Tower;
import com.gundogstudios.modules.DesktopAssets;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.modules.SystemLogger;
import com.gundogstudios.modules.basic.BasicPreferenceModule;
import com.gundogstudios.modules.basic.EmptyProfilerModule;

public class SizeTester {
	private static final int CREATURES_SIZE = 100;

	// private static final int TOWERS_SIZE = 100;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		Modules.LOG = new SystemLogger();
		Modules.MESSENGER = new CustomMessageModule();
		Modules.PREFERENCES = new BasicPreferenceModule();
		Modules.PROFILER = new EmptyProfilerModule();
		Modules.ASSETS = new DesktopAssets();
		Board board = Boards.ALL_BOARDS[0];

		Grid grid = new Grid(board);
		Player player = new AIPlayer(0, new PlayerStats(100), Races.ALL, grid, true, true);

		BaseCreature[] baseCreatures = BaseCreature.getBaseCreatures();
		for (int i = 0; i < CREATURES_SIZE; i++) {
			Creature creature = new Creature(player, baseCreatures[i % baseCreatures.length]);
			grid.addCreature(creature);
		}
		BaseTower[] baseTowers = BaseTower.getBaseTowers();
		for (int r = 0; r < board.getRows(); r++) {
			for (int c = 0; c < board.getColumns(); c++) {
				GridSquare square = grid.getGridSquare(r, c);
				Tower tower = new Tower(baseTowers[(r * board.getColumns() + c) % baseTowers.length], player.getRace()
						.getBaseRace(), square.getX(), square.getY());
				grid.addTower(tower);
			}
		}
		ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("output/size.sav"));
		output.writeObject(player);
		output.close();
	}

}
