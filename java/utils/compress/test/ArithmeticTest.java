package com.zoubworld.java.utils.compress.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.binalgo.arithemic.ArithmeticDecoder;
import com.zoubworld.java.utils.compress.binalgo.arithemic.ArithmeticEncoder;
import com.zoubworld.java.utils.compress.binalgo.arithemic.IFrequencyTable;
import com.zoubworld.java.utils.compress.file.BinaryFinFout;

import sandbox.mit.CheckedFrequencyTable;
import sandbox.mit.FrequencyTable;
import sandbox.mit.SimpleFrequencyTable;

public class ArithmeticTest {

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
	public final void testArith(List<ISymbol> ls1,String response) throws IOException 
	{
        //00 00 00 00 10 00 00 00 01 10 11 00 01 1
BinaryFinFout bin = new BinaryFinFout();
ArithmeticEncoder sw = new ArithmeticEncoder(32);
com.zoubworld.java.utils.compress.binalgo.arithemic.FrequencyTable freqs;
sw.setFrequencyTable(freqs = new com.zoubworld.java.utils.compress.binalgo.arithemic.FrequencyTable(ls1));
//sw.writeCodingRule(bin);
for (ISymbol sym : ls1)
sw.writeSymbol(bin, sym);
bin.flush();
//assertEquals(response, bin.toRaw());
ArithmeticDecoder sr=new ArithmeticDecoder(32);
sr.setFrequencyTable(freqs );
sr.init(bin);
List<ISymbol> lso =new ArrayList<ISymbol>();
for (ISymbol sym : ls1)
	lso.add(sr.getSymbol(bin));

assertEquals(Symbol.toString(lso), Symbol.toString(ls1));
	
	}
	@Test
	public final void testArithmetic2() throws IOException {
	
	/*	List<ISymbol> ls1 = Symbol.from(	new byte[]{0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2});
		testArith(ls1,"000000001000000001101100011") ;
		*/
		List<ISymbol> ls2 = Symbol.from(	new byte[]{0, 3, 1, 2});
		//001000011111
		testArith(ls2,"001000011111") ;
		
		List<ISymbol> ls3 = Symbol.from(	new byte[] {0,0,0,0,0,0,0,0,0,0});
		              //011
		testArith(ls3,"011") ;
		
		
		byte[] b = new byte[256];
		for (int i = 0; i < b.length; i++)
			b[i] = (byte)i;	
		List<ISymbol> ls4 = Symbol.from(b);	
		//stream0000000000000000111111111111111111111111111110101110011110111001010110101011100011000000010100100101010110011101000010110110101101111110001101100011001001100011011111100010110100111011010010110001111101010000100110111100000101001001111100100011011011001110001011010000000111010111001110001010100110101011100110000101001000001001010101001100110010110111011111010101100011010000100110001011010111010111111101011110111001111101000101111001110010110100110100100110010101101111010110010101010010010000001100101001110111110110010110111110100011110111010111010110101100110000101110011001011001000010000001110010011011101011011011101000001111010010010001010001010011011011011001111111111011001111000001100010100101100001100000000101001101010110101000010101000110000001111100100110000010000011011011011101001001100001001001101100110001011101000101111001110001010000110100101011011110010111011010011011110111111011011000100101000101001100101100100001110111101100100101011000110101000011100110110111100001000010111010011011111100100100111101100000111101000101011010111101010111010010101011010011000000100110110101011111011111010110001101011100100001001100100000010000101100110010100100111110010111011110000011110010100011011011010000010000011101000100101000011001011101100100000100111010110011010010100001100110011001110000101000000010111000010010100111011010101011100001011101010101001001011110001001000011000010110111111011001010100101110101001001011000110000101000110010011110101001010100100000000011000111100000000000010101001110001111001001011000011001100110011100111101100110000011110110110100011001101001101010110110111101010010111100011010101000010000100000010101100100100101011100110110010110110111001000100110010100101110110100001010111100101101010000101001000010001011000011100001101100111111011110000010101101111001110001010111000000100111110110000010111001100101111000101101000100001101010000011010111110010101101100100100101110000100100110010100011000111010000101101010110100001000111011010000000001100111010100010101110000110111111111001101011111100000
		//ART:1281<256
	}
	
