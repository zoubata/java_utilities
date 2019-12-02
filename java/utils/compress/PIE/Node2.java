package com.zoubworld.java.utils.compress.PIE;

import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.utils.JavaUtils;

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
    		return s.toString();
    }
    /**
	 * @return the childrens
	 */
	public Map<T, Node2<T, V>> getChildrens() {
		if(childrens==null)
			childrens = new HashMap<T,Node2<T,V>>();     
		return childrens;
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
    		 ls=new ArrayList();
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
		List<Node2<T, V>> l=new ArrayList();
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
		List<Node2<T, V>> l=new ArrayList();
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
	 * */
	public void add2(Tree<T,V> tree,Long index1,Long index2, List<T> ls) {
	//	for(int i=index1.intValue();i<index2;i++)
		if(index1.intValue()<index2)
		{
		//	if(i==index1.intValue())
			int i=index1.intValue();
			{
			if (get(ls.get(i))==null)
				this.add((V)Long.valueOf(i), ls.get(i)).add2(tree,Long.valueOf(i+1),index2, ls);
			else
				this.get(ls.get(i)).add2(tree,Long.valueOf(i+1),index2, ls);
			}
		//	else
			
	//		tree.getRoot().add2(tree, Long.valueOf(i+1),index2, ls);
		}
		
	}
	
}
