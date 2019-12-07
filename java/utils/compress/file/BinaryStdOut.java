package com.zoubworld.java.utils.compress.file;
/******************************************************************************
 *  Compilation:  javac BinaryStdOut.java
 *  Execution:    java BinaryStdOut
 *  Dependencies: none
 *
 *  Write binary data to standard output, either one 1-bit boolean,
 *  one 8-bit char, one 32-bit int, one 64-bit double, one 32-bit float,
 *  or one 64-bit long at a time.
 *
 *  The bytes written are not aligned.
 *
 ******************************************************************************/

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import com.zoubworld.java.utils.compress.ICode;
import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;

/**
 *  <i>Binary standard output</i>. This class provides methods for converting
 *  primtive type variables ({@code boolean}, {@code byte}, {@code char},
 *  {@code int}, {@code long}, {@code float}, and {@code double})
 *  to sequences of bits and writing them to standard output.
 *  Uses big-endian (most-significant byte first).
 *  <p>
 *  The client must {@code flush()} the output stream when finished writing bits.
 *  <p>
 *  The client should not intermix calls to {@code BinaryStdOut} with calls
 *  to {@code StdOut} or {@code System.out}; otherwise unexpected behavior 
 *  will result.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public  class BinaryStdOut {
	private  BufferedOutputStream out;  // output stream (standard output)
    private  int buffer;                // 8-bit buffer of bits to write
    private  int n;                     // number of bits remaining in buffer
    private  boolean isInitialized;     // has BinaryStdOut been called for first time?

    // don't instantiate
    public BinaryStdOut(PrintStream out2) 
    { out=new BufferedOutputStream(out2);}
    
    public BinaryStdOut(File fout2) 
    {
    	try {
			out=new BufferedOutputStream(new FileOutputStream(fout2));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	}
    public BinaryStdOut(String filename) 
    {
    	try {
			out=new BufferedOutputStream(new FileOutputStream(new File(filename)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	}
    
    public BinaryStdOut() 
    { out=new BufferedOutputStream(System.out);}

    // initialize BinaryStdOut
    private  void initialize() {
    	try {
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        buffer = 0;
        n = 0;
        isInitialized = true;
    }

   /**
     * Writes the specified bit to standard output.
     */
    private  void writeBit(boolean bit) {
        if (!isInitialized) initialize();

        // add bit to buffer
        buffer <<= 1;
        if (bit) buffer |= 1;

        // if buffer is full (8 bits), write out as a single byte
        n++;
        if (n == 8) clearBuffer();
    } 

   /**
     * Writes the 8-bit byte to standard output.
     */
    private  void writeByte(int x) {
        if (!isInitialized) initialize();

        assert x >= 0 && x < 256;

        // optimized if byte-aligned
        if (n == 0) {
            try {
                out.write(x);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        // otherwise write one bit at a time
        for (int i = 0; i < 8; i++) {
            boolean bit = ((x >>> (8 - i - 1)) & 1) == 1;
            writeBit(bit);
        }
    }

    // write out any remaining bits in buffer to standard output, padding with 0s
    private  void clearBuffer() {
        if (!isInitialized) initialize();

        if (n == 0) return;
        if (n > 0) buffer <<= (8 - n);
        try {
            out.write(buffer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        n = 0;
        buffer = 0;
    }

   /**
     * Flushes standard output, padding 0s if number of bits written so far
     * is not a multiple of 8.
     */
    public  void flush() {
        clearBuffer();
        try {
            out.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

   /**
     * Flushes and closes standard output. Once standard output is closed, you can no
     * longer write bits to it.
     */
    public  void close() {
        flush();
        try {
            out.close();
            isInitialized = false;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


   /**
     * Writes the specified bit to standard output.
     * @param x the {@code boolean} to write.
     */
    public  void write(boolean x) {
        writeBit(x);
    } 

   /**
     * Writes the 8-bit byte to standard output.
     * @param x the {@code byte} to write.
     */
    public  void write(byte x) {
        writeByte(x & 0xff);
    }

   /**
     * Writes the 32-bit int to standard output.
     * @param x the {@code int} to write.
     */
    public  void write(int x) {
        writeByte((x >>> 24) & 0xff);
        writeByte((x >>> 16) & 0xff);
        writeByte((x >>>  8) & 0xff);
        writeByte((x >>>  0) & 0xff);
    }

   /**
     * Writes the r-bit int to standard output.
     * @param x the {@code int} to write.
     * @param r the number of relevant bits in the char.
     * @throws IllegalArgumentException if {@code r} is not between 1 and 32.
     * @throws IllegalArgumentException if {@code x} is not between 0 and 2<sup>r</sup> - 1.
     */
    public  void write(int x, int r) {
        if (r == 32) {
            write(x);
            return;
        }
        if (r < 1 || r > 32)        throw new IllegalArgumentException("Illegal value for r = " + r);
 //       if (x < 0 || x >= (1 << r)) throw new IllegalArgumentException("Illegal " + r + "-bit char = " + x);
        for (int i = 0; i < r; i++) {
            boolean bit = ((x >>> (r - i - 1)) & 1) == 1;
            writeBit(bit);
        }
    }



    public  void write(long x, int r) {
    	if (r == 64) {    		
    		write(x);
            return;
        }
    	if (r == 32) {
            write((int)x);
            return;
        }
        
        if (r < 1 || r > 64)        throw new IllegalArgumentException("Illegal value for r = " + r);
 /*       if ( x > (1L << r)) throw new IllegalArgumentException("Illegal " + r + "-bit long = " + x);
        if (x < -(1L << r) ) throw new IllegalArgumentException("Illegal " + r + "-bit negatif long = " + x);
  */      for (int i = 0; i < r; i++) {
            boolean bit = ((x >>> (r - i - 1)) & 1) == 1;
            writeBit(bit);
        }
    }




   /**
     * Writes the 64-bit double to standard output.
     * @param x the {@code double} to write.
     */
    public  void write(double x) {
        write(Double.doubleToRawLongBits(x));
    }

   /**
     * Writes the 64-bit long to standard output.
     * @param x the {@code long} to write.
     */
    public  void write(long x) {
        writeByte((int) ((x >>> 56) & 0xff));
        writeByte((int) ((x >>> 48) & 0xff));
        writeByte((int) ((x >>> 40) & 0xff));
        writeByte((int) ((x >>> 32) & 0xff));
        writeByte((int) ((x >>> 24) & 0xff));
        writeByte((int) ((x >>> 16) & 0xff));
        writeByte((int) ((x >>>  8) & 0xff));
        writeByte((int) ((x >>>  0) & 0xff));
    }

   /**
     * Writes the 32-bit float to standard output.
     * @param x the {@code float} to write.
     */
    public  void write(float x) {
        write(Float.floatToRawIntBits(x));
    }

   /**
     * Writes the 16-bit int to standard output.
     * @param x the {@code short} to write.
     */
    public  void write(short x) {
        writeByte((x >>>  8) & 0xff);
        writeByte((x >>>  0) & 0xff);
    }

   /**
     * Writes the 8-bit char to standard output.
     * @param x the {@code char} to write.
     * @throws IllegalArgumentException if {@code x} is not betwen 0 and 255.
     */
    public  void write(char x) {
        if (x < 0 || x >= 256) throw new IllegalArgumentException("Illegal 8-bit char = " + x);
        writeByte(x);
    }

   /**
     * Writes the r-bit char to standard output.
     * @param x the {@code char} to write.
     * @param r the number of relevant bits in the char.
     * @throws IllegalArgumentException if {@code r} is not between 1 and 16.
     * @throws IllegalArgumentException if {@code x} is not between 0 and 2<sup>r</sup> - 1.
     */
    public  void write(char x, int r) {
        if (r == 8) {
            write(x);
            return;
        }
        if (r < 1 || r > 16) throw new IllegalArgumentException("Illegal value for r = " + r);
        if (x >= (1 << r))   throw new IllegalArgumentException("Illegal " + r + "-bit char = " + x);
        for (int i = 0; i < r; i++) {
            boolean bit = ((x >>> (r - i - 1)) & 1) == 1;
            writeBit(bit);
        }
    }

   /**
     * Writes the string of 8-bit characters to standard output.
     * @param s the {@code String} to write.
     * @throws IllegalArgumentException if any character in the string is not
     * between 0 and 255.
     */
    public  void write(String s) {
        for (int i = 0; i < s.length(); i++)
            write(s.charAt(i));
        write((char)0);
        
    }

   /**
     * Writes the string of r-bit characters to standard output.
     * @param s the {@code String} to write.
     * @param r the number of relevants bits in each character.
     * @throws IllegalArgumentException if r is not between 1 and 16.
     * @throws IllegalArgumentException if any character in the string is not
     * between 0 and 2<sup>r</sup> - 1.
     */
    public  void write(String s, int r) {
        for (int i = 0; i < s.length(); i++)
            write(s.charAt(i), r);
        write((char)0, r);
        
    }

   /**
     * Tests the methods in this class.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        int m = Integer.parseInt(args[0]);
        BinaryStdOut o=new BinaryStdOut();
        // write n integers to binary standard output
        for (int i = 0; i < m; i++) {
            o.write(i);
        }
        o.flush();
    }

public void write(ISymbol sym) {
	if(sym==null)
		return;
	if(codingRule==null)
		write(sym.getCode());
	else
		write(codingRule.get(sym));
		
}
public void writes(List<ISymbol> ls) {
	if(ls==null)
		return ;
	if(codingRule==null)
	for(ISymbol sym:ls)
	write(sym.getCode());	
	else
		for(ISymbol sym:ls)
			write(codingRule.get(sym));	
		
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

public void write(List<ICode> lc)
{
	if(lc==null)
		return;
	for(ICode c:lc)
		write(c);
	}
public void write(ICode code) {
	if(code==null)
		return;
	if(code.length()<=64)
	write(code.getLong(),code.length());
	else
		new Exception("code too long");
}

}
