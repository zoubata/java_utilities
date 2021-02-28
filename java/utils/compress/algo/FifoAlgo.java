/**
 * 
 */
package com.zoubworld.java.utils.compress.algo;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Number;
/**
 * @author M43507
 *
 */
public class FifoAlgo implements IAlgoCompress {

	long param=0L;	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (param ^ (param >>> 32));
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
		FifoAlgo other = (FifoAlgo) obj;
		if (getParam() != other.getParam())
			return false;
		return true;
	}
	/**
	 * 
	 */
	public FifoAlgo() {
		// TODO Auto-generated constructor stub
	}
	public FifoAlgo(Long param) {
		if(param!=null)
		this.param=param;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.algo.IAlgoCompress#decodeSymbol(java.util.List)
	 */
	@Override
	public List<ISymbol> decodeSymbol(List<ISymbol> lenc) {
		List<ISymbol> ldec=new ArrayList<ISymbol>();
		for(ISymbol le:lenc)
		{
			ISymbol l=null;
			long index=(le.getLong().longValue());
			if (index<fifo.size())
			{		ldec.add(l=fifo.remove((int)index));
			fifo.add(0,l);
			}
			else
			{
				ldec.add(l=new Number(index-fifo.size()));
				fifo.add(0,l);
			}
		}
		return ldec;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.algo.IAlgoCompress#encodeSymbol(java.util.List)
	 */
	@Override
	public List<ISymbol> encodeSymbol(List<ISymbol> ldec) {
		List<ISymbol> lse=new ArrayList<ISymbol>();
		for(ISymbol l:ldec)
		{
			int index=fifo.indexOf(l);
			if (index==-1)
			{
				
				lse.add(new Number(fifo.size()+l.getLong()));
				fifo.add(0,l);
			}
			else
				{lse.add(new Number(index));	fifo.remove(index);fifo.add(0,l);}	
		}
		
		return lse;
	}
	List<ISymbol> fifo=new ArrayList<ISymbol>();

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.algo.IAlgoCompress#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	static public double bitlen(List<ISymbol> ldec) {
	double len=0;
	for(ISymbol l:ldec)
	{
		len+=Math.log(l.getLong().longValue()+1)/Math.log(2);
	}
	return len;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long d[]={8589934591L, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1024, 
				1024, 1024, 0, 1024, 1024, 1024, 0, 17408, 0, 0, 0,
				0, 0, 512, 512, 0, 0};
		long d2[]= {33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 31};
		d=d2;
		List<ISymbol> ld = Number.from(d);
		FifoAlgo fifo=new FifoAlgo();
		List<ISymbol> le = fifo.encodeSymbol(ld);
		System.out.println(ld.size()*8+" ; "+bitlen(ld)+":"+ld);
		System.out.println(""+bitlen(le)+":"+le);
		fifo=new FifoAlgo();
		List<ISymbol>  ldec = fifo.decodeSymbol(le);
		System.out.println(ldec.equals(ld)+":"+ldec);
		
	}

}
