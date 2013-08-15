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

public class CampaignLevel implements Externalizable {

	public static final int EASY = 10;
	public static final int MEDIUM = 100;
	public static final int HARD = 1000;
	public static final int EXPERT = 10000;

	private int gameType;
	private int waves;
	private int playerRaces;
	private int computerRaces;
	private int stars;
	private int difficulty;
	private String board;

	public CampaignLevel() {

	}

	public CampaignLevel(int gameType, int waves, int playerRaces, int computerRaces, String board) {
		this.gameType = gameType;
		this.waves = waves;
		this.playerRaces = playerRaces;
		this.computerRaces = computerRaces;
		this.stars = 0;
		this.difficulty = EASY;
		this.board = board;
	}

	public String getBoard() {
		return board;
	}

	public int getGameType() {
		return gameType;
	}

	public int getWaves() {
		return waves;
	}

	public int getPlayerRaces() {
		return playerRaces;
	}

	public int getComputerRaces() {
		return computerRaces;
	}

	public int getStars() {
		return stars;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void update(int stars, int difficulty) {
		this.stars = stars;
		this.difficulty = difficulty;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(gameType);
		out.writeInt(waves);
		out.writeInt(playerRaces);
		out.writeInt(computerRaces);
		out.writeInt(stars);
		out.writeInt(difficulty);
		out.writeObject(board);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		gameType = in.readInt();
		waves = in.readInt();
		playerRaces = in.readInt();
		computerRaces = in.readInt();
		stars = in.readInt();
		difficulty = in.readInt();
		board = (String) in.readObject();
	}

}