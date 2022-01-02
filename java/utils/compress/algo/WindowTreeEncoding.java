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
		super();
		 reset();
		 Noccure=2;
	     Ndeep=16;
	}
	public void reset()
	{
		super.reset();
		root = new NodeTreePos();
	}
	/**
	 * @param occurence
	 * @param splitsym
	 */
	private WindowTreeEncoding(int occurence, ISymbol splitsym) {
		super(occurence, splitsym);
		 reset();
		 Noccure=occurence;
	}
	public WindowTreeEncoding(int occurence, int deep) {
		super(occurence, null);
		 reset();
		 Noccure=occurence;
		 Ndeep=deep;
	}
	//int Noccure=2;
	int Ndeep=16;
	
	//NodeTreePos root=new NodeTreePos();
	@Override
	public void init(List<ISymbol> ls)
	{
		//build the tree
	//	((NodeTreePos)root).reset();
		root=new NodeTreePos();
		List<NodeTreePos> ln=new ArrayList<NodeTreePos>();
		ln.add((NodeTreePos)root);
		int index=0;
		for(ISymbol s:ls)
		{
			List<NodeTreePos> ln2=new ArrayList<NodeTreePos>();
			for(NodeTreePos n:ln)
			ln2.add(n.putpos(index,s));
			if (ln2.size()>Ndeep)
				ln2.remove(0);
			
				ln2.add((NodeTreePos)root);
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

		NodeTreePos l=(NodeTreePos)r;
		while(l!=null)
		{
			NodeTreePos p=(NodeTreePos)l.getParent();
			if(p!=null)
			if((p.getCount()>l.getCount())&& (p.getDeep()>Noccure))
				p.setPackedcount(0L);			
			l=p;
		}
	}
			
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	/** build the symbol that represent the optimization based on node tree info and/or index of word
	 * @param good the tree node that represent the optimization.
	 * @param indexdico index in the dictionary of word represented by good.
	 * @return Copy symbol with offset of 1st symbol to copy and length.
	 */
	@Override
	protected ISymbol BuildUseSymbol(int indexdico,NodeTree good,ISymbol sympacked) {
		if(good!=null)
		{
		NodeTreePos Good=(NodeTreePos)good;
		Long deep = good.getDeep();
		return new CompositeSymbols(Symbol.Copy, new Number(Good.getPos().get(0)-deep+1),new Number(deep));
		}
		return sympacked;
	}
	/** build a new entry inside the dictionary stored inside the symbol stream,
	 * 
	 * @param good the tree node that represent the optimization.
	 * @return null if the dictionary isn't store in the stream.
	 */
	@Override
	protected CompositeSymbols BuildNewSymbol(NodeTree good) {
		return null;
	}

	/** say if is a new words
	 * @param cs
	 * @return true if it is a new word for decode algo
	 */
	@Override
	protected boolean IsNewWord(CompositeSymbols cs) {
		return IsUseWord(cs);
	}
	@Override
	protected boolean IsUseWord(CompositeSymbols cs) {
		return (cs!=null) && Symbol.Copy.equals(cs.getS0());
	}
	/** convert a word into a list of symbols.
	 * @param cs
	 * @param ldec
	 * @return return the list of symbol associated to the cs dictionnary symbol.
	 */
	@Override
	protected List<ISymbol> getSymbolListOf(CompositeSymbols cs, List<ISymbol> ldec) {
		int pos=Number.getValue(cs.getS1()).intValue();
		int len=Number.getValue(cs.getS2()).intValue();
		
		List<ISymbol> ws=
		new ArrayList<ISymbol>();
		ws.addAll(ldec.subList(pos, pos+len));
		return ws;
	}
}
