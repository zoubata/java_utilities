package com.zoubworld.java.utils.compress.SymbolComplex;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zoubworld.java.utils.compress.Code;
import com.zoubworld.java.utils.compress.CompositeSymbol;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.binalgo.HuffmanCode;
import com.zoubworld.java.utils.compress.file.IBinaryReader;
import com.zoubworld.utils.JavaUtils;

/**
 * @author zoubata
 *
 */
public class SymbolHuffman extends CompositeSymbol {
	HuffmanCode huff = null;
	static private List<HuffmanCode> table;

	/** return the best Huffcode coding available in history
	 * 
	 * 
	 * */
	public HuffmanCode findBest(List<ISymbol> ls)
	{

		Map<ISymbol, Long> m = Symbol.Freq(ls);
		return findBest( m);
	}
	/** return the best Huffcode coding available in history
	 * 
	 * 
	 * */
	public HuffmanCode findBest(Map<ISymbol, Long> m)
		{		
			HuffmanCode r=null;		
		for(HuffmanCode h:table)
			if (r==null)
				r=h;
			else
				if(Symbol.length(m, h)<Symbol.length(m, r))
					r=h;		
		return r;
	}
	
	/**
	 * @param mys1
	 * @param mys2
	 */
	private SymbolHuffman(ISymbol mys1, ISymbol mys2) {
		super(mys1, mys2);
	}

	public SymbolHuffman(ISymbol mys2) {
		super(Symbol.HUFFMAN, mys2);
	}
	/** generate a Huffman symbol this the data of Huffman table i
	 * */
	public SymbolHuffman(int i) {
		super(Symbol.HUFFMAN, new Symbol(i));
		Code c = new Code();
		huff = getTables().get(i);
		huff.WriteTable(c);
		getS1().setCode(c);
	}
	/** generate a Huffman symbol this the data of Huffman table i
	 * */
	public SymbolHuffman(HuffmanCode h ) {
		super(Symbol.HUFFMAN, new Symbol(getTables().size()));
		Code c = new Code();
		 huff = h;
		huff.WriteTable(c);
		getS1().setCode(c);
		getTables().add(huff);
	}

	public static void main(String[] args) {
		String r="";int count=0;
		String dir="C:\\home_user\\pvalleau\\cvs_home\\Jules\\Testprogram\\selftest\\verification\\";
		Set<String> l = JavaUtils.listFileNames(dir,
				"", false, true,true);
		Map<String , List<String>> m=new HashMap<String, List<String>>();
		for(String f:l)
		{
			String ext;
			List<String> lf=m.get(ext=JavaUtils.ExtensionOfPath(f));
			if (lf==null)				
			m.put(ext, lf=new ArrayList<String>());
			lf.add(f);
		}
		for(String k:m.keySet())
		{
			List<HuffmanCode> lc=new ArrayList<HuffmanCode>();
			for(String f:m.get(k))
				{
		HuffmanCode n=HuffmanCode.Factory(Symbol.from(new File(dir+f)));
		String b = ">>"+f+"\r\n";
		if(n!=null)
		b+=n.ToFilecodes();
		
		lc.add(n);
		System.out.println(b);
			}
			HuffmanCode n=HuffmanCode.MergeCode(lc);
			String b = ">>N="+count+"; ext="+k+"\r\n";count++;
			if(n!=null)
			b+=n.ToFilecodes();
		
			System.out.println(b);
			r+=b;
		}
		System.out.println(r);
	}
	private static List<HuffmanCode> getTables() {
		if (table==null)
			{
			table=new ArrayList<HuffmanCode>();
			}
		
		return table;
	}
	
	/** read an huffman symbol and the table of codes
	 **/
	public SymbolHuffman(IBinaryReader binaryStdIn) {
		super(Symbol.HUFFMAN, new Symbol(getTables().size()));
		huff = new HuffmanCode(binaryStdIn);
		Code c = new Code();
		huff.WriteTable(c);
		getS1().setCode(c);
		getTables().add(huff);//save this new table.
	}
	
	/** read an huffman symbol reference
	 **/
	public SymbolHuffman(Symbol s,IBinaryReader binaryStdIn) {
		super(s, binaryStdIn.readSymbol());
		assert Symbol.getINTn(getS1())!=null;
	}
}
