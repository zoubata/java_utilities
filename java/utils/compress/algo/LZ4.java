package com.zoubworld.java.utils.compress.algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.SymbolComplex.Sym_LZ4;

public class LZ4 implements IAlgoCompress {

	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + MaxLen;
		result = prime * result + sizewindow;
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
		LZ4 other = (LZ4) obj;
		if (MaxLen != other.MaxLen)
			return false;
		if (sizewindow != other.sizewindow)
			return false;
		return true;
	}
	@Override
	public String getName() {
		
		return "LZ4()";
	}
	// dev time 4H 29/7/2018
	public LZ4() {
		// LZ4 Auto-generated constructor stub
	}

	public List<ISymbol> decodeSymbol(List<ISymbol> lenc) {

		List<ISymbol> ldec = new ArrayList<ISymbol>();

		long lenliterals = 0;
		long matchlength = 0;
		long offset = 0;
		for (ISymbol s : lenc) {
			/*
			 * if (lenliterals > 0) { ldec.add(s); lenliterals--; } else if (matchlength >
			 * 0) { offset += ldec.size(); assert offset >= 0; if (offset + matchlength <
			 * ldec.size()) { ldec.addAll(ldec.subList((int) offset, (int) ((int) offset +
			 * matchlength))); } else for (int index = (int) offset; index < (int) offset +
			 * matchlength; index++) ldec.add(ldec.get(index)); } else
			 */ {
				if (Sym_LZ4.class.isInstance(s)) {
					Sym_LZ4 lzs = (Sym_LZ4) s;
					lenliterals = lzs.getLenliterals();
					matchlength = lzs.getMatchlength();
					offset = lzs.getOffset();
					ldec.addAll(lzs.getLitterals());

					if (matchlength > 0) {
						offset += ldec.size();
						assert offset >= 0;
						if (offset + matchlength < ldec.size()) {
							ldec.addAll(ldec.subList((int) offset, (int) ((int) offset + matchlength)));
						} else
							for (int index = (int) offset; index < (int) offset + matchlength; index++)
								ldec.add(ldec.get(index));
					}

				}
			}

		}

		return ldec;
	}

	/** wide of slide windows and max offset value */
	int sizewindow = 65536;
	/** max len of matched copy string */
	int MaxLen = 1900;

	public List<ISymbol> encodeSymbol(List<ISymbol> ldec) {
		List<ISymbol> lenc = new ArrayList<ISymbol>();
		List<ISymbol> ltmp = new ArrayList<ISymbol>();

		List<ISymbol> l = new ArrayList<ISymbol>();
		int toIndex = 0;
		int fromIndex = 0;
		int offsetold = -1;
		int offsetold2 = -1;
		// init stream
		/*
		 * ltmp.add(ldec.get(toIndex)); toIndex++;
		 */
		// loop in stream
		while (toIndex < ldec.size()) {
			l.add(ldec.get(toIndex));
			List<ISymbol> slidingwindow;
			/* if(toIndex>=1) */
			slidingwindow = ldec.subList(fromIndex, toIndex);
			/*
			 * else slidingwindow = new ArrayList<ISymbol>();
			 */
			int offset = Collections.lastIndexOfSubList(slidingwindow, l) + fromIndex;
			if ((offset >= fromIndex) && ((toIndex - offset) < sizewindow) && (l.size() < MaxLen)
					&& (toIndex < (ldec.size() - 6))) {
				// compress more than lenc.add(new Sym_LZS((offset-toIndex),l.size()));
			} else// no more possible
			if (offsetold >= fromIndex && l.size() > 2 && (toIndex <= (ldec.size() - 6))) {
				int size = l.size();
				int pos = (offsetold - (toIndex - size) - 1);
				lenc.add(new Sym_LZ4(ltmp.size(), size, pos, ltmp));
				// lenc.addAll(ltmp);
				ltmp.clear();
				// int i = toIndex - size + (pos);
				// l = ldec.subList(i, i + size);
				l.clear();
				// l.add(ldec.get(toIndex));
			} else {
				ltmp.add(l.remove(0));
			}
			toIndex++;
			if (toIndex > (sizewindow + fromIndex))
				fromIndex++;
			offsetold2 = offsetold;
			offsetold = offset;
		}
		toIndex--;
		// end the stream
		{
			int size = l.size();
			int pos = (offsetold2 - (toIndex - size + 2));
			if (size == 0)
				pos = 0;
			lenc.add(new Sym_LZ4(ltmp.size(), size, pos, ltmp));
			// lenc.addAll(ltmp);
			ltmp.clear();
			// int i = toIndex - size + (pos);
			// l = ldec.subList(i, i + size);

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
