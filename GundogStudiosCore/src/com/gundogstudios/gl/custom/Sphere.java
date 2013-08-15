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

public class Sphere extends BaseModel {

	private final static int FLOAT_SIZE = 4;
	private final static int VERTEX_SIZE = 3 * FLOAT_SIZE;

	private int length;
	private int vertexBufferId;

	public Sphere(Sphere sphere) {
		super(sphere);
		this.length = sphere.length;
		this.vertexBufferId = sphere.vertexBufferId;
	}

	public Sphere() {
		super(Actions.IDLE);
		super.setRGBA(.25f, .25f, .25f, .25f);

		float radius = 1.5f;
		int stacks = 15 - ModelUtils.getBitmapScaleSize();
		int slices = 15 - ModelUtils.getBitmapScaleSize();

		this.length = 2 * (stacks + 1) * slices;

		int[] vertexBufferIds = new int[1];
		GLES11Module gl = Modules.GL;
		gl.glGenBuffers(1, vertexBufferIds, 0);
		vertexBufferId = vertexBufferIds[0];

		ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect(length * VERTEX_SIZE).order(ByteOrder.nativeOrder());
		FloatBuffer vertexBuffer = vertexByteBuffer.asFloatBuffer();

		float[] vertexCoords = new float[6 * (stacks + 1)];
		for (int i = 0; i < slices; i++) {

			float alpha0 = i * (2 * FastMath.PI) / slices;
			float alpha1 = (i + 1) * (2 * FastMath.PI) / slices;

			float cosAlpha0 = FastMath.cos(alpha0);
			float sinAlpha0 = FastMath.sin(alpha0);
			float cosAlpha1 = FastMath.cos(alpha1);
			float sinAlpha1 = FastMath.sin(alpha1);

			for (int j = 0; j <= stacks; j++) {
				float beta = j * FastMath.PI / stacks - FastMath.PI / 2;

				float cosBeta = FastMath.cos(beta) * radius;
				float sinBeta = FastMath.sin(beta) * radius;

				setXYZ(vertexCoords, 6 * j, cosBeta * cosAlpha1, sinBeta, cosBeta * sinAlpha1);
				setXYZ(vertexCoords, 6 * j + 3, cosBeta * cosAlpha0, sinBeta, cosBeta * sinAlpha0);

			}
			vertexBuffer.put(vertexCoords);
		}
		vertexBuffer.position(0);

		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, vertexBufferId);
		gl.glBufferData(GLES11Module.GL_ARRAY_BUFFER, vertexByteBuffer.capacity(), vertexByteBuffer,
				GLES11Module.GL_STATIC_DRAW);

		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, 0);

	}

	private void setXYZ(float[] vector, int offset, float x, float y, float z) {
		vector[offset] = x;
		vector[offset + 1] = y;
		vector[offset + 2] = z;
	}

	@Override
	protected void drawModel(int timePassed) {

		GLES11Module gl = Modules.GL;
		gl.glEnableClientState(GLES11Module.GL_VERTEX_ARRAY);
		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, vertexBufferId);
		gl.glVertexPointer(3, GLES11Module.GL_FLOAT, VERTEX_SIZE, 0);
		gl.glDrawArrays(GLES11Module.GL_TRIANGLE_STRIP, 0, length);
		gl.glDisableClientState(GLES11Module.GL_VERTEX_ARRAY);
		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, 0);
	}

	@Override
	public void reset() {
	}

}
