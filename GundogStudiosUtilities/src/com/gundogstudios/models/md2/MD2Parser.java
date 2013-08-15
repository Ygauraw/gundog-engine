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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.gundogstudios.models.CombinedModel;

public class MD2Parser {
	private static final String DIRECTORY = "D:/Users/Joe/Documents/TowerDefenseWars/Graphics/Processed 3D Models/MD2/";

	private static void checkCreature(String baseName) throws Exception {
		Model idleModel = MD2Parser.parse(new FileInputStream("data/" + baseName + "_idle.md2"));
		// Model walkModel = MD2Parser.parse(new FileInputStream("data/" + baseName + "_walk.md2"));
		Model attackModel = MD2Parser.parse(new FileInputStream("data/" + baseName + "_attack.md2"));
		// Model deathModel = MD2Parser.parse(new FileInputStream("data/" + baseName + "_death.md2"));
		float[] uvs = idleModel.getUvs();
		float[] otherUVs = attackModel.getUvs();
		if (uvs.length != otherUVs.length)
			System.out.println("Different Length UVs");

		System.out.println("Total UVs: " + uvs.length / 2);
		for (int i = 0; i < uvs.length; i += 2) {
			if (Float.floatToIntBits(uvs[i]) != Float.floatToIntBits(otherUVs[i])
					&& Float.floatToIntBits(uvs[i + 1]) != Float.floatToIntBits(otherUVs[i + 1])) {

				System.out.println("UV's not equal: " + i + " " + uvs[i] + " vs " + otherUVs[i] + " and " + uvs[i + 1]
						+ " vs " + otherUVs[i + 1]);
			}
		}
		short[] indices = idleModel.getIndices();
		short[] otherIndices = attackModel.getIndices();
		if (indices.length != otherIndices.length)
			System.out.println("Different Length Indices");
		for (int i = 0; i < indices.length; i++) {
			if (indices[i] != otherIndices[i])
				System.out.println("INDEX: " + i + " is different " + indices[i] + " vs " + otherIndices[i]);
		}
	}

	public static void main(String[] argv) throws Exception {
		// parseReduced("Pillars");
		// CombinedModel model = parseReduced("Soldiers");
		// System.out.println(model.getIdleVertices()[0].length / 3);
		checkCreature("Cannons");
	}

	public static CombinedModel parseReduced(String baseName) {
		CombinedModel model = parse(baseName);
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		HashMap<Vertex, Short> table = new HashMap<Vertex, Short>();
		float[] verts = model.getFloatIdleVertices()[0];
		float[] uvs = model.getFloatUVs();
		short[] indices = new short[verts.length / 3];

		// System.out.println(verts.length / 3 + " " + uvs.length / 2 + " " + indices.length);
		int index = 0;
		for (int v = 0, u = 0; v < verts.length; v += 3, u += 2) {
			Vertex temp = new Vertex(verts[v], verts[v + 1], verts[v + 2], uvs[u], uvs[u + 1]);
			Short loc = table.get(temp);
			if (loc == null) {
				loc = (short) vertices.size();
				table.put(temp, loc);
				vertices.add(temp);
			}
			indices[index++] = loc;
		}

		uvs = new float[vertices.size() * 2];
		int curr = 0;
		for (Vertex v : vertices) {
			uvs[curr++] = v.u;
			uvs[curr++] = v.v;
		}

		float[][] idleVertices = generateReducedVertices(model.getFloatIdleVertices(), indices, vertices.size() * 3);
		float[][] moveVertices = generateReducedVertices(model.getFloatMoveVertices(), indices, vertices.size() * 3);
		float[][] attackVertices = generateReducedVertices(model.getFloatAttackVertices(), indices, vertices.size() * 3);
		float[][] deathVertices = generateReducedVertices(model.getFloatDeathVertices(), indices, vertices.size() * 3);
		return new CombinedModel(indices, uvs, idleVertices, moveVertices, attackVertices, deathVertices);
	}

	private static float[][] generateReducedVertices(float[][] vertices, short[] indices, int length) {
		if (vertices == null)
			return null;

		float[][] newVertices = new float[vertices.length][];
		for (int i = 0; i < newVertices.length; i++) {
			newVertices[i] = new float[length];
			int nextVertex = 0;
			int curr = 0;
			int j = 0;
			for (int currentIndex = 0; currentIndex < indices.length;) {
				if (indices[currentIndex++] == nextVertex) {
					newVertices[i][curr++] = vertices[i][j++];
					newVertices[i][curr++] = vertices[i][j++];
					newVertices[i][curr++] = vertices[i][j++];
					nextVertex++;
				} else {
					j += 3;
				}
			}
		}
		return newVertices;
	}

