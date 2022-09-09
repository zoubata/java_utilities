package com.zoubworld.java.utils.compress.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.algo.IAlgoCompress;
import com.zoubworld.java.utils.compress.algo.RLE;
import com.zoubworld.java.utils.compress.binalgo.CodingSet;
import com.zoubworld.java.utils.compress.binalgo.HuffmanCode;
import com.zoubworld.java.utils.compress.file.FileSymbol;
import com.zoubworld.utils.JavaUtils;

public class RLETest {

	@Test
	public void testDecode() {
		// fail("Not yet implemented");
	}

	public void testRLEBasic(String text, int r) {

		RLE rle = new RLE();

		List<ISymbol> ls = Symbol.factoryCharSeq(text);
		// System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));

		List<ISymbol> lse = rle.encodeSymbol(ls);
		// System.out.println(lse.toString());

		System.out.println(lse.size() + ":" + ls.size());
		assertTrue(ls.size() >= lse.size());
		assertTrue(ls.size() - r >= lse.size());
		ls = rle.decodeSymbol(lse);
		// System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));
		String text2 = new String(Symbol.listSymbolToCharSeq(ls));
		System.out.println(lse.size() + "/" + ls.size());
		assertEquals(text, (text2));
		assertTrue(text.equals(text2));
	}

	@Test
	public void testRLE_Perf() {
		long timens = 220 * 1000 * 1000L;// 0.22s

		long nano_startTime = System.nanoTime();
		testRLEBasic(TestData.string1, 9347 - 8722);
		long nano_stopTime = System.nanoTime();
		System.out.print("duration :" + (nano_stopTime - nano_startTime) + " ns");
		assertTrue("speed perf", (nano_stopTime - nano_startTime) <= timens);// speed performance

	}

	@Test
	public void testRleBasicAll() {
		long timens = 105 * 1000 * 1000L;// 0.15s

		testRLEBasic("11", 0);
		testRLEBasic("1", 0);
		testRLEBasic(
				"test de compression AAAAAAAAAAAAAAAAA CDCDCDCDCDCDCD test de compression AAAAAAAAAAAAAAAAA CDCDCDCDC\n",
				101 - 73);

		// at end testRLEBasic( LZWBasic.file,9347-8722);

		String s = "";
		for (int i = 0; i < 2048; i += 16)
			s += "0123456789ABCDEF";
		testRLEBasic(s, 2048 - 2048);

		for (int i = 0; i <= 2048; i += 10)
			s += "0123456789ABCDE\n";
		testRLEBasic(s, 5328 - 5328);
		s = "";
		for (int i = 0; i <= 2048; i += 10)
			s += "000005677777CDE\n";
		testRLEBasic(s, 3280 - 2460);
		s = "";
		for (int i = 0; i <= 2048; i += 10)
			s += "0000000000000000";
		testRLEBasic(s, 3280 - 3);
		s = "";
		for (int i = 0; i <= 2048; i += 10)
			s += "0000000000000001";
		testRLEBasic(s, 3280 - 4 * 205);

		testRLEBasic(
				"klefnhatrytvzyeryyteyrretouizybrebyyelkjkdjfhgjksdnjkdsj,vvi,ouybiotruybiortuyioruyoirtyebetyryetberybre"
						+ "rtyeryteryreybetyreberybyiokemoiskherkhiuilhisunehkvjhlrkuthurzeioazertyuwsdfghjcvbn,rtycvdhjskqieozpahj"
						+ "vcbnxkg hcjxk tyucixow tvyfudiz cndjeio nvcjdkezo& ,ckdlsozpa ,;cldsmpz ,cdklazertyuisdfghjkxcvbpklbn,zb"
						+ "azertyuiopqqqqqqqqqqqqqqqqqsdfghjklmwxcvbn,azertyuiopsdfghjkxcvbvretczehcgbtkzjebgtckhekzbgnxkhegrhztghz"
						+ "wqaxszcdevfrbgtnhy,ju;ki:lo!mp^*$wqaxszxszcdecdevfrvfrbgtcdeznhy,juxsz;kiwq:lo!mpcdevfrcdzxsznhywqa,jun"
						+ "njibhuvgycftxdrwsewqawqzwsewsewszwsdcdevfdbchdun jcdienbjvkfdflwjkvcsnvhlrejkhtvlhy;kivfrcdenhycdexsz)",
				607 - 621);

		long nano_startTime = System.nanoTime();
		testRLEBasic(TestData.string1, 9327 - 8722);
		long nano_stopTime = System.nanoTime();
		System.out.print("duration :" + (nano_stopTime - nano_startTime) + " ns");
		assertTrue("speed perf", (nano_stopTime - nano_startTime) <= timens);// speed performance

	}

