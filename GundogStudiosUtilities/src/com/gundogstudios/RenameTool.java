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
package com.gundogstudios;

import java.io.File;

public class RenameTool {

	private static final String DIRECTORY = "E:/";

	public static void main(String[] args) {
		rename(DIRECTORY);
	}

	private static void rename(String directory) {
		File dir = new File(directory);
		if (!dir.isDirectory() || dir.list() == null)
			return;
		System.out.println(directory);

		for (String fileName : dir.list()) {
			if (fileName.contains(".m4v")) {
				File file = new File(dir, fileName);
				String newName = fileName.replace(".m4v", ".mp4");
				file.renameTo(new File(dir, newName));
				System.out.println(file.getAbsolutePath());
			} else {
				rename(directory + "/" + fileName);

			}
		}
	}

}
