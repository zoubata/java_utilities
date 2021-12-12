package com.zoubworld.java.utils.compress;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.Test;

import com.zoubworld.java.utils.compress.SymbolComplex.SymbolBigINT;
import com.zoubworld.java.utils.compress.SymbolComplex.SymbolHuffman;
import com.zoubworld.java.utils.compress.SymbolComplex.SymbolINT;
import com.zoubworld.java.utils.compress.SymbolComplex.SymbolINT12;
import com.zoubworld.java.utils.compress.SymbolComplex.SymbolINT16;
import com.zoubworld.java.utils.compress.SymbolComplex.SymbolINT24;
import com.zoubworld.java.utils.compress.SymbolComplex.SymbolINT32;
import com.zoubworld.java.utils.compress.SymbolComplex.SymbolINT4;
import com.zoubworld.java.utils.compress.SymbolComplex.SymbolINT48;
import com.zoubworld.java.utils.compress.SymbolComplex.SymbolINT64;
import com.zoubworld.java.utils.compress.SymbolComplex.SymbolINT8;
import com.zoubworld.java.utils.compress.file.BinaryStdIn;
import com.zoubworld.java.utils.compress.file.BinaryStdOut;
import com.zoubworld.java.utils.compress.file.IBinaryReader;
import com.zoubworld.java.utils.compress.file.IBinaryWriter;
import com.zoubworld.utils.JavaUtils;

/** Symbol class represents basically a character in a file.
 * so the commun understood is that is a byte of 8 bits, 
 * for example it is equal to 0x55 for the letter U as define on the ascii table.
 * but here we extend the concept upper to 255 to create extra new symbol that coulb be usefull to perform algo of compression.
 * so Symbol is an abstraction of a char. This char have a meaning 'U'(ISymbol) for example and a code 0x55(Icode).
 * 
 * A default relationchip exists between code and symbol, but accroding to the context we can would like to change it.
 * this is a goal of the class ICondingRule, it define for each Symbol an associated Code.
 * this code can have different nature, the natif one :symbol(127)->Code(127) or an huffnam coding or somthing else.
 * 
 * 
 * 
 * symbol class that define 0..255 symbol foreach byte value, and associate a
 * coding that are defaultly the same value. Symbol after 255 are special, it is
 * a concept with a coding rule e.i.: it presents something with a way to code
 * it. This is often to manage algo and internal state on compressed file
 * i.e. INT4(5) represent the number 5, its coding is the coding of symbol INT4
 * plus 4 bit representing 5, so 0b1001 EOF represent the end of a file, its
 * coding it Composed symbol are an association of symbol to describe something
 * more complex. example RLE+INT4(5)+symbol represent the repetition of 'symbol'
 * 5 times, its coding is the concatenation of code(RLE), Code(INT4(5)),
 * code(Symbol)
 */
public class Symbol implements ISymbol {
	/* symbol 0..255 : code 0..255 */

	public static Symbol INT4 = new Symbol(0x100, new Code(256));// 4 bit number coding : INT4 +0Bxxxx
	public static Symbol INT8 = new Symbol(0x101, new Code(257));// 8 bit number coding : INT8 +0Bxxxxxxxx
	public static Symbol INT12 = new Symbol(0x102, new Code(258));// 16 bit number coding : INT12+0Bxxxxxxxxxxxxxxxx
	public static Symbol INT16 = new Symbol(0x103, new Code(259));// 16 bit number coding : INT16+0Xxxxx
	public static Symbol INT24 = new Symbol(0x104, new Code(260));// 24 bit number coding : INT24+0Xxxxxxx
	public static Symbol INT32 = new Symbol(0x105, new Code(261));// 32 bit number coding : INT32+0Xxxxxxxxx
	public static Symbol INT48 = new Symbol(0x106, new Code(262));// 32 bit number coding : INT48+0Xxxxxxxxxxxxx
	public static Symbol INT64 = new Symbol(0x107, new Code(263));// 32 bit number coding : INT68+0Xxxxxxxxxxxxxxxxx
	// INTN n bit number coding INTN+0x.....
	// INTi i bit number coding INTi+0x.....
	// INTj j bit number coding INTj+0b.....

	// dictionary/words algo and utility symbol
	public static Symbol RLE = new Symbol(0x108, new Code(264));// RLE compression symbol; use : symbol+RLE+N
	public static Symbol RPE = new Symbol(0x109, new Code(265));//
	public static Symbol LZW = new Symbol(0x10A, new Code(266));// Ziv and Lempel and Welch compression method : LZW+
																// index inside the dico
	public static Symbol PIE = new Symbol(0x10B, new Code(267));// Past Index encoding : PIE+index+Size
	public static Symbol HUFFMAN = new Symbol(0x10C, new Code(268));// New Huffman table :HUF+ INT32+N+tab[n] ;
																	// tab[i]=0xlleeeeee // size: ~1.1ko
	public static Symbol EOF = new Symbol(0x10D, new Code(269));// End of File
	public static Symbol HOF = new Symbol(0x10E, new Code(270));// Header of File
	public static Symbol EOS = new Symbol(0x10F, new Code(271));// End of String
	public static Symbol EOBS = new Symbol(0x110, new Code(272));// End of Bit Stream
	public static Symbol PAT = new Symbol(0x111, new Code(273));// pattern : PAT+INT(n)+n*symbol[0..255+WILDCARD]
	/*
	 * example :
	 * "BPL858,1,42,20,1,2,59B08_FA136_MAG_000_PB,pass_bin,1,1,,59B08,,BPL858,,,,,2_1,,2016-11-23T17:42:10"
	 * declare a PAT0 : PAT+INT(187)+
	 * "BPL858,*,*,*,*,*,59B08_FA136_MAG_000_PB,*,*,*,*,59B08,*,BPL858,*,*,*,*,*,*,2016-11-23*"
	 * * is symbol Wildcard or IntAsASCII+Wildcard or IntAsASCII+Wildcard, encode
	 * example : PATr(0)+"1"+"42"+"20"+"1"+
	 * 2"+"pass_bin"+"1"+"1"+""+""+""+""+""+""+"2_1"+""+"T17:42:10" "" is symbol
	 * Empty "42" will be replace by a INT6(42) "1" will be replace by a INT4(1)
	 * "pass_bin" will be SOS+"pass_bin"+EOS or SOl(8)+"pass_bin" or RPT(12,32)
	 */
	public static Symbol PATr = new Symbol(0x112, new Code(274));// pattern repeated: PATr+INT(x)+few symbol=wildcard
	public static Symbol Wildcard = new Symbol(0x113, new Code(275));// wildcard
	public static Symbol Empty = new Symbol(0x114, new Code(276));// this is an empty symbol meaning that code has a
																	// size of 0.

