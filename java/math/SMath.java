/**
 * 
 */
package com.zoubworld.java.math;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Pierre Valleau
 *
 * utility function.
 */
public class SMath {

	/**
	 * 
	 */
	public SMath() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

public static List<Integer> toInt(List<String> colunm) {
	return colunm.stream()
            .map(Integer::valueOf)
            .collect(Collectors.toList());
}
public static <T extends Number > T min(List<T> val) {
	if (val==null || val.isEmpty())
		return null;
T min=val.get(0);
	for(T e: val)
		if(e.doubleValue()<min.doubleValue())
			min=e;
	return min;	
}
public static <T extends Number > T min(Collection<T> val) {
	if (val==null || val.isEmpty())
		return null;
T min=null;
	for(T e: val)
		if(min==null)
			min=e;
		else
		if(e.doubleValue()<min.doubleValue())
			min=e;
	return min;	
}/*
public static <T extends Number > T min(Set<T> val) {
	if (val==null || val.isEmpty())
		return null;
T min=null;
	for(T e: val)
		if(min==null) min=e;else
		if(e.doubleValue()<min.doubleValue())
			min=e;
	return min;	
}*/
public static    List<Double> add(List<Double> val, Double add) {
	List<Double> val2=new ArrayList<Double>();
	for(Double e: val)
	val2.add((e.doubleValue()+add.doubleValue()));	
	return val2;	
}
public static    List<Integer> add(List<Integer> val, Integer add) {
	List<Integer> val2=new ArrayList<Integer>();
	for(Integer e: val)
	val2.add((e.intValue()+add.intValue()));	
	return val2;	
}
public static <T extends Number > T max(List<T> val) {
	if (val==null || val.isEmpty())
		return null;
T max=val.get(0);
	for(T e: val)
		if(e.doubleValue()>max.doubleValue())
			max=e;
	return max;	
}
public static <T extends Number > T max(Collection<T> val) {
	if (val==null || val.isEmpty())
		return null;
T max=null;
	for(T e: val)
		if(max==null) max=e; else
		if(e.doubleValue()>max.doubleValue())
			max=e;
	return max;	
}
public static <T extends Number > Long count(Collection<T> val) {
	if (val==null || val.isEmpty())
		return 0L;

	return (long) val.size();	
}
public static <T extends Number > double sum(Collection<T> val) {
	if (val==null || val.isEmpty())
		return 0.0;
double max=0.0;
	for(T e: val)
			max+=e.doubleValue();
	return max;	
}
public static <T extends Number > double average(Collection<T> val) {
	
	return sum(val)/count(val);	
}

public static <T extends Number > double StandardDeviation(Collection<T> val) {
	 double mean = average( val) ;
	 double standardDeviation = 0.0;
	 for(T e: val)
         standardDeviation += Math.pow(e.doubleValue() - mean, 2);
     

     return Math.sqrt(standardDeviation/val.size());
}


public static List<Double> toDouble(List<String> colunm) {
	return colunm.stream()
            .map(Double::valueOf)
            .collect(Collectors.toList());
}
public static Set<Integer> toInt(Set<String> colunm) {
	return colunm.stream()
            .map(Integer::valueOf)
            .collect(Collectors.toSet());
}
public static Set<Double> toDouble(Set<String> colunm) {
	return colunm.stream()
            .map(Double::valueOf)
            .collect(Collectors.toSet());
}
}
