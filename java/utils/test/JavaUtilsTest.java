/**
 * 
 */
package com.zoubworld.java.utils.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zoubworld.utils.ExcelArray;
import com.zoubworld.utils.JavaUtils;
import com.zoubworld.utils.MathUtils;

/**
 * @author Pierre Valleau
 *
 */
public class JavaUtilsTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.zoubworld.utils.JavaUtils#executeCommandColor(java.lang.String)}.
	 */
	@Test
	public void testExecuteCommandColor() {
		
	//	JavaUtils.executeCommandColor("dir");
		assertTrue(true);
	}
	@Test
	public void testExcelArray2() {
		List<String> row1=new ArrayList();
		row1.add("a1");
		row1.add("b1");
		row1.add("c1");
		row1.add("d1");
		List<String> hdr1=new ArrayList();
		hdr1.add("ha");
		hdr1.add("hb");
		hdr1.add("hc");
		hdr1.add("hd");
		List<String> row2=new ArrayList();
		row2.add("a1");
		row2.add("d1");
		row2.add("b1");
		row2.add("c1");
		List<String> hdr2=new ArrayList();
		hdr2.add("ha");
		hdr2.add("hd");
		hdr2.add("hb");
		hdr2.add("hc");
		assertEquals( ExcelArray.compare(hdr1, row1, hdr2, row2, 0, 0),"");
		hdr2.add("he");row2.add("e2");
		
		assertEquals( ExcelArray.compare(hdr1, row1, hdr2, row2, 0, 0),"Missing collunm []\n" + 
				"new collunm [he]\n" + 
				"diff collunm <=>e2\n"  
				);
		hdr1.add("he");
		row1.add("e1");
		assertEquals(""+ ExcelArray.compare(hdr1, row1, hdr2, row2, 0, 0), 
				"diff collunm 'he' e1<=>e2\n" 
				
				);
		
		
	}
	@Test
		public void testExcelArray() {
		ExcelArray e= new ExcelArray();
	//e.read("src\\com\\zoubworld\\chemistry\\data\\chimie.xls.xlsx", "info");
	e.addColumn("a");
	e.addColumn("b");
	e.addColumn("c");
	List<String> row=new ArrayList();
	row.add("a1");
	row.add("b1");
	row.add("c1");	
	e.addRow(row);
	row=new ArrayList();//row.clear();
	row.add("a2");
	row.add("b2");
	row.add("c2");	
	e.addRow(row);
	e.saveAs("res/result.test/tmp/excel.csv");
	e=new ExcelArray(e);
	e.setCell(3, 0, "A3");
	e.setCell(3, "b", "B3");
	e.setCell(3, 2, "C3");
	e.setCell(3,"d", "D3");
	
	assertEquals(3, e.rowMax());
	assertEquals(null, e.getCell(-1, 0));
	assertEquals(null, e.getCell(0, -1));
	assertEquals("a1", e.getCell(0, 0));
	assertEquals("b1", e.getCell(0, "b"));
	assertEquals("D3", e.getCell(3, 3));
	assertEquals("[a, b, c, d]", e.getHeader().toString());
	assertEquals("[c1, c2, C3]",e.getColunm("c").toString());
	assertEquals("res/result.test/tmp/excel.csv",e.getFilename());
	assertEquals("[a2, b2, c2]",e.getRow(1).toString());
	assertEquals(",",e.getSeparator());
	//e.getValue(row, colunms);
	e.saveAs("res/result.test/tmp/excel2.csv");
	 e= new ExcelArray();
	 e.read("res/result.test/tmp/excel2.csv");
	 e.saveAs("res/result.test/tmp/excel3.csv");
	 assertEquals(4-1, e.rowMax());
		assertEquals(null, e.getCell(-1, 0));
		assertEquals(null, e.getCell(0, -1));
		e.deleteEmptyRow();
		e.deleteEmptyColunm();
		
		assertEquals("a1", e.getCell(0, 0));
		assertEquals("b1", e.getCell(0, "b"));
		assertEquals("D3", e.getCell(2, 3));
		assertEquals("[a, b, c, d]", e.getHeader().toString());
		assertEquals("[c1, c2, C3]",e.getColunm("c").toString());
		assertEquals("res/result.test/tmp/excel3.csv",e.getFilename());
		assertEquals("[a2, b2, c2, ]",e.getRow(1).toString());
		assertEquals(",",e.getSeparator());
		
		List<String> hdr=new ArrayList();
		row=new ArrayList();//row.clear();
		row.add("a4");
		row.add("d4");
		row.add("c4");
		
		hdr.add("a");
		hdr.add("d");
		hdr.add("c");
		e.addRow(row, hdr);
		row=new ArrayList();//row.clear();
		row.add("a5");
		row.add("b5");
		row.add("c5");
		row.add("d5");
		e.addRow(row);
		System.out.println(e.toString());

		assertEquals("[a4, null, c4, d4]",e.getRow(3).toString());
		assertEquals("[a5, b5, c5, d5]",e.getRow(4).toString());
		assertEquals("[a5, b5, c5, d5]",e.findRow("b", "b5").toString());
		assertEquals("[a4, null, c4, d4]",e.findRow("c", "c4").toString());
		assertEquals(""+3,e.findiRow("c", "c4").toString());
		assertEquals(null,e.getRow(12));
		 
		
	}
	@Test
	public void testMathUtils() {
		MathUtils m=new MathUtils();
		List<Double> ld=new ArrayList();
		ld.add(1.0);
		ld.add(2.0);
		ld.add(3.0);
		ld.add(4.0);
		ld.add(5.0);
		
		assertEquals(MathUtils.median(ld),3.0,0.0);
		assertEquals(MathUtils.max(ld),5.0,0.0);
		assertEquals(MathUtils.min(ld),1.0,0.0);
		assertEquals(MathUtils.sum(ld),15.0,0.0);
		assertEquals(MathUtils.average(ld),3.0,0.0);
		assertEquals(MathUtils.USH(ld,1.33),14.97,0.1);
		assertEquals(MathUtils.USL(ld,1.33),-8.97,0.1);
		assertEquals(MathUtils.outlayer(ld,1.33).toString(),"[]");
		assertEquals(MathUtils.outlayer(ld,0.16).toString(),"[1.0, 5.0]");
		
		assertEquals(MathUtils.R(ld),4.0,0.0);
	}
	
	

	/**
	 * Test method for {@link com.zoubworld.utils.JavaUtils#executeCommand(java.lang.String)}.
	 */
	@Test
	public void testExecuteCommandString() {
		assertEquals("toto\n",JavaUtils.executeCommand("echo toto"));
	}

	/**
	 * Test method for {@link com.zoubworld.utils.JavaUtils#executeCommand(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testExecuteCommandStringString() {
		assertEquals("toto\n",JavaUtils.executeCommand("echo toto","res/result.test/tmp/"));
	
		
	}

	/**
	 * Test method for {@link com.zoubworld.utils.JavaUtils#intValueOf(java.io.BufferedReader)}.
	 */
	@Test
	public void testIntValueOf() {
	//	fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.zoubworld.utils.JavaUtils#doubleValueOf(java.io.BufferedReader)}.
	 */
	@Test
	public void testDoubleValueOf() {
	}
	@Test
	public void testparseMapStringListString() {
				Map<String,List<String>> m=new HashMap();
		
		List<String> l=new ArrayList();
		l.add("a01");
		l.add("a02");
		l.add("a03");
		
		m.put("toto", l);
		l=new ArrayList();
		l.add("b11");
		l.add("b12");
		l.add("b13");
		m.put("titi", l);
		System.out.println(m);
		assertEquals(m,JavaUtils.parseMapStringListString(m.toString()));
		assertEquals(l,JavaUtils.parseListString(l.toString()));
		
	}
	
	

	/**
	 * Test method for {@link com.zoubworld.utils.JavaUtils#Max(java.lang.Integer[])}.
	 */
	@Test
	public void testMax() {
		Integer tabi[]= {1,10,500,2,3000,-9999};		
		assertEquals(3000,JavaUtils.Max(tabi));
		tabi=new Integer[0];
		assertEquals(-1,JavaUtils.Max(tabi));
		tabi=null;
		assertEquals(-1,JavaUtils.Max(tabi));
		
		Double tabd[]= {1.0,10.1,500.55,1.0/3.0,3000.222,-9999.9,12.12,Math.PI};		
		assertEquals((double)3000.222,(double)JavaUtils.Max(tabd),0.0000000001);
		tabd=new Double[0];
		assertEquals(-1,JavaUtils.Max(tabd),0.0000000001);
		tabd=null;
		assertEquals(-1,JavaUtils.Max(tabd),0.0000000001);
	}

	/**
	 * Test method for {@link com.zoubworld.utils.JavaUtils#Min(java.lang.Integer[])}.
	 */
	@Test
	public void testMin() {
		Integer tabi[]= {1,10,500,2,3000,-9999,12};		
		assertEquals(-9999,JavaUtils.Min(tabi));
		tabi=new Integer[0];
		assertEquals(-1,JavaUtils.Min(tabi));
		tabi=null;
		assertEquals(-1,JavaUtils.Min(tabi));
		
		Double tabd[]= {1.0,10.1,500.55,1.0/3.0,3000.222,-9999.9,12.12,Math.PI};		
		assertEquals((double)-9999.9,(double)JavaUtils.Min(tabd),0.0000000001);
		tabd=new Double[0];
		assertEquals(-1,JavaUtils.Min(tabd),0.0000000001);
		tabd=null;
		assertEquals(-1,JavaUtils.Min(tabd),0.0000000001);
	}

	/**
	 * Test method for {@link com.zoubworld.utils.JavaUtils#saveAs(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testSaveAsStringString() {
		String datatoSave=JavaUtils.read("res/test/int.txt");
		JavaUtils.saveAs("res/result.test/tmp/int.txt", datatoSave);
		
		assertEquals(datatoSave, JavaUtils.read("res/result.test/tmp/int.txt"));
		 JavaUtils.saveAs((String)null,"");// no exception
	}

	/**
	 * Test method for {@link com.zoubworld.utils.JavaUtils#saveAs(java.lang.String, java.lang.String[])}.
	 */
	@Test
	public void testSaveAsStringStringArray() {
		String datatoSave[]= {JavaUtils.read("res/test/int.txt")};
		JavaUtils.saveAs("res/result.test/tmp/int.txt", datatoSave);
		
		assertEquals(JavaUtils.read("res/test/int.txt")+"\r\n", JavaUtils.read("res/result.test/tmp/int.txt"));
		JavaUtils.saveAs((String)null,datatoSave);// no exception
	}

	/**
	 * Test method for {@link com.zoubworld.utils.JavaUtils#read(java.lang.String)}.
	 */
	@Test
	public void testReadString() {
		assertEquals("128"+"\r\n", JavaUtils.read("res/test/int.txt"));
	/*	assertEquals(0, JavaUtils.readAndCount("res/test/int.txt","129"));
		assertEquals(1, JavaUtils.readAndCount("res/test/int.txt","12"));
		assertEquals(1, JavaUtils.readAndCount("res/test/int.txt","128"));*/
		assertEquals(null, JavaUtils.read((String)null));
	}

	
	/**
	 * Test method for {@link com.zoubworld.utils.JavaUtils#read(java.io.File)}.
	 */
	@Test
	public void testReadFile() {
		String s=JavaUtils.read(new File("res/test/ref/smallDir/int.txt"));
		
		assertEquals("128"+"\r\n", s);
		JavaUtils.saveAs("res/result.test\\tmp\\int2.txt",s);
		s=JavaUtils.read("res/result.test\\tmp\\int2.txt");
		assertEquals("128"+"\r\n", s);
		JavaUtils.AppendAs("res/result.test\\tmp\\int2.txt", s);
		s=JavaUtils.read("res/result.test\\tmp\\int2.txt");
		
		assertEquals("128"+"\r\n"+"128"+"\r\n", s);
		assertEquals(null, JavaUtils.read(new File("res/test/intNotExisting.txt")));
		assertEquals(2,JavaUtils.readAndCount("res/result.test\\tmp\\int2.txt", "12"));
		
		
		assertEquals("128"+"\r\n"+"128"+"\r\n", s);
		JavaUtils.saveAs("res/result.test\\tmp\\int2.txt",s.split("\r\n"));
		s=JavaUtils.read("res/result.test\\tmp\\int2.txt");
		assertEquals("128"+"\r\n"+"128"+"\r\n", s);
		
		
		
		s="res\\result.test\\tmp\\int2.txt";
		assertEquals("int2.txt", JavaUtils.fileWithExtOfPath(s));
		assertEquals("int2", JavaUtils.fileWithoutExtOfPath(s));
		assertEquals("res\\result.test\\tmp\\", JavaUtils.dirOfPath(s));
		
	//	assertEquals(null, JavaUtils.read((File)null));
		
		
		Set<String> ss=JavaUtils.listFileNames("res\\test\\", "", false, false/*, true*/); 
		
	}
	@Test
	public void testSort() {
		Set<String> ss= new HashSet();
		ss.add("azertyu");
		ss.add("qsdfghjkl");
		ss.add("qsdfcvvbn");
		List<String> ls=JavaUtils.asSortedSet(ss);
		assertEquals(ls.toString(),"[azertyu, qsdfcvvbn, qsdfghjkl]");
		ls=JavaUtils.asSortedList(ls);
		assertEquals(ls.toString(),"[azertyu, qsdfcvvbn, qsdfghjkl]");
		
	
	}
	/**
	 * Test method for {@link com.zoubworld.utils.JavaUtils#isWindows()}.
	 */
	@Test
	public void testIsWindows() {
		assertEquals(true,JavaUtils.isWindows());
	}

	/**
	 * Test method for {@link com.zoubworld.utils.JavaUtils#isMac()}.
	 */
	@Test
	public void testIsMac() {
		assertEquals(false,JavaUtils.isMac());
	}

	/**
	 * Test method for {@link com.zoubworld.utils.JavaUtils#isUnix()}.
	 */
	@Test
	public void testIsUnix() {
		assertEquals(false,JavaUtils.isUnix());
	}

	/**
	 * Test method for {@link com.zoubworld.utils.JavaUtils#isSolaris()}.
	 */
	@Test
	public void testIsSolaris() {
		assertEquals(false,JavaUtils.isSolaris());
	}

	/**
	 * Test method for {@link com.zoubworld.utils.JavaUtils#DirDelete(java.lang.String)}.
	 *
	@Test
	public void testDirDelete() {
		JavaUtils.newDir("res/result.test/tmp/","tmp2");
		File f=(new File("res/result.test/tmp/"+"tmp2"));
		assertEquals(true,f.isDirectory());
		assertEquals(true,f.exists());		
		JavaUtils.newDir("res/result.test/tmp/tmp2","tmp2");
		JavaUtils.DirDelete("res/result.test/tmp/"+"tmp2");
		f=(new File("res/result.test/tmp/"+"tmp2"));
		assertEquals(false,f.exists());		
	}*/

}
