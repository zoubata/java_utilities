/**
 * 
 */
package com.zoubworld.java.utils.compress.test;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.zoubworld.java.utils.compress.CodingSet;
import com.zoubworld.java.utils.compress.HuffmanCode;
import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.file.BinaryStdIn;
import com.zoubworld.java.utils.compress.file.BinaryStdOut;
import com.zoubworld.java.utils.compress.file.FileSymbol;
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
	
	@Test
	public void testCodingRule() {
		File file = new File("res/test/smallfile.txt");
		FileSymbol fs=new FileSymbol(file);
		List<ISymbol> ldec;
		ICodingRule cr=HuffmanCode.buildCode(ldec=fs.toSymbol());
		assertEquals(Symbol.findId('<'),cr.get(cr.get(Symbol.findId('<'))));
		assertEquals(Symbol.findId('a'),cr.get(cr.get(Symbol.findId('a'))));
		assertEquals(Symbol.findId('A'),cr.get(cr.get(Symbol.findId('A'))));
		
		
		/*cr.get(code);
		cr.get(sym);
		cr.getCode(binaryStdIn);
		cr.getSymbol(binaryStdIn);
		cr.writeCodingRule(binaryStdOut);*/
		String code="HuffManCode(--- Printing Codes ---\n" + 
				"'r'	: (0x0 	,4),0b0000	\n"+ 
				"'p'	: (0x2 	,5),0b00010	\n" + 
				"'U'	: (0x60 	,10),0b0001100000	\n" + 
				"'9'	: (0x61 	,10),0b0001100001	\n" + 
				"']'	: (0x62 	,10),0b0001100010	\n" + 
				"'['	: (0x63 	,10),0b0001100011	\n" + 
				"'B'	: (0x32 	,9),0b000110010	\n" + 
				"'I'	: (0x33 	,9),0b000110011	\n" + 
				"'w'	: (0xd 	,7),0b0001101	\n" + 
				"'N'	: (0x38 	,9),0b000111000	\n" + 
				"'G'	: (0x39 	,9),0b000111001	\n" + 
				"'W'	: (0x3a 	,9),0b000111010	\n" + 
				"'3'	: (0x76 	,10),0b0001110110	\n" + 
				"'V'	: (0xee 	,11),0b00011101110	\n" + 
				"'''	: (0xef 	,11),0b00011101111	\n" + 
				"'A'	: (0x1e 	,8),0b00011110	\n" + 
				"'q'	: (0x3e 	,9),0b000111110	\n" + 
				"';'	: (0xfc 	,11),0b00011111100	\n" + 
				"'+'	: (0x3f4 	,13),0b0001111110100	\n" + 
				"'$'	: (0x3f5 	,13),0b0001111110101	\n" + 
				"'\\'	: (0x1fb 	,12),0b000111111011	\n" + 
				"'4'	: (0x7f 	,10),0b0001111111	\n" + 
				"'n'	: (0x2 	,4),0b0010	\n" + 
				"'d'	: (0x6 	,5),0b00110	\n" + 
				"'m'	: (0x7 	,5),0b00111	\n" + 
				"'h'	: (0x8 	,5),0b01000	\n" + 
				"'0'	: (0x48 	,8),0b01001000	\n" + 
				"'T'	: (0x49 	,8),0b01001001	\n" + 
				"','	: (0x25 	,7),0b0100101	\n" + 
				"'g'	: (0x13 	,6),0b010011	\n" + 
				"'a'	: (0x5 	,4),0b0101	\n" + 
				"'i'	: (0x6 	,4),0b0110	\n" + 
				"'c'	: (0xe 	,5),0b01110	\n" + 
				"'Z'	: (0xf0 	,9),0b011110000	\n" + 
				"'R'	: (0xf1 	,9),0b011110001	\n" + 
				"'L'	: (0x79 	,8),0b01111001	\n" + 
				"'E'	: (0xf4 	,9),0b011110100	\n" + 
				"'?'	: (0xf5 	,9),0b011110101	\n" + 
				"'C'	: (0x7b 	,8),0b01111011	\n" + 
				"'f'	: (0x1f 	,6),0b011111	\n" + 
				"'t'	: (0x8 	,4),0b1000	\n" + 
				"'o'	: (0x9 	,4),0b1001	\n" + 
				"'s'	: (0xa 	,4),0b1010	\n" + 
				"'l'	: (0x16 	,5),0b10110	\n" + 
				"'D'	: (0x170 	,9),0b101110000	\n" + 
				"'X'	: (0xb88 	,12),0b101110001000	\n" + 
				"0x104	: (0x5c48 	,15),0b101110001001000	\n" + 
				"0x106	: (0x5c49 	,15),0b101110001001001	\n" + 
				"0x10d	: (0x5c4a 	,15),0b101110001001010	\n" + 
				"'<'	: (0x5c4b 	,15),0b101110001001011	\n" + 
				"0x10e	: (0x5c4c 	,15),0b101110001001100	\n" + 
				"'#'	: (0x5c4d 	,15),0b101110001001101	\n" + 
				"'&'	: (0x5c4e 	,15),0b101110001001110	\n" + 
				"0xb5	: (0x5c4f 	,15),0b101110001001111	\n" + 
				"0x 9	: (0x5c5 	,11),0b10111000101	\n" + 
				"'8'	: (0x5c6 	,11),0b10111000110	\n" + 
				"':'	: (0x5c7 	,11),0b10111000111	\n" + 
				"'k'	: (0xb9 	,8),0b10111001	\n" + 
				"'.'	: (0x5d 	,7),0b1011101	\n" + 
				"'u'	: (0x2f 	,6),0b101111	\n" + 
				"' '	: (0x6 	,3),0b110	\n" + 
				"'\"'	: (0x1c0 	,9),0b111000000	\n" + 
				"'H'	: (0x382 	,10),0b1110000010	\n" + 
				"'O'	: (0x706 	,11),0b11100000110	\n" + 
				"0xae	: (0x7070 	,15),0b111000001110000	\n" + 
				"0x10f	: (0x7071 	,15),0b111000001110001	\n" + 
				"'%'	: (0x3839 	,14),0b11100000111001	\n" + 
				"'j'	: (0x1c1d 	,13),0b1110000011101	\n" + 
				"'K'	: (0xe0f 	,12),0b111000001111	\n" + 
				"'F'	: (0x1c2 	,9),0b111000010	\n" + 
				"'-'	: (0x1c3 	,9),0b111000011	\n" + 
				"'b'	: (0x71 	,7),0b1110001	\n" + 
				"'7'	: (0x390 	,10),0b1110010000	\n" + 
				"'z'	: (0x391 	,10),0b1110010001	\n" + 
				"')'	: (0x1c9 	,9),0b111001001	\n" + 
				"'('	: (0x1ca 	,9),0b111001010	\n" + 
				"'M'	: (0x1cb 	,9),0b111001011	\n" + 
				"'x'	: (0x1cc 	,9),0b111001100	\n" + 
				"'1'	: (0x1cd 	,9),0b111001101	\n" + 
				"'2'	: (0x1ce 	,9),0b111001110	\n" + 
				"'S'	: (0x1cf 	,9),0b111001111	\n" + 
				"'6'	: (0x740 	,11),0b11101000000	\n" + 
				"'J'	: (0x741 	,11),0b11101000001	\n" + 
				"'/'	: (0xe84 	,12),0b111010000100	\n" + 
				"'Q'	: (0xe85 	,12),0b111010000101	\n" + 
				"'5'	: (0x743 	,11),0b11101000011	\n" + 
				"'P'	: (0x1d1 	,9),0b111010001	\n" + 
				"'v'	: (0xe9 	,8),0b11101001	\n" + 
				"'y'	: (0x75 	,7),0b1110101	\n" + 
				"0x d	: (0x76 	,7),0b1110110	\n" + 
				"0x a	: (0x77 	,7),0b1110111	\n" + 
				"'e'	: (0xf 	,4),0b1111	\n" + 
				")";
		String code2="--- Printing Codes ---\n" + 
				"' '	:	5272	:	(0x6 	,3),0b110	\n" + 
				"'e'	:	2900	:	(0xf 	,4),0b1111	\n" + 
				"'s'	:	2076	:	(0xa 	,4),0b1010	\n" + 
				"'o'	:	2037	:	(0x9 	,4),0b1001	\n" + 
				"'t'	:	1956	:	(0x8 	,4),0b1000	\n" + 
				"'i'	:	1853	:	(0x6 	,4),0b0110	\n" + 
				"'a'	:	1806	:	(0x5 	,4),0b0101	\n" + 
				"'n'	:	1617	:	(0x2 	,4),0b0010	\n" + 
				"'r'	:	1511	:	(0x0 	,4),0b0000	\n" + 
				"'l'	:	1073	:	(0x16 	,5),0b10110	\n" + 
				"'c'	:	911	:	(0xe 	,5),0b01110	\n" + 
				"'h'	:	892	:	(0x8 	,5),0b01000	\n" + 
				"'m'	:	866	:	(0x7 	,5),0b00111	\n" + 
				"'d'	:	792	:	(0x6 	,5),0b00110	\n" + 
				"'p'	:	769	:	(0x2 	,5),0b00010	\n" + 
				"'u'	:	551	:	(0x2f 	,6),0b101111	\n" + 
				"'f'	:	517	:	(0x1f 	,6),0b011111	\n" + 
				"'g'	:	448	:	(0x13 	,6),0b010011	\n" + 
				"\\xa :	382	:	(0x77 	,7),0b1110111	\n" + 
				"\\xd :	380	:	(0x76 	,7),0b1110110	\n" + 
				"'y'	:	375	:	(0x75 	,7),0b1110101	\n" + 
				"'b'	:	309	:	(0x71 	,7),0b1110001	\n" + 
				"'.'	:	275	:	(0x5d 	,7),0b1011101	\n" + 
				"','	:	235	:	(0x25 	,7),0b0100101	\n" + 
				"'w'	:	198	:	(0xd 	,7),0b0001101	\n" + 
				"'v'	:	186	:	(0xe9 	,8),0b11101001	\n" + 
				"'k'	:	138	:	(0xb9 	,8),0b10111001	\n" + 
				"'C'	:	127	:	(0x7b 	,8),0b01111011	\n" + 
				"'L'	:	122	:	(0x79 	,8),0b01111001	\n" + 
				"'T'	:	105	:	(0x49 	,8),0b01001001	\n" + 
				"'0'	:	104	:	(0x48 	,8),0b01001000	\n" + 
				"'A'	:	103	:	(0x1e 	,8),0b00011110	\n" + 
				"'P'	:	87	:	(0x1d1 	,9),0b111010001	\n" + 
				"'2'	:	85	:	(0x1ce 	,9),0b111001110	\n" + 
				"'S'	:	85	:	(0x1cf 	,9),0b111001111	\n" + 
				"'1'	:	84	:	(0x1cd 	,9),0b111001101	\n" + 
				"'x'	:	84	:	(0x1cc 	,9),0b111001100	\n" + 
				"'M'	:	82	:	(0x1cb 	,9),0b111001011	\n" + 
				"')'	:	81	:	(0x1c9 	,9),0b111001001	\n" + 
				"'('	:	81	:	(0x1ca 	,9),0b111001010	\n" + 
				"'-'	:	76	:	(0x1c3 	,9),0b111000011	\n" + 
				"'F'	:	75	:	(0x1c2 	,9),0b111000010	\n" + 
				"'\"'	:	71	:	(0x1c0 	,9),0b111000000	\n" + 
				"'?'	:	65	:	(0xf5 	,9),0b011110101	\n" + 
				"'D'	:	65	:	(0x170 	,9),0b101110000	\n" + 
				"'E'	:	61	:	(0xf4 	,9),0b011110100	\n" + 
				"'R'	:	58	:	(0xf1 	,9),0b011110001	\n" + 
				"'Z'	:	57	:	(0xf0 	,9),0b011110000	\n" + 
				"'q'	:	51	:	(0x3e 	,9),0b000111110	\n" + 
				"'W'	:	50	:	(0x3a 	,9),0b000111010	\n" + 
				"'G'	:	49	:	(0x39 	,9),0b000111001	\n" + 
				"'B'	:	48	:	(0x32 	,9),0b000110010	\n" + 
				"'I'	:	48	:	(0x33 	,9),0b000110011	\n" + 
				"'N'	:	48	:	(0x38 	,9),0b000111000	\n" + 
				"'z'	:	39	:	(0x391 	,10),0b1110010001	\n" + 
				"'7'	:	38	:	(0x390 	,10),0b1110010000	\n" + 
				"'H'	:	36	:	(0x382 	,10),0b1110000010	\n" + 
				"'4'	:	26	:	(0x7f 	,10),0b0001111111	\n" + 
				"'3'	:	25	:	(0x76 	,10),0b0001110110	\n" + 
				"'['	:	24	:	(0x63 	,10),0b0001100011	\n" + 
				"']'	:	24	:	(0x62 	,10),0b0001100010	\n" + 
				"'9'	:	23	:	(0x61 	,10),0b0001100001	\n" + 
				"'5'	:	22	:	(0x743 	,11),0b11101000011	\n" + 
				"'U'	:	22	:	(0x60 	,10),0b0001100000	\n" + 
				"'J'	:	21	:	(0x741 	,11),0b11101000001	\n" + 
				"'6'	:	20	:	(0x740 	,11),0b11101000000	\n" + 
				"':'	:	18	:	(0x5c7 	,11),0b10111000111	\n" + 
				"'O'	:	18	:	(0x706 	,11),0b11100000110	\n" + 
				"'8'	:	17	:	(0x5c6 	,11),0b10111000110	\n" + 
				"\\x9 :	16	:	(0x5c5 	,11),0b10111000101	\n" + 
				"'''	:	13	:	(0xef 	,11),0b00011101111	\n" + 
				"';'	:	13	:	(0xfc 	,11),0b00011111100	\n" + 
				"'V'	:	13	:	(0xee 	,11),0b00011101110	\n" + 
				"'/'	:	11	:	(0xe84 	,12),0b111010000100	\n" + 
				"'Q'	:	11	:	(0xe85 	,12),0b111010000101	\n" + 
				"'K'	:	10	:	(0xe0f 	,12),0b111000001111	\n" + 
				"'X'	:	7	:	(0xb88 	,12),0b101110001000	\n" + 
				"'\\'	:	7	:	(0x1fb 	,12),0b000111111011	\n" + 
				"'j'	:	5	:	(0x1c1d 	,13),0b1110000011101	\n" + 
				"'$'	:	3	:	(0x3f5 	,13),0b0001111110101	\n" + 
				"'+'	:	3	:	(0x3f4 	,13),0b0001111110100	\n" + 
				"'%'	:	2	:	(0x3839 	,14),0b11100000111001	\n" + 
				"INT24:	1	:	(0x5c48 	,15),0b101110001001000	\n" + 
				"INT48:	1	:	(0x5c49 	,15),0b101110001001001	\n" + 
				"EOF:	1	:	(0x5c4a 	,15),0b101110001001010	\n" + 
				"HOF:	1	:	(0x5c4c 	,15),0b101110001001100	\n" + 
				"EOS:	1	:	(0x7071 	,15),0b111000001110001	\n" + 
				"'#'	:	1	:	(0x5c4d 	,15),0b101110001001101	\n" + 
				"'&'	:	1	:	(0x5c4e 	,15),0b101110001001110	\n" + 
				"\\xae :	1	:	(0x7070 	,15),0b111000001110000	\n" + 
				"\\xb5 :	1	:	(0x5c4f 	,15),0b101110001001111	\n" + 
				"'<'	:	1	:	(0x5c4b 	,15),0b101110001001011	\n" + 
				"";
		HuffmanCode hc=(HuffmanCode)cr;
		assertEquals("",hc.toFreqString());
		assertEquals(code,hc.toString());
		assertEquals(code2,hc.codesToString(Symbol.Freq(ldec)));
		assertEquals((Long)19606L,hc.getSize(Symbol.Freq(ldec)));
		
		assertEquals("(0x1e 	,8),0b00011110	",cr.get(Symbol.findId('A')).toString());
		assertEquals("(0x5 	,4),0b0101	",cr.get(Symbol.findId('a')).toString());
		assertEquals("(0x5c4b 	,15),0b101110001001011	",cr.get(Symbol.findId('<')).toString());
		assertEquals("(0x5c4c 	,15),0b101110001001100	",cr.get(Symbol.HOF).toString());
		
		assertEquals("(0x1e 	,8),0b00011110	",(Symbol.findId('A').getCode()).toString());
		assertEquals("(0x5 	,4),0b0101	",(Symbol.findId('a').getCode()).toString());
		assertEquals("(0x5c4b 	,15),0b101110001001011	",(Symbol.findId('<').getCode()).toString());

		assertEquals("(0x5c4b 	,15),0b101110001001011	",cr.get(Symbol.findId('<')).toString());
				
		assertEquals(null,cr.get(Symbol.Empty));
		
		// test huff table
		ldec.clear();
		ldec.add(Symbol.EOF);
		ldec.add(Symbol.EOF);
		ldec.add(Symbol.EOF);
		ldec.add(Symbol.EOF);
		ldec.add(Symbol.EOS);
		ldec.add(Symbol.EOS);
		ldec.add(Symbol.findId('a'));
		ldec.add(Symbol.findId('A'));
		ldec.add(Symbol.findId('@'));
		ldec.add(Symbol.findId('à'));
		ldec.add(Symbol.findId('b'));
		ldec.add(Symbol.findId('x'));
		ldec.add(Symbol.findId('c'));
		ldec.add(Symbol.findId('d'));
		ldec.add(Symbol.findId('A'));
		ldec.add(Symbol.SOS);
		
		cr=hc=(HuffmanCode) HuffmanCode.buildCode(ldec);
		System.out.print(hc.getRoot().toFreq());
		System.out.print(hc.getRoot().toSym());
		
		String filename="res\\result.test\\tmp\\FileSymbol.toArchive";
		Symbol.initCode();
		BinaryStdOut bo=new BinaryStdOut(filename);
		CodingSet c16;
		bo.setCodingRule(c16=new CodingSet(CodingSet.NOCOMPRESS16));
		c16.writeCodingRule(bo);
		cr.writeCodingRule(bo);
		bo.close();
		BinaryStdIn bi=new BinaryStdIn(filename);
		bi.setCodingRule(c16);
		ICodingRule cs = ICodingRule.ReadCodingRule(bi);
		assertEquals(c16.toString(),cs.toString());
		cs = ICodingRule.ReadCodingRule(bi);
		
		bi.close();
		assertEquals(cr.toString(),cs.toString());
		// test files
		ldec=fs.toSymbol();
		cr=HuffmanCode.buildCode(ldec);		
		FileSymbol.toArchive(ldec, cr, filename);		
		List<ISymbol> ld = FileSymbol.fromArchive(null, filename);
		assertEquals(ldec,ld);
		
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
