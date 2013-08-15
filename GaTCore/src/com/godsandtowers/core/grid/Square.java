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
package com.godsandtowers.core.grid;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;

public class Square implements Externalizable {

	// T - Tree
	// R - Rocks
	// A - AttackRate Bonus
	// D - Damage Bonus
	// R - Range Bonus
	// S - Start
	// F - Finish
	// # - Path (as # from start to finish)

	private static final String _TREE = "_tree";
	private static final String _ROCK = "_rock";
	public static final String EMPTY = "";
	public static final String RANGE_BONUS = "range_bonus";
	public static final String DAMAGE_BONUS = "damage_bonus";
	public static final String ATTACK_RATE_BONUS = "attack_rate_bonus";
	public static final String FINISH = "finish";
	public static final String START = "start";

	public static final String[] SQUARE_MODELS;

	static {
		ArrayList<String> models = new ArrayList<String>();
		models.add(START);
		models.add(FINISH);
		models.add(RANGE_BONUS);
		models.add(DAMAGE_BONUS);
		models.add(ATTACK_RATE_BONUS);
		for (String baseTexture : Boards.TILESETS) {
			models.add(baseTexture + _ROCK);
			models.add(baseTexture + _TREE);
		}
		SQUARE_MODELS = models.toArray(new String[0]);
	}

	private int row;
	private int column;
	private boolean validForTowers;
	private boolean validForCreatures;
	private String model;

	public Square() {
	}

	Square(int row, int column) {
		this.row = row;
		this.column = column;
		setEmpty();
	}

	void setStart() {
		this.model = START;
		this.validForTowers = false;
		this.validForCreatures = true;
	}

	void setFinish() {
		this.model = FINISH;
		this.validForTowers = false;
		this.validForCreatures = true;
	}

	void setTree(String baseTexture) {
		this.model = baseTexture + _TREE;
		this.validForTowers = false;
		this.validForCreatures = false;
	}

	void setRock(String baseTexture) {
		this.model = baseTexture + _ROCK;
		this.validForTowers = false;
		this.validForCreatures = false;
	}

	void setAttackRateBonus() {
		this.model = ATTACK_RATE_BONUS;
		// this.validForTowers = true;
		// this.validForCreatures = true;
	}

	void setDamageBonus() {
		// this.model = DAMAGE_BONUS;
		// this.validForTowers = true;
		// this.validForCreatures = true;
	}

	void setRangeBonus() {
		// this.model = RANGE_BONUS;
		// this.validForTowers = true;
		// this.validForCreatures = true;
	}

	void setEmpty() {
		this.model = EMPTY;
		this.validForTowers = true;
		this.validForCreatures = true;
	}

	void load(String baseTexture, String line) {
		line.toUpperCase();
		char type = line.charAt(0);
		switch (type) {
		case 'S':
			setStart();
			return;
		case 'F':
			setFinish();
			return;
		case 'T':
			setTree(baseTexture);
			return;
		case 'R':
			setRock(baseTexture);
			return;
		case 'A':
			setAttackRateBonus();
			break;
		case 'D':
			setDamageBonus();
			break;
		case 'B':
			setRangeBonus();
			break;
		case 'E':
		default:
			setEmpty();
			break;
		}

	}

	int getRow() {
		return row;
	}

	int getColumn() {
		return column;
	}

	boolean isStart() {
		return model == START;
	}

	boolean isFinish() {
		return model == FINISH;
	}

	boolean isAttackRateBonus() {
		return model == ATTACK_RATE_BONUS;
	}

	boolean isDamageBonus() {
		return model == DAMAGE_BONUS;
	}

	boolean isRangeBonus() {
		return model == RANGE_BONUS;
	}

	boolean isValidForTowers() {
		return validForTowers;
	}

	boolean isValidForCreatures() {
		return validForCreatures;
	}

	String getModel() {
		return model;
	}

	void swap() {
		int tmp = row;
		row = column;
		column = tmp;
	}

	@Override
	public String toString() {
		return model;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(row);
		out.writeInt(column);
		out.writeBoolean(validForCreatures);
		out.writeBoolean(validForTowers);
		out.writeObject(model);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		row = in.readInt();
		column = in.readInt();
		validForCreatures = in.readBoolean();
		validForTowers = in.readBoolean();
		model = (String) in.readObject();
	}
}
