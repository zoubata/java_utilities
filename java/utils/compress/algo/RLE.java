/**
 * 
 */
package com.zoubworld.java.utils.compress.algo;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.java.utils.compress.CompositeSymbol;
import com.zoubworld.java.utils.compress.CompositeSymbols;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.Number;
import com.zoubworld.java.utils.compress.SymbolComplex.SymbolINT;

/**
 * @author Pierre Valleau
 * 
 *         RLE+N+symbol
 *
 */
public class RLE implements IAlgoCompress {
	// dev time 4H 28/7/2018
	// File=list(code)=> list(sym)=>list(sym)....=>list(code)
	int level = 3;

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = this.getClass().hashCode();
		int result = 1;
		result = prime * result + level;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RLE other = (RLE) obj;
		if (level != other.level)
			return false;
		return true;
	}

	/**
	 * 
	 */
	public RLE() {
		level = 3;
	}

	/**
	 * @param level
	 */
	public RLE(Long parameter) {
		super();
		this.level = (int) parameter.intValue();
	}
	public  Long getParam() {return (long) level;}

	/*
	 * public List<Symbol> decode(List<Code> lc) { return null;
	 * 
	 * 
	 * }
	 */

	/*
	 * public List<Code> encode(List<Symbol> ls) { //List<Code>
	 * 
	 * return null;
	 * 
	 * }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zoubworld.java.utils.compress.algo.IalgoCompress#decodeSymbol(java.util.
	 * List)
	 */
	@Override
	public List<ISymbol> decodeSymbol(List<ISymbol> lenc) {
		List<ISymbol> ldec = new ArrayList<ISymbol>();
		int state = 0;// no RLE
		long N = 1;
		ISymbol previous = null;
		for (ISymbol e : lenc) {
			/*if (keySym.equals(e)) {
				state = 1;// RLE

			} else if (state == 1)// N
			{
				state = 2;

				//N = ((CompositeSymbol) e).getS2().getId();
				N=Number.getValue(e);
				
			}*/
			if (CompositeSymbols.class.isInstance(e))
			{
				CompositeSymbols ce=(CompositeSymbols) e;
				if (ce.getS0().equals(keySym))
					state = 1;
				if (state == 1)
				{	state = 2;

				N=Number.getValue(ce.getS1());
			}
			}
			else if (state == 2)// Sym
			{
				state = 0;
				for (long i = 0; i < N; i++)
					ldec.add(e);
				// ldec.add(e);
				N = 1;
			}

			else
				ldec.add(e);
			if ((state != 2) && (state != 1))
				previous = e;
		}
		return ldec;
	}
	/*
	 * tab[0..255][0..255] list<offset> 65355 x
	 * 
	 * 
	 * dico1 256 50% dico2 65356 25% dico3 16777216 10%
	 * 
	 * 
	 * 1 Mo range; dico 16Mo 1 + 16*8+1.6*64=260Mo
	 * 
	 * 
	 */

	ISymbol keySym=Symbol.RLE;
