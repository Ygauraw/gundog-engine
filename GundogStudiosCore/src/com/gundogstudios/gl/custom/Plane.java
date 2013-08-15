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
import com.gundogstudios.modules.GLES11Module;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.util.FastMath;

public class Plane extends BaseModel {

	public static final String NAME = "square_plane";
	private final static int FLOAT_SIZE = Float.SIZE / Byte.SIZE;
	private final static int VERTEX_SIZE = 3 * FLOAT_SIZE;

	private int vertexBufferId;
	private int indexBufferId;
	private int indexCount;
	private boolean randomColor;

	public Plane(Plane model) {
		super(model);
		this.vertexBufferId = model.vertexBufferId;
		this.indexBufferId = model.indexBufferId;
		this.indexCount = model.indexCount;
		this.randomColor = model.randomColor;
	}

	public Plane() {
		super(Actions.combineActions(Actions.ALL));

		float tileSize = .5f;

		float vertices[] = { -tileSize, -tileSize, 0f, // v0
				tileSize, -tileSize, 0f, // v1
				-tileSize, tileSize, 0f, // v2
				tileSize, tileSize, 0f, // v3
		};
		short[] indices = { 0, 1, 2, 1, 3, 2, };

		this.vertexBufferId = ModelUtils.generateVertexBuffer(new float[][] { vertices })[0];
		this.indexBufferId = ModelUtils.generateIndexBuffer(indices);
		this.indexCount = indices.length;
		this.randomColor = false;
	}

	@Override
	protected void drawModel(int timePassed) {
		float currentFrame = ((float) totalTime) / 4000f;

		super.setZTranslation(currentFrame);

		GLES11Module gl = Modules.GL;

		gl.glEnableClientState(GLES11Module.GL_VERTEX_ARRAY);
		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, vertexBufferId);
		gl.glVertexPointer(3, GLES11Module.GL_FLOAT, VERTEX_SIZE, 0);

		gl.glBindBuffer(GLES11Module.GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
		gl.glDrawElements(GLES11Module.GL_TRIANGLE_STRIP, indexCount, GLES11Module.GL_UNSIGNED_SHORT, 0);

		gl.glBindBuffer(GLES11Module.GL_ELEMENT_ARRAY_BUFFER, 0);
		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, 0);
		super.totalTime = (totalTime + timePassed) % 4000;

	}

	@Override
	public void setXYZTranslation(float x, float y, float z) {
		super.setXYTranslation(x, y);
	}

	public void setRandomColor(boolean randomColor) {
		this.randomColor = randomColor;
		super.setRGBA(FastMath.random(), FastMath.random(), FastMath.random(), FastMath.random());
	}

	@Override
	public void reset() {
		totalTime = 0;
		if (randomColor)
			super.setRGBA(FastMath.random(), FastMath.random(), FastMath.random(), FastMath.random());
	}

}
