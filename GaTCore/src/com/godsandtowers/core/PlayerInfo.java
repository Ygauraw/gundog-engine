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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

public class PlayerInfo implements Externalizable {

	private long level;
	private int races;
	private float score;
	private ArrayList<Float> life;
	private ArrayList<Float> income;
	private ArrayList<Float> defensivePower;
	private ArrayList<Float> offensivePower;

	public PlayerInfo() {

	}

	public PlayerInfo(long level, int races, float startingLife, float startingGold) {
		this.level = level;
		this.races = races;
		this.score = 0;
		life = new ArrayList<Float>();
		life.add(startingLife);
		income = new ArrayList<Float>();
		income.add(startingGold);
		defensivePower = new ArrayList<Float>();
		defensivePower.add(0f);
		offensivePower = new ArrayList<Float>();
		offensivePower.add(0f);
	}

	public long getLevel() {
		return level;
	}

	public float getScore() {
		return score;
	}

	public int getRaces() {
		return races;
	}

	public float getFirstLife() {
		return life.get(0);
	}

	public float getLastLife() {
		return life.get(life.size() - 1);
	}

	public float getCurrentDefensivePower() {
		return defensivePower.get(defensivePower.size() - 1);
	}

	public float getCurrentOffensivePower() {
		return offensivePower.get(offensivePower.size() - 1);
	}

	public ArrayList<Float> getLife() {
		return life;
	}

	public ArrayList<Float> getIncome() {
		return income;
	}

	public ArrayList<Float> getDefensivePower() {
		return defensivePower;
	}

	public ArrayList<Float> getOffensivePower() {
		return offensivePower;
	}

	public void addScore(float value) {
		score += value;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public void setLife(float value, int currentWave) {

		if (life.size() < currentWave) {
			life.add(value);
		} else {
			life.set(currentWave - 1, value);
		}
	}

	public void setIncome(float value, int currentWave) {

		if (income.size() < currentWave) {
			income.add(value);
		} else {
			income.set(currentWave - 1, value);
		}
	}

	public void setDefensivePower(float value, int currentWave) {
		if (defensivePower.size() < currentWave) {
			defensivePower.add(value);
		} else {
			defensivePower.set(currentWave - 1, value);
		}
	}

	public void setOffensivePower(float value, int currentWave) {
		if (offensivePower.size() < currentWave) {
			offensivePower.add(value);
		} else {
			offensivePower.set(currentWave - 1, value);
		}
	}

	public void addOffensivePower(float value, int currentWave) {
		float currentPower = offensivePower.get(offensivePower.size() - 1);
		while (offensivePower.size() < currentWave) {
			offensivePower.add(currentPower);
		}
		offensivePower.set(currentWave - 1, currentPower + value);
	}

	public void addDefensivePower(float value, int currentWave) {
		float currentPower = defensivePower.get(defensivePower.size() - 1);
		while (defensivePower.size() < currentWave) {
			defensivePower.add(currentPower);
		}
		defensivePower.set(currentWave - 1, currentPower + value);

	}

	public void subtractDefensivePower(float value, int currentWave) {
		float currentPower = defensivePower.get(defensivePower.size() - 1);
		while (defensivePower.size() < currentWave) {
			defensivePower.add(currentPower);
		}
		defensivePower.set(currentWave - 1, currentPower - value);

	}

	@Override
	public String toString() {
		return "PlayerInfo [level=" + level + ", races=" + races + ", life=" + life + ", income=" + income
				+ ", defensivePower=" + defensivePower + ", offensivePower=" + offensivePower + "]";
	}

	@Override
	public void writeExternal(ObjectOutput output) throws IOException {

		output.writeLong(level);
		output.writeInt(races);
		output.writeFloat(score);
		output.writeObject(life);
		output.writeObject(income);
		output.writeObject(defensivePower);
		output.writeObject(offensivePower);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {

		level = input.readLong();
		races = input.readInt();
		score = input.readFloat();
		life = (ArrayList<Float>) input.readObject();
		income = (ArrayList<Float>) input.readObject();
		defensivePower = (ArrayList<Float>) input.readObject();
		offensivePower = (ArrayList<Float>) input.readObject();

	}

}