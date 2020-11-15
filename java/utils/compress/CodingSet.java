/**
 * 
 */
package com.zoubworld.java.utils.compress;

import java.util.List;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.lang3.StringUtils;

import com.zoubworld.java.utils.compress.SymbolComplex.SymbolINT12;
import com.zoubworld.java.utils.compress.file.IBinaryReader;
import com.zoubworld.java.utils.compress.file.IBinaryWriter;
import com.zoubworld.utils.JavaUtils;

/**
 * @author Pierre Valleau
 *
 *         provide fix couple (code,symbol). 
 *         almost of then are fix length where the code value and the symbol value
 *         are the same
 */
public class CodingSet implements ICodingRule {
	/**
	 * flat coding in byte base: i->i for i=[0..255] the coding is 8bits fix length
	 * this didn't support extra symbol.
	 */
	public final static int UNCOMPRESS = 0;
	/**
	 * flat coding including internal/extra/special symbols : i->i for
	 * i=[0..255,256...] the coding is 9bits fix length(today,perhaps later it will
	 * be 10bits)
	 */
	public final static int NOCOMPRESS = 1;
	/**
	 * flat coding including internal symbol : i->i for i=[0..255,256...] the coding
	 * is 16bits fix length it is used for debug reading only
	 */
	public final static int NOCOMPRESS16 = 2;
	/**
	 * flat coding including internal symbol : i->i for i=[0..255,256...] the coding
	 * is 32bits fix length it is used for debug reading only interesting only for
	 * debug
	 */
	public final static int NOCOMPRESS32 = 3;
	/**
	 * exponential coding, this code is used to play with bit stream entropy 1->0b0
	 * 2->0b10 3->0b100 i->0b1 i*'0' 255->0b1000...000 (254*'0') 0->0b1000...000
	 * (255*'0')
	 * 
	 */
	public final static int COMPRESS01TO1x0 = 4;
	/**
	 * https://en.wikipedia.org/wiki/Unary_coding
	 */
	public final static int UnaryCode = 5;
	/**
	 * https://en.wikipedia.org/wiki/Varicode
	 * used hitoricaly for PSK31 and ascii table.
	 * warning a lot of symbol is missing.
	 * */
	public final static int VariCode = 6;
	/**
	 * no coding define
	 */
	public final static Integer UNDEFINED = null;
	// Map<ISymbol,ICode> m;
	BidiMap<ISymbol, ICode> m;
	void buildVariCode()
	{
		m = new DualHashBidiMap<>();
		//Control characters
		m.put(Symbol.findId(0x0), new Code("1010101011"));
		m.put(Symbol.findId(0x1), new Code("1011011011"));
		m.put(Symbol.findId(0x2), new Code("1011101101"));
		m.put(Symbol.findId(0x3), new Code("1101110111"));
		m.put(Symbol.findId(0x4), new Code("1011101011"));
		m.put(Symbol.findId(0x5), new Code("1101011111"));
		m.put(Symbol.findId(0x6), new Code("1011101111"));
		m.put(Symbol.findId(0x7), new Code("1011111101"));
		m.put(Symbol.findId(0x8), new Code("1011111111"));
		m.put(Symbol.findId(0x9), new Code("11101111"));
		m.put(Symbol.findId(0x0A), new Code("11101"));
		m.put(Symbol.findId(0x0B), new Code("1101101111"));
		m.put(Symbol.findId(0x0C), new Code("1011011101"));
		m.put(Symbol.findId(0x0D), new Code("11111"));
		m.put(Symbol.findId(0x0E), new Code("1101110101"));
		m.put(Symbol.findId(0x0F), new Code("1110101011"));
		m.put(Symbol.findId(0x10), new Code("1011110111"));
		m.put(Symbol.findId(0x11), new Code("1011110101"));
		m.put(Symbol.findId(0x12), new Code("1110101101"));
		m.put(Symbol.findId(0x13), new Code("1110101111"));
		m.put(Symbol.findId(0x14), new Code("1101011011"));
		m.put(Symbol.findId(0x15), new Code("1101101011"));
		m.put(Symbol.findId(0x16), new Code("1101101101"));
		m.put(Symbol.findId(0x17), new Code("1101010111"));
		m.put(Symbol.findId(0x18), new Code("1101111011"));
		m.put(Symbol.findId(0x19), new Code("1101111101"));
		m.put(Symbol.findId(0x1A), new Code("1110110111"));
		m.put(Symbol.findId(0x1B), new Code("1101010101"));
		m.put(Symbol.findId(0x1C), new Code("1101011101"));
		m.put(Symbol.findId(0x1D), new Code("1110111011"));
		m.put(Symbol.findId(0x1E), new Code("1011111011"));
		m.put(Symbol.findId(0x1F), new Code("1101111111"));
		m.put(Symbol.findId(0x7F), new Code("1110110101"));
		//Printable characters
		m.put(Symbol.findId(0x20), new Code("1"));
		m.put(Symbol.findId(0x21), new Code("111111111"));
		m.put(Symbol.findId(0x22), new Code("101011111"));
		m.put(Symbol.findId(0x23), new Code("111110101"));
		m.put(Symbol.findId(0x24), new Code("111011011"));
		m.put(Symbol.findId(0x25), new Code("1011010101"));
		m.put(Symbol.findId(0x26), new Code("1010111011"));
		m.put(Symbol.findId(0x27), new Code("101111111"));
		m.put(Symbol.findId(0x28), new Code("11111011"));
		m.put(Symbol.findId(0x29), new Code("11110111"));
		m.put(Symbol.findId(0x2A), new Code("101101111"));
		m.put(Symbol.findId(0x2B), new Code("111011111"));
		m.put(Symbol.findId(0x2C), new Code("1110101"));
		m.put(Symbol.findId(0x2D), new Code("110101"));
		m.put(Symbol.findId(0x2E), new Code("1010111"));
		m.put(Symbol.findId(0x2F), new Code("110101111"));
		m.put(Symbol.findId(0x30), new Code("10110111"));
		m.put(Symbol.findId(0x31), new Code("10111101"));
		m.put(Symbol.findId(0x32), new Code("11101101"));
		m.put(Symbol.findId(0x33), new Code("11111111"));
		m.put(Symbol.findId(0x34), new Code("101110111"));
		m.put(Symbol.findId(0x35), new Code("101011011"));
		m.put(Symbol.findId(0x36), new Code("101101011"));
		m.put(Symbol.findId(0x37), new Code("110101101"));
		m.put(Symbol.findId(0x38), new Code("110101011"));
		m.put(Symbol.findId(0x39), new Code("110110111"));
		m.put(Symbol.findId(0x3A), new Code("11110101"));
		m.put(Symbol.findId(0x3B), new Code("110111101"));
		m.put(Symbol.findId(0x3C), new Code("111101101"));
		m.put(Symbol.findId(0x3D), new Code("1010101"));
		m.put(Symbol.findId(0x3E), new Code("111010111"));
		m.put(Symbol.findId(0x3F), new Code("1010101111"));
		m.put(Symbol.findId(0x40), new Code("1010111101"));
		m.put(Symbol.findId(0x41), new Code("1111101"));
		m.put(Symbol.findId(0x42), new Code("11101011"));
		m.put(Symbol.findId(0x43), new Code("10101101"));
		m.put(Symbol.findId(0x44), new Code("10110101"));
		m.put(Symbol.findId(0x45), new Code("1110111"));
		m.put(Symbol.findId(0x46), new Code("11011011"));
		m.put(Symbol.findId(0x47), new Code("11111101"));
		m.put(Symbol.findId(0x48), new Code("101010101"));
		m.put(Symbol.findId(0x49), new Code("1111111"));
		m.put(Symbol.findId(0x4A), new Code("111111101"));
		m.put(Symbol.findId(0x4B), new Code("101111101"));
		m.put(Symbol.findId(0x4C), new Code("11010111"));
		m.put(Symbol.findId(0x4D), new Code("10111011"));
		m.put(Symbol.findId(0x4E), new Code("11011101"));
		m.put(Symbol.findId(0x4F), new Code("10101011"));
		m.put(Symbol.findId(0x50), new Code("11010101"));
		m.put(Symbol.findId(0x51), new Code("111011101"));
		m.put(Symbol.findId(0x52), new Code("10101111"));
		m.put(Symbol.findId(0x53), new Code("1101111"));
		m.put(Symbol.findId(0x54), new Code("1101101"));
		m.put(Symbol.findId(0x55), new Code("101010111"));
		m.put(Symbol.findId(0x56), new Code("110110101"));
		m.put(Symbol.findId(0x57), new Code("101011101"));
		m.put(Symbol.findId(0x58), new Code("101110101"));
		m.put(Symbol.findId(0x59), new Code("101111011"));
		m.put(Symbol.findId(0x5A), new Code("1010101101"));
		m.put(Symbol.findId(0x5B), new Code("111110111"));
		m.put(Symbol.findId(0x5C), new Code("111101111"));
		m.put(Symbol.findId(0x5D), new Code("111111011"));
		m.put(Symbol.findId(0x5E), new Code("1010111111"));
		m.put(Symbol.findId(0x5F), new Code("101101101"));
		m.put(Symbol.findId(0x60), new Code("1011011111"));
		m.put(Symbol.findId(0x61), new Code("1011"));
		m.put(Symbol.findId(0x62), new Code("1011111"));
		m.put(Symbol.findId(0x63), new Code("101111"));
		m.put(Symbol.findId(0x64), new Code("101101"));
		m.put(Symbol.findId(0x65), new Code("11"));
		m.put(Symbol.findId(0x66), new Code("111101"));
		m.put(Symbol.findId(0x67), new Code("1011011"));
		m.put(Symbol.findId(0x68), new Code("101011"));
		m.put(Symbol.findId(0x69), new Code("1101"));
		m.put(Symbol.findId(0x6A), new Code("111101011"));
		m.put(Symbol.findId(0x6B), new Code("10111111"));
		m.put(Symbol.findId(0x6C), new Code("11011"));
		m.put(Symbol.findId(0x6D), new Code("111011"));
		m.put(Symbol.findId(0x6E), new Code("1111"));
		m.put(Symbol.findId(0x6F), new Code("111"));
		m.put(Symbol.findId(0x70), new Code("111111"));
		m.put(Symbol.findId(0x71), new Code("110111111"));
		m.put(Symbol.findId(0x72), new Code("10101"));
		m.put(Symbol.findId(0x73), new Code("10111"));
		m.put(Symbol.findId(0x74), new Code("101"));
		m.put(Symbol.findId(0x75), new Code("110111"));
		m.put(Symbol.findId(0x76), new Code("1111011"));
		m.put(Symbol.findId(0x77), new Code("1101011"));
		m.put(Symbol.findId(0x78), new Code("11011111"));
		m.put(Symbol.findId(0x79), new Code("1011101"));
		m.put(Symbol.findId(0x7A), new Code("111010101"));
		m.put(Symbol.findId(0x7B), new Code("1010110111"));
		m.put(Symbol.findId(0x7C), new Code("110111011"));
		m.put(Symbol.findId(0x7D), new Code("1010110101"));
		m.put(Symbol.findId(0x7E), new Code("1011010111"));

	}
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

