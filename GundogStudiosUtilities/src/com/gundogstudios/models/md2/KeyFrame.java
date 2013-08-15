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
package com.gundogstudios.models.md2;

public class KeyFrame {
	private float[] vertices;
	private float[] normals;
	private int[] indices;

	public KeyFrame(float[] vertices) {
		this.vertices = vertices;
	}

	public KeyFrame(float[] vertices, float[] normals) {
		this(vertices);
		this.normals = normals;
	}

	public float[] getVertices() {
		return vertices;
	}

	public int[] getIndices() {
		return indices;
	}

	public float[] getNormals() {
		return normals;
	}

	public void setIndices(int[] indices) {
		this.indices = indices;
		float[] compressed = vertices;
		vertices = new float[indices.length * 3];
		int len = indices.length;
		int vi = 0;
		int ii = 0;
		int normalIndex = 0;

		for (int i = 0; i < len; i++) {
			ii = indices[i] * 3;
			vertices[vi++] = compressed[ii];
			vertices[vi++] = compressed[ii + 1];
			vertices[vi++] = compressed[ii + 2];
		}

		normals = new float[vertices.length];
		int vertLen = vertices.length;

		for (int i = 0; i < vertLen; i += 9) {
			Number3d normal = calculateFaceNormal(new Number3d(vertices[i], vertices[i + 1], vertices[i + 2]),
					new Number3d(vertices[i + 3], vertices[i + 4], vertices[i + 5]), new Number3d(vertices[i + 6],
							vertices[i + 7], vertices[i + 8]));
			normals[normalIndex++] = normal.x;
			normals[normalIndex++] = normal.y;
			normals[normalIndex++] = normal.z;
			normals[normalIndex++] = normal.x;
			normals[normalIndex++] = normal.y;
			normals[normalIndex++] = normal.z;
			normals[normalIndex++] = normal.x;
			normals[normalIndex++] = normal.y;
			normals[normalIndex++] = normal.z;
		}
	}

	public Number3d calculateFaceNormal(Number3d v1, Number3d v2, Number3d v3) {
		Number3d vector1 = Number3d.subtract(v2, v1);
		Number3d vector2 = Number3d.subtract(v3, v1);

		Number3d normal = new Number3d();
		normal.x = (vector1.y * vector2.z) - (vector1.z * vector2.y);
		normal.y = -((vector2.z * vector1.x) - (vector2.x * vector1.z));
		normal.z = (vector1.x * vector2.y) - (vector1.y * vector2.x);

		double normFactor = Math.sqrt((normal.x * normal.x) + (normal.y * normal.y) + (normal.z * normal.z));

		normal.x /= normFactor;
		normal.y /= normFactor;
		normal.z /= normFactor;

		return normal;
	}

}
