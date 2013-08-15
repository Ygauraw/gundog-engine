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
package com.godsandtowers.achievements;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.godsandtowers.core.PlayerStats;

public abstract class Achievement implements Externalizable {
	public static final int BRONZE = 1;
	public static final int SILVER = 2;
	public static final int GOLD = 3;
	public static final int PLATINUM = 4;
	protected String name;
	protected boolean completed;
	protected int achievementLevel;

	public Achievement() {
	}

	public Achievement(String name, int achievementLevel) {
		this.name = name;
		this.achievementLevel = achievementLevel;
		this.completed = false;
	}

	public String getName() {
		return name;
	}

	public int getAchievementLevel() {
		return achievementLevel;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (completed ? 1231 : 1237);
		result = prime * result + achievementLevel;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Achievement other = (Achievement) obj;
		if (completed != other.completed)
			return false;
		if (achievementLevel != other.achievementLevel)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public boolean executeIfNotCompleted(PlayerStats playerStats) {
		if (!completed) {
			completed = execute(playerStats);
			return completed;
		} else {
			return false;
		}
	}

	protected abstract boolean execute(PlayerStats playerStats);

	public abstract float getPercentComplete(PlayerStats playerStats);

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeBoolean(completed);
		out.writeInt(achievementLevel);
		out.writeObject(name);

	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		completed = in.readBoolean();
		achievementLevel = in.readInt();
		name = (String) in.readObject();
	}
}