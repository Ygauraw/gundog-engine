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

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.restlet.resource.ResourceException;

import com.godsandtowers.core.GameEngine;
import com.godsandtowers.core.GameInfo;
import com.godsandtowers.core.HostGameEngine;
import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.core.RemoteGameEngine;
import com.godsandtowers.core.grid.Board;
import com.godsandtowers.core.grid.Boards;
import com.godsandtowers.core.grid.Grid;
import com.godsandtowers.core.networking.ClientInfo;
import com.godsandtowers.core.networking.DeviceInfo;
import com.godsandtowers.core.networking.HostNetworkManager;
import com.godsandtowers.core.networking.NetworkGameInfo;
import com.godsandtowers.graphics.GameSurface;
import com.godsandtowers.messaging.GLHandler;
import com.godsandtowers.messaging.GLMessageProcessor;
import com.godsandtowers.messaging.LogicHandler;
import com.godsandtowers.messaging.LogicMessageProcessor;
import com.godsandtowers.sprites.AIPlayer;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.sprites.Races;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.gl.ModelUtils;
import com.gundogstudios.modules.DesktopAssets;
import com.gundogstudios.modules.DesktopGL11;
import com.gundogstudios.modules.DesktopGLUtils;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.modules.basic.BasicMessageModule;
import com.gundogstudios.modules.basic.BasicNetworkModule;
import com.gundogstudios.modules.basic.BasicPreferenceModule;
import com.gundogstudios.modules.basic.BasicProfilerModule;
import com.gundogstudios.modules.basic.SystemLogger;

public class FindGameLauncher {
	private static int width = 640;
	private static int height = 360;

	private static final BasicMessageModule MESSAGE_MODULE = new BasicMessageModule();
	private static GameInfo info;
	private static Player[] players;

	private static void initGame(boolean isServer) {
		Board board = Boards.ALL_BOARDS[0];

		Player playerOne;
		Player playerTwo;

		if (isServer) {
			Grid grid = new Grid(board);
			playerOne = new AIPlayer(0, new PlayerStats(1000), Races.ALL, grid, true, true);

			grid = new Grid(board);
			playerTwo = new Player(1, new PlayerStats(1000), Races.ALL, grid);
		} else {
			Grid grid = new Grid(board);
			playerOne = new Player(0, new PlayerStats(1000), Races.ALL, grid);

			grid = new Grid(board);
			playerTwo = new AIPlayer(1, new PlayerStats(1000), Races.ALL, grid, true, true);
		}
		players = new Player[] { playerOne, playerTwo };

		int maxWaves = 1000;
		int timeUntilNextWave = 5000;
		info = new GameInfo((isServer) ? 0 : 1, players, timeUntilNextWave, GameInfo.BATTLE, maxWaves, board);

	}

	public static void main(String[] args) throws LWJGLException, ResourceException, IOException {

		DeviceInfo deviceInfo = getDeviceInfo("desktop" + System.currentTimeMillis());
		ClientInfo clientInfo = getClientInfo(deviceInfo);
		NetworkGameInfo networkGameInfo = getNetworkGameInfo(clientInfo);

		boolean isHost = networkGameInfo.getHost().getDeviceInfo().getModel().equals(deviceInfo.getModel());

		System.out.println(isHost ? "Server" : "Client");
		initModules(isHost ? "Server" : "Client");
		initGame(isHost);

		GameSurface surface = new GameSurface(info);

		GameEngine engine;

		if (isHost) {
			HostNetworkManager hostManager = new HostNetworkManager(info);
			engine = new HostGameEngine(info, hostManager);
		} else {
			engine = new RemoteGameEngine(info);
		}
		engine.setPaused(false);
		engine.setLoading(false);

		Modules.MESSENGER.register(LogicMessageProcessor.ID, new LogicHandler(engine));
		GLHandler glHandler = new GLHandler(surface);
		Modules.MESSENGER.register(GLMessageProcessor.ID, glHandler);

		surface.onSurfaceCreated();
		surface.onSurfaceChanged(width, height);

		ExecutorService executor = Executors.newCachedThreadPool();

		executor.execute(engine);

		if (isHost) {
			Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.NEXT_BOARD);
		} else {
			executor.execute(new AI((AIPlayer) players[1]));
		}

		while (!Display.isCloseRequested()) {

			surface.onDrawFrame();
			Display.update();
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.NEXT_BOARD);

			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		executor.shutdownNow();
	}

	private static void initModules(String type) throws LWJGLException {

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
		Display.setTitle("Tower Defense Wars " + type);
		Display.create();
	}

	private static class AI implements Runnable {
		private AIPlayer player;

		public AI(AIPlayer player) {
			this.player = player;
		}

		@Override
		public void run() {
			try {
				while (true) {
					player.calculateMoves(50);
					Thread.sleep(50);
				}
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

	}

	private static ClientInfo getClientInfo(DeviceInfo deviceInfo) throws SocketException {

		Inet4Address localAddress = null;

		for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
			NetworkInterface intf = en.nextElement();
			for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
				InetAddress inetAddress = enumIpAddr.nextElement();

				if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
					System.out.println(inetAddress.getHostAddress().toString());
					localAddress = (Inet4Address) inetAddress;
				}

			}
		}

		if (localAddress != null)
			System.out.println("Local Address Is: " + localAddress.getHostAddress());
		else
			System.err.println("Unable to determine local address");

		PlayerStats playerStats = new PlayerStats(1000);
		return new ClientInfo(deviceInfo, playerStats, Races.ALL, GameInfo.FAST, Boards.ALL_BOARDS[0].getName());
	}

	private static DeviceInfo getDeviceInfo(String model) {
		return new DeviceInfo(model, Runtime.getRuntime().maxMemory(), Runtime.getRuntime().availableProcessors(), 99,
				GameEngine.FAST, ModelUtils.getMeshQuality(), ModelUtils.getTextureQuality());
	}

	private static NetworkGameInfo getNetworkGameInfo(ClientInfo clientInfo) throws ResourceException, IOException {
		//
		// ClientResource cr = new ClientResource("http://towerdefensewars.appspot.com/games");
		// // Workaround for GAE servers to prevent chunk encoding
		// cr.setRequestEntityBuffering(true);
		//
		// // Get the Contact object
		// PrepareGameResource resource = cr.wrap(PrepareGameResource.class);
		// int id = resource.prepareGame(clientInfo);
		//
		// NetworkGameInfo gameInfo = resource.getGame(id);
		// if (gameInfo != null) {
		// System.out.println("HOST: " + gameInfo.getHost().getAddress() + " " + gameInfo.getHost().getPort());
		// System.out.println("CLIENT: " + gameInfo.getHost().getAddress() + " " + gameInfo.getHost().getPort());
		// }
		// return gameInfo;
		return null;
	}
}
