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
package com.godsandtowers.graphics.menu.layouts;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES11;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.view.MotionEvent;

import com.godsandtowers.graphics.ModelFetcher;
import com.godsandtowers.messaging.ApplicationMessageProcessor;
import com.gundogstudios.gl.Actions;
import com.gundogstudios.gl.CombinedModel;
import com.gundogstudios.modules.AndroidGLUtils;
import com.gundogstudios.modules.Modules;

public class AlmanacModelView extends GLSurfaceView implements Renderer {

	private static final String TAG = "ModelView";
	private float xRot = -90f;
	private float zRot = -90f;
	private float xTrans = 0f;
	private float yTrans = -1f;
	private float zTrans = -3f;
	private int currentAction = Actions.IDLE;
	private CombinedModel model;
	private String name;
	private ModelFetcher modelFetcher;
	private long lastRender = 0;
	private Executor executor;
	private boolean loading = false;

	public AlmanacModelView(Context context) {
		super(context);
		executor = Executors.newSingleThreadExecutor();
		// Turn on error-checking and logging
		// this.setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
		// this.setDebugFlags(DEBUG_LOG_GL_CALLS);
		this.setRenderer(this);
		this.requestFocus();
		this.setFocusableInTouchMode(true);

		super.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}

	@Override
	public void onSurfaceCreated(GL10 UNUSED, EGLConfig config) {
		Modules.GLUTIL = new AndroidGLUtils(UNUSED);
		GLES11.glClearColor(0f, 0f, 0f, 1.0f);
		GLES11.glShadeModel(GLES11.GL_SMOOTH);
		GLES11.glClearDepthf(1.0f);
		GLES11.glEnable(GLES11.GL_DEPTH_TEST);
		GLES11.glDepthFunc(GLES11.GL_LESS);
		GLES11.glHint(GLES11.GL_PERSPECTIVE_CORRECTION_HINT, GLES11.GL_NEAREST);
		GLES11.glEnable(GLES11.GL_BLEND);
		GLES11.glBlendFunc(GLES11.GL_SRC_ALPHA, GLES11.GL_ONE_MINUS_SRC_ALPHA);
		GLES11.glEnable(GLES11.GL_ALPHA_TEST);
		GLES11.glAlphaFunc(GLES11.GL_GREATER, .01f);

		long start = System.currentTimeMillis();
		modelFetcher = new ModelFetcher();

		Modules.LOG.info(TAG, "Took: " + (System.currentTimeMillis() - start) + " ms to load");
	}

	public void release() {
		super.queueEvent(new Runnable() {

			@Override
			public void run() {
				if (modelFetcher != null) {
					try {
						Modules.LOG.info(TAG, "Releasing all VBOs and Textures");
						modelFetcher.release();
						Modules.LOG.info(TAG, "Released all VBOs and Textures");
					} catch (Exception e) {
						Modules.LOG.error(TAG, "Error releasing VBOs and Textures: " + e);
					}
					modelFetcher = null;
				}
			}
		});

	}

	public void setModel(String name) {
		this.name = name;
		this.model = null;
	}

	@Override
	public void onDrawFrame(GL10 UNUSED) {
		if (modelFetcher == null) {
			Modules.LOG.info("AlmanacModelView", "modelFetcher was released, unable to render");
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
			return;
		}
		long currentTime = System.currentTimeMillis();

		int timePassed = (int) (currentTime - lastRender);
		if (timePassed < 0 || timePassed > 10000) {
			timePassed = 20;
		}
		lastRender = currentTime;

		modelFetcher.loadVBOandTextures();

		GLES11.glClear(GLES11.GL_COLOR_BUFFER_BIT | GLES11.GL_DEPTH_BUFFER_BIT);

		if (name == null) {
			return;
		} else if (loading) {
			return;
		} else if (model == null) {
			loading = true;
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						model = modelFetcher.getModel(name);
						loading = false;
					} catch (OutOfMemoryError e) {
						Modules.MESSENGER.submit(ApplicationMessageProcessor.ID,
								ApplicationMessageProcessor.LOWER_GRAPHICS);
					}
				}
			});
			return;
		}

		GLES11.glMatrixMode(GLES11.GL_MODELVIEW);
		GLES11.glLoadIdentity();
		GLES11.glPushMatrix();
		GLES11.glTranslatef(xTrans, yTrans, zTrans);
		GLES11.glRotatef(xRot, 1, 0, 0);
		GLES11.glRotatef(zRot, 0, 0, 1);

		GLES11.glPushMatrix();
		if (model != null)
			model.draw(timePassed);
		GLES11.glPopMatrix();

		zRot = (zRot + 25f * (((float) timePassed) / 1000f)) % 360;
		Modules.PROFILER.updateRenderFPS();
		Thread.yield();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES11.glViewport(0, 0, width, height);
		GLES11.glMatrixMode(GLES11.GL_PROJECTION);
		GLES11.glLoadIdentity();
		float ratio = (float) width / (float) height;
		GLU.gluPerspective(gl, 45.0f, ratio, 0.01f, 10.0f);
		GLES11.glMatrixMode(GLES11.GL_MODELVIEW);
		GLES11.glLoadIdentity();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (model != null && event.getAction() == MotionEvent.ACTION_DOWN) {

			switch (currentAction) {
			case Actions.IDLE:
				model.setAction(Actions.MOVE);
				currentAction = Actions.MOVE;
				Modules.LOG.info(TAG, "Displaying Model: " + name + " MOVE");
				break;
			case Actions.MOVE:
				model.setAction(Actions.ATTACK);
				currentAction = Actions.ATTACK;
				Modules.LOG.info(TAG, "Displaying Model: " + name + " ATTACK");
				break;
			case Actions.ATTACK:
				model.setAction(Actions.DEATH);
				currentAction = Actions.DEATH;
				Modules.LOG.info(TAG, "Displaying Model: " + name + " DEATH");
				break;
			case Actions.DEATH:
				model.setAction(Actions.IDLE);
				currentAction = Actions.IDLE;
				Modules.LOG.info(TAG, "Displaying Model: " + name + " IDLE");
				break;
			default:
				Modules.LOG.error(TAG, "Unknown Action");
			}

		}

		return true;
	}

}
