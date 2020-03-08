/**
 * 
 */
package com.zoubworld.java.utils.compress.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.algo.BytePairEncoding;
import com.zoubworld.java.utils.compress.algo.ByteTripleEncoding;
import com.zoubworld.java.utils.compress.algo.IAlgoCompress;
import com.zoubworld.java.utils.compress.algo.LZWBasic;
import com.zoubworld.java.utils.compress.algo.MultiAlgo;
import com.zoubworld.java.utils.compress.algo.RLE;

/**
 * @author M43507
 *
 */
public class TestMultiAlgo {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}


	public void testMultiAlgoBasic(String text,int r) {
		
	
	
	
		BytePairEncoding bpe= new BytePairEncoding();
		ByteTripleEncoding bte= new ByteTripleEncoding();
		RLE rle= new RLE();
		
		List<IAlgoCompress> l=new ArrayList<IAlgoCompress>();
		l.add(rle);
		l.add(bpe);		
		l.add(bte);		
		MultiAlgo multiAlgo= new MultiAlgo(l);

		
		
	List<ISymbol> ls=Symbol.factoryCharSeq(text);
	//System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));
	
	List<ISymbol> lse=multiAlgo.encodeSymbol(ls);
	//System.out.println(lse.toString());
	
	System.out.println(lse.size()+":"+ls.size());
	assertTrue(ls.size()>=lse.size());
	assertTrue(ls.size()-r>=lse.size());
	List<ISymbol> ls2 = multiAlgo.decodeSymbol(lse);
//	System.out.println(new String(Symbol.listSymbolToCharSeq(ls)));
	String text2=new String(Symbol.listSymbolToCharSeq(ls2));
	System.out.println(lse.size()+"/"+ls.size());
	assertEquals(text,(text2));
	assertTrue(text.equals(text2));
}

	@Test
	public void testMultiAlgo_Perf() {
		long timens=220*1000*1000L;//0.22s
		
		
		long nano_startTime = System.nanoTime(); 			
		testMultiAlgoBasic( LZWBasic.file,0);			
		long nano_stopTime = System.nanoTime(); 
		System.out.print("duration :"+(nano_stopTime-nano_startTime)+" ns");
		assertTrue("speed perf",(nano_stopTime-nano_startTime)<=timens);//speed performance
	/*	assertThat("speed perf",
		           (nano_stopTime-nano_startTime),
		           lessThan(timens));*/
		
	}
	@Test
	public void testMultiAlgoBasicAll() {
		long timens=355*1000*1000L;//0.15s
		
	/*	testLZ4Basic( "11",0);
		testLZ4Basic( "1",0);*/
		testMultiAlgoBasic("test de compression AAAAAAAAAAAAAAAAA CDCDCDCDCDCDCD test de compression AAAAAAAAAAAAAAAAA CDCDCDCDC\n"
				,101-73);
		testMultiAlgoBasic(":020000042000DA\r\n" + 
		":10000000F0FF0320F10B002081",44-38);
		String s="";
		for(int i=0;i<2048;i+=16)
		s+="0123456789ABCDEF";
		testMultiAlgoBasic(s,2048-2048);
		
		for(int i=0;i<=2048;i+=10)
			s+="0123456789ABCDE\n";
		testMultiAlgoBasic(s,5328-5328);
		s="";
		for(int i=0;i<=2048;i+=10)
			s+="000005677777CDE\n";
		testMultiAlgoBasic(s,3280-2460);
		s="";
		for(int i=0;i<=2048;i+=10)
			s+="0000000000000000";
		testMultiAlgoBasic(s,3280-3);
			
		testMultiAlgoBasic( "klefnhatrytvzyeryyteyrretouizybrebyyelkjkdjfhgjksdnjkdsj,vvi,ouybiotruybiortuyioruyoirtyebetyryetberybre"
		+ "rtyeryteryreybetyreberybyiokemoiskherkhiuilhisunehkvjhlrkuthurzeioazertyuwsdfghjcvbn,rtycvdhjskqieozpahj"
		+ "vcbnxkg hcjxk tyucixow tvyfudiz cndjeio nvcjdkezo& ,ckdlsozpa ,;cldsmpz ,cdklazertyuisdfghjkxcvbpklbn,zb"
		+ "azertyuiopqqqqqqqqqqqqqqqqqsdfghjklmwxcvbn,azertyuiopsdfghjkxcvbvretczehcgbtkzjebgtckhekzbgnxkhegrhztghz"
		+ "wqaxszcdevfrbgtnhy,ju;ki:lo!mp^*$wqaxszxszcdecdevfrvfrbgtcdeznhy,juxsz;kiwq:lo!mpcdevfrcdzxsznhywqa,jun"
		+ "njibhuvgycftxdrwsewqawqzwsewsewszwsdcdevfdbchdun jcdienbjvkfdflwjkvcsnvhlrejkhtvlhy;kivfrcdenhycdexsz)"
		,607-621);
		
		long nano_startTime = System.nanoTime(); 			
		testMultiAlgoBasic( LZWBasic.file,8722-9327);			
		long nano_stopTime = System.nanoTime(); 
		System.out.println("duration :"+(nano_stopTime-nano_startTime)+" ns, excpedted<"+timens+" ns");
		assertTrue("speed perf",(nano_stopTime-nano_startTime)<=timens);//speed performance
	
		
	}	
}
