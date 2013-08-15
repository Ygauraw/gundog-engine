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

public class TowerCreator {
	private static final String TOWER_INPUT = "input/towers.csv";
	private static final String TOWER_OUTPUT = "output/towers.java";

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(TOWER_INPUT));

		BufferedWriter writer = new BufferedWriter(new FileWriter(TOWER_OUTPUT));
		writer.append("private static final BaseTower[] BASE_TOWERS = {\n");

		String line = reader.readLine(); // title row

		while ((line = reader.readLine()) != null) {
			processTower(writer, line);
		}

		writer.append("};");

		reader.close();
		writer.close();
	}

	private static void processTower(BufferedWriter writer, String line) throws IOException {
		String[] values = line.split(",");

		String name = values[0].toUpperCase();
		int races = Races.getRaces(values[1].split("/"));
		float cost = Float.parseFloat(values[2]);
		float damage = Float.parseFloat(values[3]);
		float health = Float.parseFloat(values[4]);
		float defense = Float.parseFloat(values[5]);
		float attackRate = Float.parseFloat(values[6]);
		float attackRange = Float.parseFloat(values[7]);
		boolean attacksGround = Boolean.parseBoolean(values[8]);
		boolean attacksAir = Boolean.parseBoolean(values[9]);
		boolean attacksAll = Boolean.parseBoolean(values[10]);
		boolean attacksInstantly = Boolean.parseBoolean(values[11]);
		boolean isUnlocked = Boolean.parseBoolean(values[12]);

		writer.append("new BaseTower(");
		writer.append(name);
		writer.append(",");
		writer.append("" + races);
		writer.append(",");
		writer.append("" + cost + "f");
		writer.append(",");
		writer.append("" + damage + "f");
		writer.append(",");
		writer.append("" + health + "f");
		writer.append(",");
		writer.append("" + defense + "f");
		writer.append(",");
		writer.append("" + attackRate + "f");
		writer.append(",");
		writer.append("" + attackRange + "f");
		writer.append(",");
		writer.append("" + attacksGround);
		writer.append(",");
		writer.append("" + attacksAir);
		writer.append(",");
		writer.append("" + attacksAll);
		writer.append(",");
		writer.append("" + attacksInstantly);
		writer.append(",");
		writer.append("" + isUnlocked);
		writer.append("),\n");

	}
}
