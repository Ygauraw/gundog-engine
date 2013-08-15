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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;

import com.godsandtowers.core.GameInfo;
import com.godsandtowers.core.PlayerInfo;
import com.godsandtowers.core.grid.Grid;
import com.godsandtowers.sprites.BuildingSphere;
import com.godsandtowers.sprites.Creature;
import com.godsandtowers.sprites.MovingProjectile;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.sprites.Tower;
import com.gundogstudios.gl.Actions;
import com.gundogstudios.gl.Sprite;

public class SnapshotProcessor {

	private static final int IDLE_ACTION_MASK = 0x0000000;
	private static final int MOVE_ACTION_MASK = 0x40000000;
	private static final int DEATH_ACTION_MASK = 0x80000000;
	private static final int ATTACK_ACTION_MASK = 0xC0000000;
	private static final int ACTION_MASK = 0xC0000000;
	private static final int TRANSPARENT_MASK = 0x80000000;

	private static final int SPRITE_BYTE_SIZE = 16;
	private float xScale;
	private float yScale;
	private float zScale;

	public SnapshotProcessor(int rows, int columns) {
		this.yScale = ((float) Short.MAX_VALUE) / (float) rows * 2f;
		this.xScale = ((float) Short.MAX_VALUE) / (float) columns * 2f;
		this.zScale = ((float) Short.MAX_VALUE) / 4f;
	}

	public Snapshot parseSnapshot(ByteBuffer buffer) {
		ArrayList<PlayerSnapshot> playerSnapshots = new ArrayList<PlayerSnapshot>();
		long timeStamp = buffer.getLong();
		int timeUntilNextWave = buffer.getInt();
		int currentWave = buffer.getInt();
		int numPlayers = buffer.getInt();

		while (--numPlayers >= 0) {
			int id = buffer.getInt();

			int creatureLevel = buffer.getInt();

			float income = buffer.getFloat();

			float gold = buffer.getFloat();

			float life = buffer.getFloat();

			float score = buffer.getFloat();

			float defensivePower = buffer.getFloat();

			float offensivePower = buffer.getFloat();

			int numSprites = buffer.getInt();

			ArrayList<SpriteSnapshot> sprites = new ArrayList<SpriteSnapshot>(numSprites);

			while (--numSprites >= 0) {
				SpriteSnapshot sprite = loadSprite(buffer);
				sprites.add(sprite);
			}

			PlayerSnapshot playerSnapshot = new PlayerSnapshot(id, creatureLevel, income, gold, life, score,
					defensivePower, offensivePower, sprites);
			playerSnapshots.add(playerSnapshot);
		}

		return new Snapshot(playerSnapshots, timeStamp, timeUntilNextWave, currentWave);
	}

	private SpriteSnapshot loadSprite(ByteBuffer buffer) {
		int id = buffer.getInt();
		int action = removeActionMask(id);
		id = id & ~ACTION_MASK;
		int targetID = buffer.getInt();

		boolean translucent = false;
		if ((targetID & TRANSPARENT_MASK) != 0) {
			translucent = true;
			targetID = targetID & ~TRANSPARENT_MASK;
		}

		String model = ModelNameConverter.getByteAsModelName(buffer.get());

		int level = buffer.get();

		float x = buffer.getShort() / xScale;

		float y = buffer.getShort() / yScale;

		float z = buffer.getShort() / zScale;

		return new SpriteSnapshot(id, level, action, model, x, y, z, targetID, translucent);
	}

	private int removeActionMask(int id) {
		id = id & ACTION_MASK;

		switch (id) {
		case IDLE_ACTION_MASK:
			return Actions.IDLE;
		case MOVE_ACTION_MASK:
			return Actions.MOVE;
		case DEATH_ACTION_MASK:
			return Actions.DEATH;
		case ATTACK_ACTION_MASK:
			return Actions.ATTACK;
		default:
			throw new RuntimeException("Unknown Action Mask in SnapshotProcessor: " + id);
		}
	}

