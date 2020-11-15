/**
 * 
 */
package com.zoubworld.java.utils.compress.test;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.zoubworld.java.utils.compress.Code;
import com.zoubworld.java.utils.compress.CodingSet;
import com.zoubworld.java.utils.compress.HuffmanCode;
import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.file.BinaryFinFout;
import com.zoubworld.java.utils.compress.file.BinaryStdIn;
import com.zoubworld.java.utils.compress.file.BinaryStdOut;
import com.zoubworld.java.utils.compress.file.FileSymbol;
import com.zoubworld.java.utils.compress.file.IBinaryReader;
import com.zoubworld.java.utils.compress.file.IBinaryWriter;
import com.zoubworld.utils.JavaUtils;

/**
 * @author zoubata
 *
 */
public class HuffmanCodeTest {

	@Test
	public void testTreeNodeSave() {
		
		 List<ISymbol>  ls=new ArrayList<ISymbol>();
		 ls.add(Symbol.findId('a'));

		 HuffmanCode cs=HuffmanCode.buildCode(ls);
		 
		 assertEquals("1",cs.get(Symbol.findId('a')).toRaw());
		 ls.add(Symbol.findId('a'));
		 ls.add(Symbol.findId('a'));
		 ls.add(Symbol.findId('A'));
		 ls.add(Symbol.findId('B'));
		 ls.add(Symbol.findId('c'));
		 ls.add(Symbol.findId('C'));
		 ls.add(Symbol.findId('C'));
		 ls.add(Symbol.findId('c'));
		 ls.add(Symbol.findId('C'));
		 ls.add(Symbol.findId('a'));
		 cs=HuffmanCode.buildCode(ls);
		 assertEquals("0",cs.get(Symbol.findId('a')).toRaw());
		 assertEquals("10",cs.get(Symbol.findId('C')).toRaw());
		 assertEquals("110",cs.get(Symbol.findId('c')).toRaw());
		 assertEquals("1111",cs.get(Symbol.findId('B')).toRaw());
		 assertEquals("1110",cs.get(Symbol.findId('A')).toRaw());
		 cs.clearfreq();
		 String s=cs.toString();
		 CodingSet cs16 = new CodingSet(CodingSet.NOCOMPRESS16);
		 JavaUtils.mkDir("res\\result.test\\test\\small_ref\\");		
		 IBinaryWriter binaryStdOut=new BinaryStdOut("res\\result.test\\test\\small_ref\\huff1.table");
		 binaryStdOut.setCodingRule(cs16);// define how to write symbol in default
		cs.writeCodingRule(binaryStdOut);
		binaryStdOut.close();
		IBinaryReader bi=new BinaryStdIn("res\\result.test\\test\\small_ref\\huff1.table");
		bi.setCodingRule(cs16);// define how to read symbol in default
		cs=(HuffmanCode) ICodingRule.ReadCodingRule(bi);
		bi.close();
		
		assertEquals(s,cs.toString());
		assertEquals("0",cs.get(Symbol.findId('a')).toRaw());
		 assertEquals("10",cs.get(Symbol.findId('C')).toRaw());
		 assertEquals("110",cs.get(Symbol.findId('c')).toRaw());
		 assertEquals("1111",cs.get(Symbol.findId('B')).toRaw());
		 assertEquals("1110",cs.get(Symbol.findId('A')).toRaw());
		 
		
		   ls=FileSymbol.read("res/test/small_ref/pie.txt");
		
		
		
		  cs=HuffmanCode.buildCode(ls);
		 
		FileSymbol.toArchive(ls,cs, "res\\result.test\\test\\small_ref\\pie.huf");
		 
		 List<ISymbol>  lsd=FileSymbol.fromArchive(null, "res\\result.test\\test\\small_ref\\pie.huf");
		
		 FileSymbol.saveAs(FileSymbol.ExtractDataSymbol(lsd), "res/result.test/test/small_ref/pie2.txt");
		assertEquals(JavaUtils.read(                          "res/test/small_ref/pie.txt"),
					JavaUtils.read(                           "res/result.test/test/small_ref/pie2.txt")
					);
		
	
	
	}
	@Test
	public void testbasic_encode_decode()
	{
		String test="000000000000000111111111112222222333445";
		encode_decode("1");
		encode_decode("12");
		encode_decode(test);
		encode_decode("0123456789");
		encode_decode("azertyuiop^$qsdfghjklmù*wxcvbn,;:!<01478258/369*.369*0258/0147");
		encode_decode("011222233333333"
				    + "4444444444444444"
					+ "5555555555555555");
		
		
	}
	
