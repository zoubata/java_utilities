package com.zoubworld.java.utils.compress.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zoubworld.java.utils.compress.CodingSet;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.algo.LZ4;
import com.zoubworld.java.utils.compress.algo.LZS;
import com.zoubworld.java.utils.compress.algo.LZWBasic;
import com.zoubworld.java.utils.compress.algo.lz4.LZ4Block;
import com.zoubworld.java.utils.compress.algo.lz4.LZ4FrameFormat;
import com.zoubworld.java.utils.compress.file.BinaryFinFout;

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
	public void testLZ4FrameFormat() {
		LZ4FrameFormat f=new LZ4FrameFormat();
		LZ4Block e=new LZ4Block();
		e.setCompressed(true);
		//e.getData().addAll(Symbol.from("AAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
		//e.getData().addAll(Symbol.from("01234567890123456789012345678901234567890123456789\r\n"));
		e.getData().addAll(Symbol.from("0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz"));
		String s="0123456789abcdefghijklmnopqrstuvwxyzzzyxwvutsrqponmlkjihgfedcba9876543210./*-+!:;,ù*$^=)àç_è-('\"é&²&aqwézsx\"edc'rfv(tgb-yhnèuj,_ik;çol:àpm!§MP0/LO9.KI8?JU7NHY6BGT5VFR4CDE3XSZ2WQA1!mpà:loç;ki_,juènhy-bgt(vfr'cdde\"xszéwqa&AZERTYUIOP¨£QSDFGHJKLM%µWXCVBN?./§§/.?NBVCXWµ%MLKJHGFDSQ£¨POIUYTREZA0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz0123456789abcdefghijklmnopqrstuvwxyz";
		e.getData().addAll(Symbol.from(s));
		f.getBlocks().add(e);
		BinaryFinFout bin=new BinaryFinFout();
		bin.write(f.toCodes());
		bin.flush();
	/*	for(;!bin.isEmpty();)
			System.out.println(String.format("%2X", bin.readByte()).replace(' ', '0'));
	*/	LZ4FrameFormat f2=new LZ4FrameFormat();
	bin.setCodingRule(new CodingSet(CodingSet.UNCOMPRESS));
	f2.read(bin);
	assertEquals(f.getChecksum(), f2.getChecksum());
	assertEquals(f.getDescript().toString(), f2.getDescript().toString());
	assertEquals(f.getBlocks().size(), f2.getBlocks().size());
	assertEquals(f.getBlocks().get(0), f2.getBlocks().get(0));
	
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
