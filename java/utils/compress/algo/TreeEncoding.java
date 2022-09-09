/**
 * 
 */
package com.zoubworld.java.utils.compress.algo;

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
import com.zoubworld.java.utils.compress.blockSorting.FifoAlgo;
import com.zoubworld.utils.ArgsParser;
import com.zoubworld.utils.JavaUtils;

/**
 * @author Pierre Valleau
 * this algo use tree to define a dictionary.
 * 
 * This is mainly an generic algo and we can define a new algo by extedning this class and overriding  
 * - the tree initialisation with method init().
 * - the symbols with methods BuildeNewSymbol() BuildUseSymbol
 * - the detection of an element of dictionary with  IsNewWord()
 * - the decription of word with getSymbolListOf(wordsymbol, decrypted stream)
 * 
 */
public class TreeEncoding  implements IAlgoCompress {

	/** like ByteParEncoding() or ByteTripleEncoding() the TupleEncoding is similar for size=2,3.
	 * but allow any size.
	 *  
	 */
	public TreeEncoding() {
		
	}
	public TreeEncoding(int occurence, ISymbol splitsym) {
		Noccure=occurence;
		splitSymbol=splitsym;
	}
	int Noccure=32;
	/*public int readDictionary(List<ISymbol> lenc)
	{

		CompositeSymbols sdico=(CompositeSymbols)lenc.get(0);
		lenc=sdico.getSs();
		lenc=lenc.subList(1, lenc.size());
		
		int wsize=(int) lenc.get(0).getId();
		int dsize=(int) lenc.get(1).getId();
		int idico=0;
		ListBeginEnd<ISymbol> lenc2=new ListBeginEnd<ISymbol>(lenc,0,dsize*wsize+2-1);
		for (int fromIndex = 2; fromIndex < dsize*wsize+2; fromIndex+=wsize) {
			NodeTree n=root.add((ListBeginEnd<ISymbol>)lenc2.subList(fromIndex, fromIndex + wsize));
			n.setSymPacked(
					new CompositeSymbols(Symbol.Tuple, new Number(ldico.size()))
					);
			ldico.add(n);
			}
			
		return 1;//dsize*wsize+2;	
	}*/
	/*
	public   List<ISymbol> saveDictionary()
	{
		List<ISymbol> ld =new ArrayList<ISymbol>();
		//ld.add(Symbol.DICO);
		ld.add(new Number(getsize()));
		ld.add(new Number(ldico.size()));
		
		for(NodeTree n:ldico)
			ld.addAll(n.getList());
		return ld;
	}
	*/
	@Override
	public List<ISymbol> decodeSymbol(List<ISymbol> lenc) {
		List<ISymbol> ldec =new ArrayList<ISymbol>();
		
	/*	int i=readDictionary(lenc);
		lenc=lenc.subList(i, lenc.size());
		*/		
		Map<ISymbol,List<ISymbol>> m=new HashMap<ISymbol,List<ISymbol>>();
		/*for(NodeTree n:ldico)
			m.put(n.getSymPacked(), n.getList());
		*/
		for(ISymbol s:lenc)
		{
			CompositeSymbols cs=null;
			if (CompositeSymbols.class.isInstance(s))				
					
			{ cs=(CompositeSymbols) s;}
			
			boolean nw;
			if(nw=IsNewWord(cs))
			{
				List<ISymbol> ws = getSymbolListOf(cs, ldec);
				
				m.put(
						BuildUseSymbol(indexdico,null,cs),
						ws);
				//ldico.add(ws);
				indexdico++;
				
			}
			boolean uw;
			if(uw=IsUseWord(cs))
			{
			List<ISymbol> al=m.get(s);
			if (al!=null)
				ldec.addAll(al);
			else
					ldec.add(s);
			}
			else if (!nw && !uw)
				ldec.add(s);
				
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
	/** say if is a use words
	 * @param cs
	 * @return true if it is a use word for decode algo
	 */
	protected boolean IsUseWord(CompositeSymbols cs) {
		return (cs!=null) && Symbol.Word.equals(cs.getS0());
	}
	/** convert a word into a list of symbols.
	 * @param cs
	 * @param ldec
	 * @return return the list of symbol associated to the cs dictionary symbol.
	 */
	protected List<ISymbol> getSymbolListOf(CompositeSymbols cs, List<ISymbol> ldec) {
		int len=Number.getValue(cs.getS1()).intValue();
		
		List<ISymbol> ws=
		new ArrayList<ISymbol>();
		ws.addAll(ldec.subList(ldec.size()-len, ldec.size()));
		return ws;
	}

	@Override
	public List<ISymbol> encodeSymbol(List<ISymbol> ldec) {
		List<ISymbol> lse =new ArrayList<ISymbol>();
		int i=0;
		/*CompositeSymbols sdico=new CompositeSymbols(Symbol.DicoTuple,(List<ISymbol>)null);
		lse.add(sdico);*/
		for( i=0;i<ldec.size();)
		{
			int j=0;
			NodeTree node = root;
			NodeTree good = null;
			while (i+j<ldec.size() && (node=node.getChild(ldec.get(i+j)))!=null)
				{good=node;j++;}
		
			if (good==null || good.getPackedcount()==null)
			{
				/*for(int k=0;k<j;k++)
				lse.add(ls.get(i+k));*/
				lse.add(ldec.get(i));i++;
			/*	good.symPacked=new CompositeSymbols(Symbol.BTE, new Number(dico++));
				good.Packedcount=0L;*/
			}
			else
			{
				
				
					good.IncPackedcount();
					if (good.getSymPacked()==null)
					{
						//find already packed ancestor
				List<NodeTree> l=good.getListNode();
				NodeTree Packed=null;
				for(NodeTree g:l)
				{
					if (g.getSymPacked()!=null)
						Packed=g;	
				}
				
						if(Packed!=null)
							{
							lse.add(Packed.getSymPacked());
							lse.addAll(good.getList(Packed));
							}
							else
						lse.addAll(good.getList());
							
						CompositeSymbols ns =(CompositeSymbols) BuildNewSymbol(good);
						if(ns!=null)//embeded dico
							lse.add(ns);
						
						//else dico is store somewhere else
						good.setSymPacked(BuildUseSymbol(indexdico,good,null));
						ldico.add(good);
						indexdico++;
						if(ns==null)//no embeded dico
						{
							//refers to other sub list
							while((good=good.getParent())!=null)
								if ((good.getPackedcount()!=null) && (good.getSymPacked()==null))
								{
								good.setSymPacked(BuildUseSymbol(indexdico++,good,null));
							ldico.add(good);
								}
						}
							
					}
					else
					lse.add(good.getSymPacked());
					i+=j;
				
			}
		
		}
		/*
		//remove dictionnary entry not used
		ldico = JavaUtils.asSortedSet(ldico, NodeTree.compCountPacked);
		for(i=0;(i<ldico.size()) && (ldico.get(i).getPackedcount()==0);i++);
			ldico=ldico.subList(i,ldico.size());
		int dico=0;
		//renumbers word
		for(NodeTree n:ldico)
			((CompositeSymbols)n.getSymPacked()).setS1(new Number(dico++));
		// save dictionnary :
		sdico.addAll(saveDictionary());
		*/
		return lse;
	}
	/** build the symbol that represent the optimization based on node tree info and/or index of word
	 * @param good the tree node that represent the optimization.
	 * @param indexdico index in the dictionary of word represented by good.
	 * @param sympacked : the packed symbol use on compressed stream, should be the result of this function in some algo, when BuildNewSymbol is null, this parameter can be used during uncompress. 
	 * @return
	 */
	protected ISymbol BuildUseSymbol(int indexdico,NodeTree good,ISymbol sympacked) {
		return new CompositeSymbols(Symbol.Word, new Number(indexdico));
	}
	/** build a new entry inside the dictionary stored inside the symbol stream,
	 * 
	 * @param good the tree node that represent the optimization.
	 * @return null if the dictionary isn't store in the stream.
	 */
	protected ISymbol BuildNewSymbol(NodeTree good) {
		return new CompositeSymbols(Symbol.NewWord, new Number(good.getDeep()));
	}
	NodeTree root;
	List<NodeTree> ldico ;
	int indexdico=0;
	public void reset()
	{
		root = new NodeTree();
		ldico= new ArrayList<NodeTree> ();
		indexdico=0;
	}
	long param=0x000003L;	
	/*
	public TreeEncoding(int size,int limit) {
		param=size&0xff + (0xffff&limit)<<8;
	}*/
	private int getsize() {		
		return (int) (param&0xff);
	}
	private int getlimit() {		
		return (int) ((param>>8)&0xffff);
	}
	ISymbol splitSymbol=Symbol.findId('\n');
	/* build a tree of symbol
	 * */
	public void init(List<ISymbol> ls)
	{
		
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
	/*
	 * 
	  for (; (i < ldico.size()) && (ldico.get(i).getCount() <Noccure ); i++)
			;
		ldico = ldico.subList(i, ldico.size());
		Collections.reverse(ldico);
	//create dico:
	ldico = root.getLeafs(root);
	ldico = JavaUtils.asSortedSet(ldico, NodeTree.compCount);
	//assign symbol
	for(NodeTree n:ldico)
	{
		n.setSymPacked(new CompositeSymbols(Symbol.Tuple, new Number(indexdico++)));
	n.setPackedcount(0L);
}*/
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
		
		
		
		List<ISymbol> ls = Symbol.from(new File("C:\\Temp\\FAT\\2ndprobe\\NJLM4-14.pbs"));
		                                            //25849s:134403b
		IAlgoCompress enc=new BytePairEncoding();   //25849s:134021b
		enc=new RLE();                            //23013s:128947b
		//enc=new ByteTripleEncoding();             //18469s/111261b
	//	enc=new MultiAlgo(new RLE(),new ByteTripleEncoding());
													//16631s:107463b
		enc=new LZS();								//12474s: 54310b++ /=5296+3589+3589s
		List<ISymbol> lse=enc.encodeSymbol(ls);
		ISymbol.getEntropie(lse);
		System.out.println("ls="+ls.size()+":"+ls.size()*ISymbol.getEntropie(ls)+":"+ls);
		System.out.println("lse="+lse.size()+":"+lse.size()*ISymbol.getEntropie(lse)+":"+lse);
		System.out.println(JavaUtils.SortMapByValue(Symbol.Freq(lse)));
		List<ISymbol> ldec=enc.decodeSymbol(lse);
		System.out.println("ls="+ldec.size()+":"+ldec.size()*ISymbol.getEntropie(ldec)+":"+ldec);
		System.out.println(ldec.equals(ls));
		List<List<ISymbol>> streams=CompositeSymbols.flatter(lse,new Sym_LZS(0, 1));
		for(List<ISymbol> lst:streams)
		System.out.println(lst.size()+"\t:\t"+lst);
		List<ISymbol> ln=streams.get(1);//1
		Map<ISymbol, Long> fn = ISymbol.Freq(ln);
		ICodingRule cs= ICodingRule.Factory( ln);
		System.out.println(cs);
		System.out.println(JavaUtils.SortMapByValue(fn));
		System.out.println(Symbol.length(fn,cs)+"/"+ln.size());
		
		FifoAlgo fifo=new FifoAlgo();
		List<ISymbol> lne = fifo.encodeSymbol(ln);
		System.out.println("ln="+ln.size()+":"+ln.size()*ISymbol.getEntropie(ln)+":"+ln);
		System.out.println("lne="+lne.size()+":"+lne.size()*ISymbol.getEntropie(lne)+":"+lne);
		System.out.println("ln  H "+ISymbol.length(ln,cs)+"/"+ln.size());
		System.out.println(cs);
		cs= ICodingRule.Factory( lne);
		System.out.println("lne H "+ISymbol.length(lne,cs)+"/"+lne.size());
		cs= new CodeNumberSet(ln);
		System.out.println("ln  N "+ISymbol.length(ln,cs)+"/"+ln.size());
		cs= new CodeNumberSet( lne);
		System.out.println("lne N "+ISymbol.length(lne,cs)+"/"+lne.size());
		
		enc=new LZS();  
		lne=enc.encodeSymbol(ln);
		cs= new CodeNumberSet( lne);
		System.out.println("lnRLE N "+ISymbol.length(lne,cs)+"/"+lne.size()+":"+lne);
		System.out.println(ICodingRule.Factory( ln));

	}
	public void config(int occurence, ISymbol splitsym) {
		Noccure=occurence;
		splitSymbol=splitsym;
	}
}
