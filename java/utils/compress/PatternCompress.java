/**
 * 
 */
package com.zoubworld.java.utils.compress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.zoubworld.java.utils.compress.HuffmanCode.HuffmanNode;
import com.zoubworld.java.utils.compress.PIE.Tree;
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
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
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
		Collector.of( () -> new ArrayList<Map<ISymbol,Integer>>(),
				 (result, article) -> {for(int i=0;i<article.size();i++) {
					 Map<ISymbol,Integer> m1=result.get(i);
					 Map<ISymbol,Integer> m2=article.get(i);
					 m2.forEach((k, v) -> m1.merge(k, v, (v1, v2) ->((new Integer( v1 + v2)));
					
					 }
						 }
						 result[0] += article.getWordCount(),)
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
	Map<ISymbol, Long> mfreq = Symbol.Freq(lsc);
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
	/**
	 * @param args
	 */
	public static void main2(String[] args) {
		// TODO Auto-generated method stub

	}


public List<ISymbol> compress(List<ISymbol> ls)
{

	List<ISymbol> lsc=new ArrayList();
	
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
	List<ISymbol> lsd=new ArrayList();
	
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

}
