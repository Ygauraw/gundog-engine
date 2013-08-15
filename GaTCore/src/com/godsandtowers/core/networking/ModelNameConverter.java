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

import gnu.trove.map.hash.TByteObjectHashMap;
import gnu.trove.map.hash.TObjectByteHashMap;

import com.godsandtowers.core.grid.Square;
import com.godsandtowers.sprites.BaseCreature;
import com.godsandtowers.sprites.BaseSpecial;
import com.godsandtowers.sprites.BaseTower;
import com.godsandtowers.sprites.BuildingSphere;
import com.godsandtowers.sprites.MovingProjectile;

public class ModelNameConverter {
	private static TObjectByteHashMap<String> modelNameToByteMap;
	private static TByteObjectHashMap<String> byteToModelNameMap;

	static {
		byteToModelNameMap = new TByteObjectHashMap<String>();
		modelNameToByteMap = new TObjectByteHashMap<String>();

		int count = 1;
		for (BaseCreature creature : BaseCreature.getBaseCreatures()) {
			count = putModelName(creature.getName(), count);
		}
		for (BaseTower tower : BaseTower.getBaseTowers()) {
			count = putModelName(tower.getName(), count);
		}
		for (String squareModel : Square.SQUARE_MODELS) {
			count = putModelName(squareModel, count);
		}
		for (BaseSpecial special : BaseSpecial.getSpecials()) {
			count = putModelName(special.getName(), count);
		}
		count = putModelName(MovingProjectile.EARTH_BALL_PROJECTILE, count);
		count = putModelName(MovingProjectile.FIRE_BALL_PROJECTILE, count);
		count = putModelName(MovingProjectile.LIFE_BALL_PROJECTILE, count);
		count = putModelName(MovingProjectile.UNDEAD_BALL_PROJECTILE, count);
		count = putModelName(MovingProjectile.ICE_BALL_PROJECTILE, count);
		count = putModelName(MovingProjectile.WIND_BALL_PROJECTILE, count);
		count = putModelName(BuildingSphere.NAME, count);
	}

	private static int putModelName(String name, int count) {
		byteToModelNameMap.put((byte) count, name);
		modelNameToByteMap.put(name, (byte) count);
		return ++count;
	}

	public static byte getModelNameAsByte(String name) {
		return modelNameToByteMap.get(name);
	}

	public static String getByteAsModelName(byte b) {
		return byteToModelNameMap.get(b);
	}
}
