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

import com.godsandtowers.core.grid.GridSquare;
import com.gundogstudios.gl.Actions;
import com.gundogstudios.gl.Sprite;
import com.gundogstudios.util.FastMath;

public class Creature implements Sprite, Externalizable {
	private static final int ONE_SECOND = 1000; // ms
	private GridSquare currentGridSquare;
	private BaseCreature baseCreature;
	private Race race;
	private Player owner;

	private float health;
	private float slowDuration = 0;
	private float slowFactor = 0;
	private float stunDuration = 0;
	private float drainDuration = 0;
	private float drainFactor = 0;
	private float cooldown = 0;
	private Tower target;

	private float x = 0;
	private float y = 0;
	private float z = 0;
	private int id = 0;

	public Creature() {
	}

	public Creature(Player owner, BaseCreature baseCreature) {
		this.owner = owner;
		this.race = new Race(owner.getRace().getBaseRace(), baseCreature.getRaces());
		this.baseCreature = baseCreature;
		this.health = (int) (baseCreature.getHealth() * race.getCreatureHealthModifier());
		this.id = IDGenerator.getNextID();
	}

	public float getPower() {
		return baseCreature.getPower();
	}

	public int getTotalUpgradeCount() {
		return baseCreature.getTotalUpgradeCount();
	}

	public int getLevel() {
		return baseCreature.getLevel();
	}

	public float getAttackRange() {
		return baseCreature.getAttackRange();
	}

	public String getName() {
		return baseCreature.getName();
	}

	public Race getRace() {
		return race;
	}

	public float getDefense() {
		return baseCreature.getDefense() * race.getCreatureDefenseModifier();
	}

	public float getDamage() {
		return baseCreature.getDamage() * race.getCreatureDamageModifier();
	}

	public float getAttackRate() {
		return baseCreature.getAttackRate();
	}

	public float getCost() {
		return baseCreature.getCost();
	}

	public float getIncome() {
		return baseCreature.getIncome();
	}

	public boolean isAir() {
		return baseCreature.isAir();
	}

	public boolean isAttacking() {
		if (owner.isAttacking())
			return target != null && target.isAlive();
		else {
			target = null;
			return false;
		}
	}

	public void setTarget(Tower target) {
		this.target = target;
	}

	public Tower getAttackingTarget() {
		return target;
	}

	@Override
	public Sprite getTarget() {
		if (target != null)
			return target;
		else {
			if (isAir()) {
				return currentGridSquare.getNextAir();
			} else {
				return currentGridSquare.getNext();
			}
		}
	}

	public boolean canAttack(int timePassed) {
		if (cooldown > 0) {
			cooldown -= timePassed;
			return false;
		} else if (!owner.isAttacking() || getDamage() <= 0.0f) {
			return false;
		} else {
			return true;
		}
	}

	public void attacked() {
		cooldown = baseCreature.getAttackRate();
	}

	public void setSlow(float slowDuration, float slowFactor) {
		this.slowDuration = slowDuration;
		this.slowFactor = slowFactor;
	}

	public void setStun(float stunDuration) {
		this.stunDuration = stunDuration;
	}

	public void setDrain(float drainDuration, float drainFactor) {
		this.drainDuration = drainDuration;
		this.drainFactor = drainFactor;
	}

	public void nextTick(int timePassed) {
		if (slowDuration > 0)
			slowDuration -= timePassed;
		if (stunDuration > 0)
			stunDuration -= timePassed;
		if (drainDuration > 0) {
			health -= (health * drainFactor * timePassed / ONE_SECOND);
			if (health <= 0) {
				health = 1;
			}
			drainDuration -= timePassed;
		}
		if (race.isLife()) {
			float diff = baseCreature.getHealth() - health;
			if (diff > 0) {
				float maxIncrease = (baseCreature.getHealth() * race.getHealPercentage() * timePassed / ONE_SECOND);
				health += (maxIncrease > diff) ? diff : maxIncrease;
			}
		}
	}

	public boolean isSlowed() {
		return slowDuration > 0;
	}

