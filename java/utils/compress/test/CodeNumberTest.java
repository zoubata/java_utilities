package com.zoubworld.java.utils.compress.test;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import com.zoubworld.java.utils.compress.CodeNumber;
import com.zoubworld.java.utils.compress.CodeNumberSet;
import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Number;
import com.zoubworld.java.utils.compress.file.BinaryFinFout;
import com.zoubworld.java.utils.compress.file.IBinaryStream;
import com.zoubworld.java.utils.compress.file.IBinaryWriter;

public class CodeNumberTest {

	
	@Test
	public void testGetExpGolomb0Code() {
		assertEquals("1",CodeNumber.getExpGolomb0Code(0).toRaw());
		assertEquals("010",CodeNumber.getExpGolomb0Code(1).toRaw());
		assertEquals("011",CodeNumber.getExpGolomb0Code(2).toRaw());
		assertEquals("00100",CodeNumber.getExpGolomb0Code(3).toRaw());
		assertEquals("00101",CodeNumber.getExpGolomb0Code(4).toRaw());
		assertEquals("00110",CodeNumber.getExpGolomb0Code(5).toRaw());
		assertEquals("00111",CodeNumber.getExpGolomb0Code(6).toRaw());
		assertEquals("0001000",CodeNumber.getExpGolomb0Code(7).toRaw());
		assertEquals("0001001",CodeNumber.getExpGolomb0Code(8).toRaw());
		assertEquals("0001010",CodeNumber.getExpGolomb0Code(9).toRaw());
		assertEquals("0001011",CodeNumber.getExpGolomb0Code(10).toRaw());
		assertEquals("0001100",CodeNumber.getExpGolomb0Code(11).toRaw());
		assertEquals("0001101",CodeNumber.getExpGolomb0Code(12).toRaw());
		assertEquals("0001110",CodeNumber.getExpGolomb0Code(13).toRaw());
		assertEquals("0001111",CodeNumber.getExpGolomb0Code(14).toRaw());
		assertEquals("000010000",CodeNumber.getExpGolomb0Code(15).toRaw());
		assertEquals("000010001",CodeNumber.getExpGolomb0Code(16).toRaw());
		assertEquals("000010010",CodeNumber.getExpGolomb0Code(17).toRaw());
		assertEquals("000010011",CodeNumber.getExpGolomb0Code(18).toRaw());
		assertEquals("000010100",CodeNumber.getExpGolomb0Code(19).toRaw());
		assertEquals("000010101",CodeNumber.getExpGolomb0Code(20).toRaw());
		assertEquals("000010110",CodeNumber.getExpGolomb0Code(21).toRaw());
		assertEquals("000010111",CodeNumber.getExpGolomb0Code(22).toRaw());
		assertEquals("000011000",CodeNumber.getExpGolomb0Code(23).toRaw());
		assertEquals("000011001",CodeNumber.getExpGolomb0Code(24).toRaw());
		assertEquals("000011010",CodeNumber.getExpGolomb0Code(25).toRaw());
		assertEquals("000011011",CodeNumber.getExpGolomb0Code(26).toRaw());
		assertEquals("000011100",CodeNumber.getExpGolomb0Code(27).toRaw());
		assertEquals("000011101",CodeNumber.getExpGolomb0Code(28).toRaw());
		assertEquals("000011110",CodeNumber.getExpGolomb0Code(29).toRaw());

	}

	@Test
	public void testReadExpGolomb0Code() {
		BinaryFinFout b=new BinaryFinFout();
		for(int i=0;i<16;i++)
			b.write(CodeNumber.getExpGolomb0Code(i));
		b.flush();
		for(Long i=0L;i<16;i++)
		assertEquals(i,CodeNumber.readExpGolomb0Code(b));
	}

