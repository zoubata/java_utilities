package com.zoubworld.java.utils.compress.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
	 * Fast byte-wise CRC16 calculation.
	 *
	 * @author Christian d'Heureuse, Inventec Informatik AG, Switzerland,
	 *         www.source-code.biz
	 */
	public class Crc16 {
		   /**
	     * Calculate the CRC64 of the given file's content.
	     *
	     * @param f
	     * @return new {@link CRC64} instance initialized to the file's CRC value
	     * @throws IOException
	     *             in case the {@link FileInputStream#read(byte[])} method fails
	     */
	    public static Crc16 fromFile(File f) throws IOException
	    {
	        return fromInputStream(new FileInputStream(f));
	    }

	    /**
	     * Calculate the CRC64 of the given {@link InputStream} until the end of the
	     * stream has been reached.
	     *
	     * @param in
	     *            the stream will be closed automatically
	     * @return new {@link CRC64} instance initialized to the {@link InputStream}'s CRC value
	     * @throws IOException
	     *             in case the {@link InputStream#read(byte[])} method fails
	     */
	    public static Crc16 fromInputStream(InputStream in) throws IOException
	    {
	        try
	        {
	        	Crc16 crc = new Crc16();
	            byte[] b = new byte[65536];
	            int l = 0;

	            while ((l = in.read(b)) != -1)
	            {
	                crc.update(b, l);
	            }

	            return crc;

	        } finally
	        {
	            in.close();
	        }
	    }
	    /* Current CRC value. */
	    private int value=0;

		public Integer getValue() {
			return value;
		}
	    /**
	     * Update CRC64 with new byte block.
	     */
	    public void update(byte[] b, int len) {
	    	if(b.length==len)
	    	value=calculate(b, value);
	    	else
	    	{	byte[] data=new byte[len];
	    	for(int i=0;i<len;i++)
	    		data[i]=b[i];
	    	value=calculate(data, value);
	    	}
	    }
		// Generator polynom codes (the bits are in reverse order and the bit for x^16
		// is omitted):
		public static final int stdPoly = 0xA001; // standard CRC-16 x^16 + x^15 + x^2 + 1 (CRC-16-IBM)
		public static final int stdRPoly = 0xC002; // standard reverse x^16 + x^14 + x + 1 (CRC-16-IBM)
		public static final int ccittPoly = 0x8408; // CCITT/SDLC/HDLC x^16 + x^12 + x^5 + 1 (CRC-16-CCITT)
		// The initial CRC value is usually 0xFFFF and the result is complemented.
		public static final int ccittRPoly = 0x8810; // CCITT reverse x^16 + x^11 + x^4 + 1 (CRC-16-CCITT)
		public static final int lrcPoly = 0x8000; // LRCC-16 x^16 + 1

		private short[] crcTable;
		private boolean bitOrder;

		/**
		 * Creates a Crc16 instance and generates the internal lookup table;
		 *
		 * @param poly
		 *            a bit mask of the generator polynomial, in reversed bit order (LSB
		 *            first).
		 * @param bitOrder
		 *            false for LSB first (which is normally used), or true for MSB
		 *            first (which is used e.g. for XMODEM/CRC).
		 */
		public Crc16(int poly, boolean bitOrder) {
			this.bitOrder = bitOrder;
			if (bitOrder) {
				crcTable = genCrc16TableMsbFirst(reverseInt16(poly));
			} else {
				crcTable = genCrc16TableLsbFirst(poly);
			}
		}public Crc16() {
			this.bitOrder = true;
			{
				crcTable = genCrc16TableMsbFirst(reverseInt16(ccittPoly));
			}
		}

		/**
		 * Calculates the CRC code over a data block.
		 */
		public int calculate(byte[] data, int initialCrcValue) {
			if (bitOrder) {
				return calculateCrcMsbFirst(data, initialCrcValue);
			} else {
				return calculateCrcLsbFirst(data, initialCrcValue);
			}
		}

		public int calculate(long data, int initialCrcValue) {
			if (bitOrder) {
				return calculateCrcMsbFirst(data, initialCrcValue);
			} else {
				return calculateCrcLsbFirst(data, initialCrcValue);
			}
		}

		private int calculateCrcLsbFirst(byte[] data, int initialCrcValue) {
			int crc = initialCrcValue;
			for (int p = 0; p < data.length; p++) {
				crc = (crc >> 8) ^ (crcTable[(crc & 0xFF) ^ (data[p] & 0xFF)] & 0xFFFF);
			}
			return crc;
		}

		private int calculateCrcMsbFirst(byte[] data, int initialCrcValue) {
			int crc = initialCrcValue;
			for (int p = 0; p < data.length; p++) {
				crc = ((crc << 8) & 0xFF00) ^ (crcTable[(crc >> 8) ^ (data[p] & 0xFF)] & 0xFFFF);
			}
			return crc;
		}

		private int calculateCrcLsbFirst(long data, int initialCrcValue) {
			int crc = initialCrcValue;
			for (int p = 0; p < 64; p += 8) {
				crc = (crc >> 8) ^ (crcTable[(int) ((crc & 0xFF) ^ ((data >> p) & 0xFF))] & 0xFFFF);
			}
			return crc;
		}

		private int calculateCrcMsbFirst(long data, int initialCrcValue) {
			int crc = initialCrcValue;
			for (int p = 0; p < 64; p += 8) {
				crc = ((crc << 8) & 0xFF00) ^ (crcTable[(int) ((crc >> 8) ^ ((data >> p) & 0xFF))] & 0xFFFF);
			}
			return crc;
		}

		private short[] genCrc16TableLsbFirst(int poly) {
			short[] table = new short[256];
			for (int x = 0; x < 256; x++) {
				int w = x;
				for (int i = 0; i < 8; i++) {
					if ((w & 1) != 0) {
						w = (w >> 1) ^ poly;
					} else {
						w = w >> 1;
					}
				}
				table[x] = (short) w;
			}
			return table;
		}

		private short[] genCrc16TableMsbFirst(int poly) {
			short[] table = new short[256];
			for (int x = 0; x < 256; x++) {
				int w = x << 8;
				for (int i = 0; i < 8; i++) {
					if ((w & 0x8000) != 0) {
						w = (w << 1) ^ poly;
					} else {
						w = w << 1;
					}
				}
				table[x] = (short) w;
			}
			return table;
		}

		// Reverses the bits of a 16 bit integer.
		private int reverseInt16(int i) {
			i = (i & 0x5555) << 1 | (i >>> 1) & 0x5555;
			i = (i & 0x3333) << 2 | (i >>> 2) & 0x3333;
			i = (i & 0x0F0F) << 4 | (i >>> 4) & 0x0F0F;
			i = (i & 0x00FF) << 8 | (i >>> 8);
			return i;
		}


	}
