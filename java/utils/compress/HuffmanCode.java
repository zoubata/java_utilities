package com.zoubworld.java.utils.compress;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zoubworld.java.utils.compress.file.BinaryStdIn;
import com.zoubworld.java.utils.compress.file.BinaryStdOut;
import com.zoubworld.utils.JavaUtils;

import edu.princeton.cs.algs4.MinPQ;

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
	public BinaryStdOut binaryStdOut = new BinaryStdOut();
	public BinaryStdIn binaryStdIn = new BinaryStdIn();
	static List<HuffmanCode> tables;
	static public boolean add(HuffmanCode e) {
		return getTables().add(e);
	}

	public static List<HuffmanCode> getTables() {
		if (tables==null)
			tables=new ArrayList<HuffmanCode> ();
		return tables;
	}

	// Do not instantiate.
	public HuffmanCode() {
		R = 256 + Symbol.special.length;
		Nb = (int) (Math.log(R) / Math.log(2) + 1);
		HuffmanCode.add(this);
	}
	HuffmanNode root=null;
	
	public HuffmanCode(BinaryStdIn binaryStdIn2) {
		R = 256 + Symbol.special.length;
		Nb = (int) (Math.log(R) / Math.log(2) + 1);
		root = readTrie(binaryStdIn2);
		codingrule=null;
		binaryStdIn=binaryStdIn2;
		//buildCode(/* String[] st, */ root, "");
		HuffmanCode.add(this);
		}
	
	
	
	// Huffman trie node
	public static class HuffmanNode implements Comparable<HuffmanNode> {
		private final ISymbol ch;
		private final long freq;
		private final HuffmanNode left, right;
		private  HuffmanNode parent;
		
		public String toString()
		{
			return "Node("+ch+","+freq+")";
		}
		public String toSym()
		{
			String s="";
			String c1="";
			String c2="";
			if(left!=null)
				c1=left.toSym();
			if(right!=null)
				c2=right.toSym();
			if (ch!=null)
				s=" "+ch.toString()+"";
			else
				s="|";
			String cx=merge(c1,c2);
			String[] sx = cx.split("\n");
			int len=0;
			if (sx.length>0)
				len=	sx[0].length();
			while (s.length()<len)
				if(s.length()%2==0)
					s+=" ";
				else
					s=" "+s;
			s+="\n";
			s+=cx;
			return s;
		}
		public String toFreq()
		{
 			String s="";
			String c1=" \n";
			String c2=" \n";
			if(left!=null)
				c1=left.toFreq();
			if(right!=null)
				c2=right.toFreq();
			if (ch!=null)
				s=String.format(" %1d",freq);
			else
				s="|";
			String cx=merge(c1,c2);
			String[] sx = cx.split("\n");
			int len=0;
			if(sx.length>0)
			len=sx[0].length();
			while (s.length()<len)
				if(s.length()%2==0)
					s+=" ";
				else
					s=" "+s;
			s+="\n";
			s+=cx;
			return s;
		}
		
		private String merge(String c1, String c2) {
			String s="";
			String[] s1 = c1.split("\n");
			String[] s2 = c2.split("\n");
			//adjust line number
			int d1=s1.length;
			int d2=s2.length;
			int d=Math.max(d1, d2);
			while (c1.split("\n").length<d)
				c1+=" \n";
			while (c2.split("\n").length<d)
				c2+=" \n";
			s1 = c1.split("\n");
			s2 = c2.split("\n");
			// adjust with of line
			int l1=s1[0].length();
			int l2=s2[0].length();
			for(int i=0;i<d1;i++)
				l1=Math.max(l1, s1[i].length());
			for(int i=0;i<d2;i++)
				l2=Math.max(l2, s2[i].length());
			int l=Math.max(l1, l2);
			for(int i=0;i<d1;i++)
				while (s1[i].length()<l1)
				s1[i]+=" ";
			for(int i=0;i<d2;i++)
				while (s2[i].length()<l2)
				s2[i]+=" ";
			for(int i=0;i<d1;i++)
				while (s1[i].length()<l)
				if(s1[i].length()%2==0)
					s1[i]+=" ";
				else
					s1[i]=" "+s1[i];
			
			for(int i=0;i<d2;i++)
				while (s2[i].length()<l)
				if(s2[i].length()%2==0)
					s2[i]+=" ";
				else
					s2[i]=" "+s2[i];
			//merge
					for (int i=0;i<d;i++)
					{
						s+=s1[i]+s2[i]+"\n";
					}			
			return s;
		}
		/**
		 * @return the parent
		 */
		public HuffmanNode getParent() {
			return parent;
		}
		/**
		 * @param parent the parent to set
		 */
		public void setParent(HuffmanNode parent) {
			this.parent = parent;
		}
		HuffmanNode(HuffmanNode parent,int ch, long freq, HuffmanNode left, HuffmanNode right) {
			this.ch = Symbol.findId( ch);
			this.freq = freq;
			this.left = left;
			this.right = right;
			if(right!=null)
				right.setParent(this);
			if(left!=null)
				left.setParent(this);
			
		}
		HuffmanNode(HuffmanNode parent,ISymbol sym, long freq, HuffmanNode left, HuffmanNode right) {
			this.ch = sym;
			this.freq = freq;
			this.left = left;
			this.right = right;
			if(right!=null)
				right.setParent(this);
			if(left!=null)
				left.setParent(this);
		}

		// is the node a leaf node?
		private boolean isLeaf() {
			assert ((left == null) && (right == null)) || ((left != null) && (right != null));
			return (left == null) && (right == null);
		}

		// compare, based on frequency
		public int compareTo(HuffmanNode that) {
			return (int)(this.freq - that.freq);
		}
		
	}
	/** estimate the octet size of a file based on the frequency map after an huffman compression
	 * */
	public Long getSize(Map<ISymbol, Long> freq)
	{
		return getBitSize( freq)/8+1;
	}
	/** estimate the bit size of a file based on the frequency map after an huffman compression
	 * */
	public Long getBitSize(Map<ISymbol, Long> freq)
	{
		Long l=0L;
		int nb=(int)(Math.log10(freq.keySet().size())/Math.log10(2));
		nb++;
		root=getRoot( freq);
		for (ISymbol key:freq.keySet())
			l+=key.getCode().length()*freq.get(key);
		for (ISymbol key:freq.keySet())
			l+=key.getCode().length()+nb;
/*		for (ISymbol key:freq.keySet())
		System.out.println("'"+key+"' ->"+key.getCode().length()+" : "+key.getCode().toRaw()+" "+freq.get(key));
*/		
		return l;
	}
	public HuffmanNode getRoot(Map<ISymbol, Long> freq)
	{
		
		root = buildTrie(freq);
		codingrule=null;
		// 2�896 used tab 85 symbol =112o
		// 3�145 full tab 265 symbol=361
		// 2�784 no tab

		// print trie for decoder
	//	WriteTable(root,binaryStdOut);

		// build code table
		/* st = new String[R]; */
		buildCode(/* st, */root, "");
		return root;
	}
	
	int[] freq=null;

	/* String[] st ; */
	/**
	 * Reads a sequence of 8-bit bytes from standard input; compresses them using
	 * Huffman codes with an 8-bit alphabet; and writes the results to standard
	 * output.
	 * * binstream=table+size+list<code>
	 */ 
	public void compress() {
		// read the input
		String s = binaryStdIn.readString();
		char[] input = s.toCharArray();

		// tabulate frequency counts
		freq = new int[R];
		/*
		 * for (int i = 0; i < R; i++) freq[i]++;//stop dirrentes from 0, to keep all
		 * symbol
		 */
		for (int i = 0; i < input.length; i++)
			freq[input[i]]++;
	//	printFreqs();
		// build Huffman trie
		root = buildTrie(freq);
		codingrule=null;
		// 2�896 used tab 85 symbol =112o
		// 3�145 full tab 265 symbol=361
		// 2�784 no tab

		// print trie for decoder
		WriteTable(root,binaryStdOut);

		// build code table
		/* st = new String[R]; */
		buildCode(/* st, */root, "");
	//	printCodes();
		/*
		 * for(int i=0;i<R;i++) if (freq[i]>0) System.out.println((((i>31) &&
		 * (i<127))?("'" + (char)i + "'  "):(String.format("0x%2x ", i))) +
		 * " : "+String.format( "%2.3f", freq[i]* 100.0 / (input.length ))+"% : " +
		 * Symbol.findId(i).getCode().toString());
		 */

		// print number of bytes in original uncompressed message
		binaryStdOut.write(input.length);

		// use Huffman code to encode input
		for (int i = 0; i < input.length; i++) {
			ISymbol sym = Symbol.findId(input[i]);
			binaryStdOut.write(sym.getCode().getLong(), sym.getCode().length());
		}
		// close output stream
		binaryStdOut.close();
	}

	// build the Huffman trie given frequencies
	static public HuffmanNode buildTrie(Map<ISymbol, Long> freq) {

			// initialze priority queue with singleton trees
			MinPQ<HuffmanNode> pq = new MinPQ<HuffmanNode>();
			for (ISymbol key:freq.keySet())
				if (freq.get(key) > 0)
					pq.insert(new HuffmanNode(null,key, freq.get(key), null, null));

			// special case in case there is only one character with a nonzero frequency
			if (pq.size() == 1) {
				ISymbol sym=freq.keySet().iterator().next();
				if (freq.get(sym) == 0)
					pq.insert(new HuffmanNode(null,'\0', 0, null, null));
				else
					pq.insert(new HuffmanNode(null,'\1', 0, null, null));
			}

			// merge two smallest trees
			while (pq.size() > 1) {
				HuffmanNode left = pq.delMin();
				HuffmanNode right = pq.delMin();
				HuffmanNode parent = new HuffmanNode(null,'\0', left.freq + right.freq, left, right);
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
				pq.insert(new HuffmanNode(null,i, freq[i], null, null));

		// special case in case there is only one character with a nonzero frequency
		if (pq.size() == 1) {
			if (freq['\0'] == 0)
				pq.insert(new HuffmanNode(null,'\0', 0, null, null));
			else
				pq.insert(new HuffmanNode(null,'\1', 0, null, null));
		}

		// merge two smallest trees
		while (pq.size() > 1) {
			HuffmanNode left = pq.delMin();
			HuffmanNode right = pq.delMin();
			HuffmanNode parent = new HuffmanNode(null,'\0', left.freq + right.freq, left, right);
			pq.insert(parent);
		}
		return pq.delMin();
	}

	public void printCodes() {
		System.out.println(codesToString());
	}
	public String toString()
	{
		return "HuffManCode("+codesToString()+")";
		
	}
	private String codesToString() {
		String s = ("--- Printing Codes ---\n");
		if(freq==null && root==null)
		for (int i = 0; i < R; i++)
			s += (((i > 31) && (i < 127)) ? ("'" + (char) i + "'\t") : (String.format("0x%2x\t", i))) + ": "
					+ Symbol.findId(i).getCode().toString() + "\n";
		else
			if(freq!=null)	
			for(int i=0;i<freq.length;i++)
				s += (((i > 31) && (i < 127)) ? ("'" + (char) i + "'\t") : (String.format("0x%2x\t", i))) + ": "
						+ get(Symbol.findId(i)).toString() + "\n";
			else
			{
				List<HuffmanNode> ls = getAllLeaf(root);
				for(HuffmanNode l:ls)
					{
					long i=0;
					if (l.ch!=null)
					i=l.ch.getId();
					else
						{
						System.err.println("one symbol is null of the tree");
						}
					s += (((i > 31) && (i < 127)) ? ("'" + (char) i + "'\t") : (String.format("0x%2x\t", i))) + ": "
							+ get(Symbol.findId((int)i)) + "\n";
			}}
		return s;
		
	}
	public String codesToString(Map<ISymbol, Long> mfreqorig) {
		Map<ISymbol, Long> mfreq=JavaUtils.SortMapByValue(mfreqorig);
		String s = ("--- Printing Codes ---\n");
		for (ISymbol sym:mfreq.keySet())
			s += (((sym.getId() > 31) && (sym.getId() < 127)) ? ("'" + (char) sym.getId() + "'\t") : ((sym.getId() < 256)?(String.format("\\x%x ", sym.getId() )):sym.toString())) + ":\t"
					+ mfreq.get(sym)+"\t:\t"
					+ sym.getCode().toString() + "\n";
		return s;
	}

	public void printFreqs() {
		System.out.println(toFreqString());
	}

	public String toFreqString() {
		int length = 0;
		String s = "";
		if(freq!=null)
		{
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
	//	printFreqs();

		// build Huffman trie
		root = buildTrie(freq);
		codingrule=null;
	
		return root;
	}
	/** build the coding set form a symbol list
	 * */
	static public HuffmanCode buildCode(List<ISymbol> ldec)
	{
		Map<ISymbol, Long> f = Symbol.Freq(ldec);
		return buildCode(f);
	}
	/** build the coding set form a frequency table
	 * */
	static public ICodingRule buildCode(int[]  freq)
	{
		return buildCode(buildTrie(freq));
	}
	/** build the coding set form a frequency table
	 * */
	static public HuffmanCode buildCode(Map<ISymbol, Long> freq)
	{
		HuffmanNode n = buildTrie(freq);
		return buildCode(n);
	}
	
	/** build the coding set for the Huffman tree
	 * */
	static public HuffmanCode buildCode(HuffmanNode root)
	{
		HuffmanCode cs=new HuffmanCode();
		cs.buildCode(root, "");
		cs.root=root;
		/*
		CodingSet cs=new CodingSet(null);
		ISymbol sym = root.ch;
		
		for(HuffmanNode lf:getAllLeaf( root))
		{
			sym = lf.ch;
			ICode code = toCode(lf);
		cs.put(sym, code);
		}*/
		return cs;	
		
		
	}
	private static List<HuffmanNode> getAllLeaf(HuffmanNode root)
	{
		List<HuffmanNode> ls=null;
		if(!root.isLeaf())
		{
			 ls=getAllLeaf(root.left);
			 ls.addAll(getAllLeaf(root.right));
		}
		else
		{ ls=new ArrayList<HuffmanNode>();
			ls.add(root);
			}
		return ls;
	}
	private static ICode toCode(HuffmanNode node) {
		
		ICode c;
		if(node.getParent()==null)
			c=new Code();
			
		else
		{			
			c=toCode(node.getParent());
			if (node.getParent().left==node)
				c.huffmanAddBit('0');
			else
			if (node.getParent().right==node)
				c.huffmanAddBit('1');
		}	/*if(!node.isLeaf())
			return null;	*/
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

	/** update currents symbol to use the code of this huffman tree/table
	 * */
	public void buildCode() {
		if (getRoot()==null)
			Symbol.initCode();//default coding.
		else
		buildCode(/* st, */ getRoot(), "");//huffman coding.
		codingrule=null;

	}


static public  int[] getFreq(List<ISymbol> ldec) {
	int[] Myfreq=new int[R];
	for (int i = 0; i < ldec.size(); i++) {
		ISymbol sym = ldec.get(i);
		int id = (int) sym.getId();
		Myfreq[id]++;
	}
		return Myfreq;
}

/**
 * binout =Symbol.huffman + trie + list<code> + EOBS
 * */
	public void encodeSymbol(List<ISymbol> ldec, BinaryStdOut binaryStdOut) {

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
	 * binout =Symbol.huffman + trie + list<code> + EOBS
	 * but trie isn't encoded
	 * */
		public void storeSymbol(List<ISymbol> ldec, BinaryStdOut binaryStdOut) {

		//	root = encodeSymbol(ldec);
			//Code.reworkCode(ldec, Code.getDefaultLength());
			binaryStdOut.setCodingRule(new CodingSet(CodingSet.NOCOMPRESS));
			writeTrie(root, binaryStdOut);
			// build code table

			// print number of bytes in original uncompressed message
			// binaryStdOut.write(ldec.size());
			//WriteSymbol(ldec, binaryStdOut);
			binaryStdOut.writes(ldec);
			binaryStdOut.write(Symbol.EOBS);
			
		
			// return lenc;
		}
	/**
	 * * binout =huffman  trie
  */
	static public void WriteTable(HuffmanNode x, BinaryStdOut binaryStdOut2) {

		if (x.isLeaf()) {
			binaryStdOut2.write(true);
			/*if (x.ch.isChar())
				binaryStdOut2.write(x.ch);
			else*/
				binaryStdOut2.write(x.ch);
			return;
		}
		binaryStdOut2.write(false);
		WriteTable(x.left,binaryStdOut2);
		WriteTable(x.right,binaryStdOut2);
	}
	// deprecated
	public void WriteTable( ICode code)
	{
		HuffmanCode.WriteTable(this.root,  code);
	}
	// deprecated
	static public void WriteTable(HuffmanNode x, ICode code) {
		if (x.isLeaf()) {
			code.huffmanAddBit('1');
			if (x.ch.isChar())
				for(int i=0;i<Nb;i++)
					code.huffmanAddBit((char)(((x.ch.getId()>>i)&1)+'0'));
			return;
		}
		code.huffmanAddBit('0');
		WriteTable(x.left,code);
		WriteTable(x.right,code);
	}
		/**
	* binout = list<codehuffman>
	*/	 
	public void WriteSymbol(List<ISymbol> ldec, BinaryStdOut binaryStdOut) {

		buildCode(/* st, */ root, "");

//		printCodes();

		// print number of bytes in original uncompressed message

		// use Huffman code to encode input
		for (int i = 0; i < ldec.size(); i++) {
			;

			//binaryStdOut.write(ldec.get(i).getCode().getLong(), ldec.get(i).getCode().length());
			binaryStdOut.write(ldec.get(i));

		}

		// close output stream
		// binaryStdOut.close();

		// return lenc;
	}

	public List<ISymbol> decodeSymbol(BinaryStdIn binaryStdIn2) {
		List<ISymbol> ldec = new ArrayList<ISymbol>();
/*
read Symbol.HUFFMAN?
		// read in Huffman trie from input stream
		root = readTrie(binaryStdIn2);
		buildCode( root, "");
		*/
	//	printCodes();
		// number of bytes to write
		// decode using the Huffman trie
		ISymbol sym = null;
		while(sym!=Symbol.EOBS) {
				sym = decodeASymbol(binaryStdIn2);
				if (sym==Symbol.HUFFMAN)
				{
//					???build apply...
				}
				ldec.add(sym);			
		}
		return ldec;
	}
	
	/**
	 * supposed : trie build and code also
	 * */
	public ISymbol decodeASymbol(BinaryStdIn binaryStdIn2) {
		
		ICode c = getGenericCode(binaryStdIn2);
		if(c==null) return null;
		 ISymbol sym = c.getSymbol();
		 
			if (sym.getId() > 256)// complex symbol
			{
				 sym = Symbol.decode(sym, binaryStdIn2);
				
			} /*else
				sym=sym;*/
			
		
		return sym;
	}

	/**
	 * Reads a sequence of bits that represents a Huffman-compressed message from
	 * standard input; expands them; and writes the results to standard output.
	 * binstream=table+size+list<code>
	 */
	public void expand() {
		// read in Huffman trie from input stream
		HuffmanNode root = readTrie(binaryStdIn);
	//	printCodes();
		// number of bytes to write
		int length = binaryStdIn.readInt();
		// decode using the Huffman trie
		for (int i = 0; i < length; i++) {
			HuffmanNode x = root;
			while (!x.isLeaf()) {
				boolean bit = binaryStdIn.readBoolean();
				if (bit)
					x = x.right;
				else
					x = x.left;
			}
			binaryStdOut.write(x.ch.getChar(), 8);// x.ch
		}
		binaryStdOut.close();
	}
/** read the Huffman tree based on coding rules of binaryStdIn
 * */
	HuffmanNode readTrie(BinaryStdIn binaryStdIn2) {
		Boolean isLeaf = binaryStdIn2.readBoolean();
		if(isLeaf==null)
			return null;
		if (isLeaf) {
			ICode c = binaryStdIn2.getCodingRule().getGenericCode(binaryStdIn2);
			return new HuffmanNode(null,c.getSymbol(), -1, null, null);
		} else {
			return new HuffmanNode(null,null, -1, readTrie(binaryStdIn2), readTrie(binaryStdIn2));
		}
	}
/**
 * * binout =Symbol.huffman + trie
 */
	private void writeTrie(HuffmanNode x,BinaryStdOut  binaryStdOut2) {
		binaryStdOut2.write(Symbol.HUFFMAN);
		
		WriteTable( x,  binaryStdOut2);
	}

	/**
	 * Sample client that calls {@code compress()} if the command-line argument is
	 * "-" an {@code expand()} if it is "+".
	 *
	 * @param args
	 *            the command-line arguments
	 */
	public static void main(String[] args) {
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
			huff.binaryStdIn = new BinaryStdIn(new File(filetxt));
			huff.binaryStdOut = new BinaryStdOut(new File(filetxt + ".huff"));
			// huff.compress();
			List<ISymbol> ls = Symbol.factoryFile(filetxt);
			huff.encodeSymbol(ls, huff.binaryStdOut);
			// close output stream
			huff.binaryStdOut.close();

			huff.analyse(ls);
		}
		{
			String filetxt = "C:\\Temp\\zip-test\\QPQRN-19.pbs";
			System.out.println(filetxt);
			huff.binaryStdIn = new BinaryStdIn(new File(filetxt));
			huff.binaryStdOut = new BinaryStdOut(new File(filetxt + ".huff"));
			// huff.compress();
			List<ISymbol> ls = Symbol.factoryFile(filetxt);
			huff.encodeSymbol(ls, huff.binaryStdOut);
			huff.binaryStdOut.close();
			huff.analyse(ls);
		}
		{
			String filetxt = "C:\\Temp\\zip-test\\cycling_eedata_1k_cold.lss";
			System.out.println(filetxt);
			huff.binaryStdIn = new BinaryStdIn(new File(filetxt));
			huff.binaryStdOut = new BinaryStdOut(new File(filetxt + ".huff"));
			// huff.compress();
			List<ISymbol> ls = Symbol.factoryFile(filetxt);
			huff.encodeSymbol(ls, huff.binaryStdOut);
			huff.binaryStdOut.close();
			huff.analyse(ls);
		}
		{
			String filetxt = "C:\\Temp\\zip-test\\cycling_eedata_1k_cold.hex";
			System.out.println(filetxt);
			huff.binaryStdIn = new BinaryStdIn(new File(filetxt));
			huff.binaryStdOut = new BinaryStdOut(new File(filetxt + ".huff"));
			// huff.compress();
			List<ISymbol> ls = Symbol.factoryFile(filetxt);
			huff.encodeSymbol(ls, huff.binaryStdOut);
			huff.binaryStdOut.close();
			huff.analyse(ls);
		}

	}
	ICodingRule codingrule;
	/** to speed up the translation use internal cache(CodingSet)
	 * @return the codingrule
	 */
	private ICodingRule getCodingRule() {
		if(codingrule==null)
			{
			
			CodingSet cs=new CodingSet(null);
			ISymbol sym = root.ch;
			
			for(HuffmanNode lf:getAllLeaf( root))
			{
				sym = lf.ch;
				ICode code = toCode(lf);
			cs.put(sym, code);
			}
			codingrule=cs;
			}
		return codingrule;
	}

	/**
	 * @return the root
	 */
	public HuffmanNode getRoot() {
		if(root==null)
			if(freq!=null)
			{root=buildTrie(freq);	codingrule=null;}	
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
	public ICode getCode(BinaryStdIn binaryStdIn) {

		ISymbol s = decodeASymbol( binaryStdIn);
		return get(s);
	}
	@Override
	public ICode getGenericCode(BinaryStdIn binaryStdIn2) {
		ISymbol sym = null;
		if (root==null)//use default coding sym=code (size=Nb)
			{
				int c=binaryStdIn2.readInt(Nb);
				sym= Symbol.findId(c);
			}
		else
		{
		HuffmanNode x = root;
			while (!x.isLeaf()) {
				Boolean bit = binaryStdIn2.readBoolean();
				if(bit==null)
					return null;
				if (bit)
					x = x.right;
				else
					x = x.left;
			}
			sym=x.ch;			
		}
	
		return sym.getCode();
	}
	@Override
	public ISymbol getSymbol(BinaryStdIn binaryStdIn) {
		
		return decodeASymbol( binaryStdIn);
	}

	@Override
	public void writeCodingRule(BinaryStdOut binaryStdOut) {
		writeTrie(getRoot(),binaryStdOut);		
	}



	

	

}
