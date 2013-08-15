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
import java.util.ArrayList;

import com.gundogstudios.modules.GLES11Module;
import com.gundogstudios.modules.Modules;

public class ModelUtils {

	private static final int CHAR_SIZE = Character.SIZE / Byte.SIZE;
	private static final int FLOAT_SIZE = Float.SIZE / Byte.SIZE;
	public static final String TEXTURE_QUALITY = "TextureQuality";
	public static final String MESH_QUALITY = "MeshQuality";

	public static final int LOW_QUALITY = 0;
	public static final int MEDIUM_QUALITY = 1;
	public static final int HIGH_QUALITY = 2;

	public static int generateIndexBuffer(short[] indices) {
		if (indices == null)
			return 0;
		int[] vboIds = new int[1];
		GLES11Module gl = Modules.GL;
		gl.glGenBuffers(1, vboIds, 0);
		ByteBuffer indexByteBuffer = ByteBuffer.allocateDirect(CHAR_SIZE * indices.length);
		CharBuffer indexBuffer = indexByteBuffer.order(ByteOrder.nativeOrder()).asCharBuffer();

		for (int x = 0; x < indices.length; x++) {
			indexBuffer.put((char) indices[x]);
		}

		indexBuffer.position(0);
		gl.glBindBuffer(GLES11Module.GL_ELEMENT_ARRAY_BUFFER, vboIds[0]);
		gl.glBufferData(GLES11Module.GL_ELEMENT_ARRAY_BUFFER, indexByteBuffer.capacity(), indexByteBuffer,
				GLES11Module.GL_STATIC_DRAW);
		gl.glBindBuffer(GLES11Module.GL_ELEMENT_ARRAY_BUFFER, 0);
		return vboIds[0];
	}

	public static int generateTextureBuffer(float[] uvs) {
		if (uvs == null)
			return 0;
		int[] vboIds = new int[1];
		GLES11Module gl = Modules.GL;
		gl.glGenBuffers(vboIds.length, vboIds, 0);

		ByteBuffer uvByteBuffer = ByteBuffer.allocateDirect(uvs.length * FLOAT_SIZE).order(ByteOrder.nativeOrder());
		FloatBuffer uvBuffer = uvByteBuffer.asFloatBuffer();

		uvBuffer.put(uvs);

		uvByteBuffer.position(0);
		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, vboIds[0]);
		gl.glBufferData(GLES11Module.GL_ARRAY_BUFFER, uvByteBuffer.capacity(), uvByteBuffer,
				GLES11Module.GL_STATIC_DRAW);

		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, 0);
		return vboIds[0];
	}

	public static int[] generateVertexBuffer(float[][] vertices) {
		if (vertices == null)
			return null;

		GLES11Module gl = Modules.GL;
		int[] vboIds = new int[vertices.length];
		gl.glGenBuffers(vboIds.length, vboIds, 0);

		for (int i = 0; i < vertices.length; i++) {
			ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect(vertices[i].length * FLOAT_SIZE).order(
					ByteOrder.nativeOrder());
			FloatBuffer vertexBuffer = vertexByteBuffer.asFloatBuffer();
			vertexBuffer.put(vertices[i]);

			vertexByteBuffer.position(0);
			gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, vboIds[i]);
			gl.glBufferData(GLES11Module.GL_ARRAY_BUFFER, vertexByteBuffer.capacity(), vertexByteBuffer,
					GLES11Module.GL_STATIC_DRAW);
		}
		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, 0);
		return vboIds;
	}

	public static ArrayList<GSModel> createModel(int textureId, GSModelVBOs modelData) {
		ArrayList<GSModel> models = new ArrayList<GSModel>();
		int indexBufferID = modelData.getIndexBufferId();
		int textureBufferID = modelData.getTextureBufferId();
		int indexCount = modelData.getIndexCount();

		int[] actionIDs = modelData.getIdleBufferIds();
		int animationLength = modelData.getIdleAnimationLength();
		int action = Actions.IDLE;
		if (actionIDs != null) {
			GSModel model = new GSModel(action, animationLength, actionIDs, indexBufferID, textureBufferID, textureId,
					indexCount);
			models.add(model);
		}

		actionIDs = modelData.getMoveBufferIds();
		animationLength = modelData.getMoveAnimationLength();
		action = Actions.MOVE;
		if (actionIDs != null) {
			GSModel model = new GSModel(action, animationLength, actionIDs, indexBufferID, textureBufferID, textureId,
					indexCount);
			models.add(model);
		}

		actionIDs = modelData.getAttackBufferIds();
		animationLength = modelData.getMoveAnimationLength();
		action = Actions.ATTACK;
		if (actionIDs != null) {
			GSModel model = new GSModel(action, animationLength, actionIDs, indexBufferID, textureBufferID, textureId,
					indexCount);
			models.add(model);
		}

		actionIDs = modelData.getDeathBufferIds();
		animationLength = modelData.getMoveAnimationLength();
		action = Actions.DEATH;
		if (actionIDs != null) {
			GSModel model = new GSModel(action, animationLength, actionIDs, indexBufferID, textureBufferID, textureId,
					indexCount);
			models.add(model);
		}

		return models;
	}

	public static boolean useMIPMAPs() {
		return false;// ModuleManager.PREFERENCES.get(TEXTURE_QUALITY, HIGH_QUALITY) != LOW_QUALITY;
	}

	public static int getTextureFilter() {
		return getTextureQuality() == HIGH_QUALITY ? GLES11Module.GL_LINEAR : GLES11Module.GL_NEAREST;
	}

	public static int getMeshQuality() {
		return Modules.PREFERENCES.get(MESH_QUALITY, HIGH_QUALITY);
	}

	public static int getTextureQuality() {
		long memory = Runtime.getRuntime().maxMemory();
		int quality;
		if (memory < 20000000) {
			quality = LOW_QUALITY;
		} else if (memory < 40000000) {
			quality = MEDIUM_QUALITY;
		} else {
			quality = HIGH_QUALITY;
		}

		return Modules.PREFERENCES.get(TEXTURE_QUALITY, quality);
	}

	public static int getMeshFrameRate() {
		switch (getMeshQuality()) {
		case (HIGH_QUALITY):
			return 20;
		case (MEDIUM_QUALITY):
			return 10;
		case (LOW_QUALITY):
			return 5;
		default:
			return 1;
		}
	}

	public static int getBitmapScaleSize() {
		switch (getTextureQuality()) {
		case (HIGH_QUALITY):
			return 1;
		case (MEDIUM_QUALITY):
			return 2;
		case (LOW_QUALITY):
		default:
			return 4;
		}
	}

	public static float getParticleScaleSize() {
		switch (getTextureQuality()) {
		case (HIGH_QUALITY):
			return 1f;
		case (MEDIUM_QUALITY):
			return 1.5f;
		case (LOW_QUALITY):
		default:
			return 2f;
		}
	}

	public static void decreaseTextureQuality() {
		int newQuality;
		switch (getTextureQuality()) {
		case (HIGH_QUALITY):
			newQuality = MEDIUM_QUALITY;
		case (MEDIUM_QUALITY):
			newQuality = LOW_QUALITY;
		case (LOW_QUALITY):
		default:
			newQuality = LOW_QUALITY;
		}
		Modules.PREFERENCES.put(TEXTURE_QUALITY, newQuality);
	}
}
