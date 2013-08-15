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

import com.godsandtowers.core.HostGameEngine;
import com.godsandtowers.messaging.LogicMessageProcessor;
import com.godsandtowers.messaging.ViewMessageProcessor;
import com.gundogstudios.modules.MessageModule;

public class CustomMessageModule implements MessageModule {

	// private int count = 0;
	private HostGameEngine[] engines;
	private int current = 0;
	private static final int SIZE = 65536;

	public CustomMessageModule() {
		this.engines = new HostGameEngine[SIZE];
	}

	public int addEngine(HostGameEngine engine) {
		synchronized (engines) {
			int loc = current % SIZE;
			current++;
			engines[loc] = engine;
			return loc;
		}
	}

	public void removeEngine(int id) {
		engines[id] = null;
	}

	@Override
	public void register(int processerID, MessageProcessor processor) {

	}

	@Override
	public void submit(int processerID, int what, Object... object) {
		if (processerID == ViewMessageProcessor.ID && what == ViewMessageProcessor.REFRESH) {
			// if (++count % 100 == 0) System.out.println(engine.toString());
		} else if (processerID == LogicMessageProcessor.ID) {
			int gameID = (Integer) object[object.length - 1];
			HostGameEngine engine = engines[gameID];

			switch (what) {
			case LogicMessageProcessor.UPGRADE_TOWER:
				engine.upgradeTower((Integer) object[0], (Float) object[1], (Float) object[2]);
				break;
			case LogicMessageProcessor.BUY_CREATURE:
				engine.buyCreature((Integer) object[0], (String) object[1]);
				break;
			case LogicMessageProcessor.BUY_TOWER:
				engine.buyTower((Integer) object[0], (String) object[1], (Float) object[2], (Float) object[3]);
				break;
			default:
				System.out.println("Unknown message: " + what);
			}
		}
	}

}
