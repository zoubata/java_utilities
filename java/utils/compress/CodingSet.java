/**
 * 
 */
package com.zoubworld.java.utils.compress;

import java.util.List;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import com.zoubworld.java.utils.compress.SymbolComplex.SymbolINT12;
import com.zoubworld.java.utils.compress.file.IBinaryReader;
import com.zoubworld.java.utils.compress.file.IBinaryWriter;
import com.zoubworld.utils.JavaUtils;

/**
 * @author Pierre Valleau
 *
 *         provide fix length code. where the code value and the symbol value
 *         are the same
 */
public class CodingSet implements ICodingRule {
	/**
	 * flat coding in byte base: i->i for i=[0..255] the coding is 8bits fix length
	 * this didn't support extra symbol.
	 */
	public final static int UNCOMPRESS = 0;
	/**
	 * flat coding including internal/extra/special symbols : i->i for i=[0..255,256...] the coding
	 * is 9bits fix length(today,perhaps later it will be 10bits)
	 */
	public final static int NOCOMPRESS = 1;
	/**
	 * flat coding including internal symbol : i->i for i=[0..255,256...] the coding
	 * is 16bits fix length it is used for debug reading only
	 */
	public final static int NOCOMPRESS16 = 2;
	/**
	 * flat coding including internal symbol : i->i for i=[0..255,256...] the coding
	 * is 32bits fix length it is used for debug reading only
	 * interesting only for debug
	 */
	public final static int NOCOMPRESS32 = 3;
	/**
	 * no coding define
	 */
	public final static Integer UNDEFINED = null;
	// Map<ISymbol,ICode> m;
	BidiMap<ISymbol, ICode> m;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zoubworld.java.utils.compress.ICodingRule#get(com.zoubworld.java.utils.
	 * compress.ISymbol)
	 */
	@Override
	public ICode get(ISymbol sym) {
		if (CompositeSymbol.class.isInstance(sym)) {
			CompositeSymbol cs = (CompositeSymbol) sym;
			ISymbol sa = cs.getS1();
			ICode a = get(sa);
			ISymbol sb = cs.getS2();
			ICode b = sb.getCode();
			/*
			 * ICode code=new CompositeCode(a,b);
			 */
			sa.setCode(a);
			sb.setCode(b);
			
			CompositeCode cc=new CompositeCode(cs);
			
			cs.setCode(cc);

			// ICode code=new CompositeCode(cs);
			// cs.setCode(code);
			ICode code = cs.getCode();

			/**/
			return code;

		}

		return m.get(sym);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zoubworld.java.utils.compress.ICodingRule#get(com.zoubworld.java.utils.
	 * compress.ICode)
	 */
	@Override
	public ISymbol get(ICode code) {
		/*
		 * for(Entry<ISymbol, ICode> e:m.entrySet()) if(e.getValue()==code) return
		 * e.getKey(); return null;
		 */
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

	int len = 8;

	/**
	 * 
	 */
	public CodingSet(Integer method) {

		// m=new HashMap();
		m = new DualHashBidiMap<>();
		if (method == null)
			return;

		if (method == UNCOMPRESS) {
			len = 8;
		}
		if (method == NOCOMPRESS) {
			len = 9;
		}
		if (method == NOCOMPRESS16) {
			len = 16;
		}
		if (method == NOCOMPRESS32) {
			len = 32;
		}
		for (char c = 0; c < 256; c++)
			m.put(Symbol.findId(c), new Code(c, len));

		/*
		 * for (short c=256;c<Symbol.getNbSymbol();c++) m.put(new Symbol(c), new Code(
		 * c));
		 */

		if (method != UNCOMPRESS) {

			for (short c = 256; c < Symbol.getNbSymbol(); c++)
				if (Symbol.findId(c) != null)
					m.put(Symbol.findId(c), new Code(c, len));
		}

	}

	public CodingSet(int nbsym, int nbBit, IBinaryReader binaryStdin) {
		m = new DualHashBidiMap<>();
		len = nbBit;
		for (int sym = 0; sym < nbsym; sym++) {
			long code = binaryStdin.readLong(nbBit);

			m.put(Symbol.findId(sym), new Code(code, nbBit));
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zoubworld.java.utils.compress.ICodingRule#getCode(com.zoubworld.java.
	 * utils.compress.file.BinaryStdIn) support UNCOMPRESS and NOCOMPRESS only it
	 * need a dedicated class for other coding
	 */
	@Override
	public ICode getCode(IBinaryReader binaryStdIn) {

		Integer b = binaryStdIn.readInt(len);
		if (b == null)
			return null; /*
							 * Code c = new Code(b); c.setSymbol(Symbol.findId(b));
							 */
		ISymbol s = Symbol.findId(b);
		if (s == null) {
			System.out.println("symbol doesn't exist " + s + "," + b);
			return null;
		}
		ICode c = get(s);
		if (c != null)
			c.setSymbol(s);
		else
			System.out.println("symbol haven't code " + s + "," + c + "," + b);
		s.setCode(c);
		if (CompositeCode.isit(s)) {
			ICode cc = getCode(c, binaryStdIn);

			return cc;

		}
		return c;
	}

	public ICode getGenericCode(IBinaryReader binaryStdIn) {
		int b = binaryStdIn.readInt(len);
		/*
		 * Code c = new Code(b); c.setSymbol(Symbol.findId(b));
		 */
		ISymbol s = Symbol.findId(b);
		if (s == null) {
			System.out.println("symbol doesn't exist " + s + "," + b);
			return null;
		}
		ICode c = get(s);
		c.setSymbol(s);
		return c;
	}

	/**
	 * return the complex code starting by c from binaryStdIn
	 */
	public ICode getCode(ICode c, IBinaryReader binaryStdIn) {

		ISymbol s1 = get(c);
		int l = CompositeCode.getC2Length(s1);
		ICode c2 = new Code(binaryStdIn.readLong(l), l);
		ISymbol s2 = new Symbol(c2.getLong(), c2);
		c2.setSymbol(s2);
		CompositeSymbol cs = new CompositeSymbol(s1, s2);
		CompositeCode cc = new CompositeCode(cs);
		cs.setCode(cc);
		return cc;

	}

	@Override
	public ISymbol getSymbol(IBinaryReader binaryStdIn) {
		ICode c = getCode(binaryStdIn);
		if (c == null)
			return null;
		return c.getSymbol();
	}

	public String toString() {
		String s = "CodingSet(" + len + ",";
		List<ISymbol> l = JavaUtils.asSortedSet(m.keySet());
		for (ISymbol e : l)
			s += e.toString() + "->" + m.get(e).toString() + "\n";
		s += ")";
		return s;
	}

	@Override
	public void writeCodingRule(IBinaryWriter binaryStdOut) {
		int nbSym = 0;
		/*
		 * for(ISymbol s:m.keySet()) if(s.getId()>nbSym) nbSym=(int) (s.getId());
		 */
		nbSym = m.keySet().size();
		binaryStdOut.write(new SymbolINT12((short) nbSym));
		binaryStdOut.write(Symbol.FactorySymbolINT(len));
		// System.out.println("nbSym->"+nbSym);
		// System.out.println("len->"+len);
		for (int sym = 0; sym < nbSym; sym++) {
			ISymbol s = Symbol.findId(sym);
			if (s == null)
				s = Symbol.Empty;
			ICode c = get(s);
			binaryStdOut.write(c.getLong(), len);
			// System.out.println(s.toString()+"->"+c.toString());
		}

	}

	@Override
	public boolean equals(Object obj) {
		if (ICodingRule.class.isInstance(obj)) {
			ICodingRule c = (ICodingRule) obj;
			for (ISymbol sym : Symbol.getAll())
				if (!((this.get(sym) != null && this.get(sym).equals(c.get(sym)))
						|| (this.get(sym) == null && c.get(sym) == null)))
					return false;
			return true;
		} else
			return super.equals(obj);

	}
}
