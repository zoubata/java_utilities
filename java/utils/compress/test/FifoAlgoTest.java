package com.zoubworld.java.utils.compress.test;


import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;


import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Number;
import com.zoubworld.java.utils.compress.blockSorting.FifoAlgo;

public class FifoAlgoTest {

	@Test
	public  void testFifoAlgoLong() {
		long d2[]= {21341564210L, 33, 33, 33, 33, 9656, 33, 
				33, 33, 9656, 33, 9656, 78943104, 33, 33, 33, 33, 33, 
				33, 78943104, 33, 33, 33, 33, 123456, 33, 33, 31};
		
		test(Number.from(d2),0.8);
		}

	@Test
	public  void testDecodeSymbol() {
		long d[]={8589934591L, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1024, 
				1024, 1024, 0, 1024, 1024, 1024, 0, 17408, 0, 0, 0,
				0, 0, 512, 512, 0, 0};
	
		test(Number.from(d),0.6);
		
		
	}
void test(List<ISymbol> ld,double r)
{
	 
	FifoAlgo fifo=new FifoAlgo();
	List<ISymbol> le = fifo.encodeSymbol(ld);
	System.out.println(ld.size()*8+" ; "+fifo.bitlen(ld)+":"+ld);
	System.out.println(""+fifo.bitlen(le)+":"+le);
	fifo=new FifoAlgo();
	System.out.println("r "+fifo.bitlen(le)/fifo.bitlen(ld) );
	assertTrue(fifo.bitlen(le)<fifo.bitlen(ld)*r);
	List<ISymbol>  ldec = fifo.decodeSymbol(le);
//	System.out.println(ldec.equals(ld)+":"+ldec);
	assertEquals(ld, ldec);
	}
	@Test
	public  void testEncodeSymbol() {
		long d2[]= {33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 31};
		
		test(Number.from(d2),0.8);
		
	}

	@Test
	public  void testGetName() {
		fail("Not yet implemented"); // TODO
	}

}
