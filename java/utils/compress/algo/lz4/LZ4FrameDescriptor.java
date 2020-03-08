/**
 * 
 */
package com.zoubworld.java.utils.compress.algo.lz4;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.svg.GetSVGDocument;

import com.zoubworld.java.utils.compress.Code;
import com.zoubworld.java.utils.compress.ICode;
import com.zoubworld.java.utils.compress.file.IBinaryReader;

/**
 * @author Pierre Valleau Frame Descriptor
 * 
 *         3 to 15 Bytes, to be detailed in its own paragraph, as it is the most
 *         important part of the spec.
 * 
 *         The combined Magic Number and Frame Descriptor fields are sometimes
 *         called LZ4 Frame Header. Its size varies between 7 and 19 bytes. The
 *         descriptor uses a minimum of 3 bytes, and up to 15 bytes depending on
 *         optional parameters.
 */
public class LZ4FrameDescriptor {

	/**
	 * @return the fLG
	 */
	public byte getFLG() {
		byte FLG = 0;
		FLG |= getVersion() << 6;
		FLG |= isBlockIndependenceFlag() ? 0x20 : 0;
		FLG |= isBlockChecksumFlag() ? 0x10 : 0;
		FLG |= isContentSizeFlag() ? 0x08 : 0;
		FLG |= isContentChecksumFlag() ? 0x04 : 0;
		FLG |= isDictionaryIDFlag() ? 0x01 : 0;
		return FLG;
	}
	public String toString()
	{
		String s="";
		s+="Frame Descriptor\n{";
		s+="FLG byte : "+String.format("0x2x", getFLG())+"\n";
		s+="\tVersion "+getVersion();
		s+="\tB.Indep "+isBlockIndependenceFlag();
		s+="\tB.Checksum "+isBlockChecksumFlag();
		s+="\tC.Size "+isContentSizeFlag();
		s+="\tC.Checksum "+isContentChecksumFlag();
		s+="\tDictID "+(getDictionaryID()==null);
		
		if (getContentSize()!=null) s+="FLG byte : "+String.format("0x16x", getContentSize())+"\n";
		s+="\tBlock MaxSize "+getBlockMaximumSize();
		
		if (getDictionaryID()!=null) s+="FLG byte : "+String.format("0x8x", getDictionaryID())+"\n";
		s+="HC byte : "+String.format("0x2x", getHC())+"\n";
		
		s+="}\n";
		return s;
	}
	/**
	 * @param fLG
	 *            the fLG to set
	 */
	public void setFLG(byte fLG) {
		// FLG = fLG;
		setVersion(fLG >> 6);
		setBlockIndependenceFlag((fLG & 0x20) == 0x20);
		setBlockChecksumFlag((fLG & 0x10) == 0x10);
		setContentSizeFlag((fLG & 0x08) == 0x08);
		setContentChecksumFlag((fLG & 0x04) == 0x04);
		setDictionaryIDFlag((fLG & 0x01) == 0x01);
	}

	/**
	 * @return the bD
	 */
	public byte getBD() {
		byte BD = 0;
		BD |= (getBlockMaximumSize() & 0x7) << 4;
		return BD;
	}

	/**
	 * @param bD
	 *            the bD to set
	 */
	public void setBD(byte bD) {
		setBlockMaximumSize(bD >> 4);

	}

	/**
	 * @return the contentSize
	 */
	public Long getContentSize() {
		return ContentSize;
	}

	/**
	 * @param contentSize
	 *            the contentSize to set
	 */
	public void setContentSize(Long contentSize) {
		ContentSize = contentSize;
	}

	/**
	 * @return the dictionaryID
	 */
	public Integer getDictionaryID() {
		return DictionaryID;
	}

	/**
	 * @param dictionaryID
	 *            the dictionaryID to set
	 */
	public void setDictionaryID(Integer dictionaryID) {
		DictionaryID = dictionaryID;
	}

