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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import android.content.res.AssetManager;
import android.os.Environment;

public class AndroidAssets implements AssetModule {

	private static final String FILE_SYSTEM_PATH = Environment.getExternalStorageDirectory() + "/Gods And Towers/";
	private static final String TAG = "AndroidAssets";
	private AssetManager assetManager;
	private HashMap<String, String> filenames;

	public AndroidAssets(AssetManager assetManager) {
		this.assetManager = assetManager;

		new File(FILE_SYSTEM_PATH).mkdirs();

		try {
			refresh();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed");
		}
	}

	private void refresh() throws IOException {
		filenames = new HashMap<String, String>();
		String[] dirs = assetManager.list("");
		if (dirs == null)
			return;
		for (String directories : dirs) {
			// Modules.LOG.error(TAG, "dir: " + directories);

			String[] files = assetManager.list(directories);
			for (String sub : files) {
				// Modules.LOG.error(TAG, "sub: " + sub);
				try {
					filenames.put(directories + "/" + sub.substring(0, sub.length() - 4), directories + "/" + sub);
				} catch (Exception e) {
				}
			}
		}
	}

	@Override
	public InputStream openInput(String path, String fileName) {
		try {
			String name = filenames.get(path + fileName);
			if (name == null)
				return new FileInputStream(FILE_SYSTEM_PATH + path + fileName);
			else
				return assetManager.open(name);
		} catch (IOException e) {
			Modules.LOG.info(TAG, "openOutput: " + path + fileName);
			throw new RuntimeException(path + fileName + " " + e);
		}
	}

	@Override
	public OutputStream openOutput(String path, String fileName) {
		try {
			return new FileOutputStream(FILE_SYSTEM_PATH + path + fileName);
		} catch (IOException e) {
			Modules.LOG.info(TAG, "openOutput: " + FILE_SYSTEM_PATH + path + fileName);
			throw new RuntimeException(path + fileName + " " + e);
		}
	}

	@Override
	public boolean delete(String path, String name) {

		return true;
	}

}
