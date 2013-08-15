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
import java.util.Arrays;
import java.util.HashMap;

import com.godsandtowers.achievements.Achievement;
import com.godsandtowers.achievements.Achievements;
import com.godsandtowers.campaigns.Campaign;
import com.godsandtowers.campaigns.CampaignLevel;
import com.godsandtowers.sprites.BaseCreature;
import com.godsandtowers.sprites.BasePlayer;
import com.godsandtowers.sprites.BaseRace;
import com.godsandtowers.sprites.BaseSpecial;
import com.godsandtowers.sprites.BaseTower;
import com.godsandtowers.sprites.Races;
import com.gundogstudios.modules.Modules;

public class PlayerStats implements Externalizable {

	public static final int VERSION = 4;
	private int currentLevel;
	private long totalXP;
	private long longestGame;
	private long shortestGame;
	private long highestScore;
	private long highestLevelBeaten;
	private long wins;
	private long losses;
	private boolean multipleRacesUnlocked;

	private HashMap<Integer, Long> raceXP;
	private HashMap<Integer, Integer> defensiveGamesWon;
	private HashMap<Integer, Integer> battleGamesWon;
	private HashMap<Integer, Campaign> campaigns;
	private BasePlayer basePlayer;
	private BaseRace baseRace;
	private BaseCreature[] baseCreatures;
	private BaseTower[] baseTowers;
	private BaseSpecial[] specials;
	private Achievement[] achievements;

	public PlayerStats() {
	}

	public PlayerStats(int level) {
		reset();
		totalXP = getLevelXP(level);
		currentLevel = level;
	}

	public PlayerStats(int level, PlayerStats stats) {
		this(level, stats.basePlayer, stats.baseRace, stats.baseTowers, stats.baseCreatures);
	}

	public PlayerStats(int level, BasePlayer basePlayer, BaseRace baseRace, BaseTower[] baseTowers,
			BaseCreature[] baseCreatures) {
		this(level);
		this.basePlayer = new BasePlayer(basePlayer);
		this.baseRace = new BaseRace(baseRace);

		for (int i = 0; i < baseTowers.length; i++) {
			this.baseTowers[i] = new BaseTower(baseTowers[i]);
		}

		for (int i = 0; i < baseCreatures.length; i++) {
			this.baseCreatures[i] = new BaseCreature(baseCreatures[i]);
		}
	}

	protected PlayerStats(long totalXP, long longestGame, long shortestGame, long highestScore,
			long highestLevelBeaten, int currentLevel, long wins, long losses, boolean multipleRacesUnlocked) {
		reset();
		this.totalXP = totalXP;
		this.longestGame = longestGame;
		this.shortestGame = shortestGame;
		this.highestScore = highestScore;
		this.highestLevelBeaten = highestLevelBeaten;
		this.currentLevel = currentLevel;
		this.wins = wins;
		this.losses = losses;
		this.multipleRacesUnlocked = multipleRacesUnlocked;
	}

	public void reset() {
		totalXP = 0;
		longestGame = 0;
		shortestGame = Long.MAX_VALUE;
		highestScore = 0;
		highestLevelBeaten = 0;
		currentLevel = 0;
		wins = 0;
		losses = 0;
		multipleRacesUnlocked = false;
		raceXP = new HashMap<Integer, Long>();
		defensiveGamesWon = new HashMap<Integer, Integer>();
		battleGamesWon = new HashMap<Integer, Integer>();
		for (int race : Races.ALL_RACES) {
			raceXP.put(race, 0l);
			defensiveGamesWon.put(race, 0);
			battleGamesWon.put(race, 0);
		}
		campaigns = Campaign.generateCampaigns();
		basePlayer = BasePlayer.getBasePlayer();
		baseRace = BaseRace.getBaseRace();
		baseCreatures = BaseCreature.getBaseCreatures();
		baseTowers = BaseTower.getBaseTowers();
		specials = BaseSpecial.getSpecials();
		achievements = Achievements.getAchievements();
	}

	public long getRaceXP(int race) {
		return raceXP.get(race);
	}

	public void decreaseXP(int race, long amount) {
		long xp = raceXP.get(race) - amount;
		raceXP.put(race, xp);
	}

	public int getDefensiveGamesWon(int race) {
		return defensiveGamesWon.get(race);
	}

	public int getBattleGamesWon(int race) {
		return battleGamesWon.get(race);
	}

	public static long getLevelXP(long level) {
		return (level * (level + 1) / 2);
	}

	private void processRace(GameInfo gameInfo, int race, long xp) {
		xp = raceXP.get(race) + xp;
		raceXP.put(race, xp);
		if (gameInfo.won()) {
			switch (gameInfo.getGameType()) {
			case GameInfo.DEFENSE:
				int defenseWon = defensiveGamesWon.get(race);
				defensiveGamesWon.put(race, defenseWon + 1);
				break;
			case GameInfo.BATTLE:
				int battleWon = battleGamesWon.get(race);
				battleGamesWon.put(race, battleWon + 1);
				break;
			default:
				Modules.LOG.error("PlayerStats", "Unknown game type in player stats " + gameInfo.getGameType());
			}
		}
	}

