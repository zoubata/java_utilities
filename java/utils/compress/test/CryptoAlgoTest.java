/**
 * 
 */
package com.zoubworld.java.utils.compress.test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zoubworld.java.utils.cryptography.CaesarCipher32;
import com.zoubworld.java.utils.cryptography.CaesarCipher8;
import com.zoubworld.java.utils.cryptography.VegenereCipher8;
import com.zoubworld.java.utils.cryptography.XorCipher8;
import com.zoubworld.java.utils.cryptography.Enigma.Machine;

/**
 * @author Pierre Valleau
 *
 */
public class CryptoAlgoTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testEnigma() throws IOException {

		String f = // "* B III IV I AXLE\r\n" +
				"FROM his shoulder Hiawatha\r\n" + "Took the camera of rosewood\r\n"
						+ "Made of sliding folding rosewood\r\n" + "Neatly put it all together\r\n"
						+ "In its case it lay compactly\r\n" + "Folded into nearly nothing\r\n"
						+ "But he opened out the hinges\r\n" + "Pushed and pulled the joints\r\n" + "and hinges\r\n"
						+ "Till it looked all squares\r\n" + "and oblongs\r\n" + "Like a complicated figure\r\n"
						+ "In the Second Book of Euclid";

		Machine M = new Machine();
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

		assertEquals(true,
				com.zoubworld.java.utils.cryptography.Enigma.Main.isConfigurationLine("* B III IV I AXLE\r\n"));
		assertEquals(false, com.zoubworld.java.utils.cryptography.Enigma.Main
				.isConfigurationLine("FROM his shoulder Hiawatha\r\n"));
		com.zoubworld.java.utils.cryptography.Enigma.Main.buildRotors();
		com.zoubworld.java.utils.cryptography.Enigma.Main.configure(M, "* B III IV I AXLE\r\n");
		String f2 = "";
		for (String line : f.split("\r\n"))
			f2 += M.convert(com.zoubworld.java.utils.cryptography.Enigma.Main.standardize(line));
		assertEquals(
				"HYIHLBKOMLIUYDCMPPSFSZWSQCNJEXNUOJYRZEKTCNBDGUFLIIEGEPGRSJUJTCALGXSNCTMKUFWMFCKWIPRYSODJCVCFYQLVQLMBYUQRIRXEPOVEUHFIRIFKCGVSFPBGPKDRFYRTVMWGFUNMXEHFHVPQIDOACGUIWGTNMKVCKCFDZIOPYEVXNTBXYAHAOBMQOPGTZXVXQXOLEDRWYCMMWAONVUKQOUFASRHACKKXOMZTDALHUNVXKPXBHAVQXVXEPUNUXTXYNIFFMDYJVKH",
				f2);
		com.zoubworld.java.utils.cryptography.Enigma.Main.configure(M, "* B III IV I AXLE\r\n");

		String f3 = M.convert(com.zoubworld.java.utils.cryptography.Enigma.Main.standardize(f2));
		assertEquals(f.replaceAll(" ", "").replaceAll("\r\n", "").toUpperCase(), f3);

	}

	@Test
	public final void test() {
		byte key = 1;
		CaesarCipher32 c = new CaesarCipher32(key, 8);
		assertEquals((int) 'B' + 0x01010100, c.encrypt('A').intValue());
		assertEquals((int) 'A', 0xff & c.decrypt('B').intValue());

		int d = ((int) 'A') + (((int) 'A') << 24) + (((int) 'A') << 16) + (((int) 'A') << 8);
		int e = ((int) 'B') + (((int) 'B') << 24) + (((int) 'B') << 16) + (((int) 'B') << 8);
		CaesarCipher8 c2 = new CaesarCipher8(key);
		assertEquals(e, c2.encrypt(d).intValue());
		assertEquals(0x01010143, c2.encrypt('B').intValue());
		c2.reset();
		assertEquals(d, c2.decrypt(e).intValue());

		XorCipher8 c3 = new XorCipher8("abc");
		assertEquals(0x20222320, c3.encrypt(d).intValue());
		assertEquals(0x62616323, c3.encrypt('A').intValue());
		c3.reset();
		assertEquals(d, c3.decrypt(0x20222320).intValue());
		assertEquals((int) 'A', c3.decrypt(0x62616323).intValue());

		CaesarCipher8 c4 = new CaesarCipher8((byte) 1);
		assertEquals(e, c4.encrypt(d).intValue());
		assertEquals(e, c4.encrypt(d).intValue());
		assertEquals(e, c4.encrypt(d).intValue());
		c4.reset();
		assertEquals(d, c4.decrypt(e).intValue());
		assertEquals(d, c4.decrypt(e).intValue());
		assertEquals(d, c4.decrypt(e).intValue());

		VegenereCipher8 c6 = new VegenereCipher8("abc");
		assertEquals((byte) -74, c6.encrypt((byte) 0x55));
		assertEquals((byte) -73, c6.encrypt((byte) 0x55));
		assertEquals((byte) -72, c6.encrypt((byte) 0x55));
		assertEquals(1633903286, c6.encrypt((int) 0x55).intValue());
		c6.reset();
		assertEquals((byte) 'U', c6.decrypt((byte) -74));
		assertEquals((byte) 'U', c6.decrypt((byte) -73));
		assertEquals((byte) 'U', c6.decrypt((byte) -72));
		assertEquals((int) 0x55, c6.decrypt((int) 1633903286).intValue());

		/*
		 * EnigmaAlgo c5=new EnigmaAlgo("abc"); assertEquals(e,
		 * c5.encrypt('A').intValue()); assertEquals(e, c5.encrypt('A').intValue());
		 * assertEquals(e, c5.encrypt('A').intValue()); c5.reset();
		 * assertEquals((int)'A', c5.decrypt('B').intValue()); assertEquals((int)'A',
		 * c5.decrypt('B').intValue()); assertEquals((int)'A',
		 * c5.decrypt('B').intValue());
		 */

	}

}