	@Test
	public final void testArithmetic() {
		BinaryFinFout bin = new BinaryFinFout();
		ArithmeticEncoder sw = new ArithmeticEncoder(32);
		List<ISymbol> ls = Symbol.from("0123456987133200305153413406341031231012012102102985985985958958955985");
		FrequencyTable freqs;
		sw.setFrequencyTable(freqs = new FrequencyTable(ls));
		sw.writeCodingRule(bin);
		for (ISymbol sym : ls)
			sw.writeSymbol(bin, sym);
		bin.flush();

		List<ISymbol> ls2 = new ArrayList<ISymbol>();
		ArithmeticDecoder sr = null;

		try {
			sr = new ArithmeticDecoder(32);
			sr.ReadCodingRule(bin);
			// sr.setFrequencyTable(freqs);
			sr.init(bin);
			for (int i = 0; i < ls.size(); i++)
				ls2.add(sr.getSymbol(bin));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(((FrequencyTable) sw.getFrequencyTable()).freq, ((FrequencyTable) sr.getFrequencyTable()).freq);
		assertEquals(Symbol.toString(ls), Symbol.toString(ls2));
	}

	@Test
	public final void testSimpleFrequencyTable() {
		try {
			testFreqintTable(SimpleFrequencyTable.class);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			fail("issue");
			e.printStackTrace();
		}

	}

	@Test
	public final void testIFrequencyTable() {
		try {
			testFreqintTable3(com.zoubworld.java.utils.compress.binalgo.arithemic.FrequencyTable.class);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			fail("issue");
			e.printStackTrace();
		}

	}

	@Test
	public final void testCheckedFrequencyTable() {
		try {
			testFreqintTable2(CheckedFrequencyTable.class);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			fail("issue");
			e.printStackTrace();
		}

	}

	public final void testFreqintTable(Class c) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		int t[] = { 100, 100, 10, 10, 10, 1, 1, 2, 3, 100, 101, 102 };
		Class[] cArg = new Class[1];
		cArg[0] = int[].class;
		FrequencyTable ft = (FrequencyTable) c.getDeclaredConstructor(cArg).newInstance(t);
		testFreqintTablei(ft);
	}

	public final void testFreqintTable3(Class c) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		int t[] = { 100, 100, 10, 10, 10, 1, 1, 2, 3, 100, 101, 102 };
		Class[] cArg = new Class[1];
		cArg[0] = int[].class;
		IFrequencyTable ft = (IFrequencyTable) c.getDeclaredConstructor(cArg).newInstance(t);
		testFreqintTableii(ft);
	}

	public final void testFreqintTable2(Class c) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		int t[] = { 100, 100, 10, 10, 10, 1, 1, 2, 3, 100, 101, 102 };

		FrequencyTable ft = new SimpleFrequencyTable(t);
		testFreqintTablei(new CheckedFrequencyTable(ft));
	}

	public final void testFreqintTablei(FrequencyTable ft) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		assertEquals(ft.get(1), 100);
		assertEquals(ft.get(11), 102);
		assertEquals(ft.getTotal(), 540);
		assertEquals(ft.getSymbolLimit(), 12);
		assertEquals(ft.getHigh(5), 231);
		assertEquals(ft.getLow(5), 230);
		assertEquals(ft.get(5), 1);

		assertEquals(ft.getHigh(9), 337);
		assertEquals(ft.getLow(9), 237);
		assertEquals(ft.get(9), 100);

	}

	public final void testFreqintTableii(IFrequencyTable ft) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		assertEquals(ft.get(1), 100);
		assertEquals(ft.get(11), 102);
		assertEquals(ft.getTotal(), 540);
		assertEquals(ft.getSymbolLimit(), 12);
		assertEquals(ft.getHigh(5), 231);
		assertEquals(ft.getLow(5), 230);
		assertEquals(ft.get(5), 1);

		assertEquals(ft.getHigh(9), 337);
		assertEquals(ft.getLow(9), 237);
		assertEquals(ft.get(9), 100);

	}
}
