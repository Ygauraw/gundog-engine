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
package com.godsandtowers.graphics;

import java.util.ArrayList;

import com.godsandtowers.core.GameInfo;
import com.godsandtowers.core.grid.Grid;
import com.godsandtowers.core.grid.GridSquare;
import com.godsandtowers.core.networking.SpriteSnapshot;
import com.godsandtowers.messaging.ApplicationMessageProcessor;
import com.godsandtowers.messaging.GLMessageProcessor;
import com.godsandtowers.messaging.LogicMessageProcessor;
import com.godsandtowers.sprites.Creature;
import com.godsandtowers.sprites.MovingProjectile;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.sprites.Tower;
import com.gundogstudios.gl.Actions;
import com.gundogstudios.gl.CombinedModel;
import com.gundogstudios.gl.Scene;
import com.gundogstudios.gl.Sprite;
import com.gundogstudios.gl.TouchPlane;
import com.gundogstudios.gl.custom.Border;
import com.gundogstudios.gl.custom.SkyBox;
import com.gundogstudios.gl.custom.Wall;
import com.gundogstudios.modules.GLES11Module;
import com.gundogstudios.modules.Modules;

public class GameSurface {
	private static final float TOUCH_SCALE = 0.05f;
	private static final float DEFAULT_XROT = -20f;
	private static final float DEFAULT_XTRANS = 0f;
	private static final float DEFAULT_YTRANS = 4f;
	private static final float DEFAULT_ZTRANS = -10f;

	private float xMaxTrans;
	private float yMaxTrans;
	private float xRot = DEFAULT_XROT;
	private float xTrans = DEFAULT_XTRANS;
	private float yTrans = DEFAULT_YTRANS;
	private float zTrans = DEFAULT_ZTRANS;
	private int pressedX = 0;
	private int pressedY = 0;
	private int height;

	private boolean released = false;

	private Scene[] boards;
	private int activeBoard;
	private GameInfo gameInfo;
	private long lastRender = 0;
	private TouchPlane touchPlane;
	private ModelFetcher modelFetcher;

	public GameSurface(GameInfo gameInfo) {
		this.gameInfo = gameInfo;
		float rowSize = gameInfo.getPlayers()[0].getGrid().getNumRows();
		float colSize = gameInfo.getPlayers()[0].getGrid().getNumColumns();
		xMaxTrans = colSize / 2f;
		yMaxTrans = rowSize;
	}

	public void release() {
		if (modelFetcher != null) {
			modelFetcher.release();
			modelFetcher = null;
		}
	}

	public void onSurfaceCreated() {

		GLES11Module gl = Modules.GL;
		gl.glClearColor(.5f, .5f, .5f, 1.0f);
		gl.glShadeModel(GLES11Module.GL_SMOOTH);
		gl.glEnable(GLES11Module.GL_DITHER);
		// Culling does not work with current 3D models due to transparency in the textures
		// gl.glEnable(GLES11Module.GL_CULL_FACE);
		// gl.glCullFace(GLES11Module.GL_CCW);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GLES11Module.GL_DEPTH_TEST);
		gl.glDepthFunc(GLES11Module.GL_LESS);
		gl.glHint(GLES11Module.GL_PERSPECTIVE_CORRECTION_HINT, GLES11Module.GL_FASTEST);
		gl.glEnable(GLES11Module.GL_BLEND);
		gl.glBlendFunc(GLES11Module.GL_SRC_ALPHA, GLES11Module.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GLES11Module.GL_ALPHA_TEST);
		gl.glAlphaFunc(GLES11Module.GL_GEQUAL, .01f);

		Player[] players = gameInfo.getPlayers();
		int rows = players[0].getGrid().getNumRows();
		int columns = players[0].getGrid().getNumColumns();
		boards = new Scene[players.length];
		touchPlane = new TouchPlane(rows, columns, 1f);

		this.modelFetcher = new ModelFetcher();
		String background = gameInfo.getBoard().getBackgroundTextureName();
		String wall = gameInfo.getBoard().getWallTextureName();
		try {
			modelFetcher.loadCustomModels(rows + 2, columns + 2, background, wall);
		} catch (OutOfMemoryError e) {
			Modules.MESSENGER.submit(ApplicationMessageProcessor.ID, ApplicationMessageProcessor.LOWER_GRAPHICS);
		}
		for (int i = 0; i < players.length; i++) {
			Grid grid = players[i].getGrid();
			boards[i] = new Scene();

			// TODO Take count here, and until addSprite is called for all of the models, dont call FINISHED_LOADING
			ArrayList<Sprite> sprites = new ArrayList<Sprite>();
			sprites.add(new SpriteSnapshot(0, 0, Actions.IDLE, background, 0, 0, 0, 0, false));
			sprites.add(new SpriteSnapshot(0, 0, Actions.IDLE, SkyBox.NAME, 0, 0, 0, 0, false));
			sprites.add(new SpriteSnapshot(0, 0, Actions.IDLE, Wall.NAME, 0, 0, 0, 0, false));
			sprites.add(new SpriteSnapshot(0, 0, Actions.IDLE, Border.NAME, 0, 0, 0, 0, false));
			Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.ADD_SPRITE_BATCH, i, sprites);
			sprites = new ArrayList<Sprite>();
			for (Creature creature : grid.getCreatures()) {
				sprites.add(creature);
			}
			Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.ADD_SPRITE_BATCH, i, sprites);

