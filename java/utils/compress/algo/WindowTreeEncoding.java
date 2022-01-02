/**
 * 
 */
package com.zoubworld.java.utils.compress.algo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.zoubworld.java.math.SMath;
import com.zoubworld.java.utils.ListBeginEnd;
import com.zoubworld.java.utils.compress.CompositeSymbols;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Number;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.utils.JavaUtils;

/**
 * @author Pierre Valleau
 *
 */
public class WindowTreeEncoding extends TreeEncoding {

	/**
	 * 
	 */
	public WindowTreeEncoding() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param occurence
	 * @param splitsym
	 */
	public WindowTreeEncoding(int occurence, ISymbol splitsym) {
		super(occurence, splitsym);
		// TODO Auto-generated constructor stub
	}
	int Noccure=32;
	int Ndeep=16;
	
	NodeTreePos root=new NodeTreePos();
	@Override
	public void init(List<ISymbol> ls)
	{
		//build the tree
		root.reset();
		List<NodeTreePos> ln=new ArrayList<NodeTreePos>();
		ln.add(root);
		int index=0;
		for(ISymbol s:ls)
		{
			List<NodeTreePos> ln2=new ArrayList<NodeTreePos>();
			for(NodeTreePos n:ln)
			ln2.add(n.putpos(index,s));
			if (ln2.size()>Ndeep)
				ln2.remove(0);
			
				ln2.add(root);
				index++;
			ln=ln2;
		
			
			
		}
		
		//optimize the tree
		while(root.refactor(Noccure));
		
		// plan symbol
		for(NodeTree r:root.getLeafs(root))
		{
			if (r.getCount()>Noccure)
				r.setPackedcount(0L);
}
		NodeTreePos p=r.;
		while(p!=null);
	}
			
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
