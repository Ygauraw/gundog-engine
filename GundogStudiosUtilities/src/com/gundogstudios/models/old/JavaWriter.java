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
package com.gundogstudios.models.old;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.gundogstudios.models.CombinedModel;
import com.gundogstudios.models.md2.MD2Parser;
import com.gundogstudios.models.tristripper.PrimitiveGroup;
import com.gundogstudios.models.tristripper.TriStrip;

public class JavaWriter {
	private static final String IDLE = "Idle";
	private static final String MOVE = "Move";
	private static final String ATTACK = "Attack";
	private static final String DEATH = "Death";

	private static final boolean REDUCED_FRAMES = false;
	private static final boolean REDUCED_VERTICES = true;
	// private static final String LOCATION =
	// "D:/Users/Joe/Workspace/TDWGenerated/src/com/towerdefensewars/graphics/game/gl/models/generated/";
	private static final String LOCATION = "output/";

	public static void main(String[] argv) throws Exception {
		// System.out.println(ByteOrder.nativeOrder());
		writeModel("Cannons");
		writeModel("Emitters");
		writeModel("Catapults");
		writeModel("Pillars");
		writeModel("Base");
		writeModel("Projectiles");
		writeModel("Rubble");
		writeModel("Statues");
		writeModel("ArcherHorsemen");
		writeModel("Archers");
		writeModel("DeadHorsemen");
		writeModel("Horsemen");
		writeModel("Horses");
		writeModel("Bears");
		writeModel("Birds");
		writeModel("DeadSoldiers");
		writeModel("Dragons");
		writeModel("FemaleAngels");
		writeModel("Golems");
		writeModel("MaleAngels");
		writeModel("Soldiers");
	}

	private static void writeModel(String baseName) throws Exception {
		System.out.println("Writing: " + baseName);
		CombinedModel model;
		if (REDUCED_VERTICES) {
			model = MD2Parser.parseReduced(baseName);
			TriStrip strip = new TriStrip();
			PrimitiveGroup[] groups = strip.generateStrips(model.getShortIndices());

			short[] sindices = new short[groups[0].indices.length];
			for (int i = 0; i < sindices.length; i++)
				sindices[i] = (short) groups[0].indices[i];
			model.setIndices(sindices);
		} else
			model = MD2Parser.parse(baseName);
		writeModel(baseName, model);
	}

	private static void writeModel(String baseName, CombinedModel model) throws IOException {
		writeIndices(baseName, model.getByteIndices());
		writeUVs(baseName, model.getByteUVs());
		writeVertices(baseName, IDLE, model.getByteIdleVertices());
		writeVertices(baseName, MOVE, model.getByteMoveVertices());
		writeVertices(baseName, ATTACK, model.getByteAttackVertices());
		writeVertices(baseName, DEATH, model.getByteDeathVertices());
		writeWrapper(baseName, model, 5);
		if (!REDUCED_FRAMES) {
			writeWrapper(baseName, model, 10);
			writeWrapper(baseName, model, 20);
		}
	}

	private static BufferedWriter getBufferedWriter(String folder, String filename) throws IOException {
		new File(folder.toLowerCase()).mkdir();
		return new BufferedWriter(new FileWriter(folder.toLowerCase() + filename));
	}

	private static void writeIndices(String baseName, byte[] indices) throws IOException {
		if (indices == null)
			return;

		BufferedWriter output = getBufferedWriter(LOCATION + baseName + "/", baseName + "Indices.java");
		output.write("package com.godsandtowers.graphics.game.gl.models.generated." + baseName.toLowerCase() + ";\n\n");
		output.write("public class " + baseName + "Indices {\n");

		output.write("public static byte[] INDICES = new byte[] {\n");
		System.out.println(indices.length);
		for (int i = 0; i < indices.length; i++) {
			output.write("" + indices[i] + ",");
			if (i % 10 == 9) {
				output.write("\n");
			}
		}
		output.write("};\n\n");

		output.write("}\n");
		output.close();
	}

	private static void writeUVs(String baseName, byte[] uvs) throws IOException {
		if (uvs == null)
			return;

		BufferedWriter output = getBufferedWriter(LOCATION + baseName + "/", baseName + "UVs.java");
		output.write("package com.godsandtowers.graphics.game.gl.models.generated." + baseName.toLowerCase() + ";\n\n");
		output.write("public class " + baseName + "UVs {\n");
		System.out.println(uvs.length);
		output.write("public static byte[] UVS = new byte[] {\n");
		for (int i = 0; i < uvs.length; i++) {
			output.write("" + uvs[i] + ",");
			if (i % 10 == 9) {
				output.write("\n");
			}
		}
		output.write("};\n\n");

		output.write("}\n");
		output.close();
	}

