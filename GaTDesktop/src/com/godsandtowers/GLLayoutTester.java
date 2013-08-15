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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class GLLayoutTester {

	private static final int WIDTH = 720;
	private static final int HEIGHT = 480;
	private static final float RATIO = (float) WIDTH / (float) HEIGHT;

	private static float[] quadCoords = new float[] { -RATIO, -1 + .1f, 0, -RATIO, -1, 0, RATIO, -1 + .1f, 0, RATIO,
			-1, 0 };

	private static FloatBuffer quadBuffer;
	static {
		quadBuffer = ByteBuffer.allocateDirect(quadCoords.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		quadBuffer.put(quadCoords);
		quadBuffer.position(0);
	}

	public static void main(String[] args) throws LWJGLException {
		Display.setLocation((Display.getDisplayMode().getWidth() - WIDTH) / 2,
				(Display.getDisplayMode().getHeight() - HEIGHT) / 2);
		Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
		Display.setTitle("Tower Defense Wars");
		Display.create();
		// Display.setDisplayModeAndFullscreen(mode)
		initGL();
		initViewport();
		while (!Display.isCloseRequested()) {

			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
				break;

			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			// GL11.glRotatef(0, 1, 0, 0);
			// GL11.glTranslatef(0, 0, 0);

			// draw triangle
			// GL11.glColor3f(1f, 0f, 0f);
			// GL11.glTranslatef(-1.5f, 0, -6);
			// GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			// GL11.glVertexPointer(3, 0, triangleBuffer);
			// GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 3);
			// GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);

			// draw quad
			GL11.glColor3f(0f, 1f, 0f);
			GL11.glTranslatef(0, 0, -1f);
			GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
			GL11.glVertexPointer(3, 0, quadBuffer);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
			GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);

			Display.update();
			try {
				Thread.sleep(33);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static void initGL() {

		GL11.glClearColor(.5f, .5f, .5f, 1.0f);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		// gl.glEnable(GL11.GL_CULL_FACE); CANNOT ENABLE UNTIL TOUCHPLANE IS FIXED IN CCW order
		GL11.glClearDepth(1.0f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LESS);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_FASTEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GREATER, .01f);
	}

	private static void initViewport() {
		GL11.glViewport(0, 0, WIDTH, HEIGHT);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();

		GLU.gluPerspective(90.0f, (float) WIDTH / (float) HEIGHT, 0.01f, 100.0f);
	}
}
