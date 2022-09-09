package com.zoubworld.java.utils.compress.binalgo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.zoubworld.java.utils.compress.Code;
import com.zoubworld.java.utils.compress.ICode;
import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.file.BinaryFinFout;
import com.zoubworld.java.utils.compress.file.BinaryStdIn;
import com.zoubworld.java.utils.compress.file.BinaryStdOut;
import com.zoubworld.java.utils.compress.file.IBinaryReader;
import com.zoubworld.java.utils.compress.file.IBinaryWriter;
import com.zoubworld.utils.JavaUtils;

import com.zoubworld.extern.edu.princeton.cs.algs4.MinPQ;

/******************************************************************************
 *  Compilation:  javac Huffman.java
 *  Execution:    java Huffman - < input.txt   (compress)
 *  Execution:    java Huffman + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   https://algs4.cs.princeton.edu/55compression/abra.txt
 *                https://algs4.cs.princeton.edu/55compression/tinytinyTale.txt
 *                https://algs4.cs.princeton.edu/55compression/medTale.txt
 *                https://algs4.cs.princeton.edu/55compression/tale.txt
 *
 *  Compress or expand a binary input stream using the Huffman algorithm.
 *
 *  % java Huffman - < abra.txt | java BinaryDump 60
 *  010100000100101000100010010000110100001101010100101010000100
 *  000000000000000000000000000110001111100101101000111110010100
 *  120 bits
 *
 *  % java Huffman - < abra.txt | java Huffman +
 *  ABRACADABRA!
 *
 ******************************************************************************/

/**
 * The {@code Huffman} class provides static methods for compressing and
 * expanding a binary input using Huffman codes over the 8-bit extended ASCII
 * alphabet.
 * <p>
 * For additional documentation, see
 * <a href="https://algs4.cs.princeton.edu/55compress">Section 5.5</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne reworked by zoubata 8h
 */
public class HuffmanCode implements ICodingRule {
	// alphabet size of extended ASCII
	static private int R = 256;
	static int Nb = 8;
	private IBinaryWriter binaryStdOut_internal = new BinaryStdOut();
	private IBinaryReader binaryStdIn_internal = new BinaryStdIn();
	/*
	 * static List<HuffmanCode> tables; static public boolean add(HuffmanCode e) {
	 * return getTables().add(e); }
	 * 
	 * public static List<HuffmanCode> getTables() { if (tables==null) tables=new
	 * ArrayList<HuffmanCode> (); return tables; }
	 */

	// Do not instantiate.
	public HuffmanCode() {
		R = 256 + Symbol.special.length;
		Nb = (int) (Math.log(R) / Math.log(2) + 1);
		// HuffmanCode.add(this);
	}

	public HuffmanNode root = null;

	public HuffmanCode(IBinaryReader binaryStdIn2) {
		R = 256 + Symbol.special.length;
		Nb = (int) (Math.log(R) / Math.log(2) + 1);
		root = readTrie(binaryStdIn2);
		codingrule = null;
		binaryStdIn_internal = binaryStdIn2;
		// buildCode(/* String[] st, */ root, "");
		// HuffmanCode.add(this);
	}

	/**
	 * estimate the octet size of a file based on the frequency map after an huffman
	 * compression
	 */
	public Long getSize(Map<ISymbol, Long> freq) {
		return getBitSize(freq) / 8 + 1;
	}

	// https://fr.wikipedia.org/wiki/Entropie_de_Shannon
	static public <K> Double getEntropie(Map<K, Long> freq) {
		Double e = 0.0;
	//	Map<K, Double> P = new HashMap<K, Double>();
		Long nb = 0L;
		for (Long l : freq.values())
			nb += l;
		/*
		 * for(K k:freq.keySet()) P.put(k,freq.get(k).doubleValue()/nb);
		 */
		for (K i : freq.keySet()) {
			Double Pi = freq.get(i).doubleValue() / nb;
			if (Pi != 0)
				e += -Pi * Math.log(Pi) / Math.log(2);
		}
		return e;

	}

	public Double getEntropie() {
		Map<Integer, Long> mfreq = new HashMap<Integer, Long>();
		for (int i = 0; i < freq.length; i++)
			mfreq.put(i, (long) freq[i]);
		return getEntropie(mfreq);

	}

