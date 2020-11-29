package com.zoubworld.java.utils.compress.algo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;

class TreeTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}


	@Test
	final void testFind() {

		Tree<ISymbol> tree=new Tree<ISymbol>() ;
		List<ISymbol> ls = Symbol.from("ABCABCABC");
		tree.fullfill(ls, 3);
		Tree<ISymbol> t;
		System.out.println(t=tree.find(ls));
		assertEquals("'A''B''C'",tree.find(ls).toString());
		assertEquals(2,t.getIndex());
		assertEquals(3,t.getCount());
		assertEquals("'B''C''A'",(t=tree.find(1,ls)).toString());
		assertEquals(3,t.getIndex());
		assertEquals(3,t.getDeep());
		assertEquals(2,t.getCount());

		
	}

	@Test
	final void testNbLeaf() {
		Tree<ISymbol> tree=new Tree<ISymbol>() ;
		List<ISymbol> ls = Symbol.from("ABCABCABC");
		tree.fullfill(ls, 3);
		System.out.println(tree.NbLeaf());
		assertEquals(3,tree.NbLeaf());
	}

	@Test
	final void testLeafs() {
		Tree<ISymbol> tree=new Tree<ISymbol>() ;
		List<ISymbol> ls = Symbol.from("ABCABCABC");
		tree.fullfill(ls, 3);
		System.out.println(tree.Leafs());
		assertEquals("['A''B''C', 'C''A''B', 'B''C''A']",tree.Leafs().toString());
	}

	@Test
	final void testSize() {
		Tree<ISymbol> tree=new Tree<ISymbol>() ;
		List<ISymbol> ls = Symbol.from("ABCABCABC");
		tree.fullfill(ls, 3);
		System.out.println(tree.toStringAll());
		assertEquals(10,tree.size());
	}

	@Test
	final void testFullfill() {
		Tree<ISymbol> tree=new Tree<ISymbol>() ;
		List<ISymbol> ls = Symbol.from("ABCABCABCAABBCCABCABCABCAABBCC");
		tree.fullfill(ls, 1);
		System.out.println(tree.toStringAll());
		assertEquals(":30\r\n" + 
				"'A':10\r\n" + 
				"'C':10\r\n" + 
				"'B':10\r\n", tree.toStringAll());
		tree=new Tree<ISymbol>() ;
		ls = Symbol.from("ABCABCABC");
		tree.fullfill(ls, 3);

		System.out.println(tree.toStringAll());
		assertEquals(":9\r\n" + 
				"'A':5\r\n" + 
				"'A''B':4\r\n" + 
				"'A''B''C':3\r\n" + 
				"'C':2\r\n" + 
				"'C''A':2\r\n" + 
				"'C''A''B':2\r\n" + 
				"'B':2\r\n" + 
				"'B''C':2\r\n" + 
				"'B''C''A':2\r\n", tree.toStringAll());
	}

	@Test
	final void testFillIntListOfTInt() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testCleanup() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testGetHeavy() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testGetHeavyTreeOfT() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testGetHeavy2() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testFillIntIntListOfT() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testToString() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testFactorize() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	final void testReturnTree() {
		Tree<ISymbol> tree=new Tree<ISymbol>() ;
		List<ISymbol> ls = Symbol.from("ABCABCABC");
		tree.fullfill(ls, 3);
		tree=tree.returnTree();
		System.out.println(tree.toStringAll());
		assertEquals(":10\r\n" + 
				"'A':8\r\n" + 
				"'A''C':2\r\n" + 
				"'A''C''B':2\r\n" + 
				"'C':7\r\n" + 
				"'C''B':4\r\n" + 
				"'C''B''A':5\r\n" + 
				"'B':8\r\n" + 
				"'B''A':2\r\n" + 
				"'B''A''C':2\r\n",tree.toStringAll());
	}

	@Test
	final void testGetChild() {
		Tree<ISymbol> tree=new Tree<ISymbol>() ;
		List<ISymbol> ls = Symbol.from("ABCABCABC");
		tree.fullfill(ls, 3);
		Tree<ISymbol> t=tree.getChild(Symbol.findId('A'));
		t=t.getChild(Symbol.findId('B'));
		t=t.getChild(Symbol.findId('C'));
		System.out.println(t.toString());
		assertEquals("'A''B''C'",t.toString());
		assertEquals(3,t.getCount());
		assertEquals(2,t.getIndex());		
	}
	
	@Test
	final void testgetNodesAtDeep() {
		Tree<ISymbol> tree=new Tree<ISymbol>() ;
		List<ISymbol> ls = Symbol.from("ABCABCABC");
		tree.fullfill(ls, 2);
		System.out.println(tree.getNodesAtDeep(0));
		System.out.println(tree.getNodesAtDeep(1));
		System.out.println(tree.getNodesAtDeep(2));
		assertEquals("[]",tree.getNodesAtDeep(0).toString());
		assertEquals("['A', 'C', 'B']",tree.getNodesAtDeep(1).toString());
		assertEquals("['A''B', 'C''A', 'B''C']",tree.getNodesAtDeep(2).toString());
		}
	
	@Test
	final void testFreq() {
		Tree<ISymbol> tree=new Tree<ISymbol>() ;
		List<ISymbol> ls = Symbol.from("ABCABCABC");
		tree.fullfill(ls, 2);
		System.out.println(tree.Freq());
		System.out.println(tree.Freq(1));
		System.out.println(tree.Freq(2));
		assertEquals("{'A'=4, 'C'=2, 'B'=3}",tree.Freq().toString());
		assertEquals("{['C']=2, ['B']=3, ['A']=4}",tree.Freq(1).toString());
		assertEquals("{['B', 'C']=3, ['A', 'B']=3, ['C', 'A']=2}",tree.Freq(2).toString());
		}
	
	
	@Test
	final void testToStringAll() {
		Tree<ISymbol> tree=new Tree<ISymbol>() ;
		List<ISymbol> ls = Symbol.from("ABCABCABC");
		tree.fullfill(ls, 2);
		System.out.println(tree.toStringAll());
		assertEquals(":9\r\n" + 
				"'A':4\r\n" + 
				"'A''B':3\r\n" + 
				"'C':2\r\n" + 
				"'C''A':2\r\n" + 
				"'B':3\r\n" + 
				"'B''C':3\r\n",tree.toStringAll());
	}

	@Test
	final void testMain() {
		fail("Not yet implemented"); // TODO
	}

}
