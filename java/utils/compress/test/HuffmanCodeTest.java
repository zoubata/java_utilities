/**
 * 
 */
package com.zoubworld.java.utils.compress.test;


import java.io.File;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.zoubworld.java.utils.compress.HuffmanCode;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.file.BinaryStdIn;
import com.zoubworld.java.utils.compress.file.BinaryStdOut;
import com.zoubworld.utils.JavaUtils;

/**
 * @author zoubata
 *
 */
public class HuffmanCodeTest {

	@Test
	public void testDummy() {
	//HuffmanCode.main(null);
	assertEquals(1,1);
	}
	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.HuffmanCode#compress()}.
	 */
	@Test
	public void testCompress() {

		System.out.println("testCompress");
		HuffmanCode huff=new HuffmanCode();
		File file = new File("res/test/smallfile.txt");
		File file2 = new File("res/test/smallfile.txt"+".hufdec");
		File filec = new File("res/test/smallfile.txt"+".huf");

		huff.binaryStdIn=new BinaryStdIn(file);
    	huff.binaryStdOut= new BinaryStdOut(filec);    	
    	System.out.println(" huff.compress();");
 huff.compress();

 huff.printFreqs();
 huff.printCodes();
 
	huff.binaryStdIn=new BinaryStdIn(filec);
	huff.binaryStdOut= new BinaryStdOut(file2);    	
	System.out.println(" huff.expand();");
 huff.expand();
 huff.printFreqs();
 huff.printCodes();
    
	

	assertTrue(file.length()==file2.length());
	assertArrayEquals(JavaUtils.read(file).toCharArray(),JavaUtils.read(file2).toCharArray());
	}

	
	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.HuffmanCode#encodeSymbol(java.util.List, com.zoubwolrd.java.utils.compress.file.BinaryStdOut)}.
	 */
	@Test
	public void testEncodeSymbol() {
		System.out.println("testEncodeSymbol");
		HuffmanCode huff=new HuffmanCode();
		File file = new File("res/test/smallfile.txt");
		File fileCRef = new File("res/test/smallfile.txt"+".hufref");
		File filec = new File("res/test/smallfile.txt"+".huf3");
		File file2 = new File("res/test/smallfile.txt"+".hufdec3");
		
		System.out.println("huff.encodeSymbol(...)");
		List<ISymbol> ldec=Symbol.factoryFile(file.getAbsolutePath());
		
		huff.encodeSymbol(ldec,new BinaryStdOut(filec));

		 huff.printFreqs();
		 huff.printCodes();
		
		 assertEquals(filec.length(),fileCRef.length());
         assertEquals(JavaUtils.read(filec),JavaUtils.read(fileCRef));
 
         System.out.println("huff.decodeSymbol(...)");

		 huff=new HuffmanCode();
	    	List<ISymbol> ls=huff.decodeSymbol( new BinaryStdIn(fileCRef.getAbsolutePath())    	);
	    	Symbol.initCode();//reset coding to uncompress	    			
	    	Symbol.listSymbolToFile(ls,file2.getAbsolutePath(),8);
	    	huff.printFreqs();
		    huff.printCodes();
		    	assertTrue(file.length()==file2.length());

	    	assertEquals(JavaUtils.read(file),JavaUtils.read(file2));
	    	
	    	
	    

		
	}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.HuffmanCode#decodeSymbol(com.zoubwolrd.java.utils.compress.file.BinaryStdIn)}.
	 */
	@Test
	public void testDecodeSymbol() {
		
		System.out.println("testDecodeSymbol");
		HuffmanCode huff=new HuffmanCode();
		File file = new File("res/test/smallfile.txt");
		File file2 = new File("res/test/smallfile.txt"+".hufdec4");
		File fileCRef = new File("res/test/smallfile.txt"+".hufref");

	//	huff.binaryStdIn=new BinaryStdIn(file);
    //	huff.binaryStdOut= new BinaryStdOut(filec);    	

    	List<ISymbol> ls=huff.decodeSymbol( new BinaryStdIn(fileCRef.getAbsolutePath())    	);
    	Symbol.initCode();//reset coding to uncompress	    			
    	Symbol.listSymbolToFile(ls,file2.getAbsolutePath(),8);
    	assertTrue(file.length()==file2.length());

    	assertEquals(JavaUtils.read(file),JavaUtils.read(file2));

	}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.HuffmanCode#expand()}.
	 */
	@Test
	public void testExpand() {
		testCompress();
	}

}
