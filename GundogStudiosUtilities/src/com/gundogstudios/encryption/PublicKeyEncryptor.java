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
package com.gundogstudios.encryption;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class PublicKeyEncryptor {
	public static final int MASK = 234758624;
	public static final String PUBLIC_KEY = "";

	public static void main(String[] args) throws IOException {
		String encryptedKey = encrypt(PUBLIC_KEY);

		BufferedWriter outputStream = new BufferedWriter(new FileWriter("key.txt"));

		outputStream.write(encryptedKey);

		outputStream.close();
	}

	public static String encrypt(String key) throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream stream = new ObjectOutputStream(byteStream);
		stream.writeUTF(PUBLIC_KEY);
		stream.flush();
		byteStream.flush();
		byte[] bytes = byteStream.toByteArray();

		encrypt(bytes, MASK);

		StringBuilder builder = new StringBuilder();
		for (byte b : bytes) {
			builder.append("" + b + ",");
		}
		return builder.toString();
	}

	private static void encrypt(byte[] bytes, int mask) {

		for (int i = 0; i < bytes.length - 1; i += 2) {
			bytes[i] = (byte) ~bytes[i];
			bytes[i + 1] = (byte) ~bytes[i + 1];
			bytes[i] = (byte) (bytes[i] ^ bytes[i + 1]);
			bytes[i + 1] = (byte) (bytes[i] ^ bytes[i + 1]);
			bytes[i] = (byte) (bytes[i] ^ bytes[i + 1]);
		}
	}

}