	/**
	 * check file conversion and performance on bigger set :
	 * res\test\ref\com_zoubworld\ utils_compress\image\plan.bmp
	 * res\test\ref\com_zoubworld\ utils_compress\image\plan256.bmp
	 * res\test\ref\com_zoubworld\ utils_compress\image\plan16.bmp
	 * res\test\ref\com_zoubworld\ utils_compress\txt\Doxyfile
	 * res\test\ref\com_zoubworld\ utils_compress\txt\pio.h
	 * res\test\ref\com_zoubworld\
	 * utils_compress\txt\sam_ba_Pilo_SAMD21J15A_PCOM3_SERIAL2.hex
	 * res\test\ref\com_zoubworld\ utils_compress\txt\board_driver_serial.pbi
	 * res\test\ref\com_zoubworld\
	 * utils_compress\txt\sam_ba_Pilo_SAMC21J18A_FTDI.bin
	 * res\test\ref\com_zoubworld\ utils_compress\txt\test_fl.elf
	 * 
	 */
	@Test
	public void testRLePerformance() {
		// RLePerformance(fn,fnc,ratio, timens)
		String fn = ".\\res\\test\\ref\\com_zoubworld\\utils_compress\\image\\plan.bmp";
		String fnc = ".\\res\\result.test\\ref\\com_zoubworld\\utils_compress\\image\\plan.bmp";
		double ratio = 0.1;
		long timens = 2 * 1000 * 1000 * 1000L;// 2s

		RLE rle = new RLE(3);
		List<ISymbol> ls = null;
		List<ISymbol> lsc = null;
		List<ISymbol> lse = null;
		long nano_startTime = System.nanoTime();

		FileSymbol fs = new FileSymbol(new File(fn));
		ls = fs.toSymbol();
		lse = rle.encodeSymbol(ls);
		ICodingRule huf = HuffmanCode.buildCode(lse);
		Symbol.apply(huf);
		lsc = rle.decodeSymbol(lse);
		assertTrue(lse.size() <= ls.size());
		assertTrue("size perf", lse.size() <= (ls.size() * ratio));
		assertEquals("compress/decompress work", ls, lsc);
		FileSymbol.toArchive(lse, huf, fnc + ".rlehuf");
		FileSymbol.toArchive(ls, huf, fnc + ".huf");

		// reset compress seting
		Symbol.apply(new CodingSet(CodingSet.NOCOMPRESS));
		rle = new RLE(333);

		List<ISymbol> lse2 = FileSymbol.fromArchive(null, fnc + ".huf");
		assertEquals("integrity of symbol list in huf file size()", lse.size(), lse2.size());
		assertEquals("integrity of symbol list in huf file", lse, lse2);
		lse2 = FileSymbol.fromArchive(null, fnc + ".rlehuf");
		assertEquals("integrity of symbol list in file", lse, lse2);

		lsc = rle.decodeSymbol(lse2);
		assertEquals("store in file work", ls, lsc);
		FileSymbol.toFile(lsc, fnc);
		assertEquals("integrity of un compressed file", JavaUtils.read(fn), JavaUtils.read(fnc));

		long nano_stopTime = System.nanoTime();
		assertTrue("speed perf", (nano_stopTime - nano_startTime) <= timens);// speed performance
	}

