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
import com.godsandtowers.sprites.BaseSpecial;
import com.godsandtowers.sprites.Races;
import com.gundogstudios.util.FastMath;

public class BattleAchievement extends Achievement {
	private int races;
	private int wins;

	public BattleAchievement() {
	}

	public BattleAchievement(String baseName, int achievementLevel, int races, int wins) {
		super("battle_" + baseName + "_" + wins, achievementLevel);
		this.races = races;
		this.wins = wins;
	}

	@Override
	protected boolean execute(PlayerStats playerStats) {
		if (playerStats.getBattleGamesWon(races) < wins) {
			return false;
		}
		for (BaseSpecial special : playerStats.getSpecials()) {
			if (Races.isRaces(special.getRaces(), races)) {
				for (int i = 0; i < achievementLevel * 5; i++) {
					special.upgrade(0);
				}
			}
		}
		return true;
	}

	@Override
	public float getPercentComplete(PlayerStats playerStats) {
		return FastMath.min(1f, ((float) playerStats.getBattleGamesWon(races)) / ((float) wins));
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
		out.writeInt(races);
		out.writeInt(wins);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);
		races = in.readInt();
		wins = in.readInt();
	}

}