package com.zoubworld.games.dames;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zoubworld.games.ILocation;
import com.zoubworld.games.IPart;
import com.zoubworld.games.Location;
import com.zoubworld.games.dames.part.Pown;
import com.zoubworld.utils.JavaUtilList;
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
		/*
			//JavaUtilList.listToSet(collection)
			{
				String s3=JavaUtils.asSortedList((bd.getpart("D2")).getMoves()).toString();
								String s4="[D2E1, D2E3]";
		//		System.out.println("'"+s3+"'");
				assertEquals("move C1", s3.trim(),( s4.trim()));
				}*/
		for(List<ILocation> m:bd.getpart("D2").getMoves())
		{
			ILocation l=m.get(0);
			assertEquals("d2",l.getSLoc(),"D2");
			assertEquals("d2",l.getZ(),null);
			assertEquals("d2",l.toString(),"D2");
			
		}
		
		Location l=new Location();
		ILocation il=l.Factory(0, 0, null);
		assertEquals("A1",il.toString(),"A1");
		
			
	}

	@Test
	public final void testIsMoveAllow() {
		{
			String s3=(bd.getpart("C1")).getMoves().toString();
			String s4="[]";
			ILocation l=new Location();
			assertEquals(s4,s3);
			
			System.out.println("'"+s3+"'");
			assertTrue("move C1D2", !(bd.getpart("C1")).isMoveAllow(l.parseMove("C1D2")));
			assertTrue("move D2E1", (bd.getpart("D2")).isMoveAllow(l.parseMove("D2E1")));
			
			}
			/*
			{
				String s3=JavaUtils.asSortedList((bd.getpart("D2")).getMoves()).toString();
				String s4="[D2E1, D2E3]";
				System.out.println("'"+s3+"'");
				assertEquals("move C1", s3.trim(),( s4.trim()));
				}*/
	}

	@Test
	public final void testIsEatAllow() {
//		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testToString() {
	//	fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetTeam() {
	//	fail("Not yet implemented"); // TODO
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
		bd.init();
		Location l=new Location();
		ILocation d2=l.Factory(1, 3, 0);
		ILocation c1=l.Factory(0, 2, 0);
		ILocation e1=l.Factory(0, 4, 0);
		
		assertEquals(e1.getSLoc(),"E1");
		
		assertTrue(bd.getPart(d2)!=null);
		assertTrue(bd.getPart(e1)==null);
		

		IPart p=new Pown('?');
		assertEquals(bd.getLoc(p),null);		
		 p=bd.getPart(d2);
		 assertEquals(bd.getLoc(p).getSLoc(),"D2");
		 
		 
		assertTrue(bd.isMoveAllow(d2,e1));
		assertTrue(bd.move(d2.getSLoc()+e1.getSLoc()));
		
		
		assertFalse(bd.move((String)null));
		assertFalse(bd.move(d2.getSLoc()));
		
		

			
		bd.put(e1, p);
		bd.put(d2, null);
		assertEquals(bd.getPart(d2),null);
		assertEquals(bd.getPart(e1),p);
		
	}

	@Test
	public final void testSetBoard() {
	//	fail("Not yet implemented"); // TODO
	}

}
