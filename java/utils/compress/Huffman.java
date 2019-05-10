package com.zoubwolrd.java.utils.compress;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zoubworld.utils.JavaUtils;

import net.zoubwolrd.java.utils.compress.file.BinaryStdIn;
import net.zoubwolrd.java.utils.compress.file.BinaryStdOut;

import org.junit.runner.JUnitCore;

import org.junit.runner.notification.Failure;

public class Huffman {
	ISymbol charToSymbol[]=new ISymbol[256];
/*
	Map<Symbol, Code> tableEncode;
	Map<Code, Symbol> tableDecode;*/
	public Huffman() {
		// TODO Auto-generated constructor stub
	}
/*
	public Huffman(Map<Symbol, Long> table) {
		// TODO Auto-generated constructor stub
	}

	
	public ICode code(ISymbol s)
	{
		return s.getCode();
	}
	public ISymbol decode(ICode s)
	{
		return s.getSymbol();
	}
	
	public Map<Symbol, Long> getTableEntropie(File filein) {
		Map<Symbol, Long> table = new HashMap<Symbol, Long>();
		String s = JavaUtils.read(filein);
		for (int i = 0; i < s.length(); i++)
			if (Symbol.getFromSet(table.keySet(), s.charAt(i)) != null) {
				Symbol sym = (Symbol.getFromSet(table.keySet(), s.charAt(i)));
				table.put(sym, table.get(sym) + 1);

			} else
				table.put(new Symbol(s.charAt(i)), (long) 1);
		return table;

	}*/
/*
	public static void main(String[] args) {
		
		
		
	
		
			 
		{

/** basic copy
		int BUFFER_SIZE=1024*1024;
		
		String inputFile="C:\\home_user\\pvalleau\\cvs_home\\Beast\\Testprogram\\selftest\\ASF\\sam\\utils\\make\\Makefile.in";
		String outputFile="C:\\temp\\temp.in";
try {
	InputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
    OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
    byte[] buffer = new byte[BUFFER_SIZE];
 int size=0;
    while ((size=inputStream.read(buffer)) != -1) {
    	Symbol.ByteArrayToListSymbol(buffer, size).stream()
    	.map(s->s.getCode())
    	.forEach(c->{
			try {
				outputStream.write(c.code[0]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
 //   outputStream.write(buffer,0,size);
    }
 
} catch (IOException ex) {
        ex.printStackTrace();
}
*/
	/*
			        org.junit.runner.Result result = JUnitCore.runClasses(AllTests.class);
		
			        for (Failure failure : result.getFailures()) {
		
			            System.out.println(failure.toString());
		
			        }
		
			        System.out.println(result.wasSuccessful());
		
			    }
		
			 
		
			
			

		int BUFFER_SIZE=1024*1024;
		
		String inputFile="C:\\home_user\\pvalleau\\cvs_home\\Beast\\Testprogram\\selftest\\ASF\\sam\\utils\\make\\Makefile.in";
		String outputFile="C:\\temp\\temp.in";
try {
	InputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
    OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
    byte[] buffer = new byte[BUFFER_SIZE];
 int size=0;
 ISymbol RLE8=new Symbol("RLE");
 int count=0;
 ISymbol sprevious=null;
    while ((size=inputStream.read(buffer)) != -1) {
    	for( ISymbol s:Symbol.ByteArrayToListSymbol(buffer, size))
    	{
    		if (s==sprevious)
    			count++;
    		else
    		{
    			if (count==0)
    				outputStream.write(s.getCode().toCode()[0]);
    			else
    			{
    				
    			// if <8 < 2**16;<2**24
    				outputStream.write(RLE8.getCode().toCode()[0]);
    				outputStream.write((byte)count);
    				outputStream.write(sprevious.getCode().toCode()[0]);
    			}
    		}
    		sprevious=s;
 //   outputStream.write(buffer,0,size);
    }}}
  catch (IOException ex) {
        ex.printStackTrace();
}


//RLE 

		
		
		
		
		System.exit(0);
		Huffman hm = new Huffman();
		Map<Symbol, Long> table = hm.getTableEntropie(new File(
				"C:\\home_user\\pvalleau\\cvs_home\\Beast\\Testprogram\\selftest\\ASF\\sam\\utils\\make\\Makefile.in"));
	//	table.clear();
	/*	table.put(new Symbol(' '), (long) 10000);
		table.put(new Symbol('a'), (long) 1000);
		table.put(new Symbol('z'), (long) 1001);*/
		//  #pos,offset, size
		//dic,index
		table.put(new Symbol("#pos16_8"), (long) 101);//n/q8  n=sizefiles/ratio
		table.put(new Symbol("#pos32_16"), (long) 110);//n/q7  n=sizefiles/ratio
		table.put(new Symbol("#pos32_8"), (long) 100);//n/q6  n=sizefiles/ratio
		table.put(new Symbol("#pos24_8"), (long) 111);//n/q5  n=sizefiles/ratio
		table.put(new Symbol("#pos64_32"), (long) 110);//n/q4  n=sizefiles/ratio
		table.put(new Symbol("#pos64_8"), (long) 10);//n/q3  n=sizefiles/ratio
		table.put(new Symbol("#dic16"), (long) 300);//n/q0  n=sizefiles/ratio
		table.put(new Symbol("#dic24"), (long) 100);//n/q1  n=sizefiles/ratio
		table.put(new Symbol("#dic32"), (long) 1000);//n/q2  n=sizefiles/ratio
		table.put(new Symbol("#end"), (long) 10);// nb special
		table.put(new Symbol("#RLE8"), (long) 10);// repeate encoding  n
		table.put(new Symbol("#RLE16"), (long) 10);// repeate encoding n
		table.put(new Symbol("#RLC8"), (long) 10);// repeate char  n
		table.put(new Symbol("#RLC16"), (long) 10);// repeate char n
		table.put(new Symbol("#FileList"), (long) 1);//1
		table.put(new Symbol("#File"), (long) 101);//nb file
		table.put(new Symbol("#HuffmanTable"), (long) 101);//nb file
		//#HuffmanTable N,sym[N]; 
		//sym : S=0..255,W: (N/8+1),Code 8x(S/8+1)
		//RLE8 : n=0..255,a symbol #pos/#dic
		//RLE16 : n=0..65535,a symbol #pos/#dic
		//RLC8 : n=0..255
		//RLC16 : n=0..65535
				
