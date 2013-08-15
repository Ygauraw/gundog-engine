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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.godsandtowers.campaigns.Campaign;
import com.godsandtowers.campaigns.CampaignLevel;
import com.godsandtowers.core.PlayerSaver;
import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.sprites.BaseCreature;
import com.godsandtowers.sprites.BasePlayer;
import com.godsandtowers.sprites.Races;
import com.godsandtowers.sprites.Upgradeable;

public class PlayerSaverTester {

	private PlayerStats stats;

	@Before
	public void setUp() {
		int currentLevel = 8;
		long totalXP = 1000;
		long longestGame = 4000;
		long shortestGame = 200;
		long highestScore = 5000;
		long highestLevelBeaten = 5;
		long wins = 20;
		long losses = 27;
		boolean multipleRacesUnlocked = true;
		stats = new PlayerStats(totalXP, longestGame, shortestGame, highestScore, highestLevelBeaten, currentLevel,
				wins, losses, multipleRacesUnlocked);

		int[] races = Races.ALL_RACES;
		int raceCount = races.length;
		HashMap<Integer, Long> raceXP = stats.getRaceXP();

		long xp = 200;
		for (int i = 0; i < raceCount; i++) {
			raceXP.put(races[i], xp);
			xp *= 2;
		}

		int defenseWins = 4;
		HashMap<Integer, Integer> defensiveGamesWon = stats.getDefensiveGamesWon();
		for (int i = 0; i < raceCount; i++) {
			defensiveGamesWon.put(races[i], defenseWins);
			defenseWins *= 4;
		}

		HashMap<Integer, Campaign> campaigns = stats.getCampaigns();
		for (int i = 0; i < raceCount; i++) {
			Campaign campaign = campaigns.get(races[i]);
			CampaignLevel[] levels = campaign.getLevels();
			levels[2].update(i, CampaignLevel.EASY);
		}

		BasePlayer player = stats.getBasePlayer();
		upgrade(player);

		for (BaseCreature creature : stats.getBaseCreatures()) {
			upgrade(creature);
		}

	}

	private void upgrade(Upgradeable upgradeable) {
		int count = 2;
		int base = 2;
		for (int id : upgradeable.getUpgradeIDs()) {
			while (count-- >= 0)
				upgradeable.upgrade(id);
			count = base;
			base *= 2;
		}
	}

	@Test
	public void testSave() throws IOException, ClassNotFoundException {
		String fileName = "temp/file.txt";
		new File(fileName).delete();
		FileOutputStream out = new FileOutputStream(fileName);
		PlayerSaver.write(stats, out);
		out.flush();
		FileInputStream in = new FileInputStream(fileName);
		PlayerStats other = PlayerSaver.load(in);

		assertEquals("totalXP", stats.getTotalXP(), other.getTotalXP());
		assertEquals("FIRE XP", stats.getRaceXP(Races.FIRE), other.getRaceXP(Races.FIRE));
		assertEquals("LIFE XP", stats.getRaceXP(Races.LIFE), other.getRaceXP(Races.LIFE));
		assertEquals("DEATH WINS", stats.getDefensiveGamesWon(Races.DEATH), other.getDefensiveGamesWon(Races.DEATH));
		assertEquals("WIND WINS", stats.getDefensiveGamesWon(Races.WIND), other.getDefensiveGamesWon(Races.WIND));

		assertEqual("BasePlayer", stats.getBasePlayer(), other.getBasePlayer());

		BaseCreature[] oneCreatures = stats.getBaseCreatures();
		BaseCreature[] twoCreatures = other.getBaseCreatures();
		assertEquals("Creatures Size", oneCreatures.length, twoCreatures.length);
		for (int i = 0; i < oneCreatures.length; i++) {
			assertEqual(oneCreatures[i].getName(), oneCreatures[i], twoCreatures[i]);
		}

		int[] races = Races.ALL_RACES;
		int raceCount = races.length;

		HashMap<Integer, Campaign> oneCampaigns = stats.getCampaigns();
		HashMap<Integer, Campaign> twoCampaigns = other.getCampaigns();
		for (int i = 0; i < raceCount; i++) {
			Campaign onecampaign = oneCampaigns.get(races[i]);
			Campaign twocampaign = twoCampaigns.get(races[i]);
			CampaignLevel[] onelevels = onecampaign.getLevels();
			CampaignLevel[] twolevels = twocampaign.getLevels();
			assertEquals("Stars", onelevels[2].getStars(), twolevels[2].getStars());
		}

	}

	private void assertEqual(String message, Upgradeable one, Upgradeable two) {
		for (int id : one.getUpgradeIDs()) {
			assertEquals(message + " upgrade count " + id, one.getUpgradeCount(id), two.getUpgradeCount(id));
			assertEquals(message + " upgrade cost " + id, one.getUpgradeCost(id), two.getUpgradeCost(id));
		}
	}
}
