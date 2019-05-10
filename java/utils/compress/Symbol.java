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
import java.util.Set;

import com.zoubworld.java.utils.compress.SymbolComplex.*;
import com.zoubworld.java.utils.compress.file.BinaryStdIn;
/** symbol class that define 0..255 symbol foreach byte value, and associate a coding that are defaultly the same value.
 * 
 * */
public class Symbol implements ISymbol {
/* symbol 0..255 : code 0..255 */

	public static Symbol INT4 =new Symbol(256,new Code(256));// 8 bit number coding : INT8+0Bxxxxxxxx
	public static Symbol INT8 =new Symbol(257,new Code(257));// 8 bit number coding : INT8+0Bxxxxxxxx
	public static Symbol INT12=new Symbol(258,new Code(258));// 16 bit number coding : INT8+0Bxxxxxxxxxxxxxxxx
	public static Symbol INT16=new Symbol(259,new Code(259));// 16 bit number coding : INT8+0Bxxxxxxxxxxxxxxxx
	public static Symbol INT24=new Symbol(260,new Code(260));// 24 bit number coding : INT8+0Xxxxxxx
	public static Symbol INT32=new Symbol(261,new Code(261));// 32 bit number coding : INT8+0Xxxxxxxxx
	public static Symbol INT48=new Symbol(262,new Code(262));// 32 bit number coding : INT8+0Xxxxxxxxx
	public static Symbol INT64=new Symbol(263,new Code(263));// 32 bit number coding : INT8+0Xxxxxxxxx
	
	
	public static Symbol RLE=new Symbol(264,new Code(264));// RLE compression symbol; use : symbol+RLE+N
	public static Symbol RPE=new Symbol(265,new Code(265));// 
	public static Symbol LZW=new Symbol(266,new Code(266));// Ziv and Lempel and Welch compression method : LZW+ index inside the dico
	public static Symbol PIE=new Symbol(267,new Code(267));// Past Index encoding : POE+index+Size
	public static Symbol HUFFMAN=new Symbol(268,new Code(268));// New Huffman table :HUF+ INT32+N+tab[n] ; tab[i]=0xlleeeeee // size: ~1.1ko
	public static Symbol EOF=new Symbol(269,new Code(269));// End of File
	public static Symbol HOF=new Symbol(270 ,new Code(270));// Header of File
	public static Symbol EOS=new Symbol(271,new Code(271));// End of String
	public static Symbol EOBS=new Symbol(272,new Code(272));// End of Bit Stream
	public static Symbol special[]= {INT4,INT8,INT12,INT16,INT24,INT32,INT48,INT64, //should be ordered
									 RLE ,RPE  ,LZW  ,PIE  ,HUFFMAN,EOF,
									 HOF,EOS,EOBS};
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
{	
	return ((char)(symbol[0] & 0xff));
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
	 }
		  catch (IOException ex) {
		        ex.printStackTrace();
		}
	 
	
	

	 
	return ls;
			}

public static List<ISymbol> factoryCharSeq(String text)
{
	 List<ISymbol> ls= new ArrayList<ISymbol>();
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
	public static ISymbol FactorySymbolINT(long  i)
	{
		if ((i>=0)&&(i<16))
			return new SymbolINT4((byte)i);
		if ((i>=0)&&(i<=Byte.MAX_VALUE))
			return new SymbolINT8((byte)i);
		if ((i>=0)&&(i<256L*16L))
			return new SymbolINT12((short)i);
		if ((i>=0)&&(i<=Short.MAX_VALUE))
			return new SymbolINT16((short)i);
		if ((i>=0)&&(i<256*256*256))
			return new SymbolINT24((int)i);
		if ((i>=0)&&(i<=Integer.MAX_VALUE))
			return new SymbolINT32((int)i);
		if ((i>=0)&&(i<256L*256L*256L*256L*256L*256L))
			return new SymbolINT48(i);
		long l=Long.MAX_VALUE;
		if ((i>=0)&&(i<=l))
			return new SymbolINT64(i);
		
		return null;
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
	

}
