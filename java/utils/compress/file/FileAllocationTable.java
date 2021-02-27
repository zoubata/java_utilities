package com.zoubworld.java.utils.compress.file;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.algo.Tree;
import com.zoubworld.utils.JavaUtils;

public class FileAllocationTable {

	static final long bsStartRedundancy=0x526564756e64616eL;
	static final long bsStartIntegrity =0x496e746567726974L;
	static final long bsStartFAT       =0x46696c65416c6c6fL;
	static final long bsStopRedundancy =0x6e61646e75646552L;
	static final long bsStopIntegrity  =0x7469726765746e49L;
	static final long bsStopFAT        =0x6f6c6c41656c6946L;

	
	String Filename[];
	long date[];
	long attribute[];
	long size[];
	String path;
			
	public FileAllocationTable(String dir) {
		path=dir;//JavaUtils.dirOfPath(dir);
		Set<String> s = JavaUtils.listFileNames(dir, null, false, true,true);
		Filename=new String[s.size()];
		Filename=(String[]) s.toArray(Filename);
		date=new long[Filename.length];
		attribute=new long[Filename.length];
		size=new long[Filename.length];		
	}
	public String toString()
	{//-rw-r--r-- 1 M43507 1049089 26005 déc.  23  2019 graph.svg
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<Filename.length;i++)
			sb.append(""+attribute[i]+" "+size[i]+" "+date[i]+" "+Filename[i]+"\r\n");
		return sb.toString();
		
	}
	public FileAllocationTable(List<ISymbol>  ls)
	{
		FromSymbol(ls);	
	}
	public void FromSymbol(List<ISymbol>  ls)
	{
		int i=0;
		int nb=Symbol.getINTn(ls.get(i++)).intValue();
		Filename=new String[nb];
		date=new long[Filename.length];
		attribute=new long[Filename.length];
		size=new long[Filename.length];	
		int j;
		for(j=0;j<nb;j++)
		{			
			Filename[j]=Symbol.listSymbolToString(i,ls);
			i+=Filename[j].length()+1;
		}
		for(j=0;j<nb;j++)
			date[j]=Symbol.getINTn(ls.get(i++));
		for(j=0;j<nb;j++)
			attribute[j]=Symbol.getINTn(ls.get(i++));
		for(j=0;j<nb;j++)
			size[j]=Symbol.getINTn(ls.get(i++));		
	}
	
	public List<ISymbol> toDataSymbol()	
	{
		List<ISymbol> ls=new ArrayList<ISymbol>();
		for(String f:Filename)
		{
			ls.addAll(Symbol.from(new File(path+f)));		
		}
		return ls;	
	}
	
	public List<ISymbol> toSymbol()
	{
		List<ISymbol> ls=new ArrayList<ISymbol>();;
		ls.add(Symbol.FactorySymbolINT(Filename.length));
		for(String f:Filename)
		{
			ls.addAll(Symbol.from(f));
			ls.add(Symbol.EOS);
			}
		for(long l:date)
			ls.add(Symbol.FactorySymbolINT(l));
		for(long l:attribute)
			ls.add(Symbol.FactorySymbolINT(l));
		for(long l:size)
			ls.add(Symbol.FactorySymbolINT(l));			
		return ls;		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileAllocationTable fat=new FileAllocationTable("C:\\Temp\\test\\1\\1stprobe");
		List<ISymbol> ls = fat.toSymbol();
	//	ls=Symbol.from("ABCABCABCAABBCC");
		Tree<ISymbol> tree=new Tree<ISymbol>() ;
		tree.fullfill(ls,164);
		//System.out.println(tree.toStringAll());
		String s=("tree.NbLeaf()="+tree.NbLeaf()+"tree.getHeavy()="+tree.getHeavy()+" tree.size()="+tree.size());
		int j=tree.size();
		int h=tree.getHeavy();
				int i=tree.cleanup(tree,16,3);
		System.out.println(tree.toStringAll());
		System.out.println(s);
		System.out.println("tree.NbLeaf()="+tree.NbLeaf()+"tree.getHeavy()="+tree.getHeavy()+" tree.size()="+tree.size());
		System.out.println("ls  "+ls.size());
		//System.out.println(tree.returnTree().toStringAll());
		String t[][]=new String[fat.Filename.length][];
		i=0;
		for(String f:fat.Filename)
		{t[i++]=JavaUtils.read("C:\\Temp\\test\\1\\1stprobe\\"+f).split("\r\n");}
		for(i=0;i<100000;i++)
			for(j=0;j<fat.Filename.length;j++)
				if (t[j].length>i) System.out.println(t[j][i]);
				
	}
}
