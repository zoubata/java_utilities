package com.zoubworld.java.utils.compress.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.file.FilesSymbol;
import com.zoubworld.utils.JavaUtils;

public class FilesSymbolTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFilesSymbolSetOfFile() {

		File f1 = new File("res/test/smallfile.txt");		
		File f2 = new File("res/test/excel.csv");
		Set<File> fset=new HashSet<File>();
		fset.add(f1);
		fset.add(f2);
		
		FilesSymbol fs=new FilesSymbol(fset,f2.getAbsoluteFile().getParent());
		FilesSymbol fs1=new FilesSymbol(f1);
		FilesSymbol fs2=new FilesSymbol(f2);
		
		assertEquals(fs.toSymbol().size(),fs2.toSymbol().size()+fs1.toSymbol().size());
	
		File fd = new File("res/test/ref");
		FilesSymbol fds=new FilesSymbol(fd);
		assertEquals(fs.toSymbol().size()+"ref/".length()*2,fds.toSymbol().size()+8);
		fs=new FilesSymbol(fset,f2.getAbsoluteFile().getParent());
		assertTrue(fs.toSymbol().size()+"ref/".length()*2>fds.toSymbol().size()+5);
		
	}

	@Test
	public void testFilesSymbolFile() {
		
		File f = new File("res/test/smallfile.txt");		
		FilesSymbol fs=new FilesSymbol(f);
		assertEquals(fs.toSymbol().size(),4528);
		assertEquals(fs.toSymbol().size(),(f.length()+1+f.getAbsolutePath().length()-f.getAbsoluteFile().getParent().length()+2));
		assertTrue(fs.toSymbol().size()>=(f.length()+1+f.getAbsolutePath().length()-f.getAbsoluteFile().getParent().length()+2));
			
	}

	@Test
	public void testToFiles() {
		File f = new File("res/result.test/test/smallfile.txt");		
		File fr = new File("res/test/smallfile.txt");
		FilesSymbol fs=new FilesSymbol(f);
		List<ISymbol> ls=fs.toSymbol();
		fr.setLastModified(f.lastModified());
		f.delete();
		//f2.renameTo(f);
		long n=FilesSymbol.toFiles(ls, null).size();
		f = new File("res/result.test/test/smallfile.txt");		
		assertEquals(fr.length(),f.length());
		assertEquals(fr.lastModified(),f.lastModified());
		assertEquals(n,1);
		assertEquals(JavaUtils.read(f),JavaUtils.read(fr));
		
		File d = new File("res/test/small_ref");
		fs=new FilesSymbol(d);
	
		}

	@Test
	public void testToSymbol() {
		
		File fr = new File("res/test/ref/smallDir");
		FilesSymbol fsr=new FilesSymbol(fr);
		List<ISymbol> ls=fsr.toSymbol();
		FilesSymbol fs=new FilesSymbol(ls,"res/result.test/tmp/ref/smallDir");
		assertEquals(JavaUtils.read("res/test/ref/smallDir"+"smallfile.txt"),JavaUtils.read("res/result.test/tmp/ref/smallDir/"+"smallfile.txt"));
		assertEquals(JavaUtils.read("res/test/ref/smallDir"+"int.txt"),JavaUtils.read("res/result.test/tmp/ref/smallDir/"+"int.txt"));
		
		
	}

}
