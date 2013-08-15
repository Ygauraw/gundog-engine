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
package com.godsandtowers.graphics.game.layouts;

import android.app.Activity;

import com.godsandtowers.core.GameInfo;
import com.godsandtowers.sprites.BaseCreature;
import com.godsandtowers.sprites.BaseTower;
import com.godsandtowers.sprites.Player;

public class BottomLayout {

	private BottomDefaultLayout defaultLayout;
	private BottomCancelLayout cancelLayout;
	private BottomTowerLayout towerLayout;
	private BottomUpgradeTowerLayout upgradeTowerLayout;
	private BottomCreatureLayout creatureLayout;

	public BottomLayout(Activity activity, CenterLayout centerLayout) {
		defaultLayout = new BottomDefaultLayout(activity, this, centerLayout);
		cancelLayout = new BottomCancelLayout(activity, this, centerLayout);
		towerLayout = new BottomTowerLayout(activity, this, centerLayout);
		creatureLayout = new BottomCreatureLayout(activity, this, centerLayout);
		upgradeTowerLayout = new BottomUpgradeTowerLayout(activity, this, centerLayout);
		defaultLayout.attach();
	}

	public void setAttacking(boolean attacking) {
		defaultLayout.setAttacking(attacking);
	}

	public void setPlayers(GameInfo gameInfo) {
		defaultLayout.setPlayers(gameInfo);
		Player player = gameInfo.getPlayer(gameInfo.getLocalPlayerID());
		towerLayout.setPlayer(player);
		cancelLayout.setPlayer(player);
		creatureLayout.setPlayer(player);
		upgradeTowerLayout.setPlayer(player);
	}

	public void setTower(BaseTower tower) {
		towerLayout.setTower(tower);
	}

	public void setUpgradeTower(BaseTower tower) {
		upgradeTowerLayout.setTower(tower);
	}

	public void setCreature(BaseCreature creature) {
		creatureLayout.setCreature(creature);
	}

	public void attachDefaultLayout() {
		defaultLayout.attach();
	}

	public void attachCancelLayout() {
		cancelLayout.attach();
	}

	public void attachTowerLayout() {
		towerLayout.attach();
	}

	public void attachCreatureLayout() {
		creatureLayout.attach();
	}

	public void attachUpgradeTowerLayout() {
		upgradeTowerLayout.attach();
	}

	public void onResume() {
		defaultLayout.onResume();
	}

	public void onPause() {
		defaultLayout.onPause();
	}

}
