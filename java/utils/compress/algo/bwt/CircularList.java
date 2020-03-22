package com.zoubworld.java.utils.compress.algo.bwt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
/** this class  is created to optimize the BWT algorithm
 * https://en.wikipedia.org/wiki/Burrows%E2%80%93Wheeler_transform
 * https://fr.wikipedia.org/wiki/Transform%C3%A9e_de_Burrows-Wheeler
 * */
public class  CircularList<T extends Comparable<T>> implements Comparable<CircularList<T>>,Comparator<CircularList<T>>  
	{
		private T[] elements;

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (CircularList.class.isInstance(obj))
				return compareTo((CircularList<T> )obj)==0;
			return super.equals(obj);
		}
		public CircularList(T... elements) {
			this.elements = elements;
			this.end=elements.length;
			this.pos=0;
			this.begin =0;
		}
		public CircularList(List<T> elements) {
			this.elements = (T[]) elements.toArray((T[]) new Comparable[0]);
			this.end=elements.size();
			this.pos=0;
			this.begin =0;
		}
		public CircularList(int size2) {
			this.elements = (T[]) new Comparable[size2];
			this.end=size2;
			this.pos=0;
			this.begin =size2;
		}
		/** clone the l object, but with a rotation at position pos
		 * */
		public CircularList(CircularList<T> l,int pos) {
			this.elements = l.elements;
			this.end = l.end;
			this.begin = l.begin;			
			end=elements.length;
			this.pos=(end+pos)% end;
		}
		/** create a CircularList of size size2
		 * */
		public CircularList(Class<T> type,int size2) {
			this.elements = (T[]) java.lang.reflect.Array.newInstance(type, size2);
			this.end=size2;
			this.begin=size2;
			this.pos=0;
		}
		int end=0;//end of circular buffer at end-1
		int begin=0;//begin of circular buffer
		int pos=0;// represent the position from begin(0) up to end(size()-1)
		/** get the element at index i % size(). */
		public T get(int i) {
			assert i>=begin;
			assert end>i;
			return elements[((i+pos) % size())+begin];
		}

		/** return the first index of the object o in the circular list, -1 if the
		 ** object is not present. Note that an element of the circular list may be 
		 ** null. */
		public int indexOf(T o) {
			for (int i = begin; i < end; i++) {
				if (get( i).equals( o))
					return (i-begin);
			}
			return -1;
		}

		/** number of elements contained in the circular list. */
		public int size() {
			assert end>=begin;
			return end-begin;
		}
		public String toString() {
			String s=" ";
		
			for (int i = 0; i < size(); i++) 
				s+=get(i).toString()+"," ;
			return "("+s+")";
		}
		public T getlast() {
			assert end<=elements.length;
			return get(end-1);
		}
		@Override
		public int compareTo(CircularList<T> b) {
			return compare(this,b);
		}
		public List<T> decode(int indexRef,List<T> cl) {
		//	long nano_startTime = System.nanoTime();
			//bull sort by T
			Map<T,List<Integer>> m=new HashMap<T,List<Integer>>();
			int index=0;
			for(T s:cl)//for each T, create the list of index
			{
				List<Integer> l = m.get(s);
				if (l==null)
				   m.put(s, l=new ArrayList<Integer> ());
				l.add(index++);
			}
			List<T> ol=new ArrayList<T>();
			ol.addAll(m.keySet());
			Collections.sort(ol);// sort the Ti Tj
			List<Integer> orderedIndex = new ArrayList<Integer> ();
			for(T s:ol)//merge
			orderedIndex.addAll(m.get(s));
			
			ol.clear();
			index=indexRef;
			for(int i=0;i<cl.size();i++)
			{
			index=orderedIndex.get(index);			
			ol.add(cl.get(index));
			}
		//	long nano_stopTime = System.nanoTime();
		//	System.out.println(">>time :"+(nano_stopTime-nano_startTime)/1000000.0);	
		
			return ol;		
		}
			public CircularList<T> decode2(int index,List<T> cl) {
				long nano_startTime = System.nanoTime();
				
				 List<CircularList<T>> tab=new ArrayList<CircularList<T>>(cl.size());
			 
				for(int i=0;i<cl.size();i++)
					tab.add(new CircularList<T>(cl.size()));
				
				for(int j=0;j<cl.size();j++)
				{
					for(int i=0;i<cl.size();i++)
							tab.get(i).add(cl.get(i));
					
				/*	System.out.println(">");
					for(int i=0;i<tab.size();i++)
						System.out.println(tab.get(i));	*/
					Collections.sort( tab);
					/*System.out.println("<>");	
				for(int i=0;i<tab.size();i++)
					System.out.println(tab.get(i));	*/
				
				}
				long nano_stopTime = System.nanoTime();
				/*	for(int i=0;i<tab.size();i++)
					System.out.println(tab.get(i));	*/
				System.out.println(">>time :"+(nano_stopTime-nano_startTime)/1000000.0);	
			
			return tab.get(index);
		 }
			 public  List<T> encode(CircularList<T> cl) {
//				long nano_startTime = System.nanoTime();
				 
			 List<CircularList<T>> tab=new ArrayList<CircularList<T>>(cl.size());
				for(int i=0;i<cl.size();i++)
					tab.add(new CircularList<T>(cl,-i));
				
				Collections.sort( tab);
				
				int index=tab.indexOf(cl);
					
					List<T> lr = new ArrayList<T>();
					
					for(int i=0;i<tab.size();i++)
						lr.add(tab.get(i).getlast());
//				long nano_stopTime = System.nanoTime();
					/*	for(int i=0;i<tab.size();i++)
						System.out.println(tab.get(i));	*/
//					System.out.println("time :"+(nano_stopTime-nano_startTime)/1000000.0);	
					Index=index;
//				System.out.println(index+""+lr.subList(0,100).toString());				
			return lr;
		}
			 Integer Index;
		@Override
		 public int compare(CircularList<T> a, CircularList<T> b) {
        	int i=0;
        	int name =0;
        	while(i<a.size() && name==0)
        	{
        		name = a.get(i).compareTo(b.get(i));i++;	                
            } 
        	if(name!=0)
        		return name;
        	
        	return a.size()-b.size();
        	
        }
		public void add(T string) {
			
			assert (end-begin)>=0;
			if ((end-begin)==elements.length)
				throw new IndexOutOfBoundsException("--");
			begin--;
			assert (begin)>=0;
			elements[begin]=string;
			assert (end-begin)<=elements.length;
			
			
		}
		public int getIndex() {
			// TODO Auto-generated method stub
			return Index;
		}
		
		
	}