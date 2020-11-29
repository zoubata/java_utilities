package com.zoubworld.java.utils.compress;

import java.util.Map;

import com.zoubworld.java.utils.compress.file.IBinaryReader;
import com.zoubworld.java.utils.compress.file.IBinaryWriter;
/** 
 * This coding set is more dedicated to represent number as ISymbol.
 * it implements several representation like universal code :
 * Elias gamma coding
 * Elias delta coding 
 * Elias omega coding 
 * Exp-Golomb coding 
 * Fibonacci coding
 * Levenshtein coding 
 * Variable-length quantity
 * ...
but also
 * Unary coding
 * Rice coding
 * Golomb coding.
 * ...
    @see CodeNumber.
 * */
public class CodeNumberSet implements ICodingRule{
	int current_mode=0;
	
	/**
	 * */
	public CodeNumberSet(int mode) {
		 current_mode=mode;
	}
	
	/**
	 * */
	public CodeNumberSet(Map<ISymbol, Long> f) {
		 current_mode=getConfigIndex( f);
	}
	
	static public int getConfigIndex( Map<ISymbol, Long> f)
	{
		int index=-1;
		long sumref=Long.MAX_VALUE;
		for(int i=0;i<=CodeNumber.MaxCodingIndex;i++)
		{
			long sum=0;
			for(ISymbol s:f.keySet())
				sum+=CodeNumber.getCode(i, s.getId()).length()*f.get(s);
			if(sumref>sum)
			{
				index=i;
				sumref=sum;
			}
		}	
		return index;		
	}
	
	@Override
	public ICode get(ISymbol sym) {
		long num=sym.getId();
		return CodeNumber.getCode(current_mode,  num);
	}
	@Override
	public ISymbol get(ICode code) {		
		return code.getSymbol();
	}
	@Override
	public ICode getCode(IBinaryReader binaryStdIn) {
		Long n=CodeNumber.readCode(current_mode,  binaryStdIn);
		ISymbol sym=new Number (n);
		sym.setCode(get( sym));
		return sym.getCode();
	}
	@Override
	public ICode getGenericCode(IBinaryReader binaryStdIn) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ISymbol getSymbol(IBinaryReader binaryStdIn) {
		Long n=CodeNumber.readCode(current_mode,  binaryStdIn);
		ISymbol sym=new Symbol (n);
		return sym;
	}
	@Override
	public void writeCodingRule(IBinaryWriter binaryStdOut) {

		binaryStdOut.write(Symbol.CodingSet);
		ICodingRule cr = binaryStdOut.getCodingRule();
		binaryStdOut.setCodingRule(new CodeNumberSet(CodeNumber.Golomb4Coding));
		binaryStdOut.write(new Number(ICodingRule.iCodeNumberSet));
		binaryStdOut.write(new Number(CodeNumber.GammaCoding));		
		 binaryStdOut.setCodingRule(cr);
		
	}
}
