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
package com.godsandtowers.graphics.game;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.HashMap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.gundogstudios.modules.Modules;

public class BitmapCache {
	private static TIntObjectHashMap<Bitmap> bitmaps = new TIntObjectHashMap<Bitmap>();
	private static HashMap<String, Bitmap> fileBitmaps = new HashMap<String, Bitmap>();
	private static Resources resources;

	private BitmapCache() {
	}

	public static void init(Resources resources) {
		BitmapCache.resources = resources;
	}

	public static Bitmap getBitmap(int id) {
		Bitmap bitmap = bitmaps.get(id);
		if (bitmap == null) {
			bitmap = BitmapFactory.decodeResource(resources, id);
			bitmaps.put(id, bitmap);
		}
		return bitmap;
	}

	public static Bitmap getBitmap(String path, String file) {
		Bitmap bitmap = fileBitmaps.get(file);
		if (bitmap == null) {
			bitmap = BitmapFactory.decodeStream(Modules.ASSETS.openInput(path, file));
			fileBitmaps.put(file, bitmap);
		}
		return bitmap;
	}

	public static void clear() {
		for (Bitmap bitmap : bitmaps.values()) {
			bitmap.recycle();
		}
		for (Bitmap bitmap : fileBitmaps.values()) {
			bitmap.recycle();
		}
		bitmaps.clear();
		fileBitmaps.clear();
	}
}
