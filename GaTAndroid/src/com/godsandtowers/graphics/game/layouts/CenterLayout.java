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
import android.widget.RelativeLayout;

import com.godsandtowers.R;
import com.godsandtowers.core.GameInfo;
import com.godsandtowers.graphics.game.GameLayoutManager;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.sprites.Tower;

public class CenterLayout {

	private Activity activity;
	private CenterTowerLayout towerLayout;
	private CenterCreatureLayout creatureLayout;
	private CenterSpecialLayout specialLayout;
	private CenterOptionsLayout optionsLayout;
	private CenterBoardLayout boardLayout;
	private GameLayoutManager manager;
	private CenterUpgradeTowerLayout upgradeLayout;
	private Player localPlayer;

	public CenterLayout(Activity activity, GameLayoutManager manager, TopLayout top) {
		this.activity = activity;
		this.manager = manager;
		towerLayout = new CenterTowerLayout(activity, this, manager);
		creatureLayout = new CenterCreatureLayout(activity, manager);
		optionsLayout = new CenterOptionsLayout(activity);
		boardLayout = new CenterBoardLayout(activity, top);
		RelativeLayout center = (RelativeLayout) activity.findViewById(R.id.centerLayout);
		center.bringToFront();
	}

	public void setPlayers(GameInfo gameInfo) {
		localPlayer = gameInfo.getPlayer(gameInfo.getLocalPlayerID());
		towerLayout.setPlayer(localPlayer);
		creatureLayout.setPlayer(localPlayer);
		boardLayout.setPlayer(gameInfo);
	}

	public void attachUpgradeTowerLayout(Tower tower) {
		upgradeLayout = new CenterUpgradeTowerLayout(tower, localPlayer, activity, manager);
		upgradeLayout.attach();
	}

	public void attachTowerLayout() {
		towerLayout.attach();
	}

	public void attachCreatureLayout() {
		creatureLayout.attach();
	}

	public void attachSpecialLayout() {
		specialLayout = new CenterSpecialLayout(activity, localPlayer);
		specialLayout.attach();
	}

	public void attachOptionsLayout() {
		optionsLayout.attach();
	}

	public void attachBoardLayout() {
		boardLayout.attach();
		if (upgradeLayout != null) {
			upgradeLayout = null;
		}
	}
}
