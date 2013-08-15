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

import com.godsandtowers.core.GameInfo;
import com.godsandtowers.core.HostGameEngine;
import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.core.grid.Board;
import com.godsandtowers.core.grid.Boards;
import com.godsandtowers.core.grid.Grid;
import com.godsandtowers.sprites.AIPlayer;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.sprites.Races;
import com.gundogstudios.modules.DesktopAssets;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.modules.SystemLogger;
import com.gundogstudios.modules.basic.BasicPreferenceModule;
import com.gundogstudios.modules.basic.EmptyProfilerModule;

public class Tester {
	private static final CustomMessageModule MESSAGE_MODULE = new CustomMessageModule();
	private static final BasicPreferenceModule PREFERENCE_MODULE = new BasicPreferenceModule();
	private static Player human, ai;

	public static void main(String[] args) {

		Modules.LOG = new SystemLogger();
		Modules.MESSENGER = MESSAGE_MODULE;
		Modules.PREFERENCES = PREFERENCE_MODULE;
		Modules.PROFILER = new EmptyProfilerModule();
		// PREFERENCE_MODULE.put(TDWPreferences.GAME_ENGINE_SPEED, 3);
		Modules.ASSETS = new DesktopAssets();
		GameInfo info = run();
		System.out.println(info);
	}

	public static GameInfo run() {
		Board board = Boards.ALL_BOARDS[0];

		Grid computerGrid = new Grid(board);
		human = new AIPlayer(0, new PlayerStats(100), Races.combineRaces(Races.ICE), computerGrid, true, true);

		computerGrid = new Grid(board);
		ai = new AIPlayer(1, new PlayerStats(1000), Races.combineRaces(Races.ICE), computerGrid, true, true);

		int maxWaves = 1000;
		int timeUntilNextWave = 5000;
		GameInfo info = new GameInfo(0, new Player[] { human, ai }, timeUntilNextWave, GameInfo.BATTLE, maxWaves, board);

		HostGameEngine engine = new HostGameEngine(info, null);
		MESSAGE_MODULE.addEngine(engine);

		engine.setPaused(false);
		engine.setLoading(false);
		engine.run();
		return info;
	}

}
