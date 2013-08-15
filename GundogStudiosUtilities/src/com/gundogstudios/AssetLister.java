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

public class AssetLister {

	public static void main(String[] args) {
		File file = new File("C:/Users/Joe/Workspace/GaTGAE/war/assets/");
		System.out.println("private static final String[][] ASSETS = {\n");
		outputFilePaths(file, "");
		System.out.println("\n};");
		System.out.println("private static final long[] ASSET_SIZES = {\n");
		long size = outputFileSizes(file);
		System.out.println("\n};");
		System.out.println("public static final long TOTAL_SIZE = " + size + ";");

	}

	private static void outputFilePaths(File file, String path) {
		File[] files = file.listFiles();
		if (files != null) {
			for (File f : files) {
				outputFilePaths(f, file.getName());
			}
		} else {
			System.out.println("{\"" + path + "/\",\"" + file.getName() + "\"},");
		}

	}

	private static long outputFileSizes(File file) {
		long size = 0;
		File[] files = file.listFiles();
		if (files != null) {
			for (File f : files) {
				size += outputFileSizes(f);
			}
		} else {
			size = file.length();
			System.out.println(size + ",");
		}
		return size;

	}

}