	/**
	 * check symbol convertion
	 */
	@Test
	public void testRLeBasic() {

		// JUNIT : todo
		// CodingSet cs=new CodingSet(CodingSet.NOCOMPRESS);
		// Symbol.apply(cs);
		RLE rle = new RLE(3);
		List<ISymbol> ls = null;
		List<ISymbol> lsc = null;
		List<ISymbol> lse = null;
		ICodingRule huf = new CodingSet(CodingSet.UNCOMPRESS);
		ICodingRule cs9 = new CodingSet(CodingSet.NOCOMPRESS);
		ls = Symbol.factoryCharSeq(":1003200000000000000000000000000000000000CD");
		lse = rle.encodeSymbol(ls);
		System.out.println("flat " + Symbol.length(ls) + " : '" + ls + "'");
		System.out.println("rle  " + Symbol.length(lse, cs9) + " : '" + lse + "'");
		lsc = rle.decodeSymbol(lse);
		assertEquals(344L, Symbol.length(ls, huf).longValue());
		assertTrue(Symbol.length(lse) <= 144);
		assertTrue(lse.size() <= ls.size());

		System.out.println("" + Symbol.listSymbolToString(ls) + "=\r\n" + Symbol.listSymbolToString(lsc));
		assertEquals("" + Symbol.listSymbolToString(ls), "" + Symbol.listSymbolToString(lsc));

		ls = Symbol.factoryCharSeq(":10029000AB050020AD050020AF050020B105002012");
		lse = rle.encodeSymbol(ls);
		System.out.println("flat " + Symbol.length(ls) + " : '" + ls.size() + "'");
		System.out.println("rle  " + Symbol.length(lse) + " : '" + lse.size() + "'");
		lsc = rle.decodeSymbol(lse);
		assertEquals(344L, Symbol.length(ls, huf).longValue());
		assertTrue(lse.size() <= ls.size());
		assertTrue(Symbol.length(lse, huf) <= 344);
		assertEquals(ls, lsc);

		ls = Symbol.factoryCharSeq(":102D10001D0000001E0000001F0000002000000039");
		lse = rle.encodeSymbol(ls);
		System.out.println("flat " + Symbol.length(ls, huf) + " : '" + ls.size() + "'");
		System.out.println("rle  " + Symbol.length(lse, cs9) + " : '" + lse.size() + "'");
		lsc = rle.decodeSymbol(lse);
		assertTrue(lse.size() <= ls.size());// it should compress

		assertEquals(43, ls.size());// check performance at symbol level
		assertTrue(lse.size() <= 30);

		assertEquals(344L, Symbol.length(ls, huf).longValue());
		assertTrue(Symbol.length(lse, cs9) <= 448);// need huff to be performant at bit level
		assertEquals(ls, lsc);// test integrity of data

		ls = Symbol.factoryCharSeq("F00000020000000");
		lse = rle.encodeSymbol(ls);

		System.out.println("flat " + Symbol.length(ls, huf) + "/" + ls.size() + "=" + (Symbol.length(ls) / ls.size())
				+ " : '" + ls + "'");
		System.out.println("rle  " + Symbol.length(lse, cs9) + "/" + lse.size() + "="
				+ (Symbol.length(lse) / lse.size()) + " : '" + lse + "'");

		lsc = rle.decodeSymbol(lse);
		assertTrue(lse.size() <= ls.size());
		assertTrue(ls.size() == 15);
		assertTrue(lse.size() <= 8);
		assertEquals(120L, Symbol.length(ls, huf).longValue());
		assertTrue(Symbol.length(lse) <= 168);
		assertEquals(ls, lsc);// test integrity of data

		/*
		 * System.out.println(huf.get(Symbol.findId('0')));
		 * System.out.println(huf.get(Symbol.findId('F')));
		 * System.out.println(huf.get(Symbol.findId('2')));
		 * System.out.println(huf.get(Symbol.RLE));
		 * System.out.println(huf.get(Symbol.INT4));
		 */
		/*
		 * huf = HuffmanCode.buildCode(lse); Symbol.apply(huf);
		 * System.out.println("flat "+Symbol.length(ls,huf)+"/"+ls.size()+"="+(Symbol.
		 * length(ls)/ls.size())+" : '"+Symbol.toCodes(ls,huf)+"'");
		 * System.out.println("rle  "+Symbol.length(lse,huf)+"/"+lse.size()+"="+(Symbol.
		 * length(lse)/lse.size())+" : '"+Symbol.toCodes(lse,huf)+"'"); /*
		 * System.out.println(huf.get(Symbol.findId('0'))+":"+Symbol.toCode(Symbol.
		 * findId('0')).length());
		 * System.out.println(huf.get(Symbol.findId('F'))+":"+Symbol.toCode(Symbol.
		 * findId('F')).length());
		 * System.out.println(huf.get(Symbol.findId('2'))+":"+Symbol.toCode(Symbol.
		 * findId('2')).length());
		 * System.out.println(huf.get(Symbol.RLE)+":"+Symbol.toCode(Symbol.RLE).length()
		 * );
		 * System.out.println(huf.get(Symbol.INT4)+":"+Symbol.toCode(Symbol.INT4).length
		 * ());
		 */

		/*
		 *
		 * cs=9bits flat 318834 rle 303738 flat 387 : '[':', '1', '0', '0', '3', '2',
		 * '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
		 * '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
		 * '0', '0', '0', '0', '0', 'C', 'D']' rle 107 : '[':', '1', '0', '3', '3', '2',
		 * '0', RLE, INT8(35), 'C', 'D']' flat 387 : '[':', '1', '0', '0', '2', '9',
		 * '0', '0', '0', 'A', 'B', '0', '5', '0', '0', '2', '0', 'A', 'D', '0', '5',
		 * '0', '0', '2', '0', 'A', 'F', '0', '5', '0', '0', '2', '0', 'B', '1', '0',
		 * '5', '0', '0', '2', '0', '1', '2']' rle 387 : '[':', '1', '0', '2', '2', '9',
		 * '0', 'A', 'A', 'A', 'B', '0', '5', '0', '2', '2', '0', 'A', 'D', '0', '5',
		 * '0', '2', '2', '0', 'A', 'F', '0', '5', '0', '2', '2', '0', 'B', '1', '0',
		 * '5', '0', '2', '2', '0', '1', '2']' flat 387 : '[':', '1', '0', '2', 'D',
		 * '1', '0', '0', '0', '1', 'D', '0', '0', '0', '0', '0', '0', '1', 'E', '0',
		 * '0', '0', '0', '0', '0', '1', 'F', '0', '0', '0', '0', '0', '0', '2', '0',
		 * '0', '0', '0', '0', '0', '0', '3', '9']' rle 286 : '[':', '1', '0', '2', 'D',
		 * '1', '0', '1', '1', '1', 'D', '0', RLE, INT4(6), '1', 'E', '0', RLE, INT4(6),
		 * '1', 'F', '0', RLE, INT4(6), '2', '0', RLE, INT4(7), '3', '9']' flat 135/15=9
		 * : '['F', '0', '0', '0', '0', '0', '0', '2', '0', '0', '0', '0', '0', '0',
		 * '0']' rle 89/9=9 : '['F', '0', RLE, INT4(6), '2', '0', RLE, INT4(7), '0']'
		 * 
		 * cs=huff
		 * 
		 * flat 133043=16.631ko rle 248060 flat 102 : '[':', '1', '0', '0', '3', '2',
		 * '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
		 * '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0',
		 * '0', '0', '0', '0', '0', 'C', 'D']' rle 60 : '[':', '1', '0', '3', '3', '2',
		 * '0', RLE, INT8(35), 'C', 'D']' flat 142 : '[':', '1', '0', '0', '2', '9',
		 * '0', '0', '0', 'A', 'B', '0', '5', '0', '0', '2', '0', 'A', 'D', '0', '5',
		 * '0', '0', '2', '0', 'A', 'F', '0', '5', '0', '0', '2', '0', 'B', '1', '0',
		 * '5', '0', '0', '2', '0', '1', '2']' rle 158 : '[':', '1', '0', '2', '2', '9',
		 * '0', 'A', 'A', 'A', 'B', '0', '5', '0', '2', '2', '0', 'A', 'D', '0', '5',
		 * '0', '2', '2', '0', 'A', 'F', '0', '5', '0', '2', '2', '0', 'B', '1', '0',
		 * '5', '0', '2', '2', '0', '1', '2']' flat 120 : '[':', '1', '0', '2', 'D',
		 * '1', '0', '0', '0', '1', 'D', '0', '0', '0', '0', '0', '0', '1', 'E', '0',
		 * '0', '0', '0', '0', '0', '1', 'F', '0', '0', '0', '0', '0', '0', '2', '0',
		 * '0', '0', '0', '0', '0', '0', '3', '9']' rle 150 : '[':', '1', '0', '2', 'D',
		 * '1', '0', '1', '1', '1', 'D', '0', RLE, INT4(6), '1', 'E', '0', RLE, INT4(6),
		 * '1', 'F', '0', RLE, INT4(6), '2', '0', RLE, INT4(7), '3', '9']' flat 32/15=2
		 * : '['F', '0', '0', '0', '0', '0', '0', '2', '0', '0', '0', '0', '0', '0',
		 * '0']' rle 26/8=3 : '['F', RLE, INT4(6),'0', '2', RLE, INT4(7), '0']'
		 * 
		 */

		ls = Symbol.factoryCharSeq(":10030000F0FF032002F084BCAFF30080AFF3008065\r\n"
				+ ":1003100000000000000000000000000000000000DD\r\n" + ":1003200000000000000000000000000000000000CD\r\n"
				+ ":1003300000000000000000000000000000000000BD\r\n" + ":1003400000000000000000000000000000000000AD\r\n"
				+ ":10035000000000000000000000000000000000009D\r\n" + ":10036000000000000000000000000000000000008D\r\n"
				+ ":10037000000000000000000000000000000000007D\r\n" + ":10038000000000000000000000000000000000006D\r\n"
				+ ":10039000000000000000000000000000000000005D\r\n" + ":1003A000000000000000000000000000000000004D\r\n"
				+ ":1003B000000000000000000000000000000000003D\r\n" + ":1003C000000000000000000000000000000000002D\r\n"
				+ ":1003D000000000000000000000000000000000001D\r\n" + ":1003E000000000000000000000000000000000000D\r\n"
				+ ":1003F00000000000000000000000000000000000FD\r\n" + ":1004000000040020CF050020BD050020BF0500200E");
		lse = rle.encodeSymbol(ls);
		huf = HuffmanCode.buildCode(lse);
		Symbol.apply(huf);
		System.out.println("flat " + Symbol.length(ls) + "/" + ls.size() + "=" + (Symbol.length(ls) / ls.size())
				+ " : '" + ls + "'");
		System.out.println("rle  " + Symbol.length(lse) + "/" + lse.size() + "=" + (Symbol.length(lse) / lse.size())
				+ " : '" + lse + "'");

		assertTrue(lse.size() <= ls.size());// it should compress
		assertTrue(ls.size() == 763);
		assertTrue(lse.size() <= 277);// test performance

		lsc = rle.decodeSymbol(lse);
		assertEquals(ls, lsc);// test integrity of data

	}

