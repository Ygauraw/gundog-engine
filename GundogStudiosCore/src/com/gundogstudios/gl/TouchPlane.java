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
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;

import com.gundogstudios.modules.GLES11Module;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.util.FastMath;

public class TouchPlane {
	private final static int FLOAT_SIZE = 4;
	private final static int CHAR_SIZE = 2;

	private final static int VERTEX_SIZE = 7 * FLOAT_SIZE;
	private final static int VERTEX_COLOR_BUFFER_INDEX_OFFSET = 3 * FLOAT_SIZE;

	private int vertexBufferObjectId;
	private int elementBufferObjectId;
	private int indexCount;

	private int rows;
	private int cols;

	private byte[] arr;
	private ByteBuffer pixels;

	public TouchPlane(int rows, int cols, float size) {
		arr = new byte[4];
		pixels = ByteBuffer.wrap(arr);
		this.rows = rows;
		this.cols = cols;

		float width = cols * size / 2;
		float height = rows * size / 2;

		float[] vertices = new float[(rows + 1) * (cols + 1) * 3];
		float[] colors = new float[(rows + 1) * (cols + 1) * 4];
		int currentVertex = 0;
		int currentColor = 0;
		float ystart = -height;
		for (short r = 0; r <= rows; r++) {
			float xstart = -width;
			for (short c = 0; c <= cols; c++) {
				vertices[currentVertex] = xstart; // X
				vertices[currentVertex + 1] = ystart; // Y
				vertices[currentVertex + 2] = 0f; // Z
				colors[currentColor] = (xstart + width) / (width * 2); // R
				colors[currentColor + 1] = (ystart + height) / (height * 2); // G
				colors[currentColor + 2] = 0f; // B
				colors[currentColor + 3] = 1f; // A
				currentVertex += 3;
				currentColor += 4;
				xstart += size;
			}
			ystart += size;
		}

		short[] indices = new short[rows * cols * 6];
		int currentIndex = 0;
		for (short r = 0; r < rows; r++) {
			for (short c = 0; c < cols; c++) {
				indices[currentIndex] = (short) (r * (cols + 1) + c);
				indices[currentIndex + 1] = (short) (indices[currentIndex] + 1);
				indices[currentIndex + 2] = (short) ((r + 1) * (cols + 1) + c);
				indices[currentIndex + 3] = indices[currentIndex + 1];
				indices[currentIndex + 4] = (short) (indices[currentIndex + 2] + 1);
				indices[currentIndex + 5] = indices[currentIndex + 2];
				currentIndex += 6;
			}
		}

		ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect((vertices.length + colors.length) * FLOAT_SIZE).order(
				ByteOrder.nativeOrder());
		FloatBuffer vertexBuffer = vertexByteBuffer.asFloatBuffer();

		for (int i = 0, j = 0; i < vertices.length && j < colors.length;) {
			vertexBuffer.put(vertices[i++]);
			vertexBuffer.put(vertices[i++]);
			vertexBuffer.put(vertices[i++]);
			vertexBuffer.put(colors[j++]);
			vertexBuffer.put(colors[j++]);
			vertexBuffer.put(colors[j++]);
			vertexBuffer.put(colors[j++]);
		}

		indexCount = indices.length;
		ByteBuffer indexByteBuffer = ByteBuffer.allocateDirect(CHAR_SIZE * indexCount).order(ByteOrder.nativeOrder());
		CharBuffer indexBuffer = indexByteBuffer.asCharBuffer();

		for (int x = 0; x < indices.length; x++) {
			indexBuffer.put((char) indices[x]);
		}

		GLES11Module gl = Modules.GL;
		int[] vboIds = new int[2];
		gl.glGenBuffers(2, vboIds, 0);
		vertexBufferObjectId = vboIds[0];
		elementBufferObjectId = vboIds[1];

		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, vertexBufferObjectId);
		vertexByteBuffer.position(0);
		gl.glBufferData(GLES11Module.GL_ARRAY_BUFFER, vertexByteBuffer.capacity(), vertexByteBuffer,
				GLES11Module.GL_STATIC_DRAW);

		gl.glBindBuffer(GLES11Module.GL_ELEMENT_ARRAY_BUFFER, elementBufferObjectId);
		indexByteBuffer.position(0);
		gl.glBufferData(GLES11Module.GL_ELEMENT_ARRAY_BUFFER, indexByteBuffer.capacity(), indexByteBuffer,
				GLES11Module.GL_STATIC_DRAW);

	}

	public int[] touch(int x, int y) {
		draw();

		return readPixels(x, y);
	}

	private void draw() {
		GLES11Module gl = Modules.GL;
		gl.glEnableClientState(GLES11Module.GL_VERTEX_ARRAY);

		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, vertexBufferObjectId);
		gl.glVertexPointer(3, GLES11Module.GL_FLOAT, VERTEX_SIZE, 0);

		gl.glEnableClientState(GLES11Module.GL_COLOR_ARRAY);
		gl.glColorPointer(4, GLES11Module.GL_FLOAT, VERTEX_SIZE, VERTEX_COLOR_BUFFER_INDEX_OFFSET);

		gl.glBindBuffer(GLES11Module.GL_ELEMENT_ARRAY_BUFFER, elementBufferObjectId);
		gl.glDrawElements(GLES11Module.GL_TRIANGLES, indexCount, GLES11Module.GL_UNSIGNED_SHORT, 0);

		gl.glDisableClientState(GLES11Module.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GLES11Module.GL_COLOR_ARRAY);

		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, 0);
		gl.glBindBuffer(GLES11Module.GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	private int[] readPixels(int x, int y) {

		GLES11Module gl = Modules.GL;
		pixels.position(0);
		gl.glReadPixels(x, y, 1, 1, GLES11Module.GL_RGBA, GLES11Module.GL_UNSIGNED_BYTE, pixels);
		gl.glClear(GLES11Module.GL_COLOR_BUFFER_BIT | GLES11Module.GL_DEPTH_BUFFER_BIT);

		int r = arr[0];
		int g = arr[1];
		int b = arr[2];
		int a = arr[3];

		if (r < 0)
			r += 256;
		if (g < 0)
			g += 256;
		if (b < 0)
			b += 256;
		if (a < 0)
			a += 256;

		float rf = r / 256f;
		float gf = g / 256f;
		float bf = b / 256f;

		// touched off the game board
		if (bf > .001f)
			return null;

		int col = FastMath.floor(rf * cols);
		int row = FastMath.floor(gf * rows);
		return new int[] { col, row };
	}

}
