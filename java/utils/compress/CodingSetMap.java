/**
 * 
 */
package com.zoubworld.java.utils.compress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.NotImplementedException;

import java.util.Map.Entry;

import com.zoubworld.java.utils.compress.file.IBinaryCoding;
import com.zoubworld.java.utils.compress.file.IBinaryReader;
import com.zoubworld.java.utils.compress.file.IBinaryWriter;

/** A coding set merge or superSet, that allow to manage complex ISymbol (Number,CompositeSymbols,CompositeSymbol)
 * it embeds several condingRule
 * 
 *
 * 
 * An ICodingRule represent the coding for a ISymbol class, like Symbol or Number.
 * But complex symbol like class CompositeSymbol/CompositeSymbols embedded 2 or more symbol of different nature.
 * From the default ICodingRule, bit stream is converted to basic ISymbol from a specific class(Number/Symbol,..), 
 * but the ICodingRule didn't manage complex symbol, here we describe the symbol that need to be mapped into complex symbol and how to decode additional symbol on it. 
 * lets define :
 * N: number of complex symbol.
 * n: number of symbol inside the complex symbol.
 * Sym 1st symbol of complex symbol
 * Crn : a coding set known in the bit stream stack, the n th.
 * Cr0 : a coding set for basic class.Symbol
 * a coding description is a Symbol, #n number of symbol inside, a list of Crn: describing the coding of 2nd, 3rd, 4th symbol, first one use the current bitstream one.
 * example a coding description for a complex symbol RLE #i will be : RLE+#2+#crn
 * example a coding description for a complex symbol COPY #pos #len will be : COPY+#2+#Crn+#Crn'
 * 
 * Cr0
 * N
 * [Sym,n,[crn]]
 * @author Pierre valleau
 */
public class CodingSetMap implements ICodingRule {

	Map<ISymbol,List<ICodingRule>> m;
	IBinaryCoding binary;
	public ISymbol getSymbol(IBinaryReader bin)
	{
		ISymbol  sym=getDefaultCodingRule().getSymbol(bin);
		List<ICodingRule> lcr=m.get(sym);
		if(lcr==null)
			return sym;
		else
		{
			List<ISymbol> l=new ArrayList<ISymbol>();
			l.add(sym);
			for(ICodingRule cr:lcr )
			{
				ISymbol sn=cr.getSymbol(bin);
				l.add(sn);
			}
			return new CompositeSymbols(l);
		}
	}
	public ICode getCode(IBinaryReader binaryStdIn)
	{
		return get(getSymbol(binaryStdIn));
	}

