/**
 * 
 */
package com.zoubworld.java.utils.compress.algo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zoubworld.java.math.SMath;
import com.zoubworld.java.utils.ListBeginEnd;
import com.zoubworld.java.utils.compress.CompositeSymbols;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.utils.JavaUtils;
import com.zoubworld.java.utils.compress.Number;
/**
 * @author Pierre Valleau
 *
 */
public class NodeTree {

	public static Comparator<NodeTree> compCount= (NodeTree player1, NodeTree player2) -> Long.compare(player1.getCount(), player2.getCount());
	public static Comparator<NodeTree> compCountPacked= (NodeTree player1, NodeTree player2) -> Long.compare(player1.getPackedcount(), player2.getPackedcount());

	/**
	 * 
	 */
	public NodeTree() {
		child = new HashMap<ISymbol, NodeTree>();
		sym = null;
		setCount(0L);
	}

	public NodeTree(ISymbol key) {
		sym = key;
		setCount(1L);
		child = new HashMap<ISymbol, NodeTree>();
	}

	public NodeTree(NodeTree parent, ISymbol key) {
		sym = key;
		setCount(0L);
		child = new HashMap<ISymbol, NodeTree>();
		this.parent = parent;
	}

	Map<ISymbol, NodeTree> child = null;
	NodeTree parent = null;
	ISymbol sym;
	private ISymbol symPacked = null;
	private Long Packedcount=null;
	private Long count;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<ISymbol> ls = Symbol.from(new File("C:\\Temp\\compression\\image_simple\\test24.bmp"));
		Map<ISymbol, Long> m = ISymbol.Freq(ls);
		int l;
		for(l=0;l<1000000;l++);
		Runtime rt = Runtime.getRuntime();
	    long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
	    
		System.out.println("list size=" + ls.size() + "alphabet size=" + m.size());
	//	System.out.println(JavaUtils.Format(JavaUtils.SortMapByValue(m), ":", "\r\n"));
		long time=System.nanoTime();
		usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
		System.out.println("usedMB "+usedMB+" Mo duration : "+ (System.nanoTime()-time)/1000000.0);
		ls = new ListBeginEnd(ls);
		NodeTree root = new NodeTree();
		int size = 3;
		//2 : 1.327s	0.744 		217 Mo
		//3 : 4.812s   	1.028s  	301 Mo
		//4 : 5.196s 	1.5s   		216 Mo
		//6 : 9.326  	6.311s 		294 Mo 
		//12 : 16.249  	15.186s   	868 Mo
		//16 : 31.625    25.937    1339 Mo 
		for (int fromIndex = 0; fromIndex + size < ls.size(); fromIndex++) {
			root.add((ListBeginEnd<ISymbol>)ls.subList(fromIndex, fromIndex + size));
			// if(fromIndex%100000==0) System.out.print(".");
		}
		List<NodeTree> pool=new ArrayList<NodeTree> ();
		
		usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
		System.out.println("usedMB "+usedMB+" Mo duration : "+ (System.nanoTime()-time)/1000000.0);
		System.out.println(root);
		List<NodeTree> ln = getLeafs(root);
		usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
		System.out.println("usedMB "+usedMB+" Mo duration : "+ (System.nanoTime()-time)/1000000.0);
		
		System.out.println();
		System.out.println("ln size" + ln.size());
		System.out.println("ls size" + ls.size());
		Comparator<NodeTree> comp = (NodeTree player1, NodeTree player2) -> Long.compare(player1.getCount(), player2.getCount());
		System.out.println("."+ln.size());
		ln = JavaUtils.asSortedSet(ln, comp);
		System.out.println(ln.subList(ln.size() - 100, ln.size() - 1));
		Collection<Long> s = m.values();
		s.remove(1L);
		Long min = SMath.min((Collection<Long>) s);

