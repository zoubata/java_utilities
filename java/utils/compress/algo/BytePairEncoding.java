/**
 * 
 */
package com.zoubworld.java.utils.compress.algo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import com.zoubworld.java.utils.compress.CodeNumberSet;
import com.zoubworld.java.utils.compress.CompositeSymbols;
import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.SymbolComplex.Sym_LZS;
import com.zoubworld.java.utils.compress.blockSorting.BWT;
import com.zoubworld.java.utils.compress.blockSorting.FifoAlgo;
import com.zoubworld.java.utils.compress.utils.Pair;
import com.zoubworld.utils.JavaUtils;

/**
 * @author Pierre Valleau
 * 
 *         see https://en.wikipedia.org/wiki/Byte_pair_encoding
 * 
 *         This algo replaces 2 symbols : a,b by PBE,INTx(n) so regarding symbol
 *         count we win nothing we lost nothing. but regarding entropy we win
 *         because we replace 256*256 combination of couple a,b by
 *         1(PBE)*12(INT4,8,12,....) combination plus few bit to code n. this
 *         will help huffman
 * 
 *         this algo detects all new a,b and store it in a dictionary to perform
 *         replacement. The benefit is - no cost to store dictionary. - same
 *         complexity to encode and decode. during decoding, the algo to encode
 *         run to build dicionary, and we add a reverse table to boost the
 *         decode of BPE,Intn symbol.
 * 
 * 
 *         the drawback is - the number of bit to code n isn't optimized, it can
 *         bit 17 for most popular symbols and 4 bit for less popular. - the
 *         memory to decode is biger than to encode. this is link to the reverse
 *         table.
 *
 *         opportunity to optimize is : - build histogram of (a,b), and optimize
 *         the size of field n. by puting a record symbol.
 * 
 *         -
 * 
 */
public class BytePairEncoding implements IAlgoCompress {

