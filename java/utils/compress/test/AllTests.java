package com.zoubworld.java.utils.compress.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;



@RunWith(Suite.class)
@SuiteClasses({
//	TestJunit1.class,

	CodeTest.class,
	RLETest.class,
	SymbolTest.class,
	HuffmanCodeTest.class,
	CompositeCodeTest.class,
	FilesSymbolTest.class,
	BinaryStdInTest.class,
	FileTest.class,
	LZWTest.class
	
})
public class AllTests {

}
