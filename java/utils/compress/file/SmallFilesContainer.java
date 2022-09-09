/**
 * 
 */
package com.zoubworld.java.utils.compress.file;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.comparator.ExtensionFileComparator;
import org.apache.commons.io.comparator.SizeFileComparator;

import com.zoubworld.utils.ComposedComparator;
import com.zoubworld.utils.JavaUtils;
/**
 * @author Pierre Valleau
 *
 *small file container size<64ko, nb(file+dir)<4M

totlen(64);list[File header(i)]
	=> sort by size, and by containt=,entropie(same sym list; after same proba) (do a presort by extention to improve probability, after syze)
	  nb(32);
	  [list->(size(16)) ]
			=> sort by size allow RLE, and INC1 compression, increase proba of same data for RLE,len,repeat.
	[ list->(pos_in_steam(64)) ]
		=> allow doublons to point on same pos.
			X bad option cost n*64bit, while RLE,len,rep cost 3 symbol on match.
	  ;list(paramFixeLen)
	  
	  totlen(64);list(->path/filename) 
						=> sort by path allow dico compression; 
							=> replace by a tree, allow path compression. tree= Ddir Ffile1 Ff2 D.. Ddir2 ......
						=>noeud aproche : @father(32)D/Fname example 0D. 0Ddir1 1Ffile1 1Ffile2 0Ddir2 4Dsubdir2 
	  totlen(64);list(paramVariableLen)
	  
tot len(64);list[data(i)] //no end of file, file(i) position find with sum(0..i;size(j) on uncompres stream
	=> try RLE,len,rep (len=file len)
	=>dico_abs with stack
	=> remove size and put end_Of_File, save 16bit, cost 1 symbol(9bit),


 */
public class SmallFilesContainer {

	/**
	 * 
	 */
	public SmallFilesContainer() {
		// TODO Auto-generated constructor stub
	}
	List<File> files;
	public void sort()
	 {
		//files=(new SizeFileComparator()).sort(files );
		Collections.sort(files,
				new ComposedComparator(
					//	new ContainsFileComparator(),
					//	new SizeFileComparator(),
					//	new ExtensionFileComparator(),
					null
						));
	}
	public void save(String fileName)
	 {
		JavaUtils.FileDelete(fileName);
		JavaUtils.saveAs(fileName, toString());
	}
	public String toString( )
	 {
		String tmp="";
		for(File f:files)
			tmp+=f.length() +""+ f.getAbsolutePath()+"\n";
		for(File f:files)
			tmp+=JavaUtils.read(f).replaceAll("\r\n", "/")+"EOF\n";
		return tmp;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SmallFilesContainer d=new SmallFilesContainer();
		String path="C:\\Temp\\xfer\\acp_data\\ftp.acp.atmel.com\\data\\662A5_N08F\\";
		
		for(String f:JavaUtils.listFileNames(path, ".fir", false, true))
			d.add(new File(path+f));
		d.sort();
		System.out.println( d.toString( ));	
		d.save(path+"fir.txt");
	}
	public void add(File file) {
		if (files==null)
			files=new ArrayList<>();
		files.add(file);
		
	}

}
