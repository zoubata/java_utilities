/**
 * 
 */
package com.zoubworld.java.utils.compress.algo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.utils.JavaUtils;

import sandbox.InstrumentationAgent;

/**
 * @author Pierre Valleau
 *
 */
public class NodeTreePos extends NodeTree {

	/**
	 * 
	 */
	public NodeTreePos() {
		super();
	}
	List<Long> pos=new ArrayList<Long>();
	/**
	 * @param key
	 */
	public NodeTreePos(ISymbol key) {
		super(key);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param parent
	 * @param key
	 */
	public NodeTreePos(NodeTree parent, ISymbol key) {
		super(parent, key);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String s0="0123456789\r\n"
				+ "0123456789\r\n"
				+ "0123456789\r\n"
				+ "0123456789\r\n";
				

		List<ISymbol> ls = Symbol.from(s0);
		ls=Symbol.from(new File("C:\\Temp\\compression\\litle_file\\data1\\2.txt"));
		NodeTreePos root=new NodeTreePos();
		root.reset();
		List<NodeTreePos> ln=new ArrayList<NodeTreePos>();
		ln.add(root);
		int index=0;
		for(ISymbol s:ls)
		{
			List<NodeTreePos> ln2=new ArrayList<NodeTreePos>();
			for(NodeTreePos n:ln)
			ln2.add(n.putpos(index,s));
			if (ln2.size()>128)
				ln2.remove(0);
			
				ln2.add(root);
				index++;
			ln=ln2;
		
			
			
		}
	//	InstrumentationAgent.printObjectSize(root);
		while(root.refactor(4));
	//	InstrumentationAgent.printObjectSize(root);
		System.out.println(JavaUtils.Format(JavaUtils.asSortedSet(root.getLeafs(root),root.compCount),"\r\n"));
	//	System.out.println(JavaUtils.Format(JavaUtils.asSortedSet(root.getLeafs(root,10),root.compCount),"\r\n"));
		
	}
	
	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.algo.NodeTree#Factory(com.zoubworld.java.utils.compress.algo.NodeTree, com.zoubworld.java.utils.compress.ISymbol)
	 */
	@Override
	public NodeTree Factory(NodeTree nodeTree, ISymbol key) {
		// TODO Auto-generated method stub
		return new NodeTreePos(nodeTree, key);
	}
	@Override
	public Long getCount() {
		return (long) getPos().size();
	}
	@Override
	public String toString() {
		String s;
		if (compact)
		{
			if(sym!=null)
			s= sym.toString().charAt(1)  + ":" + getPos();
			else
			s="?root";
		}
		else
		s= sym + ":" + getPos();
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

	public NodeTreePos putpos(long pos,ISymbol s) {
		/*long pos=0;
		if (getPos().isEmpty())
			pos=0;
		else
			pos=getPos().get(getPos().size()-1);*/
		NodeTreePos e = (NodeTreePos)this.put(s);
		e.getPos().add(pos);
		return e;
	}

	/**
	 * @return the pos
	 */
	public List<Long> getPos() {
		return pos;
	}

	void reset() {
		// TODO Auto-generated method stub
		
	}

}
