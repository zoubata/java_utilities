/**
 * 
 */
package com.zoubworld.java.utils.compress;

import java.util.ArrayList;
import java.util.List;

/**
 * @author this is a symbol with a header and n data, so it answer(getId) as
 *         symbol0, but is is coded as symbol 0(huffman coding) symbol 1(raw
 *         coding)+ symbol 2 code(raw coding)....
 */
public class CompositeSymbols implements ISymbol {

/** Convert a list of Symbol into several one by decomposing a specific composite symbol class
 * 
 * */
	public static List<List<ISymbol>> flatter(List<ISymbol> lse, CompositeSymbols sym_LZS) {
		List<List<ISymbol>> streams=new ArrayList<List<ISymbol>>();
		List<ISymbol> ss=sym_LZS.getSs() ;
		for(ISymbol s:ss)
			streams.add(new ArrayList<ISymbol>());
		for(ISymbol s:lse)
		{
			if (s.getClass()==sym_LZS.getClass())
			{
				CompositeSymbols cs=(CompositeSymbols)s;
				int i=0;
				for(ISymbol e:cs.getSs())
					streams.get(i++).add(e);				
			}
			else
				streams.get(0).add(s);
		}
		return streams;
	}
	public static List<ISymbol> join(List<List<ISymbol>> streams, CompositeSymbols sym_LZS) {
		List<ISymbol> lse=new ArrayList<ISymbol>();
		int index=0;
		for(ISymbol s:streams.get(0))
		{
			if (s.getClass()==sym_LZS.getClass())
			{
				CompositeSymbols cs=(CompositeSymbols)s;
				List<ISymbol> ss=new ArrayList<ISymbol>();
				
				for(int i=1;i<sym_LZS.getSs().size() ;i++)
					ss.add(streams.get(i).get(index));
				lse.add(sym_LZS.Factory(s,ss));index++;
			}
			else
				lse.add(s);
		}
		return lse;
	}
	
	List<ISymbol> listSymbol;

	/**
	 * 
	 */
	public CompositeSymbols(ISymbol mys0, ISymbol mys1, ISymbol mys2) {
		listSymbol = new ArrayList<ISymbol>();
		listSymbol.add(mys0);
		listSymbol.add(mys1);
		listSymbol.add(mys2);
	}

	public CompositeSymbols(ISymbol mys0, List<ISymbol> mysl) {
		listSymbol = new ArrayList<ISymbol>();
		listSymbol.add(mys0);
		listSymbol.addAll(mysl);
	}
	private CompositeSymbols( List<ISymbol> mysl) {
		listSymbol = new ArrayList<ISymbol>();
		listSymbol.addAll(mysl);
	}
	public CompositeSymbols Factory(ISymbol mys0, List<ISymbol> mysl) {
		return new CompositeSymbols( mys0,  mysl) ;
	}
	public CompositeSymbols Factory( List<ISymbol> mysl) {
		return new CompositeSymbols(   mysl) ;
	}

	/**
	 * 
	 */
	public CompositeSymbols(ISymbol mys1, ISymbol mys2) {
		listSymbol = new ArrayList<ISymbol>();
		listSymbol.add(mys1);
		listSymbol.add(mys2);
	}

	public ISymbol getS1() {
		return listSymbol.get(1);
	}

	public ISymbol getS0() {
		return listSymbol.get(0);
	}

	public ISymbol getS2() {
		return listSymbol.get(2);
	}

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
		if (CompositeSymbols.class.isInstance(obj)) {
			CompositeSymbols c = (CompositeSymbols) obj;
			if (!c.getSs().equals(getSs()))
				return false;
			return true;
		}
		return super.equals(obj);
	}

	public List<ISymbol> getSs() {

		return listSymbol;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int i = 0;
		for (ISymbol e : listSymbol)
			i ^= e.hashCode();
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

		return getS0().getId();
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
		String ss = "";
		for (ISymbol e : listSymbol)
			ss += e.toString() + ",";
		return ("composite(" + ss + ")");
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
			code = new CompositeCodes(this);
		return code;
	}

	/**
	 * @param e
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(ISymbol e) {
		code = null;
		return getSs().add(e);
	}

	public void addAll(List<ISymbol> list) {
		code = null;
		getSs().addAll(list);
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
			if (CompositeSymbols.class.isInstance(o)) {
				CompositeSymbols cs = (CompositeSymbols) o;
				int i = 0;
				for (; i < Math.min(getSs().size(), cs.getSs().size()); i++) {
					int r = getSs().get(i).compareTo(cs.getSs().get(i));
					if (r != 0)
						return r;

				}
				if ((getSs().size() == cs.getSs().size()))
					return 0;
				else if ((getSs().size() < cs.getSs().size()))
					return -1;
				else
					return 1;
			}
		return c;

	}

	@Override
	public ISymbol Factory(Long nId) {
		// TODO Auto-generated method stub
		return null;
	}
	public void setS1(ISymbol s1) {
		 listSymbol.remove(1);
		 listSymbol.add(1,s1);
	}
}
