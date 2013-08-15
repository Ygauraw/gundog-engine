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

public class Tower implements Sprite, Externalizable {

	private BaseTower baseTower;
	private Race race;

	private boolean building;
	private float health;
	private float cooldown;
	private float x;
	private float y;
	private float z;
	private Creature target;
	private int id;

	public Tower() {
	}

	public Tower(BaseTower baseTower, BaseRace race, float x, float y) {
		this.baseTower = baseTower;
		this.x = x;
		this.y = y;
		this.race = new Race(race, baseTower.getRaces());
		cooldown = 0;
		health = baseTower.getHealth();
		this.id = IDGenerator.getNextID();
	}

	public float getPower() {
		return baseTower.getPower();
	}

	public int getLevel() {
		return baseTower.getLevel();
	}

	public int getTotalUpgradeCount() {
		return baseTower.getTotalUpgradeCount();
	}

	public void setTarget(Creature target) {
		this.target = target;
	}

	public Creature getTarget() {
		return target;
	}

	public boolean attacksAllInRange() {
		return baseTower.attacksAllInRange();
	}

	public boolean instantAttack() {
		return baseTower.attacksInstantly();
	}

	public String getName() {
		return baseTower.getName();
	}

	public BaseTower getBaseTower() {
		return baseTower;
	}

	public void replace(BaseTower baseTower) {
		this.baseTower = baseTower;
	}

	public Race getRace() {
		return race;
	}

	public float getDamage() {
		return baseTower.getDamage() * race.getTowerDamageModifier();
	}

	public float getFireRate() {
		return baseTower.getAttackRate() * race.getTowerAttackRateModifier();
	}

	public float getAttackRange() {
		return baseTower.getAttackRange();
	}

	public float getDefense() {
		return baseTower.getDefense() * race.getTowerDefenseModifier();
	}

	public boolean attacksGround() {
		return baseTower.attacksGround();
	}

	public boolean attacksAir() {
		return baseTower.attacksAir();
	}

	public float getCost() {
		return baseTower.getCost();
	}

	public boolean subtractHealth(float damage) {
		damage /= getDefense();
		health -= damage;
		return health <= 0.0f;
	}

	public float getHealth() {
		return health;
	}

	public boolean isAlive() {
		return health > 0;
	}

	public boolean attacksOnlyAir() {
		return baseTower.attacksAir() && !baseTower.attacksGround();
	}

	public boolean attacksOnlyGround() {
		return !baseTower.attacksAir() && baseTower.attacksGround();
	}

	public float getSellValue() {
		return baseTower.getCost() / 2;
	}

	public boolean canFire(int timePassed) {
		timePassed *= race.getTowerAttackRateModifier();
		if (cooldown > 0) {
			cooldown -= timePassed;
			return false;
		} else {
			return true;
		}
	}

	public float getCooldown() {
		return cooldown;
	}

	public void shot() {
		cooldown = baseTower.getAttackRate();
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

	@Override
	public String toString() {
		return "Tower [x=" + x + ", y=" + y + ", z=" + z + ", id=" + id + ", getLevel()=" + getLevel()
				+ ", getModel()=" + getModel() + "]";
	}

	@Override
	public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
		baseTower = (BaseTower) input.readObject();
		race = (Race) input.readObject();
		health = input.readFloat();
		cooldown = input.readFloat();
		x = input.readFloat();
		y = input.readFloat();
		z = input.readFloat();
		id = input.readInt();
		building = input.readBoolean();
		target = (Creature) input.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput output) throws IOException {
		output.writeObject(baseTower);
		output.writeObject(race);
		output.writeFloat(health);
		output.writeFloat(cooldown);
		output.writeFloat(x);
		output.writeFloat(y);
		output.writeFloat(z);
		output.writeInt(id);
		output.writeBoolean(building);
		output.writeObject(target);
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public String getModel() {
		return baseTower.getName();
	}

	@Override
	public int getAction() {
		if (health <= 0)
			return Actions.DEATH;
		else if (cooldown > 0 && target != null)
			return Actions.ATTACK;
		else
			return Actions.IDLE;
	}

}