	@Test
	public void testGetBinaryCode() {
		assertEquals("10000000",CodeNumber.getBinaryCode(256,128).toRaw());
		assertEquals("10000000",CodeNumber.getBinaryCode(129,128).toRaw());
		assertEquals("0001",CodeNumber.getBinaryCode(16,1).toRaw());
		assertEquals("1111",CodeNumber.getBinaryCode(16,15).toRaw());
		assertEquals("1000",CodeNumber.getBinaryCode(11,8).toRaw());		
	}
	@Test
	public void testGetTruncatedBinaryCode() {
		
		assertEquals("00",CodeNumber.getTruncatedBinaryCode(5,0).toRaw());
		assertEquals("01",CodeNumber.getTruncatedBinaryCode(5,1).toRaw());
		assertEquals("10",CodeNumber.getTruncatedBinaryCode(5,2).toRaw());
		assertEquals("110",CodeNumber.getTruncatedBinaryCode(5,3).toRaw());
		assertEquals("111",CodeNumber.getTruncatedBinaryCode(5,4).toRaw());
		assertEquals(null,CodeNumber.getTruncatedBinaryCode(5,5));
		assertEquals(null,CodeNumber.getTruncatedBinaryCode(5,-1));
		
		assertEquals("00",CodeNumber.getTruncatedBinaryCode(7,0).toRaw());
		assertEquals("010",CodeNumber.getTruncatedBinaryCode(7,1).toRaw());
		assertEquals("011",CodeNumber.getTruncatedBinaryCode(7,2).toRaw());
		assertEquals("100",CodeNumber.getTruncatedBinaryCode(7,3).toRaw());
		assertEquals("101",CodeNumber.getTruncatedBinaryCode(7,4).toRaw());
		assertEquals("110",CodeNumber.getTruncatedBinaryCode(7,5).toRaw());
		assertEquals("111",CodeNumber.getTruncatedBinaryCode(7,6).toRaw());
		
		
		assertEquals("000",CodeNumber.getTruncatedBinaryCode(10,0).toRaw());
		assertEquals("001",CodeNumber.getTruncatedBinaryCode(10,1).toRaw());
		assertEquals("010",CodeNumber.getTruncatedBinaryCode(10,2).toRaw());
		assertEquals("011",CodeNumber.getTruncatedBinaryCode(10,3).toRaw());
		assertEquals("100",CodeNumber.getTruncatedBinaryCode(10,4).toRaw());
		assertEquals("101",CodeNumber.getTruncatedBinaryCode(10,5).toRaw());
		assertEquals("1100",CodeNumber.getTruncatedBinaryCode(10,6).toRaw());
		assertEquals("1101",CodeNumber.getTruncatedBinaryCode(10,7).toRaw());
		assertEquals("1110",CodeNumber.getTruncatedBinaryCode(10,8).toRaw());
		assertEquals("1111",CodeNumber.getTruncatedBinaryCode(10,9).toRaw());
		assertEquals(null,CodeNumber.getTruncatedBinaryCode(10,10));
		
		
		assertEquals("10000000",CodeNumber.getTruncatedBinaryCode(256,128).toRaw());
		assertEquals("11111111",CodeNumber.getTruncatedBinaryCode(129,128).toRaw());
		assertEquals("0001",CodeNumber.getTruncatedBinaryCode(16,1).toRaw());
		assertEquals("1111",CodeNumber.getTruncatedBinaryCode(16,15).toRaw());
		assertEquals("1000",CodeNumber.getTruncatedBinaryCode(16,8).toRaw());
		assertEquals("1101",CodeNumber.getTruncatedBinaryCode(11,8).toRaw());		
	}
		@Test
		public void testGetExpGolombkCode() {
			assertEquals("1",CodeNumber.getExpGolombkCode(0,0).toRaw());
			assertEquals("010",CodeNumber.getExpGolombkCode(0,1).toRaw());
			assertEquals("011",CodeNumber.getExpGolombkCode(0,2).toRaw());
			assertEquals("00100",CodeNumber.getExpGolombkCode(0,3).toRaw());
			assertEquals("00101",CodeNumber.getExpGolombkCode(0,4).toRaw());
			assertEquals("00110",CodeNumber.getExpGolombkCode(0,5).toRaw());
		assertEquals("00111",CodeNumber.getExpGolombkCode(0,6).toRaw());
		assertEquals("0001000",CodeNumber.getExpGolombkCode(0,7).toRaw());
		assertEquals("0001001",CodeNumber.getExpGolombkCode(0,8).toRaw());
		assertEquals("0001010",CodeNumber.getExpGolombkCode(0,9).toRaw());
		assertEquals("0001011",CodeNumber.getExpGolombkCode(0,10).toRaw());
		assertEquals("0001100",CodeNumber.getExpGolombkCode(0,11).toRaw());
		assertEquals("0001101",CodeNumber.getExpGolombkCode(0,12).toRaw());
		assertEquals("0001110",CodeNumber.getExpGolombkCode(0,13).toRaw());
		assertEquals("0001111",CodeNumber.getExpGolombkCode(0,14).toRaw());
		assertEquals("000010000",CodeNumber.getExpGolombkCode(0,15).toRaw());
		assertEquals("000010001",CodeNumber.getExpGolombkCode(0,16).toRaw());
		assertEquals("000010010",CodeNumber.getExpGolombkCode(0,17).toRaw());
		assertEquals("000010011",CodeNumber.getExpGolombkCode(0,18).toRaw());
		assertEquals("000010100",CodeNumber.getExpGolombkCode(0,19).toRaw());
		assertEquals("000010101",CodeNumber.getExpGolombkCode(0,20).toRaw());
		assertEquals("000010110",CodeNumber.getExpGolombkCode(0,21).toRaw());
		assertEquals("000010111",CodeNumber.getExpGolombkCode(0,22).toRaw());
		assertEquals("000011000",CodeNumber.getExpGolombkCode(0,23).toRaw());
		assertEquals("000011001",CodeNumber.getExpGolombkCode(0,24).toRaw());
		assertEquals("000011010",CodeNumber.getExpGolombkCode(0,25).toRaw());
		assertEquals("000011011",CodeNumber.getExpGolombkCode(0,26).toRaw());
		assertEquals("000011100",CodeNumber.getExpGolombkCode(0,27).toRaw());
		assertEquals("000011101",CodeNumber.getExpGolombkCode(0,28).toRaw());
		assertEquals("000011110",CodeNumber.getExpGolombkCode(0,29).toRaw());

		assertEquals("10",CodeNumber.getExpGolombkCode(1,0).toRaw());
		assertEquals("11",CodeNumber.getExpGolombkCode(1,1).toRaw());
		assertEquals("0100",CodeNumber.getExpGolombkCode(1,2).toRaw());
		assertEquals("0101",CodeNumber.getExpGolombkCode(1,3).toRaw());
		assertEquals("0110",CodeNumber.getExpGolombkCode(1,4).toRaw());
		assertEquals("0111",CodeNumber.getExpGolombkCode(1,5).toRaw());
		assertEquals("001000",CodeNumber.getExpGolombkCode(1,6).toRaw());
		assertEquals("001001",CodeNumber.getExpGolombkCode(1,7).toRaw());
		assertEquals("001010",CodeNumber.getExpGolombkCode(1,8).toRaw());
		assertEquals("001011",CodeNumber.getExpGolombkCode(1,9).toRaw());
		assertEquals("001100",CodeNumber.getExpGolombkCode(1,10).toRaw());
		assertEquals("001101",CodeNumber.getExpGolombkCode(1,11).toRaw());
		assertEquals("001110",CodeNumber.getExpGolombkCode(1,12).toRaw());
		assertEquals("001111",CodeNumber.getExpGolombkCode(1,13).toRaw());
		assertEquals("00010000",CodeNumber.getExpGolombkCode(1,14).toRaw());
		assertEquals("00010001",CodeNumber.getExpGolombkCode(1,15).toRaw());
		assertEquals("00010010",CodeNumber.getExpGolombkCode(1,16).toRaw());
		assertEquals("00010011",CodeNumber.getExpGolombkCode(1,17).toRaw());
		assertEquals("00010100",CodeNumber.getExpGolombkCode(1,18).toRaw());
		assertEquals("00010101",CodeNumber.getExpGolombkCode(1,19).toRaw());
		assertEquals("00010110",CodeNumber.getExpGolombkCode(1,20).toRaw());
		assertEquals("00010111",CodeNumber.getExpGolombkCode(1,21).toRaw());
		assertEquals("00011000",CodeNumber.getExpGolombkCode(1,22).toRaw());
		assertEquals("00011001",CodeNumber.getExpGolombkCode(1,23).toRaw());
		assertEquals("00011010",CodeNumber.getExpGolombkCode(1,24).toRaw());
		assertEquals("00011011",CodeNumber.getExpGolombkCode(1,25).toRaw());
		assertEquals("00011100",CodeNumber.getExpGolombkCode(1,26).toRaw());
		assertEquals("00011101",CodeNumber.getExpGolombkCode(1,27).toRaw());
		assertEquals("00011110",CodeNumber.getExpGolombkCode(1,28).toRaw());
		assertEquals("00011111",CodeNumber.getExpGolombkCode(1,29).toRaw());

		assertEquals("100",CodeNumber.getExpGolombkCode(2,0).toRaw());
		assertEquals("101",CodeNumber.getExpGolombkCode(2,1).toRaw());
		assertEquals("110",CodeNumber.getExpGolombkCode(2,2).toRaw());
		assertEquals("111",CodeNumber.getExpGolombkCode(2,3).toRaw());
		assertEquals("01000",CodeNumber.getExpGolombkCode(2,4).toRaw());
		assertEquals("01001",CodeNumber.getExpGolombkCode(2,5).toRaw());
		assertEquals("01010",CodeNumber.getExpGolombkCode(2,6).toRaw());
		assertEquals("01011",CodeNumber.getExpGolombkCode(2,7).toRaw());
		assertEquals("01100",CodeNumber.getExpGolombkCode(2,8).toRaw());
		assertEquals("01101",CodeNumber.getExpGolombkCode(2,9).toRaw());
		assertEquals("01110",CodeNumber.getExpGolombkCode(2,10).toRaw());
		assertEquals("01111",CodeNumber.getExpGolombkCode(2,11).toRaw());
		assertEquals("0010000",CodeNumber.getExpGolombkCode(2,12).toRaw());
		assertEquals("0010001",CodeNumber.getExpGolombkCode(2,13).toRaw());
		assertEquals("0010010",CodeNumber.getExpGolombkCode(2,14).toRaw());
		assertEquals("0010011",CodeNumber.getExpGolombkCode(2,15).toRaw());
		assertEquals("0010100",CodeNumber.getExpGolombkCode(2,16).toRaw());
		assertEquals("0010101",CodeNumber.getExpGolombkCode(2,17).toRaw());
		assertEquals("0010110",CodeNumber.getExpGolombkCode(2,18).toRaw());
		assertEquals("0010111",CodeNumber.getExpGolombkCode(2,19).toRaw());
		assertEquals("0011000",CodeNumber.getExpGolombkCode(2,20).toRaw());
		assertEquals("0011001",CodeNumber.getExpGolombkCode(2,21).toRaw());
		assertEquals("0011010",CodeNumber.getExpGolombkCode(2,22).toRaw());
		assertEquals("0011011",CodeNumber.getExpGolombkCode(2,23).toRaw());
		assertEquals("0011100",CodeNumber.getExpGolombkCode(2,24).toRaw());
		assertEquals("0011101",CodeNumber.getExpGolombkCode(2,25).toRaw());
		assertEquals("0011110",CodeNumber.getExpGolombkCode(2,26).toRaw());
		assertEquals("0011111",CodeNumber.getExpGolombkCode(2,27).toRaw());
		assertEquals("000100000",CodeNumber.getExpGolombkCode(2,28).toRaw());
		assertEquals("000100001",CodeNumber.getExpGolombkCode(2,29).toRaw());

		assertEquals("1000",CodeNumber.getExpGolombkCode(3,0).toRaw());
		assertEquals("1001",CodeNumber.getExpGolombkCode(3,1).toRaw());
		assertEquals("1010",CodeNumber.getExpGolombkCode(3,2).toRaw());
		assertEquals("1011",CodeNumber.getExpGolombkCode(3,3).toRaw());
		assertEquals("1100",CodeNumber.getExpGolombkCode(3,4).toRaw());
		assertEquals("1101",CodeNumber.getExpGolombkCode(3,5).toRaw());
		assertEquals("1110",CodeNumber.getExpGolombkCode(3,6).toRaw());
		assertEquals("1111",CodeNumber.getExpGolombkCode(3,7).toRaw());
		assertEquals("010000",CodeNumber.getExpGolombkCode(3,8).toRaw());
		assertEquals("010001",CodeNumber.getExpGolombkCode(3,9).toRaw());
		assertEquals("010010",CodeNumber.getExpGolombkCode(3,10).toRaw());
		assertEquals("010011",CodeNumber.getExpGolombkCode(3,11).toRaw());
		assertEquals("010100",CodeNumber.getExpGolombkCode(3,12).toRaw());
		assertEquals("010101",CodeNumber.getExpGolombkCode(3,13).toRaw());
		assertEquals("010110",CodeNumber.getExpGolombkCode(3,14).toRaw());
		assertEquals("010111",CodeNumber.getExpGolombkCode(3,15).toRaw());
		assertEquals("011000",CodeNumber.getExpGolombkCode(3,16).toRaw());
		assertEquals("011001",CodeNumber.getExpGolombkCode(3,17).toRaw());
		assertEquals("011010",CodeNumber.getExpGolombkCode(3,18).toRaw());
		assertEquals("011011",CodeNumber.getExpGolombkCode(3,19).toRaw());
		assertEquals("011100",CodeNumber.getExpGolombkCode(3,20).toRaw());
		assertEquals("011101",CodeNumber.getExpGolombkCode(3,21).toRaw());
		assertEquals("011110",CodeNumber.getExpGolombkCode(3,22).toRaw());
		assertEquals("011111",CodeNumber.getExpGolombkCode(3,23).toRaw());
		assertEquals("00100000",CodeNumber.getExpGolombkCode(3,24).toRaw());
		assertEquals("00100001",CodeNumber.getExpGolombkCode(3,25).toRaw());
		assertEquals("00100010",CodeNumber.getExpGolombkCode(3,26).toRaw());
		assertEquals("00100011",CodeNumber.getExpGolombkCode(3,27).toRaw());
		assertEquals("00100100",CodeNumber.getExpGolombkCode(3,28).toRaw());
		assertEquals("00100101",CodeNumber.getExpGolombkCode(3,29).toRaw());
		
		
	}

