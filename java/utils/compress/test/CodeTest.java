package com.zoubworld.java.utils.compress.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.zoubworld.java.utils.compress.Code;
import com.zoubworld.java.utils.compress.CodingSet;
import com.zoubworld.java.utils.compress.ICode;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;

public class CodeTest {

	@Test
	public void testORL() {
		
		assertEquals("101111", (Code.ORL(new Code("0011"),new Code("100011"))).toRaw());
		assertEquals("101111", (Code.ORL(new Code("100011"),new Code("0011"))).toRaw());
		assertEquals("110011", (Code.ORL(new Code("000011"),new Code("110000"))).toRaw());
		
	}
	@Test
	public void testFactoryShannonFanoEliasCode() {
		List<ISymbol> ls = Symbol.from("ABCDAAABBCDD");
		Map<ISymbol, Long> freq = Symbol.Freq(ls);
		assertEquals("001", (Code.FactoryShannonFanoEliasCode(freq,ls.get(0))).toRaw());
		assertEquals("011", (Code.FactoryShannonFanoEliasCode(freq,ls.get(1))).toRaw());
		assertEquals("1010", (Code.FactoryShannonFanoEliasCode(freq,ls.get(2))).toRaw());
		assertEquals("111", (Code.FactoryShannonFanoEliasCode(freq,ls.get(3))).toRaw());
		
	}
	@Test
	public void testFactoryRiceCode() {

		assertEquals("00000", (Code.FactoryRiceCode(4,0)).toRaw());
		assertEquals("00001", (Code.FactoryRiceCode(4,1)).toRaw());
		assertEquals("00010", (Code.FactoryRiceCode(4,2)).toRaw());
		assertEquals("00011", (Code.FactoryRiceCode(4,3)).toRaw());
		assertEquals("00100", (Code.FactoryRiceCode(4,4)).toRaw());
		assertEquals("00101", (Code.FactoryRiceCode(4,5)).toRaw());
		assertEquals("00110", (Code.FactoryRiceCode(4,6)).toRaw());
		assertEquals("00111", (Code.FactoryRiceCode(4,7)).toRaw());
		assertEquals("01000", (Code.FactoryRiceCode(4,8)).toRaw());
		assertEquals("01001", (Code.FactoryRiceCode(4,9)).toRaw());
		assertEquals("01010", (Code.FactoryRiceCode(4,10)).toRaw());
		assertEquals("01011", (Code.FactoryRiceCode(4,11)).toRaw());
		assertEquals("01100", (Code.FactoryRiceCode(4,12)).toRaw());
		assertEquals("01101", (Code.FactoryRiceCode(4,13)).toRaw());
		assertEquals("01110", (Code.FactoryRiceCode(4,14)).toRaw());
		assertEquals("01111", (Code.FactoryRiceCode(4,15)).toRaw());
		assertEquals("100000", (Code.FactoryRiceCode(4,16)).toRaw());
		assertEquals("100001", (Code.FactoryRiceCode(4,17)).toRaw());
		
		assertEquals("11111111111111110", (Code.FactoryRiceCode(1,17)).toRaw());
		assertEquals("0", (Code.FactoryRiceCode(1,0)).toRaw());
		assertEquals("10", (Code.FactoryRiceCode(1,1)).toRaw());
		assertEquals("110", (Code.FactoryRiceCode(1,2)).toRaw());
		assertEquals("1110", (Code.FactoryRiceCode(1,3)).toRaw());
		assertEquals("11110", (Code.FactoryRiceCode(1,4)).toRaw());
		assertEquals("111110", (Code.FactoryRiceCode(1,5)).toRaw());
		assertEquals("1111110", (Code.FactoryRiceCode(1,6)).toRaw());
		assertEquals("11111110", (Code.FactoryRiceCode(1,7)).toRaw());
		assertEquals("111111110", (Code.FactoryRiceCode(1,9)).toRaw());
		assertEquals("1111111110", (Code.FactoryRiceCode(1,10)).toRaw());
		assertEquals("11111111110", (Code.FactoryRiceCode(1,11)).toRaw());
		assertEquals("111111111110", (Code.FactoryRiceCode(1,12)).toRaw());
		assertEquals("1111111111110", (Code.FactoryRiceCode(1,13)).toRaw());
		assertEquals("11111111111110", (Code.FactoryRiceCode(1,14)).toRaw());
		assertEquals("111111111111110", (Code.FactoryRiceCode(1,15)).toRaw());
		assertEquals("1111111111111110", (Code.FactoryRiceCode(1,16)).toRaw());
	}
	@Test
	public void testFactoryEvenRodehCode() {
		assertEquals("000", (Code.FactoryEvenRodehCode(0)).toRaw());
		assertEquals("001", (Code.FactoryEvenRodehCode(1)).toRaw());
		assertEquals("010", (Code.FactoryEvenRodehCode(2)).toRaw());
		assertEquals("011", (Code.FactoryEvenRodehCode(3)).toRaw());
		assertEquals("1000", (Code.FactoryEvenRodehCode(4)).toRaw());
		assertEquals("1010", (Code.FactoryEvenRodehCode(5)).toRaw());
		assertEquals("1100", (Code.FactoryEvenRodehCode(6)).toRaw());
		assertEquals("1110", (Code.FactoryEvenRodehCode(7)).toRaw());
		assertEquals("10010000", (Code.FactoryEvenRodehCode(8)).toRaw());
		assertEquals("10010010", (Code.FactoryEvenRodehCode(9)).toRaw());
		assertEquals("10011110", (Code.FactoryEvenRodehCode(15)).toRaw());
		assertEquals("101100000", (Code.FactoryEvenRodehCode(16)).toRaw());
		assertEquals("10011001010110010010", (Code.FactoryEvenRodehCode(2761)).toRaw());
	}
	@Test
	public void testFactoryVariableLengthQuantityCode() {
		assertEquals("00000000", (Code.FactoryVariableLengthQuantityUnsignedCode(0)).toRaw());
		assertEquals("01010101", (Code.FactoryVariableLengthQuantityUnsignedCode(0x55)).toRaw());
		assertEquals("01110000", (Code.FactoryVariableLengthQuantityUnsignedCode(0x70)).toRaw());
		assertEquals("01111111", (Code.FactoryVariableLengthQuantityUnsignedCode(127)).toRaw());
		assertEquals("1000000100000000", (Code.FactoryVariableLengthQuantityUnsignedCode(128)).toRaw());
		assertEquals("1100000000000000", (Code.FactoryVariableLengthQuantityUnsignedCode(8192)).toRaw());
		assertEquals("1111111101111111", (Code.FactoryVariableLengthQuantityUnsignedCode(16383)).toRaw());
		assertEquals("100000011000000000000000", (Code.FactoryVariableLengthQuantityUnsignedCode(16384)).toRaw());
		assertEquals("111111111111111101111111", (Code.FactoryVariableLengthQuantityUnsignedCode(2097151)).toRaw());
		assertEquals("10000001100000001000000000000000", (Code.FactoryVariableLengthQuantityUnsignedCode(2097152)).toRaw());
		assertEquals("11000000100000001000000000000000", (Code.FactoryVariableLengthQuantityUnsignedCode(134217728)).toRaw());
		assertEquals("11111111111111111111111101111111", (Code.FactoryVariableLengthQuantityUnsignedCode(268435455)).toRaw());
		assertEquals("10000001011010101", (Code.FactoryVariableLengthQuantitySignedCode(-0x55)).toRaw());
		assertEquals("10000001011110000", (Code.FactoryVariableLengthQuantitySignedCode(-0x70)).toRaw());
			
	}
	@Test
	public void testFactoryLevenshteinCode() {
		
		assertEquals("0", (Code.FactoryLevenshteinCode(0)).toRaw());
		assertEquals("10", (Code.FactoryLevenshteinCode(1)).toRaw());
		assertEquals("1100", (Code.FactoryLevenshteinCode(2)).toRaw());
		assertEquals("1101", (Code.FactoryLevenshteinCode(3)).toRaw());
		assertEquals("1110000", (Code.FactoryLevenshteinCode(4)).toRaw());
		assertEquals("1110001", (Code.FactoryLevenshteinCode(5)).toRaw());
		assertEquals("1110010", (Code.FactoryLevenshteinCode(6)).toRaw());
		assertEquals("1110011", (Code.FactoryLevenshteinCode(7)).toRaw());
		assertEquals("11101000", (Code.FactoryLevenshteinCode(8)).toRaw());
		assertEquals("11101001", (Code.FactoryLevenshteinCode(9)).toRaw());
		assertEquals("11101010", (Code.FactoryLevenshteinCode(10)).toRaw());
		assertEquals("11101011", (Code.FactoryLevenshteinCode(11)).toRaw());
		assertEquals("11101100", (Code.FactoryLevenshteinCode(12)).toRaw());
		assertEquals("11101101", (Code.FactoryLevenshteinCode(13)).toRaw());
		assertEquals("11101110", (Code.FactoryLevenshteinCode(14)).toRaw());
		assertEquals("11101111", (Code.FactoryLevenshteinCode(15)).toRaw());
		assertEquals("111100000000", (Code.FactoryLevenshteinCode(16)).toRaw());
		assertEquals("111100000001", (Code.FactoryLevenshteinCode(17)).toRaw());
		
	}
	@Test
	public void testFactoryUnaryCode() {
		
		assertEquals("1", (Code.FactoryUnaryCode(0)).toRaw());
		assertEquals("01", (Code.FactoryUnaryCode(1)).toRaw());
		assertEquals("001", (Code.FactoryUnaryCode(2)).toRaw());
		assertEquals("0001", (Code.FactoryUnaryCode(3)).toRaw());
		assertEquals("000000000000000000000001", (Code.FactoryUnaryCode(23)).toRaw());

		assertEquals("0", (Code.FactoryUnaryCode1(0)).toRaw());
		assertEquals("10", (Code.FactoryUnaryCode1(1)).toRaw());
		assertEquals("110", (Code.FactoryUnaryCode1(2)).toRaw());
		assertEquals("1110", (Code.FactoryUnaryCode1(3)).toRaw());
		assertEquals("111111111111111111111110", (Code.FactoryUnaryCode1(23)).toRaw());
                      
	}
	@Test
	public void testFactoryEliasGammaCode() {
			
		assertEquals("1", (Code.FactoryEliasGammaCode(1)).toRaw());
		assertEquals("010", (Code.FactoryEliasGammaCode(2)).toRaw());
		assertEquals("011", (Code.FactoryEliasGammaCode(3)).toRaw());
		assertEquals("00100", (Code.FactoryEliasGammaCode(4)).toRaw());
		assertEquals("00101", (Code.FactoryEliasGammaCode(5)).toRaw());
		assertEquals("00110", (Code.FactoryEliasGammaCode(6)).toRaw());
		assertEquals("00111", (Code.FactoryEliasGammaCode(7)).toRaw());
		assertEquals("0001000", (Code.FactoryEliasGammaCode(8)).toRaw());
		assertEquals("0001001", (Code.FactoryEliasGammaCode(9)).toRaw());
		assertEquals("0001010", (Code.FactoryEliasGammaCode(10)).toRaw());
		assertEquals("0001011", (Code.FactoryEliasGammaCode(11)).toRaw());
		
		assertEquals("1", (Code.FactoryEliasDeltaCode(1)).toRaw());
		assertEquals("0100", (Code.FactoryEliasDeltaCode(2)).toRaw());
		assertEquals("0101", (Code.FactoryEliasDeltaCode(3)).toRaw());
		assertEquals("01100", (Code.FactoryEliasDeltaCode(4)).toRaw());
		assertEquals("01101", (Code.FactoryEliasDeltaCode(5)).toRaw());
		assertEquals("01110", (Code.FactoryEliasDeltaCode(6)).toRaw());
		assertEquals("01111", (Code.FactoryEliasDeltaCode(7)).toRaw());
		assertEquals("00100000", (Code.FactoryEliasDeltaCode(8)).toRaw());
		assertEquals("00100001", (Code.FactoryEliasDeltaCode(9)).toRaw());
		assertEquals("00100010", (Code.FactoryEliasDeltaCode(10)).toRaw());
		assertEquals("00100011", (Code.FactoryEliasDeltaCode(11)).toRaw());
		assertEquals("00100100", (Code.FactoryEliasDeltaCode(12)).toRaw());
		assertEquals("00100101", (Code.FactoryEliasDeltaCode(13)).toRaw());
		assertEquals("00100110", (Code.FactoryEliasDeltaCode(14)).toRaw());
		assertEquals("00100111", (Code.FactoryEliasDeltaCode(15)).toRaw());
		assertEquals("001010000", (Code.FactoryEliasDeltaCode(16)).toRaw());
		assertEquals("001010001", (Code.FactoryEliasDeltaCode(17)).toRaw());
		
		assertEquals("0", (Code.FactoryEliasOmegaCode(1)).toRaw());
		assertEquals("100", (Code.FactoryEliasOmegaCode(2)).toRaw());
		assertEquals("110", (Code.FactoryEliasOmegaCode(3)).toRaw());
		assertEquals("101000", (Code.FactoryEliasOmegaCode(4)).toRaw());
		assertEquals("101010", (Code.FactoryEliasOmegaCode(5)).toRaw());
		assertEquals("101100", (Code.FactoryEliasOmegaCode(6)).toRaw());
		assertEquals("101110", (Code.FactoryEliasOmegaCode(7)).toRaw());
		assertEquals("1110000", (Code.FactoryEliasOmegaCode(8)).toRaw());
		assertEquals("1110010", (Code.FactoryEliasOmegaCode(9)).toRaw());
		assertEquals("1110100", (Code.FactoryEliasOmegaCode(10)).toRaw());
		assertEquals("1110110", (Code.FactoryEliasOmegaCode(11)).toRaw());
		assertEquals("1111000", (Code.FactoryEliasOmegaCode(12)).toRaw());
		assertEquals("1111010", (Code.FactoryEliasOmegaCode(13)).toRaw());
		assertEquals("1111100", (Code.FactoryEliasOmegaCode(14)).toRaw());
		assertEquals("1111110", (Code.FactoryEliasOmegaCode(15)).toRaw());
		assertEquals("10100100000", (Code.FactoryEliasOmegaCode(16)).toRaw());
		assertEquals("10100100010", (Code.FactoryEliasOmegaCode(17)).toRaw());
		assertEquals("1011011001000", (Code.FactoryEliasOmegaCode(100)).toRaw());
		assertEquals("11100111111010000", (Code.FactoryEliasOmegaCode(1000)).toRaw());
		assertEquals("111101100111000100000", (Code.FactoryEliasOmegaCode(10000)).toRaw());
		assertEquals("1010010000110000110101000000", (Code.FactoryEliasOmegaCode(100000)).toRaw());
	//fail:?	assertEquals("1010010011111101000010010000000", (Code.FactoryEliasOmegaCode(100000)).toRaw());
		
		
	}
	@Test
	public void testFactoryGolombCode() {
		
		
		assertEquals("0", (Code.FactoryGolombCode(1, 0)).toRaw());
		assertEquals("10", (Code.FactoryGolombCode(1, 1)).toRaw());
		assertEquals("110", (Code.FactoryGolombCode(1, 2)).toRaw());
		assertEquals("1110", (Code.FactoryGolombCode(1, 3)).toRaw());
		assertEquals("11110", (Code.FactoryGolombCode(1, 4)).toRaw());
		assertEquals("111110", (Code.FactoryGolombCode(1, 5)).toRaw());
		assertEquals("1111110", (Code.FactoryGolombCode(1, 6)).toRaw());
		assertEquals("11111110", (Code.FactoryGolombCode(1, 7)).toRaw());
		assertEquals("111111110", (Code.FactoryGolombCode(1, 8)).toRaw());
		assertEquals("1111111110", (Code.FactoryGolombCode(1, 9)).toRaw());
		assertEquals("11111111110", (Code.FactoryGolombCode(1, 10)).toRaw());
		
		assertEquals("00", (Code.FactoryGolombCode(2, 0)).toRaw());
		assertEquals("01", (Code.FactoryGolombCode(2, 1)).toRaw());
		assertEquals("100", (Code.FactoryGolombCode(2, 2)).toRaw());
		assertEquals("101", (Code.FactoryGolombCode(2, 3)).toRaw());
		assertEquals("1100", (Code.FactoryGolombCode(2, 4)).toRaw());
		assertEquals("1101", (Code.FactoryGolombCode(2, 5)).toRaw());
		assertEquals("11100", (Code.FactoryGolombCode(2, 6)).toRaw());
		assertEquals("11101", (Code.FactoryGolombCode(2, 7)).toRaw());
		assertEquals("111100", (Code.FactoryGolombCode(2, 8)).toRaw());
		assertEquals("111101", (Code.FactoryGolombCode(2, 9)).toRaw());
		assertEquals("1111100", (Code.FactoryGolombCode(2, 10)).toRaw());
		
		assertEquals("000", (Code.FactoryGolombCode(4, 0)).toRaw());
		assertEquals("001", (Code.FactoryGolombCode(4, 1)).toRaw());
		assertEquals("010", (Code.FactoryGolombCode(4, 2)).toRaw());
		assertEquals("011", (Code.FactoryGolombCode(4, 3)).toRaw());
		assertEquals("1000", (Code.FactoryGolombCode(4, 4)).toRaw());
		assertEquals("1001", (Code.FactoryGolombCode(4, 5)).toRaw());
		assertEquals("1010", (Code.FactoryGolombCode(4, 6)).toRaw());
		assertEquals("1011", (Code.FactoryGolombCode(4, 7)).toRaw());
		assertEquals("11000", (Code.FactoryGolombCode(4, 8)).toRaw());
		assertEquals("11001", (Code.FactoryGolombCode(4, 9)).toRaw());
		assertEquals("11010", (Code.FactoryGolombCode(4, 10)).toRaw());
		
		
		assertEquals("00000", (Code.FactoryGolombCode(16, 0)).toRaw());
		assertEquals("00001", (Code.FactoryGolombCode(16, 1)).toRaw());
		assertEquals("00010", (Code.FactoryGolombCode(16, 2)).toRaw());
		assertEquals("00011", (Code.FactoryGolombCode(16, 3)).toRaw());
		assertEquals("00100", (Code.FactoryGolombCode(16, 4)).toRaw());
		assertEquals("00101", (Code.FactoryGolombCode(16, 5)).toRaw());
		assertEquals("00110", (Code.FactoryGolombCode(16, 6)).toRaw());
		assertEquals("00111", (Code.FactoryGolombCode(16, 7)).toRaw());
		assertEquals("01000", (Code.FactoryGolombCode(16, 8)).toRaw());
		assertEquals("01001", (Code.FactoryGolombCode(16, 9)).toRaw());
		assertEquals("01010", (Code.FactoryGolombCode(16, 10)).toRaw());
		
		
		assertEquals("1", (Code.FactoryExponentialGolombCode(0, 0)).toRaw());
		assertEquals("010", (Code.FactoryExponentialGolombCode(0, 1)).toRaw());
		assertEquals("011", (Code.FactoryExponentialGolombCode(0, 2)).toRaw());
		assertEquals("00100", (Code.FactoryExponentialGolombCode(0, 3)).toRaw());
		assertEquals("00101", (Code.FactoryExponentialGolombCode(0, 4)).toRaw());
		assertEquals("00110", (Code.FactoryExponentialGolombCode(0, 5)).toRaw());
		assertEquals("00111", (Code.FactoryExponentialGolombCode(0, 6)).toRaw());
		assertEquals("0001000", (Code.FactoryExponentialGolombCode(0, 7)).toRaw());
		assertEquals("0001001", (Code.FactoryExponentialGolombCode(0, 8)).toRaw());
		assertEquals("0001010", (Code.FactoryExponentialGolombCode(0, 9)).toRaw());
		assertEquals("0001011", (Code.FactoryExponentialGolombCode(0, 10)).toRaw());
		assertEquals("0001100", (Code.FactoryExponentialGolombCode(0, 11)).toRaw());
		assertEquals("0001101", (Code.FactoryExponentialGolombCode(0, 12)).toRaw());
		
		assertEquals(   "10", (Code.FactoryExponentialGolombCode(1, 0)).toRaw());
		assertEquals(   "11", (Code.FactoryExponentialGolombCode(1, 1)).toRaw());
	assertEquals(  "0100", (Code.FactoryExponentialGolombCode(1, 2)).toRaw());
		assertEquals(  "0101", (Code.FactoryExponentialGolombCode(1, 3)).toRaw());
		assertEquals(  "0110", (Code.FactoryExponentialGolombCode(1, 4)).toRaw());
		assertEquals(  "0111", (Code.FactoryExponentialGolombCode(1, 5)).toRaw());
	assertEquals( "001000", (Code.FactoryExponentialGolombCode(1, 6)).toRaw());
		assertEquals( "001001", (Code.FactoryExponentialGolombCode(1, 7)).toRaw());
		assertEquals( "001010", (Code.FactoryExponentialGolombCode(1, 8)).toRaw());
		assertEquals( "001011", (Code.FactoryExponentialGolombCode(1, 9)).toRaw());
		assertEquals( "001100", (Code.FactoryExponentialGolombCode(1, 10)).toRaw());
	assertEquals( "001101", (Code.FactoryExponentialGolombCode(1, 11)).toRaw());
		assertEquals( "001110", (Code.FactoryExponentialGolombCode(1, 12)).toRaw());
		assertEquals( "001111", (Code.FactoryExponentialGolombCode(1, 13)).toRaw());
	assertEquals("00010000", (Code.FactoryExponentialGolombCode(1, 14)).toRaw());
		
		assertEquals(  "100", (Code.FactoryExponentialGolombCode(2, 0)).toRaw());
		assertEquals(  "101", (Code.FactoryExponentialGolombCode(2, 1)).toRaw());
		assertEquals(  "110", (Code.FactoryExponentialGolombCode(2, 2)).toRaw());
		assertEquals(  "111", (Code.FactoryExponentialGolombCode(2, 3)).toRaw());
		assertEquals( "01000", (Code.FactoryExponentialGolombCode(2, 4)).toRaw());
		assertEquals( "01001", (Code.FactoryExponentialGolombCode(2, 5)).toRaw());
		assertEquals( "01010", (Code.FactoryExponentialGolombCode(2, 6)).toRaw());
		assertEquals( "01011", (Code.FactoryExponentialGolombCode(2, 7)).toRaw());
		assertEquals( "01100", (Code.FactoryExponentialGolombCode(2, 8)).toRaw());
		assertEquals( "01101", (Code.FactoryExponentialGolombCode(2, 9)).toRaw());
		assertEquals( "01110", (Code.FactoryExponentialGolombCode(2, 10)).toRaw());
		assertEquals( "01111", (Code.FactoryExponentialGolombCode(2, 11)).toRaw());
		assertEquals("0010000", (Code.FactoryExponentialGolombCode(2, 12)).toRaw());
		
		assertEquals( "1000", (Code.FactoryExponentialGolombCode(3, 0)).toRaw());
		assertEquals( "1001", (Code.FactoryExponentialGolombCode(3, 1)).toRaw());
		assertEquals( "1010", (Code.FactoryExponentialGolombCode(3, 2)).toRaw());
		assertEquals( "1011", (Code.FactoryExponentialGolombCode(3, 3)).toRaw());
		assertEquals( "1100", (Code.FactoryExponentialGolombCode(3, 4)).toRaw());
		assertEquals( "1101", (Code.FactoryExponentialGolombCode(3, 5)).toRaw());
		assertEquals( "1110", (Code.FactoryExponentialGolombCode(3, 6)).toRaw());
		assertEquals( "1111", (Code.FactoryExponentialGolombCode(3, 7)).toRaw());
		assertEquals("010000", (Code.FactoryExponentialGolombCode(3, 8)).toRaw());
		assertEquals("010001", (Code.FactoryExponentialGolombCode(3, 9)).toRaw());
		assertEquals("010010", (Code.FactoryExponentialGolombCode(3, 10)).toRaw());
		assertEquals("010011", (Code.FactoryExponentialGolombCode(3, 11)).toRaw());
		assertEquals("010100", (Code.FactoryExponentialGolombCode(3, 12)).toRaw());
	
		
		assertEquals("1", (Code.FactoryExponentialGolombCode(0,1, 0)).toRaw());
		assertEquals("010", (Code.FactoryExponentialGolombCode(0,1, 1)).toRaw());
		assertEquals("011", (Code.FactoryExponentialGolombCode(0,1, 2)).toRaw());
		assertEquals("00100", (Code.FactoryExponentialGolombCode(0,1, 3)).toRaw());
		assertEquals("00101", (Code.FactoryExponentialGolombCode(0,1, 4)).toRaw());
		assertEquals("00110", (Code.FactoryExponentialGolombCode(0,1, 5)).toRaw());
		assertEquals("00111", (Code.FactoryExponentialGolombCode(0,1, 6)).toRaw());
		assertEquals("0001000", (Code.FactoryExponentialGolombCode(0,1, 7)).toRaw());
		assertEquals("0001001", (Code.FactoryExponentialGolombCode(0,1, 8)).toRaw());
		assertEquals("0001010", (Code.FactoryExponentialGolombCode(0,1, 9)).toRaw());
		assertEquals("0001011", (Code.FactoryExponentialGolombCode(0,1, 10)).toRaw());
		assertEquals("0001100", (Code.FactoryExponentialGolombCode(0,1, 11)).toRaw());
		assertEquals("0001101", (Code.FactoryExponentialGolombCode(0,1, 12)).toRaw());
		
		assertEquals("1", (Code.FactoryExponentialGolombCode(0,4, 0)).toRaw());
		assertEquals("010000", (Code.FactoryExponentialGolombCode(0,4, 1)).toRaw());
		assertEquals("010001", (Code.FactoryExponentialGolombCode(0,4, 2)).toRaw());
		assertEquals("010010", (Code.FactoryExponentialGolombCode(0,4, 3)).toRaw());
		assertEquals("010011", (Code.FactoryExponentialGolombCode(0,4, 4)).toRaw());
		assertEquals("010100", (Code.FactoryExponentialGolombCode(0,4, 5)).toRaw());
		assertEquals("010101", (Code.FactoryExponentialGolombCode(0,4, 6)).toRaw());
		assertEquals("010110", (Code.FactoryExponentialGolombCode(0,4, 7)).toRaw());
		assertEquals("010111", (Code.FactoryExponentialGolombCode(0,4, 8)).toRaw());
		assertEquals("011000", (Code.FactoryExponentialGolombCode(0,4, 9)).toRaw());
		assertEquals("011001", (Code.FactoryExponentialGolombCode(0,4, 10)).toRaw());
		assertEquals("011111", (Code.FactoryExponentialGolombCode(0,4, 15)).toRaw());
		assertEquals("00100000000", (Code.FactoryExponentialGolombCode(0,4, 16)).toRaw());
		assertEquals("00111111111", (Code.FactoryExponentialGolombCode(0,4, 16+255)).toRaw());
		

		assertEquals( "1000", (Code.FactoryExponentialGolombCode(3,1, 0)).toRaw());
		assertEquals( "1001", (Code.FactoryExponentialGolombCode(3,1, 1)).toRaw());
		assertEquals( "1010", (Code.FactoryExponentialGolombCode(3,1, 2)).toRaw());
		assertEquals( "1011", (Code.FactoryExponentialGolombCode(3,1, 3)).toRaw());
		assertEquals( "1100", (Code.FactoryExponentialGolombCode(3,1, 4)).toRaw());
		assertEquals( "1101", (Code.FactoryExponentialGolombCode(3,1, 5)).toRaw());
		assertEquals( "1110", (Code.FactoryExponentialGolombCode(3,1, 6)).toRaw());
		assertEquals( "1111", (Code.FactoryExponentialGolombCode(3,1, 7)).toRaw());
		assertEquals("010000", (Code.FactoryExponentialGolombCode(3,1, 8)).toRaw());
		assertEquals("010001", (Code.FactoryExponentialGolombCode(3,1, 9)).toRaw());
		assertEquals("010010", (Code.FactoryExponentialGolombCode(3,1, 10)).toRaw());
		assertEquals("010011", (Code.FactoryExponentialGolombCode(3,1, 11)).toRaw());
		assertEquals("010100", (Code.FactoryExponentialGolombCode(3,1, 12)).toRaw());
		

		assertEquals( "1000", (Code.FactoryExponentialGolombCode(3,2, 0)).toRaw());
		assertEquals( "1001", (Code.FactoryExponentialGolombCode(3,2, 1)).toRaw());
		assertEquals( "1010", (Code.FactoryExponentialGolombCode(3,2, 2)).toRaw());
		assertEquals( "1011", (Code.FactoryExponentialGolombCode(3,2, 3)).toRaw());
		assertEquals( "1100", (Code.FactoryExponentialGolombCode(3,2, 4)).toRaw());
		assertEquals( "1101", (Code.FactoryExponentialGolombCode(3,2, 5)).toRaw());
		assertEquals( "1110", (Code.FactoryExponentialGolombCode(3,2, 6)).toRaw());
		assertEquals( "1111", (Code.FactoryExponentialGolombCode(3,2, 7)).toRaw());
		assertEquals("0100000", (Code.FactoryExponentialGolombCode(3,2, 8)).toRaw());
		assertEquals("0100001", (Code.FactoryExponentialGolombCode(3,2, 9)).toRaw());
		assertEquals("0100010", (Code.FactoryExponentialGolombCode(3,2, 10)).toRaw());
		assertEquals("0100011", (Code.FactoryExponentialGolombCode(3,2, 11)).toRaw());
		assertEquals("0100100", (Code.FactoryExponentialGolombCode(3,2, 12)).toRaw());
	
	}
	@Test
	public void testFactoryCode3() {
		
	
		assertEquals("00", (Code.FactoryCode3(0)).toRaw());
		assertEquals("01", (Code.FactoryCode3(1)).toRaw());
		assertEquals("10", (Code.FactoryCode3(2)).toRaw());
		assertEquals("1100", (Code.FactoryCode3(3)).toRaw());
		assertEquals("1101", (Code.FactoryCode3(4)).toRaw());
		assertEquals("1110", (Code.FactoryCode3(5)).toRaw());
		assertEquals("111100", (Code.FactoryCode3(6)).toRaw());
		assertEquals("111101", (Code.FactoryCode3(7)).toRaw());
		assertEquals("111110", (Code.FactoryCode3(8)).toRaw());
		assertEquals("11111100", (Code.FactoryCode3(9)).toRaw());
		assertEquals("11111101", (Code.FactoryCode3(10)).toRaw());
		assertEquals("1111111111111111111110", (Code.FactoryCode3(32)).toRaw());
	}
	@Test
	public void testFactoryCode255() {
		
			
		assertEquals("00000000", (Code.FactoryCode255(0)).toRaw());
		assertEquals("11111110", (Code.FactoryCode255(254)).toRaw());
		assertEquals("1111111100000000", (Code.FactoryCode255(255)).toRaw());
		assertEquals("1111111100000001", (Code.FactoryCode255(256)).toRaw());
		assertEquals("111111111111111100000011", (Code.FactoryCode255(513)).toRaw());
		assertEquals("0101", (Code.merge(Code.FactoryCode3(1),Code.FactoryCode3(1))).toRaw());
		assertEquals("11110101", (Code.merge(Code.FactoryCode3(7),Code.FactoryCode3(1))).toRaw());
		assertEquals("01111101", (Code.merge(Code.FactoryCode3(1),Code.FactoryCode3(7))).toRaw());
		assertEquals("000001", (Code.FactoryCodeN3k(1)).toRaw());
		assertEquals("000000", (Code.FactoryCodeN3k(0)).toRaw());
		assertEquals("001110", (Code.FactoryCodeN3k(14)).toRaw());
		assertEquals("001111", (Code.FactoryCodeN3k(15)).toRaw());
		assertEquals("0100000000", (Code.FactoryCodeN3k(16)).toRaw());
		assertEquals("0100000001", (Code.FactoryCodeN3k(17)).toRaw());
		assertEquals("0111111111", (Code.FactoryCodeN3k(16+255)).toRaw());
		assertEquals("10000000000000", (Code.FactoryCodeN3k(16+256)).toRaw());
		assertEquals("10111111111111", (Code.FactoryCodeN3k(16+256+4095)).toRaw());
		assertEquals("11000000000000000000", (Code.FactoryCodeN3k(16+256+4096)).toRaw());
		
	}
	@Test
	public void testFactoryFibonacciCode() {
		
	/*	assertEquals( 0, (Code.Fib(0)));
		assertEquals( 1, (Code.Fib(1)));
		assertEquals( 1, (Code.Fib(2)));
		assertEquals( 2, (Code.Fib(3)));
		assertEquals( 3, (Code.Fib(4)));
		assertEquals( 5, (Code.Fib(5)));
		assertEquals( 8, (Code.Fib(6)));
		assertEquals( 13, (Code.Fib(7)));
		assertEquals( 21, (Code.Fib(8)));
		assertEquals( 34, (Code.Fib(9)));
		assertEquals( 55, (Code.Fib(10)));
		assertEquals( 89, (Code.Fib(11)));
		assertEquals( 144, (Code.Fib(12)));
		assertEquals( 233, (Code.Fib(13)));
		assertEquals( 377, (Code.Fib(14)));
		assertEquals( 610, (Code.Fib(15)));
		assertEquals( 6765L, (Code.Fib(20)));
		
		assertEquals( 0, (Code.Fib(0)));
		assertEquals( 1, (Code.Fib(1)));
		assertEquals( 1, (Code.Fib(2)));
		assertEquals( 2, (Code.Fib(3)));
		assertEquals( 6765L, (Code.Fibonacci(20)));*/
			
		assertEquals( "11", (Code.FactoryFibonacciCode(1)).toRaw());
		assertEquals( "011", (Code.FactoryFibonacciCode(2)).toRaw());
		assertEquals( "0011", (Code.FactoryFibonacciCode(3)).toRaw());
		assertEquals( "1011", (Code.FactoryFibonacciCode(4)).toRaw());
		assertEquals( "00011", (Code.FactoryFibonacciCode(5)).toRaw());
		assertEquals( "10011", (Code.FactoryFibonacciCode(6)).toRaw());
		assertEquals( "01011", (Code.FactoryFibonacciCode(7)).toRaw());
		assertEquals( "000011", (Code.FactoryFibonacciCode(8)).toRaw());
		assertEquals( "100011", (Code.FactoryFibonacciCode(9)).toRaw());
		assertEquals( "010011", (Code.FactoryFibonacciCode(10)).toRaw());
		assertEquals( "001011", (Code.FactoryFibonacciCode(11)).toRaw());
		assertEquals( "101011", (Code.FactoryFibonacciCode(12)).toRaw());
		assertEquals( "0000011", (Code.FactoryFibonacciCode(13)).toRaw());
		assertEquals( "1000011", (Code.FactoryFibonacciCode(14)).toRaw());
		assertEquals( "0100100011", (Code.FactoryFibonacciCode(2+8+55)).toRaw());
		
	}
	
	
	@Test
	public void testDummy() {
		Code.main(null);
		assertEquals(1, 1);
	}