	public static CombinedModel parse(String baseName) {
		Model idleModel = tryParse(DIRECTORY + baseName + "_idle.md2");
		Model moveModel = tryParse(DIRECTORY + baseName + "_walk.md2");
		Model attackModel = tryParse(DIRECTORY + baseName + "_attack.md2");
		Model deathModel = tryParse(DIRECTORY + baseName + "_death.md2");
		return new CombinedModel(idleModel.getIndices(), idleModel.getUvs(), idleModel.getVertices(),
				moveModel.getVertices(), attackModel.getVertices(), deathModel.getVertices());
	}

	private static Model tryParse(String location) {
		try {
			return MD2Parser.parse(new FileInputStream(location));
		} catch (IOException e) {
			System.out.println(location + " does not exist");
		}
		return new Model();
	}

	public static Model parse(InputStream fileIn) throws IOException {
		BufferedInputStream stream = new BufferedInputStream(fileIn);

		MD2Header header = new MD2Header();

		header.parse(stream);
		byte[] bytes = new byte[header.offsetEnd - 68];
		stream.read(bytes);
		// getMaterials(stream, bytes);

		float[] uvs = getTexCoords(bytes, header);

		KeyFrame[] frames = getFrames(bytes, header);

		ArrayList<Float> correctUVs = new ArrayList<Float>(uvs.length);
		int[] compressedIndices = getTriangles(bytes, uvs, correctUVs, header);
		for (int j = 0; j < frames.length; j++) {
			frames[j].setIndices(compressedIndices);
		}

		for (int i = 0; i < correctUVs.size(); i++) {
			uvs[i] = correctUVs.get(i);
		}

		float[][] vertices = new float[frames.length][];
		for (int i = 0; i < frames.length; i++) {
			vertices[i] = frames[i].getVertices();
		}
		int length = vertices[0].length / 3;

		short[] indices = new short[length];
		for (short i = 0; i < length; i++) {
			indices[i] = i;
		}
		return new Model(uvs, indices, vertices);
	}

	// private static void getMaterials(BufferedInputStream stream, byte[] bytes, MD2Header header) throws IOException {
	// ByteArrayInputStream ba = new ByteArrayInputStream(bytes, header.offsetSkins - 68, bytes.length
	// - header.offsetSkins);
	// LittleEndianDataInputStream is = new LittleEndianDataInputStream(ba);
	//
	// for (int i = 0; i < header.numSkins; i++) {
	// String skinPath = is.readString(64);
	// }
	// }

	private static float[] getTexCoords(byte[] bytes, MD2Header header) throws IOException {
		ByteArrayInputStream ba = new ByteArrayInputStream(bytes, header.offsetTexCoord - 68, bytes.length
				- header.offsetTexCoord);
		LittleEndianDataInputStream is = new LittleEndianDataInputStream(ba);
		float[] uvs = new float[header.numTexCoord * 2];

		for (int i = 0; i < uvs.length;) {
			uvs[i++] = (float) is.readShort() / (float) header.skinWidth;
			uvs[i++] = (float) is.readShort() / (float) header.skinHeight;
		}
		is.close();
		return uvs;
	}

	private static KeyFrame[] getFrames(byte[] bytes, MD2Header header) throws IOException {
		ByteArrayInputStream ba = new ByteArrayInputStream(bytes, header.offsetFrames - 68, bytes.length
				- header.offsetFrames);
		LittleEndianDataInputStream is = new LittleEndianDataInputStream(ba);
		KeyFrame[] frames = new KeyFrame[header.numFrames];
		for (int i = 0; i < header.numFrames; i++) {
			float scaleX = is.readFloat();
			float scaleY = is.readFloat();
			float scaleZ = is.readFloat();
			float translateX = is.readFloat();
			float translateY = is.readFloat();
			float translateZ = is.readFloat();
			// String name =
			is.readString(16);

			float[] vertices = new float[header.numVerts * 3];

			for (int j = 0; j < vertices.length;) {
				vertices[j++] = scaleX * is.readUnsignedByte() + translateX;
				vertices[j++] = scaleY * is.readUnsignedByte() + translateY;
				vertices[j++] = scaleZ * is.readUnsignedByte() + translateZ;

				// what is this for?
				// int normalIndex =
				is.readUnsignedByte();
			}
			frames[i] = new KeyFrame(vertices);

		}
		is.close();
		return frames;
	}

