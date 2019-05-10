package com.zoubworld.java.utils.compress.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import org.junit.Test;

import com.zoubworld.java.utils.compress.Code;
import com.zoubworld.java.utils.compress.ICode;

public class CodeTest {

	@Test
	public void testToCode() {
		ICode c=new Code((char) 0x12);		
		assertEquals(c.toCode().length,1);
		assertEquals(c.toCode()[0],(char) 0x12);		
	}
	
	@Test
	public void testDummy() {
	Code.main(null);
	assertEquals(1,1);
	}
	@Test
	public void testToRaw() {
		Code c=new Code((char) 0x12);		
		assertEquals("00010010",c.toRaw());
		c.trim();
		assertEquals("10010",c.toRaw());
		
		 c=new Code((char) 0x1);	
		 assertEquals("00000001",c.toRaw());
		 c.trim();
		 assertEquals("1",c.toRaw());
		 c=new Code((char) 0x80);	
		 assertEquals("10000000",c.toRaw());
		 c.trim();
		 assertEquals("10000000",c.toRaw());
		 c=new Code((short) 0x280);
		 assertEquals("0000001010000000",c.toRaw());
		 
	}
/*
	@Test
public void testToRaw() {
		fail("Not yet implemented");
	}

	@Test
public void testToString() {
		fail("Not yet implemented");
	}
*/
	@Test
	public void testCodeChar() {
		ICode c=new Code((char) 0x12);		
		assertEquals(c.toCode().length,1);
		assertEquals(c.toCode()[0],(char) 0x12);		
	}

	@Test
	public void testCodeShort() {
		ICode c=new Code((short) 0x1234);		
		assertEquals(c.toCode().length,2);
		assertEquals(c.toCode()[0],(char) 0x12);	
		assertEquals(c.toCode()[1],(char) 0x34);	
		
	}

	@Test
	public void testCodeInt() {
		ICode c=new Code((int) 0x12345678);		
		assertEquals(c.toCode().length,4);
		assertEquals(c.toCode()[0],(char) 0x12);	
		assertEquals(c.toCode()[1],(char) 0x34);	
		assertEquals(c.toCode()[2],(char) 0x56);	
		assertEquals(c.toCode()[3],(char) 0x78);	
		
	}
	@Test
	public void testCodeString() {
		Code c=new Code("00001101");
		Code d=new Code((byte)0x0d );
		assertEquals(d.toRaw(),c.toRaw());	
		assertEquals(d.getLong(),c.getLong());	
		assertEquals(d.length(),c.length());	
		assertTrue(d.compareToCode(c)==0);	
		
		
	}

	@Test
	public void testCodeLong() {
		ICode c=new Code((long) 0x123456789abcdef0L);		
		assertEquals(c.toCode().length,8);
		assertEquals(c.toCode()[0],(char) 0x12);	
		assertEquals(c.toCode()[1],(char) 0x34);	
		assertEquals(c.toCode()[2],(char) 0x56);	
		assertEquals(c.toCode()[3],(char) 0x78);
		
		
		assertEquals(c.toCode()[4],(char) 0x9a);	
		assertEquals(c.toCode()[5],(char) 0xbc);	
		assertEquals(c.toCode()[6],(char) 0xde);	
		assertEquals(c.toCode()[7],(char) 0xf0);
		
		c=new Code((long) 0x123456789abcdef0L,64);		
		assertEquals(c.toCode().length,8);
		assertEquals(c.toCode()[0],(char) 0x12);	
		assertEquals(c.toCode()[1],(char) 0x34);	
		assertEquals(c.toCode()[2],(char) 0x56);	
		assertEquals(c.toCode()[3],(char) 0x78);
		
		
		assertEquals(c.toCode()[4],(char) 0x9a);	
		assertEquals(c.toCode()[5],(char) 0xbc);	
		assertEquals(c.toCode()[6],(char) 0xde);	
		assertEquals(c.toCode()[7],(char) 0xf0);
		
		
		c=new Code((long) 0x1L,1);		
		assertEquals(c.toCode().length,1);
		assertEquals(c.toCode()[0],(char) 0x80);
		c=new Code((long) 0x71L,8);		
		assertEquals(c.toCode().length,1);
		assertEquals(c.toCode()[0],(char) 0x71);
		
		c=new Code((long) 0x71L,7);		
		assertEquals(c.toCode().length,1);
		assertEquals(c.toCode()[0],(char) 0x71*2);
		c=new Code((long) 0x177L,9);		
		assertEquals(c.toCode().length,2);
		
		
		
	}