	@Test
	public void testReadExpGolombkCode() {
		for(int k=0;k<4;k++)
		{
			BinaryFinFout b=new BinaryFinFout();
			for(int i=0;i<16;i++)
				b.write(CodeNumber.getExpGolombkCode(k,i));
			b.flush();
			for(Long i=0L;i<16;i++)
			assertEquals(i,CodeNumber.readExpGolombkCode(k,b));
		}
	}

	@Test
	public void testGetUnaryCode() {
		assertEquals("0",CodeNumber.getUnaryCode(0).toRaw());
		assertEquals("10",CodeNumber.getUnaryCode(1).toRaw());
		assertEquals("110",CodeNumber.getUnaryCode(2).toRaw());
		assertEquals("1110",CodeNumber.getUnaryCode(3).toRaw());
		assertEquals("11110",CodeNumber.getUnaryCode(4).toRaw());
		assertEquals("111110",CodeNumber.getUnaryCode(5).toRaw());
		assertEquals("1111110",CodeNumber.getUnaryCode(6).toRaw());
		assertEquals("11111110",CodeNumber.getUnaryCode(7).toRaw());
		assertEquals("111111110",CodeNumber.getUnaryCode(8).toRaw());
		assertEquals("1111111110",CodeNumber.getUnaryCode(9).toRaw());
		assertEquals("11111111110",CodeNumber.getUnaryCode(10).toRaw());
		assertEquals("111111111110",CodeNumber.getUnaryCode(11).toRaw());
		assertEquals("1111111111110",CodeNumber.getUnaryCode(12).toRaw());
		assertEquals("11111111111110",CodeNumber.getUnaryCode(13).toRaw());
		assertEquals("111111111111110",CodeNumber.getUnaryCode(14).toRaw());
		assertEquals("1111111111111110",CodeNumber.getUnaryCode(15).toRaw());		
	}

	@Test
	public void testReadUnaryCode() {
		BinaryFinFout b=new BinaryFinFout();
		for(int i=0;i<16;i++)
			b.write(CodeNumber.getUnaryCode(i));
		b.flush();
		for(Long i=0L;i<16;i++)
		assertEquals(i,CodeNumber.readUnaryCode(b));
	}

	@Test
	public void testGetPhaseInCode() {
		assertEquals("00",CodeNumber.getPhaseInCode(5,0).toRaw());
		assertEquals("01",CodeNumber.getPhaseInCode(5,1).toRaw());
		assertEquals("10",CodeNumber.getPhaseInCode(5,2).toRaw());
		assertEquals("110",CodeNumber.getPhaseInCode(5,3).toRaw());
		assertEquals("111",CodeNumber.getPhaseInCode(5,4).toRaw());
		
		assertEquals("00",CodeNumber.getPhaseInCode(7,0).toRaw());
		assertEquals("010",CodeNumber.getPhaseInCode(7,1).toRaw());
		assertEquals("011",CodeNumber.getPhaseInCode(7,2).toRaw());
		assertEquals("100",CodeNumber.getPhaseInCode(7,3).toRaw());
		assertEquals("101",CodeNumber.getPhaseInCode(7,4).toRaw());
		assertEquals("110",CodeNumber.getPhaseInCode(7,5).toRaw());
		assertEquals("111",CodeNumber.getPhaseInCode(7,6).toRaw());
		
		
	}
	
