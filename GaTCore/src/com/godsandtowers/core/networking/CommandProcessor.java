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

import static com.godsandtowers.messaging.LogicMessageProcessor.ID;

import java.nio.ByteBuffer;

import com.godsandtowers.core.GameInfo;
import com.godsandtowers.messaging.LogicMessageProcessor;
import com.godsandtowers.sprites.BaseTower;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.sprites.Tower;
import com.gundogstudios.modules.MessageModule;
import com.gundogstudios.modules.Modules;

public class CommandProcessor {

	private MessageModule messenger;

	public CommandProcessor() {
		this.messenger = Modules.MESSENGER;
	}

	public void encodeExecuteSpecial(ByteBuffer buffer, int player, String name) {
		buffer.clear();
		buffer.putInt(NetworkPackets.EXECUTE_SPECIAL);
		buffer.putInt(player);
		buffer.put(ModelNameConverter.getModelNameAsByte(name));
		buffer.position(0);
		buffer.limit(9);
	}

	public void parseExecuteSpecial(ByteBuffer buffer) {
		int player = buffer.getInt();
		String name = ModelNameConverter.getByteAsModelName(buffer.get());
		messenger.submit(ID, LogicMessageProcessor.EXECUTE_SPECIAL, player, name);
	}

	public void encodeCancelAutoBuyCreature(ByteBuffer buffer, int player) {
		buffer.clear();
		buffer.putInt(NetworkPackets.CANCEL_AUTO_BUY_CREATURE);
		buffer.putInt(player);
		buffer.position(0);
		buffer.limit(8);
	}

	public void parseCancelAutoBuyCreature(ByteBuffer buffer) {
		int player = buffer.getInt();
		messenger.submit(ID, LogicMessageProcessor.CANCEL_AUTO_BUY_CREATURE, player);
	}

	public void encodeMaxUpgradeTower(ByteBuffer buffer, int player, float x, float y) {
		buffer.clear();
		buffer.putInt(NetworkPackets.MAX_UPGRADE_TOWER);
		buffer.putInt(player);
		buffer.putFloat(x);
		buffer.putFloat(y);
		buffer.position(0);
		buffer.limit(16);
	}

	public void parseMaxUpgradeTower(ByteBuffer buffer) {
		int player = buffer.getInt();
		float x = buffer.getFloat();
		float y = buffer.getFloat();
		messenger.submit(ID, LogicMessageProcessor.MAX_UPGRADE_TOWER, player, x, y);
	}

	public void encodeAutoBuyCreature(ByteBuffer buffer, int player, String name) {
		buffer.clear();
		buffer.putInt(NetworkPackets.AUTO_BUY_CREATURE);
		buffer.putInt(player);
		buffer.put(ModelNameConverter.getModelNameAsByte(name));
		buffer.position(0);
		buffer.limit(9);
	}

	public void parseAutoBuyCreature(ByteBuffer buffer) {
		int player = buffer.getInt();
		String name = ModelNameConverter.getByteAsModelName(buffer.get());
		messenger.submit(ID, LogicMessageProcessor.AUTO_BUY_CREATURE, player, name);
	}

	public void encodeResults(ByteBuffer buffer, GameInfo gameInfo) {
		buffer.clear();
		buffer.putInt(NetworkPackets.RESULTS);
		buffer.putInt(gameInfo.getWinnerID());
		buffer.position(0);
		buffer.limit(8);
	}

	public void parseResults(ByteBuffer buffer) {
		int winnerID = buffer.getInt();
		messenger.submit(ID, LogicMessageProcessor.PROCESS_RESULTS, winnerID);
	}

	public void encodeLaunchUpgradeView(ByteBuffer buffer, int player, Tower tower) {
		buffer.clear();
		buffer.putInt(NetworkPackets.LAUNCH_UPGRADE_VIEW);
		buffer.putInt(player);
		buffer.putInt(tower.getLevel());
		buffer.putFloat(tower.getX());
		buffer.putFloat(tower.getY());
		buffer.put(ModelNameConverter.getModelNameAsByte(tower.getName()));
		buffer.position(0);
		buffer.limit(21);
	}

	public void parseLaunchUpgradeView(Player localPlayer, ByteBuffer buffer) {
		int player = buffer.getInt();

		if (localPlayer.getID() != player)
			return;

		int level = buffer.getInt();
		float x = buffer.getFloat();
		float y = buffer.getFloat();
		String name = ModelNameConverter.getByteAsModelName(buffer.get());
		BaseTower baseTower = localPlayer.getTower(name);
		while (baseTower.getLevel() < level)
			baseTower = localPlayer.getUpgrade(baseTower);
		Tower tower = new Tower(baseTower, localPlayer.getRace().getBaseRace(), x, y);
		messenger.submit(ID, LogicMessageProcessor.LAUNCH_UPGRADE_VIEW, player, tower);
	}

	public void encodeBuyCreature(ByteBuffer buffer, int player, String name) {
		buffer.clear();
		buffer.putInt(NetworkPackets.BUY_CREATURE);
		buffer.putInt(player);
		buffer.put(ModelNameConverter.getModelNameAsByte(name));
		buffer.position(0);
		buffer.limit(9);
	}

