package com.zoubworld.java.utils.compress.algo;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;

public class NodeTreeTest {

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
	public final void testNodeTreeISymbol() {
		NodeTree node=new NodeTree(Symbol.findId('A'));
		assertEquals(Symbol.findId('A'),node.getSym());
	}

	@Test
	public final void testNodeTreeNodeTreeISymbol() {
		NodeTree nodep=new NodeTree(Symbol.findId('A'));
		NodeTree nodeB=new NodeTree(nodep,Symbol.findId('B'));
		
		assertEquals(Symbol.findId('B'),nodeB.getSym());
		assertEquals(nodep,nodeB.getParent());
		
	}

	@Test
	public final void testGetLeafs() {
		NodeTree root=new NodeTree();
		root.add(Symbol.from("ABC"));
		root.add(Symbol.from("ABD"));
		root.add(Symbol.from("ABE"));
		List<NodeTree> l = root.getLeafs(root);
		assertEquals(l.size(),3);
		
	}

	@Test
	public final void testIncCount() {
		NodeTree root=new NodeTree();
		assertEquals((long)root.getCount(),0L);
		root.IncCount();
		assertEquals((long)root.getCount(),1L);
		root.setCount(99L);
		assertEquals((long)root.getCount(),99L);
		
	}

	@Test
	public final void testGetSymPacked() {
		NodeTree root=new NodeTree();
		assertEquals(root.getSymPacked(),null);
		root.setSymPacked(Symbol.RLE);
		assertEquals(root.getSymPacked(),Symbol.RLE);
		
		
	}

	@Test
	public final void testIncPackedcount() {
		NodeTree root=new NodeTree();
		assertEquals(root.getPackedcount(),null);
		root.IncPackedcount();
		assertEquals((long)root.getPackedcount(),1L);
		root.setPackedcount(99L);
		assertEquals((long)root.getPackedcount(),99L);
	}

	@Test
	public final void testGetChild() {
		NodeTree root=new NodeTree();
		root.add(Symbol.from("ABC"));
		assertEquals(root.getChild().get(Symbol.findId('A')).getSym(),Symbol.findId('A'));
		assertEquals(root.getChild(Symbol.findId('A')).getSym(),Symbol.findId('A'));
		assertEquals(root.getChild(Symbol.findId('B')),null);
		
	}
	@Test
	public final void testrefactor() {
		NodeTree root=new NodeTree();
		root.add(Symbol.from("ABCDABC"));
		root.add(Symbol.from("ABCEABC"));
		ISymbol a=Symbol.findId('A');;
		ISymbol b=Symbol.findId('B');;
		ISymbol c=Symbol.findId('C');;
		ISymbol d=Symbol.findId('D');
		ISymbol e=Symbol.findId('E');
		NodeTree na = root.getChild(a);
		NodeTree nb = na.getChild(b);
		NodeTree nc = nb.getChild(c);
		NodeTree nd = nc.getChild(d);
		NodeTree ne = nc.getChild(e);
		assertEquals(2L,na.getCount().longValue());
		assertEquals(2L,nb.getCount().longValue());
		assertEquals(2L,nc.getCount().longValue());
		boolean bool = root.refactor(root,1);
		//abcdabc
		//abcdabc
		//become :
		//abc x2
		//dabc
		//eabc
		assertEquals(2L,(na=root.getChild(a)).getCount().longValue());
		assertEquals(2L,(nb=na.getChild(b)).getCount().longValue());
		assertEquals(2L,(nc=nb.getChild(c)).getCount().longValue());
		assertEquals(true,bool);
		assertEquals(1L,nd.getCount().longValue());
		assertEquals(1L,ne.getCount().longValue());
		assertEquals(null,nc.getChild(d));
		assertEquals(null,nc.getChild(e));
		assertNotEquals(null,nd=root.getChild(d));
		assertNotEquals(null,ne=root.getChild(e));
		
		
		bool = root.refactor(root,1);
		//abc x2
		//dabc
		//eabc
		//become :
		//abc x4
		//d
		//e
		assertEquals(4L,(na=root.getChild(a)).getCount().longValue());
		assertEquals(4L,(nb=na.getChild(b)).getCount().longValue());
		assertEquals(4L,(nc=nb.getChild(c)).getCount().longValue());
		assertEquals(true,bool);
		assertEquals(1L,nd.getCount().longValue());
		assertEquals(1L,ne.getCount().longValue());
		bool = root.refactor(root,1);
		assertEquals(false,bool);
	}
		
	 
	@Test
	public final void testaddT() {
		NodeTree root=new NodeTree();
		List<ISymbol> l = Symbol.from("ABCDABC");
		ISymbol a=l.get(0);
		ISymbol b=l.get(1);
		ISymbol c=l.get(2);
		ISymbol d=Symbol.findId('D');
		root.add(l);
		NodeTree na = root.getChild(a);
		NodeTree nb = na.getChild(b);
		NodeTree nc = nb.getChild(c);
		NodeTree nd = nc.getChild(d);
		root.add(nd.getChild(a));
		assertEquals(2L,na.getCount().longValue());
		assertEquals(2L,nb.getCount().longValue());
		assertEquals(2L,nc.getCount().longValue());
		assertEquals(1L,nd.getCount().longValue());
		
		
	}
	@Test
	public final void testadd() {
		NodeTree root=new NodeTree();
		List<ISymbol> l = Symbol.from("ABC");
		ISymbol a=l.get(0);
		ISymbol b=l.get(1);
		ISymbol c=l.get(2);
		ISymbol d=Symbol.findId('D');
		root.add(l);
		NodeTree na = root.getChild(a);
		NodeTree nb = na.getChild(b);
		NodeTree nc = nb.getChild(c);
		assertEquals(na.getSym(),Symbol.findId('A'));
		assertEquals(nb.getSym(),Symbol.findId('B'));
		assertEquals(nc.getSym(),Symbol.findId('C'));
		assertEquals(1L,root.getCount().longValue());
		assertEquals(1L,na.getCount().longValue());
		assertEquals(1L,nb.getCount().longValue());
		assertEquals(1L,nc.getCount().longValue());
		root.add(Symbol.from("ABD"));
		NodeTree nd = nb.getChild(d);
		assertEquals(nd.getSym(),d);
		assertEquals(2L,root.getCount().longValue());
		assertEquals(2L,na.getCount().longValue());
		assertEquals(2L,nb.getCount().longValue());
		assertEquals(1L,nc.getCount().longValue());
		assertEquals(1L,nd.getCount().longValue());
		
	}

	@Test
	public final void testGetParent() {
		NodeTree root=new NodeTree();
		root.add(Symbol.from("ABC"));
		assertEquals(root.getChild().get(Symbol.findId('A')).getParent(),root);
	}

	@Test
	public final void testGetSym() {
		NodeTree node=new NodeTree(Symbol.findId('A'));
		assertEquals(Symbol.findId('A'),node.getSym());
	}

}
