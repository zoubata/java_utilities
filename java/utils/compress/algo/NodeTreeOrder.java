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
public class NodeTreeOrder extends NodeTree {

	List<ISymbol> order = new ArrayList<ISymbol>();

	/**
	 * @param key
	 */
	public NodeTreeOrder(ISymbol key) {
		super(key);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param parent
	 * @param key
	 */
	public NodeTreeOrder(NodeTree parent, ISymbol key) {
		super(parent, key);
		
	}

	public ISymbol getOrderSymbol(int n) {
		return order.get(n);
	}
	@Override
	public NodeTree Factory(NodeTree nodeTree, ISymbol key) {
		
		return new NodeTreeOrder(this, key);
	}
	public long updateOrder(ISymbol s) {
		long pos = order.indexOf(s);
		if (pos < 0) {
			pos =  (order.size() + s.getId());
			put(s);
		} else
			order.remove((int)pos);
		order.add(0, s);
		return pos;
	}
	public ISymbol updateOrder(long n,ISymbol sprout) {
		ISymbol pos =null;
		if (order.size() <= n) {
			pos =sprout.Factory( (long) (n-order.size()) );
			put(pos);
		} else
		{	
			pos = order.get((int)n);		
			order.remove(pos);
		}
		order.add(0, pos);
		return pos;
	}
	
}