	public void encode_decode(String test) {
		 List<ISymbol> ls=Symbol.factoryCharSeq(test);
		 HuffmanCode cs = HuffmanCode.buildCode(ls);
		 BinaryFinFout fifo=new BinaryFinFout();
			fifo.setCodingRule(cs);
			fifo.writes(ls);
			long s=fifo.size();
			fifo.flush();
			List<ISymbol> lsd = fifo.readSymbols(test.length());
			assertEquals(test, Symbol.listSymbolToString(ls));
			fifo.close();
			fifo.write(test);
			double e=cs.getEntropie();		
			assertTrue(s<=(fifo.size()/8*e+1));
	}
	@Test
	public void testbasicBuild_String() {
		 List<ISymbol> ls=Symbol.factoryCharSeq("000000000000000111111111112222222333445");
		 String sfreq="'0'   : 38.462%\n" + 
			 		"'1'   : 28.205%\n" + 
			 		"'2'   : 17.949%\n" + 
			 		"'3'   : 7.692%\n" + 
			 		"'4'   : 5.128%\n" + 
			 		"'5'   : 2.564%\n" + 
			 		"";
		 String snode="HuffManCode(--- Printing Codes ---\n" + 
			 		"Nodes : 6\n" + 
			 		"'0'	: (0x0 	,1),0b0	\n" + 
			 		"'1'	: (0x2 	,2),0b10	\n" + 
			 		"'3'	: (0xc 	,4),0b1100	\n" + 
			 		"'5'	: (0x1a 	,5),0b11010	\n" + 
			 		"'4'	: (0x1b 	,5),0b11011	\n" + 
			 		"'2'	: (0x7 	,3),0b111	\n" + 
			 		")";
		 String scode="HuffManCode(--- Printing Codes ---\n" + 
			 		"Symbols : 292\n" + 
			 		"0x 0	: ''\n" + 
			 		"0x 1	: ''\n" + 
			 		"0x 2	: ''\n" + 
			 		"0x 3	: ''\n" + 
			 		"0x 4	: ''\n" + 
			 		"0x 5	: ''\n" + 
			 		"0x 6	: ''\n" + 
			 		"0x 7	: ''\n" + 
			 		"0x 8	: ''\n" + 
			 		"0x 9	: ''\n" + 
			 		"0x a	: ''\n" + 
			 		"0x b	: ''\n" + 
			 		"0x c	: ''\n" + 
			 		"0x d	: ''\n" + 
			 		"0x e	: ''\n" + 
			 		"0x f	: ''\n" + 
			 		"0x10	: ''\n" + 
			 		"0x11	: ''\n" + 
			 		"0x12	: ''\n" + 
			 		"0x13	: ''\n" + 
			 		"0x14	: ''\n" + 
			 		"0x15	: ''\n" + 
			 		"0x16	: ''\n" + 
			 		"0x17	: ''\n" + 
			 		"0x18	: ''\n" + 
			 		"0x19	: ''\n" + 
			 		"0x1a	: ''\n" + 
			 		"0x1b	: ''\n" + 
			 		"0x1c	: ''\n" + 
			 		"0x1d	: ''\n" + 
			 		"0x1e	: ''\n" + 
			 		"0x1f	: ''\n" + 
			 		"' '	: ''\n" + 
			 		"'!'	: ''\n" + 
			 		"'\"'	: ''\n" + 
			 		"'#'	: ''\n" + 
			 		"'$'	: ''\n" + 
			 		"'%'	: ''\n" + 
			 		"'&'	: ''\n" + 
			 		"'''	: ''\n" + 
			 		"'('	: ''\n" + 
			 		"')'	: ''\n" + 
			 		"'*'	: ''\n" + 
			 		"'+'	: ''\n" + 
			 		"','	: ''\n" + 
			 		"'-'	: ''\n" + 
			 		"'.'	: ''\n" + 
			 		"'/'	: ''\n" + 
			 		"'0'	: (0x0 	,1),0b0	\n" + 
			 		"'1'	: (0x2 	,2),0b10	\n" + 
			 		"'2'	: (0x7 	,3),0b111	\n" + 
			 		"'3'	: (0xc 	,4),0b1100	\n" + 
			 		"'4'	: (0x1b 	,5),0b11011	\n" + 
			 		"'5'	: (0x1a 	,5),0b11010	\n" + 
			 		"'6'	: ''\n" + 
			 		"'7'	: ''\n" + 
			 		"'8'	: ''\n" + 
			 		"'9'	: ''\n" + 
			 		"':'	: ''\n" + 
			 		"';'	: ''\n" + 
			 		"'<'	: ''\n" + 
			 		"'='	: ''\n" + 
			 		"'>'	: ''\n" + 
			 		"'?'	: ''\n" + 
			 		"'@'	: ''\n" + 
			 		"'A'	: ''\n" + 
			 		"'B'	: ''\n" + 
			 		"'C'	: ''\n" + 
			 		"'D'	: ''\n" + 
			 		"'E'	: ''\n" + 
			 		"'F'	: ''\n" + 
			 		"'G'	: ''\n" + 
			 		"'H'	: ''\n" + 
			 		"'I'	: ''\n" + 
			 		"'J'	: ''\n" + 
			 		"'K'	: ''\n" + 
			 		"'L'	: ''\n" + 
			 		"'M'	: ''\n" + 
			 		"'N'	: ''\n" + 
			 		"'O'	: ''\n" + 
			 		"'P'	: ''\n" + 
			 		"'Q'	: ''\n" + 
			 		"'R'	: ''\n" + 
			 		"'S'	: ''\n" + 
			 		"'T'	: ''\n" + 
			 		"'U'	: ''\n" + 
			 		"'V'	: ''\n" + 
			 		"'W'	: ''\n" + 
			 		"'X'	: ''\n" + 
			 		"'Y'	: ''\n" + 
			 		"'Z'	: ''\n" + 
			 		"'['	: ''\n" + 
			 		"'\\'	: ''\n" + 
			 		"']'	: ''\n" + 
			 		"'^'	: ''\n" + 
			 		"'_'	: ''\n" + 
			 		"'`'	: ''\n" + 
			 		"'a'	: ''\n" + 
			 		"'b'	: ''\n" + 
			 		"'c'	: ''\n" + 
			 		"'d'	: ''\n" + 
			 		"'e'	: ''\n" + 
			 		"'f'	: ''\n" + 
			 		"'g'	: ''\n" + 
			 		"'h'	: ''\n" + 
			 		"'i'	: ''\n" + 
			 		"'j'	: ''\n" + 
			 		"'k'	: ''\n" + 
			 		"'l'	: ''\n" + 
			 		"'m'	: ''\n" + 
			 		"'n'	: ''\n" + 
			 		"'o'	: ''\n" + 
			 		"'p'	: ''\n" + 
			 		"'q'	: ''\n" + 
			 		"'r'	: ''\n" + 
			 		"'s'	: ''\n" + 
			 		"'t'	: ''\n" + 
			 		"'u'	: ''\n" + 
			 		"'v'	: ''\n" + 
			 		"'w'	: ''\n" + 
			 		"'x'	: ''\n" + 
			 		"'y'	: ''\n" + 
			 		"'z'	: ''\n" + 
			 		"'{'	: ''\n" + 
			 		"'|'	: ''\n" + 
			 		"'}'	: ''\n" + 
			 		"'~'	: ''\n" + 
			 		"0x7f	: ''\n" + 
			 		"0x80	: ''\n" + 
			 		"0x81	: ''\n" + 
			 		"0x82	: ''\n" + 
			 		"0x83	: ''\n" + 
			 		"0x84	: ''\n" + 
			 		"0x85	: ''\n" + 
			 		"0x86	: ''\n" + 
			 		"0x87	: ''\n" + 
			 		"0x88	: ''\n" + 
			 		"0x89	: ''\n" + 
			 		"0x8a	: ''\n" + 
			 		"0x8b	: ''\n" + 
			 		"0x8c	: ''\n" + 
			 		"0x8d	: ''\n" + 
			 		"0x8e	: ''\n" + 
			 		"0x8f	: ''\n" + 
			 		"0x90	: ''\n" + 
			 		"0x91	: ''\n" + 
			 		"0x92	: ''\n" + 
			 		"0x93	: ''\n" + 
			 		"0x94	: ''\n" + 
			 		"0x95	: ''\n" + 
			 		"0x96	: ''\n" + 
			 		"0x97	: ''\n" + 
			 		"0x98	: ''\n" + 
			 		"0x99	: ''\n" + 
			 		"0x9a	: ''\n" + 
			 		"0x9b	: ''\n" + 
			 		"0x9c	: ''\n" + 
			 		"0x9d	: ''\n" + 
			 		"0x9e	: ''\n" + 
			 		"0x9f	: ''\n" + 
			 		"0xa0	: ''\n" + 
			 		"0xa1	: ''\n" + 
			 		"0xa2	: ''\n" + 
			 		"0xa3	: ''\n" + 
			 		"0xa4	: ''\n" + 
			 		"0xa5	: ''\n" + 
			 		"0xa6	: ''\n" + 
			 		"0xa7	: ''\n" + 
			 		"0xa8	: ''\n" + 
			 		"0xa9	: ''\n" + 
			 		"0xaa	: ''\n" + 
			 		"0xab	: ''\n" + 
			 		"0xac	: ''\n" + 
			 		"0xad	: ''\n" + 
			 		"0xae	: ''\n" + 
			 		"0xaf	: ''\n" + 
			 		"0xb0	: ''\n" + 
			 		"0xb1	: ''\n" + 
			 		"0xb2	: ''\n" + 
			 		"0xb3	: ''\n" + 
			 		"0xb4	: ''\n" + 
			 		"0xb5	: ''\n" + 
			 		"0xb6	: ''\n" + 
			 		"0xb7	: ''\n" + 
			 		"0xb8	: ''\n" + 
			 		"0xb9	: ''\n" + 
			 		"0xba	: ''\n" + 
			 		"0xbb	: ''\n" + 
			 		"0xbc	: ''\n" + 
			 		"0xbd	: ''\n" + 
			 		"0xbe	: ''\n" + 
			 		"0xbf	: ''\n" + 
			 		"0xc0	: ''\n" + 
			 		"0xc1	: ''\n" + 
			 		"0xc2	: ''\n" + 
			 		"0xc3	: ''\n" + 
			 		"0xc4	: ''\n" + 
			 		"0xc5	: ''\n" + 
			 		"0xc6	: ''\n" + 
			 		"0xc7	: ''\n" + 
			 		"0xc8	: ''\n" + 
			 		"0xc9	: ''\n" + 
			 		"0xca	: ''\n" + 
			 		"0xcb	: ''\n" + 
			 		"0xcc	: ''\n" + 
			 		"0xcd	: ''\n" + 
			 		"0xce	: ''\n" + 
			 		"0xcf	: ''\n" + 
			 		"0xd0	: ''\n" + 
			 		"0xd1	: ''\n" + 
			 		"0xd2	: ''\n" + 
			 		"0xd3	: ''\n" + 
			 		"0xd4	: ''\n" + 
			 		"0xd5	: ''\n" + 
			 		"0xd6	: ''\n" + 
			 		"0xd7	: ''\n" + 
			 		"0xd8	: ''\n" + 
			 		"0xd9	: ''\n" + 
			 		"0xda	: ''\n" + 
			 		"0xdb	: ''\n" + 
			 		"0xdc	: ''\n" + 
			 		"0xdd	: ''\n" + 
			 		"0xde	: ''\n" + 
			 		"0xdf	: ''\n" + 
			 		"0xe0	: ''\n" + 
			 		"0xe1	: ''\n" + 
			 		"0xe2	: ''\n" + 
			 		"0xe3	: ''\n" + 
			 		"0xe4	: ''\n" + 
			 		"0xe5	: ''\n" + 
			 		"0xe6	: ''\n" + 
			 		"0xe7	: ''\n" + 
			 		"0xe8	: ''\n" + 
			 		"0xe9	: ''\n" + 
			 		"0xea	: ''\n" + 
			 		"0xeb	: ''\n" + 
			 		"0xec	: ''\n" + 
			 		"0xed	: ''\n" + 
			 		"0xee	: ''\n" + 
			 		"0xef	: ''\n" + 
			 		"0xf0	: ''\n" + 
			 		"0xf1	: ''\n" + 
			 		"0xf2	: ''\n" + 
			 		"0xf3	: ''\n" + 
			 		"0xf4	: ''\n" + 
			 		"0xf5	: ''\n" + 
			 		"0xf6	: ''\n" + 
			 		"0xf7	: ''\n" + 
			 		"0xf8	: ''\n" + 
			 		"0xf9	: ''\n" + 
			 		"0xfa	: ''\n" + 
			 		"0xfb	: ''\n" + 
			 		"0xfc	: ''\n" + 
			 		"0xfd	: ''\n" + 
			 		"0xfe	: ''\n" + 
			 		"0xff	: ''\n" + 
			 		"0x100	: ''\n" + 
			 		"0x101	: ''\n" + 
			 		"0x102	: ''\n" + 
			 		"0x103	: ''\n" + 
			 		"0x104	: ''\n" + 
			 		"0x105	: ''\n" + 
			 		"0x106	: ''\n" + 
			 		"0x107	: ''\n" + 
			 		"0x108	: ''\n" + 
			 		"0x109	: ''\n" + 
			 		"0x10a	: ''\n" + 
			 		"0x10b	: ''\n" + 
			 		"0x10c	: ''\n" + 
			 		"0x10d	: ''\n" + 
			 		"0x10e	: ''\n" + 
			 		"0x10f	: ''\n" + 
			 		"0x110	: ''\n" + 
			 		"0x111	: ''\n" + 
			 		"0x112	: ''\n" + 
			 		"0x113	: ''\n" + 
			 		"0x114	: ''\n" + 
			 		"0x115	: ''\n" + 
			 		"0x116	: ''\n" + 
			 		"0x117	: ''\n" + 
			 		"0x118	: ''\n" + 
			 		"0x119	: ''\n" + 
			 		"0x11a	: ''\n" + 
			 		"0x11b	: ''\n" + 
			 		"0x11c	: ''\n" + 
			 		"0x11d	: ''\n" + 
			 		"0x11e	: ''\n" + 
			 		"0x11f	: ''\n" + 
			 		"0x120	: ''\n" + 
			 		"0x121	: ''\n" + 
			 		"0x122	: ''\n" + 
			 		"0x123	: ''\n" + 
			 		")";
		 HuffmanCode cs = HuffmanCode.buildCode(ls);
		 assertEquals(sfreq,cs.toFreqString());
		 
		 assertEquals(scode,cs.toString());
		 int[] freq = HuffmanCode.getFreq(ls);
		 cs = HuffmanCode.buildCode(freq);
		 assertEquals(sfreq,cs.toFreqString());
		 assertEquals(scode,cs.toString());
		 Map<ISymbol, Long> mfreq = Symbol.FreqId(ls);
		 assertEquals(HuffmanCode.getEntropie(mfreq),1.476,0.001);
		 cs = HuffmanCode.buildCode(mfreq);
		 assertEquals(sfreq,cs.toFreqString());
		 assertEquals(scode,cs.toString());
		 cs.clearfreq();
		 assertEquals(snode,cs.toString());
		/* huff.printFreqs();
		 huff.printCodes();*/
		 
	}
	@Test
	public void testcoverage()
	{
		 List<ISymbol> ls=Symbol.factoryCharSeq("000000000000000000000000res/result.test/test/small_ref/pie2.txt123456789145601256/*-+azertyuiop^$*ùmlkjhgfdsq<wxcvbn"
			 		+ "++++,;:!&é\"'(-è_çà)=1234567890°+&~#{[|`|`\\^@]}9874567891233210......!:;,?./§>WXCVBN?.QSDFGHJKLMAZERTYUIOP¨£µ%MLK");
		
		 HuffmanCode cs = HuffmanCode.buildCode(ls);
			cs.analyse(ls);
	}
	@Test
	public void testwriteread() {
		 List<ISymbol> ls=Symbol.factoryCharSeq("000000000000000000000000res/result.test/test/small_ref/pie2.txt123456789145601256/*-+azertyuiop^$*ùmlkjhgfdsq<wxcvbn"
			 		+ "++++,;:!&é\"'(-è_çà)=1234567890°+&~#{[|`|`\\^@]}9874567891233210......!:;,?./§>WXCVBN?.QSDFGHJKLMAZERTYUIOP¨£µ%MLK");
			 HuffmanCode cs = HuffmanCode.buildCode(ls);
			 Code	 code=new Code(0,0);
		cs.WriteTable(code);
		assertEquals("000010111010000101101100011010110000011100111001111101000010100110001100011000010000110000011101010001101001100000011100011001010001100011001011101100000100011000010001110111000001011110100110110110001111110100100111110000000100110110001000111100010101111001011101100000100001101011100101000100000111010100101001100111000000101100110011011010000100000110001110001000110100100000100110010001011000100111110110001101100100010001001001100011100000010101110000101010010011110111000010001010101111001100010010001001110101100100101110000010110010000100011010011101101001010011100000101011010010111110000110101010010010101000010010010001100100100110101110000000010011110001100110100011001111001110111100001111001110100000010001000101000110100010000100110100011101001000010000101001111001010010111001001101111100000100001110001011111100101000100000100100110010100001001111111000011110110001001011000000100011100011100110000001111001000110001010010101010000011001111101111010100010110101001101111000000010101011001111000100011011101001001110100001110001010100010110001011011100111110010000010001011101100101000110000110000110101101011100001001100101100"
				, code.toRaw());
		/*
		BinaryFinFout bin=new BinaryFinFout();
		bin.write(code);
		bin.flush();
		HuffmanCode c = new HuffmanCode(bin); 
		cs.clearfreq();
		assertEquals(c.codesToString(),cs.codesToString());
		bin=new BinaryFinFout();
		bin.write(code);
		bin.flush();*/
		//cs.getCode(bin);
		
	}
	@Test
		public void testbasic() {
		 List<ISymbol> ls=Symbol.factoryCharSeq("000000000000000000000000res/result.test/test/small_ref/pie2.txt123456789145601256/*-+azertyuiop^$*ùmlkjhgfdsq<wxcvbn"
		 		+ "++++,;:!&é\"'(-è_çà)=1234567890°+&~#{[|`|`\\^@]}9874567891233210......!:;,?./§>WXCVBN?.QSDFGHJKLMAZERTYUIOP¨£µ%MLK");
		 HuffmanCode cs = HuffmanCode.buildCode(ls);
		 Map<ISymbol, Long> freq1=Symbol.FreqId(ls);
		assertEquals(2872L,(long)cs.getBitSize(freq1));
		 cs.getRoot(freq1);
		 assertEquals(2872L,(long)cs.getBitSize(freq1));
		
		 CodingSet cs9=new CodingSet(CodingSet.NOCOMPRESS);
		 IBinaryWriter binaryStdOut=new BinaryStdOut("res/result.test/test/small_ref/tree.huff");
		 binaryStdOut.setCodingRule(cs9);
		 cs.writeCodingRule(binaryStdOut);
		 binaryStdOut.writes(ls);
		 binaryStdOut.setCodingRule(cs);
		 binaryStdOut.writes(ls);
		 binaryStdOut.close();
		 Symbol.initCode();//reset
		 IBinaryReader in=new BinaryStdIn("res/result.test/test/small_ref/tree.huff");
		 in.setCodingRule(cs9);
		 HuffmanCode cs2=(HuffmanCode)ICodingRule.ReadCodingRule(in);
		 assertEquals("integrity of writing and reading huffman tree(called a coding rule)",cs,cs2);
		 List<ISymbol> ls2 = in.readSymbols(ls.size());		 
			assertEquals("integrity of writing and reading symbols in binary mode with flat coding",ls,ls2);
			in.setCodingRule(cs2);
			 ls2 = in.readSymbols(ls.size());
			 //as there is variable length the list of symbol can finish at bit 1 of an octect,
			 //and next bit are known and could be consider as a valid new symbol that didn't exist 
			 //on original symbol's list
			 // so we define the numlber of symbol read.
			assertEquals("integrity of writing and reading symbols in binary mode with huffman coding",ls,ls2); 
		in.close();	
		
	}
	