	@Test
	public void testToRaw() {
		Code c = new Code((char) 0x12);
		assertEquals("00010010", c.toRaw());
		c.trim();
		assertEquals("10010", c.toRaw());
		c = new Code((byte) 0x12);
		assertEquals("00010010", c.toRaw());
		
		c = new Code((char) 0x1);
		assertEquals("00000001", c.toRaw());
		c.trim();
		assertEquals("1", c.toRaw());
		c = new Code((byte) 0x80);
		assertEquals("10000000", c.toRaw());
		c.trim();
		assertEquals("10000000", c.toRaw());
		c = new Code((short) 0x280);
		assertEquals("0000001010000000", c.toRaw());

	}

	/*
	 * @Test public void testToRaw() { fail("Not yet implemented"); }
	 * 
	 * @Test public void testToString() { fail("Not yet implemented"); }
	 */
	@Test
	public void testCodeChar() {
		ICode c = new Code((char) 0x12);
		assertEquals(c.toCode().length, 1);
		assertEquals(c.toCode()[0], (char) 0x12);
		}
	@Test
	public void testToCode() {
		ICode c = new Code((byte) 0x12);
		assertEquals(c.toCode().length, 1);
		assertEquals(c.toCode()[0], (char) 0x12);
	}

	@Test
	public void testCodeShort() {
		ICode c = new Code((short) 0x1234);
		assertEquals(c.toCode().length, 2);
		assertEquals(c.toCode()[0], (char) 0x12);
		assertEquals(c.toCode()[1], (char) 0x34);

	}

