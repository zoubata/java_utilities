package com.zoubworld.java.utils.compress;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.zoubworld.java.utils.compress.SymbolComplex.*;
import com.zoubworld.java.utils.compress.algo.LZW;
import com.zoubworld.java.utils.compress.file.BinaryStdIn;
import com.zoubworld.utils.JavaUtilList;
import com.zoubworld.utils.JavaUtils;
/** symbol class that define 0..255 symbol foreach byte value, and associate a coding that are defaultly the same value.
 * symbol after 255 are special, it is a concept with a coding rule e.i.: it presents something with a way to code it. this is tipicaly to manage algo and internal state on compressed file 
 * i.e. INT4(5) represent the number 5, its coding is the coding of symbol INT4 plus 4 bit representing 5, so 0b1001 
 * EOF represent the end of a file, its coding it 
 * Composed symbol are an association of symbol to describe something more complex. 
 * example RLE+INT4(5)+symbol represent the repetition of 'symbol' 5 times, its coding is the concatenation of code(RLE), Code(INT4(5)), code(Symbol)
 * */
public class Symbol implements ISymbol {
/* symbol 0..255 : code 0..255 */

	public static Symbol INT4 =new Symbol(0x100,new Code(256));// 8 bit number coding : INT8+0Bxxxxxxxx
	public static Symbol INT8 =new Symbol(0x101,new Code(257));// 8 bit number coding : INT8+0Bxxxxxxxx
	public static Symbol INT12=new Symbol(0x102,new Code(258));// 16 bit number coding : INT8+0Bxxxxxxxxxxxxxxxx
	public static Symbol INT16=new Symbol(0x103,new Code(259));// 16 bit number coding : INT8+0Bxxxxxxxxxxxxxxxx
	public static Symbol INT24=new Symbol(0x104,new Code(260));// 24 bit number coding : INT8+0Xxxxxxx
	public static Symbol INT32=new Symbol(0x105,new Code(261));// 32 bit number coding : INT8+0Xxxxxxxxx
	public static Symbol INT48=new Symbol(0x106,new Code(262));// 32 bit number coding : INT8+0Xxxxxxxxx
	public static Symbol INT64=new Symbol(0x107,new Code(263));// 32 bit number coding : INT8+0Xxxxxxxxx
	
	// dictionary/words algo and utility symbol
	public static Symbol RLE=new Symbol(0x108,new Code(264));// RLE compression symbol; use : symbol+RLE+N
	public static Symbol RPE=new Symbol(0x109,new Code(265));// 
	public static Symbol LZW=new Symbol(0x10A,new Code(266));// Ziv and Lempel and Welch compression method : LZW+ index inside the dico
	public static Symbol PIE=new Symbol(0x10B,new Code(267));// Past Index encoding : PIE+index+Size
	public static Symbol HUFFMAN=new Symbol(0x10C,new Code(268));// New Huffman table :HUF+ INT32+N+tab[n] ; tab[i]=0xlleeeeee // size: ~1.1ko
	public static Symbol EOF=new Symbol(0x10D,new Code(269));// End of File
	public static Symbol HOF=new Symbol(0x10E ,new Code(270));// Header of File
	public static Symbol EOS=new Symbol(0x10F,new Code(271));// End of String
	public static Symbol EOBS=new Symbol(0x110,new Code(272));// End of Bit Stream
	public static Symbol PAT=new Symbol(0x111,new Code(273));// pattern : PAT+INT(n)+n*symbol[0..255+WILDCARD] 
	/*example : "BPL858,1,42,20,1,2,59B08_FA136_MAG_000_PB,pass_bin,1,1,,59B08,,BPL858,,,,,2_1,,2016-11-23T17:42:10"
	 * declare a PAT0 : PAT+INT(187)+"BPL858,*,*,*,*,*,59B08_FA136_MAG_000_PB,*,*,*,*,59B08,*,BPL858,*,*,*,*,*,*,2016-11-23*"
	 * * is symbol Wildcard or IntAsASCII+Wildcard or IntAsASCII+Wildcard, 
	 * encode example : PATr(0)+"1"+"42"+"20"+"1"+2"+"pass_bin"+"1"+"1"+""+""+""+""+""+""+"2_1"+""+"T17:42:10"
	 * "" is symbol Empty
	 * "42" will be replace by a INT6(42)
	 * "1" will be replace by a INT4(1)
	 * "pass_bin" will be SOS+"pass_bin"+EOS or SOl(8)+"pass_bin" or RPT(12,32) 
	 */
	public static Symbol PATr=new Symbol(0x112,new Code(274));// pattern repeated: PATr+INT(x)+few symbol=wildcard
	public static Symbol Wildcard=new Symbol(0x113,new Code(275));// wildcard
	public static Symbol Empty=new Symbol(0x114,new Code(276));// this is an empty symbol meaning that code has a size of 0.
	
