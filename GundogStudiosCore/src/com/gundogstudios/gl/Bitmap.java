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
package com.gundogstudios.gl;

import java.nio.ByteBuffer;

public class Bitmap {
	private String name;
	private int width;
	private int height;
	private int format;
	private ByteBuffer imageBytes;

	public Bitmap(String name, int width, int height, int format, ByteBuffer imageBytes) {
		this.name = name;
		this.width = width;
		this.height = height;
		this.format = format;
		this.imageBytes = imageBytes;
	}

	public String getName() {
		return name;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getFormat() {
		return format;
	}

	public ByteBuffer getImageBytes() {
		return imageBytes;
	}

}