	// specialized symbol to represent a list of octet.
	public static Symbol IntAsASCII = new Symbol(0x115, new Code(277));// it represents an String of a integer, composed
																		// symbol :INTASASCII,INTxx(yy) replace
																		// String.format("%i",yy).
	public static Symbol TBD = new Symbol(0x116, new Code(278));// it represents an String of a float, composed symbol
																// :FLOATASASCII,x[4bit],y[4bit],float[32] replace
																// String.format("%x.yf",z).
	public static Symbol FloatAsASCII = new Symbol(0x117, new Code(279));// it represents an String of a float composed
																			// symbol :FLOATASASCII,INTn(f) replace
																			// String.format("%1.f",float(f)).
	public static Symbol FloatAsASCIIes2 = new Symbol(0x118, new Code(280));// it represents an String of a float in
																			// scientific notation, composed symbol
																			// :FLOAT1ASASCIIes2,INTn(f) replace
																			// String.format("%.e",float(f)).
	public static Symbol DoubleAsASCIIes3 = new Symbol(0x119, new Code(281));// it represents an String of a double in
																				// scientific notation, composed symbol
																				// :FLOAT1ASASCIIes3,INTn(f) replace
																				// String.format("%.g",float(f)).
	public static Symbol CRLF = new Symbol(0x11A, new Code(282));// CRLF symbol to replace CR+LF(\0x13\0x10)
	public static Symbol SOS = new Symbol(0x11B, new Code(283));// Start of String.
	public static Symbol SOln = new Symbol(0x11C, new Code(284));// String of length n. SOl+INTxx(n) +...symbols....
	public static Symbol Qn_mAsASCII = new Symbol(0x11D, new Code(285));// it represents an String of a decimal number
																		// with fixed point composed symbol
																		// :Qn_mAsASCII+INTn+INTm replace
																		// String.format("%d.%d",(signed)n,(unsigned)m).
																		// (https://en.wikipedia.org/wiki/Fixed-point_arithmetic)
	public static Symbol INTN = new Symbol(0x11E, new Code(286));// 38..101 bit number coding : INTNX[N=6 bit
																	// ]+X=0Xxxxxxxxx , (N+38) is the number of bit
																	// after to describe X.
	public static Symbol SAliasn = new Symbol(0x11F, new Code(287));// String alias nï¿½ SOl+INTxx(n), it replace a nth
																	// declaration of Soln/SOS...EOS
	public static Symbol IntAsHEX = new Symbol(0x120, new Code(288));// it represents an String of a integer in upper
																		// case, composed symbol :INTASASCII,INTxx(yy)
																		// replace String.format("%X",yy).
	public static Symbol IntAsHex = new Symbol(0x121, new Code(289));// it represents an String of a hex integer in
																		// lower case, composed symbol
																		// :INTASASCII,INTxx(yy) replace
																		// String.format("%x",yy).
	public static Symbol INTj = new Symbol(0x122, new Code(290));// n bit number coding : INTnX[j=3 bit ]+X=0Xxxxxxxxx ,
																	// (n=>20,21,22,28,29,30,36,37) is the number of bit
																	// after to describe X.
	public static Symbol INTi = new Symbol(0x123, new Code(291));// 1,5,9,13 bit number coding : INTnX[n=2 bit
																	// ]+X=0Xxxxxxxxx , (n=>1,5,9,13) is the number of
																	// bit after to describe X.
	public static Symbol BigINTn = new Symbol(0x124, new Code(292));// bit number coding : BigINTnmX[N=3 bit,M=10 bits
																	// ]+X=0Xxxxxxxxx ,
																	// (100+N:M->100..8292)(2^(6+n)+m*16) is the number
																	// of bit after to describe X.

	public static Symbol LZS = new Symbol(0x125, new Code(1, 1));// https://en.wikipedia.org/wiki/Lempel%E2%80%93Ziv%E2%80%93Stac
	public static Symbol LZS_EndMarker = new Symbol(0x126, new Code(0b110000000, 9));// https://en.wikipedia.org/wiki/Lempel%E2%80%93Ziv%E2%80%93Stac

	public static Symbol BPE = new Symbol(0x127, new Code(295));// BytePairEncoding :BPE
																// https://en.wikipedia.org/wiki/Byte_pair_encoding

	public static Symbol TableSwap = new Symbol(0x128, new Code(296));// TableSwap+xsize+ysize + xsize*ysize symbols :
																		// define a table that has been transposed from
																		// x/y to y/x, this is a good way to reduce
																		// entropie and optimize RLE or dictionary algo.
																		// this is powerfull after a pattern algo.
	public static Symbol Row = new Symbol(0x129, new Code(297));// Row : define the location to put the row of the
																// TableSwap, this is ordered.
	public static Symbol RPT = new Symbol(0x12A, new Code(298));// ...+RTP+Intn(l)+Intn(c) : repete the previous string
																// of length (l) count times(c)
	public static Symbol BTE = new Symbol(0x12B, new Code(299));// ByteTripleEncoding :BTE derivated from BPE :
	public static Symbol BWT = new Symbol(0x12C, new Code(300));// Burrows–Wheeler transform :BWT +Int(n) :
	public static Symbol LZSe = new Symbol(0x12D, new Code(301));// https://en.wikipedia.org/wiki/Lempel%E2%80%93Ziv%E2%80%93Stac
																// https://en.wikipedia.org/wiki/Burrows%E2%80%93Wheeler_transform

	public static Symbol HuffRef = new Symbol(0x12E, new Code(302));// New reference to an existing Huffman table :HUF+ INTn;
	// tab[i]=0xlleeeeee // size: ~1.1ko
	public static Symbol Stack=new Symbol(0x12F,new Code(303));//	 :Stack+Newsymbol, else we use directly the Icode associated to an existing element.
	
