package com.zoubworld.java.utils.compress.test;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;


import com.zoubworld.java.utils.compress.CodingSet;
import com.zoubworld.java.utils.compress.HuffmanCode;
import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.ShannonFanoEliasCode;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.file.BinaryFinFout;
import com.zoubworld.java.utils.compress.file.FileAllocationTable;

public class FileAllocationTableTest {

	@Test
	public void test() {
		FileAllocationTable fat=new FileAllocationTable("C:\\Temp\\test\\1\\");
		System.out.println(fat.toString());
		List<ISymbol> ls = fat.toSymbol();
		System.out.println(ls.size());
		FileAllocationTable fat2=new FileAllocationTable(ls);
		assertEquals(ls, fat2.toSymbol());
		//System.out.println(Symbol.listSymbolToString(ls));
		Map<ISymbol, Long> freq = Symbol.FreqId(ls);
		System.out.println(freq.toString().replaceAll(",", "\n"));
		System.out.println("N="+freq.size());
		BinaryFinFout b=new BinaryFinFout();
		 
		 ICodingRule h=ShannonFanoEliasCode.buildCode(freq);
		 // h=HuffmanCode.buildCode(freq);
		b.setCodingRule(h);
		// b.setCodingRule(new CodingSet(CodingSet.NOCOMPRESS));
			b.writes(ls);
		System.out.println("symbols="+ls.size());
		System.out.println("bits="+b.size());
		b.write(h);
		System.out.println("bits="+b.size());
		// CodingSet.NOCOMPRESS = 57678 / 47440
		// HuffmanCode.buildCode(freq) 32653/33096
		// ShannonFanoEliasCode.buildCode(freq) 38035
		while (ls.remove(Symbol.EOS));
		while (ls.remove(Symbol.INT4));
		while (ls.remove(Symbol.INT12));
		System.out.println(Symbol.listSymbolToString(ls));
		
	}

}