		hm.BuildCode(table);
	}

	
	
	interface MyHuffmanbuilder {
		 void buildHuffman(List<Symbol> nblist);
	}
	public void BuildCode(Map<Symbol, Long> table) {

		class SymbolCountComparator implements Comparator<Symbol> {
			public int compare(Symbol s1, Symbol s2) {
				return -table.get(s1).compareTo(table.get(s2));

			}}
		
		// Map<Long, List<Symbol>> tabler=new HashMap<Long,List<Symbol>>();
		List<Symbol> nblist = new ArrayList<Symbol>();
		nblist.addAll(table.keySet());
		Collections.sort(nblist, new SymbolCountComparator());
		// link sym ->code
		for(ISymbol n:nblist)
			n.setCode(new Code());
		MyHuffmanbuilder func = new MyHuffmanbuilder() {
				
			 public void buildHuffman(List<Symbol> nblist) {
					if (nblist.size()<=1)
						return;
					long total = 0;
					for (ISymbol n : nblist) {
						// System.out.println(n.toString()+" : "+table.get(n).toString());
						total += table.get(n);
					}
					List<Symbol> nblist0=new ArrayList();
					List<Symbol> nblist1=new ArrayList();
					long total2 = 0;
					char code = '0';
					for (Symbol n : nblist) {
											
						if ((code == '0') && (total2 > (total / 2)))
						{	
												
							code = '1';						
						//	System.out.println(n.toString() + "= " + ((n.getCode()==null)?"?":n.getCode().toString()) + ": " + table.get(n).toString());
						}		
						else
						{						
						 //   System.out.println(n.toString() + "= " + ((n.getCode()==null)?"?":n.getCode().toString()) + ": " + table.get(n).toString());
						}
						if(code == '1')
							nblist1.add(n);
						else	
						{					
						nblist0.add(n);
						total2 += table.get(n);	
						}
					}
					if(nblist1.size()==0)
					
						nblist1.add(nblist0.remove(0));
					  
					if(nblist0.size()==0)
						nblist0.add(nblist1.remove(0));
					
		
					for (ISymbol n : nblist0)						
					n.getCode().huffmanAddBit('0');
					
					for (ISymbol n : nblist1)
					n.getCode().huffmanAddBit('1');
					
					System.out.println("total="+total+" total2="+total2);
					for (ISymbol n : nblist0)
						System.out.println("\t"+n.toString() + "= " + ((n.getCode()==null)?"?":n.getCode().toString()) + ": " + table.get(n).toString());
					for (ISymbol n : nblist1)
						System.out.println("\t"+n.toString() + "= " + ((n.getCode()==null)?"?":n.getCode().toString()) + ": " + table.get(n).toString());
				
					System.out.println("splited :\n");
					buildHuffman(nblist0);//'0'
					System.out.println("");
					buildHuffman(nblist1);//'1'
					
				}

			
		  };
		
		
		  func.buildHuffman(nblist);
		  for(ISymbol n:nblist)
		  {
			    System.out.println(n.toString() + "= " + ((n.getCode()==null)?"?":n.getCode().toString()) + ": " + table.get(n).toString());
				
		  }
		  //link code <-> symbol
		  for(Symbol sym:nblist)
			  sym.getCode().setSymbol(sym);
		  
		
		  
	
		  {
		  Collections.sort(nblist, new MySymbolCodeComparator());
		  System.out.println("\nsymmary\n=========");
		  for(ISymbol n:nblist)
		  {
			    System.out.println(n.toString() + "=\t" + ((n.getCode()==null)?"?":n.getCode().toString()) + ": " + table.get(n).toString());
				
		  }
		  }
		  {
		  Collections.sort(nblist, new MyCodeSymbolComparator());
		  System.out.println("\n\n");
		  for(ISymbol n:nblist)
		  {
			    System.out.println(((n.getCode()==null)?"?":n.getCode().toString())+  "\t\t: " +n.toString() + "= "  + table.get(n).toString());
				
		  }
		  }
		  

		  for(ISymbol n:nblist)
		  {
			  if (n.isChar())
		  charToSymbol[n.getChar()]=n;
			  
				  }
		  
	}
	public void compress( BinaryStdIn i,BinaryStdOut o)
	{
		 // read one 8-bit char at a time
	       while (!i.isEmpty()) {
	           char c = i.readChar();
	        //   System.out.println(" char('"+c+"')="+charToSymbol[c].getCode().toString());
	           charToSymbol[c].getCode().write(o);

	       }
	}
	*/
}
