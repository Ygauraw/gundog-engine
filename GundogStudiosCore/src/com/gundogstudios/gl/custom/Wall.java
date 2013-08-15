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
package com.gundogstudios.gl.custom;

import com.gundogstudios.gl.Actions;
import com.gundogstudios.gl.AnimatedModel;
import com.gundogstudios.gl.BaseModel;
import com.gundogstudios.gl.CombinedModel;
import com.gundogstudios.gl.ModelUtils;
import com.gundogstudios.gl.TextureManager;

public class Wall extends CombinedModel {

	public static final String NAME = "wall_side";

	public Wall(Wall border) {
		super();
		for (BaseModel model : border.getModels()) {
			super.addModel(new AnimatedModel((AnimatedModel) model));
		}

	}

	public Wall(TextureManager textureManager, int rows, int columns) {
		super();
		int textureId = textureManager.getTextureID(NAME, true);
		short[] indices = { 1, 1, 0, 2, 3, 3, 4, 4, 5, 7, 6, 6, 9, 9, 8, 10, 11, 11, 13, 13, 12, 14, 15, 15, 17, 17,
				16, 18, 19, 19, 21, 21, 20, 22, 23, };

		int indexBufferId = ModelUtils.generateIndexBuffer(indices);

		float width = columns * .5f;
		float height = 1f;
		float depth = rows * .5f;

		// TOP
		int[] vertexBufferIds = ModelUtils.generateVertexBuffer(new float[][] { generateBoxVertices(-width * 4f, depth,
				0, width * 4f, depth * 4, height) });
		int textureBufferId = ModelUtils.generateTextureBuffer(generateBoxUVs(8f, 1f));
		AnimatedModel model = new AnimatedModel(Actions.IDLE, vertexBufferIds, indexBufferId, textureBufferId,
				textureId, indices.length);
		super.addModel(model);

		// BOTTOM
		vertexBufferIds = ModelUtils.generateVertexBuffer(new float[][] { generateBoxVertices(-width * 4f, -depth * 4,
				0, width * 4f, -depth, height) });
		textureBufferId = ModelUtils.generateTextureBuffer(generateBoxUVs(8f, 1f));
		model = new AnimatedModel(Actions.IDLE, vertexBufferIds, indexBufferId, textureBufferId, textureId,
				indices.length);
		super.addModel(model);

		// LEFT
		vertexBufferIds = ModelUtils.generateVertexBuffer(new float[][] { generateBoxVertices(-width * 4f, -depth, 0,
				-width, depth, height) });
		textureBufferId = ModelUtils.generateTextureBuffer(generateBoxUVs(3f, 2f));
		model = new AnimatedModel(Actions.IDLE, vertexBufferIds, indexBufferId, textureBufferId, textureId,
				indices.length);
		super.addModel(model);

		// RIGHT
		vertexBufferIds = ModelUtils.generateVertexBuffer(new float[][] { generateBoxVertices(width, -depth, 0,
				width * 4, depth, height) });
		textureBufferId = ModelUtils.generateTextureBuffer(generateBoxUVs(3f, 2f));
		model = new AnimatedModel(Actions.IDLE, vertexBufferIds, indexBufferId, textureBufferId, textureId,
				indices.length);
		super.addModel(model);

	}

	private static float[] generateBoxVertices(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
		return new float[] { minX, minY, maxZ, maxX, minY, maxZ, // 0,1 = front
				maxX, maxY, maxZ, minX, maxY, maxZ, // 2,3
				minX, minY, minZ, maxX, minY, minZ, // 4,5 = back
				maxX, maxY, minZ, minX, maxY, minZ, // 6,7 (index below)
				maxX, minY, maxZ, maxX, minY, minZ, // 1,5 = right 8,9
				maxX, maxY, minZ, maxX, maxY, maxZ, // 6,2 10,11
				minX, minY, minZ, minX, minY, maxZ, // 4,0 = left 12,13
				minX, maxY, maxZ, minX, maxY, minZ, // 3,7 14,15
				minX, maxY, maxZ, maxX, maxY, maxZ, // 3,2 = top 16,17
				maxX, maxY, minZ, minX, maxY, minZ, // 6,7 18,19
				maxX, minY, maxZ, minX, minY, maxZ, // 1,0 = bottom 20,21
				minX, minY, minZ, maxX, minY, minZ // 4,5 22,23
		};

	}

	private static float[] generateBoxUVs(float xCount, float factor) {
		return new float[] { 0, 0, xCount, 0, xCount, 1, 0, 1, // front
				0, 0, xCount / factor, 0, xCount / factor, 1, 0, 1, // back
				0, 0, xCount / factor, 0, xCount / factor, 1, 0, 1, // right
				0, 0, xCount / factor, 0, xCount / factor, 1, 0, 1, // left
				0, 0, xCount / factor, 0, xCount / factor, 1, 0, 1, // top
				0, 0, xCount / factor, 0, xCount / factor, 1, 0, 1 // bottom
		};

	}

}
