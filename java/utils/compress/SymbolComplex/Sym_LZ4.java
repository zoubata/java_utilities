package com.zoubworld.java.utils.compress.SymbolComplex;

import java.math.BigInteger;
import java.util.List;

import com.zoubworld.java.utils.compress.Code;
import com.zoubworld.java.utils.compress.CompositeSymbols;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.file.IBinaryReader;

/**
 * @author Pierre Valleau this symbol is used to reflect the LZ4 format.
 */
public class Sym_LZ4 extends CompositeSymbols {

	private Sym_LZ4(ISymbol mys1, ISymbol mys2) {
		super(mys1, mys2);

		// TODO Auto-generated constructor stub
	}

	/**
	 * build a bit stream according to
	 * https://github.com/lz4/lz4/blob/master/doc/lz4_Block_format.md
	 * https://en.wikipedia.org/wiki/LZ4_(compression_algorithm) and also store the
	 * data of LZ4 algo.
	 * 
	 * lenliterals=getS0().getId(); this is the number of the length of literals to
	 * follow matchlength==getS1().getId(); This is the length of the match copy
	 * operation offset=getS2().getId(); This is the possition of the match copy
	 * operation bit Stream :
	 * lenliterals[4]:matchlength[4](:offset[16])(:lenliterals[...])
	 */
	public Sym_LZ4(int lenliterals, int matchlength, int offset, List<ISymbol> litterals) {
		// https://github.com/lz4/lz4/blob/master/doc/lz4_Block_format.md
		super(new Symbol(lenliterals, new Code(Math.min(15, lenliterals), 4)), new Symbol(matchlength,
				new Code(Math.min(15, (matchlength == 0) ? 0 : (matchlength < 19) ? matchlength - 4 : 15), 4))

		);// 16 bit number coding : INT12+0Bxxxxxxxxxxxxxxxx);
		this.lenliterals = lenliterals;
		this.matchlength = matchlength;
		this.offset = offset;
		this.litterals = litterals;
		if (lenliterals >= 15)
			add(new Symbol(-1, Code.FactoryCode255(lenliterals - 15)));
		if (litterals != null)
			addAll(litterals);

		add(new Symbol(offset, Code.Factory((-offset), matchlength == 0 ? 0 : 16, false)));
		if (matchlength >= 19)
			add(new Symbol(-2, Code.FactoryCode255(matchlength - 18)));

		assert lenliterals < 16 + 255 * 256;
		assert lenliterals >= 0;
		if (litterals != null)
			assert lenliterals == litterals.size();
		if (matchlength != 0) {
			assert offset > -65536;
			assert offset <= 0;
		}
		assert matchlength >= 0 + 4;
		assert matchlength < 16 + 4 + 255 * 256;

	}

	int lenliterals = 0;
	int matchlength = 0;

	/**
	 * @return the lenliterals
	 */
	public int getLenliterals() {
		return lenliterals;
	}

	/**
	 * @return the matchlength
	 */
	public int getMatchlength() {
		return matchlength;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	int offset = 0;
	List<ISymbol> litterals = null;

	public static Sym_LZ4 read(IBinaryReader bin) {
		int lenliterals = (int) bin.readLong(4);
		int matchlength = (int) bin.readLong(4);
		int offset = 0;
		if (matchlength != 0)
			matchlength += 4;
		if (lenliterals == 15)
			lenliterals += Code.readCode255(bin);

		List<ISymbol> litterals = bin.readSymbols(lenliterals);

		if (matchlength != 0)
			offset = (int) bin.readLong(16, false);

		if (matchlength >= 19)
			matchlength += Code.readCode255(bin);
		return new Sym_LZ4(lenliterals, matchlength, offset, litterals);

	}
	/*
	 * static BigInteger f(int n) { if (n == 0) return new BigInteger("0"); if (n <=
	 * 2) return new BigInteger("1");
	 * 
	 * return f(n - 1).add(f(n - 2)); }
	 * 
	 * public static void main(String[] args) {
	 * 
	 * System.out.println("f(10)=" + f(10).toString()); System.out.println("f(16)="
	 * + f(16).toString()); // System.out.println("f(1000)=" + f(1000).toString());
	 * 
	 * }
	 */

	/**
	 * @return the litterals
	 */
	public List<ISymbol> getLitterals() {
		return litterals;
	}

}