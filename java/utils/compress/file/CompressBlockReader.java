/**
 * 
 */
package com.zoubworld.java.utils.compress.file;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.algo.IAlgoCompress;

/**
 * @author Pierre Valleau
 * data structure inside the archive :
 * [32] block type
 * [64] nbISymbols number of symbol uncompressed
 * [64] nbBit : number of bits that represent this compressed block (data/datab)
 * [32] nbS2S : number of symbol to symbol algos IAlgoCompress
 * [64*nbS2S] S2S_param[nbS2S] : array of parameter for each algo
 * [32*nbS2S] S2S_algo[nbS2S] : array of algo class
 * [64] nbBinSymbols: number of symbol to read inside the bit stream.(compressed symbol)
 * [64] S2B_param : parameter of Sym to binary Algorithm apply on this block.
 * [32] S2B_algo :  Sym to binary Algorithm apply on this block.
 * [nbBit]	: the bit stream	
 */
public class CompressBlockReader {
	
	public CompressBlockReader(IBinaryReader in) {
		datab=in;
		pos = in.getposIn();
		type = in.readInt();
		nbISymbols=in.readLong();
		nbBit=in.readLong();
		nbS2S=in.readInt();
		Class[] cArg = new Class[1]; //Our constructor has 3 arguments
		cArg[0] = Long.class; //First argument is of *object* type Long
		S2S_algo=new ArrayList<IAlgoCompress> ();
		S2S_param=new ArrayList<Long> ();
		for(int i=0;i<nbS2S;i++)
		{
			long a=in.readLong();
			S2S_param.add(a);
		}
		for(int i=0;i<nbS2S;i++)
		{
			int a=in.readInt();
		try {
			S2S_algo.add((IAlgoCompress) (IAlgoCompress.list[a].getDeclaredConstructor(cArg).newInstance(S2S_param.get(i))));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {			
			e.printStackTrace();
		}
		}
		nbBinSymbols=in.readLong();
		S2B_param=in.readLong();
		int a=in.readInt();
		try {
			S2B_algo=(ICodingRule) ICodingRule.list[a].getDeclaredConstructor(cArg).newInstance(S2B_param);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		in.rjumpIn(nbBit);
		
	}
	/**
	 * @return the s2S_algo
	 */
	public List<IAlgoCompress> getS2S_algo() {
		return S2S_algo;
	}
	/**
	 * @return the s2S_param
	 */
	public List<Long> getS2S_param() {
		return S2S_param;
	}
	/**
	 * @return the s2B_algo
	 */
	public ICodingRule getS2B_algo() {
		return S2B_algo;
	}
	/**
	 * @return the s2B_param
	 */
	public Long getS2B_param() {
		return S2B_param;
	}
	/**
	 * @return the nbISymbols
	 */
	public Long getNbISymbols() {
		return nbISymbols;
	}
	Long nbISymbols;//number of symbol that is inside this Block(uncompressed symbols).
	
	//List<CompressBlock> data;//if nbStream>1, data are inside a CompressBlock
	
	int type=0;
//	Integer nbStream;//number of Stream of sub block inside this block.
	
	int nbS2S; // number of symbol to symbol algos
	List<IAlgoCompress> S2S_algo;// symbol to symbol Algorithm apply on this block.
	List<Long> S2S_param;// parameter of symbol to symbol Algorithm apply on this block.
	
	long nbBit; //number of bits that represent this compressed block (data/datab)
	long nbBinSymbols;// number of symbol to read inside the bit stream.(compressed symbol)
	
	
	ICodingRule S2B_algo;// Sym to binary Algorithm apply on this block.
	long S2B_param;// parameter of Sym to binary Algorithm apply on this block.
	IBinaryReader datab;//if nbStream==1, this is define and containt raw data.
	
//internal
	Long pos ;// position of begin of block
	/// position of begin ofbinary stream
	long getposBinStream()
	{	return pos+nbS2S*96+3*32+64*4; }
	// return index of end of block
	long getposend()
	{	return getposBinStream()+nbBit;	}
	/**
	 * 
	 */
	public List<ISymbol> getSymbols() {
		//S2B_algo.setup(S2B_param);
		datab.setCodingRule(S2B_algo);
		datab.jumpIn(getposBinStream());
		List<ISymbol> lenc=datab.readSymbols((int)nbBinSymbols);
		int index=0;
		for(IAlgoCompress algo:S2S_algo)
		{
			//algo.setup(S2S_param.get(index++));
			lenc=algo.decodeSymbol(lenc);
		}
		return lenc;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
