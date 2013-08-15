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
package com.godsandtowers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.util.Enumeration;

import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import com.godsandtowers.core.GameEngine;
import com.godsandtowers.core.GameInfo;
import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.core.grid.Boards;
import com.godsandtowers.core.networking.ClientInfo;
import com.godsandtowers.core.networking.DeviceInfo;
import com.godsandtowers.core.networking.GetGameResource;
import com.godsandtowers.core.networking.NetworkGameInfo;
import com.godsandtowers.core.networking.PrepareGameResource;
import com.godsandtowers.sprites.Races;
import com.godsandtowers.util.Constants;
import com.gundogstudios.gl.ModelUtils;

public class FindGameClient implements Runnable {

	private int threadID;

	public FindGameClient(int threadID) {
		this.threadID = threadID;
	}

	public static void main(String[] args) throws ResourceException, IOException {
		new Thread(new FindGameClient(0)).start();
		// new Thread(new FindGameClient(1)).start();
	}

	@Override
	public void run() {
		try {
			getNetworkGameInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getNetworkGameInfo() throws ResourceException, IOException, InterruptedException, URISyntaxException {
		DeviceInfo deviceInfo = new DeviceInfo("desktop-" + threadID, Runtime.getRuntime().maxMemory(), Runtime
				.getRuntime().availableProcessors(), 99, GameEngine.FAST, ModelUtils.getMeshQuality(),
				ModelUtils.getTextureQuality());

		ClientInfo clientInfo = new ClientInfo(deviceInfo, new PlayerStats(1000), Races.ALL, GameInfo.FAST,
				Boards.ALL_BOARDS[0].getName());

		ClientResource prepareGameClient = new ClientResource("http://" + Constants.RESTLET_HOSTNAME + ":"
				+ Constants.RESTLET_PORT + "/preparegame");
		prepareGameClient.setRequestEntityBuffering(false);
		PrepareGameResource prepareGameResource = prepareGameClient.wrap(PrepareGameResource.class);
		System.out.println("sending preparegame requests");
		int id = prepareGameResource.prepareGame(clientInfo);

		InetAddress address = getAddress();
		DatagramSocket socket = new DatagramSocket(ClientInfo.UDP_PORT + threadID, address);

		ClientResource getGameClient = new ClientResource("http://" + Constants.RESTLET_HOSTNAME + ":"
				+ Constants.RESTLET_PORT + "/getgame");
		getGameClient.setRequestEntityBuffering(false);
		GetGameResource getGameResource = getGameClient.wrap(GetGameResource.class);

		NetworkGameInfo networkGameInfo = null;
		long start = System.currentTimeMillis();

		do {
			System.out.println("sending update packet on ID: " + id);
			String data = "" + id;
			DatagramPacket sentPacket = new DatagramPacket(data.getBytes(), data.length(),
					Inet4Address.getByName(Constants.RESTLET_HOSTNAME), ClientInfo.UDP_PORT);
			socket.send(sentPacket);
			System.out.println("sent packet, trying to get game now");

			networkGameInfo = getGameResource.getGame(id);

			if (networkGameInfo == null)
				Thread.sleep(1000);

			System.out.println(networkGameInfo == null ? "no network game info found" : networkGameInfo);
		} while (networkGameInfo == null && (System.currentTimeMillis() - start) < 30000);

	}

	private static Inet4Address getAddress() throws SocketException {

		Inet4Address localAddress = null;

		for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
			NetworkInterface intf = en.nextElement();
			for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
				InetAddress inetAddress = enumIpAddr.nextElement();

				if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
					localAddress = (Inet4Address) inetAddress;
				}

			}
		}

		if (localAddress != null)
			System.out.println("Local Address Is: " + localAddress.getHostAddress());
		else
			System.err.println("Unable to determine local address");
		return localAddress;
	}
}
