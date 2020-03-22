package com.zoubworld.java.utils.compress.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.algo.LZS;
import com.zoubworld.java.utils.compress.algo.LZSe;

public class LZSTest {

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

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	public void testLZSBasic(Integer size,Integer Len,String text, int r) {

		LZS lzs ;
		if(size!=null)
			lzs = new LZSe(size,Len);
		else
			lzs = new LZS();
		List<ISymbol> ls = Symbol.factoryCharSeq(text);
		// System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));

		List<ISymbol> lse = lzs.encodeSymbol(ls);
		// System.out.println(lse.toString());

		System.out.println(lse.size() + ":" + ls.size());
		assertTrue(ls.size() >= lse.size());
		assertTrue(ls.size() - r >= lse.size());
		ls = lzs.decodeSymbol(lse);
		// System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));
		String text2 = new String(Symbol.listSymbolToCharSeq(ls));
		System.out.println(lse.size() + "/" + ls.size());
		assertEquals(text, (text2));
		assertTrue(text.equals(text2));
	}

	@Test
	public void testLZSBasicAll() {
		long timens = 350 * 1000 * 1000L;// 0.15s

		testLZSBasic(null,null,"11", 0);
		testLZSBasic(null,null,"1", 0);
		testLZSBasic(
				null,null,"test de compression AAAAAAAAAAAAAAAAA CDCDCDCDCDCDCD test de compression AAAAAAAAAAAAAAAAA CDCDCDCDC\n",
				101-30);

		String s = "";
		for (int i = 0; i < 2048; i += 16)
			s += "0123456789ABCDEF";
		testLZSBasic(null,null,s, 2048 - 71);
		testLZSBasic(8192,512,s, 2048 - 20);

		for (int i = 0; i <= 2048; i += 10)
			s += "0123456789ABCDE\n";
		testLZSBasic(null,null,s, 5328 - 162);
		testLZSBasic(8192,512,s, 5328 - 29);
		
		testLZSBasic(null,null,TestData.string3, 25726 - 1268);
		testLZSBasic(null,null,TestData.string4, 11372 - 3086);
		testLZSBasic(null,null,TestData.string5, 5427 - 1288);
//save cpu		testLZSBasic(null,null,TestData.string2, 38394 - 8025);
		testLZSBasic(256,38,TestData.string2, 38394 - 11220);
//save cpu		testLZSBasic(8192,256,TestData.string2, 38394 - 6799);
//save cpu		testLZSBasic(65536,256,TestData.string2, 38394 - 6331);
		
		testLZSBasic(null,null,
				"klefnhatrytvzyeryyteyrretouizybrebyyelkjkdjfhgjksdnjkdsj,vvi,ouybiotruybiortuyioruyoirtyebetyryetberybre"
						+ "rtyeryteryreybetyreberybyiokemoiskherkhiuilhisunehkvjhlrkuthurzeioazertyuwsdfghjcvbn,rtycvdhjskqieozpahj"
						+ "vcbnxkg hcjxk tyucixow tvyfudiz cndjeio nvcjdkezo& ,ckdlsozpa ,;cldsmpz ,cdklazertyuisdfghjkxcvbpklbn,zb"
						+ "azertyuiopqqqqqqqqqqqqqqqqqsdfghjklmwxcvbn,azertyuiopsdfghjkxcvbvretczehcgbtkzjebgtckhekzbgnxkhegrhztghz"
						+ "wqaxszcdevfrbgtnhy,ju;ki:lo!mp^*$wqaxszxszcdecdevfrvfrbgtcdeznhy,juxsz;kiwq:lo!mpcdevfrcdzxsznhywqa,jun"
						+ "njibhuvgycftxdrwsewqawqzwsewsewszwsdcdevfdbchdun jcdienbjvkfdflwjkvcsnvhlrejkhtvlhy;kivfrcdenhycdexsz)",
				621 - 357);

		long nano_startTime = System.nanoTime();
		testLZSBasic(null,null,TestData.string1, 9347 - 1972);
		long nano_stopTime = System.nanoTime();
		System.out.print("duration :" + (nano_stopTime - nano_startTime) / 2 + " ns, excpected<" + timens + " ns");
		assertTrue("speed perf", (nano_stopTime - nano_startTime) / 2 <= timens);// speed performance

	}


}
