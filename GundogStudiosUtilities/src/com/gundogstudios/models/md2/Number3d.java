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
package com.gundogstudios.models.md2;

public class Number3d {
	public float x;
	public float y;
	public float z;

	private static Number3d TEMP = new Number3d();

	public Number3d() {
		x = 0;
		y = 0;
		z = 0;
	}

	public Number3d(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setAll(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setAllFrom(Number3d n) {
		x = n.x;
		y = n.y;
		z = n.z;
	}

	public void normalize() {
		float mod = (float) Math.sqrt(x * x + y * y + z * z);

		if (mod != 0 && mod != 1) {
			mod = 1 / mod;
			x *= mod;
			y *= mod;
			z *= mod;
		}
	}

	public void add(Number3d n) {
		x += n.x;
		y += n.y;
		z += n.z;
	}

	public void subtract(Number3d n) {
		x -= n.x;
		y -= n.y;
		z -= n.z;
	}

	public void multiply(float f) {
		x *= f;
		y *= f;
		z *= f;
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	public Number3d clone() {
		return new Number3d(x, y, z);
	}

	public void rotateX(float angle) {
		float cosRY = (float) Math.cos(angle);
		float sinRY = (float) Math.sin(angle);

		TEMP.setAllFrom(this);

		y = (TEMP.y * cosRY) - (TEMP.z * sinRY);
		z = (TEMP.y * sinRY) + (TEMP.z * cosRY);
	}

	public void rotateY(float angle) {
		float cosRY = (float) Math.cos(angle);
		float sinRY = (float) Math.sin(angle);

		TEMP.setAllFrom(this);

		x = (TEMP.x * cosRY) + (TEMP.z * sinRY);
		z = (TEMP.x * -sinRY) + (TEMP.z * cosRY);
	}

	public void rotateZ(float angle) {
		float cosRY = (float) Math.cos(angle);
		float sinRY = (float) Math.sin(angle);

		TEMP.setAllFrom(this);

		x = (TEMP.x * cosRY) - (TEMP.y * sinRY);
		y = (TEMP.x * sinRY) + (TEMP.y * cosRY);
	}

	@Override
	public String toString() {
		return x + "," + y + "," + z;
	}

	public static Number3d add(Number3d a, Number3d b) {
		return new Number3d(a.x + b.x, a.y + b.y, a.z + b.z);
	}

	public static Number3d subtract(Number3d a, Number3d b) {
		return new Number3d(a.x - b.x, a.y - b.y, a.z - b.z);
	}

	public static Number3d multiply(Number3d a, Number3d b) {
		return new Number3d(a.x * b.x, a.y * b.y, a.z * b.z);
	}

	public static Number3d cross(Number3d v, Number3d w) {
		return new Number3d((w.y * v.z) - (w.z * v.y), (w.z * v.x) - (w.x * v.z), (w.x * v.y) - (w.y * v.x));
	}

	public static float dot(Number3d v, Number3d w) {
		return (v.x * w.x + v.y * w.y + w.z * v.z);
	}
}
