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

import gnu.trove.map.hash.TIntObjectHashMap;

import com.gundogstudios.modules.MessageModule;
import com.gundogstudios.modules.Modules;

public class BasicMessageModule implements MessageModule {
	private TIntObjectHashMap<MessageProcessor> processors;

	public BasicMessageModule() {
		this.processors = new TIntObjectHashMap<MessageProcessor>();
	}

	@Override
	public void register(int processerID, MessageProcessor processor) {
		processors.put(processerID, processor);
	}

	@Override
	public void submit(int processerID, int what, Object... object) {
		MessageProcessor processor = processors.get(processerID);
		if (processor == null) {
			Modules.LOG.error("BasicMessageModule", "Unknown processerID: " + processerID + " sending " + what);
		} else {
			processor.process(what, object);
		}
	}

}
