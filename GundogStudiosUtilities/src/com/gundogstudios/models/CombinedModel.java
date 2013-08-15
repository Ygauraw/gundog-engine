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
package com.gundogstudios.models;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.FloatBuffer;

public class CombinedModel {
	private static final int FLOAT_SIZE = 4;
	private static final int CHAR_SIZE = 2;
	private short[] indices;
	private float[] uvs;
	private float[][] idleVertices;
	private float[][] moveVertices;
	private float[][] attackVertices;
	private float[][] deathVertices;

	public CombinedModel(short[] indices, float[] uvs, float[][] idleVertices, float[][] moveVertices,
			float[][] attackVertices, float[][] deathVertices) {
		this.indices = indices;
		this.uvs = uvs;
		this.idleVertices = idleVertices;
		this.moveVertices = moveVertices;
		this.attackVertices = attackVertices;
		this.deathVertices = deathVertices;
	}

	public void setIndices(short[] indices) {
		this.indices = indices;
	}

	public void setIdleVertices(float[][] vertices) {
		this.idleVertices = vertices;
	}

	public short[] getShortIndices() {
		return indices;
	}

	public byte[] getByteIndices() {
		if (indices == null)
			return null;

		byte[] bytes = new byte[indices.length * CHAR_SIZE];
		ByteBuffer byteBuffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);

		CharBuffer indexBuffer = byteBuffer.asCharBuffer();

		for (int x = 0; x < indices.length; x++) {
			indexBuffer.put((char) indices[x]);
		}

		indexBuffer.position(0);
		return bytes;
	}

	public float[] getFloatUVs() {
		return uvs;
	}

	public byte[] getByteUVs() {
		if (uvs == null)
			return null;

		byte[] bytes = new byte[uvs.length * FLOAT_SIZE];
		ByteBuffer byteBuffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		floatBuffer.put(uvs);
		byteBuffer.position(0);
		return bytes;
	}

	private byte[][] getByteVertices(float[][] floatVertices) {
		if (floatVertices == null)
			return null;

		byte[][] byteVertices = new byte[floatVertices.length][];
		for (int i = 0; i < byteVertices.length; i++) {
			byteVertices[i] = new byte[floatVertices[i].length * FLOAT_SIZE];
			ByteBuffer byteBuffer = ByteBuffer.wrap(byteVertices[i]).order(ByteOrder.LITTLE_ENDIAN);
			FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
			floatBuffer.put(floatVertices[i]);
			byteBuffer.position(0);
		}
		return byteVertices;
	}

	public void flipZAxis() {
		flipZAxis(idleVertices);
		flipZAxis(moveVertices);
		flipZAxis(deathVertices);
		flipZAxis(attackVertices);
	}

	private void flipZAxis(float[][] vertices) {
		if (vertices == null)
			return;

		for (float[] verts : vertices) {
			for (int i = 0; i < verts.length; i += 3) {
				verts[i] *= -1f;
			}
		}
	}

	public float[][] getFloatIdleVertices() {
		return idleVertices;
	}

	public byte[][] getByteIdleVertices() {
		return getByteVertices(idleVertices);
	}

	public float[][] getFloatMoveVertices() {
		return moveVertices;
	}

	public byte[][] getByteMoveVertices() {
		return getByteVertices(moveVertices);
	}

	public float[][] getFloatAttackVertices() {
		return attackVertices;
	}

	public byte[][] getByteAttackVertices() {
		return getByteVertices(attackVertices);
	}

	public float[][] getFloatDeathVertices() {
		return deathVertices;
	}

	public byte[][] getByteDeathVertices() {
		return getByteVertices(deathVertices);
	}

	public ReducedModel asReducedModel() {
		char[] indices = convertIndices(getShortIndices());
		// char[] uvs = convertUVs(getFloatUVs());
		short[][] idleVertices = convertVertices(getFloatIdleVertices());
		short[][] attackVertices = convertVertices(getFloatAttackVertices());
		short[][] deathVertices = convertVertices(getFloatDeathVertices());
		short[][] moveVertices = convertVertices(getFloatMoveVertices());
		return new ReducedModel(indices, getFloatUVs(), idleVertices, moveVertices, attackVertices, deathVertices);
	}

	private char[] convertIndices(short[] indices) {
		char[] newIndices = new char[indices.length];
		for (int i = 0; i < indices.length; i++) {
			newIndices[i] = (char) indices[i];
		}
		return newIndices;
	}

	// private char[] convertUVs(float[] uvs) {
	// char[] newUVs = new char[uvs.length];
	// for (int i = 0; i < uvs.length; i++) {
	// newUVs[i] = (char) Math.round(uvs[i] * Short.MAX_VALUE);
	// }
	// return newUVs;
	// }

	private static float SCALE_MAX = 620.5913f;

	private short[][] convertVertices(float[][] vertices) {
		if (vertices == null) {
			return null;
		}

		for (float[] arr : vertices) {
			for (float f : arr) {
				float tmp = Math.abs(f);
				if (tmp > SCALE_MAX) {
					System.err.println("Found new max scale: " + tmp);
				}
			}
		}
		short[][] newVertices = new short[vertices.length][];
		for (int i = 0; i < vertices.length; i++) {
			newVertices[i] = new short[vertices[i].length / 3 * 4];
			int current = 0;
			for (int j = 0; j < vertices[i].length; j++) {
				newVertices[i][current] = (short) (vertices[i][j] / SCALE_MAX * Short.MAX_VALUE);
				current++;

				if (j % 3 == 2) {
					// System.out.println(j);
					newVertices[i][current] = 0;
					current++;
				}
				// System.out.println(vertices[i][j]);
			}
		}
		return newVertices;
	}
}
