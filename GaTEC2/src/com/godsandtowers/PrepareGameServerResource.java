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

import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.godsandtowers.core.networking.ClientInfo;
import com.godsandtowers.core.networking.PrepareGameResource;

public class PrepareGameServerResource extends ServerResource implements PrepareGameResource {

	@Put
	public int prepareGame(ClientInfo clientInfo) {
		// System.out.println("PrepareGameServerResource prepareGame start: " + clientInfo);
		int id = GameMatcher.instance().prepareGame(clientInfo);
		// System.out.println("PrepareGameServerResource prepareGame end:" + id);
		return id;
	}
}
