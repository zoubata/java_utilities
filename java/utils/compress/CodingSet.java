/**
 * 
 */
package com.zoubworld.java.utils.compress;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.zoubworld.java.utils.compress.file.BinaryStdIn;


/**
 * @author Pierre Valleau
 *
 */
public class CodingSet implements ICodingRule {
	/** flat coding in byte base: i->i for i=[0..255] 
	 * */
public final static int UNCOMPRESS = 0;
/** flat coding including internal symbol : i->i for i=[0..255,256...] 
 * */
public final static int NOCOMPRESS = 1;
/** no coding define
 * */
public final static Integer UNDEFINED = null;
	Map<ISymbol,ICode> m;
	
	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.ICodingRule#get(com.zoubworld.java.utils.compress.ISymbol)
	 */
	@Override
	public ICode get(ISymbol sym) {
		return m.get(sym);
	}
	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.ICodingRule#get(com.zoubworld.java.utils.compress.ICode)
	 */
	@Override
	public ISymbol get(ICode code) {
		for(Entry<ISymbol, ICode> e:m.entrySet())
			if(e.getValue()==code)
				return e.getKey();
		return null;
	}

	/**
	 * @param sym
	 * @param code
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public ICode put(ISymbol sym, ICode code) {
		return m.put(sym, code);
	}

	int len=8;
	/**
	 * 
	 */
	public CodingSet(Integer method ) {
		
		m=new HashMap();
		if (method==null)
			return;
		
		if (method==UNCOMPRESS)	
		{
			len=8;
		for (char c=0;c<256;c++)
			m.put(Symbol.findId(c), new Code( c,len));
		
		/*for (short c=256;c<Symbol.getNbSymbol();c++)
			m.put(new Symbol(c), new Code( c));*/
	
		}
		
		if (method==NOCOMPRESS)	
		{
			len=9;
			for (short c=0;c<256;c++)
				m.put(Symbol.findId(c), new Code( c,len));
			
			for (short c=256;c<Symbol.getNbSymbol();c++)
				m.put(Symbol.findId(c), new Code( c,len));
		}
	
	}
	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.ICodingRule#getCode(com.zoubworld.java.utils.compress.file.BinaryStdIn)
	 * support UNCOMPRESS and NOCOMPRESS only
	 * it need a dedicated class for other coding
	 */
	@Override
	public ICode getCode(BinaryStdIn binaryStdIn) {
		int b = binaryStdIn.readInt(len);
	/*	Code c = new Code(b);
		c.setSymbol(Symbol.findId(b));*/
		ICode c =get(Symbol.findId(b));
		return c;
	}
	@Override
	public ISymbol getSymbol(BinaryStdIn binaryStdIn) {
		int b = binaryStdIn.readInt(len);
		return Symbol.findId(b);
	}

}
