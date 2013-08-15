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
import java.nio.ShortBuffer;

public class ReducedModel {
	private static final int CHAR_SIZE = Character.SIZE / Byte.SIZE;
	private static final int FLOAT_SIZE = Float.SIZE / Byte.SIZE;
	private byte[] indices;
	private byte[] uvs;
	private byte[][] idleVertices;
	private byte[][] moveVertices;
	private byte[][] attackVertices;
	private byte[][] deathVertices;

	public ReducedModel(char[] indices, float[] uvs, short[][] idleVertices, short[][] moveVertices,
			short[][] attackVertices, short[][] deathVertices) {
		this.indices = convert(indices);
		this.uvs = convert(uvs);
		this.idleVertices = convert(idleVertices);
		this.moveVertices = convert(moveVertices);
		this.attackVertices = convert(attackVertices);
		this.deathVertices = convert(deathVertices);
	}

	private byte[] convert(char[] shorts) {
		if (shorts == null)
			return null;

		byte[] bytes = new byte[shorts.length * CHAR_SIZE];

		ByteBuffer byteBuffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
		CharBuffer indexBuffer = byteBuffer.asCharBuffer();

		for (int x = 0; x < shorts.length; x++) {
			indexBuffer.put(shorts[x]);
		}

		return bytes;
	}

	private byte[] convert(float[] uvs) {

		if (uvs == null)
			return null;

		byte[] bytes = new byte[uvs.length * FLOAT_SIZE];
		ByteBuffer byteBuffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		floatBuffer.put(uvs);
		byteBuffer.position(0);
		return bytes;

	}

	private byte[][] convert(short[][] shorts) {
		if (shorts == null)
			return null;

		byte[][] byteVertices = new byte[shorts.length][];
		for (int i = 0; i < byteVertices.length; i++) {
			byteVertices[i] = new byte[shorts[i].length * CHAR_SIZE];
			ByteBuffer byteBuffer = ByteBuffer.wrap(byteVertices[i]).order(ByteOrder.LITTLE_ENDIAN);
			ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
			shortBuffer.put(shorts[i]);
			byteBuffer.position(0);
		}
		return byteVertices;
	}

	public byte[] getIndices() {
		return indices;
	}

	public byte[] getUVs() {
		return uvs;
	}

	public byte[][] getIdleVertices() {
		return idleVertices;
	}

	public byte[][] getMoveVertices() {
		return moveVertices;
	}

	public byte[][] getAttackVertices() {
		return attackVertices;
	}

	public byte[][] getDeathVertices() {
		return deathVertices;
	}

}
