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

import static com.godsandtowers.core.networking.NetworkPackets.BUILD_ALL_TOWERS;
import static com.godsandtowers.core.networking.NetworkPackets.BUILD_TOWER;
import static com.godsandtowers.core.networking.NetworkPackets.BUY_CREATURE;
import static com.godsandtowers.core.networking.NetworkPackets.BUY_TOWER;
import static com.godsandtowers.core.networking.NetworkPackets.CANCEL_BUILD_TOWERS;
import static com.godsandtowers.core.networking.NetworkPackets.EXECUTE_SPECIAL;
import static com.godsandtowers.core.networking.NetworkPackets.GRID_TOUCH_COMMAND;
import static com.godsandtowers.core.networking.NetworkPackets.HEART_BEAT;
import static com.godsandtowers.core.networking.NetworkPackets.SELL_TOWER;
import static com.godsandtowers.core.networking.NetworkPackets.SNAPSHOT;
import static com.godsandtowers.core.networking.NetworkPackets.UPGRADE_TOWER;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.godsandtowers.core.GameInfo;
import com.godsandtowers.core.grid.Grid;
import com.godsandtowers.sprites.Tower;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.modules.NetworkModule;

public class HostNetworkManager implements Runnable {

	private static final String TAG = "HostNetworkManager";
	private boolean keepRunning;
	private ByteBuffer sendBuffer;
	private ByteBuffer receiveBuffer;
	private SnapshotProcessor snapshotProcessor;
	private CommandProcessor commandProcessor;
	private GameInfo gameInfo;
	private NetworkModule networking;
	private long timeSinceLastHeartbeat;

	public HostNetworkManager(GameInfo gameInfo) {
		this.gameInfo = gameInfo;
		keepRunning = true;
		sendBuffer = ByteBuffer.allocateDirect(32768).order(ByteOrder.LITTLE_ENDIAN);
		receiveBuffer = ByteBuffer.allocateDirect(32768).order(ByteOrder.LITTLE_ENDIAN);

		Grid grid = gameInfo.getPlayer(0).getGrid();
		snapshotProcessor = new SnapshotProcessor(grid.getNumRows(), grid.getNumColumns());
		commandProcessor = new CommandProcessor();
		networking = Modules.NETWORKING;
	}

	public void sendSnapshot(long currentTime) {
		sendBuffer.clear();
		snapshotProcessor.encodeSnapshot(sendBuffer, gameInfo, currentTime);
		networking.send(sendBuffer);
	}

	public void sendUpgradeView(int player, Tower tower) {
		sendBuffer.clear();
		commandProcessor.encodeLaunchUpgradeView(sendBuffer, player, tower);
		networking.send(sendBuffer);
	}

	public void sendGameResults(GameInfo gameInfo) {
		sendBuffer.clear();
		commandProcessor.encodeResults(sendBuffer, gameInfo);
		networking.send(sendBuffer);
	}

	@Override
	public void run() {
		try {
			timeSinceLastHeartbeat = System.currentTimeMillis();
			while (keepRunning) {
				receiveBuffer.clear();
				networking.receive(receiveBuffer);
				receiveBuffer.position(0);

				int command = receiveBuffer.getInt();

				switch (command) {
				case HEART_BEAT:
					timeSinceLastHeartbeat = System.currentTimeMillis();
					commandProcessor.parseHeartbeat(receiveBuffer);
					break;
				case BUY_CREATURE:
					commandProcessor.parseBuyCreature(receiveBuffer);
					break;
				case BUY_TOWER:
					commandProcessor.parseBuyTower(receiveBuffer);
					break;
				case SELL_TOWER:
					commandProcessor.parseSellTower(receiveBuffer);
					break;
				case UPGRADE_TOWER:
					commandProcessor.parseUpgradeTower(receiveBuffer);
					break;
				case BUILD_ALL_TOWERS:
					commandProcessor.parseBuildAllTowers(receiveBuffer);
					break;
				case BUILD_TOWER:
					commandProcessor.parseBuildTower(receiveBuffer);
					break;
				case CANCEL_BUILD_TOWERS:
					commandProcessor.parseCancelBuildTowers(receiveBuffer);
					break;
				case GRID_TOUCH_COMMAND:
					commandProcessor.parseGridTouchCommand(receiveBuffer);
					break;
				case EXECUTE_SPECIAL:
					commandProcessor.parseExecuteSpecial(receiveBuffer);
					break;
				case NetworkPackets.AUTO_BUY_CREATURE:
					commandProcessor.parseAutoBuyCreature(receiveBuffer);
					break;
				case NetworkPackets.MAX_UPGRADE_TOWER:
					commandProcessor.parseMaxUpgradeTower(receiveBuffer);
					break;
				case NetworkPackets.CANCEL_AUTO_BUY_CREATURE:
					commandProcessor.parseCancelAutoBuyCreature(receiveBuffer);
					break;
				case SNAPSHOT:
				default:
					Modules.LOG.warn(TAG, "Unknown command, skipping it: " + command);
				}

			}
		} catch (Exception e) {
			Modules.LOG.warn(TAG, e.toString());
			return;
		}
	}

	public long getTimeSinceLastHeartbeat() {
		return timeSinceLastHeartbeat;
	}

	public void shutDown() {
		keepRunning = false;

	}

}
