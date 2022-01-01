package com.zoubworld.java.utils.compress.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zoubworld.java.utils.compress.CodingSet;
import com.zoubworld.java.utils.compress.ICode;
import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.file.BinaryFinFout;
import com.zoubworld.java.utils.compress.file.BinaryStdIn;
import com.zoubworld.java.utils.compress.file.BinaryStdOut;
import com.zoubworld.java.utils.compress.file.FileSymbol;
import com.zoubworld.java.utils.compress.file.FilesSymbol;
import com.zoubworld.java.utils.compress.file.IBinaryReader;
import com.zoubworld.java.utils.compress.file.IBinaryWriter;
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
	public final void testFlush()
	{
		assertEquals(2,1);
	}
	@Test
	public final void testrjump()
	{
		BinaryFinFout bin=new BinaryFinFout();
	//	assertEquals((Long)null,bin.getposIn());
		bin.write(true);//0
		bin.write(false);//1
		bin.write(true);//2
		bin.write(false);//3
		bin.write(false);//4
		bin.write(true);//5
		bin.write(true);//6
		bin.write(false);//7
		bin.write(false);//8
		bin.write(false);//9
		bin.flush();
		assertEquals((Long)0L,bin.getposIn());
		assertEquals(true,bin.readBoolean());
		assertEquals((Long)1L,bin.getposIn());
		bin.rjumpIn(4);
		assertEquals((Long)5L,bin.getposIn());		
		assertEquals(true,bin.readBoolean());
		bin.jumpIn(6);		
		assertEquals((Long)6L,bin.getposIn());
		assertEquals(true,bin.readBoolean());
		assertEquals((Long)7L,bin.getposIn());
		assertEquals(false,bin.readBoolean());
		assertEquals((Long)8L,bin.getposIn());
		bin.rjumpIn(-2);
		assertEquals((Long)6L,bin.getposIn());
		assertEquals(true,bin.readBoolean());
		assertEquals((Long)7L,bin.getposIn());
		assertEquals(false,bin.readBoolean());
		assertEquals((Long)8L,bin.getposIn());
		bin.jumpIn(0);	
		assertEquals(true,bin.readBoolean());
		assertEquals(false,bin.readBoolean());
		assertEquals(true,bin.readBoolean());
		assertEquals(false,bin.readBoolean());
		assertEquals(false,bin.readBoolean());		
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
		IBinaryReader bi = new BinaryStdIn(f2.getAbsolutePath());
		CodingSet cs = new CodingSet(CodingSet.NOCOMPRESS16);
		
		bo.setCodingRule(cs );
		bi.setCodingRule(cs );
		
		testBinary0( bo, bi,cs);
		 f1 = new File("res/result.test/binsmall.bin");
		 f2 = new File("res/result.test/binsmall.bin");
		 bo = new BinaryStdOut(f1.getAbsolutePath());
		 bi = new BinaryStdIn(f2.getAbsolutePath());
		testBinary1( bo, bi);
		 f1 = new File("res/result.test/binsmall.bin");
		 f2 = new File("res/result.test/binsmall.bin");
		 bo = new BinaryStdOut(f1.getAbsolutePath());
		 bi = new BinaryStdIn(f2.getAbsolutePath());
		testBinary2( bo, bi);
		
		 f1 = new File("res/result.test/binsmall.bin");
		 f2 = new File("res/result.test/binsmall.bin");
		 bo = new BinaryStdOut(f1.getAbsolutePath());
		 bi = new BinaryStdIn(f2.getAbsolutePath());
		testBinary3( bo, bi);
		
		 f1 = new File("res/result.test/binsmall.bin");
		 f2 = new File("res/result.test/binsmall.bin");
		 bo = new BinaryStdOut(f1.getAbsolutePath());
		 bi = new BinaryStdIn(f2.getAbsolutePath());
		testBinary4( bo, bi);
		
		
	}
	
	
	@Test
	public final void testBinaryStdfinfout() {

		BinaryFinFout b = new BinaryFinFout();
		IBinaryWriter bo =b;
		IBinaryReader bi = b;
	CodingSet cs = new CodingSet(CodingSet.NOCOMPRESS16);
		
		bo.setCodingRule(cs );
		bi.setCodingRule(cs );
		testBinary0( bo, bi,cs);
		b = new BinaryFinFout();
		 bo = b;
		 bi = b;
		testBinary1( bo, bi);
		b = new BinaryFinFout();
		 bo = b;
		 bi = b;
		testBinary2( bo, bi);
		b = new BinaryFinFout();
		 bo = b;
		 bi = b;
		testBinary3( bo, bi);
		b = new BinaryFinFout();
		 bo = b;
		 bi = b;
		testBinary4( bo, bi);
		
		
	}
	public final void testBinary0(IBinaryWriter bo,IBinaryReader bi,CodingSet cs) {
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
		bo.write((int) 0x12345678);
		
		bo.write("azertyuiop", 8);
		byte ba[] = {0x12,0x45,0x79,0x61};
		bo.write(ba[0]);
		bo.write(ba[1]);
		bo.write(ba[2]);
		bo.write(ba[3]);
		
		bo.write((long) 0x1234567890ABL,48/*,false*/);
		bo.write((long) 0x1234567890ABL,48/*,true*/);
		List<ISymbol> ls = new ArrayList<ISymbol>();
		ls.add(Symbol.EOF);
		ls.add(Symbol.CRLF);
		ls.add(Symbol.SOS);
		ls.add(Symbol.EOS);
		bo.writes(ls);
		bo.write(Symbol.toCode(ls,cs));
		bo.writes((List<ISymbol>)null);
		bo.write((ISymbol)null);
		assertEquals(cs, bo.getCodingRule());
		bo.write((List<ICode>) null);
		bo.setCodingRule(null);
		/*bo.writes(ls);
		bo.write(Symbol.EOS);
		bo.write(Symbol.toCode(ls));*/
		bo.setCodingRule(cs);
		
		bo.write((byte) 0x1);
		bo.write((byte) 0x2);
		bo.flush();

		assertEquals(true, bi.readBoolean());
		assertEquals((char) 12345, bi.readChar());
		assertEquals((byte) 0x45, bi.readByte());
		assertEquals((double) Math.PI, bi.readDouble(), 1 / Math.pow(2, 50));
		assertEquals((float) 3.14, bi.readFloat(), 1 / Math.pow(2, 22));
		assertEquals((short) 1234, bi.readShort());
		assertEquals((int) 123456789, bi.readInt());
		assertEquals((long) 0x123456789ABCDEFL, bi.readLong());
		assertEquals((int) 123456, (int) bi.readSignedInt(24));

		assertEquals((long) 0x123456789ABCDEFL, bi.readSignedLong(64));
		assertEquals((long) 0x12345678, bi.readSignedLong(32));
		assertEquals((long) 0x123456789ABL, bi.readSignedLong(48));
		for (byte i = -128; i < 127; i++)
			assertEquals((byte) i, bi.readByte());
		assertEquals((char) '@', bi.readChar(7));
		assertEquals(0x12345678,bi.readSignedInt(32).intValue());
		assertEquals("azertyuiop", bi.readString());
		assertArrayEquals(ba, bi.readBytes(4));
		assertEquals((long) 0x1234567890ABL, bi.readLong(48,true));
		assertEquals((long) 0x1234567890ABL, bi.readLong(48,true));
		assertEquals(ls, bi.readSymbols(4));
		assertEquals(ls, bi.readSymbols(4));
		assertEquals(cs, bi.getCodingRule());
	/*	bi.setCodingRule(null);
		assertEquals(ls, bi.readSymbols(4));
		assertEquals(Symbol.EOS, bi.readSymbol());		
		assertEquals(ls, bi.readSymbols(4));
		bi.setCodingRule(cs);*/
		assertEquals((byte) 0x1, bi.readByte());
		assertEquals((byte) 0x2, bi.readByte());
		
		assertEquals(null, bi.readSymbol());
		bi.setCodingRule(null);
		assertEquals(null, bi.readSymbol());
		assertEquals(null, bi.getCodingRule());
		assertEquals(null, bi.readCode());
//		assertEquals(null,bi.readSignedInt(32));
		
		bi.close();
		bo.close();

	}
	public final void testBinary1(IBinaryWriter bo,IBinaryReader bi) {
	for (byte i = 1; i <= 64; i++)
			bo.write((long) (((long) i) | (1L << (i - 1L))), i);
		for (byte i = 1; i <= 32; i++)
			bo.write((int) (i | (1L << (i - 1L))), i);
		for (byte i = 1; i <= 16; i++)
			bo.write((char) (i | (1L << (i - 1L))), i);
		for (byte i = 1; i <= 64; i++)
			bo.write((long) (-i), i);
		bo.write("azertyuiop");
		bo.flush();

		for (byte i = 1; i <= 64; i++)
			assertEquals((long) (((long) i) | (1L << (((long) i) - 1L))), bi.readUnsignedLong(i).longValue());
		for (byte i = 1; i <= 32; i++)
			assertEquals((int) (i | (1 << (i - 1))), (int) bi.readUnsignedInt(i));
		for (byte i = 1; i <= 16; i++)
			assertEquals("char" + i, (char) (i | (1L << (i - 1L))), bi.readChar(i));
		for (long i = 1; i <= 63; i++) {
			// System.out.println(" "+(long)(-i)+"
			// "+(((1L)<<i)-1L)+"="+((long)(-i)&(((1L)<<i)-1L)));
			assertEquals((long) (-i) & (((1L) << i) - 1), bi.readUnsignedLong((byte) i).longValue());
		}
		assertEquals((long) (-64), bi.readSignedLong((byte) 64));
		assertEquals("azertyuiop", bi.readString());
		bi.close();
		bo.close();
		
	}
		public final void testBinary2(IBinaryWriter bo,IBinaryReader bi) {
			
		CodingSet cs = new CodingSet(CodingSet.UNCOMPRESS);
		bo.setCodingRule(cs);
		bo.write(cs.get(Symbol.findId('@')));
		bo.write(Symbol.findId('9'));

		bo.flush();

		bi.setCodingRule(cs);
		assertEquals(cs.get(Symbol.findId('@')), bi.readCode());
		assertEquals(Symbol.findId('9'), bi.readSymbol());

		bi.close();
		bo.close();

		}
		public final void testBinary3(IBinaryWriter bo,IBinaryReader bi) {
	
		CodingSet cs;
		bo.setCodingRule(cs = new CodingSet(CodingSet.NOCOMPRESS16));
		bo.write(cs.get(Symbol.findId('@')));

		bo.write(cs.get(Symbol.SOS));
		bo.write(Symbol.findId('9'));
		bo.write(Symbol.PATr);
		bo.write(Symbol.FactorySymbolINT(0x1));
		bo.write(Symbol.FactorySymbolINT(-17));
		bo.write(Symbol.FactorySymbolINT(0x1234567890L));
		bo.flush();

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
		bo.close();

		}
		public final void testBinary4(IBinaryWriter bo,IBinaryReader bi) {

		CodingSet cs;
		bo.setCodingRule(cs = new CodingSet(CodingSet.NOCOMPRESS));
		bo.write(cs.get(Symbol.findId('@')));

		bo.write(cs.get(Symbol.SOS));
		bo.write(Symbol.findId('9'));
		bo.write(Symbol.PATr);
		bo.write(Symbol.FactorySymbolINT(0x1));
		bo.write(Symbol.FactorySymbolINT(-17));
		bo.write(Symbol.FactorySymbolINT(0x1234567890L));
		bo.flush();

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
		bo.close();

	}

}
