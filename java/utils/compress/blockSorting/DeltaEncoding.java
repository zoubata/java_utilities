/**
 * 
 */
package com.zoubworld.java.utils.compress.blockSorting;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Number;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.algo.IAlgoCompress;
import com.zoubworld.java.utils.compress.blockSorting.bwt.CircularList;
import com.zoubworld.utils.JavaUtils;


/**
 * @author Pierre Valleau
 * implementation of Delta Encoding
 * 
https://en.wikipedia.org/wiki/Delta_encoding
This class didn't compress, it just change the entropie.

 */
public class DeltaEncoding implements IAlgoCompress {

	/**
	 * 
	 */
	public DeltaEncoding() {
		// TODO Auto-generated constructor stub
	}
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
		BWT other = (BWT) obj;
		if (param != other.param)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.algo.IAlgoCompress#decodeSymbol(java.util.List)
	 */
	@Override
	public List<ISymbol> decodeSymbol(List<ISymbol> lenc) {
		if(lenc==null) return null;
		 List<ISymbol> lr =new ArrayList<ISymbol>();
		 ISymbol sprout=null;
		 ISymbol old=null;
			for(ISymbol s:lenc)
			{
				if (old==null)
					{lr.add(s);sprout=s;}
				else
				{
					long id=s.getId()+old.getId();
					lr.add(s=sprout.Factory(id));
				}
				old=s;
			}
		return lr;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.algo.IAlgoCompress#encodeSymbol(java.util.List)
	 */
	@Override
	public List<ISymbol> encodeSymbol(List<ISymbol> ldec) {
		if(ldec==null) return null;
		List<ISymbol> le=new ArrayList<ISymbol>();
		ISymbol old=null;
		for(ISymbol s:ldec)
		{
			if (old==null)
				le.add(s);
			else
				le.add(new Number(s.getId()-old.getId()));
			old=s;
		}
		return le;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.algo.IAlgoCompress#getName()
	 */
	@Override
	public String getName() {
		return "DeltaEncoding";
	}

}
