package com.zoubworld.java.utils.compress.blockSorting;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zoubworld.java.utils.compress.Number;
import com.zoubworld.java.math.SMath;
import com.zoubworld.java.utils.ListBeginEnd;
import com.zoubworld.java.utils.compress.CodeNumberSet;
import com.zoubworld.java.utils.compress.CompositeSymbols;
import com.zoubworld.java.utils.compress.ICodingRule;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.SymbolComplex.Sym_LZS;
import com.zoubworld.java.utils.compress.algo.IAlgoCompress;
import com.zoubworld.java.utils.compress.algo.NodeTree;
import com.zoubworld.java.utils.compress.algo.NodeTreeOrder;
import com.zoubworld.java.utils.compress.blockSorting.FifoAlgo;
import com.zoubworld.utils.ArgsParser;
import com.zoubworld.utils.JavaUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.zoubworld.java.math.SMath;
import com.zoubworld.java.utils.ListBeginEnd;
import com.zoubworld.java.utils.compress.CompositeSymbols;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Number;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.utils.JavaUtils;

/**
 * @author Pierre Valleau
 * this algo use tree to define a rank of order n.
 * 
 * This is mainly an generic algo and we can define a new algo by extedning this class and overriding  
 * - the tree initialisation with method init().
 * - the symbols with methods BuildeNewSymbol() BuildUseSymbol
 * - the detection of an element of dictionary with  IsNewWord()
 * - the decription of word with getSymbolListOf(wordsymbol, decrypted stream)
 * 
 */
public class RankTreeEncoding  implements IAlgoCompress {

	/** like ByteParEncoding() or ByteTripleEncoding() the TupleEncoding is similar for size=2,3.
	 * but allow any size.
	 *  
	 */
	public RankTreeEncoding() {
		
	}
	public RankTreeEncoding(int myOrderlevel) {
		orderlevel=myOrderlevel;
		
	}
	public RankTreeEncoding(ISymbol symbol, int myorderlevel) {
		orderlevel=myorderlevel;
		sprout=symbol;
	}
	ISymbol sprout=Symbol.findId(0);
	int orderlevel=1;
	@Override
	public List<ISymbol> decodeSymbol(List<ISymbol> lenc) {
		List<ISymbol> ldec =new ArrayList<ISymbol>();
		NodeTreeOrder node = root;
		for(int j=0;j<orderlevel;j++)
		{
			ISymbol s=lenc.get(j);
				
			ISymbol s2=node.updateOrder(s.getId(),sprout);
			node=(NodeTreeOrder)node.getChild(s2);
			ldec.add(s2);
		}
		
			
			for(int  i=orderlevel;i<lenc.size();i++)
			{
				
				ISymbol s2=lenc.get(i);
				node = root;
				for(int j=0;j<orderlevel;j++)
				{
					ISymbol s=ldec.get(j+i-orderlevel);
					long index=node.updateOrder(s);
					node=(NodeTreeOrder)node.getChild(s);				
				}
				{
					ISymbol s=lenc.get(i);
						
					 s2=node.updateOrder(s.getId(),sprout);
					node=(NodeTreeOrder)node.getChild(s2);
					
				}
				ldec.add(s2);
				
				
			}
			
		
		return ldec;
	}
	/** say if is a new words
	 * @param cs
	 * @return true if it is a new word for decode algo
	 */
	protected boolean IsNewWord(CompositeSymbols cs) {
		return (cs!=null) && Symbol.NewWord.equals(cs.getS0());
	}
	/** convert a word into a list of symbols.
	 * @param cs
	 * @param ldec
	 * @return return the list of symbol associated to the cs dictionnary symbol.
	 */
	protected ISymbol getSymbolOf(int index, NodeTreeOrder node) {
		/*
		List<ISymbol> ws=
		new ArrayList<ISymbol>();
		ws.addAll(ldec.subList(ldec.size()-len, ldec.size()));*/
		return null;
	}
	

	@Override
	public List<ISymbol> encodeSymbol(List<ISymbol> ldec) {
		List<ISymbol> lse =new ArrayList<ISymbol>();
		int i=0;
		NodeTreeOrder node = root;
		for( i=0;i<orderlevel;i++)
		{
			ISymbol s=ldec.get(i);
			long index=node.updateOrder(s);
			node=(NodeTreeOrder)node.getChild(s);
			lse.add(BuildUseSymbol(index, node));
		}
		if(orderlevel<=0)
			lse.addAll(ldec);
		else
		for( i=orderlevel;i<ldec.size();i++)
		{
			long index=-1;node = root;
			for(int j=0;j<=orderlevel;j++)
			{
				ISymbol s=ldec.get(j+i-orderlevel);
				index=node.updateOrder(s);
				node=(NodeTreeOrder)node.getChild(s);				
			}
			lse.add(BuildUseSymbol(index, node));		
		}		
		return lse;
	}
	/** build the symbol that represent the optimization based on node tree info and/or index of word
	 * @param good the tree node that represent the optimization.
	 * @param indexdico index in the dictionary of word represented by good.
	 * @return
	 */
	protected ISymbol BuildUseSymbol(long indexdico,NodeTreeOrder good) {
		return new Number(indexdico);
	}
	/** build a new entry inside the dictionary stored inside the symbol stream,
	 * 
	 * @param good the tree node that represent the optimization.
	 * @return null if the dictionary isn't store in the stream.
	 */
	protected CompositeSymbols BuildNewSymbol(NodeTreeOrder good) {
		return null;
	}
	NodeTreeOrder root;
	//List<NodeTreeOrder> ldico ;
	//int indexdico=0;
	public void reset()
	{
		root = new NodeTreeOrder(null);
	//	indexdico=0;
	}
	long param=0x000003L;	
	/* build a tree of symbol
	 * */
	public void init(List<ISymbol> ls)
	{
		/*
		// build the tree :
		if(splitSymbol!=null)
		for (List<ISymbol>  al:Symbol.Split(ls,splitSymbol)) {
			root.add(al);
			// if(fromIndex%100000==0) System.out.print(".");
		}
		else
			for (List<ISymbol>  al:Symbol.Split(ls,Noccure)) {
				root.add(al);
				// if(fromIndex%100000==0) System.out.print(".");
			}
				
	// keep only branch upper than n occurrence
	while (root.refactor(Noccure));
		
	// plan symbol
	for(NodeTree r:root.getLeafs(root))
		if (r.getCount()>Noccure)
			r.setPackedcount(0L);
*/
		}

