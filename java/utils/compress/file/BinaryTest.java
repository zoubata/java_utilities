package com.zoubworld.java.utils.compress.file;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zoubworld.java.utils.compress.CodingSet;
import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.utils.JavaUtils;

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
	public final void testFileSymbol() {
		String di = "res\\test\\small_ref\\";
		String d0 = "res\\result.test\\small_ref2\\";
		JavaUtils.DirDelete(d0);
		JavaUtils.mkDir(d0);
		File d = new File(di);
		FilesSymbol ds = new FilesSymbol(d);
		List<ISymbol> ls = ds.toSymbol();
		// test empty dir
		assertEquals(false, JavaUtils.fileExist(d0 + "smallfile.txt"));
		assertEquals(false, JavaUtils.fileExist(d0 + "smallfile.bin"));
		assertEquals(false, JavaUtils.fileExist(d0 + "Book3.csv"));
		// test list of symbol to files
		ICodingRule cs = new CodingSet(CodingSet.UNCOMPRESS);
		ICodingRule csc = new CodingSet(CodingSet.NOCOMPRESS16);
		FilesSymbol.toFile(ls, cs, d0);

		assertEquals(JavaUtils.read(di + "smallfile.txt"), JavaUtils.read(d0 + "smallfile.txt"));
		assertEquals(JavaUtils.read(di + "smallfile.bin"), JavaUtils.read(d0 + "smallfile.bin"));
		assertEquals(JavaUtils.read(di + "Book3.csv"), JavaUtils.read(d0 + "Book3.csv"));
		assertEquals(true, JavaUtils.fileExist(d0 + "smallfile.txt"));
		assertEquals(true, JavaUtils.fileExist(d0 + "smallfile.bin"));
		assertEquals(true, JavaUtils.fileExist(d0 + "Book3.csv"));

		di = "res\\test\\very_small\\";
		d = new File(di);
		ds = new FilesSymbol(d);
		ls = ds.toSymbol();
		// test compressed file to files
		String filecompname = d0.substring(0, d0.length() - 1) + "zip.bin";
		FileSymbol.toArchive(ls, csc, filecompname);
		JavaUtils.DirDelete(d0);
		assertEquals(false, JavaUtils.fileExist(d0 + "smallfile.txt"));
		assertEquals(false, JavaUtils.fileExist(d0 + "smallfile.bin"));
		assertEquals(false, JavaUtils.fileExist(d0 + "Book3.csv"));
		List<ISymbol> lse = FileSymbol.fromArchive(null, filecompname);
		FilesSymbol.toFile(lse, cs, d0);

		assertEquals(JavaUtils.read(di + "smallfile.txt"), JavaUtils.read(d0 + "smallfile.txt"));
		assertEquals(JavaUtils.read(di + "smallfile.bin"), JavaUtils.read(d0 + "smallfile.bin"));
		assertEquals(JavaUtils.read(di + "Book3.csv"), JavaUtils.read(d0 + "Book3.csv"));
		assertEquals(true, JavaUtils.fileExist(d0 + "pie.txt"));
		assertEquals(true, JavaUtils.fileExist(d0 + "Book1.csv"));
		assertEquals(true, JavaUtils.fileExist(d0 + "Book3.csv"));

	}

	@Test
	public final void testBinaryStdInOut() {
		File f1 = new File("res/result.test/binsmall.bin");
		File f2 = new File("res/result.test/binsmall.bin");
		IBinaryWriter bo = new BinaryStdOut(f1.getAbsolutePath());
		bo.write(true);
		bo.write((char) 12345);// 16bits

		bo.write((byte) 0x45);
		bo.write((double) Math.PI);
		bo.write((float) 3.14);
		bo.write((short) 1234);
		bo.write((int) 123456789);
		bo.write((long) 0x123456789ABCDEFL);
		bo.write((int) 123456, 24);
		bo.write((long) 0x123456789ABCDEFL, 64);
		bo.write((long) 0x12345678, 32);
		bo.write((long) 0x123456789ABL, 48);

		for (byte i = -128; i < 127; i++)
			bo.write((byte) i);
		bo.write((char) '@', 7);
		bo.write("azertyuiop", 8);

		bo.close();

		IBinaryReader bi = new BinaryStdIn(f2.getAbsolutePath());
		assertEquals(true, bi.readBoolean());
		assertEquals((char) 12345, bi.readChar());
		assertEquals((byte) 0x45, bi.readByte());
		assertEquals((double) Math.PI, bi.readDouble(), 1 / Math.pow(2, 50));
		assertEquals((float) 3.14, bi.readFloat(), 1 / Math.pow(2, 22));
		assertEquals((short) 1234, bi.readShort());
		assertEquals((int) 123456789, bi.readInt());
		assertEquals((long) 0x123456789ABCDEFL, bi.readLong());
		assertEquals((int) 123456, (int) bi.readInt(24));

		assertEquals((long) 0x123456789ABCDEFL, bi.readLong(64));
		assertEquals((long) 0x12345678, bi.readLong(32));
		assertEquals((long) 0x123456789ABL, bi.readLong(48));
		for (byte i = -128; i < 127; i++)
			assertEquals((byte) i, bi.readByte());
		assertEquals((char) '@', bi.readChar(7));
		assertEquals("azertyuiop", bi.readString());
		bi.close();

		f1 = new File("res/result.test/binsmall.bin");
		f2 = new File("res/result.test/binsmall.bin");
		bo = new BinaryStdOut(f1);
		for (byte i = 1; i <= 64; i++)
			bo.write((long) (((long) i) | (1L << (i - 1L))), i);
		for (byte i = 1; i <= 32; i++)
			bo.write((int) (i | (1L << (i - 1L))), i);
		for (byte i = 1; i <= 16; i++)
			bo.write((char) (i | (1L << (i - 1L))), i);
		for (byte i = 1; i <= 64; i++)
			bo.write((long) (-i), i);
		bo.write("azertyuiop");
		bo.close();

		bi = new BinaryStdIn(f2);
		for (byte i = 1; i <= 64; i++)
			assertEquals((long) (((long) i) | (1L << (((long) i) - 1L))), bi.readLong(i));
		for (byte i = 1; i <= 32; i++)
			assertEquals((int) (i | (1 << (i - 1))), (int) bi.readInt(i));
		for (byte i = 1; i <= 16; i++)
			assertEquals("char" + i, (char) (i | (1L << (i - 1L))), bi.readChar(i));
		for (long i = 1; i <= 63; i++) {
			// System.out.println(" "+(long)(-i)+"
			// "+(((1L)<<i)-1L)+"="+((long)(-i)&(((1L)<<i)-1L)));
			assertEquals((long) (-i) & (((1L) << i) - 1), bi.readLong((byte) i));
		}
		assertEquals((long) (-64), bi.readLong((byte) 64));
		assertEquals("azertyuiop", bi.readString());
		bi.close();

		f1 = new File("res/result.test/binsmall.bin");
		f2 = new File("res/result.test/binsmall.bin");
		CodingSet cs = new CodingSet(CodingSet.UNCOMPRESS);
		bo = new BinaryStdOut(f1);
		bo.setCodingRule(cs);
		bo.write(cs.get(Symbol.findId('@')));
		bo.write(Symbol.findId('9'));

		bo.close();

		bi = new BinaryStdIn(f2);
		bi.setCodingRule(cs);
		assertEquals(cs.get(Symbol.findId('@')), bi.readCode());
		assertEquals(Symbol.findId('9'), bi.readSymbol());

		bi.close();

		// check complexe symbole and code base 16(easy to read)
		f1 = new File("res/result.test/binsmall.bin");
		f2 = new File("res/result.test/binsmall.bin");
		bo = new BinaryStdOut(f1);
		bo.setCodingRule(cs = new CodingSet(CodingSet.NOCOMPRESS16));
		bo.write(cs.get(Symbol.findId('@')));

		bo.write(cs.get(Symbol.SOS));
		bo.write(Symbol.findId('9'));
		bo.write(Symbol.PATr);
		bo.write(Symbol.FactorySymbolINT(0x1));
		bo.write(Symbol.FactorySymbolINT(-17));
		bo.write(Symbol.FactorySymbolINT(0x1234567890L));
		bo.close();

		bi = new BinaryStdIn(f2);
		bi.setCodingRule(new CodingSet(CodingSet.NOCOMPRESS16));
		assertEquals(cs.get(Symbol.findId('@')), bi.readCode());

		assertEquals(cs.get(Symbol.SOS), bi.readCode());
		assertEquals(Symbol.findId('9'), bi.readSymbol());
		assertEquals(Symbol.PATr, bi.readSymbol());
		assertNotEquals(Symbol.SOS, Symbol.PATr);

		assertEquals(Symbol.FactorySymbolINT(0x1), bi.readSymbol());
		assertEquals(Symbol.FactorySymbolINT(-17).getCode(), bi.readCode());
		assertEquals(Symbol.FactorySymbolINT(0x1234567890L), bi.readSymbol());

		bi.close();

		// check complexe symbole and code base 9(easy to read)
		f1 = new File("res/result.test/binsmall.bin");
		f2 = new File("res/result.test/binsmall.bin");
		bo = new BinaryStdOut(f1);
		bo.setCodingRule(cs = new CodingSet(CodingSet.NOCOMPRESS));
		bo.write(cs.get(Symbol.findId('@')));

		bo.write(cs.get(Symbol.SOS));
		bo.write(Symbol.findId('9'));
		bo.write(Symbol.PATr);
		bo.write(Symbol.FactorySymbolINT(0x1));
		bo.write(Symbol.FactorySymbolINT(-17));
		bo.write(Symbol.FactorySymbolINT(0x1234567890L));
		bo.close();

		bi = new BinaryStdIn(f2);
		bi.setCodingRule(new CodingSet(CodingSet.NOCOMPRESS));
		assertEquals(cs.get(Symbol.findId('@')), bi.readCode());

		assertEquals(cs.get(Symbol.SOS), bi.readCode());
		assertEquals(Symbol.findId('9'), bi.readSymbol());
		assertEquals(Symbol.PATr, bi.readSymbol());
		assertNotEquals(Symbol.SOS, Symbol.PATr);

		assertEquals(Symbol.FactorySymbolINT(0x1), bi.readSymbol());
		assertEquals(Symbol.FactorySymbolINT(-17).getCode(), bi.readCode());
		assertEquals(Symbol.FactorySymbolINT(0x1234567890L), bi.readSymbol());

		bi.close();

	}

}
