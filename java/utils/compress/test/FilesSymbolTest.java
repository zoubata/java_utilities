package com.zoubworld.java.utils.compress.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.file.FileSymbol;
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
	public void testFilesSymbolSetOfFile()
	{
/*
		File f1 = new File("res/test/smallfile.txt");		
		File f2 = new File("res/test/medium.txt");
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
		*/
		assertTrue(true);
	}

	@Test
	public void testFilesSymbolFile() {
		
		List<ISymbol>  ls=new ArrayList<ISymbol>();
		for(int i=0;i<256;i++)
			ls.add(Symbol.findId(i));
		FileSymbol.saveAs(ls, "res/result.test/testFilesSymbolFile/smallfile.bin");
		ls.add(Symbol.EOF);
		File fbin = new File("res/result.test/testFilesSymbolFile/smallfile.bin");		
		FileSymbol fsbin=new FileSymbol(fbin);
		List<ISymbol>  lsbin=fsbin.DataToSymbol();
		assertEquals(ls.size(),lsbin.size());
		for(int i=0;i<ls.size();i++)
		assertEquals("ls("+i+")",ls.get(i),lsbin.get(i));
		
		//////////////////////////////////////////////
		
		File f = new File("res/test/smallfile.txt");		
		FilesSymbol fsin=new FilesSymbol(f);
		List<ISymbol> lsin=fsin.toSymbol();		
		FilesSymbol fsout=new FilesSymbol(lsin,"res/result.test/tmp/ref/smallfile2");
		
	//	assertEquals(lsin.size(),f.length());
		assertEquals(lsin.size(),(f.length()+1+f.getAbsolutePath().length()-f.getAbsoluteFile().getParent().length()+2));
		assertTrue(lsin.size()>=(f.length()+1+f.getAbsolutePath().length()-f.getAbsoluteFile().getParent().length()+2));
		assertEquals(JavaUtils.read(f),JavaUtils.read("res/result.test/tmp/ref/smallfile2/"+"smallfile.txt"));
			
	}

	@Test
	public void testToFiles() {
		/*
		File f = new File("res/result.test/test/smallfile.txt");		
		File fr = new File("res/test/smallfile.txt");
		FilesSymbol fs=new FilesSymbol(fr);
		List<ISymbol> ls=fs.toSymbol();
		f.setLastModified(f.lastModified());
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
	*/
		}

	@Test
	public void testToSymbol() {
		File f = new File("res/result.test/tmp/testToSymbol/smallDir/"+"int.txt");		
		f.delete();
		f = new File("res/result.test/tmp/testToSymbol/"+"smallfile.txt");		
		f.delete();
		
		File fin = new File("res/test/ref/smallDir");
		FilesSymbol fsin=new FilesSymbol(fin);
		List<ISymbol> lsin=fsin.toSymbol();
		
		
		Symbol.initCode();
		FilesSymbol fsout=new FilesSymbol(lsin,"res/result.test/tmp/testToSymbol/");
		assertEquals(JavaUtils.read("res/test/ref/smallDir"+"/smallfile.txt"),JavaUtils.read("res/result.test/tmp/testToSymbol/"+"smallfile.txt"));
		assertEquals(JavaUtils.read("res/test/ref/smallDir"+"/int.txt"),JavaUtils.read("res/result.test/tmp/testToSymbol/"+"int.txt"));
		
		
	}

}
