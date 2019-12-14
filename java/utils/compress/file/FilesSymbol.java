/**
 * 
 */
package com.zoubworld.java.utils.compress.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;

/** image of an unpacted list of file
 * @author zoubata
 *
 */
public class FilesSymbol {
	Set<File> file;
	String path=null;
	
	/**
	 * 
	 */
	public FilesSymbol(Set<File> f,String mypath) {
		file=expand(f);
		path=mypath;
		
	}
	public FilesSymbol(File f) {
		Set<File> f2=new HashSet<File>();
		f2.add(f);
		file=expand(f2);
		if (f.isDirectory())
			path=f.getAbsolutePath();
		else
		path=f.getAbsoluteFile().getParent();
		
		if (!path.endsWith(File.separator))
			path+=File.separator;
	}
	private Set<File> expand(Set<File> f1)
	{
		Set<File> f2=new HashSet<File>();
		for(File f:f1)
		{
			if (f.isDirectory())
			{
				Set<File> fs=new HashSet<File>();
				for(File ff:f.listFiles())
				fs.add(ff);
				f2.addAll(expand(fs));
			}	else if (f.isFile())
					f2.add(f);
				
		}
		return f2;
	}
	
	/** it store several file in several files
	 * */
	public FilesSymbol(List<ISymbol> ls,String mypath) {
		file=toFiles(  ls,mypath);
		path=mypath;
		
	}
		/* create  files from unpacked symbol
	 * 
	 * */
	static public Set<File>  toFiles( List<ISymbol> ls, String path)
	{
		Set<File> fs=new HashSet<File>();
		for(List<ISymbol> lsd:split(ls))
		{
			File f=FileSymbol.toFile(lsd,path);
			if (f!=null) 
				fs.add(f);
		}
		return fs;
	}
	/** split List<ISymbol> into several List<ISymbol> associated to each files
	 * */
	static public List<List<ISymbol>>  split( List<ISymbol> ls)
	{
		Set<File> fs=new HashSet<File>();
		int index=0;
		int size=0;
		long N=0;
		List<List<ISymbol>> ll=new ArrayList<List<ISymbol>>();
		while(ls.size()>0)
		{
			index=0;
			size=ls.indexOf(Symbol.EOF)+1;
			if(size<=0)
				size=ls.size();
			List<ISymbol> lsd=ls.subList(index,index+size);
			ll.add(lsd);
			
			ls=ls.subList(index+size,ls.size());
		}
		return ll;
	}
	/*
	 * /** Split the list of symbol on sub list to optimize the huffman algo on each list of symbol.
	 * 
	 * It is usefull hen the nature of the sequence of symbol isn'h homogen exemple, a text file foolowed by a binary file.
	 * followed by a csv tabe file. each sublist of symbol have it own entropy that was significantly different.
	 * 
	 * The order is keeped, to keep integrity of other compression method working at symbol level.
	 * *
	public static List<List<ISymbol>> split(List<ISymbol> ldec) {
// internal algo on each file analyse frequency and difference, split is difference is significative.
 
		List<List<ISymbol>> ll=new ArrayList<List<ISymbol>> ();
		int toIndex=ldec.indexOf(Symbol.EOF);
		while(toIndex>0)
		{
			
		ll.add( ldec.subList(0, toIndex));
		ldec=ldec.subList( toIndex+1,ldec.size());
		toIndex=ldec.indexOf(Symbol.EOF);
	}
		ll.add(ldec);
		return ll;
	}*/
	/*
	public	List<ISymbol> ToCompactedSymbol()
	{
		List<ISymbol> ls=toSymbol();
		return Symbol.CompactSymbol(ls);
	}*/
	public List<ISymbol> toSymbol()
	{
		List<ISymbol>  ls=new ArrayList<ISymbol>();
		for(File f:file) {
			FileSymbol fs=new FileSymbol(f,path);
			ls.addAll( fs.toSymbol());
		}
		return ls;
	}
		/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
			/* create  files from unpacked symbol
			 * 
			 * */
		public static Set<File> toFile(List<ISymbol> ls, ICodingRule cs, String path) 
			{
			if(ls==null)
				return null;
				Set<File> fs=new HashSet<File>();
				for(List<ISymbol> lsd:split(ls))
				{
					File f=FileSymbol.toFile(lsd,cs,path);
					if (f!=null) 
						fs.add(f);
				}
				return fs;
			}

}
