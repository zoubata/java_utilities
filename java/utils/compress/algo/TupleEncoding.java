/**
 * 
 */
package com.zoubworld.java.utils.compress.algo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zoubworld.java.utils.compress.Number;
import com.zoubworld.java.math.SMath;
import com.zoubworld.java.utils.ListBeginEnd;
import com.zoubworld.java.utils.compress.CompositeSymbols;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.utils.JavaUtils;

/**
 * @author Pierre Valleau
 *
 */
public class TupleEncoding  implements IAlgoCompress {

	/**
	 * 
	 */
	public TupleEncoding() {
		
	}
	public int readDictionary(List<ISymbol> lenc)
	{
		int wsize=(int) lenc.get(0).getId();
		int dsize=(int) lenc.get(1).getId();
		int idico=0;
		ListBeginEnd<ISymbol> lenc2=new ListBeginEnd<ISymbol>(lenc,0,dsize*wsize+2-1);
		for (int fromIndex = 2; fromIndex < dsize*wsize+2; fromIndex+=wsize) {
			NodeTree n=root.add((ListBeginEnd<ISymbol>)lenc2.subList(fromIndex, fromIndex + wsize));
			n.setSymPacked(
					new CompositeSymbols(Symbol.Tuple, new Number(ldico.size()))
					);
			ldico.add(n);
			}
			
		return dsize*wsize+2;	
	}
	public   List<ISymbol> saveDictionary()
	{
		List<ISymbol> ld =new ArrayList<ISymbol>();
		//ld.add(Symbol.DICO);
		ld.add(new Number(getsize()));
		ld.add(new Number(ldico.size()));
		
		for(NodeTree n:ldico)
			ld.addAll(n.getList());
		return ld;
	}
	
	@Override
	public List<ISymbol> decodeSymbol(List<ISymbol> lenc) {
		List<ISymbol> ldec =new ArrayList<ISymbol>();
		Map<ISymbol,List<ISymbol>> m=new HashMap<ISymbol,List<ISymbol>>();
		for(NodeTree n:ldico)
			m.put(n.getSymPacked(), n.getList());
		
		for(ISymbol s:lenc)
		{
			List<ISymbol> al=m.get(s);
			if (al!=null)
				ldec.addAll(al);
			else
					ldec.add(s);
				
		}
		return ldec;
	}

	@Override
	public List<ISymbol> encodeSymbol(List<ISymbol> ldec) {
		List<ISymbol> lse =new ArrayList<ISymbol>();
		int i=0;
		for( i=0;i<ldec.size();)
		{
			int j=0;
			NodeTree node = root;
			NodeTree good = null;
			while (i+j<ldec.size() && (node=node.getChild(ldec.get(i+j)))!=null)
				{good=node;j++;}
			/*if ((i+j>=ldec.size())&&(good==null || good.getSymPacked()==null))
			{
				for( int k=i;k<ldec.size();k++)
					lse.add(ldec.get(k));
			}
			else*/
			if (good==null || good.getSymPacked()==null)
			{
				/*for(int k=0;k<j;k++)
				lse.add(ls.get(i+k));*/
				lse.add(ldec.get(i));i++;
			/*	good.symPacked=new CompositeSymbols(Symbol.BTE, new Number(dico++));
				good.Packedcount=0L;*/
			}
			else
			{
					good.IncPackedcount();
					lse.add(good.getSymPacked());
					i+=j;
			}
		
		}
		ldico = JavaUtils.asSortedSet(ldico, NodeTree.compCountPacked);
		for(i=0;(i<ldico.size()) && (ldico.get(i).getPackedcount()==0);i++);
			ldico=ldico.subList(i,ldico.size());
		int dico=0;
		for(NodeTree n:ldico)
			((CompositeSymbols)n.getSymPacked()).setS1(new Number(dico++));
		return lse;
	}
	NodeTree root;
	List<NodeTree> ldico ;
	public void reset()
	{
		root = new NodeTree();
		ldico= new ArrayList<NodeTree> ();
	}
	long param=0x000003L;	
	
	
	private int getsize() {		
		return (int) (param&0xff);
	}
	private int getlimit() {		
		return (int) ((param>>8)&0xffff);
	}
	/* build a tree of symbol
	 * */
	public void init(List<ISymbol> ls)
	{
		
		//2 : 1.327s	0.744 		217 Mo
		//3 : 4.812s   	1.028s  	301 Mo
		//4 : 5.196s 	1.5s   		216 Mo
		//6 : 9.326  	6.311s 		294 Mo 
		//12 : 16.249  	15.186s   	868 Mo
		//16 : 31.625    25.937    1339 Mo 
		int step=1;//getsize();
		ls=new ListBeginEnd(ls);
		for (int fromIndex = 0; fromIndex + getsize() < ls.size(); fromIndex+=step) {
			root.add((ListBeginEnd<ISymbol>)ls.subList(fromIndex, fromIndex + getsize()));
			// if(fromIndex%100000==0) System.out.print(".");
		}
		// identify the sylmbol occurence :
		Map<ISymbol, Long> m = ISymbol.Freq(ls);
		Collection<Long> s = m.values();
		s.remove(1L);
		Long min = SMath.min((Collection<Long>) s);
		Long max = SMath.max((Collection<Long>) s);
		double average = SMath.average((Collection<Long>) s);
		
		ldico = root.getLeafs(root);
		ldico = JavaUtils.asSortedSet(ldico, NodeTree.compCount);
		//keep the most probable sequence.
		int i=0;
		int limit=getlimit();
		if (limit<getsize())
			limit+=getsize();
		else if (limit==getsize())
			limit=min.intValue();
		
			
		for (; (i < ldico.size()) && (ldico.get(i).getCount() <limit ); i++)
			;
		ldico = ldico.subList(i, ldico.size());
		Collections.reverse(ldico);
		int idico=0;
		for(NodeTree n:ldico)
		{
			n.setSymPacked(new CompositeSymbols(Symbol.Tuple, new Number(idico++)));
		n.setPackedcount(0L);
	}}

	@Override
	public String getName() {
		
		return this.getClass().getSimpleName();
	}

}
