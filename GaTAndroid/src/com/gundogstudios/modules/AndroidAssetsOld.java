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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import android.os.Environment;

public class AndroidAssetsOld implements AssetModule {
	// TODO use the top FILE_SYSTEM_PATH for the release version of the game so that all data is deleted for the user
	// after uninstall
	// private static final String FILE_SYSTEM_PATH = Environment.getExternalStorageDirectory() +
	// "/Android/data/com.godsandtowers/";
	private static final String FILE_SYSTEM_PATH = Environment.getExternalStorageDirectory()
			+ "/Gods And Towers/assets/";
	private static final String TAG = "AndroidAssets";
	private HashMap<String, String> filenames;

	public AndroidAssetsOld() {
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			throw new RuntimeException("External media is not mounted");
		}

		new File(FILE_SYSTEM_PATH).mkdirs();

		refresh();
	}

	private void refresh() {
		filenames = new HashMap<String, String>();
		String[] dirs = new File(FILE_SYSTEM_PATH).list();
		if (dirs == null)
			return;
		for (String directories : dirs) {
			File temp = new File(FILE_SYSTEM_PATH + directories);
			if (!temp.isDirectory())
				continue;
			for (String sub : temp.list()) {
				filenames.put(directories + "/" + sub.substring(0, sub.length() - 4), FILE_SYSTEM_PATH + directories
						+ "/" + sub);
			}
		}
	}

	public void deleteAllAssets() {
		String[] dirs = new File(FILE_SYSTEM_PATH).list();
		if (dirs == null)
			return;
		for (String directories : dirs) {
			for (String sub : new File(FILE_SYSTEM_PATH + directories).list()) {
				new File(FILE_SYSTEM_PATH + directories, sub).delete();
			}
		}
		refresh();
	}

	@Override
	public FileInputStream openInput(String path, String fileName) {

		try {
			String name = filenames.get(path + fileName);
			if (name == null) {
				return new FileInputStream(FILE_SYSTEM_PATH + path + fileName);
			}
			return new FileInputStream(name);
		} catch (IOException e) {
			Modules.LOG.info(TAG, "openInput: " + FILE_SYSTEM_PATH + path + fileName);
			throw new RuntimeException(path + fileName + " " + e);
		}

	}

	@Override
	public FileOutputStream openOutput(String path, String fileName) {
		try {
			return new FileOutputStream(FILE_SYSTEM_PATH + path + fileName);
		} catch (IOException e) {
			Modules.LOG.info(TAG, "openOutput: " + FILE_SYSTEM_PATH + path + fileName);
			throw new RuntimeException(path + fileName + " " + e);
		}
	}

	@Override
	public boolean delete(String path, String name) {
		return new File(FILE_SYSTEM_PATH + path + name).delete();
	}

}
