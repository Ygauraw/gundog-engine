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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

import com.gundogstudios.modules.AssetModule;

public class DesktopAssets implements AssetModule {
	private static final String BASE = "assets/";
	private HashMap<String, String> filenames;

	public DesktopAssets() {
		filenames = new HashMap<String, String>();

		for (String directories : new File(BASE).list()) {
			for (String sub : new File(BASE + directories).list()) {
				filenames.put(sub.substring(0, sub.length() - 4), sub);
			}
		}

	}

	@Override
	public FileInputStream openInput(String path, String name) {
		try {
			return new FileInputStream(BASE + path + filenames.get(name));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(path + " " + name + " " + e);
		}
	}

	@Override
	public FileOutputStream openOutput(String path, String name) {
		try {
			return new FileOutputStream(BASE + path + name);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean delete(String path, String name) {
		return new File(BASE + path + name).delete();
	}

}
