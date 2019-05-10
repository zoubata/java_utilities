/**
 * 
 */
package com.zoubwolrd.java.utils.compress.file;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.zoubwolrd.java.utils.compress.CompositeSymbol;
import com.zoubwolrd.java.utils.compress.ISymbol;
import com.zoubwolrd.java.utils.compress.Symbol;

/** image of an unpacted file
 * @author zoubata
 *
 */
public class FileSymbol {
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
	/* create a file*/
	static public File toFile( List<ISymbol> ls,String path)
	{		
		
		boolean valid=(ls.get(0)== Symbol.HOF);
		long time=((CompositeSymbol)ls.get(1)).getS2().getId();
		String sfilename=Symbol.listSymbolToString(ls.subList(2, ls.size()));
		List<ISymbol> lsd=ls.subList(2+sfilename.length()+1, ls.size());
		if (path!=null)
		{
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
		    	for( ISymbol s:lsd)
		    	{
		    		if (s==Symbol.EOF)
		    			break;
		    		if ( (s.getId()<256) && (0<=s.getId()))
		    		outputStream.write((int) s.getId());
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
	
	public List<ISymbol> toSymbol()
	{
	
		List<ISymbol>  ls= HeaderToSymbol();
				ls.addAll(DataToSymbol());
		return ls;
	}
	public	List<ISymbol> DataToSymbol()
		{
				List<ISymbol> ls=Symbol.factoryFile(file.getAbsolutePath());
		ls.add(Symbol.EOF);
		return ls;
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

}
