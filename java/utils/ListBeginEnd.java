/**
 * 
 */
package com.zoubworld.java.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Pierre Valleau
 *
 */
public class  ListBeginEnd<E> implements List<E> {
	
	List l;
	int beginindex=0;
	int endindex=0;
	/**
	 * @param o
	 * @return
	 * @see java.util.List#indexOf(java.lang.Object)
	 */
	public int indexOf(Object o) {
		return l.indexOf(o)-beginindex;
	}

	/**
	 * @return
	 * @see java.util.List#isEmpty()
	 */
	public boolean isEmpty() {
		return l.isEmpty();
	}

	/**
	 * @return
	 * @see java.util.List#iterator()
	 */
	public Iterator iterator() {
		return l.iterator();
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#lastIndexOf(java.lang.Object)
	 */
	public int lastIndexOf(Object o) {
		return l.lastIndexOf(o)-beginindex;
	}

	/**
	 * @return
	 * @see java.util.List#listIterator()
	 */
	public ListIterator listIterator() {
		return l.listIterator();
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#listIterator(int)
	 */
	public ListIterator listIterator(int index) {
		return l.listIterator(index);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#remove(int)
	 */
	public E remove(int index) {
		if(index==0)
			{beginindex++;return (E)l.get(beginindex-1);}
		else if(index==size()-1)
			 {endindex--;return (E)l.get(beginindex+index);}
		
		return (E)l.remove(index+beginindex);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#remove(java.lang.Object)
	 */
	public boolean remove(Object o) {
		endindex--;
		return l.remove(o);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection c) {
		return l.removeAll(c);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#retainAll(java.util.Collection)
	 */
	public boolean retainAll(Collection c) {
		return l.retainAll(c);
	}

	/**
	 * @param index
	 * @param element
	 * @return
	 * @see java.util.List#set(int, java.lang.Object)
	 */
	public Object set(int index, Object element) {
		return l.set(index+beginindex, element);
	}

	/**
	 * @return
	 * @see java.util.List#size()
	 */
	public int size() {
		return (endindex+1-beginindex);
	}

	/**
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 * @see java.util.List#subList(int, int)
	 */
	public List subList(int fromIndex, int toIndex) {
		return new ListBeginEnd(l,fromIndex+beginindex, toIndex+beginindex-1);
	}
	public String toString()
	{
		String s="[";
		for(int i=beginindex;i<=endindex;i++)
			s+=l.get(i)+",";
		return s+"]";
	}
	/**
	 * @return
	 * @see java.util.List#toArray()
	 */
	public Object[] toArray() {
		return l.subList(beginindex, endindex+1).toArray();
	}

	/**
	 * @param a
	 * @return
	 * @see java.util.List#toArray(java.lang.Object[])
	 */
	public Object[] toArray(Object[] a) {
		return l.subList(beginindex, endindex+1).toArray(a);
	}

	/**
	 * @param index
	 * @param element
	 * @see java.util.List#add(int, java.lang.Object)
	 */
	public void add(int index, Object element) {
		l.add(index+beginindex, element);endindex++;
	}

	/**
	 * @param e
	 * @return
	 * @see java.util.List#add(java.lang.Object)
	 */
	public boolean add(Object e) {
		int i=endindex;
		endindex++;
		if(endindex>=l.size())
			return l.add(e);
		else
		 l.add(endindex,e);
		 return true;
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection c) {
		boolean b;
		if(endindex+1==l.size())
			b= l.addAll(c);
		else
		 b= l.addAll(endindex,c);
		if(beginindex==endindex)
			endindex--;
		endindex+=c.size();
		return b;
	}

	/**
	 * @param index
	 * @param c
	 * @return
	 * @see java.util.List#addAll(int, java.util.Collection)
	 */
	public boolean addAll(int index, Collection c) {
		boolean b=  l.addAll(index+beginindex, c);
		endindex+=c.size();
		return b;
	}

	/**
	 * 
	 * @see java.util.List#clear()
	 */
	public void clear() {
		l.clear();
		beginindex=endindex=0;
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.List#contains(java.lang.Object)
	 */
	public boolean contains(Object o) {
		return l.contains(o);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.List#containsAll(java.util.Collection)
	 */
	public boolean containsAll(Collection c) {
		return l.containsAll(c);
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.List#get(int)
	 */
	public E get(int index) {
		return (E)l.get(index+beginindex);
	}

	/**
	 * 
	 */
	public ListBeginEnd() {
		l=new ArrayList<E>(100);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + beginindex;
		result = prime * result + endindex;
		result = prime * result + ((l == null) ? 0 : l.hashCode());
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
	
if (!List.class.isAssignableFrom(obj.getClass()))
	return false;
List lo=(List) obj;
if ((endindex+1-beginindex)!=lo.size())
	return false;
int i=beginindex;
for(Object e:lo) 
{
	Object o=l.get(i++);
	if (!e.equals(o))
		return false;
}
		return true;
	}

	public ListBeginEnd(List l2, int i, int j) {
		l=l2;
		beginindex=i;
		endindex=j;
	}
	public ListBeginEnd(List l2) {
		l=l2;
		beginindex=0;
		endindex=l2.size()-1;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
