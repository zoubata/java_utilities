package com.zoubworld.java.utils.compress.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.algo.BWT;
import com.zoubworld.java.utils.compress.algo.MTF;


public class BWTTest {

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

	public void testBWTBasic(String text, int r) {
r=-2;
		BWT rle = new BWT();

		List<ISymbol> ls = Symbol.factoryCharSeq(text);
		// System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));

		List<ISymbol> lse = rle.encodeSymbol(ls);
		// System.out.println(lse.toString());

		System.out.println(lse.size() + ":" + ls.size());
		assertTrue(ls.size()+2 >= lse.size());
		assertTrue(ls.size() - r >= lse.size());
		List<ISymbol> ls2 = rle.decodeSymbol(lse);
		// System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));
		String text2 = new String(Symbol.listSymbolToCharSeq(ls2));
		System.out.println(lse.size() + "/" + ls.size());
		assertEquals(text, (text2));
		assertTrue(text.equals(text2));
	}

	@Test
	public void testMTF_Perf() {
		long timens = 180 * 1000 * 1000L;// 0.22s

		long nano_startTime = System.nanoTime();
		testBWTBasic(TestData.string1, 0);
		long nano_stopTime = System.nanoTime();
		System.out.print("duration :" + (nano_stopTime - nano_startTime) + " ns");
		assertTrue("speed perf", (nano_stopTime - nano_startTime) <= timens);// speed performance
		/*
		 * assertThat("speed perf", (nano_stopTime-nano_startTime), lessThan(timens));
		 */

	}

	@Test
	public void testMTFBasicAll() {
		long timens = 250 * 1000 * 1000L;// 0.15s

		testBWTBasic("11", 0);
		testBWTBasic("1", 0);
		testBWTBasic(
				"test de compression AAAAAAAAAAAAAAAAA CDCDCDCDCDCDCD test de compression AAAAAAAAAAAAAAAAA CDCDCDCDC\n",
				101 - 101);

		String s = "";
		for (int i = 0; i < 2048; i += 16)
			s += "0123456789ABCDEF";
		testBWTBasic(s, 2048 - 2048);

		for (int i = 0; i <= 2048; i += 10)
			s += "0123456789ABCDE\n";
		testBWTBasic(s, 5328 - 5328);
		s = "";
		for (int i = 0; i <= 2048; i += 10)
			s += "000005677777CDE\n";
		testBWTBasic(s, 3280 - 3280);
		s = "";
		for (int i = 0; i <= 2048; i += 10)
			s += "0000000000000000";
		testBWTBasic(s, 3280 - 3280);

		testBWTBasic(
				"klefnhatrytvzyeryyteyrretouizybrebyyelkjkdjfhgjksdnjkdsj,vvi,ouybiotruybiortuyioruyoirtyebetyryetberybre"
						+ "rtyeryteryreybetyreberybyiokemoiskherkhiuilhisunehkvjhlrkuthurzeioazertyuwsdfghjcvbn,rtycvdhjskqieozpahj"
						+ "vcbnxkg hcjxk tyucixow tvyfudiz cndjeio nvcjdkezo& ,ckdlsozpa ,;cldsmpz ,cdklazertyuisdfghjkxcvbpklbn,zb"
						+ "azertyuiopqqqqqqqqqqqqqqqqqsdfghjklmwxcvbn,azertyuiopsdfghjkxcvbvretczehcgbtkzjebgtckhekzbgnxkhegrhztghz"
						+ "wqaxszcdevfrbgtnhy,ju;ki:lo!mp^*$wqaxszxszcdecdevfrvfrbgtcdeznhy,juxsz;kiwq:lo!mpcdevfrcdzxsznhywqa,jun"
						+ "njibhuvgycftxdrwsewqawqzwsewsewszwsdcdevfdbchdun jcdienbjvkfdflwjkvcsnvhlrejkhtvlhy;kivfrcdenhycdexsz)",
				607 - 621);

		long nano_startTime = System.nanoTime();
		testBWTBasic(TestData.string1, 8722 - 9327);
		long nano_stopTime = System.nanoTime();
		System.out.println("duration :" + (nano_stopTime - nano_startTime) + " ns, budget : " + timens + " ns");
		assertTrue("speed perf", (nano_stopTime - nano_startTime) <= timens);// speed performance

	}

}