	@Test
	public void testEncode() {
		// fail("Not yet implemented");
	}
	/*
	 * @Test public void testPIE() { { Tree<ISymbol,Long> tree=new
	 * Tree<ISymbol,Long>(); List<ISymbol> l=new ArrayList<ISymbol>();
	 * l.add(Symbol.findId('A')); l.add(Symbol.findId('B'));
	 * l.add(Symbol.findId('C')); l.add(Symbol.findId('D'));
	 * l.add(Symbol.findId('E')); l.add(Symbol.findId('A'));
	 * l.add(Symbol.findId('B')); l.add(Symbol.findId('E'));
	 * 
	 * // tree.getRoot().add(tree,0L,Long.valueOf(l.size()),l);
	 * tree.add(0L,Long.valueOf(l.size()),l);
	 * 
	 * // System.out.print(tree.getRoot().toString());
	 * assertEquals("'A''B''C''D''E''A''B''E'",tree.getRoot().toString().trim());
	 * tree=new Tree<ISymbol,Long>();
	 * 
	 * tree.getRoot().add(0L, Symbol.findId('A')); tree.getRoot().add(0L,
	 * Symbol.findId('B')); tree.getRoot().add(0L, Symbol.findId('C'));
	 * tree.getRoot().get( Symbol.findId('A')).add(0L, Symbol.findId('B')).add(0L,
	 * Symbol.findId('C')); tree.getRoot().get( Symbol.findId('A')).add(0L,
	 * Symbol.findId('C')); assertEquals( JavaUtils.asSortedString("\r\n" +
	 * 
	 * "'A''B''C'\r\n" + "'A''C'\r\n" + "'B'\r\n" + "'C'","\r\n"),
	 * JavaUtils.asSortedString(tree.getRoot().toString(),"\r\n"));
	 * 
	 * 
	 * } }
	 */

