package com.zoubworld.java.utils.compress.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.PIE.Tree;
import com.zoubworld.java.utils.compress.algo.IAlgoCompress;
import com.zoubworld.java.utils.compress.algo.PIEcompress;
import com.zoubworld.java.utils.compress.algo.RLE;
import com.zoubworld.java.utils.compress.file.FileSymbol;
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
	public void testPIE() {
		{	
		Tree<ISymbol,Long> tree=new Tree();
		List<ISymbol> l=new ArrayList();
		l.add(Symbol.findId('A'));
	l.add(Symbol.findId('B'));
	l.add(Symbol.findId('C'));
	l.add(Symbol.findId('D'));
	l.add(Symbol.findId('E'));
	l.add(Symbol.findId('A'));
	l.add(Symbol.findId('B'));
	l.add(Symbol.findId('E'));
	
//	tree.getRoot().add(tree,0L,Long.valueOf(l.size()),l);
		tree.add(0L,Long.valueOf(l.size()),l);
		
	//	System.out.print(tree.getRoot().toString());
		assertEquals("'A''B''C''D''E''A''B''E'",tree.getRoot().toString().trim());
		tree=new Tree();

			tree.getRoot().add(0L, Symbol.findId('A'));
			tree.getRoot().add(0L, Symbol.findId('B'));
			tree.getRoot().add(0L, Symbol.findId('C'));
			tree.getRoot().get( Symbol.findId('A')).add(0L, Symbol.findId('B')).add(0L, Symbol.findId('C'));
			tree.getRoot().get( Symbol.findId('A')).add(0L, Symbol.findId('C'));
		assertEquals(
				JavaUtils.asSortedString("\r\n" + 
				
				"'A''B''C'\r\n" + 
				"'A''C'\r\n" + 
				"'B'\r\n" + 
				"'C'","\r\n"),
				JavaUtils.asSortedString(tree.getRoot().toString(),"\r\n"));
		
		
	}
		
	{

		PIEcompress cmp=new PIEcompress();

		 List<ISymbol>  ls=FileSymbol.read("res/test/small_ref/pie.txt");
		 List<ISymbol>  lsc=cmp.compress(ls);
		
		
		FileSymbol.saveCompressedAs(lsc, "res/result.test/test/small_ref/pie.pie");
		 JavaUtils.saveAs("res/result.test/test/small_ref/pie.tree",cmp.getTree().toString());
			
		 
		 List<ISymbol>  lsd=cmp.uncompress(lsc);
		
		 FileSymbol.saveAs(FileSymbol.ExtractDataSymbol(lsd), "res/result.test/test/small_ref/pie.txt");
		assertEquals(JavaUtils.read("res/test/small_ref/pie.txt"),
					JavaUtils.read("res/result.test/test/small_ref/pie.txt")
					);
		
	}
	
	}
	
	@Test
	public void testDummy() {
	//RLE.main(null);
	assertEquals(1,1);
	}
	@Test
	public void testDecodeSymbol() {
		//fail("Not yet implemented");
		IAlgoCompress rle= new RLE();
		//String text="test de compression AAAAAAAAAAAAAAAAABBBBBBBBBBBBBSSSSSSSSSSSSSSSSSSSSSSSSSDDDDDDDDDDDDDDDDDDDDDDD\n";
		String text="test de compression AAAAAAAAAAAAAAAAA CDCDCDCDCDCDCD test de compression AAAAAAAAAAAAAAAAA CDCDCDCDC";
		List<ISymbol> ls=Symbol.factoryCharSeq(text);
	//	System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));
		
		List<ISymbol> lse=rle.encodeSymbol(ls);
	//	System.out.println(lse.size()+"/"+ls.size());
		assertTrue(ls.size()>=lse.size());
		assertTrue(ls.size()-25>lse.size());
	//	System.out.println(new String(Symbol.listSymbolToCharSeq(lse)));
		ls=rle.decodeSymbol(lse);
	//	System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));
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
