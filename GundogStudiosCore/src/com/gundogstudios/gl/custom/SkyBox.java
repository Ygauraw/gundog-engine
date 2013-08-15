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
package com.gundogstudios.gl.custom;

import com.gundogstudios.gl.Actions;
import com.gundogstudios.gl.BaseModel;
import com.gundogstudios.gl.ModelUtils;
import com.gundogstudios.gl.TextureManager;
import com.gundogstudios.modules.GLES11Module;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.util.FastMath;

public class SkyBox extends BaseModel {

	public static final String NAME = "skybox";
	private final static int FLOAT_SIZE = Float.SIZE / Byte.SIZE;
	private final static int VERTEX_SIZE = 3 * FLOAT_SIZE;
	private final static int TEXTURE_SIZE = 2 * FLOAT_SIZE;

	private int vertexBufferId;
	private int indexBufferId;
	private int textureBufferId;
	private int indexCount;
	private int textureId;
	private float xWidth;
	private float frameRate;

	public SkyBox(SkyBox model) {
		super(model);
		this.vertexBufferId = model.vertexBufferId;
		this.indexBufferId = model.indexBufferId;
		this.textureBufferId = model.textureBufferId;
		this.textureId = model.textureId;
		this.indexCount = model.indexCount;
		this.xWidth = model.xWidth;
		this.frameRate = model.frameRate;
	}

	public SkyBox(TextureManager textureManager, int rows, int columns) {
		super(Actions.combineActions(Actions.ALL));

		this.textureId = textureManager.getTextureID((FastMath.random() > .5f) ? "sky_day" : "sky_night", true);
		this.frameRate = ModelUtils.getMeshFrameRate();

		float width = columns * 2;
		float height = rows * 2;

		this.xWidth = 2 * width;
		float vertices[] = { -width * 3, height, 0, // v0
				width * 3, height, 0, // v1
				-width * 3, height, 20f, // v2
				width * 3, height, 20f,// v3
		};

		float[] uvs = { 0.0f, 1f, 3f, 1f, 0.0f, 0.0f, 3f, 0.0f };

		short[] indices = { 0, 1, 2, 1, 3, 2, };

		vertexBufferId = ModelUtils.generateVertexBuffer(new float[][] { vertices })[0];
		indexBufferId = ModelUtils.generateIndexBuffer(indices);
		textureBufferId = ModelUtils.generateTextureBuffer(uvs);
		indexCount = indices.length;
	}

	@Override
	protected void drawModel(int timePassed) {

		super.x += 1f / frameRate / 10f;
		if (super.x >= xWidth) {
			super.x = 0;
		}

		GLES11Module gl = Modules.GL;
		gl.glEnableClientState(GLES11Module.GL_VERTEX_ARRAY);
		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, vertexBufferId);
		gl.glVertexPointer(3, GLES11Module.GL_FLOAT, VERTEX_SIZE, 0);

		gl.glEnable(GLES11Module.GL_TEXTURE_2D);
		gl.glEnableClientState(GLES11Module.GL_TEXTURE_COORD_ARRAY);
		gl.glBindTexture(GLES11Module.GL_TEXTURE_2D, textureId);
		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, textureBufferId);
		gl.glTexCoordPointer(2, GLES11Module.GL_FLOAT, TEXTURE_SIZE, 0);

		gl.glBindBuffer(GLES11Module.GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
		gl.glDrawElements(GLES11Module.GL_TRIANGLE_STRIP, indexCount, GLES11Module.GL_UNSIGNED_SHORT, 0);

		gl.glDisableClientState(GLES11Module.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GLES11Module.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GLES11Module.GL_TEXTURE_2D);

		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, 0);
		gl.glBindBuffer(GLES11Module.GL_ELEMENT_ARRAY_BUFFER, 0);

	}

	@Override
	public void reset() {
		super.x = 0;
	}

}