		@Test
		public void testCodingRule() {
		File file = new File("res/test/ref/smallfile.txt");
		FileSymbol fs=new FileSymbol(file);
		List<ISymbol> ldec=new ArrayList<ISymbol>();;
		
		ldec.add(Symbol.findId('<'));
		ldec.add(Symbol.findId('a'));
		ldec.add(Symbol.findId('A'));
		
		ICodingRule cr=HuffmanCode.buildCode(ldec);
		
		assertEquals(Symbol.findId('<'),cr.get(cr.get(Symbol.findId('<'))));
		assertEquals(Symbol.findId('a'),cr.get(cr.get(Symbol.findId('a'))));
		assertEquals(Symbol.findId('A'),cr.get(cr.get(Symbol.findId('A'))));
		ldec=fs.toSymbol();
		cr=HuffmanCode.buildCode(ldec);
		assertEquals(Symbol.findId('<'),cr.get(cr.get(Symbol.findId('<'))));
		assertEquals(Symbol.findId('a'),cr.get(cr.get(Symbol.findId('a'))));
		assertEquals(Symbol.findId('A'),cr.get(cr.get(Symbol.findId('A'))));

		/*cr.get(code);
		cr.get(sym);
		cr.getCode(binaryStdIn);
		cr.getSymbol(binaryStdIn);
		cr.writeCodingRule(binaryStdOut);*/
		String code="HuffManCode(--- Printing Codes ---\n" + 
				"Nodes : 112\n" + 
				"'y'	: (0x0 	,6),0b000000	\n" + 
				"'v'	: (0x2 	,7),0b0000010	\n" + 
				"'B'	: (0xc 	,9),0b000001100	\n" + 
				"'I'	: (0xd 	,9),0b000001101	\n" + 
				"'N'	: (0xe 	,9),0b000001110	\n" + 
				"'['	: (0x1e 	,10),0b0000011110	\n" + 
				"']'	: (0x1f 	,10),0b0000011111	\n" + 
				"0x a	: (0x2 	,6),0b000010	\n" + 
				"0x d	: (0x3 	,6),0b000011	\n" + 
				"'p'	: (0x2 	,5),0b00010	\n" + 
				"'d'	: (0x3 	,5),0b00011	\n" + 
				"'n'	: (0x2 	,4),0b0010	\n" + 
				"'w'	: (0x18 	,7),0b0011000	\n" + 
				"'G'	: (0x64 	,9),0b001100100	\n" + 
				"0x80	: (0x65 	,9),0b001100101	\n" + 
				"0x93	: (0x66 	,9),0b001100110	\n" + 
				"'W'	: (0x67 	,9),0b001100111	\n" + 
				"'A'	: (0x34 	,8),0b00110100	\n" + 
				"'3'	: (0xd4 	,10),0b0011010100	\n" + 
				"'V'	: (0x1aa 	,11),0b00110101010	\n" + 
				"'''	: (0x1ab 	,11),0b00110101011	\n" + 
				"'q'	: (0x6b 	,9),0b001101011	\n" + 
				"'0'	: (0x36 	,8),0b00110110	\n" + 
				"'4'	: (0xdc 	,10),0b0011011100	\n" + 
				"';'	: (0x1ba 	,11),0b00110111010	\n" + 
				"0x88	: (0x6ec 	,13),0b0011011101100	\n" + 
				"0x92	: (0x6ed 	,13),0b0011011101101	\n" + 
				"'$'	: (0x6ee 	,13),0b0011011101110	\n" + 
				"0xd0	: (0x6ef 	,13),0b0011011101111	\n" + 
				"0xe2	: (0x6f 	,9),0b001101111	\n" + 
				"'m'	: (0x7 	,5),0b00111	\n" + 
				"'h'	: (0x8 	,5),0b01000	\n" + 
				"'g'	: (0x12 	,6),0b010010	\n" + 
				"'T'	: (0x4c 	,8),0b01001100	\n" + 
				"'Z'	: (0x9a 	,9),0b010011010	\n" + 
				"'R'	: (0x9b 	,9),0b010011011	\n" + 
				"','	: (0x27 	,7),0b0100111	\n" + 
				"'a'	: (0x5 	,4),0b0101	\n" + 
				"'i'	: (0x6 	,4),0b0110	\n" + 
				"'c'	: (0xe 	,5),0b01110	\n" + 
				"'L'	: (0x78 	,8),0b01111000	\n" + 
				"'E'	: (0xf2 	,9),0b011110010	\n" + 
				"'D'	: (0xf3 	,9),0b011110011	\n" + 
				"'C'	: (0x7a 	,8),0b01111010	\n" + 
				"'X'	: (0x7b0 	,12),0b011110110000	\n" + 
				"'\\'	: (0x7b1 	,12),0b011110110001	\n" + 
				"'%'	: (0x1ec8 	,14),0b01111011001000	\n" + 
				"0xa0	: (0x1ec9 	,14),0b01111011001001	\n" + 
				"0xaa	: (0x3d94 	,15),0b011110110010100	\n" + 
				"0xae	: (0x3d95 	,15),0b011110110010101	\n" + 
				"0xb1	: (0x3d96 	,15),0b011110110010110	\n" + 
				"0xb9	: (0x3d97 	,15),0b011110110010111	\n" + 
				"0xc3	: (0xf66 	,13),0b0111101100110	\n" + 
				"0x81	: (0x1ece 	,14),0b01111011001110	\n" + 
				"0x94	: (0x3d9e 	,15),0b011110110011110	\n" + 
				"0xa7	: (0x3d9f 	,15),0b011110110011111	\n" + 
				"0x 9	: (0x3da 	,11),0b01111011010	\n" + 
				"'8'	: (0x3db 	,11),0b01111011011	\n" + 
				"0x10e	: (0x3dc0 	,15),0b011110111000000	\n" + 
				"'#'	: (0x3dc1 	,15),0b011110111000001	\n" + 
				"0x83	: (0x3dc2 	,15),0b011110111000010	\n" + 
				"0x10f	: (0x3dc3 	,15),0b011110111000011	\n" + 
				"0xb8	: (0x3dc4 	,15),0b011110111000100	\n" + 
				"0xba	: (0x3dc5 	,15),0b011110111000101	\n" + 
				"0xce	: (0x1ee3 	,14),0b01111011100011	\n" + 
				"'?'	: (0x1ee4 	,14),0b01111011100100	\n" + 
				"0x10d	: (0x3dca 	,15),0b011110111001010	\n" + 
				"'<'	: (0x3dcb 	,15),0b011110111001011	\n" + 
				"'j'	: (0xf73 	,13),0b0111101110011	\n" + 
				"':'	: (0x3dd 	,11),0b01111011101	\n" + 
				"'H'	: (0x1ef 	,10),0b0111101111	\n" + 
				"'f'	: (0x1f 	,6),0b011111	\n" + 
				"'t'	: (0x8 	,4),0b1000	\n" + 
				"'o'	: (0x9 	,4),0b1001	\n" + 
				"'s'	: (0xa 	,4),0b1010	\n" + 
				"'l'	: (0x16 	,5),0b10110	\n" + 
				"'u'	: (0x2e 	,6),0b101110	\n" + 
				"'.'	: (0x5e 	,7),0b1011110	\n" + 
				"'k'	: (0xbe 	,8),0b10111110	\n" + 
				"'\"'	: (0x17e 	,9),0b101111110	\n" + 
				"'-'	: (0x17f 	,9),0b101111111	\n" + 
				"' '	: (0x6 	,3),0b110	\n" + 
				"'b'	: (0x70 	,7),0b1110000	\n" + 
				"'F'	: (0x1c4 	,9),0b111000100	\n" + 
				"'O'	: (0x714 	,11),0b11100010100	\n" + 
				"'6'	: (0x715 	,11),0b11100010101	\n" + 
				"'7'	: (0x38b 	,10),0b1110001011	\n" + 
				"'('	: (0x1c6 	,9),0b111000110	\n" + 
				"')'	: (0x1c7 	,9),0b111000111	\n" + 
				"'z'	: (0x390 	,10),0b1110010000	\n" + 
				"'K'	: (0xe44 	,12),0b111001000100	\n" + 
				"'/'	: (0xe45 	,12),0b111001000101	\n" + 
				"'J'	: (0x723 	,11),0b11100100011	\n" + 
				"'M'	: (0x1c9 	,9),0b111001001	\n" + 
				"'x'	: (0x1ca 	,9),0b111001010	\n" + 
				"'1'	: (0x1cb 	,9),0b111001011	\n" + 
				"'S'	: (0x1cc 	,9),0b111001100	\n" + 
				"'2'	: (0x1cd 	,9),0b111001101	\n" + 
				"'P'	: (0x1ce 	,9),0b111001110	\n" + 
				"'U'	: (0x73c 	,11),0b11100111100	\n" + 
				"'5'	: (0x73d 	,11),0b11100111101	\n" + 
				"0x104	: (0x73e0 	,15),0b111001111100000	\n" + 
				"0x106	: (0x73e1 	,15),0b111001111100001	\n" + 
				"'+'	: (0x39f1 	,14),0b11100111110001	\n" + 
				"'&'	: (0x73e4 	,15),0b111001111100100	\n" + 
				"0xbc	: (0xe7ca 	,16),0b1110011111001010	\n" + 
				"0xc2	: (0xe7cb 	,16),0b1110011111001011	\n" + 
				"0xd1	: (0x39f3 	,14),0b11100111110011	\n" + 
				"'Q'	: (0xe7d 	,12),0b111001111101	\n" + 
				"'9'	: (0x73f 	,11),0b11100111111	\n" + 
				"'r'	: (0x1d 	,5),0b11101	\n" + 
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
	//	assertEquals("",hc.toFreqString());
		hc.clearfreq();
		assertEquals(code,hc.toString());
		assertEquals(code2,hc.codesToString(Symbol.FreqId(ldec)));
		assertEquals((Long)19606L,hc.getSize(Symbol.FreqId(ldec)));
		
		assertEquals("(0x1e 	,8),0b00011110	",cr.get(Symbol.findId('A')).toString());
		assertEquals("(0x5 	,4),0b0101	",cr.get(Symbol.findId('a')).toString());
		assertEquals("(0x5c4b 	,15),0b101110001001011	",cr.get(Symbol.findId('<')).toString());
		assertEquals("(0x5c4c 	,15),0b101110001001100	",cr.get(Symbol.HOF).toString());
		
		assertEquals("(0x1e 	,8),0b00011110	",(Symbol.findId('A').getCode()).toString());
		assertEquals("(0x5 	,4),0b0101	",(Symbol.findId('a').getCode()).toString());
		assertEquals("(0x5c4b 	,15),0b101110001001011	",(Symbol.findId('<').getCode()).toString());

		assertEquals("(0x5c4b 	,15),0b101110001001011	",cr.get(Symbol.findId('<')).toString());
				
		assertEquals(null,cr.get(Symbol.Empty));
		
		String filename="res\\result.test\\tmp\\FileSymbol.toArchive";
		Symbol.initCode();
		IBinaryWriter bo=new BinaryStdOut(filename);
		CodingSet c16;
		bo.setCodingRule(c16=new CodingSet(CodingSet.NOCOMPRESS16));
		c16.writeCodingRule(bo);
		cr.writeCodingRule(bo);
		bo.close();
		IBinaryReader bi=new BinaryStdIn(filename);
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
				while(ld.get(ld.size()-1)!=Symbol.EOF)
					ld.remove(ld.size()-1);// remove dummy data
				assertEquals(ldec,ld);
				
		}
		@Test
		public void testCodingRule2() {
			List<ISymbol> ldec=new ArrayList<ISymbol>();
			
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
		HuffmanCode hc;
		HuffmanCode cr=hc=(HuffmanCode) HuffmanCode.buildCode(ldec);
		System.out.print(hc.getRoot().toFreq());
		System.out.print(hc.getRoot().toSym());
	hc.clearfreq();
		assertEquals("HuffManCode(--- Printing Codes ---\n" + 
				"Nodes : 11\n"
				+ "0x10d	: (0x0 	,2),0b00	\n" + 
				"'a'	: (0x4 	,4),0b0100	\n" + 
				"0x11b	: (0x5 	,4),0b0101	\n" + 
				"'c'	: (0x6 	,4),0b0110	\n" + 
				"'@'	: (0x7 	,4),0b0111	\n" + 
				"0xe0	: (0x8 	,4),0b1000	\n" + 
				"'x'	: (0x9 	,4),0b1001	\n" + 
				"'A'	: (0x5 	,3),0b101	\n" + 
				"0x10f	: (0x6 	,3),0b110	\n" + 
				"'b'	: (0xe 	,4),0b1110	\n" + 
				"'d'	: (0xf 	,4),0b1111	\n" + 
				")",hc.toString());
		assertEquals("--- Printing Codes ---\n" + 
				"EOF:	4	:	(0x0 	,2),0b00	\n" + 
				"'A'	:	2	:	(0x5 	,3),0b101	\n" + 
				"EOS:	2	:	(0x6 	,3),0b110	\n" + 
				"'a'	:	1	:	(0x4 	,4),0b0100	\n" + 
				"'@'	:	1	:	(0x7 	,4),0b0111	\n" + 
				"\\xe0 :	1	:	(0x8 	,4),0b1000	\n" + 
				"'c'	:	1	:	(0x6 	,4),0b0110	\n" + 
				"'b'	:	1	:	(0xe 	,4),0b1110	\n" + 
				"'d'	:	1	:	(0xf 	,4),0b1111	\n" + 
				"'x'	:	1	:	(0x9 	,4),0b1001	\n" + 
				"SOS:	1	:	(0x5 	,4),0b0101	\n",hc.codesToString(Symbol.FreqId(ldec)));
	
		
		
	assertEquals(1,1);
	}
		
	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.HuffmanCode#compress()}.
	 */
/*	@Test
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
*/
	
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
		File file = new File("res/test/ref/smallDir/smallfile.txt");
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
/*	@Test
	public void testExpand() {
		testCompress();
	}*/

}
