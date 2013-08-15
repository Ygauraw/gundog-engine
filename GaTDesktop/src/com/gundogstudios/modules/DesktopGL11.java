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
package com.gundogstudios.modules;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.ARBBufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;

import com.gundogstudios.modules.GLES11Module;

public class DesktopGL11 implements GLES11Module {

	public DesktopGL11() {
	}

	public final void glActiveTexture(int texture) {
		GL13.glActiveTexture(texture);
	}

	public final void glAlphaFunc(int func, float ref) {
		GL11.glAlphaFunc(func, ref);
	}

	public void glBindBuffer(int target, int buffer) {
		ARBBufferObject.glBindBufferARB(target, buffer);
	}

	public final void glBindTexture(int target, int texture) {
		GL11.glBindTexture(target, texture);
	}

	public final void glBlendFunc(int sfactor, int dfactor) {
		GL11.glBlendFunc(sfactor, dfactor);
	}

	public void glBufferData(int target, int size, ByteBuffer data, int usage) {
		GL15.glBufferData(target, data, usage);
	}

	public void glBufferSubData(int target, int offset, int size, Buffer data) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public final void glClear(int mask) {
		GL11.glClear(mask);
	}

	public final void glClearColor(float red, float green, float blue, float alpha) {
		GL11.glClearColor(red, green, blue, alpha);
	}

	public final void glClearDepthf(float depth) {
		GL11.glClearDepth(depth);
	}

	public final void glClearStencil(int s) {
		GL11.glClearStencil(s);
	}

	public final void glClientActiveTexture(int texture) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public void glClipPlanef(int plane, float[] equation, int offset) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public void glClipPlanef(int plane, FloatBuffer equation) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public final void glColor4f(float red, float green, float blue, float alpha) {
		GL11.glColor4f(red, green, blue, alpha);
	}

	public void glColor4ub(byte red, byte green, byte blue, byte alpha) {
		GL11.glColor4ub(red, green, blue, alpha);
	}

