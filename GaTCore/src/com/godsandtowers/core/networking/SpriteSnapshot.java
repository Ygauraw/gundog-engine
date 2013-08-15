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
package com.godsandtowers.core.networking;

import com.gundogstudios.gl.Sprite;

public class SpriteSnapshot implements Sprite {
	private int id;
	private int level;
	private int action;
	private String model;
	private float x;
	private float y;
	private float z;
	private Sprite target;
	private int targetID;
	private boolean transparent;

	public SpriteSnapshot(int id, int level, int action, String model, float x, float y, float z, int targetID,
			boolean transparent) {
		this.id = id;
		this.level = level;
		this.action = action;
		this.model = model;
		this.x = x;
		this.y = y;
		this.z = z;
		this.targetID = targetID;
		this.transparent = transparent;
	}

	public SpriteSnapshot(Sprite sprite) {
		this(sprite.getID(), sprite.getLevel(), sprite.getAction(), sprite.getModel(), sprite.getX(), sprite.getY(),
				sprite.getZ(), sprite.getTarget() == null ? 0 : sprite.getTarget().getID(), false);
	}

	public boolean isTransparent() {
		return transparent;
	}

	public void setTarget(Sprite target) {
		this.target = target;
	}

	public int getTargetID() {
		return targetID;
	}

	public int getID() {
		return id;
	}

	public int getLevel() {
		return level;
	}

	public int getAction() {
		return action;
	}

	public String getModel() {
		return model;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public Sprite getTarget() {
		return target;
	}

	public void update(SpriteSnapshot sprite) {
		x = sprite.x;
		y = sprite.y;
		z = sprite.z;
		targetID = sprite.targetID;
		action = sprite.action;
		level = sprite.level;
	}

	@Override
	public String toString() {
		return "SpriteSnapshot [id=" + id + ", level=" + level + ", action=" + action + ", model=" + model + ", x=" + x
				+ ", y=" + y + ", z=" + z + ", target=" + target + ", targetID=" + targetID + ", transparent="
				+ transparent + "]";
	}

}
