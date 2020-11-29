package com.zoubworld.java.utils.compress.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.zoubworld.java.utils.compress.Code;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.algo.LZS;
import com.zoubworld.java.utils.compress.algo.LZW;
import com.zoubworld.java.utils.compress.algo.LZWBasic;
import com.zoubworld.utils.JavaUtils;

public class LZWTest {

	@Test
	public void testDecodeSymbol() {
		LZW rle = new LZW();
		String text = "AAAAAAAAAAACDCDCDCDCDCDCDCDCDCDCD";
		List<ISymbol> ls = Symbol.factoryCharSeq(text);
		System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));

		List<ISymbol> lse = new ArrayList<ISymbol>();

		lse.add(new Symbol('A'));
		lse.add(Symbol.PIE);
		lse.add(Symbol.FactorySymbolINT(0));
		lse.add(Symbol.FactorySymbolINT(10));
		lse.add(new Symbol('C'));
		lse.add(new Symbol('D'));
		lse.add(Symbol.PIE);
		lse.add(Symbol.FactorySymbolINT(11));
		lse.add(Symbol.FactorySymbolINT(20));

		System.out.println(new String(Symbol.listSymbolToCharSeq(lse)));
		// assertTrue(ls.size()>=lse.size());
		// assertTrue(ls.size()-60>lse.size());
		ls = rle.decodeSymbol(lse);
		System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));
		String text2 = new String(Symbol.listSymbolToCharSeq(ls));

		assertTrue(text.equals(text2));
	}

	@Test
	public void testEncodeSymbol() {

		LZW rle = new LZW();
		String text = "test de compression AAAAAAAAAAAAAAAAA CDCDCDCDCDCDCD test de compression AAAAAAAAAAAAAAAAA CDCDCDCDC\n";
		/*
		 * +
		 * "klefnhatrytvzyeryyteyrretouizybrebyyelkjkdjfhgjksdnjkdsj,vvi,ouybiotruybiortuyioruyoirtyebetyryetberybre"
		 * +
		 * "rtyeryteryreybetyreberybyiokemoiskherkhiuilhisunehkvjhlrkuthurzeioazertyuwsdfghjcvbn,rtycvdhjskqieozpahj"
		 * +
		 * "vcbnxkg hcjxk tyucixow tvyfudiz cndjeio nvcjdkezo& ,ckdlsozpa ,;cldsmpz ,cdklazertyuisdfghjkxcvbpklbn,zb"
		 * +
		 * "azertyuiopqqqqqqqqqqqqqqqqqsdfghjklmwxcvbn,azertyuiopsdfghjkxcvbvretczehcgbtkzjebgtckhekzbgnxkhegrhztghz"
		 * +
		 * "wqaxszcdevfrbgtnhy,ju;ki:lo!mp^*$wqaxszxszcdecdevfrvfrbgtcdeznhy,juxsz;kiwq:lo!mpcdevfrcdzxsznhywqa,jun"
		 * +
		 * "njibhuvgycftxdrwsewqawqzwsewsewszwsdcdevfdbchdun jcdienbjvkfdflwjkvcsnvhlrejkhtvlhy;kivfrcdenhycdexsz)"
		 * ;
		 */
		List<ISymbol> ls = Symbol.factoryCharSeq(text);
		System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));

		List<ISymbol> lse = rle.encodeSymbol(ls);
		System.out.println(new String(Symbol.listSymbolToCharSeq(lse)));
		assertTrue(ls.size() >= lse.size());
		assertTrue(ls.size() - 60 > lse.size());
		ls = rle.decodeSymbol(lse);
		System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));
		String text2 = new String(Symbol.listSymbolToCharSeq(ls));
		System.out.println(lse.size() + "/" + ls.size());

		assertTrue(text.equals(text2));

	}

	@Test
	public void testLZWBasic() {

		{
			LZWBasic rle = new LZWBasic();

			String text = "test de compression AAAAAAAAAAAAAAAAA CDCDCDCDCDCDCD test de compression AAAAAAAAAAAAAAAAA CDCDCDCDC\n";
			/*
			 * +
			 * "klefnhatrytvzyeryyteyrretouizybrebyyelkjkdjfhgjksdnjkdsj,vvi,ouybiotruybiortuyioruyoirtyebetyryetberybre"
			 * +
			 * "rtyeryteryreybetyreberybyiokemoiskherkhiuilhisunehkvjhlrkuthurzeioazertyuwsdfghjcvbn,rtycvdhjskqieozpahj"
			 * +
			 * "vcbnxkg hcjxk tyucixow tvyfudiz cndjeio nvcjdkezo& ,ckdlsozpa ,;cldsmpz ,cdklazertyuisdfghjkxcvbpklbn,zb"
			 * +
			 * "azertyuiopqqqqqqqqqqqqqqqqqsdfghjklmwxcvbn,azertyuiopsdfghjkxcvbvretczehcgbtkzjebgtckhekzbgnxkhegrhztghz"
			 * +
			 * "wqaxszcdevfrbgtnhy,ju;ki:lo!mp^*$wqaxszxszcdecdevfrvfrbgtcdeznhy,juxsz;kiwq:lo!mpcdevfrcdzxsznhywqa,jun"
			 * +
			 * "njibhuvgycftxdrwsewqawqzwsewsewszwsdcdevfdbchdun jcdienbjvkfdflwjkvcsnvhlrejkhtvlhy;kivfrcdenhycdexsz)"
			 * ;
			 */

			List<Integer> li = rle.compress(text);
			String s2 = rle.decompress(li);
			assertEquals(text, s2);

			List<ISymbol> ls = Symbol.factoryCharSeq(text);
			System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));

			List<ISymbol> lse = rle.encodeSymbol(ls);
			System.out.println(new String(Symbol.listSymbolToCharSeq(lse)));

			System.out.println(lse.size() + ":" + ls.size());
			assertTrue(ls.size() >= lse.size());
			assertTrue(ls.size() - 48 > lse.size());
			ls = rle.decodeSymbol(lse);
			System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));
			String text2 = new String(Symbol.listSymbolToCharSeq(ls));
			System.out.println(lse.size() + "/" + ls.size());

			assertTrue(text.equals(text2));
		}
		// test 2
		{
			LZW comptool = new LZW();
			String text = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
			List<ISymbol> ls = Symbol.factoryCharSeq(text);
			List<ISymbol> lse = comptool.encodeSymbol(ls);
			File file2 = new File("res/test/basic.txt" + ".lzwtxtdec");
			File file3 = new File("res/test/basic.txt" + ".lzwdec");
			File filec = new File("res/test/basic.txt" + ".lzw");
			Code.reworkCode(lse, 32);
			Symbol.listSymbolToFile(lse, filec.getAbsolutePath(), 32);

			Symbol.listSymbolToFile(ls, file2.getAbsolutePath(), 8);
			ls = comptool.decodeSymbol(lse);
			Code.reworkCode(lse, 8);
			Symbol.listSymbolToFile(ls, file3.getAbsolutePath(), 8);
			Symbol.listSymbolToFile(ls, file3.getAbsolutePath(), 8);

		}
