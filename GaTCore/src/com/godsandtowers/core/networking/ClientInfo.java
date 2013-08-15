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

import java.io.Serializable;
import java.net.Inet4Address;

import com.godsandtowers.core.PlayerStats;

public class ClientInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final int STALE_TIME = 30000;
	public static final int UDP_PORT = 8550;

	private DeviceInfo deviceInfo;
	private Inet4Address address;
	private int port;
	private long lastPingTime;

	private PlayerStats playerStats;
	private int races;
	private int requestedSpeed;
	private String requestedBoard;

	public ClientInfo(DeviceInfo deviceInfo, PlayerStats playerStats, int races, int requestedSpeed,
			String requestedBoard) {
		this.deviceInfo = deviceInfo;
		this.playerStats = playerStats;
		this.races = races;
		this.requestedSpeed = requestedSpeed;
		this.requestedBoard = requestedBoard;
		this.address = null;
		this.port = 0;
		this.lastPingTime = 0;
	}

	public long getLastPingTime() {
		return lastPingTime;
	}

	public boolean checkIfStale() {
		return System.currentTimeMillis() - lastPingTime > STALE_TIME;
	}

	public boolean readyForGame() {
		return address != null;
	}

	public void resetPing() {
		this.lastPingTime = System.currentTimeMillis();
	}

	public void update(Inet4Address address, int port) {
		resetPing();
		this.port = port;
		// update address last since readyForGame checks address
		this.address = address;
	}

	public Inet4Address getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public DeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	public PlayerStats getPlayerStats() {
		return playerStats;
	}

	public int getRaces() {
		return races;
	}

	public int getRequestedSpeed() {
		return requestedSpeed;
	}

	public String getRequestedBoard() {
		return requestedBoard;
	}

	@Override
	public String toString() {
		return "ClientInfo [deviceInfo=" + deviceInfo + ", address=" + address + ", port=" + port + "]";
	}

}
