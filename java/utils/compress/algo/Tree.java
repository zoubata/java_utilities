package com.zoubworld.java.utils.compress.algo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.zoubworld.java.utils.compress.Number;
import org.apache.poi.ss.formula.functions.T;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.utils.JavaUtils;

public class Tree<T>
{
	private  Map<T,Tree<T>> tree=new HashMap<T,Tree<T>> ();//children of node.
	private int count=0;//number of occurency
	private Tree<T> parent=null;//father
	private T element=null;//the data stored in the tree and the stream.
	int process=0;
	/** process is a number used externaly to take decision
	 * 0 means undefined
	 * @return the process
	 */
	public int getProcess() {
		return process;
	}
	/** from tree of symbol do the histogram of frequency
	 * */
	public  Map<T, Long> Freq()
	{
		Map<T, Long> freq=new HashMap();
		for(T e:this.tree.keySet())
		{
			freq.put(e, (long)getChild(e).getCount());
		}
		return freq;
	}
	public  Map<List<T>, Long> Freq(int deep)
	{
		Map<List<T>, Long> freq=new HashMap<List<T>, Long>();
		for(Tree<T> e:this.getNodesAtDeep(deep))
		{
			freq.put(e.getWorde(), (long)e.getCount());
		}
		return freq;
	}
	
	public List<Tree<T>> getNodesAtDeep(int deep)
	{
		List<Tree<T>> l=new ArrayList<Tree<T>> ();
		if(deep==0)
			l.add(this);
		else
		for(Tree<T> t:tree.values())
			l.addAll(t.getNodesAtDeep(deep-1));
		return l;
	}
	/** process is a number used externaly to take decision
	 * 0 means undefined
	 * @param process the process to set
	 */
	public void setProcess(int process) {
		this.process = process;
	}
	/** process is a number used externaly to take decision
	 * 0 means undefined
	 * @param process the process to set for the full tree
	 */
	public void setTreeProcess(int process) {
		this.process = process;
		for(Tree<T> t:tree.values())
		{
			t.setTreeProcess(process);
		}
			}
	public static void MyProcess(Tree<ISymbol> root)
	{
		root.setTreeProcess(-10);
		 List<Tree<ISymbol>> l = root.Leafs();
		for(Tree<ISymbol> t:l)
		{
			List<Tree<ISymbol>> l2 = t.getWord();
			Tree<ISymbol> t2 = t.getParent();
			if (t2.getCount()==t.getCount())
				t.setProcess(-1);
			if (t2.getHeavy2()<t.getHeavy2())
				t.setProcess(-3);
			while(t2!=null)
			{	/*
				if (t2.getCount()>t.getCount())
					t2.setProcess(2);*/
				if (t2.getHeavy2()>t.getHeavy2())
					t2.setProcess(-2);
				t=t2;
				t2 = t.getParent();
			}
			
		}
	}
	/**
	 * @return the element
	 */
	public T getElement() {
		return element;
	}
	/**
	 * @param element the element to set
	 */
	public void setElement(T element) {
		this.element = element;
	}
	int index=-1;// position on the stream
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	/**
	 * @return the parent
	 */
	public Tree<T> getParent() {
		return parent;
	}
	public Tree(T element,Tree<T> parent,int index) {
		setElement(element);
		this.parent=parent;
		this.index=index;
	}
	public Tree() {
		
	}
	/**
	 * */
	Tree<T> find(List<T> l)
	{
		return find(0, l);
	}	
	/** return the size of the upper/top in direction of root
	 *  this return the number of node, or the length of the word inside the tree
	 * */
	private int size(Tree<T> root)
	{
		if(root==this)
			return 0;
		return 1+parent.size(root);
	}
	//short deep=-1;
	/** return the distance from the current node(a leaf) to the root
	 * this give the length of the current word.
	 * */
	public int getDeep()
	{/*
		if(deep>=0)
			return deep;*/
		int i=0;
		Tree<T> t = getParent();
		while (t!=null)
			{i++;t=t.getParent();}
		return /*deep=*/(short) i;
	}
	public int NbLeaf()
	{
		int i=0;
		if(tree.size()==0)
			i=1;
		else
		for(Tree<T> t:tree.values())
			i+=t.NbLeaf();
		return i;
	}
	/** return the 'word' of the noeud as a list of noeud*/
	public List<Tree<T>> getWord()
	{
		List<Tree<T>> l=new ArrayList<Tree<T>>();
		Tree<T> t=this;
		while(t!=null)
			{l.add(0,t);t=t.getParent();}
		return l;
	}
	/** return the 'word' of the noeud as a list of symbol*/
	
