/**
 * 
 */
package com.zoubworld.java.utils.compress.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.zoubworld.java.utils.compress.CompositeSymbol;
import com.zoubworld.java.utils.compress.ICode;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.utils.JavaUtils;

/** image of an unpacted file
 * @author zoubata
 *
 */
public class FileSymbol  {
	File file;
	String path=null;
	/**
	 * 
	 */
	public FileSymbol(File f) {
		file=f;		
	}
	public FileSymbol(File f,String mypath) {
		file=f;	
		path=mypath;
		if (path!=null)
		if (!path.endsWith(File.separator))
			path+=File.separator;
	}
	public FileSymbol(Path x) {
		file=x.toFile();	
		path=x.toString();
	}
	/* create a file*/
	static public File toFile( List<ISymbol> ls,String path)
	{		
		if (path==null)
			path=".";
		boolean valid=(ls.get(0)== Symbol.HOF);
		long time=((CompositeSymbol)ls.get(1)).getS2().getId();
		String sfilename=Symbol.listSymbolToString(HeaderOfFileToFilename(ls));
	//	sfilename=sfilename.trim();
		List<ISymbol> lsd=HeaderOfFileToDatas(ls);
		if (path!=null)
		{
			path=path.trim();
			if (!path.endsWith(File.separator))
				path+=File.separator;
			sfilename=path+sfilename;
		
		}
		path=sfilename.substring(0, sfilename.lastIndexOf(File.separator));
		
		File files = new File(path);
        if (!files.exists()) {
            if (files.mkdirs()) {
                System.out.println("Multiple directories are created!");
            } else {
                System.out.println("Failed to create multiple directories!");
            }
        }
		File f=new File(sfilename);
			
		try {
			f.createNewFile();
			FileOutputStream fo=new FileOutputStream(f);
			    OutputStream outputStream = new BufferedOutputStream(fo);
		   {
		    	for( ISymbol s:lsd)/*
		    	{
		    		if (s==Symbol.EOF)
		    			break;
		    		if (s!=null)
		    		if ( (s.getId()<256) && (0<=s.getId()))
		    		outputStream.write((int) s.getId());
		    	}*/
		    	{
		    		ICode i=s.getCode();
						if (i!=null)
					{
						int d=i.getLong().intValue();
						if(d>=128)
							d=d-256;
						outputStream.write( d);
					}
						}
		 //   outputStream.write(buffer,0,size);
		    	outputStream.close();
		    	fo.close();
		    }}
		  catch (IOException ex) {
		        ex.printStackTrace();
		}
		
		f.setLastModified(time);
		
		return f;
	}
	/** return the symbol list of data like DataToSymbol()
	 * */
	private static List<ISymbol> HeaderOfFileToDatas(List<ISymbol> ls) {
		int index=0;
		while(!ls.get(index).equals(Symbol.EOS))index++;
		
		return ls.subList(index+1, ls.size());
	}
	/** return the symbol list of filename from a Hof
	 * */
	public static List<ISymbol> HeaderOfFileToFilename(List<ISymbol> ls) {
		
		return ls.subList(3, ls.size());
	}
	public List<ISymbol> toSymbol()
	{
	
		List<ISymbol>  ls= HeaderToSymbol();
				ls.addAll(DataToSymbol());
		return ls;
	}
	/**
	 * */
	public	List<ISymbol> DataToSymbol()
		{
				List<ISymbol> ls=Symbol.factoryFile(file.getAbsolutePath());
		ls.add(Symbol.EOF);
		return ls;
	}
	static public List<ISymbol>  read(String fileName )
	{
		File f=new File(fileName);
		FileSymbol fs=new FileSymbol(f);
		return fs.toSymbol();
	}
	static public List<ISymbol> ExtractDataSymbol(List<ISymbol> fileSymmbol)
	{
		int fromIndex=fileSymmbol.indexOf(Symbol.EOS)+1;
		int toIndex=fileSymmbol.indexOf(Symbol.EOF);
		if (toIndex<0)
			toIndex=fileSymmbol.size();
		if (fromIndex<0)
			fromIndex=0;
		return fileSymmbol.subList(fromIndex,toIndex );
	}
	/** allow to save datasymbol uncompressed
	 * */
	static public void saveAs(List<ISymbol> dataSymmbol,String fileName )
	{
		
		File fileOut;

		if (fileName != null) {
			fileOut = new File(fileName);
		} else {
			System.err.println("error");
			return;
		}
		String dir=JavaUtils.dirOfPath(fileName);
		if (!JavaUtils.fileExist(dir))
			JavaUtils.mkDir(dir);
		try {
			FileOutputStream out = new FileOutputStream(fileOut);
			System.out.println("\t-  :save File As : " + fileOut.getAbsolutePath());
			
			for(ICode i:(SymbolToCode(dataSymmbol)))
				
				if (i!=null)
				{
					int d=i.getLong().intValue();
					if(d>=128)
						d=d-256;
					out.write( d);
				}

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);

		}

	}

	/** allow to save datasymbol uncompressed
	 * */
	static public void saveCompressedAs(List<ISymbol> dataSymmbol,String fileName )
	{
		
		File fileOut;

		if (fileName != null) {
			fileOut = new File(fileName);
		} else {
			System.err.println("error");
			return;
		}
		String dir=JavaUtils.dirOfPath(fileName);
		if (!JavaUtils.fileExist(dir))
			JavaUtils.mkDir(dir);
		try {
			FileOutputStream out = new FileOutputStream(fileOut);
			System.out.println("\t-  :save File As : " + fileOut.getAbsolutePath());
			
			for(ISymbol s:((dataSymmbol)))
			{
				ICode i=s.getCode();
				i.write(out);
				/*if (i!=null)
			{
				int d=i.getLong().intValue();
				out.write( d);
			}*/
			}

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);

		}

	}
	/**
	 * return the byte array of the file
	 * */
	static public	ICode[] SymbolToCode(List<ISymbol> ls)
	{
		ICode[] a=new ICode[ls.size()];
		int i=0;
		for(ISymbol s:ls)
			if(Symbol.EOF==s)
				return a;
			else
				if(s.getId()<256)
					a[i++]= s.getCode();
				else
					return null;			
	return a;	
}
		/*
	public	List<ISymbol> ToCompactedSymbol()
	{
		List<ISymbol> ls=toSymbol();
		return Symbol.CompactSymbol(ls);
	}*/
	List<ISymbol> HeaderToSymbol()
	{
		List<ISymbol> ls=new ArrayList<ISymbol>();	//ls.addAll(0, file.lastModified());
		/*
		Symbol s= new Symbol();
		s.setCode(new Code((long)file.lastModified()));
		CompositeSymbol cs=new CompositeSymbol(Symbol.INT64,s);
		CompositeCode cc=new CompositeCode(cs);
		cs.setCode(cc);
		ls.add(0, cs );
		*/
		ls.add( Symbol.HOF);
		ls.add(Symbol.FactorySymbolINT((long)file.lastModified()));
		ls.add(Symbol.FactorySymbolINT((long)file.length()));
		ls.addAll( Symbol.factoryCharSeq(file.getAbsolutePath().substring(path==null?0:path.length())));
		ls.add( Symbol.EOS);
		 return ls;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}	
	public Iterator<ISymbol>  getIterator() {		
		return toSymbol().iterator();
	}
	public Stream<ISymbol>  getStream() {	
		 return StreamSupport.stream(
	          Spliterators.spliteratorUnknownSize(getIterator(), Spliterator.ORDERED),
	          false);
	}
}
