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
package com.gundogstudios;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLES11;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.godsandtowers.graphics.ModelFetcher;
import com.godsandtowers.sprites.BaseCreature;
import com.godsandtowers.sprites.BaseSpecial;
import com.godsandtowers.util.TDWAndroidPreferences;
import com.gundogstudios.gl.Actions;
import com.gundogstudios.gl.CombinedModel;
import com.gundogstudios.modules.AndroidAssets;
import com.gundogstudios.modules.AndroidGLES11;
import com.gundogstudios.modules.AndroidGLUtils;
import com.gundogstudios.modules.AndroidLogger;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.modules.basic.BasicProfilerModule;

public class GundogStudiosModelViewerActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.modelviewer);

		Modules.LOG = new AndroidLogger();
		Modules.ASSETS = new AndroidAssets(this.getAssets());
		Modules.GL = new AndroidGLES11();
		Modules.PREFERENCES = new TDWAndroidPreferences(this);
		Modules.PROFILER = new BasicProfilerModule();

		RelativeLayout main = (RelativeLayout) this.findViewById(R.id.backgroundLayout);
		final ModelView glView = new ModelView(this);

		main.addView(glView, 0);

		Button button;
		final Activity activity = this;

		button = (Button) this.findViewById(R.id.previous);
		button.bringToFront();
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String model = glView.previousModel();
				Toast.makeText(activity, model, Toast.LENGTH_SHORT).show();
			}
		});
		button = (Button) this.findViewById(R.id.next);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String model = glView.nextModel();
				Toast.makeText(activity, model, Toast.LENGTH_SHORT).show();
			}
		});
		button = (Button) this.findViewById(R.id.up);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				glView.rotateUp();
			}
		});
		button = (Button) this.findViewById(R.id.down);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				glView.rotateDown();
			}
		});
		button = (Button) this.findViewById(R.id.left);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				glView.rotateLeft();
			}
		});
		button = (Button) this.findViewById(R.id.right);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				glView.rotateRight();
			}
		});
		button = (Button) this.findViewById(R.id.in);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				glView.zoomIn();
			}
		});
		button = (Button) this.findViewById(R.id.out);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				glView.zoomOut();
			}
		});

		final Button actionButton = (Button) this.findViewById(R.id.action);
		actionButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int action = glView.nextAction();
				actionButton.setText(Actions.toString(action));
			}
		});

		button = (Button) this.findViewById(R.id.render);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				glView.requestRender();
			}
		});
	}

	private class ModelView extends GLSurfaceView implements Renderer {
		private static final float TOUCH_SCALE = 0.005f;
		private float xRot = -90f;
		private float zRot = -90f;
		private float xTrans = 0f;
		private float yTrans = -.5f;
		private float zTrans = -3f;
		private float touchedX = 0;
		private float touchedY = 0;
		private ArrayList<CombinedModel> models;
		private ArrayList<String> modelNames;
		private int currentAction = Actions.IDLE;
		private int current = 0;
		private long lastRender = 0;
		private boolean finishedLoading = false;
		private ModelFetcher modelFetcher;

		public ModelView(Context context) {
			super(context);
			// Turn on error-checking and logging
			super.setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
			super.setDebugFlags(DEBUG_LOG_GL_CALLS);
			super.setRenderer(this);
			super.requestFocus();
			super.setFocusableInTouchMode(true);
			super.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		}

		@Override
		public void onSurfaceCreated(GL10 UNUSED, EGLConfig config) {
			Modules.GLUTIL = new AndroidGLUtils(UNUSED);
			GLES11.glClearColor(0f, 0f, 0f, 0.0f);
			GLES11.glShadeModel(GLES11.GL_SMOOTH);
			GLES11.glClearDepthf(1.0f);
			GLES11.glEnable(GLES11.GL_DEPTH_TEST);
			GLES11.glDepthFunc(GLES11.GL_LESS);
			GLES11.glHint(GLES11.GL_PERSPECTIVE_CORRECTION_HINT, GLES11.GL_NEAREST);
			GLES11.glEnable(GLES11.GL_BLEND);
			GLES11.glBlendFunc(GLES11.GL_SRC_ALPHA, GLES11.GL_ONE_MINUS_SRC_ALPHA);
			GLES11.glEnable(GLES11.GL_ALPHA_TEST);
			GLES11.glAlphaFunc(GLES11.GL_GREATER, .01f);

			modelFetcher = new ModelFetcher();
			modelFetcher.loadCustomModels(15, 7, ModelFetcher.FOREST_BACKGROUND, ModelFetcher.FOREST_WALL);

			new Thread(new Runnable() {

				@Override
				public void run() {
					loadModels();
					finishedLoading = true;
					Modules.LOG.info("ModelView", "Displaying Model: " + modelNames.get(current) + " IDLE");
				}
			}).start();

		}

		private void loadModels() {
			// modelNames = modelFetcher.getAllModelNames();
			// models = modelFetcher.getAllModels();
			modelNames = new ArrayList<String>();
			models = new ArrayList<CombinedModel>();
			addModel(BaseCreature.FROZEN_SOLDIER_HORSEMAN);
			addModel(BaseSpecial.DAMAGE_CREATURES);
			addModel(BaseSpecial.HEAL_CREATURES);
			addModel(BaseSpecial.KILL_CREATURES);
			addModel(BaseSpecial.SLOW_CREATURES);
			addModel(BaseSpecial.SPEEDUP_CREATURES);
			addModel(BaseSpecial.STUN_CREATURES);
		}

		private void addModel(String modelName, Object... objects) {
			models.add(modelFetcher.getModel(modelName, objects));
			modelNames.add(modelName);
		}

		@Override
		public void onDrawFrame(GL10 UNUSED) {
			long currentTime = System.currentTimeMillis();
			int timePassed = (int) (currentTime - lastRender);
			if (timePassed < 0 || timePassed > 10000) {
				timePassed = 20;
			}
			lastRender = currentTime;
			modelFetcher.loadVBOandTextures();

			if (!finishedLoading) {
				Thread.yield();
				return;
			}

			GLES11.glClear(GLES11.GL_COLOR_BUFFER_BIT | GLES11.GL_DEPTH_BUFFER_BIT);

			GLES11.glMatrixMode(GLES11.GL_MODELVIEW);
			GLES11.glLoadIdentity();
			GLES11.glPushMatrix();
			GLES11.glTranslatef(xTrans, yTrans, zTrans);
			GLES11.glRotatef(xRot, 1, 0, 0);
			GLES11.glRotatef(zRot, 0, 0, 1);

			GLES11.glPushMatrix();
			models.get(current).draw(timePassed);
			GLES11.glPopMatrix();

			Modules.PROFILER.updateRenderFPS();

		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			GLES11.glViewport(0, 0, width, height);
			GLES11.glMatrixMode(GLES11.GL_PROJECTION);
			GLES11.glLoadIdentity();
			float ratio = (float) width / (float) height;
			GLU.gluPerspective(gl, 45.0f, ratio, 0.01f, 100.0f);
			GLES11.glMatrixMode(GLES11.GL_MODELVIEW);
			GLES11.glLoadIdentity();
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX();
			float y = event.getY();
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				xTrans += (x - touchedX) * TOUCH_SCALE;
				yTrans -= (y - touchedY) * TOUCH_SCALE;
			}
			touchedX = x;
			touchedY = y;

			return true;
		}

		public void zoomIn() {
			zTrans += .5f;
		}

		public void zoomOut() {
			zTrans -= .5f;
		}

		public void rotateLeft() {
			zRot -= 5f;
		}

		public void rotateRight() {
			zRot += 5f;
		}

		public void rotateUp() {
			xRot -= 5f;
		}

		public void rotateDown() {
			xRot += 5f;
		}

		public String nextModel() {
			if (++current >= models.size())
				current = 0;
			currentAction = Actions.IDLE;
			return modelNames.get(current);
		}

		public String previousModel() {
			if (--current < 0)
				current = models.size() - 1;
			currentAction = Actions.IDLE;
			return modelNames.get(current);
		}

		public int nextAction() {
			switch (currentAction) {
			case Actions.IDLE:
				models.get(current).setAction(Actions.MOVE);
				currentAction = Actions.MOVE;
				break;
			case Actions.MOVE:
				models.get(current).setAction(Actions.ATTACK);
				currentAction = Actions.ATTACK;
				break;
			case Actions.ATTACK:
				models.get(current).setAction(Actions.DEATH);
				currentAction = Actions.DEATH;
				break;
			case Actions.DEATH:
				models.get(current).setAction(Actions.IDLE);
				currentAction = Actions.IDLE;
				break;
			default:
				Modules.LOG.error("ModelView", "Unknown Action");
			}
			return currentAction;
		}
	}
}