	public final void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
		GL11.glColorMask(red, green, blue, alpha);
	}

	public final void glColorPointer(int size, int type, int stride, Buffer pointer) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public void glColorPointer(int size, int type, int stride, int pointer) {
		GL11.glColorPointer(size, type, stride, pointer);
	}

	public final void glCompressedTexImage2D(int target, int level, int internalformat, int width, int height,
			int border, int imageSize, Buffer data) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public final void glCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height,
			int format, int imageSize, Buffer data) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public final void glCopyTexImage2D(int target, int level, int internalformat, int x, int y, int width, int height,
			int border) {
		GL11.glCopyTexImage2D(target, level, internalformat, x, y, width, height, border);
	}

	public final void glCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y, int width,
			int height) {
		GL11.glCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height);
	}

	public final void glCullFace(int mode) {
		GL11.glCullFace(mode);
	}

	public void glDeleteBuffers(int n, int[] buffers, int offset) {
		for (int i = offset; i < offset + n; i++)
			GL15.glDeleteBuffers(buffers[i]);
	}

	public void glDeleteBuffers(int n, IntBuffer buffers) {
		GL15.glDeleteBuffers(buffers);
	}

	public final void glDeleteTextures(int n, int[] textures, int offset) {
		for (int i = offset; i < offset + n; i++)
			GL11.glDeleteTextures(textures[i]);
	}

	public final void glDeleteTextures(int n, IntBuffer textures) {
		GL11.glDeleteTextures(textures);
	}

	public final void glDepthFunc(int func) {
		GL11.glDepthFunc(func);
	}

	public final void glDepthMask(boolean flag) {
		GL11.glDepthMask(flag);
	}

	public final void glDepthRangef(float zNear, float zFar) {
		GL11.glDepthRange(zNear, zFar);
	}

	public final void glDisable(int cap) {
		GL11.glDisable(cap);
	}

	public final void glDisableClientState(int array) {
		GL11.glDisableClientState(array);
	}

	public final void glDrawArrays(int mode, int first, int count) {
		GL11.glDrawArrays(mode, first, count);
	}

	public final void glDrawElements(int mode, int count, int type, ByteBuffer indices) {
		GL11.glDrawElements(mode, indices);
	}

	public void glDrawElements(int mode, int count, int type, int indices) {
		GL11.glDrawElements(mode, count, type, indices);
	}

	public final void glEnable(int cap) {
		GL11.glEnable(cap);
	}

	public final void glEnableClientState(int array) {
		GL11.glEnableClientState(array);
	}

	public final void glFinish() {
		GL11.glFinish();
	}

	public final void glFlush() {
		GL11.glFlush();
	}

	public final void glFogf(int pname, float param) {
		GL11.glFogf(pname, param);
	}

	public final void glFogfv(int pname, float[] params, int offset) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public final void glFogfv(int pname, FloatBuffer params) {
		GL11.glFog(pname, params);
	}

	public final void glFrontFace(int mode) {
		GL11.glFrontFace(mode);
	}

	public final void glFrustumf(float left, float right, float bottom, float top, float zNear, float zFar) {
		GL11.glFrustum(left, right, bottom, top, zNear, zFar);
	}

	public void glGenBuffers(int n, int[] buffers, int offset) {
		for (int i = offset; i < offset + n; i++)
			buffers[i] = GL15.glGenBuffers();
	}

	public void glGenBuffers(int n, IntBuffer buffers) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public final void glGenTextures(int n, int[] textures, int offset) {
		for (int i = offset; i < offset + n; i++)
			textures[i] = GL11.glGenTextures();
	}

	public final void glGenTextures(int n, IntBuffer textures) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public void glGetBooleanv(int pname, boolean[] params, int offset) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public void glGetBooleanv(int pname, IntBuffer params) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public void glGetBufferParameteriv(int target, int pname, int[] params, int offset) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public void glGetBufferParameteriv(int target, int pname, IntBuffer params) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public void glGetClipPlanef(int pname, float[] eqn, int offset) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public void glGetClipPlanef(int pname, FloatBuffer eqn) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public final int glGetError() {
		return GL11.glGetError();
	}

	public void glGetFloatv(int pname, float[] params, int offset) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public void glGetFloatv(int pname, FloatBuffer params) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public final void glGetIntegerv(int pname, int[] params, int offset) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public final void glGetIntegerv(int pname, IntBuffer params) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public void glGetLightfv(int light, int pname, float[] params, int offset) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public void glGetLightfv(int light, int pname, FloatBuffer params) {
		GL11.glGetLight(light, pname, params);
	}

	public void glGetMaterialfv(int face, int pname, float[] params, int offset) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public void glGetMaterialfv(int face, int pname, FloatBuffer params) {
		GL11.glGetMaterial(face, pname, params);
	}

	public final String glGetString(int name) {
		return GL11.glGetString(name);
	}

	public void glGetTexEnviv(int env, int pname, int[] params, int offset) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public void glGetTexEnviv(int env, int pname, IntBuffer params) {
		GL11.glGetTexEnv(env, pname, params);
	}

	public void glGetTexParameterfv(int target, int pname, float[] params, int offset) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public void glGetTexParameterfv(int target, int pname, FloatBuffer params) {
		GL11.glGetTexParameter(target, pname, params);
	}

	public void glGetTexParameteriv(int target, int pname, int[] params, int offset) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public void glGetTexParameteriv(int target, int pname, IntBuffer params) {
		GL11.glGetTexParameter(target, pname, params);
	}

	public final void glHint(int target, int mode) {
		GL11.glHint(target, mode);
	}

	public boolean glIsBuffer(int buffer) {
		return GL15.glIsBuffer(buffer);
	}

	public boolean glIsEnabled(int cap) {
		return GL11.glIsEnabled(cap);
	}

	public boolean glIsTexture(int texture) {
		return GL11.glIsTexture(texture);
	}

	public final void glLightf(int light, int pname, float param) {
		GL11.glLightf(light, pname, param);
	}

	public final void glLightfv(int light, int pname, float[] params, int offset) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public final void glLightfv(int light, int pname, FloatBuffer params) {
		GL11.glLight(light, pname, params);
	}

	public final void glLightModelf(int pname, float param) {
		GL11.glLightModelf(pname, param);
	}

	public final void glLightModelfv(int pname, float[] params, int offset) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public final void glLightModelfv(int pname, FloatBuffer params) {
		GL11.glLightModel(pname, params);
	}

	public final void glLineWidth(float width) {
		GL11.glLineWidth(width);
	}

	public final void glLoadIdentity() {
		GL11.glLoadIdentity();
	}

	public final void glLoadMatrixf(float[] m, int offset) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public final void glLoadMatrixf(FloatBuffer m) {
		GL11.glLoadMatrix(m);
	}

	public final void glLogicOp(int opcode) {
		GL11.glLogicOp(opcode);
	}

	public final void glMaterialf(int face, int pname, float param) {
		GL11.glMaterialf(face, pname, param);
	}

	public final void glMaterialfv(int face, int pname, float[] params, int offset) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public final void glMaterialfv(int face, int pname, FloatBuffer params) {
		GL11.glMaterial(face, pname, params);
	}

	public final void glMatrixMode(int mode) {
		GL11.glMatrixMode(mode);
	}

	public final void glMultiTexCoord4f(int target, float s, float t, float r, float q) {
		GL13.glMultiTexCoord4f(target, s, t, r, q);
	}

	public final void glMultMatrixf(float[] m, int offset) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public final void glMultMatrixf(FloatBuffer m) {
		GL11.glMultMatrix(m);
	}

	public final void glNormal3f(float nx, float ny, float nz) {
		GL11.glNormal3f(nx, ny, nz);
	}

	public final void glNormalPointer(int type, int stride, ByteBuffer pointer) {
		GL11.glNormalPointer(type, pointer);
	}

	public void glNormalPointer(int type, int stride, int pointer) {
		GL11.glNormalPointer(type, stride, pointer);
	}

	public final void glOrthof(float left, float right, float bottom, float top, float zNear, float zFar) {
		GL11.glOrtho(left, right, bottom, top, zNear, zFar);
	}

	public final void glPixelStorei(int pname, int param) {
		GL11.glPixelStorei(pname, param);
	}

	public void glPointParameterf(int pname, float param) {
		GL14.glPointParameterf(pname, param);
	}

	public void glPointParameterfv(int pname, float[] params, int offset) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public void glPointParameterfv(int pname, FloatBuffer params) {
		GL14.glPointParameter(pname, params);
	}

	public final void glPointSize(float size) {
		GL11.glPointSize(size);
	}

	public void glPointSizePointerOES(int type, int stride, Buffer pointer) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public final void glPolygonOffset(float factor, float units) {
		GL11.glPolygonOffset(factor, units);
	}

	public final void glPopMatrix() {
		GL11.glPopMatrix();
	}

	public final void glPushMatrix() {
		GL11.glPushMatrix();
	}

	public final void glReadPixels(int x, int y, int width, int height, int format, int type, ByteBuffer pixels) {
		GL11.glReadPixels(x, y, width, height, format, type, pixels);
	}

	public final void glRotatef(float angle, float x, float y, float z) {
		GL11.glRotatef(angle, x, y, z);
	}

	public final void glSampleCoverage(float value, boolean invert) {
		GL13.glSampleCoverage(value, invert);
	}

	public final void glScalef(float x, float y, float z) {
		GL11.glScalef(x, y, z);
	}

	public final void glScissor(int x, int y, int width, int height) {
		GL11.glScissor(x, y, width, height);
	}

	public final void glShadeModel(int mode) {
		GL11.glShadeModel(mode);
	}

	public final void glStencilFunc(int func, int ref, int mask) {
		GL11.glStencilFunc(func, ref, mask);
	}

	public final void glStencilMask(int mask) {
		GL11.glStencilMask(mask);
	}

	public final void glStencilOp(int fail, int zfail, int zpass) {
		GL11.glStencilOp(fail, zfail, zpass);
	}

	public final void glTexCoordPointer(int size, int type, int stride, Buffer pointer) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public void glTexCoordPointer(int size, int type, int stride, int pointer) {
		GL11.glTexCoordPointer(size, type, stride, pointer);
	}

	public final void glTexEnvf(int target, int pname, float param) {
		GL11.glTexEnvf(target, pname, param);
	}

	public final void glTexEnvfv(int target, int pname, float[] params, int offset) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public final void glTexEnvfv(int target, int pname, FloatBuffer params) {
		GL11.glTexEnv(target, pname, params);
	}

	public void glTexEnvi(int target, int pname, int param) {
		GL11.glTexEnvi(target, pname, param);
	}

	public void glTexEnviv(int target, int pname, int[] params, int offset) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public void glTexEnviv(int target, int pname, IntBuffer params) {
		GL11.glTexEnv(target, pname, params);
	}

	public final void glTexImage2D(int target, int level, int internalformat, int width, int height, int border,
			int format, int type, ByteBuffer pixels) {
		GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
	}

	public final void glTexParameterf(int target, int pname, float param) {
		GL11.glTexParameterf(target, pname, param);
	}

	public void glTexParameterfv(int target, int pname, float[] params, int offset) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public void glTexParameterfv(int target, int pname, FloatBuffer params) {
		GL11.glTexParameter(target, pname, params);
	}

	public void glTexParameteri(int target, int pname, int param) {
		GL11.glTexParameteri(target, pname, param);
	}

	public void glTexParameteriv(int target, int pname, int[] params, int offset) {
		throw new UnsupportedOperationException("Not Implemented Yet");
	}

	public void glTexParameteriv(int target, int pname, IntBuffer params) {
		GL11.glTexParameter(target, pname, params);
	}

	public final void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height,
			int format, int type, ByteBuffer pixels) {
		GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
	}

	public final void glTranslatef(float x, float y, float z) {
		GL11.glTranslatef(x, y, z);
	}

	public final void glVertexPointer(int size, int type, int stride, Buffer pointer) {
		if (pointer instanceof FloatBuffer)
			GL11.glVertexPointer(3, 0, (FloatBuffer) pointer);
		else
			throw new UnsupportedOperationException("Not Implemented Yet");

	}

	public void glVertexPointer(int size, int type, int stride, int pointer) {
		GL11.glVertexPointer(size, type, stride, pointer);
	}

	public final void glViewport(int x, int y, int width, int height) {
		GL11.glViewport(x, y, width, height);
	}
}