//	ISymbol valSym=Symbol.FactorySymbolINT(1024*1024);//new Number(0);
	//new Number(0);
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zoubworld.java.utils.compress.algo.IalgoCompress#encodeSymbol(java.util.
	 * List)
	 */
	@Override
	public List<ISymbol> encodeSymbol(List<ISymbol> ldec) {
		List<ISymbol> lenc = new ArrayList<ISymbol>();
		if (ldec.isEmpty())
			return lenc;
		List<ISymbol> lenc2 = lenc;
		ISymbol previous = null;
		int count = 1;
		for (ISymbol e : ldec) {
			if (previous == null) {
			} else {
				if (e.equals(previous))
					count++;
				else {
					if (count > level) {

						/*lenc.add(keySym);
						 lenc.add(new Number(count));*/
					//	lenc2.add(Symbol.FactorySymbolINT((long)count));
						
						 lenc.add(new CompositeSymbols(keySym,new Number(count)));
						lenc.add(previous);// new symbol
						count = 1;
					} else {

						for (int i = 0; i < count; i++)
							lenc.add(previous);// new symbol
						count = 1;
					}
				}

			}
			previous = e;
		}
		if (count > 1) {
			if (count > level) {
				/*lenc.add(Symbol.RLE);
				// lenc.add(new Symbol(count));
				//lenc.add(Symbol.FactorySymbolINT(count));
				 lenc.add(new Number(count));*/
				 lenc.add(new CompositeSymbols(keySym,new Number(count)));
					
				lenc.add(previous);// new symbol
				count = 1;
			} else {
				for (int i = 0; i < count; i++)
					lenc.add(previous);// new symbol
				count = 1;
			}
		} else
			lenc.add(previous);

		return lenc;
	}
	/*
	 * public List<ISymbol> encodeSymbol2(List<ISymbol> ldec) { List<ISymbol> lenc =
	 * new ArrayList<ISymbol>(); ISymbol previous = null; int count = 1; for
	 * (ISymbol e : ldec) { if (previous == null) { } else { if (e == previous)
	 * count++; else if (count > level) { lenc.remove(lenc.size() - 1);
	 * lenc.add(Symbol.RLE); // lenc.add(new Symbol(count));
	 * lenc.add(Symbol.FactorySymbolINT(count)); lenc.add(previous);// new symbol
	 * count = 1; } else {
	 * 
	 * for (int i = 0; i < count; i++) lenc.add(previous);// new symbol lenc.add(e);
	 * count = 1; }
	 * 
	 * } previous = e; } if (count > 1) { if (count > level) {
	 * lenc.remove(lenc.size() - 1); lenc.add(Symbol.RLE); // lenc.add(new
	 * Symbol(count)); lenc.add(Symbol.FactorySymbolINT(count));
	 * lenc.add(previous);// new symbol count = 1; } else { for (int i = 0; i <
	 * count; i++) lenc.add(previous);// new symbol count = 1; } } return lenc; }
	 */

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		 * 
		 * RLE cmp= new RLE(); HuffmanCode huff=new HuffmanCode();
		 * 
		 * 
		 * { String filetxt=
		 * "C:\\\\Temp\\\\zip-test\\\\661P2H20_05_QNSJ8J_01_CP2_20170411T160536.csv";
		 * System.out.println(filetxt); File file = new File(filetxt); List<ISymbol>
		 * ls=Symbol.factoryFile( file.getAbsolutePath()); List<ISymbol>
		 * lse=cmp.encodeSymbol(ls); huff.encodeSymbol(lse,new BinaryStdOut(new
		 * File(filetxt+".rle"))); huff.analyse(lse); } { String
		 * filetxt="C:\\\\Temp\\\\zip-test\\\\QPQRN-19.pbs";System.out.println(filetxt);
		 * File file = new File(filetxt); List<ISymbol> ls=Symbol.factoryFile(
		 * file.getAbsolutePath()); List<ISymbol> lse=cmp.encodeSymbol(ls);
		 * huff.encodeSymbol(lse,new BinaryStdOut(new File(filetxt+".rle")));
		 * huff.analyse(lse); } { String
		 * filetxt="C:\\\\Temp\\\\zip-test\\\\cycling_eedata_1k_cold.lss";System.out.
		 * println(filetxt); File file = new File(filetxt); List<ISymbol>
		 * ls=Symbol.factoryFile( file.getAbsolutePath()); List<ISymbol>
		 * lse=cmp.encodeSymbol(ls); huff.encodeSymbol(lse,new BinaryStdOut(new
		 * File(filetxt+".rle"))); huff.analyse(lse); } { String
		 * filetxt="C:\\\\Temp\\\\zip-test\\\\cycling_eedata_1k_cold.hex";System.out.
		 * println(filetxt); File file = new File(filetxt); List<ISymbol>
		 * ls=Symbol.factoryFile( file.getAbsolutePath()); List<ISymbol>
		 * lse=cmp.encodeSymbol(ls); huff.encodeSymbol(lse,new BinaryStdOut(new
		 * File(filetxt+".rle"))); huff.analyse(lse); }
		 * 
		 * // Symbol.listSymbolToFile(lse,filec.getAbsolutePath(),32);
		 */
	}

	@Override
	public String getName() {
		
		return "RLE()";
	}
}
