package com.zoubworld.java.utils.compress.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.zoubworld.java.utils.test.ArgsParserTest;
import com.zoubworld.java.utils.test.JavaUtilsTest;



@RunWith(Suite.class)
@SuiteClasses({
//	TestJunit1.class,
	ArgsParserTest.class,
	CodeTest.class,
	RLETest.class,
	SymbolTest.class,
	HuffmanCodeTest.class,
	CompositeCodeTest.class,
	FilesSymbolTest.class,
	BinaryStdInTest.class,
	FileTest.class,
	JavaUtilsTest.class,
	LZWTest.class
	
})
public class AllTests {

}
