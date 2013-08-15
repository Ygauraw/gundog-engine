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
package com.godsandtowers.core.commands;

import java.util.ArrayList;

import com.godsandtowers.messaging.GLMessageProcessor;
import com.godsandtowers.sprites.BaseSpecial;
import com.godsandtowers.sprites.Creature;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.sprites.Races;
import com.godsandtowers.sprites.Special;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.util.FastMath;

public class ExecuteSpecialCommand implements Runnable {
	private static final String TAG = "ExecuteSpecialCommand";
	private Player attacker;
	private Player defender;
	private String name;

	public ExecuteSpecialCommand(Player attacker, Player defender, String name) {
		this.attacker = attacker;
		this.defender = defender;
		this.name = name;
	}

	@Override
	public void run() {
		BaseSpecial baseSpecial = attacker.getSpecial(name);
		if (baseSpecial.getCount() <= 0) {
			return;
		}
		baseSpecial.decrement();

		Player player;
		switch (baseSpecial.getRaces()) {
		case Races.DEATH:
			player = executeDeathSpecial(baseSpecial);
			break;
		case Races.EARTH:
			player = executeEarthSpecial(baseSpecial);
			break;
		case Races.FIRE:
			player = executeFireSpecial(baseSpecial);
			break;
		case Races.ICE:
			player = executeIceSpecial(baseSpecial);
			break;
		case Races.LIFE:
			player = executeLifeSpecial(baseSpecial);
			break;
		case Races.WIND:
			player = executeWindSpecial(baseSpecial);
			break;
		default:
			Modules.LOG.error(TAG, "unknown race, unable to execute special");
			return;
		}
		Special special = new Special(baseSpecial);
		player.getGrid().addSpecial(special);
		Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.ADD_SPRITE, player.getID(), special);
	}

	private Player executeDeathSpecial(BaseSpecial baseSpecial) {
		int count = 0;
		int limit = FastMath.round(attacker.getGrid().getCreatures().size() * .25f);
		ArrayList<Creature> remove = new ArrayList<Creature>();
		for (Creature creature : attacker.getGrid().getCreatures()) {
			if (count++ >= limit)
				break;

			remove.add(creature);
		}
		for (Creature creature : remove) {
			creature.kill();
			attacker.getGrid().removeKilledCreature(creature);
		}

		return attacker;
	}

	private Player executeEarthSpecial(BaseSpecial baseSpecial) {
		for (Creature creature : attacker.getGrid().getCreatures()) {
			creature.setStun(attacker.getRace().getStunDuration());
		}
		return attacker;
	}

	private Player executeFireSpecial(BaseSpecial baseSpecial) {
		for (Creature creature : attacker.getGrid().getCreatures()) {
			creature.modifyHealth(.5f);
		}
		return attacker;
	}

	private Player executeIceSpecial(BaseSpecial baseSpecial) {
		for (Creature creature : attacker.getGrid().getCreatures()) {
			creature.setSlow(attacker.getRace().getSlowDuration(), attacker.getRace().getSlowFactor());
		}
		return attacker;
	}

	private Player executeLifeSpecial(BaseSpecial baseSpecial) {
		for (Creature creature : defender.getGrid().getCreatures()) {
			creature.heal();
		}
		return defender;
	}

	private Player executeWindSpecial(BaseSpecial baseSpecial) {
		for (Creature creature : defender.getGrid().getCreatures()) {
			creature.setSlow(attacker.getRace().getSlowDuration(), attacker.getRace().getCreatureSpeedModifier());
		}
		return defender;
	}
}
