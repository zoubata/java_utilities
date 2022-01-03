/**
 * 
 */
package com.zoubworld.java.utils.compress.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zoubworld.java.utils.compress.Code;
import com.zoubworld.java.utils.compress.CodeComparator;
import com.zoubworld.java.utils.compress.CodeComparatorInteger;
import com.zoubworld.java.utils.compress.CodingSet;
import com.zoubworld.java.utils.compress.CompositeSymbol;
import com.zoubworld.java.utils.compress.ICode;
import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.file.BinaryFinFout;

public class SymbolTest {

	/**
	 * Test method for
	 * {@link com.zoubwolrd.java.utils.compress.Symbol#Symbol(char)}.
	 */
	@Test
	public void testSymbolChar() {
		// fail("Not yet implemented");
	}
	@Test
	public void testSymbolBigIOnteger() {
		ICodingRule cs=new CodingSet(CodingSet.NOCOMPRESS32);
		Symbol.apply(cs);
		BigInteger b=new BigInteger("123");
		ISymbol s0 = Symbol.FactorySymbolINT(b);
		assertEquals(s0, Symbol.FactorySymbolINT((long) 123));
		b=new BigInteger("12345678901234567890012345678900123456789001234567890");
		ISymbol s1 = Symbol.FactorySymbolINT(b);
		assertEquals( Symbol.BigINTn.getId(),s1.getId());
		assertEquals( "00000000000000000000000100100100000000000001011000100000111111110100000110011100010101001100001111011001101011001101000010100110101010101101101011100000100110000110110111100001010100111011110101010001011100000101001011010010",s1.getCode().toRaw());
		b=new BigInteger("-12345678901234567890");
		ISymbol s2 = Symbol.FactorySymbolINT(b);
		assertEquals( Symbol.BigINTn.getId(),s2.getId());
		assertEquals( "000000000000000000000001001001000000000000001001111111110101010010101011010101100111001100010100111000001111010100101110",s2.getCode().toRaw());
		
		b=new BigInteger("12345678901234567891");
		ISymbol s3 = Symbol.FactorySymbolINT(b);
		assertEquals( Symbol.BigINTn.getId(),s3.getId());
		assertEquals( "000000000000000000000001001001000000000000001001000000001010101101010100101010011000110011101011000111110000101011010011",s3.getCode().toRaw());
		
		b=new BigInteger("12345678901");
		ISymbol s4 = Symbol.FactorySymbolINT(b);
		assertEquals( 12345678901L,((CompositeSymbol)s4).getS2().getId());
		assertEquals( Symbol.INT48.getId(),s4.getId());
		assertEquals( "00000000000000000000000100000110000000000000001011011111110111000001110000110101",s4.getCode().toRaw());
		
		b=new BigInteger("-123456789");
		ISymbol s5 = Symbol.FactorySymbolINT(b);
		assertEquals( -123456789,((CompositeSymbol)s5).getS2().getId());
		assertEquals( Symbol.INT32.getId(),s5.getId());
		assertEquals( "11111000101001000011001011101011",((CompositeSymbol)s5).getS2().getCode().toRaw());
		assertEquals( "0000000000000000000000010000010111111000101001000011001011101011",s5.getCode().toRaw());
		
		b=new BigInteger("12345");
		ISymbol s6 = Symbol.FactorySymbolINT(b);
		assertEquals( 12345,((CompositeSymbol)s6).getS2().getId());
		assertEquals( Symbol.INT16.getId(),s6.getId());
		assertEquals( "000000000000000000000001000000110011000000111001",s6.getCode().toRaw());
		
		b=new BigInteger("-123");
		ISymbol s7 = Symbol.FactorySymbolINT(b);
		assertEquals( Symbol.INT8.getId(),s7.getId());
		assertEquals( -123,((CompositeSymbol)s7).getS2().getId());
		assertEquals( "10000101",((CompositeSymbol)s7).getS2().getCode().toRaw());
		assertEquals( "0000000000000000000000010000000110000101",s7.getCode().toRaw());
		
		
		b=new BigInteger("123456789012345");
		ISymbol s8 = Symbol.FactorySymbolINT(b);
		assertEquals( 123456789012345L,((CompositeSymbol)s8).getS2().getId());
		assertEquals( Symbol.INT48.getId(),s8.getId());
		assertEquals( "00000000000000000000000100000110011100000100100010000110000011011101111101111001",s8.getCode().toRaw());
		
		b=new BigInteger("-8");
		ISymbol s9 = Symbol.FactorySymbolINT(b);
		assertEquals( -8,((CompositeSymbol)s9).getS2().getId());
		assertEquals( "1000",((CompositeSymbol)s9).getS2().getCode().toRaw());
		assertEquals( Symbol.INT4.getId(),s9.getId());
		assertEquals( "000000000000000000000001000000001000",s9.getCode().toRaw());
		
		b=new BigInteger("-1234");
		ISymbol s10 = Symbol.FactorySymbolINT(b);
		assertEquals( Symbol.INT12.getId(),s10.getId());
		assertEquals( -1234,((CompositeSymbol)s10).getS2().getId());
		assertEquals( "101100101110",((CompositeSymbol)s10).getS2().getCode().toRaw());
		assertEquals( "00000000000000000000000100000010101100101110",s10.getCode().toRaw());
		
		b=new BigInteger("-1234345");
		ISymbol s11 = Symbol.FactorySymbolINT(b);
		assertEquals( -1234345,((CompositeSymbol)s11).getS2().getId());
		assertEquals( Symbol.INT24.getId(),s11.getId());
		assertEquals( "00000000000000000000000100000100111011010010101001010111",s11.getCode().toRaw());
		
		
		BinaryFinFout bin=new BinaryFinFout();
		bin.setCodingRule(cs);
		bin.write(s0);
		bin.write(s1);
		bin.write(s2);
		bin.write(s3);
		bin.write(s0);
		bin.write(s4);
		bin.write(s5);
		bin.write(s6);
		bin.write(s7);
		bin.write(s8);
		bin.write(s9);
		bin.write(s10);
		bin.write(s11);
		
		bin.flush();
		assertEquals( s0,bin.readSymbol());
		assertEquals( s1,bin.readSymbol());
		assertEquals( s2,bin.readSymbol());
		assertEquals( s3,bin.readSymbol());
		assertEquals( s0,bin.readSymbol());
		assertEquals( s4,bin.readSymbol());
		assertEquals( s5,bin.readSymbol());
		assertEquals( s6,bin.readSymbol());
		ISymbol s = bin.readSymbol();
		assertEquals( "10000101",((CompositeSymbol)s).getS2().getCode().toRaw());
		assertEquals( s7,s);
		assertEquals( s8,bin.readSymbol());
		assertEquals( s9,bin.readSymbol());
		assertEquals( s10,bin.readSymbol());
		assertEquals( s11,bin.readSymbol());
		
		
		
	}
	
