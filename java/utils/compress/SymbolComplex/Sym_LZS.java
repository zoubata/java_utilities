package com.zoubworld.java.utils.compress.SymbolComplex;

import com.zoubworld.java.utils.compress.Code;
import com.zoubworld.java.utils.compress.CompositeSymbols;
import com.zoubworld.java.utils.compress.ICode;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;

public class Sym_LZS extends CompositeSymbols {

	private Sym_LZS(ISymbol mys1, ISymbol mys2) {
		super(mys1, mys2);

		// TODO Auto-generated constructor stub
	}
	public long getOffset() {

		return getS1().getId();
	}
	
	public long getLength() {

		return getS2().getId();
	}
	static ICode lenCode[] = { null, // 0
			null, // 1
			new Code(0L, 2), // 2
			new Code(1, 2), // 3
			new Code(2, 2), // 4
			new Code(12, 4), // 5
			new Code(13, 4), // 6
			new Code(14, 4), // 7
			new Code(0xF0, 8), // 8
			new Code(0xF1, 8), // 9
			new Code(0xF2, 8), // 10
			new Code(0xF3, 8), // 11
			new Code(0xF4, 8), // 12
			new Code(0xF5, 8), // 13
			new Code(0xF6, 8), // 14
			new Code(0xF7, 8), // 15
			new Code(0xF8, 8), // 16
			new Code(0xF9, 8), // 17
			new Code(0xFA, 8), // 18
			new Code(0xFB, 8), // 19
			new Code(0xFC, 8), // 20
			new Code(0xFD, 8), // 21
			new Code(0xFE, 8), // 22
			new Code(0xFF0, 12), // 23
			new Code(0xFF1, 12), // 24
			new Code(0xFF2, 12), // 25
			new Code(0xFF3, 12), // 26
			new Code(0xFF4, 12), // 27
			new Code(0xFF5, 12), // 28
			new Code(0xFF6, 12), // 29
			new Code(0xFF7, 12), // 30
			new Code(0xFF8, 12), // 31
			new Code(0xFF9, 12), // 32
			new Code(0xFFA, 12), // 33
			new Code(0xFFB, 12), // 34
			new Code(0xFFC, 12), // 35
			new Code(0xFFD, 12), // 36
			new Code(0xFFE, 12),// 37
			// new Code(0xFFF,12)//38

	};

	/**
	 * create a symbol where :
	 * 
	 * offset : -2047..-1 length : 2..37
	 * 
	 * offset= this.getS1().getId() length= this.getS2().getId() the bit stream is
	 * this.getCode() according to
	 * https://en.wikipedia.org/wiki/Lempel%E2%80%93Ziv%E2%80%93Stac
	 */
	public Sym_LZS(int offset, int length) {

		super(Symbol.LZS, new Symbol(offset, (offset > -128) ? new Code(-offset + 128, 8) : new Code(-offset, 12)),
				new Symbol(length, lenCode[length]));// 16 bit number coding : INT12+0Bxxxxxxxxxxxxxxxx);
		assert offset < 0;
		assert offset > -2048;
		assert length > 1;
		assert offset < 38;
		this.offset=offset;
		this.length=length;
	}
	int offset; int length;
	public String toString()
	{
		return "LZS("+offset+","+length+")";
	}

	protected Sym_LZS(Symbol s1, ISymbol s2, ISymbol s3) {
		super(s1,s2,s3);	
	}

}