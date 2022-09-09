package com.zoubworld.utils;

import java.util.Comparator;
/** this clas allow compare with several criteria,
 * first acomp is used if equal then we check bcomp, if equal then we check ccomp, .. up to ecomp
 *  */
public class ComposedComparator<T> implements Comparator<T>
{

	Comparator<T> a;
	Comparator<T> b;
	Comparator<T> c;
	Comparator<T> d;
	Comparator<T> e;
	
	public ComposedComparator(Comparator<T> acomp) {
		 a=acomp;
	}
	public ComposedComparator(Comparator<T> acomp,Comparator<T> bcomp) {
		a=acomp;
		b=bcomp;
		 
	}
	public ComposedComparator(Comparator<T> acomp,Comparator<T> bcomp,Comparator<T> ccomp) {
		a=acomp;
		b=bcomp;
		c=ccomp;
		 
	}
	public ComposedComparator(Comparator<T> acomp,Comparator<T> bcomp,Comparator<T> ccomp,Comparator<T> dcomp) {
		a=acomp;
		b=bcomp;
		c=ccomp;
		d=dcomp;		 
	}
	public ComposedComparator(Comparator<T> acomp,Comparator<T> bcomp,Comparator<T> ccomp,Comparator<T> dcomp,Comparator<T> ecomp) {
		a=acomp;
		b=bcomp;
		c=ccomp;
		d=dcomp;	
		e=ecomp;	
		
	}

	@Override
	public int compare(T o1, T o2) {
		int i=0;
		if(a!=null)
			i=a.compare(o1, o2);
		if(i==0) if(b!=null)
			i=b.compare(o1, o2);
		if(i==0) if(c!=null)
			i=c.compare(o1, o2);
		if(i==0) if(d!=null)
			i=d.compare(o1, o2);
		if(i==0) if(e!=null)
			i=e.compare(o1, o2);		
		return i;
	}
	

}
