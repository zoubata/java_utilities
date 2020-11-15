/**
 * 
 */
package com.zoubworld.java.utils.compress.algo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.zoubworld.java.utils.compress.HuffmanCode;
import com.zoubworld.java.utils.compress.HuffmanCode.HuffmanNode;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.java.utils.compress.file.FileSymbol;
import com.zoubworld.utils.JavaUtils;

/**
 * @author pierre valleau
 *
 */
public class PatternCompress {

	/**
	 * 
	 */
	public PatternCompress() {
		
		 
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		FFT(Symbol.from(LZWBasic.file));
		FFT(Symbol.from(file));
		
		List<ISymbol> ls;
		List<ISymbol> lst;
		System.out.println(lst=transpose(ls=Symbol.from(file2),45));
		Map<ISymbol, Map<Long, Long>> m = FreqDist(ls);
		Map<Long, Long> mr = FreqDistSum(m);
		long count=0;
		for(Long dist:mr.keySet())
		{
			Long l = mr.get(dist);
			if(l!=null)
			count+=l;
		}
		long rle=mr.get(1L);
		RLE rle2=new RLE(3);
		List<ISymbol> lste = rle2.encodeSymbol(lst);
		List<ISymbol> lse = rle2.encodeSymbol(ls);
		System.out.println("lst size"+lst.size()+"/"+lste.size()+"rle size "+lse.size()+"rle size #"+"  size="+count+" : rle="+rle+": newsize~="+(count-rle+3*m.keySet().size()));
		System.out.println((lst));
		System.out.println((lste));
			ls=transpose(lst,lst.size()/45);
		System.out.println(Symbol.listSymbolToString(ls));
		
	}
	static public  List<ISymbol> transpose(List<ISymbol>  ls, int rowsizeX)
	{
		
		int size=ls.size();
		int Y=size/rowsizeX;
	List<ISymbol> lso=new ArrayList<ISymbol>();
	int i=0;
	for(int index=0;index<size;index++)
	{
		ISymbol e=null;
		e=ls.get(i);
		if(e==null)
			e=Symbol.Empty;
		lso.add(e);
		i+=rowsizeX;
		if (i>=size)
			i=i-size+1;//new collumn;
	}
	
	return lso;
	
	}
	/** build the freq of symbol from the freq of distance per symbol.
	 * */
	static public  Map<ISymbol, Long> Freq(Map<ISymbol, Map<Long, Long>> m)
	{
		Map<ISymbol, Long> freq=new HashMap<ISymbol, Long>();
		Long sum=0L;
		System.out.println("Sym"+" : "+"count");
		for(ISymbol k:m.keySet())
		{
			Map<Long, Long> mm = m.get(k);
			Long count=0L;
		for(Long c:mm.values())
		 {
			count+=c;
		 }
		sum+=count;
		System.out.println(k+" : "+count);
		freq.put(k, count);
		}
		double e=HuffmanCode.getEntropie(freq);
		System.out.println("Symbols : "+m.keySet().size()+" / " +sum+" symbols");
		System.out.println("entropie(sym) : "+e+" : " +sum*e+" bits");
		
		return freq;
	}
	/** from symbol list it build the freq of distance per symbol
	 * */
		static public  Map<ISymbol, Map<Long, Long>> FreqDist(List<ISymbol>  ls)
		{
		int nb=Symbol.getNbSymbol();
		Long index[]=new Long[nb];
		 Map<ISymbol, Map<Long, Long>> m=new HashMap<ISymbol, Map<Long, Long>>();
		 /*for(int i=0;i<nb;i++)
		 {
			 index[i]=null;
			 m.put(Symbol.findId(i), new HashMap<Long, Long>());			 
		 }*/
		 long count=0;
		 for(ISymbol s:ls)
		 {
			 if(index[(int)s.getId()]!=null)
			 {
			 Map<Long, Long> mm = m.get(s);
			 if(mm==null)
				m.put(s,  mm=new HashMap<Long, Long>());
			 long dist=count-index[(int)s.getId()];
			 
			 Long lc=mm.get(dist);
			 if (lc==null)
				 lc=0l;
			 else
				 lc++;
			 mm.put(dist, lc);
			 }
			 index[(int)s.getId()]=count;
			 count++;
		 }
		 for(ISymbol s:m.keySet())
		 {
			 Set<Long> t = new HashSet<Long>();
				t.addAll( m.get(s).keySet());
			 for(Long dist:t)
			 {
				 if (m.get(s).get(dist)==0)
					 m.get(s).remove(dist);
			 }
		 }
	return m;
	}
	/** regarding the concept it is a little bit like fft for mp3,
	 * but we will speak about periodicity instead of frequency, occurency instead of amplitude
	 * the periodicity is the distance between 2 times that a symbol happen.
	 * the occurence is the number of times where this periodicity happens.	 * 
	 * the goal is to catch the periodicity that happen the more often for all symbol to find the structure of the file.
	 * after we extract a pattern with fix field, and variable area.
	 * 
	 * **/
	static public void FFT(List<ISymbol>  ls)
	{
		int nb=Symbol.getNbSymbol();
		/*	Long index[]=new Long[nb];
		 Map<ISymbol, Map<Long, Long>> m=new HashMap<ISymbol, Map<Long, Long>>();
		 for(int i=0;i<nb;i++)
		 {
			 index[i]=null;
			 m.put(Symbol.findId(i), new HashMap<Long, Long>());			 
		 }
		 long count=0;
		 for(ISymbol s:ls)
		 {
			 if(index[(int)s.getId()]!=null)
			 {
			 Map<Long, Long> mm = m.get(s);
			 long dist=count-index[(int)s.getId()];
			 
			 Long lc=mm.get(dist);
			 if (lc==null)
				 lc=0l;
			 else
				 lc++;
			 mm.put(dist, lc);
			 }
			 index[(int)s.getId()]=count;
			 count++;
		 }*/
		 Map<ISymbol, Map<Long, Long>> m=FreqDist(  ls);
		 /* double sum=0.0;
		 Map<Long, Long> mr =new  HashMap<Long, Long>();
		 for(int i=0;i<nb;i++)
		 {
			 Map<Long, Long> mm = m.get(Symbol.findId(i));			 
			 m.put(Symbol.findId(i), mm=JavaUtils.SortMapByValue(mm));	
			 double e=HuffmanCode.getEntropie(mm);
			 System.out.println(Symbol.findId(i)+":"+e+":"+mm);
			sum+=e*m.keySet().size();
			for(Long k:mm.keySet())
			 {
				 Long lc=mm.get(k);
				 Long lr=mr.get(k);
				 if(lc==null)
					 lc=0L;
				 if(lr==null)
					 lr=0L;
				 lr+=lc;
				 mr.put(k, lr);
			 }
		 }
		 mr=JavaUtils.SortMapByValue(mr);
		 System.out.println("e="+sum+":"+mr);
		*/
		 Map<Long, Long> mr=FreqDistSum(m) ;
		 Freq(m);
				 //identify the best periodicity.
		long period=0;
		long heavy=0;
		 for(Long k:mr.keySet())
			 {
			 if(k*mr.get(k)>heavy)
			 {
				 period=k;
				 heavy=k*mr.get(k);
			 }
		}
		 System.out.println("period : "+period);
		 //identify separator symbol
		 ISymbol sym=null;
		long repeat=0;
		 for(int i=0;i<nb;i++)
		 {
			 Map<Long, Long> mm = m.get(Symbol.findId(i));	
			 if(mm!=null)
			 {
			Long l = mm.get(period);
			if(l!=null)
			if(l>=repeat)
			{
				repeat=l;
				sym=Symbol.findId(i);
				
			 System.out.println("symbol("+Symbol.findId(i)+") : "+l);
			}
			
		 }}
		 System.out.println("separator : "+sym);
		 //find pattern
		 long indexs=0L;
		 Long pos=0L;
		 Long posold=0L;
		 List<List<ISymbol>>  lss=new ArrayList<List<ISymbol>>();
		 List<List<ISymbol>>  filelss=new ArrayList<List<ISymbol>>();
		 //split the file
				for(ISymbol s:ls)
			{
				if(s.equals(sym))
					pos=indexs;
				indexs++;
				if(posold!=pos )
				{
					if(pos-posold==period)//match
					{
						List<ISymbol> ls1 = ls.subList(posold.intValue(), pos.intValue());
						lss.add(ls1);
						filelss.add(ls1);
				//	System.out.println("match : "+ls1);
					} 
					else
					{
						List<ISymbol> ls1 = ls.subList(posold.intValue(), pos.intValue());
						filelss.add(ls1);
				
					//	System.out.println("garbage : "+ls1);
					}
				}
				posold=pos;
			}
			//learn the pattern.	
			List<ISymbol> pattern=new ArrayList<ISymbol>();
			pattern.addAll(lss.get(0));
			for(List<ISymbol> ls1:lss)
			{
				for(int i=0;i<pattern.size();i++)
					if(!pattern.get(i).equals(Symbol.Wildcard))
					if(!pattern.get(i).equals(ls1.get(i)))
					{
					
						pattern.remove(i);
						pattern.add(i,Symbol.Wildcard);						
					}
			}
			int nbcount=0;
			
			for(ISymbol w:pattern)
				if(w.equals(Symbol.Wildcard))
					nbcount++;
			System.out.println("pattern "+nbcount+"/"+pattern.size()+": "+pattern);
			for(int i=0;i<filelss.size();i++)
			{
				List<ISymbol> ls1=filelss.get(i);
				List<ISymbol> ls2=new ArrayList<ISymbol>();
				int idx=0;
				ls2.add(Symbol.PATr);
				boolean bmatched=true;
				if(ls1.size()!=pattern.size())
					bmatched=false;
				else
				for(ISymbol w:pattern)
				{
					if(w.equals(Symbol.Wildcard))
						ls2.add(ls1.get(idx));
					else
						if (!w.equals(ls1.get(idx)))
							bmatched=false;
						idx++;
				}	
				if(bmatched)
				{
					filelss.remove(i);
					filelss.add(i, ls2);
					System.out.println("line "+": "+ls2);
							
					}
				
			}
			pattern.add(0,Symbol.PATr);
			filelss.add(0,pattern);
			// replace by the pattern.
				
	}
	/** from the freq of distance per symbol, it build a summary of frequency of distance overall symbol.
	 * 
	 * */
	