	/*
	 * @Test public void testDummy2() {
	 * 
	 * List<ISymbol> ls; TxtCompress txtc=new TxtCompress();
	 * 
	 * ls=FileSymbol.read("res/test/small_ref/pie.txt");
	 * 
	 * List<ISymbol> lsc = txtc.Encode(ls);
	 * 
	 * HuffmanCode cs = HuffmanCode.buildCode(ls);
	 * 
	 * FileSymbol.toArchive(lsc,cs, "res\\result.test\\test\\small_ref\\pie.txtc");
	 * 
	 * List<ISymbol> lsd=FileSymbol.fromArchive(null,
	 * "res\\result.test\\test\\small_ref\\pie.txtc"); txtc=new TxtCompress();
	 * List<ISymbol> lsf=txtc.Encode(lsd);
	 * FileSymbol.saveAs(FileSymbol.ExtractDataSymbol(lsf),
	 * "res/result.test/test/small_ref/pie2.txt"); assertEquals(JavaUtils.read(
	 * "res/test/small_ref/pie.txt"), JavaUtils.read(
	 * "res/result.test/test/small_ref/pie2.txt") );
	 * 
	 * 
	 * 
	 * }
	 */

	@Test
	public void testDummy() {
		// RLE.main(null);
		assertEquals(1, 1);
	}

