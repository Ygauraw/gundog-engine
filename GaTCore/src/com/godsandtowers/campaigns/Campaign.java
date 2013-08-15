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
package com.godsandtowers.campaigns;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;

import com.godsandtowers.core.GameInfo;
import com.godsandtowers.core.grid.Boards;
import com.godsandtowers.sprites.Races;

public class Campaign implements Externalizable {

	public static HashMap<Integer, Campaign> generateCampaigns() {
		HashMap<Integer, Campaign> campaigns = new HashMap<Integer, Campaign>();

		campaigns.put(Races.ICE, new Campaign(Races.ICE));

		campaigns.put(Races.WIND, new Campaign(Races.WIND));

		campaigns.put(Races.EARTH, new Campaign(Races.EARTH));

		campaigns.put(Races.FIRE, new Campaign(Races.FIRE));

		campaigns.put(Races.LIFE, new Campaign(Races.LIFE));

		campaigns.put(Races.DEATH, new Campaign(Races.DEATH));

		return campaigns;
	}

	private static final int BATTLE_WAVES = Integer.MAX_VALUE;

	private int races;
	private CampaignLevel[] levels;

	public Campaign() {

	}

	private Campaign(int races) {
		this.races = races;
		this.levels = new CampaignLevel[Races.ALL_RACES.length * 2];

		int current = 0;

		for (int race : Races.ALL_RACES) {
			levels[current] = new CampaignLevel(GameInfo.BATTLE, BATTLE_WAVES, races, race,
					Boards.ALL_BOARDS[current].getName());
			current++;
		}

		for (int race : Races.ALL_RACES) {
			levels[current] = new CampaignLevel(GameInfo.BATTLE, BATTLE_WAVES, races, race,
					Boards.ALL_BOARDS[current].getName());
			current++;
		}

	}

	public int getRaces() {
		return races;
	}

	public CampaignLevel[] getLevels() {
		return levels;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(races);
		out.writeObject(levels);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		races = in.readInt();
		levels = (CampaignLevel[]) in.readObject();
	}
}
