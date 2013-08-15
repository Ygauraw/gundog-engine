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
package com.gundogstudios.gl;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import com.gundogstudios.modules.GLES11Module;
import com.gundogstudios.modules.Modules;

public class TextureManager {

	private static final String TAG = "TextureManager";
	private HashMap<String, Integer> textureIDs;
	private Queue<Bitmap> bitmaps;

	public TextureManager() {
		textureIDs = new HashMap<String, Integer>();
		bitmaps = new LinkedList<Bitmap>();
	}

	public void releaseTextures() {
		int[] ids = new int[textureIDs.values().size()];
		int c = 0;
		for (Integer id : textureIDs.values()) {
			ids[c++] = id;
		}
		Modules.GL.glDeleteTextures(ids.length, ids, 0);
	}

	public int getTextureID(String name, boolean isGLThread) {
		Integer id = textureIDs.get(name);
		if (id == null) {
			long start = System.currentTimeMillis();
			id = loadBitmap(name, isGLThread);
			Modules.LOG.info(TAG, "TOOK " + (System.currentTimeMillis() - start) + " to load the texture");
		}
		return id;
	}

	private int loadBitmap(String name, boolean isGLThread) {
		try {
			Bitmap bitmap = Modules.GLUTIL.generateBitmap(name, ModelUtils.getBitmapScaleSize());
			if (isGLThread) {
				return generateTextureID(bitmap);
			} else {
				synchronized (bitmaps) {
					bitmaps.add(bitmap);
					bitmaps.wait();
				}
			}
			return textureIDs.get(name);
		} catch (InterruptedException e) {
			Modules.LOG.info(TAG, "Interrupted in loadBitmap: " + e);
			return 0;
		}
	}

	public void generateTextureIDs() {
		synchronized (bitmaps) {
			Bitmap bitmap;
			while ((bitmap = bitmaps.poll()) != null) {
				generateTextureID(bitmap);
			}

			bitmaps.notifyAll();
		}
	}

	private int generateTextureID(Bitmap bitmap) {
		boolean generateMIPMAP = ModelUtils.useMIPMAPs();

		GLES11Module gl = Modules.GL;
		int[] textures = new int[1];
		gl.glGenTextures(1, textures, 0);

		int textureFilter = ModelUtils.getTextureFilter();
		gl.glBindTexture(GLES11Module.GL_TEXTURE_2D, textures[0]);
		gl.glTexParameterf(GLES11Module.GL_TEXTURE_2D, GLES11Module.GL_GENERATE_MIPMAP,
				(generateMIPMAP) ? GLES11Module.GL_TRUE : GLES11Module.GL_FALSE);
		gl.glTexParameterf(GLES11Module.GL_TEXTURE_2D, GLES11Module.GL_TEXTURE_MIN_FILTER,
				(generateMIPMAP) ? GLES11Module.GL_LINEAR_MIPMAP_LINEAR : textureFilter);
		gl.glTexParameterf(GLES11Module.GL_TEXTURE_2D, GLES11Module.GL_TEXTURE_MAG_FILTER, textureFilter);
		gl.glTexParameterf(GLES11Module.GL_TEXTURE_2D, GLES11Module.GL_TEXTURE_WRAP_S, GLES11Module.GL_REPEAT);
		gl.glTexParameterf(GLES11Module.GL_TEXTURE_2D, GLES11Module.GL_TEXTURE_WRAP_T, GLES11Module.GL_REPEAT);

		ByteBuffer buffer = bitmap.getImageBytes();
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int format = bitmap.getFormat();

		gl.glTexImage2D(GLES11Module.GL_TEXTURE_2D, 0, format, width, height, 0, format, GLES11Module.GL_UNSIGNED_BYTE,
				buffer);

		textureIDs.put(bitmap.getName(), textures[0]);
		return textures[0];
	}
}