	/**
	 * estimate the bit size of a file based on the frequency map after an huffman
	 * compression
	 */
	public Long getBitSize(Map<ISymbol, Long> freq) {
		Long l = 0L;
		int nb = (int) (Math.log10(freq.keySet().size()) / Math.log10(2));
		nb++;
		root = getRoot(freq);
		for (ISymbol key : freq.keySet())
			l += key.getCode().length() * freq.get(key);
		for (ISymbol key : freq.keySet())
			l += key.getCode().length() + nb;
		/*
		 * for (ISymbol key:freq.keySet())
		 * System.out.println("'"+key+"' ->"+key.getCode().length()+" : "+key.getCode().
		 * toRaw()+" "+freq.get(key));
		 */
		return l;
	}

	public HuffmanNode getRoot(Map<ISymbol, Long> freq) {

		root = buildTrie(freq);
		codingrule = null;
		// 2�896 used tab 85 symbol =112o
		// 3�145 full tab 265 symbol=361
		// 2�784 no tab

		// print trie for decoder
		// WriteTable(root,binaryStdOut);

		// build code table
		/* st = new String[R]; */
		buildCode(/* st, */root, "");
		return root;
	}

	int[] freq = null;

	/* String[] st ; */
	/**
	 * Reads a sequence of 8-bit bytes from standard input; compresses them using
	 * Huffman codes with an 8-bit alphabet; and writes the results to standard
	 * output. * binstream=table+size+list<code>
	 */
	public void compress() {
		// read the input
		String s = binaryStdIn_internal.readString();
		char[] input = s.toCharArray();

		// tabulate frequency counts
		freq = new int[R];
		/*
		 * for (int i = 0; i < R; i++) freq[i]++;//stop dirrentes from 0, to keep all
		 * symbol
		 */
		for (int i = 0; i < input.length; i++)
			freq[input[i]]++;
		// printFreqs();
		// build Huffman trie
		root = buildTrie(freq);
		codingrule = null;
		// 2�896 used tab 85 symbol =112o
		// 3�145 full tab 265 symbol=361
		// 2�784 no tab

		// print trie for decoder
		WriteTable(root, binaryStdOut_internal);

		// build code table
		/* st = new String[R]; */
		buildCode(/* st, */root, "");
		// printCodes();
		/*
		 * for(int i=0;i<R;i++) if (freq[i]>0) System.out.println((((i>31) &&
		 * (i<127))?("'" + (char)i + "'  "):(String.format("0x%2x ", i))) +
		 * " : "+String.format( "%2.3f", freq[i]* 100.0 / (input.length ))+"% : " +
		 * Symbol.findId(i).getCode().toString());
		 */

		// print number of bytes in original uncompressed message
		binaryStdOut_internal.write(input.length);

		// use Huffman code to encode input
		for (int i = 0; i < input.length; i++) {
			ISymbol sym = Symbol.findId(input[i]);
			binaryStdOut_internal.write(sym.getCode().getLong(), sym.getCode().length());
		}
		// close output stream
		binaryStdOut_internal.close();
	}

	// build the Huffman trie given frequencies
	static public HuffmanNode buildTrie(Map<ISymbol, Long> freq) {
		if (freq.size()==0)
			return null;
		// initialze priority queue with singleton trees
		MinPQ<HuffmanNode> pq = new MinPQ<HuffmanNode>();
		for (ISymbol key : freq.keySet())
			if (freq.get(key) > 0)
				pq.insert(new HuffmanNode(null, key, freq.get(key), null, null));

		// special case in case there is only one character with a nonzero frequency
		if (pq.size() == 1) {
			ISymbol sym = freq.keySet().iterator().next();
			if (freq.get(sym) == 0)
				pq.insert(new HuffmanNode(null, '\0', 0, null, null));
			else
				pq.insert(new HuffmanNode(null, '\1', 0, null, null));
		}

		// merge two smallest trees
		while (pq.size() > 1) {
			HuffmanNode left = pq.delMin();
			HuffmanNode right = pq.delMin();
			HuffmanNode parent = new HuffmanNode(null, '\0', left.freq + right.freq, left, right);
			pq.insert(parent);
		}
		return pq.delMin();
	}