	@Test
	public void testCodeInt() {
		ICode c = new Code((int) 0x12345678);
		assertEquals(c.toCode().length, 4);
		assertEquals(c.toCode()[0], (char) 0x12);
		assertEquals(c.toCode()[1], (char) 0x34);
		assertEquals(c.toCode()[2], (char) 0x56);
		assertEquals(c.toCode()[3], (char) 0x78);

	}

	@Test
	public void testCodeSet() {
		CodingSet a = new CodingSet(CodingSet.UnaryCode);
		CodingSet b = new CodingSet(CodingSet.VariCode);
		CodingSet c = new CodingSet(CodingSet.UnaryCode);
		assertEquals(a, c);
		assertNotEquals(a, b);
		assertNotEquals(a, 0L);
		assertEquals("0", a.get(Symbol.findId(0)).toRaw());
		assertEquals(
				"1010101011",
				b.get(Symbol.findId(0)).toRaw());
		assertEquals("10", a.get(Symbol.findId(1)).toRaw());
		assertEquals("1011011011", b.get(Symbol.findId(1)).toRaw());
		assertEquals("111110", a.get(Symbol.findId(5)).toRaw());
		assertEquals("1101011111", b.get(Symbol.findId(5)).toRaw());

	}

	@Test
	public void testCodeString() {
		Code c = new Code("00001101");
		Code d = new Code((byte) 0x0d);
		assertEquals(d.toRaw(), c.toRaw());
		assertEquals(d.getLong(), c.getLong());
		assertEquals(d.length(), c.length());
		assertTrue(d.compareToCode(c) == 0);

	}