		int i = 0;// remove unefficient symbol.
		int limit=(int) (min / size);
		limit=size+4;
		for (; (i < ln.size()) && (ln.get(i).getCount() < limit); i++)
			;
		ln = ln.subList(i, ln.size());
		System.out.println("ls count="+ls.size()+"\r\nsize="+size+",\r\nmin count symbol="+min + ",\r\n limit=" +limit+",\r\nln(<limit)="+ i + ",\r\nln(>limit)=" + ln.size() + ":"+"\r\n\t"+JavaUtils.Format(ln,"\r\n\t"));
		usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
		System.out.println("usedMB "+usedMB+" Mo duration : "+ (System.nanoTime()-time)/1000000.0);
		
		int dico=0;
		List<ISymbol> lse =new ArrayList<ISymbol>();
		for( i=0;i<ls.size();)
		{
			int j=0;
			NodeTree node = root;
			NodeTree good = null;
			while ((node=node.getChild(ls.get(i+(j++))))!=null)
				good=node;
			
			if (good.getSymPacked()==null)
			{
				/*for(int k=0;k<j;k++)
				lse.add(ls.get(i+k));*/
				lse.add(ls.get(i));i++;
			/*	good.symPacked=new CompositeSymbols(Symbol.BTE, new Number(dico++));
				good.Packedcount=0L;*/
			}
			else
			{
					good.setPackedcount(good.getPackedcount() + 1);
					lse.add(good.getSymPacked());
					i+=j-1;
			}
		
		}
		/*
		 * IAlgoCompress rle=new RLE(3L); List<ISymbol> lsrle = rle.encodeSymbol(ls);
		 * System.out.println(lsrle.size()+"/"+ls.size()+"="+lsrle.size()*100.0/ls.size(
		 * )+"%  "+lsrle);
		 * 
		 * IAlgoCompress dico=new LZS(); List<ISymbol> lse = dico.encodeSymbol(ls);
		 * System.out.println(lse.size()+"/"+ls.size()+"="+lse.size()*100.0/ls.size()
		 * +"%  "+lse);
		 */
	}
	/** get the leaf */
	public static List<NodeTree> getLeafs(NodeTree root) {
		List<NodeTree> ln = new ArrayList<NodeTree>();
		if ((root==null))
			return ln;
		if ((root.child==null) || (root.child.size()==0))
		{
			ln.add(root);
			return ln;
		}
		for (NodeTree n1 : root.child.values()) {
			// System.out.print(".");
			
			ln.addAll(getLeafs(n1));
					
		}
		return ln;
	}
	/** get the leaf or branch at a deep of n */
	public static List<NodeTree> getLeafs(NodeTree root,int n) {
		List<NodeTree> ln = new ArrayList<NodeTree>();
		if ((root==null))
			return ln;
		if ((root.child==null) || (root.child.size()==0) || (n<=0))
		{
			ln.add(root);
			return ln;
		}
			
		for (NodeTree n1 : root.child.values()) {
			// System.out.print(".");
			
			ln.addAll(getLeafs(n1,n-1));
					
		}
		return ln;
	}
	/*
	 	public static List<NodeTree> getLeafs(NodeTree root) {
		List<NodeTree> ln = new ArrayList<NodeTree>();
		for (NodeTree n1 : root.child.values()) {
			// System.out.print(".");
			for (NodeTree n2 : n1.child.values())
				for (NodeTree n3 : n2.child.values())
					ln.add(n3);
		}
		return ln;
	}
	*/

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public NodeTree getChild(ISymbol key) {
		return child.get(key);
	}
