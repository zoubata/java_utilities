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

import org.apache.batik.ext.awt.image.spi.ImageWriter;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.file.IBinaryWriter;
import com.zoubworld.java.utils.compress.file.ISymbolWriter;


/**
 * Encodes symbols and writes to an arithmetic-coded bit stream. Not thread-safe.
 * @see ArithmeticDecoder
 */
public final class ArithmeticEncoder extends ArithmeticCoderBase implements ISymbolWriter{
	
	/*---- Fields ----*/
	
	// The underlying bit output stream (not null).
	private IBinaryWriter output;
	
	// Number of saved underflow bits. This value can grow without bound,
	// so a truly correct implementation would use a BigInteger.
	private int numUnderflow;
	
	
	
	/*---- Constructor ----*/
	
	/**
	 * Constructs an arithmetic coding encoder based on the specified bit output stream.
	 * @param numBits the number of bits for the arithmetic coding range
	 * @param out the bit output stream to write to
	 * @throws NullPointerException if the output stream is {@code null}
	 * @throws IllegalArgumentException if stateSize is outside the range [1, 62]
	 */
	public ArithmeticEncoder(int numBits) {
		super(numBits);
		numUnderflow = 0;
	}
	
	
	
	/*---- Methods ----*/
	
	/**
	 * Encodes the specified symbol based on the specified frequency table.
	 * This updates this arithmetic coder's state and may write out some bits.
	 * @param freqs the frequency table to use
	 * @param symbol the symbol to encode
	 * @throws NullPointerException if the frequency table is {@code null}
	 * @throws IllegalArgumentException if the symbol has zero frequency
	 * or the frequency table's total is too large
	 * @throws IOException if an I/O exception occurred
	 *
	public void write(IFrequencyTable freqs, int symbol) throws IOException {
		write(new CheckedFrequencyTable(freqs), symbol);
	}*/
	
	
	/**
	 * Encodes the specified symbol based on the specified frequency table.
	 * Also updates this arithmetic coder's state and may write out some bits.
	 * @param freqs the frequency table to use
	 * @param symbol the symbol to encode
	 * @throws NullPointerException if the frequency table is {@code null}
	 * @throws IllegalArgumentException if the symbol has zero frequency
	 * or the frequency table's total is too large
	 * @throws IOException if an I/O exception occurred
	 */
	public void write(IFrequencyTable freqs, int symbol) throws IOException {
		update(freqs, symbol);
	}
	
	
	/**
	 * Terminates the arithmetic coding by flushing any buffered bits, so that the output can be decoded properly.
	 * It is important that this method must be called at the end of the each encoding process.
	 * <p>Note that this method merely writes data to the underlying output stream but does not close it.</p>
	 * @throws IOException if an I/O exception occurred
	 */
	public void finish() throws IOException {
		output.writeBit(1);
	}
	
	
	protected void shift() throws IOException {
		int bit = (int)(low >>> (numStateBits - 1));
		output.writeBit(bit);
		
		// Write out the saved underflow bits
		for (; numUnderflow > 0; numUnderflow--)
			output.writeBit(bit ^ 1);
	}
	
	
	protected void underflow() {
		if (numUnderflow == Integer.MAX_VALUE)
			throw new ArithmeticException("Maximum underflow reached");
		numUnderflow++;
	}

	@Override
	public void writeCodingRule(IBinaryWriter binaryStdOut) {		
		binaryStdOut.write((int) freqs.getSymbolLimit());	
		for (int i = 0; i < freqs.getSymbolLimit(); i++)
			binaryStdOut.write((int) freqs.get(i));	
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

	IFrequencyTable freqs;
	@Override
	public void writeSymbol(IBinaryWriter binaryStdout, ISymbol sym) {
		output=binaryStdout;
		try {
			update(freqs, (int) sym.getId());
		//@todo	write additional data
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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