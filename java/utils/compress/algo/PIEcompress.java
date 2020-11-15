/**
 * 
 */
package com.zoubworld.java.utils.compress.algo;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.PIE.Node2;
import com.zoubworld.java.utils.compress.PIE.Tree;

/**
 * @author pierre valleau this class compress symbol based on PIE symbol :Past
 *         Index encoding : PIE+index+Size
 */
public class PIEcompress implements IAlgoCompress {
	@Override
	public String getName() {
		
		return "PIEcompress()";
	}
	Tree<ISymbol, Long> tree;
	Node2<ISymbol, Long> currentLeaf;
	Long index;
	ISymbol n1;
	ISymbol n2;
	List<ISymbol> uncompressedflux;

	public List<ISymbol> processUncompress(ISymbol n0) {
		if (n2 == Symbol.PIE) {
			// List<ISymbol> ls=new ArrayList();
			/*
			 * SymbolINT64 i1=(SymbolINT64)n1; Long pos=i1.getS2().getLong(); SymbolINT64
			 * i0=(SymbolINT64)n0; Long len=i0.getS2().getLong();
			 */
			Long pos = Symbol.getLong(n1);
			Long len = Symbol.getLong(n0);
			for (Long i = pos; i < pos + len; i++)
				uncompressedflux.add(uncompressedflux.get((int) i.intValue()));

			// uncompressedflux.addAll(ls);

			n2 = n1 = n0 = null;
			index++;
			return uncompressedflux;
		} else if (n2 != null) {
			/*
			 * List<ISymbol> ls=new ArrayList();
			 * 
			 * ls.add(n2);
			 */
			uncompressedflux.add(n2);

			n2 = n1;
			n1 = n0;
			index++;
			return uncompressedflux;
		}
		n2 = n1;
		n1 = n0;
		index++;
		return null;
	}

	public List<ISymbol> processcompress(ISymbol s) {
		Node2<ISymbol, Long> nextNode = null;
		if (s != null)// end of stream
			nextNode = currentLeaf.get(s);
		if (nextNode == null)// no more match
		{
			// 0
			// currentLeaf.add(index, s);
			getTree().add(currentLeaf, index, s);
			List<ISymbol> ls;
			Long len = currentLeaf.length();
			Long pos = null;
			if (currentLeaf.getIndex() != null)
				pos = currentLeaf.getIndex() - len + 1;// currentLeaf.pos();
			ls = new ArrayList<ISymbol>();
			// save currentLeaf.getSymbols()
			// add s to tree
			// filltree(pos,currentLeaf.getSymbols());//leaf=A,A,C,D,E,F,pos=0=>add
			// ACDEF1,CDEF2,DEF3,EF4,F5
			if (currentLeaf == tree.getRoot()) {
				if (s != null)
					ls.add(s);

			} else {
				if (len > 2) {
					ls.add(Symbol.PIE);
					/*
					 * if (pos<Byte.MAX_VALUE) ls.add(new SymbolINT8(pos.byteValue())); else if
					 * (pos<Short.MAX_VALUE) ls.add(new SymbolINT16(pos.shortValue())); else if
					 * (pos<Integer.MAX_VALUE) ls.add(new SymbolINT32(pos.intValue())); else
					 * 
					 */ ls.add(Symbol.FactorySymbolINT(pos.longValue()));

					/*
					 * if (len<Byte.MAX_VALUE) ls.add(new SymbolINT8(len.byteValue())); else if
					 * (len<Short.MAX_VALUE) ls.add(new SymbolINT16(len.shortValue())); else if
					 * (len<Integer.MAX_VALUE) ls.add(new SymbolINT32(len.intValue())); else
					 */
					ls.add(Symbol.FactorySymbolINT(len.longValue()));

				} else

				{
					ls = currentLeaf.getSymbols();
				}

				currentLeaf = tree.getRoot().get(s);
				if (currentLeaf == null)
					currentLeaf = tree.getRoot().add(index, s);
			}
			index++;
			return ls;
		} else {
			currentLeaf = nextNode;
			index++;
			return null;
		}
	}

	@Override
	public List<ISymbol> encodeSymbol(List<ISymbol> ls) {
		tree = new Tree<ISymbol, Long>();
		currentLeaf = tree.getRoot();
		index = 0L;

		List<ISymbol> lsc = new ArrayList<ISymbol>();
		for (ISymbol s : ls) {
			List<ISymbol> sc = processcompress(s);
			if (sc != null)
				lsc.addAll(sc);
			// 1tree.add(0L,index,ls);
			// tree.add(index-1, s);
		}
		List<ISymbol> sc = processcompress(null);
		if (sc != null)
			lsc.addAll(sc);
		return lsc;
	}

	@Override
	public List<ISymbol> decodeSymbol(List<ISymbol> ls) {
		index = 0L;
		n1 = n2 = null;
		uncompressedflux = new ArrayList<ISymbol>();

		// List<ISymbol> lsc=new ArrayList<ISymbol>();
		for (ISymbol s : ls) {
			/* List<ISymbol> sc= */processUncompress(s);
			/*
			 * if (sc!=null) sc.addAll(sc);
			 */
		}
		if (n2 != null)
			uncompressedflux.add(n2);
		if (n1 != null)
			uncompressedflux.add(n1);

		return uncompressedflux;
	}

