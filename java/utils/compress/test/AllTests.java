package com.zoubworld.java.utils.compress.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.zoubworld.java.utils.compress.AdaptativeHuffmanCodeTest;
import com.zoubworld.java.utils.compress.CodeStatisticalSetTest;
import com.zoubworld.java.utils.compress.algo.NodeTreeTest;
import com.zoubworld.java.utils.compress.algo.TreeTest;
import com.zoubworld.java.utils.compress.algo.TupleEncodingTest;
import com.zoubworld.java.utils.compress.blockSorting.JunitTest;

@RunWith(Suite.class)
@SuiteClasses({
		// TestJunit1.class,
	TreeTest.class,
	NodeTreeTest.class,
	JunitTest.class,
	CodeStatisticalSetTest.class,
	AdaptativeHuffmanCodeTest.class,
	TupleEncodingTest.class,
	BinaryStdInTest.class,
	BinaryTest.class,
	BPETest.class, 
	BTETest.class,
	BWTTest.class,
	CodeNumberTest.class,
	CodeTest.class, 
	CompositeCodeTest.class, 
	CompressBlockTest.class,
	CryptoAlgoTest.class,
	FifoAlgoTest.class,
	FileAllocationTableTest.class,
	FileCompactedTest.class,
	FilesSymbolTest.class,
	FileTest.class, 
	HuffmanCodeTest.class,
	IntegrityTest.class,
	
	Lz4Test.class,
	LZSTest.class,
	LZWTest.class, 
	MTFTest.class,
	NumberTest.class,
	PieTest.class, 
	RLETest.class,
	SymbolTest.class,
	TestMultiAlgo.class,
})
public class AllTests {

}
