package com.zoubworld.java.math;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class Node
{
	Integer leaf;
	Collection<Node> branch;	
	public long countEndLeaf()
	{
		if (branch==null || branch.size()==0)
			return 1;
		long l=0;
		
		for(Node e:branch)
			l+=e.countEndLeaf();
		return l;
	}

	public Collection<Node> getEndLeaf()
	{
		Collection<Node>  c=new HashSet<Node>();//new ArrayList();
		if (branch==null || branch.size()==0)
			{c.add(this);return  c;}
		
		for(Node e:branch)
			c.addAll(e.getEndLeaf());
		return c;
	}
	public String toString()
	{
		String s="";
		if(leaf!=null)
		s+=leaf.toString();
		String tmp="";
		for(Node e: branch)
		{
			for(String es:e.toString().split("\n"))
		tmp+=s+","+es	+"\n";
		}
		if(branch.size()==0)
		return s;
		return tmp;
	}
	public Node() {
		branch=new ArrayList<Node>();
	}
	/**
	 * @param arg0
	 * @return
	 * @see java.util.Set#add(java.lang.Object)
	 */
	public boolean add(Node arg0) {
		return branch.add(arg0);
	}
	public Node(Integer i) {
		branch=new ArrayList<Node>();
		leaf=i;
	}
}