	/**
	 * @return the hC
	 * Header Checksum

One-byte checksum of combined descriptor fields, including optional ones. 
The value is the second byte of xxh32() : 
(xxh32()>>8) & 0xFF using zero as a seed, and the full Frame Descriptor as an input (including optional fields when they are present).
 A wrong checksum indicates an error in the descriptor. 
 Header checksum is informational and can be skipped.
	 */
	public byte getHC() {
		if(HC==null)
		{
		XXHash32 crc=new XXHash32(0);
		
		int i=0;
		int len=0;
			for(ICode s:toCodeint())
			{
				
				i=(int)((i<<s.length())+s.getLong());
				len+=s.length();
				if(len>=8)
				{
				crc.update((int)i);i>>=4;len-=8;
						}
			}
			HC=(byte)
			((crc.getValue()>>8) & 0xFF);
		}
		return HC;
	}

	/**
	 * @param hC
	 *            the hC to set
	 */
	public void setHC(byte hC) {
		HC = hC;
	}

	/**
	 * @return the blockIndependenceFlag
	 */
	public boolean isBlockIndependenceFlag() {
		return BlockIndependenceFlag;
	}

	/**
	 * @param blockIndependenceFlag
	 *            the blockIndependenceFlag to set
	 */
	public void setBlockIndependenceFlag(boolean blockIndependenceFlag) {
		BlockIndependenceFlag = blockIndependenceFlag;
	}

	/**
	 * @return the blockChecksumFlag
	 */
	public boolean isBlockChecksumFlag() {
		return BlockChecksumFlag;
	}

	/**
	 * @param blockChecksumFlag
	 *            the blockChecksumFlag to set
	 */
	public void setBlockChecksumFlag(boolean blockChecksumFlag) {
		BlockChecksumFlag = blockChecksumFlag;
	}

	/**
	 * @return the contentSizeFlag
	 */
	public boolean isContentSizeFlag() {
		return ContentSizeFlag;
	}

	/**
	 * @param contentSizeFlag
	 *            the contentSizeFlag to set
	 */
	public void setContentSizeFlag(boolean contentSizeFlag) {
		ContentSizeFlag = contentSizeFlag;
	}

	/**
	 * @return the contentChecksumFlag
	 */
	public boolean isContentChecksumFlag() {
		return ContentChecksumFlag;
	}

	/**
	 * @param contentChecksumFlag
	 *            the contentChecksumFlag to set
	 */
	public void setContentChecksumFlag(boolean contentChecksumFlag) {
		ContentChecksumFlag = contentChecksumFlag;
	}

	/**
	 * @return the dictionaryIDFlag
	 */
	public boolean isDictionaryIDFlag() {
		return DictionaryIDFlag;
	}

	/**
	 * @param dictionaryIDFlag
	 *            the dictionaryIDFlag to set
	 */
	public void setDictionaryIDFlag(boolean dictionaryIDFlag) {
		DictionaryIDFlag = dictionaryIDFlag;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return Version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		Version = version;
	}

	/**
	 * @return the blockMaximumSize
	 */
	public int getBlockMaximumSize() {
		return BlockMaximumSize;
	}

	/**
	 * @param blockMaximumSize
	 *            the blockMaximumSize to set 0 1 2 3 4 5 6 7 N/A N/A N/A N/A 64 KB
	 *            256 KB 1 MB 4 MB
	 */
	public void setBlockMaximumSize(int blockMaximumSize) {
		BlockMaximumSize = blockMaximumSize;
	}

	// byte FLG=0;
	// byte BD=0;
	Long ContentSize = null;
	/**
	 * Dictionary ID
	 * 
	 * Dict-ID is only present if the associated flag is set. It's an unsigned
	 * 32-bits value, stored using little-endian convention. A dictionary is useful
	 * to compress short input sequences. The compressor can take advantage of the
	 * dictionary context to encode the input in a more compact manner. It works as
	 * a kind of “known prefix” which is used by both the compressor and the
	 * decompressor to “warm-up” reference tables.
	 * 
	 * The decompressor can use Dict-ID identifier to determine which dictionary
	 * must be used to correctly decode data. The compressor and the decompressor
	 * must use exactly the same dictionary. It's presumed that the 32-bits dictID
	 * uniquely identifies a dictionary.
	 * 
	 * Within a single frame, a single dictionary can be defined. When the frame
	 * descriptor defines independent blocks, each block will be initialized with
	 * the same dictionary. If the frame descriptor defines linked blocks, the
	 * dictionary will only be used once, at the beginning of the frame.
	 */
	Integer DictionaryID = null;
	/**
	 * Header Checksum
	 * 
	 * One-byte checksum of combined descriptor fields, including optional ones. The
	 * value is the second byte of xxh32() : (xxh32()>>8) & 0xFF using zero as a
	 * seed, and the full Frame Descriptor as an input (including optional fields
	 * when they are present). A wrong checksum indicates an error in the
	 * descriptor. Header checksum is informational and can be skipped.
	 */
	Byte HC=null;

