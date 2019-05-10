package com.zoubworld.java.utils.compress.file;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zoubworld.java.utils.compress.HuffmanCode;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.utils.ArgsParser;
/** image of compacted file
 * it can compress(generate a FileCompacted) or expand(generate FilesSymbol).
 * */
public class FileCompacted {
	
	static public	List<ISymbol> compress(List<ISymbol> ls)
	{
	
		return Symbol.CompactSymbol(ls);
	}
	static public	List<ISymbol> expand(List<ISymbol> ls)
	{
	
		return Symbol.ExpandSymbol(ls);
	}
	
	 public	void compress()
	{
		 List<ISymbol>  ls=fs.toSymbol();
		List<ISymbol> lsenc= compress(ls);
		toFile(  lsenc,fc.getAbsolutePath(), true, true);///args.getoption("huff"),args.getoption("huffauto")
	}
	 public	void expand()
	{
		 
		 
		 List<ISymbol> lsenc= toSymbol( fc);
		 List<ISymbol>  ls=expand(lsenc);
		 // FilesSymbol.split(ls);
		 //foreach =>new file
		 fs=new FilesSymbol(FilesSymbol.toFiles(ls,null),null);
		 
	
		 //getFs();
	}
	FilesSymbol fs;
	/** note it perform the unpack and the write
	 * */
	public FilesSymbol getFs() {
		if (fs==null)
			fs=new FilesSymbol(FilesSymbol.toFiles(expand(toSymbol( fc)),null),null);
		return fs;
	}
	File fc;
	/** to compact
	 * */
	public FileCompacted(FilesSymbol fs2,String filename) {
		fs2=fs;	
		fc=new File(filename);
	}
	
	/** to unpack
	 * */
	public 	FileCompacted(File fc2) {
		fc=fc2;
		fs=null;
		
	}
	/** Create a compacked file called "filename" with data of ldec that should come from FilesSymbol.
	 * so ldec can be several files image.
	 *  The compression is only an bit stream level method: HuffmanCode 
	 * */
	static public File toFile( List<ISymbol> ldec,String filename,boolean HuffNstore,boolean multipass)
	{
		File fc=new File(filename);
		HuffmanCode huff=new HuffmanCode();
		if(HuffNstore)
			huff.storeSymbol(ldec, new BinaryStdOut(fc));
		else
		{
			BinaryStdOut binout=new BinaryStdOut(fc);
			if (!multipass)
			huff.encodeSymbol(ldec,binout);
			else
			{
				for(List<ISymbol> ldecs:FilesSymbol.split(ldec))
					huff.encodeSymbol(ldecs,binout);
								
			}
		}
		return fc;
	}
	/** Create a list of data from a compacked file "f".
	 *  The decompression is only an bit stream level method: HuffmanCode 
	 *  so you get a list of symbol that can be several files. it will be manage with FilesSymbol.
	 * */
	static public List<ISymbol> toSymbol(File f)
	{
		HuffmanCode huff=new HuffmanCode();
		List<ISymbol> ls=huff.decodeSymbol( new BinaryStdIn(f.getAbsolutePath())    	);
		
		return ls;
	}
	ArgsParser args=null;
	
	public static void main(String[] args) {
	}
	public void getArgs()
	{
		if(args==null)
		{
	Map<String,String> optionparam= new HashMap<String,String>();
	//optionparam.put("PatternFile=.*"," regular expression to filter file to parse");
	//optionparam.put("Extention=.csv"," Extention to filter file to parse");
	//optionparam.put("Separator=,"," separator string on file between field");
	//optionparam.put("filtercol=1,2,3,4,5,6,7,8"," if action is filtercol, the list of colum number, if action is FILTERCOL, list of colunm name where space char is replace by \\s");
	//optionparam.put("Action"," list of action: \n\t- merge : merge several file\n\t- filtercol : perform filtering in file process\n\t- FILTERCOL : perform filtering in RAM process\n\t- STREAM : perform filtering in stream process(heavy file)\n\t- header : extrat header to get colunm number ");
	//optionparam.put("Dir","path to file(s)");
	//optionparam.put("outputfile","file to save");
	
	
	//optionparam.put("Separator=,"," separator string on file between field");
	//optionparam.put("LineFilter=.*"," (STREAM) regular expression on the string line to filter some row");
	//optionparam.put("ColNDelete="," (STREAM) list of Colunm number to delete (can't be use with ColNTranspose or ColNFilter)");
	//optionparam.put("ColDelete="," list of Colunm name to delete");
	//optionparam.put("ColFilter=.*"," list of Colunm name to keep, example \"ColFilter=toto,titi,tu tu,ta\"");
	//optionparam.put("ColNFilter=.*"," (STREAM) list of Colunm number(1st=0) to keep, example ColNFilter=0,1,2,3 (can't be use with ColNDelete or ColNTranspose)");
	//optionparam.put("ColNTranspose="," (STREAM) list of Colunm number(1st=0) to keep, example ColNFilter=0,1,2,3(can't be use with ColNDelete or ColNFilter)");
	//optionparam.put("Replace={}"," (STREAM) Replace=(a,b) will replace all a by b; Replace={{a,b},{a1,b1},{a2,b2}} will replace all a by b, all a1 by b1, all a2 by b2; a can be a regular expression ");
	//optionparam.put("Split="," (STREAM) Split=separator Split a cell into several one based on a separator");
	
	optionparam.put("Action","x : extract; a  : add");
	
	optionparam.put("+HUF"," add 1 huffman compression bitstream method");
	optionparam.put("-HUFAUTO"," add n huffman bitstream method, adapted to each peace of files");
	optionparam.put("-LZW"," add windows/dictionnary algo to compression symbols");
	optionparam.put("-RLE"," add redondant length encoding algo to compression symbols");
	optionparam.put("Extract="," (STREAM) Extract=regEx Split a cell into several one based on a regular expression");
	
	
	
	
	args=new ArgsParser(optionparam);	
	}}
}