	// specialized symbol to represent a list of octet.
	public static Symbol IntAsASCII=new Symbol(0x115,new Code(277));//it represents an String of a integer, composed symbol :INTASASCII,INTxx(yy) replace String.format("%i",yy).
	public static Symbol TBD=new Symbol(0x116,new Code(278));//it represents an String of a float, composed symbol :FLOATASASCII,x[4bit],y[4bit],float[32] replace String.format("%x.yf",z).
	public static Symbol FloatAsASCII=new Symbol(0x117,new Code(279));//it represents an String of a float composed symbol :FLOATASASCII,INTn(f) replace String.format("%1.f",float(f)).
	public static Symbol FloatAsASCIIes2=new Symbol(0x118,new Code(280));//it represents an String of a float in scientific notation, composed symbol :FLOAT1ASASCIIes2,INTn(f) replace String.format("%.e",float(f)).
	public static Symbol DoubleAsASCIIes3=new Symbol(0x119,new Code(281));//it represents an String of a double in scientific notation, composed symbol :FLOAT1ASASCIIes3,INTn(f) replace String.format("%.g",float(f)).
	public static Symbol CRLF=new Symbol(0x11A,new Code(282));//CRLF symbol to replace CR+LF(\0x13\0x10)
	public static Symbol SOS=new Symbol(0x11B,new Code(283));//Start of String.
	public static Symbol SOln=new Symbol(0x11C,new Code(284));//String of length n. SOl+INTxx(n) +...symbols....
	public static Symbol Qn_mAsASCII=new Symbol(0x11D,new Code(285));//it represents an String of a decimal number with fixed point composed symbol :Qn_mAsASCII,INTn.INTm replace String.format("%d.%d",(signed)n,(unsigned)m). (https://en.wikipedia.org/wiki/Fixed-point_arithmetic)
	public static Symbol INTn=new Symbol(0x11E,new Code(286));// 96 bit number coding : INTnX[n=6 bit ]+X=0Xxxxxxxxx , (n+33) is the number of bit after to describe X.
	public static Symbol SAliasn=new Symbol(0x11F,new Code(287));//String alias n° SOl+INTxx(n), it replace a nth declaration of Soln/SOS...EOS
	
	//https://en.wikipedia.org/wiki/Single-precision_floating-point_format
	//INTntoFLOAT convertion : INT12=abcdefghijkl..    : float : seeeeeeeedd....dd( 8e 23d)
	/* a->s
	 * bcdefghi->eeeeeeee
	 * jkl..->dd..dd
	 * */
	