	@Test
	public void testCodeLong() {
		ICode c = new Code((long) 0x123456789abcdef0L);
		assertEquals(c.toCode().length, 8);
		assertEquals(c.toCode()[0], (char) 0x12);
		assertEquals(c.toCode()[1], (char) 0x34);
		assertEquals(c.toCode()[2], (char) 0x56);
		assertEquals(c.toCode()[3], (char) 0x78);

		assertEquals(c.toCode()[4], (char) 0x9a);
		assertEquals(c.toCode()[5], (char) 0xbc);
		assertEquals(c.toCode()[6], (char) 0xde);
		assertEquals(c.toCode()[7], (char) 0xf0);

		c = new Code((long) 0x123456789abcdef0L, 64);
		assertEquals(c.toCode().length, 8);
		assertEquals(c.toCode()[0], (char) 0x12);
		assertEquals(c.toCode()[1], (char) 0x34);
		assertEquals(c.toCode()[2], (char) 0x56);
		assertEquals(c.toCode()[3], (char) 0x78);

		assertEquals(c.toCode()[4], (char) 0x9a);
		assertEquals(c.toCode()[5], (char) 0xbc);
		assertEquals(c.toCode()[6], (char) 0xde);
		assertEquals(c.toCode()[7], (char) 0xf0);

		c = new Code((long) 0x1L, 1);
		assertEquals(c.toCode().length, 1);
		assertEquals(c.toCode()[0], (char) 0x80);
		c = new Code((long) 0x71L, 8);
		assertEquals(c.toCode().length, 1);
		assertEquals(c.toCode()[0], (char) 0x71);

		c = new Code((long) 0x71L, 7);
		assertEquals(c.toCode().length, 1);
		assertEquals(c.toCode()[0], (char) 0x71 * 2);
		c = new Code((long) 0x177L, 9);
		assertEquals(c.toCode().length, 2);

	}

