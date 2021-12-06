/**
 * 
 */
package com.zoubworld.java.utils.compress.SymbolComplex;

import java.math.BigInteger;

import com.zoubworld.java.utils.compress.Code;
import com.zoubworld.java.utils.compress.CompositeCodes;
import com.zoubworld.java.utils.compress.CompositeSymbol;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.file.IBinaryReader;

/**
 * @author zoubata 8 hours
 *
 */
public class SymbolBigINT extends SymbolINT {

	/**
	 * @param mys1
	 * @param mys2
	 */
	private SymbolBigINT(ISymbol mys1, ISymbol mys2) {
		super(mys1, mys2);
	}

	public SymbolBigINT(ISymbol mys2) {
		super(Symbol.BigINTn, mys2);
	}

	@Override
	public String toString() {
		return (getS1().toString() + "(" + getS2().getId() + ")");
	}

	public SymbolBigINT(BigInteger i) {
		
		super(Symbol.BigINTn, new Symbol(i));
		assert i.bitLength()<65535*8;
		getS2().setCode(new CompositeCodes(new Code(i.toByteArray().length,16),new Code( i)));
		}

	public SymbolBigINT(IBinaryReader binaryStdIn) {
		super(Symbol.BigINTn,null);
		int l=binaryStdIn.readSignedInt(16);
		BigInteger i = new BigInteger(binaryStdIn.readBytes(l));
		this.s2=new Symbol(i);
			getS2().setCode(new CompositeCodes(new Code(i.toByteArray().length,16),new Code( i)));
	}
}