package com.zoubworld.java.utils.compress.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.RLE;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.utils.JavaUtils;

public class RLETest {

	@Test
	public void testDecode() {
	//	fail("Not yet implemented");
	}

	@Test
	public void testEncode() {
	//	fail("Not yet implemented");
	}
	@Test
	public void testDummy() {
	RLE.main(null);
	assertEquals(1,1);
	}
	@Test
	public void testDecodeSymbol() {
		//fail("Not yet implemented");
		RLE rle= new RLE();
		//String text="test de compression AAAAAAAAAAAAAAAAABBBBBBBBBBBBBSSSSSSSSSSSSSSSSSSSSSSSSSDDDDDDDDDDDDDDDDDDDDDDD\n";
		String text="test de compression AAAAAAAAAAAAAAAAA CDCDCDCDCDCDCD test de compression AAAAAAAAAAAAAAAAA CDCDCDCDC";
		List<ISymbol> ls=Symbol.factoryCharSeq(text);
		System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));
		
		List<ISymbol> lse=rle.encodeSymbol(ls);
		System.out.println(lse.size()+"/"+ls.size());
		assertTrue(ls.size()>=lse.size());
		assertTrue(ls.size()-25>lse.size());
		System.out.println(new String(Symbol.listSymbolToCharSeq(lse)));
		ls=rle.decodeSymbol(lse);
		System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));
		String text2=new String(Symbol.listSymbolToCharSeq(ls));
		assertTrue(text.equals(text2));
	}



	@Test
	public void testEncodeSymbol() {
		testDecodeSymbol();
		//fail("Not yet implemented");
	}

	 @After // tearDown()
	    public void after() throws Exception {

  //    System.out.println("Running: tearDown");

//			assertEquals(JavaUtils.read(file),JavaUtils.read(file2));
  }
@Before // setup()

  public void before() throws Exception {

   //  System.out.println("Setting it up!");


  }
}
