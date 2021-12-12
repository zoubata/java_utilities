package com.zoubworld.java.utils.compress;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.Set;

public interface ISymbol extends Comparable<ISymbol> {

	boolean isChar();

	boolean isInt();

	boolean isShort();

	char getChar();

	Integer getInt();

	public Long getLong();

	short getShort();

	long getId();

	char[] toSymbol();

	String toString();

	ICode getCode();

	void setCode(ICode code2);

	Comparator<ISymbol> ComparatorbyId = 
			  (ISymbol player1, ISymbol player2) -> Long.compare(player2.getId(), player1.getId());

	/** List of class available to build ISymbol*/
	public static Class list[]= 
		{
			Symbol.class,
			Number.class,
				}; 
	
	/**
	 * from a list of symbol do the histogram of frequency
	 */
	public static Map<ISymbol, Long> Freq(List<ISymbol> l) {
		if (l==null) return null;
		return l.stream().parallel()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}
	public static Map<ISymbol, Double> DFreq(List<ISymbol> l)
	{
		return DFreq(Freq(l));
	}
	public static double Norm(Map<ISymbol, Double> a, Map<ISymbol, Double> b)
	{
		double d=0.0;
		Set<ISymbol> c=new HashSet<ISymbol>();
		c.addAll(a.keySet());
		c.addAll(b.keySet());
		for(ISymbol s:c)
		{
			Double da=a.get(s);
			Double db=b.get(s);
			if (da==null)
				da=0.0;
			if (db==null)
				db=0.0;
			d+=Math.pow((da-db),2);
		}
		return Math.pow(d, 1.0/2);
		
	}
	public static Map<ISymbol, Double> DFreq(Map<ISymbol, Long> m) 
	{
		Map<ISymbol, Double> md=new HashMap<ISymbol, Double>();
		Long s=0L;
		for(Long l:m.values())
			s+=l;
		for(Entry<ISymbol, Long> e:m.entrySet())
		{
			md.put(e.getKey(), e.getValue().doubleValue()/s.doubleValue());
		}
		return  md;
	}
	static public <K> Double getEntropied(Map<ISymbol, Double> freq)
	{
		double d=0.0;
		//double s=0.0;
		for(Entry<ISymbol, Double> e:freq.entrySet())
		{
			d+=e.getValue()*Math.log(e.getValue())/Math.log(2);
			//s+=e.getValue();
		}
		//System.out.println("s" +s);
		return -d;
	}
	static public <K> Double getEntropie(Map<K, Long> freq) 
	{
		return HuffmanCode.getEntropie( freq);
	}
	static public <K> Double getEntropie(Collection<K> list) 
	{
		if (list==null) return null;
		return HuffmanCode.getEntropie( list.stream().parallel()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting())));
	}
/** Factory that generate an ISymbol from it id and current sprout object
	 * */
	ISymbol Factory(Long nId);
	
	
	/**
	 * return the length in bit of the list according to the coding rules : cs
	 */
	public static Long length(List<ISymbol> ls, ICodingRule cs) {
		 Map<ISymbol, Long> m=Freq(ls);
		 Long l=0L;
		
		for(Entry<ISymbol, Long> e:m.entrySet())
		{	
			ISymbol sym=e.getKey();
			ICode c=cs.get(sym);
			l+=(c.length())*e.getValue();
		}
		return l;
	}

	/**
	 * return the length in bit of the list
	 */
	static public Long length(List<ISymbol> ls) {
		Map<ISymbol, Long> m=Freq(ls);
		Long l=0L;
		
		for(Entry<ISymbol, Long> e:m.entrySet())
			 l+=(e.getKey()).getCode().length()*e.getValue();
		return l;
	}
	
}