	public void encodeSnapshot(ByteBuffer buffer, GameInfo gameInfo, long currentTime) {

		buffer.position(0);
		buffer.putInt(NetworkPackets.SNAPSHOT);
		int size = 4;

		buffer.putLong(currentTime);
		size += 8;

		buffer.putInt(gameInfo.getTimeUntilNextWave());
		size += 4;

		buffer.putInt(gameInfo.getCurrentWave());
		size += 4;

		buffer.putInt(gameInfo.getPlayers().length);
		size += 4;

		for (Player player : gameInfo.getPlayers()) {
			int id = player.getID();
			buffer.putInt(id);
			size += 4;

			buffer.putInt(player.getCreatureLevel());
			size += 4;

			buffer.putFloat(player.getIncome());
			size += 4;

			buffer.putFloat(player.getGold());
			size += 4;

			buffer.putFloat(player.getLife());
			size += 4;

			PlayerInfo playerInfo = gameInfo.getPlayerInfo(id);

			buffer.putFloat(playerInfo.getScore());
			size += 4;

			buffer.putFloat(playerInfo.getCurrentDefensivePower());
			size += 4;

			buffer.putFloat(playerInfo.getCurrentOffensivePower());
			size += 4;

			Grid grid = player.getGrid();
			Collection<Creature> creatures = grid.getCreatures();
			Collection<Tower> towers = grid.getTowers();
			Collection<MovingProjectile> projectiles = grid.getMovingProjectiles();
			Collection<Sprite> dyingSprites = grid.getDyingSprites();
			Collection<Tower> buildingTowers = grid.getBuildingTowers();
			Collection<BuildingSphere> buildingSpheres = grid.getBuildingSpheres();

			int spriteByteCount = creatures.size() + towers.size() + projectiles.size() + dyingSprites.size()
					+ buildingTowers.size() + buildingSpheres.size();
			buffer.putInt(spriteByteCount);
			size += 4;

			for (Creature creature : creatures) {
				addSprite(buffer, creature);
			}
			for (Tower tower : towers) {
				addSprite(buffer, tower);
			}
			for (MovingProjectile projectile : projectiles) {
				addSprite(buffer, projectile);
			}
			for (Sprite sprite : dyingSprites) {
				addSprite(buffer, sprite);
			}
			for (Tower buildingTower : buildingTowers) {
				addSprite(buffer, buildingTower);
			}
			for (BuildingSphere buildingSphere : buildingSpheres) {
				addTransparentSprite(buffer, buildingSphere);
			}

			spriteByteCount *= SPRITE_BYTE_SIZE;
			size += spriteByteCount;
		}
		buffer.position(0);
		buffer.limit(size);
	}

	private void addTransparentSprite(ByteBuffer buffer, Sprite sprite) {
		int id = sprite.getID() | getActionMask(sprite);
		buffer.putInt(id);

		Sprite target = sprite.getTarget();
		int targetID = (target == null) ? 0 : target.getID();
		targetID |= TRANSPARENT_MASK;
		buffer.putInt(targetID);

		byte model = ModelNameConverter.getModelNameAsByte(sprite.getModel());
		buffer.put(model);

		byte level = (byte) sprite.getLevel();
		buffer.put(level);

		short x = (short) (sprite.getX() * xScale);
		buffer.putShort(x);

		short y = (short) (sprite.getY() * yScale);
		buffer.putShort(y);

		short z = (short) (sprite.getZ() * zScale);
		buffer.putShort(z);
	}

	private void addSprite(ByteBuffer buffer, Sprite sprite) {
		int id = sprite.getID() | getActionMask(sprite);
		buffer.putInt(id);

		Sprite target = sprite.getTarget();
		int targetID = (target == null) ? 0 : target.getID();
		buffer.putInt(targetID);

		byte model = ModelNameConverter.getModelNameAsByte(sprite.getModel());
		buffer.put(model);

		byte level = (byte) sprite.getLevel();
		buffer.put(level);

		short x = (short) (sprite.getX() * xScale);
		buffer.putShort(x);

		short y = (short) (sprite.getY() * yScale);
		buffer.putShort(y);

		short z = (short) (sprite.getZ() * zScale);
		buffer.putShort(z);
	}

	private int getActionMask(Sprite sprite) {
		switch (sprite.getAction()) {
		case Actions.IDLE:
			return IDLE_ACTION_MASK;
		case Actions.MOVE:
			return MOVE_ACTION_MASK;
		case Actions.DEATH:
			return DEATH_ACTION_MASK;
		case Actions.ATTACK:
			return ATTACK_ACTION_MASK;
		default:
			throw new RuntimeException("Unknown Action for Sprite in SnapshotProcessor: " + sprite);
		}
	}

}
