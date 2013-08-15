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

public class GSModelVBOs {

	private int indexBufferId;
	private int indexCount;
	private int textureBufferId;
	private int idleAnimationLength;
	private int[] idleBufferIds;
	private int moveAnimationLength;
	private int[] moveBufferIds;
	private int attackAnimationLength;
	private int[] attackBufferIds;
	private int deathAnimationLength;
	private int[] deathBufferIds;

	public GSModelVBOs(int indexBufferId, int indexCount, int textureBufferId, int idleAnimationLength,
			int[] idleBufferIds, int moveAnimationLength, int[] moveBufferIds, int attackAnimationLength,
			int[] attackBufferIds, int deathAnimationLength, int[] deathBufferIds) {
		this.indexBufferId = indexBufferId;
		this.indexCount = indexCount;
		this.textureBufferId = textureBufferId;
		this.idleAnimationLength = idleAnimationLength;
		this.idleBufferIds = idleBufferIds;
		this.moveAnimationLength = moveAnimationLength;
		this.moveBufferIds = moveBufferIds;
		this.attackAnimationLength = attackAnimationLength;
		this.attackBufferIds = attackBufferIds;
		this.deathAnimationLength = deathAnimationLength;
		this.deathBufferIds = deathBufferIds;
	}

	public int getIndexBufferId() {
		return indexBufferId;
	}

	public int getIndexCount() {
		return indexCount;
	}

	public int getTextureBufferId() {
		return textureBufferId;
	}

	public int getIdleAnimationLength() {
		return idleAnimationLength;
	}

	public int[] getIdleBufferIds() {
		return idleBufferIds;
	}

	public int getMoveAnimationLength() {
		return moveAnimationLength;
	}

	public int[] getMoveBufferIds() {
		return moveBufferIds;
	}

	public int getAttackAnimationLength() {
		return attackAnimationLength;
	}

	public int[] getAttackBufferIds() {
		return attackBufferIds;
	}

	public int getDeathAnimationLength() {
		return deathAnimationLength;
	}

	public int[] getDeathBufferIds() {
		return deathBufferIds;
	}

}