	@Test
	public void testGetZetaxCode() {
		assertEquals("0",CodeNumber.getZetaCode(1,1).toRaw());
		assertEquals("100",CodeNumber.getZetaCode(1,2).toRaw());
		assertEquals("101",CodeNumber.getZetaCode(1,3).toRaw());
		assertEquals("11000",CodeNumber.getZetaCode(1,4).toRaw());
		assertEquals("11001",CodeNumber.getZetaCode(1,5).toRaw());
		assertEquals("11010",CodeNumber.getZetaCode(1,6).toRaw());
		assertEquals("11011",CodeNumber.getZetaCode(1,7).toRaw());
		assertEquals("1110000",CodeNumber.getZetaCode(1,8).toRaw());
		
		assertEquals("00",CodeNumber.getZetaCode(2,1).toRaw());
		assertEquals("010",CodeNumber.getZetaCode(2,2).toRaw());
		assertEquals("011",CodeNumber.getZetaCode(2,3).toRaw());
		assertEquals("10000",CodeNumber.getZetaCode(2,4).toRaw());
		assertEquals("10001",CodeNumber.getZetaCode(2,5).toRaw());
		assertEquals("10010",CodeNumber.getZetaCode(2,6).toRaw());
		assertEquals("10011",CodeNumber.getZetaCode(2,7).toRaw());
		assertEquals("101000",CodeNumber.getZetaCode(2,8).toRaw());
		
		assertEquals("000",CodeNumber.getZetaCode(3,1).toRaw());
		assertEquals("0010",CodeNumber.getZetaCode(3,2).toRaw());
		assertEquals("0011",CodeNumber.getZetaCode(3,3).toRaw());
		assertEquals("0100",CodeNumber.getZetaCode(3,4).toRaw());
		assertEquals("0101",CodeNumber.getZetaCode(3,5).toRaw());
		assertEquals("0110",CodeNumber.getZetaCode(3,6).toRaw());
		assertEquals("0111",CodeNumber.getZetaCode(3,7).toRaw());
		assertEquals("1000000",CodeNumber.getZetaCode(3,8).toRaw());
		
		assertEquals("0000",CodeNumber.getZetaCode(4,1).toRaw());
		assertEquals("00010",CodeNumber.getZetaCode(4,2).toRaw());
		assertEquals("00011",CodeNumber.getZetaCode(4,3).toRaw());
		assertEquals("00100",CodeNumber.getZetaCode(4,4).toRaw());
		assertEquals("00101",CodeNumber.getZetaCode(4,5).toRaw());
		assertEquals("00110",CodeNumber.getZetaCode(4,6).toRaw());
		assertEquals("00111",CodeNumber.getZetaCode(4,7).toRaw());
		assertEquals("01000",CodeNumber.getZetaCode(4,8).toRaw());
		
		
		
	}
	@Test
	public void testReadPhaseInCode() {
		int k=5;
		BinaryFinFout b=new BinaryFinFout();
		for(int i=0;i<k;i++)
			b.write(CodeNumber.getPhaseInCode(k,i));
		b.flush();
		for(Long i=0L;i<k;i++)
		assertEquals(i,CodeNumber.readPhaseInCode(k,b));
		k=7;
		b=new BinaryFinFout();
		for(int i=0;i<k;i++)
			b.write(CodeNumber.getPhaseInCode(k,i));
		b.flush();
		for(Long i=0L;i<k;i++)
		assertEquals(i,CodeNumber.readPhaseInCode(k,b));
		
		
		b=new BinaryFinFout();
		for( k=2;k<16;k++)
			for(int i=0;i<k;i++)
			b.write(CodeNumber.getPhaseInCode(k,i));
		b.flush();
		for( k=2;k<16;k++)
			for(Long i=0L;i<k;i++)
		assertEquals(i,CodeNumber.readPhaseInCode(k,b));
	}

	@Test
	public void testGetGammaCode() {
		assertEquals(null,CodeNumber.getGammaCode(0));
		assertEquals("1",CodeNumber.getGammaCode(1).toRaw());
		assertEquals("010",CodeNumber.getGammaCode(2).toRaw());
		assertEquals("011",CodeNumber.getGammaCode(3).toRaw());
		assertEquals("00100",CodeNumber.getGammaCode(4).toRaw());
		assertEquals("00101",CodeNumber.getGammaCode(5).toRaw());
		assertEquals("00110",CodeNumber.getGammaCode(6).toRaw());
		assertEquals("00111",CodeNumber.getGammaCode(7).toRaw());
		assertEquals("0001000",CodeNumber.getGammaCode(8).toRaw());
		assertEquals("0001001",CodeNumber.getGammaCode(9).toRaw());
		assertEquals("0001010",CodeNumber.getGammaCode(10).toRaw());
		assertEquals("0001011",CodeNumber.getGammaCode(11).toRaw());
		assertEquals("0001100",CodeNumber.getGammaCode(12).toRaw());
		assertEquals("0001101",CodeNumber.getGammaCode(13).toRaw());
		assertEquals("0001110",CodeNumber.getGammaCode(14).toRaw());
		assertEquals("0001111",CodeNumber.getGammaCode(15).toRaw());
		assertEquals("000010000",CodeNumber.getGammaCode(16).toRaw());
		assertEquals("000010001",CodeNumber.getGammaCode(17).toRaw());
		
	}

	@Test
	public void testReadGammaCode() {
	
		BinaryFinFout b=new BinaryFinFout();
		for(int i=1;i<16;i++)
			b.write(CodeNumber.getGammaCode(i));
		b.flush();
		for(Long i=1L;i<16;i++)
		assertEquals(i,CodeNumber.readGammaCode(b));
	}

	@Test
	public void testGetDeltaCode() {
		assertEquals(null,CodeNumber.getDeltaCode(0));
		assertEquals("1",CodeNumber.getDeltaCode(1).toRaw());
		assertEquals("0100",CodeNumber.getDeltaCode(2).toRaw());
		assertEquals("0101",CodeNumber.getDeltaCode(3).toRaw());
		assertEquals("01100",CodeNumber.getDeltaCode(4).toRaw());
		assertEquals("01101",CodeNumber.getDeltaCode(5).toRaw());
		assertEquals("01110",CodeNumber.getDeltaCode(6).toRaw());
		assertEquals("01111",CodeNumber.getDeltaCode(7).toRaw());
		assertEquals("00100000",CodeNumber.getDeltaCode(8).toRaw());
		assertEquals("00100001",CodeNumber.getDeltaCode(9).toRaw());
		assertEquals("00100010",CodeNumber.getDeltaCode(10).toRaw());
		assertEquals("00100011",CodeNumber.getDeltaCode(11).toRaw());
		assertEquals("00100100",CodeNumber.getDeltaCode(12).toRaw());
		assertEquals("00100101",CodeNumber.getDeltaCode(13).toRaw());
		assertEquals("00100110",CodeNumber.getDeltaCode(14).toRaw());
		assertEquals("00100111",CodeNumber.getDeltaCode(15).toRaw());
		assertEquals("001010000",CodeNumber.getDeltaCode(16).toRaw());
		assertEquals("001010001",CodeNumber.getDeltaCode(17).toRaw());
		
	}

