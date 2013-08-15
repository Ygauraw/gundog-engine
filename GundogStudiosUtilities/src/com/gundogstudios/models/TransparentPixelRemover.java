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
package com.gundogstudios.models;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.gundogstudios.models.md2.MD2Parser;

public class TransparentPixelRemover {
	/**
	 * The goal for this class was to generate a new index list that eliminates vertices that are completely
	 * transparent. This allows textures that are jpg's, with no transparent component to be utilized which are 10% the
	 * size of their png counterparts.
	 * 
	 * The idea behind this tool is so that different models can share the same vertex list, with only minor differences
	 * in their index lists. This should reduce the image and model memory requirement significantly for reused Meshes.
	 */
	public static void main(String[] args) throws IOException {
		BufferedImage imageOne = ImageIO.read(new File("images/angel.png"));
		BufferedImage imageTwo = ImageIO.read(new File("images/fallen_angel.png"));
		int width = imageOne.getWidth();
		int height = imageOne.getHeight();
		System.out.println(width + " " + height);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int rgba = imageOne.getRGB(x, y);
				int r = rgba & 0x000000ff;
				int g = rgba & 0x0000ff00;
				int b = rgba & 0x00ff0000;
				int a = rgba & 0xff000000;
				System.out.printf("%h %h %h %h %h\n", rgba, r, g, b, a);
			}
		}

		CombinedModel model = MD2Parser.parseReduced("MaleAngels");
		float[] uvs = model.getFloatUVs();
		System.out.println(uvs.length);
		for (int i = 0; i < uvs.length - 1; i += 2) {
			int x = (int) (width * uvs[i]);
			int y = (int) (height * uvs[i + 1]);
			int rgbaOne = imageOne.getRGB(x, y);
			int rgbaTwo = imageTwo.getRGB(x, y);

			int r = rgbaOne & 0x000000ff;
			int g = rgbaOne & 0x0000ff00;
			int b = rgbaOne & 0x00ff0000;
			int a = rgbaOne & 0xff000000;

			System.out.printf("%d %d %h %h %h %h %h\n", x, y, rgbaOne, r, g, b, a);
			// System.out.println(uvs[i] + " " + uvs[i + 1]);
			if (rgbaOne == 0 || rgbaTwo == 0) {
				System.out.printf("%d %d %h %h\n", x, y, rgbaOne, rgbaTwo);
			}
		}
	}

}