	// build the Huffman trie given frequencies
	static private HuffmanNode buildTrie(int[] freq) {

		// initialze priority queue with singleton trees
		MinPQ<HuffmanNode> pq = new MinPQ<HuffmanNode>();
		for (char i = 0; i < freq.length; i++)
			if (freq[i] > 0)
				pq.insert(new HuffmanNode(null, i, freq[i], null, null));

		// special case in case there is only one character with a nonzero frequency
		if (pq.size() == 1) {
			if (freq['\0'] == 0)
				pq.insert(new HuffmanNode(null, '\0', 0, null, null));
			else
				pq.insert(new HuffmanNode(null, '\1', 0, null, null));
		}

		// merge two smallest trees
		while (pq.size() > 1) {
			HuffmanNode left = pq.delMin();
			HuffmanNode right = pq.delMin();
			HuffmanNode parent = new HuffmanNode(null, '\0', left.freq + right.freq, left, right);
			pq.insert(parent);
		}
		return pq.delMin();
	}

	public void printCodes() {
		System.out.println(codesToString());
	}

	public String toString() {
		return "HuffManCode(" + codesToString() + ")";

	}

	public String codesToString() {
		String s = ("--- Printing Codes ---\n");
		if (freq == null && root == null) {
			s += "Symbols : " + R + "\n";

			for (int i = 0; i < R; i++)
				s += (((i > 31) && (i < 127)) ? ("'" + (char) i + "'\t") : (String.format("0x%2x\t", i))) + ": "
						+ Symbol.findId(i).getCode().toString() + "\n";
		} else if (freq != null) {
			s += "Symbols : " + freq.length + "\n";
			for (int i = 0; i < freq.length; i++) {
				ISymbol sym = Symbol.findId(i);
				ICode code = get(sym);
				if (code == null)// no code
				{
					s += (((i > 31) && (i < 127)) ? ("'" + (char) i + "'\t") : (String.format("0x%2x\t", i))) + ": "
							+ "''" + "\n";
				} else
					s += (((i > 31) && (i < 127)) ? ("'" + (char) i + "'\t") : (String.format("0x%2x\t", i))) + ": "
							+ code.toString() + "\n";
			}
		} else {
			List<HuffmanNode> ls = getAllLeaf(root);
			s += "Nodes : " + ls.size() + "\n";
			for (HuffmanNode l : ls) {
				long i = 0;
				if (l.ch != null)
					i = l.ch.getId();
				else {
					System.err.println("one symbol is null of the tree");
				}
				s += (((i > 31) && (i < 127)) ? ("'" + l.ch + "'\t") : (String.format("0x%2x\t", i))) + ": "
						+ get(l.ch) + "\n";
			}
		}
		return s;

	}
	public String ToFilecodes()
	{
		String s="";
		for(int i=0;i<Symbol.getNbSymbol();i++)
			if (get(Symbol.findId(i))!=null)
				s+=String.format("0x%x\t:\t0b%s\t//%3.3f%%\n", i,get(Symbol.findId(i)).toRaw(),freq(Symbol.findId(i)));
		return s;
	}
	
	Long count=null;
	private double freq(ISymbol findId) {
		if (count==null)
		{count=0L;
for(Integer c:freq)
	count+=c==null?0:c;}
		double rr=freq[(int) findId.getId()];
		rr/=count;
		return rr*100;
	}

	public String codesToString(Map<ISymbol, Long> mfreqorig) {
		Map<ISymbol, Long> mfreq = JavaUtils.SortMapByValue(mfreqorig);
		String s = ("--- Printing Codes ---\n");
		for (ISymbol sym : mfreq.keySet())
			s += (((sym.getId() > 31) && (sym.getId() < 127)) ? ("'" + (char) sym.getId() + "'\t")
					: ((sym.getId() < 256) ? (String.format("\\x%x ", sym.getId())) : sym.toString())) + ":\t"
					+ mfreq.get(sym) + "\t:\t" + sym.getCode().toString() + "\n";
		return s;
	}

	public void printFreqs() {
		System.out.println(toFreqString());
	}

	public String toFreqString() {
		int length = 0;
		String s = "";
		if (freq != null) {
			for (int i = 0; i < freq.length; i++)
				length += freq[i];
			for (int i = 0; i < freq.length; i++)

				if (freq[i] != 0)
					s += ((((i > 31) && (i < 127)) ? ("'" + (char) i + "'  ") : (String.format("0x%2x ", i))) + " : "
							+ String.format("%2.3f", freq[i] * 100.0 / (length)) + "%") + "\n";
		}
		return s;
	}

