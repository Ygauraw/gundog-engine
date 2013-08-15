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

import android.opengl.GLES11;

public class AndroidGLES11 implements GLES11Module {

	public AndroidGLES11() {
	}

	public final void glActiveTexture(int texture) {
		GLES11.glActiveTexture(texture);
	}

	public final void glAlphaFunc(int func, float ref) {
		GLES11.glAlphaFunc(func, ref);
	}

	public void glBindBuffer(int target, int buffer) {
		GLES11.glBindBuffer(target, buffer);
	}

	public final void glBindTexture(int target, int texture) {
		GLES11.glBindTexture(target, texture);
	}

	public final void glBlendFunc(int sfactor, int dfactor) {
		GLES11.glBlendFunc(sfactor, dfactor);
	}

	public void glBufferData(int target, int size, ByteBuffer data, int usage) {
		GLES11.glBufferData(target, size, data, usage);
	}

	public void glBufferSubData(int target, int offset, int size, Buffer data) {
		GLES11.glBufferSubData(target, offset, size, data);
	}

	public final void glClear(int mask) {
		GLES11.glClear(mask);
	}

	public final void glClearColor(float red, float green, float blue, float alpha) {
		GLES11.glClearColor(red, green, blue, alpha);
	}

	public final void glClearDepthf(float depth) {
		GLES11.glClearDepthf(depth);
	}

	public final void glClearStencil(int s) {
		GLES11.glClearStencil(s);
	}

	public final void glClientActiveTexture(int texture) {
		GLES11.glClientActiveTexture(texture);
	}

	public void glClipPlanef(int plane, float[] equation, int offset) {
		GLES11.glClipPlanef(plane, equation, offset);
	}

	public void glClipPlanef(int plane, FloatBuffer equation) {
		GLES11.glClipPlanef(plane, equation);
	}

	public final void glColor4f(float red, float green, float blue, float alpha) {
		GLES11.glColor4f(red, green, blue, alpha);
	}

	public void glColor4ub(byte red, byte green, byte blue, byte alpha) {
		GLES11.glColor4ub(red, green, blue, alpha);
	}

