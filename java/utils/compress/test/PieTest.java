package com.zoubworld.java.utils.compress.test;

import static org.junit.Assert.*;

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


	@Test
	public void testPIEcompress() {

		File fc=new File("res/result.test/test/small_ref/pie3.pie");
		 File ff=new File("res/test/small_ref/pie.txt");
		 
	PIEcompress cmp=new PIEcompress();

	 List<ISymbol>  ls=FileSymbol.read(ff.getAbsolutePath());
	 List<ISymbol>  lsc=cmp.compress(ls);
	System.out.println("PIE : lf="+ls.size()+"> lc="+lsc.size());
	System.out.println("sf="+Symbol.length(ls)+"> sc="+Symbol.length(lsc));
	 
	 assertEquals("PIE compress rate ",true, ls.size()*0.699>lsc.size());
	 //the goal is to be clearly smaller than before in symbol count
//depending of coding defaul is 16bits	 assertEquals(true, Symbol.length(ls)>Symbol.length(lsc));
			
	FileSymbol.saveCompressedAs(lsc, fc.getAbsolutePath());
	 JavaUtils.saveAs(               "res/result.test/test/small_ref/pie3.tree",cmp.getTree().toString());
		
	 
	 List<ISymbol>  lsd=cmp.uncompress(lsc);
	 System.out.println("ff="+ff.length()+"> fc="+fc.length());
//	 assertEquals(true, ff.length()>fc.length());
	 FileSymbol.saveAs(FileSymbol.ExtractDataSymbol(lsd), "res/result.test/test/small_ref/pie2.txt");
	assertEquals(JavaUtils.read(                          "res/test/small_ref/pie.txt"),
				JavaUtils.read(                           "res/result.test/test/small_ref/pie2.txt")
				);
	
}


}
