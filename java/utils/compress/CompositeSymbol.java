/**
 * 
 */
package com.zoubworld.java.utils.compress;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author @author Pierre Valleau this is a symbol with a data, so it answer as
 *         symbol1, but is is coded as symbol 1(huffman coding)+ symbol 2
 *         code(raw coding).
 */
public class CompositeSymbol implements ISymbol {

	ISymbol s0;
	protected ISymbol s1;

	Object obj=null;
	/**
	 * @return the obj
	 */
	public Object getObj() {
		return obj;
	}

	/**
	 * @param obj the obj to set
	 */
	public void setObj(Object obj) {
		this.obj = obj;
	}

	/**
	 * 
	 */
	public CompositeSymbol(ISymbol mys1, ISymbol mys2) {
		s0 = mys1;
		s1 = mys2;
	}

	public ISymbol getS0() {
		return s0;
	}

	public ISymbol getS1() {
		return s1;
	}

	/*
	 * Symbol.FactorySymbolINT(long i) public ISymbol FactoryINTxx(long i) { if
	 * (i<16) return new SymbolINT4((byte)i); else if (i<Byte.MAX_VALUE) return new
	 * SymbolINT8((byte)i); else if (i<Short.MAX_VALUE) return new
	 * SymbolINT16((short)i); else if (i<4096) return new SymbolINT12((short)i);
	 * else if (i<256*256*256) return new SymbolINT24((short)i); else if
	 * (i<Integer.MAX_VALUE) return new SymbolINT32((int)i); else return new
	 * SymbolINT64((long)i); }
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#isChar()
	 */
	@Override
	public boolean isChar() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#isInt()
	 */
	@Override
	public boolean isInt() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#isShort()
	 */
	@Override
	public boolean isShort() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (CompositeSymbol.class.isInstance(obj)) {
			CompositeSymbol c = (CompositeSymbol) obj;
			if (!c.getS0().equals(getS0()))
				return false;
			if (!c.getS1().equals(getS1()))
				return false;

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
		int i = getS0().hashCode();
		i ^= getS1().hashCode();
		return i;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#getChar()
	 */
	@Override
	public char getChar() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#getInt()
	 */
	@Override
	public Integer getInt() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#getShort()
	 */
	@Override
	public short getShort() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#getId()
	 */
	@Override
	public long getId() {

		return s0.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#toSymbol()
	 */
	@Override
	public char[] toSymbol() {

		return getS0().toSymbol();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ("composite(" + ((obj==null)?"":obj.toString() + ":") + ((s0==null)?"":s0.toString()) + "," + ((s1==null)?"":s1.toString() )+ ")");
	}

	ICode code = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.ISymbol#getCode()
	 */
	@Override
	public ICode getCode() {
		if (code == null)
			code = new CompositeCode(this);
		return code;
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

	@Override
	public int compareTo(ISymbol o) {
		if (o == null)
			return -1;
		int c = (int) (getId() - o.getId());
		if (c == 0)
			if (CompositeSymbol.class.isInstance(o)) {
				if (getS1() == null)
					return 1;
				else if (getS1() == null)
					return -1;
				else
					return (int) (getS1().getId() - ((CompositeSymbol) o).getS1().getId());
			}
		return c;
	}

	@Override
	public ISymbol Factory(Long nId) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Set<ISymbol> getCompositeSymbols(List<ISymbol> lse) {
		Set<ISymbol> l=new HashSet<ISymbol>();
		for(ISymbol s:lse)
		{
			if (CompositeSymbol.class.isInstance(s))
			{
				CompositeSymbol cs=(CompositeSymbol)s;
				l.add(cs.getS0());
			}
		}
		return l;
	}

	public static List<List<ISymbol>> flatter(List<ISymbol> lse, ISymbol sym_ref) {
		List<List<ISymbol>> streams=new ArrayList<List<ISymbol>>();

		streams.add(new ArrayList<ISymbol>());//s0
		streams.add(new ArrayList<ISymbol>());//s1
		for(ISymbol s:lse)
		{
			if (CompositeSymbol.class.isInstance(s))
				{			
					CompositeSymbol cs=(CompositeSymbol)s;
					if(cs.getS0().equals(sym_ref))
					{	
						streams.get(0).add(cs.getS0());	
						streams.get(1).add(cs.getS1());	
						
					}
					else
						streams.get(0).add(s);
				}
			else
				streams.get(0).add(s);
		}
		return streams;
	}
}
