package com.zoubworld.java.utils.compress.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zoubworld.java.utils.compress.CodeNumberSet;
import com.zoubworld.java.utils.compress.CodingSet;
import com.zoubworld.java.utils.compress.CompositeSymbols;
import com.zoubworld.java.utils.compress.HuffmanCode;
import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Number;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.algo.IAlgoCompress;
import com.zoubworld.java.utils.compress.algo.RLE;
import com.zoubworld.java.utils.compress.algo.TreeEncoding;
import com.zoubworld.utils.JavaUtils;

import clojure.lang.IFn.L;

public class TarFile {

	static public void saveArchiveAs(File archive,List<File> filesToSave)
	{
		BinaryStdOut bo=new BinaryStdOut(archive);
		bo.setCodingRule(new CodingSet(CodingSet.NOCOMPRESS));
		bo.write("ZJC");//header of file
		bo.write((byte) 0x00);//version
		SymbolBloc.Write(bo, getblockHeader(filesToSave));
		//bo.writes(getblockHeader(filesToSave));
		//bo.writes(getblockdata(filesToSave));
		SymbolBloc.Write(bo, getblockdata(filesToSave));
		bo.close();
		
	}
	static public void saveFilesAs(File archive,File path)
	{
		BinaryStdIn bi=new BinaryStdIn(archive);
		bi.setCodingRule(new CodingSet(CodingSet.NOCOMPRESS16));
		String Header=bi.readString();//header of file
		Byte version=bi.readByte();//version
		List<File> files=readblockHeader(bi);
		for(File f:files)
		{
			saveFile(f,bi);
		}
		bi.close();
		
	}
	private static void saveFile(File f, BinaryStdIn bi) {
		List<ISymbol> ls=new ArrayList<ISymbol> ();
		ISymbol Sym;
	/*	while((Sym=bi.readSymbol())!=Symbol.EOF)
			ls.add(Sym);*/
		ls=bi.readSymbols(Symbol.EOF);
		Symbol.toFile(f,new CodingSet(CodingSet.CODE8BITS), ls);
		
	}
	private static List<File> readblockHeader(BinaryStdIn bi) {
		Number NumberOfFiles=(Number) bi.readSymbol();//n:0..infinity
		int n=Number.getValue(NumberOfFiles).intValue();
		Number LinkIndicatorFileType[]=new Number[n];// tar : '0' File, '5',repertoire,\0 file, ...
		for(int i=0;i<n;i++)
			LinkIndicatorFileType[i]=(Number)bi.readSymbol();
		Number FileMode[]=new Number[n];;//drwxr-xr-x : 10 bit
		for(int i=0;i<n;i++)
			FileMode[i]=(Number)bi.readSymbol();
		Number OwnerNumericUserID[]=new Number[n];;//32bits 
		for(int i=0;i<n;i++)
			OwnerNumericUserID[i]=(Number)bi.readSymbol();
		Number GroupNumericUserID[]=new Number[n];;//32bits 
		for(int i=0;i<n;i++)
			GroupNumericUserID[i]=(Number)bi.readSymbol();
		Number LastModificationTimeInNumericUnixTimeFormat[]=new Number[n];;// 0 for Friday 1901-12-13, 00h00:00; 1 per seconde; 32/64bit word signed.
		for(int i=0;i<n;i++)
			LastModificationTimeInNumericUnixTimeFormat[i]=(Number)bi.readSymbol();
		List<ISymbol> OwnerUserName[]=new List[n];//((Symbol)+EOS)[n]
		for(int i=0;i<n;i++)
			OwnerUserName[i]=bi.readSymbols(Symbol.EOS);
		
		List<ISymbol> OwnerGroupName[]=new List[n];
		for(int i=0;i<n;i++)
			OwnerGroupName[i]=bi.readSymbols(Symbol.EOS);
		List<ISymbol> NameOfLinkedFile[]=new List[n];
		for(int i=0;i<n;i++)
			NameOfLinkedFile[i]=bi.readSymbols(Symbol.EOS);
		List<ISymbol> FileName[]=new List[n];//((Symbol)+EOS)[n]
		for(int i=0;i<n;i++)
			FileName[i]=bi.readSymbols(Symbol.EOS);
			
			 List<File> lf=new ArrayList<File> ();
		for(int i=0;i<n;i++)
		{
		File f=new File(Symbol.toString(FileName[i]));
		f.setLastModified(Number.getValue(LastModificationTimeInNumericUnixTimeFormat[i]));
		/*f.setExecutable(executable, ownerOnly)
		f.setWritable(writable, ownerOnly))
		f.setReadable(readable, ownerOnly)
		*/
		if (Number.getValue(LinkIndicatorFileType[i]).intValue()=='5')
		f.mkdirs();
		lf.add(f);
		}
		//[File size in bytes[];
		//[Checksum for header block];
		return lf;
	}
	private static List<ISymbol> getblockdata(List<File> filesToSave) {
		List<ISymbol> ls=new ArrayList<ISymbol> ();
		for(File f:filesToSave)
			ls.addAll(Symbol.from(f));	
		
		
		
		IAlgoCompress rle;
		
		
		/*	TreeEncoding tre=new TreeEncoding(6,Symbol.EOF);

		tre.reset();
		tre.init(ls);
		ls=tre.encodeSymbol(ls);
		
		tre.config(6,Symbol.findId('\n'));	*/	
		///TreeEncoding tre=new TreeEncoding(6,Symbol.EOF);		
		//TreeEncoding tre=new TreeEncoding(6,Symbol.findId(' '));		
		TreeEncoding tre=new TreeEncoding(6,Symbol.findId('\n'));
		tre.reset();
		tre.init(ls);
		ls=tre.encodeSymbol(ls);
	
		
		rle=new RLE(3L);
		rle.reset();
		rle.init(ls);
		ls=rle.encodeSymbol(ls);
		
		
		/*
		ICodingRule cs=new CodingSet(ls);
		ls.add(0,cs.toSymbol());
	*/
		return ls;
	}
	private static List<ISymbol> getblockHeader(List<File> filesToSave) {
		List<ISymbol> ls=new ArrayList<ISymbol> ();
		ls.add(new Number(filesToSave.size()));
		for(File f:filesToSave)
			ls.add(new Number(getLinkIndicatorFileType(f)));
		for(File f:filesToSave)
			ls.add(new Number(getFileMode(f)));
		for(File f:filesToSave)
			ls.add(new Number(getOwnerNumericUserID(f)));
		for(File f:filesToSave)
			ls.add(new Number(getOwnerNumericUserID(f)));
		for(File f:filesToSave)
			ls.add(new Number(getLastModificationTimeInNumericUnixTimeFormat(f)));
	/*	ICodingRule cs=new CodeNumberSet(ls);
		ls.add(0,cs.toSymbol());
		**/
		List<ISymbol> lss=new ArrayList<ISymbol> ();
		
		for(File f:filesToSave)
			lss.addAll((getOwnerUserName(f)));
		for(File f:filesToSave)
			lss.addAll((getOwnerGroupName(f)));
		for(File f:filesToSave)
			lss.addAll((getNameOfLinkedFile(f)));
		for(File f:filesToSave)
			lss.addAll((getFileName(f)));
		//compute coding set
		/*
		lss.add(Symbol.CodingSet);
		cs=HuffmanCode.Factory(lss);
		lss.remove(lss.size()-1);
		//add it
		ls.add(cs.toSymbol());
		*/
		ls.addAll(lss);
		
		IAlgoCompress rle=null;
		rle=new RLE(3L);
		rle.reset();
		rle.init(ls);
		ls=rle.encodeSymbol(ls);
		
	//	rle=new TreeEncoding(6,Symbol.findId(File.separatorChar));
		rle=new TreeEncoding(6,Symbol.EOS);
		
		rle.reset();
		rle.init(ls);
		ls=rle.encodeSymbol(ls);
		/*
		Map<ISymbol, Long> ms = ISymbol.Freq(ls);
		Map<Class, Long> mc = ISymbol.FreqClass(ls);
		System.out.println("CompositeSymbols : "+CompositeSymbols.getCompositeSymbols(ls));
		System.out.println("CompositeSymbol  : "+mc);
		List<List<ISymbol>> l1 = CompositeSymbols.flatter(ls, Symbol.NewWord);
		System.out.println("NewWord : "+l1.get(1).size()+" : "+l1.get(1));
		List<List<ISymbol>> l2 = CompositeSymbols.flatter(l1.get(0), Symbol.Word);
		System.out.println("Word : "+l2.get(1).size()+" : "+l2.get(1));
		List<List<ISymbol>> l3 = CompositeSymbols.flatter(l2.get(0), Symbol.RLE);
		System.out.println("RLE : "+l3.get(1).size()+" : "+l3.get(1));
		List<List<ISymbol>> l4 = CompositeSymbols.flatterClass(l3.get(0), new Number(0),Symbol.Number);
		System.out.println("Number : "+l4.get(1).size()+" : "+l4.get(1));
		System.out.println("Other : "+l4.get(0).size()+" : "+l4.get(0));
		mc = ISymbol.FreqClass(l4.get(0));
		 rle=new RLE(3L);
		rle.reset();
		rle.init(ls);
		List<ISymbol> l4e = rle.encodeSymbol(l4.get(0));
		System.out.println("l4e : "+l4e.size()+" : "+l4e);
		List<List<ISymbol>> l5 = CompositeSymbols.flatter(l4e, Symbol.RLE);
		System.out.println("RLE : "+l5.get(1).size()+" : "+l5.get(1));
		System.out.println("Other : "+l5.get(0).size()+" : "+l5.get(0));
		*/
		return ls;
	}
	private static List<ISymbol> getFileName(File f)  {
		List<ISymbol> ls=new ArrayList<ISymbol> ();
		try {
			ls.addAll(Symbol.from(f.getCanonicalPath()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ls.add(Symbol.EOS);
		return ls;
	}
	private static List<ISymbol> getOwnerGroupName(File f) {
		List<ISymbol> ls=new ArrayList<ISymbol> ();
		ls.add(Symbol.EOS);
		return ls;
	}
	private static List<ISymbol> getNameOfLinkedFile(File f) {
		List<ISymbol> ls=new ArrayList<ISymbol> ();
		ls.add(Symbol.EOS);
		return ls;
	}
	private static List<ISymbol> getOwnerUserName(File f) {
		List<ISymbol> ls=new ArrayList<ISymbol> ();
		ls.add(Symbol.EOS);
		return ls;
	}
	private static Long getLastModificationTimeInNumericUnixTimeFormat(File f) {
	/*	 try {
			FileTime timestamp = Files.getLastModifiedTime(Paths.get("/tmp/test"));
			timestamp.
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return f.lastModified();
	}
	private static Long getOwnerNumericUserID(File f) {
		// TODO Auto-generated method stub
		return 0L;
	}
	private static Long getFileMode(File f) {
		// TODO Auto-generated method stub
		return 0L;
	}
	private static Long getLinkIndicatorFileType(File f) {
		// TODO Auto-generated method stub
		return 0L;
	}
	public TarFile() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) {
		String path="C:\\Temp\\compression\\litle_file\\data1\\";
		//path="C:\\Temp\\compression\\litle_file\\data2\\";
		//path="C:\\Temp\\compression\\litle_file\\empty\\";
		System.out.println(path);
		Set<String> l = JavaUtils.listFileNames(path, null, false, false, true);
		List<File> filesToSave=new ArrayList<File>();
		for(String f:l)
			filesToSave.add(new File(path+f));
				TarFile.saveArchiveAs(new File(path.replaceFirst("\\\\$", ".zjc")), filesToSave);
	}

}
