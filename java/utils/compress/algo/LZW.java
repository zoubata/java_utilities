package com.zoubworld.java.utils.compress.algo;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.java.utils.compress.CompositeSymbol;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;

public class LZW {
	// dev time 4H 29/7/2018
	public LZW() {
		// TODO Auto-generated constructor stub
	}

	public List<ISymbol> decodeSymbol(List<ISymbol> lenc) {

		List<ISymbol> ldec = new ArrayList<ISymbol>();
		int state = 0;// no RLE
		long N = 1;
		long index = 0;
		// ISymbol previous=null;
		for (ISymbol e : lenc) {
			if (e == Symbol.PIE)

			{
				state = 1;// RLE

			} else if (state == 1)// index
			{
				state = 2;
				index = ((CompositeSymbol) e).getS2().getId();
			}

			else if (state == 2)// index
			{
				state = 3;
				N = ((CompositeSymbol) e).getS2().getId();

				state = 0;
				for (long i = index; i < N + index; i++)
					ldec.add(ldec.get((int) i));
				N = 1;
			}

			else
				ldec.add(e);
			// previous=e;
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
	// List<Integer> tab[]=new List<Integer>[256];
	ArrayList<Integer>[][] tab = (ArrayList<Integer>[][]) new ArrayList[256][];
	// aaaaaaaaaaaaaaaaaaaaaaaaaaaaabbbbbbbbbbbbbbbbbbbbbbbbbbbbcdcdcdcdcdcdcdcdcd

	public List<ISymbol> encodeSymbol(List<ISymbol> ldec) {
		List<ISymbol> lenc = new ArrayList<ISymbol>();
		ISymbol previous = null;
		int index = -1;
		for (index = 0; index < ldec.size(); index++) {
			ISymbol e = ldec.get(index);
			// index++;
			if (tab[e.getChar()] == null)
				tab[e.getChar()] = (ArrayList<Integer>[]) new ArrayList[256];

			if (null == previous) {
				previous = e;
			} else {
				if (tab[previous.getChar()][e.getChar()] == null) {
					tab[previous.getChar()][e.getChar()] = new ArrayList<Integer>();
				}
				if (!tab[previous.getChar()][e.getChar()].isEmpty()) {
					int len = 0;
					int indexcompress = -1;
					for (Integer i : tab[previous.getChar()][e.getChar()]) {
						int len2 = find(i, index - 1, ldec);
						if (len < len2) {
							len = len2;
							indexcompress = i;
						}
					}
					if (len > 3) {
						// compress
						lenc.add(Symbol.PIE);
						lenc.add(Symbol.FactorySymbolINT(indexcompress));
						lenc.add(Symbol.FactorySymbolINT(len));// new symbol
						tab[previous.getChar()][e.getChar()].add(index - 1);
						previous = null;
						index += len - 1 - 1;
					} else {

						lenc.add(previous);
						tab[previous.getChar()][e.getChar()].add(index - 1);
						previous = e;
					} //
				} else {
					lenc.add(previous);
					tab[previous.getChar()][e.getChar()].add(index - 1);

					previous = e;
				}
			}

		}
		if (previous != null)
			lenc.add(previous);

		return lenc;
	}

	// search length of identical of char seq index at i position, should be 2 or
	// upper. baed on tab[][] cache system.
	private int find(int i, int index, List<ISymbol> ldec) {
		int index2 = index;
		int i2 = i;
		for (; (index2 < ldec.size()) && (i2 < ldec.size()) && (ldec.get(index2) == ldec.get(i2)); i2++, index2++) {

		}
		return i2 - i;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/*
		 * LZW cmp= new LZW(); HuffmanCode huff=new HuffmanCode();
		 * 
		 * 
		 * { String filetxt=
		 * "C:\\\\Temp\\\\zip-test\\\\661P2H20_05_QNSJ8J_01_CP2_20170411T160536.csv";
		 * System.out.println(filetxt); File file = new File(filetxt); List<ISymbol>
		 * ls=Symbol.factoryFile( file.getAbsolutePath()); List<ISymbol>
		 * lse=cmp.encodeSymbol(ls); huff.encodeSymbol(lse,new BinaryStdOut(new
		 * File(filetxt+".LZW"))); huff.analyse(ls); huff.analyse(lse); } { String
		 * filetxt="C:\\\\Temp\\\\zip-test\\\\QPQRN-19.pbs";System.out.println(filetxt);
		 * File file = new File(filetxt); List<ISymbol> ls=Symbol.factoryFile(
		 * file.getAbsolutePath()); List<ISymbol> lse=cmp.encodeSymbol(ls);
		 * huff.encodeSymbol(lse,new BinaryStdOut(new File(filetxt+".LZW")));
		 * huff.analyse(ls); huff.analyse(lse); } { String
		 * filetxt="C:\\\\Temp\\\\zip-test\\\\cycling_eedata_1k_cold.lss";System.out.
		 * println(filetxt); File file = new File(filetxt); List<ISymbol>
		 * ls=Symbol.factoryFile( file.getAbsolutePath()); List<ISymbol>
		 * lse=cmp.encodeSymbol(ls); huff.encodeSymbol(lse,new BinaryStdOut(new
		 * File(filetxt+".LZW"))); huff.analyse(ls); huff.analyse(lse); } { String
		 * filetxt="C:\\\\Temp\\\\zip-test\\\\cycling_eedata_1k_cold.hex";System.out.
		 * println(filetxt); File file = new File(filetxt); List<ISymbol>
		 * ls=Symbol.factoryFile( file.getAbsolutePath()); List<ISymbol>
		 * lse=cmp.encodeSymbol(ls); huff.encodeSymbol(lse,new BinaryStdOut(new
		 * File(filetxt+".LZW"))); huff.analyse(ls); huff.analyse(lse); }
		 */
	}
}
