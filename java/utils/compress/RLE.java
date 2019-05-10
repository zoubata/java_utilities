/**
 * 
 */
package com.zoubworld.java.utils.compress;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.zoubworld.java.utils.compress.file.BinaryStdOut;
import com.zoubworld.utils.JavaUtils;

/**
 * @author Pierre Valleau
 * 
 * RLE+N+symbol
 *
 */
public class RLE {
//dev time 4H 28/7/2018
	//File=list(code)=> list(sym)=>list(sym)....=>list(code)

	/**
	 * 
	 */
	public RLE() {
		// TODO Auto-generated constructor stub
	}
	/*
	public List<Symbol> decode(List<Code> lc)
	{
		return null;
		
		
	}*/
	
	/*
	public List<Code> encode(List<Symbol> ls)
	{
		//List<Code> 
		
		return null;
		
	}*/
	
	public List<ISymbol> decodeSymbol(List<ISymbol> lenc)
	{
		List<ISymbol> ldec=new ArrayList<ISymbol>();
		int state=0;//no RLE
		long N=1;
		ISymbol previous=null;
		for(ISymbol e:lenc)
		{	if(e==Symbol.RLE)
			{
				state=1;//RLE
				
			}
			else
				if (state==1)//N
				{
					state=2;
					
					N=((CompositeSymbol)e).getS2().getId();
				}
				
					else
						if (state==2)//Sym
						{
							state=0;
							for (long i=1;i<N;i++)
								ldec.add(previous);
							    ldec.add(e);
									N=1;
						}
						
							else
							ldec.add(e);
		if ((state!=2)&& (state!=1))
		previous=e;
		}
		return ldec;
	}
	/*
	tab[0..255][0..255] list<offset>
	65355 x

	
	dico1 256                  50% 
	dico2 65356                25%
	dico3 16777216             10%
	
	
	1 Mo range; dico 16Mo
	1 + 16*8+1.6*64=260Mo
	
	
	*/
	
	public List<ISymbol> encodeSymbol(List<ISymbol> ldec)
	{
		List<ISymbol> lenc=new ArrayList<ISymbol>();
		ISymbol previous=null;
		int count = 1;
		for(ISymbol e:ldec)
		{
			if (e==previous)
				count++;
			else
				if (count>1)
				{
					lenc.add(Symbol.RLE);
				//	lenc.add(new Symbol(count));
					lenc.add(Symbol.FactorySymbolINT(count));
				 	lenc.add(e);// new symbol
				 
					count =1;
				}
				else
			lenc.add(e);
			previous=e;
		}
		return lenc;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	
		RLE cmp= new RLE();
		HuffmanCode huff=new HuffmanCode();	
		
		
	{	
	String filetxt="C:\\\\Temp\\\\zip-test\\\\661P2H20_05_QNSJ8J_01_CP2_20170411T160536.csv";System.out.println(filetxt);  	
	File file = new File(filetxt);	
	List<ISymbol> ls=Symbol.factoryFile( file.getAbsolutePath());
	List<ISymbol> lse=cmp.encodeSymbol(ls);	
	huff.encodeSymbol(lse,new BinaryStdOut(new File(filetxt+".rle")));
	huff.analyse(lse);
	}
	{	
	String filetxt="C:\\\\Temp\\\\zip-test\\\\QPQRN-19.pbs";System.out.println(filetxt);  	
	File file = new File(filetxt);	
	List<ISymbol> ls=Symbol.factoryFile( file.getAbsolutePath());
	List<ISymbol> lse=cmp.encodeSymbol(ls);	
	huff.encodeSymbol(lse,new BinaryStdOut(new File(filetxt+".rle")));
	huff.analyse(lse);
	}
	{	
	String filetxt="C:\\\\Temp\\\\zip-test\\\\cycling_eedata_1k_cold.lss";System.out.println(filetxt);  	
	File file = new File(filetxt);	
	List<ISymbol> ls=Symbol.factoryFile( file.getAbsolutePath());
	List<ISymbol> lse=cmp.encodeSymbol(ls);	
	huff.encodeSymbol(lse,new BinaryStdOut(new File(filetxt+".rle")));
	huff.analyse(lse);
	}
	{	
	String filetxt="C:\\\\Temp\\\\zip-test\\\\cycling_eedata_1k_cold.hex";System.out.println(filetxt);  	
	File file = new File(filetxt);	
	List<ISymbol> ls=Symbol.factoryFile( file.getAbsolutePath());
	List<ISymbol> lse=cmp.encodeSymbol(ls);	
	huff.encodeSymbol(lse,new BinaryStdOut(new File(filetxt+".rle")));
	huff.analyse(lse);
	}
	
	// Symbol.listSymbolToFile(lse,filec.getAbsolutePath(),32);	

	}
}

