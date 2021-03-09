package com.zoubworld.java.utils.compress;

import java.util.List;
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + current_mode;
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CodeNumberSet other = (CodeNumberSet) obj;
		if (current_mode != other.current_mode)
			return false;
		return true;
	}
	/**
	 * */
	public CodeNumberSet(int mode) {
		 current_mode=mode;
	}
	public CodeNumberSet(Long mode) {
		 current_mode=mode.intValue();
	}
	public CodeNumberSet(List<ISymbol> ls) {
		 Map<ISymbol, Long> m = ISymbol.Freq(ls);
		 current_mode=getConfigIndex(m);
	}
	

	public Long getParam( ) 
	{
		return (long)current_mode;
	}
	 
	public CodeNumberSet(Map<ISymbol, Long> f) {
		 current_mode=getConfigIndex( f);
	}
	public String toString() 
	{
		return CodeNumber.getName(current_mode);
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
		ICode c = CodeNumber.getCode(current_mode,  num);
		c.setSymbol(sym);
		return c;
	}
	@Override
	public ISymbol get(ICode code) {		
		return code.getSymbol();
	}
	@Override
	public ICode getCode(IBinaryReader binaryStdIn) {
		Long n=CodeNumber.readCode(current_mode,  binaryStdIn);
		ISymbol sym=sprout.Factory(n);
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
		ISymbol sym=sprout.Factory(n);
		return sym;
	}
	@Override
	public void writeCodingRule(IBinaryWriter binaryStdOut) {

		binaryStdOut.write(Symbol.CodingSet);
		ICodingRule cr = binaryStdOut.getCodingRule();
		binaryStdOut.setCodingRule(new CodeNumberSet(CodeNumber.Golomb4Coding));
		binaryStdOut.write(new Number(ICodingRule.iCodeNumberSet));
		binaryStdOut.write(new Number(current_mode));		
		 binaryStdOut.setCodingRule(cr);
		
	}
	
	ISymbol sprout=new Number();
	@Override
	public void setSprout(ISymbol sprout)
	{
	this.sprout=sprout;	
	}
	
}
