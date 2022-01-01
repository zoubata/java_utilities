/**
 * 
 */
package com.zoubworld.java.utils.compress.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zoubworld.java.utils.compress.CodeNumberSet;
import com.zoubworld.java.utils.compress.CompositeSymbols;
import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Number;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.utils.JavaUtils;

/**
 * @author Pierre Valeau
 * 
 * this class convert ISymBol list from various class/composite into a Symbol list writable of basic Symbol.
 *
 */
public class SymbolBloc {

	/**
	 * 
	 */
	public SymbolBloc(List<ISymbol> ls) {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param args
	 */

//	Coding rules
//number of list
//length of each lists(n+1)
//for each list
	//	Coding rules(1)
	//	list(n)

	public static void main(String[] args) {}
	
	public static List<ISymbol>  Read(BinaryStdIn bi) {
		return null;
		}
		public static void Write(BinaryStdOut bo,List<ISymbol> ls) {
			//class var
		Map<ISymbol, List<ISymbol>> flatted=new HashMap<ISymbol, List<ISymbol>> ();
		//input :
		/*List<ISymbol> ls=null;
		BinaryStdOut bo=new BinaryStdOut();
		*/
		
		// analyse, and flat it
		Map<Class, Long> mc = ISymbol.FreqClass(ls);
		for(Class cl:mc.keySet())
		{
			if (cl==CompositeSymbols.class)
			{
				Set<ISymbol> cs = CompositeSymbols.getCompositeSymbols(ls);
				for(ISymbol c:cs)
				{
					List<List<ISymbol>> l1 = CompositeSymbols.flatter(ls, c);
					if (l1.size()==2)
					{
					flatted.put(c,l1.get(1));
					ls=l1.get(0);
					}
					else
						System.exit(1);//oups
				}
			}
			else
			if(cl==Number.class)
			{
				List<List<ISymbol>> l1 = CompositeSymbols.flatterClass(ls, new Number(0),Symbol.Number);
				flatted.put(Symbol.Number,l1.get(1));
				ls=l1.get(0);
			} else
			if(cl==Symbol.class)
				{}
			else
			{
				System.exit(1);//oups
			}
		}
		
		
		//adjust coding
		for (List<ISymbol> l0:flatted.values())
		{
			ICodingRule cs=ICodingRule.Factory(l0);
			System.out.println(ISymbol.length(l0,cs)+":"+l0.size()+":"+l0);
			System.out.println("cs:"+cs.toSymbol().getCode().length()+":"+cs);
				l0.add(0,cs.toSymbol());
		}
		if (!ls.isEmpty())
		{
		ICodingRule cs=ICodingRule.Factory(ls);
		System.out.println(ISymbol.length(ls,cs)+":"+ls.size()+":"+ls);
		System.out.println("cs:"+cs.toSymbol().getCode().length()+":"+cs);
		ls.add(0,cs.toSymbol());
		}
		
		//write sizes
		List<ISymbol> ln = new ArrayList<ISymbol>();
		ln.add(new Number(flatted.size()+1));
		//bo.write(1);
		ln.add(new Number(ls.size()));
	//	flatted=JavaUtils.SortMapByKey(flatted);
		for (ISymbol s0:flatted.keySet())
		{
			List<ISymbol> l0=flatted.get(s0);
			//bo.write(1);
			ln.add(new Number(l0.size()+1));
			
		}
		ICodingRule csn=ICodingRule.Factory(ln);
		ln.add(0,csn.toSymbol());
	
		bo.writes(ln);
		
		///write Lists of symbols
		bo.writes(ls);
		for (ISymbol s0:flatted.keySet())
		{
			List<ISymbol> l0=flatted.get(s0);
			bo.setCodingRule(bo.getCodingRules().get(0));//patch
		bo.write(s0);
			bo.writes(l0);
		}		

	}

}

