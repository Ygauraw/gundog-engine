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

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.junit.Before;
import org.junit.Test;

import com.gundogstudios.encryption.PublicKeyEncryptor;

public class PublicKeyTester {

	@Before
	public void setUp() {

	}

	@Test
	public void testSave() throws IOException, ClassNotFoundException {

		String encryptedKey = PublicKeyEncryptor.encrypt(PublicKeyEncryptor.PUBLIC_KEY);
		System.out.println(encryptedKey);
		String decryptedKey = decrypt(encryptedKey);
		String publicKey = PublicKeyEncryptor.PUBLIC_KEY;
		assertEquals("Strings not equal", publicKey, decryptedKey);

	}

	private static String decrypt(String encryptedKey) throws IOException {
		String[] strings = encryptedKey.split(",");
		byte[] bytes = new byte[strings.length];
		for (int i = 0; i < strings.length; i++) {
			bytes[i] = Byte.parseByte(strings[i]);
		}
		decrypt(bytes, PublicKeyEncryptor.MASK);

		ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
		ObjectInputStream stream = new ObjectInputStream(byteStream);

		String key = stream.readUTF();
		return key;
	}

	private static void decrypt(byte[] bytes, int mask) {

		for (int i = 0; i < bytes.length - 1; i += 2) {
			bytes[i] = (byte) ~bytes[i];
			bytes[i + 1] = (byte) ~bytes[i + 1];
			bytes[i] = (byte) (bytes[i] ^ bytes[i + 1]);
			bytes[i + 1] = (byte) (bytes[i] ^ bytes[i + 1]);
			bytes[i] = (byte) (bytes[i] ^ bytes[i + 1]);
		}
	}
}
