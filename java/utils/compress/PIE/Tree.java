package com.zoubworld.java.utils.compress.PIE;

import java.util.ArrayList;
import java.util.List;


public class Tree<T extends Comparable<? super T>,V> {
    private Node2<T,V> root;

    public Tree() {
        setRoot(new Node2<T,V>(null,null,null));
    }

	public Node2<T,V> getRoot() {
		return root;
	}
	 public String toString()
	    { 
		 return root.toString();	    
	    }
	public void setRoot(Node2<T,V> root) {
		this.root = root;
	}
	/** add to tree symbol from index1 to index2 to build the tree
	 * */
	public void add(Long index1, Long index2, List<T> ls) {
		if(index2<=Long.valueOf(ls.size()))
		this.getRoot().add2(this,index1,index2,ls);
		
	}
	List<Node2<T,V>> ln=new ArrayList<Node2<T,V>>();
	
	/** add to tree ordored symbol
	 * */
	public List<Node2<T,V>> add(V index,T s) {
		
		List<Node2<T,V>> ln2=new ArrayList<Node2<T,V>>();
		
		for(Node2<T,V> n:ln)
			if(n.get(s)==null)
			ln2.add(n.add(index, s));
			else
				ln2.add(n.get(s));
		if(getRoot().get(s)==null)
			ln2.add(getRoot().add(index, s));
		else
			ln2.add(getRoot().get(s));
		ln=ln2;
		return ln2;
	}

	public void add(Node2<T, V> currentLeaf, V newIndex, T newSymbol) {

		 Node2<T,V> newnode= currentLeaf.add(newIndex, newSymbol);
		 if(currentLeaf!=root)
		 root.add(newIndex, newSymbol);
	}
}