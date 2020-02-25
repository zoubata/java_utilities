/**
 * 
 */
package com.zoubworld.java.utils.compress.algo.lz4;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.java.utils.compress.Code;
import com.zoubworld.java.utils.compress.ICode;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.SymbolComplex.Sym_LZ4;
import com.zoubworld.java.utils.compress.algo.LZ4;
import com.zoubworld.java.utils.compress.file.IBinaryReader;

/**
 * @author Pierre Valleau
 *
 */
public class LZ4Block {
	/**
	 * @return the blockChecksum
	 */
	public Integer getBlockChecksum() {
	
	if(BlockChecksum==null)
	{
	XXHash32 crc=new XXHash32(0);
	
	int i=0;
	int len=0;
		for(ICode s:getCodeData())
		{
			
			i=(int)((i<<s.length())+s.getLong());
			len+=s.length();
			if(len>=8)
			{
			crc.update((int)i);i>>=4;len-=8;
					}
		}
	BlockChecksum=(int)crc.getValue();
	}
	return BlockChecksum;
	}

	/**
	 * @param blockChecksum
	 *            the blockChecksum to set
	 */
	public void setBlockChecksum(Integer blockChecksum) {
		BlockChecksum = blockChecksum;
	}

	/**
	 * @return the blockSize
	 */
	public int getBlockSize() {
		if(!isCompressed())
		return getData().size()+(isCompressed()?0:0x80000000);
		int l=0;
		for(ICode c:getCodeData())
			l+=c.length();
		return l/8;
	}

	
	/**
	 * @return the data
	 */
	public List<ISymbol> getData() {
		if(data==null)
			data=new ArrayList<ISymbol>();
		return data;
	}
	public List<ICode> getCodeData() {
		if(code==null)
		{
			if( !isCompressed())
				code= Symbol.toCode(getData());
			else
			{ 
				LZ4 algo=new LZ4();
				int i=0;
				List<ISymbol> lsenc = algo.encodeSymbol(getData());
				code= Symbol.toCode(lsenc);
			}
	}
		return code;
		
	
	}
	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<ISymbol> data) {
		this.data = data;
	}

	/**
	 * Block checksum
	 * 
	 * Only present if the associated flag is set. This is a 4-bytes checksum value,
	 * in little endian format, calculated by using the xxHash-32 algorithm on the
	 * raw (undecoded) data block, and a seed of zero. The intention is to detect
	 * data corruption (storage or transmission errors) before decoding.
	 * 
	 * Block checksum is cumulative with Content checksum.
	 */
	Integer BlockChecksum = null;
	/**
	 * Block Size
	 * 
	 * This field uses 4-bytes, format is little-endian.
	 * 
	 * The highest bit is “1” if data in the block is uncompressed.
	 * 
	 * The highest bit is “0” if data in the block is compressed by LZ4.
	 * 
	 * All other bits give the size, in bytes, of the following data block. The size
	 * does not include the block checksum if present.
	 * 
	 * Block Size shall never be larger than Block Maximum Size. Such a thing could
	 * potentially happen for non-compressible sources. In such a case, such data
	 * block shall be passed using uncompressed format.
	 */
	//int BlockSize = 0;
	boolean compressed=false;
	/**
	 * @return the compressed
	 */
	public boolean isCompressed() {
		return compressed;
	}

	/**
	 * @param compressed the compressed to set
	 */
	public void setCompressed(boolean compressed) {
		this.compressed = compressed;
	}

	/**
	 * Data : list of symbol
	 * 
	 * Where the actual data to decode stands. It might be compressed or not,
	 * depending on previous field indications.
	 * 
	 * When compressed, the data must respect the LZ4 block format specification.
	 * 
	 * Note that the block is not necessarily full. Uncompressed size of data can be
	 * any size, up to "Block Maximum Size”, so it may contain less data than the
	 * maximum block size.
	 */
	List<ISymbol> data;
	/**compacted data */
	List<ICode> code=null;
	public List<ICode> toCodes(boolean isBlockChecksumFlag)
	{
	
		List<ICode> lc=new ArrayList<ICode>();
		lc.add( Code.Factory(getBlockSize(),32,false));
		lc.addAll(getCodeData());
		System.out.println(getCodeData().toString());
		if( isBlockChecksumFlag)
			lc.add(new Code(getBlockChecksum(),32));
	
		return lc;
	}

	public   LZ4Block read(IBinaryReader bin, boolean blockChecksumFlag) {
		int BlockSize=((int)bin.readLong(32,false));
		List<ICode> lc=new ArrayList<ICode>();
		for(int i=0;i<BlockSize;)
		{
			Sym_LZ4 sym=Sym_LZ4.read(bin);
			getData().add(sym);
			ICode ac;
			getCodeData().add(ac=sym.getCode());
			i+=ac.length()/8;
		}
		if( blockChecksumFlag)
		{
			int aBlockChecksum=((int)bin.readLong(32,false));
			if (getBlockChecksum()!=aBlockChecksum)
					return null;//Error
		}
		LZ4 l=new LZ4();
		data = l.decodeSymbol(getData());
		
		return this;
		
	}
}
