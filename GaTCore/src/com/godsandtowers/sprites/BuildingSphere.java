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
package com.godsandtowers.sprites;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.gundogstudios.gl.Actions;
import com.gundogstudios.gl.Sprite;
import com.gundogstudios.util.FastMath;

public class BuildingSphere implements Sprite, Externalizable {
	public static final String NAME = "building_sphere";
	private float x;
	private float y;
	private float z;
	private int id;
	private int level;

	public BuildingSphere(Tower tower) {
		this.x = tower.getX();
		this.y = tower.getY();
		this.z = tower.getZ();
		this.id = IDGenerator.getNextID();
		this.level = FastMath.round(tower.getAttackRange() * 7.5f);
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public float getZ() {
		return z;
	}

	@Override
	public Sprite getTarget() {
		return null;
	}

	@Override
	public String getModel() {
		return NAME;
	}

	@Override
	public int getAction() {
		return Actions.IDLE;
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeFloat(x);
		out.writeFloat(y);
		out.writeFloat(z);
		out.writeInt(id);
		out.writeInt(level);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		x = in.readFloat();
		y = in.readFloat();
		z = in.readFloat();
		id = in.readInt();
		level = in.readInt();
	}

}
