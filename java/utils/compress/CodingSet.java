/**
 * 
 */
package com.zoubworld.java.utils.compress;

import java.util.List;
import java.util.Map;

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
	public final static long UNCOMPRESS = 0;
	/**
	 * flat coding including internal/extra/special symbols : i->i for
	 * i=[0..255,256...] the coding is 9bits fix length(today,perhaps later it will
	 * be 10bits)
	 */
	public final static long NOCOMPRESS = 1;
	/**
	 * flat coding including internal symbol : i->i for i=[0..255,256...] the coding
	 * is 16bits fix length it is used for debug reading only
	 */
	public final static long NOCOMPRESS16 = 2;
	/**
	 * flat coding including internal symbol : i->i for i=[0..255,256...] the coding
	 * is 32bits fix length it is used for debug reading only interesting only for
	 * debug
	 */
	public final static long NOCOMPRESS32 = 3;
	/**
	 * exponential coding, this code is used to play with bit stream entropy 1->0b0
	 * 2->0b10 3->0b100 i->0b1 i*'0' 255->0b1000...000 (254*'0') 0->0b1000...000
	 * (255*'0')
	 * 
	 */
	public final static long COMPRESS01TO1x0 = 4;
	/**
	 * https://en.wikipedia.org/wiki/Unary_coding
	 */
	public final static long UnaryCode = 5;
	/**
	 * https://en.wikipedia.org/wiki/Varicode
	 * used hitoricaly for PSK31 and ascii table.
	 * warning a lot of symbol is missing.
	 * */
	public final static long VariCode = 6;
	/**
	 * no coding define
	 */
	public final static Long UNDEFINED = -1L;
	// Map<ISymbol,ICode> m;
	BidiMap<ISymbol, ICode> m;
	void buildVariCode()
	{
		m = new DualHashBidiMap<>();
		//Control characters
		m.put(sprout.Factory(0x0L), new Code("1010101011"));
		m.put(sprout.Factory(0x1L), new Code("1011011011"));
		m.put(sprout.Factory(0x2L), new Code("1011101101"));
		m.put(sprout.Factory(0x3L), new Code("1101110111"));
		m.put(sprout.Factory(0x4L), new Code("1011101011"));
		m.put(sprout.Factory(0x5L), new Code("1101011111"));
		m.put(sprout.Factory(0x6L), new Code("1011101111"));
		m.put(sprout.Factory(0x7L), new Code("1011111101"));
		m.put(sprout.Factory(0x8L), new Code("1011111111"));
		m.put(sprout.Factory(0x9L), new Code("11101111"));
		m.put(sprout.Factory(0x0AL), new Code("11101"));
		m.put(sprout.Factory(0x0BL), new Code("1101101111"));
		m.put(sprout.Factory(0x0CL), new Code("1011011101"));
		m.put(sprout.Factory(0x0DL), new Code("11111"));
		m.put(sprout.Factory(0x0EL), new Code("1101110101"));
		m.put(sprout.Factory(0x0FL), new Code("1110101011"));
		m.put(sprout.Factory(0x10L), new Code("1011110111"));
		m.put(sprout.Factory(0x11L), new Code("1011110101"));
		m.put(sprout.Factory(0x12L), new Code("1110101101"));
		m.put(sprout.Factory(0x13L), new Code("1110101111"));
		m.put(sprout.Factory(0x14L), new Code("1101011011"));
		m.put(sprout.Factory(0x15L), new Code("1101101011"));
		m.put(sprout.Factory(0x16L), new Code("1101101101"));
		m.put(sprout.Factory(0x17L), new Code("1101010111"));
		m.put(sprout.Factory(0x18L), new Code("1101111011"));
		m.put(sprout.Factory(0x19L), new Code("1101111101"));
		m.put(sprout.Factory(0x1AL), new Code("1110110111"));
		m.put(sprout.Factory(0x1BL), new Code("1101010101"));
		m.put(sprout.Factory(0x1CL), new Code("1101011101"));
		m.put(sprout.Factory(0x1DL), new Code("1110111011"));
		m.put(sprout.Factory(0x1EL), new Code("1011111011"));
		m.put(sprout.Factory(0x1FL), new Code("1101111111"));
		m.put(sprout.Factory(0x7FL), new Code("1110110101"));
		//Printable characters
		m.put(sprout.Factory(0x20L), new Code("1"));
		m.put(sprout.Factory(0x21L), new Code("111111111"));
		m.put(sprout.Factory(0x22L), new Code("101011111"));
		m.put(sprout.Factory(0x23L), new Code("111110101"));
		m.put(sprout.Factory(0x24L), new Code("111011011"));
		m.put(sprout.Factory(0x25L), new Code("1011010101"));
		m.put(sprout.Factory(0x26L), new Code("1010111011"));
		m.put(sprout.Factory(0x27L), new Code("101111111"));
		m.put(sprout.Factory(0x28L), new Code("11111011"));
		m.put(sprout.Factory(0x29L), new Code("11110111"));
		m.put(sprout.Factory(0x2AL), new Code("101101111"));
		m.put(sprout.Factory(0x2BL), new Code("111011111"));
		m.put(sprout.Factory(0x2CL), new Code("1110101"));
		m.put(sprout.Factory(0x2DL), new Code("110101"));
		m.put(sprout.Factory(0x2EL), new Code("1010111"));
		m.put(sprout.Factory(0x2FL), new Code("110101111"));
		m.put(sprout.Factory(0x30L), new Code("10110111"));
		m.put(sprout.Factory(0x31L), new Code("10111101"));
		m.put(sprout.Factory(0x32L), new Code("11101101"));
		m.put(sprout.Factory(0x33L), new Code("11111111"));
		m.put(sprout.Factory(0x34L), new Code("101110111"));
		m.put(sprout.Factory(0x35L), new Code("101011011"));
		m.put(sprout.Factory(0x36L), new Code("101101011"));
		m.put(sprout.Factory(0x37L), new Code("110101101"));
		m.put(sprout.Factory(0x38L), new Code("110101011"));
		m.put(sprout.Factory(0x39L), new Code("110110111"));
		m.put(sprout.Factory(0x3AL), new Code("11110101"));
		m.put(sprout.Factory(0x3BL), new Code("110111101"));
		m.put(sprout.Factory(0x3CL), new Code("111101101"));
		m.put(sprout.Factory(0x3DL), new Code("1010101"));
		m.put(sprout.Factory(0x3EL), new Code("111010111"));
		m.put(sprout.Factory(0x3FL), new Code("1010101111"));
		m.put(sprout.Factory(0x40L), new Code("1010111101"));
		m.put(sprout.Factory(0x41L), new Code("1111101"));
		m.put(sprout.Factory(0x42L), new Code("11101011"));
		m.put(sprout.Factory(0x43L), new Code("10101101"));
		m.put(sprout.Factory(0x44L), new Code("10110101"));
		m.put(sprout.Factory(0x45L), new Code("1110111"));
		m.put(sprout.Factory(0x46L), new Code("11011011"));
		m.put(sprout.Factory(0x47L), new Code("11111101"));
		m.put(sprout.Factory(0x48L), new Code("101010101"));
		m.put(sprout.Factory(0x49L), new Code("1111111"));
		m.put(sprout.Factory(0x4AL), new Code("111111101"));
		m.put(sprout.Factory(0x4BL), new Code("101111101"));
		m.put(sprout.Factory(0x4CL), new Code("11010111"));
		m.put(sprout.Factory(0x4DL), new Code("10111011"));
		m.put(sprout.Factory(0x4EL), new Code("11011101"));
		m.put(sprout.Factory(0x4FL), new Code("10101011"));
		m.put(sprout.Factory(0x50L), new Code("11010101"));
		m.put(sprout.Factory(0x51L), new Code("111011101"));
		m.put(sprout.Factory(0x52L), new Code("10101111"));
		m.put(sprout.Factory(0x53L), new Code("1101111"));
		m.put(sprout.Factory(0x54L), new Code("1101101"));
		m.put(sprout.Factory(0x55L), new Code("101010111"));
		m.put(sprout.Factory(0x56L), new Code("110110101"));
		m.put(sprout.Factory(0x57L), new Code("101011101"));
		m.put(sprout.Factory(0x58L), new Code("101110101"));
		m.put(sprout.Factory(0x59L), new Code("101111011"));
		m.put(sprout.Factory(0x5AL), new Code("1010101101"));
		m.put(sprout.Factory(0x5BL), new Code("111110111"));
		m.put(sprout.Factory(0x5CL), new Code("111101111"));
		m.put(sprout.Factory(0x5DL), new Code("111111011"));
		m.put(sprout.Factory(0x5EL), new Code("1010111111"));
		m.put(sprout.Factory(0x5FL), new Code("101101101"));
		m.put(sprout.Factory(0x60L), new Code("1011011111"));
		m.put(sprout.Factory(0x61L), new Code("1011"));
		m.put(sprout.Factory(0x62L), new Code("1011111"));
		m.put(sprout.Factory(0x63L), new Code("101111"));
		m.put(sprout.Factory(0x64L), new Code("101101"));
		m.put(sprout.Factory(0x65L), new Code("11"));
		m.put(sprout.Factory(0x66L), new Code("111101"));
		m.put(sprout.Factory(0x67L), new Code("1011011"));
		m.put(sprout.Factory(0x68L), new Code("101011"));
		m.put(sprout.Factory(0x69L), new Code("1101"));
		m.put(sprout.Factory(0x6AL), new Code("111101011"));
		m.put(sprout.Factory(0x6BL), new Code("10111111"));
		m.put(sprout.Factory(0x6CL), new Code("11011"));
		m.put(sprout.Factory(0x6DL), new Code("111011"));
		m.put(sprout.Factory(0x6EL), new Code("1111"));
		m.put(sprout.Factory(0x6FL), new Code("111"));
		m.put(sprout.Factory(0x70L), new Code("111111"));
		m.put(sprout.Factory(0x71L), new Code("110111111"));
		m.put(sprout.Factory(0x72L), new Code("10101"));
		m.put(sprout.Factory(0x73L), new Code("10111"));
		m.put(sprout.Factory(0x74L), new Code("101"));
		m.put(sprout.Factory(0x75L), new Code("110111"));
		m.put(sprout.Factory(0x76L), new Code("1111011"));
		m.put(sprout.Factory(0x77L), new Code("1101011"));
		m.put(sprout.Factory(0x78L), new Code("11011111"));
		m.put(sprout.Factory(0x79L), new Code("1011101"));
		m.put(sprout.Factory(0x7AL), new Code("111010101"));
		m.put(sprout.Factory(0x7BL), new Code("1010110111"));
		m.put(sprout.Factory(0x7CL), new Code("110111011"));
		m.put(sprout.Factory(0x7DL), new Code("1010110101"));
		m.put(sprout.Factory(0x7EL), new Code("1011010111"));

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
		ICode code = m.get(sym);
		if(code==null)
		if (CompositeSymbol.class.isInstance(sym)) {
			CompositeSymbol cs = (CompositeSymbol) sym;
			ISymbol sa = cs.getS1();
			ICode a = get(sa);
			ISymbol sb = cs.getS2();
			ICode b = sb.getCode();
			// ICode code=new CompositeCode(a,b);
			 
			sa.setCode(a);
			sb.setCode(b);

			CompositeCode cc = new CompositeCode(cs);

			cs.setCode(cc);

			// ICode code=new CompositeCode(cs);
			// cs.setCode(code);
			 code = cs.getCode();
			
			
			return code;

		}

		
		if (code==null)
			code=m.get(sprout.Factory((long)sym.getId()));
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
	private long parameter;
	
	public CodingSet(List<ISymbol> ls) {
		 Map<ISymbol, Long> m = ISymbol.Freq(ls);
		 getConfig(m);
	}
	public CodingSet(Map<ISymbol, Long> m) {
		getConfig(m);
	}
	/** specify the len of the coding, an encode the symbol.
	 * */
	private void getConfig(Map<ISymbol, Long> m) {
		long s=m.size();
		for(ISymbol e:m.keySet())
			s=Math.max(s,e.getId());
		if (s<=256)
		{parameter = UNCOMPRESS;len = 8; buildcodes();}
	else
		if (s<=512)
		{parameter = NOCOMPRESS;len = 9; buildcodes();}
	else
		if (s<=512)
		{parameter = NOCOMPRESS16;len = 16; buildcodes();}
		else
		
			{parameter = NOCOMPRESS32;len = 32; buildcodes();}
	}
	/**
	 * 
	 */
	public CodingSet(Long method) {
		if (method==null)
			method=UNDEFINED;
		this.parameter=method;
		// m=new HashMap();
		m = new DualHashBidiMap<>();
	
		
		
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
		if(method!=UNDEFINED)
		buildcodes();


	}
	private void buildcodes() {
		
		if (len != 0) {
			for (char c = 0; c < 256; c++)
				m.put(Symbol.findId(c), new Code(c, len));

			/*
			 * for (short c=256;c<Symbol.getNbSymbol();c++) m.put(new Symbol(c), new Code(
			 * c));
			 */

			if (parameter != UNCOMPRESS) {

				for (short c = 256; c < Symbol.getNbSymbol(); c++)
					if (Symbol.findId(c) != null)
						m.put(Symbol.findId(c), new Code(c, len));
			}
		
		if (parameter == COMPRESS01TO1x0) {

			for (char c = 2; c < 256; c++)
				m.put(Symbol.findId(c), new Code("1" + StringUtils.repeat("0", c - 1)));
			m.put(Symbol.findId(0), new Code("1" + StringUtils.repeat("0", 255)));
			m.put(Symbol.findId(1), new Code("1"));


				for (char c = 0; c < 256; c++)
					m.put(Symbol.findId(c), new Code(StringUtils.repeat("1", c)+"0"));				
			}
		}
		else
		if (parameter == VariCode) {
			buildVariCode();
		}
			
		

		 else if (parameter == UnaryCode) {

			for (char c = 0; c < 256; c++)
				m.put(Symbol.findId(c), new Code(StringUtils.repeat("1", c) + "0"));
		}
		
	}
	public Long getParam( ) 
	{
		return (long)parameter;
	}
	public CodingSet(int nbsym, int nbBit, IBinaryReader binaryStdin) {
		m = new DualHashBidiMap<>();
		len = nbBit;
		for (int sym = 0; sym < nbsym; sym++) {
			long code = binaryStdin.readSignedLong(nbBit);

			m.put(sprout.Factory((long)sym), new Code(code, nbBit));
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
							 * Code c = new Code(b); c.setSymbol(sprout.Factory(b));
							 */
		ISymbol s = sprout.Factory((long)b.longValue());
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

			c= cc;

		}
		if (c.getSymbol().equals(Symbol.CodingSet)
				||
				c.getSymbol().equals(Symbol.HUFFMAN))
		{	
			ICodingRule cr = ICodingRule.ReadCodingRule(c.getSymbol(),binaryStdIn);
			binaryStdIn.setCodingRule(cr);
			return cr.getCode(binaryStdIn);
		}
		return c;
	}

	public ICode getGenericCode(IBinaryReader binaryStdIn) {
		int b = binaryStdIn.readUnsignedInt(len);
		/*
		 * Code c = new Code(b); c.setSymbol(sprout.Factory(b));
		 */
		ISymbol s = sprout.Factory((long)b);
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
	/** to encode an array of code/symbol
	 * Nb Symbol (FibonacciCoding)
	 * coding    (FibonacciCoding)
	 * array[Nb]={(len,code),....}
	 * */
	public void writeCodingRule2(int coding,IBinaryWriter binaryStdOut) {
		int nbSym = 0;
		//int coding=CodeNumber.Golomb2Coding;
		nbSym = m.keySet().size();
		binaryStdOut.write(CodeNumber.getCode(CodeNumber.FibonacciCoding, nbSym));
		binaryStdOut.write(CodeNumber.getCode(CodeNumber.FibonacciCoding, coding));
		for (int sym = 0; sym < nbSym; sym++) {
			ISymbol s = sprout.Factory((long)sym);
			ICode c=Code.NULL;
			if (s == null)
				s = Symbol.Empty;
			else 
				c= get(s);
			binaryStdOut.write(CodeNumber.getCode(coding, c.length()) );
			binaryStdOut.write(c.getLong(), c.length());
		}
	}
	
	
	/** to encode a set of code/symbol
	 * Nb Symbol (FibonacciCoding)
	 * coding    (FibonacciCoding)
	 * set[Nb]={(symbolId,len,code),....}
	 * */
	public void writeCodingRule3(int coding,IBinaryWriter binaryStdOut) {
		int nbSym = 0;
		//int coding=CodeNumber.Golomb2Coding;
		nbSym = m.keySet().size();
		binaryStdOut.write(CodeNumber.getCode(CodeNumber.FibonacciCoding, nbSym));
		binaryStdOut.write(CodeNumber.getCode(CodeNumber.FibonacciCoding, coding));
		for (ISymbol s:m.keySet()) {			
			ICode c=Code.NULL;
			if (s == null)
				s = Symbol.Empty;
			else 
				c= get(s);
			binaryStdOut.write(CodeNumber.getCode(coding, s.getId()) );			
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
			ISymbol s = sprout.Factory((long)sym);
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
	
	ISymbol sprout=new Symbol();
	@Override
	public void setSprout(ISymbol sprout) {
		this.sprout=sprout;
		
	}
	public void flush() {
		m.clear();		
	}
}

