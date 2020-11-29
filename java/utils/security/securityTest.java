/**
 * 
 */
package com.zoubworld.java.utils.security;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Pierre Valleau
 *
 */
public class securityTest {

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
	public final void testPassWordChecker() {

		assertTrue(!PassWordChecker.checkPassWord("/*-+--/*/*--"));
		assertTrue(!PassWordChecker.checkPassWord("123456789012"));
		assertTrue(!PassWordChecker.checkPassWord("pierreeeeeee"));
		assertTrue(!PassWordChecker.checkPassWord("pierreeeeee0"));
		assertTrue(!PassWordChecker.checkPassWord("PIERREEEEEEE"));
		assertTrue(!PassWordChecker.checkPassWord("Pierre"));
		assertTrue(!PassWordChecker.checkPassWord("Pierre000000"));
		assertTrue(PassWordChecker.checkPassWord("PDuPont+000*"));
		assertTrue(!PassWordChecker.checkPassWord("P!erre000000"));
		assertTrue(!PassWordChecker.checkPassWord("Pierre00000!"));
		assertTrue(!PassWordChecker.checkPassWord("Pierre00000!"));

		assertTrue(PassWordChecker.checkPassWord("Pierre01234!"));

		assertTrue(!PassWordChecker.checkStrongPassWord("Dupont01234!"));
		assertTrue(!PassWordChecker.checkStrongPassWord("Pierre01234!"));
		assertTrue(PassWordChecker.checkStrongPassWord("P!erre01234i"));
		assertTrue(PassWordChecker.checkStrongPassWord("P!erre=3.141592"));

		assertTrue(!PassWordChecker.checkVeryStrongPassWord("P!erre01234i"));
		assertTrue(!PassWordChecker.checkVeryStrongPassWord("P!erre000000"));
		assertTrue(!PassWordChecker.checkVeryStrongPassWord("Pierre01234!"));
		assertTrue(!PassWordChecker.checkVeryStrongPassWord("P!er*e01234i"));
		assertTrue(!PassWordChecker.checkVeryStrongPassWord("Dµpont01234!"));
		assertTrue(PassWordChecker.checkVeryStrongPassWord("/*-$Po12yhbd"));
		assertTrue(PassWordChecker.checkVeryStrongPassWord("P!*r=3.141592"));

	}

}
