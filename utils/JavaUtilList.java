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

	/* selection inside a string list, a sublist that match with regExp
	 * */
	public static List<String> Select(Collection<String> ls, String regExp) {
		List<String> cs = new ArrayList<String>();


		Pattern p = Pattern.compile(regExp);
	
		for (String c : ls)
			if (( p.matcher(c)).matches())
				cs.add(c);
		return cs;
	}

	public static List<Double> StringToDoubleList(Collection<String> ls) {
		if (ls == null)
			return null;
		if (ls.size() == 0)
			return new ArrayList<Double>();
		/*
		 * try { return ls.parallelStream() .map(s-> (((s)==null ||
		 * s.trim().equals(""))?(Double)null:Double.valueOf(s))
		 * 
		 * ).collect(Collectors.toList()); } catch (java.lang.NumberFormatException e) {
		 */

		return ls.parallelStream().map(s -> {
			try {
				return (((s) == null || s.trim().equals("")) ? (Double) Double.NaN
						: (s.equals("inf") ? Double.POSITIVE_INFINITY
								: (s.equals("-inf") ? Double.NEGATIVE_INFINITY
										: (s.equals("NA") ? Double.NaN : Double.valueOf(s)))));
			} catch (java.lang.NumberFormatException e2) {

				e2.printStackTrace();
				System.err.println("NumberFormatException : '" + listToSet(ls).toString() + "'");
				return (Double) null;
			}
		}).collect(Collectors.toList());
		/* } */
		/*
		 * //***rework to work with NA** ls=listToSet(ls);
		 * System.err.println("NumberFormatException : '"+ls.toString()+"'"); return new
		 * ArrayList<Double>();
		 */

		/*
		 * List<Double> ld=new ArrayList(); for(String s:ls) try {
		 * ld.add(Double.valueOf(s)); } catch (java.lang.NumberFormatException e) {
		 * System.err.println("NumberFormatException : '"+s+"'"); ld.add( null); }
		 * return ld;
		 */
	}

	/**
	 * check is the majority of the list is numerical number
	 * an empty list is false
	 */
	public static boolean IsNumberList(Collection<String> ls) {
		if (ls == null)
			return false;
		if (ls.size() == 0)
			return false;
		/*
		 * take 22.313s List<Double> ld=new ArrayList(); int counttrue=0; int
		 * countfalse=0; for(String s:ls) try { ld.add(Double.valueOf(s)); counttrue++;
		 * } catch (java.lang.NumberFormatException e) { countfalse++; } return
		 * counttrue>countfalse;
		 */

		// reduce the data by set
		// this can save a lot of lamba expression time
		if (!Set.class.isInstance(ls)) {
			Set<String> stmp = new HashSet<String>();
			stmp.addAll(ls);
			ls = stmp;
		}
		{

			Integer countTrue = ls.parallelStream().map(x ->
			/*
			 * take 500ms but only int (StringUtils.isNumericSpace(x)?1:0)
			 */
			{
				try {
					Double.valueOf(x);
					return 1;
				} catch (java.lang.NumberFormatException e) {
					return 0;
				}
			}).mapToInt(Integer::valueOf).sum();
			// 72s .collect(Collectors.summingInt(Integer::intValue));
			// 69s $1 .reduce(0, Integer::sum);
			int countFalse = ls.size() - countTrue;
			return countTrue > countFalse;
		}

	}

	public static boolean IsIntegerList(Collection<String> ls) {
		/*
		 * take 22.313s List<Double> ld=new ArrayList(); int counttrue=0; int
		 * countfalse=0; for(String s:ls) try { ld.add(Double.valueOf(s)); counttrue++;
		 * } catch (java.lang.NumberFormatException e) { countfalse++; } return
		 * counttrue>countfalse;
		 */
		// reduce the data by set
		// this can save a lot of lamba expression time
		Set<String> stmp = new HashSet<String>();
		stmp.addAll(ls);
		ls = stmp;
		{// take 500ms
			Integer countTrue = ls.parallelStream().map(x -> (StringUtils.isNumeric(x) ? 1 : 0)
			/*
			 * {try { Double.valueOf(x); return 1; } catch (java.lang.NumberFormatException
			 * e) { return 0; }}
			 */).mapToInt(Integer::valueOf).sum();
			// 72s .collect(Collectors.summingInt(Integer::intValue));
			// 69s $1 .reduce(0, Integer::sum);
			int countFalse = ls.size() - countTrue;
			return countTrue > countFalse;
		}

	}

	public static int count(Collection<String> ls, String s) {

		int count = 0;
		if (s != null)
			for (String e : ls)
				if (e != null)
					if (s.equalsIgnoreCase(e))
						count++;
		return count;
	}

	/**
	 * Min computation excluding null and NaN values
	 */
	public static Double Min(Collection<Double> ld) {
		if (ld == null || ld.size() == 0)
			return null;
		ld = listToSet(ld);
		Double a = null;
		for (Double d : ld)
			if (d != null && d != Double.NaN /* && d!=Double.NEGATIVE_INFINITY && d!=Double.POSITIVE_INFINITY */) {
				if (a == null)
					a = d;
				else if (d != null)
					if (d < a)
						a = d;
			}
		return a;
	}

	/**
	 * Max computation excluding null and NaN values
	 */
	public static Double Max(Collection<Double> ld) {
		if (ld == null || ld.size() == 0)
			return null;

		if (!Set.class.isInstance(ld))
			ld = listToSet(ld);

		Double a = null;
		for (Double d : ld)
			if (d != null && d != Double.NaN /* && d!=Double.NEGATIVE_INFINITY && d!=Double.POSITIVE_INFINITY */) {
				if (a == null)
					a = d;
				else if (d != null)
					if (d > a)
						a = d;
			}
		return a;
	}

	/**
	 * std dev computation excluding null and NaN values
	 */
	public static Double StdDev(Collection<Double> ld) {
		if (ld == null || ld.size() == 0)
			return null;
		double sum = 0.0, standardDeviation = 0.0;
		int length = ld.size();
		int count = 0;
		for (Double d : ld) {
			if (d != null && d != Double.NaN /* && d!=Double.NEGATIVE_INFINITY && d!=Double.POSITIVE_INFINITY */)
			{
				sum += d;
				count++;
			}
		}

		double mean = sum / length;

		for (Double d : ld) {
			if (d != null && d != Double.NaN /* && d!=Double.NEGATIVE_INFINITY && d!=Double.POSITIVE_INFINITY */)

				standardDeviation += Math.pow(d - mean, 2);
		}

		return Math.sqrt(standardDeviation / count);
	}

	/**
	 * Average computation excluding null and NaN values
	 */
	public static Double Average(Collection<Double> ld) {
		if (ld == null || ld.size() == 0)
			return null;
		Double a = 0.0;
		int count = 0;
		for (Double d : ld)
			if (d != null && d != Double.NaN /* && d!=Double.NEGATIVE_INFINITY && d!=Double.POSITIVE_INFINITY */) {
				a += d;
				count++;
			}
		return a / count;
	}

	/**
	 * median computation excluding null and NaN values
	 */
	public static Double median(Collection<Double> cd) {
		if (cd == null || cd.size() == 0)
			return null;
		List<Double> ld = new ArrayList<Double>();

		for (Double d : cd)
			if (d != null && d != Double.NaN /* && d!=Double.NEGATIVE_INFINITY && d!=Double.POSITIVE_INFINITY */)
				ld.add(d);
		Collections.sort(ld);
		return ld.get(ld.size() / 2);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * convert a list into a set.
	 */
	public static <T> Set<T> listToSet(Collection<T> collection) {
		if (collection == null)
			return null;
		if (Set.class.isInstance(collection))
			return (Set<T>) collection;
		return collection.parallelStream().collect(Collectors.toSet());
		// guava : return Sets.newHashSet(collection);
		// new HashSet<>(list);
		/*
		 * Set<T> ss=new HashSet(); if(collection!=null) ss.addAll(collection); else
		 * return null; return ss;
		 */
	}

	/**
	 * count number of element not null
	 */

	public static <T> long count(Collection<T> l) {
		if (l == null)
			return 0;
		if (Set.class.isInstance(l))
			if (l.contains(null))
				return l.size() - 1;
			else
				return l.size();
		return l.parallelStream().filter(x -> x != null).count();
		/*
		 * 
		 * public static <T> int count(Collection<T> l ) { if (l==null) return 0;
		 * 
		 * List<T> t = new ArrayList();
		 * 
		 * for(T e:l) if(e!=null) t.add(e); return t.size();
		 */
	}

	/**
	 * compare to StringToDoubleList it is faster becasue the list is reduce to a
	 * set.
	 */
	public static Set<Double> StringToDoubleSet(Collection<String> datas) {
		return listToSet(StringToDoubleList(listToSet(datas)));
	}

	public static List<String> setToList(Set<String> set) {
		List<String> r = new ArrayList<String>();
		r.addAll(set);
		return r;
	}

	public static <T> Set<T> interSection(Collection<T> list, Collection<T> otherList) {
		Set<T> result = list.stream().distinct().filter(otherList::contains).collect(Collectors.toSet());

		return result;
	}

	public static <T> Set<T> union(Collection<T> list, Collection<T> otherList) {
		Set<T> result = new HashSet<T>();

		result.addAll(list);
		result.addAll(otherList);

		return result;
	}

	public static <T> Set<T> xor(Collection<T> list, Collection<T> otherList) {
		Set<T> result = JavaUtilList.union(list, otherList);
		result.removeAll(interSection(list, otherList));
		return result;
	}

}
