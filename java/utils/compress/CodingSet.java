/**
 * 
 */
package com.zoubworld.java.utils.compress;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import com.zoubworld.java.utils.compress.file.BinaryStdIn;


/**
 * @author Pierre Valleau
 *
 * provide fix length code.
 * where the code value and the symbol value are the same
 */
public class CodingSet implements ICodingRule {
	/** flat coding in byte base: i->i for i=[0..255] 
	 * the coding is 8bits fix length
	 * */
public final static int UNCOMPRESS = 0;
/** flat coding including internal symbol : i->i for i=[0..255,256...]
 * the coding is 9bits  fix length 
 * */
public final static int NOCOMPRESS = 1;
/** flat coding including internal symbol : i->i for i=[0..255,256...]
 * the coding is 16bits  fix length
 * it is used for debug reading only
 * */
public final static int NOCOMPRESS16 = 2;
/** no coding define
 * */
public final static Integer UNDEFINED = null;
	//Map<ISymbol,ICode> m;
	BidiMap<ISymbol, ICode> m ;
	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.ICodingRule#get(com.zoubworld.java.utils.compress.ISymbol)
	 */
	@Override
	public ICode get(ISymbol sym) {
		if(CompositeSymbol.class.isInstance(sym))
		{
			CompositeSymbol cs=(CompositeSymbol)sym;
			ISymbol sa=cs.getS1();
			ICode a=get(sa);
			ISymbol sb=cs.getS2();
			ICode b=sb.getCode();
			/*
			ICode code=new CompositeCode(a,b);*/
			sa.setCode(a);
			sb.setCode(b);
			//ICode code=new CompositeCode(cs);
			//cs.setCode(code);
			ICode code=cs.getCode();
			/**/
			return code;
			
		}

		return m.get(sym);
	}
	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.ICodingRule#get(com.zoubworld.java.utils.compress.ICode)
	 */
	@Override
	public ISymbol get(ICode code) {
		/*for(Entry<ISymbol, ICode> e:m.entrySet())
			if(e.getValue()==code)
				return e.getKey();
		return null;*/
		return m.getKey(code);
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
		
		//m=new HashMap();
		m=new DualHashBidiMap<>();
		if (method==null)
			return;
		
		if (method==UNCOMPRESS)	
		{
			len=8;
		}
		if (method==NOCOMPRESS)	
		{
			len=9;
		}
		if (method==NOCOMPRESS16)	
		{
			len=16;
		}
		for (char c=0;c<256;c++)
			m.put(Symbol.findId(c), new Code( c,len));
		
		/*for (short c=256;c<Symbol.getNbSymbol();c++)
			m.put(new Symbol(c), new Code( c));*/
	
		
		if (method!=UNCOMPRESS)	
		{
		
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
		ISymbol s=Symbol.findId(b);
		ICode c =get(s);
		if(c!=null)
		c.setSymbol(s);
		else
			System.out.println("symbol hjaven't code"+s);
		s.setCode(c);
		if(CompositeCode.isit(s))
		{
			ICode cc=getCode(c,binaryStdIn); 
			
			return cc;		
			
		}
		return c;
	}
	/** return the complex code starting by c from binaryStdIn
	 * */
	public ICode getCode(ICode c, BinaryStdIn binaryStdIn) {
	
		ISymbol s1=get(c);
		int l=CompositeCode.getC2Length(s1);
		ICode c2=new Code(binaryStdIn.readLong(l),l);
		ISymbol s2=new Symbol(c2.getLong(),c2);
		c2.setSymbol(s2);
		CompositeSymbol cs=new CompositeSymbol(s1, s2);		
		CompositeCode cc=new CompositeCode(cs);
		cs.setCode(cc);
return cc;
		
	}
	@Override
	public ISymbol getSymbol(BinaryStdIn binaryStdIn) {
		ICode c= getCode( binaryStdIn);		
		return c.getSymbol();
	}

}