	public List<T> getWorde()
	{
		List<T> l=new ArrayList<T>();
		Tree<T> t=this;
		while((t!=null) && (t.getElement()!=null))
			{l.add(0,t.getElement());t=t.getParent();}
		return l;
	}
	
	/** return all leafs*/
	public List<Tree<T>> Leafs()
	{
		List<Tree<T>> l=new ArrayList<Tree<T>> ();
		if(tree.size()==0)
			l.add(this);
		else
		for(Tree<T> t:tree.values())
			l.addAll(t.Leafs());
		return l;
	}
	/** return the size of the below/bottom in direction of leaf
	 * this return the number of node, of the number of symbol stored inside the tree.
	 * */
	public int size()
	{
		int i=1;
		for(Tree<T> t:tree.values())
			i+=t.size();
		return i;
	}
	
	public Tree<T> find(int i,List<T> l)
	{
		Tree<T> fils = tree.get(l.get(i));
		if (fils!=null)			
			return fils.find( i+1, l);
		else
			return this;
	}
	public int fullfill(List<T> l ,int maxDeep)
	{
		int i=0;
		for(int j=0;j<l.size();j++)
			i=Math.max(i, fill(j, l ,maxDeep));
		return i;
	}
	/** add word at position index
	 *  
	 * */
	public int fill(int index,List<T> l,int maxDeep )
	{
		int len=Math.min(maxDeep-1,index);
		
		return fill(index-len,len+1,l );
	}
	
	/** remove all branch with an heavy less than heavyMin
	 * it free a lot of mem, and keep must efficient word.
	 * return the size of the tree
	 * */
	public int cleanup(Tree<T> root,int heavyMin,int countMin)
	{
	int i=0;
		
		if (tree.size()==0)
			i++;
		else
		{List<T> removeList=new ArrayList<T>();
		for(Tree<T> t:tree.values())
			if ((t.getHeavy2(root)<heavyMin)|| (t.count<countMin))
				removeList.add((T) t.element);
			else
				i+=t.cleanup( root, heavyMin,countMin);
		i++;
		for(T l:removeList)
				tree.remove(l);
		}
		return i;	
	}
	/** return the heavy of the tree below in direction of leaf
	 * */
	public int getHeavy()
	{
		int i=count;
		if(tree.size()==0)
			return count;
			else
		for(Tree<T> t:tree.values())
			i+=t.getHeavy();
		return i;
	}
	/** return the heavy of the tree upper in direction of root
	 * */
	int getHeavy(Tree<T> root)
	{
		return size(root)*count;
	}
	int getHeavy2(Tree<T> root)
	{
		return size(root)*(count-1);
	}
	int getHeavy2()
	{
		return getDeep()*(count-1);
	}
	/** add word at index of len T 
	 * */
	public int fill(int index,int len,List<T> l )
	{
		count++;
		if (len==0)
			return count;
		Tree<T> fils = tree.get(l.get(index));
		if (fils==null)
			tree.put(l.get(index),fils=new Tree<T>(l.get(index),this,index));		
		return fils.fill(index+1,len-1,l );		
	}
	/** print the branch from here to root*/
	public String toString()
	{
		return (parent==null?"":parent.toString())+(element==null?"":element.toString());
	}
	
