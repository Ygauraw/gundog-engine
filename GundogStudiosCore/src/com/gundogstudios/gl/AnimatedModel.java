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

import com.gundogstudios.modules.GLES11Module;
import com.gundogstudios.modules.Modules;

public class AnimatedModel extends BaseModel {

	private final static int FLOAT_SIZE = Float.SIZE / Byte.SIZE;
	private final static int VERTEX_SIZE = 3 * FLOAT_SIZE;
	private final static int TEXTURE_SIZE = 2 * FLOAT_SIZE;

	private int currentFrame;

	private int[] vertexBufferIds;
	private int indexBufferId;
	private int textureBufferId;
	private int indexCount;
	private int textureId;

	public AnimatedModel(AnimatedModel model) {
		super(model);
		this.vertexBufferIds = model.vertexBufferIds;
		this.indexBufferId = model.indexBufferId;
		this.textureBufferId = model.textureBufferId;
		this.textureId = model.textureId;
		this.indexCount = model.indexCount;
		this.currentFrame = model.currentFrame;
	}

	public AnimatedModel(int action, int[] vertexBufferIds, int indexBufferId, int textureBufferId, int textureId,
			int indexCount) {
		super(action);
		this.vertexBufferIds = vertexBufferIds;
		this.indexBufferId = indexBufferId;
		this.textureBufferId = textureBufferId;
		this.textureId = textureId;
		this.indexCount = indexCount;
		currentFrame = 0;
	}

	public void reduceToOneFrame() {
		int[] tmp = vertexBufferIds;
		vertexBufferIds = new int[1];
		vertexBufferIds[0] = tmp[0];
	}

	@Override
	protected void drawModel(int timePassed) {

		if (++currentFrame >= vertexBufferIds.length)
			currentFrame = 0;

		GLES11Module gl = Modules.GL;

		gl.glEnableClientState(GLES11Module.GL_VERTEX_ARRAY);
		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, vertexBufferIds[currentFrame]);
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
		currentFrame = 0;
	}

}
