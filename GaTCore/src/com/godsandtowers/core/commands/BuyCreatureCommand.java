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

import com.godsandtowers.core.GameInfo;
import com.godsandtowers.messaging.GLMessageProcessor;
import com.godsandtowers.sprites.BaseCreature;
import com.godsandtowers.sprites.Creature;
import com.godsandtowers.sprites.Player;
import com.gundogstudios.modules.Modules;

public class BuyCreatureCommand implements Runnable {

	private GameInfo stats;
	private Player attacker;
	private Player defender;
	private String name;

	public BuyCreatureCommand(GameInfo stats, Player attacker, Player defender, String name) {
		this.stats = stats;
		this.attacker = attacker;
		this.defender = defender;
		this.name = name;
	}

	@Override
	public void run() {
		BaseCreature baseCreature = attacker.getCreature(name);
		float cost = baseCreature.getCost();
		if (attacker.getGold() >= cost) {
			float incomeChange = baseCreature.getIncome();
			attacker.increaseIncome(incomeChange);
			attacker.decreaseGold(cost);

			Creature creature = new Creature(attacker, baseCreature);
			defender.getGrid().addCreature(creature);

			Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.ADD_SPRITE, defender.getID(), creature);

			stats.creaturePurchased(attacker.getID(), creature);
		}
	}

}
