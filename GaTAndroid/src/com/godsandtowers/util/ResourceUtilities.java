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
package com.godsandtowers.util;

import java.text.NumberFormat;

import android.content.res.Resources;

import com.godsandtowers.R;
import com.godsandtowers.achievements.Achievement;
import com.godsandtowers.achievements.UnlockAchievement;
import com.godsandtowers.sprites.BaseCreature;
import com.godsandtowers.sprites.BaseSpecial;
import com.godsandtowers.sprites.BaseTower;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.util.FastMath;

public class ResourceUtilities {

	private static final String NEW_LINE = "\n";
	private static final String SPACER = ": ";

	private static Resources resources;
	private static NumberFormat formatter;

	public static void init(Resources resources) {
		ResourceUtilities.resources = resources;
		formatter = NumberFormat.getInstance();
		formatter.setMaximumFractionDigits(0);
	}

	public static String getAchievementInfo(Achievement achievement) {
		if (achievement instanceof UnlockAchievement)
			return getString("achievement_" + achievement.getName()) + " " + getString(achievement.getName());
		else
			return getString("achievement_" + achievement.getName());
	}

	public static int getAchievementIcon(Achievement achievement) {
		return getIconID(achievement.getName());
	}

	public static String getName(BaseCreature creature) {
		return getString(creature.getName());
	}

	public static String[] getNames(BaseCreature[] creatures) {
		String[] names = new String[creatures.length];
		for (int i = 0; i < names.length; i++) {
			names[i] = getName(creatures[i]);
		}
		return names;
	}

	public static int getIconID(BaseCreature creature) {
		return getIconID(creature.getName());
	}

	public static int[] getIconIDs(BaseCreature[] creatures) {
		int[] ids = new int[creatures.length];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = getIconID(creatures[i]);
		}
		return ids;
	}

	public static String getName(BaseTower tower) {
		return getString(tower.getName());
	}

	public static String[] getNames(BaseTower[] towers) {
		String[] names = new String[towers.length];
		for (int i = 0; i < names.length; i++) {
			names[i] = getName(towers[i]);
		}
		return names;
	}

	public static int getIconID(BaseTower tower) {
		return getIconID(tower.getName());
	}

	public static int[] getIconIDs(BaseTower[] towers) {
		int[] ids = new int[towers.length];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = getIconID(towers[i]);
		}
		return ids;
	}

	public static int[] getIconIDs(String[] names) {
		int[] ids = new int[names.length];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = getIconID(names[i]);
		}
		return ids;
	}

	public static int getStringID(String name) {
		return resources.getIdentifier(resources.getText(R.string.app_package) + ":string/" + name, null, null);
	}

	public static String getString(String name) {
		int id = resources.getIdentifier(resources.getText(R.string.app_package) + ":string/" + name, null, null);
		if (id == 0)
			Modules.LOG.error("ResourceUtilities", name + " does not exists in getName");
		return resources.getString(id);
	}

	public static int getIconID(String name) {
		int id = resources.getIdentifier(resources.getText(R.string.app_package) + ":drawable/" + "icon_" + name, null,
				null);
		if (id == 0)
			Modules.LOG.error("ResourceUtilities", name + " does not exists in getIconID");
		return id;
	}

	public static String getInfo(BaseSpecial special) {
		return getString(special.getName());
	}

	public static String getInfo(BaseCreature creature) {

		StringBuilder builder = new StringBuilder();
		append(R.string.stat_cost, creature.getCost(), builder);
		append(R.string.stat_income, creature.getIncome(), builder);
		append(R.string.stat_health, creature.getHealth(), builder);
		append(R.string.stat_speed, creature.getSpeed() / 1000f, builder);
		append(R.string.stat_defense, creature.getDefense(), builder);
		append(R.string.stat_damage, creature.getDamage(), builder);
		append(R.string.stat_attack_rate, creature.getAttackRate() / 1000f, builder);
		append(R.string.stat_air, creature.isAir(), builder);

		return builder.toString();
	}

	public static String getInfo(BaseTower tower) {

		StringBuilder builder = new StringBuilder();
		append(R.string.stat_cost, tower.getCost(), builder);
		append(R.string.stat_damage, tower.getDamage(), builder);
		append(R.string.stat_attack_range, tower.getAttackRange(), builder);
		append(R.string.stat_attack_rate, tower.getAttackRate() / 1000f, builder);
		append(R.string.stat_health, tower.getHealth(), builder);
		append(R.string.stat_defense, tower.getDefense(), builder);
		append(R.string.stat_ground, tower.attacksGround(), builder);
		append(R.string.stat_air, tower.attacksAir(), builder);

		return builder.toString();
	}

	private static void append(int id, boolean text, StringBuilder builder) {
		builder.append(resources.getString(id));
		builder.append(SPACER);
		builder.append(text);
		builder.append(NEW_LINE);
	}

	private static void append(int id, float text, StringBuilder builder) {
		builder.append(resources.getString(id));
		builder.append(SPACER);
		builder.append(formatter.format(FastMath.ceil(text)));
		builder.append(NEW_LINE);
	}

}
