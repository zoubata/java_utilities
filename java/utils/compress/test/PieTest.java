package com.zoubworld.java.utils.compress.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.algo.PIEcompress;
import com.zoubworld.java.utils.compress.file.FileSymbol;
import com.zoubworld.utils.JavaUtils;

public class PieTest {

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

	public void testPIEBasic(String text, int r) {

		PIEcompress cmp = new PIEcompress();

		List<ISymbol> ls = Symbol.factoryCharSeq(text);
		// System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));

		List<ISymbol> lse = cmp.encodeSymbol(ls);
		// System.out.println(lse.toString());

		System.out.println(lse.size() + ":" + ls.size());
		assertTrue(ls.size() >= lse.size());
		assertTrue(ls.size() - r >= lse.size());
		List<ISymbol> ls2 = cmp.decodeSymbol(lse);
		// System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));
		String text2 = new String(Symbol.listSymbolToCharSeq(ls2));
		System.out.println(lse.size() + "/" + ls.size());
		assertEquals(text, (text2));
		assertTrue(text.equals(text2));
	}

	@Test
	public void testPIE_Perf() {
		long timens = 180 * 1000 * 1000L;// 0.22s

		long nano_startTime = System.nanoTime();
		testPIEBasic(TestData.string1, 0);
		long nano_stopTime = System.nanoTime();
		System.out.print("duration :" + (nano_stopTime - nano_startTime) + " ns, expected" + timens + "ns");
		assertTrue("speed perf", (nano_stopTime - nano_startTime) <= timens);// speed performance
		/*
		 * assertThat("speed perf", (nano_stopTime-nano_startTime), lessThan(timens));
		 */

	}

	@Test
	public void testPIEBasicAll() {
		long timens = 150 * 1000 * 1000L;// 0.15s

		testPIEBasic("12", 0);
		testPIEBasic("1", 0);
		testPIEBasic(
				"test de compression AAAAAAAAAAAAAAAAA CDCDCDCDCDCDCD test de compression AAAAAAAAAAAAAAAAA CDCDCDCDC\n",
				101 - 89);

		String s = "";
		for (int i = 0; i < 2048; i += 16)
			s += "0123456789ABCDEF";
		testPIEBasic(s, 2048 - 709);

		for (int i = 0; i <= 2048; i += 10)
			s += "0123456789ABCDE\n";
		testPIEBasic(s, 5328 - 1275);
		s = "";
		for (int i = 0; i <= 2048; i += 10)
			s += "000005677777CDE\n";
		testPIEBasic(s, 3280 - 884);
		s = "";
		for (int i = 0; i <= 2048; i += 10)
			s += "0000000000000000";
		testPIEBasic(s, 3280 - 241);

		testPIEBasic(
				"klefnhatrytvzyeryyteyrretouizybrebyyelkjkdjfhgjksdnjkdsj,vvi,ouybiotruybiortuyioruyoirtyebetyryetberybre"
						+ "rtyeryteryreybetyreberybyiokemoiskherkhiuilhisunehkvjhlrkuthurzeioazertyuwsdfghjcvbn,rtycvdhjskqieozpahj"
						+ "vcbnxkg hcjxk tyucixow tvyfudiz cndjeio nvcjdkezo& ,ckdlsozpa ,;cldsmpz ,cdklazertyuisdfghjkxcvbpklbn,zb"
						+ "azertyuiopqqqqqqqqqqqqqqqqqsdfghjklmwxcvbn,azertyuiopsdfghjkxcvbvretczehcgbtkzjebgtckhekzbgnxkhegrhztghz"
						+ "wqaxszcdevfrbgtnhy,ju;ki:lo!mp^*$wqaxszxszcdecdevfrvfrbgtcdeznhy,juxsz;kiwq:lo!mpcdevfrcdzxsznhywqa,jun"
						+ "njibhuvgycftxdrwsewqawqzwsewsewszwsdcdevfdbchdun jcdienbjvkfdflwjkvcsnvhlrejkhtvlhy;kivfrcdenhycdexsz)",
				621 - 610);

		long nano_startTime = System.nanoTime();
		testPIEBasic(TestData.string1, 9347 - 6350);
		long nano_stopTime = System.nanoTime();
		System.out.println("duration :" + (nano_stopTime - nano_startTime) + " ns, budget : " + timens + " ns");
		assertTrue("speed perf", (nano_stopTime - nano_startTime) <= timens);// speed performance

	}

	@Test
	public void testPIEcompress() {

		File fc = new File("res/result.test/test/small_ref/pie3.pie");
		File ff = new File("res/test/small_ref/pie.txt");

		PIEcompress cmp = new PIEcompress();

		List<ISymbol> ls = FileSymbol.read(ff.getAbsolutePath());
		List<ISymbol> lsc = cmp.encodeSymbol(ls);
		System.out.println("PIE : lf=" + ls.size() + "> lc=" + lsc.size());
		System.out.println("sf=" + Symbol.length(ls) + "> sc=" + Symbol.length(lsc));

		assertEquals("PIE compress rate ", true, ls.size() * 0.699 > lsc.size());
		// the goal is to be clearly smaller than before in symbol count
		// depending of coding defaul is 16bits assertEquals(true,
		// Symbol.length(ls)>Symbol.length(lsc));

		FileSymbol.saveCompressedAs(lsc, fc.getAbsolutePath());
		JavaUtils.saveAs("res/result.test/test/small_ref/pie3.tree", cmp.getTree().toString());

		List<ISymbol> lsd = cmp.decodeSymbol(lsc);
		System.out.println("ff=" + ff.length() + "> fc=" + fc.length());
		// assertEquals(true, ff.length()>fc.length());
		FileSymbol.saveAs(FileSymbol.ExtractDataSymbol(lsd), "res/result.test/test/small_ref/pie2.txt");
		assertEquals(JavaUtils.read("res/test/small_ref/pie.txt"),
				JavaUtils.read("res/result.test/test/small_ref/pie2.txt"));

	}

}