/*
		{
			LZWBasic rle = new LZWBasic();

			List<Integer> li;

			li = new ArrayList<Integer>();
			li.add(987654);
			li.add(98764);
			li.add(9876);
			li.add(98765);
			boolean b = false;
			try {
				expectedEx.expect(IllegalArgumentException.class);

				// do something that should throw the exception...
				String s2 = rle.decompress(li);
			} catch (IllegalArgumentException e) {
				b = true;
			}
			assertTrue(b);
			expectedEx.expect(IllegalArgumentException.class);

			List<ISymbol> cmp = new ArrayList<ISymbol>();
			cmp.add(Symbol.FactorySymbolINT(123));
			cmp.add(Symbol.FactorySymbolINT(26));
			cmp.add(Symbol.FactorySymbolINT(76512345));
			cmp.add(Symbol.FactorySymbolINT(19802345));
			// do something that should throw the exception...
			List<ISymbol> s2 = rle.decodeSymbol(cmp);

		}*/

	}

	@Test
	public void testbig() {
		File file = new File("res/test/smallfile.txt");
		File file2 = new File("res/test/smallfile.txt" + ".lzwdec");
		File filec = new File("res/test/smallfile.txt" + ".lzw");
		System.out.println(file.getAbsolutePath());
		LZW cmp = new LZW();
	//	String inputFile = "";
		List<ISymbol> ls = Symbol.factoryFile(file.getAbsolutePath());
		List<ISymbol> lse = cmp.encodeSymbol(ls);
		System.out.println(filec.getAbsolutePath());

		System.out.println("LZW : ls=" + ls.size() + " => lse=" + lse.size());
		assertEquals("LZW compress rate ", true, ls.size() * 0.54 > lse.size());

		Code.reworkCode(lse, 32);
		Symbol.listSymbolToFile(lse, filec.getAbsolutePath(), 32);

		ls = cmp.decodeSymbol(lse);
		Code.reworkCode(lse, 8);

		Symbol.listSymbolToFile(ls, file2.getAbsolutePath(), 8);
		// System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));
		// String text2=new String(Symbol.listSymbolToCharSeq(ls));
		System.out.println(lse.size() + "/" + ls.size());

		assertTrue(file.length() == file2.length());
		String s1 = JavaUtils.read(file);
		String s2 = JavaUtils.read(file2);
		assertArrayEquals(s1.toCharArray(), s2.toCharArray());

	}

	@Test
	public void testDummy() {
		LZW.main(null);
		assertEquals(1, 1);
	}

	@After // tearDown()
	public void after() throws Exception {

		// System.out.println("Running: tearDown");

	}

	@Before // setup()

	public void before() throws Exception {

		// System.out.println("Setting it up!");

	}
}
