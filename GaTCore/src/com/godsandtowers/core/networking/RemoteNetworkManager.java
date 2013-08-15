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
package com.godsandtowers.core.networking;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.LinkedBlockingQueue;

import com.godsandtowers.core.GameInfo;
import com.godsandtowers.core.grid.Grid;
import com.godsandtowers.sprites.Player;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.modules.NetworkModule;

public class RemoteNetworkManager implements Runnable {

	private static final String TAG = "RemoteNetworkManager";

	private LinkedBlockingQueue<Runnable> commands;

	private boolean keepRunning;
	private ByteBuffer sendBuffer;
	private ByteBuffer receiveBuffer;
	private SnapshotProcessor snapshotProcessor;
	private CommandProcessor commandProcessor;
	private NetworkModule networking;
	private GameInfo gameInfo;
	private Snapshot latestSnapshot;

	public RemoteNetworkManager(GameInfo gameInfo) {
		this.gameInfo = gameInfo;
		this.latestSnapshot = null;
		commands = new LinkedBlockingQueue<Runnable>();
		keepRunning = true;
		sendBuffer = ByteBuffer.allocateDirect(32768).order(ByteOrder.LITTLE_ENDIAN);
		receiveBuffer = ByteBuffer.allocateDirect(32768).order(ByteOrder.LITTLE_ENDIAN);

		Grid grid = gameInfo.getPlayer(0).getGrid();
		snapshotProcessor = new SnapshotProcessor(grid.getNumRows(), grid.getNumColumns());
		commandProcessor = new CommandProcessor();
		networking = Modules.NETWORKING;
	}

	public void buyCreature(final int player, final String name) {
		commands.add(new Runnable() {

			public void run() {
				commandProcessor.encodeBuyCreature(sendBuffer, player, name);
				networking.send(sendBuffer);
			}
		});
	}

	public void buyTower(final int player, final String name, final float x, final float y) {
		commands.add(new Runnable() {

			public void run() {
				commandProcessor.encodeBuyTower(sendBuffer, player, name, x, y);
				networking.send(sendBuffer);
			}
		});
	}

	public void upgradeTower(final int player, final float x, final float y) {
		commands.add(new Runnable() {

			public void run() {
				commandProcessor.encodeUpgradeTower(sendBuffer, player, x, y);
				networking.send(sendBuffer);
			}
		});

	}

	public void sellTower(final int player, final float x, final float y) {
		commands.add(new Runnable() {

			public void run() {
				commandProcessor.encodeUpgradeTower(sendBuffer, player, x, y);
				networking.send(sendBuffer);
			}
		});
	}

	public void buildAllTowers(final int player) {
		commands.add(new Runnable() {

			public void run() {
				commandProcessor.encodeBuildAllTowers(sendBuffer, player);
				networking.send(sendBuffer);
			}
		});
	}

	public void buildTower(final int player, final String name) {
		commands.add(new Runnable() {

			public void run() {
				commandProcessor.encodeBuildTower(sendBuffer, player, name);
				networking.send(sendBuffer);
			}
		});
	}

	public void cancelBuildTowers(final int player) {
		commands.add(new Runnable() {

			public void run() {
				commandProcessor.encodeCancelBuildTowers(sendBuffer, player);
				networking.send(sendBuffer);
			}
		});
	}

	public void gridTouchCommand(final int player, final int col, final int row) {
		commands.add(new Runnable() {

			public void run() {
				commandProcessor.encodeGridTouchCommand(sendBuffer, player, col, row);
				networking.send(sendBuffer);
			}
		});
	}

	public void executeSpecial(final int player, final String name) {
		commands.add(new Runnable() {

			public void run() {
				commandProcessor.encodeExecuteSpecial(sendBuffer, player, name);
				networking.send(sendBuffer);
			}
		});
	}

	public void autoBuyCreature(final int player, final String name) {
		commands.add(new Runnable() {

			public void run() {
				commandProcessor.encodeAutoBuyCreature(sendBuffer, player, name);
				networking.send(sendBuffer);
			}
		});
	}

	public void maxUpgradeTower(final int player, final float x, final float y) {
		commands.add(new Runnable() {

			public void run() {
				commandProcessor.encodeMaxUpgradeTower(sendBuffer, player, x, y);
				networking.send(sendBuffer);
			}
		});
	}

	public void cancelAutoBuyCreature(final int player) {
		commands.add(new Runnable() {

			public void run() {
				commandProcessor.encodeCancelAutoBuyCreature(sendBuffer, player);
				networking.send(sendBuffer);
			}
		});
	}

	public void run() {
		Modules.LOG.info(TAG, "STARTED REMOTE NETWORK MANAGER");

		try {
			while (keepRunning) {
				Player localPlayer = gameInfo.getLocalPlayer();
				commandProcessor.encodeHeartbeat(sendBuffer, localPlayer.getID(), localPlayer.isAttacking());
				networking.send(sendBuffer);

				Runnable command;
				while ((command = commands.poll()) != null)
					command.run();

				receiveBuffer.clear();
				networking.receive(receiveBuffer);
				receiveBuffer.position(0);
				int commandID = receiveBuffer.getInt();

				switch (commandID) {
				case NetworkPackets.SNAPSHOT:
					Snapshot snapshot = snapshotProcessor.parseSnapshot(receiveBuffer);

					if (latestSnapshot == null || snapshot.getTimeStamp() > latestSnapshot.getTimeStamp())
						latestSnapshot = snapshot;
					break;
				case NetworkPackets.LAUNCH_UPGRADE_VIEW:
					commandProcessor.parseLaunchUpgradeView(localPlayer, receiveBuffer);
					break;
				case NetworkPackets.RESULTS:
					commandProcessor.parseResults(receiveBuffer);
					break;
				default:
					Modules.LOG.warn(TAG, "Unknown command id, skipping it: " + commandID);

				}

			}
		} catch (Exception e) {
			Modules.LOG.warn(TAG, e.toString());
			return;
		}
	}

	public Snapshot getLatestSnapshot() {
		return latestSnapshot;
	}

	public void shutDown() {
		keepRunning = false;
	}

}