	public void analyse(List<ISymbol> ldec) {
		// read the input

		// tabulate frequency counts
		freq = new int[R];
		for (int i = 0; i < ldec.size(); i++) {
			ISymbol sym = ldec.get(i);
			int id = (int) sym.getId();
			freq[id]++;
		}
		printFreqs();
		int length = 0;
		for (int i = 0; i < freq.length; i++)
			length += freq[i];
		double entropie = 0;
		int N = 0;
		for (int i = 0; i < freq.length; i++)
			if (freq[i] != 0)
				entropie += -(double) freq[i] / (double) length * Math.log((double) freq[i] / (double) length)
						/ Math.log(2);
			else
				N++;
		System.out.println(
				"Entropie binaire : " + String.format("%3.6f", entropie) + " With " + N + " symbol(s) missing");
		System.out.println("huffman compress ration target : " + String.format("%3.1f", (8 - entropie) / 8.0 * 100.0)
				+ "%, " + length + " bytes ; estimate size " + String.format("%3.0f", length * (8 - entropie) / 8.0)
				+ " bytes .");

	}

	/**
	 * change the symbol coding. and return the image of Huffman tree
	 */
	public HuffmanNode encodeSymbol(List<ISymbol> ldec) {

		// read the input

		// tabulate frequency counts
		freq = getFreq(ldec);
		// printFreqs();

		// build Huffman trie
		root = buildTrie(freq);
		codingrule = null;

		return root;
	}

	/** build the coding set form a symbol list
	 * */
	static public HuffmanCode Factory(List<ISymbol> ldec)
	{
		Map<ISymbol, Long> f = ISymbol.Freq(ldec);
		//Map<ISymbol, Long> f = Symbol.FreqId(ldec);
		
		return Factory(f);
	}
	 public HuffmanCode(List<ISymbol> ldec) {
		 Map<ISymbol, Long> f = Symbol.FreqId(ldec);
		 root = buildTrie(f);		
		 buildCode(/* st, */ getRoot(), "");
	}
	 public HuffmanCode(Map<ISymbol, Long> f) {
		 root = buildTrie(f);		
		 buildCode(/* st, */ getRoot(), "");
	}
	/**
	 * build the coding set form a frequency table
	 */
	static public HuffmanCode Factory(int[] freq) {
		HuffmanCode h = Factory(buildTrie(freq));
		h.freq = freq;
		return h;
	}

	/**
	 * build the coding set form a frequency table
	 */
	static public HuffmanCode Factory(Map<ISymbol, Long> freq) {
		if(freq.size()==0)
			return null;
		HuffmanNode n = buildTrie(freq);
		HuffmanCode h = Factory(n);
		/*
		 *h.freq = new int[Symbol.getNbSymbol()];
		for (Entry<ISymbol, Long> e : freq.entrySet())
			h.freq[(int) e.getKey().getId()] = e.getValue().intValue();
		*/
		return h;
	}

	
		/**
	 * build the coding set for the Huffman tree
	 */
	static public HuffmanCode Factory(HuffmanNode root) {
			if(root==null)
			return null;
		HuffmanCode cs = new HuffmanCode();
		cs.buildCode(root, "");
		cs.root = root;
		/*
		 * CodingSet cs=new CodingSet(null); ISymbol sym = root.ch;
		 * 
		 * for(HuffmanNode lf:getAllLeaf( root)) { sym = lf.ch; ICode code = toCode(lf);
		 * cs.put(sym, code); }
		 */
		return cs;

	}

	private static List<HuffmanNode> getAllLeaf(HuffmanNode root) {
		List<HuffmanNode> ls = null;
		if (!root.isLeaf()) {
			ls = getAllLeaf(root.left);
			ls.addAll(getAllLeaf(root.right));
		} else {
			ls = new ArrayList<HuffmanNode>();
			ls.add(root);
		}
		return ls;
	}

	private static ICode toCode(HuffmanNode node) {

		ICode c;
		if (node.getParent() == null)
			c = new Code();

		else {
			c = toCode(node.getParent());
			if (node.getParent().left == node)
				c.huffmanAddBit('0');
			else if (node.getParent().right == node)
				c.huffmanAddBit('1');
		} /*
			 * if(!node.isLeaf()) return null;
			 */
		return c;
	}
	// write bitstring-encoded trie to standard output

