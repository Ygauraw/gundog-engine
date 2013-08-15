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

public class NetworkGameInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private int hostID;
	private ClientInfo host;
	private ClientInfo client;
	private String boardName;
	private int gameSpeed;

	public NetworkGameInfo(int hostID, ClientInfo host, ClientInfo client, String boardName, int gameSpeed) {
		this.hostID = hostID;
		this.host = host;
		this.client = client;
		this.boardName = boardName;
		this.gameSpeed = gameSpeed;
	}

	public int getHostID() {
		return hostID;
	}

	public ClientInfo getHost() {
		return host;
	}

	public ClientInfo getClient() {
		return client;
	}

	public String getBoardName() {
		return boardName;
	}

	public int getGameSpeed() {
		return gameSpeed;
	}

	@Override
	public String toString() {
		return "NetworkGameInfo [hostID=" + hostID + ", host=" + host + ", client=" + client + "]";
	}

}
