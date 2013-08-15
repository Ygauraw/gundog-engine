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
package com.godsandtowers.achievements;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.sprites.BaseCreature;
import com.godsandtowers.sprites.BaseTower;
import com.godsandtowers.sprites.Races;
import com.gundogstudios.util.FastMath;

public class UnlockAchievement extends Achievement {
	private int wins;

	public UnlockAchievement() {
	}

	public UnlockAchievement(String name, int achievementLevel, int wins) {
		super(name, achievementLevel);
		this.wins = wins;
	}

	@Override
	protected boolean execute(PlayerStats playerStats) {

		int races;
		BaseTower tower = BaseTower.getBaseTower(playerStats.getBaseTowers(), name);
		BaseCreature creature = BaseCreature.getBaseCreature(playerStats.getBaseCreatures(), name);
		if (tower == null && creature == null) {
			throw new RuntimeException("Unknown creature/tower name in UnlockAchievement: " + name);
		} else if (tower == null) {
			races = creature.getRaces();
		} else {
			races = tower.getRaces();
		}

		boolean hasWins = true;
		for (int race : Races.asArray(races)) {
			int gamesWon = playerStats.getDefensiveGamesWon(race) + playerStats.getBattleGamesWon(race);
			if (gamesWon < wins) {
				hasWins = false;
			}
		}

		if (hasWins) {
			if (tower != null)
				tower.setUnlocked(true);
			if (creature != null)
				creature.setUnlocked(true);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
		out.writeInt(wins);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);
		wins = in.readInt();
	}

	@Override
	public float getPercentComplete(PlayerStats playerStats) {
		float total = 0f;
		float completed = 0f;

		int races;
		BaseTower tower = BaseTower.getBaseTower(playerStats.getBaseTowers(), name);
		BaseCreature creature = BaseCreature.getBaseCreature(playerStats.getBaseCreatures(), name);
		if (tower == null && creature == null) {
			throw new RuntimeException("Unknown creature/tower name in UnlockAchievement: " + name);
		} else if (tower == null) {
			races = creature.getRaces();
		} else {
			races = tower.getRaces();
		}

		for (int race : Races.asArray(races)) {
			int gamesWon = playerStats.getDefensiveGamesWon(race) + playerStats.getBattleGamesWon(race);
			if (gamesWon < wins) {
				completed += FastMath.min(gamesWon, wins);
			}
			total += wins;
		}

		return FastMath.min(1f, completed / total);
	}
}