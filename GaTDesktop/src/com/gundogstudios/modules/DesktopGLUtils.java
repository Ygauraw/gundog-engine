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

import org.lwjgl.util.glu.GLU;

import com.gundogstudios.gl.Bitmap;
import com.gundogstudios.modules.AssetModule;
import com.gundogstudios.modules.GLES11Module;
import com.gundogstudios.modules.GLUtilModule;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.util.PNGDecoder;

public class DesktopGLUtils implements GLUtilModule {
	private static final String TEXTURES_PATH = "textures/";

	public DesktopGLUtils() {

	}

	@Override
	public void gluPerspective(float fovy, float aspect, float zNear, float zFar) {
		GLU.gluPerspective(fovy, aspect, zNear, zFar);
	}

	@Override
	public Bitmap generateBitmap(String textureName, int sampleSize) {
		AssetModule assets = Modules.ASSETS;

		InputStream input = assets.openInput(TEXTURES_PATH, textureName);
		PNGDecoder data = new PNGDecoder(input);

		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int format = (data.hasAlpha()) ? GLES11Module.GL_RGBA : GLES11Module.GL_RGB;
		return new Bitmap(textureName, data.getWidth(), data.getHeight(), format, data.getImageBufferData());
	}

}