	@Test
	public void testEqualsObject() {
		
		List<ISymbol> ls=Symbol.from("abbcccddeefff");
		assertEquals(ls.get(1), ls.get(1));
		assertEquals(new Symbol('a'), new Symbol('a'));
		assertEquals(ls.get(0), new Symbol('a'));
		assertEquals(ls.get(1), ls.get(2));
		assertNotEquals(ls.get(0), ls.get(1));
		 Map<ISymbol, Long> m = ISymbol.Freq(ls);
		 assertEquals(6, m.size());
	}
	@Test
	public void testtodo() {
		List<ISymbol> ls = Symbol.from("a1321321132\nbcdsadcb\ndennbscbd\n123451236");
		List<ISymbol>[] lst = Symbol.from("a1321321132\nbcdsadcb\ndennbscbd\n123451236".split("\n"));
		List<List<ISymbol>> lsl = Symbol.Split(ls, Symbol.findId('\n'));
		for (int i = 0; i < lst.length - 1; i++)
			lst[i].add(Symbol.findId('\n'));
		for (int i = 0; i < lst.length; i++)
			assertEquals("i=" + i, lsl.get(i), lst[i]);
		lsl = Symbol.Split(ls, Symbol.findId('\n'));
		assertEquals("abd1", Symbol.listSymbolToString(Symbol.transpose(lsl).get(0)));
		assertEquals("{9=1, 10=1, 11=1}", Symbol.Distance(ls, Symbol.findId('\n')).toString());
		/*
		 * assertEquals("[{'1'=1, 'a'=1, 'b'=1, 'd'=1}, " +
		 * "{'1'=1, 'c'=1, '2'=1, 'e'=1}," + " {'3'=2, 'd'=1, 'n'=1}, " +
		 * "{'s'=1, '2'=1, '4'=1, 'n'=1}," + " {'a'=1, '1'=1, 'b'=1, '5'=1}, " +
		 * "{'1'=1, 's'=1, '3'=1, 'd'=1}, " + "{'c'=2, '2'=2}, " +
		 * "{'1'=1, '3'=1, 'b'=2}," + " {'1'=1, 'd'=1, '6'=1, \\xa=1}," +
		 * " {'3'=1, \\xa=1}, {'2'=1}, {\\xa=1}]",Symbol.Freql(lsl).toString());
		 */
	
		ICodingRule cs=new CodingSet(CodingSet.UNCOMPRESS);
		assertEquals("01100001", Symbol.toCodes(ls,cs).get(0).toRaw());
		assertEquals("00001010", Symbol.toCode(Symbol.findId('\n'),cs).toRaw());
	
	}

