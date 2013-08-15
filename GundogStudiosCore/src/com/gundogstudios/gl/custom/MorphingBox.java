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

public class MorphingBox extends BaseModel {

	public static final String NAME = "morphingbox";
	private final static int FLOAT_SIZE = Float.SIZE / Byte.SIZE;
	private final static int VERTEX_SIZE = 3 * FLOAT_SIZE;
	private final static int TEXTURE_SIZE = 2 * FLOAT_SIZE;

	private int vertexBufferId;
	private int indexBufferId;
	private int textureBufferId;
	private int indexCount;
	private int textureId;

	public MorphingBox(MorphingBox model) {
		super(model);
		this.vertexBufferId = model.vertexBufferId;
		this.indexBufferId = model.indexBufferId;
		this.textureBufferId = model.textureBufferId;
		this.textureId = model.textureId;
		this.indexCount = model.indexCount;
	}

	public MorphingBox(TextureManager textureManager) {
		super(Actions.combineActions(Actions.IDLE, Actions.ATTACK));

		this.textureId = textureManager.getTextureID("wall", true);
		short[] indices = { 1, 1, 0, 2, 3, 3, 4, 4, 5, 7, 6, 6, 9, 9, 8, 10, 11, 11, 13, 13, 12, 14, 15, 15, 17, 17,
				16, 18, 19, 19, 21, 21, 20, 22, 23, };
		indexBufferId = ModelUtils.generateIndexBuffer(indices);
		this.indexCount = indices.length;

		float minX, minY, minZ, maxX, maxY, maxZ;
		minX = minY = minZ = -.5f;
		maxX = maxY = maxZ = .5f;

		float[] cubeVertexCoords = new float[] { minX, minY, maxZ, maxX, minY, maxZ, // 0,1 = front
				maxX, maxY, maxZ, minX, maxY, maxZ, // 2,3
				minX, minY, minZ, maxX, minY, minZ, // 4,5 = back
				maxX, maxY, minZ, minX, maxY, minZ, // 6,7 (index below)
				maxX, minY, maxZ, maxX, minY, minZ, // 1,5 = right 8,9
				maxX, maxY, minZ, maxX, maxY, maxZ, // 6,2 10,11
				minX, minY, minZ, minX, minY, maxZ, // 4,0 = left 12,13
				minX, maxY, maxZ, minX, maxY, minZ, // 3,7 14,15
				minX, maxY, maxZ, maxX, maxY, maxZ, // 3,2 = top 16,17
				maxX, maxY, minZ, minX, maxY, minZ, // 6,7 18,19
				maxX, minY, maxZ, minX, minY, maxZ, // 1,0 = bottom 20,21
				minX, minY, minZ, maxX, minY, minZ // 4,5 22,23
		};

		vertexBufferId = ModelUtils.generateVertexBuffer(new float[][] { cubeVertexCoords })[0];
		float[] cubeTextureCoords = new float[] { 0, 0, 1, 0, 1, 1, 0, 1, // front
				0, 0, 1, 0, 1, 1, 0, 1, // back
				0, 0, 1, 0, 1, 1, 0, 1, // right
				0, 0, 1, 0, 1, 1, 0, 1, // left
				0, 0, 1, 0, 1, 1, 0, 1, // top
				0, 0, 1, 0, 1, 1, 0, 1 // bottom
		};
		textureBufferId = ModelUtils.generateTextureBuffer(cubeTextureCoords);
	}

	@Override
	protected void drawModel(int timePassed) {

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
	}

}