	@Test
	public void testDecodeSymbolRLE() {
		// fail("Not yet implemented");
		IAlgoCompress rle = new RLE();
		// String text="test de compression
		// AAAAAAAAAAAAAAAAABBBBBBBBBBBBBSSSSSSSSSSSSSSSSSSSSSSSSSDDDDDDDDDDDDDDDDDDDDDDD\n";
		String text = "test de compression AAAAAAAAAAAAAAAAA CDCDCDCDCDCDCD test de compression AAAAAAAAAAAAAAAAA CDCDCDCDC";
		List<ISymbol> ls = Symbol.factoryCharSeq(text);
		// System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));

		List<ISymbol> lse = rle.encodeSymbol(ls);
		// System.out.println(lse.size()+"/"+ls.size());
		assertTrue(ls.size() >= lse.size());
		assertTrue(ls.size() - 25 > lse.size());
		System.out.println("RLE : ls=" + ls.size() + " => lse=" + lse.size());
		assertEquals("RLE compress rate ", true, ls.size() * 0.75 > lse.size());
		// System.out.println(new String(Symbol.listSymbolToCharSeq(lse)));
		ls = rle.decodeSymbol(lse);
		// System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));
		String text2 = new String(Symbol.listSymbolToCharSeq(ls));
		assertTrue(text.equals(text2));
	}

	@Test
	public void testEncodeSymbol() {
		testDecodeSymbolRLE();
		// fail("Not yet implemented");
	}

	@After // tearDown()
	public void after() throws Exception {

		// System.out.println("Running: tearDown");

		// assertEquals(JavaUtils.read(file),JavaUtils.read(file2));
	}

	@Before // setup()

	public void before() throws Exception {

		// System.out.println("Setting it up!");

	}
}
