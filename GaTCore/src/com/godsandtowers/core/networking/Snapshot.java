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
package com.godsandtowers.core.networking;

import java.util.ArrayList;

public class Snapshot {

	private ArrayList<PlayerSnapshot> playerSnapshots;
	private long timeStamp;
	private int timeUntilNextWave;
	private int currentWave;

	public Snapshot(ArrayList<PlayerSnapshot> playerSnapshots, long timeStamp, int timeUntilNextWave, int currentWave) {
		this.playerSnapshots = playerSnapshots;
		this.timeStamp = timeStamp;
		this.timeUntilNextWave = timeUntilNextWave;
		this.currentWave = currentWave;
	}

	public int getCurrentWave() {
		return currentWave;
	}

	public ArrayList<PlayerSnapshot> getPlayerSnapshots() {
		return playerSnapshots;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public int getTimeUntilNextWave() {
		return timeUntilNextWave;
	}

	@Override
	public String toString() {
		return "Snapshot [playerSnapshots=" + playerSnapshots + ", timeStamp=" + timeStamp + ", timeUntilNextWave="
				+ timeUntilNextWave + ", currentWave=" + currentWave + "]";
	}

}
