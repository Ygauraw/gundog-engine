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
package com.godsandtowers.util;

public class Constants {
	public static final String APP_VERSION = "0.9.03";
	public static final boolean TESTING = false;
	public static final boolean BALANCING = false;
	public static final String RESTLET_HOSTNAME = "ec2-67-202-34-23.compute-1.amazonaws.com";
	public static final int RESTLET_PORT = 8005;
	public static final String RESTLET_GETINFO = "/getinfo";
	public static final String RESTLET_PREPAREGAME = "/preparegame" + APP_VERSION.replace(".", "");
	public static final String RESTLET_GETGAME = "/getgame" + APP_VERSION.replace(".", "");
}