	public static Symbol Mark=new Symbol(0x130,new Code(304));//	 :Mark+len, identify a word of length len where char are before the mark, it is register inside a dictionary at index++
	public static Symbol UseMark=new Symbol(0x131,new Code(305));//	 :UseMark+index, use a recorded word at index index in a dictionary.
	public static Symbol CodingSet=new Symbol(0x132,new Code(306));//	 :CodingSet(defaultcoding)+Classindex(Golomb4Coding)+Configindex(Golomb4Coding), used to defind the translate from symbol to code
	public static Symbol Alphabet=new Symbol(0x133,new Code(307));//	 :Alphabet(defaultcoding)+ISymbol.list.index(Golomb4Coding)+Configindex(Golomb4Coding), used to defind the translate from symbol to code
	public static Symbol Tuple=new Symbol(0x134,new Code(308));//	 :Tuple Number, 
	public static Symbol Null=new Symbol(0x135,new Code(309));//	 :Null Symbol, 

	// https://en.wikipedia.org/wiki/Single-precision_floating-point_format
	// INTntoFLOAT convertion : INT12=abcdefghijkl.. : float : seeeeeeeedd....dd( 8e
	// 23d)
	/*
	 * a->s bcdefghi->eeeeeeee jkl..->dd..dd
	 */

	// https://en.wikipedia.org/wiki/Double-precision_floating-point_format
	// INTntoDOUBLE convertion : INT24=abcdefghijkl mnopqrst.. :
	// double:seeeeeeeeeeeddddddddd....dd(11e 52d)
	/*
	 * a->s bcdefghijkl->eeeeeeeeeee mnopqrst..->dddd...
	 */
	// 1.03125=>FloatAsASCII+INT28(0 00000001 001 1001 0010 1101 0101)
	// 7*8b=56b => 2s+28b=~40
	public static Symbol special[] = { INT4, INT8, INT12, INT16, INT24, INT32, INT48, INT64, // should be ordered
			RLE, RPE, LZW, PIE, HUFFMAN, EOF, HOF, EOS, EOBS, PAT, PATr, Wildcard, Empty, IntAsASCII, TBD, FloatAsASCII,
			FloatAsASCIIes2, DoubleAsASCIIes3, CRLF, SOS, SOln, Qn_mAsASCII, INTN, SAliasn, IntAsHEX, IntAsHex, INTj,
			INTi, BigINTn, LZS, LZS_EndMarker, BPE, TableSwap, Row, RPT, BTE,BWT,LZSe,HuffRef,
			Stack,Mark,UseMark,CodingSet,Alphabet,Tuple,Null };
	// EOD, SOD/SOL EOS EOL NIL EndOfData StartOfData /
	// StartOfList,NextInList,EndOfList,EndOfString
	// Multi file : SOL ... NIL ... NIL ... ... EOL, SOD
	// ...l<sym>...EOF....EOF....EOD
	// ...=file.path/sizeU64/date+time
	/*
	 * static Symbol EOF=new Symbol("EOF",new Code(257));// End Of file symbol; use
	 * [data symbol]+EOF+[data symbol]+EOF
	 * 
	 * static Symbol FL=new Symbol("FL",new Code(258));// File List symbol; use FL
	 * path; FL; path;....
	 */
	public static void initCode() {
		int R = 256 + Symbol.special.length;
		int Nb = (int) (Math.log(R) / Math.log(2) + 1);

		for (int i = 0; i < Symbol.tabId.length; i++) {
			ISymbol s = Symbol.findId(i);
			if (s.getId() < 256)
				s.setCode(new Code((byte) s.getId(), Nb));
			else
				s.setCode(new Code((int) s.getId(), Nb));
		}
	}

	public Symbol() {
		// TODO Auto-generated constructor stub
	}


public static char[] listSymbolToCharSeq(List<ISymbol> ls)
{
	 char[] ac= new char[ls.size()];
	 int i=0;
	 for(ISymbol s: ls)
	 {
		 ac[i++]=s.getChar();
	 }
	 
	return ac;
}
/**
 * a string is a list of char ending by \0, from ls, a list of symbol ending by
 * EOS but other Symbol can exist after EOS, it will be ignore.
 */
public static String listSymbolToString(List<ISymbol> ls) {
	String s = "";
	int index = 0;
	while (index < ls.size() && ls.get(index) != Symbol.EOS) {
		s += (ls.get(index++).getChar());
	}

	return s;
}
public static String listSymbolToString(int fromindex,List<ISymbol> ls)
{
	 String s="";
	 int index=fromindex;
	 while(index<ls.size() && ls.get( index )!=Symbol.EOS)
	 {
		 s+=(ls.get( index++ ).getChar());
	 }
	 
	return s;
}

	ICode code = null;

	public Symbol(char charAt) {
		symbol = new byte[1];
		symbol[0] = (byte) charAt;
	}

	private Symbol(String s, ICode cc) {
		symbol = new byte[s.length()];
		for (int i = 0; i < s.length(); i++)
			symbol[i] = (byte) s.charAt(i);
		code = cc;
	}

	private Symbol(int s, ICode cc) {
		symbol = new byte[4];
		for (int i = 0; i < 4; i++)
			symbol[i] = (byte) ((s >> ((3 - i) * 8)) & 0xff);
		code = cc;
	}

	public Symbol(long s, ICode cc) {
		symbol = new byte[8];
		for (int i = 0; i < 8; i++)
			symbol[i] = (byte) ((s >> ((7 - i) * 8)) & 0xff);
		code = cc;
	}

