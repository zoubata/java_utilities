/**
 * 
 */
package com.zoubworld.java.utils.compress.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zoubworld.java.utils.compress.file.BinaryStdIn;
import com.zoubworld.utils.JavaUtils;

import junit.framework.Assert;

/**
 * @author zoubata
 *
 */
public class BinaryStdInTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.file.BinaryStdIn#BinaryStdIn()}.
	 */
	@Test
	public void testBinaryStdIn() {
		BinaryStdIn bsi=new BinaryStdIn();
		assertTrue(bsi.isEmpty());
		bsi=new BinaryStdIn("res/test/smallfile.txt");
		assertEquals(bsi.readChar(), 'D');
		bsi.close();
		bsi=new BinaryStdIn("res/test/smallfile.txt");
		assertEquals(bsi.readChar(), 'D');
		bsi.close();
		
		bsi=new BinaryStdIn(new File("res/test/smallfile.txt"));
		assertEquals(bsi.readChar(), 'D');
		bsi.close();
		
	}
	@Test(expected = NoSuchElementException.class)
	public void testBinaryStdInEx()
	{
		BinaryStdIn bsi=new BinaryStdIn();
		bsi.close();
		bsi.readChar();
	}
	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.file.BinaryStdIn#readChar()}.
	 */
	@Test
	public void testReadChar() {
		BinaryStdIn bsi=null;		
		bsi=new BinaryStdIn("res/test/smallfile.txt");
		assertEquals(bsi.readChar(), 'D');
		assertEquals(bsi.readBoolean(), (('U'&0x80)>>7)==1);
		assertEquals(bsi.readChar(7), ('U' & 0x7F));
		assertEquals(bsi.readShort(),(int)(('A'& 0xFF)<<8)+(int)('L'& 0xFF));
		bsi.close();
		bsi=new BinaryStdIn("res/test/smallfile.txt");
		assertEquals(bsi.readString(), JavaUtils.read("res/test/smallfile.txt"));
		bsi.close();
		bsi=new BinaryStdIn("res/test/smallfile.txt");
		short s1=bsi.readShort();
		short s2=bsi.readShort();		
		bsi.close();
		bsi=new BinaryStdIn("res/test/smallfile.txt");
		int i=bsi.readInt();
		assertEquals(s1, i>>16);
		assertEquals(s2, i&0xffff);
		bsi.close();
		bsi=new BinaryStdIn("res/test/smallfile.txt");
		int i8=bsi.readInt(8);
		long l8=bsi.readLong(8);
		int i1=bsi.readInt(1);
		long l1=bsi.readLong(1);
		assertEquals(s1>>8, i8);
		assertEquals(s1&0xff, l8);
		assertEquals(i1, (('A'&0x80)>>7));
		assertEquals(l1, (('A'&0x40)>>6));
		bsi.close();
		bsi=new BinaryStdIn("res/test/smallfile.txt");
		long l32=bsi.readLong(32);
		assertEquals(l32,i);
		bsi.close();
		bsi=new BinaryStdIn("res/test/smallfile.txt");
		int i32=bsi.readInt(32);
		int i322=bsi.readInt(32);
		assertEquals(l32,i32);
		bsi.close();
		bsi=new BinaryStdIn("res/test/smallfile.txt");
		long l64=bsi.readLong(64);
		assertEquals(l64>>32,i32);
		assertEquals((long)(l64&0xffffffffL),(long)i322);		
		bsi.close();
		bsi=new BinaryStdIn("res/test/smallfile.txt");
		long l=bsi.readLong();
		assertEquals(l64,l);
		bsi.close();
		bsi=new BinaryStdIn("res/test/smallfile.txt");
		byte b=bsi.readByte();
		assertEquals(b, i8);
		
		bsi.close();
		
		
		
		
		
		
		
		
		
	}

}
