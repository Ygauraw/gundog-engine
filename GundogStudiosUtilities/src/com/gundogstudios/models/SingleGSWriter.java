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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.HashSet;

import com.gundogstudios.models.md2.MD2Parser;
import com.gundogstudios.models.tristripper.PrimitiveGroup;
import com.gundogstudios.models.tristripper.TriStrip;

public class SingleGSWriter {
	private static final String LOCATION = "D:/Users/Joe/Documents/TowerDefenseWars/Graphics/Processed 3D Models/GS1/";
	private static final String EXTENSION = ".gs1";

	private static final short DEFAULT_ANIMATION_LENGTH = 1000;
	private static final String ARCHER_HORSEMEN = "ArcherHorsemen";
	private static final String ARCHERS = "Archers";
	private static final String ARROW_PROJECTILES = "ArrowProjectiles";
	private static final String BALL_PROJECTILES = "BallProjectiles";
	private static final String BALLISTAS = "Ballistas";
	private static final String BASE = "Base";
	private static final String BEARS = "Bears";
	private static final String BIRDS = "Birds";
	private static final String BLADE = "Blade";
	private static final String CACTUS = "Cactus";
	private static final String CANNONS = "Cannons";
	private static final String CATAPULTS = "Catapults";
	private static final String CRYSTALLIZERS = "Crystallizers";
	private static final String DEAD_HORSEMEN = "DeadHorsemen";
	private static final String DEAD_SOLDIERS = "DeadSoldiers";
	private static final String DRAGONS = "Dragons";
	private static final String EMITTERS = "Emitters";
	private static final String FEMALE_ANGELS = "FemaleAngels";
	private static final String FEMALE_MAGES = "FemaleMages";
	private static final String GOLEMS = "Golems";
	private static final String HORSEMEN = "Horsemen";
	private static final String HORSES = "Horses";
	private static final String HUMANOIDS = "Humanoids";
	private static final String HUMANOID_BEASTS = "HumanoidBeasts";
	private static final String MAGES = "Mages";
	private static final String MALE_ANGELS = "MaleAngels";
	private static final String PILLARS = "Pillars";
	private static final String ROCK = "Rock";
	private static final String RUBBLE = "Rubble";
	private static final String SOLDIERS = "Soldiers";
	private static final String STATUES = "Statues";
	private static final String TREE = "Tree";
	private static final String WALL_CORNER = "WallCorner";
	private static final String WALL_SIDE = "WallSide";
	private static final String WHIP = "Whip";

	private static final String[] MODELS = { ARCHER_HORSEMEN, ARCHERS, BALLISTAS, BASE, BEARS, BIRDS, BLADE, CACTUS,
			CANNONS, CATAPULTS, CRYSTALLIZERS, DEAD_HORSEMEN, DEAD_SOLDIERS, DRAGONS, EMITTERS, FEMALE_ANGELS,
			FEMALE_MAGES, GOLEMS, HORSEMEN, HORSES, HUMANOIDS, HUMANOID_BEASTS, MAGES, MALE_ANGELS, PILLARS,
			BALL_PROJECTILES, ARROW_PROJECTILES, ROCK, RUBBLE, SOLDIERS, STATUES, TREE, WALL_CORNER, WALL_SIDE, WHIP };

	private static final HashSet<String> REDUCE_IDLE = new HashSet<String>();
	private static final HashSet<String> FLIP_AXIS = new HashSet<String>();
	private static final HashMap<String, Integer> IDLE_ANIMATION_LENGTH = new HashMap<String, Integer>();
	private static final HashMap<String, Integer> MOVE_ANIMATION_LENGTH = new HashMap<String, Integer>();
	private static final HashMap<String, Integer> DEATH_ANIMATION_LENGTH = new HashMap<String, Integer>();
	private static final HashMap<String, Integer> ATTACK_ANIMATION_LENGTH = new HashMap<String, Integer>();

	static {
		REDUCE_IDLE.add(ARROW_PROJECTILES);
		REDUCE_IDLE.add(BALL_PROJECTILES);
		REDUCE_IDLE.add(BALLISTAS);
		REDUCE_IDLE.add(BASE);
		REDUCE_IDLE.add(BLADE);
		REDUCE_IDLE.add(CACTUS);
		REDUCE_IDLE.add(CANNONS);
		REDUCE_IDLE.add(CATAPULTS);
		// REDUCE_IDLE.add(CRYSTALLIZERS);
		REDUCE_IDLE.add(EMITTERS);
		REDUCE_IDLE.add(PILLARS);
		REDUCE_IDLE.add(ROCK);
		REDUCE_IDLE.add(STATUES);
		REDUCE_IDLE.add(TREE);
		REDUCE_IDLE.add(WALL_CORNER);
		REDUCE_IDLE.add(WALL_SIDE);
		// REDUCE_IDLE.add(WHIP);
		FLIP_AXIS.add(BALLISTAS);
		IDLE_ANIMATION_LENGTH.put(DEAD_SOLDIERS, 1500);
		IDLE_ANIMATION_LENGTH.put(HUMANOID_BEASTS, 1500);
		IDLE_ANIMATION_LENGTH.put(FEMALE_MAGES, 1500);
		IDLE_ANIMATION_LENGTH.put(DRAGONS, 2000);
		IDLE_ANIMATION_LENGTH.put(CRYSTALLIZERS, 4000);
		IDLE_ANIMATION_LENGTH.put(WHIP, 4000);
		ATTACK_ANIMATION_LENGTH.put(DEAD_SOLDIERS, 1500);
		ATTACK_ANIMATION_LENGTH.put(ARCHERS, 1500);
		ATTACK_ANIMATION_LENGTH.put(GOLEMS, 1500);
		ATTACK_ANIMATION_LENGTH.put(BIRDS, 2000);
		ATTACK_ANIMATION_LENGTH.put(DRAGONS, 2000);
		MOVE_ANIMATION_LENGTH.put(BIRDS, 2000);
		MOVE_ANIMATION_LENGTH.put(DRAGONS, 2000);
	}

