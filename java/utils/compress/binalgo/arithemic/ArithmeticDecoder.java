/* 
 * Reference arithmetic coding
 * Copyright (c) Project Nayuki
 * 
 * https://www.nayuki.io/page/reference-arithmetic-coding
 * https://github.com/nayuki/Reference-arithmetic-coding
 */
package com.zoubworld.java.utils.compress.binalgo.arithemic;


import java.io.IOException;
import java.util.Objects;

import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.file.IBinaryReader;
import com.zoubworld.java.utils.compress.file.ISymbolReader;

import sandbox.mit.SimpleFrequencyTable;


/**
 * Reads from an arithmetic-coded bit stream and decodes symbols. Not thread-safe.
 * @see ArithmeticEncoder
 */
public final class ArithmeticDecoder extends ArithmeticCoderBase implements ISymbolReader{
	
	/*---- Fields ----*/
	
	// The underlying bit input stream (not null).
	private IBinaryReader input;
	
	// The current raw code bits being buffered, which is always in the range [low, high].
	private long code=0L;
	
	
	
	/*---- Constructor ----*/
	
	/**
	 * Constructs an arithmetic coding decoder based on the
	 * specified bit input stream, and fills the code bits.
	 * @param numBits the number of bits for the arithmetic coding range
	 * @param in the bit input stream to read from
	 * @throws NullPointerException if the input steam is {@code null}
	 * @throws IllegalArgumentException if stateSize is outside the range [1, 62]
	 * @throws IOException if an I/O exception occurred
	 */
	public ArithmeticDecoder(int numBits)  {
		super(numBits);
		code = 0L;
		
	}
	public void init(IBinaryReader binaryStdIn) throws IOException
	{
	code=0L;
	input=binaryStdIn;
	for (int i = 0; i < numStateBits; i++)
		code = code << 1 | readCodeBit();
	}
	/*---- Methods ----*/
	
	/**
	 * Decodes the next symbol based on the specified frequency table and returns it.
	 * Also updates this arithmetic coder's state and may read in some bits.
	 * @param freqs the frequency table to use
	 * @return the next symbol
	 * @throws NullPointerException if the frequency table is {@code null}
	 * @throws IOException if an I/O exception occurred
	 *
	public int read(IFrequencyTable freqs) throws IOException {
		return read(new CheckedFrequencyTable(freqs));
	}
	
	
	/**
	 * Decodes the next symbol based on the specified frequency table and returns it.
	 * Also updates this arithmetic coder's state and may read in some bits.
	 * @param freqs the frequency table to use
	 * @return the next symbol
	 * @throws NullPointerException if the frequency table is {@code null}
	 * @throws IllegalArgumentException if the frequency table's total is too large
	 * @throws IOException if an I/O exception occurred
	 */
	public int read(IFrequencyTable freqs) throws IOException {
		// Translate from coding range scale to frequency table scale
		long total = freqs.getTotal();
		if (total > maximumTotal)
			throw new IllegalArgumentException("Cannot decode symbol because total is too large");
		long range = high - low + 1;
		long offset = code - low;
		long value = ((offset + 1) * total - 1) / range;
		if (value * range / total > offset)
			throw new AssertionError();
		if (value < 0 || value >= total)
			throw new AssertionError();
		
		// A kind of binary search. Find highest symbol such that freqs.getLow(symbol) <= value.
		int start = 0;
		int end = freqs.getSymbolLimit();
		while (end - start > 1) {
			int middle = (start + end) >>> 1;
			if (freqs.getLow(middle) > value)
				end = middle;
			else
				start = middle;
		}
		if (start + 1 != end)
			throw new AssertionError();
		
		int symbol = start;
		if (offset < freqs.getLow(symbol) * range / total || freqs.getHigh(symbol) * range / total <= offset)
			throw new AssertionError();
		update(freqs, symbol);
		if (code < low || code > high)
			throw new AssertionError("Code out of range");
		return symbol;
	}
	
	
	protected void shift() throws IOException {
		code = ((code << 1) & stateMask) | readCodeBit();
	}
	
	
	protected void underflow() throws IOException {
		code = (code & halfRange) | ((code << 1) & (stateMask >>> 1)) | readCodeBit();
	}
	
	
	// Returns the next bit (0 or 1) from the input stream. The end
	// of stream is treated as an infinite number of trailing zeros.
	private int readCodeBit() throws IOException {
		int temp = input.readBoolean()?1:0;
		if (temp == -1)
			temp = 0;
		return temp;
	}


IFrequencyTable freqs;
	@Override
	public ISymbol getSymbol(IBinaryReader binaryStdIn) {
		int isym=-1;
		try {
			isym = read( freqs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ISymbol sym=Symbol.findId(isym);
		sym=Symbol.decode(sym, binaryStdIn);
		return sym;
	}



	@Override
	public ICodingRule ReadCodingRule(IBinaryReader binaryStdin) {
		int nb=binaryStdin.readInt();
		int[] freq = new int[nb];
		for (int i = 0; i < nb; i++)
			freq[i] = binaryStdin.readInt();
		
		  freqs=new FrequencyTable(freq);
		  
		  return null;
	}
	public void setFrequencyTable( IFrequencyTable freqs)  {
		this.freqs=freqs;
	}
	
	/**
	 * @return the freqs
	 */
	public IFrequencyTable getFrequencyTable() {
		return freqs;
	}
	
}

/* original copyrigth & information :

from : https://github.com/nayuki/Reference-arithmetic-coding
licence : 
Reference arithmetic coding

This project is a clear implementation of arithmetic coding, suitable as a reference for educational purposes. It is provided separately in Java, Python, C++, and is open source.

The code can be used for study, and as a solid basis for modification and extension. Consequently, the codebase optimizes for readability and avoids fancy logic, and does not target the best speed/memory/performance.

Home page with detailed description: https://www.nayuki.io/page/reference-arithmetic-coding
License

Copyright © 2018 Project Nayuki. (MIT License)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

The Software is provided "as is", without warranty of any kind, express or implied, including but not limited to the warranties of merchantability, fitness for a particular purpose and noninfringement. In no event shall the authors or copyright holders be liable for any claim, damages or other liability, whether in an action of contract, tort or otherwise, arising from, out of or in connection with the Software or the use or other dealings in the Software.

*/