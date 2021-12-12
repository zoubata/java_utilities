package com.zoubworld.java.utils.compress.blockSorting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.zoubworld.java.utils.compress.CodingSet;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.Number;
import com.zoubworld.java.utils.compress.algo.IAlgoCompress;
import com.zoubworld.java.utils.compress.algo.RLE;

/**
 * @author Pierre Valleau
 * 
 */
public class Transpose implements IAlgoCompress{

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
	@Override
	public List<ISymbol> encodeSymbol(List<ISymbol> ldec) {
		List<ISymbol> le=new ArrayList<ISymbol> ();
		List<List<ISymbol> >  lls=Symbol.Split(ldec, split);
		int size=0;
		int linecount=lls.size();
		for(List<ISymbol> l:lls)
			size=Math.max(size, l.size());
		for(int index=0;index<size;index++)
			for(List<ISymbol> l:lls)
				if(l.size()>index)
					le.add(l.get(index));
				else
					le.add((Symbol.Null));
		le.add(0,new Number(size));
		le.add(1,new Number(linecount));
		return le;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Transpose";
	}

}
