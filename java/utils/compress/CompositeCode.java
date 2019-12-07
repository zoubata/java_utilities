/**
 * 
 */
package com.zoubworld.java.utils.compress;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.fop.util.GenerationHelperContentHandler;

import com.zoubworld.java.utils.compress.file.BinaryStdIn;
import com.zoubworld.java.utils.compress.file.BinaryStdOut;

/**
 * @author 
 *
 *this class contains 2 code from 2 symbol inside a composite symbol. example INT16(0x1234) code(INT16)+code(0x1234)
 * 2 H
 */
public class CompositeCode implements ICode {

	CompositeSymbol sc;
	ICode c1;
	ICode c2;

	public CompositeCode(CompositeSymbol s) {
		sc=s;
	}
	
	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.ICode#length()
	 */
	@Override
	public int length() {
		// TODO Auto-generated method stub
	//	return sc.getS1().getCode().length()+sc.getS2().getCode().length();
		return getC1().length()+getC2().length();
	}

	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.ICode#getSymbol()
	 */
	@Override
	public ISymbol getSymbol() {
		if(sc==null)
			sc=new CompositeSymbol(c1.getSymbol(), c2.getSymbol());
		return sc;
	}

	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.ICode#setSymbol(net.zoubwolrd.java.utils.compress.ISymbol)
	 */
	@Override
	public void setSymbol(ISymbol sym) {
		if (sym.getClass().isInstance(CompositeSymbol.class))
		sc=(CompositeSymbol)sym;
		

	}

	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.ICode#toCode()
	 */
	@Override
	public char[] toCode() {
		char[] c=new char[(length()-1)/8+1];
		int i=0;
		for( i=0;i<c.length;i++)
			c[i]=0;
		for( i=0;i<sc.getS1().getCode().toCode().length;i++)
			c[i]=sc.getS1().getCode().toCode()[i];
		for( i=0;i<sc.getS2().getCode().length();i++)
			c[(i+sc.getS1().getCode().length())/8]+=(((sc.getS2().getCode().toCode()[(i)/8])>>(7-i%8)) & 0x1)<<(7-(i+sc.getS1().getCode().length())%8);
		
		return c;
	}

	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.ICode#toRaw()
	 */
	@Override
	public String toRaw() {
		return sc.getS1().getCode().toRaw()+sc.getS2().getCode().toRaw();
	}

	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.ICode#getMsb(int)
	 */
	@Override
	public Integer getMsb(int index) {
		if (index>=length())
			return null;
		return (toCode()[(length()-1-index)/8]>>(7-(index%8)) & 0x1);
	}

	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.ICode#write(net.zoubwolrd.java.utils.compress.BinaryStdOut)
	 */
	@Override
	public void write(BinaryStdOut o) {
		sc.getS1().getCode().write(o);
		sc.getS2().getCode().write(o);

	}
	@Override
	public void write(FileOutputStream o) throws IOException {
		sc.getS1().getCode().write(o);
		sc.getS2().getCode().write(o);
		
	}

	public String toString() {
		String s="(";
		s+=getC1().toRaw();
		s+="+";
		s+=getC2().toRaw();
				
		s+= ")";	
		
		return s;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(CompositeCode.class.isInstance(obj))
		{
			CompositeCode c=(CompositeCode)obj;
			if (!c.getC1().equals(this.getC1()))
				return false;
			if (!c.getC2().equals(this.getC2()))
				return false;
			
				return true;
		}
		return super.equals(obj);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int i=getC1().hashCode();		
		i^=getC2().hashCode();
		return i;
	}
	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.ICode#getLong()
	 */
	@Override
	public Long getLong() {
		if((getC1().length()+getC2().length()>64))
			return null;
		return getC2().getLong()+(getC1().getLong()<<getC2().length());
	}
	public ICode getC1()
	{
		if (c1==null)
			c1=sc.getS1().getCode();
		return c1;
	}
	public ICode getC2()
	{
		if (c2==null)
			c2=sc.getS2().getCode();
		return c2;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	@Override
	public void huffmanAddBit(char c) {
		sc.getS2().getCode().huffmanAddBit(c);
		
	}
	@Override
	public int compareToCode(ICode iCode) {
		// TODO Auto-generated method stub
		return ((CompositeSymbol)getSymbol()).getS1().getCode().compareToCode(iCode);
	}
	@Override
	public int compareToInt(ICode iCode) {
		// TODO Auto-generated method stub
		return ((CompositeSymbol)getSymbol()).getS1().getCode().compareToInt(iCode);
	}

	/** return the length of code 2 of a complexe code starting with symbol s1
	 * */
	public static int getC2Length(ISymbol s1) {
		if (s1==null)
			return 0;
		switch((int)s1.getId())
		{
		case 0x100 : return 4;//"INT4";
		case 0x101 : return 8;//"INT8";
		case 0x102 : return 12;//"INT12";
		case 0x103 : return 13;//"INT16";
		case 0x104 : return 24;//"INT24";
		case 0x105 : return 32;//"INT32";
		case 0x106 : return 48;//"INT48";
		case 0x107 : return 64;// "INT64";
		case 0x108 : return 0;//"RLE";
		case 0x109 : return 0;//"RPE";
		case 0x10A : return 0;//"LZW";
		case 0x10B : return 0;//"PIE";
		case 0x10C : return 0;//"HUF";
		case 0x10D : return 0;//"EOF";
		case 0x10E : return 0;//"HOF";
		case 0x10F : return 0;//"EOS";
		case 0x110 : return 0;//"EOBS";// End of Bit Stream
		case 0x111 : return 0;//"PAT";
		case 0x112 : return 0;//"PATr";
		case 0x113 : return 0;//"Wildcard";
		case 0x114 : return 0;//"Empty";
		case 0x115 : return 0;//"IntAsASCII";
		case 0x117 : return 0;//"FloatAsASCII";
		case 0x118 : return 0;//"FloatAsASCIIes2";
		case 0x119 : return 0;//"DoubleAsASCIIes3";
		case 0x11A : return 0;//"CRLF";
		case 0x11B : return 0;//"SOS";
		case 0x11C : return 0;//"SOln";
		case 0x11D : return 0;//"Qn_mAsASCII";
		case 0x11E : return 6;//"INTn";
		case 0x11F : return 0;//"SAliasn";
		
		
		
		
		
		}
		return 0;
	}
	/** return true if it is a composite code*/
	public static boolean isit(ISymbol s1) {
		
		return getC2Length(s1)>0;
	}
	
	

}
