/**
 * 
 */
package com.zoubworld.java.utils.compress.binalgo.arithemic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;

/**
 * @author Pierre valleau
 *
 */
public class FrequencyTable implements IFrequencyTable {

	public Map<ISymbol, Long> freq;

	/**
	 * 
	 */
	public FrequencyTable(List<ISymbol> ls) {
		freq=Symbol.Freq(ls);
		for(int i=0;i<Symbol.getNbSymbol();i++)
			{
			ISymbol s=Symbol.findId(i);
			if( freq.get(s)==null)
				freq.put(s, 0L);}
	}

	public FrequencyTable(int[] freqs) {
		freq=new HashMap<ISymbol, Long>();
		for(int i=0;i<freqs.length;i++)
			freq.put(Symbol.findId(i), (long) freqs[i]);
			
	}
	public FrequencyTable(int nbsymbol) {
		freq=new HashMap<ISymbol, Long>();
		for(int i=0;i<nbsymbol;i++)
			freq.put(Symbol.findId(i), 1L);
			
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.binalgo.arithemic.IFrequencyTable#getSymbolLimit()
	 */
	@Override
	public int getSymbolLimit() {
		
		return freq.size();
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.binalgo.arithemic.IFrequencyTable#get(int)
	 */
	@Override
	public int get(int isymbol) {
		return get(Symbol.findId(isymbol));
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.binalgo.arithemic.IFrequencyTable#get(com.zoubworld.java.utils.compress.ISymbol)
	 */
	@Override
	public int get(ISymbol symbol) {
		
		return freq.get(symbol).intValue();
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.binalgo.arithemic.IFrequencyTable#set(int, int)
	 */
	@Override
	public void set(int isymbol, int freq) {
		set(Symbol.findId(isymbol),freq);
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.binalgo.arithemic.IFrequencyTable#set(com.zoubworld.java.utils.compress.ISymbol, int)
	 */
	@Override
	public void set(ISymbol symbol, int freqi) {
		freq.put(symbol,(long) freqi);
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.binalgo.arithemic.IFrequencyTable#increment(int)
	 */
	@Override
	public void increment(int isymbol) {
		increment(Symbol.findId(isymbol));

	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.binalgo.arithemic.IFrequencyTable#increment(com.zoubworld.java.utils.compress.ISymbol)
	 */
	@Override
	public void increment(ISymbol symbol) {
		long freqi=get(symbol);
		freq.put(symbol, freqi+1);

	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.binalgo.arithemic.IFrequencyTable#getTotal()
	 */
	@Override
	public int getTotal() {
		long s=0;
	
		for(Long l:freq.values())
			s+=l;
		return (int) s;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.binalgo.arithemic.IFrequencyTable#getLow(int)
	 */
	@Override
	public int getLow(int isymbol) {
		return getLow(Symbol.findId(isymbol));
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.binalgo.arithemic.IFrequencyTable#getLow(com.zoubworld.java.utils.compress.ISymbol)
	 */
	@Override
	public int getLow(ISymbol symbol) {
		long s=0;
		int c=0;
		while(!Symbol.tabId[c].equals(symbol))
			s+=freq.get(Symbol.tabId[c++]);
		return (int) s;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.binalgo.arithemic.IFrequencyTable#getHigh(int)
	 */
	@Override
	public int getHigh(int isymbol) {
		return getHigh(Symbol.findId(isymbol));
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.binalgo.arithemic.IFrequencyTable#getHigh(com.zoubworld.java.utils.compress.ISymbol)
	 */
	@Override
	public int getHigh(ISymbol symbol) {
		return (int) (getLow( symbol)+freq.get(symbol));
	}

}
