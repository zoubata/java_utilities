/**
 * 
 */
package com.zoubworld.java.utils.compress.algo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import com.zoubworld.java.utils.compress.HuffmanCode;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.utils.Pair;
import com.zoubworld.java.utils.compress.utils.Triple;
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
	 * 
	 * /** display the stat about the hostogram of symbol freq1, the symbol coding
	 * length is nbBit, the total number of symbol is count(null will force
	 * recompute it)
	 * 
	 */
	public static <T extends Object> String FreqToString(Map<T, Long> freq1, int nbBit, Long count) {
		freq1 = JavaUtils.SortMapByValue(freq1);

		StringBuffer sb = new StringBuffer();
		String s = "";
		int coef = nbBit / 8;
		if (coef == 0)
			coef = 1;
		long size = 0L;
		if (count == null)
			for (Entry<T, Long> s1 : freq1.entrySet()) {
				size += s1.getValue();
			}
		else
			size = count;
		size /= coef;
		if (size == 0)
			size = 1;
		sb.append("Symbol" + "\t:\t" + "count" + "\t:\t" + "optimal length" + "\t:\t" + "efficiency" + "\n");
		for (Entry<T, Long> s1 : freq1.entrySet()) {
			// size+=s1.getValue();
			double pi = ((double) s1.getValue()) / size;
			double len = -Math.log(pi) / Math.log(2);
			double eff = nbBit / len;
			sb.append(s1.getKey() + "\t:\t" + s1.getValue() + "\t:\t" + String.format("%1.3f", len) + "\t:\t"
					+ String.format("%1.3f", eff) + "\n");
		}
		sb.append("\n");
		double len = HuffmanCode.getEntropie(freq1);
		double eff = nbBit / len;
		s += ("size : " + size + " elements /" + size * nbBit + " bits\n");
		s += ("Optimum size " + String.format("%.1f", size * len) + " bits\n");
		s += ("symbols : " + freq1.keySet().size() + " \n");
		s += ("Entropie : " + String.format("%.3f", len) + " / " + nbBit + "Bits \n");
		s += ("Efficiency : " + String.format("%.3f", eff) + " \n");

		return s + sb.toString();
	}

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

	static public Map<Pair, Long> BuildStat(List<ISymbol> ls) {
		Map<Triple, Long> freq3 = new HashMap<Triple, Long>();
		Map<Pair, Long> freq2 = new HashMap<Pair, Long>();
		Map<ISymbol, Long> freq1 = new HashMap<ISymbol, Long>();
		ISymbol old = null;
		ISymbol old2 = null;

		int Nb = Symbol.getNbSymbol();
		byte octect2Bit[] = new byte[Nb];
		for (int i = 0; i < Nb; i++)
			for (byte j = 0; j < 10; j++)
				octect2Bit[i] += (byte) ((i >>> j) & 1);

		for (ISymbol e : ls) {
			if (old != null) {
				Pair p = new Pair(old, e);

				if (freq2.get(p) == null) {
					freq2.put(p, 1L);
				} else
					freq2.put(p, freq2.get(p) + 1L);

			}
			if (old2 != null) {
				Triple p = new Triple(old2, old, e);

				if (freq3.get(p) == null) {
					freq3.put(p, 1L);
				} else
					freq3.put(p, freq3.get(p) + 1L);

			}

			if (freq1.get(e) == null) {
				freq1.put(e, 1L);
			} else
				freq1.put(e, freq1.get(e) + 1L);
			old2 = old;
			old = e;
		}

		{
			long count = 0;
			long size = 0;
			for (Pair s1 : freq2.keySet()) {
				count += freq2.get(s1);
				size += freq2.get(s1) * 16;
			}
			Map<Boolean, Long> freq0 = new HashMap<Boolean, Long>();
			freq0.put(true, 0L);
			freq0.put(false, 0L);

			for (Entry<ISymbol, Long> s1 : freq1.entrySet()) {
				byte b = octect2Bit[(int) s1.getKey().getId()];
				freq0.put(true, freq0.get(true) + b * s1.getValue());
				freq0.put(false, freq0.get(false) + (8 - b) * s1.getValue());
			}

			System.out.print(FreqToString(freq0, 1, null));
			System.out.print(FreqToString(freq1, 8, null));
			System.out.print(FreqToString(freq2, 16, null));
			System.out.print(FreqToString(freq3, 24, null));
			/*
			 * StringBuffer sb = new StringBuffer();
			 * 
			 * 
			 * System.out.print("size : " + ls.size() * 8 + "/" + size + " bits\n");
			 * System.out.print("symbols : " + freq1.keySet().size() + " \n");
			 * System.out.print("Entropie : " + HuffmanCode.getEntropie(freq1) +
			 * " : "+ls.size()*HuffmanCode.getEntropie(freq1) + "\n"); for (Entry<ISymbol,
			 * Long> s1: freq1.entrySet()) { // long i=freq.get(s); sb.append(s1.getKey()+
			 * "\t:\t" + s1.getValue() + "\n"); } System.out.println(sb.toString()); sb=new
			 * StringBuffer(); System.out.print("size : " + ls.size() * 8 + "/" + size +
			 * " bits\n"); System.out.print("symbols : " + freq2.keySet().size() + " \n");
			 * System.out.print("Entropie : " + HuffmanCode.getEntropie(freq2) +
			 * " : "+ls.size()*HuffmanCode.getEntropie(freq2) + "\n"); for (Entry<Pair,
			 * Long> s1: freq2.entrySet()) { // long i=freq.get(s); sb.append(s1.getKey()+
			 * "\t:\t" + s1.getValue() + "\n"); }
			 * 
			 * System.out.println(sb.toString());
			 */
		}
		return freq2;
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
