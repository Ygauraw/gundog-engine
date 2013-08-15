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

public class Explosion extends BaseModel {

	public static final String NAME = "explosion";
	private final static int FLOAT_SIZE = 4;
	private final static int VERTEX_SIZE = 3 * FLOAT_SIZE;

	private int[] vertexBufferIds;
	private int totalParticles;
	private float pointSize;

	public Explosion(Explosion system) {
		super(system);
		this.vertexBufferIds = system.vertexBufferIds;
		this.totalParticles = system.totalParticles;
		this.pointSize = system.pointSize;
	}

	public Explosion() {
		super(Actions.DEATH);

		this.pointSize = ModelUtils.getParticleScaleSize();
		this.totalParticles = 400 / ModelUtils.getBitmapScaleSize();

		int maxFrames = ModelUtils.getMeshFrameRate();
		Particle[] particles = new Particle[totalParticles];

		initExplosion(particles, maxFrames, 1f);

		this.vertexBufferIds = new int[maxFrames];
		generateAnimation(particles);
	}

	private void initExplosion(Particle[] particles, int maxFrames, float tileSize) {

		Random gen = new Random(System.currentTimeMillis());
		for (int i = 0; i < totalParticles; i++) {
			particles[i] = new Particle();
			particles[i].x = 0f;
			particles[i].y = 0f;
			particles[i].z = 0f;

			int frameModifier = 20 / maxFrames;

			float speed = tileSize / 40f;

			float totalMovement = speed * 1.75f;

			float dx = gen.nextFloat() * speed;
			particles[i].dx = dx * (gen.nextBoolean() ? -1 : 1) * frameModifier;
			totalMovement -= dx;

			float dy = FastMath.min(gen.nextFloat() * speed, totalMovement);
			particles[i].dy = dy * (gen.nextBoolean() ? -1 : 1) * frameModifier;
			totalMovement -= dy;

			float dz = FastMath.min(gen.nextFloat() * speed, totalMovement);
			particles[i].dz = dz * (gen.nextBoolean() ? -1 : 1) * frameModifier;
			particles[i].ax = 1f;
			particles[i].ay = 1f;
			particles[i].az = 1f;
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
		if (totalTime >= 1000f)
			return;

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
