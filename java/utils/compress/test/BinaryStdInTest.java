/**
 * 
 */
package com.zoubworld.java.utils.compress.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.binalgo.CodingSet;
import com.zoubworld.java.utils.compress.file.BinaryFinFout;
import com.zoubworld.java.utils.compress.file.BinaryPipeAlgo;
import com.zoubworld.java.utils.compress.file.BinaryStdIn;
import com.zoubworld.java.utils.compress.file.IBinaryReader;
import com.zoubworld.java.utils.compress.file.IBinaryWriter;
import com.zoubworld.utils.JavaUtils;

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
	@Test
	public void testperf() {
		int timens=1400000000;
			BinaryFinFout bin=new BinaryFinFout();
			int s=64*1024*1024;
			long nano_startTime = System.nanoTime();
			for(int i=0;i<s;i++)
			bin.write((i&1)==1);
			bin.flush();
			long size=bin.size();
			for(int i=0;i<s;i++)
				bin.readBoolean();
			//   0.1ms  0.4mo/s 32b
			//  34  ms 30  mo/s 1Mb
			//  55  ms 75  mo/s 4Mb
			// 117  ms 140  mo/s 16mb
			// 300  ms 110  mo/s 64mb
			//1550  ms 170  mo/s 256mb
			
			long nano_stopTime = System.nanoTime();
			long duration=(nano_stopTime - nano_startTime);
			System.out.println("duration :" + duration/1000000.0 + " ms");
			System.out.println("data :" + size + " bit");
				/*
			 * assertThat("speed perf", (nano_stopTime-nano_startTime), lessThan(timens));
			 */
			double speed=size;
			speed/=duration;
			speed*=1000;
			System.out.println("speed :" + speed + " Mo/s");
		
			assertTrue("speed perf", (duration) <= timens);// speed performance
			assertTrue("speed perf", (speed) > 100.0);// speed performance
			
		}
	/**
	 * 1 day to validated this class.
	 */
	@Test
	public void testBinaryFinFout() {
		{
			BinaryFinFout fifo = new BinaryFinFout();
			fifo.setCodingRule(new CodingSet(CodingSet.NOCOMPRESS));
			fifo.write((int) (0x12345678));
			assertEquals(32, fifo.size());
			fifo.write(true);
			fifo.write((byte) (-125));
			assertEquals(41, fifo.size());
			fifo.write(false);
			fifo.write(true);
			fifo.write((int) (0x1));
			assertEquals(43 + 32, fifo.size());

			fifo.write("0123456789/*^$!:;,");
			fifo.write(Symbol.EOF);
			assertEquals(236, fifo.size());
			fifo.flush();// +28bits (word alignment 32)
			assertEquals(256, fifo.size());
			assertEquals((int) (0x12345678), fifo.readInt());
			assertEquals(true, fifo.readBoolean());
			assertEquals((byte) (-125), fifo.readByte());
			assertEquals((false), fifo.readBoolean());
			assertEquals(true, fifo.readBoolean());
			assertEquals((int) (0x1), fifo.readInt());
			assertEquals("0123456789/*^$!:;,", fifo.readString());
			assertEquals(Symbol.EOF, fifo.readSymbol());
			assertEquals(20, fifo.size());// empty space

			fifo.close();
		}
		{
			BinaryFinFout fifo = new BinaryFinFout();
			fifo.setCodingRule(new CodingSet(CodingSet.NOCOMPRESS));
			fifo.write(true);
			fifo.write((byte) (1));
			fifo.write(true);
			fifo.write((char) (0x1));
			fifo.write(true);
			fifo.write((short) (0x1));
			fifo.write(true);
			fifo.write((int) (0x1));
			fifo.write(true);
			fifo.write((long) (0x1));
			fifo.write(true);
			fifo.flush();
			assertEquals(true, fifo.readBoolean());
			assertEquals((int) (1), fifo.readByte());
			assertEquals(true, fifo.readBoolean());
			assertEquals((int) (1), fifo.readChar());
			assertEquals(true, fifo.readBoolean());
			assertEquals((int) (1), fifo.readShort());
			assertEquals(true, fifo.readBoolean());
			assertEquals((int) (1), fifo.readInt());
			assertEquals(true, fifo.readBoolean());
			assertEquals((long) (1), fifo.readLong());
			assertEquals(true, fifo.readBoolean());
			fifo.close();
		}

		BinaryFinFout fifo = new BinaryFinFout();
		CodingSet cs;
		fifo.setCodingRule(cs = new CodingSet(CodingSet.NOCOMPRESS));

		for (int x = 0; x < 123; x++)
			fifo.write((boolean) (x % 2 == 1));
		for (byte x = -128; x < 127; x++)
			fifo.write((byte) (x));
		for (int x = -3; x < 257; x++)
			fifo.write((short) (x * x));
		for (int x = -3; x < 257; x++)
			fifo.write((int) (x * x * x));
		for (long x = -3; x < 257; x++)
			fifo.write((long) (x * 256L * x * x * 256));
		for (int x = 0; x < 257; x++)
			fifo.write((char) x);
		for (int x = 0; x < 257; x++)
			fifo.write((int) x, Math.max(x % 32, 9));
		for (int x = -127; x < 255; x++)
			fifo.write((int) x, Math.max(x % 32, 9));
		fifo.write((int) 0x12345678);

		fifo.write("0123456789?*^$!:;,");
		fifo.writes(Symbol.from("0123456789?*^$!:;,*"));

		for (long x = 1; x < 256; x++)
			fifo.write((double) -1.0 / ((double) x * x));
		for (long x = 1; x < 256; x++)
			fifo.write((float) 1.0 / x);
		for (int x = 0; x < 256; x++)
			fifo.write(cs.get(Symbol.findId(x)));

		fifo.flush();

		for (int x = 0; x < 123; x++)
			assertEquals("bool" + x, (x % 2 == 1), fifo.readBoolean());
		for (byte x = -128; x < 127; x++)
			assertEquals("byte" + x, (byte) x, (byte) fifo.readByte());
		for (int x = -3; x < 257; x++)
			assertEquals("short" + x, (short) (x * x), fifo.readShort());
		for (int x = -3; x < 257; x++)
			assertEquals("int" + x, (int) (x * x * x), fifo.readInt());
		for (int x = -3; x < 257; x++)
			assertEquals("long" + x, (long) (x * 256L * x * x * 256), fifo.readLong());
		for (int x = 0; x < 257; x++)
			assertEquals("char" + x, (char) x, fifo.readChar());
		for (int x = 0; x < 257; x++)
			assertEquals("int(" + Math.max(x % 32, 9) + ")" + x, x, (int) fifo.readUnsignedInt(Math.max(x % 32, 9)));
		for (int x = -127; x < 255; x++)
			assertEquals("int(" + Math.max(x % 32, 9) + ")" + x, x, (int) fifo.readSignedInt(Math.max(x % 32, 9)));
		assertEquals(0x12345678, fifo.readInt());
		assertEquals("0123456789?*^$!:;,", fifo.readString());
		assertEquals(Symbol.from("0123456789?*^$!:;,"), fifo.readSymbols("0123456789?*^$!:;,".length()));
		assertEquals(Symbol.findId('*'), fifo.readSymbol());
		for (long x = 1; x < 256; x++)
			assertEquals((double) -1.0 / ((double) x * x), fifo.readDouble(), 0.0000000000001);
		for (long x = 1; x < 256; x++)
			assertEquals("float=1/" + x, (float) 1.0 / x, fifo.readFloat(), 0.00000001);
		for (int x = 0; x < 256; x++)
			assertEquals("code(" + x + ")", cs.get(Symbol.findId(x)), fifo.readCode());

	}

	/**
	 * Test method for
	 * {@link com.zoubwolrd.java.utils.compress.file.BinaryStdIn#BinaryStdIn()}.
	 */
	@Test
	public void testBinaryStdIn() {
		IBinaryReader bsi = new BinaryStdIn();
		assertTrue(bsi.isEmpty());
		bsi = new BinaryStdIn("res/test/ref/smallDir/smallfile.txt");
		assertEquals('D', bsi.readByte());
		bsi.close();
		bsi = new BinaryStdIn("res/test/ref/smallDir/smallfile.txt");
		assertEquals(bsi.readByte(), 'D');
		bsi.close();

		bsi = new BinaryStdIn(new File("res/test/ref/smallDir/smallfile.txt"));
		assertEquals(bsi.readByte(), 'D');
		bsi.close();

	}

	@Test
	public void testBinaryPipeAlgo() {
		BinaryFinFout fifo = new BinaryFinFout();
		BinaryPipeAlgo bout = new BinaryPipeAlgo('a' - 'A', 8, true, (IBinaryWriter) fifo);
		BinaryPipeAlgo bin = new BinaryPipeAlgo('a' - 'A', 8, false, (IBinaryReader) fifo);
		bout.write("ABCDEFGHIJKLM");
		bout.write("ABCDEFGHIJKLM");
		bout.write("ABCDEFGHIJKLM");
		bout.write("ABCDEFGHIJKLM");

		bout.write("abcdefghijklm");
		bout.write("abcdefghijklm");
		bout.write("abcdefghijklm");
		bout.write("abcdefghijklm");
		bout.flush();
		assertEquals("abcdefghijklm ", fifo.readString(13));
		fifo.flush();
		assertEquals("ABCDEFGHIJKLM", bin.readString());
		assertEquals("ABCDEFGHIJKLM", bin.readString());
		assertEquals("ABCDEFGHIJKLM", bin.readString());
		assertEquals("abcdefghijklm", bin.readString());
		assertEquals("abcdefghijklm", bin.readString());
		assertEquals("abcdefghijklm", bin.readString());
		/*
		 * fifo =new BinaryFinFout(); bout=new BinaryPipeAlgo(new
		 * CaesarCipher8((byte)1),true,(IBinaryWriter)fifo); bin=new BinaryPipeAlgo(new
		 * CaesarCipher8((byte)1),true,(IBinaryReader)fifo);
		 * bout.write("ABCDEFGHIJKLM"); bout.write("ABCDEFGHIJKLM");
		 * bout.write("ABCDEFGHIJKLM"); bout.write("ABCDEFGHIJKLM");
		 * 
		 * bout.write("abcdefghijklm"); bout.write("abcdefghijklm");
		 * bout.write("abcdefghijklm"); bout.write("abcdefghijklm"); bout.flush();
		 * assertEquals("BCDEFGHIJKLMN",fifo.readString(12));//encrypted
		 * fifo.readString(1); fifo.flush();
		 * assertEquals("ABCDEFGHIJKLM",bin.readString(12));
		 * assertEquals("ABCDEFGHIJKLM",bin.readString());
		 * assertEquals("ABCDEFGHIJKLM",bin.readString());
		 * assertEquals("abcdefghijklm",bin.readString());
		 * assertEquals("abcdefghijklm",bin.readString());
		 * assertEquals("abcdefghijklm",bin.readString());
		 */
	}

	@Test(expected = NoSuchElementException.class)
	public void testBinaryStdInEx() {
		IBinaryReader bsi = new BinaryStdIn();
		bsi.close();
		bsi.readChar();
	}

	/**
	 * Test method for
	 * {@link com.zoubwolrd.java.utils.compress.file.BinaryStdIn#readChar()}.
	 */
	@Test
	public void testReadChar() {
		IBinaryReader bsi = null;
		bsi = new BinaryStdIn("res/test/ref/smallDir/smallfile.txt");
		assertEquals(bsi.readByte(), 'D');
		assertEquals(bsi.readBoolean(), (('U' & 0x80) >> 7) == 1);
		assertEquals(bsi.readChar(7), ('U' & 0x7F));
		assertEquals(bsi.readShort(), (int) (('A' & 0xFF) << 8) + (int) ('L' & 0xFF));
		bsi.close();
		bsi = new BinaryStdIn("res/test/ref/smallDir/smallfile.txt");
		assertEquals(JavaUtils.read("res/test/ref/smallDir/smallfile.txt"), bsi.readString());
		bsi.close();
		bsi = new BinaryStdIn("res/test/ref/smallDir/smallfile.txt");
		short s1 = bsi.readShort();
		short s2 = bsi.readShort();
		bsi.close();
		bsi = new BinaryStdIn("res/test/ref/smallDir/smallfile.txt");
		int i = bsi.readInt();
		assertEquals(s1, i >> 16);
		assertEquals(s2, i & 0xffff);
		bsi.close();
		bsi = new BinaryStdIn("res/test/ref/smallDir/smallfile.txt");
		int i8 = bsi.readSignedInt(8);
		long l8 = bsi.readSignedLong(8);
		int i1 = bsi.readSignedInt(1);
		long l1 = bsi.readUnsignedInt(1);
		assertEquals(s1 >> 8, i8);
		assertEquals(s1 & 0xff, l8);
		assertEquals(i1, (('A' & 0x80) >> 7));
		assertEquals(l1, (('A' & 0x40) >>> 6));
		bsi.close();
		bsi = new BinaryStdIn("res/test/ref/smallDir/smallfile.txt");
		long l32 = bsi.readSignedLong(32);
		assertEquals(l32, i);
		bsi.close();
		bsi = new BinaryStdIn("res/test/ref/smallDir/smallfile.txt");
		int i32 = bsi.readSignedInt(32);
		int i322 = bsi.readSignedInt(32);
		assertEquals(l32, i32);
		bsi.close();
		bsi = new BinaryStdIn("res/test/ref/smallDir/smallfile.txt");
		long l64 = bsi.readSignedLong(64);
		assertEquals(l64 >> 32, i32);
		assertEquals((long) (l64 & 0xffffffffL), (long) i322);
		bsi.close();
		bsi = new BinaryStdIn("res/test/ref/smallDir/smallfile.txt");
		long l = bsi.readLong();
		assertEquals(l64, l);
		bsi.close();
		bsi = new BinaryStdIn("res/test/ref/smallDir/smallfile.txt");
		byte b = bsi.readByte();
		assertEquals(b, i8);

		bsi.close();

	}

}
