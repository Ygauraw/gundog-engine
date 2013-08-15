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
package com.godsandtowers.messaging;

import com.godsandtowers.core.GameEngine;
import com.godsandtowers.sprites.Tower;
import com.gundogstudios.modules.Modules;

public class LogicHandler implements LogicMessageProcessor {

	private GameEngine engine;

	public LogicHandler(GameEngine engine) {
		this.engine = engine;
	}

	@Override
	public void process(int what, Object[] object) {
		int player;
		String name;
		float x;
		float y;
		switch (what) {
		case BUY_TOWER:
			player = (Integer) object[0];
			name = (String) object[1];
			x = (Float) object[2];
			y = (Float) object[3];
			engine.buyTower(player, name, x, y);
			break;
		case BUY_CREATURE:
			player = (Integer) object[0];
			name = (String) object[1];
			engine.buyCreature(player, name);
			break;
		case UPGRADE_TOWER:
			player = (Integer) object[0];
			x = (Float) object[1];
			y = (Float) object[2];
			engine.upgradeTower(player, x, y);
			break;
		case SELL_TOWER:
			player = (Integer) object[0];
			x = (Float) object[1];
			y = (Float) object[2];
			engine.sellTower(player, x, y);
			break;
		case EXECUTE_SPECIAL:
			engine.executeSpecial((Integer) object[0], (String) object[1]);
			break;
		case BUILD_TOWER:
			engine.buildTower((Integer) object[0], (String) object[1]);
			break;
		case BUILD_ALL_TOWERS:
			engine.buildAllTowers((Integer) object[0]);
			break;
		case CANCEL_BUILD_TOWERS:
			engine.cancelBuildTowers((Integer) object[0]);
			break;
		case GRID_TOUCHED:
			player = (Integer) object[0];
			int col = (Integer) object[1];
			int row = (Integer) object[2];
			engine.gridTouchCommand(player, col, row);
			break;
		case PAUSE:
			engine.setPaused(true);
			break;
		case PLAY:
			engine.setPaused(false);
			break;
		case GL_FINISHED_LOADING:
			engine.setLoading(false);
			break;
		case ATTACKING:
			player = (Integer) object[0];
			boolean attacking = (Boolean) object[1];
			engine.setAttacking(player, attacking);
			break;
		case LAUNCH_UPGRADE_VIEW:
			player = (Integer) object[0];
			Tower tower = (Tower) object[1];
			engine.launchUpgradeView(player, tower);
			break;
		case PROCESS_RESULTS:
			engine.processResults((Integer) object[0]);
			break;
		case AUTO_BUY_CREATURE:
			player = (Integer) object[0];
			name = (String) object[1];
			engine.autoBuyCreature(player, name);
			break;
		case MAX_UPGRADE_TOWER:
			player = (Integer) object[0];
			x = (Float) object[1];
			y = (Float) object[2];
			engine.maxUpgradeTower(player, x, y);
			break;
		case CANCEL_AUTO_BUY_CREATURE:
			player = (Integer) object[0];
			engine.cancelAutoBuyCreature(player);
			break;
		default:
			Modules.LOG.error("LogicHandler", "Unknown message " + what);
		}
	}
}
