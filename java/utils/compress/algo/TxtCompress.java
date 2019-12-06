package com.zoubworld.java.utils.compress.algo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.zoubworld.java.utils.compress.HuffmanCode;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.utils.JavaUtils;

public class TxtCompress {

	String separator=",";
	Map<String, List<ISymbol>> mdico;
	Map<String, List<ISymbol>> mencode;
	public TxtCompress() {
		mdico=new TreeMap();
		mencode=new TreeMap();
	}
	public static void main(String[] args) {
	String fileName="C:\\home_user\\pvalleau\\svn_home\\Unicron_svn\\trunk\\Charac\\charac_0001\\chara_log\\CharacStSpeedtestVddSearch_tg.csv";
	TxtCompress tc=new TxtCompress();
	String data = JavaUtils.read(fileName);
	String[] split=data.split(tc.separator);
	Map<String,Long> m=tc.Histogram(split);
	m=JavaUtils.SortMapByValue(m);
	m=tc.filter(m);
	System.out.print(JavaUtils.Format(m, "->", "\r\n"));
	m=tc.countToweigth(m)   ;                                                                                                                                                                                                              
	m=JavaUtils.SortMapByValue(m);
	System.out.print(JavaUtils.Format(m, "->", "\r\n"));
	 tc.Encode(m);
	List<ISymbol> lse = tc.Encode(split);	
	System.out.println("lse ="+Symbol.length(lse)+" bits");
	System.out.println("lseh="+Symbol.length(lse,HuffmanCode.buildCode(lse))+" bits");
	
	List<ISymbol> lsd = tc.decode(lse);
	List<ISymbol> ls=Symbol.factoryCharSeq(data);
	System.out.println("lsd  ="+Symbol.length(lsd)+" bits");
	System.out.println("f   ="+data.length());
	System.out.println("fs  ="+Symbol.length(ls)+" bits");
	System.out.println("fsh ="+Symbol.length(ls,HuffmanCode.buildCode(ls))+" bits");
	
	System.out.println("lse="+lse.toString());
	System.out.println("dico="+tc.mdico.toString());
	System.out.println("encode="+tc.mencode.toString());
	
			}

	private List<ISymbol> decode(List<ISymbol> lse) {
		List<ISymbol> ls=new ArrayList<>();
		List<ISymbol>  lsw=null;
		int count=-1;
		ISymbol sprevious = null;
		for(ISymbol s:lse)
		{
			if(s.getId()<256)
				ls.add(s);
			else if (sprevious==Symbol.SOln)
				{
				count=s.getInt()/*getvalue()*/;
				lsw=new ArrayList();
				}
			else if(sprevious==Symbol.SAliasn)
			{
				count=s.getInt()/*getvalue()*/;
				lsw=mdico/*i*/.get("count");
				ls.addAll(lsw);
			}
			if(count>0)
				lsw.add(s);
			else
				if (count==0)
					{
					mdico/*i*/.put("count", lsw);
					//ls.addAll(lsw);done on the fly
					}
				count--;
		}
		return ls;		
	}
	private List<ISymbol> Encode(String[] split) {
		List<ISymbol> lse=new ArrayList<>();
		for(String s:split)
		{
			Double d=null;
			Float f=null;
			Long l=null;
			try{
			
			d=Double.parseDouble(s);
			f=Float.parseFloat(s);
			l=Long.parseLong(s);
			}
			catch (Exception e) {
			//	System.err.println("Error '"+s+"' "+e.getMessage());
			}
			
			List<ISymbol> ls = mdico.get(s);
			if (ls==null)
				ls=mencode.get(s);
			else
			{
				mdico.remove(s);//1st time use dico description, next time use alias
			}
			
			if(f!=null)
			{
				 List<ISymbol>  ls2=Symbol.FactorySymbolFloatAsASCII(f);
				
				if(s.length()>2)
					ls=ls2;//do a choise
			} else if(d!=null)
			{
				 List<ISymbol>  ls2=Symbol.FactorySymbolDoubleAsASCIIes3(d);
				
				if(s.length()>2)
					ls=ls2;//do a choise
			}
			if(l!=null)
			{
				List<ISymbol>  ls2=Symbol.FactorySymbolIntAsASCII(l);
				
				
				if(s.length()>2)
				ls=ls2;//do a choise
			}
			if (ls==null)//if doesn't exist use flat coding
			{
				ls=Symbol.factoryCharSeq(s);
				lse.addAll(ls);
			}
			else
			{
				lse.addAll(ls);
			}
			
			ls=Symbol.factoryCharSeq(separator);
			lse.addAll(ls);
		}
		return lse;		
	}
	/** from an histogram of String , create symbol encoding
	 * */
	private  void Encode(Map<String, Long> m) {
		m=JavaUtils.SortMapByValue(m);
		
		for(Entry<String, Long> e:m.entrySet())
		{			
			List<ISymbol> value=new ArrayList();
			value.add(Symbol.SOln);
			value.add(Symbol.FactorySymbolINT(e.getKey().length()));
			value.addAll(Symbol.factoryCharSeq(e.getKey()));			
			mdico.put(e.getKey(), value);
		}
		int count=0;
		
		for(Entry<String, Long> e:m.entrySet())
		{			
			List<ISymbol> value=new ArrayList();
			value.add(Symbol.SAliasn);
			value.add(Symbol.FactorySymbolINT(count));					
			mencode.put(e.getKey(), value);
			count++;
		}
		
	}
	/** filter to remove word with a poor probability, so not compressable.
	 * */
	private  Map<String, Long> filter(Map<String, Long> mc) {
		Map<String, Long> m=new TreeMap();
		for(Entry<String, Long> e:mc.entrySet())
		{
			if(e.getValue()>=3)
				if(e.getKey().length()>=3)
						
			m.put(e.getKey(), e.getValue());
		}
		return m;
	}
	private  Map<String, Long> countToweigth(Map<String, Long> mc) {
		Map<String, Long> m=new TreeMap();
		for(Entry<String, Long> e:mc.entrySet())
		{
			m.put(e.getKey(), e.getValue()*e.getKey().length());
		}
		return m;
	}
	private  Map<String, Long> Histogram(String[] split) {
		
		Map<String, Long> m=new TreeMap();
		for(String s:split)
		{
			Long count=m.get(s);
			if (count==null)
					count=0L;
			count++;
			m.put(s, count);
		}	
		
		return m;
	}
}