/**
 * 
 */
package com.zoubworld.java.utils.compress;

import com.zoubworld.java.utils.compress.file.BinaryStdOut;

/**
 * @author 
 *
 *this class contains 2 code from 2 symbol inside a composite symbol. example INT16(0x1234) code(INT16)+code(0x1234)
 * 2 H
 */
public class CompositeCode implements ICode {

	CompositeSymbol sc;

	public CompositeCode(CompositeSymbol s) {
		sc=s;
	}
	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.ICode#length()
	 */
	@Override
	public int length() {
		// TODO Auto-generated method stub
		return sc.getS1().getCode().length()+sc.getS2().getCode().length();
	}

	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.ICode#getSymbol()
	 */
	@Override
	public ISymbol getSymbol() {
		// TODO Auto-generated method stub
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

	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.ICode#getLong()
	 */
	@Override
	public Long getLong() {
		if((sc.getS1().getCode().length()+sc.getS2().getCode().length()>64))
			return null;
		return sc.getS2().getCode().getLong()+(sc.getS1().getCode().getLong()<<sc.getS2().getCode().length());
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

}