	public ISymbol get(ICode code)
	{
		throw new NotImplementedException("not yet implemented " );
		
	//	return null;
	};
	public ICode get(ISymbol sym) {
		if (sym == null)
			return null;
		if(CompositeSymbols.class.isInstance(sym))
		{
			CompositeSymbols cs=(CompositeSymbols )sym;
			sym=cs.getS0();
			ICode code=new Code(getDefaultCodingRule().get(sym));
			
			for(int i=1;i<cs.getSs().size();i++)
				code=new Code(code,
						m.get(sym).get(i-1).get(cs.getSs().get(i))
						);
			 return code;
		}
		if(CompositeSymbol.class.isInstance(sym))
		{
			CompositeSymbol cs=(CompositeSymbol )sym;
			sym=cs.getS0();
			ICode code=new Code(getDefaultCodingRule().get(sym));
			 code=new Code(code,
						m.get(sym).get(1).get(cs.getS1())
						);
			 return code;			
		}
		if(Number.class.isInstance(sym))//class Number is manage as CompositeSymbol(Symbol.Number,Number) , Symbol.Number code play the prefix role 
		{
			ICode code=new Code(getDefaultCodingRule().get(Symbol.Number),
						m.get(Symbol.Number).get(1).get(sym)
						);
			 return code;			
		}
		else
			return getDefaultCodingRule().get(sym);		
	}
	/*
	public void write(ISymbol sym) {
		if (sym == null)
			return;
		if(CompositeSymbols.class.isInstance(sym))
		{
			CompositeSymbols cs=(CompositeSymbols )sym;
			sym=cs.getS0();
			Write(sym,getCodingRule());
			for(int i=1;i<cs.getSs().size();i++)
			Write(cs.getSs().get(i),m.get(sym).get(i-1));
			
		}
		if(CompositeSymbol.class.isInstance(sym))
		{
			CompositeSymbol cs=(CompositeSymbol )sym;
			sym=cs.getS0();
			Write(sym,getCodingRule());			
			Write(cs.getS1(),m.get(sym).get(0));
			
		}
		else
			Write(sym,getCodingRule());
		
	}*/
	ICodingRule Crdefault;
	public ICodingRule getDefaultCodingRule()
	{ 
		return Crdefault;
		}
	
	
	/** read a CodingSetMap in a bit Stream.
	 * 
	 */
	public CodingSetMap(IBinaryReader bin) {
		m=new HashMap<ISymbol,List<ICodingRule>> ();
		
		ISymbol CS0s = bin.readSymbol();
		int CS0=(int)CS0s.getId();
		Crdefault = bin.getCodingRules().get(CS0);
		
		ISymbol Ns = bin.readSymbol();
		int N=(int)Ns.getId();
		for(int i=0;i<N;i++)
		{
			ISymbol Sym = bin.readSymbol();
			ISymbol ns = bin.readSymbol();
			int n=(int)ns.getId();
			List<ICodingRule> lcs=new ArrayList<ICodingRule> ();
			for(int j=1;j<n;j++)//symbol S0 is already managed
			{
				ISymbol CSns = bin.readSymbol();
				int CSni=(int)CSns.getId();
				ICodingRule CSn = bin.getCodingRules().get(CSni);
				lcs.add(CSn);
			}	
			m.put(Sym, lcs);				
		}
		
	}
	private CodingSetMap() {
		m=new HashMap<ISymbol,List<ICodingRule>> ();
		
	}
	static public ICodingRule Factory(Map<ISymbol, Long>  m) {
		throw new NotImplementedException("not yet implemented " );
		
		
	}
	/** build a CodingSetMap From Symbols to Write.
	 * 
	 */
	static public CodingSetMap Factory(List<ISymbol> lse) {
		CodingSetMap csm=new CodingSetMap();
		Map<ISymbol, List<List<ISymbol>>> flatted=new HashMap<ISymbol, List<List<ISymbol>>> ();
		
		Map<Class, Long> mc = ISymbol.FreqClass(lse);
		for(Class cl:mc.keySet())
		{
			if (cl==CompositeSymbols.class)
			{
				Set<ISymbol> cs = CompositeSymbols.getCompositeSymbols(lse);
				for(ISymbol c:cs)
				{
					List<List<ISymbol>> l1 = CompositeSymbols.flatter(lse, c);					
					flatted.put(c,l1);
					lse=l1.get(0);	
					
					List<ICodingRule> lcs=new ArrayList<ICodingRule> ();
					for(int i=1;i<l1.size();i++)
					{
					ICodingRule cri=ICodingRule.Factory(l1.get(i));
					lcs.add(cri);
					}
					csm.m.put(c, lcs);
				}
			}
			else
				if (cl==CompositeSymbol.class)
				{
					Set<ISymbol> cs = CompositeSymbol.getCompositeSymbols(lse);
					for(ISymbol c:cs)
					{
						List<List<ISymbol>> l1 = CompositeSymbol.flatter(lse, c);					
						flatted.put(c,l1);
						lse=l1.get(0);	
						
						List<ICodingRule> lcs=new ArrayList<ICodingRule> ();
						for(int i=1;i<l1.size();i++)
						{
						ICodingRule cri=ICodingRule.Factory(l1.get(i));
						lcs.add(cri);
						}
						csm.m.put(c, lcs);
					}
				}
				else
			if(cl==Number.class)// convert number into composite symbols Number(#), symbol Number play the prefix of class Number for the coding.
			{
				List<List<ISymbol>> l1 = CompositeSymbols.flatterClass(lse, new Number(0),Symbol.Number);
				flatted.put(Symbol.Number,l1);
				lse=l1.get(0);
				List<ICodingRule> lcs=new ArrayList<ICodingRule> ();
				ICodingRule cri=ICodingRule.Factory(l1.get(1));
				lcs.add(cri);
				csm.m.put(Symbol.Number, lcs);
			} else
			if(cl==Symbol.class)
				{}
			else
			{
				System.exit(1);//oups
			}
		}
		
		csm.Crdefault=ICodingRule.Factory(lse);
		return csm;
		
	}
	
	public List<ISymbol> toSymbol(IBinaryWriter bin)
	{
		//ICodingRule defaultcr=bin.getCodingRule();
		
		List<ISymbol> ls=new ArrayList<ISymbol> ();
		
		int CS0=bin.getCodingRules().indexOf(getDefaultCodingRule());							
		ls.add(new Number(CS0));	
		
		ls.add(new Number(m.size()));
		for(Entry<ISymbol,List<ICodingRule>> e:m.entrySet())
		{
			ls.add(e.getKey());
			ls.add(new Number(e.getValue().size()));			
			for(ICodingRule cs:e.getValue())
			{
				int CSn=bin.getCodingRules().indexOf(cs);							
				ls.add(new Number(CSn));			
			}	
		}
		//bin.write(defaultcr);		
		return ls;
	}
	public void writeCodingRule(IBinaryWriter bo)
	{
		ICodingRule defaultcr=bo.getCodingRule();
		bo.setCodingRule(null);
		//record all CodingRules
		for(Entry<ISymbol,List<ICodingRule>> e:m.entrySet())
		{			
			for(ICodingRule cs:e.getValue())
			{
				int index=bo.getCodingRules().indexOf(cs);
				if (index<0)
				{
					bo.write(cs);
					bo.setCodingRule(cs);
					
				}						
			}	
		}
		ICodingRule.super.writeCodingRule(bo);
		List<ISymbol> ls=toSymbol(bo);
		bo.writes(ls);
		
		bo.setCodingRule(defaultcr);
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	@Override
	public void setSprout(ISymbol sprout) {
		
		throw new NotImplementedException("not applicable " );
		
		
	}
	@Override
	public ICode getGenericCode(IBinaryReader binaryStdIn) {
		// TODO Auto-generated method stub
		throw new NotImplementedException("not applicable " );
	}

}