	/**
	 * Test method for
	 * {@link com.zoubwolrd.java.utils.compress.Symbol#Symbol(java.lang.String)}.
	 */
	@Test
	public void testSymbolString() {
		// fail("Not yet implemented");
	}

	@Test
	public void testEqual() {
		ISymbol s1 = Symbol.FactorySymbolINT(5);
		ISymbol s2 = Symbol.FactorySymbolINT(5);
		ISymbol s3 = Symbol.FactorySymbolINT(7);
		ISymbol a = Symbol.findId('a');
		ISymbol b = Symbol.findId('b');
	/*	ISymbol c = Symbol.findId('c');
		ISymbol D = Symbol.findId('D');
*/
		assertNotEquals(a, b);
		assertEquals(s1, s1);
		assertEquals(s1, s2);
		assertNotEquals(s1, s3);

		assertEquals(s1.hashCode(), s1.hashCode());
		assertEquals(s1.hashCode(), s2.hashCode());
		assertNotEquals(s1.hashCode(), s3.hashCode());
		assertEquals(s1.compareTo(s1), 0);
		assertEquals(s1.compareTo(s2), 0);
		assertEquals(s2.compareTo(s1), 0);
		assertEquals(s1.compareTo(s3), -2);
		assertEquals(s3.compareTo(s1), 2);

	}

