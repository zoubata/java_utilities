package com.zoubworld.java.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ListBeginEndTest {

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
	public void Performance(List<String> l)
	{
		int size=100000;
		String RES="ns";double Res=1.0;
		
		List<String> a = Arrays.asList("a", "b", "c","d", "e", "f","g", "h", "i", "j", "k","l","m","n","o","p");
		long start=System.nanoTime();
		for(int i=0;i<size;i++)
		l.add(a.get(i%16));
		long add=System.nanoTime()-start;
		System.out.println(l.getClass()+" add : "+String.format("%3.1f", add*1000000.0/size)+ RES);
		
		start=System.nanoTime();
		for(int i=0;i<size;i++)
			l.add(0,a.get(i%16));
		long add0=System.nanoTime()-start;
		System.out.println(l.getClass()+" add0 : "+String.format("%3.1f", add0*Res/size)+ RES);
		
		start=System.nanoTime();
		for(int i=0;i<size;i++)
			l.add(size,a.get(i%16));
		long addN=System.nanoTime()-start;
		System.out.println(l.getClass()+" addN : "+String.format("%3.1f", addN*Res/size)+ RES);
		
		
		start=System.nanoTime();
		for(int i=0;i<size;i++)
			l.subList(size, size*2);
		long subList=System.nanoTime()-start;
		System.out.println(l.getClass()+" subList : "+String.format("%3.1f", subList*Res/size)+ RES);
		
		
		start=System.nanoTime();
		for(int i=0;i<size;i++)
			l.remove(0);
		long remove0=System.nanoTime()-start;
		System.out.println(l.getClass()+" remove0 : "+String.format("%3.1f", remove0*Res/size)+ RES);
		
		start=System.nanoTime();
		for(int i=0;i<size;i++)
			l.size();
		long lsize=System.nanoTime()-start;
		System.out.println(l.getClass()+" lsize : "+String.format("%3.1f", lsize*Res/size)+ RES);
		
		start=System.nanoTime();
		for(int i=0;i<size;i++)
			l.remove(size);
		long removeN=System.nanoTime()-start-size;
		System.out.println(l.getClass()+" removeN : "+String.format("%3.1f", removeN*Res/size)+ RES);
		
		start=System.nanoTime();
		for(int i=0;i<size;i++)
			l.remove(l.size()-1);
		long removeE=System.nanoTime()-start-size;
		System.out.println(l.getClass()+" removeE : "+String.format("%3.1f", removeE*Res/size)+ RES);
		
		
	}
	
	  @Test
	    public void testListBasic() {

	        List<String> a0 = Arrays.asList("a", "b", "c");
	        List<String> b0 = Arrays.asList("a", "b", "c");
	        List<String> c0 = Arrays.asList("d", "e", "f");
	        List<String> d0 = Arrays.asList("g", "h", "i", "j", "k","l");
	        List<String> e0 = Arrays.asList("g", "h", "i");
	        List<String> d00=new ArrayList<String>();
	        d00.addAll(d0);
	        List<String> d1=new ListBeginEnd<String>(d00);
	        List<String> e1=d1.subList(0, 3);
	        List<String> e2=new ListBeginEnd<String>(e0);
		       assertEquals(e1.size(), e0.size());
	        assertEquals(e1, e0);
	        assertArrayEquals(e1.toArray(), e0.toArray());
	        assertEquals(e1.subList(1,2), e0.subList(1,2));
			        assertEquals(e1.contains("g"), e0.contains("g"));
	        assertEquals(e1.contains("b"), e0.contains("b"));
	        assertEquals(e1.get(0), e0.get(0));
	        assertEquals(e1.get(2), e0.get(2));
	    	        assertEquals(e1, e2);
	    	        
 //           assertEquals(e1.hashCode(), e2.hashCode());
	
	        
	        List<String> bc=new ArrayList<String>();
	        bc.addAll(b0);
	        bc.addAll(c0);
	        List<String> a1=new ListBeginEnd<String>(a0);
	        ListBeginEnd<String> b1=new ListBeginEnd<String>();
	        b1.addAll(b0);
	        List<String> c1=new ListBeginEnd<String>();
	        c1.addAll(c0);
	        assertEquals(a1.size(), a0.size());
	        assertEquals(b0.size(), b1.size());
	        assertEquals(a1, a0);
	        assertEquals(b0, b1);
	        assertEquals(a1, b1);
	        assertNotEquals(a1, c1);
	        assertNotEquals(c1,b1);
	        
	        List<String> bc1=new ListBeginEnd<String>();
	        bc1.addAll(b1);
	        assertEquals(bc1, b1);
	        assertEquals(bc1, b0);
	        bc1.addAll(c1);
	        assertEquals(bc1, bc);
	        assertEquals(bc, bc1);
	        
	        List<String> e0c=new ArrayList<String>();
	        e0c.addAll(e0);
	        assertEquals(e1, e0c);
	        assertEquals(e1.get(2), e0c.get(2));
	        assertEquals(e1.remove(2), e0c.remove(2));

	        assertEquals(e1.remove(0), e0c.remove(0));
	        assertEquals(e1, e0c);
	        e0c.add("z");
	        e1.add("z");
	        assertEquals(e1, e0c);
	    	
	    }
	@Test
	public final void test() {
		
		Performance(new ArrayList<String>());
		Performance(new ListBeginEnd<String>());
		
		fail("Not yet implemented"); // TODO
	}

}
