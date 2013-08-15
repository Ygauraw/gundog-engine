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
package com.gundogstudios.modules;

import java.io.FileWriter;
import java.io.IOException;

import com.gundogstudios.modules.LogModule;

public class FileSystemLogger implements LogModule {
	private FileWriter fileWriter;

	public FileSystemLogger(String fileName) {
		try {
			fileWriter = new FileWriter(fileName);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void debug(String tag, String message) {
		try {
			fileWriter.append("DEBUG: " + tag + " - " + message + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void error(String tag, String message) {
		System.err.println(": " + tag + " - " + message);
		try {
			fileWriter.append("ERROR: " + tag + " - " + message + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void info(String tag, String message) {
		try {
			fileWriter.append("INFO: " + tag + " - " + message + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void verbose(String tag, String message) {
		try {
			fileWriter.append("VERBOSE: " + tag + " - " + message + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void warn(String tag, String message) {
		try {
			fileWriter.append("WARN: " + tag + " - " + message + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