	// make a lookup table from symbols and their encodings

	public void buildCode(/* String[] st, */ HuffmanNode x, String s) {
		if (!x.isLeaf()) {
			buildCode(/* st, */ x.left, s + '0');
			buildCode(/* st, */ x.right, s + '1');
		} else {
			Code c = new Code(s);
			x.ch.setCode(c);
			c.setSymbol(x.ch);
			/* st[x.ch.getId()] = s; */
		}
	}

	/**
	 * update currents symbol to use the code of this huffman tree/table
	 */
	public void buildCode() {
		if (getRoot() == null)
			Symbol.initCode();// default coding.
		else
			buildCode(/* st, */ getRoot(), "");// huffman coding.
		codingrule = null;

	}

	static public int[] getFreq(List<ISymbol> ldec) {
		int[] Myfreq = new int[R];
		for (int i = 0; i < ldec.size(); i++) {
			ISymbol sym = ldec.get(i);
			int id = (int) sym.getId();
			Myfreq[id]++;
		}
		return Myfreq;
	}

	/**
	 * binout =Symbol.huffman + trie + list<code> + EOBS
	 */
	public void encodeSymbol(List<ISymbol> ldec, IBinaryWriter binaryStdOut) {

		root = encodeSymbol(ldec);

		writeTrie(root, binaryStdOut);
		// build code table

		// print number of bytes in original uncompressed message
		// binaryStdOut.write(ldec.size());
		WriteSymbol(ldec, binaryStdOut);
		binaryStdOut.write(Symbol.EOBS);

		// return lenc;
	}

	/**
	 * binout =Symbol.huffman + trie + list<code> + EOBS but trie isn't encoded
	 */
	public void storeSymbol(List<ISymbol> ldec, IBinaryWriter binaryStdOut) {

		// root = encodeSymbol(ldec);
		// Code.reworkCode(ldec, Code.getDefaultLength());
		binaryStdOut.setCodingRule(new CodingSet(CodingSet.NOCOMPRESS));
		writeTrie(root, binaryStdOut);
		// build code table

		// print number of bytes in original uncompressed message
		// binaryStdOut.write(ldec.size());
		// WriteSymbol(ldec, binaryStdOut);
		binaryStdOut.writes(ldec);
		binaryStdOut.write(Symbol.EOBS);

		// return lenc;
	}

	/**
	 * * binout =huffman trie
	 */
	static public void WriteTable(HuffmanNode x, IBinaryWriter binaryStdOut2) {

		if (x.isLeaf()) {
			binaryStdOut2.write(true);
			/*
			 * if (x.ch.isChar()) binaryStdOut2.write(x.ch); else
			 */
			binaryStdOut2.write(x.ch);
			return;
		}
		binaryStdOut2.write(false);
		WriteTable(x.left, binaryStdOut2);
		WriteTable(x.right, binaryStdOut2);
	}

	// deprecated
	public void WriteTable(ICode code) {
		HuffmanCode.WriteTable(this.root, code);
	}

	// deprecated
	static public void WriteTable(HuffmanNode x, ICode code) {
		if (code == null)
			return;
		if (x.isLeaf()) {
			code.huffmanAddBit('1');
			if (x.ch.isChar())
				for (int i = 0; i < Nb; i++)
					code.huffmanAddBit((char) (((x.ch.getId() >> i) & 1) + '0'));
			return;
		}
		code.huffmanAddBit('0');
		WriteTable(x.left, code);
		WriteTable(x.right, code);
	}

	/**
	 * binout = list<codehuffman>
	 */
	public void WriteSymbol(List<ISymbol> ldec, IBinaryWriter binaryStdOut) {

		buildCode(/* st, */ root, "");

		// printCodes();

		// print number of bytes in original uncompressed message

		// use Huffman code to encode input
		for (int i = 0; i < ldec.size(); i++) {
			;

			// binaryStdOut.write(ldec.get(i).getCode().getLong(),
			// ldec.get(i).getCode().length());
			binaryStdOut.write(ldec.get(i));

		}

		// close output stream
		// binaryStdOut.close();

		// return lenc;
	}

