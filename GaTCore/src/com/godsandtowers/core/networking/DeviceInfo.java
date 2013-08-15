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

import java.io.Serializable;

public class DeviceInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String model;
	private long maxMemory;
	private int numProcessors;
	private int apiLevel;
	private int gameEngineSpeed;
	private int meshQuality;
	private int textureQuality;

	public DeviceInfo(String model, long maxMemory, int numProcessors, int apiLevel, int gameEngineSpeed,
			int meshQuality, int textureQuality) {
		this.model = model;
		this.maxMemory = maxMemory;
		this.numProcessors = numProcessors;
		this.apiLevel = apiLevel;
		this.gameEngineSpeed = gameEngineSpeed;
		this.meshQuality = meshQuality;
		this.textureQuality = textureQuality;
	}

	public String getModel() {
		return model;
	}

	public long getMaxMemory() {
		return maxMemory;
	}

	public int getNumProcessors() {
		return numProcessors;
	}

	public int getApiLevel() {
		return apiLevel;
	}

	public int getGameEngineSpeed() {
		return gameEngineSpeed;
	}

	public int getMeshQuality() {
		return meshQuality;
	}

	public int getTextureQuality() {
		return textureQuality;
	}

	@Override
	public String toString() {
		return "DeviceInfo [model=" + model + ", maxMemory=" + maxMemory + ", numProcessors=" + numProcessors
				+ ", apiLevel=" + apiLevel + ", gameEngineSpeed=" + gameEngineSpeed + ", meshQuality=" + meshQuality
				+ ", textureQuality=" + textureQuality + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + apiLevel;
		result = prime * result + gameEngineSpeed;
		result = prime * result + (int) (maxMemory ^ (maxMemory >>> 32));
		result = prime * result + meshQuality;
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + numProcessors;
		result = prime * result + textureQuality;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeviceInfo other = (DeviceInfo) obj;
		if (apiLevel != other.apiLevel)
			return false;
		if (gameEngineSpeed != other.gameEngineSpeed)
			return false;
		if (maxMemory != other.maxMemory)
			return false;
		if (meshQuality != other.meshQuality)
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (numProcessors != other.numProcessors)
			return false;
		if (textureQuality != other.textureQuality)
			return false;
		return true;
	}

}
