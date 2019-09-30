/**
 * 
 */
package com.zoubworld.java.utils.test;

import static org.junit.Assert.*;

import java.io.File;
import java.math.BigDecimal;
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
import com.zoubworld.utils.JavaUtilList;
import com.zoubworld.utils.JavaUtils;
import com.zoubworld.utils.MathUtils;

import junit.framework.Assert;

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
/** @todo list
 * List<IParsable> ParseFile(List<IParsable> classList, File aFile, String MyMultiLine)
 <T,Number extends Comparable<Number>> Map<T, Number> SortMapByValue(Map<T, Number> map)
  newDir(String location, String NewDir)
  mkDir(String string) 
  fileExist(String fileName)
   void DirDelete(String dir)
  delay(int time, TimeUnit unit)
  
  List<IParsable> ParseFile(List<IParsable> classList, String filename)
   List<IParsable> ParseFile(List<IParsable> classList, String filename, String MyMultiLine)
   List<IParsable> ParseFile(List<IParsable> classList, File aFile, String MyMultiLine)
   List<IParsable> ParseFile(List<IParsable> classList, String[] Filelines, String MyMultiLine)
    static Map<IParsable, String[]> ExtractChapterFile(List<IParsable> classList, File aFile)
	static Map<IParsable, String[]> ExtractChapterFile(List<IParsable> classList, String[] Filelines)
	
	
	 int intValueOf(BufferedReader input) 
	 double doubleValueOf(BufferedReader input)
	 
	  List<U> convertList(List<T> from, Function<T, U> func)
	  convertArray(T[] from, Function<T, U> func, IntFunction<U[]> generator) 
	  
	  unzip(String zipFilePath, String destDir)
	  zipFiles(String[] srcFiles, String zipFile)
	   sevenZip(String SourceFilePath, String destZipFile)
	   sevenZipDir(String SourceFilePaths, String destZipFile)
	   sevenZip(String SourceFilePaths[], String destZipFile)
	    zipDir(String SourceFilePaths, String destZipFile)
		
		freeMemory(Float freefactor, Integer freeAbsoluteInByte)
		
		 memory()
		 
		 Map<String, Integer> CountElementInList(List<String> list)
		  Set<String> getelementFromFormula(String e2)

		  */
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
	public void testExcelArray() {
		/** @todo list:
		 * setSeparator(String separator)
		getSetOfColunm(String colunm )
		split(String colunmName)
		filter(String colunmName, String k)
		getValue(List<String> row, List<String> colunms)
		 getValue(List<String> row, String excelArraycolunm)
		 findiRow(List<String> ColumnTitle, List<String> cellValue)
		 read(String filename2, String sheetname)
		 addColumn(List<String> columnTitlelist)
		 addColumn(String[] columnsTitles) 
		 flush() 
		 RowtoMap(int indexrow)
		 sort(String colunm)
		 addFile(String fielname)
		 deleteColunm(String c)
		 renameColumn(String oldname, String newname)
		 moveColumn(String Columnname, int ilocationnewColumn)
		 copyColunm(String columnname, int ilocation,String newColumnname) 
		 */
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
	public void testSplit() {
		
		List<String> l1= new ArrayList();
		l1.add("01");
		l1.add("23");
		l1.add("\n");
		List<String> l2= new ArrayList();
		l2.add("45");
		l2.add("67");
		l2.add("\n");
		List<String> l3= new ArrayList();
		l3.add("89");
		l3.add("01");
		l3.add("\n");
		List<String> l4= new ArrayList();
		l4.add("01");
		l4.add("12");
		l4.add("\n");
		List<String> l= new ArrayList();
		l.addAll(l1);
		l.addAll(l2);
		l.addAll(l3);
		l.addAll(l4);
		List<List<String>> lR= new ArrayList();
		lR.add(l1);
		lR.add(l2);
		lR.add(l3);
		lR.add(l4);
		
		assertEquals(JavaUtils.split(l, "\n").size(),4);
		assertEquals(JavaUtils.split(l, "\n"),lR);
		
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
	 * Test method for {@link com.zoubworld.utils.JavaUtils#isUnix()}.
	 */
	@Test
	public void testJavaUtil() {
		
	
		assertEquals(JavaUtils.arround(0.8999999 ,1, BigDecimal.ROUND_UP), 0.9,0.1);
		assertEquals(JavaUtils.arround(8999999.0 ,1, BigDecimal.ROUND_HALF_EVEN), 9000000,0.1);
		 assertEquals(JavaUtils.arround(9.1,0, BigDecimal.ROUND_UP), 10.0,0.1);
		assertEquals(JavaUtils.arround(9.9,0, BigDecimal.ROUND_DOWN), 9.0,0.1);
		assertEquals(JavaUtils.arround(9.51,0, BigDecimal.ROUND_HALF_EVEN), 10.0,0.1);
		assertEquals(JavaUtils.arround(9.49,0, BigDecimal.ROUND_HALF_EVEN), 9.0,0.1);
			
	}
	/**
	 * Test method for {@link com.zoubworld.utils.JavaUtils#isUnix()}.
	 */
	@Test
	public void testJavaUtilList() {
		List<String> l=new ArrayList<String>();
		
		
		assertEquals(JavaUtilList.count(null),0);
		assertTrue(!JavaUtilList.IsNumberList(null));
		
		assertTrue(JavaUtilList.IsNumberList(l));
		
		
		
		l.add("0");
		l.add("9997");
		l.add("500");
		assertTrue(JavaUtilList.IsIntegerList(l));
		l.add("1.0");
		l.add("2.0");
		l.add("123.456789");
		l.add("376.543211");
		assertTrue(!JavaUtilList.IsIntegerList(l));
		
		
		assertEquals(JavaUtilList.count(l),l.size());
		List<Double> ld = JavaUtilList.StringToDoubleList(null);
		assertEquals(null,JavaUtilList.Average(ld));
		assertEquals(null,JavaUtilList.Max(ld));
		assertEquals(null,JavaUtilList.Min(ld));
		assertEquals(null,JavaUtilList.median(ld));
		assertEquals(null,JavaUtilList.StdDev(ld));
		
		assertEquals(null,JavaUtilList.Average(null));
		assertEquals(null,JavaUtilList.Max(null));
		assertEquals(null,JavaUtilList.Min(null));
		assertEquals(null,JavaUtilList.median(null));
		assertEquals(null,JavaUtilList.StdDev(null));
		
		
		
				ld=JavaUtilList.StringToDoubleList(l);
		assertEquals(JavaUtilList.count(l),ld.size());
		
		assertEquals(11000.0/l.size(),JavaUtilList.Average(ld),0.1);
		assertEquals(9997.0,JavaUtilList.Max(ld),0.1);
		assertEquals(0,JavaUtilList.Min(ld),0.1);
		assertEquals(123.456789,JavaUtilList.median(ld),0.1);
		assertEquals(3444.692109,JavaUtilList.StdDev(ld),0.1);
		assertTrue(JavaUtilList.IsNumberList(l));
		
		l.add("1.0");
		assertEquals(JavaUtilList.count(JavaUtilList.StringToDoubleSet(l)),l.size()-1);
		assertEquals(JavaUtilList.count(l,"1.0"),2);
		assertEquals(JavaUtilList.count(l,"0"),1);
			
		l.add("NA");
		assertTrue(JavaUtilList.IsNumberList(l));
		l.add("NA");
		
		
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
