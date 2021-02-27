/**
 * 
 */
package com.zoubworld.java.utils.compress.algo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.utils.Pair;

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

}
