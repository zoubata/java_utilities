package com.zoubworld.java.utils.compress.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zoubworld.java.utils.compress.file.FileCompacted;
import com.zoubworld.java.utils.compress.file.FilesSymbol;

public class FileCompactedTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testCompress() {
		FilesSymbol fs2=new FilesSymbol(new File("res\\test\\small_ref"));
		FileCompacted fc=new FileCompacted(fs2,"res\\result.test\\tmp\\small.z");
		fc.compress();
		assertEquals(true,(new File("res\\result.test\\tmp\\small.z")).exists());
		fc=new FileCompacted(new File("res\\result.test\\tmp\\small.z"));
		fc.expand("res\\result.test\\tmp\\umcompress\\");
		
	}

	@Test
	public final void testExpand() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testToFile() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testToSymbol() {
		fail("Not yet implemented"); // TODO
	}

}
