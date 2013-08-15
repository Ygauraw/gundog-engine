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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

import com.godsandtowers.achievements.Achievement;
import com.godsandtowers.campaigns.Campaign;
import com.godsandtowers.campaigns.CampaignLevel;
import com.godsandtowers.sprites.BaseCreature;
import com.godsandtowers.sprites.BasePlayer;
import com.godsandtowers.sprites.BaseRace;
import com.godsandtowers.sprites.BaseSpecial;
import com.godsandtowers.sprites.BaseTower;
import com.godsandtowers.sprites.Races;
import com.godsandtowers.sprites.Upgradeable;

public class PlayerSaver {
	// TODO mask will be a device dependent # and utilize a cipher
	private static final int mask = 234758624;

	public static PlayerStats load(InputStream in) throws IOException, ClassNotFoundException {

		int length = in.read() << (Byte.SIZE * 3);
		length |= in.read() << (Byte.SIZE * 2);
		length |= in.read() << (Byte.SIZE);
		length |= in.read();

		byte[] bytes = new byte[length];
		in.read(bytes);
		decrypt(bytes, mask);

		ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);

		ObjectInputStream stream = new ObjectInputStream(byteStream);

		int version = stream.readInt();

		if (version < 3)
			return null;

		// BasicStats
		int currentLevel = stream.readInt();
		long totalXP = stream.readLong();
		long longestGame = stream.readLong();
		long shortestGame = stream.readLong();
		long highestScore = stream.readLong();
		long highestLevelBeaten = stream.readLong();
		long wins = stream.readLong();
		long losses = stream.readLong();
		boolean multipleRacesUnlocked = stream.readBoolean();

		if (version < 4)
			stream.readBoolean();

		// CreatePlayer
		PlayerStats stats = new PlayerStats(totalXP, longestGame, shortestGame, highestScore, highestLevelBeaten,
				currentLevel, wins, losses, multipleRacesUnlocked);

		// Races
		int raceCount = stream.readInt();
		int[] races = new int[raceCount];
		for (int i = 0; i < raceCount; i++) {
			races[i] = stream.readInt();
		}

		// RaceXP
		HashMap<Integer, Long> raceXP = stats.getRaceXP();
		for (int i = 0; i < raceCount; i++) {
			long xp = stream.readLong();
			raceXP.put(races[i], xp);
		}

		// DefensiveGamesWon
		HashMap<Integer, Integer> defensiveGamesWon = stats.getDefensiveGamesWon();
		for (int i = 0; i < raceCount; i++) {
			int defenseWins = stream.readInt();
			defensiveGamesWon.put(races[i], defenseWins);
		}

		// BattleGamesWon
		HashMap<Integer, Integer> battleGamesWon = stats.getBattleGamesWon();
		for (int i = 0; i < raceCount; i++) {
			int battleWins = stream.readInt();
			battleGamesWon.put(races[i], battleWins);
		}

		// Campaigns
		HashMap<Integer, Campaign> campaigns = stats.getCampaigns();
		for (int i = 0; i < raceCount; i++) {
			Campaign campaign = campaigns.get(races[i]);
			int levelCount = stream.readInt();
			CampaignLevel[] levels = campaign.getLevels();
			for (int j = 0; j < levelCount; j++) {
				int stars = stream.readInt();
				int level = stream.readInt();
				levels[j].update(stars, level);
			}
		}

		// BasePlayer
		String name = stream.readUTF();
		BasePlayer basePlayer = stats.getBasePlayer();
		read(stream, basePlayer);

		// BaseRace
		name = stream.readUTF();
		BaseRace baseRace = stats.getBaseRace();
		read(stream, baseRace);

		// BaseCreatures
		HashMap<String, BaseCreature> creatures = new HashMap<String, BaseCreature>();
		for (BaseCreature creature : stats.getBaseCreatures()) {
			creatures.put(creature.getName(), creature);
		}

		length = stream.readInt();
		for (int i = 0; i < length; i++) {
			boolean unlocked = stream.readBoolean();
			name = stream.readUTF();
			BaseCreature creature = creatures.get(name);
			creature.setUnlocked(unlocked);
			read(stream, creature);
		}

		// BaseTowers
		HashMap<String, BaseTower> towers = new HashMap<String, BaseTower>();
		for (BaseTower tower : stats.getBaseTowers()) {
			towers.put(tower.getName(), tower);
		}

		length = stream.readInt();
		for (int i = 0; i < length; i++) {
			boolean unlocked = stream.readBoolean();
			name = stream.readUTF();
			BaseTower tower = towers.get(name);
			tower.setUnlocked(unlocked);
			read(stream, tower);
		}

		// Specials
		HashMap<String, BaseSpecial> specials = new HashMap<String, BaseSpecial>();
		for (BaseSpecial special : stats.getSpecials()) {
			specials.put(special.getName(), special);
		}

		length = stream.readInt();
		for (int i = 0; i < length; i++) {
			name = stream.readUTF();
			BaseSpecial special = specials.get(name);
			read(stream, special);
		}

		// Achievements
		HashMap<String, Achievement> achievements = new HashMap<String, Achievement>();
		for (Achievement achievement : stats.getAchievements()) {
			achievements.put(achievement.getName(), achievement);
		}

		length = stream.readInt();
		for (int i = 0; i < length; i++) {
			name = stream.readUTF();
			Achievement achievement = achievements.get(name);
			boolean completed = stream.readBoolean();
			achievement.setCompleted(completed);
		}

