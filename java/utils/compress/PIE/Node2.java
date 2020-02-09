package com.zoubworld.java.utils.compress.PIE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;

public class Node2<T extends Comparable<? super T>,V> {
	static long count=0;// count the total number of node added.
	 
	private V index;
	private T symbol;        
    private Node2<T,V> parent;
    private Map<T,Node2<T,V>> childrens;
    public Node2(Node2<T,V> newParent,V newIndex,T newSymbol) {
        index = newIndex;
        setSymbol(newSymbol);
        parent=newParent;
           
    }

    public String toString()
    {
    	return symbol+"("+getIndex()+")";
    	/*
    //	return symbol+"("+childrens.size()+","+length()+")";
    	StringBuffer  s=new StringBuffer();
    	//childrens=SortMapBykey(childrens);
    	//childrens.remove(null);
    	if(childrens==null || childrens.values().size()==0)
    	{
    	if (symbol!=null)
    	s.append("\r\n"+symbol);
    	else
    		s.append("");
    	}
    	else
    	for(T ee:(getChildrens().keySet()))
    	{
    		Node2<T,V> e=getChildrens().get(ee);
    		try {
    		if (e.getSymbol()!=Symbol.findId('\n') && e.getSymbol()!=Symbol.findId('\\') && symbol!=Symbol.findId('$'))
    		s.append(e.toString().replaceAll("\r\n", "\r\n"+((symbol==null)?"":(symbol.toString()))));
    		}
    		catch(java.lang.IllegalArgumentException ex)
    		{
    			System.err.println("for symbol '"+e.getSymbol().toString()+"' or '"+this.getSymbol()+"'");
    			ex.printStackTrace();
    		};
    	}
    		return s.toString();*/
    }
    /**
	 * @return the childrens
	 */
	public Map<T, Node2<T, V>> getChildrens() {
		if(childrens==null)
			childrens = new HashMap<T,Node2<T,V>>();     
		return childrens;
	}
	public  Node2<T, V> getChildren(T key) {
		return getChildrens().get(key);		
	}	
	public Long length()
    {
    	if (parent==null)
    		return 0L;
    	return parent.length()+1;
    }
    public List<T> getSymbols() {
    	List<T> ls;
    	if (parent==null)
    		 ls=new ArrayList<T>();
    	else
    	{
    		ls=parent.getSymbols();
    		ls.add(symbol);
    	}
    	return ls;
	}
    public Node2<T,V> get(T symbolSearched)
    {
    	if (childrens==null)
    		return null;
    	return getChildrens().get(symbolSearched);
    }
    public Node2<T,V> add( Node2<T,V> n)
    {
    	if(this.equals(n))
    		return this;
    	if(getRootNode().getChildrens().values().contains(n))
    		return this;
    	if(get(n.getSymbol())!=null)
    	{
    		for(Node2<T, V> cn:n.getChildrens().values())
    		get(n.getSymbol()).add(cn);
    	}	else
    			getChildrens().put(n.getSymbol(), n);
		return n;
    	
    }
    public Node2<T,V> add(V newIndex,T newSymbol)
    {
    	Node2<T,V> child ;
    	if ((child=getChildrens().get(newSymbol))==null) {
    	child = new Node2<T,V>(this,newIndex,newSymbol);
    	getChildrens().put(newSymbol,child);
    	count++;
    	}
    	return child;
    }
	/**
	 * @return the parent
	 */
	public Node2<T, V> getParent() {
		return parent;
	}

	public T getSymbol() {
		return symbol;
	}
	/** return the node just after the root of the tree.
	 * 
	 * @return
	 */
	public Node2<T,V> getRootNode()
	{
		if ((parent!=null) && (parent.getSymbol()!=null))
			return parent.getRootNode();
		return this;
	}
	
	/** return Nodes on the branch associated to the symbol 
	 * */
	public List<Node2<T,V>> getNodes(T symbolold)
	{
		Node2<T,V> node=this;
		List<Node2<T, V>> l=new ArrayList<Node2<T, V>>();
		if(symbolold==null)
			return l;
		while((node=node.parent)!=null)
		{
			if (symbolold.equals(node.getSymbol()))
				l.add(node);
		}
		return l;
	}
	/** return th list od node from leaf to root
	 * */
	public List<Node2<T,V>> getNodes()
	{
		Node2<T,V> node=this;
		List<Node2<T, V>> l=new ArrayList<Node2<T, V>>();
		while((node=node.parent)!=null)
		{
				l.add(node);
		}
		return l;
	}
	/*
abcdabcf
abcf
bcda
cda
da
A
{a}
ababababc
abc
ababc
babc
babababc
bc
c
*/
	
/*
 * rootnode=getRootNode();
	List<Node2<T,V>>  l=getNodes(rootnode.getSymbol())
	l=l.reverse();
	
for(i=0,i<len,i++){
	tree.addtree(l);l.remove(0);}
		*/	
	public V getIndex() {
		return index;
	}
	public void setSymbol(T symbol) {
		this.symbol = symbol;
	}
	/** add to tree symbol from index1 to index2 to build the tree
	 * @return 
	 * */
	public Node2<T, V> add2(Tree<T,V> tree,Long index1,Long index2, List<T> ls) {
		if(index1.intValue()<index2)
		{
					int i=index1.intValue();
					T sym = ls.get(i);
			{
			if (get(sym)==null)
				return this.add((V)Long.valueOf(i), sym).add2(tree,Long.valueOf(i+1),index2, ls);
			else
				return this.get(sym).add2(tree,Long.valueOf(i+1),index2, ls);
			}
		}
		return this;		
	}
	/** add a branch ls to the tree with starting index index1
	 * */
	public Node2<T, V> add(Tree<T,V> tree,Long index1, List<T> ls) {
		int i=index1.intValue();
		
		if(ls.size()>1)
		{
			T sym = ls.get(0);
			{
				 
			if (get(sym)==null)
				return this.add((V)Long.valueOf(i), sym).add(tree,Long.valueOf(i+1),ls.subList(1, ls.size()));
			else
				return this.get(sym).add(tree,Long.valueOf(i+1), ls.subList(1, ls.size()));
			}
		}
		else
		
			if(ls.size()==1)
			{
				T sym = ls.get(0);
				if (get(sym)==null)
					return this.add((V)Long.valueOf(i), sym);
			}
		return this;		
	}
	
	public String toGraphViz() {
		
		String s="";
		if(Tree.cached.contains(this))
			return "";
		/*if(getRootNode().getChildrens().values().contains(this))
			return "";*/
		for( T c:getChildrens().keySet())
		if(!Tree.cached.contains(getChildren(c)))
		{
		s+="\""+getSymbol()+"_"+getIndex()+"_"+hashCode()+"\" -> \""+getChildren(c).getSymbol()+"_"+getChildren(c).getIndex()+"_"+getChildren(c).hashCode()+"\"\r\n";
		}
		Tree.cached.add(this);
			for( T c:getChildrens().keySet())	
			if(!Tree.cached.contains(getChildren(c)))
			s+=getChildren(c).toGraphViz();
		return s;
		
	}

	public List<T> tolist(int s) {
		List<T> l=new ArrayList();
		Node2<T, V> n = this;
		do
		{
			l.add(0, n.getSymbol());
			s--;
		}
		while ((n=n.getParent())!=null&&s>0);
		return l;
	}
	
}
