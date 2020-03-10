package com.zoubworld.java.utils.compress;

// Huffman trie node
public class HuffmanNode implements Comparable<HuffmanNode> {
	 final ISymbol ch;
	 long freq;
	 HuffmanNode left, right;
	 HuffmanNode parent;
	int VitterIndex; //Vitter algorithm
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
		VitterIndex=-1;
		
	}
	/** used for adaptative huffman tree
	 * */
	public HuffmanNode(ISymbol sym, int freq, int VitterIndex, HuffmanNode left, HuffmanNode right,HuffmanNode parent) {
		this.ch = sym;
		this.freq = freq;
		this.left = left;
		this.right = right;
		this.parent=parent;
		this.VitterIndex=VitterIndex;
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
		VitterIndex=-1;
	}


	// is the node a leaf node?
	public boolean isLeaf() {
		assert ((left == null) && (right == null)) || ((left != null) && (right != null));
		return (left == null) && (right == null);
	}

	// compare, based on frequency
	public int compareTo(HuffmanNode that) {
		return (int)(this.freq - that.freq);
	}
	
}