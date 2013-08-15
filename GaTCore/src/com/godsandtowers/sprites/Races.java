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

import java.util.ArrayList;

import com.gundogstudios.util.FastMath;

public class Races {

	private static final String ICE_NAME = "ICE";
	private static final String WIND_NAME = "WIND";
	private static final String EARTH_NAME = "EARTH";
	private static final String FIRE_NAME = "FIRE";
	private static final String LIFE_NAME = "LIFE";
	private static final String DEATH_NAME = "DEATH";
	public static final int ICE = 1;
	public static final int WIND = 2;
	public static final int EARTH = 4;
	public static final int FIRE = 8;
	public static final int LIFE = 16;
	public static final int DEATH = 32;
	public static final int ALL = ICE | WIND | EARTH | FIRE | LIFE | DEATH;
	public static final int[] ALL_RACES = { ICE, WIND, EARTH, FIRE, LIFE, DEATH };
	public static final String[] ALL_RACES_STRINGS = { ICE_NAME, WIND_NAME, EARTH_NAME, FIRE_NAME, LIFE_NAME,
			DEATH_NAME };

	public static int combineRaces(int... races) {
		int value = 0;
		for (int race : races) {
			value |= race;
		}
		return value;
	}

	public static boolean isRaces(int combinedRace, int race) {
		return (combinedRace & race) - race == 0;
	}

	public static int getNumRaces(int races) {
		int count = 0;
		if (isIce(races))
			count++;
		if (isWind(races))
			count++;
		if (isEarth(races))
			count++;
		if (isFire(races))
			count++;
		if (isLife(races))
			count++;
		if (isDeath(races))
			count++;
		return count;
	}

	public static boolean isIce(int race) {
		return (ICE & race) > 0;
	}

	public static boolean isWind(int race) {
		return (WIND & race) > 0;
	}

	public static boolean isEarth(int race) {
		return (EARTH & race) > 0;
	}

	public static boolean isFire(int race) {
		return (FIRE & race) > 0;
	}

	public static boolean isLife(int race) {
		return (LIFE & race) > 0;
	}

	public static boolean isDeath(int race) {
		return (DEATH & race) > 0;
	}

	public static String getName(int race) {
		switch (race) {
		case ICE:
			return "race_ice";
		case WIND:
			return "race_wind";
		case EARTH:
			return "race_earth";
		case FIRE:
			return "race_fire";
		case LIFE:
			return "race_life";
		case DEATH:
			return "race_death";
		default:
			throw new RuntimeException("Trying to get name for an unknown race: " + race);
		}
	}

	public static int pickRandomRace(int races) {

		ArrayList<Integer> list = new ArrayList<Integer>(6);

		if (isIce(races))
			list.add(ICE);
		if (isWind(races))
			list.add(WIND);
		if (isEarth(races))
			list.add(EARTH);
		if (isFire(races))
			list.add(FIRE);
		if (isLife(races))
			list.add(LIFE);
		if (isDeath(races))
			list.add(DEATH);

		return list.get(FastMath.floor(FastMath.random() * list.size()));
	}

	public static String toString(int races) {
		StringBuilder text = new StringBuilder();

		if (isIce(races))
			text.append(((text.length() > 1) ? "/" : "") + ICE_NAME);
		if (isWind(races))
			text.append(((text.length() > 1) ? "/" : "") + WIND_NAME);
		if (isEarth(races))
			text.append(((text.length() > 1) ? "/" : "") + EARTH_NAME);
		if (isFire(races))
			text.append(((text.length() > 1) ? "/" : "") + FIRE_NAME);
		if (isLife(races))
			text.append(((text.length() > 1) ? "/" : "") + LIFE_NAME);
		if (isDeath(races))
			text.append(((text.length() > 1) ? "/" : "") + DEATH_NAME);

		return text.toString();
	}

	public static int[] asArray(int races) {
		ArrayList<Integer> list = new ArrayList<Integer>(6);

		if (isIce(races))
			list.add(ICE);
		if (isWind(races))
			list.add(WIND);
		if (isEarth(races))
			list.add(EARTH);
		if (isFire(races))
			list.add(FIRE);
		if (isLife(races))
			list.add(LIFE);
		if (isDeath(races))
			list.add(DEATH);

		int[] array = new int[list.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = list.get(i);
		}
		return array;
	}

	public static int getRaces(boolean[] racesSelected) {
		int selected = 0;
		for (int i = 0; i < racesSelected.length; i++) {
			if (racesSelected[i])
				selected |= ALL_RACES[i];
		}
		return selected;
	}

	public static boolean[] getRacesSelected(int races) {
		boolean[] selected = new boolean[ALL_RACES.length];

		for (int i = 0; i < ALL_RACES.length; i++) {
			selected[i] = (ALL_RACES[i] & races) > 0;
		}
		return selected;
	}

	public static boolean compareRaces(int[] arr, int races) {
		return combineRaces(arr) == races;
	}

	public static int getRaces(String... races) {
		int race = 0;
		for (String raceName : races) {
			if (raceName.equalsIgnoreCase(ICE_NAME))
				race |= ICE;
			if (raceName.equalsIgnoreCase(WIND_NAME))
				race |= WIND;
			if (raceName.equalsIgnoreCase(EARTH_NAME))
				race |= EARTH;
			if (raceName.equalsIgnoreCase(FIRE_NAME))
				race |= FIRE;
			if (raceName.equalsIgnoreCase(LIFE_NAME))
				race |= LIFE;
			if (raceName.equalsIgnoreCase(DEATH_NAME))
				race |= DEATH;
		}
		return race;
	}

}
