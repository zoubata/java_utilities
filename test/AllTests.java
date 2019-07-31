package com.zoubworld.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.zoubworld.games.chess.ChessTest;
import com.zoubworld.games.dames.DameTest;
import com.zoubworld.geometry.GeometryTest;
import com.zoubworld.java.utils.compress.test.BinaryStdInTest;
import com.zoubworld.java.utils.compress.test.CodeTest;
import com.zoubworld.java.utils.compress.test.CompositeCodeTest;
import com.zoubworld.java.utils.compress.test.FileTest;
import com.zoubworld.java.utils.compress.test.FilesSymbolTest;
import com.zoubworld.java.utils.compress.test.HuffmanCodeTest;
import com.zoubworld.java.utils.compress.test.LZWTest;
import com.zoubworld.java.utils.compress.test.RLETest;
import com.zoubworld.java.utils.compress.test.SymbolTest;
import com.zoubworld.java.utils.test.ArgsParserTest;
import com.zoubworld.java.utils.test.JavaUtilsTest;

@RunWith(Suite.class)
@SuiteClasses({
	
	DameTest.class,
	ChessTest.class,
	
	ArgsParserTest.class,
//	CodeTest.class,
	RLETest.class,
	SymbolTest.class,
		HuffmanCodeTest.class,/**/
	//	CompositeCodeTest.class,/**/
		FilesSymbolTest.class,/**/
//	BinaryStdInTest.class,
//	FileTest.class,/**/
	GeometryTest.class,
	JavaUtilsTest.class,
//	LZWTest.class
	// TestJunit1.class,
	//   TestJunit2.class
	   
})
public class AllTests {

}