		return stats;
	}

	private static void read(ObjectInputStream stream, Upgradeable upgradeable) throws IOException {
		int length = stream.readInt();
		for (int i = 0; i < length; i++) {
			int id = stream.readInt();
			int count = stream.readInt();
			for (int j = 1; j < count; j++) {
				upgradeable.upgrade(id);
			}
		}
	}

	public static void write(PlayerStats stats, OutputStream out) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream stream = new ObjectOutputStream(byteStream);
		stream.writeInt(PlayerStats.VERSION);

		// BasicStats
		stream.writeInt(stats.getCurrentLevel());
		stream.writeLong(stats.getTotalXP());
		stream.writeLong(stats.getLongestGame());
		stream.writeLong(stats.getShortestGame());
		stream.writeLong(stats.getHighestScore());
		stream.writeLong(stats.getHighestLevelBeaten());
		stream.writeLong(stats.getWins());
		stream.writeLong(stats.getLosses());
		stream.writeBoolean(stats.areMultipleRacesUnlocked());

		// Races
		int[] races = Races.ALL_RACES;
		stream.writeInt(races.length);
		for (int race : races) {
			stream.writeInt(race);
		}

		// RaceXP
		HashMap<Integer, Long> raceXP = stats.getRaceXP();
		for (int race : races) {
			stream.writeLong(raceXP.get(race));
		}

		// DefensiveGamesWon
		HashMap<Integer, Integer> defensiveGamesWon = stats.getDefensiveGamesWon();
		for (int race : races) {
			stream.writeInt(defensiveGamesWon.get(race));
		}

		// BattleGamesWon
		HashMap<Integer, Integer> battleGamesWon = stats.getBattleGamesWon();
		for (int race : races) {
			stream.writeInt(battleGamesWon.get(race));
		}

		// Campaigns
		HashMap<Integer, Campaign> campaigns = stats.getCampaigns();
		for (int race : races) {
			Campaign campaign = campaigns.get(race);
			CampaignLevel[] levels = campaign.getLevels();
			stream.writeInt(levels.length);
			for (int j = 0; j < levels.length; j++) {
				stream.writeInt(levels[j].getStars());
				stream.writeInt(levels[j].getDifficulty());
			}
		}

		// BasePlayer
		BasePlayer basePlayer = stats.getBasePlayer();
		write(stream, basePlayer);

		// BaseRace
		BaseRace baseRace = stats.getBaseRace();
		write(stream, baseRace);

		// BaseCreatures
		BaseCreature[] baseCreatures = stats.getBaseCreatures();
		stream.writeInt(baseCreatures.length);
		for (BaseCreature creature : baseCreatures) {
			stream.writeBoolean(creature.isUnlocked());
			write(stream, creature);
		}

		// BaseTowers
		BaseTower[] baseTowers = stats.getBaseTowers();
		stream.writeInt(baseTowers.length);
		for (BaseTower tower : baseTowers) {
			stream.writeBoolean(tower.isUnlocked());
			write(stream, tower);
		}

		// Specials
		BaseSpecial[] specials = stats.getSpecials();
		stream.writeInt(specials.length);
		for (BaseSpecial special : specials) {
			write(stream, special);
		}

		// Achievements
		Achievement[] achievements = stats.getAchievements();
		stream.writeInt(achievements.length);
		for (Achievement achievement : achievements) {
			stream.writeUTF(achievement.getName());
			stream.writeBoolean(achievement.isCompleted());
		}

		stream.flush();
		byteStream.flush();
		byte[] bytes = byteStream.toByteArray();
		encrypt(bytes, mask);

		int value = bytes.length;
		out.write(value >> (Byte.SIZE * 3));
		out.write(value >> (Byte.SIZE * 2));
		out.write(value >> Byte.SIZE);
		out.write(value);
		out.write(bytes);
	}

	private static void encrypt(byte[] bytes, int mask) {

		for (int i = 0; i < bytes.length - 1; i += 2) {
			bytes[i] = (byte) ~bytes[i];
			bytes[i + 1] = (byte) ~bytes[i + 1];
			bytes[i] = (byte) (bytes[i] ^ bytes[i + 1]);
			bytes[i + 1] = (byte) (bytes[i] ^ bytes[i + 1]);
			bytes[i] = (byte) (bytes[i] ^ bytes[i + 1]);
		}
	}

	private static void decrypt(byte[] bytes, int mask) {

		for (int i = 0; i < bytes.length - 1; i += 2) {
			bytes[i] = (byte) ~bytes[i];
			bytes[i + 1] = (byte) ~bytes[i + 1];
			bytes[i] = (byte) (bytes[i] ^ bytes[i + 1]);
			bytes[i + 1] = (byte) (bytes[i] ^ bytes[i + 1]);
			bytes[i] = (byte) (bytes[i] ^ bytes[i + 1]);
		}
	}

	private static void write(ObjectOutputStream stream, Upgradeable upgradeable) throws IOException {
		stream.writeUTF(upgradeable.getName());
		int[] upgradeIDs = upgradeable.getUpgradeIDs();
		stream.writeInt(upgradeIDs.length);
		for (int id : upgradeIDs) {
			stream.writeInt(id);
			stream.writeInt(upgradeable.getUpgradeCount(id));
		}
	}
}
