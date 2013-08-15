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
package com.godsandtowers.sprites;

public interface Upgradeable {

	public String getName();

	public String getUpgradeName(int id);

	public int[] getUpgradeIDs();

	public long getUpgradeCost(int id);

	public float getBaseValue(int id);

	public float getUpgradedValue(int id);

	public void upgrade(int id);

	public int getUpgradeCount(int id);
}
