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
package com.godsandtowers.core.commands;

import com.godsandtowers.core.GameInfo;
import com.godsandtowers.core.grid.Grid;
import com.godsandtowers.messaging.GLMessageProcessor;
import com.godsandtowers.sprites.BaseTower;
import com.godsandtowers.sprites.BuildingSphere;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.sprites.Tower;
import com.gundogstudios.modules.Modules;

public class MaxUpgradeTowerCommand implements Runnable {
	private GameInfo stats;
	private Player player;
	private float x;
	private float y;

	public MaxUpgradeTowerCommand(GameInfo stats, Player player, float x, float y) {
		this.stats = stats;
		this.player = player;
		this.x = x;
		this.y = y;
	}

	@Override
	public void run() {
		Grid grid = player.getGrid();
		Tower oldTower = grid.getTower(x, y);

		if (oldTower != null && player.getGold() >= oldTower.getCost()) {

			Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.REMOVE_SPRITE, player.getID(), oldTower);
			BaseTower baseTower = oldTower.getBaseTower();

			do {
				player.decreaseGold(baseTower.getCost());
				baseTower = player.getUpgrade(baseTower);
			} while (player.getGold() >= baseTower.getCost());

			Tower newTower = player.getGrid().replaceTower(oldTower, baseTower);
			stats.towerUpgraded(player.getID(), oldTower, newTower);

			Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.ADD_SPRITE, player.getID(), newTower);
		}

		BuildingSphere sphere = grid.removeBuildingSphere(oldTower);

		Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.REMOVE_TRANSPARENT_SPRITE, player.getID(),
				sphere);

	}
}