	public void update(GameInfo gameInfo) {
		int races = gameInfo.getLocalPlayerRaces();
		CampaignLevel campaignLevel = gameInfo.getCampaignLevel();
		if (campaignLevel != null && gameInfo.won()) {
			int stars = gameInfo.getStarCount();
			campaignLevel.update(stars, gameInfo.getComputerLevel());
		}

		long xp = increaseXP(gameInfo);
		for (int race : Races.asArray(races)) {
			processRace(gameInfo, race, xp);
		}
		if (gameInfo.won())
			wins++;
		else
			losses++;

		if (longestGame < gameInfo.getLength())
			longestGame = gameInfo.getLength();
		if (shortestGame > gameInfo.getLength())
			shortestGame = gameInfo.getLength();
		if (highestLevelBeaten < gameInfo.getComputerLevel())
			highestLevelBeaten = gameInfo.getComputerLevel();
		if (highestScore < xp)
			highestScore = xp;

		for (Achievement achievement : achievements) {
			if (achievement.executeIfNotCompleted(this)) {
				// add achievement to list if it was executed for the first time
				gameInfo.addAchievement(achievement);
			}
		}
	}

	public long increaseXP(GameInfo info) {

		long xp = info.getXP();

		long xpToNextLevel = getLevelXP(currentLevel + 1) - totalXP;

		totalXP += xp;

		long temp = xp - xpToNextLevel;
		if (xp >= 0) {
			currentLevel++;

			long nextLevel = currentLevel + 1;

			while ((temp -= nextLevel) >= 0) {
				currentLevel++;
				nextLevel++;
			}
		}
		return xp;
	}

	public void doubleExperience() {
		for (int race : Races.ALL_RACES) {
			long xp = raceXP.get(race) * 2;
			raceXP.put(race, xp);
		}
	}

	public void unlockMultipleRaces() {
		multipleRacesUnlocked = true;
	}

	public void setMultipleRaces(boolean unlocked) {
		multipleRacesUnlocked = unlocked;
	}

	public long getTotalXP() {
		return totalXP;
	}

	public long getLongestGame() {
		return longestGame;
	}

	public long getShortestGame() {
		return shortestGame;
	}

	public long getHighestScore() {
		return highestScore;
	}

	public long getHighestLevelBeaten() {
		return highestLevelBeaten;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public long getWins() {
		return wins;
	}

	public long getLosses() {
		return losses;
	}

	public boolean areMultipleRacesUnlocked() {
		return multipleRacesUnlocked;
	}

	public boolean areAllRacesUnlocked() {
		return true;
	}

	public HashMap<Integer, Long> getRaceXP() {
		return raceXP;
	}

	public HashMap<Integer, Integer> getDefensiveGamesWon() {
		return defensiveGamesWon;
	}

	public HashMap<Integer, Integer> getBattleGamesWon() {
		return battleGamesWon;
	}

	public HashMap<Integer, Campaign> getCampaigns() {
		return campaigns;
	}

	public BasePlayer getBasePlayer() {
		return basePlayer;
	}

	public BaseRace getBaseRace() {
		return baseRace;
	}

	public BaseCreature[] getBaseCreatures() {
		return baseCreatures;
	}

	public BaseTower[] getBaseTowers() {
		return baseTowers;
	}

	public BaseSpecial[] getSpecials() {
		return specials;
	}

	public Achievement[] getAchievements() {
		return achievements;
	}

	@Override
	public String toString() {
		return "PlayerStats [totalXP=" + totalXP + ", longestGame=" + longestGame + ", shortestGame=" + shortestGame
				+ ", highestScore=" + highestScore + ", highestLevelBeaten=" + highestLevelBeaten + ", currentLevel="
				+ currentLevel + ", wins=" + wins + ", losses=" + losses + ", multipleRacesUnlocked="
				+ multipleRacesUnlocked + ", raceXP=" + raceXP + ", defensiveGamesWon=" + defensiveGamesWon
				+ ", battleGamesWon=" + battleGamesWon + ", campaigns=" + campaigns + ", basePlayer=" + basePlayer
				+ ", baseRace=" + baseRace + ", baseCreatures=" + Arrays.toString(baseCreatures) + ", baseTowers="
				+ Arrays.toString(baseTowers) + ", specials=" + Arrays.toString(specials) + ", achievements="
				+ Arrays.toString(achievements) + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
		currentLevel = input.readInt();
		totalXP = input.readLong();
		longestGame = input.readLong();
		shortestGame = input.readLong();
		highestScore = input.readLong();
		highestLevelBeaten = input.readLong();
		wins = input.readLong();
		losses = input.readLong();
		multipleRacesUnlocked = input.readBoolean();
		raceXP = (HashMap<Integer, Long>) input.readObject();
		defensiveGamesWon = (HashMap<Integer, Integer>) input.readObject();
		battleGamesWon = (HashMap<Integer, Integer>) input.readObject();
		campaigns = (HashMap<Integer, Campaign>) input.readObject();
		basePlayer = (BasePlayer) input.readObject();
		baseRace = (BaseRace) input.readObject();
		baseCreatures = (BaseCreature[]) input.readObject();
		baseTowers = (BaseTower[]) input.readObject();
		specials = (BaseSpecial[]) input.readObject();
		achievements = (Achievement[]) input.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput output) throws IOException {
		output.writeInt(currentLevel);
		output.writeLong(totalXP);
		output.writeLong(longestGame);
		output.writeLong(shortestGame);
		output.writeLong(highestScore);
		output.writeLong(highestLevelBeaten);
		output.writeLong(wins);
		output.writeLong(losses);
		output.writeBoolean(multipleRacesUnlocked);
		output.writeObject(raceXP);
		output.writeObject(defensiveGamesWon);
		output.writeObject(battleGamesWon);
		output.writeObject(campaigns);
		output.writeObject(basePlayer);
		output.writeObject(baseRace);
		output.writeObject(baseCreatures);
		output.writeObject(baseTowers);
		output.writeObject(specials);
		output.writeObject(achievements);
	}
}