	@Test
	public void testReadDeltaCode() {
		BinaryFinFout b=new BinaryFinFout();
		for(int i=1;i<16;i++)
			b.write(CodeNumber.getDeltaCode(i));
		b.flush();
		for(Long i=1L;i<16;i++)
		assertEquals(i,CodeNumber.readDeltaCode(b));
	}

	@Test
	public void testGetOmegaCode() {

		assertEquals(null,CodeNumber.getOmegaCode(0));
		assertEquals("0",CodeNumber.getOmegaCode(1).toRaw());
		assertEquals("100",CodeNumber.getOmegaCode(2).toRaw());
		assertEquals("110",CodeNumber.getOmegaCode(3).toRaw());
		assertEquals("101000",CodeNumber.getOmegaCode(4).toRaw());
		assertEquals("101010",CodeNumber.getOmegaCode(5).toRaw());
		assertEquals("101100",CodeNumber.getOmegaCode(6).toRaw());
		assertEquals("101110",CodeNumber.getOmegaCode(7).toRaw());
		assertEquals("1110000",CodeNumber.getOmegaCode(8).toRaw());
		assertEquals("1110010",CodeNumber.getOmegaCode(9).toRaw());
		assertEquals("1110100",CodeNumber.getOmegaCode(10).toRaw());
		assertEquals("1110110",CodeNumber.getOmegaCode(11).toRaw());
		assertEquals("1111000",CodeNumber.getOmegaCode(12).toRaw());
		assertEquals("1111010",CodeNumber.getOmegaCode(13).toRaw());
		assertEquals("1111100",CodeNumber.getOmegaCode(14).toRaw());
		assertEquals("1111110",CodeNumber.getOmegaCode(15).toRaw());
		assertEquals("10100100000",CodeNumber.getOmegaCode(16).toRaw());
		assertEquals("10100100010",CodeNumber.getOmegaCode(17).toRaw());
		assertEquals("1011011001000",CodeNumber.getOmegaCode(100).toRaw());
		assertEquals("11100111111010000",CodeNumber.getOmegaCode(1000).toRaw());
		assertEquals("111101100111000100000",CodeNumber.getOmegaCode(10000).toRaw());
		assertEquals("1010010000110000110101000000",CodeNumber.getOmegaCode(100000).toRaw());
		assertEquals("1010010011111101000010010000000",CodeNumber.getOmegaCode(1000000).toRaw());
		}

	@Test
	public void testReadOmegaCode() {
		BinaryFinFout b=new BinaryFinFout();
		for(int i=1;i<16;i++)
			b.write(CodeNumber.getOmegaCode(i));
		b.flush();
		for(Long i=1L;i<16;i++)
		assertEquals(i,CodeNumber.readOmegaCode(b));
	}

	@Test
	public void testGetRiceCode() {
		assertEquals("0",CodeNumber.getRiceCode(0,0).toRaw());
		assertEquals("10",CodeNumber.getRiceCode(0,1).toRaw());
		assertEquals("110",CodeNumber.getRiceCode(0,2).toRaw());
		assertEquals("1110",CodeNumber.getRiceCode(0,3).toRaw());
		assertEquals("11110",CodeNumber.getRiceCode(0,4).toRaw());
		assertEquals("111110",CodeNumber.getRiceCode(0,5).toRaw());
		assertEquals("1111110",CodeNumber.getRiceCode(0,6).toRaw());
		assertEquals("11111110",CodeNumber.getRiceCode(0,7).toRaw());
		assertEquals("111111110",CodeNumber.getRiceCode(0,8).toRaw());
		assertEquals("1111111110",CodeNumber.getRiceCode(0,9).toRaw());
		assertEquals("11111111110",CodeNumber.getRiceCode(0,10).toRaw());
		
		assertEquals("00",CodeNumber.getRiceCode(1,0).toRaw());
		assertEquals("01",CodeNumber.getRiceCode(1,1).toRaw());
		assertEquals("100",CodeNumber.getRiceCode(1,2).toRaw());
		assertEquals("101",CodeNumber.getRiceCode(1,3).toRaw());
		assertEquals("1100",CodeNumber.getRiceCode(1,4).toRaw());
		assertEquals("1101",CodeNumber.getRiceCode(1,5).toRaw());
		assertEquals("11100",CodeNumber.getRiceCode(1,6).toRaw());
		assertEquals("11101",CodeNumber.getRiceCode(1,7).toRaw());
		assertEquals("111100",CodeNumber.getRiceCode(1,8).toRaw());
		assertEquals("111101",CodeNumber.getRiceCode(1,9).toRaw());
		assertEquals("1111100",CodeNumber.getRiceCode(1,10).toRaw());
		
		assertEquals("000",CodeNumber.getRiceCode(2,0).toRaw());
		assertEquals("001",CodeNumber.getRiceCode(2,1).toRaw());
		assertEquals("010",CodeNumber.getRiceCode(2,2).toRaw());
		assertEquals("011",CodeNumber.getRiceCode(2,3).toRaw());
		assertEquals("1000",CodeNumber.getRiceCode(2,4).toRaw());
		assertEquals("1001",CodeNumber.getRiceCode(2,5).toRaw());
		assertEquals("1010",CodeNumber.getRiceCode(2,6).toRaw());
		assertEquals("1011",CodeNumber.getRiceCode(2,7).toRaw());
		assertEquals("11000",CodeNumber.getRiceCode(2,8).toRaw());
		assertEquals("11001",CodeNumber.getRiceCode(2,9).toRaw());
		assertEquals("11010",CodeNumber.getRiceCode(2,10).toRaw());
		
		assertEquals("0000",CodeNumber.getRiceCode(3,0).toRaw());
		assertEquals("0001",CodeNumber.getRiceCode(3,1).toRaw());
		assertEquals("0010",CodeNumber.getRiceCode(3,2).toRaw());
		assertEquals("0011",CodeNumber.getRiceCode(3,3).toRaw());
		assertEquals("0100",CodeNumber.getRiceCode(3,4).toRaw());
		assertEquals("0101",CodeNumber.getRiceCode(3,5).toRaw());
		assertEquals("0110",CodeNumber.getRiceCode(3,6).toRaw());
		assertEquals("0111",CodeNumber.getRiceCode(3,7).toRaw());
		assertEquals("10000",CodeNumber.getRiceCode(3,8).toRaw());
		assertEquals("10001",CodeNumber.getRiceCode(3,9).toRaw());
		assertEquals("10010",CodeNumber.getRiceCode(3,10).toRaw());
		
		assertEquals("00000",CodeNumber.getRiceCode(4,0).toRaw());
		assertEquals("00001",CodeNumber.getRiceCode(4,1).toRaw());
		assertEquals("00010",CodeNumber.getRiceCode(4,2).toRaw());
		assertEquals("00011",CodeNumber.getRiceCode(4,3).toRaw());
		assertEquals("00100",CodeNumber.getRiceCode(4,4).toRaw());
		assertEquals("00101",CodeNumber.getRiceCode(4,5).toRaw());
		assertEquals("00110",CodeNumber.getRiceCode(4,6).toRaw());
		assertEquals("00111",CodeNumber.getRiceCode(4,7).toRaw());
		assertEquals("01000",CodeNumber.getRiceCode(4,8).toRaw());
		assertEquals("01001",CodeNumber.getRiceCode(4,9).toRaw());
		assertEquals("01010",CodeNumber.getRiceCode(4,10).toRaw());
		
	}

