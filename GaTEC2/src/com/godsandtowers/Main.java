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

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;
import org.restlet.service.LogService;

import com.godsandtowers.util.Constants;

public class Main extends Application {

	public static void main(String[] args) throws Exception {
		NATPuncher.init();
		GameMatcher.init();

		Component component = new Component();
		component.setLogService(new LogService(false));
		component.getServers().add(Protocol.HTTP, Constants.RESTLET_PORT);
		component.getDefaultHost().attach(new Main());
		component.start();
	}

	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());
		router.attach(Constants.RESTLET_GETGAME, GetGameServerResource.class);
		router.attach(Constants.RESTLET_PREPAREGAME, PrepareGameServerResource.class);
		router.attach(Constants.RESTLET_GETINFO, GetAppInfoServerResource.class);
		return router;
	}
}
