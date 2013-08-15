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
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.godsandtowers.core.GameEngine;
import com.godsandtowers.core.GameInfo;
import com.godsandtowers.core.HostGameEngine;
import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.core.grid.Board;
import com.godsandtowers.core.grid.Boards;
import com.godsandtowers.core.grid.Grid;
import com.godsandtowers.graphics.GameSurface;
import com.godsandtowers.messaging.GLHandler;
import com.godsandtowers.messaging.GLMessageProcessor;
import com.godsandtowers.messaging.LogicHandler;
import com.godsandtowers.messaging.LogicMessageProcessor;
import com.godsandtowers.messaging.ViewMessageProcessor;
import com.godsandtowers.sprites.AIPlayer;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.sprites.Races;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.DesktopAssets;
import com.gundogstudios.modules.DesktopGL11;
import com.gundogstudios.modules.DesktopGLUtils;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.modules.basic.BasicMessageModule;
import com.gundogstudios.modules.basic.BasicNetworkModule;
import com.gundogstudios.modules.basic.BasicPreferenceModule;
import com.gundogstudios.modules.basic.BasicProfilerModule;
import com.gundogstudios.modules.basic.SystemLogger;

public class Launcher {
	private static int width = 640;
	private static int height = 360;

	private static final BasicMessageModule MESSAGE_MODULE = new BasicMessageModule();

	public static void main(String[] args) throws LWJGLException, FileNotFoundException, IOException {

		Modules.MESSENGER = MESSAGE_MODULE;
		Modules.LOG = new SystemLogger();
		Modules.PREFERENCES = new BasicPreferenceModule();
		Modules.ASSETS = new DesktopAssets();
		Modules.PROFILER = new BasicProfilerModule();
		Modules.GL = new DesktopGL11();
		Modules.GLUTIL = new DesktopGLUtils();
		Modules.NETWORKING = new BasicNetworkModule();
		Modules.PREFERENCES.put(TDWPreferences.GAME_ENGINE_SPEED, GameEngine.FAST);

		Display.setLocation((Display.getDisplayMode().getWidth() - width) / 2,
				(Display.getDisplayMode().getHeight() - height) / 2);
		Display.setDisplayMode(new DisplayMode(width, height));
		Display.setTitle("Tower Defense Wars");
		Display.create();

		// Board board = Board.load(Boards.getBoards()[1].getName());
		Board board = Boards.ALL_BOARDS[0];

		Grid computerGrid = new Grid(board);
		AIPlayer human = new AIPlayer(0, new PlayerStats(1000), Races.ALL, computerGrid, true, true);

		computerGrid = new Grid(board);
		AIPlayer ai = new AIPlayer(1, new PlayerStats(1000), Races.ALL, computerGrid, true, true);

		Player[] players = new Player[] { human, ai };

		int maxWaves = 1000;
		int timeUntilNextWave = 5000;
		GameInfo info = new GameInfo(0, players, timeUntilNextWave, GameInfo.BATTLE, maxWaves, board);

		GameSurface surface = new GameSurface(info);

		HostGameEngine engine = new HostGameEngine(info, null);
		engine.setPaused(false);
		engine.setLoading(false);
		Modules.MESSENGER.register(LogicMessageProcessor.ID, new LogicHandler(engine));
		GLHandler glHandler = new GLHandler(surface);
		Modules.MESSENGER.register(GLMessageProcessor.ID, glHandler);
		Modules.MESSENGER.register(ViewMessageProcessor.ID, new ViewMessageProcessor() {

			@Override
			public void process(int what, Object[] object) {
			}
		});

		surface.onSurfaceCreated();
		surface.onSurfaceChanged(width, height);

		ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();

		newSingleThreadExecutor.execute(engine);

		while (!Display.isCloseRequested()) {

			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.NEXT_BOARD);
			if (Keyboard.isKeyDown(Keyboard.KEY_Q))
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.ZOOM_IN);
			if (Keyboard.isKeyDown(Keyboard.KEY_E))
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.ZOOM_OUT);
			if (Keyboard.isKeyDown(Keyboard.KEY_C))
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.ROTATE_UP);
			if (Keyboard.isKeyDown(Keyboard.KEY_Z))
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.ROTATE_DOWN);
			if (Keyboard.isKeyDown(Keyboard.KEY_D))
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.UPDATE_TRANSLATION, -2f, 0f, 0f);
			if (Keyboard.isKeyDown(Keyboard.KEY_A))
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.UPDATE_TRANSLATION, 2f, 0f, 0f);
			if (Keyboard.isKeyDown(Keyboard.KEY_S))
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.UPDATE_TRANSLATION, 0f, -2f, 0f);
			if (Keyboard.isKeyDown(Keyboard.KEY_W))
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.UPDATE_TRANSLATION, 0f, 2f, 0f);

			surface.onDrawFrame();
			Display.update();
			try {
				Thread.sleep(33);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		newSingleThreadExecutor.shutdownNow();
	}

}
