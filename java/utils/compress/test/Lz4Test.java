package com.zoubworld.java.utils.compress.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.algo.LZ4;
import com.zoubworld.java.utils.compress.algo.LZS;
import com.zoubworld.java.utils.compress.algo.LZWBasic;

public class Lz4Test {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testLZ4BasicAll() {
		long timens=150*1000*1000L;//0.15s
		
		testLZ4Basic( "11",0);
		testLZ4Basic( "1",0);
		testLZ4Basic( "1111100000001111100000",0);
		testLZ4Basic("test de compression AAAAAAAAAAAAAAAAA CDCDCDCDCDCDCD test de compression AAAAAAAAAAAAAAAAA CDCDCDCDC\n",60);
		
		String s="";
		for(int i=0;i<2048;i+=16)
		s+="0123456789ABCDEF";
		testLZ4Basic(s,2048-71);
		
		for(int i=0;i<=2048;i+=10)
			s+="0123456789ABCDE\n";
			testLZ4Basic(s,3280-105);
			
		testLZ4Basic( "klefnhatrytvzyeryyteyrretouizybrebyyelkjkdjfhgjksdnjkdsj,vvi,ouybiotruybiortuyioruyoirtyebetyryetberybre"
		+ "rtyeryteryreybetyreberybyiokemoiskherkhiuilhisunehkvjhlrkuthurzeioazertyuwsdfghjcvbn,rtycvdhjskqieozpahj"
		+ "vcbnxkg hcjxk tyucixow tvyfudiz cndjeio nvcjdkezo& ,ckdlsozpa ,;cldsmpz ,cdklazertyuisdfghjkxcvbpklbn,zb"
		+ "azertyuiopqqqqqqqqqqqqqqqqqsdfghjklmwxcvbn,azertyuiopsdfghjkxcvbvretczehcgbtkzjebgtckhekzbgnxkhegrhztghz"
		+ "wqaxszcdevfrbgtnhy,ju;ki:lo!mp^*$wqaxszxszcdecdevfrvfrbgtcdeznhy,juxsz;kiwq:lo!mpcdevfrcdzxsznhywqa,jun"
		+ "njibhuvgycftxdrwsewqawqzwsewsewszwsdcdevfdbchdun jcdienbjvkfdflwjkvcsnvhlrejkhtvlhy;kivfrcdenhycdexsz)",621-357);
		
		long nano_startTime = System.nanoTime(); 			
		testLZ4Basic( LZWBasic.file,9347-1972);			
		long nano_stopTime = System.nanoTime(); 
		System.out.print("duration :"+(nano_stopTime-nano_startTime)+" ns");
		assertTrue("speed perf",(nano_stopTime-nano_startTime)<=timens);//speed performance
	
		
	}	

	public void testLZ4Basic(String text,int r) {
		
	
	
	
		LZ4 lz4= new LZ4();

		
		
	List<ISymbol> ls=Symbol.factoryCharSeq(text);
	//System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));
	
	List<ISymbol> lse=lz4.encodeSymbol(ls);
	//System.out.println(lse.toString());
	
	System.out.println(lse.size()+":"+ls.size());
	assertTrue(ls.size()>=lse.size());
	assertTrue(ls.size()-r>=lse.size());
	ls=lz4.decodeSymbol(lse);
//	System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));
	String text2=new String(Symbol.listSymbolToCharSeq(ls));
	System.out.println(lse.size()+"/"+ls.size());
	assertEquals(text,(text2));
	assertTrue(text.equals(text2));
}

}