	/**
	 * Block Independence flag
	 * 
	 * If this flag is set to “1”, blocks are independent. If this flag is set to
	 * “0”, each block depends on previous ones (up to LZ4 window size, which is 64
	 * KB). In such case, it’s necessary to decode all blocks in sequence.
	 * 
	 * Block dependency improves compression ratio, especially for small blocks. On
	 * the other hand, it makes random access or multi-threaded decoding impossible.
	 */
	boolean BlockIndependenceFlag=true;
	/**
	 * Block checksum flag
	 * 
	 * If this flag is set, each data block will be followed by a 4-bytes checksum,
	 * calculated by using the xxHash-32 algorithm on the raw (compressed) data
	 * block. The intention is to detect data corruption (storage or transmission
	 * errors) immediately, before decoding. Block checksum usage is optional.
	 */
	boolean BlockChecksumFlag;
	/**
	 * Content Size flag
	 * 
	 * If this flag is set, the uncompressed size of data included within the frame
	 * will be present as an 8 bytes unsigned little endian value, after the flags.
	 * Content Size usage is optional.
	 */
	boolean ContentSizeFlag=false;
	/**
	 * Content checksum flag
	 * 
	 * If this flag is set, a 32-bits content checksum will be appended after the
	 * EndMark.
	 */
	boolean ContentChecksumFlag=true;
	/**
	 * Dictionary ID flag
	 * 
	 * If this flag is set, a 4-bytes Dict-ID field will be present, after the
	 * descriptor flags and the Content Size.
	 */
	boolean DictionaryIDFlag=false;

	/**
	 * Version Number
	 * 
	 * 2-bits field, must be set to 01. Any other value cannot be decoded by this
	 * version of the specification. Other version numbers will use different flag
	 * layouts.
	 */
	int Version = 1;
	/**
	 * Block Maximum Size
	 * 
	 * This information is useful to help the decoder allocate memory. Size here
	 * refers to the original (uncompressed) data size. Block Maximum Size is one
	 * value among the following table : 0 1 2 3 4 5 6 7 N/A N/A N/A N/A 64 KB 256
	 * KB 1 MB 4 MB
	 * 
	 * The decoder may refuse to allocate block sizes above any system-specific
	 * size. Unused values may be used in a future revision of the spec. A decoder
	 * conformant with the current version of the spec is only able to decode block
	 * sizes defined in this spec.
	 */
	int BlockMaximumSize=4;

	
	public List<ICode> toCodes()
	{
		List<ICode> lc=toCodeint();
		lc.add(new Code(getHC(),8));
		return lc;
	}
	private List<ICode> toCodeint()
	{
		List<ICode> lc=new ArrayList<ICode>();
		lc.add(new Code(getFLG(),8));
		lc.add(new Code(getBD(),8));
		
		if( isContentSizeFlag())
			lc.add(new Code(getContentSize(),64));
		
		if( isDictionaryIDFlag())
			lc.add(new Code(getDictionaryID(),32));
		
		return lc;
	}

	public void read(IBinaryReader bin) {
		setFLG((byte)bin.readLong(8,false));
		setBD((byte)bin.readLong(8,false));

		if( isContentSizeFlag())
			setContentSize(bin.readLong(64,false));
		
		if( isDictionaryIDFlag())
			setDictionaryID((int)bin.readLong(32,false));
		int i=getHC();
		setHC((byte)bin.readLong(8,false));
		if(i!=getHC())
			return;//error
	}

}