	@Test
	public void testint() {
		
		assertEquals(null,Symbol.getINTn(Symbol.RLE));
		assertEquals((long)10L,Symbol.getINTn(Symbol.FactorySymbolINT(10)).longValue());
		assertEquals((long)100L,Symbol.getINTn(Symbol.FactorySymbolINT(100)).longValue());
		assertEquals((long)1000L,Symbol.getINTn(Symbol.FactorySymbolINT(1000)).longValue());
		assertEquals((long)10000L,Symbol.getINTn(Symbol.FactorySymbolINT(10000)).longValue());
		assertEquals((long)100000L,Symbol.getINTn(Symbol.FactorySymbolINT(100000)).longValue());
		assertEquals((long)10000000L,Symbol.getINTn(Symbol.FactorySymbolINT(10000000)).longValue());
		assertEquals((long)1000000000L,Symbol.getINTn(Symbol.FactorySymbolINT(1000000000)).longValue());
		assertEquals((long)100000000000L,Symbol.getINTn(Symbol.FactorySymbolINT(100000000000L)).longValue());
		assertEquals((long)10000000000000000L,Symbol.getINTn(Symbol.FactorySymbolINT(10000000000000000L)).longValue());
	
		
		assertEquals((long)-10L,Symbol.getINTn(Symbol.FactorySymbolINT(-10)).longValue());
		assertEquals((long)-100L,Symbol.getINTn(Symbol.FactorySymbolINT(-100)).longValue());
		assertEquals((long)-1000L,Symbol.getINTn(Symbol.FactorySymbolINT(-1000)).longValue());
		assertEquals((long)-10000L,Symbol.getINTn(Symbol.FactorySymbolINT(-10000)).longValue());
		assertEquals((long)-100000L,Symbol.getINTn(Symbol.FactorySymbolINT(-100000)).longValue());
		assertEquals((long)-10000000L,Symbol.getINTn(Symbol.FactorySymbolINT(-10000000)).longValue());
		assertEquals((long)-1000000000L,Symbol.getINTn(Symbol.FactorySymbolINT(-1000000000)).longValue());
		assertEquals((long)-100000000000L,Symbol.getINTn(Symbol.FactorySymbolINT(-100000000000L)).longValue());
		assertEquals((long)-10000000000000000L,Symbol.getINTn(Symbol.FactorySymbolINT(-10000000000000000L)).longValue());
		
		assertEquals((long)10L,Symbol.getLong(Symbol.FactorySymbolINT(10)).longValue());
		assertEquals((long)100L,Symbol.getLong(Symbol.FactorySymbolINT(100)).longValue());
		assertEquals((long)1000L,Symbol.getLong(Symbol.FactorySymbolINT(1000)).longValue());
		assertEquals((long)10000L,Symbol.getLong(Symbol.FactorySymbolINT(10000)).longValue());
		assertEquals((long)100000L,Symbol.getLong(Symbol.FactorySymbolINT(100000)).longValue());
		assertEquals((long)10000000L,Symbol.getLong(Symbol.FactorySymbolINT(10000000)).longValue());
		assertEquals((long)1000000000L,Symbol.getLong(Symbol.FactorySymbolINT(1000000000)).longValue());
		assertEquals((long)100000000000L,Symbol.getLong(Symbol.FactorySymbolINT(100000000000L)).longValue());
		assertEquals((long)10000000000000000L,Symbol.getLong(Symbol.FactorySymbolINT(10000000000000000L)).longValue());
	
		
		assertEquals((long)-10L &0xf,Symbol.getLong(Symbol.FactorySymbolINT(-10)).longValue());
		assertEquals((long)-100L &0xff,Symbol.getLong(Symbol.FactorySymbolINT(-100)).longValue());
		assertEquals((long)-1000L &0xfff,Symbol.getLong(Symbol.FactorySymbolINT(-1000)).longValue());
		assertEquals((long)-10000L &0xffff,Symbol.getLong(Symbol.FactorySymbolINT(-10000)).longValue());
		assertEquals((long)-100000L &0xffffff,Symbol.getLong(Symbol.FactorySymbolINT(-100000)).longValue());
		assertEquals((long)-10000000L   &0xffffffffL,Symbol.getLong(Symbol.FactorySymbolINT(-10000000)).longValue());
		assertEquals((long)-1000000000L &0xffffffffL,Symbol.getLong(Symbol.FactorySymbolINT(-1000000000)).longValue());
		assertEquals((long)-100000000000L&0xffffffffffffL,Symbol.getLong(Symbol.FactorySymbolINT(-100000000000L)).longValue());
		assertEquals((long)-10000000000000000L&0xffffffffffffffffL,Symbol.getLong(Symbol.FactorySymbolINT(-10000000000000000L)).longValue());
	
	}
	@Test
	public void testEqu() {
		assertEquals(0, Symbol.BTE.compareTo(Symbol.BTE));
		assertEquals(-35, Symbol.RLE.compareTo(Symbol.BTE));
		assertEquals(35, Symbol.BTE.compareTo(Symbol.RLE));
		assertEquals(false, Symbol.BTE.equals(null));
		assertEquals(-1, Symbol.BTE.compareTo(null));
		assertEquals(true, Symbol.BTE.equals(Symbol.BTE));
		assertEquals(false, Symbol.RLE.equals(Symbol.BTE));
		assertEquals(false, Symbol.BTE.equals(Symbol.RLE));
		assertEquals(299, Symbol.BTE.getInt().intValue());
		assertEquals(null, Symbol.BTE.getLong());
		assertEquals(null, Symbol.findId('0').getInt());
		assertEquals(0, Symbol.BTE.getShort());
		
	}	
	@Test
	public void test1() 
	{
		List<ISymbol> ls;
	/*
	public static 
	<T> List<List<T>> normalizeDistance(List<List<T>> table,T separator)
	*/
		
	List<List<ISymbol>> lts=Symbol.Split(ls=Symbol.factoryCharSeq(TestData.string1), 64);
	List<Map<ISymbol, Long>> lm=Symbol.Freql(lts);
	CodingSet cs = new CodingSet(CodingSet.NOCOMPRESS16);
	assertEquals(ls.subList(0, 64).toString(), lts.get(0).toString());
	assertEquals(ls.toString(), Symbol.join(lts).toString());
	assertEquals(64, lts.get(0).size());
	assertEquals(18, lm.get(0).size());
	assertEquals(2352, ISymbol.length(lm.get(0), cs).intValue());
	lts=Symbol.Split(ls=Symbol.factoryCharSeq(TestData.string1), Symbol.findId('\n'));

	/*List<List<ISymbol>> lts2 = Symbol.normalizeDistance(lts,Symbol.Empty);
	assertEquals(ls.toString(), Symbol.join(lts).toString());
	assertEquals(lts2.size(), lts.size());
	assertEquals(0, Symbol.join(lts2).size());
	*/
	
	/*	
	 * public static List<ISymbol> FactorySymbolDoubleAsASCIIes3(Double d) {
	public static List<ISymbol> FactorySymbolFloatAsASCII(Float f) {
	
	public static Double getDoubleAsASCII(ISymbol s)
	public static Float getFloatAsASCII(ISymbol s) 
	public static List<ISymbol> FactorySymbolIntAsASCII(Long l) 
	getINTn(ISymbol s) 

	static public List<ICode> toCodes(List<ISymbol> ls) {
	static public ICode toCode(ISymbol s, ICodingRule cs) {
	static public ICode toCode(ISymbol s) {
	public static Symbol getFromSet(Set<Symbol> keySet, char charAt) {
	public static String toString(List<ISymbol> ls) {
		*/
	}
	@Test
	public void testlength() {
		List<ISymbol> ls = null;
		ls = Symbol.factoryCharSeq("0123456789");
		Symbol s = new Symbol("0123456789");
		assertEquals('0', s.getChar());
		ICodingRule cs = new CodingSet(CodingSet.NOCOMPRESS);
		assertEquals('0', s.getChar());
		assertEquals(10 * 9, ISymbol.length(ls, cs).intValue());
		assertEquals("0123456789", Symbol.toString(ls));
		
		cs = new CodingSet(CodingSet.NOCOMPRESS16);
		assertEquals(10 * 16, ISymbol.length(ls, cs).intValue());

		cs = new CodingSet(CodingSet.NOCOMPRESS32);
		assertEquals(10 * 32, ISymbol.length(ls, cs).intValue());

		cs = new CodingSet(CodingSet.UNCOMPRESS);
		assertEquals(10 * 8, ISymbol.length(ls, cs).intValue());
		
		Map<ISymbol, Long> freqSym=Symbol.Freq(ls);
		assertEquals(10 * 8, ISymbol.length(freqSym,  cs).intValue());
		
		
		
		
	}

