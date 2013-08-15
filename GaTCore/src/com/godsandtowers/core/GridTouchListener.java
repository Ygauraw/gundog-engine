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
package com.godsandtowers.core;

import java.util.ArrayList;

import com.godsandtowers.core.grid.Grid;
import com.godsandtowers.messaging.GLMessageProcessor;
import com.godsandtowers.messaging.LogicMessageProcessor;
import com.godsandtowers.sprites.BaseTower;
import com.godsandtowers.sprites.BuildingSphere;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.sprites.Tower;
import com.gundogstudios.modules.Modules;

public class GridTouchListener {
	private static final String TAG = "GridTouchListener";
	private BaseTower tower;
	private Player player;
	private boolean upgrading = false;

	public GridTouchListener(Player player) {
		this.player = player;
	}

	public void buildAll() {
		Modules.LOG.info(TAG, "buildAll " + player.getID());
		this.tower = null;
		clearBuildingSpheres();
		ArrayList<Tower> failures = player.getGrid().buildAll();
		for (Tower tower : failures) {
			player.increaseGold(tower.getCost());
			Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.REMOVE_SPRITE, player.getID(), tower);
		}
	}

	public void buildTower(String name) {
		Modules.LOG.info(TAG, "buildTower " + player.getID());
		this.tower = player.getTower(name);
	}

	public void cancelBuildTowers() {
		Modules.LOG.info(TAG, "cancelBuildTowers " + player.getID());
		this.tower = null;
		for (Tower tower : player.getGrid().getBuildingTowers()) {
			player.increaseGold(tower.getCost());
			Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.REMOVE_SPRITE, player.getID(), tower);
		}
		clearBuildingSpheres();
		player.getGrid().cancelBuild();
		upgrading = false;

	}

	private void clearBuildingSpheres() {
		for (BuildingSphere buildingSphere : player.getGrid().getBuildingSpheres()) {
			Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.REMOVE_TRANSPARENT_SPRITE,
					player.getID(), buildingSphere);
		}
	}

	public void touched(int column, int row) {

		Modules.LOG.info(TAG, "touched " + player.getID());
		Grid grid = player.getGrid();
		if (tower != null) {
			Tower building = grid.removeBuildingTower(row, column);
			if (building != null) {
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.REMOVE_SPRITE, player.getID(),
						building);
				player.increaseGold(building.getCost());
				BuildingSphere sphere = grid.removeBuildingSphere(building);
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.REMOVE_TRANSPARENT_SPRITE,
						player.getID(), sphere);
				return;
			}

			if (player.getGold() >= tower.getCost()) {
				float x = grid.convertToX(column);
				float y = grid.convertToY(row);
				Tower clone = new Tower(tower, player.getRace().getBaseRace(), x, y);
				BuildingSphere sphere = grid.addBuildingTower(clone);
				if (sphere != null) {
					player.decreaseGold(tower.getCost());
					Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.ADD_SPRITE, player.getID(),
							clone);
					Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.ADD_TRANSPARENT_SPRITE,
							player.getID(), sphere, clone.getAttackRange() * .75f);
				}
			}
		} else {
			Tower upgrade = grid.getTower(row, column);
			if (upgrade != null) {
				if (upgrading) {
					clearBuildingSpheres();
					player.getGrid().cancelBuild();
				}
				Modules.MESSENGER.submit(LogicMessageProcessor.ID, LogicMessageProcessor.LAUNCH_UPGRADE_VIEW,
						player.getID(), upgrade);

				BuildingSphere sphere = grid.addBuildingSphere(upgrade);
				Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.ADD_TRANSPARENT_SPRITE,
						player.getID(), sphere, upgrade.getAttackRange() * .75f);
				upgrading = true;
			}
		}
	}

	public void cancelUpgrading() {
		this.upgrading = false;
	}
}
