package com.zoubworld.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExcelArrayTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGetHeader() {
		ExcelArray ex=new ExcelArray();
		ex.parse("a,b,c,\r\n"
				+ "0,0,0,\r\n"
				+ "1,1,1,\r\n"
				+ "2,2,2,\r\n"
				);
		assertEquals(ex.getHeader().toString(), "[a, b, c]");
	}

	@Test
	void testMerge() {
		
		ExcelArray ex1=new ExcelArray();
		ex1.parse("a,b,c,\r\n"
				+ "0,10b,10c,\r\n"
				+ "1,11,11,\r\n"
				+ "2,12,12,\r\n"
				);
		ExcelArray ex2=new ExcelArray();
		ex2.parse("a,d,e,\r\n"
				+ "0,20d,20e,\r\n"
				+ "1,21,21,\r\n"
				+ "2,22,22,\r\n"
				);
		Map<String, ExcelArray> m=new HashMap<String, ExcelArray>();
		List<String> l=new ArrayList<String>();
		 l.add("a");
		 m.put("e1", ex1);
		 m.put("e2", ex2);
		 
		ExcelArray ex3=ExcelArray.Merge(m, l, "Merge");
		
		assertEquals(ex3.getHeader().toString(), "[a, b[e1], c[e1], d[e2], e[e2]]");
		assertEquals(ex3.getData().get(0).toString(), "[0, 10b, 10c, 20d, 20e]");
		
	}

	@Test
	void testRenameColumn() {
		ExcelArray ex=new ExcelArray();
	ex.parse("a,b,c,\r\n"
			+ "0,0,0,\r\n"
			+ "1,1,1,\r\n"
			+ "2,2,2,\r\n"
			);
	ex.renameColumn("a", "e");
	assertEquals(ex.getHeader().toString(), "[e, b, c]");
	
	}

}