	@Test
	public void testTrim() {

		Code c = new Code((long) 0x082345678L);
		c.trim();
		assertEquals(c.length(), 4 * 8);
		assertEquals(c.toCode()[0], (char) 0x82);
		assertEquals(c.toCode()[1], (char) 0x34);
		assertEquals(c.toCode()[2], (char) 0x56);
		assertEquals(c.toCode()[3], (char) 0x78);

		Code a = new Code((char) 0x01);
		a.trim();
		assertEquals(a.length(), 1);
		assertEquals(a.toCode()[0], (char) 0x80);

		Code b = new Code((short) 0x0123);// 0000 0001 0010 0011
		b.trim();/// 1 0010 0011 = 9 1 8
		assertEquals(b.length(), 9);
		assertEquals(b.toCode()[0], (char) 0x91);
		assertEquals(b.toCode()[1] & 0x80, (char) 0x80);// 1 bit used

		Code d = new Code();

		d.huffmanAddBit('0');
		d.huffmanAddBit('1');
		d.huffmanAddBit('1');
		assertEquals(d.length(), 3);
		assertEquals(d.getLong().longValue(), 3);
		d.trim();

		assertEquals(d.length(), 2);
		assertEquals(d.getLong().longValue(), 3);

		Code de = new Code();

		de.huffmanAddBit('0');
		de.huffmanAddBit('0');
		de.huffmanAddBit('1');
		de.huffmanAddBit('1');
		de.huffmanAddBit('0');
		de.huffmanAddBit('0');
		de.huffmanAddBit('1');
		assertEquals(de.length(), 7);
		assertEquals(de.getLong().longValue(), 0x19);
		de.trim();

		assertEquals(de.length(), 5);
		assertEquals(de.getLong().longValue(), 0x19);

	}

