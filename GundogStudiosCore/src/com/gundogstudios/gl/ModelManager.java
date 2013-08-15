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

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import com.gundogstudios.modules.AssetModule;
import com.gundogstudios.modules.GLES11Module;
import com.gundogstudios.modules.Modules;

public class ModelManager {

	private static final int GS_FRAME_RATE = 20;
	private static final String MODEL_PATH = "models/";
	private HashMap<String, GSModelVBOs> modelVBOs;
	private Queue<GSModelData> modelData;

	public ModelManager() {
		modelVBOs = new HashMap<String, GSModelVBOs>();
		modelData = new LinkedList<GSModelData>();
	}

	public void releaseVBOs() {
		ArrayList<Integer> vbos = new ArrayList<Integer>();

		for (GSModelVBOs model : modelVBOs.values()) {
			addVBO(vbos, model.getTextureBufferId());
			addVBO(vbos, model.getIndexBufferId());
			addVBOs(vbos, model.getAttackBufferIds());
			addVBOs(vbos, model.getDeathBufferIds());
			addVBOs(vbos, model.getIdleBufferIds());
			addVBOs(vbos, model.getMoveBufferIds());
		}

		int[] ids = new int[vbos.size()];
		int c = 0;
		for (Integer id : vbos) {
			ids[c++] = id;
		}
		Modules.GL.glDeleteBuffers(ids.length, ids, 0);
	}

	private void addVBO(ArrayList<Integer> vbos, int id) {
		if (id != 0)
			vbos.add(id);
	}

	private void addVBOs(ArrayList<Integer> vbos, int[] ids) {
		if (ids != null) {
			for (int id : ids) {
				vbos.add(id);
			}
		}
	}

	public GSModelVBOs getGSModelVBOs(String name, boolean isGLThread) {
		GSModelVBOs vbos = modelVBOs.get(name);
		if (vbos == null) {
			long start = System.currentTimeMillis();
			vbos = loadModelData(name, isGLThread);
			Modules.LOG.info("ModelManager", "TOOK " + (System.currentTimeMillis() - start) + " to load the mesh");
		}
		return vbos;
	}