	@Test
	public void testReadRiceCode() {
		for(int k=0;k<5;k++)
		{
		BinaryFinFout b=new BinaryFinFout();
		for(int i=0;i<16;i++)
			b.write(CodeNumber.getRiceCode(k,i));
		b.flush();
		for(Long i=0L;i<16;i++)
		assertEquals(i,CodeNumber.readRiceCode(k,b));
		}
	}

	@Test
	public void testGetGolombkCode() {
		assertEquals(null,CodeNumber.getGolombkCode(0,0));
		assertEquals("0",CodeNumber.getGolombkCode(1,0).toRaw());
		assertEquals("10",CodeNumber.getGolombkCode(1,1).toRaw());
		assertEquals("110",CodeNumber.getGolombkCode(1,2).toRaw());
		assertEquals("1110",CodeNumber.getGolombkCode(1,3).toRaw());
		assertEquals("11110",CodeNumber.getGolombkCode(1,4).toRaw());
		assertEquals("111110",CodeNumber.getGolombkCode(1,5).toRaw());
		assertEquals("1111110",CodeNumber.getGolombkCode(1,6).toRaw());
		assertEquals("11111110",CodeNumber.getGolombkCode(1,7).toRaw());
		assertEquals("111111110",CodeNumber.getGolombkCode(1,8).toRaw());
		assertEquals("1111111110",CodeNumber.getGolombkCode(1,9).toRaw());
		assertEquals("11111111110",CodeNumber.getGolombkCode(1,10).toRaw());
		

		assertEquals("00",CodeNumber.getGolombkCode(2,0).toRaw());
		assertEquals("01",CodeNumber.getGolombkCode(2,1).toRaw());
		assertEquals("100",CodeNumber.getGolombkCode(2,2).toRaw());
		assertEquals("101",CodeNumber.getGolombkCode(2,3).toRaw());
		assertEquals("1100",CodeNumber.getGolombkCode(2,4).toRaw());
		assertEquals("1101",CodeNumber.getGolombkCode(2,5).toRaw());
		assertEquals("11100",CodeNumber.getGolombkCode(2,6).toRaw());
		assertEquals("11101",CodeNumber.getGolombkCode(2,7).toRaw());
		assertEquals("111100",CodeNumber.getGolombkCode(2,8).toRaw());
		assertEquals("111101",CodeNumber.getGolombkCode(2,9).toRaw());
		assertEquals("1111100",CodeNumber.getGolombkCode(2,10).toRaw());
		

		assertEquals("000",CodeNumber.getGolombkCode(4,0).toRaw());
		assertEquals("001",CodeNumber.getGolombkCode(4,1).toRaw());
		assertEquals("010",CodeNumber.getGolombkCode(4,2).toRaw());
		assertEquals("011",CodeNumber.getGolombkCode(4,3).toRaw());
		assertEquals("1000",CodeNumber.getGolombkCode(4,4).toRaw());
		assertEquals("1001",CodeNumber.getGolombkCode(4,5).toRaw());
		assertEquals("1010",CodeNumber.getGolombkCode(4,6).toRaw());
		assertEquals("1011",CodeNumber.getGolombkCode(4,7).toRaw());
		assertEquals("11000",CodeNumber.getGolombkCode(4,8).toRaw());
		assertEquals("11001",CodeNumber.getGolombkCode(4,9).toRaw());
		assertEquals("11010",CodeNumber.getGolombkCode(4,10).toRaw());
		

		assertEquals("00000",CodeNumber.getGolombkCode(16,0).toRaw());
		assertEquals("00001",CodeNumber.getGolombkCode(16,1).toRaw());
		assertEquals("00010",CodeNumber.getGolombkCode(16,2).toRaw());
		assertEquals("00011",CodeNumber.getGolombkCode(16,3).toRaw());
		assertEquals("00100",CodeNumber.getGolombkCode(16,4).toRaw());
		assertEquals("00101",CodeNumber.getGolombkCode(16,5).toRaw());
		assertEquals("00110",CodeNumber.getGolombkCode(16,6).toRaw());
		assertEquals("00111",CodeNumber.getGolombkCode(16,7).toRaw());
		assertEquals("01000",CodeNumber.getGolombkCode(16,8).toRaw());
		assertEquals("01001",CodeNumber.getGolombkCode(16,9).toRaw());
		assertEquals("01010",CodeNumber.getGolombkCode(16,10).toRaw());
		
		}

	@Test
	public void testReadGolombkCode() {
		for(int k=1;k<16;k*=2)
		{
		BinaryFinFout b=new BinaryFinFout();
		for(int i=0;i<16;i++)
			b.write(CodeNumber.getGolombkCode(k,i));
		b.flush();
		for(Long i=0L;i<16;i++)
		assertEquals(i,CodeNumber.readGolombkCode(k,b));
		assertEquals(null,CodeNumber.readGolombkCode(-1,b));
		}
	}

	@Test
	public void testFib() {
		assertEquals(0,CodeNumber.Fib(0));
		assertEquals(1,CodeNumber.Fib(1));
		assertEquals(1,CodeNumber.Fib(2));
		assertEquals(2,CodeNumber.Fib(3));
		assertEquals(3,CodeNumber.Fib(4));
		
		assertEquals(377,CodeNumber.Fib(14));
		
		assertEquals(5,CodeNumber.Fib(5));
		assertEquals(8,CodeNumber.Fib(6));
		assertEquals(13,CodeNumber.Fib(7));
		assertEquals(21,CodeNumber.Fib(8));
		assertEquals(34,CodeNumber.Fib(9));
		assertEquals(55,CodeNumber.Fib(10));
		assertEquals(89,CodeNumber.Fib(11));
		assertEquals(144,CodeNumber.Fib(12));
		assertEquals(233,CodeNumber.Fib(13));
		
		assertEquals(610,CodeNumber.Fib(15));
		assertEquals(987,CodeNumber.Fib(16));
		assertEquals(1597,CodeNumber.Fib(17));
		assertEquals(2584,CodeNumber.Fib(18));
		assertEquals(4181,CodeNumber.Fib(19));
		assertEquals(6765,CodeNumber.Fib(20));
		
	}