	@Override
	public String getName() {
		
		return this.getClass().getSimpleName();
	}

	
	public static void main(String[] args)
	{
		
		
		Map<String,String> optionparam= null;
		ArgsParser myargs=null;
		
		optionparam= new HashMap<String,String>();
		// parameter "="
		optionparam.put("PatternFile=.*"," regular expression to filter file to parse");
		optionparam.put("Extention=.csv"," Extention to filter file to parse");
		optionparam.put("Separator=,"," separator string on file between field");
		optionparam.put("filtercol=1,2,3,4,5,6,7,8"," if action is filtercol, the list of colum number, if action is FILTERCOL, list of colunm name where space char is replace by \\s");
		// argument : "" "string"
		optionparam.put("Action"," list of action: \n\t- merge : merge several file\n\t- filtercol : perform filtering in file process\n\t- FILTERCOL : perform filtering in RAM process\n\t- header : extrat header");
		optionparam.put("Dir","path to file");
		optionparam.put("outputfile","file to save");
		// option"+" "-" "--"
		/*
		  Main operation mode:

  -A, --catenate, --concatenate   append tar files to an archive
  -c, --create               create a new archive
      --delete               delete from the archive (not on mag tapes!)
  -d, --diff, --compare      find differences between archive and file system
  -r, --append               append files to the end of an archive
  -t, --list                 list the contents of an archive
      --test-label           test the archive volume label and exit
  -u, --update               only append files newer than copy in archive
  -x, --extract, --get       extract files from an archive

		  */
		optionparam.put("--Create","create a new archive");
		optionparam.put("--concAtenate","append tar files to an archive");
		optionparam.put("--eXtract","extract files from an archive");
		optionparam.put("--Update","only append files newer than copy in archive");
		optionparam.put("--lisT","list the contents of an archive");
		optionparam.put("--Diff","find differences between archive and file systeme");
		
		            
		/* Local file name selection:

  -C, --directory=DIR        change to directory DIR
  -T, --files-from=FILE      get names to extract or create from FILE
  -X, --exclude-from=FILE    exclude patterns listed in FILE
      --add-file=FILE        add given FILE to the archive (useful if its name
                             starts with a dash)
      --exclude=PATTERN      exclude files, given as a PATTERN
      --exclude-backups      exclude backup and lock files
      --exclude-caches       exclude contents of directories containing
                             CACHEDIR.TAG, except for the tag file itself
      --exclude-caches-all   exclude directories containing CACHEDIR.TAG
      --exclude-caches-under exclude everything under directories containing
                             CACHEDIR.TAG
      --exclude-ignore=FILE  read exclude patterns for each directory from
                             FILE, if it exists
      --exclude-ignore-recursive=FILE
                             read exclude patterns for each directory and its
                             subdirectories from FILE, if it exists
      --exclude-tag=FILE     exclude contents of directories containing FILE,
                             except for FILE itself
      --exclude-tag-all=FILE exclude directories containing FILE
      --exclude-tag-under=FILE   exclude everything under directories
                             containing FILE
      --exclude-vcs          exclude version control system directories
      --exclude-vcs-ignores  read exclude patterns from the VCS ignore files
      --no-null              disable the effect of the previous --null option
      --no-recursion         avoid descending automatically in directories
      --no-unquote           do not unquote input file or member names
      --no-verbatim-files-from   -T treats file names starting with dash as
                             options (default)
      --null                 -T reads null-terminated names; implies
                             --verbatim-files-from
      --recursion            recurse into directories (default)
      --unquote              unquote input file or member names (default)
      --verbatim-files-from  -T reads file names verbatim (no escape or option
                             handling)
*/	
		optionparam.put("directory=DIR","change to directory DIR");
		optionparam.put("--recursion","recurse into directories (default)");

		/* Compression options:

  -I, --use-compress-program=PROG
                             filter through PROG (must accept -d)
  -J, --xz                   filter the archive through xz
  -Z, --compress, --uncompress   filter the archive through compress
  -a, --auto-compress        use archive suffix to determine the compression
                             program
  -j, --bzip2                filter the archive through bzip2
      --lzip                 filter the archive through lzip
      --lzma                 filter the archive through lzma
      --lzop                 filter the archive through lzop
      --no-auto-compress     do not use archive suffix to determine the
                             compression program
  -z, --gzip, --gunzip, --ungzip   filter the archive through gzip
      --zstd                 filter the archive through zstd
*/
		optionparam.put("--help"," this help");
		myargs=new ArgsParser(ArgsParser.class,optionparam);
		// parse it
		myargs.parse(args);
		if (!myargs.check())
			System.exit(-1);
		//use it
		myargs.getArgument(1);
		myargs.getParam("Separator");
		
		
		
	//	List<ISymbol> ls = Symbol.from(new File("C:\\Temp\\FAT\\2ndprobe\\NJLM4-14.pbs"));
		
	}
	
}
