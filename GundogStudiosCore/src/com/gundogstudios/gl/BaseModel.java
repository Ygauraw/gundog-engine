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

import com.gundogstudios.modules.GLES11Module;
import com.gundogstudios.modules.Modules;

public abstract class BaseModel {
	protected float rx = 0;
	protected float ry = 0;
	protected float rz = 0;
	protected float x = 0;
	protected float y = 0;
	protected float z = 0;
	protected float sx = 1f;
	protected float sy = 1f;
	protected float sz = 1f;
	protected float red = 1f;
	protected float green = 1f;
	protected float blue = 1f;
	protected float alpha = 1f;
	protected int totalTime = 0;
	protected int modelAction = Actions.IDLE;
	protected boolean rotates = false;

	public BaseModel(int action) {
		this.modelAction = action;
	}

	public BaseModel(BaseModel model) {
		this.rx = model.rx;
		this.ry = model.ry;
		this.rz = model.rz;
		this.x = model.x;
		this.y = model.y;
		this.z = model.z;
		this.sx = model.sx;
		this.sy = model.sy;
		this.sz = model.sz;
		this.red = model.red;
		this.green = model.green;
		this.blue = model.blue;
		this.alpha = model.alpha;
		this.totalTime = model.totalTime;
		this.modelAction = model.modelAction;
		this.rotates = model.rotates;
	}

	protected abstract void drawModel(int timePassed);

	public abstract void reset();

	public void draw(int action, int timePassed) {
		GLES11Module gl = Modules.GL;
		if (Actions.isAction(modelAction, action)) {
			gl.glPushMatrix();
			gl.glTranslatef(x, y, z);
			if (rotates) { // TODO find a better way to do this by overriding the set RX,RY,RZ function in a subclass
				gl.glRotatef(rx, 1, 0, 0);
				gl.glRotatef(ry, 0, 1, 0);
				gl.glRotatef(rz, 0, 0, 1);
			}
			gl.glScalef(sx, sy, sz);
			gl.glColor4f(red, green, blue, alpha);
			drawModel(timePassed);
			gl.glPopMatrix();
		}
	}

	public void setAction(int action) {
		this.modelAction = action;
	}

	public void setRotates(boolean rotates) {
		this.rotates = rotates;

	}

	public void setZRotation(float rz) {
		this.rz = rz;
	}

	public void setXYZScale(float scaleSize) {
		this.setXYZScale(scaleSize, scaleSize, scaleSize);
	}

	public void modifyXYZScale(float scale) {
		sx *= scale;
		sy *= scale;
		sz *= scale;
	}

	public void modifyXYZScale(float sx, float sy, float sz) {
		this.sx *= sx;
		this.sy *= sy;
		this.sz *= sz;
	}

	public void setXYZScale(float sx, float sy, float sz) {
		this.sx = sx;
		this.sy = sy;
		this.sz = sz;
	}

	public void setXYZRotation(float rx, float ry, float rz) {
		this.rx = rx;
		this.ry = ry;
		this.rz = rz;
	}

	public void setZTranslation(float z) {
		this.z = z;
	}

	public void setXYTranslation(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void setXYZTranslation(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setRGB(float r, float g, float b) {
		this.red = r;
		this.green = g;
		this.blue = b;
	}

	public void setRGBA(float r, float g, float b, float a) {
		red = r;
		green = g;
		blue = b;
		alpha = a;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
}
