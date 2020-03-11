/**
 * 
 */
package com.zoubworld.java.utils.compress.algo.lz4;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.java.utils.compress.Code;
import com.zoubworld.java.utils.compress.ICode;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.file.IBinaryReader;

/**
 * @author Pierre Valleau see
 *         https://github.com/lz4/lz4/blob/master/doc/lz4_Frame_format.md
 *         https://en.wikipedia.org/wiki/LZ4_(compression_algorithm)
 */
public class LZ4FrameFormat {

	static int MagicNb = 0x184D2204;

	LZ4FrameDescriptor descript;
	/**
	 * Data Blocks That’s where compressed data is stored.
	 */
	List<LZ4Block> blocks;
	/**
	 * EndMark
	 * 
	 * The flow of blocks ends when the last data block has a size of “0”. The size
	 * is expressed as a 32-bits value.
	 */
	int EndMark = 0;
	/**
	 * Content Checksum
	 * 
	 * Content Checksum verify that the full content has been decoded correctly. The
	 * content checksum is the result of xxh32() hash function digesting the
	 * original (decoded) data as input, and a seed of zero. Content checksum is
	 * only present when its associated flag is set in the frame descriptor. Content
	 * Checksum validates the result, that all blocks were fully transmitted in the
	 * correct order and without error, and also that the encoding/decoding process
	 * itself generated no distortion. Its usage is recommended.
	 */
	Integer Checksum = null;

	/**
	 * 
	 */
	public LZ4FrameFormat() {
		// TODO Auto-generated constructor stub
	}

	public List<ICode> toCodes() {
		List<ICode> lc = new ArrayList<ICode>();
		lc.add(Code.Factory(MagicNb, 32, false));
		lc.addAll(getDescript().toCodes());
		for (LZ4Block b : getBlocks())
			lc.addAll(b.toCodes(getDescript().isBlockChecksumFlag()));
		lc.add(Code.Factory(getEndMark(), 32, false));
		if (getDescript().isContentChecksumFlag())
			lc.add(Code.Factory(getChecksum(), 32, false));
		return lc;
	}

	public void read(IBinaryReader bin) {
		int i = (int) bin.readLong(32, false);
		if (i != MagicNb)
			return;// error
		getDescript().read(bin);
		LZ4Block bl;
		do {
			bl = new LZ4Block();
			bl.read(bin, getDescript().isBlockChecksumFlag());
			getBlocks().add(bl);
		} while (bl.getBlockSize() > 0);
		i = (int) bin.readLong(32, false);
		if (i != getEndMark())
			return;// error
		if (getDescript().isContentChecksumFlag()) {
			i = (int) bin.readLong(32, false);
			if (i != getChecksum())
				return; // error
		}
	}

	/**
	 * @return the descript
	 */
	public LZ4FrameDescriptor getDescript() {
		if (descript == null)
			descript = new LZ4FrameDescriptor();
		return descript;
	}

	/**
	 * @param descript
	 *            the descript to set
	 */
	public void setDescript(LZ4FrameDescriptor descript) {
		this.descript = descript;
	}

	/**
	 * @return the blocks
	 */
	public List<LZ4Block> getBlocks() {
		if (blocks == null)
			blocks = new ArrayList<LZ4Block>();
		return blocks;
	}

	/**
	 * @param blocks
	 *            the blocks to set
	 */
	public void setBlocks(List<LZ4Block> blocks) {
		this.blocks = blocks;
	}

	/**
	 * @return the checksum
	 */
	public int getChecksum() {
		if (Checksum == null) {
			XXHash32 crc = new XXHash32(0);

			for (LZ4Block b : getBlocks())
				if (b != null && b.getData() != null)
					for (ISymbol s : b.getData()) {
						crc.update((int) s.getId());
					}
			Checksum = (int) crc.getValue();
		}
		// not yet implemented.
		return Checksum;
	}

	/**
	 * @param checksum
	 *            the checksum to set
	 */
	public void setChecksum(int checksum) {
		Checksum = checksum;
	}

	/**
	 * @return the magicNb
	 */
	public static int getMagicNb() {
		return MagicNb;
	}

	/**
	 * @return the endMark
	 */
	public int getEndMark() {
		return EndMark;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * LZ4FrameFormat f=new LZ4FrameFormat(); LZ4Block e=new LZ4Block();
		 * e.setCompressed(true);
		 * //e.getData().addAll(Symbol.from("AAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		 * //e.getData().addAll(Symbol.from(
		 * "01234567890123456789012345678901234567890123456789\r\n"));
		 * e.getData().addAll(Symbol.from(
		 * "0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz"));
		 * String s=
		 * "0123456789abcdefghijklmnopqrstuvwxyzzzyxwvutsrqponmlkjihgfedcba9876543210./*-+!:;,ù*$^=)àç_è-('\"é&²&aqwézsx\"edc'rfv(tgb-yhnèuj,_ik;çol:àpm!§MP0/LO9.KI8?JU7NHY6BGT5VFR4CDE3XSZ2WQA1!mpà:loç;ki_,juènhy-bgt(vfr'cdde\"xszéwqa&AZERTYUIOP¨£QSDFGHJKLM%µWXCVBN?./§§/.?NBVCXWµ%MLKJHGFDSQ£¨POIUYTREZA0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz";
		 * e.getData().addAll(Symbol.from(s)); f.getBlocks().add(e); BinaryFinFout
		 * bin=new BinaryFinFout(); bin.write(f.toCodes()); bin.flush(); LZ4FrameFormat
		 * f2=new LZ4FrameFormat(); bin.setCodingRule(new
		 * CodingSet(CodingSet.UNCOMPRESS)); f2.read(bin);
		 */
	}

}