	@Test
	public void testCode() {
		{

			Code c = new Code();
			System.out.println(c.toString());
			c.huffmanAddBit('0');
			System.out.println(c.toString());
			assertEquals(c.toString(), "(0x0 	,1),0b0	");
			c.huffmanAddBit('1');
			System.out.println(c.toString());
			assertEquals(c.toString(), "(0x1 	,2),0b01	");
			c.huffmanAddBit('0');
			System.out.println(c.toString());
			assertEquals(c.toString(), "(0x2 	,3),0b010	");
			c.huffmanAddBit('1');
			System.out.println(c.toString());
			assertEquals(c.toString(), "(0x5 	,4),0b0101	");
			c.huffmanAddBit('0');
			System.out.println(c.toString());
			assertEquals(c.toString(), "(0xa 	,5),0b01010	");
			c.huffmanAddBit('1');
			System.out.println(c.toString());
			assertEquals(c.toString(), "(0x15 	,6),0b010101	");
			c.huffmanAddBit('1');
			System.out.println(c.toString());
			assertEquals(c.toString(), "(0x2b 	,7),0b0101011	");
			c.huffmanAddBit('1');
			System.out.println(c.toString());
			assertEquals(c.toString(), "(0x57 	,8),0b01010111	");
/*
			ICode a = new Code((char) 1);
			System.out.println("(char) 1 : " + a.toString() + ": " + a.toRaw());
			assertEquals(a.toString(), "(0x1 	,16),0b0000000000000001	");
			assertEquals(a.toRaw(), "0000000000000001");
			*/
			ICode a = new Code((byte) 1);
			System.out.println("(char) 1 : " + a.toString() + ": " + a.toRaw());
			assertEquals(a.toString(), "(0x1 	,8),0b00000001	");
			assertEquals(a.toRaw(), "00000001");
			
			ICode b = new Code((short) 2);
			System.out.println("(short) 2 : " + b.toString() + ": " + b.toRaw());
			assertEquals(b.toString(), "(0x2 	,16),0b0000000000000010	");
			assertEquals(b.toRaw(), "0000000000000010");
			assertEquals(c.toString(), "(0x57 	,8),0b01010111	");
			assertEquals(c.toRaw(), "01010111");
			ICode d = new Code((int) 4);
			System.out.println("(int) 4 : " + d.toString() + ": " + c.toRaw());
			assertEquals(d.toString(), "(0x4 	,32),0b00000000000000000000000000000100	");
			assertEquals(d.toRaw(), "00000000000000000000000000000100");
			c = new Code((long) 0x123456789L);
			System.out.println("(long) 0x123456789 : " + c.toString() + ": " + c.toRaw());
			System.out.print("0b");
			assertEquals(c.toString(),
					"(0x123456789 	,64),0b0000000000000000000000000000000100100011010001010110011110001001	");
			assertEquals(c.toRaw(), "0000000000000000000000000000000100100011010001010110011110001001");
			/*
			 * for(int i=c.lenbit-1;i>=0;i--) System.out.print(c.getMsb(i));
			 * System.out.println();
			 */}

		{
			Code a = new Code((char) 0x80);
			System.out.println("() 0x80 : \t" + a.toString() + "\t: " + a.toRaw());
			Code b = new Code((char) 0x8F);
			System.out.println("() 0x8F : \t" + b.toString() + "\t: " + b.toRaw());
			Code c = new Code(0x1007F);
			System.out.println("() 0x1007F : \t" + c.toString() + "\t: " + c.toRaw());
			Code d = new Code((short) 0x1);
			System.out.println("() 0x1 : \t" + d.toString() + "\t: " + d.toRaw());

			System.out.println(a.toString() + ".compareToCode(" + b.toString() + ")=" + a.compareToCode(b));
			assertEquals(a.compareToCode(b), -1);
			assertEquals(b.compareToCode(c), 1);
			assertEquals(c.compareToCode(a), -1);

			System.out.println(b.toString() + ".compareToCode(" + c.toString() + ")=" + b.compareToCode(c));
			System.out.println(c.toString() + ".compareToCode(" + a.toString() + ")=" + c.compareToCode(a));
			System.out.println(c.toString() + ".compareToCode(" + d.toString() + ")=" + c.compareToCode(d));
		}
		{// test sort
			List<Code> lc = new ArrayList<Code>();
			for (int i = 1; i < 25; i += 5)
				lc.add(new Code((char) i));
			for (int i = 1; i < 25; i += 5)
				lc.add(new Code((short) (i)));
			for (int i = 1; i < 25; i += 5)
				lc.add(new Code((short) (i * 256)));
			for (int i = 1; i < 30; i += 5)
				lc.add(new Code((short) (i * 256 + i)));

			Collections.sort(lc, new CodeComparator());
			System.out.println("\nCodeComparator\n=========");
			for (ICode n : lc) {
				System.out.println(n.toString() + "\t=\t" + n.toString());

			}

			Collections.sort(lc, new CodeComparatorInteger());
			System.out.println("\nCodeComparatorInteger\n=========");
			for (ICode n : lc) {
				System.out.println(n.toString() + "\t=\t" + n.toString());

			}

		}

	}

