package com.zoubworld.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.zoubworld.chemistry.ChemistryTest;
import com.zoubworld.electronic.logic.ElectronicTest;
/*
import com.backblaze.erasure.GaloisTest;
import com.backblaze.erasure.MatrixTest;
import com.backblaze.erasure.ReedSolomonTest;*/
import com.zoubworld.games.chess.ChessTest;
import com.zoubworld.games.dames.DameTest;
import com.zoubworld.geometry.GeometryTest;
import com.zoubworld.java.math.MathTest;
import com.zoubworld.java.utils.compress.file.BinaryTest;
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
import com.zoubworld.robot.Odometry2Test;

@RunWith(Suite.class)
@SuiteClasses({
	/*
	GaloisTest.class,
	ReedSolomonTest.class,
	MatrixTest.class,*/
	
	DameTest.class,
	ChessTest.class,
	TestEcc.class,
	ArgsParserTest.class,
	ElectronicTest.class,
	GeometryTest.class,
	JavaUtilsTest.class,
	BinaryTest.class,

	FilesSymbolTest.class,
	HuffmanCodeTest.class,/**/
		CompositeCodeTest.class,/**/
	BinaryStdInTest.class,
	FileTest.class,
	CodeTest.class,
	RLETest.class,
	SymbolTest.class,
	LZWTest.class,
	

	GeometryTest.class,

	MathTest.class,
	ChemistryTest.class,
	Odometry2Test.class,
	// TestJunit1.class,
	//   TestJunit2.class
	  

})
public class AllTests {

}