	private GSModelVBOs loadModelData(String name, boolean isGLThread) {
		try {
			AssetModule assets = Modules.ASSETS;
			InputStream input = assets.openInput(MODEL_PATH, name);

			ReadableByteChannel channel = Channels.newChannel(input);// input.getChannel();
			int headerOffset = Short.SIZE / Byte.SIZE * 11;
			ByteBuffer buffer = ByteBuffer.allocate(headerOffset);
			channel.read(buffer);
			buffer.position(0);

			// Load GS1 header information
			int indexBufferLength = buffer.getShort();
			int uvBufferLength = buffer.getShort();
			int frameLength = buffer.getShort();
			int idleFrames = buffer.getShort();
			int idleAnimationLength = buffer.getShort();
			int moveFrames = buffer.getShort();
			int moveAnimationLength = buffer.getShort();
			int attackFrames = buffer.getShort();
			int attackAnimationLength = buffer.getShort();
			int deathFrames = buffer.getShort();
			int deathAnimationLength = buffer.getShort();

			// long offset = headerOffset;
			int size = indexBufferLength + uvBufferLength + frameLength
					* (idleFrames + moveFrames + attackFrames + deathFrames);

			buffer = ByteBuffer.allocate(size);
			channel.read(buffer);
			buffer.position(0);

			GSModelData data = new GSModelData(name, indexBufferLength, uvBufferLength, frameLength, idleFrames,
					idleAnimationLength, moveFrames, moveAnimationLength, attackFrames, attackAnimationLength,
					deathFrames, deathAnimationLength, buffer);

			input.close();

			if (isGLThread) {
				return convertToVBO(data);
			} else {
				synchronized (modelData) {
					modelData.add(data);
					modelData.wait();
				}
				return modelVBOs.get(name);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public void generateVBOs() {
		synchronized (modelData) {
			GSModelData data;
			while ((data = modelData.poll()) != null)
				convertToVBO(data);

			modelData.notifyAll();
		}
	}

	private GSModelVBOs convertToVBO(GSModelData data) {
		int maxFrames = ModelUtils.getMeshFrameRate();

		ByteBuffer buffer = data.buffer;
		int indexBufferLength = data.indexBufferLength;
		int uvBufferLength = data.uvBufferLength;
		int frameLength = data.frameLength;

		int bufferOffset = 0;
		int indexBufferId = generateIndexBuffer(buffer, bufferOffset, indexBufferLength);

		bufferOffset += indexBufferLength;
		int textureBufferId = generateTextureBuffer(buffer, bufferOffset, uvBufferLength);

		bufferOffset += uvBufferLength;
		int idleFrames = data.idleFrames;
		int[] idleBufferIds = generateVertexBuffer(buffer, bufferOffset, frameLength, idleFrames, maxFrames);

		bufferOffset += frameLength * idleFrames;
		int moveFrames = data.moveFrames;
		int[] moveBufferIds = generateVertexBuffer(buffer, bufferOffset, frameLength, moveFrames, maxFrames);

		bufferOffset += frameLength * moveFrames;
		int attackFrames = data.attackFrames;
		int[] attackBufferIds = generateVertexBuffer(buffer, bufferOffset, frameLength, attackFrames, maxFrames);

		bufferOffset += frameLength * attackFrames;
		int deathFrames = data.deathFrames;
		int[] deathBufferIds = generateVertexBuffer(buffer, bufferOffset, frameLength, deathFrames, maxFrames);

		GSModelVBOs vbos = new GSModelVBOs(indexBufferId, indexBufferLength / 2, textureBufferId,
				data.idleAnimationLength, idleBufferIds, data.moveAnimationLength, moveBufferIds,
				data.attackAnimationLength, attackBufferIds, data.deathAnimationLength, deathBufferIds);

		modelVBOs.put(data.name, vbos);
		return vbos;
	}

	private int generateIndexBuffer(ByteBuffer buffer, int offset, int size) {
		buffer.limit(offset + size);
		buffer.position(offset);
		GLES11Module gl = Modules.GL;
		int[] vboIds = new int[1];
		gl.glGenBuffers(1, vboIds, 0);
		gl.glBindBuffer(GLES11Module.GL_ELEMENT_ARRAY_BUFFER, vboIds[0]);
		gl.glBufferData(GLES11Module.GL_ELEMENT_ARRAY_BUFFER, size, buffer, GLES11Module.GL_STATIC_DRAW);
		gl.glBindBuffer(GLES11Module.GL_ELEMENT_ARRAY_BUFFER, 0);
		return vboIds[0];
	}

	private int generateTextureBuffer(ByteBuffer buffer, int offset, int size) {
		buffer.limit(offset + size);
		buffer.position(offset);

		GLES11Module gl = Modules.GL;
		int[] vboIds = new int[1];
		gl.glGenBuffers(vboIds.length, vboIds, 0);
		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, vboIds[0]);
		gl.glBufferData(GLES11Module.GL_ARRAY_BUFFER, size, buffer, GLES11Module.GL_STATIC_DRAW);
		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, 0);
		return vboIds[0];
	}

	private int[] generateVertexBuffer(ByteBuffer buffer, int offset, int frameLength, int frameCount, int maxFrames) {
		if (frameCount == 0)
			return null;

		GLES11Module gl = Modules.GL;
		int numVBOs = (frameCount < maxFrames) ? frameCount : maxFrames;

		int[] vboIds = new int[numVBOs];
		gl.glGenBuffers(vboIds.length, vboIds, 0);

		int step = GS_FRAME_RATE / maxFrames;
		int current = 0;
		for (int i = 0; i < frameCount; i++) {
			if (i % step == 0) {
				buffer.limit(offset + frameLength);
				buffer.position(offset);
				gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, vboIds[current]);
				gl.glBufferData(GLES11Module.GL_ARRAY_BUFFER, frameLength, buffer, GLES11Module.GL_STATIC_DRAW);
				current++;
			}
			offset += frameLength;
		}

		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, 0);
		return vboIds;
	}

	private class GSModelData {
		public String name;
		public int indexBufferLength;
		public int uvBufferLength;
		public int frameLength;
		public int idleFrames;
		public int idleAnimationLength;
		public int moveFrames;
		public int moveAnimationLength;
		public int attackFrames;
		public int attackAnimationLength;
		public int deathFrames;
		public int deathAnimationLength;
		public ByteBuffer buffer;

		public GSModelData(String name, int indexBufferLength, int uvBufferLength, int frameLength, int idleFrames,
				int idleAnimationLength, int moveFrames, int moveAnimationLength, int attackFrames,
				int attackAnimationLength, int deathFrames, int deathAnimationLength, ByteBuffer buffer) {
			this.name = name;
			this.indexBufferLength = indexBufferLength;
			this.uvBufferLength = uvBufferLength;
			this.frameLength = frameLength;
			this.idleFrames = idleFrames;
			this.idleAnimationLength = idleAnimationLength;
			this.moveFrames = moveFrames;
			this.moveAnimationLength = moveAnimationLength;
			this.attackFrames = attackFrames;
			this.attackAnimationLength = attackAnimationLength;
			this.deathFrames = deathFrames;
			this.deathAnimationLength = deathAnimationLength;
			this.buffer = buffer;
		}

	}
}
