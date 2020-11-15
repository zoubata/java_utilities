/**
 * 
 */
package com.zoubworld.java.utils.compress.algo.lz4;

/**
 * @author Pierre Valleau
 *
 *         Skippable Frames Magic Number Frame Size User Data 4 bytes 4 bytes
 * 
 *         Skippable frames allow the integration of user-defined data into a
 *         flow of concatenated frames. Its design is pretty straightforward,
 *         with the sole objective to allow the decoder to quickly skip over
 *         user-defined data and continue decoding.
 * 
 *         For the purpose of facilitating identification, it is discouraged to
 *         start a flow of concatenated frames with a skippable frame. If there
 *         is a need to start such a flow with some user data encapsulated into
 *         a skippable frame, it’s recommended to start with a zero-byte LZ4
 *         frame followed by a skippable frame. This will make it easier for
 *         file type identifiers.
 * 
 */
public class SkippableFrames extends LZ4Block {
	/**
	 * Magic Number
	 * 
	 * 4 Bytes, Little endian format. Value : 0x184D2A5X, which means any value from
	 * 0x184D2A50 to 0x184D2A5F. All 16 values are valid to identify a skippable
	 * frame.
	 */
	static int MagicNumberBegin = 0x184D2A50;
	/**
	 * Magic Number
	 * 
	 * 4 Bytes, Little endian format. Value : 0x184D2A5X, which means any value from
	 * 0x184D2A50 to 0x184D2A5F. All 16 values are valid to identify a skippable
	 * frame.
	 */
	int MagicNumber = MagicNumberBegin;

	/**
	 * @return the magicNumber
	 */
	public int getMagicNumber() {
		return MagicNumber;
	}

	/**
	 * @param magicNumber
	 *            the magicNumber to set
	 */
	public void setMagicNumber(int magicNumber) {
		MagicNumber = magicNumber;
	}

}
