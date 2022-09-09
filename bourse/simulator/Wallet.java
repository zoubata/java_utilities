/**
 * 
 */
package com.zoubworld.bourse.simulator;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author M43507
 *
 */
public class Wallet {

	Map<Stock,Double> asset=new HashMap<Stock,Double>();
	/**
	 * 
	 */
	public Wallet() {
		// TODO Auto-generated constructor stub
	}

	public String toString()
	{
		return toString(null);
	}
	public String toString(Date d)
	{
		String s="";
		for(Stock st:asset.keySet())
			s+=st.getSymbol()+" : "+asset.get(st).toString()+" : "+asset.get(st)*st.get(d).getClose()+"\r\n";
		return s;
	}

	static public String separator=", ";
	public String tocsvheader(Date d,List<Stock> l)
	{
		String s="";
		s+="wallet eval($)"+separator;
		for(Stock st:l)
			s+=st.getSymbol()+"(u)"+separator+st.getSymbol()+"($)"+separator;
			return s;	
	}
	public String tocsv(Date d,List<Stock> l)
	{
		String s="";
		s+=eval(d)+separator;
		Set<Stock> ss = asset.keySet();
		for(Stock st:l)
		s+=asset.get(st).toString()+separator+asset.get(st)*st.get(d).getClose()+separator+"\r\n";
		ss.removeAll(l);
		
		return s;
	}
	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
		Market m = new Market();
		Date d = m.simpleDateFormat.parse("2020-01-01");
		System.out.println(m);

		Wallet w=new Wallet();
				Stock s;
				w.buyWithDollar(d,m,"ETH-USD",1000.0);
				w.buyWithDollar(d,m,"USDT-USD",1000.0);
//				w.buyWithDollar(d,m,"BTC-USD",1000.0);
/*
				w.asset.put(s=m.get("CHSB-USD"), s.from(d,1000));
				w.asset.put(s=m.get("BNB-USD"), s.from(d,1000));
				w.asset.put(s=m.get("XRP-USD"), s.from(d,1000));
				w.asset.put(s=m.get("ADA-USD"), s.from(d,1000));
				w.asset.put(s=m.get("DOGE-USD"), s.from(d,1000));
*/				
				/*
				System.out.println(d +" : "+w.eval(d)+" $");
				System.out.println(w);
				System.out.println(m.getLastDate()+" : "+w.eval(m.getLastDate())+" $");
				*/
				w.stragety( w,m,d);
	}
	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public Double get(Stock key) {
		return asset.get(key);
	}

	/**
	 * 
	 * @see java.util.Map#clear()
	 */
	public void clear() {
		asset.clear();
	}

	private double eval(Date d) {
		double solde=0;
		for(Stock s:asset.keySet())
			solde+=get(s)*s.get(d).getClose();
		return solde;
	}

	
	public void stragety(Wallet w,Market  m,Date datestart)
	{
		List<Date> l = m.getDates();
		double oldsolde=0;
		for(int i=0;i<l.size();i+=22)
		{
			Date d = l.get(i);
			if (!d.before(datestart))
			{
				double solde=0;
				if(oldsolde>(solde=w.eval(d)))
				{
					solde=0;
			int count=w.asset.keySet().size();
			for(Stock s:w.asset.keySet())
				solde+=w.sell(d, m, s);
			Stock s=m.get("USDT-USD");
				w.buyWithDollar(d, m, s.getSymbol(), solde);
				}
				else
				{
					solde=0;
			int count=w.asset.keySet().size();
			for(Stock s:w.asset.keySet())
				solde+=w.sell(d, m, s);
			for(Stock s:w.asset.keySet())
				w.buyWithDollar(d, m, s.getSymbol(), solde/count);
				}
			//System.out.println(d +" : "+solde+" $");
			System.out.println(d +" : "+w.eval(d)+" $");
			System.out.println(w.toString(d));
			oldsolde=solde;
		}
			}
		
	}
	public void stragety2(Wallet w,Market  m,Date datestart)
	{
		List<Date> l = m.getDates();
		double oldsolde=0;
		for(int i=0;i<l.size();i+=22)
		{
			Date d = l.get(i);
			if (!d.before(datestart))
			{
				double solde=0;
				if(oldsolde>(solde=w.eval(d)))
				{
					solde=0;
			int count=w.asset.keySet().size();
			for(Stock s:w.asset.keySet())
				solde+=w.sell(d, m, s);
			for(Stock s:w.asset.keySet())
				w.buyWithDollar(d, m, s.getSymbol(), solde/count);
				}
			//System.out.println(d +" : "+solde+" $");
			System.out.println(d +" : "+w.eval(d)+" $");
			System.out.println(w.toString(d));
			oldsolde=solde;
		}
			}
		
	}
	
	public void stragety1(Wallet w,Market  m,Date datestart)
	{
		List<Date> l = m.getDates();
		for(int i=0;i<l.size();i+=22)
		{
			Date d = l.get(i);
			if (!d.before(datestart))
			{
				
			double solde=0;
			int count=w.asset.keySet().size();
			for(Stock s:w.asset.keySet())
				solde+=w.sell(d, m, s);
			for(Stock s:w.asset.keySet())
				w.buyWithDollar(d, m, s.getSymbol(), solde/count);
			
			//System.out.println(d +" : "+solde+" $");
			System.out.println(d +" : "+w.eval(d)+" $");
			//System.out.println(w.toString(d));
		}}
		
	}
	private double buyWithDollar(Date d, Market m,String symbol, double dollards) {
		Stock s;
		Double ds=asset.get(s=m.get(symbol));
		if (ds==null)
			ds=0.0;
		double q;
		asset.put(s, q=s.from(d,dollards)+ds);	
		return q;
	}
   /** sell all */
	private double sell(Date d, Market m,Stock s) {
		return sell( d,  m, s, asset.get(s)); 
	}
	/**
	 * 
	 * return the dollard get from the sell
	 * 
	 * */
		private double sell(Date d, Market m,Stock s, double quantity) {
			//	Stock s;
		Double ds=asset.get(s);
		if (ds==null)
			ds=0.0;
		ds-=quantity;
		asset.put(s, ds);
		return quantity*s.get(d).getClose();
	}

}
