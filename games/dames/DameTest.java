package com.zoubworld.games.dames;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zoubworld.games.Location;
import com.zoubworld.utils.JavaUtils;

public class DameTest {
	BoardDames bd=null;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		bd= new BoardDames();
		bd.init();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testGetMoves() {
		{
			String s3=(bd.getpart("C1")).getMoves().toString();
			String s4="[]";
	//		System.out.println("'"+s3+"'");
			assertEquals("move C1", s3.trim(),( s4.trim()));
			}
			
			{
				String s3=JavaUtils.asSortedList((bd.getpart("D2")).getMoves()).toString();
				String s4="[D2E1, D2E3]";
		//		System.out.println("'"+s3+"'");
				assertEquals("move C1", s3.trim(),( s4.trim()));
				}
	}

	@Test
	public final void testIsMoveAllow() {
		{
			String s3=(bd.getpart("C1")).getMoves().toString();
			String s4="[]";
			ILocation l=new Location();
			
			System.out.println("'"+s3+"'");
			assertTrue("move C1D2", (bd.getpart("C1")).isMoveAllow(l.parseMove("C1D2")));
			}
			
			{
				String s3=JavaUtils.asSortedList((bd.getpart("D2")).getMoves()).toString();
				String s4="[D2E1, D2E3]";
				System.out.println("'"+s3+"'");
				assertEquals("move C1", s3.trim(),( s4.trim()));
				}
	}

	@Test
	public final void testIsEatAllow() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testToString() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetTeam() {
		fail("Not yet implemented"); // TODO
	}
	@Test
	public final void testBoardInit() {
	
		bd.init();
		String s1=bd.toString();
		String s2="J--Bp--Bp--Bp--Bp--Bp\r\n" + 
				"IBp--Bp--Bp--Bp--Bp--\r\n" + 
				"H--Bp--Bp--Bp--Bp--Bp\r\n" + 
				"GBp--Bp--Bp--Bp--Bp--\r\n" + 
				"F--  --  --  --  --  \r\n" + 
				"E  --  --  --  --  --\r\n" + 
				"D--Wp--Wp--Wp--Wp--Wp\r\n" + 
				"CWp--Wp--Wp--Wp--Wp--\r\n" + 
				"B--Wp--Wp--Wp--Wp--Wp\r\n" + 
				"AWp--Wp--Wp--Wp--Wp--\r\n" + 
				" 1 2 3 4 5 6 7 8 9 1\r\n" + 
				"                   0\r\n" ;
						
		assertEquals("init", s1.trim(),( s2.trim()));
		
		
	}

	@Test
	public final void testGetBoard() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSetBoard() {
		fail("Not yet implemented"); // TODO
	}

}
