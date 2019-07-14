/**
 * 
 */
package com.zoubworld.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author M43507
 *
 */
public class JavaUtilList {

	/**
	 * 
	 */
	public JavaUtilList() {
		// TODO Auto-generated constructor stub
	}
	/**/
	public static Collection<String> Select(Collection<String> ls,String regExp)
	{
		 Collection<String> cs= new ArrayList();
		 Pattern p = Pattern.compile(regExp);
		 Matcher m =null;
		 for(String c:ls)
			 if((m=p.matcher(c)).matches())
				 cs.add(c);
		return cs;
	}
	public static List<Double> StringToDoubleList(Collection<String> ls)
	{
		List<Double> ld=new ArrayList();
		for(String s:ls)
			try
		{
			ld.add(Double.valueOf(s));
		}
		catch (java.lang.NumberFormatException e)
		{
			System.err.println("NumberFormatException : '"+s+"'");
			ld.add( null);
		}
		return ld;
	}
	/** check is the majority of the list is numerical number
	 * */
	public static boolean IsNumberList(Collection<String> ls)
	{
		List<Double> ld=new ArrayList();
		int counttrue=0;
		int countfalse=0;
		for(String s:ls)
			try
		{
			ld.add(Double.valueOf(s));
			counttrue++;
		}
		catch (java.lang.NumberFormatException e)
		{
			countfalse++;
		}
		return counttrue>countfalse;
	}
	
	public static int count(Collection<String> ls,String s)
	{
		int count=0;
		if (s!=null)
		for(String e:ls)
		if (e!=null)			
			if (s.equalsIgnoreCase(e))
				count++;
		return count;
	}
	
	public static Double Min(Collection<Double> ld)
	{
		Double a=null;
		for(Double d:ld)
		if (a==null)
			a=d;
		else
			if ( d!=null)
				if (d<a)
					a=d;
		return a;
	}
	public static Double Max(Collection<Double> ld)
	{
		Double a=null;
		for(Double d:ld)
		if (a==null)
			a=d;
		else
			if ( d!=null)
				if (d>a)
					a=d;
		return a;
	}
	 public static Double StdDev(Collection<Double> ld)
	    {
	        double sum = 0.0, standardDeviation = 0.0;
	        int length = ld.size();
	        int count=0;
	        for(Double num : ld) {
	        	if (num!=null)
	        	{
	            sum += num;
	            count++;
	        	}
	        }

	        double mean = sum/length;

	        for(Double num: ld) {
	        	if(num!=null)
	            standardDeviation += Math.pow(num - mean, 2);
	        }

	        return Math.sqrt(standardDeviation/count);
	    }
	 
	public static Double Average(Collection<Double> ld)
	{
		Double a=0.0;
		int count=0;
		for(Double d:ld)
			if ( d!=null)
				{a+=d;count++;}				
		return a/count;
	}
	public static Double median(Collection<Double> cd)
	{
		List<Double> ld=new ArrayList();
				
				for(Double c:cd)
					if(c!=null)
					ld.add(c);
		 Collections.sort(ld); 				
		return ld.get(ld.size()/2);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	/** convert a list into a set.
	 * */
	public static <T>  Set<T> listToSet(Collection<T> collection) {
		Set<T> ss=new HashSet();
		ss.addAll(collection);
		
		return ss;
	}
	/** count number of element not null
	 * */
	public static <T>   int count(List<T> l ) {
		List<T> t = new ArrayList();
		for(T e:l)
			if(e!=null)
		t.add(e);
		return t.size();
	}

}
