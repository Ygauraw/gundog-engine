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
package com.godsandtowers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.godsandtowers.sprites.Races;

public class CreatureCreator {
	private static final String CREATURE_INPUT = "input/creatures.csv";
	private static final String CREATURE_OUTPUT = "output/creatures.java";

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(CREATURE_INPUT));

		BufferedWriter writer = new BufferedWriter(new FileWriter(CREATURE_OUTPUT));
		writer.append("private static final BaseCreature[] BASE_CREATURES = {\n");

		String line = reader.readLine(); // title row

		while ((line = reader.readLine()) != null) {
			processCreature(writer, line);
		}

		writer.append("};");

		reader.close();
		writer.close();
	}

	private static void processCreature(BufferedWriter writer, String line) throws IOException {
		String[] values = line.split(",");

		String name = values[0].toUpperCase();
		int races = Races.getRaces(values[1].split("/"));
		float cost = Float.parseFloat(values[2]);
		float speed = Float.parseFloat(values[3]);
		float defense = Float.parseFloat(values[4]);
		float damage = Float.parseFloat(values[5]);
		float attackRate = Float.parseFloat(values[6]);
		float attackRange = Float.parseFloat(values[7]);
		boolean isAir = Boolean.parseBoolean(values[8]);
		boolean isUnlocked = Boolean.parseBoolean(values[9]);

		writer.append("new BaseCreature(");
		writer.append(name);
		writer.append(",");
		writer.append("" + races);
		writer.append(",");
		writer.append("" + cost + "f");
		writer.append(",");
		writer.append("" + speed + "f");
		writer.append(",");
		writer.append("" + defense + "f");
		writer.append(",");
		writer.append("" + damage + "f");
		writer.append(",");
		writer.append("" + attackRate + "f");
		writer.append(",");
		writer.append("" + attackRange + "f");
		writer.append(",");
		writer.append("" + isAir);
		writer.append(",");
		writer.append("" + isUnlocked);
		writer.append("),\n");

	}
}