	@Test
	public void testCompareToInt() {

		Long A = 0x123456789abcdef0L;
		Long B = 0x123456789abcdefL;

		Code a = new Code((long) A);
		Code b = new Code((long) B);
		a.trim();
		b.trim();
		assertTrue(A.compareTo(B) > 0);// a>b
		assertTrue(a.compareToInt(b) > 0);// a>b
	}

	@Test
	public void testCompareToCode() {
		Code a = new Code((long) 0x123456789abcdef0L);
		Code b = new Code((long) 0x123456789abcdefL);
		assertTrue(a.compareToCode(b) > 0);// a>b
		Code c = new Code((long) 0x123456789abcdefL);
		c.trim();
		assertTrue(c.compareToCode(b) > 0);// a>b

		Code d = new Code();
		Code e = new Code();
		d.huffmanAddBit('1');

		e.huffmanAddBit('1');
		assertTrue(d.compareToCode(e) == 0);// a>b
		e.huffmanAddBit('0');
		assertTrue(d.compareToCode(e) < 0);// a>b

		d = new Code();
		e = new Code();
		d.huffmanAddBit('0');
		d.huffmanAddBit('1');
		d.huffmanAddBit('1');

		e.huffmanAddBit('1');
		assertTrue(d.compareToCode(e) < 0);// a>b

	}