	@Test
	public void testFactorySymbolINT() {
		CompositeSymbol s;

		s = (CompositeSymbol) Symbol.FactorySymbolINT(1);
		assertEquals(1, s.getS2().getId());
		assertEquals(Symbol.INT4.getId(), s.getS1().getId());
		assertEquals(Symbol.INT4.getId(), 256);
		s = (CompositeSymbol) Symbol.FactorySymbolINT(256);
		assertEquals(Symbol.INT12.getId(), s.getS1().getId());
		assertEquals(256, s.getS2().getId());
		s = (CompositeSymbol) Symbol.FactorySymbolINT(256 * 126);
		assertEquals(Symbol.INT16.getId(), s.getS1().getId());

		assertEquals(256 * 126, s.getS2().getId());
		s = (CompositeSymbol) Symbol.FactorySymbolINT(256 * 6);
		assertEquals(256 * 6, s.getS2().getId());

		s = (CompositeSymbol) Symbol.FactorySymbolINT(127);
		assertEquals(Symbol.INT8.getId(), s.getS1().getId());

		assertEquals(127, s.getS2().getId());

		s = (CompositeSymbol) Symbol.FactorySymbolINT(256 * 256);
		assertEquals(256 * 256, s.getS2().getId());
		s = (CompositeSymbol) Symbol.FactorySymbolINT(256L * 256L * 256L);
		assertEquals(256L * 256L * 256L, s.getS2().getId());
		s = (CompositeSymbol) Symbol.FactorySymbolINT(256L * 256L * 256L * 256L * 256L);
		assertEquals(256L * 256L * 256L * 256L * 256L, s.getS2().getId());
		s = (CompositeSymbol) Symbol.FactorySymbolINT(256L * 256L * 256L * 256L);
		assertEquals(256L * 256L * 256L * 256L, s.getS2().getId());

		s = (CompositeSymbol) Symbol.FactorySymbolINT(256L * 256L * 256L * 256L * 256L * 256L);
		assertEquals(256L * 256L * 256L * 256L * 256L * 256L, s.getS2().getId());

		s = (CompositeSymbol) Symbol.FactorySymbolINT(256L * 256L * 256L * 256L * 256L * 256L * 255L);
		assertEquals(256L * 256L * 256L * 256L * 256L * 256L * 255L, s.getS2().getId());

		s = (CompositeSymbol) Symbol.FactorySymbolINT(-1);
		assertEquals("INT4(-1)", s.toString());

	}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.Symbol#isChar()}.
	 */
	@Test
	public void testIsChar() {
		ISymbol c = new Symbol((char) 123);
		assertTrue(c.isChar());

		ISymbol s = new Symbol((int) 12356);
		assertTrue(!s.isChar());
	}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.Symbol#getChar()}.
	 */
	@Test
	public void testGetChar() {

		ISymbol c = new Symbol((char) 123);
		assertTrue(c.getChar() == 123);

	}

