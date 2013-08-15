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
import java.util.Arrays;

import com.godsandtowers.achievements.Achievement;
import com.godsandtowers.campaigns.CampaignLevel;
import com.godsandtowers.core.grid.Board;
import com.godsandtowers.sprites.Creature;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.sprites.Races;
import com.godsandtowers.sprites.Tower;

public class GameInfo implements Externalizable {
	private static final long AVERAGE_GAME_LENGTH = 10 * 60 * 1000; // in minutes
	public static final int DEFENSE = 0;
	public static final int BATTLE = 1;
	public static final int TUTORIAL = 2;

	public static final int SLOW = 15000; // ms
	public static final int NORMAL = 10000; // ms
	public static final int FAST = 5000; // ms
	public static final int BATTLE_WAVES = Integer.MAX_VALUE;
	public static final int NORMAL_WAVES = 100;

	private boolean noXP;
	private boolean won;
	private boolean disconnected;
	private int localPlayerID;
	private int gameType;
	private int timeUntilNextWave;
	private int timeBetweenWaves;
	private int currentWave;
	private int maxWaves;
	private int achievementModifier;
	private long length;

	private Board board;
	private CampaignLevel campaignLevel;
	private PlayerInfo[] playerInfos;
	private Player[] players;
	private ArrayList<Achievement> achievements;

	public GameInfo() {

	}

	public GameInfo(int localPlayerID, Player[] players, int timeUntilNextWave, int gameType, int maxWaves, Board board) {
		this(localPlayerID, players, timeUntilNextWave, gameType, maxWaves, null, board);
	}

	public GameInfo(int localPlayerID, Player[] players, int timeUntilNextWave, int gameType, int maxWaves,
			CampaignLevel campaignLevel, Board board) {
		this.board = board;
		this.players = players;
		this.localPlayerID = localPlayerID;
		this.campaignLevel = campaignLevel;
		this.gameType = gameType;
		this.timeUntilNextWave = timeUntilNextWave;
		this.timeBetweenWaves = timeUntilNextWave;
		this.currentWave = 1;
		this.maxWaves = maxWaves;
		this.won = false;
		this.disconnected = false;
		this.playerInfos = new PlayerInfo[players.length];
		this.achievements = new ArrayList<Achievement>();

		for (int i = 0; i < players.length; i++) {
			Player player = players[i];
			playerInfos[i] = new PlayerInfo(player.getPlayerStats().getCurrentLevel(), player.getRace().getRaces(),
					player.getLife(), player.getGold());
		}

		achievementModifier = 1;
		for (Achievement achievement : players[localPlayerID].getPlayerStats().getAchievements()) {
			if (achievement.isCompleted())
				achievementModifier += achievement.getAchievementLevel();
		}
	}

	public PlayerInfo[] getPlayerInfos() {
		return playerInfos;
	}

	public PlayerInfo getPlayerInfo(int player) {
		return playerInfos[player];
	}

	public Player[] getPlayers() {
		return players;
	}

	public Player getPlayer(int player) {
		return players[player];
	}

	public Player getLocalPlayer() {
		return players[localPlayerID];
	}

	public int getWinnerID() {
		if (won)
			return localPlayerID;
		else
			return 1 - localPlayerID;
	}

	public void setWinner(int winnerID) {
		if (winnerID == localPlayerID)
			won = true;
		else
			won = false;
	}

	public Board getBoard() {
		return board;
	}

	public int getTimeBetweenWaves() {
		return timeBetweenWaves;
	}

	public int getTimeUntilNextWave() {
		return timeUntilNextWave;
	}

	public int getCurrentWave() {
		return currentWave;
	}

	public int getMaxWaves() {
		return maxWaves;
	}

	public int getGameType() {
		return gameType;
	}

	public int getLocalPlayerRaces() {
		return playerInfos[localPlayerID].getRaces();
	}

	public long getLength() {
		return length;
	}

	public int getComputerLevel() {
		int level = 0;
		for (int i = 0; i < playerInfos.length; i++) {
			if (i != localPlayerID)
				level += playerInfos[i].getLevel();
		}
		return level;
	}

	public long getPlayerLevel() {
		return playerInfos[localPlayerID].getLevel();
	}

	public CampaignLevel getCampaignLevel() {
		return campaignLevel;
	}

	public ArrayList<Achievement> getAchievements() {
		return achievements;
	}

	public void setNoXP(boolean noXP) {
		this.noXP = noXP;
	}

	public long getXP() {
		long XP = getBaseXP();
		XP *= getAchievementBonus();
		XP *= getWinBonus();
		XP *= getLengthBonus();
		XP *= getLifeBonus();
		XP *= getMultipleRaceFactor();
		return noXP ? 0 : XP;
	}

	public int getLocalPlayerID() {
		return localPlayerID;
	}

	public long getBaseXP() {
		return Math.round(Math.log1p(playerInfos[localPlayerID].getScore()));
	}

	public int getAchievementBonus() {
		return achievementModifier;
	}

	public boolean won() {
		return won;
	}

	public boolean disconnected() {
		return disconnected;
	}

	public int getWinBonus() {
		return (won() ? 5 : 1);
	}

