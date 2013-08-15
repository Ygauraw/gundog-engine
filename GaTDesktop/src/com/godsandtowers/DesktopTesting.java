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
import java.util.Enumeration;

import org.restlet.resource.ResourceException;

import com.gundogstudios.modules.Modules;
import com.gundogstudios.modules.basic.SystemLogger;

public class DesktopTesting {
	public static void main(String[] args) throws ResourceException, IOException {
		Modules.LOG = new SystemLogger();

		String data = "DesktopData";
		DatagramSocket socket = new DatagramSocket(8551, getAddress());
		// socket.setBroadcast(true);
		socket.setSoTimeout(10000);

		DatagramPacket sentPacket = new DatagramPacket(data.getBytes(), data.length(),
				Inet4Address.getByName("ec2-23-20-228-248.compute-1.amazonaws.com"), 8550);
		socket.send(sentPacket);
		System.out.println("Sent from " + getAddress());

		System.out.println("Waiting for packet");
		byte[] buf = new byte[1024];
		DatagramPacket receivedPacket = new DatagramPacket(buf, buf.length);
		;
		socket.receive(receivedPacket);
		final String result = new String(buf, 0, receivedPacket.getLength());
		System.out.println("Received: " + result + " from " + receivedPacket.getAddress().getHostAddress() + ":"
				+ receivedPacket.getPort());

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
