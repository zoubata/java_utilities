package com.zoubworld.java.utils.compress.algo;

import java.util.*;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;

/**
 * @author Pierre Valleau
 * 
 *         inspired from
 *         https://algs4.cs.princeton.edu/55compression/LZW.java.html
 */
public class LZWBasic implements IAlgoCompress {
	@Override
	public String getName() {
		
		return "LZWBasic()";
	}
	/**
	 * Compress a string to a list of output symbols. come from
	 * https://algs4.cs.princeton.edu/55compression/LZW.java.html
	 */
	public static List<Integer> compress(String uncompressed) {
		// Build the dictionary.
		int dictSize = 256;
		Map<String, Integer> dictionary = new HashMap<String, Integer>();
		for (int i = 0; i < 256; i++)
			dictionary.put("" + (char) i, i);

		String w = "";
		List<Integer> result = new ArrayList<Integer>();
		for (char c : uncompressed.toCharArray()) {
			String wc = w + c;
			if (dictionary.containsKey(wc))
				w = wc;
			else {
				result.add(dictionary.get(w));
				// Add wc to the dictionary.
				dictionary.put(wc, dictSize++);
				w = "" + c;
			}
		}

		// Output the code for w.
		if (!w.equals(""))
			result.add(dictionary.get(w));
		return result;
	}

	@Override
	public List<ISymbol> encodeSymbol(List<ISymbol> uncompressed) {
		// Build the dictionary.
		int dictSize = Symbol.getNbSymbol();
		Map<List<ISymbol>, ISymbol> dictionary = new HashMap<List<ISymbol>, ISymbol>();
		for (int i = 0; i < 256; i++) {
			List<ISymbol> l = new ArrayList<ISymbol>();
			l.add(Symbol.findId(i));
			dictionary.put(l, Symbol.FactorySymbolINT(i));
		}
		List<ISymbol> w = new ArrayList<ISymbol>();
		List<ISymbol> result = new ArrayList<ISymbol>();
		for (ISymbol c : uncompressed) {
			List<ISymbol> wc = new ArrayList<ISymbol>();
			wc.addAll(w);
			wc.add(c);
			if (dictionary.containsKey(wc))
				w = wc;
			else {
				result.add(dictionary.get(w));
				// Add wc to the dictionary.
				dictionary.put(wc, Symbol.FactorySymbolINT(dictSize++));
				w.clear();
				w.add(c);
			}
		}
		// Output the code for w.
		if (!(w.size() == 0))
			result.add(dictionary.get(w));
		return result;
	}

	/**
	 * Decompress a list of output ks to a string. come from
	 * https://algs4.cs.princeton.edu/55compression/LZW.java.html
	 */
	public static String decompress(List<Integer> compressed) {
		// Build the dictionary.
		int dictSize = 256;
		Map<Integer, String> dictionary = new HashMap<Integer, String>();
		for (int i = 0; i < 256; i++)
			dictionary.put(i, "" + (char) i);

		String w = "" + (char) (int) compressed.remove(0);
		StringBuffer result = new StringBuffer(w);
		for (int k : compressed) {
			String entry;
			if (dictionary.containsKey(k))
				entry = dictionary.get(k);
			else if (k == dictSize)
				entry = w + w.charAt(0);
			else
				throw new IllegalArgumentException("Bad compressed k: " + k);

			result.append(entry);

			// Add w+entry[0] to the dictionary.
			dictionary.put(dictSize++, w + entry.charAt(0));

			w = entry;
		}
		return result.toString();
	}

	@Override
	public List<ISymbol> decodeSymbol(List<ISymbol> compressed) {
		// Build the dictionary.
		int dictSize = Symbol.getNbSymbol();
		Map<ISymbol, List<ISymbol>> dictionary = new HashMap<ISymbol, List<ISymbol>>();
		for (int i = 0; i < 256; i++) {
			List<ISymbol> l = new ArrayList<ISymbol>();
			l.add(Symbol.findId(i));
			dictionary.put(Symbol.FactorySymbolINT(i), l);
		}
		List<ISymbol> w = new ArrayList<ISymbol>();
		ISymbol s = compressed.remove(0);
		List<ISymbol> ss = dictionary.get(s);
		w.addAll(ss);
		List<ISymbol> result = new ArrayList<ISymbol>();
		result.addAll(w);
		for (ISymbol k : compressed) {
			List<ISymbol> entry = new ArrayList<ISymbol>();
			if (dictionary.containsKey(k))
				entry.addAll(dictionary.get(k));
			else if (k.equals(Symbol.FactorySymbolINT(dictSize))) {
				entry.clear();
				entry.addAll(w);
				entry.add(w.get(0));
			} else
				throw new IllegalArgumentException("Bad compressed k: " + k);

			result.addAll(entry);

			// Add w+entry[0] to the dictionary.
			w.add(entry.get(0));
			dictionary.put(Symbol.FactorySymbolINT(dictSize++), w);

			w = entry;
		}
		return result;
	}

	public static void main(String[] args) {
		/*
		 * LZWBasic proc=new LZWBasic(); String s="TO BE OR NOT TO BE OR TO BE OR NOT";
		 * s=LZWBasic.file; { List<Integer> compressed = compress(s);
		 * System.out.println(compressed.size()+":"+compressed); String decompressed =
		 * decompress(compressed);
		 * System.out.println(decompressed.length()+":"+decompressed); } { List<ISymbol>
		 * compressed = proc.encodeSymbol(Symbol.from(s));
		 * System.out.println(compressed.size()+":"+compressed); List<ISymbol>
		 * decompressed = proc.decodeSymbol(compressed);
		 * System.out.println(decompressed.size()+":"+Symbol.listSymbolToString(
		 * decompressed));
		 * 
		 * }
		 */

	}

}