	@Test
	public void testGetFibonacciCode() {
		assertEquals(null,CodeNumber.getFibonacciCode(0));
		assertEquals("11",CodeNumber.getFibonacciCode(1).toRaw());
		assertEquals("011",CodeNumber.getFibonacciCode(2).toRaw());
		assertEquals("0011",CodeNumber.getFibonacciCode(3).toRaw());
		assertEquals("1011",CodeNumber.getFibonacciCode(4).toRaw());
		assertEquals("00011",CodeNumber.getFibonacciCode(5).toRaw());
		assertEquals("10011",CodeNumber.getFibonacciCode(6).toRaw());
		assertEquals("01011",CodeNumber.getFibonacciCode(7).toRaw());
		assertEquals("000011",CodeNumber.getFibonacciCode(8).toRaw());
		assertEquals("100011",CodeNumber.getFibonacciCode(9).toRaw());
		assertEquals("010011",CodeNumber.getFibonacciCode(10).toRaw());
		assertEquals("001011",CodeNumber.getFibonacciCode(11).toRaw());
		assertEquals("101011",CodeNumber.getFibonacciCode(12).toRaw());
		assertEquals("0000011",CodeNumber.getFibonacciCode(13).toRaw());
		assertEquals("1000011",CodeNumber.getFibonacciCode(14).toRaw());
		
		assertEquals("0100100011",CodeNumber.getFibonacciCode(65).toRaw());
			
	}

	@Test
	public void testReadFibonacciCode() {
	
		{
			long start=0;
			if(CodeNumber.getFibonacciCode(0)==null)
				start=1;
			BinaryFinFout b=new BinaryFinFout();
			for(long i=start;i<16;i++)
				b.write(CodeNumber.getFibonacciCode(i));
			b.flush();
			for(Long i=start;i<16;i++)
			assertEquals(i,CodeNumber.readFibonacciCode(b));
		}
	}

	@Test
	public void testFibonacci() {
		assertEquals(0,CodeNumber.fibonacci(0));
		assertEquals(1,CodeNumber.fibonacci(1));
		assertEquals(1,CodeNumber.fibonacci(2));
		assertEquals(2,CodeNumber.fibonacci(3));
		assertEquals(3,CodeNumber.fibonacci(4));
		
		assertEquals(377,CodeNumber.fibonacci(14));
		
		assertEquals(5,CodeNumber.fibonacci(5));
		assertEquals(8,CodeNumber.fibonacci(6));
		assertEquals(13,CodeNumber.fibonacci(7));
		assertEquals(21,CodeNumber.fibonacci(8));
		assertEquals(34,CodeNumber.fibonacci(9));
		assertEquals(55,CodeNumber.fibonacci(10));
		assertEquals(89,CodeNumber.fibonacci(11));
		assertEquals(144,CodeNumber.fibonacci(12));
		assertEquals(233,CodeNumber.fibonacci(13));
		
		assertEquals(610,CodeNumber.fibonacci(15));
		assertEquals(987,CodeNumber.fibonacci(16));
		assertEquals(1597,CodeNumber.fibonacci(17));
		assertEquals(2584,CodeNumber.fibonacci(18));
		assertEquals(4181,CodeNumber.fibonacci(19));
		assertEquals(6765,CodeNumber.fibonacci(20));
	}

	@Test
	public void testGetZetaCode() {
		assertEquals(null,CodeNumber.getZetaCode(1,0));
		assertEquals("0",CodeNumber.getZetaCode(1,1).toRaw());
		assertEquals("100",CodeNumber.getZetaCode(1,2).toRaw());
		assertEquals("101",CodeNumber.getZetaCode(1,3).toRaw());
		assertEquals("11000",CodeNumber.getZetaCode(1,4).toRaw());
		assertEquals("11001",CodeNumber.getZetaCode(1,5).toRaw());
		assertEquals("11010",CodeNumber.getZetaCode(1,6).toRaw());
		assertEquals("11011",CodeNumber.getZetaCode(1,7).toRaw());
		assertEquals("1110000",CodeNumber.getZetaCode(1,8).toRaw());
		
		assertEquals(null,CodeNumber.getZetaCode(2,0));
		assertEquals("00",CodeNumber.getZetaCode(2,1).toRaw());
		assertEquals("010",CodeNumber.getZetaCode(2,2).toRaw());
		assertEquals("011",CodeNumber.getZetaCode(2,3).toRaw());
		assertEquals("10000",CodeNumber.getZetaCode(2,4).toRaw());
		assertEquals("10001",CodeNumber.getZetaCode(2,5).toRaw());
		assertEquals("10010",CodeNumber.getZetaCode(2,6).toRaw());
		assertEquals("10011",CodeNumber.getZetaCode(2,7).toRaw());
		assertEquals("101000",CodeNumber.getZetaCode(2,8).toRaw());
		
		assertEquals(null,CodeNumber.getZetaCode(3,0));
		assertEquals("000",CodeNumber.getZetaCode(3,1).toRaw());
		assertEquals("0010",CodeNumber.getZetaCode(3,2).toRaw());
		assertEquals("0011",CodeNumber.getZetaCode(3,3).toRaw());
		assertEquals("0100",CodeNumber.getZetaCode(3,4).toRaw());
		assertEquals("0101",CodeNumber.getZetaCode(3,5).toRaw());
		assertEquals("0110",CodeNumber.getZetaCode(3,6).toRaw());
		assertEquals("0111",CodeNumber.getZetaCode(3,7).toRaw());
		assertEquals("1000000",CodeNumber.getZetaCode(3,8).toRaw());
		
		assertEquals(null,CodeNumber.getZetaCode(4,0));
		assertEquals("0000",CodeNumber.getZetaCode(4,1).toRaw());
		assertEquals("00010",CodeNumber.getZetaCode(4,2).toRaw());
		assertEquals("00011",CodeNumber.getZetaCode(4,3).toRaw());
		assertEquals("00100",CodeNumber.getZetaCode(4,4).toRaw());
		assertEquals("00101",CodeNumber.getZetaCode(4,5).toRaw());
		assertEquals("00110",CodeNumber.getZetaCode(4,6).toRaw());
		assertEquals("00111",CodeNumber.getZetaCode(4,7).toRaw());
		assertEquals("01000",CodeNumber.getZetaCode(4,8).toRaw());
		
	}
	@Test
	public void testmain() 
{
		//dummy for code coverage
		//CodeNumber.main(null);
}
	@Test
	public void testReadZetaCode() {
		for(int k=1;k<5;k++)
		{
			long start=0;
			if(CodeNumber.getZetaCode(k,0)==null)
				start=1;
			BinaryFinFout b=new BinaryFinFout();
			for(int i=(int)start;i<16;i++)
				b.write(CodeNumber.getZetaCode(k,i));
			b.flush();
			for(Long i=start;i<16;i++)
			{
				Long n = CodeNumber.readZetaCode(k,b);
				System.out.println(""+i+"->"+n);
			//assertEquals(i,n);
		}}
	}

	@Test
	public void testGetEvenRodehCode() {	
		
		assertEquals("000",CodeNumber.getEvenRodehCode(0).toRaw());
		assertEquals("001",CodeNumber.getEvenRodehCode(1).toRaw());
		assertEquals("010",CodeNumber.getEvenRodehCode(2).toRaw());
		assertEquals("011",CodeNumber.getEvenRodehCode(3).toRaw());
		assertEquals("1000",CodeNumber.getEvenRodehCode(4).toRaw());
		assertEquals("1010",CodeNumber.getEvenRodehCode(5).toRaw());
		assertEquals("1100",CodeNumber.getEvenRodehCode(6).toRaw());
		assertEquals("1110",CodeNumber.getEvenRodehCode(7).toRaw());
		assertEquals("10010000",CodeNumber.getEvenRodehCode(8).toRaw());
		assertEquals("10010010",CodeNumber.getEvenRodehCode(9).toRaw());
		
		assertEquals("10011110",CodeNumber.getEvenRodehCode(15).toRaw());
		assertEquals("101100000",CodeNumber.getEvenRodehCode(16).toRaw());
		
		assertEquals("10011001010110010010",CodeNumber.getEvenRodehCode(2761).toRaw());
		
	}