	private static Map<Long, Long> FreqDistSum(Map<ISymbol, Map<Long, Long>> m) {
		int nb=Symbol.getNbSymbol();
		 double sum=0.0;
		 Map<Long, Long> mr =new  HashMap<Long, Long>();
		 Long countSym=0L;
		 for(int i=0;i<nb;i++)
		 {
			 Map<Long, Long> mm = m.get(Symbol.findId(i));
			 if (mm!=null)
			 {
			 mm=JavaUtils.SortMapByValue(mm);
			 m.put(Symbol.findId(i), mm);	
			 double e=HuffmanCode.getEntropie(mm);
			Long localcountSym=0L;
			for(Long dist:mm.keySet())
			 {
				 Long lc=mm.get(dist);
				 Long lr=mr.get(dist);
				 if(lc==null)
					 lc=0L;
				 if(lr==null)
					 lr=0L;
				 lr+=lc;
				 mr.put(dist, lr);
				 localcountSym+=lc;
			 }
			countSym+=localcountSym;
			sum+=e*localcountSym;//count bits
			System.out.println(Symbol.findId(i)+":"+e+":"+mm.keySet().size()+"/"+localcountSym+":"+mm);
				
		 }}
		 mr=JavaUtils.SortMapByValue(mr);
		 System.out.println("sum :"+mr.keySet().size()+"/"+countSym+":"+mr);
		
			System.out.println("Symbols : "+m.keySet().size()+" / " +countSym+" symbols");
			System.out.println("entropie(dist) : "+"?"+" : " +sum+" bits");

		return mr;
	}
	public static void main3(String[] args) {
		String filename="res/test/small_ref/pat.txt";
		PatternCompress cmp=new PatternCompress();
	 List<ISymbol>  ls=FileSymbol.read(filename);
	 
	 
	 
	 List<List<ISymbol>>  lss=JavaUtils.split(ls,Symbol.findId('\n'));
	 //map(line,len)
  Map<List<ISymbol>, Long> m = lss.stream().collect(
        Collectors.groupingBy(Function.identity(),Collectors.counting()));
  m=JavaUtils.SortMapByValue(m);
	
  //map(len,count)
   Map<List<ISymbol>, Integer> n = m.keySet().stream()
		.collect(Collectors.toMap(Function.identity(), List::size));
	n=JavaUtils.SortMapByValue(n);
	Map<Integer, Long> o = n.values().stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	o=JavaUtils.SortMapByValue(o);
	System.out.println(m);//line, count
	System.out.println(n);//line,len
	System.out.println(o);// len count =>42,21,20
	
	o = o.entrySet().stream()
	.filter(x -> x.getValue() >= 3)
	.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
	{
	final Set<Integer> lo=o.keySet();
	n = n.entrySet().stream()
			.filter(x -> lo.contains(x.getValue()) )
			.collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
	
	for(Integer l:lo)
	{
		List<List<ISymbol>> n1 = n.entrySet().stream()
				.filter(x -> l.equals(x.getValue()) )
				.map(x->x.getKey())
				.collect(Collectors.toList());
		 Stream<List<Map<ISymbol,Integer>>> n2 = n1.stream()
		.map(x->x.stream().map(y->{Map<ISymbol,Integer> t=new HashMap<ISymbol,Integer>();t.put(y,(Integer)1);return t;} ).collect(Collectors.toList()));
		 n2.collect(Collectors.toList());
		/*@todo finish : Collector.of( () -> new ArrayList<Map<ISymbol,Integer>>(),
				 (result, article) -> {for(int i=0;i<article.size();i++) {
					 Map<ISymbol,Integer> m1=result.get(i);
					 Map<ISymbol,Integer> m2=article.get(i);
					 m2.forEach((k, v) -> m1.merge(k, v, (v1, v2) ->((new Integer( v1 + v2)))));
					
					 }
						 }
						 result[0] += article.getWordCount(),)*/
		;
				;	
	}
	}
	/*
	 * 
	n.filter(value=42).findstructure
	List<histo(ysm,count))=n.filter(value=42).do
	list(	sym::filter(count=1),wildcard)*/
	 List<ISymbol>  lsc=cmp.compress(ls);
	
	System.out.println(Symbol.PrintFreq(ls));
	Map<ISymbol, Long> mfreq = Symbol.FreqId(lsc);
	HuffmanCode hc= new HuffmanCode();
	HuffmanNode rootnode = hc.buildTrie(mfreq);
	hc.buildCode(rootnode, "");
	long size=hc.getSize(mfreq);
	System.out.println(ls.size()+"<=>"+lsc.size()+"<=>"+size);
	System.out.println(hc.codesToString(mfreq));
	FileSymbol.saveCompressedAs(lsc, "res/result.test/test/small_ref/pat.pie");
	JavaUtils.saveAs("res/result.test/test/small_ref/pat.pietxt",lsc.stream().map(Object::toString).collect(Collectors.joining(",")));
//	JavaUtils.saveAs("res/result.test/test/small_ref/pat.tree",cmp.getTree().toString());
	
	List<ISymbol>  lsf=cmp.uncompress(lsc);
	FileSymbol.saveAs(FileSymbol.ExtractDataSymbol(lsf), "res/result.test/test/small_ref/pat.txt");
	
	}
	


public List<ISymbol> compress(List<ISymbol> ls)
{

	List<ISymbol> lsc=new ArrayList<ISymbol>();
	
/*	tree=new Tree();
	currentLeaf=tree.getRoot();
	index=0L;
	for(ISymbol s:ls)
	{
		List<ISymbol> sc=processcompress(s);
		if (sc!=null)
			lsc.addAll(sc);
		//1tree.add(0L,index,ls);
		//tree.add(index-1, s);
	}
	List<ISymbol> sc=processcompress(null);
	if (sc!=null)
		lsc.addAll(sc);*/
	return lsc;
}

public List<ISymbol> uncompress(List<ISymbol> ls)
{
	List<ISymbol> lsd=new ArrayList<ISymbol>();
	
	/*
	index=0L;
	n1=n2=null;
	uncompressedflux=new ArrayList();
	
	for(ISymbol s:ls)
	{
		List<ISymbol> sc=processUncompress(s);

	}*/
	return lsd;
}
static String file2=":10000000F0FF0320810800207D04002081040020EF\r\n" + 
		":1000100085040020890400208D0400209104002024\r\n" + 
		":100020000000000000000000000000009504002017\r\n" + 
		":1000300099040020000000009D040020A10400207D\r\n" + 
		":10004000A5040020A9040020AD040020B104002074\r\n" + 
		":10005000B5040020B9040020BD040020C104002024\r\n" + 
		":10006000C5040020C9040020CD040020D1040020D4\r\n" + 
		":10007000D5040020D9040020DD040020E104002084\r\n" + 
		":10008000E5040020E9040020ED040020F104002034\r\n" + 
		":10009000F5040020F9040020FD04002001050020E3\r\n" + 
		":1000A00005050020090500200D0500201105002090\r\n" + 
		":1000B00015050020190500201D0500202105002040\r\n" + 
		":1000C00025050020290500202D05002031050020F0\r\n" + 
		":1000D00035050020390500203D05002041050020A0\r\n" + 
		":1000E00045050020490500204D0500205105002050\r\n" + 
		":1000F00055050020590500205D0500206105002000\r\n" + 
		":1001000065050020690500206D05002071050020AF\r\n" + 
		":1001100075050020790500207D050020810500205F\r\n" + 
		":1001200085050020890500208D050020910500200F\r\n" + 
		":1001300095050020990500209D050020A1050020BF\r\n" + 
		":10014000A5050020A9050020AD050020B10500206F\r\n" + 
		":10015000B5050020B9050020BD050020C10500201F\r\n" + 
		":10016000C5050020C9050020CD050020D1050020CF\r\n" + 
		":10017000D5050020D9050020DD050020E10500207F\r\n" + 
		":10018000E5050020E9050020ED050020F10500202F\r\n" + 
		":10019000F5050020F9050020FD05002001060020DE\r\n" + 
		":1001A00005060020090600200D060020110600208B\r\n" + 
		":1001B00015060020190600201D060020210600203B\r\n" + 
		":1001C00025060020290600202D06002031060020EB\r\n" + 
		":1001D00035060020390600203D060020410600209B\r\n" + 
		":1001E00045060020490600204D060020510600204B\r\n" + 
		":1001F00055060020590600205D06002061060020FB\r\n" + 
		":1002000065060020690600206D06002071060020AA\r\n" + 
		":1002100075060020790600207D060020810600205A\r\n" + 
		":1002200085060020890600208D060020910600200A\r\n" + 
		":1002300095060020990600209D060020A1060020BA\r\n" + 
		":10024000A5060020A9060020AD060020B10600206A\r\n" + 
		":10025000B5060020B9060020BD060020C10600201A\r\n" + 
		":10026000C5060020C9060020CD060020D1060020CA\r\n" + 
		":10027000D5060020D9060020DD060020E10600207A\r\n" + 
		":10028000E5060020E9060020ED060020F10600202A\r\n" + 
		":10029000F5060020F9060020FD06002001070020D9\r\n" + 
		":1002A00005070020090700200D0700201107002086\r\n" + 
		":1002B0001507002079040020F0FF032081080020AA\r\n" + 
		":1002C0007D040020810400200000000000000000E8\r\n" + 
		":1002D00000200100020003000400050006000700E2\r\n" + 
		":1002E0005FEA00085FEA00095FEA000A5FEA000BC4\r\n" + 
		":1002F0005FEA000C02488546002000BFAFF3008093\r\n" + 
		":10030000F0FF032000F0BCBAAFF30080AFF3008031\r\n" + 
		":1003100000000000000000000000000000000000DD\r\n" + 
		":1003200000000000000000000000000000000000CD\r\n" + 
		":1003300000000000000000000000000000000000BD\r\n" + 
		":1003400000000000000000000000000000000000AD\r\n" + 
		":10035000000000000000000000000000000000009D\r\n" + 
		":10036000000000000000000000000000000000008D\r\n" + 
		":10037000000000000000000000000000000000007D\r\n" + 
		":10038000000000000000000000000000000000006D\r\n" + 
		":10039000000000000000000000000000000000005D\r\n" + 
		":1003A000000000000000000000000000000000004D\r\n" + 
		":1003B000000000000000000000000000000000003D\r\n" + 
		":1003C000000000000000000000000000000000002D\r\n" + 
		":1003D000000000000000000000000000000000001D\r\n" + 
		":1003E000000000000000000000000000000000000D\r\n" + 
		":1003F00000000000000000000000000000000000FD\r\n" + 
		":10040000000400202D070020190700201D070020F0\r\n" + 
		":1004100099070020000000208009002039040020F6\r\n" + 
		":100420003D040020000000FF27474E55432056277B\r\n" + 
		":10043000342E372E33000000704700BF704700BFD6\r\n" + 
		":100440004FF44043C4F201435A6912B14FF0FF30F8\r\n" + 
		":1004500058614FF44041C4F201418B6913B14FF030\r\n" + 
		":10046000FF328A614FF44040C4F20140C16911B1CA\r\n" + 
		":100470004FF0FF33C3617047FEE700BFFEE700BFE8\r\n" + 
		":10048000FEE700BFFEE700BFFEE700BFFEE700BFDC\r\n" + 
		":10049000FEE700BFFEE700BFFEE700BFFEE700BFCC\r\n" + 
		":1004A000FEE700BF704700BF704700BF704700BF46\r\n" + 
		":1004B000704700BF704700BF704700BF704700BF64\r\n" + 
		":1004C000704700BF704700BF704700BF704700BF54\r\n" + 
		":1004D000704700BF704700BF704700BF704700BF44\r\n" + 
		":1004E000704700BF704700BF704700BF704700BF34\r\n" + 
		":1004F000704700BF704700BF704700BF704700BF24\r\n" + 
		":10050000704700BF704700BF704700BF704700BF13\r\n" + 
		":10051000704700BF704700BF704700BF704700BF03\r\n" + 
		":10052000704700BF704700BF704700BF704700BFF3\r\n" + 
		":10053000704700BF704700BF704700BF704700BFE3\r\n" + 
		":10054000704700BF704700BF704700BF704700BFD3\r\n" + 
		":10055000704700BF704700BF704700BF704700BFC3\r\n" + 
		":10056000704700BF704700BF704700BF704700BFB3\r\n" + 
		":10057000704700BF704700BF704700BF704700BFA3\r\n" + 
		":10058000704700BF704700BF704700BF704700BF93\r\n" + 
		":10059000704700BF704700BF704700BF704700BF83\r\n" + 
		":1005A000704700BF704700BF704700BF704700BF73\r\n" + 
		":1005B000704700BF704700BF704700BF704700BF63\r\n" + 
		":1005C000704700BF704700BF704700BF704700BF53\r\n" + 
		":1005D000704700BF704700BF704700BF704700BF43\r\n" + 
		":1005E000704700BF704700BF704700BF704700BF33\r\n" + 
		":1005F000704700BF704700BF704700BF704700BF23\r\n" + 
		":10060000704700BF704700BF704700BF704700BF12\r\n" + 
		":10061000704700BF704700BF704700BF704700BF02\r\n" + 
		":10062000704700BF704700BF704700BF704700BFF2\r\n" + 
		":10063000704700BF704700BF704700BF704700BFE2\r\n" + 
		":10064000704700BF704700BF704700BF704700BFD2\r\n" + 
		":10065000704700BF704700BF704700BF704700BFC2\r\n" + 
		":10066000704700BF704700BF704700BF704700BFB2\r\n" + 
		":10067000704700BF704700BF704700BF704700BFA2\r\n" + 
		":10068000704700BF704700BF704700BF704700BF92\r\n" + 
		":10069000704700BF704700BF704700BF704700BF82\r\n" + 
		":1006A000704700BF704700BF704700BF704700BF72\r\n" + 
		":1006B000704700BF704700BF704700BF704700BF62\r\n" + 
		":1006C000704700BF704700BF704700BF704700BF52\r\n" + 
		":1006D000704700BF704700BF704700BF704700BF42\r\n" + 
		":1006E000704700BF704700BF704700BF704700BF32\r\n" + 
		":1006F000704700BF704700BF704700BF704700BF22\r\n" + 
		":10070000704700BF704700BF704700BF704700BF11\r\n" + 
		":10071000704700BF704700BF704700BF10B50C4660\r\n" + 
		":1007200000F020F8204600F01DF810BD08B500F0DC\r\n" + 
		":1007300013F8FEE74FF6FF724FF40053C4F2014383\r\n" + 
		":100740001146C0F20F01C0F27F024FF6FF701A612E\r\n" + 
		":1007500059619861DA61704708B50446204600F097\r\n" + 
		":1007600009F8FBE708B500F011F800F009F80007F8\r\n" + 
		":10077000FBD408BD4FF08843C3F81801704700BF91\r\n" + 
		":100780004FF08843D3F80401704700BF4FF088430F\r\n" + 
		":10079000C3F81C01704700BFDFF8D8C02D482E49B0\r\n" + 
		":1007A0002E4A2F4B2DE9F041DCF80080DFF8C8E03D\r\n" + 
		":1007B0002C4F2D4E2D4D2E4C08F58058CCF8008036\r\n" + 
		":1007C000DEF800C00CF58058CEF80080D7F800E0C5\r\n" + 
		":1007D0000EF5805CC7F800C0376807F580573760B2\r\n" + 
		":1007E0002E6806F580572F6025781035EEB22670FA\r\n" + 
		":1007F00004781034E7B2077008781030C5B20D7075\r\n" + 
		":1008000011781031CEB216701A781032D4B21C7032\r\n" + 
		":100810000023312B4FF089474FF4007506DD4BF66E\r\n" + 
		":10082000BE20CCF6FE20FFF797FF002397F8490083\r\n" + 
		":100830006FF3000087F84900BD607D6197F84910AB\r\n" + 
		":1008400001336FF30001312B87F84910BD60BD61A2\r\n" + 
		":10085000E5DCEBE77E090020640900206509002043\r\n" + 
		":100860007C0900207009002074090020780900200C\r\n" + 
		":10088000002000210022002300240025002600274C\r\n" + 
		":10089000804681468246834684461A481A4B9842CF\r\n" + 
		":1008A00017D2DA1C041D111B21F003020346101895\r\n" + 
		":1008B000002183421960C2F380020AD01AB1844237\r\n" + 
		":1008C0002346216005D01A1D5960083383425160C8\r\n" + 
		":1008D000F9D1FFF7B5FD0D484FF46D41CEF200019F\r\n" + 
		":1008E00020F07F038B608B6000F012F8AFF3008084\r\n" + 
		":1008F0000348024986468C46604700BF990700209E\r\n" + 
		":100900002D0700208809002088090020000400200D\r\n" + 
		":1009100008B5FFF70FFF4FF40053C4F20043104A2D\r\n" + 
		":100920001068196860F30B21196018681168C0F32A\r\n" + 
		":10093000032088420BD15068196860F3CF311960E9\r\n" + 
		":1009400018685368C0F3C0329A4204D108BD6FF0F2\r\n" + 
		":10095000C700FFF7EBFE6FF0C800FFF7E7FE00BF30\r\n" + 
		":100964000304000001010000020100000301000073\r\n" + 
		"";
static String file="\r\n" + 
		"#include \"patterns_include.h\"\r\n" + 
		"PATTERN( scan2020_01_28_13_46_diag_p, logic )\r\n" + 
		"\r\n" + 
		"//          Title \"  TetraMAX(R)  P-2019.03-i20190305_154542 STIL output\"\r\n" + 
		"//          Date \"Tue Jan 28 12:37:26 2020\"\r\n" + 
		"//          Source \"Minimal STIL for design `chip'\"\r\n" + 
		"\r\n" + 
		"//       Procedure multiclock_capture is assigned to TSET3\r\n" + 
		"//       Procedure load_unload is assigned to TSET1\r\n" + 
		"//       Procedure iddq_capture is assigned to TSET3\r\n" + 
		"//       Procedure test_setup is assigned to TSET2\r\n" + 
		"\r\n" + 
		"//       Vector statements that are outside of procedures are assigned to TSET5\r\n" + 
		"\r\n" + 
		"#include \"vecdef_scan_lpcCore.inc\"\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"//       test_setup\r\n" + 
		"//#include \"test_setup.pat\"\r\n" + 
		"%VEC    1 0 10 00 XX 0 , TSET1\r\n" + 
		"%VEC    1 0 10 00 XX 0 , TSET1\r\n" + 
		"%VEC    1 0 10 11 XX 0 , TSET1 // Start sequencer\r\n" + 
		"%VEC    1 0 10 11 XX 0 , TSET1 // Set value for reset : scan_in[0]\r\n" + 
		"%VEC    1 0 10 00 XX 0 , TSET1 // End sequencer\r\n" + 
		"%VEC    1 0 10 00 XX 0 , TSET2\r\n" + 
		"%VEC    1 0 10 00 XX 0 , TSET1\r\n" + 
		" \r\n" + 
		"//       pattern 0\r\n" + 
		"//       load_unload\r\n" + 
		"%VEC  1 1 00 XX XX   0 , TSET1 // LineCount=         0, PatternCount=    0, VectorCount=    0, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=         1, PatternCount=    0, VectorCount=    0, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=         2, PatternCount=    0, VectorCount=    1, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=         3, PatternCount=    0, VectorCount=    2, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=         4, PatternCount=    0, VectorCount=    3, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=         5, PatternCount=    0, VectorCount=    4, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=         6, PatternCount=    0, VectorCount=    5, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=         7, PatternCount=    0, VectorCount=    6, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=         8, PatternCount=    0, VectorCount=    7, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=         9, PatternCount=    0, VectorCount=    8, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        10, PatternCount=    0, VectorCount=    9, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        11, PatternCount=    0, VectorCount=   10, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        12, PatternCount=    0, VectorCount=   11, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        13, PatternCount=    0, VectorCount=   12, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        14, PatternCount=    0, VectorCount=   13, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        15, PatternCount=    0, VectorCount=   14, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        16, PatternCount=    0, VectorCount=   15, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        17, PatternCount=    0, VectorCount=   16, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        18, PatternCount=    0, VectorCount=   17, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        19, PatternCount=    0, VectorCount=   18, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        20, PatternCount=    0, VectorCount=   19, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        21, PatternCount=    0, VectorCount=   20, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        22, PatternCount=    0, VectorCount=   21, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        23, PatternCount=    0, VectorCount=   22, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        24, PatternCount=    0, VectorCount=   23, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        25, PatternCount=    0, VectorCount=   24, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        26, PatternCount=    0, VectorCount=   25, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        27, PatternCount=    0, VectorCount=   26, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        28, PatternCount=    0, VectorCount=   27, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        29, PatternCount=    0, VectorCount=   28, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        30, PatternCount=    0, VectorCount=   29, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        31, PatternCount=    0, VectorCount=   30, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        32, PatternCount=    0, VectorCount=   31, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        33, PatternCount=    0, VectorCount=   32, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        34, PatternCount=    0, VectorCount=   33, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        35, PatternCount=    0, VectorCount=   34, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        36, PatternCount=    0, VectorCount=   35, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        37, PatternCount=    0, VectorCount=   36, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        38, PatternCount=    0, VectorCount=   37, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        39, PatternCount=    0, VectorCount=   38, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        40, PatternCount=    0, VectorCount=   39, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        41, PatternCount=    0, VectorCount=   40, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        42, PatternCount=    0, VectorCount=   41, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        43, PatternCount=    0, VectorCount=   42, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        44, PatternCount=    0, VectorCount=   43, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        45, PatternCount=    0, VectorCount=   44, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        46, PatternCount=    0, VectorCount=   45, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        47, PatternCount=    0, VectorCount=   46, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        48, PatternCount=    0, VectorCount=   47, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        49, PatternCount=    0, VectorCount=   48, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        50, PatternCount=    0, VectorCount=   49, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        51, PatternCount=    0, VectorCount=   50, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        52, PatternCount=    0, VectorCount=   51, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        53, PatternCount=    0, VectorCount=   52, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        54, PatternCount=    0, VectorCount=   53, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        55, PatternCount=    0, VectorCount=   54, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        56, PatternCount=    0, VectorCount=   55, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        57, PatternCount=    0, VectorCount=   56, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        58, PatternCount=    0, VectorCount=   57, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        59, PatternCount=    0, VectorCount=   58, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        60, PatternCount=    0, VectorCount=   59, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        61, PatternCount=    0, VectorCount=   60, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        62, PatternCount=    0, VectorCount=   61, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        63, PatternCount=    0, VectorCount=   62, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        64, PatternCount=    0, VectorCount=   63, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        65, PatternCount=    0, VectorCount=   64, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        66, PatternCount=    0, VectorCount=   65, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        67, PatternCount=    0, VectorCount=   66, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        68, PatternCount=    0, VectorCount=   67, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        69, PatternCount=    0, VectorCount=   68, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        70, PatternCount=    0, VectorCount=   69, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        71, PatternCount=    0, VectorCount=   70, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        72, PatternCount=    0, VectorCount=   71, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        73, PatternCount=    0, VectorCount=   72, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        74, PatternCount=    0, VectorCount=   73, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        75, PatternCount=    0, VectorCount=   74, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        76, PatternCount=    0, VectorCount=   75, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        77, PatternCount=    0, VectorCount=   76, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        78, PatternCount=    0, VectorCount=   77, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        79, PatternCount=    0, VectorCount=   78, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        80, PatternCount=    0, VectorCount=   79, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        81, PatternCount=    0, VectorCount=   80, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        82, PatternCount=    0, VectorCount=   81, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        83, PatternCount=    0, VectorCount=   82, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        84, PatternCount=    0, VectorCount=   83, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        85, PatternCount=    0, VectorCount=   84, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        86, PatternCount=    0, VectorCount=   85, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        87, PatternCount=    0, VectorCount=   86, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        88, PatternCount=    0, VectorCount=   87, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        89, PatternCount=    0, VectorCount=   88, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        90, PatternCount=    0, VectorCount=   89, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        91, PatternCount=    0, VectorCount=   90, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        92, PatternCount=    0, VectorCount=   91, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        93, PatternCount=    0, VectorCount=   92, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        94, PatternCount=    0, VectorCount=   93, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        95, PatternCount=    0, VectorCount=   94, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        96, PatternCount=    0, VectorCount=   95, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        97, PatternCount=    0, VectorCount=   96, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        98, PatternCount=    0, VectorCount=   97, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=        99, PatternCount=    0, VectorCount=   98, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       100, PatternCount=    0, VectorCount=   99, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       101, PatternCount=    0, VectorCount=  100, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       102, PatternCount=    0, VectorCount=  101, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       103, PatternCount=    0, VectorCount=  102, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       104, PatternCount=    0, VectorCount=  103, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       105, PatternCount=    0, VectorCount=  104, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       106, PatternCount=    0, VectorCount=  105, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       107, PatternCount=    0, VectorCount=  106, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       108, PatternCount=    0, VectorCount=  107, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       109, PatternCount=    0, VectorCount=  108, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       110, PatternCount=    0, VectorCount=  109, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       111, PatternCount=    0, VectorCount=  110, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       112, PatternCount=    0, VectorCount=  111, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       113, PatternCount=    0, VectorCount=  112, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       114, PatternCount=    0, VectorCount=  113, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       115, PatternCount=    0, VectorCount=  114, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       116, PatternCount=    0, VectorCount=  115, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       117, PatternCount=    0, VectorCount=  116, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       118, PatternCount=    0, VectorCount=  117, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       119, PatternCount=    0, VectorCount=  118, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       120, PatternCount=    0, VectorCount=  119, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       121, PatternCount=    0, VectorCount=  120, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       122, PatternCount=    0, VectorCount=  121, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       123, PatternCount=    0, VectorCount=  122, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       124, PatternCount=    0, VectorCount=  123, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       125, PatternCount=    0, VectorCount=  124, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       126, PatternCount=    0, VectorCount=  125, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       127, PatternCount=    0, VectorCount=  126, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       128, PatternCount=    0, VectorCount=  127, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       129, PatternCount=    0, VectorCount=  128, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       130, PatternCount=    0, VectorCount=  129, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       131, PatternCount=    0, VectorCount=  130, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       132, PatternCount=    0, VectorCount=  131, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       133, PatternCount=    0, VectorCount=  132, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       134, PatternCount=    0, VectorCount=  133, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       135, PatternCount=    0, VectorCount=  134, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       136, PatternCount=    0, VectorCount=  135, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       137, PatternCount=    0, VectorCount=  136, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       138, PatternCount=    0, VectorCount=  137, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       139, PatternCount=    0, VectorCount=  138, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       140, PatternCount=    0, VectorCount=  139, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       141, PatternCount=    0, VectorCount=  140, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       142, PatternCount=    0, VectorCount=  141, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       143, PatternCount=    0, VectorCount=  142, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       144, PatternCount=    0, VectorCount=  143, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       145, PatternCount=    0, VectorCount=  144, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       146, PatternCount=    0, VectorCount=  145, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       147, PatternCount=    0, VectorCount=  146, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       148, PatternCount=    0, VectorCount=  147, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       149, PatternCount=    0, VectorCount=  148, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       150, PatternCount=    0, VectorCount=  149, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       151, PatternCount=    0, VectorCount=  150, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       152, PatternCount=    0, VectorCount=  151, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       153, PatternCount=    0, VectorCount=  152, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       154, PatternCount=    0, VectorCount=  153, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       155, PatternCount=    0, VectorCount=  154, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       156, PatternCount=    0, VectorCount=  155, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       157, PatternCount=    0, VectorCount=  156, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       158, PatternCount=    0, VectorCount=  157, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       159, PatternCount=    0, VectorCount=  158, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       160, PatternCount=    0, VectorCount=  159, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       161, PatternCount=    0, VectorCount=  160, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       162, PatternCount=    0, VectorCount=  161, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       163, PatternCount=    0, VectorCount=  162, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       164, PatternCount=    0, VectorCount=  163, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       165, PatternCount=    0, VectorCount=  164, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       166, PatternCount=    0, VectorCount=  165, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       167, PatternCount=    0, VectorCount=  166, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       168, PatternCount=    0, VectorCount=  167, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       169, PatternCount=    0, VectorCount=  168, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       170, PatternCount=    0, VectorCount=  169, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       171, PatternCount=    0, VectorCount=  170, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       172, PatternCount=    0, VectorCount=  171, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       173, PatternCount=    0, VectorCount=  172, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       174, PatternCount=    0, VectorCount=  173, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       175, PatternCount=    0, VectorCount=  174, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       176, PatternCount=    0, VectorCount=  175, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       177, PatternCount=    0, VectorCount=  176, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       178, PatternCount=    0, VectorCount=  177, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       179, PatternCount=    0, VectorCount=  178, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       180, PatternCount=    0, VectorCount=  179, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       181, PatternCount=    0, VectorCount=  180, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       182, PatternCount=    0, VectorCount=  181, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       183, PatternCount=    0, VectorCount=  182, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       184, PatternCount=    0, VectorCount=  183, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       185, PatternCount=    0, VectorCount=  184, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       186, PatternCount=    0, VectorCount=  185, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       187, PatternCount=    0, VectorCount=  186, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       188, PatternCount=    0, VectorCount=  187, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       189, PatternCount=    0, VectorCount=  188, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       190, PatternCount=    0, VectorCount=  189, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       191, PatternCount=    0, VectorCount=  190, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       192, PatternCount=    0, VectorCount=  191, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       193, PatternCount=    0, VectorCount=  192, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       194, PatternCount=    0, VectorCount=  193, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       195, PatternCount=    0, VectorCount=  194, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       196, PatternCount=    0, VectorCount=  195, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       197, PatternCount=    0, VectorCount=  196, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       198, PatternCount=    0, VectorCount=  197, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       199, PatternCount=    0, VectorCount=  198, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       200, PatternCount=    0, VectorCount=  199, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       201, PatternCount=    0, VectorCount=  200, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       202, PatternCount=    0, VectorCount=  201, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       203, PatternCount=    0, VectorCount=  202, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       204, PatternCount=    0, VectorCount=  203, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       205, PatternCount=    0, VectorCount=  204, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       206, PatternCount=    0, VectorCount=  205, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       207, PatternCount=    0, VectorCount=  206, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       208, PatternCount=    0, VectorCount=  207, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       209, PatternCount=    0, VectorCount=  208, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       210, PatternCount=    0, VectorCount=  209, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       211, PatternCount=    0, VectorCount=  210, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       212, PatternCount=    0, VectorCount=  211, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       213, PatternCount=    0, VectorCount=  212, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       214, PatternCount=    0, VectorCount=  213, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       215, PatternCount=    0, VectorCount=  214, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       216, PatternCount=    0, VectorCount=  215, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       217, PatternCount=    0, VectorCount=  216, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       218, PatternCount=    0, VectorCount=  217, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       219, PatternCount=    0, VectorCount=  218, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       220, PatternCount=    0, VectorCount=  219, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       221, PatternCount=    0, VectorCount=  220, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       222, PatternCount=    0, VectorCount=  221, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       223, PatternCount=    0, VectorCount=  222, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       224, PatternCount=    0, VectorCount=  223, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       225, PatternCount=    0, VectorCount=  224, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       226, PatternCount=    0, VectorCount=  225, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       227, PatternCount=    0, VectorCount=  226, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       228, PatternCount=    0, VectorCount=  227, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       229, PatternCount=    0, VectorCount=  228, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       230, PatternCount=    0, VectorCount=  229, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       231, PatternCount=    0, VectorCount=  230, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       232, PatternCount=    0, VectorCount=  231, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       233, PatternCount=    0, VectorCount=  232, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       234, PatternCount=    0, VectorCount=  233, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       235, PatternCount=    0, VectorCount=  234, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       236, PatternCount=    0, VectorCount=  235, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       237, PatternCount=    0, VectorCount=  236, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       238, PatternCount=    0, VectorCount=  237, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       239, PatternCount=    0, VectorCount=  238, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       240, PatternCount=    0, VectorCount=  239, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       241, PatternCount=    0, VectorCount=  240, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       242, PatternCount=    0, VectorCount=  241, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       243, PatternCount=    0, VectorCount=  242, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       244, PatternCount=    0, VectorCount=  243, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       245, PatternCount=    0, VectorCount=  244, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       246, PatternCount=    0, VectorCount=  245, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       247, PatternCount=    0, VectorCount=  246, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       248, PatternCount=    0, VectorCount=  247, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       249, PatternCount=    0, VectorCount=  248, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       250, PatternCount=    0, VectorCount=  249, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       251, PatternCount=    0, VectorCount=  250, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       252, PatternCount=    0, VectorCount=  251, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       253, PatternCount=    0, VectorCount=  252, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       254, PatternCount=    0, VectorCount=  253, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       255, PatternCount=    0, VectorCount=  254, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       256, PatternCount=    0, VectorCount=  255, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       257, PatternCount=    0, VectorCount=  256, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       258, PatternCount=    0, VectorCount=  257, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       259, PatternCount=    0, VectorCount=  258, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       260, PatternCount=    0, VectorCount=  259, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       261, PatternCount=    0, VectorCount=  260, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       262, PatternCount=    0, VectorCount=  261, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       263, PatternCount=    0, VectorCount=  262, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       264, PatternCount=    0, VectorCount=  263, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       265, PatternCount=    0, VectorCount=  264, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       266, PatternCount=    0, VectorCount=  265, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       267, PatternCount=    0, VectorCount=  266, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       268, PatternCount=    0, VectorCount=  267, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       269, PatternCount=    0, VectorCount=  268, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       270, PatternCount=    0, VectorCount=  269, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       271, PatternCount=    0, VectorCount=  270, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       272, PatternCount=    0, VectorCount=  271, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       273, PatternCount=    0, VectorCount=  272, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       274, PatternCount=    0, VectorCount=  273, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       275, PatternCount=    0, VectorCount=  274, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       276, PatternCount=    0, VectorCount=  275, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       277, PatternCount=    0, VectorCount=  276, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       278, PatternCount=    0, VectorCount=  277, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       279, PatternCount=    0, VectorCount=  278, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       280, PatternCount=    0, VectorCount=  279, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       281, PatternCount=    0, VectorCount=  280, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       282, PatternCount=    0, VectorCount=  281, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       283, PatternCount=    0, VectorCount=  282, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       284, PatternCount=    0, VectorCount=  283, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       285, PatternCount=    0, VectorCount=  284, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       286, PatternCount=    0, VectorCount=  285, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       287, PatternCount=    0, VectorCount=  286, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       288, PatternCount=    0, VectorCount=  287, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       289, PatternCount=    0, VectorCount=  288, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       290, PatternCount=    0, VectorCount=  289, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       291, PatternCount=    0, VectorCount=  290, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 01 XX   0 , TSET1 // LineCount=       292, PatternCount=    0, VectorCount=  291, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       293, PatternCount=    0, VectorCount=  292, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       294, PatternCount=    0, VectorCount=  293, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       295, PatternCount=    0, VectorCount=  294, Shift 1, Capture 0\r\n" + 
		"\r\n" + 
		"//       multiclock_capture\r\n" + 
		"%VEC  0 0 11 XX XX   0 , TSET2 // LineCount=       296, PatternCount=    0, VectorCount=  295, Shift 0, Capture 1\r\n" + 
		"//var pause\r\n" + 
		"//       pattern 1\r\n" + 
		"//       load_unload\r\n" + 
		"%VEC  1 1 00 XX XX   0 , TSET1 // LineCount=       297, PatternCount=    0, VectorCount=    0, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 01 XX   0 , TSET1 // LineCount=       298, PatternCount=    0, VectorCount=    0, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 11 XX   0 , TSET1 // LineCount=       299, PatternCount=    0, VectorCount=    1, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       300, PatternCount=    0, VectorCount=    2, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 01 XX   0 , TSET1 // LineCount=       301, PatternCount=    0, VectorCount=    3, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 10 XX   0 , TSET1 // LineCount=       302, PatternCount=    0, VectorCount=    4, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       303, PatternCount=    0, VectorCount=    5, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 10 XX   0 , TSET1 // LineCount=       304, PatternCount=    0, VectorCount=    6, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 10 XX   0 , TSET1 // LineCount=       305, PatternCount=    0, VectorCount=    7, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       306, PatternCount=    0, VectorCount=    8, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 01 XX   0 , TSET1 // LineCount=       307, PatternCount=    0, VectorCount=    9, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 10 XX   0 , TSET1 // LineCount=       308, PatternCount=    0, VectorCount=   10, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 10 XX   0 , TSET1 // LineCount=       309, PatternCount=    0, VectorCount=   11, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 01 XX   0 , TSET1 // LineCount=       310, PatternCount=    0, VectorCount=   12, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 10 XX   0 , TSET1 // LineCount=       311, PatternCount=    0, VectorCount=   13, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 00 XX   0 , TSET1 // LineCount=       312, PatternCount=    0, VectorCount=   14, Shift 1, Capture 0\r\n" + 
		"%VEC  1 1 11 01 XX   0 , TSET1 // LineCount=       313, PatternCount=    0, VectorCount=   15, Shift 1, Capture 0";
}
