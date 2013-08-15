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

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.BitmapFactory;
import android.opengl.GLU;

import com.gundogstudios.gl.Bitmap;

public class AndroidGLUtils implements GLUtilModule {
	private static final String TEXTURES_PATH = "textures/";
	private GL10 gl;

	public AndroidGLUtils(GL10 gl) {
		this.gl = gl;
	}

	@Override
	public void gluPerspective(float fovy, float aspect, float zNear, float zFar) {
		GLU.gluPerspective(gl, fovy, aspect, zNear, zFar);
	}

	@Override
	public Bitmap generateBitmap(String textureName, int sampleSize) {
		AssetModule assets = Modules.ASSETS;
		InputStream input = assets.openInput(TEXTURES_PATH, textureName);

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = sampleSize;
		options.inScaled = false;
		options.inPreferredConfig = android.graphics.Bitmap.Config.ARGB_8888;

		android.graphics.Bitmap bitmap = BitmapFactory.decodeStream(input, null, options);

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int format = GLES11Module.GL_RGBA;
		ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4).order(ByteOrder.nativeOrder());
		bitmap.copyPixelsToBuffer(buffer);
		buffer.position(0);
		bitmap.recycle();
		bitmap = null;
		System.gc();
		Runtime.getRuntime().gc();

		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new Bitmap(textureName, width, height, format, buffer);
	}

}