protected boolean compact=true;//for debug
	public String toString() {
		String s;
		if (compact)
		{
			if(sym!=null)
			s= sym.toString().charAt(1)  + ":" + getCount();
			else
			s="?root";
		}
		else
		s= sym + ":" + getCount();
		NodeTree p = this.parent;
		while (p != null) {
			if (p.sym!=null)
				if (compact)
			s = p.sym.toString().charAt(1) /*+ "-"*/ + s;
				else
					s = p.sym.toString()+ "-" + s;
			p = p.parent;
		}
		return s;
	}

	public Set<ISymbol> getSet() {
		Set<ISymbol> s = new HashSet<ISymbol>();
		s.addAll(getList());
		return s;
	}
	

	public List<NodeTree> getListNode() {
		List<NodeTree> l = new ArrayList<NodeTree>();
		l.add(this);
		NodeTree p = this.parent;
		while (p != null) {
			if (p.sym!=null)
			l.add(0, p);
			p = p.parent;
		}
		return l;
	}
	public List<ISymbol> getList() {
		return getList(null);
	}
		public List<ISymbol> getList(NodeTree ans) {
				List<ISymbol> l = new ArrayList<ISymbol>();
		l.add(sym);
		NodeTree p = this.parent;
		while ((p != null)&&(p != ans)) {
			if (p.sym!=null)
			l.add(0, p.sym);
			p = p.parent;
		}
		return l;
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public NodeTree put(ISymbol key) {
		NodeTree value = getChild(key);
		if (value == null)
			value = Factory(this, key);
		else
		{}
		child.put(key, value);
		return value;
	}

	public NodeTree Factory(NodeTree nodeTree, ISymbol key) {
		
		return new NodeTree(this, key);
	}

	public NodeTree add(List<ISymbol> subList) {		
		return add(new ListBeginEnd(subList));		
	}
		public NodeTree add(ListBeginEnd<ISymbol> subList) {
				NodeTree n=null;
				IncCount();
		if (subList.size() > 0) {
			n = put(subList.remove(0));
			if (subList.size() > 0)
			n=n.add(subList);
			else n.IncCount();
		}
		return n;
	}
		/** sum of count of all  leafs multiply by deep
		 * it give an image of uncompressed file size
		 * */
	public Long TotalCount() {
		Long l=0L;
		for(NodeTree e:getLeafs(this))
			l+=e.getCount()*e.getDeep();
			return l;
		}
	
	public Long getDeep() {
			if (getParent()==null)
				return 0L;
			else
			return 1L+getParent().getDeep();
		}

	/** sum of count of all  leafs plus sum of all deep of leafs
	 * it give an image of compressed file size
	 * */
public Long TotalPackedCount() {
	Long l=0L;
	for(NodeTree e:getLeafs(this))
		l+=e.getCount()+e.getDeep();
		return l;
	}

public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}
	public void IncCount( ) {
		this.count++;
	}

	public ISymbol getSymPacked() {
		return symPacked;
	}

	public void setSymPacked(ISymbol symPacked) {
		this.symPacked = symPacked;
	}

	public Long getPackedcount() {
		return Packedcount;
	}

	public void setPackedcount(Long packedcount) {
		Packedcount = packedcount;
	}

	public void IncPackedcount() {
		if(Packedcount==null)
			Packedcount=0L;
		Packedcount++;
		
	}
	/** this function cut branch below n of count, and connect it at this.
	 * */
	public boolean refactor(int n) {
		return refactor(this, n); 
	}
	/** this function cut branch and connect it to rot if the count of branch is equal or below n
	 * return true if tree is modified.
	 * it could be needed to run several time the function to modify completely the tree.
	 * */
		public boolean refactor(NodeTree root,int n) {
			boolean b=false;
			Collection<NodeTree> s = new HashSet<NodeTree>(); 
					s.addAll(this.child.values());
			
		for(NodeTree e:s)
		{
			if ((e.getParent()!=root) && (e.getCount()<=n))
				{
				rm(e);				
				root.add(e);
				b=true;
				}
			else
				if (e.refactor(root, n))
					b=true;
		}
		
		return b;
	}
		/** add e branch here
		 * */
		protected void add(NodeTree e) {
			
			NodeTree c = getChild(e.getSym());
			if(c==null)
				{child.put(e.getSym(), e);e.parent=this;}
			else
			{
			for(NodeTree ec:e.getChild().values())
				c.add(ec);
			c.setCount(c.getCount()+e.getCount());
			e.parent=this;
			}
		}

		private void rm(NodeTree e) {
			this.child.remove(e.getSym());
			e.parent=null;
			
		}

		/**
		 * @return the child
		 */
		public Map<ISymbol, NodeTree> getChild() {
			return child;
		}

		/**
		 * @return the parent
		 */
		public NodeTree getParent() {
			return parent;
		}

		/**
		 * @return the sym
		 */
		public ISymbol getSym() {
			return sym;
		}

}
