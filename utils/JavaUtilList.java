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
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

/**
 * @author pierre valleau
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
	/*	try
		{
		return ls.parallelStream()
		.map(s->
		(((s)==null || s.trim().equals(""))?(Double)null:Double.valueOf(s))
			
			).collect(Collectors.toList());
		}
		catch (java.lang.NumberFormatException e)
		{	*/
			
			return ls.parallelStream()
					.map(s->{try
					{
					return (((s)==null || s.trim().equals(""))?(Double)null:Double.valueOf(s));
					}
					catch (java.lang.NumberFormatException e2)
					{
						e2.printStackTrace();
						System.err.println("NumberFormatException : '"+ls.toString()+"'");
						return (Double)null;
					}
					}	).collect(Collectors.toList());
				/*	}*/
			/*
			//***rework to work with NA**		
			ls=listToSet(ls);
			System.err.println("NumberFormatException : '"+ls.toString()+"'");
			return  new ArrayList<Double>();*/
		
		/*
		 * List<Double> ld=new ArrayList();
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
		return ld;*/
	}
	/** check is the majority of the list is numerical number
	 * */
	public static boolean IsNumberList(Collection<String> ls)
	{
	/*	take 22.313s
	 * List<Double> ld=new ArrayList();
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
		return counttrue>countfalse;*/

		//reduce the data by set
		// this can save a lot of lamba expression time
		Set<String> stmp=new HashSet();
		stmp.addAll(ls);
		ls=stmp;
		
		{
			
		Integer countTrue =
		 ls.parallelStream().map(x->
		 /* take 500ms but only int
		 (StringUtils.isNumericSpace(x)?1:0)*/
		 {try
		{
					Double.valueOf(x);
			return 1;
		}
		catch (java.lang.NumberFormatException e)
		{
			return 0;
		}})
		 .mapToInt(Integer::valueOf)
		 .sum();
		//72s  .collect(Collectors.summingInt(Integer::intValue));
		//69s $1 .reduce(0, Integer::sum);
		int countFalse=ls.size()-countTrue;
		return countTrue>countFalse;
		}
		
	}
	
	public static boolean IsIntegerList(Collection<String> ls)
	{
	/*	take 22.313s
	 * List<Double> ld=new ArrayList();
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
		return counttrue>countfalse;*/
		//reduce the data by set
		// this can save a lot of lamba expression time
				Set<String> stmp=new HashSet();
				stmp.addAll(ls);
				ls=stmp;
		{// take 500ms
		Integer countTrue =
		 ls.parallelStream().map(x->
		 (StringUtils.isNumeric(x)?1:0)
		 /*{try
		{
					Double.valueOf(x);
			return 1;
		}
		catch (java.lang.NumberFormatException e)
		{
			return 0;
		}}*/)
		 .mapToInt(Integer::valueOf)
		 .sum();
		//72s  .collect(Collectors.summingInt(Integer::intValue));
		//69s $1 .reduce(0, Integer::sum);
		int countFalse=ls.size()-countTrue;
		return countTrue>countFalse;
		}
		
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
		if(ld==null || ld.size()==0)
			return null;
		ld=listToSet(ld);
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
		if(ld==null || ld.size()==0)
			return null;
		ld=listToSet(ld);
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
			if(ld==null || ld.size()==0)
				return null;
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
		if(ld==null || ld.size()==0)
			return null;
		Double a=0.0;
		int count=0;
		for(Double d:ld)
			if ( d!=null)
				{a+=d;count++;}				
		return a/count;
	}
	public static Double median(Collection<Double> cd)
	{
		if(cd==null || cd.size()==0)
			return null;
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
		if(collection!=null)
		return collection.parallelStream().collect(Collectors.toSet());
		return null;
		/*
		Set<T> ss=new HashSet();
		if(collection!=null)
		ss.addAll(collection);
		else
			return null;
		return ss;*/
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
	/** compare to StringToDoubleList it is faster becasue the list is reduce to a set.
	 * */
	public static Set<Double> StringToDoubleSet(Collection<String> datas) {		
		return listToSet(StringToDoubleList(listToSet(datas)));
	}

}