	//https://en.wikipedia.org/wiki/Double-precision_floating-point_format
	//INTntoDOUBLE convertion : INT24=abcdefghijkl mnopqrst..        : double:seeeeeeeeeeeddddddddd....dd(11e 52d)
	/* a->s
	 * bcdefghijkl->eeeeeeeeeee
	 * mnopqrst..->dddd...
	 * */
	// 1.03125=>FloatAsASCII+INT28(0 00000001 001 1001 0010 1101 0101)
	// 7*8b=56b => 2s+28b=~40
	public static Symbol special[]= {INT4,INT8,INT12,INT16,INT24,INT32,INT48,INT64, //should be ordered
									 RLE ,RPE  ,LZW  ,PIE  ,HUFFMAN,EOF,
									 HOF,EOS,EOBS,PAT,PATr,
									 Wildcard,Empty,
									 IntAsASCII,TBD,
									 FloatAsASCII,FloatAsASCIIes2,DoubleAsASCIIes3,
									 CRLF,SOS,SOln,Qn_mAsASCII,
									 INTn,SAliasn};
	// EOD, SOD/SOL EOS EOL NIL EndOfData StartOfData / StartOfList,NextInList,EndOfList,EndOfString
	//Multi file : SOL ... NIL ... NIL ... ... EOL, SOD ...l<sym>...EOF....EOF....EOD
	//                  ...=file.path/sizeU64/date+time
/*
 	static Symbol EOF=new Symbol("EOF",new Code(257));// End Of file symbol; use [data symbol]+EOF+[data symbol]+EOF
 
	static Symbol FL=new Symbol("FL",new Code(258));// File List symbol; use FL path; FL; path;....
*/
	public static void initCode()
	{
		int R = 256 + Symbol.special.length;
		int Nb = (int) (Math.log(R) / Math.log(2) + 1);
		
    	for(int i=0;i<Symbol.tabId.length;i++)
    			{ISymbol s=Symbol.findId(i);
    		if(s.getId()<256)
    		s.setCode(new Code((char)s.getId(),Nb));
    		else
    			s.setCode(new Code((int)s.getId(),Nb));
    		}  	
	}
	public Symbol() {
		// TODO Auto-generated constructor stub
	}
	ICode code=null;
public Symbol(char charAt) {
	symbol= new byte[1];
	symbol[0]=(byte)charAt;
	}
private Symbol(String s, ICode cc) {
	symbol= new byte[s.length()];
	for(int i=0;i<s.length();i++)
		symbol[i]=(byte)s.charAt(i);
	code=cc;
	}
private Symbol(int s, ICode cc) {
	symbol= new byte[4];	
	for(int i=0;i<4;i++)
		symbol[i]=(byte) ((s>>((3-i)*8))&0xff);
	code=cc;
	}
public Symbol(long s, ICode cc) {
	symbol= new byte[8];	
	for(int i=0;i<8;i++)
		symbol[i]=(byte) ((s>>((7-i)*8))&0xff);
	code=cc;
	}

public Symbol(String s) {
	symbol= new byte[s.length()];
	for(int i=0;i<s.length();i++)
		symbol[i]=(byte)s.charAt(i);
	}
/* (non-Javadoc)
 * @see net.zoubwolrd.java.utils.compress.ISymbol#isChar()
 */
@Override
public boolean isChar()
{
	return symbol.length==1;
}
/* (non-Javadoc)
 * @see net.zoubwolrd.java.utils.compress.ISymbol#isInt()
 */
@Override
public boolean isInt()
{
	return symbol.length==4;
}
public boolean isLong()
{
	return symbol.length==8;
}
/* (non-Javadoc)
 * @see net.zoubwolrd.java.utils.compress.ISymbol#isShort()
 */
@Override
public boolean isShort()
{
	return symbol.length==2;
}
/* (non-Javadoc)
 * @see net.zoubwolrd.java.utils.compress.ISymbol#getChar()
 */
@Override
public char getChar()
{	int a=((int)(symbol[0] & 0xff));
	return (char)a;
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
/** a string is a list of char ending by \0, from ls, a list of symbol ending by EOS but other Symbol can exist after EOS, it will be ignore.*/
public static String listSymbolToString(List<ISymbol> ls)
{
	 String s="";
	 int index=0;
	 while(index<ls.size() && ls.get( index )!=Symbol.EOS)
	 {
		 s+=(ls.get( index++ ).getChar());
	 }
	 
	return s;
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

/** it build a list of symbol between 0..255
 * 
 * */
public static List<ISymbol> factoryFile(String inputFile)
{ return factoryFile( inputFile, 8);

}
/* not yet implemented for sizecode!=8
 * */
private static List<ISymbol> factoryFile(String inputFile, int sizecode)
{
	 List<ISymbol> ls= new ArrayList<ISymbol>();
	 try {
			InputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
		    byte[] buffer = new byte[1024*1024];
		 int size=0;
		 ISymbol RLE8=Symbol.RLE;
		 int count=0;
		 ISymbol sprevious=null;
		    while ((size=inputStream.read(buffer)) != -1) {
		    	ls.addAll(Symbol.ByteArrayToListSymbol(buffer, size));
		    
		 //   outputStream.write(buffer,0,size);
		    }
		    inputStream.close();
	 }
		  catch (IOException ex) {
		        ex.printStackTrace();
		}	 
	return ls;
			}

public static List<ISymbol> factoryCharSeq(String text)
{
	 List<ISymbol> ls= new ArrayList<ISymbol>();
	 if (text.length()==0)
		 ls.add(Symbol.Empty);
	 else		 
	 for(char c: text.toCharArray())
	 {
		 ls.add(Symbol.findId((byte) c));
	 }
	 
	return ls;
			}

/* (non-Javadoc)
 * @see net.zoubwolrd.java.utils.compress.ISymbol#getInt()
 */
@Override
public Integer getInt() {

	if (symbol.length==4)
	{
		int i= (symbol[0] & 0xff);
		i= (i<<8) +(symbol[1] & 0xff);
		i= (i<<8) +(symbol[2] & 0xff);
		i= (i<<8) +(symbol[3] & 0xff);
		return i;
	}
	return null;
}
public Long getLong() {

	if (symbol.length==8)
	{
		long i= (symbol[0] & 0xff);
		i= (i<<8) +(symbol[1] & 0xff);
		i= (i<<8) +(symbol[2] & 0xff);
		i= (i<<8) +(symbol[3] & 0xff);
		i= (i<<8) +(symbol[4] & 0xff);
		i= (i<<8) +(symbol[5] & 0xff);
		i= (i<<8) +(symbol[6] & 0xff);
		i= (i<<8) +(symbol[7] & 0xff);		
		return i;
	}
	return null;
}
/* (non-Javadoc)
 * @see net.zoubwolrd.java.utils.compress.ISymbol#getShort()
 */
@Override
public short getShort() {

	if (symbol.length==2)
	{
		int i= (symbol[0] & 0xff);
		i= (i<<8); 
		i=i+(((int)symbol[1]) & 0xff);
	
		return (short) i;
	}
	return 0;
}

/* (non-Javadoc)
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

public static List<ICode> toCode(List<ISymbol> ls)
{
	List<ICode> lc= new ArrayList<ICode>();
	for(ISymbol c:ls)
		lc.add(c.getCode());
	return lc;
}
public Symbol(byte b) {
	symbol= new byte[1];
	symbol[0]=(byte)b;
}
public Symbol(int count) {
	symbol= new byte[4];
	symbol[0]=(byte)((count>>24)& 0xff);
	symbol[1]=(byte)((count>>16)& 0xff);
	symbol[2]=(byte)((count>>8)& 0xff);
	symbol[3]=(byte)((count>>0)& 0xff);	
}
public Symbol(short count) {
	symbol= new byte[2];	
	symbol[0]=(byte)((count>>8)& 0xff);
	symbol[1]=(byte)((count>>0)& 0xff);	
}
public Symbol(long s) {
	symbol= new byte[8];	
	for(int i=0;i<8;i++)
		symbol[i]=(byte) ((s>>((7-i)*8))&0xff);
}


private byte symbol[]=null;
	
	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#toSymbol()
	 */
	@Override
	public char[] toSymbol()
	{
		char[] chart =new char[symbol.length];
		for(int i=0;i<chart.length;i++)
		chart[i]=(char)symbol[i];
		return chart;
	}
	
	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#toString()
	 */
	@Override
	public String toString()
	{
		if ((symbol.length==1) )
		return ""+(char)symbol[0];
	
		if ((symbol.length==1) && (symbol[0]<127) && (symbol[0]>31))
			return "char('"+(char)symbol[0]+"')";
		switch((int)getId())
		{
		case 0x100 : return "INT4";
		case 0x101 : return "INT8";
		case 0x102 : return "INT12";
		case 0x103 : return "INT16";
		case 0x104 : return "INT24";
		case 0x105 : return "INT32";
		case 0x106 : return "INT48";
		case 0x107 : return "INT64";
		case 0x108 : return "RLE";
		case 0x109 : return "RPE";
		case 0x10A : return "LZW";
		case 0x10B : return "PIE";
		case 0x10C : return "HUF";
		case 0x10D : return "EOF";
		case 0x10E : return "HOF";
		case 0x10F : return "EOS";
		case 0x110 : return "EOBS";// End of Bit Stream
		case 0x111 : return "PAT";
		case 0x112 : return "PATr";
		case 0x113 : return "Wildcard";
		case 0x114 : return "Empty";
		case 0x115 : return "IntAsASCII";
		case 0x117 : return "FloatAsASCII";
		case 0x118 : return "FloatAsASCIIes2";
		case 0x119 : return "DoubleAsASCIIes3";
		case 0x11A : return "CRLF";
		case 0x11B : return "SOS";
		case 0x11C : return "SOln";
		case 0x11D : return "Qn_mAsASCII";
		case 0x11E : return "INTn";
		case 0x11F : return "SAliasn";
		
		
		
		
		
		}		
		String s="Symbol(0x";
		
		for(int i=0;i<symbol.length;i++)
			s+=String.format("%2x", symbol[i]);
		s+=")";
		return s;
	}
	
	public static List<ISymbol> ByteArrayToListSymbol(byte[] datas, int size)
	{
		List<ISymbol> l=new ArrayList<ISymbol>(size);
		for(int i=0;i<size;i++)
		{
			byte c= datas[i];
			l.add(Symbol.findId(c));
		}
		return l;
		}
// symbol list
	static ISymbol tabId[]=new Symbol[256+special.length];
	public static ISymbol findId(int c) {
		if (c<0)
			c=256+c+0;
		if (c>=tabId.length)
			return null;
		
		if (tabId[c]==null)
		{
			if (c<256) 
			{
				tabId[c]=new Symbol((char)c);
				tabId[c].setCode(new Code((char)c));
				tabId[c].getCode().setSymbol(tabId[c]);
			}
			else
			tabId[c]=special[c-256];
			
			
		}
		return tabId[c];
	}
	public static Symbol getFromSet(Set<Symbol> keySet, char charAt) {
		for(Symbol s:keySet)
			if ((s.symbol[0]==charAt) && (s.symbol.length==1))
				return s;
		return null;
	}

	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#getCode()
	 */
	@Override
	public ICode getCode() {
		// TODO Auto-generated method stub
		return code;
	}
	
	static public ICode toCode(ISymbol s) {
		if (s!=null)
		return s.getCode() ;
		return null;
	}
	/** convert a list of symbol into a list of code
	 * */
	static public List<ICode> toCodes(List<ISymbol> ls) {
		if (ls!=null)
		return ls.stream().map(s->toCode(s)).collect(Collectors.toList()) ;
		return null;
	}
	
	/** convert a list of symbol into a list of code
	 * */
	static public List<ICode> toCodes(List<ISymbol> ls,ICodingRule cs) {
		if (ls!=null)
		return ls.stream().map(s->cs.get(s)).collect(Collectors.toList()) ;
		return null;
	}
	public static Long length(List<ISymbol> ls, ICodingRule cs) {
		return Code.length(Symbol.toCodes(ls,cs));
	}
	static public Long length(List<ISymbol> ls)
	{ 
		return Code.length(Symbol.toCode(ls));
	}

	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#setCode(net.zoubwolrd.java.utils.compress.Code)
	 */
	@Override
	public void setCode(ICode code2) {
		code=code2;
		
	}
	/** complex symbol embed a data, this function read the data associated to a complex symbol.
	 * */
	public static ISymbol decode(ISymbol SimpleSym, BinaryStdIn binaryStdIn) {
		switch((int)SimpleSym.getId()) 
		{
		case 256://INT8.getId() :
			 return new SymbolINT4(binaryStdIn);
		case 257://INT16.getId() :
			 return new SymbolINT8(binaryStdIn);
		case 258://INT8.getId() :
			 return new SymbolINT12(binaryStdIn);
		case 259://INT16.getId() :
			 return new SymbolINT16(binaryStdIn);
		case 260://INT24.getId() :
			 return new SymbolINT24(binaryStdIn);
		case 261://INT32.getId() :
			 return new SymbolINT32(binaryStdIn);
		case 262://INT48.getId() :
			 return new SymbolINT48(binaryStdIn);
		case 263://INT64.getId() :
			 return new SymbolINT64(binaryStdIn);
		case 268://HUFFMAN.getId() :
			 return new SymbolHuffman(binaryStdIn);
	/*	NEWHUFF:table(n+1)=
		USEHUFFTABLE(n)
	 */  default : // Optional
	      return SimpleSym;
	}
	}
	/** return the list of symbol representing f
	 * */
	public static List<ISymbol>   FactorySymbolFloatAsASCII(Float f)
	{
		 List<ISymbol>  ls2 = new ArrayList<>();		
			ls2.add(Symbol.FloatAsASCII);
			ls2.add(Symbol.FactorySymbolINT(Float.floatToIntBits(f)));			
		return ls2;
	}
	/** return the list of symbol representing d
	 * */
	public static List<ISymbol>   FactorySymbolDoubleAsASCIIes3(Double d)
	{
		 List<ISymbol>  ls2 = new ArrayList<>();		
			ls2.add(Symbol.FloatAsASCII);
			ls2.add(Symbol.FactorySymbolINT(Double.doubleToLongBits(d)));			
		return ls2;
	}
	/** give the int/long value of a INT../INTn symbol
	 * */
	public static Long getINTn(ISymbol s)
	{
		CompositeSymbol cs=null;
		if (s.getClass().isAssignableFrom(CompositeSymbol.class))
			 cs=(CompositeSymbol)cs;
		switch((int)cs.getId())
		{
		case 0x100 : return cs.getS2().getId();//INT4
		case 0x101 : return cs.getS2().getId();//"INT8";
		case 0x102 : return cs.getS2().getId();//"INT12";
		case 0x103 : return cs.getS2().getId();//"INT16";
		case 0x104 : return cs.getS2().getId();//"INT24";
		case 0x105 : return cs.getS2().getId();//"INT32";
		case 0x106 : return cs.getS2().getId();//"INT48";
		case 0x107 : return cs.getS2().getId();//"INT64";
	/*	case 0x108 : return "RLE";
		case 0x109 : return "RPE";
		case 0x10A : return "LZW";
		case 0x10B : return "PIE";
		case 0x10C : return "HUF";
		case 0x10D : return "EOF";
		case 0x10E : return "HOF";
		case 0x10F : return "EOS";
		case 0x110 : return "EOBS";// End of Bit Stream
		case 0x111 : return "PAT";
		case 0x112 : return "PATr";
		case 0x113 : return "Wildcard";
		case 0x114 : return "Empty";
		case 0x115 : return "IntAsASCII";
		case 0x117 : return "FloatAsASCII";
		case 0x118 : return "FloatAsASCIIes2";
		case 0x119 : return "DoubleAsASCIIes3";
		case 0x11A : return "CRLF";
		case 0x11B : return "SOS";
		case 0x11C : return "SOln";
		case 0x11D : return "Qn_mAsASCII";*/
		case 0x11E : return cs.getS2().getId();//"INTn";
	//	case 0x11F : return "SAliasn";
		}	
		return null;
			
	}
	/** give the double value of a INT../INTn symbol preceded by DoubleAsAscii* Symbol
	 * */
	public static Double getDoubleAsASCII(ISymbol s)
	{
		Long l=getINTn( s);
		if (l==null)
			return null;
		return Double.longBitsToDouble(l);
	}
	/** give the float/double value of a INT../INTn symbol preceded by FloatAsAscii* Symbol
	 * */
	public static Float getFloatAsASCII(ISymbol s)
	{
		Long l=getINTn( s);
		if (l==null)
			return null;
		return Float.intBitsToFloat(l.intValue());
	}
	
	/** return the list of symbol representing l
	 * */
	public static List<ISymbol>   FactorySymbolIntAsASCII(Long l)
	{
		 List<ISymbol>  ls2 = new ArrayList<>();
			ls2.add(Symbol.IntAsASCII);
			ls2.add(Symbol.FactorySymbolINT(l));
		return ls2;
	}
	/** return an signed int i */
	public static ISymbol FactorySymbolINT(long  i)
	{
		if((i>=0))
		{
		if ((i<16))
			return new SymbolINT4((byte)i);
		if ((i<=Byte.MAX_VALUE))
			return new SymbolINT8((byte)i);
		if ((i<128L*16L))
			return new SymbolINT12((short)i);
		if ((i<=Short.MAX_VALUE))
			return new SymbolINT16((short)i);
		if ((i<128*256*256))
			return new SymbolINT24((int)i);
		if ((i<=Integer.MAX_VALUE))
			return new SymbolINT32((int)i);
		if ((i<128L*256L*256L*256L*256L*256L))
			return new SymbolINT48(i);
		long l=Long.MAX_VALUE;
		if ((i<=l))
			return new SymbolINT64(i);
		}
		else
		{
			if ((i>=-16))
				return new SymbolINT4((byte)i);
			if ((i>=Byte.MIN_VALUE))
				return new SymbolINT8((byte)i);
			if ((i>=-128L*16L))
				return new SymbolINT12((short)i);
			if ((i>=Short.MIN_VALUE))
				return new SymbolINT16((short)i);
			if ((i>=-128*256*256))
				return new SymbolINT24((int)i);
			if ((i>=Integer.MIN_VALUE))
				return new SymbolINT32((int)i);
			if ((i>=-128L*256L*256L*256L*256L*256L))
				return new SymbolINT48(i);
			long l=Long.MIN_VALUE;
			if ((i>=l))
				return new SymbolINT64(i);
		}
		  throw new UnsupportedOperationException();
		//return null;
	}
	public static List<ISymbol> CompactSymbol(List<ISymbol> ldec) {
		LZW lzw=new LZW();		
		return lzw.encodeSymbol(ldec);
	}
	
	public static List<ISymbol> ExpandSymbol(List<ISymbol> ldec) {
		LZW lzw=new LZW();		
		return lzw.decodeSymbol(ldec);
	}
	public static int getNbSymbol() {
		int R = 256 + Symbol.special.length;
		
		return R;
	}
	/** from a list of symbol do the histogram of frequency
	 * */
	public static Map<ISymbol, Long> Freq(List<ISymbol> l)
	{
		return l.stream()
				.map(x->x.getId())
				.map(x->Symbol.findId((int)x.intValue()))
				.collect(
			    Collectors.groupingBy(
					      Function.identity(),
					      Collectors.counting()
					    ));
	}
	public static String PrintFreq(List<ISymbol> l)
	{
		Map<ISymbol, Long> m=Freq( l);
		Map<ISymbol, Long> ms=JavaUtils.SortMapByValue(m);
		StringBuffer s=new StringBuffer();
		s.append("Symbol : "+ms.keySet().size()+"/"+l.size()+"\r\n");
		double S=0;
		for(ISymbol e:ms.keySet())
		{	s.append(e.toString()+"\t"+ms.get(e)+"\r\n");
		double Pi=ms.get(e)/(double)l.size();
		S+=-Pi*Math.log(Pi)/Math.log(2);
		}
		s.append("Entropy by symbol: "+S+" bits\r\n");
		s.append("huffman ration : "+(S/8*100)+" %\r\n");
		
		return s.toString();
	}
	public static Long getLong(ISymbol n1) {
	
		if(SymbolINT4.class.isInstance(n1))
		{
			SymbolINT4 s=(SymbolINT4)	n1;
					return s.getS2().getCode().getLong();
		}
		else if(SymbolINT8.class.isInstance(n1))
		{
			SymbolINT8 s=(SymbolINT8)	n1;
					return s.getS2().getCode().getLong();
		}
		else if(SymbolINT12.class.isInstance(n1))
		{
			SymbolINT12 s=(SymbolINT12)	n1;
					return s.getS2().getCode().getLong();
		}
		else if(SymbolINT16.class.isInstance(n1))
		{
			SymbolINT16 s=(SymbolINT16)	n1;
					return s.getS2().getCode().getLong();
		}
		else if(SymbolINT24.class.isInstance(n1))
		{
			SymbolINT24 s=(SymbolINT24)	n1;
					return s.getS2().getCode().getLong();
		}
		else if(SymbolINT32.class.isInstance(n1))
		{
			SymbolINT32 s=(SymbolINT32)	n1;
					return s.getS2().getCode().getLong();
		}
		else if(SymbolINT48.class.isInstance(n1))
		{
			SymbolINT48 s=(SymbolINT48)	n1;
					return s.getS2().getCode().getLong();
		}
		else if(SymbolINT64.class.isInstance(n1))
		{
			SymbolINT64 s=(SymbolINT64)	n1;
					return s.getS2().getCode().getLong();
		}
		else
		return null;
	}
	@Override
	public int compareTo(ISymbol o) {
		if (o==null)
			return -1;
		return (int)(getId()-o.getId());
	}

	

}
