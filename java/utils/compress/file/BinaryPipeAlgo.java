/**
 * 
 */
package com.zoubworld.java.utils.compress.file;

import java.util.NoSuchElementException;

import com.zoubworld.java.utils.cryptography.CaesarCipher32;
import com.zoubworld.java.utils.cryptography.ICryptoAlgo;

/**
 * @author Pierre Valleau
 * 
 * this class can be used to do a processing on a Binary data: a compression, an encryption, and redondancy adder, or a checker adder.
 *
 */
public class BinaryPipeAlgo extends BinaryFinFout  {

	@Override
	protected  void initialize()
	 {
		super.initialize();
		if (algo!=null)
		algo.reset();
	 }
	boolean encryptIn, decryptIn, decryptOut, encryptOut;
	IBinaryReader reader = null;
	IBinaryWriter writer = null;

	/** for caesar cipher, piplines a IBinaryReader
	 * This will override the behaviour of reader to de/encrypt the data. 
	 * my key is an integer between 0 and 1^keysize-1 
	 * keysize : 1,2,4,8,16,32 
	 * encrypt=true, will encrypt the data from reader 
	 * encrypt=false, will decrypt the data from reader
	 */
	public BinaryPipeAlgo(int mykey, int keysize, boolean encrypt, IBinaryReader readerToOverride) {
		super();
		encryptIn = encrypt;
		decryptIn = !encrypt;
		decryptOut = false;
		encryptOut = false;
		reader = readerToOverride;
		// IBinaryWriter
		algo=new CaesarCipher32(mykey,  keysize);
	}
	
	/** for caesar cipher, piplines a IBinaryWriter
	 * this will override the behaviour of writer to de/encrypt the data. 
	 * my key is an integer between 0 and 1^keysize-1 
	 * keysize : 1,2,4,8,16,32 
	 * encrypt=true, will encrypt the data to writer 
	 * encrypt=false, will decrypt the data to writer
	 */
	public BinaryPipeAlgo(int mykey, int keysize, boolean encrypt, IBinaryWriter writerToOverride) {
		super();
		encryptOut = encrypt;
		decryptOut = !encrypt;
		decryptIn = false;
		encryptIn = false;
		writer = writerToOverride;
		algo=new CaesarCipher32(mykey,  keysize);
		//
	}
	ICryptoAlgo algo;
	/**
	 * This will override the behaviour of reader to de/encrypt the data. my key an
	 * int between 0 and 1^keysize-1 keysize : 1,2,4,8,16,32 encrypt=true, will
	 * encrypt the data from reader encrypt=false, will decrypt the data from reader
	 */
	public BinaryPipeAlgo(ICryptoAlgo algo, boolean encrypt, IBinaryReader readerToOverride) {
		super();
		encryptIn = encrypt;
		decryptIn = !encrypt;
		decryptOut = false;
		encryptOut = false;
		reader = readerToOverride;
		this.algo=algo;
	}
	
	/**
	 * this will override the behaviour of writer to de/encrypt the data. my key an
	 * int between 0 and 1^keysize-1 keysize : 1,2,4,8,16,32 encrypt=true, will
	 * encrypt the data to writer encrypt=false, will decrypt the data to writer
	 */
	public BinaryPipeAlgo(ICryptoAlgo algo, boolean encrypt, IBinaryWriter writerToOverride) {
		super();
		encryptOut = encrypt;
		decryptOut = !encrypt;
		decryptIn = false;
		encryptIn = false;
		writer = writerToOverride;
		this.algo=algo;
	}

	@Override
	protected void updateBufferout() {
		if (indexOut == 32) {
			int data = (int) (bufferout & 0xffffffff);
			
			indexOut = 0;
			bufferout = 0;
			if (encryptOut)
				//data = (int) ((data + key) & mask);
				data=algo.encrypt(data);
			else if (decryptOut)
				//data = (int) ((data - key) & mask);
				data=algo.decrypt(data);
			if (writer != null)
				writer.write(data);
			else
				fifodata.add(data);
		} else if (indexOut > 32) {
			indexOut -= 32;
			int x = ((int) bufferout & ((1 << indexOut) - 1));

			int data = (int) ((bufferout >> indexOut) & 0xffffffff);
			if (writer != null)
				writer.write(data);
			else
				fifodata.add(data);

			bufferout = x;
			if (encryptOut)
				bufferout = algo.encrypt(x) | (bufferout & ~0xFFFFFFFFL);
			else if (decryptOut)
				bufferout = algo.decrypt(x) | (bufferout & ~0xFFFFFFFFL);
		}

	}

	@Override
	protected void fillBuffer() {
		if ((fifodata == null) && (reader == null))
			return;

		if ((fifodata == null) || !fifodata.isEmpty() || indexIn < 32) {
			if (indexIn == 0) {

				if (reader != null)
					bufferin = reader.readInt();
				else
					bufferin = fifodata.remove(0);
				indexIn = 32;
				if (encryptIn)
					bufferin = algo.encrypt((int)bufferin );
				else if (decryptIn)
					bufferin = algo.decrypt((int)bufferin);
			} else if ((fifodata == null) || !fifodata.isEmpty()) {
				bufferin <<= 32;
				int data;
				if (reader != null)
					data = reader.readInt();
				else
					data = fifodata.remove(0);

				bufferin = bufferin | ((long) (data) & 0xFFFFFFFFL);
				indexIn += 32;

				if (encryptIn)
					bufferin = algo.encrypt((int)bufferin ) | (bufferin & ~0xFFFFFFFFL);
				else if (decryptIn)
					bufferin = algo.decrypt((int)bufferin) | (bufferin & ~0xFFFFFFFFL);
			} else
				throw new NoSuchElementException("Reading from empty input stream");

		} else {
			bufferin = EOF;
			indexIn = -2;
			throw new NoSuchElementException("Reading from empty input stream");
		}
	}
	 /**
     * Returns true if standard input is empty.
     * @return true if and only if standard input is empty
     */
	@Override
	public  boolean isEmpty() {
        if (!isInitialized) 
        	initialize();
        if(reader==null)
        return fifodata.isEmpty() && indexIn <= 0;
        return reader.isEmpty() && indexIn <= 0;
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BinaryFinFout fifo =new BinaryFinFout();
		BinaryPipeAlgo bout=new BinaryPipeAlgo('a'-'A',8,true,(IBinaryWriter)fifo);
		BinaryPipeAlgo bin=new BinaryPipeAlgo('a'-'A',8,false,(IBinaryReader)fifo);
		bout.write("ABCDEFGHIJKLM");
		bout.write("ABCDEFGHIJKLM");
		bout.write("ABCDEFGHIJKLM");
		bout.write("ABCDEFGHIJKLM");
		
		bout.write("abcdefghijklm");
		bout.write("abcdefghijklm");
		bout.write("abcdefghijklm");
		bout.write("abcdefghijklm");
		bout.flush();
		bout.flush();
	//	System.out.println("'"+fifo.readString(12)+"'");
	//	fifo.flush();
		System.out.println("'"+bin.readString(13)+"'");
		System.out.println("'"+bin.readString(13)+"'");
		System.out.println("'"+bin.readString(13)+"'");
		System.out.println("'"+bin.readString(13)+"'");
		System.out.println("'"+bin.readString(13)+"'");
		System.out.println("'"+bin.readString(13)+"'");
		System.out.println("'"+bin.readString(13)+"'");
		System.out.println("'"+bin.readString(13)+"'");
		
	}

}
