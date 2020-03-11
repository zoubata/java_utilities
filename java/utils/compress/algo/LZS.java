package com.zoubworld.java.utils.compress.algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.SymbolComplex.Sym_LZS;

public class LZS {
	// dev time 4H 29/7/2018
	public LZS() {
		// TODO Auto-generated constructor stub
	}

	public List<ISymbol> decodeSymbol(List<ISymbol> lenc) {

		List<ISymbol> ldec = new ArrayList<ISymbol>();

		int toIndex = 0;
		int fromIndex = 0;

		for (ISymbol s : lenc) {
			if (Sym_LZS.class.isInstance(s)) {
				Sym_LZS lzs = (Sym_LZS) s;
				long offset = lzs.getS1().getId();
				long length = lzs.getS2().getId();
				offset += ldec.size();
				assert offset >= 0;
				if (offset + length < ldec.size()) {
					ldec.addAll(ldec.subList((int) offset, (int) ((int) offset + length)));
				} else
					for (int index = (int) offset; index < (int) offset + length; index++)
						ldec.add(ldec.get(index));
			} else
				ldec.add(s);
		}

		return ldec;
	}

	/** wide of slide windows and max offset value */
	int sizewindow = 2048;
	/** max len of matched copy string */
	int MaxLen = 38;

	public List<ISymbol> encodeSymbol(List<ISymbol> ldec) {
		List<ISymbol> lenc = new ArrayList<ISymbol>();

		List<ISymbol> l = new ArrayList<ISymbol>();
		int toIndex = 0;
		int fromIndex = 0;
		int offsetold = -1;
		int offsetold2 = -1;
		// init stream
		lenc.add(ldec.get(toIndex));
		toIndex++;
		// loop in stream
		while (toIndex < ldec.size()) {
			l.add(ldec.get(toIndex));
			List<ISymbol> slidingwindow = ldec.subList(fromIndex, toIndex - 1);
			int offset = Collections.lastIndexOfSubList(slidingwindow, l) + fromIndex;
			if (offset >= fromIndex && (toIndex - offset) < sizewindow && l.size() < MaxLen) {
				// compress more than lenc.add(new Sym_LZS((offset-toIndex),l.size()));
			} else// no more possible
			if (offsetold >= fromIndex && l.size() > 2) {
				int size = l.size() - 1;
				int pos = (offsetold - (toIndex - size));
				lenc.add(new Sym_LZS(pos, size));
				int i = toIndex - size + (pos);
				l = ldec.subList(i, i + size);
				l = new ArrayList<ISymbol>();
				l.add(ldec.get(toIndex));
			} else {
				lenc.add(l.remove(0));
			}
			toIndex++;
			if (toIndex > (sizewindow + fromIndex))
				fromIndex++;
			offsetold2 = offsetold;
			offsetold = offset;
		}
		toIndex--;
		// end the stream
		if (offsetold2 >= 0 && l.size() > 2) {
			int size = l.size();
			int pos = (offsetold2 - (toIndex - size + 1));
			lenc.add(new Sym_LZS(pos, size));
			int i = toIndex - size + (pos);
			l = ldec.subList(i, i + size);

		} else {
			lenc.addAll(l);
		}
		return lenc;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