	public List<ISymbol> decodeSymbol(IBinaryReader binaryStdIn2) {
		List<ISymbol> ldec = new ArrayList<ISymbol>();
		/*
		 * read Symbol.HUFFMAN? // read in Huffman trie from input stream root =
		 * readTrie(binaryStdIn2); buildCode( root, "");
		 */
		// printCodes();
		// number of bytes to write
		// decode using the Huffman trie
		ISymbol sym = null;
		while (sym != Symbol.EOBS && sym != null) {
			sym = decodeASymbol(binaryStdIn2);
			if (sym == Symbol.HUFFMAN) {
				// ???build apply...
			}
			ldec.add(sym);
		}
		return ldec;
	}

	/**
	 * supposed : trie build and code also
	 */
	public ISymbol decodeASymbol(IBinaryReader binaryStdIn2) {

		ICode c = getGenericCode(binaryStdIn2);
		if (c == null)
			return null;
		ISymbol sym = c.getSymbol();

		if (sym.getId() > 256)// complex symbol
		{
			sym = Symbol.decode(sym, binaryStdIn2);

		} /*
			 * else sym=sym;
			 */
		if (c.getSymbol().equals(Symbol.CodingSet)
				||
				c.getSymbol().equals(Symbol.HUFFMAN))
		{	
			ICodingRule cr = ICodingRule.ReadCodingRule(c.getSymbol(),binaryStdIn2);
			binaryStdIn2.setCodingRule(cr);
			return cr.getCode(binaryStdIn2).getSymbol();
		}
		return sym;
	}

	/**
	 * Reads a sequence of bits that represents a Huffman-compressed message from
	 * standard input; expands them; and writes the results to standard output.
	 * binstream=table+size+list<code>
	 */
	public void expand() {
		// read in Huffman trie from input stream
		HuffmanNode root = readTrie(binaryStdIn_internal);
		// printCodes();
		// number of bytes to write
		int length = binaryStdIn_internal.readInt();
		// decode using the Huffman trie
		for (int i = 0; i < length; i++) {
			HuffmanNode x = root;
			while (!x.isLeaf()) {
				boolean bit = binaryStdIn_internal.readBoolean();
				if (bit)
					x = x.right;
				else
					x = x.left;
			}
			binaryStdOut_internal.write(x.ch.getChar(), 8);// x.ch
		}
		binaryStdOut_internal.close();
	}

	/**
	 * read the Huffman tree based on coding rules of binaryStdIn
	 */

	public HuffmanNode readTrie(IBinaryReader binaryStdIn2) {
/*
	HuffmanNode readTrie(IBinaryReader binaryStdIn2) {
		ICodingRule cold = binaryStdIn2.getCodingRule();
		binaryStdIn2.setCodingRule(new CodingSet(CodingSet.NOCOMPRESS));
		HuffmanNode r;
*/
		Boolean isLeaf = binaryStdIn2.readBoolean();
		if (isLeaf == null)
			r= null;
		if (isLeaf) {
			ICode c = binaryStdIn2.getCodingRule().getGenericCode(binaryStdIn2);
			r= new HuffmanNode(null, c.getSymbol(), -1, null, null);
		} else {
			r= new HuffmanNode(null, null, -1, readTrie(binaryStdIn2), readTrie(binaryStdIn2));
		}
		binaryStdIn2.setCodingRule(cold);
		
		return r;
	}

	/**
	 * * binout =Symbol.huffman + trie
	 */
	private void writeTrie(HuffmanNode x, IBinaryWriter binaryStdOut2) {
		
		binaryStdOut2.write(Symbol.HUFFMAN);
		ICodingRule cold = binaryStdOut2.getCodingRule();
		binaryStdOut2.setCodingRule(new CodingSet(CodingSet.NOCOMPRESS));
		
		WriteTable(x, binaryStdOut2);
		binaryStdOut2.setCodingRule(cold);
		
	}

	@Override
	public boolean equals(Object obj) {
		if (ICodingRule.class.isInstance(obj)) {
			ICodingRule c = (ICodingRule) obj;
			for (ISymbol sym : Symbol.getAll())
				if (!((this.get(sym) != null && this.get(sym).equals(c.get(sym)))
						|| (this.get(sym) == null && c.get(sym) == null)))
					return false;
			return true;
		} else
			return super.equals(obj);

	}

