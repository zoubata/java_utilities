package com.zoubworld.java.utils.compress.blockSorting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zoubworld.java.utils.compress.CodingSet;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.Number;
import com.zoubworld.java.utils.compress.algo.IAlgoCompress;
import com.zoubworld.java.utils.compress.algo.RLE;
import com.zoubworld.utils.JavaUtils;

/**
 * @author Pierre Valleau
 * 
 */
public class Transpose implements IAlgoCompress{

	public Transpose(ISymbol split) {
		this.split=split;		
	}
	public Transpose(Long wide,ISymbol split) {
		this.split=split;
		this.wide=wide;
		

	}
	
	public  void init(List<ISymbol> ls)
	{
		/* good idea :
		Map<ISymbol, Long> m = JavaUtils.SortMapByValue(ISymbol.Freq(ls));
		ISymbol split=m.get(0);
		Map<Long, Long> d = JavaUtils.SortMapByValue(ISymbol.Distance(ls,split);
		wide=d.get(0);
		if (m.get(split)/6)<wide)
				split=null;
				System.out.println("ls :"+JavaUtils.Format(m,":","\n"));
		System.out.println("ls :"+JavaUtils.Format(d),":","\n");
*/
		
		Map<Long, Long> d = ISymbol.SumDistance(ISymbol.Distance(ls));
		
		d.remove(1L);
		long l=0;
		Long r=null;
		for(Long e:d.keySet())
			if(l<d.get(e))
				{l=d.get(e);r=e;}
		wide=r;//if wide is good it will be this one.
		
	}
	
	public Transpose() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) {
		List<ISymbol> ls=Symbol.from(new File("C:\\Temp\\pat.txt"));
		IAlgoCompress algo=new Transpose();
		List<ISymbol> le=algo.encodeSymbol(ls);
		IAlgoCompress rle=new RLE();
		List<ISymbol> lre=rle.encodeSymbol(le);
		System.out.println("ls= "+ls.size()+" le= "+le.size()+" lre= "+lre.size());
		System.out.println("lre"+lre);
		System.out.println("le"+le);
		le.remove(0);
		le.remove(0);
		le.remove(le.size()-1);
		Symbol.replaceAll(le,Symbol.Null,Symbol.findId(' '));
		Symbol.toFile(new File("C:\\Temp\\pat.cmp"), new CodingSet(CodingSet.NOCOMPRESS16), le);
	}

	@Override
	public List<ISymbol> decodeSymbol(List<ISymbol> lenc) {
		int size=(int)lenc.get(0).getId();
		int linecount=(int)lenc.get(1).getId();
		List<ISymbol> ld=new ArrayList<ISymbol> ();
		List<List<ISymbol> >  lls=Symbol.Split(lenc.subList(2, lenc.size()), linecount);
		for(int index=0;index<linecount;index++)
			for(List<ISymbol> l:lls)
				if(index<l.size())
				if(!l.get(index).equals(Symbol.Null))
					ld.add(l.get(index));
		return ld;
		
	}
	ISymbol split=Symbol.findId('\n');
	Long wide=null;
	@Override
	public List<ISymbol> encodeSymbol(List<ISymbol> ldec) {
		List<ISymbol> le=new ArrayList<ISymbol> ();
		int size=0;
		int linecount=0;
		if (split!=null)
		{
		List<List<ISymbol> >  lls=Symbol.Split(ldec, split);
		linecount=lls.size();
		for(List<ISymbol> l:lls)
			size=Math.max(size, l.size());
		for(int index=0;index<size;index++)
			for(List<ISymbol> l:lls)
				if(l.size()>index)
					le.add(l.get(index));
				else
					le.add((Symbol.Null));
		}else
		{	size=wide.intValue();
			List<List<ISymbol> >  lls=Symbol.Split(ldec, size);
			linecount=lls.size();
			
			for(int index=0;index<size;index++)
				for(List<ISymbol> l:lls)
					if(l.size()>index)
						le.add(l.get(index));
					else
						le.add((Symbol.Null));
		}
		
		le.add(0,new Number(size));
		le.add(1,new Number(linecount));
		return le;
	}

	@Override
	public String getName() {
		if (split==null)
		return "Transpose("+wide+")";
		return "Transpose("+split+")";
		
	}

}
