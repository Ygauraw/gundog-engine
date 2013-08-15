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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.gundogstudios.models.CombinedModel;
import com.gundogstudios.models.ReducedModel;
import com.gundogstudios.models.md2.MD2Parser;
import com.gundogstudios.models.tristripper.PrimitiveGroup;
import com.gundogstudios.models.tristripper.TriStrip;

public class ReducedGSWriter {
	private static final String EXTENSION = ".gs1";
	private static final String IDLE = "Idle";
	private static final String MOVE = "Move";
	private static final String ATTACK = "Attack";
	private static final String DEATH = "Death";

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
		CombinedModel model = MD2Parser.parseReduced(baseName);

		TriStrip strip = new TriStrip();
		PrimitiveGroup[] groups = strip.generateStrips(model.getShortIndices());

		short[] sindices = new short[groups[0].indices.length];
		for (int i = 0; i < sindices.length; i++)
			sindices[i] = (short) groups[0].indices[i];
		model.setIndices(sindices);
		ReducedModel reduced = model.asReducedModel();
		writeModel(baseName, reduced);
	}

	private static void writeModel(String baseName, ReducedModel model) throws IOException {
		write(baseName, "Indices", model.getIndices());
		write(baseName, "UVs", model.getUVs());
		writeVertices(baseName, IDLE, model.getIdleVertices());
		writeVertices(baseName, MOVE, model.getMoveVertices());
		writeVertices(baseName, ATTACK, model.getAttackVertices());
		writeVertices(baseName, DEATH, model.getDeathVertices());

	}

	private static OutputStream getOutputStream(String folder, String filename) throws IOException {
		new File(folder).mkdir();
		return new FileOutputStream(folder + filename);
	}

	private static void write(String baseName, String fileName, byte[] bytes) throws IOException {
		if (bytes == null)
			return;

		OutputStream output = getOutputStream(LOCATION + baseName + "/", baseName + fileName + EXTENSION);
		output.write(bytes);
		output.close();
	}

	private static void writeVertices(String baseName, String actionName, byte[][] vertices) throws IOException {
		if (vertices == null)
			return;

		for (int i = 0; i < vertices.length; i++) {
			write(baseName, actionName + i, vertices[i]);
		}
	}

}
