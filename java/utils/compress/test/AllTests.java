package com.zoubworld.java.utils.compress.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.zoubworld.java.utils.compress.file.BinaryTest;



@RunWith(Suite.class)
@SuiteClasses({
//	TestJunit1.class,

	CodeTest.class,
	RLETest.class,
	BPETest.class,
	BTETest.class,
	TestMultiAlgo.class,
	PieTest.class,
	Lz4Test.class,
	SymbolTest.class,
	HuffmanCodeTest.class,
	CompositeCodeTest.class,
	FilesSymbolTest.class,
	BinaryStdInTest.class,
	FileTest.class,
	LZWTest.class,
	FilesSymbolTest.class,
	CodeTest.class,
	CryptoAlgoTest.class,
	BinaryTest.class,
	
})
public class AllTests {

}
