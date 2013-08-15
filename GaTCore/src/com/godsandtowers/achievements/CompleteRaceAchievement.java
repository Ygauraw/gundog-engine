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
import com.godsandtowers.sprites.BaseSpecial;
import com.godsandtowers.sprites.BaseTower;
import com.godsandtowers.sprites.Races;
import com.gundogstudios.util.FastMath;

public class CompleteRaceAchievement extends Achievement {
	private int race;

	public CompleteRaceAchievement() {
	}

	public CompleteRaceAchievement(String name, int achievementLevel, int race) {
		super(name, achievementLevel);
		this.race = race;
	}

	@Override
	protected boolean execute(PlayerStats playerStats) {
		for (BaseCreature creature : playerStats.getBaseCreatures()) {
			if (Races.isRaces(creature.getRaces(), race) && !creature.isUnlocked()) {
				return false;
			}
		}
		for (BaseTower tower : playerStats.getBaseTowers()) {
			if (Races.isRaces(tower.getRaces(), race) && !tower.isUnlocked()) {
				return false;
			}
		}
		for (BaseSpecial special : playerStats.getSpecials()) {
			if (Races.isRaces(special.getRaces(), race)) {
				for (int i = 0; i < achievementLevel * 5; i++) {
					special.upgrade(0);
				}
			}
		}
		return true;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
		out.writeInt(race);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);
		race = in.readInt();
	}

	@Override
	public float getPercentComplete(PlayerStats playerStats) {
		float total = 0f;
		float completed = 0f;
		for (BaseCreature creature : playerStats.getBaseCreatures()) {
			if (Races.isRaces(creature.getRaces(), race)) {
				if (creature.isUnlocked()) {
					completed++;
				}
				total++;
			}
		}
		for (BaseTower tower : playerStats.getBaseTowers()) {
			if (Races.isRaces(tower.getRaces(), race)) {
				if (tower.isUnlocked()) {
					completed++;
				}
				total++;
			}
		}
		return FastMath.min(1f, completed / total);
	}
}