	public boolean isStunned() {
		return stunDuration > 0;
	}

	public boolean isDraining() {
		return drainDuration > 0;
	}

	public float getHealth() {
		return health;
	}

	public boolean isAlive() {
		return health > 0.0f;
	}

	public void kill() {
		health = 0.0f;
	}

	public void remove() {
		health = Float.MIN_VALUE;
	}

	public void heal() {
		health = baseCreature.getHealth() * race.getCreatureHealthModifier();
	}

	public void resurrect() {
		this.health = baseCreature.getHealth() * race.getCreatureHealthModifier();
	}

	public boolean subtractHealth(float damage) {
		damage /= getDefense();
		health -= damage;
		return health <= 0.0f;
	}

	public void modifyHealth(float percent) {
		health = percent * health;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public float getNextX() {
		if (isAir()) {
			return currentGridSquare.getNextAir().getX();
		} else {
			return currentGridSquare.getNext().getX();
		}
	}

	public float getNextY() {
		if (isAir()) {
			return currentGridSquare.getNextAir().getY();
		} else {
			return currentGridSquare.getNext().getY();
		}
	}

	public float getTargetX() {
		return target.getX();
	}

	public float getTargetY() {
		return target.getY();
	}

	public void setGridSquare(GridSquare grid) {
		this.currentGridSquare = grid;
	}

	public GridSquare getCurrentGridSquare() {
		return currentGridSquare;
	}

	public float move(int timePassed) {
		float time = timePassed;
		if (stunDuration > 0)
			return 0;

		time *= race.getCreatureSpeedModifier();

		if (slowDuration > 0)
			time *= slowFactor;

		float distanceMoved = time / baseCreature.getSpeed();

		return moveDistance(distanceMoved);
	}

	public float moveDistance(float distanceMoved) {
		float xdiff = (getNextX() - x);
		float ydiff = (getNextY() - y);

		float targetDistance = FastMath.sqrt(xdiff * xdiff + ydiff * ydiff);
		if (targetDistance == 0)
			return 0;
		xdiff /= targetDistance;
		ydiff /= targetDistance;

		if (targetDistance - distanceMoved > 0.0f) {
			x += xdiff * distanceMoved;
			y += ydiff * distanceMoved;
			return 0;
		} else {
			return distanceMoved - targetDistance;
		}
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public String getModel() {
		return baseCreature.getName();
	}

	@Override
	public int getAction() {
		if (health <= 0)
			return Actions.DEATH;
		else if (stunDuration > 0)
			return Actions.IDLE;
		else if (cooldown > 0 && target != null)
			return Actions.ATTACK;
		else
			return Actions.MOVE;
	}

	@Override
	public String toString() {
		return "Creature [x=" + x + ", y=" + y + ", z=" + z + ", id=" + id + ", getLevel()=" + getLevel()
				+ ", getModel()=" + getModel() + "]";
	}

	@Override
	public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
		race = (Race) input.readObject();
		baseCreature = (BaseCreature) input.readObject();
		currentGridSquare = (GridSquare) input.readObject();
		health = input.readFloat();
		slowDuration = input.readFloat();
		slowFactor = input.readFloat();
		stunDuration = input.readFloat();
		drainDuration = input.readFloat();
		drainFactor = input.readFloat();
		cooldown = input.readFloat();
		x = input.readFloat();
		y = input.readFloat();
		z = input.readFloat();
		id = input.readInt();
		target = (Tower) input.readObject();
		owner = (Player) input.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput output) throws IOException {
		output.writeObject(race);
		output.writeObject(baseCreature);
		output.writeObject(currentGridSquare);
		output.writeFloat(health);
		output.writeFloat(slowDuration);
		output.writeFloat(slowFactor);
		output.writeFloat(stunDuration);
		output.writeFloat(drainDuration);
		output.writeFloat(drainFactor);
		output.writeFloat(cooldown);
		output.writeFloat(x);
		output.writeFloat(y);
		output.writeFloat(z);
		output.writeInt(id);
		output.writeObject(target);
		output.writeObject(owner);

	}

}