	private static int[] getTriangles(byte[] bytes, float[] uvs, ArrayList<Float> correctUVs, MD2Header header)
			throws IOException {
		ByteArrayInputStream ba = new ByteArrayInputStream(bytes, header.offsetTriangles - 68, bytes.length
				- header.offsetTriangles);
		LittleEndianDataInputStream is = new LittleEndianDataInputStream(ba);
		int[] indices = new int[header.numTriangles * 3];
		int index = 0;

		for (int i = 0; i < header.numTriangles; i++) {
			int[] vertexIDs = new int[3];
			int[] uvIDS = new int[3];

			indices[index + 2] = vertexIDs[2] = is.readUnsignedShort();
			indices[index + 1] = vertexIDs[1] = is.readUnsignedShort();
			indices[index] = vertexIDs[0] = is.readUnsignedShort();
			index += 3;

			uvIDS[2] = is.readUnsignedShort();
			uvIDS[1] = is.readUnsignedShort();
			uvIDS[0] = is.readUnsignedShort();
			correctUVs.add(uvs[uvIDS[0] * 2]);
			correctUVs.add(uvs[uvIDS[0] * 2 + 1]);
			correctUVs.add(uvs[uvIDS[1] * 2]);
			correctUVs.add(uvs[uvIDS[1] * 2 + 1]);
			correctUVs.add(uvs[uvIDS[2] * 2]);
			correctUVs.add(uvs[uvIDS[2] * 2 + 1]);
		}
		is.close();
		return indices;
	}

	// private static String readString(InputStream stream) throws IOException {
	// String result = new String();
	// byte inByte;
	// while ((inByte = (byte) stream.read()) != 0)
	// result += (char) inByte;
	// return result;
	// }

	private static int readInt(InputStream stream) throws IOException {
		return stream.read() | (stream.read() << 8) | (stream.read() << 16) | (stream.read() << 24);
	}

	// private static int readShort(InputStream stream) throws IOException {
	// return (stream.read() | (stream.read() << 8));
	// }
	//
	// private static float readFloat(InputStream stream) throws IOException {
	// return Float.intBitsToFloat(readInt(stream));
	// }

	private static class Vertex {
		public float x, y, z;
		public float u, v;

		public Vertex(float x, float y, float z, float u, float v) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.u = u;
			this.v = v;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Float.floatToIntBits(u);
			result = prime * result + Float.floatToIntBits(v);
			result = prime * result + Float.floatToIntBits(x);
			result = prime * result + Float.floatToIntBits(y);
			result = prime * result + Float.floatToIntBits(z);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Vertex other = (Vertex) obj;
			if (Float.floatToIntBits(u) != Float.floatToIntBits(other.u))
				return false;
			if (Float.floatToIntBits(v) != Float.floatToIntBits(other.v))
				return false;
			if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
				return false;
			if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
				return false;
			if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
				return false;
			return true;
		}

	}

	private static class MD2Header {
		public int id;
		public int version;
		public int skinWidth;
		public int skinHeight;
		// public int frameSize;
		// public int numSkins;
		public int numVerts;
		public int numTexCoord;
		public int numTriangles;
		// public int numGLCommands;
		public int numFrames;
		// public int offsetSkins;
		public int offsetTexCoord;
		public int offsetTriangles;
		public int offsetFrames;
		// public int offsetGLCommands;
		public int offsetEnd;

		public void parse(InputStream stream) throws IOException {
			id = readInt(stream);
			version = readInt(stream);

			if (id != 844121161 || version != 8)
				throw new IOException("This is not a valid MD2 file.");
			skinWidth = readInt(stream);
			if (skinWidth == 0)
				skinWidth = 512;
			skinHeight = readInt(stream);
			if (skinHeight == 0)
				skinHeight = 512;
			// frameSize =
			readInt(stream);

			// numSkins =
			readInt(stream);
			numVerts = readInt(stream);
			numTexCoord = readInt(stream);
			numTriangles = readInt(stream);
			// numGLCommands =
			readInt(stream);
			numFrames = readInt(stream);

			// offsetSkins =
			readInt(stream);
			offsetTexCoord = readInt(stream);
			offsetTriangles = readInt(stream);
			offsetFrames = readInt(stream);
			// offsetGLCommands =
			readInt(stream);
			offsetEnd = readInt(stream);
		}
	}
}