	long param=0L;	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (param ^ (param >>> 32));
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
		BWT other = (BWT) obj;
		if (param != other.param)
			return false;
		return true;
	}
	@Override
	public String getName() {
		
		return "BytePairEncoding()";
	}
	/**
	 * 
	 */
	public BytePairEncoding() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zoubworld.java.utils.compress.algo.IAlgoCompress#decodeSymbol(java.util.
	 * List)
	 */
	@Override
	public List<ISymbol> decodeSymbol(List<ISymbol> lenc) {
		// System.out.println("decodeSymbol");
		ISymbol old = null;
		ISymbol old2 = null;
		int count = 0;
		int Nb = Symbol.getNbSymbol();

		BidiMap<Pair, Long> table = new DualHashBidiMap<Pair, Long>();
		List<ISymbol> ldec = new ArrayList<ISymbol>();
		int ldecIndex = 0;
		int justadded = -1;
		for (ISymbol e : lenc) {
			Long index = null;
			if (old2 == Symbol.BPE) {
				index = Symbol.getINTn(e).longValue();
				Pair p = table.getKey(index);
				ldec.add(p.getA());
				ldec.add(p.getB());
				// System.out.println("find(" + index + ")=" + reverseA[index] + "," +
				// reverseB[index]);

			} else if (e != Symbol.BPE)

				ldec.add(e);

			old2 = e;

			while (ldec.size() > ldecIndex) {
				e = ldec.get(ldecIndex);
				ldecIndex++;
				if (old != null) {
					Pair p = new Pair(old, e);

					if (table.get(p) == null) {
						table.put(p, (long) count);
						justadded = count;

						count++;
						// lse.add(old);
					} else if (table.get(p) == justadded) {
						// lse.add(old);
						justadded = -1;
					} else {
						justadded = -1;
						// lse.add(Symbol.BPE);
						// lse.add(Symbol.FactorySymbolINT(table.get(p)));
						// System.out.println("find(" + table[(int) old.getId()][(int) e.getId()] + ")="
						// + old + "," + e);
						e = null;
					}
				}
				old = e;
			}
		}

		return ldec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zoubworld.java.utils.compress.algo.IAlgoCompress#decodeSymbol(java.util.
	 * List)
	 *
	 * public List<ISymbol> decodeSymbol2(List<ISymbol> lenc) { //
	 * System.out.println("decodeSymbol"); ISymbol old = null; ISymbol old2 = null;
	 * int count = 0; int Nb = Symbol.getNbSymbol(); Integer table[][] = new
	 * Integer[Nb][Nb]; ISymbol reverseA[] = new ISymbol[Nb * Nb]; ISymbol
	 * reverseB[] = new ISymbol[Nb * Nb];
	 * 
	 * List<ISymbol> ldec = new ArrayList<ISymbol>(); int ldecIndex = 0; int
	 * justadded = -1; for (ISymbol e : lenc) { Integer index = null; if (old2 ==
	 * Symbol.BPE) { index = Symbol.getINTn(e).intValue();
	 * 
	 * ldec.add(reverseA[index]); ldec.add(reverseB[index]); //
	 * System.out.println("find(" + index + ")=" + reverseA[index] + "," + //
	 * reverseB[index]);
	 * 
	 * } else if (e != Symbol.BPE)
	 * 
	 * ldec.add(e);
	 * 
	 * old2 = e;
	 * 
	 * while (ldec.size() > ldecIndex) { e = ldec.get(ldecIndex); ldecIndex++; if
	 * (old != null) { if (table[(int) old.getId()][(int) e.getId()] == null) {
	 * table[(int) old.getId()][(int) e.getId()] = count; reverseA[count] = old;
	 * reverseB[count] = e; justadded = count; // System.out.println("add(" + count
	 * + ")=" + old + "," + e); count++; // lse.add(old); } else if (table[(int)
	 * old.getId()][(int) e.getId()] == justadded) { // lse.add(old); justadded =
	 * -1; } else { justadded = -1; // lse.add(Symbol.BPE); //
	 * lse.add(Symbol.FactorySymbolINT(table[(int) old.getId()][(int) e.getId()]));
	 * 
	 * e = null; } } old = e; } }
	 * 
	 * return ldec; }
	 */
	 
	
	@Override
	public List<ISymbol> encodeSymbol(List<ISymbol> ls) {
		// System.out.println("encodeSymbol");
		ISymbol old = null;
		int count = 0;
		int justadded = -1;
		Map<Pair, Long> table = new HashMap<Pair, Long>();

		List<ISymbol> lse = new ArrayList<ISymbol>();
		for (ISymbol e : ls) {
			if (old != null) {
				Pair p = new Pair(old, e);

				if (table.get(p) == null) {
					table.put(p, (long) count);
					justadded = count;

					count++;
					lse.add(old);
				} else if (table.get(p) == justadded) {
					lse.add(old);
					justadded = -1;
				} else {
					justadded = -1;
					lse.add(Symbol.BPE);
					lse.add(Symbol.FactorySymbolINT(table.get(p)));
					// System.out.println("find(" + table[(int) old.getId()][(int) e.getId()] + ")="
					// + old + "," + e);
					e = null;
				}
			}
			old = e;
		}
		if (old != null)
			lse.add(old);

		return lse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zoubworld.java.utils.compress.algo.IAlgoCompress#encodeSymbol(java.util.
	 * List)
	 *
	 * 
	 * public List<ISymbol> encodeSymbol2(List<ISymbol> ls) { //
	 * System.out.println("encodeSymbol"); ISymbol old = null; int count = 0; int
	 * justadded = -1; int Nb = Symbol.getNbSymbol(); Integer table[][] = new
	 * Integer[Nb][Nb]; List<ISymbol> lse = new ArrayList<ISymbol>(); for (ISymbol e
	 * : ls) { if (old != null) {
	 * 
	 * if (table[(int) old.getId()][(int) e.getId()] == null) { table[(int)
	 * old.getId()][(int) e.getId()] = count; justadded = count; //
	 * System.out.println("add(" + count + ")=" + old + "," + e);
	 * 
	 * count++; lse.add(old);
	 * 
	 * } else if (table[(int) old.getId()][(int) e.getId()] == justadded) {
	 * lse.add(old); justadded = -1; } else { justadded = -1; lse.add(Symbol.BPE);
	 * lse.add(Symbol.FactorySymbolINT(table[(int) old.getId()][(int) e.getId()]));
	 * // System.out.println("find(" + table[(int) old.getId()][(int) e.getId()] +
	 * ")=" // + old + "," + e); e = null; } } old = e; } if (old != null)
	 * lse.add(old);
	 * 
	 * return lse; }/
	 **/
	public static void main(String[] args)
	{
		List<ISymbol> ls = Symbol.from(new File("C:\\Temp\\FAT\\2ndprobe\\NJLM4-14.pbs"));
		                                            //25849s:134403b
		IAlgoCompress enc=new BytePairEncoding();   //25849s:134021b
		enc=new RLE();                            //23013s:128947b
		//enc=new ByteTripleEncoding();             //18469s/111261b
	//	enc=new MultiAlgo(new RLE(),new ByteTripleEncoding());
													//16631s:107463b
		enc=new LZS();								//12474s: 54310b++ /=5296+3589+3589s
		List<ISymbol> lse=enc.encodeSymbol(ls);
		ISymbol.getEntropie(lse);
		System.out.println("ls="+ls.size()+":"+ls.size()*ISymbol.getEntropie(ls)+":"+ls);
		System.out.println("lse="+lse.size()+":"+lse.size()*ISymbol.getEntropie(lse)+":"+lse);
		System.out.println(JavaUtils.SortMapByValue(Symbol.Freq(lse)));
		List<ISymbol> ldec=enc.decodeSymbol(lse);
		System.out.println("ls="+ldec.size()+":"+ldec.size()*ISymbol.getEntropie(ldec)+":"+ldec);
		System.out.println(ldec.equals(ls));
		List<List<ISymbol>> streams=CompositeSymbols.flatter(lse,new Sym_LZS(0, 1));
		for(List<ISymbol> lst:streams)
		System.out.println(lst.size()+"\t:\t"+lst);
		List<ISymbol> ln=streams.get(1);//1
		Map<ISymbol, Long> fn = ISymbol.Freq(ln);
		ICodingRule cs= ICodingRule.Factory( ln);
		System.out.println(cs);
		System.out.println(JavaUtils.SortMapByValue(fn));
		System.out.println(Symbol.length(fn,cs)+"/"+ln.size());
		
		FifoAlgo fifo=new FifoAlgo();
		List<ISymbol> lne = fifo.encodeSymbol(ln);
		System.out.println("ln="+ln.size()+":"+ln.size()*ISymbol.getEntropie(ln)+":"+ln);
		System.out.println("lne="+lne.size()+":"+lne.size()*ISymbol.getEntropie(lne)+":"+lne);
		System.out.println("ln  H "+ISymbol.length(ln,cs)+"/"+ln.size());
		System.out.println(cs);
		cs= ICodingRule.Factory( lne);
		System.out.println("lne H "+ISymbol.length(lne,cs)+"/"+lne.size());
		cs= new CodeNumberSet(ln);
		System.out.println("ln  N "+ISymbol.length(ln,cs)+"/"+ln.size());
		cs= new CodeNumberSet( lne);
		System.out.println("lne N "+ISymbol.length(lne,cs)+"/"+lne.size());
		
		enc=new LZS();  
		lne=enc.encodeSymbol(ln);
		cs= new CodeNumberSet( lne);
		System.out.println("lnRLE N "+ISymbol.length(lne,cs)+"/"+lne.size()+":"+lne);
		System.out.println(ICodingRule.Factory( ln));
/*		
		int count=fn.keySet().size();
		Long max=fn.keySet().stream().map(e->e.getId()).max(Long::compare);
		Long min=fn.keySet().stream().map(e->e.getId()).min(Long::compare);
			
		long l=1;
		write(max);//-2044
		write(min);//-2
		write(threhold);//-148
		
		write(l);//l=1 factor of compression
		boolean b=false;
		long d;
		for(long i=min;i<max;i++)
		{
			if (fn.keySet().contains(i))
			{
				b=(true);
				d|=1<<(i%l);
			}
			if ((i%l)==0)				
			{
				write(b);
				if (b && (l>1))
				write(d,l);
				d=0;
				b=false;
			}
			
			{}=min,max,l=(range/count), mask 01010101010000000000000 / mask(l) 1[10101:l]001[]01[]
			code=loix, param k, j,i
			
			threhold size
			threhold size+dataHuffman
			huffmancode(0)
			..
			huffmancode(n)
			
			bbbb    1..16
			
			0bb		1..4	3b 16
			1bbbb	5..20	5b	1M
			
			0bb		1..4	3b 16
			1bbb	5..13   4b 8k
			
			0bbb	1..8	4b 256
			1bbbb	9..24   5b 16M
			
			0bbbb 1..16		5b	64k
			10bbbbbb 17..80	8b	100M
			unary+binaire 
			
			0 bbb 1..8			4b 256
			10 bbbbbb 9..72		8b 1GG
			
			0 bb 1..4         	3b 16
			10 bbbb 5..20		6b 1M
			110 bbbbbb 21..84
			
			0 bbb 1..8 			4b 256
			10 bbb b 9..24      6b 16M
			110 bbb bb 25..56
			1110
		}
		/*

min,max,-2047,0
Mask bit,k
32,32,32
*/
	}

}
