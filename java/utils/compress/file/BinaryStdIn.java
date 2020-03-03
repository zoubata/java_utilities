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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
public  class BinaryStdIn implements IBinaryReader {
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
        isInitialized = true;
    }

    private  void fillBuffer() {
    	if(in==null)return;
    	try {
    		if (!isInitialized) initialize();
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

   /* (non-Javadoc)
 * @see com.zoubworld.java.utils.compress.file.IBinaryReader#close()
 */
    @Override
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

   /* (non-Javadoc)
 * @see com.zoubworld.java.utils.compress.file.IBinaryReader#isEmpty()
 */
    @Override
	public  boolean isEmpty() {
        if (!isInitialized) 
        	{
        	initialize();
        	fillBuffer();
        	}
        return buffer == EOF;
    }

   /* (non-Javadoc)
 * @see com.zoubworld.java.utils.compress.file.IBinaryReader#readBoolean()
 */
    @Override
	public  Boolean readBoolean() {
        if (isEmpty()) 
        	return null;//throw new NoSuchElementException("Reading from empty input stream");
        if (n == 0) fillBuffer();
        n--;
        boolean bit = ((buffer >> n) & 1) == 1;
      
        return bit;
    }

   /* (non-Javadoc)
 * @see com.zoubworld.java.utils.compress.file.IBinaryReader#readChar()
 */
    @Override
	public  char readChar() {
    	 int x = 0;
         for (int i = 0; i < 2; i++) {
             int c = ((int)readByte() & 0xff);;
             x <<= 8;
             x |= c;
         }
         return (char)x; }
    /* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.file.IBinaryReader#readSymbol()
	 */
    @Override
	public  ISymbol readSymbol()
    {
    	if(codingRule!=null)
			return codingRule.getSymbol(this);
    	return null;
    }
    /* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.file.IBinaryReader#readCode()
	 */
    @Override
	public  ICode readCode()
    {    	
		ICode c=null;
		if(codingRule!=null)
			return codingRule.getCode(this);
		return c;    	
    }
    /* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.file.IBinaryReader#readSymbol(com.zoubworld.java.utils.compress.HuffmanCode)
	 */
    @Override
	public  ISymbol readSymbol(HuffmanCode huff) {
    	return huff.decodeASymbol(this);
    }
   /* (non-Javadoc)
 * @see com.zoubworld.java.utils.compress.file.IBinaryReader#readChar(int)
 */
    @Override
	public  char readChar(int r) {
        if (r < 1 || r > 16) throw new IllegalArgumentException("Illegal value of r = " + r);

        // optimize r = 8 case
        if (r == 8) return ((char)(readByte()&0xff));

        char x = 0;
        for (int i = 0; i < r; i++) {
            x <<= 1;
            boolean bit = readBoolean();
            if (bit) 
            	x |= 1;
        }
        return x;
    }

ICodingRule codingRule=null;

/* (non-Javadoc)
 * @see com.zoubworld.java.utils.compress.file.IBinaryReader#getCodingRule()
 */
@Override
public ICodingRule getCodingRule() {
	return codingRule;
}

/* (non-Javadoc)
 * @see com.zoubworld.java.utils.compress.file.IBinaryReader#setCodingRule(com.zoubworld.java.utils.compress.ICodingRule)
 */
@Override
public void setCodingRule(ICodingRule codingRule) {
	this.codingRule = codingRule;
}
   /* (non-Javadoc)
 * @see com.zoubworld.java.utils.compress.file.IBinaryReader#readString()
 */
    @Override
	public  String readString() {
        if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");

        StringBuilder sb = new StringBuilder();
       char c= (char)(readByte());
        while (c!=0) {
             
            sb.append(c);
            if(isEmpty())
            	c=0;
            else
            c = (char)readByte();
        }
        return sb.toString();
    }


   /* (non-Javadoc)
 * @see com.zoubworld.java.utils.compress.file.IBinaryReader#readShort()
 */
    @Override
	public  short readShort() {
        short x = 0;
        for (int i = 0; i < 2; i++) {
            int c = ((int)readByte() & 0xff);
            x <<= 8;
            x |= c;
        }
        return x;
    }

   /* (non-Javadoc)
 * @see com.zoubworld.java.utils.compress.file.IBinaryReader#readInt()
 */
    @Override
	public  int readInt() {
        int x = 0;
        for (int i = 0; i < 4; i++) {
            int c = ((int)readByte() & 0xff);;
            x <<= 8;
            x |= c;
        }
        return x;
    }

   /* (non-Javadoc)
 * @see com.zoubworld.java.utils.compress.file.IBinaryReader#readInt(int)
 */
    @Override
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
    /* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.file.IBinaryReader#readLong(int)
	 */
    @Override
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

   /* (non-Javadoc)
 * @see com.zoubworld.java.utils.compress.file.IBinaryReader#readLong()
 */
    @Override
	public  long readLong() {
        long x = 0;
        for (int i = 0; i < 8; i++) {
            int c = ((int)readByte() & 0xff);;
            x <<= 8;
            x |= c;
        }
        return x;
    }


   /* (non-Javadoc)
 * @see com.zoubworld.java.utils.compress.file.IBinaryReader#readDouble()
 */
    @Override
	public  double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

   /* (non-Javadoc)
 * @see com.zoubworld.java.utils.compress.file.IBinaryReader#readFloat()
 */
    @Override
	public  float readFloat() {
        return Float.intBitsToFloat(readInt());
    }


   /* (non-Javadoc)
 * @see com.zoubworld.java.utils.compress.file.IBinaryReader#readByte()
 */
    @Override
	public  byte readByte() {

        // special case when aligned byte
        if (n == 8) {
            int x = buffer;
            fillBuffer();
            return (byte) (x & 0xff);
        }

        // combine last n bits of current buffer with first 8-n bits of new buffer
        int x = buffer;
        x <<= (8 - n);
        int oldN = n;
        fillBuffer();
        if (isEmpty()) throw new NoSuchElementException("Reading from empty input stream");
        n = oldN;
        x |= (buffer >>> n);
        return (byte) (x & 0xff);
        // the above code doesn't quite work for the last character if n = 8
        // because buffer will be -1, so there is a special case for aligned byte

    }
    
   /**
     * Test client. Reads in a binary input file from standard input and writes
     * it to standard output.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
    	IBinaryReader i=new BinaryStdIn();

        IBinaryWriter o=new BinaryStdOut();
        // read one 8-bit char at a time
        while (!i.isEmpty()) {
            char c = i.readChar();
            o.write(c);
        }
        o.flush();
    }
/* (non-Javadoc)
 * @see com.zoubworld.java.utils.compress.file.IBinaryReader#readSymbols()
 */
@Override
public List<ISymbol> readSymbols() {
	List<ISymbol> ls=new ArrayList<ISymbol>();
	ISymbol e=null;
	while((e=readSymbol())!=null)
		ls.add(e);
	return ls;
}
/* (non-Javadoc)
 * @see com.zoubworld.java.utils.compress.file.IBinaryReader#readSymbols(int)
 */
@Override
public List<ISymbol> readSymbols(int n) {
	List<ISymbol> ls=new ArrayList<ISymbol>();
	ISymbol e=null;
	while((n-->0)&&((e=readSymbol())!=null))
		ls.add(e);
	return ls;
}


@Override
public  long readLong(int len,boolean bigendian)
{
	long s=readLong(len);
	if (bigendian)
		return s;
	if (len%8==0)
	{
		long sr=0;
		for(int i=0;i<len;i+=8)
		{
			long tmp=((s>>i)&0xff);
			tmp=tmp<<(len-i-8);
			sr|=tmp;
		}
		return sr;
		
	}
	return s;
}
}