	public final void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
		GLES11.glColorMask(red, green, blue, alpha);
	}

	public final void glColorPointer(int size, int type, int stride, Buffer pointer) {
		GLES11.glColorPointer(size, type, stride, pointer);
	}

	public void glColorPointer(int size, int type, int stride, int pointer) {
		GLES11.glColorPointer(size, type, stride, pointer);
	}

	public final void glCompressedTexImage2D(int target, int level, int internalformat, int width, int height,
			int border, int imageSize, Buffer data) {
		GLES11.glCompressedTexImage2D(target, level, internalformat, width, height, border, imageSize, data);
	}

	public final void glCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height,
			int format, int imageSize, Buffer data) {
		GLES11.glCompressedTexSubImage2D(target, level, xoffset, yoffset, width, height, format, imageSize, data);
	}

	public final void glCopyTexImage2D(int target, int level, int internalformat, int x, int y, int width, int height,
			int border) {
		GLES11.glCopyTexImage2D(target, level, internalformat, x, y, width, height, border);
	}

	public final void glCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y, int width,
			int height) {
		GLES11.glCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height);
	}

	public final void glCullFace(int mode) {
		GLES11.glCullFace(mode);
	}

	public void glDeleteBuffers(int n, int[] buffers, int offset) {
		GLES11.glDeleteBuffers(n, buffers, offset);
	}

	public void glDeleteBuffers(int n, IntBuffer buffers) {
		GLES11.glDeleteBuffers(n, buffers);
	}

	public final void glDeleteTextures(int n, int[] textures, int offset) {
		GLES11.glDeleteTextures(n, textures, offset);
	}

	public final void glDeleteTextures(int n, IntBuffer textures) {
		GLES11.glDeleteTextures(n, textures);
	}

	public final void glDepthFunc(int func) {
		GLES11.glDepthFunc(func);
	}

	public final void glDepthMask(boolean flag) {
		GLES11.glDepthMask(flag);
	}

	public final void glDepthRangef(float zNear, float zFar) {
		GLES11.glDepthRangef(zNear, zFar);
	}

	public final void glDisable(int cap) {
		GLES11.glDisable(cap);
	}

	public final void glDisableClientState(int array) {
		GLES11.glDisableClientState(array);
	}

	public final void glDrawArrays(int mode, int first, int count) {
		GLES11.glDrawArrays(mode, first, count);
	}

	public final void glDrawElements(int mode, int count, int type, ByteBuffer indices) {
		GLES11.glDrawElements(mode, count, type, indices);
	}

	public void glDrawElements(int mode, int count, int type, int indices) {
		GLES11.glDrawElements(mode, count, type, indices);
	}

	public final void glEnable(int cap) {
		GLES11.glEnable(cap);
	}

	public final void glEnableClientState(int array) {
		GLES11.glEnableClientState(array);
	}

	public final void glFinish() {
		GLES11.glFinish();
	}

	public final void glFlush() {
		GLES11.glFlush();
	}

	public final void glFogf(int pname, float param) {
		GLES11.glFogf(pname, param);
	}

	public final void glFogfv(int pname, float[] params, int offset) {
		GLES11.glFogfv(pname, params, offset);
	}

	public final void glFogfv(int pname, FloatBuffer params) {
		GLES11.glFogfv(pname, params);
	}

	public final void glFrontFace(int mode) {
		GLES11.glFrontFace(mode);
	}

	public final void glFrustumf(float left, float right, float bottom, float top, float zNear, float zFar) {
		GLES11.glFrustumf(left, right, bottom, top, zNear, zFar);
	}

	public void glGenBuffers(int n, int[] buffers, int offset) {
		GLES11.glGenBuffers(n, buffers, offset);
	}

	public void glGenBuffers(int n, IntBuffer buffers) {
		GLES11.glGenBuffers(n, buffers);
	}

	public final void glGenTextures(int n, int[] textures, int offset) {
		GLES11.glGenTextures(n, textures, offset);
	}

	public final void glGenTextures(int n, IntBuffer textures) {
		GLES11.glGenTextures(n, textures);
	}

	public void glGetBooleanv(int pname, boolean[] params, int offset) {
		GLES11.glGetBooleanv(pname, params, offset);
	}

	public void glGetBooleanv(int pname, IntBuffer params) {
		GLES11.glGetBooleanv(pname, params);
	}

	public void glGetBufferParameteriv(int target, int pname, int[] params, int offset) {
		GLES11.glGetBufferParameteriv(target, pname, params, offset);
	}

	public void glGetBufferParameteriv(int target, int pname, IntBuffer params) {
		GLES11.glGetBufferParameteriv(target, pname, params);
	}

	public void glGetClipPlanef(int pname, float[] eqn, int offset) {
		GLES11.glGetClipPlanef(pname, eqn, offset);
	}

	public void glGetClipPlanef(int pname, FloatBuffer eqn) {
		GLES11.glGetClipPlanef(pname, eqn);
	}

	public final int glGetError() {
		return GLES11.glGetError();
	}

	public void glGetFloatv(int pname, float[] params, int offset) {
		GLES11.glGetFloatv(pname, params, offset);
	}

	public void glGetFloatv(int pname, FloatBuffer params) {
		GLES11.glGetFloatv(pname, params);
	}

	public final void glGetIntegerv(int pname, int[] params, int offset) {
		GLES11.glGetIntegerv(pname, params, offset);
	}

	public final void glGetIntegerv(int pname, IntBuffer params) {
		GLES11.glGetIntegerv(pname, params);
	}

	public void glGetLightfv(int light, int pname, float[] params, int offset) {
		GLES11.glGetLightfv(light, pname, params, offset);
	}

	public void glGetLightfv(int light, int pname, FloatBuffer params) {
		GLES11.glGetLightfv(light, pname, params);
	}

	public void glGetMaterialfv(int face, int pname, float[] params, int offset) {
		GLES11.glGetMaterialfv(face, pname, params, offset);
	}

	public void glGetMaterialfv(int face, int pname, FloatBuffer params) {
		GLES11.glGetMaterialfv(face, pname, params);
	}

	public final String glGetString(int name) {
		return GLES11.glGetString(name);
	}

	public void glGetTexEnviv(int env, int pname, int[] params, int offset) {
		GLES11.glGetTexEnviv(env, pname, params, offset);
	}

	public void glGetTexEnviv(int env, int pname, IntBuffer params) {
		GLES11.glGetTexEnviv(env, pname, params);
	}

	public void glGetTexParameterfv(int target, int pname, float[] params, int offset) {
		GLES11.glGetTexParameterfv(target, pname, params, offset);
	}

	public void glGetTexParameterfv(int target, int pname, FloatBuffer params) {
		GLES11.glGetTexParameterfv(target, pname, params);
	}

	public void glGetTexParameteriv(int target, int pname, int[] params, int offset) {
		GLES11.glGetTexParameteriv(target, pname, params, offset);
	}

	public void glGetTexParameteriv(int target, int pname, IntBuffer params) {
		GLES11.glGetTexParameteriv(target, pname, params);
	}

	public final void glHint(int target, int mode) {
		GLES11.glHint(target, mode);
	}

	public boolean glIsBuffer(int buffer) {
		return GLES11.glIsBuffer(buffer);
	}

	public boolean glIsEnabled(int cap) {
		return GLES11.glIsEnabled(cap);
	}

	public boolean glIsTexture(int texture) {
		return GLES11.glIsTexture(texture);
	}

	public final void glLightf(int light, int pname, float param) {
		GLES11.glLightf(light, pname, param);
	}

	public final void glLightfv(int light, int pname, float[] params, int offset) {
		GLES11.glLightfv(light, pname, params, offset);
	}

	public final void glLightfv(int light, int pname, FloatBuffer params) {
		GLES11.glLightfv(light, pname, params);
	}

	public final void glLightModelf(int pname, float param) {
		GLES11.glLightModelf(pname, param);
	}

	public final void glLightModelfv(int pname, float[] params, int offset) {
		GLES11.glLightModelfv(pname, params, offset);
	}

	public final void glLightModelfv(int pname, FloatBuffer params) {
		GLES11.glLightModelfv(pname, params);
	}

	public final void glLineWidth(float width) {
		GLES11.glLineWidth(width);
	}

	public final void glLoadIdentity() {
		GLES11.glLoadIdentity();
	}

	public final void glLoadMatrixf(float[] m, int offset) {
		GLES11.glLoadMatrixf(m, offset);
	}

	public final void glLoadMatrixf(FloatBuffer m) {
		GLES11.glLoadMatrixf(m);
	}

	public final void glLogicOp(int opcode) {
		GLES11.glLogicOp(opcode);
	}

	public final void glMaterialf(int face, int pname, float param) {
		GLES11.glMaterialf(face, pname, param);
	}

	public final void glMaterialfv(int face, int pname, float[] params, int offset) {
		GLES11.glMaterialfv(face, pname, params, offset);
	}

	public final void glMaterialfv(int face, int pname, FloatBuffer params) {
		GLES11.glMaterialfv(face, pname, params);
	}

	public final void glMatrixMode(int mode) {
		GLES11.glMatrixMode(mode);
	}

	public final void glMultiTexCoord4f(int target, float s, float t, float r, float q) {
		GLES11.glMultiTexCoord4f(target, s, t, r, q);
	}

	public final void glMultMatrixf(float[] m, int offset) {
		GLES11.glMultMatrixf(m, offset);
	}

	public final void glMultMatrixf(FloatBuffer m) {
		GLES11.glMultMatrixf(m);
	}

	public final void glNormal3f(float nx, float ny, float nz) {
		GLES11.glNormal3f(nx, ny, nz);
	}

	public final void glNormalPointer(int type, int stride, ByteBuffer pointer) {
		GLES11.glNormalPointer(type, stride, pointer);
	}

	public void glNormalPointer(int type, int stride, int pointer) {
		GLES11.glNormalPointer(type, stride, pointer);
	}

	public final void glOrthof(float left, float right, float bottom, float top, float zNear, float zFar) {
		GLES11.glOrthof(left, right, bottom, top, zNear, zFar);
	}

	public final void glPixelStorei(int pname, int param) {
		GLES11.glPixelStorei(pname, param);
	}

	public void glPointParameterf(int pname, float param) {
		GLES11.glPointParameterf(pname, param);
	}

	public void glPointParameterfv(int pname, float[] params, int offset) {
		GLES11.glPointParameterfv(pname, params, offset);
	}

	public void glPointParameterfv(int pname, FloatBuffer params) {
		GLES11.glPointParameterfv(pname, params);
	}

	public final void glPointSize(float size) {
		GLES11.glPointSize(size);
	}

	public void glPointSizePointerOES(int type, int stride, Buffer pointer) {
		GLES11.glPointSizePointerOES(type, stride, pointer);
	}

	public final void glPolygonOffset(float factor, float units) {
		GLES11.glPolygonOffset(factor, units);
	}

	public final void glPopMatrix() {
		GLES11.glPopMatrix();
	}

	public final void glPushMatrix() {
		GLES11.glPushMatrix();
	}

	public final void glReadPixels(int x, int y, int width, int height, int format, int type, ByteBuffer pixels) {
		GLES11.glReadPixels(x, y, width, height, format, type, pixels);
	}

	public final void glRotatef(float angle, float x, float y, float z) {
		GLES11.glRotatef(angle, x, y, z);
	}

	public final void glSampleCoverage(float value, boolean invert) {
		GLES11.glSampleCoverage(value, invert);
	}

	public final void glScalef(float x, float y, float z) {
		GLES11.glScalef(x, y, z);
	}

	public final void glScissor(int x, int y, int width, int height) {
		GLES11.glScissor(x, y, width, height);
	}

	public final void glShadeModel(int mode) {
		GLES11.glShadeModel(mode);
	}

	public final void glStencilFunc(int func, int ref, int mask) {
		GLES11.glStencilFunc(func, ref, mask);
	}

	public final void glStencilMask(int mask) {
		GLES11.glStencilMask(mask);
	}

	public final void glStencilOp(int fail, int zfail, int zpass) {
		GLES11.glStencilOp(fail, zfail, zpass);
	}

	public final void glTexCoordPointer(int size, int type, int stride, Buffer pointer) {
		GLES11.glTexCoordPointer(size, type, stride, pointer);
	}

	public void glTexCoordPointer(int size, int type, int stride, int pointer) {
		GLES11.glTexCoordPointer(size, type, stride, pointer);
	}

	public final void glTexEnvf(int target, int pname, float param) {
		GLES11.glTexEnvf(target, pname, param);
	}

	public final void glTexEnvfv(int target, int pname, float[] params, int offset) {
		GLES11.glTexEnvfv(target, pname, params, offset);
	}

	public final void glTexEnvfv(int target, int pname, FloatBuffer params) {
		GLES11.glTexEnvfv(target, pname, params);
	}

	public void glTexEnvi(int target, int pname, int param) {
		GLES11.glTexEnvi(target, pname, param);
	}

	public void glTexEnviv(int target, int pname, int[] params, int offset) {
		GLES11.glTexEnviv(target, pname, params, offset);
	}

	public void glTexEnviv(int target, int pname, IntBuffer params) {
		GLES11.glTexEnviv(target, pname, params);
	}

	public final void glTexImage2D(int target, int level, int internalformat, int width, int height, int border,
			int format, int type, ByteBuffer pixels) {
		GLES11.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
	}

	public final void glTexParameterf(int target, int pname, float param) {
		GLES11.glTexParameterf(target, pname, param);
	}

	public void glTexParameterfv(int target, int pname, float[] params, int offset) {
		GLES11.glTexParameterfv(target, pname, params, offset);
	}

	public void glTexParameterfv(int target, int pname, FloatBuffer params) {
		GLES11.glTexParameterfv(target, pname, params);
	}

	public void glTexParameteri(int target, int pname, int param) {
		GLES11.glTexParameteri(target, pname, param);
	}

	public void glTexParameteriv(int target, int pname, int[] params, int offset) {
		GLES11.glTexParameteriv(target, pname, params, offset);
	}

	public void glTexParameteriv(int target, int pname, IntBuffer params) {
		GLES11.glTexParameteriv(target, pname, params);
	}

	public final void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height,
			int format, int type, ByteBuffer pixels) {
		GLES11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
	}

	public final void glTranslatef(float x, float y, float z) {
		GLES11.glTranslatef(x, y, z);
	}

	public final void glVertexPointer(int size, int type, int stride, Buffer pointer) {
		GLES11.glVertexPointer(size, type, stride, pointer);
	}

	public void glVertexPointer(int size, int type, int stride, int pointer) {
		GLES11.glVertexPointer(size, type, stride, pointer);
	}

	public final void glViewport(int x, int y, int width, int height) {
		GLES11.glViewport(x, y, width, height);
	}
}
