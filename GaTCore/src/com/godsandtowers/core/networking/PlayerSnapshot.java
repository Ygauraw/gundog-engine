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

import java.util.ArrayList;

public class PlayerSnapshot {
	private int id;

	private int creatureLevel;

	private float income;

	private float gold;

	private float life;

	private float score;

	private float defensivePower;

	private float offensivePower;

	private ArrayList<SpriteSnapshot> sprites;

	public PlayerSnapshot(int id, int creatureLevel, float income, float gold, float life, float score,
			float defensivePower, float offensivePower, ArrayList<SpriteSnapshot> sprites) {
		this.id = id;
		this.creatureLevel = creatureLevel;
		this.income = income;
		this.gold = gold;
		this.life = life;
		this.score = score;
		this.defensivePower = defensivePower;
		this.offensivePower = offensivePower;
		this.sprites = sprites;
	}

	public float getDefensivePower() {
		return defensivePower;
	}

	public float getOffensivePower() {
		return offensivePower;
	}

	public int getId() {
		return id;
	}

	public int getCreatureLevel() {
		return creatureLevel;
	}

	public float getIncome() {
		return income;
	}

	public float getGold() {
		return gold;
	}

	public float getLife() {
		return life;
	}

	public float getScore() {
		return score;
	}

	public ArrayList<SpriteSnapshot> getSprites() {
		return sprites;
	}

	@Override
	public String toString() {
		return "PlayerSnapshot [id=" + id + ", creatureLevel=" + creatureLevel + ", income=" + income + ", gold="
				+ gold + ", life=" + life + ", score=" + score + ", defensivePower=" + defensivePower
				+ ", offensivePower=" + offensivePower + ", sprites=" + sprites + "]";
	}

}