	public static void main(String[] argv) throws Exception {
		for (String model : MODELS)
			writeModel(model);
		// writeModel(BALLISTAS);
		// writeModel(CACTUS);
		// writeModel(WALL_CORNER);
		// writeModel(WALL_SIDE);
	}

	private static void writeModel(String baseName) throws Exception {
		System.out.println("Writing: " + baseName);
		CombinedModel model = MD2Parser.parseReduced(baseName);
		if (REDUCE_IDLE.contains(baseName)) {
			System.out.println("Reducing idle for " + baseName + " from " + model.getFloatIdleVertices().length);
			float[][] vertices = new float[][] { model.getFloatIdleVertices()[0] };
			model.setIdleVertices(vertices);
		}

		if (FLIP_AXIS.contains(baseName)) {
			model.flipZAxis();
		}

		TriStrip strip = new TriStrip();
		PrimitiveGroup[] groups = strip.generateStrips(model.getShortIndices());

		short[] sindices = new short[groups[0].indices.length];
		for (int i = 0; i < sindices.length; i++) {
			sindices[i] = (short) groups[0].indices[i];
		}
		model.setIndices(sindices);
		ReducedModel reduced = model.asReducedModel();
		writeModel(baseName, reduced);
	}

	private static void writeModel(String baseName, ReducedModel model) throws IOException {
		OutputStream output = new FileOutputStream(LOCATION + baseName + EXTENSION);
		write(output, baseName, model);
		write(output, model.getIndices());
		write(output, model.getUVs());
		writeVertices(output, model.getIdleVertices());
		writeVertices(output, model.getMoveVertices());
		writeVertices(output, model.getAttackVertices());
		writeVertices(output, model.getDeathVertices());
		output.close();
	}

	private static void write(OutputStream output, String baseName, ReducedModel model) throws IOException {
		byte[] header = new byte[Short.SIZE / Byte.SIZE * 11];

		ByteBuffer byteBuffer = ByteBuffer.wrap(header).order(ByteOrder.LITTLE_ENDIAN);
		ShortBuffer shortBuffer = byteBuffer.asShortBuffer();

		shortBuffer.put((short) model.getIndices().length);
		shortBuffer.put((short) model.getUVs().length);
		shortBuffer.put((short) model.getIdleVertices()[0].length);

		shortBuffer.put(getVertexLength(model.getIdleVertices()));
		shortBuffer.put(getAnimationLength(IDLE_ANIMATION_LENGTH, baseName));

		shortBuffer.put(getVertexLength(model.getMoveVertices()));
		shortBuffer.put(getAnimationLength(MOVE_ANIMATION_LENGTH, baseName));

		shortBuffer.put(getVertexLength(model.getAttackVertices()));
		shortBuffer.put(getAnimationLength(ATTACK_ANIMATION_LENGTH, baseName));

		shortBuffer.put(getVertexLength(model.getDeathVertices()));
		shortBuffer.put(getAnimationLength(DEATH_ANIMATION_LENGTH, baseName));

		shortBuffer.put(getVertexLength(model.getIdleVertices()));
		shortBuffer.put(getAnimationLength(IDLE_ANIMATION_LENGTH, baseName));

		write(output, header);
	}

	private static short getVertexLength(byte[][] vertices) {
		if (vertices != null) {
			return (short) vertices.length;
		} else {
			return (short) 0;
		}
	}

	public static short getAnimationLength(HashMap<String, Integer> animationLengths, String baseName) {
		Integer length = animationLengths.get(baseName);
		if (length != null) {
			return (short) length.intValue();
		} else {
			return DEFAULT_ANIMATION_LENGTH;
		}

	}

	private static void write(OutputStream output, byte[] bytes) throws IOException {
		if (bytes == null) {
			return;
		}

		output.write(bytes);
	}

	private static void writeVertices(OutputStream output, byte[][] vertices) throws IOException {
		if (vertices == null) {
			return;
		}

		for (byte[] vertice : vertices) {
			write(output, vertice);
		}
	}

}