	Tree<T> factorize(Tree<T> tree)
	{
		Tree<T> newroot= new Tree<T> ();/*
		start from roots, take a path, and search on root.
		merge into 1 branch:
			's''t''p''r''o''b''e''\''Q''L''T''C''4':25
			's''t''p''r''o''b''e''\''Q''L''T''C''4''-':25
			's''t''p''r''o''b''e''\''Q''L''T''C''4''-''1':11
			
		             'r''o''b''e''\''Q''L''T''C''4':25
			         'r''o''b''e''\''Q''L''T''C''4''-':25
			         
			                  'e''\''Q''L''T''C''4':25
			                  'e''\''Q''L''T''C''4''-':25
			                  
       EOS'1''s''t''p''r''o''b''e''\''Q''L''T''C''4''-':25
	   EOS'1''s''t''p''r''o''b''e''\''Q''L''T''C''4''-''1':11*/
		return newroot;
	}
	/** return the tree, 
	 * put the root on sky, and the leaf under ground
	 * so leaf become root and root become leaf.
	 * */
	public Tree returnTree()
	{
		Tree<T> newroot= new Tree<T> ();
		List<Tree<T>> lt = this.Leafs();
		for(Tree<T> t:lt)
		{
			Tree<T> root=newroot;
			while(t!=null)
			{
				
				
				Tree<T> fils = root.getChild(t.element);
				if (fils==null)
					root.tree.put(t.element,fils=new Tree<T>(t.element,root,t.index));	
				fils.count=t.count;
				t=t.parent;root=fils;
			}	}
		return newroot;
	}
	public Tree<T> getChild(T e)
	{ return tree.get(e);}
	/** print the tree
	 * 
	 * word:count-heavy-process*/
	public String toStringAll()
	{
		return toStringAll(this);
	}
	public String toStringAll(Tree<T> root)
	{
		String s="";
		
		if (tree.size()==0)
			s=toString()+":"+getCount()+"-"+getHeavy2(root)+"-"+getProcess()+"\r\n";
		else
		{
			s=toString()+":"+getCount()+"-"+getHeavy2(root)+"-"+getProcess()+"\r\n";
		for(Tree<T> t:tree.values())
			s+=t.toStringAll(root);
		}
		return s;
	}
	/** print node where process is upper/equal than p*/
	public String toStringProcessUp(int p)
	{
		String s="";
		
		if (tree.size()==0)
			{if (getProcess()>=p)
			s=toString()+":"+getCount()+"-"+getHeavy2()+"-"+getProcess()+"\r\n";
			}	else
		{
				if (getProcess()>=p)
			s=toString()+":"+getCount()+"-"+getHeavy2()+"-"+getProcess()+"\r\n";
		for(Tree<T> t:tree.values())
			s+=t.toStringProcessUp(p);
		}
		return s;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Tree<ISymbol> tree=new Tree<ISymbol>() ;
		List<ISymbol> ls = Symbol.from("ABCABCABCAABBCCABCABCABCAABBCC");
//		ls=Symbol.from(new File("C:\\Temp\\FAT\\2ndprobe\\NJLM4-14.pbs"));
		ls=Symbol.from(new File("C:\\Temp\\compression\\image_simple\\test24.jpg"));
		tree.fullfill(ls,8);
//		System.out.println(tree.toStringAll());
		System.out.println("ls size "+ls.size());
		System.out.println("tree.NbLeaf()="+tree.NbLeaf()+"tree.getHeavy()="+tree.getHeavy()+" tree.size()="+tree.size());
		tree.cleanup(tree, 1,2);
		
		System.out.println("tree.NbLeaf()="+tree.NbLeaf()+"tree.getHeavy()="+tree.getHeavy()+" tree.size()="+tree.size());
		Tree.MyProcess(tree);
		//System.out.println(tree.toStringProcessUp(-1));
	//	System.out.println(tree.toStringAll());
		List<ISymbol> lse = new ArrayList<ISymbol>();
		int index=0;
		List<Tree<ISymbol>> dico=new ArrayList<Tree<ISymbol>>();
		while(index<ls.size())
		{
			Tree<ISymbol> leaf = tree.find(index, ls);
			if ((leaf==null) || (leaf.getDeep()<=2))
				{lse.add(ls.get(index));index++;}
			else
			{
				Tree<ISymbol> leaf2 = leaf;
				while(leaf2!=null && (leaf2.getProcess()==-10))
				{
					leaf2=leaf2.getParent();
				}
				if(leaf2!=null)//don't take longest bug best.
					leaf=leaf2;
				
				if (leaf.getProcess()==-10)//if nothing found
				{
					lse.add(ls.get(index));
					index++;
					}
				else
					if (leaf.getProcess()<0)
						{
					List<ISymbol> word = leaf.getWorde();
				lse.addAll(word);						
				lse.add(Symbol.Mark);
				lse.add(new Number(word.size()));
				index+=word.size();
				
				leaf.setProcess(dico.size());
				dico.add(leaf);
				}
				else					
				{
					lse.add(Symbol.UseMark);	
					lse.add(new Number(leaf.getProcess()));
					index+=leaf.getDeep();
				}
			}
			
		}
	//	System.out.println("lse  "+lse);
		System.out.println("ls size "+ls.size());
		System.out.println("lse size "+lse.size());
		System.out.println("Symbol.Mark "+Symbol.count(lse,Symbol.Mark));
		System.out.println("Symbol.UseMark "+Symbol.count(lse,Symbol.UseMark));
		System.out.println("Symbol.INT4 "+Symbol.countId(lse,Symbol.INT4));
		System.out.println("Symbol.INT8 "+Symbol.countId(lse,Symbol.INT8));
		System.out.println("Symbol.INT12 "+Symbol.countId(lse,Symbol.INT12));
		System.out.println("Symbol.INT16 "+Symbol.countId(lse,Symbol.INT16));
		List<ISymbol> lse2 = new ArrayList<ISymbol>();
		/*
		System.out.println("freq(1)\r\n"+JavaUtils.SortMapByValue(tree.Freq()).toString().replaceAll(",","\r\n"));
		System.out.println("freq(2)\r\n"+JavaUtils.SortMapByValue(tree.Freq(2)).toString().replaceAll(", \\[","\r\n\\["));
		System.out.println("freq(3)\r\n"+JavaUtils.SortMapByValue(tree.Freq(3)).toString().replaceAll(", \\[","\r\n\\["));
		 */
	/*	for(ISymbol e:lse)
			if(e.getId()>255)
				lse2.add(e);	
		System.out.println("freq(1)\r\n"+JavaUtils.SortMapByValue(Symbol.Freq(lse2)).toString().replaceAll(",","\r\n"));
	*/
		for(ISymbol e:lse)
			if(e.getId()<=255)
				lse2.add(e);	
		System.out.println("lse2  "+Symbol.listSymbolToString(lse2));
		lse2.clear();
		lse2.add(Symbol.Mark);
		lse2.add(Symbol.UseMark);
		Map<ISymbol, List<ISymbol>> m = Symbol.split(lse,lse2);
		for(List<ISymbol> e:m.values())
			{Map<ISymbol, Long> f = Symbol.Freq(e);
			System.out.println("freq("+e.size()+"/"+f.size()+")\r\n"+JavaUtils.SortMapByValue(f).toString().replaceAll(",","\r\n"));
			}
		
	//	System.out.println(Symbol.listSymbolToString(m.get(Symbol.Empty)));
		System.out.println((m.get(Symbol.Mark)));
		
		//System.out.println(""+JavaUtils.Format(split(lse,lse2),"->","\r\n"));
	}
	
	/** display all leaf only
	 * */
	public String toStringLeaf() {
		List<Tree<T>> l = Leafs();
		String s="";
		for(Tree<T> t:l)
			s+=t.toString()+":"+t.getCount()+"\r\n";
		return s;
	}
}