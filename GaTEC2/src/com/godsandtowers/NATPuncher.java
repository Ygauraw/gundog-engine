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
import java.net.SocketException;
import java.net.UnknownHostException;

import com.godsandtowers.core.networking.ClientInfo;
import com.godsandtowers.util.Constants;

public class NATPuncher implements Runnable {

	private static NATPuncher PUNCHER;
	private DatagramSocket socket;

	private NATPuncher() throws SocketException, UnknownHostException {
		socket = new DatagramSocket(ClientInfo.UDP_PORT, Inet4Address.getByName(Constants.RESTLET_HOSTNAME));
	}

	@Override
	public void run() {
		System.out.println("NATPuncher Started");

		while (true) {
			try {
				processPackets();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void processPackets() throws IOException {
		// System.out.println("Waiting for packet");
		byte[] buf = new byte[64];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
		// System.out.println("Received a packet");
		String result = new String(buf, 0, packet.getLength());

		int id = Integer.parseInt(result);
		Inet4Address address = (Inet4Address) packet.getAddress();
		int port = packet.getPort();

		GameMatcher.instance().update(id, address, port);
	}

	public static NATPuncher instance() {
		return PUNCHER;
	}

	public static void init() throws SocketException, UnknownHostException {
		PUNCHER = new NATPuncher();
		new Thread(PUNCHER).start();
	}

}
