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
package com.gundogstudios.gl.custom;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import com.gundogstudios.gl.Actions;
import com.gundogstudios.gl.BaseModel;
import com.gundogstudios.gl.ModelUtils;
import com.gundogstudios.modules.GLES11Module;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.util.FastMath;

public class Spray extends BaseModel {

	public static final String NAME = "spray";
	private final static int FLOAT_SIZE = 4;
	private final static int VERTEX_SIZE = 3 * FLOAT_SIZE;

	private int currentFrame;
	private int[] vertexBufferIds;
	private int totalParticles;
	private float pointSize;

	public Spray(Spray system) {
		super(system);
		this.currentFrame = system.currentFrame;
		this.vertexBufferIds = system.vertexBufferIds;
		this.totalParticles = system.totalParticles;
		this.pointSize = system.pointSize;
	}

	public Spray() {
		super(Actions.ATTACK);
		super.setRotates(true);
		this.pointSize = ModelUtils.getParticleScaleSize();
		this.totalParticles = 800 / ModelUtils.getBitmapScaleSize();

		int maxFrames = ModelUtils.getMeshFrameRate();
		Particle[] particles = new Particle[totalParticles];

		float tileSize = 1f;
		float x = tileSize * .15f;
		float y = 0f;
		float z = tileSize * .9f;
		float speed = tileSize / 25f;
		int count = totalParticles / 4;

		initSpray(particles, 0, count, maxFrames, speed, x, y, z, true, 1f);
		initSpray(particles, count, count, maxFrames, speed, x, y, z, true, -1f);
		initSpray(particles, count * 2, count, maxFrames, speed, x, y, z, false, 1f);
		initSpray(particles, count * 3, count, maxFrames, speed, x, y, z, false, -1f);

		this.vertexBufferIds = new int[maxFrames];
		generateAnimation(particles);
	}

	private void initSpray(Particle[] particles, int offset, int count, int maxFrames, float speed, float x, float y,
			float z, boolean xMove, float direction) {
		Random gen = new Random(System.currentTimeMillis());
		float speedTwo = speed / 4f;
		int frameModifier = 20 / maxFrames;
		for (int i = offset; i < offset + count; i++) {
			particles[i] = new Particle();

			float dx = (gen.nextFloat() * speed + speed) * frameModifier;
			float dy = (gen.nextFloat() * speedTwo) * (gen.nextBoolean() ? -1 : 1) * frameModifier;
			float dz = (gen.nextFloat() * speedTwo) * (gen.nextBoolean() ? -1 : 1) * frameModifier;

			if (xMove) {
				particles[i].x = x * direction;
				particles[i].y = y;
				particles[i].dx = dx * direction;
				particles[i].dy = dy;
				particles[i].ax = 1f;
				particles[i].ay = gen.nextFloat();
			} else {
				particles[i].x = y;
				particles[i].y = x * direction;
				particles[i].dx = dy;
				particles[i].dy = dx * direction;
				particles[i].ax = gen.nextFloat();
				particles[i].ay = 1f;
			}

			particles[i].z = z;
			particles[i].dz = dz;
			particles[i].az = gen.nextFloat();
		}
	}

	private void generateAnimation(Particle[] particles) {

		GLES11Module gl = Modules.GL;
		gl.glGenBuffers(vertexBufferIds.length, vertexBufferIds, 0);

		ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect(totalParticles * VERTEX_SIZE).order(
				ByteOrder.nativeOrder());

		FloatBuffer vertexBuffer = vertexByteBuffer.asFloatBuffer();
		for (int j = 0; j < vertexBufferIds.length; j++) {
			vertexBuffer.position(0);

			for (int i = 0; i < totalParticles; i++) {

				particles[i].dx = particles[i].dx * particles[i].ax;
				particles[i].dy = particles[i].dy * particles[i].ay;
				particles[i].dz = particles[i].dz * particles[i].az;

				particles[i].x = particles[i].x + (particles[i].dx);
				particles[i].y = particles[i].y + (particles[i].dy);
				particles[i].z = particles[i].z + (particles[i].dz);

				vertexBuffer.put(particles[i].x);
				vertexBuffer.put(particles[i].y);
				vertexBuffer.put(particles[i].z);
			}

			vertexBuffer.position(0);
			vertexByteBuffer.position(0);

			gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, vertexBufferIds[j]);
			gl.glBufferData(GLES11Module.GL_ARRAY_BUFFER, vertexByteBuffer.capacity(), vertexByteBuffer,
					GLES11Module.GL_STATIC_DRAW);
		}
		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, 0);
	}

	@Override
	protected void drawModel(int timePassed) {
		int currentFrame = FastMath.floor((((float) totalTime) / 1000f) * vertexBufferIds.length);

		GLES11Module gl = Modules.GL;
		// gl.glPointSize(pointSize);
		gl.glEnableClientState(GLES11Module.GL_VERTEX_ARRAY);
		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, vertexBufferIds[currentFrame]);
		gl.glVertexPointer(3, GLES11Module.GL_FLOAT, VERTEX_SIZE, 0);
		gl.glDrawArrays(GLES11Module.GL_POINTS, 0, totalParticles);
		gl.glDisableClientState(GLES11Module.GL_VERTEX_ARRAY);
		// gl.glPointSize(1f);

		gl.glBindBuffer(GLES11Module.GL_ARRAY_BUFFER, 0);

		super.totalTime = (totalTime + timePassed) % 1000;
	}

	@Override
	public void reset() {
		totalTime = 0;
	}

	private class Particle {
		public float x, y, z;
		public float dx, dy, dz;
		public float ax, ay, az;

		public Particle() {
		}
	}

}
