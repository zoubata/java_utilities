/**
 * 
 */
package com.zoubworld.java.utils.compress.file;

import java.util.NoSuchElementException;

/**
 * @author M43507
 *
 */
public class BinaryCaesarCipher extends BinaryFinFout {

	int key = 0;
	final long mask = 0xFFFFFFFF;

	boolean encryptIn, decryptIn, decryptOut, encryptOut;
	IBinaryReader reader = null;
	IBinaryWriter writer = null;

	/**
	 * This will override the behaviour of reader to de/encrypt the data. my key an
	 * int between 0 and 1^keysize-1 keysize : 1,2,4,8,16,32 encrypt=true, will
	 * encrypt the data from reader encrypt=false, will decrypt the data from reader
	 */
	public BinaryCaesarCipher(int mykey, int keysize, boolean encrypt, IBinaryReader readerToOverride) {
		key = 0;
		byte keyit = (byte) (32 / keysize);
		for (; keyit > 0; keyit--)
			key = (key << keysize) + mykey;
		encryptIn = encrypt;
		decryptIn = !encrypt;
		decryptOut = false;
		encryptOut = false;
		reader = readerToOverride;
		// IBinaryWriter
	}

	/**
	 * this will override the behaviour of writer to de/encrypt the data. my key an
	 * int between 0 and 1^keysize-1 keysize : 1,2,4,8,16,32 encrypt=true, will
	 * encrypt the data to writer encrypt=false, will decrypt the data to writer
	 */
	public BinaryCaesarCipher(int mykey, int keysize, boolean encrypt, IBinaryWriter writerToOverride) {
		key = 0;
		byte keyit = (byte) (32 / keysize);
		for (; keyit > 0; keyit--)
			key = (key << keysize) + mykey;
		encryptOut = encrypt;
		decryptOut = !encrypt;
		decryptIn = false;
		encryptIn = false;
		writer = writerToOverride;
		//
	}

	@Override
	protected void updateBufferout() {
		if (indexOut == 32) {
			int data = (int) (bufferout & 0xffffffff);
			
			indexOut = 0;
			bufferout = 0;
			if (encryptOut)
				data = (int) ((data + key) & mask);
			else if (decryptOut)
				data = (int) ((data - key) & mask);
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
				bufferout = ((bufferout + key) & mask) | (bufferout & ~mask);
			else if (decryptOut)
				bufferout = ((bufferout - key) & mask) | (bufferout & ~mask);
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
					bufferin = ((bufferin + key) & mask);
				else if (decryptIn)
					bufferin = ((bufferin - key) & mask);
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
					bufferin = ((bufferin + key) & mask) | (bufferin & ~mask);
				else if (decryptIn)
					bufferin = ((bufferin - key) & mask) | (bufferin & ~mask);
			} else
				throw new NoSuchElementException("Reading from empty input stream");

		} else {
			bufferin = EOF;
			indexIn = -2;
			throw new NoSuchElementException("Reading from empty input stream");
		}
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BinaryFinFout fifo =new BinaryFinFout();
		BinaryCaesarCipher bin=new BinaryCaesarCipher(1,8,true,(IBinaryWriter)fifo);
		bin.write("ABCEDEFGHIJKL");
		System.out.print(fifo.readString(13));
		
	}

}
