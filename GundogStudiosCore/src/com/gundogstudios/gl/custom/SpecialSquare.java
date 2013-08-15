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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.gundogstudios.gl.Actions;
import com.gundogstudios.gl.BaseModel;
import com.gundogstudios.gl.ModelUtils;
import com.gundogstudios.modules.GLES11Module;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.util.FastMath;

public class SpecialSquare extends BaseModel {

	public static final String NAME = "special_square";
	private final static int FLOAT_SIZE = 4;
	private final static int VERTEX_SIZE = 3 * FLOAT_SIZE;

	private int segments;
	private int[] vertexBufferIds;
	private float lineSize;

	private boolean randomColor;

	public SpecialSquare(float rows, float columns) {
		super(Actions.IDLE);
		int maxFrames = ModelUtils.getMeshFrameRate();
		this.segments = 2048 / ModelUtils.getBitmapScaleSize();
		this.lineSize = ModelUtils.getParticleScaleSize() * 2;
		this.vertexBufferIds = new int[maxFrames];
		generateAnimation();
		super.sx = columns;
		super.sy = rows;
		super.sz = 4f;
	}

	public SpecialSquare(boolean randomColor) {
		super(Actions.IDLE);
		int maxFrames = ModelUtils.getMeshFrameRate();
		this.segments = 1024 / ModelUtils.getBitmapScaleSize();
		this.lineSize = ModelUtils.getParticleScaleSize();
		this.vertexBufferIds = new int[maxFrames];
		this.randomColor = randomColor;
		generateAnimation();
	}

	public SpecialSquare(SpecialSquare square) {
		super(square);
		this.segments = square.segments;
		this.vertexBufferIds = square.vertexBufferIds;
		this.lineSize = square.lineSize;
		this.randomColor = square.randomColor;
	}

	public void setRandomColor(boolean randomColor) {
		this.randomColor = randomColor;
	}

	private void generateAnimation() {

		float tileSize = 1f;
		float width = tileSize;
		float length = tileSize;
		float height = tileSize;
		float stepSize = tileSize / 40 * ModelUtils.getBitmapScaleSize();
		GLES11Module gl = Modules.GL;
		gl.glGenBuffers(vertexBufferIds.length, vertexBufferIds, 0);

		ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect(segments * VERTEX_SIZE).order(ByteOrder.nativeOrder());

		FloatBuffer vertexBuffer = vertexByteBuffer.asFloatBuffer();

		int size = segments * 3;
		for (int j = 0; j < vertexBufferIds.length; j++) {
			vertexBuffer.position(0);

			for (int i = 0; i < size; i += 6) {
				float x = FastMath.random() * width - width / 2f;
				float y = FastMath.random() * length - length / 2f;
				float z = FastMath.random() * height;

				vertexBuffer.put(x);
				vertexBuffer.put(y);
				vertexBuffer.put(z);
				vertexBuffer.put(x);
				vertexBuffer.put(y);
				vertexBuffer.put(z + stepSize);
			}

			vertexBuffer.position(0);
			vertexByteBuffer.position(0);

			gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, vertexBufferIds[j]);
			gl.glBufferData(GLES11Module.GL_ARRAY_BUFFER, vertexByteBuffer.capacity(), vertexByteBuffer,
					GLES11Module.GL_STATIC_DRAW);
		}

		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, 0);
	}

	@Override
	protected void drawModel(int timePassed) {
		int currentFrame = FastMath.floor((((float) totalTime) / 1000f) * vertexBufferIds.length);
		GLES11Module gl = Modules.GL;
		// gl.glLineWidth(lineSize);
		gl.glEnableClientState(GLES11Module.GL_VERTEX_ARRAY);
		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, vertexBufferIds[currentFrame]);
		gl.glVertexPointer(3, GLES11Module.GL_FLOAT, VERTEX_SIZE, 0);
		gl.glDrawArrays(GLES11Module.GL_LINES, 0, segments);
		gl.glDisableClientState(GLES11Module.GL_VERTEX_ARRAY);
		// gl.glLineWidth(1f);
		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, 0);
		super.totalTime = (totalTime + timePassed) % 1000;
	}

	@Override
	public void reset() {
		totalTime = 0;
		if (randomColor)
			super.setRGBA(FastMath.random(), FastMath.random(), FastMath.random(), FastMath.random());
	}

}
