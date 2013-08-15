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
import java.util.HashMap;

import com.gundogstudios.gl.Actions;
import com.gundogstudios.gl.Sprite;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.util.FastMath;

public class MovingProjectile implements Sprite, Projectile, Externalizable {

	public static final String FIRE_BALL_PROJECTILE = "fire_ball_projectile";
	public static final String ICE_BALL_PROJECTILE = "ice_ball_projectile";
	public static final String WIND_BALL_PROJECTILE = "wind_ball_projectile";
	public static final String EARTH_BALL_PROJECTILE = "earth_ball_projectile";
	public static final String LIFE_BALL_PROJECTILE = "life_ball_projectile";
	public static final String UNDEAD_BALL_PROJECTILE = "undead_ball_projectile";

	public static final String FIRE_ARROW_PROJECTILE = "fire_arrow_projectile";
	public static final String ICE_ARROW_PROJECTILE = "ice_arrow_projectile";
	public static final String EARTH_ARROW_PROJECTILE = "earth_arrow_projectile";

	private static final float SPEED = 200; // 200ms to move 1 grid square
	private static final float Z_OFFSET = .5f;// hits the center of the models

	private static final HashMap<String, String> type = new HashMap<String, String>();

	static {
		type.put(BaseTower.EARTH_BALLISTA, EARTH_ARROW_PROJECTILE);
		type.put(BaseTower.FLAME_BALLISTA, FIRE_ARROW_PROJECTILE);
		type.put(BaseTower.ICE_BALLISTA, ICE_ARROW_PROJECTILE);
	}

	private Creature target;
	private Tower source;
	private float x;
	private float y;
	private float z;
	private int id;
	private boolean atTarget;

	public MovingProjectile() {
	}

	public MovingProjectile(Tower source, Creature target) {
		this.source = source;
		this.target = target;
		this.x = source.getX();
		this.y = source.getY();
		this.z = Z_OFFSET;
		this.atTarget = false;
		this.id = IDGenerator.getNextID();
	}

	public Race getRace() {
		return source.getRace();
	}

	public float getDamage() {
		return source.getDamage();
	}

	public Creature getTarget() {
		return target;
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

	@Override
	public boolean attacksAll() {
		return source.attacksAllInRange();
	}

	public boolean move(int timePassed) {
		float distanceMoved = (float) timePassed / SPEED;
		float targetX = target.getX();
		float targetY = target.getY();
		float targetZ = target.getZ() + Z_OFFSET;
		float xdiff = (targetX - x);
		float ydiff = (targetY - y);
		float zdiff = (targetZ - z);

		float targetDistance = FastMath.sqrt(xdiff * xdiff + ydiff * ydiff + zdiff * zdiff);
		if (targetDistance == 0)
			return true;
		xdiff /= targetDistance;
		ydiff /= targetDistance;
		zdiff /= targetDistance;

		if (targetDistance - distanceMoved > 0.0f) {
			x += xdiff * distanceMoved;
			y += ydiff * distanceMoved;
			z += zdiff * distanceMoved;
			return false;
		} else {
			x = targetX;
			y = targetY;
			z = targetZ;
			atTarget = true;
			return true;
		}
	}

	@Override
	public int getLevel() {
		return source.getLevel();
	}

	public boolean atTarget() {
		return atTarget;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public String getModel() {
		String name = type.get(source.getBaseTower().getName());
		if (name != null)
			return name;

		// TODO can only handle 1 race at a time, not going to work for combo races
		int race = Races.pickRandomRace(getRace().getRaces());
		switch (race) {
		case Races.DEATH:
			return UNDEAD_BALL_PROJECTILE;
		case Races.EARTH:
			return EARTH_BALL_PROJECTILE;
		case Races.FIRE:
			return FIRE_BALL_PROJECTILE;
		case Races.ICE:
			return ICE_BALL_PROJECTILE;
		case Races.LIFE:
			return LIFE_BALL_PROJECTILE;
		case Races.WIND:
			return WIND_BALL_PROJECTILE;

		}
		Modules.LOG.error("MovingProjectile", "Unknown race: " + race);
		return "";
	}

	@Override
	public int getAction() {
		if (atTarget)
			return Actions.DEATH;
		else
			return Actions.IDLE;
	}

	@Override
	public String toString() {
		return "MovingProjectile [x=" + x + ", y=" + y + ", z=" + z + ", id=" + id + "]";
	}

	@Override
	public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
		source = (Tower) input.readObject();
		target = (Creature) input.readObject();
		x = input.readFloat();
		y = input.readFloat();
		z = input.readFloat();
		id = input.readInt();
		atTarget = input.readBoolean();
	}

	@Override
	public void writeExternal(ObjectOutput output) throws IOException {
		output.writeObject(source);
		output.writeObject(target);
		output.writeFloat(x);
		output.writeFloat(y);
		output.writeFloat(z);
		output.writeInt(id);
		output.writeBoolean(atTarget);
	}

}
