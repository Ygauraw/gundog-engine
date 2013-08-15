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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class BaseSpecial implements Upgradeable, Externalizable {
	public static final String DAMAGE_CREATURES = "special_damage";
	public static final String HEAL_CREATURES = "special_heal";
	public static final String KILL_CREATURES = "special_kill";
	public static final String SLOW_CREATURES = "special_slow";
	public static final String SPEEDUP_CREATURES = "special_speed";
	public static final String STUN_CREATURES = "special_stun";

	public static BaseSpecial[] getSpecials() {
		return new BaseSpecial[] { new BaseSpecial(DAMAGE_CREATURES, Races.combineRaces(Races.FIRE)),
				new BaseSpecial(HEAL_CREATURES, Races.combineRaces(Races.LIFE)),
				new BaseSpecial(KILL_CREATURES, Races.combineRaces(Races.DEATH)),
				new BaseSpecial(SLOW_CREATURES, Races.combineRaces(Races.ICE)),
				new BaseSpecial(SPEEDUP_CREATURES, Races.combineRaces(Races.WIND)),
				new BaseSpecial(STUN_CREATURES, Races.combineRaces(Races.EARTH)) };
	}

	private String name;
	private int races;
	private int count;

	public BaseSpecial() {
	}

	private BaseSpecial(String name, int races) {
		this.name = name;
		this.races = races;
		this.count = 0;
	}

	public void decrement() {
		count--;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	public int getRaces() {
		return races;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + count;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + races;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseSpecial other = (BaseSpecial) obj;
		if (count != other.count)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (races != other.races)
			return false;
		return true;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getUpgradeName(int id) {
		return name;
	}

	@Override
	public int[] getUpgradeIDs() {
		return new int[] { 0 };
	}

	@Override
	public long getUpgradeCost(int id) {
		return (long) Math.pow(10, count);
	}

	@Override
	public float getBaseValue(int id) {
		return count;
	}

	@Override
	public float getUpgradedValue(int id) {
		return count++;
	}

	@Override
	public void upgrade(int id) {
		count++;
	}

	@Override
	public int getUpgradeCount(int id) {
		return count;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(name);
		out.writeInt(count);
		out.writeInt(races);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		name = (String) in.readObject();
		count = in.readInt();
		races = in.readInt();
	}

}