	/**
	 * Test method for
	 * {@link com.zoubwolrd.java.utils.compress.Symbol#listSymbolToCharSeq(java.util.List)}.
	 */
	@Test
	public void testListSymbolToCharSeq() {
		// fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.zoubwolrd.java.utils.compress.Symbol#factoryCharSeq(java.lang.String)}.
	 */
	@Test
	public void testFactoryCharSeq() {
		// fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.Symbol#getInt()}.
	 */
	@Test
	public void testGetInt() {
		ISymbol c = new Symbol((int) 123);
		assertTrue(c.getInt() == 123);

		ISymbol s = new Symbol((int) 12356);
		assertTrue(s.getInt() == 12356);

		ISymbol i = new Symbol((int) 0xF654321);
		assertTrue(i.getInt() == 0xF654321);

	}

	/**
	 * Test method for {@link net.zoubwolrd.java.utils.compress.factoryFile()}.
	 */
	@Test
	public void testFactoryFile() {
		// ClassLoader classLoader = getClass().getClassLoader();
		// File file = new
		// File(classLoader.getResource("res/test/smallfile.txt").getFile());
		File file = new File("res/test/smallfile.txt");
		File file2 = new File("res/test/smallfile.txt" + ".tmp");
		System.out.println(file.getAbsolutePath());

	//	String inputFile = "";
		List<ISymbol> ls = Symbol.factoryFile(file.getAbsolutePath());
		Symbol.listSymbolToFile(ls, file2.getAbsolutePath());

		assertTrue(file.length() == file2.length());

	}

	/**
	 * Test method for
	 * {@link com.zoubwolrd.java.utils.compress.Symbol#Symbol(byte)}.
	 */
	@Test
	public void testSymbolByte() {
		// fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.Symbol#Symbol(int)}.
	 */
	@Test
	public void testSymbolInt() {
		Symbol s = new Symbol((int) 256);
		assertEquals(s.getId(), 256);
		assertTrue(s.isInt());
		assertTrue(!s.isShort());
		assertTrue(!s.isChar());

		s = new Symbol((char) 123);
		assertEquals(s.getId(), 123);
		assertEquals(s.getChar(), (char) 123);
		assertTrue(s.isChar());
		assertTrue(!s.isInt());
		assertTrue(!s.isShort());

		s = new Symbol((short) 123);
		assertEquals(s.getId(), 123);
		assertEquals(123, s.getShort());
		assertTrue(s.isShort());
		assertTrue(!s.isInt());
		assertTrue(!s.isChar());

		s = new Symbol((short) 256);
		assertEquals(s.getId(), 256);
		assertEquals(s.getShort(), (short) 256);

	}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.Symbol#toSymbol()}.
	 */
	@Test
	public void testToSymbol() {
		// fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link com.zoubwolrd.java.utils.compress.Symbol#getFromSet(java.util.Set, char)}.
	 */
	@Test
	public void testGetFromSet() {
		// fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.zoubwolrd.java.utils.compress.Symbol#getCode()}.
	 */
	@Test
	public void testGetCode() {
		// fail("Not yet implemented");
	}

	@After // tearDown()
	public void after() throws Exception {

		// System.out.println("Running: tearDown");

	}

	@Before // setup()

	public void before() throws Exception {

		// System.out.println("Setting it up!");

	}

}
