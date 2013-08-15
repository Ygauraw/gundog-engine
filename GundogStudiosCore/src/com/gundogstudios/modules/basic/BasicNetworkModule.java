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
package com.gundogstudios.modules.basic;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.nio.ByteBuffer;

import com.gundogstudios.modules.Modules;
import com.gundogstudios.modules.NetworkModule;

public class BasicNetworkModule implements NetworkModule {

	private static final String TAG = "BasicNetworkModule";
	private static final int BUFFER_SIZE = 32768;
	private DatagramSocket socket;
	private byte[] receiveBuffer;
	private byte[] sendBuffer;
	private Inet4Address remoteAddress;
	private int remotePort;

	public BasicNetworkModule() {
		receiveBuffer = new byte[BUFFER_SIZE];
		sendBuffer = new byte[BUFFER_SIZE];
	}

	public boolean isConnected() {
		return socket != null && socket.isConnected();
	}

	@Override
	public void connect(DatagramSocket socket, Inet4Address remoteAddress, int remotePort) {
		disconnect();
		this.socket = socket;
		this.remoteAddress = remoteAddress;
		this.remotePort = remotePort;
		try {
			socket.setReceiveBufferSize(BUFFER_SIZE);
			socket.setSendBufferSize(BUFFER_SIZE);
			Modules.LOG.info(TAG, "Bound on remote " + remoteAddress + ":" + remotePort);
		} catch (IOException e) {
			Modules.LOG.error(TAG, e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void send(ByteBuffer buffer) {
		if (socket == null) {
			Modules.LOG.error(TAG, "Trying to send data on a null socket");
			return;
		}
		try {
			buffer.get(sendBuffer, 0, buffer.limit());
			DatagramPacket packet = new DatagramPacket(sendBuffer, buffer.limit(), remoteAddress, remotePort);
			socket.send(packet);
		} catch (IOException e) {
			Modules.LOG.error(TAG, e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void receive(ByteBuffer buffer) {
		if (socket == null) {
			Modules.LOG.error(TAG, "Trying to receive data on a null socket");
			return;
		}
		try {
			DatagramPacket packet = new DatagramPacket(receiveBuffer, receiveBuffer.length);
			socket.receive(packet);
			buffer.put(packet.getData(), packet.getOffset(), packet.getLength());
		} catch (IOException e) {
			Modules.LOG.error(TAG, e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void disconnect() {
		if (socket == null)
			return;

		socket.disconnect();
		socket.close();
		socket = null;
	}

}
