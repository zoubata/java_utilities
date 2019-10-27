package com.zoubworld.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zoubworld.java.utils.compress.file.Ecc;

public class TestEcc {

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
	public final void test() {
		Ecc ecc=new Ecc();
		byte[] word=new byte[4];
		word[0]=0;
		word[1]=0;
		word[2]=0;
		word[3]=0;
		
		byte[]  e=ecc.getECC(word);
		byte e1=ecc.getParity(word);
		 assertEquals(0, e1);
			word[0]=(byte)0x35;
			word[1]=(byte)0x21;
			word[2]=(byte)0x6A;
			word[3]=(byte)0x18;
		    e1=ecc.getParity(word);
		System.out.println(" "+word[0]+","+word[1]+","+word[2]+","+word[3]+"=>"+e1);
		assertEquals(0, e1);
		word[0]=(byte)0x63;
		word[1]=(byte)0x51;
		word[2]=(byte)0x75;
		word[3]=(byte)0x10;
		e1=ecc.getParity(word);
		assertEquals(1, e1);
		
	byte ecc1[]=ecc.getECC(word);
	byte ref[]=new byte[4];
	ref[0]=(byte)0x63;
	ref[1]=(byte)0x51;
	ref[2]=(byte)0x75;
	ref[3]=(byte)0x10;
	
	assertArrayEquals(ref,ecc.getdata(word, ecc1));
	word[0]^=0x01;
	assertArrayEquals(ref,ecc.getdata(word, ecc1));
	word[0]=ref[0];
	word[1]^=0x02;
	assertArrayEquals(ref,ecc.getdata(word, ecc1));
	word[2]^=0x04;
	word[1]=ref[1];
	assertArrayEquals(ref,ecc.getdata(word, ecc1));
	word[2]=ref[2];
	word[3]^=0x08;
	assertArrayEquals(ref,ecc.getdata(word, ecc1));

	

	assertArrayEquals(ref,ecc.getdata(word, ecc1));
	word[0]^=0x10;
	word[3]=ref[3];
	assertArrayEquals(ref,ecc.getdata(word, ecc1));
	word[0]=ref[0];
	word[1]^=0x20;
	assertArrayEquals(ref,ecc.getdata(word, ecc1));
	word[2]^=0x40;
	word[1]=ref[1];
	assertArrayEquals(ref,ecc.getdata(word, ecc1));
	word[2]=ref[2];
	word[3]^=0x80;
	assertArrayEquals(ref,ecc.getdata(word, ecc1));
	word[3]=(byte) (ref[3]^0x11);
	assertEquals(null,(ecc.getdata(word, ecc1)));

	}
	@Test
	public final void test2() {
		Ecc ecc=new Ecc();
		byte[] word=new byte[8];
		

	word[0]=(byte)0x0;
	word[1]=(byte)0x0;
	word[2]=(byte)0x0;
	word[3]=(byte)0x0;
	word[4]=(byte)0x0;
	word[5]=(byte)0x0;
	word[6]=(byte)0x0;
	word[7]=(byte)0x0;
	
	assertEquals(ecc.getParity(word),0);
	for(int i=0;i<64;i++)
	{
		word[0]=(byte)((1L<<i)&0xff);
	if (i>=8)
	word[1]=(byte)((1L<<(i-8))&0xff);
	if (i>=16)
		word[2]=(byte)((1L<<(i-16))&0xff);
	if (i>=24)
		word[3]=(byte)((1L<<(i-24))&0xff);
	if (i>=32)
		word[4]=(byte)((1L<<(i-32))&0xff);
	if (i>=40)
		word[5]=(byte)((1L<<(i-40))&0xff);
	if (i>=48)
		word[6]=(byte)((1L<<(i-48))&0xff);
	if (i>=56)
		word[7]=(byte)((1L<<(i-56))&0xff);
	
	System.out.println(i+" "+word[0]+","+word[1]+","+word[2]+","+word[3]+","+word[4]+","+word[5]+","+word[6]+","+word[7]+"=>"+(short)((ecc.getECC(word)[0]>>1)&0xff));

	assertEquals(i,( ecc.getECC(word)[0])>>1);// ecc>>1 code is equal to bit position if 1 bit set only.(bit 0  is parity)
	
	}
}
}