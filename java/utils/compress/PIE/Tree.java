package com.zoubworld.java.utils.compress.PIE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;

public class Tree<T extends Comparable<? super T>, V> {
	private Node2<T, V> root;

	public Tree() {
		cached = new HashSet<Node2<T, V>>();
		setRoot(new Node2<T, V>(null, null, null));
	}

	public Node2<T, V> getRoot() {
		return root;
	}

	public String toString() {
		return root.toString();
	}

	public void setRoot(Node2<T, V> root) {
		this.root = root;
	}

	/**
	 * add to tree symbol from index1 to index2 to build the tree
	 */
	public void add(Long index1, Long index2, List<T> ls) {
		Node2<T, V> n = null;
		if (index2 <= Long.valueOf(ls.size())) {
			n = this.getRoot().add2(this, index1, index2, ls);

			do {
				this.getRoot().add(n);

			} while ((n = n.getParent()) != null);
		}

		/*
		 * for(int i=index1.intValue();i<index2;i++)
		 * this.getRoot().add2(this,(long)i,index2,ls);
		 */

	}

	/**
	 * add to tree symbol from index1 to build the tree with mutiple branches
	 */
	public void add(Long index1, List<T> ls2) {
		Node2<T, V> n = null;
		List<T> ls = new ArrayList<T>();
		ls.addAll(ls2);
		do {
			n = this.getRoot().add(this, index1, ls);
			ls.remove(0);
			index1++;
			// System.out.println(this.toGraphViz());
		} while (ls.size() > 0);
	}

	public void addWord(Long index1, List<T> ls) {
		/*Node2<T, V> n = null;
		n =*/ this.getRoot().add(this, index1, ls);
	}

	public void addloop(Long index1, List<T> ls) {
		Node2<T, V> n = null;

		{
			n = this.getRoot().add(this, index1, ls);

			do {
				this.getRoot().add(n);
			} while ((n = n.getParent()) != null);
		}

	}

	public static void main(String[] args) {
		Tree<ISymbol, Long> tree = new Tree<ISymbol, Long>();
		String s = "ABCDAAABBDD";
		tree.add(0L, Symbol.from(s));
		System.out.println(tree.toGraphViz());
		// http://www.webgraphviz.com/

		List<ISymbol> lsd;
		List<ISymbol> ls;
		Node2<ISymbol, Long> node;

		node = tree.find(lsd = Symbol.from("AABBDD"));
		ls = node.tolist(lsd.size());
		System.out.println(Symbol.PIE + "," + node.getIndex() + "," + ls.size());// +lsd.remove(ls);

		node = tree.find(lsd = Symbol.from("CDAA"));
		ls = node.tolist(lsd.size());
		System.out.println(Symbol.PIE + "," + node.getIndex() + "," + ls.size());// +lsd.remove(ls);

		tree = new Tree<ISymbol, Long>();
		s = "ABCDAAABBDD";
		System.out.println(tree.toGraphViz());
		tree.add(0L, Symbol.from(s));
		s = "ABCABCD";
		tree.add(11L, Symbol.from(s));
		s = "ABCDABCD";
		tree.add(18L, (long) s.length(), Symbol.from(s));
		System.out.println(tree.toGraphViz());
		// http://www.webgraphviz.com/
		tree.add(0L, (long) s.length(), Symbol.from(s));
		System.out.println(tree.toGraphViz());
		// http://www.webgraphviz.com/

		// ok
	}

	private Node2<T, V> find(List<T> list) {
		Node2<T, V> n = getRoot();

		for (T key : list) {
			if (n.getChildren(key) == null)
				return n;
			n = n.getChildren(key);
		}
		return n;
	}

	List<Node2<T, V>> ln = new ArrayList<Node2<T, V>>();

	/**
	 * add to tree ordored symbol
	 */
	public List<Node2<T, V>> add(V index, T s) {

		List<Node2<T, V>> ln2 = new ArrayList<Node2<T, V>>();

		for (Node2<T, V> n : ln)
			if (n.get(s) == null)
				ln2.add(n.add(index, s));
			else
				ln2.add(n.get(s));
		if (getRoot().get(s) == null)
			ln2.add(getRoot().add(index, s));
		else
			ln2.add(getRoot().get(s));
		ln = ln2;
		return ln2;
	}

	public void add(Node2<T, V> currentLeaf, V newIndex, T newSymbol) {

		Node2<T, V> newnode = currentLeaf.add(newIndex, newSymbol);
		if (currentLeaf != root)
			root.add(newIndex, newSymbol);
	}

	public Set<Node2<T, V>> cached;

	public String toGraphViz() {
		cached = new HashSet<Node2<T, V>>();
		String s = "";
		s += "digraph tree" + " {\r\n";

		s += root.toGraphViz(this);
		s += "}\r\n";
		return s;
	}
}