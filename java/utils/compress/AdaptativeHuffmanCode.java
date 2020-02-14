package com.zoubworld.java.utils.compress;

import com.zoubworld.java.utils.compress.file.IBinaryReader;
import com.zoubworld.java.utils.compress.file.IBinaryWriter;



public class AdaptativeHuffmanCode implements ICodingRule {
	private HuffmanNode root;
	private HuffmanNode NYT; // Not Yet Transferred
	private HuffmanNode table[]; // fast look up for leaves
	private HuffmanNode list[]; // fast look up for all nodes
	private int listTop; // top of the list
	static final int N=Symbol.getNbSymbol();
	/* default constructor */
	public AdaptativeHuffmanCode() {
		table = new HuffmanNode[N];
		listTop = N*2+1;
		list = new HuffmanNode[listTop];
		
		list[--listTop] = root = NYT = new HuffmanNode((Symbol)null, 0, listTop,(HuffmanNode) null,(HuffmanNode) null,(HuffmanNode) null);
	} /* end of default constructor */

	@Override
	public ICode get(ISymbol sym) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISymbol get(ICode code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICode getCode(IBinaryReader binaryStdIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICode getGenericCode(IBinaryReader binaryStdIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISymbol getSymbol(IBinaryReader binaryStdIn) {

		return get(getCode( binaryStdIn));
	}

	@Override
	public void writeCodingRule(IBinaryWriter binaryStdOut) {
		// TODO Auto-generated method stub
		
	}
	

	/* this function inserts or increases weight of given value */
	private void insert(ISymbol val) {

		HuffmanNode t = table[(int)val.getId()];
		if (table[(int)val.getId()] == null) {// new value
			HuffmanNode temp = NYT;
			HuffmanNode retVal = new HuffmanNode(val, 1, NYT.VitterIndex - 1, null, null, temp);
			list[--listTop] = retVal;
			NYT = new HuffmanNode(null, 0, NYT.VitterIndex - 2, null, null, temp);
			list[--listTop] = NYT;
			temp.left = NYT;
			temp.right = retVal;
			temp.freq++;
			table[(int)val.getId()] = retVal;
			if (table[(int)val.getId()].parent == root) {
				return;
			}
			t = table[(int)val.getId()].parent.parent;
		}

		while (t != root) { // stops at the root
			HuffmanNode temp = t;

			int i = t.VitterIndex + 1; // +1 passes its self
			for (; (list[i].freq == t.freq) && (i < 512 /* 513 - 1 passes root*/); i++);
			i--;

			if ((list[i].VitterIndex > temp.VitterIndex) && (list[i] != t.parent)) {
				temp = list[i];
				HuffmanNode temp2 = list[temp.VitterIndex];
				list[temp.VitterIndex] = list[t.VitterIndex];
				list[t.VitterIndex] = temp2;
				if (t.parent.left == t) t.parent.left = temp;
				 else t.parent.right = temp;
				if (temp.parent.left == temp) temp.parent.left = t;
				 else temp.parent.right = t;
				temp2 = temp.parent;
				temp.parent = t.parent;
				t.parent = temp2;
				int VitterIndex = t.VitterIndex;
				t.VitterIndex = temp.VitterIndex;
				temp.VitterIndex = VitterIndex;
			}
			t.freq++;
			t = t.parent;
		}
		t.freq++;
	} /* end of private void insert(int val) */
	Icode writecode(ISymbol sym)
	{
		int intRead=(int)sym.getId();
		
			HuffmanNode temp;
			if (isExistingSymbol(sym)) { //isExistingSymbol existing symbol
				
				//write code in stdout
				temp = table[intRead];
				return temp.ch.getCode();			
				
				
			} else { // new symbol
				// write NYT
				temp = NYT;
			
				return temp.ch.getCode()+sym.cs9;	
				
			}
			insert(intRead);
		
	}
	private boolean isExistingSymbol(ISymbol sym)
	{
		return (table[(int)sym.getId()] != null);
			}
}