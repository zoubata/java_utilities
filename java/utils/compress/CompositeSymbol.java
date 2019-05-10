/**
 * 
 */
package com.zoubwolrd.java.utils.compress;

/**
 * @author 
 * this is a symbol with a data, so it answer as symbol1, but is is coded as symbol 1(huffman coding)+ symbol 2 code(raw coding).
 */
public class CompositeSymbol implements ISymbol {

	ISymbol s1;
	ISymbol s2;
	
	/**
	 * 
	 */
	public CompositeSymbol(ISymbol mys1,ISymbol mys2) {
		s1=mys1;
		s2=mys2;
	}

	public ISymbol getS1() {
		return s1;
	}

	public ISymbol getS2() {
		return s2;
	}

	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#isChar()
	 */
	@Override
	public boolean isChar() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#isInt()
	 */
	@Override
	public boolean isInt() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#isShort()
	 */
	@Override
	public boolean isShort() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#getChar()
	 */
	@Override
	public char getChar() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#getInt()
	 */
	@Override
	public Integer getInt() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#getShort()
	 */
	@Override
	public short getShort() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#getId()
	 */
	@Override
	public long getId() {

		return s1.getId();
	}

	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#toSymbol()
	 */
	@Override
	public char[] toSymbol() {
		
		return getS1().toSymbol();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ("composite("+s1.toString()+","+s2.toString()+")");
	}
	ICode code=null;
	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#getCode()
	 */
	@Override
	public ICode getCode() {
		if (code==null)
			code=new CompositeCode(this);
		return code;
	}

	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#setCode(net.zoubwolrd.java.utils.compress.Code)
	 */
	@Override
	public void setCode(ICode code2) {
		code=code2;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public Long getLong() {
		// TODO Auto-generated method stub
		return null;
	}

}
