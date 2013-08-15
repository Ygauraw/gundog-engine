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

public class Race implements Externalizable {

	private BaseRace baseRace;
	private int races;

	public Race() {

	}

	public Race(BaseRace baseRace, int races) {
		this.baseRace = baseRace;
		this.races = races;
	}

	public int getRaces() {
		return races;
	}

	public BaseRace getBaseRace() {
		return baseRace;
	}

	public boolean isFire() {
		return Races.isFire(races);
	}

	public boolean isIce() {
		return Races.isIce(races);
	}

	public boolean isWind() {
		return Races.isWind(races);
	}

	public boolean isEarth() {
		return Races.isEarth(races);
	}

	public boolean isLife() {
		return Races.isLife(races);
	}

	public boolean isDeath() {
		return Races.isDeath(races);
	}

	public boolean isRaces(int races) {
		return Races.isRaces(this.races, races);
	}

	public float getSplashRadius() {
		if (isFire())
			return baseRace.getSplashRadius();
		else
			return 0;
	}

	public float getSplashStrengthFade() {
		if (isFire())
			return baseRace.getSplashStrengthFade();
		else
			return 0;
	}

	public float getCreatureDamageModifier() {
		if (isFire())
			return baseRace.getCreatureDamageModifier();
		else
			return 1f;
	}

	public float getSlowDuration() {
		if (isIce())
			return baseRace.getSlowDuration();
		else
			return 0;
	}

	public float getSlowFactor() {
		if (isIce())
			return baseRace.getSlowFactor();
		else
			return 0;
	}

	public float getCreatureEvadePercentage() {
		if (isIce())
			return baseRace.getCreatureEvadePercentage();
		else
			return 0;
	}

	public float getStunDuration() {
		if (isWind())
			return baseRace.getStunDuration();
		else
			return 0;
	}

	public float getTowerAttackRateModifier() {
		if (isWind())
			return baseRace.getTowerAttackRateModifier();
		else
			return 1f;
	}

	public float getCreatureSpeedModifier() {
		if (isWind())
			return baseRace.getCreatureSpeedModifier();
		else
			return 1f;
	}

	public float getTowerDamageModifier() {
		if (isEarth())
			return baseRace.getTowerDamageModifier();
		else
			return 1f;
	}

	public float getTowerDefenseModifier() {
		if (isEarth())
			return baseRace.getTowerDefenseModifier();
		else
			return 1f;
	}

	public float getCreatureDefenseModifier() {
		if (isEarth())
			return baseRace.getCreatureDefenseModifier();
		else
			return 1f;
	}

	public float getDrainDuration() {
		if (isLife())
			return baseRace.getDrainDuration();
		else
			return 0;
	}

	public float getDrainPercentage() {
		if (isLife())
			return baseRace.getDrainPercentage();
		else
			return 0;
	}

	public float getHealPercentage() {
		if (isLife())
			return baseRace.getHealPercentage();
		else
			return 0;
	}

	public float getKillPercentage() {
		if (isDeath())
			return baseRace.getKillPercentage();
		else
			return 0f;
	}

	public float getCreatureHealthModifier() {
		if (isDeath())
			return baseRace.getCreatureHealthModifier();
		else
			return 1f;
	}

	public float getResurrectPercentage() {
		if (isDeath())
			return baseRace.getResurrectPercentage();
		else
			return 0;
	}

	@Override
	public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
		baseRace = (BaseRace) input.readObject();
		races = input.readInt();

	}

	@Override
	public void writeExternal(ObjectOutput output) throws IOException {
		output.writeObject(baseRace);
		output.writeInt(races);
	}
}
