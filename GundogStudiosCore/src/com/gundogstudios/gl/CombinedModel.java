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

import java.util.ArrayList;

public class CombinedModel {
	private int currentAction;
	private ArrayList<BaseModel> models;

	public CombinedModel() {
		this(new ArrayList<BaseModel>());
	}

	public CombinedModel(BaseModel model) {
		models = new ArrayList<BaseModel>();
		models.add(model);
		this.currentAction = Actions.IDLE;
	}

	public CombinedModel(ArrayList<BaseModel> models) {
		this.models = models;
		this.currentAction = Actions.IDLE;
	}

	public ArrayList<BaseModel> getModels() {
		return models;
	}

	public void addModel(BaseModel model) {
		models.add(model);
	}

	public void addModels(ArrayList<BaseModel> models) {
		this.models.addAll(models);
	}

	public void addModels(CombinedModel combinedModel) {
		this.models.addAll(combinedModel.models);
	}

	public void setAction(int action) {
		if (action != currentAction)
			reset();
		this.currentAction = action;
	}

	public void modifyXYZScale(float scale) {
		for (BaseModel model : models) {
			model.modifyXYZScale(scale);
		}
	}

	public void setAlpha(float alpha) {
		for (BaseModel model : models) {
			model.setAlpha(alpha);
		}
	}

	public void setXYTranslation(float x, float y) {
		for (BaseModel model : models) {
			model.setXYTranslation(x, y);
		}
	}

	public void setXYZTranslation(float x, float y, float z) {
		for (BaseModel model : models) {
			model.setXYZTranslation(x, y, z);
		}
	}

	public void setRGB(float r, float g, float b) {
		for (BaseModel model : models) {
			model.setRGB(r, g, b);
		}
	}

	public void setRGBA(float r, float g, float b, float a) {
		for (BaseModel model : models) {
			model.setRGBA(r, g, b, a);
		}
	}

	public void setZRotation(float rz) {
		for (BaseModel model : models) {
			model.setZRotation(rz);
		}
	}

	public void setZTranslation(float z) {
		for (BaseModel model : models) {
			model.setZTranslation(z);
		}
	}

	public void draw(int timePassed) {
		for (BaseModel model : models) {
			model.draw(currentAction, timePassed);
		}
	}

	public void reset() {
		for (BaseModel model : models) {
			model.reset();
		}
	}

}
