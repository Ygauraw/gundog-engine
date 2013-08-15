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

import com.godsandtowers.core.networking.GetGameResource;
import com.godsandtowers.core.networking.NetworkGameInfo;

public class GetGameServerResource extends ServerResource implements GetGameResource {

	@Put
	public NetworkGameInfo getGame(Integer id) {
		// System.out.println("GetGameServerResource getGame start: " + id);
		NetworkGameInfo networkGameInfo = GameMatcher.instance().getGame(id);
		// System.out.println("GetGameServerResource getGame end: " + id + " found game: " + networkGameInfo);
		return networkGameInfo;
	}

}
