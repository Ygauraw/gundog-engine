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

public class Nova extends BaseModel {

	public static final String NAME = "nova";
	private final static int FLOAT_SIZE = 4;
	private final static int VERTEX_SIZE = 3 * FLOAT_SIZE;

	private int currentFrame;
	private int count;
	private int vertexBufferId;
	private float scaleStepSize;

	public Nova(Nova nova) {
		super(nova);
		this.currentFrame = nova.currentFrame;
		this.count = nova.count;
		this.scaleStepSize = nova.scaleStepSize;
		this.vertexBufferId = nova.vertexBufferId;
	}

	public Nova() {
		super(Actions.ATTACK);

		this.currentFrame = 0;
		this.scaleStepSize = 1f / ModelUtils.getMeshFrameRate();
		float R = .5f;
		float r = R / 5f;
		int N = 20 / ModelUtils.getBitmapScaleSize();
		int n = 20 / ModelUtils.getBitmapScaleSize();

		super.setZTranslation(.5f);

		float dv = 2 * FastMath.PI / n;
		float dw = 2 * FastMath.PI / N;
		count = 0;

		int[] vertexBufferIds = new int[1];
		GLES11Module gl = Modules.GL;
		gl.glGenBuffers(vertexBufferIds.length, vertexBufferIds, 0);
		vertexBufferId = vertexBufferIds[0];

		ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect(2 * (N + 1) * (n + 1) * VERTEX_SIZE).order(
				ByteOrder.nativeOrder());
		FloatBuffer vertexBuffer = vertexByteBuffer.asFloatBuffer();
		vertexBuffer.position(0);
		vertexByteBuffer.position(0);
		// for (float w = 0f; w <= 2 * FastMath.PI; w += dw) {
		float w = 0f;
		for (float v = 0f; v <= 2 * FastMath.PI; v += dv) {
			vertexBuffer.put((R + r * FastMath.cos(v)) * FastMath.cos(w));
			vertexBuffer.put((R + r * FastMath.cos(v)) * FastMath.sin(w));
			vertexBuffer.put(r * FastMath.sin(v));
			vertexBuffer.put((R + r * FastMath.cos(v + dv)) * FastMath.cos(w + dw));
			vertexBuffer.put((R + r * FastMath.cos(v + dv)) * FastMath.sin(w + dw));
			vertexBuffer.put(r * FastMath.sin(v + dv));
			count += 2;
		}
		// }
		vertexBuffer.position(0);
		vertexByteBuffer.position(0);

		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, vertexBufferId);
		gl.glBufferData(GLES11Module.GL_ARRAY_BUFFER, count * VERTEX_SIZE, vertexByteBuffer,
				GLES11Module.GL_STATIC_DRAW);

		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, 0);
	}

	@Override
	protected void drawModel(int timePassed) {

		if (++currentFrame >= ModelUtils.getMeshFrameRate()) {
			reset();
		}
		super.modifyXYZScale(1f + scaleStepSize, 1f + scaleStepSize, 1f);

		GLES11Module gl = Modules.GL;
		gl.glEnableClientState(GLES11Module.GL_VERTEX_ARRAY);
		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, vertexBufferId);
		gl.glVertexPointer(3, GLES11Module.GL_FLOAT, VERTEX_SIZE, 0);

		gl.glDrawArrays(GLES11Module.GL_TRIANGLE_STRIP, 0, count);

		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, 0);
		gl.glDisableClientState(GLES11Module.GL_VERTEX_ARRAY);

	}

	@Override
	public void reset() {
		super.setXYZScale(1f, 1f, 1f);
		currentFrame = 0;
	}

}