	/**
	 * Sample client that calls {@code compress()} if the command-line argument is
	 * "-" an {@code expand()} if it is "+".
	 *
	 * @param args
	 *            the command-line arguments
	 */
	public static void main(String[] args) {
		// getfiles(dir,ext,recursive)
		// freq(*)
		// huff(ext)

		HuffmanCode huff = new HuffmanCode();
		/*
		 * huff.binaryStdIn=new BinaryStdIn(new File(args[1])); huff.binaryStdOut= new
		 * BinaryStdOut(new File(args[2])); if (args[0].equals("-")) huff.compress();
		 * else if (args[0].equals("+")) huff.expand();
		 * 
		 * else throw new IllegalArgumentException("Illegal command line argument");
		 * 
		 * 
		 */
		{
			String filetxt = "C:\\Temp\\zip-test\\661P2H20_05_QNSJ8J_01_CP2_20170411T160536.csv";
			System.out.println(filetxt);
			huff.binaryStdIn_internal = new BinaryStdIn(new File(filetxt));
			huff.binaryStdOut_internal = new BinaryStdOut(new File(filetxt + ".huff"));
			// huff.compress();
			List<ISymbol> ls = Symbol.factoryFile(filetxt);
			huff.encodeSymbol(ls, huff.binaryStdOut_internal);
			// close output stream
			huff.binaryStdOut_internal.close();

			huff.analyse(ls);
		}
		{
			String filetxt = "C:\\Temp\\zip-test\\QPQRN-19.pbs";
			System.out.println(filetxt);
			huff.binaryStdIn_internal = new BinaryStdIn(new File(filetxt));
			huff.binaryStdOut_internal = new BinaryStdOut(new File(filetxt + ".huff"));
			// huff.compress();
			List<ISymbol> ls = Symbol.factoryFile(filetxt);
			huff.encodeSymbol(ls, huff.binaryStdOut_internal);
			huff.binaryStdOut_internal.close();
			huff.analyse(ls);
		}
		{
			String filetxt = "C:\\Temp\\zip-test\\cycling_eedata_1k_cold.lss";
			System.out.println(filetxt);
			huff.binaryStdIn_internal = new BinaryStdIn(new File(filetxt));
			huff.binaryStdOut_internal = new BinaryStdOut(new File(filetxt + ".huff"));
			// huff.compress();
			List<ISymbol> ls = Symbol.factoryFile(filetxt);
			huff.encodeSymbol(ls, huff.binaryStdOut_internal);
			huff.binaryStdOut_internal.close();
			huff.analyse(ls);
		}
		{
			String filetxt = "C:\\Temp\\zip-test\\cycling_eedata_1k_cold.hex";
			System.out.println(filetxt);
			huff.binaryStdIn_internal = new BinaryStdIn(new File(filetxt));
			huff.binaryStdOut_internal = new BinaryStdOut(new File(filetxt + ".huff"));
			// huff.compress();
			List<ISymbol> ls = Symbol.factoryFile(filetxt);
			huff.encodeSymbol(ls, huff.binaryStdOut_internal);
			huff.binaryStdOut_internal.close();
			huff.analyse(ls);
		}

	}
	
	ICodingRule codingrule;
/*  make no sens CodingSet is fix length
	
	public ICodingRule toCodingSet()
	{
		return getCodingRule() ;
	}*/
	/**
	 * to speed up the translation use internal cache(CodingSet)
	 * 
	 * @return the codingrule
	 */
	private ICodingRule getCodingRule() {
		if (codingrule == null) {
			root = getRoot();
			CodingSet cs = new CodingSet(CodingSet.UNDEFINED);
			ISymbol sym = root.ch;
			cs.flush();
			for (HuffmanNode lf : getAllLeaf(root)) {
				sym = lf.ch;
				ICode code = toCode(lf);
				cs.put(sym, code);
				sym.setCode(code);
				code.setSymbol(sym);
			}
			codingrule = cs;
			cs.len=-1;
		}
		return codingrule;
	}

	/**
	 * @return the root
	 */
	public HuffmanNode getRoot() {
		if (root == null)
			if (freq != null) {
				root = buildTrie(freq);
				codingrule = null;
			}
		return root;
	}

	@Override
	public ICode get(ISymbol sym) {
		return getCodingRule().get(sym);
	}