	public void parseBuyCreature(ByteBuffer buffer) {
		int player = buffer.getInt();
		String name = ModelNameConverter.getByteAsModelName(buffer.get());
		messenger.submit(ID, LogicMessageProcessor.BUY_CREATURE, player, name);
	}

	public void encodeBuyTower(ByteBuffer buffer, int player, String name, float x, float y) {
		buffer.clear();
		buffer.putInt(NetworkPackets.BUY_TOWER);
		buffer.putInt(player);
		buffer.put(ModelNameConverter.getModelNameAsByte(name));
		buffer.putFloat(x);
		buffer.putFloat(y);
		buffer.position(0);
		buffer.limit(17);
	}

	public void parseBuyTower(ByteBuffer buffer) {
		int player = buffer.getInt();
		String name = ModelNameConverter.getByteAsModelName(buffer.get());
		float x = buffer.getFloat();
		float y = buffer.getFloat();
		messenger.submit(ID, LogicMessageProcessor.BUY_TOWER, player, name, x, y);
	}

	public void encodeUpgradeTower(ByteBuffer buffer, int player, float x, float y) {
		buffer.clear();
		buffer.putInt(NetworkPackets.UPGRADE_TOWER);
		buffer.putInt(player);
		buffer.putFloat(x);
		buffer.putFloat(y);
		buffer.position(0);
		buffer.limit(16);
	}

	public void parseUpgradeTower(ByteBuffer buffer) {
		int player = buffer.getInt();
		float x = buffer.getFloat();
		float y = buffer.getFloat();
		messenger.submit(ID, LogicMessageProcessor.UPGRADE_TOWER, player, x, y);
	}

	public void encodeSellTower(ByteBuffer buffer, int player, float x, float y) {
		buffer.clear();
		buffer.putInt(NetworkPackets.SELL_TOWER);
		buffer.putInt(player);
		buffer.putFloat(x);
		buffer.putFloat(y);
		buffer.position(0);
		buffer.limit(16);
	}

	public void parseSellTower(ByteBuffer buffer) {
		int player = buffer.getInt();
		float x = buffer.getFloat();
		float y = buffer.getFloat();
		messenger.submit(ID, LogicMessageProcessor.SELL_TOWER, player, x, y);
	}

	public void encodeBuildAllTowers(ByteBuffer buffer, int player) {
		buffer.clear();
		buffer.putInt(NetworkPackets.BUILD_ALL_TOWERS);
		buffer.putInt(player);
		buffer.position(0);
		buffer.limit(8);
	}

	public void parseBuildAllTowers(ByteBuffer buffer) {
		int player = buffer.getInt();
		messenger.submit(ID, LogicMessageProcessor.BUILD_ALL_TOWERS, player);
	}

	public void encodeBuildTower(ByteBuffer buffer, int player, String name) {
		buffer.clear();
		buffer.putInt(NetworkPackets.BUILD_TOWER);
		buffer.putInt(player);
		buffer.put(ModelNameConverter.getModelNameAsByte(name));
		buffer.position(0);
		buffer.limit(9);
	}

	public void parseBuildTower(ByteBuffer buffer) {
		int player = buffer.getInt();
		String name = ModelNameConverter.getByteAsModelName(buffer.get());
		messenger.submit(ID, LogicMessageProcessor.BUILD_TOWER, player, name);
	}

	public void encodeCancelBuildTowers(ByteBuffer buffer, int player) {
		buffer.clear();
		buffer.putInt(NetworkPackets.CANCEL_BUILD_TOWERS);
		buffer.putInt(player);
		buffer.position(0);
		buffer.limit(8);
	}

	public void parseCancelBuildTowers(ByteBuffer buffer) {
		int player = buffer.getInt();
		messenger.submit(ID, LogicMessageProcessor.CANCEL_BUILD_TOWERS, player);
	}

	public void encodeGridTouchCommand(ByteBuffer buffer, int player, int col, int row) {
		buffer.clear();
		buffer.putInt(NetworkPackets.GRID_TOUCH_COMMAND);
		buffer.putInt(player);
		buffer.putInt(col);
		buffer.putInt(row);
		buffer.position(0);
		buffer.limit(16);
	}

	public void parseGridTouchCommand(ByteBuffer buffer) {
		int player = buffer.getInt();
		int col = buffer.getInt();
		int row = buffer.getInt();
		messenger.submit(ID, LogicMessageProcessor.GRID_TOUCHED, player, col, row);
	}

	public void encodeHeartbeat(ByteBuffer buffer, int player, boolean attacking) {
		buffer.clear();
		buffer.putInt(NetworkPackets.HEART_BEAT);
		buffer.putInt(player);
		buffer.putInt(attacking ? 1 : 0);
		buffer.position(0);
		buffer.limit(12);
	}

	public void parseHeartbeat(ByteBuffer buffer) {
		int player = buffer.getInt();
		int attacking = buffer.getInt();
		messenger.submit(ID, LogicMessageProcessor.ATTACKING, player, (attacking > 0));
	}

}