	private static void writeVertices(String baseName, String actionName, byte[][] vertices) throws IOException {
		if (vertices == null)
			return;

		for (int i = 0; i < vertices.length; i += (REDUCED_FRAMES ? 4 : 1)) {
			BufferedWriter output = getBufferedWriter(LOCATION + baseName + "/", baseName + actionName + i + ".java");
			output.write("package com.godsandtowers.graphics.game.gl.models.generated." + baseName.toLowerCase()
					+ ";\n\n");
			output.write("public class " + baseName + actionName + i + "{\n");

			output.write("public static byte[] VERTICES = new byte[] {\n");
			System.out.println(vertices[i].length);
			for (int j = 0; j < vertices[i].length; j++) {
				output.write("" + vertices[i][j] + ",");
				if (j % 10 == 9) {
					output.write("\n");
				}
			}
			output.write("};\n\n");

			output.write("}\n");
			output.close();
		}
	}

	private static void writeWrapper(String baseName, CombinedModel model, int maxFrames) throws IOException {
		BufferedWriter output = getBufferedWriter(LOCATION + baseName + "/", baseName + maxFrames + ".java");
		output.write("package com.godsandtowers.graphics.game.gl.models.generated." + baseName.toLowerCase() + ";\n\n");

		output.write("\npublic class " + baseName + maxFrames + " {\n");

		output.write("public " + baseName + maxFrames + "() {}\n\n");

		if (model.getShortIndices() != null) {
			output.write("public byte[] getIndices() { return " + baseName + "Indices.INDICES; }\n\n");
		} else {
			output.write("public byte[] getIndices() { return null; }\n\n");
		}

		if (model.getFloatUVs() != null) {
			output.write("public byte[] getUVs() { return " + baseName + "UVs.UVS; }\n\n");
		} else {
			output.write("public byte[] getUVs() { return null; }\n\n");
		}

		writeModelVertices(output, model.getFloatIdleVertices(), baseName, IDLE, maxFrames);
		writeModelVertices(output, model.getFloatMoveVertices(), baseName, MOVE, maxFrames);
		writeModelVertices(output, model.getFloatAttackVertices(), baseName, ATTACK, maxFrames);
		writeModelVertices(output, model.getFloatDeathVertices(), baseName, DEATH, maxFrames);

		writeReset(output, baseName, model, maxFrames);
		output.write("}\n");
		output.close();
	}

	private static void writeModelVertices(BufferedWriter output, float[][] vertices, String baseName,
			String actionName, int maxFrames) throws IOException {
		if (vertices != null) {

			output.write("public byte[][] get" + actionName + "Vertices() { return new byte[][] {\n");
			int count = 0;
			int stepSize = (maxFrames > vertices.length) ? vertices.length : vertices.length / maxFrames;
			for (int i = 0; i < vertices.length; i += stepSize) {
				output.write(baseName + actionName + i + ".VERTICES,");
				if (++count % 5 == 0) {
					output.write("\n");
				}
			}

			output.write("}; \n}\n\n");
		} else {
			output.write("public byte[][] get" + actionName + "Vertices() { return null; }\n\n");
		}
	}

	private static void writeReset(BufferedWriter output, String baseName, CombinedModel model, int maxFrames)
			throws IOException {

		output.write("public void reset() {\n");
		if (model.getShortIndices() != null) {
			output.write(baseName + "Indices.INDICES = null;\n");
		}

		if (model.getFloatUVs() != null) {
			output.write(baseName + "UVs.UVS = null;\n");
		}

		writeReset(output, model.getFloatIdleVertices(), baseName, IDLE, maxFrames);
		writeReset(output, model.getFloatMoveVertices(), baseName, MOVE, maxFrames);
		writeReset(output, model.getFloatAttackVertices(), baseName, ATTACK, maxFrames);
		writeReset(output, model.getFloatDeathVertices(), baseName, DEATH, maxFrames);
		output.write("}\n");
	}

	private static void writeReset(BufferedWriter output, float[][] vertices, String baseName, String actionName,
			int maxFrames) throws IOException {
		if (vertices != null) {
			int stepSize = (maxFrames > vertices.length) ? vertices.length : vertices.length / maxFrames;
			for (int i = 0; i < vertices.length; i += stepSize) {
				output.write(baseName + actionName + i + ".VERTICES = null;\n");
			}
		}
	}
}