	@Test
	public void testReadEvenRodehCode() {
		//for(int k=1;k<5;k++)
		{
			BinaryFinFout b=new BinaryFinFout();
			for(int i=0;i<16;i++)
				b.write(CodeNumber.getEvenRodehCode(i));
			b.flush();
			for(Long i=0L;i<16;i++)
			assertEquals(i,CodeNumber.readEvenRodehCode(b));
		}
	}

	@Test
	public void testGetLevenshteinCode() {
		assertEquals("0",CodeNumber.getLevenshteinCode(0).toRaw());
		assertEquals("10",CodeNumber.getLevenshteinCode(1).toRaw());
		assertEquals("1100",CodeNumber.getLevenshteinCode(2).toRaw());
		assertEquals("1101",CodeNumber.getLevenshteinCode(3).toRaw());
		assertEquals("1110000",CodeNumber.getLevenshteinCode(4).toRaw());
		assertEquals("1110001",CodeNumber.getLevenshteinCode(5).toRaw());
		assertEquals("1110010",CodeNumber.getLevenshteinCode(6).toRaw());
		assertEquals("1110011",CodeNumber.getLevenshteinCode(7).toRaw());
		assertEquals("11101000",CodeNumber.getLevenshteinCode(8).toRaw());
		assertEquals("11101001",CodeNumber.getLevenshteinCode(9).toRaw());
		assertEquals("11101010",CodeNumber.getLevenshteinCode(10).toRaw());
		assertEquals("11101011",CodeNumber.getLevenshteinCode(11).toRaw());
		assertEquals("11101100",CodeNumber.getLevenshteinCode(12).toRaw());
		assertEquals("11101101",CodeNumber.getLevenshteinCode(13).toRaw());
		assertEquals("11101110",CodeNumber.getLevenshteinCode(14).toRaw());
		assertEquals("11101111",CodeNumber.getLevenshteinCode(15).toRaw());
		assertEquals("111100000000",CodeNumber.getLevenshteinCode(16).toRaw());
		assertEquals("111100000001",CodeNumber.getLevenshteinCode(17).toRaw());
			
	}

	@Test
	public void testReadLevenshteinCode() {
		//for(int k=1;k<5;k++)
		{
			BinaryFinFout b=new BinaryFinFout();
			for(int i=0;i<256;i=i*2+1)
				b.write(CodeNumber.getLevenshteinCode(i));
			b.flush();
			for(Long i=0L;i<256;i=i*2+1)
			assertEquals(i,CodeNumber.readLevenshteinCode(b));
		}
	}

	@Test
	public void testGetVLQCode() {
		assertEquals("00000000",CodeNumber.getVLQCode(0).toRaw());
		assertEquals("01111111",CodeNumber.getVLQCode(127).toRaw());	
		assertEquals("1000000100000000",CodeNumber.getVLQCode(128).toRaw());	
		assertEquals("1100000000000000",CodeNumber.getVLQCode(8192).toRaw());	
		assertEquals("1111111101111111",CodeNumber.getVLQCode(16383).toRaw());	
		assertEquals("100000011000000000000000",CodeNumber.getVLQCode(16384).toRaw());	
		assertEquals("111111111111111101111111",CodeNumber.getVLQCode(2097151).toRaw());	
		assertEquals("10000001100000001000000000000000",CodeNumber.getVLQCode(2097152).toRaw());	
		assertEquals("11000000100000001000000000000000",CodeNumber.getVLQCode(134217728).toRaw());	
		assertEquals("11111111111111111111111101111111",CodeNumber.getVLQCode(268435455).toRaw());	
		}

	@Test
	public void testReadVLQCode() {
		//for(int k=1;k<5;k++)
		{
			BinaryFinFout b=new BinaryFinFout();
			for(int i=0;i<1600000;i=i*16+1)
				b.write(CodeNumber.getVLQCode(i));
			b.flush();
			for(Long i=0L;i<1600000;i=i*16+1)
			assertEquals(i,CodeNumber.readVLQCode(b));
		}
	}

	@Test
	public void testGetLZ4Code() {
		assertEquals("0000",CodeNumber.getLZ4Code(0).toRaw());
		assertEquals("111100100001",CodeNumber.getLZ4Code(48).toRaw());
		assertEquals("11111111111100001010",CodeNumber.getLZ4Code(280).toRaw());
		assertEquals("111100000000",CodeNumber.getLZ4Code(15).toRaw());
		
	}

	@Test
	public void testReadLZ4Code() {
		//for(int k=1;k<5;k++)
		{
			BinaryFinFout b=new BinaryFinFout();
			for(int i=0;i<450;i=i*2+1)
				b.write(CodeNumber.getLZ4Code(i));
			b.write(CodeNumber.getLZ4Code(165535));
			b.flush();
			for(Long i=0L;i<450;i=i*2+1)
			assertEquals(i,CodeNumber.readLZ4Code(b));
			assertEquals((Long)165535L,CodeNumber.readLZ4Code(b));
		}
	}

	@Test
	public void testGetNibblesCode() {
		assertEquals("0000",CodeNumber.getNibblesCode(0).toRaw());
		assertEquals("1110",CodeNumber.getNibblesCode(14).toRaw());
		assertEquals("11110000",CodeNumber.getNibblesCode(15).toRaw());
		assertEquals("111111110000",CodeNumber.getNibblesCode(30).toRaw());
		assertEquals("111111110001",CodeNumber.getNibblesCode(31).toRaw());
		}

	@Test
	public void testReadNibblesCode() {
		//for(int k=1;k<5;k++)
				{
					BinaryFinFout b=new BinaryFinFout();
					for(int i=0;i<16;i++)
						b.write(CodeNumber.getNibblesCode(i));
					b.flush();
					for(Long i=0L;i<16;i++)
					assertEquals(i,CodeNumber.readNibblesCode(b));
				}
	}

	@Test
	public void CodeNumberSet() {
		for(int i=0;i<CodeNumber.MaxCodingIndex;i++)
			if (i!=CodeNumber.PhaseInCoding)
		{
	ICodingRule coding=new CodeNumberSet(i);
	
	IBinaryStream fifo=new BinaryFinFout();
	fifo.setCodingRule(coding);
	for(int j=1;j<257;j++)
		fifo.write(new Number(j));
	for(int j=1;j<16;j++)
	fifo.write(new Number(j*31+j*j*j));
	fifo.flush();
	for(int j=1;j<257;j++)
		{
		ISymbol n = fifo.readSymbol();
		System.out.println(coding+":"+(j)+"="+n);
	assertEquals(new Number(j),n);
	}
	for(int j=1;j<16;j++)
	{
		ISymbol n = fifo.readSymbol();
		System.out.println(coding+":"+(j*31+j*j*j)+"="+n);
	assertEquals(new Number(j*31+j*j*j),n);
	}	
	}
	}
}
