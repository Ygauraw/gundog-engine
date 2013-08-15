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

import com.gundogstudios.gl.BaseModel;
import com.gundogstudios.gl.CombinedModel;
import com.gundogstudios.gl.GSModel;
import com.gundogstudios.gl.GSModelVBOs;
import com.gundogstudios.gl.ModelManager;
import com.gundogstudios.gl.ModelUtils;
import com.gundogstudios.gl.TextureManager;

public class Border extends CombinedModel {

	private static final String WALL_CORNER_MESH = "WallCorner";
	private static final String WALL_SIDE_MESH = "WallSide";
	public static final String NAME = "border";

	public Border(Border border) {
		super();
		for (BaseModel model : border.getModels()) {
			super.addModel((GSModel) model);
		}
	}

	public Border(TextureManager textureManager, ModelManager modelManager, float rows, float columns, String texture) {
		super();
		float xOffset = columns / 2f;
		float yOffset = rows / 2f;
		GSModel side = generateModel(textureManager, modelManager, texture, WALL_SIDE_MESH);

		for (float x = 0; x < columns; x++) {
			GSModel top = new GSModel(side);
			top.setXYTranslation(x - xOffset, yOffset);
			super.addModel(top);
			GSModel bottom = new GSModel(side);
			bottom.setXYTranslation(x - xOffset, -yOffset);
			bottom.setZRotation(180f);
			super.addModel(bottom);
		}

		for (float x = 0; x < rows; x++) {
			GSModel right = new GSModel(side);
			right.setXYTranslation(xOffset, x - yOffset);
			right.setZRotation(270f);
			super.addModel(right);
			GSModel left = new GSModel(side);
			left.setXYTranslation(-xOffset, x - yOffset);
			left.setZRotation(90f);
			super.addModel(left);
		}

		GSModel corner = generateModel(textureManager, modelManager, texture, WALL_CORNER_MESH);
		addCornerModel(corner, xOffset, yOffset);
		addCornerModel(corner, -xOffset, yOffset);
		addCornerModel(corner, xOffset, -yOffset);
		addCornerModel(corner, -xOffset, -yOffset);
	}

	private void addCornerModel(GSModel corner, float xOffset, float yOffset) {
		GSModel current = new GSModel(corner);
		current.setXYTranslation(xOffset, yOffset);
		super.addModel(current);
	}

	private GSModel generateModel(TextureManager textureManager, ModelManager modelManager, String textureName,
			String meshName) {
		int textureId = textureManager.getTextureID(textureName, true);
		GSModelVBOs data = modelManager.getGSModelVBOs(meshName, true);

		GSModel model = ModelUtils.createModel(textureId, data).get(0);

		model.setRotates(true);
		model.setXYZScale(.0001f);

		return model;
	}

	@Override
	public void setXYZTranslation(float x, float y, float z) {

	}
}
