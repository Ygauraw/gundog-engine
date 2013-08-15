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
import com.godsandtowers.messaging.GLMessageProcessor;
import com.godsandtowers.sprites.BaseTower;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.sprites.Tower;
import com.gundogstudios.modules.Modules;

public class BuyTowerCommand implements Runnable {
	private GameInfo stats;
	private Player player;
	private String name;
	private float x;
	private float y;

	public BuyTowerCommand(GameInfo stats, Player player, String name, float x, float y) {
		this.stats = stats;
		this.player = player;
		this.name = name;
		this.x = x;
		this.y = y;
	}

	@Override
	public void run() {
		BaseTower baseTower = player.getTower(name);

		if (player.getGold() >= baseTower.getCost()) {
			Tower tower = new Tower(baseTower, player.getRace().getBaseRace(), x, y);
			if (player.getGrid().addTower(tower)) {

				player.decreaseGold(tower.getCost());
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.ADD_SPRITE, player.getID(), tower);
				stats.towerPurchased(player.getID(), tower);
			}
		}
	}
}
