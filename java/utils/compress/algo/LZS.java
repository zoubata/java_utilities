package com.zoubworld.java.utils.compress.algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.zoubworld.java.utils.ListBeginEnd;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.SymbolComplex.Sym_LZS;
/**
 * personal implementation of LZ4/LZ...
 * */
public class LZS  implements IAlgoCompress {
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
		LZS other = (LZS) obj;
		if (MaxLen != other.MaxLen)
			return false;
		if (sizewindow != other.sizewindow)
			return false;
		return true;
	}


	public LZS() {
		 sizewindow = 2048;		
		 MaxLen = 38;
		 minlenCompress=3;
	}		
	/**
	 * windowsSize : windows observed for runtime dictionnary(2048)
	 * this parameter impact a lot the speed.
	 * minlenCompress : minimum length to put on dictionnary and use it(2)
	 * 2 allow to match a lot but you replace 2 symbol by LZS symbol plus an offset and a size
	 * 3 offer a better compromise.
	 * */
	public LZS(int windowsSize,int minlenCompress) {
		this.sizewindow = windowsSize;		
		 this.MaxLen = 255;
		 this.minlenCompress=minlenCompress;
	}
	public LZS(int windowsSize,int minlenCompress,int maxlenCompress) {
		this.sizewindow = windowsSize;		
		 this.MaxLen = maxlenCompress;
		 this.minlenCompress=minlenCompress;
	}
	public List<ISymbol> decodeSymbol(List<ISymbol> lenc) {

		List<ISymbol> ldec = new ArrayList<ISymbol>();

		int toIndex = 0;
		int fromIndex = 0;

		for (ISymbol s : lenc) {
			if (Sym_LZS.class.isInstance(s)) {
				Sym_LZS lzs = (Sym_LZS) s;
				long offset = lzs.getOffset();
				long length = lzs.getLength();
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
	int minlenCompress=2;

	public List<ISymbol> encodeSymbol(List<ISymbol> ldec) {
		List<ISymbol> lenc = new ArrayList<ISymbol>();
		ldec=new ListBeginEnd(ldec);
		List<ISymbol> l = new ArrayList<ISymbol>();
		int toIndex = 0;
		int fromIndex = 0;
		int offsetold = -1;
		int offsetold2 = -1;
		// init stream
		lenc.add(ldec.get(toIndex));
		toIndex++;
		List<ISymbol> slidingwindow=null;
		// loop in stream
		while (toIndex < ldec.size()) {
			l.add(ldec.get(toIndex));
			slidingwindow = ldec.subList(fromIndex, toIndex - 1);
			int offset = Collections.lastIndexOfSubList(slidingwindow, l) + fromIndex;
			if (offset >= fromIndex && (toIndex - offset) < sizewindow && l.size() < MaxLen) {
				// compress more than lenc.add(new Sym_LZS((offset-toIndex),l.size()));
			} else// no more possible
			if (offsetold >= fromIndex && l.size() > minlenCompress) {
				int size = l.size() - 1;
				int pos = (offsetold - (toIndex - size));
				lenc.add(buildsymbol(pos, size));
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
		if(toIndex>fromIndex)
		{
		slidingwindow = ldec.subList(fromIndex, toIndex - 1);
		offsetold2 = Collections.lastIndexOfSubList(slidingwindow, l) + fromIndex;		
		}
		else
			offsetold2=-1;
		if (offsetold2 >= 0 && l.size() > 2) {
			int size = l.size()-1;
			int pos = (offsetold2 - (toIndex - size ));
			lenc.add(buildsymbol(pos, size+1));
			int i = toIndex - size+1 + (pos);
			l = ldec.subList(i, i + 1+size);

		} else {
			lenc.addAll(l);
		}
		return lenc;
	}

	protected ISymbol buildsymbol(int pos, int size) {		
		return new Sym_LZS(pos, size);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "LZS";
	}
}
