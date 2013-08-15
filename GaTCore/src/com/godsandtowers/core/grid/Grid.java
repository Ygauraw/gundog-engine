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

import gnu.trove.map.hash.TObjectIntHashMap;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import com.godsandtowers.sprites.BaseTower;
import com.godsandtowers.sprites.BuildingSphere;
import com.godsandtowers.sprites.Creature;
import com.godsandtowers.sprites.DelayedProjectile;
import com.godsandtowers.sprites.MovingProjectile;
import com.godsandtowers.sprites.Projectile;
import com.godsandtowers.sprites.Race;
import com.godsandtowers.sprites.Special;
import com.godsandtowers.sprites.Tower;
import com.gundogstudios.gl.Sprite;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.util.FastMath;

public class Grid implements Externalizable {
	private static final int TIME_TO_DIE = 1000;

	private static final String TAG = "Grid";
	private GridSquareComparator comparator;
	private GridSquare[][] grid;

	private HashSet<Creature> creatures;
	private HashSet<Tower> towers;
	private HashSet<Tower> buildingTowers;
	private HashMap<Tower, BuildingSphere> buildingSpheres;
	private HashSet<DelayedProjectile> delayedProjectiles;
	private HashSet<MovingProjectile> movingProjectiles;
	private HashSet<Special> specials;

	private TObjectIntHashMap<Sprite> dyingSprites;
	private ArrayList<GridSquare> startSquares;
	private ArrayList<GridSquare> allGridSquares;
	private HashSet<GridSquare> finishSquares;

	private int numRows;
	private int numColumns;

	private float xOffset;
	private float yOffset;

	public Grid() {
	}

