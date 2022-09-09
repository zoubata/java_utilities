/**
 * 
 */
package com.zoubworld.java.utils.compress.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zoubworld.java.utils.compress.Code;
import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.SymbolComplex.Sym_LSn;
import com.zoubworld.java.utils.compress.algo.LastSymbolUsed;
import com.zoubworld.java.utils.compress.binalgo.CodingSet;
import com.zoubworld.java.utils.compress.file.BinaryFinFout;

/**
 * @author Pierre Valleau
 *
 */
public class LSnNTest {

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

	@Test
	public final void test1() {
		Sym_LSn s;
		for(int i=0;i<Symbol.getNbSymbol();i++)
			assertEquals(""+Symbol.findId(i).toString(),i,Symbol.findId(i).getId());	
		ISymbol s2 = Symbol.findId((int) Symbol.LSn.getId());
		assertEquals(Symbol.LSn,s2);
		CodingSet cs=new CodingSet(CodingSet.NOCOMPRESS32);
		BinaryFinFout b=new BinaryFinFout();
		b.setCodingRule(cs);
		BinaryFinFout b2=new BinaryFinFout();
		b2.setCodingRule(cs);
		 s=new Sym_LSn(1);
		//00000000000000000000000100110000 00 0001
		// 0x130 0 1
		assertEquals("00000000000000000000000100110000000001",s.getCode().toRaw());
		b.write(s.getCode());
		b2.write(s.getCode());
		
		//00000000000000000000000100110000 01 011001
		// 0x130 1 25
		s=new Sym_LSn(41);
		assertEquals("0000000000000000000000010011000001011001",s.getCode().toRaw());
		b2.write(s.getCode());
		b.write(s.getCode());
		
		//00000000000000000000000100110000 10 00111101
		// 0x130 2 61
		s=new Sym_LSn(141);
		assertEquals("000000000000000000000001001100001000111101",s.getCode().toRaw());
		b2.write(s.getCode());
		b.write(s.getCode());
		
		//00000000000000000000000100110000 11    1110 1101 1101
		// 0x130 3 3805
		s=new Sym_LSn(4141);
		assertEquals("0000000000000000000000010011000011111011011101",s.getCode().toRaw());
		Code c = new Code("0000000000000000000000010011000011111011011101");
		b2.write(s.getCode());
		b.write(c);
		//b.write(c);
		b.flush();
		b2.flush();
		
		assertEquals(	"00000000000000000000000100110000000001" + 
						"0000000000000000000000010011000001011001" + 
						"000000000000000000000001001100001000111101" + 
						"0000000000000000000000010011000011111011011101"
						+ "00000000000000000000000000",b.toRaw());
		assertEquals(new Sym_LSn(1),b2.readSymbol());
		assertEquals(new Sym_LSn(41),b2.readSymbol());
		assertEquals(new Sym_LSn(141),b2.readSymbol());
		assertEquals(new Sym_LSn(4141),b2.readSymbol());
		
	}
	
	@Test
	public final void test() {
		
		testBasic(":1003200000000000000000000000000000000000CD",1424L);
		testBasic("1231231231231321231231231231231234123123123",1514L);
		
	}
		public final void testBasic(String data,long len) {
	
	LastSymbolUsed lsnAlgo = new LastSymbolUsed();
		List<ISymbol> ls = null;
		List<ISymbol> lsc = null;
		List<ISymbol> lse = null;
		ICodingRule huf = new CodingSet(CodingSet.UNCOMPRESS);
		ICodingRule cs9 = new CodingSet(CodingSet.NOCOMPRESS);
		ls = Symbol.factoryCharSeq(data);
		lse = lsnAlgo.encodeSymbol(ls);
		System.out.println("flat " + Symbol.length(ls) + " : '" + ls + "'");
		System.out.println("rle  " + Symbol.length(lse, cs9) + " : '" + lse + "'");
		lsc = lsnAlgo.decodeSymbol(lse);
		assertEquals(ls, lsc);
		assertEquals(344L, Symbol.length(ls, huf).longValue());
		assertEquals(len,Symbol.length(lse).longValue() );//(32+2+4)*36+7*32
		//assertTrue(Symbol.length(lse) <= 144);
		//assertTrue(lse.size() <= ls.size());
	

	}

}
