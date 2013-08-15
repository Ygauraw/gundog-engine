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
import java.util.Collection;
import java.util.HashSet;

import com.godsandtowers.sprites.Creature;
import com.godsandtowers.sprites.IDGenerator;
import com.godsandtowers.sprites.Tower;
import com.gundogstudios.gl.Actions;
import com.gundogstudios.gl.Sprite;

public class GridSquare implements Sprite, Comparable<GridSquare>, Externalizable {

	private HashSet<Creature> creatures;
	private int fliers;
	private int walkers;
	private Tower tower;

	private GridSquare nextSquare;
	private GridSquare nextAirSquare;

	private float x;
	private float y;
	private int row;
	private int column;

	private String model;
	private int cost;
	private boolean validForTowers;
	private boolean validForCreatures;
	private int id;

	public GridSquare() {
	}

	GridSquare(Square square) {
		row = square.getRow();
		column = square.getColumn();
		model = square.getModel();
		validForTowers = square.isValidForTowers();
		validForCreatures = square.isValidForCreatures();
		creatures = new HashSet<Creature>();
		tower = null;
		walkers = 0;
		fliers = 0;
		cost = Integer.MAX_VALUE;
		nextSquare = null;
		nextAirSquare = null;
		this.id = IDGenerator.getNextID();
	}

	boolean containsTower() {
		return tower != null;
	}

	boolean containsWalker() {
		return walkers > 0;
	}

	boolean containsFlier() {
		return fliers > 0;
	}

	boolean canPlaceCreature() {
		return validForCreatures && tower == null;
	}

	public boolean canPlaceTower() {
		return validForTowers && walkers <= 0 && tower == null;
	}

	public Collection<Creature> getCreatures() {
		return creatures;
	}

	public Tower getTower() {
		return tower;
	}

	void place(Tower tower) {
		this.tower = tower;
	}

	void removeTower() {
		this.tower = null;
	}

	void place(Creature creature) {
		boolean added = creatures.add(creature);
		if (added) {
			if (creature.isAir())
				fliers++;
			else
				walkers++;
		}
	}

	boolean remove(Creature creature) {
		boolean removed = creatures.remove(creature);
		if (removed) {
			if (creature.isAir())
				fliers--;
			else
				walkers--;
		}
		return removed;
	}

	int getCost() {
		return cost;
	}

	void setCost(int cost) {
		this.cost = cost;
	}

	boolean isValidForTowers() {
		return validForTowers;
	}

	boolean isValidForCreatures() {
		return validForCreatures;
	}

	void setX(float x) {
		this.x = x;
	}

	void setY(float y) {
		this.y = y;
	}

	int getRow() {
		return row;
	}

	int getColumn() {
		return column;
	}

	void setNextSquare(GridSquare nextSquare) {
		this.nextSquare = nextSquare;
	}

	public GridSquare getNext() {
		return nextSquare;
	}

	void setNextAirSquare(GridSquare nextAirSquare) {
		this.nextAirSquare = nextAirSquare;
	}

	public GridSquare getNextAir() {
		return nextAirSquare;
	}

	boolean hasNoNext() {
		return nextSquare == null;
	}

	public boolean hasModel() {
		return model != null && !model.equals("");
	}

	public boolean isStartOrFinish() {
		return model != null && (model.equals(Square.START) || model.equals(Square.FINISH));
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public float getZ() {
		return 0;
	}

	@Override
	public Sprite getTarget() {
		return null;
	}

	@Override
	public String getModel() {
		return model;
	}

	@Override
	public int getAction() {
		return Actions.IDLE;
	}

	@Override
	public int getLevel() {
		return 0;
	}

	@Override
	public String toString() {
		return "GridSquare [creatures=" + creatures + ", fliers=" + fliers + ", walkers=" + walkers + ", tower="
				+ tower + ", x=" + x + ", y=" + y + ", row=" + row + ", column=" + column + ", model=" + model
				+ ", cost=" + cost + ", validForTowers=" + validForTowers + ", validForCreatures=" + validForCreatures
				+ ", id=" + id + "]";
	}

	public String reducedString() {
		return "GridSquare [creatures=" + creatures.size() + ", fliers=" + fliers + ", walkers=" + walkers + ", tower="
				+ (tower != null) + ", x=" + x + ", y=" + y + ", row=" + row + ", column=" + column + ", model="
				+ model + ", cost=" + cost + ", validForTowers=" + validForTowers + ", validForCreatures="
				+ validForCreatures + ", id=" + id + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
		creatures = (HashSet<Creature>) input.readObject();
		tower = (Tower) input.readObject();
		model = (String) input.readObject();
		fliers = input.readInt();
		walkers = input.readInt();
		cost = input.readInt();
		row = input.readInt();
		column = input.readInt();
		x = input.readFloat();
		y = input.readFloat();
		validForTowers = input.readBoolean();
		validForCreatures = input.readBoolean();
	}

	@Override
	public void writeExternal(ObjectOutput output) throws IOException {
		output.writeObject(creatures);
		output.writeObject(tower);
		output.writeObject(model);
		output.writeInt(fliers);
		output.writeInt(walkers);
		output.writeInt(cost);
		output.writeInt(row);
		output.writeInt(column);
		output.writeFloat(x);
		output.writeFloat(y);
		output.writeBoolean(validForTowers);
		output.writeBoolean(validForCreatures);
	}

	@Override
	public int compareTo(GridSquare o) {
		int diff = cost - o.cost;
		if (diff != 0)
			return diff;
		diff = column - o.column;
		if (diff != 0)
			return diff;
		return row - o.row;
	}

}