	/**
	 * 
	 */
	public PIEcompress() {
		tree = new Tree<ISymbol, Long>();
		currentLeaf = tree.getRoot();
		index = 0L;

		n1 = n2 = null;
		uncompressedflux = new ArrayList<ISymbol>();

	}

	public static void main(String[] args) {
		/*
		 * PIEcompress cmp=new PIEcompress(); List<ISymbol>
		 * ls=FileSymbol.read("res/test/small_ref/pie2.txt"); List<ISymbol>
		 * lsc=cmp.compress(ls);
		 * 
		 * System.out.println(Symbol.PrintFreq(ls)); Map<ISymbol, Long> mfreq =
		 * Symbol.Freq(lsc); HuffmanCode hc= new HuffmanCode(); HuffmanNode rootnode =
		 * hc.buildTrie(mfreq); hc.buildCode(rootnode, ""); long size=hc.getSize(mfreq);
		 * System.out.println(ls.size()+"<=>"+lsc.size()+"<=>"+size);
		 * System.out.println(hc.codesToString(mfreq)); FileSymbol.saveCompressedAs(lsc,
		 * "res/result.test/test/small_ref/pie2.pie");
		 * JavaUtils.saveAs("res/result.test/test/small_ref/pie2.pietxt",lsc.stream().
		 * map(Object::toString).collect(Collectors.joining(",")));
		 * JavaUtils.saveAs("res/result.test/test/small_ref/pie2.tree",cmp.getTree().
		 * toString());
		 */}

	public static void main2(String[] args) {
		// TODO Auto-generated method stub
		/*
		 * Tree<ISymbol,Long> tree=new Tree(); List<ISymbol> l=new ArrayList();
		 * l.add(Symbol.findId('A')); l.add(Symbol.findId('B'));
		 * l.add(Symbol.findId('C')); l.add(Symbol.findId('D'));
		 * l.add(Symbol.findId('E')); l.add(Symbol.findId('A'));
		 * l.add(Symbol.findId('B')); l.add(Symbol.findId('E'));
		 * 
		 * // tree.getRoot().add(tree,0L,Long.valueOf(l.size()),l);
		 * tree.add(0L,Long.valueOf(l.size()),l);
		 * 
		 * // tree.getRoot().add(0L, Symbol.findId('A')); // tree.getRoot().add(0L,
		 * Symbol.findId('B')); // tree.getRoot().add(0L, Symbol.findId('C')); //
		 * tree.getRoot().get( Symbol.findId('A')).add(0L, Symbol.findId('B')).add(0L,
		 * Symbol.findId('C')); // tree.getRoot().get( Symbol.findId('A')).add(0L,
		 * Symbol.findId('C'));
		 * 
		 * System.out.print(tree.getRoot().toString());
		 * assertEquals(tree.getRoot().toString(),""); System.exit(0);
		 */
	//	PIEcompress cmp = new PIEcompress();
		/*
		 * { int len=3; List<ISymbol> ls=new ArrayList(); for(int i=0;i<len;i++) {
		 * ls.add(Symbol.findId('a')); ls.add(Symbol.findId('b'));
		 * ls.add(Symbol.findId('c')); } for(int i=0;i<len;i++)
		 * ls.add(Symbol.findId('a')); for(int i=0;i<len;i++)
		 * ls.add(Symbol.findId('b')); for(int i=0;i<len;i++)
		 * ls.add(Symbol.findId('a'));
		 * 
		 * 
		 * List<ISymbol> lsc=cmp.compress(ls); List<ISymbol> lsd=cmp.uncompress(lsc); //
		 * assertEquals(ls,lsd); System.exit(0); }
		 */
		/*
		 * List<ISymbol> ls=FileSymbol.read("res/test/small_ref/pie.txt"); List<ISymbol>
		 * lsc=cmp.compress(ls);
		 * 
		 * 
		 * FileSymbol.saveCompressedAs(lsc, "res/result.test/test/small_ref/pie.pie");
		 * JavaUtils.saveAs("res/result.test/test/small_ref/pie.tree",cmp.tree.toString(
		 * ));
		 * 
		 * 
		 * List<ISymbol> lsd=cmp.uncompress(lsc);
		 * 
		 * FileSymbol.saveAs(FileSymbol.ExtractDataSymbol(lsd),
		 * "res/result.test/test/small_ref/pie.txt"); //
		 * assertEquals(JavaUtils.read("res/test/small_ref/pie.txt"), //
		 * JavaUtils.read("res/result.test/test/small_ref/pie.txt") // );
		 * System.out.println("File origin : "+FileSymbol.ExtractDataSymbol(ls).size());
		 * System.out.println("File compressed : "+lsc.size());
		 * System.out.println("File uncompressed : "+FileSymbol.ExtractDataSymbol(lsd).
		 * size());
		 * System.out.println("ratio  : "+(lsc.size()/(double)lsd.size()*100)+"%");
		 * System.out.println(Symbol.PrintFreq(lsc));
		 */
	}

	/**
	 * @return the tree
	 */
	public Tree<ISymbol, Long> getTree() {
		return tree;
	}

}
