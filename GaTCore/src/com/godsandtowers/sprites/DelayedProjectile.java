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

public class DelayedProjectile implements Projectile, Externalizable {
	private Creature target;
	private Tower source;
	private int delay;

	public DelayedProjectile() {
	}

	public DelayedProjectile(Tower source, Creature target) {
		this.source = source;
		this.target = target;
		this.delay = 1000; // 1000 ms before hit
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

	public boolean move(int timePassed) {
		delay -= timePassed;
		return delay <= 0;
	}

	public boolean attacksAll() {
		return source.attacksAllInRange();
	}

	@Override
	public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
		source = (Tower) input.readObject();
		target = (Creature) input.readObject();
		delay = input.readInt();
	}

	@Override
	public void writeExternal(ObjectOutput output) throws IOException {
		output.writeObject(source);
		output.writeObject(target);
		output.writeInt(delay);
	}

}
