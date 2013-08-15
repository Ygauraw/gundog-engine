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
package com.godsandtowers.graphics.game;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.godsandtowers.core.GameInfo;
import com.godsandtowers.graphics.GameSurface;
import com.godsandtowers.messaging.GLHandler;
import com.godsandtowers.messaging.GLMessageProcessor;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.AndroidGLUtils;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.util.FastMath;

public class GameView extends GLSurfaceView {

	private GameRenderer renderer;
	private int pressedX = 0;
	private int pressedY = 0;
	private float touchedX = 0;
	private float touchedY = 0;
	private float max;

	public GameView(Context context, GameInfo gameInfo) {
		super(context);
		max = Modules.PREFERENCES.get(TDWPreferences.WIDTH, 10) / 25;
		renderer = new GameRenderer(gameInfo);
		// Turn on error-checking and logging THIS CAUSES game to crash when switching GL views
		// this.setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
		super.setRenderer(renderer);
		super.requestFocus();
		super.setFocusableInTouchMode(true);
		super.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

		Modules.MESSENGER.register(GLMessageProcessor.ID, new GLHandler(renderer));
	}

	public void release() {
		super.queueEvent(new Runnable() {

			@Override
			public void run() {
				try {
					Modules.LOG.info("GameView", "Releasing all VBOs and Textures");
					renderer.release();
					Modules.LOG.info("GameView", "Released all VBOs and Textures");
				} catch (Exception e) {
					Modules.LOG.error("GameView", "Error releasing VBOs and Textures: " + e);
				}
			}
		});
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			renderer.zoomIn();
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			renderer.zoomOut();
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			renderer.rotateUp();
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			renderer.rotateDown();
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		float x = event.getX();
		float y = event.getY();

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			pressedX = FastMath.round(x);
			pressedY = FastMath.round(y);
			renderer.setPressed(pressedX, pressedY);
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (Math.abs(pressedX - x) < max && Math.abs(pressedY - y) < max) {
				renderer.setReleased(true);
			}
		}

		if (!renderer.getReleased() && event.getAction() == MotionEvent.ACTION_MOVE) {
			float dx = x - touchedX;
			float dy = y - touchedY;
			float dz = 0;
			renderer.updateTrans(dx, dy, dz);
		}

		touchedX = x;
		touchedY = y;
		return true;
	}

	private class GameRenderer extends GameSurface implements Renderer {

		public GameRenderer(GameInfo gameInfo) {
			super(gameInfo);

		}

		@Override
		public void onDrawFrame(GL10 gl) {
			super.onDrawFrame();
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			super.onSurfaceChanged(width, height);
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			Modules.GLUTIL = new AndroidGLUtils(gl);
			super.onSurfaceCreated();
		}

	}
}
