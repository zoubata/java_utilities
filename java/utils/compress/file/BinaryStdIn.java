package com.zoubworld.java.utils.compress.file;
/******************************************************************************
 *  Compilation:  javac BinaryStdIn.java
 *  Execution:    java BinaryStdIn < input > output
 *  Dependencies: none             
 *  
 *  Supports reading binary data from standard input.
 *
 *  % java BinaryStdIn < input.jpg > output.jpg
 *  % diff input.jpg output.jpg
 *
 ******************************************************************************/

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.zoubworld.java.utils.compress.HuffmanCode;
import com.zoubworld.java.utils.compress.ICode;
import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;

/**
 *  <i>Binary standard input</i>. This class provides methods for reading
 *  in bits from standard input, either one bit at a time (as a {@code boolean}),
 *  8 bits at a time (as a {@code byte} or {@code char}),
 *  16 bits at a time (as a {@code short}), 32 bits at a time
 *  (as an {@code int} or {@code float}), or 64 bits at a time (as a
 *  {@code double} or {@code long}).
 *  <p>
 *  All primitive types are assumed to be represented using their 
 *  standard Java representations, in big-endian (most significant
 *  byte first) order.
 *  <p>
 *  The client should not intermix calls to {@code BinaryStdIn} with calls
 *  to {@code StdIn} or {@code System.in};
 *  otherwise unexpected behavior will result.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public  class BinaryStdIn {
    private  final int EOF = -1;      // end of file

    private  BufferedInputStream in;  // input stream
    private  int buffer;              // one character buffer
    private  int n;                   // number of bits left in buffer
    private  boolean isInitialized;   // has BinaryStdIn been called for first time?

    // don't instantiate
    public BinaryStdIn() { 
    	in = new BufferedInputStream(System.in);
    }

    public BinaryStdIn(InputStream in2) { 
    	in = new BufferedInputStream(in2);
    }
    public BinaryStdIn(File fout2) 
    {
    	try {
    		in=new BufferedInputStream(new FileInputStream(fout2));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	}
    public BinaryStdIn(String filename) 
    {
    	try {
    		File fout2=new File(filename);
    		in=new BufferedInputStream(new FileInputStream(fout2));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	}
    // fill buffer
    private  void initialize() {    	
        buffer = 0;
        n = 0;
        fillBuffer();
        isInitialized = true;
    }

    private  void fillBuffer() {
    	if(in==null)return;
    	try {
    		if (in.available()>0)
    		{
            buffer = in.read();
            n = 8;
            }
    		else
    		{
    			  buffer = EOF;
    	            n = -1;
            }
    		
        }
        catch (IOException e) {
            System.out.println("EOF");
            buffer = EOF;
            n = -1;
        }
    }

   /**
     * Close this input stream and release any associated system resources.
     */
    public  void close() {
        if (!isInitialized) initialize();
        try {
            in.close();
            isInitialized = false;
        }
        catch (IOException ioe) {
            throw new IllegalStateException("Could not close BinaryStdIn", ioe);
        }
    }

   /**
     * Returns true if standard input is empty.
     * @return true if and only if standard input is empty
     */
    public  boolean isEmpty() {
        if (!isInitialized) initialize();
        return buffer == EOF;
    }

   /**
     * Reads the next bit of data from standard input and return as a boolean.
     *
     * @return the next bit of data from standard input as a {@code boolean}
     * @throws NoSuchElementException if standard input is empty
     */
    public  Boolean readBoolean() {
        if (isEmpty()) 
        	return null;//throw new NoSuchElementException("Reading from empty input stream");
        n--;
        boolean bit = ((buffer >> n) & 1) == 1;
        if (n == 0) fillBuffer();
        return bit;
    }

   /**
     * Reads the next 8 bits from standard input and return as an 8-bit char.
     * Note that {@code char} is a 16-bit type;
     * to read the next 16 bits as a char, use {@code readChar(16)}.
     *
     * @return the next 8 bits of data from standard input as a {@code char}
     * @throws NoSuchElementException if there are fewer than 8 bits available on standard input
     */
    public  char readChar() {
        if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");

        // special case when aligned byte
        if (n == 8) {
            int x = buffer;
            fillBuffer();
            return (char) (x & 0xff);
        }

        // combine last n bits of current buffer with first 8-n bits of new buffer
        int x = buffer;
        x <<= (8 - n);
        int oldN = n;
        fillBuffer();
        if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");
        n = oldN;
        x |= (buffer >>> n);
        return (char) (x & 0xff);
        // the above code doesn't quite work for the last character if n = 8
        // because buffer will be -1, so there is a special case for aligned byte
    }
    public  ISymbol readSymbol()
    {
    	if(codingRule!=null)
			return codingRule.getSymbol(this);
    	return null;
    }
    public  ICode readCode()
    {    	
		ICode c=null;
		if(codingRule!=null)
			return codingRule.getCode(this);
		return c;    	
    }
    public  ISymbol readSymbol(HuffmanCode huff) {
    	return huff.decodeASymbol(this);
    }
   /**
     * Reads the next r bits from standard input and return as an r-bit character.
     *
     * @param  r number of bits to read.
     * @return the next r bits of data from standard input as a {@code char}
     * @throws NoSuchElementException if there are fewer than {@code r} bits available on standard input
     * @throws IllegalArgumentException unless {@code 1 <= r <= 16}
     */
    public  char readChar(int r) {
        if (r < 1 || r > 16) throw new IllegalArgumentException("Illegal value of r = " + r);

        // optimize r = 8 case
        if (r == 8) return readChar();

        char x = 0;
        for (int i = 0; i < r; i++) {
            x <<= 1;
            boolean bit = readBoolean();
            if (bit) x |= 1;
        }
        return x;
    }

