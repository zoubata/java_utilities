package com.zoubworld.java.utils.compress.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.algo.ByteTripleEncoding;
import com.zoubworld.java.utils.compress.algo.LZWBasic;

public class BTETest {

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

	public void testBTEBasic(String text, int r) {

		ByteTripleEncoding rle = new ByteTripleEncoding();

		List<ISymbol> ls = Symbol.factoryCharSeq(text);
		// System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));

		List<ISymbol> lse = rle.encodeSymbol(ls);
		// System.out.println(lse.toString());

		System.out.println(lse.size() + ":" + ls.size());
		assertTrue(ls.size() >= lse.size());
		assertTrue(ls.size() - r >= lse.size());
		List<ISymbol> ls2 = rle.decodeSymbol(lse);
		// System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));
		String text2 = new String(Symbol.listSymbolToCharSeq(ls2));
		System.out.println(lse.size() + "/" + ls.size());
		assertEquals(text, (text2));
		assertTrue(text.equals(text2));
	}

	@Test
	public void testBTE_Perf() {
		long timens = 280 * 1000 * 1000L;// 0.22s

		long nano_startTime = System.nanoTime();
		testBTEBasic(LZWBasic.file, 0);
		long nano_stopTime = System.nanoTime();
		System.out.println("duration :" + (nano_stopTime - nano_startTime) + " ns, budget : " + timens + " ns");
		assertTrue("speed perf", (nano_stopTime - nano_startTime) <= timens);// speed performance
		/*
		 * assertThat("speed perf", (nano_stopTime-nano_startTime), lessThan(timens));
		 */

	}

	@Test
	public void testBteBasicAll() {
		long timens = 450 * 1000 * 1000L;// 0.15s

		// testBTEBasic( "11",0);
		// testBTEBasic( "1",0);
		testBTEBasic("1234123512361237412412", 22 - 17);
		testBTEBasic(
				"test de compression AAAAAAAAAAAAAAAAA CDCDCDCDCDCDCD test de compression AAAAAAAAAAAAAAAAA CDCDCDCDC\n",
				101 - 86);

		String s = "";
		for (int i = 0; i < 2048; i += 16)
			s += "0123456789ABCDEF";
		testBTEBasic(s, 2048 - 1413);

		for (int i = 0; i <= 2048; i += 10)
			s += "0123456789ABCDE\n";
		testBTEBasic(s, 5328 - 3669);
		s = "";
		for (int i = 0; i <= 2048; i += 10)
			s += "000005677777CDE\n";
		testBTEBasic(s, 3280 - 2260);
		s = "";
		for (int i = 0; i <= 2048; i += 10)
			s += "0000000000000000";
		testBTEBasic(s, 3280 - 3280);

		testBTEBasic(
				"klefnhatrytvzyeryyteyrretouizybrebyyelkjkdjfhgjksdnjkdsj,vvi,ouybiotruybiortuyioruyoirtyebetyryetberybre"
						+ "rtyeryteryreybetyreberybyiokemoiskherkhiuilhisunehkvjhlrkuthurzeioazertyuwsdfghjcvbn,rtycvdhjskqieozpahj"
						+ "vcbnxkg hcjxk tyucixow tvyfudiz cndjeio nvcjdkezo& ,ckdlsozpa ,;cldsmpz ,cdklazertyuisdfghjkxcvbpklbn,zb"
						+ "azertyuiopqqqqqqqqqqqqqqqqqsdfghjklmwxcvbn,azertyuiopsdfghjkxcvbvretczehcgbtkzjebgtckhekzbgnxkhegrhztghz"
						+ "wqaxszcdevfrbgtnhy,ju;ki:lo!mp^*$wqaxszxszcdecdevfrvfrbgtcdeznhy,juxsz;kiwq:lo!mpcdevfrcdzxsznhywqa,jun"
						+ "njibhuvgycftxdrwsewqawqzwsewsewszwsdcdevfdbchdun jcdienbjvkfdflwjkvcsnvhlrejkhtvlhy;kivfrcdenhycdexsz)",
				621 - 549);

		long nano_startTime = System.nanoTime();
		testBTEBasic(LZWBasic.file, 9327 - 6644);
		long nano_stopTime = System.nanoTime();
		System.out.println("duration :" + (nano_stopTime - nano_startTime) + " ns, budget : " + timens + " ns");
		assertTrue("speed perf", (nano_stopTime - nano_startTime) <= timens);// speed performance

	}

}
