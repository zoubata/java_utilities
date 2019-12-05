package com.zoubworld.java.utils.compress.file;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zoubworld.java.utils.compress.CodingSet;
import com.zoubworld.java.utils.compress.Symbol;

public class BinaryTest {

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
	public final void testBinaryStdInOut() {
		File f1 = new File("res/result.test/binsmall.bin");		
		File f2 = new File("res/result.test/binsmall.bin");
		BinaryStdOut bo=new BinaryStdOut(f1.getAbsolutePath());
		bo.write(true);
		
		bo.write((byte) 0x45);
		bo.write((double)Math.PI);
		bo.write((float)3.14);
		bo.write((short) 1234);
		bo.write((int) 123456789);
		bo.write((long)0x123456789ABCDEFL);
		bo.write((int) 123456,24);
		bo.write((long)0x123456789ABCDEFL,64);
		bo.write((long)0x12345678,32);
		bo.write((long)0x123456789ABL,48);
		
		for(byte i=-128;i<127;i++)
		bo.write((byte)i);
		bo.write((char) '@', 7);
		bo.write("azertyuiop", 8);
		
		bo.close();
		
		BinaryStdIn bi=new BinaryStdIn(f2.getAbsolutePath());
		assertEquals(true, bi.readBoolean());
		assertEquals((byte)0x45, bi.readByte());
		assertEquals((double)Math.PI, bi.readDouble(),1/Math.pow(2, 50));
		assertEquals((float)3.14, bi.readFloat(),1/Math.pow(2, 22));
		assertEquals((short) 1234, bi.readShort());
		assertEquals((int) 123456789, bi.readInt());
		assertEquals((long)0x123456789ABCDEFL, bi.readLong());
		assertEquals((int) 123456, bi.readInt(24));
		
		assertEquals((long)0x123456789ABCDEFL, bi.readLong(64));
		assertEquals((long)0x12345678, bi.readLong(32));
		assertEquals((long)0x123456789ABL, bi.readLong(48));
		for(byte i=-128;i<127;i++)
		assertEquals((byte)i, bi.readByte());
		assertEquals((char) '@', bi.readChar(7));
        assertEquals("azertyuiop", bi.readString());
		 bi.close();
		 
		 f1 = new File("res/result.test/binsmall.bin");		
		 f2 = new File("res/result.test/binsmall.bin");
		  bo=new BinaryStdOut(f1);
		  for(byte i=1;i<=64;i++)
		  bo.write((long)(((long)i)| (1L<<(i-1L))),i); 
		  for(byte i=1;i<=32;i++)
			  bo.write((int)(i| (1L<<(i-1L))),i);
		  for(byte i=1;i<=8;i++)
			  bo.write((char)(i| (1L<<(i-1L))),i);
		  for(byte i=1;i<=64;i++)
			  bo.write((long)(-i),i); 
		  bo.write("azertyuiop");
		  bo.close();
		  
		  bi=new BinaryStdIn(f2);
		  for(byte i=1;i<=64;i++)
			  assertEquals((long)(((long)i)| (1L<<(((long)i)-1L))), bi.readLong(i));
		  for(byte i=1;i<=32;i++)
			  assertEquals((int)(i| (1<<(i-1))), bi.readInt(i));
		  for(byte i=1;i<=8;i++)
			  assertEquals((char)(i| (1<<(i-1))), bi.readChar(i));
		  for(long i=1;i<=63;i++)
		  {
		//	  System.out.println(" "+(long)(-i)+" "+(((1L)<<i)-1L)+"="+((long)(-i)&(((1L)<<i)-1L)));
			  assertEquals((long)(-i)&(((1L)<<i)-1), bi.readLong((byte)i));
		  }
		  assertEquals((long)(-64), bi.readLong((byte)64));
		  assertEquals("azertyuiop", bi.readString());
		  bi.close();
		  

		  
		  f1 = new File("res/result.test/binsmall.bin");		
			 f2 = new File("res/result.test/binsmall.bin");
			  CodingSet cs = new CodingSet(CodingSet.UNCOMPRESS);
			  bo=new BinaryStdOut(f1);
			  bo.setCodingRule(cs);
			  bo.write(cs.get(Symbol.findId('@')));
			  bo.write(Symbol.findId('9'));
			  
			  bo.close();
			  
			  bi=new BinaryStdIn(f2);
			   bi.setCodingRule(cs);
			  assertEquals(cs.get(Symbol.findId('@')), bi.readCode());
			  assertEquals(Symbol.findId('9'), bi.readSymbol());
			  
			  bi.close(); 
			  
			  
			  
			  

			  
			  f1 = new File("res/result.test/binsmall.bin");		
				 f2 = new File("res/result.test/binsmall.bin");
				  bo=new BinaryStdOut(f1);
				  bo.setCodingRule(cs=new CodingSet(CodingSet.NOCOMPRESS));
				  bo.write(cs.get(Symbol.findId('@')));
				  
				  bo.write(cs.get(Symbol.SOS));
				  bo.write(Symbol.findId('9'));
				  bo.write(Symbol.PATr);
				  
				  bo.close();
				  
				  bi=new BinaryStdIn(f2);
				  bi.setCodingRule(new CodingSet(CodingSet.NOCOMPRESS));
				  assertEquals(cs.get(Symbol.findId('@')), bi.readCode());
				  
				  assertEquals(cs.get(Symbol.SOS), bi.readCode());
				  assertEquals(Symbol.findId('9'), bi.readSymbol());
				  assertEquals(Symbol.PATr, bi.readSymbol());
				  assertNotEquals(Symbol.SOS, Symbol.PATr);
				  assertNotEquals(cs.get(Symbol.SOS), cs.get(Symbol.PATr));
				  
				  bi.close(); 
				  
				  
				  
				  
				  
		  
	}

}