	@Test
	public void testTrim() {
		
		Code c=new Code((long) 0x082345678L);
		c.trim();
		assertEquals(c.length(),4*8);
		assertEquals(c.toCode()[0],(char) 0x82);	
		assertEquals(c.toCode()[1],(char) 0x34);	
		assertEquals(c.toCode()[2],(char) 0x56);	
		assertEquals(c.toCode()[3],(char) 0x78);	
		
		Code a=new Code((char) 0x01);
		a.trim();
		assertEquals(a.length(),1);
		assertEquals(a.toCode()[0],(char) 0x80);	
		
		Code b=new Code((short) 0x0123);//0000 0001 0010 0011
		b.trim();///1 0010 0011 = 9 1 8
		assertEquals(b.length(),9);
		assertEquals(b.toCode()[0],(char) 0x91);
		assertEquals(b.toCode()[1]&0x80,(char) 0x80);//1 bit used
		
		
		Code d=new Code();
		
		d.huffmanAddBit('0');
		d.huffmanAddBit('1');
		d.huffmanAddBit('1');
		assertEquals(d.length(),3);
		assertEquals(d.getLong().longValue(),3);
		d.trim();

		assertEquals(d.length(),2);
		assertEquals(d.getLong().longValue(),3);
		

		Code de=new Code();
		
		de.huffmanAddBit('0');
		de.huffmanAddBit('0');
		de.huffmanAddBit('1');
		de.huffmanAddBit('1');
		de.huffmanAddBit('0');
		de.huffmanAddBit('0');
		de.huffmanAddBit('1');
		assertEquals(de.length(),7);
		assertEquals(de.getLong().longValue(),0x19);
		de.trim();

		assertEquals(de.length(),5);
		assertEquals(de.getLong().longValue(),0x19);
		
		
	}
	

	@Test
	public void testCompareToInt() {
		
		Long A=0x123456789abcdef0L;
		Long B=0x123456789abcdefL;
		
		Code a=new Code((long) A);	
		Code b=new Code((long) B);	
		a.trim();
		b.trim();
		assertTrue(A.compareTo(B)>0);//a>b
	 	assertTrue(a.compareToInt(b)>0);//a>b
		}

	@Test
	public void testCompareToCode() {
		Code a=new Code((long) 0x123456789abcdef0L);	
		Code b=new Code((long) 0x123456789abcdefL);	
	 	assertTrue(a.compareToCode(b)>0);//a>b
	 	Code c=new Code((long) 0x123456789abcdefL);	
		c.trim();
		assertTrue(c.compareToCode(b)>0);//a>b
		
		
		
		Code d=new Code();
		Code e=new Code();
		d.huffmanAddBit('1');
		
		e.huffmanAddBit('1');
		assertTrue(d.compareToCode(e)==0);//a>b
		e.huffmanAddBit('0');
		assertTrue(d.compareToCode(e)<0);//a>b

		 d=new Code();
		 e=new Code();
		d.huffmanAddBit('0');
		d.huffmanAddBit('1');
		d.huffmanAddBit('1');
		
		 e.huffmanAddBit('1');
		assertTrue(d.compareToCode(e)<0);//a>b

		
	 	
	}
	@Test
	public void testGetLong() {
		long D=(long)1234567890123L;//0x11F71FB04CB
		Code d=new Code(D);
		
		assertTrue(d.getLong()==D);
		assertTrue(d.length()==64);
		d.trim();
		assertTrue(d.getLong()==D);
		assertTrue(d.length()==41);
		Code a=new Code();

		assertTrue(a.getLong()==0);
		a.huffmanAddBit('1');
		a.huffmanAddBit('0');
		
		assertTrue(a.getLong()==2);
		assertTrue(a.length()==2);
		
		
		
		Code c=new Code((char) 0x12);		
		assertEquals(0x12,c.getLong().longValue());
		c.trim();
		assertEquals(0x12,c.getLong().longValue());
		
		 c=new Code((char) 0x1);	
		 assertEquals(0x1,c.getLong().longValue());
		 c.trim();
		 assertEquals(1,c.getLong().longValue());
		 c=new Code((char) 0x80);	
		 assertEquals(0x80,c.getLong().longValue());
		 c.trim();
		 assertEquals(0x80,c.getLong().longValue());
		 c=new Code((short) 0x280);
		 assertEquals(0x280,c.getLong().longValue());
	}
	/*
	@Test
	void testGetMsb() {
		fail("Not yet implemented");
	}

	@Test
	void testCompareToCode() {
		fail("Not yet implemented");
	}
*/
	

	
	 	     
}