	public int getLengthBonus() {
		return (getLength() < AVERAGE_GAME_LENGTH && won() ? 2 : 1);
	}

	public int getLifeBonus() {
		return (compareStartEndLife(localPlayerID) ? 2 : 1);
	}

	public float getMultipleRaceFactor() {
		int numRaces = Races.asArray(playerInfos[localPlayerID].getRaces()).length;
		if (numRaces <= 1)
			return 1f;
		else
			return 1.5f / numRaces;
	}

	private boolean compareStartEndLife(int playerID) {
		PlayerInfo player = playerInfos[playerID];
		return Float.floatToIntBits(player.getFirstLife()) == Float.floatToIntBits(player.getLastLife());
	}

	public int getStarCount() {
		// In campaign, Lose 1 star for not having max health. Lose the other for over 5 minutes (battle only). Lose
		// the other for playing on normal speed, lose 2 for playing of slow speed
		int stars = 5;
		if (!compareStartEndLife(localPlayerID)) {
			stars--;
		}
		if (getGameType() == GameInfo.BATTLE && getLength() > AVERAGE_GAME_LENGTH) {
			stars--;
		}
		switch (getTimeUntilNextWave()) {
		case GameInfo.SLOW:
			stars--;
		case GameInfo.NORMAL:
			stars--;
		case GameInfo.FAST:
		default:
		}
		return stars;
	}

	public void removePlayers() {
		players = null;
	}

	public void addAchievement(Achievement achievement) {
		achievements.add(achievement);
	}

	public void setWon(boolean won) {
		this.won = won;
	}

	public void setDisconnected(boolean disconnected) {
		this.disconnected = disconnected;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public void setCurrentWave(int currentWave) {
		this.currentWave = currentWave;
	}

	public void setTimeUntilNextWave(int timeUntilNextWave) {
		this.timeUntilNextWave = timeUntilNextWave;
	}

	public void creaturePurchased(int playerID, Creature creature) {
		PlayerInfo info = playerInfos[playerID];
		info.addOffensivePower(creature.getPower(), currentWave);
		info.addScore(creature.getPower());
	}

	public void towerPurchased(int playerID, Tower tower) {
		PlayerInfo info = playerInfos[playerID];
		info.addDefensivePower(tower.getPower(), currentWave);
	}

	public void towerUpgraded(int playerID, Tower oldTower, Tower newTower) {
		PlayerInfo info = playerInfos[playerID];
		info.addDefensivePower(newTower.getPower() - oldTower.getPower(), currentWave);
	}

	public void towerSold(int playerID, Tower tower) {
		PlayerInfo info = playerInfos[playerID];
		info.subtractDefensivePower(tower.getPower(), currentWave);
	}

	public void creaturesKilled(int playerID, ArrayList<Creature> creatures) {
		PlayerInfo info = playerInfos[playerID];
		for (Creature creature : creatures) {
			info.addScore(creature.getPower());
		}
	}

	public void towersDestroyed(int playerID, ArrayList<Tower> towers) {
		PlayerInfo info = playerInfos[playerID];
		for (Tower tower : towers) {
			info.addScore(tower.getPower());
		}
	}

	public boolean update(int timePassed) {
		length += timePassed;
		for (int i = 0; i < players.length; i++) {
			Player player = players[i];
			PlayerInfo info = playerInfos[i];
			info.setIncome(player.getIncome(), currentWave);
			info.setLife(player.getLife(), currentWave);
		}

		timeUntilNextWave -= timePassed;
		if (timeUntilNextWave <= 0) {
			timeUntilNextWave = timeBetweenWaves;
			currentWave++;
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "GameInfo [campaignLevel=" + campaignLevel + ", gameType=" + gameType + ", remainingTimeUntilNextWave="
				+ timeUntilNextWave + ", timeUntilNextWave=" + timeBetweenWaves + ", currentWave=" + currentWave
				+ ", maxWaves=" + maxWaves + ", length=" + length + ", won=" + won + ", playerInfo="
				+ Arrays.toString(playerInfos) + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
		won = input.readBoolean();
		disconnected = input.readBoolean();
		gameType = input.readInt();
		localPlayerID = input.readInt();
		currentWave = input.readInt();
		maxWaves = input.readInt();
		timeUntilNextWave = input.readInt();
		timeBetweenWaves = input.readInt();
		achievementModifier = input.readInt();
		length = input.readLong();
		campaignLevel = (CampaignLevel) input.readObject();
		players = (Player[]) input.readObject();
		playerInfos = (PlayerInfo[]) input.readObject();
		achievements = (ArrayList<Achievement>) input.readObject();
		board = (Board) input.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput output) throws IOException {
		output.writeBoolean(won);
		output.writeBoolean(disconnected);
		output.writeInt(gameType);
		output.writeInt(localPlayerID);
		output.writeInt(currentWave);
		output.writeInt(maxWaves);
		output.writeInt(timeUntilNextWave);
		output.writeInt(timeBetweenWaves);
		output.writeInt(achievementModifier);
		output.writeLong(length);
		output.writeObject(campaignLevel);
		output.writeObject(players);
		output.writeObject(playerInfos);
		output.writeObject(achievements);
		output.writeObject(board);
	}

}