			sprites = new ArrayList<Sprite>();
			for (Tower tower : grid.getTowers()) {
				sprites.add(tower);
			}
			Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.ADD_SPRITE_BATCH, i, sprites);

			sprites = new ArrayList<Sprite>();
			for (MovingProjectile projectile : grid.getMovingProjectiles()) {
				sprites.add(projectile);
			}
			Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.ADD_SPRITE_BATCH, i, sprites);

			ArrayList<Sprite> transparentSprites = new ArrayList<Sprite>();
			sprites = new ArrayList<Sprite>();
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < columns; c++) {
					GridSquare square = grid.getGridSquare(r, c);
					if (square.isStartOrFinish())
						transparentSprites.add(square);
					if (square.hasModel())
						sprites.add(square);
				}
			}

			Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.ADD_SPRITE_BATCH, i, sprites);
			Modules.MESSENGER.submit(GLMessageProcessor.ID, GLMessageProcessor.ADD_TRANSPARENT_SPRITE_BATCH, i,
					transparentSprites);
		}

		activeBoard = gameInfo.getLocalPlayerID();
		Modules.MESSENGER.submit(LogicMessageProcessor.ID, LogicMessageProcessor.GL_FINISHED_LOADING);
	}

	public void onDrawFrame() {
		if (modelFetcher == null) {
			Modules.LOG.info("GameSurface", "modelFetcher was released, unable to render");
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
			return;
		}
		GLES11Module gl = Modules.GL;
		long currentTime = System.currentTimeMillis();
		int timePassed = (int) (currentTime - lastRender);
		if (timePassed < 0 || timePassed > 1000) {
			timePassed = 15;
		}
		lastRender = currentTime;
		modelFetcher.loadVBOandTextures();

		gl.glClear(GLES11Module.GL_COLOR_BUFFER_BIT | GLES11Module.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GLES11Module.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		gl.glRotatef(xRot, 1, 0, 0);
		gl.glTranslatef(xTrans, yTrans, zTrans);

		if (activeBoard != gameInfo.getLocalPlayerID()) {
			released = false;
		} else if (released) {
			int[] arr = touchPlane.touch(pressedX, height - pressedY);
			if (arr != null) {
				Modules.MESSENGER.submit(LogicMessageProcessor.ID, LogicMessageProcessor.GRID_TOUCHED,
						gameInfo.getLocalPlayerID(), arr[0], arr[1]);
			}
			released = false;
		}

		boards[activeBoard].draw(timePassed);

		Modules.PROFILER.updateRenderFPS();

		gl.glPopMatrix();

		Thread.yield();
	}

	public void onSurfaceChanged(int width, int height) {
		GLES11Module gl = Modules.GL;
		gl.glViewport(0, 0, width, height);

		gl.glMatrixMode(GLES11Module.GL_PROJECTION);
		gl.glLoadIdentity();

		this.height = height;

		Modules.GLUTIL.gluPerspective(45.0f, (float) width / (float) height, 0.01f, 100.0f);

		gl.glMatrixMode(GLES11Module.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	public void nextBoard() {
		if (++activeBoard > boards.length - 1)
			activeBoard = 0;
	}

	public void previousBoard() {
		if (--activeBoard < 0)
			activeBoard = boards.length - 1;
	}

	public void addSprite(int player, Sprite sprite, CombinedModel model) {
		if (model != null)
			boards[player].addSprite(sprite, model);
	}

	public void removeSprite(int player, Sprite sprite) {
		boards[player].removeSprite(sprite);
	}

	public void addTransparentSprite(int player, Sprite sprite, CombinedModel model) {
		if (model != null)
			boards[player].addTransparentSprite(sprite, model);
	}

	public void removeTransparentSprite(int player, Sprite sprite) {
		boards[player].removeTransparentSprite(sprite);
	}

	public void zoomIn() {
		if (zTrans < -1f)
			zTrans += 1f;
	}

	public void zoomOut() {
		if (zTrans > -15f)
			zTrans -= 1f;
	}

	public void rotateUp() {
		if (xRot > -75f)
			xRot -= 5f;
	}

	public void rotateDown() {
		if (xRot < 0f)
			xRot += 5f;
	}

	public void resetView() {
		xRot = DEFAULT_XROT;
		xTrans = DEFAULT_XTRANS;
		yTrans = DEFAULT_YTRANS;
		zTrans = DEFAULT_ZTRANS;
	}

	public void setPressed(int pressedX, int pressedY) {
		this.pressedX = pressedX;
		this.pressedY = pressedY;
	}

	public void setReleased(boolean released) {
		this.released = released;
	}

	public boolean getReleased() {
		return released;
	}

	public void updateTrans(float dx, float dy, float dz) {
		xTrans += dx * TOUCH_SCALE * zTrans / DEFAULT_ZTRANS;
		yTrans -= dy * TOUCH_SCALE * zTrans / DEFAULT_ZTRANS;

		if (xTrans > xMaxTrans)
			xTrans = xMaxTrans;
		else if (xTrans < -xMaxTrans)
			xTrans = -xMaxTrans;

		if (yTrans > yMaxTrans)
			yTrans = yMaxTrans;
		else if (yTrans < -yMaxTrans)
			yTrans = -yMaxTrans;
	}

	public ModelFetcher getModelFetcher() {
		return modelFetcher;
	}
}
