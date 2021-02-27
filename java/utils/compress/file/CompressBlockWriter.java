/**
 * 
 */
package com.zoubworld.java.utils.compress.file;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
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
public class CompressBlockWriter {
		
	/**
	 * 
	 */
	public CompressBlockWriter(IBinaryWriter out) {
		this.out=out;
	}
	public CompressBlockWriter(IBinaryWriter out,List<IAlgoCompress> S2S_algo,ICodingRule codingrules) {
		this.out=out;
		this.S2S_algo=S2S_algo;
		this.S2B_algo=codingrules;
	}
	List<ISymbol> ls;
//ls.size():Long nbISymbols;//number of symbol that is inside this Block(uncompressed symbols).
	
	//List<CompressBlock> data;//if nbStream>1, data are inside a CompressBlock
	
	int type=0;
//	Integer nbStream;//number of Stream of sub block inside this block.
	
	//int nbS2S; // number of symbol to symbol algos
	List<IAlgoCompress> S2S_algo;// symbol to symbol Algorithm apply on this block.
	//List<Long> S2S_param;// parameter of symbol to symbol Algorithm apply on this block.
	
	long nbBit; //number of bits that represent this compressed block (data/datab)
	long nbBinSymbols;// number of symbol to read inside the bit stream.(compressed symbol)
	
	
	ICodingRule S2B_algo;// Sym to binary Algorithm apply on this block.
	//long S2B_param;// parameter of Sym to binary Algorithm apply on this block.
	IBinaryReader datab;//if nbStream==1, this is define and containt raw data.
	
//internal
	Long pos ;// position of begin of block

	List<ISymbol> lenc;
	public void write(List<ISymbol> ls)
	{
		this.ls=ls;
		out.write((int)type);
		out.write((long)ls.size());
		
		
		//List<ISymbol> 
		lenc=ls;
		for(IAlgoCompress algo:S2S_algo)
		{
			//algo.setup(S2S_param.get(index++));
			lenc=algo.encodeSymbol(lenc);
		}
		nbBinSymbols=lenc.size();
		nbBit=Symbol.length(lenc, S2B_algo);
		
		
		out.write((long)nbBit);
		out.write((int)S2S_algo.size());
				
		for(int i=0;i<S2S_algo.size();i++)
		{
			out.write((long)S2S_algo.get(i).getParam());


		}
		for(int i=0;i<S2S_algo.size();i++)
		{
			for(int j=0;j<IAlgoCompress.list.length;j++)
				if(IAlgoCompress.list[j].equals(S2S_algo.get(i).getClass()))
			out.write((int)j);
		}
		out.write((long)nbBinSymbols);
		out.write((long)S2B_algo.getParam());
	
		for(int j=0;j<ICodingRule.list.length;j++)
			if(ICodingRule.list[j].equals(S2B_algo.getClass()))		
				out.write((int)j);

				
		    out.setCodingRule(S2B_algo);
			
		
			
			out.writes(lenc);
	}
	IBinaryWriter out;
	

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
		List<Long> S2S_param = new ArrayList<Long> ();
		for(int i=0;i<getS2S_algo().size();i++)
		{
			long a=getS2S_algo().get(i).getParam();
			S2S_param.add(a);
		}
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
		if (getS2B_algo()!=null)
		return getS2B_algo().getParam();
		return null;
	}
	/**
	 * @return the nbISymbols
	 */
	public Long getNbISymbols() {
		if (ls==null)
			return null;
		return (long)ls.size();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