	public Grid(Board board) {
		this.numRows = board.getRows();
		this.numColumns = board.getColumns();
		creatures = new HashSet<Creature>();
		towers = new HashSet<Tower>();
		buildingTowers = new HashSet<Tower>();
		buildingSpheres = new HashMap<Tower, BuildingSphere>();
		delayedProjectiles = new HashSet<DelayedProjectile>();
		movingProjectiles = new HashSet<MovingProjectile>();
		specials = new HashSet<Special>();

		grid = new GridSquare[numRows][numColumns];
		startSquares = new ArrayList<GridSquare>();
		finishSquares = new HashSet<GridSquare>();
		allGridSquares = new ArrayList<GridSquare>();

		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				Square square = board.getSquare(row, col);
				grid[row][col] = new GridSquare(square);
				if (square.isFinish()) {
					finishSquares.add(grid[row][col]);
				} else if (square.isStart()) {
					startSquares.add(grid[row][col]);
				}
				allGridSquares.add(grid[row][col]);
			}
		}
		init();
	}

	private void init() {
		dyingSprites = new TObjectIntHashMap<Sprite>();
		this.xOffset = ((float) numRows) / 2f;// * TILE_SIZE;
		this.yOffset = ((float) numColumns) / 2f;// * TILE_SIZE;
		comparator = new GridSquareComparator(numColumns);
		updateXY();
		updateAirPaths();
		updatePaths();
	}

	/**
	 * Used for debugging purposes. Checks to make sure there is consistency in the board's structures
	 */
	public boolean checkState() {
		boolean error = false;
		int creatureCount = 0;
		int towerCount = 0;
		HashSet<Creature> gridCreatures = new HashSet<Creature>();
		HashSet<Tower> gridTowers = new HashSet<Tower>();
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				creatureCount += grid[row][col].getCreatures().size();
				gridCreatures.addAll(grid[row][col].getCreatures());
				Tower tower = grid[row][col].getTower();
				if (tower != null) {
					towerCount++;
					gridTowers.add(tower);
				}
			}
		}
		if (creatureCount != gridCreatures.size()) {
			Modules.LOG.error(TAG, "Creature is in more than one gridsquare at once " + creatureCount + " "
					+ gridCreatures.size());
			error = true;
		}

		if (gridCreatures.size() != creatures.size()) {
			Modules.LOG.error(TAG,
					"Grid squares contain different amount of creatures than the board " + gridCreatures.size() + " "
							+ creatures.size());
			error = true;
		}

		gridCreatures.removeAll(creatures);
		if (gridCreatures.size() != 0) {
			for (Creature creature : gridCreatures) {
				Modules.LOG.error(TAG, "Extra creature " + creature);
			}
			error = true;
		}

		if (towerCount != gridTowers.size()) {
			Modules.LOG.error(TAG,
					"Tower is in more than one gridsquares at once " + towerCount + " " + gridTowers.size());
			error = true;
		}

		if (gridTowers.size() != towers.size()) {
			Modules.LOG.error(TAG,
					"Grid squares contain different amount of towers than the board " + gridTowers.size() + " "
							+ towers.size());
			error = true;
		}

		gridTowers.removeAll(towers);
		if (gridTowers.size() != 0) {
			for (Tower tower : gridTowers) {
				Modules.LOG.error(TAG, "Extra tower " + tower);
			}
			error = true;
		}
		boolean canPlaceTower = false;
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				if (grid[row][col].canPlaceTower())
					canPlaceTower = true;
			}
		}
		if (!canPlaceTower)
			Modules.LOG.error(TAG, "CANNOT PLACE ANYMORE TOWERS");

		if (error)
			printGridCosts();

		return error;

	}

	public GridSquare getGridSquare(int row, int column) {
		return grid[row][column];
	}

	public int convertToRow(float y) {
		return FastMath.floor(xOffset + y);
	}

	public int convertToColumn(float x) {
		return FastMath.floor(yOffset + x);
	}

	public float convertToY(int row) {
		return row + .5f - xOffset;
	}

	public float convertToX(int column) {
		return column + .5f - yOffset;
	}

	public void addSpecial(Special special) {
		dyingSprites.put(special, special.getTimeToLive());
	}

	public ArrayList<Sprite> updateDyingSprites(int timePassed) {
		ArrayList<Sprite> remove = new ArrayList<Sprite>();
		for (Sprite dying : dyingSprites.keySet()) {
			int timeLeft = dyingSprites.get(dying) - timePassed;
			if (timeLeft <= 0) {
				remove.add(dying);
				continue;
			}
			if (dying instanceof Creature) {
				float z = dying.getZ();
				if (z > 0f) {
					z -= .125f;
					z = (z > 0) ? z : 0;
				}
				((Creature) dying).setZ(z);
			}

			dyingSprites.put(dying, timeLeft);
		}
		for (Sprite dying : remove)
			dyingSprites.remove(dying);
		return remove;
	}

	public boolean removeTower(Tower tower) {
		int row = convertToRow(tower.getY());
		int column = convertToColumn(tower.getX());

		GridSquare square = grid[row][column];

		if (square.containsTower()) {
			square.removeTower();
			towers.remove(tower);
			updatePaths();
			return true;
		}
		return false;
	}

	private void applySquareBonus(GridSquare square, Tower tower) {
		BaseTower baseTower = tower.getBaseTower();

		if (square.getModel() == Square.ATTACK_RATE_BONUS) {
			baseTower = new BaseTower(baseTower);
			baseTower.setAttackRate(baseTower.getAttackRate() * .75f);
		} else if (square.getModel() == Square.DAMAGE_BONUS) {
			baseTower = new BaseTower(baseTower);
			baseTower.setDamage(baseTower.getDamage() * 2f);
		} else if (square.getModel() == Square.RANGE_BONUS) {
			baseTower = new BaseTower(baseTower);
			baseTower.setAttackRange(baseTower.getAttackRange() + 1f);
		}

		tower.replace(baseTower);
	}

	public Tower replaceTower(Tower oldTower, BaseTower baseTower) {
		int row = convertToRow(oldTower.getY());
		int column = convertToColumn(oldTower.getX());

		Tower newTower = new Tower(baseTower, oldTower.getRace().getBaseRace(), oldTower.getX(), oldTower.getY());
		GridSquare square = grid[row][column];

		if (square.containsTower()) {
			square.removeTower();
			towers.remove(oldTower);

			applySquareBonus(square, newTower);

			square.place(newTower);
			towers.add(newTower);
			return newTower;
		} else {
			return null;
		}
	}

	public BuildingSphere addBuildingTower(Tower tower) {
		int row = convertToRow(tower.getY());
		int column = convertToColumn(tower.getX());
		if (invalid(row, column)) {
			return null;
		}

		GridSquare square = grid[row][column];

		if (!square.isValidForTowers() || square.containsTower()) {
			return null;
		}

		applySquareBonus(square, tower);
		buildingTowers.add(tower);

		BuildingSphere sphere = new BuildingSphere(tower);
		buildingSpheres.put(tower, sphere);

		return sphere;
	}

	public Tower removeBuildingTower(int row, int column) {
		float x = convertToX(column);
		float y = convertToY(row);
		Tower found = null;
		for (Tower other : buildingTowers) {
			if (other.getX() == x && other.getY() == y) {
				found = other;
				break;
			}
		}
		if (found != null) {
			buildingTowers.remove(found);
			return found;
		}

		return null;
	}

	public BuildingSphere removeBuildingSphere(Tower tower) {
		return buildingSpheres.remove(tower);
	}

	public BuildingSphere addBuildingSphere(Tower tower) {
		BuildingSphere buildingSphere = new BuildingSphere(tower);
		buildingSpheres.put(tower, buildingSphere);
		return buildingSphere;
	}

	public void cancelBuild() {
		buildingTowers.clear();
		buildingSpheres.clear();
	}

	public ArrayList<Tower> buildAll() {
		ArrayList<Tower> failed = new ArrayList<Tower>();
		for (Tower tower : buildingTowers) {
			int row = convertToRow(tower.getY());
			int column = convertToColumn(tower.getX());
			GridSquare square = grid[row][column];

			if (!square.canPlaceTower()) {
				failed.add(tower);
				continue;
			}

			square.place(tower);
			updatePaths();

			if (!checkSquares()) {
				square.removeTower();
				updatePaths();
				failed.add(tower);
				continue;
			} else {
				towers.add(tower);
			}
		}
		buildingTowers.clear();
		buildingSpheres.clear();
		return failed;
	}

	public boolean addTower(Tower tower) {
		int row = convertToRow(tower.getY());
		int column = convertToColumn(tower.getX());
		if (invalid(row, column)) {
			return false;
		}
		GridSquare square = grid[row][column];

		if (!square.canPlaceTower()) {
			return false;
		}

		applySquareBonus(square, tower);

		square.place(tower);
		updatePaths();

		if (!checkSquares()) {
			square.removeTower();
			updatePaths();
			return false;
		} else {
			towers.add(tower);
		}
		return true;
	}

	public void addCreature(Creature creature) {
		int loc = FastMath.floor(startSquares.size() * FastMath.random());
		GridSquare square = startSquares.get(loc);

		creature.setX(square.getX());
		creature.setY(square.getY());
		if (creature.isAir())
			creature.setZ(3f);
		else
			creature.setZ(0);

		square.place(creature);

		creature.setGridSquare(square);

		creatures.add(creature);
	}

	private void resetCosts() {
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				grid[row][col].setCost(Integer.MAX_VALUE);
			}
		}

		for (GridSquare finish : finishSquares)
			finish.setCost(0);
	}

	private void updateXY() {
		for (int row = 0; row < numRows; row++) {
			for (int column = 0; column < numColumns; column++) {
				GridSquare square = grid[row][column];
				float x = convertToX(square.getColumn());
				square.setX(x);
				float y = convertToY(square.getRow());
				square.setY(y);
			}
		}
	}

	public boolean checkSquares() {
		for (GridSquare start : startSquares) {
			if (start.getCost() == Integer.MAX_VALUE) {
				return false;
			}
		}

		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				if (grid[row][col].containsWalker() && grid[row][col].getCost() == Integer.MAX_VALUE) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Tries to build on the squares with the "lowest movement cost" to the finish. Skips squares that cause an invalid
	 * path
	 */
	public ArrayList<GridSquare> calculateBuildSquares() {
		ArrayList<GridSquare> buildOrder = new ArrayList<GridSquare>();
		TreeSet<GridSquare> squares = new TreeSet<GridSquare>();
		ArrayList<GridSquare> invalid = new ArrayList<GridSquare>();
		Tower fakeTower = new Tower();

		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				if (grid[row][col].canPlaceTower())
					squares.add(grid[row][col]);
			}
		}

		boolean keepGoing;
		do {
			keepGoing = false;

			// Find the next valid location to place a tower
			for (GridSquare gridSquare : squares) {
				if (!canBuildTower(gridSquare, fakeTower)) {
					invalid.add(gridSquare);
					continue;
				}

				keepGoing = true;
				buildOrder.add(gridSquare);
				invalid.add(gridSquare);
				break;
			}

			// Remove all squares that cannot be used, and sort the set based on new costs
			TreeSet<GridSquare> newSquares = new TreeSet<GridSquare>();
			for (GridSquare gridSquare : squares) {
				newSquares.add(gridSquare);
			}
			newSquares.removeAll(invalid);
			invalid.clear();
			squares = newSquares;

			// printGridCosts();
		} while (keepGoing);

		// Put the grid back in its original state
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				grid[row][col].removeTower();
			}
		}
		updatePaths();

		// for (GridSquare gridSquare : buildOrder)
		// System.out.println(gridSquare.getCost() + " " + gridSquare.getRow() + " " + gridSquare.getColumn());

		return buildOrder;
	}

	private boolean canBuildTower(GridSquare square, Tower tower) {
		if (!square.canPlaceTower()) {
			return false;
		}

		square.place(tower);
		updatePaths();

		if (!checkSquares()) {
			square.removeTower();
			updatePaths();
			return false;
		}
		return true;
	}

	private void printGridCosts() {
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numColumns; col++) {
				System.out.print(grid[row][col].getCost() + ",");
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * Uses backwards propagation to determine the best route from and square to the finish
	 */
	private void updatePaths() {

		TreeSet<GridSquare> unprocessed = new TreeSet<GridSquare>(comparator);

		resetCosts();

		for (GridSquare finish : finishSquares)
			unprocessed.add(finish);

		while (!unprocessed.isEmpty()) {
			GridSquare current = unprocessed.first();
			unprocessed.remove(current);

			boolean UP = tryMove(current.getRow() - 1, current.getColumn(), 2, unprocessed, current);
			boolean DOWN = tryMove(current.getRow() + 1, current.getColumn(), 2, unprocessed, current);
			boolean LEFT = tryMove(current.getRow(), current.getColumn() - 1, 2, unprocessed, current);
			boolean RIGHT = tryMove(current.getRow(), current.getColumn() + 1, 2, unprocessed, current);

			if (UP && RIGHT)
				tryMove(current.getRow() - 1, current.getColumn() + 1, 3, unprocessed, current);
			if (UP && LEFT)
				tryMove(current.getRow() - 1, current.getColumn() - 1, 3, unprocessed, current);
			if (DOWN && RIGHT)
				tryMove(current.getRow() + 1, current.getColumn() + 1, 3, unprocessed, current);
			if (DOWN && LEFT)
				tryMove(current.getRow() + 1, current.getColumn() - 1, 3, unprocessed, current);

		}
		// For the case where a creature reaches the finish while Board is drawing and thinks it hasn't made it yet
		// not sure if this is needed anymore
		for (GridSquare finish : finishSquares)
			finish.setNextSquare(finish);
	}

	public boolean invalid(int row, int column) {
		return (row >= numRows || column >= numColumns || row < 0 || column < 0);
	}

	private boolean tryMove(int row, int col, int cost, TreeSet<GridSquare> unprocessed, GridSquare current) {

		if (invalid(row, col))
			return false;

		GridSquare previous = grid[row][col];

		if (!previous.canPlaceCreature())
			return false;

		int newCost = current.getCost() + cost;
		if (newCost < previous.getCost()) {
			unprocessed.remove(previous);
			previous.setCost(newCost);
			previous.setNextSquare(current);
			unprocessed.add(previous);
		}
		return true;
	}

	private void updateAirPaths() {

		TreeSet<GridSquare> unprocessed = new TreeSet<GridSquare>(comparator);

		resetCosts();

		for (GridSquare finish : finishSquares)
			unprocessed.add(finish);

		while (!unprocessed.isEmpty()) {
			GridSquare current = unprocessed.first();
			unprocessed.remove(current);

			boolean UP = tryAirMove(current.getRow() - 1, current.getColumn(), 2, unprocessed, current);
			boolean DOWN = tryAirMove(current.getRow() + 1, current.getColumn(), 2, unprocessed, current);
			boolean LEFT = tryAirMove(current.getRow(), current.getColumn() - 1, 2, unprocessed, current);
			boolean RIGHT = tryAirMove(current.getRow(), current.getColumn() + 1, 2, unprocessed, current);

			if (UP && RIGHT)
				tryAirMove(current.getRow() - 1, current.getColumn() + 1, 3, unprocessed, current);
			if (UP && LEFT)
				tryAirMove(current.getRow() - 1, current.getColumn() - 1, 3, unprocessed, current);
			if (DOWN && RIGHT)
				tryAirMove(current.getRow() + 1, current.getColumn() + 1, 3, unprocessed, current);
			if (DOWN && LEFT)
				tryAirMove(current.getRow() + 1, current.getColumn() - 1, 3, unprocessed, current);

		}
		for (GridSquare finish : finishSquares)
			finish.setNextAirSquare(finish);
	}

	private boolean tryAirMove(int row, int col, int cost, TreeSet<GridSquare> unprocessed, GridSquare current) {

		if (invalid(row, col))
			return false;

		GridSquare previous = grid[row][col];

		int newCost = current.getCost() + cost;
		if (newCost < previous.getCost()) {
			unprocessed.remove(previous);
			previous.setCost(newCost);
			previous.setNextAirSquare(current);
			unprocessed.add(previous);
		}
		return true;
	}

	public ArrayList<Creature> moveProjectiles(int timePassed, ArrayList<MovingProjectile> uselessProjectiles) {
		ArrayList<Creature> creaturesKilled = new ArrayList<Creature>();

		moveMovingProjectiles(creaturesKilled, timePassed, uselessProjectiles);
		moveDelayedProjectiles(creaturesKilled, timePassed);

		return creaturesKilled;
	}

	private void moveMovingProjectiles(ArrayList<Creature> creaturesKilled, int timePassed,
			ArrayList<MovingProjectile> uselessProjectiles) {
		ArrayList<MovingProjectile> hit = new ArrayList<MovingProjectile>();
		for (MovingProjectile projectile : movingProjectiles) {
			if (!projectile.getTarget().isAlive()) {
				hit.add(projectile);
				uselessProjectiles.add(projectile);
				continue;
			}
			boolean hitTarget = projectile.move(timePassed);
			if (hitTarget) {
				processHit(projectile, creaturesKilled);
				hit.add(projectile);
				dyingSprites.put(projectile, TIME_TO_DIE);
			}
		}
		movingProjectiles.removeAll(hit);
		removeKilledCreatures(creaturesKilled);
	}

	private void moveDelayedProjectiles(ArrayList<Creature> creaturesKilled, int timePassed) {
		ArrayList<DelayedProjectile> hit = new ArrayList<DelayedProjectile>();
		for (DelayedProjectile projectile : delayedProjectiles) {
			if (!projectile.getTarget().isAlive()) {
				hit.add(projectile);
				continue;
			}
			boolean hitTarget = projectile.move(timePassed);
			if (hitTarget) {
				processHit(projectile, creaturesKilled);
				hit.add(projectile);
			}
		}
		delayedProjectiles.removeAll(hit);
		removeKilledCreatures(creaturesKilled);
	}

	private void removeKilledCreatures(ArrayList<Creature> creaturesKilled) {
		for (Creature target : creaturesKilled) {
			removeKilledCreature(target);
		}
	}

	public void removeKilledCreature(Creature creature) {
		creature.getCurrentGridSquare().remove(creature);
		creatures.remove(creature);
		dyingSprites.put(creature, TIME_TO_DIE);
	}

	private void processHit(Projectile projectile, ArrayList<Creature> creaturesKilled) {

		Creature target = projectile.getTarget();
		float row = target.getX();
		float col = target.getY();
		Race race = projectile.getRace();

		if (race.isFire() && !projectile.attacksAll()) {
			float radius = race.getSplashRadius();
			float splashDamage = projectile.getDamage() * race.getSplashStrengthFade();
			splashNeighbors(creaturesKilled, row, col, splashDamage, radius, target.isAir());
		}

		if (target.getRace().getCreatureEvadePercentage() > FastMath.random()) {
			// Creature dodged the attack
		} else if (target.subtractHealth(projectile.getDamage()) && !attemptResurrect(creaturesKilled, target)) {
			// Creature was killed and was not resurrected
		} else {
			target.setSlow(race.getSlowDuration(), race.getSlowFactor());

			target.setStun(race.getStunDuration());

			target.setDrain(race.getDrainDuration(), race.getDrainPercentage());

			if (race.getKillPercentage() > FastMath.random()) {
				target.kill();
				attemptResurrect(creaturesKilled, target);
			}
		}
	}

	private boolean attemptResurrect(ArrayList<Creature> creaturesKilled, Creature dead) {
		if (dead.getRace().getResurrectPercentage() > FastMath.random()) {
			dead.resurrect();
			return true;
		} else {
			creaturesKilled.add(dead);
			return false;
		}
	}

	private void splashNeighbors(ArrayList<Creature> creaturesKilled, float startX, float startY, float damage,
			float radius, boolean air) {
		int range = FastMath.ceil(radius);
		int towerRow = convertToRow(startY);
		int towerColumn = convertToColumn(startX);

		for (int i = -range; i < range; i++) {
			for (int j = -range; j < range; j++) {

				int row = towerRow + j;
				int column = towerColumn + i;

				if (invalid(row, column)) {
					continue;
				}

				GridSquare square = grid[row][column];
				for (Creature creature : square.getCreatures()) {
					if (creature.isAir() && !air)
						continue;
					if (!creature.isAir() && air)
						continue;

					float x = creature.getX();
					float y = creature.getY();
					float targetDistance = FastMath.sqrt(x * x + y * y);

					if (targetDistance > radius)
						continue;

					if (creature.subtractHealth(damage)) {
						attemptResurrect(creaturesKilled, creature);
					}
				}
			}
		}
	}

	public ArrayList<Creature> moveCreatures(int timePassed) {

		ArrayList<Creature> remove = new ArrayList<Creature>();
		for (Creature creature : creatures) {
			creature.nextTick(timePassed);

			if (creature.isAttacking())
				continue;

			GridSquare square = creature.getCurrentGridSquare();

			float movePercentLeft = creature.move(timePassed);

			while (movePercentLeft > 0) {

				square.remove(creature);

				if (finishSquares.contains(square)) {
					remove.add(creature);
					creature.remove();
					break;
				}

				if (creature.isAir()) {
					square = square.getNextAir();
				} else {
					square = square.getNext();
				}

				square.place(creature);
				creature.setGridSquare(square);

				movePercentLeft = creature.moveDistance(movePercentLeft);
			}

		}
		creatures.removeAll(remove);
		return remove;
	}

	public ArrayList<Tower> attackTowers(int timePassed) {
		ArrayList<Tower> destroyed = new ArrayList<Tower>();
		for (Creature creature : creatures) {
			if (creature.getAttackingTarget() != null && !creature.getAttackingTarget().isAlive()) {
				creature.setTarget(null);
			}
			if (creature.canAttack(timePassed)) {
				Tower tower = attackTower(creature);
				if (tower != null) {
					destroyed.add(tower);
				}
			}
		}
		return destroyed;
	}

	private Tower attackTower(Creature creature) {
		Tower target = findTarget(creature);
		if (target != null) {
			creature.setTarget(target);
			creature.attacked();
			if (target.subtractHealth(creature.getDamage())) {
				removeTower(target);
				dyingSprites.put(target, TIME_TO_DIE);
				return target;
			}
		} else {
			creature.setTarget(null);
		}

		return null;
	}

	private Tower findTarget(Creature creature) {
		Tower target = creature.getAttackingTarget();
		if (target != null) {
			if (!target.isAlive()) {
				creature.setTarget(null);
				target = null;
			} else {
				float y = target.getY();
				float x = target.getX();
				float targetDistance = FastMath.sqrt(y * y + x * x);

				if (targetDistance > creature.getAttackRange()) {
					target = null;
					creature.setTarget(null);
				} else {
					return target;
				}
			}
		}

		int range = FastMath.ceil(creature.getAttackRange());
		int creatureRow = convertToRow(creature.getY());
		int creatureColumn = convertToColumn(creature.getX());

		for (int i = -range; i < range; i++) {
			for (int j = -range; j < range; j++) {

				int row = creatureRow + j;
				int column = creatureColumn + i;

				if (invalid(row, column)) {
					continue;
				}

				GridSquare square = grid[row][column];
				Tower tower = square.getTower();
				if (tower != null) {
					float x = tower.getX() - creature.getX();
					float y = tower.getY() - creature.getY();
					float targetDistance = FastMath.sqrt(x * x + y * y);

					if (targetDistance > tower.getAttackRange())
						continue;
					return tower;
				}

			}
		}

		return null;
	}

	public ArrayList<Creature> attackCreatures(int timePassed, ArrayList<Projectile> newProjectiles) {
		ArrayList<Creature> creaturesKilled = new ArrayList<Creature>();
		for (Tower tower : towers) {
			if (tower.getTarget() != null && !tower.getTarget().isAlive()) {
				tower.setTarget(null);
			}
			if (tower.canFire(timePassed)) {
				if (tower.attacksAllInRange()) {
					attackAllCreatures(tower, creaturesKilled, newProjectiles);
				} else {
					attackOneCreature(tower, creaturesKilled, newProjectiles);
				}
			}
		}

		removeKilledCreatures(creaturesKilled);

		return creaturesKilled;
	}

	private void attackAllCreatures(Tower tower, ArrayList<Creature> creaturesKilled,
			ArrayList<Projectile> newProjectiles) {
		int range = FastMath.ceil(tower.getAttackRange());
		int towerRow = convertToRow(tower.getY());
		int towerColumn = convertToColumn(tower.getX());

		for (int i = -range; i < range; i++) {
			for (int j = -range; j < range; j++) {

				int row = towerRow + j;
				int column = towerColumn + i;

				if (invalid(row, column)) {
					continue;
				}

				GridSquare square = grid[row][column];
				for (Creature creature : square.getCreatures()) {
					if (creature.isAir() && tower.attacksOnlyGround())
						continue;
					if (!creature.isAir() && tower.attacksOnlyAir())
						continue;

					float x = tower.getX() - creature.getX();
					float y = tower.getY() - creature.getY();
					float targetDistance = FastMath.sqrt(x * x + y * y);

					if (targetDistance > tower.getAttackRange())
						continue;

					if (tower.instantAttack()) {
						DelayedProjectile projectile = new DelayedProjectile(tower, creature);
						delayedProjectiles.add(projectile);
					} else {
						MovingProjectile projectile = new MovingProjectile(tower, creature);
						movingProjectiles.add(projectile);
						newProjectiles.add(projectile);
					}
					tower.shot();
					tower.setTarget(creature);
				}

			}
		}
	}

	private void attackOneCreature(Tower tower, ArrayList<Creature> creaturesKilled,
			ArrayList<Projectile> newProjectiles) {

		Creature target = findTarget(tower);

		if (target != null) {
			tower.setTarget(target);
			if (tower.instantAttack()) {
				DelayedProjectile projectile = new DelayedProjectile(tower, target);
				delayedProjectiles.add(projectile);

			} else {
				MovingProjectile projectile = new MovingProjectile(tower, target);
				movingProjectiles.add(projectile);
				newProjectiles.add(projectile);
			}
			tower.shot();
		}
	}

	private Creature findTarget(Tower tower) {
		Creature target = tower.getTarget();
		if (target != null) {
			if (!target.isAlive()) {
				tower.setTarget(null);
				target = null;
			} else {
				float x = tower.getX() - target.getX();
				float y = tower.getY() - target.getY();
				float targetDistance = FastMath.sqrt(x * x + y * y);

				if (targetDistance > tower.getAttackRange()) {
					tower.setTarget(null);
					target = null;
				} else {
					return target;
				}

			}
		}

		int range = FastMath.ceil(tower.getAttackRange());
		int towerRow = convertToRow(tower.getY());
		int towerColumn = convertToColumn(tower.getX());

		for (int i = -range; i < range; i++) {
			for (int j = -range; j < range; j++) {

				int row = towerRow + j;
				int column = towerColumn + i;

				if (invalid(row, column)) {
					continue;
				}

				GridSquare square = grid[row][column];
				for (Creature creature : square.getCreatures()) {
					if (creature.isAir() && tower.attacksOnlyGround())
						continue;
					if (!creature.isAir() && tower.attacksOnlyAir())
						continue;

					float x = tower.getX() - creature.getX();
					float y = tower.getY() - creature.getY();
					float targetDistance = FastMath.sqrt(x * x + y * y);

					if (targetDistance > tower.getAttackRange())
						continue;
					return creature;
				}
			}
		}
		return null;
	}

	public Collection<Creature> getCreatures() {
		return creatures;
	}

	public Collection<Tower> getTowers() {
		return towers;
	}

	public Collection<MovingProjectile> getMovingProjectiles() {
		return movingProjectiles;
	}

	public Collection<GridSquare> getAllGridSquares() {
		return allGridSquares;
	}

	public Collection<Tower> getBuildingTowers() {
		return buildingTowers;
	}

	public Collection<BuildingSphere> getBuildingSpheres() {
		return buildingSpheres.values();
	}

	public Collection<Sprite> getDyingSprites() {
		return dyingSprites.keySet();
	}

	public Collection<Special> getSpecials() {
		return specials;
	}

	public int getNumColumns() {
		return numColumns;
	}

	public int getNumRows() {
		return numRows;
	}

	public boolean containsTower(int row, int col) {
		return grid[row][col].containsTower();
	}

	public Tower getTower(int row, int col) {
		return grid[row][col].getTower();
	}

	public Tower getTower(float x, float y) {
		return grid[convertToRow(y)][convertToColumn(x)].getTower();
	}

	private static class GridSquareComparator implements Comparator<GridSquare> {

		private int columns;

		public GridSquareComparator(int numColumns) {
			this.columns = numColumns;
		}

		@Override
		public int compare(GridSquare o1, GridSquare o2) {
			if (o1 == o2)
				return 0;
			int diff = o1.getCost() - o2.getCost();
			if (diff == 0)
				return (o1.getRow() * columns + o1.getColumn()) - (o2.getRow() * columns + o2.getColumn());
			else
				return diff;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
		numRows = input.readInt();
		numColumns = input.readInt();

		grid = (GridSquare[][]) input.readObject();

		startSquares = (ArrayList<GridSquare>) input.readObject();
		finishSquares = (HashSet<GridSquare>) input.readObject();
		allGridSquares = (ArrayList<GridSquare>) input.readObject();
		creatures = (HashSet<Creature>) input.readObject();
		towers = (HashSet<Tower>) input.readObject();
		buildingTowers = (HashSet<Tower>) input.readObject();
		buildingSpheres = (HashMap<Tower, BuildingSphere>) input.readObject();
		movingProjectiles = (HashSet<MovingProjectile>) input.readObject();
		delayedProjectiles = (HashSet<DelayedProjectile>) input.readObject();
		specials = (HashSet<Special>) input.readObject();
		init();
	}

	@Override
	public void writeExternal(ObjectOutput output) throws IOException {
		output.writeInt(numRows);
		output.writeInt(numColumns);

		output.writeObject(grid);

		output.writeObject(startSquares);
		output.writeObject(finishSquares);
		output.writeObject(allGridSquares);
		output.writeObject(creatures);
		output.writeObject(towers);
		output.writeObject(buildingTowers);
		output.writeObject(buildingSpheres);
		output.writeObject(movingProjectiles);
		output.writeObject(delayedProjectiles);
		output.writeObject(specials);

	}

}