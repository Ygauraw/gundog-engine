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

import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.procedure.TIntObjectProcedure;
import gnu.trove.procedure.TIntProcedure;

import java.net.Inet4Address;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import com.godsandtowers.core.networking.ClientInfo;
import com.godsandtowers.core.networking.DeviceInfo;
import com.godsandtowers.core.networking.NetworkGameInfo;

public class GameMatcher implements Runnable {
	private static GameMatcher MATCHER;

	private ReentrantReadWriteLock masterLock;
	private ReadLock readLock;
	private WriteLock writeLock;
	private boolean running;
	private AtomicInteger currentID;
	private TIntObjectHashMap<ClientInfo> clients;
	private TIntObjectHashMap<NetworkGameInfo> games;

	private GameMatcher() {
		running = false;
		currentID = new AtomicInteger(0);
		masterLock = new ReentrantReadWriteLock(true);
		readLock = masterLock.readLock();
		writeLock = masterLock.writeLock();
		clients = new TIntObjectHashMap<ClientInfo>(128);
		games = new TIntObjectHashMap<NetworkGameInfo>(128);
	}

	public NetworkGameInfo getGame(int id) {
		System.out.println("getGame: " + id);
		readLock.lock();
		NetworkGameInfo info = games.get(id);
		readLock.unlock();
		return info;
	}

	public int prepareGame(ClientInfo clientInfo) {
		System.out.println("prepareGame: " + clientInfo);
		int id = currentID.incrementAndGet();
		clientInfo.resetPing();

		writeLock.lock();
		clients.put(id, clientInfo);
		notifyMatcher();
		writeLock.unlock();

		return id;
	}

	private void notifyMatcher() {
		if (!running) {
			synchronized (clients) {
				running = true;
				clients.notify();
			}
		}
	}

	public void update(int id, Inet4Address address, int port) {
		System.out.println("update: " + id + " - " + address.getHostAddress() + ":" + port);
		readLock.lock();
		ClientInfo client = clients.get(id);

		if (client == null) {
			readLock.unlock();
			return;
		}
		// Note, due to the update atomically changing data for this client, we do not need a writelock
		// we do however need to notify the matcher of the change
		client.update(address, port);
		notifyMatcher();
		readLock.unlock();
	}

	@Override
	public void run() {
		System.out.println("GameMatcher Started");
		while (true) {
			System.out.println("Running match cycle: currentID=" + currentID + " numClients=" + clients.size());

			writeLock.lock();
			removeStaleClients();
			matchClients();
			synchronized (clients) {
				running = false;
				writeLock.unlock();
				try {
					clients.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

	private void removeStaleClients() {
		final TIntArrayList staleIDs = new TIntArrayList();

		clients.forEachEntry(new TIntObjectProcedure<ClientInfo>() {
			@Override
			public boolean execute(int id, ClientInfo client) {
				if (client.checkIfStale())
					staleIDs.add(id);
				return true;
			}
		});

		if (staleIDs.size() == 0)
			return;

		staleIDs.forEach(new TIntProcedure() {
			@Override
			public boolean execute(int id) {
				// System.out.println("Removing stale ID: " + id);
				clients.remove(id);
				games.remove(id);
				return true;
			}
		});
	}

	private void matchClients() {
		final TIntArrayList ids = new TIntArrayList(clients.size());

		clients.forEachEntry(new TIntObjectProcedure<ClientInfo>() {
			@Override
			public boolean execute(int id, ClientInfo client) {
				if (client.readyForGame())
					ids.add(id);
				return true;
			}
		});

		if (ids.size() < 2)
			return;

		ids.sort();

		for (int i = 0; i < ids.size(); i += 2) {
			int idOne = ids.get(i);
			int idTwo = ids.get(i + 1);

			ClientInfo clientOne = clients.remove(idOne);
			ClientInfo clientTwo = clients.remove(idTwo);

			if (clientOne.getAddress().equals(clientTwo.getAddress()) && clientOne.getPort() == clientTwo.getPort()) {
				if (clientOne.getLastPingTime() > clientTwo.getLastPingTime()) {
					clients.put(idOne, clientOne);
				} else {
					clients.put(idTwo, clientTwo);
				}
				continue;
			}

			boolean isHost = isHost(clientOne, clientTwo);

			NetworkGameInfo networkGameInfo;
			if (isHost) {
				networkGameInfo = new NetworkGameInfo(idOne, clientOne, clientTwo, clientOne.getRequestedBoard(),
						clientOne.getRequestedSpeed());
			} else {
				networkGameInfo = new NetworkGameInfo(idTwo, clientTwo, clientOne, clientTwo.getRequestedBoard(),
						clientTwo.getRequestedSpeed());
			}
			games.put(idOne, networkGameInfo);
			games.put(idTwo, networkGameInfo);

			System.out.println("Made game for " + idOne + " and " + idTwo + " " + networkGameInfo);
		}
	}

	private boolean isHost(ClientInfo clientOne, ClientInfo clientTwo) {
		DeviceInfo one = clientOne.getDeviceInfo();
		DeviceInfo two = clientTwo.getDeviceInfo();
		if (one.getNumProcessors() != two.getNumProcessors())
			return one.getNumProcessors() > two.getNumProcessors();
		if (one.getMaxMemory() != two.getMaxMemory())
			return one.getMaxMemory() > two.getMaxMemory();
		if (one.getApiLevel() != two.getApiLevel())
			return one.getApiLevel() > two.getApiLevel();
		if (one.getGameEngineSpeed() != two.getGameEngineSpeed())
			return one.getGameEngineSpeed() > two.getGameEngineSpeed();
		if (one.getMeshQuality() != two.getMeshQuality())
			return one.getMeshQuality() > two.getMeshQuality();
		if (one.getTextureQuality() != two.getTextureQuality())
			return one.getTextureQuality() > two.getTextureQuality();
		return true;
	}

	public static GameMatcher instance() {
		return MATCHER;
	}

	public static void init() {
		MATCHER = new GameMatcher();
		new Thread(MATCHER).start();
	}

}