ICodingRule codingRule=null;

/**
 * @return the codingRule
 */
public ICodingRule getCodingRule() {
	return codingRule;
}

/**
 * @param codingRule the codingRule to set
 */
public void setCodingRule(ICodingRule codingRule) {
	this.codingRule = codingRule;
}
   /**
     * Reads the remaining bytes of data from standard input and return as a string. 
     *
     * @return the remaining bytes of data from standard input as a {@code String}
     * @throws NoSuchElementException if standard input is empty or if the number of bits
     *         available on standard input is not a multiple of 8 (byte-aligned)
     */
    public  String readString() {
        if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");

        StringBuilder sb = new StringBuilder();
        char c= readChar();
        while (c!=0) {
             
            sb.append(c);
            if(isEmpty())
            	c=0;
            else
            c = readChar();
        }
        return sb.toString();
    }


   /**
     * Reads the next 16 bits from standard input and return as a 16-bit short.
     *
     * @return the next 16 bits of data from standard input as a {@code short}
     * @throws NoSuchElementException if there are fewer than 16 bits available on standard input
     */
    public  short readShort() {
        short x = 0;
        for (int i = 0; i < 2; i++) {
            char c = readChar();
            x <<= 8;
            x |= c;
        }
        return x;
    }

   /**
     * Reads the next 32 bits from standard input and return as a 32-bit int.
     *
     * @return the next 32 bits of data from standard input as a {@code int}
     * @throws NoSuchElementException if there are fewer than 32 bits available on standard input
     */
    public  int readInt() {
        int x = 0;
        for (int i = 0; i < 4; i++) {
            char c = readChar();
            x <<= 8;
            x |= c;
        }
        return x;
    }

   /**
     * Reads the next r bits from standard input and return as an r-bit int.
     *
     * @param  r number of bits to read.
     * @return the next r bits of data from standard input as a {@code int}
     * @throws NoSuchElementException if there are fewer than {@code r} bits available on standard input
     * @throws IllegalArgumentException unless {@code 1 <= r <= 32}
     */
    public  Integer readInt(int r) {
        if (r < 1 || r > 32) throw new IllegalArgumentException("Illegal value of r = " + r);

        // optimize r = 32 case
        if (r == 32) return readInt();

        int x = 0;
        for (int i = 0; i < r; i++) {
            x <<= 1;
            Boolean bit = readBoolean();
            if (bit==null) return null;
            if (bit) x |= 1;
        }
        return x;
    }
    /**
     * Reads the next r bits from standard input and return as an r-bit int.
     *
     * @param  r number of bits to read.
     * @return the next r bits of data from standard input as a {@code int}
     * @throws NoSuchElementException if there are fewer than {@code r} bits available on standard input
     * @throws IllegalArgumentException unless {@code 1 <= r <= 32}
     */
    public  long readLong(int r) {
        if (r < 1 || r > 64) throw new IllegalArgumentException("Illegal value of r = " + r);

        // optimize r = 32 case
        if (r == 32) 
        {
        	long l= readInt();
        	l=l&0xFFFFFFFFL;
        	return l;
        	}
        if (r == 64) return readLong();

        long x = 0;
        for (int i = 0; i < r; i++) {
            x <<= 1;
            boolean bit = readBoolean();
            if (bit) x |= 1;
        }
        return x;
    }

   /**
     * Reads the next 64 bits from standard input and return as a 64-bit long.
     *
     * @return the next 64 bits of data from standard input as a {@code long}
     * @throws NoSuchElementException if there are fewer than 64 bits available on standard input
     */
    public  long readLong() {
        long x = 0;
        for (int i = 0; i < 8; i++) {
            char c = readChar();
            x <<= 8;
            x |= c;
        }
        return x;
    }


   /**
     * Reads the next 64 bits from standard input and return as a 64-bit double.
     *
     * @return the next 64 bits of data from standard input as a {@code double}
     * @throws NoSuchElementException if there are fewer than 64 bits available on standard input
     */
    public  double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

   /**
     * Reads the next 32 bits from standard input and return as a 32-bit float.
     *
     * @return the next 32 bits of data from standard input as a {@code float}
     * @throws NoSuchElementException if there are fewer than 32 bits available on standard input
     */
    public  float readFloat() {
        return Float.intBitsToFloat(readInt());
    }


   /**
     * Reads the next 8 bits from standard input and return as an 8-bit byte.
     *
     * @return the next 8 bits of data from standard input as a {@code byte}
     * @throws NoSuchElementException if there are fewer than 8 bits available on standard input
     */
    public  byte readByte() {
        char c = readChar();
        return (byte) (c & 0xff);
    }
    
   /**
     * Test client. Reads in a binary input file from standard input and writes
     * it to standard output.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
    	BinaryStdIn i=new BinaryStdIn();

        BinaryStdOut o=new BinaryStdOut();
        // read one 8-bit char at a time
        while (!i.isEmpty()) {
            char c = i.readChar();
            o.write(c);
        }
        o.flush();
    }

public List<ISymbol> readSymbols() {
	List<ISymbol> ls=new ArrayList();
	ISymbol e=null;
	while((e=readSymbol())!=null)
		ls.add(e);
	return ls;
}
}

















