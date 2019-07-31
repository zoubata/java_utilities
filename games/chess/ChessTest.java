package com.zoubworld.games.chess;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zoubworld.games.IPart;
import com.zoubworld.games.Location;
import com.zoubworld.games.chess.parts.Bishop;
import com.zoubworld.games.chess.parts.King;
import com.zoubworld.games.chess.parts.Knight;
import com.zoubworld.games.chess.parts.Pown;
import com.zoubworld.games.chess.parts.Queen;
import com.zoubworld.games.chess.parts.Rook;

public class ChessTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetpart() {
	//	fail("Not yet implemented");
	}

	@Test
	public void testInit() {
	//	fail("Not yet implemented");
	}

	@Test
	public void testMoveString() {
	//	fail("Not yet implemented");
	}

	@Test
	public void testMoveStringString() {
	//	fail("Not yet implemented");
	}
	@Test
	public void testRook() {
	BoardChess bc= new BoardChess();
	bc.init();
	bc.clear();
	IPart p=new Rook('W');
	bc.put(new Location(4,4),p);
	p.setBoard(bc);
	System.out.println("Rook"+p.getMoves());

	System.out.println(bc.toString(p.getMoves()));
	assertEquals(8+8-2, p.getMoves().size());
	}


	@Test
	public void testQueen() {
	BoardChess bc= new BoardChess();
	bc.init();
	bc.clear();
	IPart p=new Queen('W');
	bc.put(new Location(4,4),p);
	p.setBoard(bc);
	System.out.println("Queen"+p.getMoves());
	System.out.println(bc.toString(p.getMoves()));
	assertEquals(27, p.getMoves().size());
	}
	

	@Test
	public void testBishop() {
	BoardChess bc= new BoardChess();
	bc.init();
	bc.clear();
	IPart p=new Bishop('W');
	bc.put(new Location(4,4),p);
	p.setBoard(bc);
	System.out.println("Bishop"+p.getMoves());

	System.out.println(bc.toString(p.getMoves()));
	assertEquals(8+8-3, p.getMoves().size());
	}

	@Test
	public void testPown() {
	BoardChess bc= new BoardChess();
	bc.init();
	bc.clear();
	IPart p=new Pown('B');
	bc.put(new Location(4,4),p);
	p.setBoard(bc);
	assertEquals(1, p.getMoves().size());

	bc.clear();
	bc.put(new Location(4,6),p);
	
	System.out.println("Pown"+p.getMoves());
	System.out.println(bc.toString(p.getMoves()));

	assertEquals(1, p.getMoves().size());
	p=new Pown('W');
	bc.put(new Location(4,6),p);
	p.setBoard(bc);
	

	assertEquals(2, p.getMoves().size());
	
	IPart p1=new Pown('W');
	bc.put(new Location(4,5),p1);
	p1.setBoard(bc);
	System.out.println("Pown"+p.getMoves());
	System.out.println(bc.toString(p.getMoves()));
	assertEquals(0, p.getMoves().size());
	assertEquals(1, p1.getMoves().size());
	}
	
	@Test
	public void testKing() {
	BoardChess bc= new BoardChess();
	bc.init();
	bc.clear();
	IPart p=new King('W');
	bc.put(new Location(4,4),p);
	p.setBoard(bc);
	System.out.println("King"+p.getMoves());
	System.out.println(bc.toString(p.getMoves()));
	assertEquals(8, p.getMoves().size());
	}
	@Test
	public void testKnight() {
	BoardChess bc= new BoardChess();
	bc.init();
	bc.clear();
	IPart p=new Knight('W');
	bc.put(new Location(4,4),p);
	p.setBoard(bc);
	System.out.println("Knight"+p.getMoves());

	System.out.println(bc.toString(p.getMoves()));
	assertEquals(8, p.getMoves().size());
	}
}
