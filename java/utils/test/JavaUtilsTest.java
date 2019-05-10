/**
 * 
 */
package com.zoubworld.java.utils.test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zoubworld.utils.JavaUtils;

/**
 * @author M43507
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
		
		JavaUtils.executeCommandColor("echo toto");
		assertTrue(true);
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
	//	fail("Not yet implemented");
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
		
		assertEquals(datatoSave+"\r\n", JavaUtils.read("res/result.test/tmp/int.txt"));
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
		assertEquals("128"+"\r\n", JavaUtils.read(new File("res/test/int.txt")));
	//	assertEquals(null, JavaUtils.read((File)null));
		
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