	@Override
	public ISymbol get(ICode code) {
		return getCodingRule().get(code);
	}

	@Override
	public ICode getCode(IBinaryReader binaryStdIn) {

		ISymbol s = decodeASymbol(binaryStdIn);
		return get(s);
	}

	@Override
	public ICode getGenericCode(IBinaryReader binaryStdIn2) {
		ISymbol sym = null;
		if (root == null)// use default coding sym=code (size=Nb)
		{
			int c = binaryStdIn2.readUnsignedInt(Nb);
			sym = sprout.Factory((long)c);
		} else {
			HuffmanNode x = root;
			while (!x.isLeaf()) {
				Boolean bit = binaryStdIn2.readBoolean();
				if (bit == null)
					return null;
				if (bit)
					x = x.right;
				else
					x = x.left;
			}
			sym = x.ch;
		}

		return sym.getCode();
	}

	@Override
	public ISymbol getSymbol(IBinaryReader binaryStdIn) {

		return decodeASymbol(binaryStdIn);
	}

	@Override
	public void writeCodingRule(IBinaryWriter binaryStdOut) {
		ICodingRule.super.writeCodingRule(binaryStdOut);
		// size=N*(1+1+size(code)+1)
		// size(292)=3212
		// size(19)=219
		writeTrie(getRoot(),binaryStdOut);
		//other option :
	//1	// swap: <id[12]+N[9]+N/2*size(code) : code(i)...code(j)
		//id=huf table of ref.
		//N : number of symbol redefined
		//code ordonn�/index� smallest, longest

		// swap(code(0),code(i)),....swap(code(last N/2),code(j))
		// size(292)=1326
		// size(19)=111

		// 2 //table update: N : id+S[9]+table(bit) n*(5+size(code)
		// S number of symbol redefine
		// id=huf table of ref.
		// size(292)=4401
		// size(19)=287
		// 4 // pick up/merge : id+idpickup+S[10]+1*S
		// S number of symbol redefine
		// id=huf table of ref.
		// idpickup=huf table of ref. where we pick code.
		// 1*s: table of bit to select coding foreach symbol.
		// second coding when =1 or first when=0, if undef, it is 0 option.
		// both coding are concatenated by adding 0 on first and a 1 on second at the
		// begining.
		// after a repack function is called to optimize the
		// tree.(leaf(0)(x(0),null(1)->x(0))
		// size(292)=326
		// size(19)=53

		// 3 // predefined table -2048..0 : id[12]
		// a table cost 400 octects, used a predefined table save memory.
		//
		// 0 CS9

		// -2048 CS32
		// -2047 CS16

		// -1 symbol292 type
		// -1 txt eng;fr;spanish.......
		// -2 csv
		// -3 java
		// -4 c++
		// -5 html
		// -6 pat
		// -7 class
		// -8 hex
		// ..
		// .... TBD in the future
		// size=12.

		/*
		 * 
		 * encode huff symbol - HUFF+bitstream code,symbol=256*9+256*~9=4608b
		 * table(count++) - HUFFUSE+INTn - HUFFDEL+INTn+bzip coding:8*32+15*bitstream
		 * code,symbol=275*.....=~500b ; code delta froml n - HUFFSWAP+INTn+.... : n
		 * reference table, SWAP define the swap of coding. ....: Intn+i+j+k+l.... : n
		 * is the number of swap, i j k l is the symbol that take the 1srt
		 * place(smallest) in huffman table.
		 * 
		 */
	}

	public void clearfreq() {
		freq = null;

	}
/** merge several huffman object into one.
 * */
	public static HuffmanCode MergeCode(List<HuffmanCode> lc) {
		int[] rfreq = new int[Symbol.getNbSymbol()];
		for(int i=0;i<Symbol.getNbSymbol();i++)
			rfreq[i]=0;
		for(HuffmanCode h:lc)
			if (h!=null)
			for(int i=0;i<Symbol.getNbSymbol();i++)
				rfreq[i]+=h.freq[i];	
		return Factory(rfreq);
	}
	
	ISymbol sprout=new Symbol();
@Override
public void setSprout(ISymbol sprout) {
	this.sprout=sprout;
	
}

public Long length() {
	BinaryFinFout bin=new BinaryFinFout();
	bin.write(this);
	return bin.getposOut();
}

}