	@Test
	public void testGetLong() {
		long D = (long) 1234567890123L;// 0x11F71FB04CB
		Code d = new Code(D);

		assertTrue(d.getLong() == D);
		assertTrue(d.length() == 64);
		d.trim();
		assertTrue(d.getLong() == D);
		assertTrue(d.length() == 41);
		Code a = new Code();

		assertTrue(a.getLong() == 0);
		a.huffmanAddBit('1');
		a.huffmanAddBit('0');

		assertTrue(a.getLong() == 2);
		assertTrue(a.length() == 2);

		Code c = new Code((char) 0x12);
		assertEquals(0x12, c.getLong().longValue());
		c.trim();
		assertEquals(0x12, c.getLong().longValue());

		c = new Code((char) 0x1);
		assertEquals(0x1, c.getLong().longValue());
		c.trim();
		assertEquals(1, c.getLong().longValue());
		c = new Code((char) 0x80);
		assertEquals(0x80, c.getLong().longValue());
		c.trim();
		assertEquals(0x80, c.getLong().longValue());
		c = new Code((short) 0x280);
		assertEquals(0x280, c.getLong().longValue());
	}
	/*
	 * @Test void testGetMsb() { fail("Not yet implemented"); }
	 * 
	 * @Test void testCompareToCode() { fail("Not yet implemented"); }
	 */

}
