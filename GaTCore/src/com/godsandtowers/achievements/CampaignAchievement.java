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
import java.util.HashMap;

import com.godsandtowers.campaigns.Campaign;
import com.godsandtowers.campaigns.CampaignLevel;
import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.sprites.BaseSpecial;
import com.godsandtowers.sprites.Races;
import com.gundogstudios.util.FastMath;

public class CampaignAchievement extends Achievement {
	private int races;
	private int difficulty;

	public CampaignAchievement() {
	}

	public CampaignAchievement(String name, int achievementLevel, int races, int difficulty) {
		super("campaign_" + name + "_" + difficulty, achievementLevel);
		this.races = races;
		this.difficulty = difficulty;
	}

	@Override
	protected boolean execute(PlayerStats playerStats) {
		HashMap<Integer, Campaign> campaigns = playerStats.getCampaigns();

		int[] raceArray = Races.asArray(races);
		for (int race : raceArray) {
			Campaign campaign = campaigns.get(race);
			for (CampaignLevel level : campaign.getLevels()) {
				if (level.getStars() <= 0 || level.getDifficulty() < difficulty) {
					return false;
				}
			}
		}

		for (int race : raceArray) {
			for (BaseSpecial special : playerStats.getSpecials()) {
				if (Races.isRaces(special.getRaces(), race)) {
					for (int i = 0; i < achievementLevel * 5; i++) {
						special.upgrade(0);
					}
				}
			}
		}
		return true;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
		out.writeInt(races);
		out.writeInt(difficulty);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);
		races = in.readInt();
		difficulty = in.readInt();
	}

	@Override
	public float getPercentComplete(PlayerStats playerStats) {
		HashMap<Integer, Campaign> campaigns = playerStats.getCampaigns();
		float total = 0f;
		float completed = 0f;

		int[] raceArray = Races.asArray(races);
		for (int race : raceArray) {
			Campaign campaign = campaigns.get(race);
			for (CampaignLevel level : campaign.getLevels()) {
				if (level.getStars() > 0 && level.getDifficulty() >= difficulty) {
					completed++;
				}
				total++;
			}
		}

		return FastMath.min(1f, completed / total);
	}
}