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

import com.godsandtowers.sprites.Tower;

public interface GameEngine extends Runnable {

	public static final int DISCONNECT_TIMEOUT = 10000;
	public static final int REFRESH_RATE = 500;// ms
	public static final int SLOW = 0;
	public static final int NORMAL = 1;
	public static final int FAST = 2;

	public void cancelAutoBuyCreature(int player);

	public void autoBuyCreature(int player, String name);

	public void maxUpgradeTower(int player, float x, float y);

	public void buyCreature(int player, String name);

	public void buyTower(int player, String name, float x, float y);

	public void upgradeTower(int player, float x, float y);

	public void sellTower(int player, float x, float y);

	public void buildAllTowers(int player);

	public void buildTower(int player, String name);

	public void cancelBuildTowers(int player);

	public void gridTouchCommand(int player, int col, int row);

	public void executeSpecial(int player, String name);

	public void launchUpgradeView(int player, Tower tower);

	public void processResults(int winnerID);

	public void run();

	public GameInfo getGameInfo();

	public void setAttacking(int player, boolean attacking);

	public boolean hasStarted();

	public boolean hasExited();

	public boolean isPaused();

	public boolean isSaveable();

	public boolean isLoading();

	public void setPaused(boolean paused);

	public void setLoading(boolean loading);

	public void quitGame();

	public String toString();

}