	public Symbol(String s) {
		symbol = new byte[s.length()];
		for (int i = 0; i < s.length(); i++)
			symbol[i] = (byte) s.charAt(i);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#isChar()
	 */
	@Override
	public boolean isChar() {
		return symbol.length == 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#isInt()
	 */
	@Override
	public boolean isInt() {
		return symbol.length == 4;
	}

	public boolean isLong() {
		return symbol.length == 8;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#isShort()
	 */
	@Override
	public boolean isShort() {
		return symbol.length == 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#getChar()
	 */
	@Override
	public char getChar() {
		int a = ((int) (symbol[0] & 0xff));
		return (char) a;
	}

/** it work for symbol between 0..255
 * 
 * */
public static void listSymbolToFile(List<ISymbol> ls,String outputFile)
{
	listSymbolToFile( ls, outputFile, 8);
}
public static void listSymbolToFile(List<ISymbol> ls,String outputFile, int size)
{

	 
	 try {
		    OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

		   {
		    	for( ISymbol s:ls)
		    	{
		    		if (s.getCode()==null)
		    		{
		    			if (size==32)
			    			s.setCode(new Code((int) s.getId()));
		    			if (size==16)
			    			s.setCode(new Code((short) s.getId()));
		    			if (size==8)
			    			s.setCode(new Code((char) s.getId()));
		    			if (size==64)
			    			s.setCode(new Code((long) s.getId()));
		    			

		    			System.err.println("warning no code for '"+s+"'");	
		    		}
		    		if (s.getCode()!=null)
		    		{
		    			if (size==8)
		    			{
		    				if (s.getCode().length()==8)
		    				outputStream.write((char) (s.getCode().toCode()[0] & 0xff));
		    				else
		    				  outputStream.write((char)(s.getCode().toCode()[(s.getCode().length()-1)/8] & 0xff));
		    			}
		    			else
		    				if (size==16)
		    				{
		    					outputStream.write((char)(s.getCode().toCode()[1]& 0xff));
		    					outputStream.write((char)(s.getCode().toCode()[0]& 0xff));
		    				}
			    			else
			    				if (size==24)
				    				{
			    					outputStream.write((char)(s.getCode().toCode()[2]& 0xff));
			    					outputStream.write((char)(s.getCode().toCode()[1]& 0xff));
			    					outputStream.write((char)(s.getCode().toCode()[0]& 0xff));
				    				}
				    			else
				    					{
				    				outputStream.write((char)(s.getCode().toCode()[3]& 0xff));
				    				outputStream.write((char)(s.getCode().toCode()[2]& 0xff));
				    				outputStream.write((char)(s.getCode().toCode()[1]& 0xff));
				    				outputStream.write((char)(s.getCode().toCode()[0]& 0xff));
				    					}
					    				
		    		}		//outputStream.write(s.getCode().code[1]);
		    		else
		    			System.err.println("error no code for '"+s+"'");
		    		
		    		}
		    		
		 //   outputStream.write(buffer,0,size);
		    	outputStream.close();
		    }}
		  catch (IOException ex) {
		        ex.printStackTrace();
		}
}

	
	/** joint list of list of symbol
	 * */
	public static List<ISymbol> join(List<List<ISymbol>> ls)
	{
		List<ISymbol> la=new ArrayList<ISymbol>();
	
		for(List<ISymbol> l :ls)
			la.addAll(l);
			return la;
	}
	/** convert symbol into string (symbol should come from ascii table).
	 * */
	public static String toString(List<ISymbol> ls) {
		/*
		StringBuffer s = new StringBuffer();
		int index = 0;
		while (index < ls.size()) {
			s.append(ls.get(index++).getChar());
		}

		return s.toString();*/
		return ls.stream().map(x->""+x.getChar()).collect(Collectors.joining());
	}
	/**
	 * it build a list of symbol between 0..255
	 * 
	 */
	public static List<ISymbol> factoryFile(String inputFile) {
		return factoryFile(inputFile, 8);

	}

	/*
	 * not yet implemented for sizecode!=8
	 */
	private static List<ISymbol> factoryFile(String inputFile, int sizecode) {
		List<ISymbol> ls = new ArrayList<ISymbol>();
		try {
			InputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
			byte[] buffer = new byte[1024 * 1024];
			int size = 0;
			while ((size = inputStream.read(buffer)) != -1) {
				ls.addAll(Symbol.ByteArrayToListSymbol(buffer, size));

				// outputStream.write(buffer,0,size);
			}
			inputStream.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return ls;
	}

	/** Parse String test to generate List of Isymbol
	 * */
	public static List<ISymbol> factoryCharSeq(String text) {
		List<ISymbol> ls = new ArrayList<ISymbol>();
		if (text.length() == 0)
			ls.add(Symbol.Empty);
		else
			for (char c : text.toCharArray()) 
			{
				ls.add(Symbol.findId((int) c));
				}
		return ls;
				}
public static ISymbol from(char charAt)
{
	return new Symbol( charAt);
}
/** write the list of Symbols according to codingRule into file
 * */
public static void toFile(File file,ICodingRule codingRule,List<ISymbol> ls)
{
	IBinaryWriter bin=new BinaryStdOut(file);
	;
	bin.setCodingRule(codingRule);
	//bin.write(codingRule);
	bin.writes(ls);
	bin.close();	
}
/** write the codingRule and list of Symbols according to codingRule into file
 * */
public static void toAFile(File file,ICodingRule codingRule,List<ISymbol> ls)
{
	IBinaryWriter bin=new BinaryStdOut(file);
	bin.write(codingRule);
	bin.setCodingRule(codingRule);
	//codingRule.apply(ls);
	bin.writes(ls);
	bin.close();	
}
/** save file from uncompressed List of symbol, symbols id must be 0..255
 * */
public static void toAFile(File file,List<ISymbol> ls)
{
	IBinaryWriter bin=new BinaryStdOut(file);
	
	bin.setCodingRule(new CodingSet(com.zoubworld.java.utils.compress.CodingSet.UNCOMPRESS) );
	bin.writes(ls);
	bin.close();	
}
/** write as is the list of codes into file
 * */
public static void toFile(File file,List<ICode> lc)
{
	IBinaryWriter bin=new BinaryStdOut(file);
	bin.write(lc);
	bin.close();	
}
/** read the codingRule and list of Symbols according to codingRule from file
 * */
public static List<ISymbol> FromAFile(File file)
{
	IBinaryReader bin=new BinaryStdIn(file);
	ICodingRule codingRule=ICodingRule.ReadCodingRule(bin);
	bin.setCodingRule(codingRule);
	List<ISymbol> ls = bin.readSymbols();
	bin.close();	
	return ls;
}
/** read as is a file and convert it as list of symbol.
 * */
public static List<ISymbol> from(File file)
{
	 byte[] allBytes = null;
	  try {
	   InputStream inputStream = new FileInputStream(file);

       long fileSize =  file.length();

        allBytes = new byte[(int) fileSize];

       inputStream.read(allBytes);


   } catch (IOException ex) {
       ex.printStackTrace();
   }
	  List<ISymbol> ls=new ArrayList<ISymbol>();
	  
	for(byte c:allBytes)
		  ls.add(Symbol.findId(c));
	ls.add(Symbol.EOF);
	return  ls;
}

	

	/** Parse binary file to generate List of Isymbol
	 * *
	public static List<ISymbol> from(File inputFile) {
		List<ISymbol> ls = new ArrayList<ISymbol>();
		try (
		        InputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
			) {
		 
		    byte[] buffer = new byte[4096];
		 
		 
			int l;
			while ((l=inputStream.read(buffer)) != -1) {
		    	for(int i=0;i<l;i++)
		    		ls.add(Symbol.findId((int)(buffer[i]&0xff)));
		    }
		 
		} catch (IOException ex) {
		        ex.printStackTrace();
		}
		return ls;
	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#getInt()
	 */
	@Override
	public Integer getInt() {

		if (symbol.length == 4) {
			int i = (symbol[0] & 0xff);
			i = (i << 8) + (symbol[1] & 0xff);
			i = (i << 8) + (symbol[2] & 0xff);
			i = (i << 8) + (symbol[3] & 0xff);
			return i;
		}
		return null;
	}

	public Long getLong() {

		if (symbol.length == 8) {
			long i = (symbol[0] & 0xff);
			i = (i << 8) + (symbol[1] & 0xff);
			i = (i << 8) + (symbol[2] & 0xff);
			i = (i << 8) + (symbol[3] & 0xff);
			i = (i << 8) + (symbol[4] & 0xff);
			i = (i << 8) + (symbol[5] & 0xff);
			i = (i << 8) + (symbol[6] & 0xff);
			i = (i << 8) + (symbol[7] & 0xff);
			return i;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#getShort()
	 */
	@Override
	public short getShort() {

		if (symbol.length == 2) {
			int i = (symbol[0] & 0xff);
			i = (i << 8);
			i = i + (((int) symbol[1]) & 0xff);

			return (short) i;
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#getId()
	 */
	@Override
	public long getId() {
		if (isChar())
			return getChar();
		if (isShort())
			return getShort();
		if (isInt())
			return getInt();
		if (isLong())
			return getLong();

		return 0;
	}

	public static List<ICode> toCode(List<ISymbol> ls) {
		List<ICode> lc = new ArrayList<ICode>();
		for (ISymbol c : ls)
			lc.add(c.getCode());
		return lc;
	}
	
	public static List<ICode> toCode(List<ISymbol> ls,ICodingRule cs) {
		List<ICode> lc = new ArrayList<ICode>();
		for (ISymbol c : ls)
			lc.add(cs.get(c));
		return lc;
	}

	public Symbol(byte b) {
		symbol = new byte[1];
		symbol[0] = (byte) b;
	}

	public Symbol(int count) {
		symbol = new byte[4];
		symbol[0] = (byte) ((count >> 24) & 0xff);
		symbol[1] = (byte) ((count >> 16) & 0xff);
		symbol[2] = (byte) ((count >> 8) & 0xff);
		symbol[3] = (byte) ((count >> 0) & 0xff);
	}

	public Symbol(BigInteger i) {
		symbol = i.toByteArray();		
	}
	public Symbol(short count) {
		symbol = new byte[2];
		symbol[0] = (byte) ((count >> 8) & 0xff);
		symbol[1] = (byte) ((count >> 0) & 0xff);
	}

	public Symbol(long s) {
		symbol = new byte[8];
		for (int i = 0; i < 8; i++)
			symbol[i] = (byte) ((s >> ((7 - i) * 8)) & 0xff);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object) 2 symbol are equal if they
	 * have the same id, there is on length care
	 */
	@Override
	public boolean equals(Object obj) {
		if (Symbol.class.isInstance(obj)) {
			Symbol c = (Symbol) obj;
			/*
			 * if (c.symbol.length!=symbol.length) return false;
			 */
			if (c.getId() != (getId()))
				return false;
			else
				return true;
		}
		return super.equals(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int i = symbol.length;
		i ^= Long.hashCode(getId());
		return i;
	}

	private byte symbol[] = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#toSymbol()
	 */
	@Override
	public char[] toSymbol() {
		char[] chart = new char[symbol.length];
		for (int i = 0; i < chart.length; i++)
			chart[i] = (char) symbol[i];
		return chart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#toString()
	 */
	@Override

	public String toString() {
		int i = (int) getId();
		if ((symbol.length == 1))
			return ((((i > 31) && (i < 127)) ? ("'" + (char) i + "'") : (String.format("\\x%1x", i))));

		switch (i) {
		case 0x100:
			return "INT4";
		case 0x101:
			return "INT8";
		case 0x102:
			return "INT12";
		case 0x103:
			return "INT16";
		case 0x104:
			return "INT24";
		case 0x105:
			return "INT32";
		case 0x106:
			return "INT48";
		case 0x107:
			return "INT64";
		case 0x108:
			return "RLE";
		case 0x109:
			return "RPE";
		case 0x10A:
			return "LZW";
		case 0x10B:
			return "PIE";
		case 0x10C:
			return "HUF";
		case 0x10D:
			return "EOF";
		case 0x10E:
			return "HOF";
		case 0x10F:
			return "EOS";
		case 0x110:
			return "EOBS";// End of Bit Stream
		case 0x111:
			return "PAT";
		case 0x112:
			return "PATr";
		case 0x113:
			return "Wildcard";
		case 0x114:
			return "Empty";
		case 0x115:
			return "IntAsASCII";
		case 0x117:
			return "FloatAsASCII";
		case 0x118:
			return "FloatAsASCIIes2";
		case 0x119:
			return "DoubleAsASCIIes3";
		case 0x11A:
			return "CRLF";
		case 0x11B:
			return "SOS";
		case 0x11C:
			return "SOln";
		case 0x11D:
			return "Qn_mAsASCII";
		case 0x11E:
			return "INTn";
		case 0x11F:
			return "SAliasn";			
		case 0x121:
			return "IntAsHex";
			
		case 0x122:
			return "INTj";
		case 0x123:
			return "INTi";
		case 0x124:
			return "BigINTn";
		case 0x125:
			return "LZS";
		case 0x126:
			return "LZS_EndMarker";
		case 0x127:
			return "BPE";
		case 0x128:
			return "TableSwap";
		case 0x129:
			return "Row";		
		case 0x12A:
			return "RPT";
		case 0x12B:
			return "BTE";
		case 0x12C:
			return "BWT";
		case 0x12D:
			return "LZSe";
		case 0x12E:
			return "HuffRef";
		case 0x12F:
			return "Stack";
		case 0x130:
			return "Mark";
		case 0x131:
			return "UseMark";
		case 0x132:
			return "CodingSet";			
		}
		String s = getClass().getSimpleName()+"(0x";

		for (int j = 0; j < symbol.length; j++)
			s += String.format("%2x", symbol[j]);
		s += ")";
		return s;
	}

	public static List<ISymbol> from(String text) {
		return factoryCharSeq(text);
	}

	public static List<ISymbol>[] from(String text[]) {
		List<ISymbol>[] als = new ArrayList[text.length];
		int count = 0;
		for (String t : text)
			als[count++] = factoryCharSeq(t);
		return als;
	}
	static public List<ISymbol> from(long[] d)
	{
		List<ISymbol> l=new ArrayList<ISymbol> ();
		for(long i:d)
			l.add( FactorySymbolINT(i));
		return l;
		
	}
	public static List<ISymbol> ByteArrayToListSymbol(byte[] datas, int size) {
		List<ISymbol> l = new ArrayList<ISymbol>(size);
		for (int i = 0; i < size; i++) {
			char c = (char) (datas[i] & 0xff);
			
			l.add(Symbol.findId(c));
		}
		return l;
	}

	// symbol list
	static ISymbol tabId[] = new Symbol[256 + special.length];

	public static ISymbol findId(byte c) {
		return findId(0xff &(int) c);
	}
		public static ISymbol findId(int c) {
			/*	if (c < 0)
			c = 256 + c + 0;*/
		if (c >= tabId.length)
			return null;

		if (tabId[c] == null) {
			if (c < 256) {
				tabId[c] = new Symbol((byte) c);
				tabId[c].setCode(new Code((byte) c));
				tabId[c].getCode().setSymbol(tabId[c]);
			} else
				tabId[c] = special[c - 256];

		}
		return tabId[c];
	}

	public static Symbol getFromSet(Set<Symbol> keySet, char charAt) {
		for (Symbol s : keySet)
			if ((s.symbol[0] == charAt) && (s.symbol.length == 1))
				return s;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#getCode()
	 */
	@Override
	public ICode getCode() {
		// TODO Auto-generated method stub
		return code;
	}

	static public ICode toCode(ISymbol s) {
		if (s != null)
			return s.getCode();
		return null;
	}
	static public ICode toCode(ISymbol s, ICodingRule cs) {
		
		if (cs != null)
			return cs.get(s);
		if (s != null)
			return s.getCode();
		return null;
	}

	/**
	 * convert a list of symbol into a list of code
	 */
	static public List<ICode> toCodes(List<ISymbol> ls) {
		if (ls != null)
			return ls.stream().map(s -> toCode(s)).collect(Collectors.toList());
		return null;
	}

	/**
	 * convert a list of symbol into a list of code
	 */
	static public List<ICode> toCodes(List<ISymbol> ls, ICodingRule cs) {
		if (ls != null)
			return ls.stream().map(s -> cs.get(s)).collect(Collectors.toList());
		return null;
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.zoubwolrd.java.utils.compress.ISymbol#setCode(net.zoubwolrd.java.utils.
	 * compress.Code)
	 */
	@Override
	public void setCode(ICode code2) {
		code = code2;

	}

	/**
	 * complex symbol embed a data, this function read the data associated to a
	 * complex symbol.
	 */
	public static ISymbol decode(ISymbol SimpleSym, IBinaryReader binaryStdIn) {
		if(SimpleSym.getId()<256)
			return SimpleSym;
		switch ((int) SimpleSym.getId()) {
		case 256:// INT8.getId() :
			return new SymbolINT4(binaryStdIn);
		case 257:// INT16.getId() :
			return new SymbolINT8(binaryStdIn);
		case 258:// INT8.getId() :
			return new SymbolINT12(binaryStdIn);
		case 259:// INT16.getId() :
			return new SymbolINT16(binaryStdIn);
		case 260:// INT24.getId() :
			return new SymbolINT24(binaryStdIn);
		case 261:// INT32.getId() :
			return new SymbolINT32(binaryStdIn);
		case 262:// INT48.getId() :
			return new SymbolINT48(binaryStdIn);
		case 263:// INT64.getId() :
			return new SymbolINT64(binaryStdIn);
		case 0x124:// BigINTn
			return new SymbolBigINT(binaryStdIn);
		case 0x10D:// EOF
			return SimpleSym;
		case 0x10E:// HOF
			return SimpleSym;
		case 0x10F:// EOS
			return SimpleSym;			
		case 0x10C:// HUFFMAN
			//return new SymbolHuffman(HUFFMAN,binaryStdIn);
		case 0x134:// HuffRef
			//return new SymbolHuffman(HuffRef,binaryStdIn);*/
			return SimpleSym;
			
		/*
		 * NEWHUFF:table(n+1)= USEHUFFTABLE(n)
		 *default: // Optional
			return SimpleSym;*/
		 default:
				throw new NotImplementedException("symbol : " + SimpleSym);
		}
	}

	/**
	 * return the list of symbol representing f
	 */
	public static List<ISymbol> FactorySymbolFloatAsASCII(Float f) {
		List<ISymbol> ls2 = new ArrayList<>();
		ls2.add(Symbol.FloatAsASCII);
		ls2.add(Symbol.FactorySymbolINT(Float.floatToIntBits(f)));
		return ls2;
	}

	/**
	 * return the list of symbol representing d
	 */
	public static List<ISymbol> FactorySymbolDoubleAsASCIIes3(Double d) {
		List<ISymbol> ls2 = new ArrayList<>();
		ls2.add(Symbol.FloatAsASCII);
		ls2.add(Symbol.FactorySymbolINT(Double.doubleToLongBits(d)));
		return ls2;
	}

	/**
	 * give the int/long value of a INT../INTn symbol
	 */
	public static Long getINTn(ISymbol s) {
		CompositeSymbol cs;

		if (CompositeSymbol.class.isInstance(s))
			cs = (CompositeSymbol) s;
		else
			cs = null;
		if (cs != null)
			switch ((int) cs.getId()) {
			case 0x100:
				return cs.getS2().getId();// INT4
			case 0x101:
				return cs.getS2().getId();// "INT8";
			case 0x102:
				return cs.getS2().getId();// "INT12";
			case 0x103:
				return cs.getS2().getId();// "INT16";
			case 0x104:
				return cs.getS2().getId();// "INT24";
			case 0x105:
				return cs.getS2().getId();// "INT32";
			case 0x106:
				return cs.getS2().getId();// "INT48";
			case 0x107:
				return cs.getS2().getId();// "INT64";
			/*
			 * case 0x108 : return "RLE"; case 0x109 : return "RPE"; case 0x10A : return
			 * "LZW"; case 0x10B : return "PIE"; case 0x10C : return "HUF"; case 0x10D :
			 * return "EOF"; case 0x10E : return "HOF"; case 0x10F : return "EOS"; case
			 * 0x110 : return "EOBS";// End of Bit Stream case 0x111 : return "PAT"; case
			 * 0x112 : return "PATr"; case 0x113 : return "Wildcard"; case 0x114 : return
			 * "Empty"; case 0x115 : return "IntAsASCII"; case 0x117 : return
			 * "FloatAsASCII"; case 0x118 : return "FloatAsASCIIes2"; case 0x119 : return
			 * "DoubleAsASCIIes3"; case 0x11A : return "CRLF"; case 0x11B : return "SOS";
			 * case 0x11C : return "SOln"; case 0x11D : return "Qn_mAsASCII";
			 */
			case 0x11E:
				return cs.getS2().getId();// "INTn";
			// case 0x11F : return "SAliasn";
				default:
					throw new NotImplementedException("symbol : " + s);
			}
		return null;

	}

	/**
	 * give the double value of a INT../INTn symbol preceded by DoubleAsAscii*
	 * Symbol
	 */
	public static Double getDoubleAsASCII(ISymbol s) {
		Long l = getINTn(s);
		if (l == null)
			return null;
		return Double.longBitsToDouble(l);
	}

	/**
	 * give the float/double value of a INT../INTn symbol preceded by FloatAsAscii*
	 * Symbol
	 */
	public static Float getFloatAsASCII(ISymbol s) {
		Long l = getINTn(s);
		if (l == null)
			return null;
		return Float.intBitsToFloat(l.intValue());
	}

	/**
	 * return the list of symbol representing l
	 */
	public static List<ISymbol> FactorySymbolIntAsASCII(Long l) {
		List<ISymbol> ls2 = new ArrayList<>();
		ls2.add(Symbol.IntAsASCII);
		ls2.add(Symbol.FactorySymbolINT(l));
		return ls2;
	}

	/** return an signed int i */
	public static SymbolINT FactorySymbolINT(BigInteger i) {
		try {
	long l=i.longValueExact();
	return FactorySymbolINT(l);
		}
		catch(ArithmeticException e)
		{
			return new SymbolBigINT(i);
		}
	
	}
	/** return an signed int i */
	public static SymbolINT FactorySymbolINT(long i) {
		if ((i >= 0)) {
			if ((i < 16))
				return new SymbolINT4((byte) i);
			if ((i <= Byte.MAX_VALUE))
				return new SymbolINT8((byte) i);
			if ((i < 128L * 16L))
				return new SymbolINT12((short) i);
			if ((i <= Short.MAX_VALUE))
				return new SymbolINT16((short) i);
			if ((i < 128 * 256 * 256))
				return new SymbolINT24((int) i);
			if ((i <= Integer.MAX_VALUE))
				return new SymbolINT32((int) i);
			if ((i < 128L * 256L * 256L * 256L * 256L * 256L))
				return new SymbolINT48(i);
			long l = Long.MAX_VALUE;
			if ((i <= l))
				return new SymbolINT64(i);
		} else {
			if ((i >= -16))
				return new SymbolINT4( i);
			if ((i >= Byte.MIN_VALUE))
				return new SymbolINT8( i);
			if ((i >= -128L * 16L))
				return new SymbolINT12((short) i);
			if ((i >= Short.MIN_VALUE))
				return new SymbolINT16((short) i);
			if ((i >= -128 * 256 * 256))
				return new SymbolINT24((int) i);
			if ((i >= Integer.MIN_VALUE))
				return new SymbolINT32((int) i);
			if ((i >= -128L * 256L * 256L * 256L * 256L * 256L))
				return new SymbolINT48(i);
			long l = Long.MIN_VALUE;
			if ((i >= l))
				return new SymbolINT64(i);
		}
		throw new UnsupportedOperationException();
		// return null;
	}

	/*
	 * public static List<ISymbol> CompactSymbol(List<ISymbol> ldec) { LZW lzw=new
	 * LZW(); return lzw.encodeSymbol(ldec); }
	 * 
	 * public static List<ISymbol> ExpandSymbol(List<ISymbol> ldec) { LZW lzw=new
	 * LZW(); return lzw.decodeSymbol(ldec); }
	 */
	public static int getNbSymbol() {
		int R = 256 + Symbol.special.length;

		return R;
	}

	public static List<List<ISymbol>> Split(List<ISymbol> l, ISymbol sym) {
		int toIndex = 0;
		List<List<ISymbol>> ll = new ArrayList<List<ISymbol>>();

		while (l.indexOf(sym) > 0) {
			toIndex = l.indexOf(sym);
			List<ISymbol> l2 = l.subList(0, toIndex + 1);
			ll.add(l2);
			l = l.subList(toIndex + 1, l.size());
		}
		if (!l.isEmpty())
		ll.add(l);
		return ll;
	}
	public static List<List<ISymbol>> Split(List<ISymbol> l, int sizex) {
		
		List<List<ISymbol>> ll = new ArrayList<List<ISymbol>>();

		while (l.size() >=sizex) {			
			List<ISymbol> l2 = l.subList(0,(int) sizex);
			ll.add(l2);
			l = l.subList(sizex , l.size());
		}
		if(!l.isEmpty())
		ll.add(l);
		return ll;
	}

	/**
	 * transpose the array table
	 */
	public static <T> List<List<T>> transpose(List<List<T>> table) {
	return transpose( table,null);
	}	
	public static <T> List<List<T>> normalizeDistance(List<List<T>> table,T separator) {
		List<List<T>> table2=null;
		List<Integer> lc = null;
		//max longer cell for each. store in lc(index)
		for (List<T> row : table) {
			int pos=0;
			int index=0;
			int count=0;
			for(T s:row)
			{
				if(separator.equals(s))
				{
					int pos2=index;
					int d=lc.get(count);
					int dd=pos2-pos;
					if(dd>d)
						
					count++;
					pos=pos2;
				}
				index++;
			}
		}
		return table2;
		

	}
	public static <T> List<List<T>> transpose(List<List<T>> table,T filler) {
				List<List<T>> ret = new ArrayList<List<T>>();
		int N = table.get(0).size();
		for (List<T> row : table)
			N = Math.max(row.size(), N);
		for (int i = 0; i < N; i++) {
			List<T> col = new ArrayList<T>();
			for (List<T> row : table) {
				if (i < row.size())
					col.add(row.get(i));				
				  else {
					  if (filler!=null)
					  col.add(filler);
					  }
				 
			}
			ret.add(col);

		}
		return ret;
	}

	public static List<Map<ISymbol, Long>> Freql(List<List<ISymbol>> list) {
		List<Map<ISymbol, Long>> lm = new ArrayList<Map<ISymbol, Long>>();

		for (List<ISymbol> l : transpose(list)) {
			Map<ISymbol, Long> e = Freq(l);
			lm.add(e);
		}
		return lm;
	}

	/**
	 * from a list of symbol, for symbol sym,do the histogram of distance between
	 * each symbol sym
	 */
	public static Map<Long, Long> Distance(List<ISymbol> l, ISymbol sym) {
		Map<Long, Long> m = new HashMap<Long, Long>();
		long dist = 0;
		for (ISymbol s : l) {
			if (sym.equals(s)) {
				Long v = m.get(dist);
				if (v == null)
					v = 0L;
				m.put(dist, v + 1L);
				dist = 0;
			}
			dist++;
		}
		return m;
	}

	public static long count(List<ISymbol> ls,ISymbol s)
	{
		long l=0;
		for(ISymbol e:ls)
			if (s.equals(e))
			l++;
		return l;
	}
	public static long countId(List<ISymbol> ls,ISymbol s)
	{
		long l=0;
		for(ISymbol e:ls)
			if ((e!=null) && (s.getId()==e.getId()))
			l++;
		return l;
	}
	/** from a list of symbol do the histogram of frequency
	 * without taking in account meta data of Symbol
	 * becarefull INT4(1) and INT4(2) will go in same bin.* */
	public static Map<ISymbol, Long> FreqId(List<ISymbol> l)
	{
		return l.stream()
				.parallel()
				.map(x->x.getId())
				.map(x->Symbol.findId((int)x.intValue()))
				.collect(
			    Collectors.groupingBy(
					      Function.identity(),
					      Collectors.counting()
					    ));
	}
	

	/** split a list of symbol into several list : 
	 * Empty : standard Symbol
	 * for all cati in category
	 * cati : Symbol just next to cati
	 * the typical use is to split standard symbol against number after a compress algo.
	 * 
	 * */
public static	Map<ISymbol,List<ISymbol>> split(List<ISymbol> source , List<ISymbol> category)
	{
		Map<ISymbol,List<ISymbol>> m=new HashMap<ISymbol,List<ISymbol>>();
		int i=0;
		List<ISymbol> std=new ArrayList();
		m.put(Symbol.Empty, std);
		for(ISymbol s:category)
			m.put(s, new ArrayList());
		while(i<source.size())
		{
			ISymbol s=source.get(i++);
		if(s.getId()>255)
		{
			List<ISymbol> lc=m.get(s);
			if(lc==null )
				std.add(s);
			else
				{lc.add(source.get(i++));std.add(s);}
		}
		else
			std.add(s);
		
		}
		return m;
		
	}
	/**
	 * from a list of symbol do the histogram of frequency
	 */
	public static Map<ISymbol, Long> Freq(List<ISymbol> l) {
		if (l==null) return null;
		return l.stream().parallel().map(x -> x.getId()).map(x -> Symbol.findId((int) x.intValue()))
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}

	
	public static String PrintFreq(List<ISymbol> l) {
		Map<ISymbol, Long> m = Freq(l);
		Map<ISymbol, Long> ms = JavaUtils.SortMapByValue(m);
		StringBuffer s = new StringBuffer();
		s.append("Symbol : " + ms.keySet().size() + "/" + l.size() + "\r\n");
		double S = 0;
		for (ISymbol e : ms.keySet()) {
			s.append(e.toString() + "\t" + ms.get(e) + "\r\n");
			double Pi = ms.get(e) / (double) l.size();
			S += -Pi * Math.log(Pi) / Math.log(2);
		}
		s.append("Entropy by symbol: " + S + " bits\r\n");
		s.append("huffman ration : " + (S / 8 * 100) + " %\r\n");

		return s.toString();
	}

	public static Long getLong(ISymbol n1) {

		if (SymbolINT4.class.isInstance(n1)) {
			SymbolINT4 s = (SymbolINT4) n1;
			return s.getS2().getCode().getLong();
		} else if (SymbolINT8.class.isInstance(n1)) {
			SymbolINT8 s = (SymbolINT8) n1;
			return s.getS2().getCode().getLong();
		} else if (SymbolINT12.class.isInstance(n1)) {
			SymbolINT12 s = (SymbolINT12) n1;
			return s.getS2().getCode().getLong();
		} else if (SymbolINT16.class.isInstance(n1)) {
			SymbolINT16 s = (SymbolINT16) n1;
			return s.getS2().getCode().getLong();
		} else if (SymbolINT24.class.isInstance(n1)) {
			SymbolINT24 s = (SymbolINT24) n1;
			return s.getS2().getCode().getLong();
		} else if (SymbolINT32.class.isInstance(n1)) {
			SymbolINT32 s = (SymbolINT32) n1;
			return s.getS2().getCode().getLong();
		} else if (SymbolINT48.class.isInstance(n1)) {
			SymbolINT48 s = (SymbolINT48) n1;
			return s.getS2().getCode().getLong();
		} else if (SymbolINT64.class.isInstance(n1)) {
			SymbolINT64 s = (SymbolINT64) n1;
			return s.getS2().getCode().getLong();
		} else
			return null;
	}

	@Override
	public int compareTo(ISymbol o) {
		if (o == null)
			return -1;
		return (int) (getId() - o.getId());
	}

	/** apply the coding set cs as default one */
	public static void apply(ICodingRule cs) {
		for (int i = 0; i < Symbol.getNbSymbol(); i++) {
			ISymbol s = Symbol.findId(i);
			ICode c = cs.get(s);
			s.setCode(c);
		}
	}

	public static List<ISymbol> getAll() {
		List<ISymbol> l = new ArrayList<ISymbol>();
		for (int i = 0; i < getNbSymbol(); i++)
			l.add(findId(i));
		return l;

	}

	public static Long length(Map<ISymbol, Long> freqSym, ICodingRule cs) {
		long size = 0;
		for (ISymbol s : freqSym.keySet()) {
			ICode c = cs.get(s);
			if (c == null)
				return null;// coding impossible
			int codelen = c.length();
			long freq = freqSym.get(s);
			size += codelen * freq;
		}
		return size;
	}

	@Override
	public ISymbol Factory(Long nId) {
		if (nId==null)
		return null;
		return findId(nId.intValue());
	}

	public static void add(Map<ISymbol, Long> fq, Symbol sym) {
		Long v=fq.get(sym);
		if (v==null)
			v=0L;
		v++;
		fq.put(sym, v);
	}

	public static void replaceAll(List<ISymbol> le, Symbol find, ISymbol newone) {
		for(int i=0;i<le.size();i++)
			{
			if(find.equals(le.get(i)))
				
			{	le.remove(i);
			if (newone!=null) le.add(i,newone);}
			}
		
	}


}