			CompositeCode cc = new CompositeCode(cs);

			cs.setCode(cc);

			// ICode code=new CompositeCode(cs);
			// cs.setCode(code);
			ICode code = cs.getCode();
			
			/**/
			return code;

		}

		ICode code = m.get(sym);
		if (code==null)
			code=m.get(Symbol.findId((int)sym.getId()));
			return code;
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

	int len = 0;

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
		if (len != 0) {
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
		
		if (method == COMPRESS01TO1x0) {

			for (char c = 2; c < 256; c++)
				m.put(Symbol.findId(c), new Code("1" + StringUtils.repeat("0", c - 1)));
			m.put(Symbol.findId(0), new Code("1" + StringUtils.repeat("0", 255)));
			m.put(Symbol.findId(1), new Code("1"));


				for (char c = 0; c < 256; c++)
					m.put(Symbol.findId(c), new Code(StringUtils.repeat("1", c)+"0"));				
			}
		}
		else
		if (method == VariCode) {
			buildVariCode();
		}
			
		

		 else if (method == UnaryCode) {

			for (char c = 0; c < 256; c++)
				m.put(Symbol.findId(c), new Code(StringUtils.repeat("1", c) + "0"));
		}


	}

	public CodingSet(int nbsym, int nbBit, IBinaryReader binaryStdin) {
		m = new DualHashBidiMap<>();
		len = nbBit;
		for (int sym = 0; sym < nbsym; sym++) {
			long code = binaryStdin.readSignedLong(nbBit);

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

		Integer b = binaryStdIn.readUnsignedInt(len);
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
		int b = binaryStdIn.readUnsignedInt(len);
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
		ISymbol sr = Symbol.decode(s1,  binaryStdIn);
		return sr.getCode();
		/*
		int l = CompositeCode.getC2Length(s1);
		ICode c2 = new Code(binaryStdIn.readLong(l), l);
		ISymbol s2 = new Symbol(c2.getLong(), c2);
		c2.setSymbol(s2);
		CompositeSymbol cs = new CompositeSymbol(s1, s2);
		CompositeCode cc = new CompositeCode(cs);
		cs.setCode(cc);
		return cc;*/

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
	//@Override
	public void writeCodingRule2(int coding,IBinaryWriter binaryStdOut) {
		int nbSym = 0;
		//int coding=CodeNumber.Golomb2Coding;
		nbSym = m.keySet().size();
		binaryStdOut.write(CodeNumber.getCode(CodeNumber.FibonacciCoding, nbSym));
		binaryStdOut.write(CodeNumber.getCode(CodeNumber.FibonacciCoding, coding));
		for (int sym = 0; sym < nbSym; sym++) {
			ISymbol s = Symbol.findId(sym);
			ICode c=Code.NULL;
			if (s == null)
				s = Symbol.Empty;
			else 
				c= get(s);
			binaryStdOut.write(CodeNumber.getCode(coding, c.length()) );
			binaryStdOut.write(c.getLong(), c.length());
		}
	}
	public void readCodingRule2(IBinaryReader binaryStdin) {
		int nbSym = 0;
		int coding=CodeNumber.Golomb2Coding;
		nbSym=CodeNumber.readCode(CodeNumber.FibonacciCoding, binaryStdin).intValue();
		coding=CodeNumber.readCode(CodeNumber.FibonacciCoding, binaryStdin).intValue();
		m = new DualHashBidiMap<>();		
		for (int sym = 0; sym < nbSym; sym++) {
			ISymbol s = Symbol.findId(sym);
			ICode c=Code.NULL;
			if (s == null)
				s = Symbol.Empty;
			else 
				c= get(s);
			int len=CodeNumber.readCode(coding, binaryStdin).intValue();
			long codelavue = binaryStdin.readUnsignedLong(len);
			m.put(s, new Code(codelavue,len));
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
