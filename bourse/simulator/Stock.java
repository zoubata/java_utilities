package com.zoubworld.bourse.simulator;

import com.zoubworld.Crypto.Wallet.IMarket;
import com.zoubworld.Crypto.Wallet.IToken;
import com.zoubworld.Crypto.Wallet.Price;
import com.zoubworld.java.math.SMath;
import com.zoubworld.utils.JavaUtils;

import java.io.File;
import java.text.ParseException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Stock implements IToken {

	Map<Date,Price> data=new HashMap<Date,Price>();
	private String symbol;
	
	public String toString()
	{
		return symbol;
	}
	
	@Override
	public Map<Date, Price> getData() {
		return JavaUtils.SortMapByKey(data);
	}
	public void setData(Map<Date, Price> data) {
		this.data = data;
	}
	@Override
	public List<Date> getDates() {
		List<Date> date=new ArrayList<Date>();
		date.addAll(data.keySet());
		Collections.sort(date); 
		return date;
	}
	@Override
	public Date getFirstDate() {
		return getDates().get(0);
	}
	@Override
	public Date getLastDate() {
		return getDates().get(getDates().size()-1);
	}
	
	public Stock() {
		// TODO Auto-generated constructor stub
	}
	public Stock(File file) {
		load(file.getAbsolutePath());
	}
	
	public void load(String filein)
	{
		data=new HashMap<Date,Price>();
		symbol=JavaUtils.fileWithoutExtOfPath(filein);
		String datas=JavaUtils.read(filein);
		int i=0;
		for (String line:datas.split("\n"))
			{ 
			if (i!=0)
				{
				Price p;
				if(line.contains("null"))
				{
					System.err.println("ignored in "+filein+" line "+i+" : \""+line+"\"");
					
				}
				else
				try {
				p=new Price(line);
			data.put(p.getDate(), p);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					System.err.println("ignored in "+filein+" line "+i+" : \""+line+"\"");
						e.printStackTrace();
				}
				}
			i++;
			}
		
	}
	
	public void strategyS()
	{
		
		double frais_min=7;//2.0;
		double frais_pourcentage=0.7/100;//0.35/100;
		double epargneMensuel=100;
		int count=0;
		int somme=0;
		int solde=0;
		double totalfrais=0;
		Date d1=getFirstDate();
		Date d2=getLastDate();
		
		int month=-1;
		for(Date d:getDates(d1,d2))
		{			if (month!=d.getMonth())
		{
			somme+=epargneMensuel;
			solde+=epargneMensuel;
			//buy(d,somme);
			float po=data.get(d).getOpen();
			int nb=(int)(solde/po);
			solde-=po*nb;
			count+=nb;
			
			double frais=Math.max(frais_min, po*nb*frais_pourcentage);
			if (nb==0) frais=0;
			solde-=frais;
			totalfrais+=frais;
					//frait(d,nb)
			System.out.println(d.toString()+" buy "+nb+" actions at price "+po+" � with "+frais+"�");
			
		}
		//System.out.println(d.toString());
		month=d.getMonth();
		}
		System.out.println("Somme investit   : "+somme+" �");
		System.out.println("nombre d'actions : "+count+" actions");
		float liquidation=data.get(d2).getOpen()*count+solde;
		double frais=Math.max(frais_min, data.get(d2).getOpen()*count*frais_pourcentage);
		totalfrais+=frais;
		liquidation-=frais;
		System.out.println("liquidation      : "+liquidation+" �");
		System.out.println("plus valus       : "+(liquidation-somme)+" �");
		double impot=(liquidation-somme)*(0.172+0.34);
		if( impot<0) impot=0;
		System.out.println("impot            : "+impot+" �");
		System.out.println("totalfrais       : "+totalfrais+" �");
		int nbyear=d2.getYear()-d1.getYear();
		double rendementBrut=(liquidation/somme-1)*100;
		double rendementNet=((liquidation-impot)/somme-1)*100;
		System.out.println("redemment brut   : "+rendementBrut+" %");
		System.out.println("redemment net    : "+rendementNet+" %");
		System.out.println("redemment net annuel: "+(Math.pow(rendementNet/100+1,1.0/nbyear)-1)*100+" %");
	//	System.out.println("risque pris        :"+valoMin+valoMax );	
		
		
	}
	public List<Date> getDates(Date begin, Date end) {
		List<Date> list=new  ArrayList<Date> ();
		for(Date d:	getDates())
		if(d.after(begin)&& d.before(end))
			list.add(d);
		return list;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		 String list = "BTC 	\r\n"
		+ "ETH \r\n"
		+ "CHSB 	\r\n"
		+ "BNB \r\n"
		+ "XRP \r\n"
		+ "USDT\r\n"
		+ "EURT\r\n"
		+ "USDC \r\n"
		+ "DAI 	\r\n"
		+ "GHST\r\n"
		+ "AVAX\r\n"
		+ "CRV\r\n"
		+ "LINK\r\n"
		+ "DOT\r\n"
		+ "AAVE\r\n"
		+ "BNT\r\n"
		+ "CAKE\r\n"
		+ "SUSHI\r\n"
		+ "MC\r\n"
		+ "BUSD\r\n"
		+ "ENS\r\n"
		+ "PAXG\r\n"
		+ "ENJ\r\n"
		+ "KNC\r\n"
		+ "COMP\r\n"
		+ "REN\r\n"
		+ "UTK\r\n"
		+ "UNI\r\n"
		+ "CHZ\r\n"
		+ "GRT\r\n"
		+ "BAT\r\n"
		+ "OCC\r\n"
		+ "UOS\r\n"
		+ "MANA\r\n"
		+ "SAND\r\n"
		+ "MIMO\r\n"
		+ "HBAR\r\n"
		+ "IMX\r\n"
		+ "LRC\r\n"
		+ "PAR\r\n"
		+ "SOL\r\n"
		+ "GALA\r\n"
		+ "APE\r\n"
		+ "FTT\r\n"
		+ "LPT\r\n"
		+ "ATOM\r\n"
		+ "NEAR\r\n"
		+ "SWEAT\r\n"
		+ "ADA\r\n"
		+ "VET\r\n"
		+ "LDO\r\n"
		+ "SHIB\r\n"
		//+ "TraderJoe\r\n"
		+ "CAPS\r\n"
		+ "MKR\r\n"
		+ "RPL\r\n"
		+ "FXS\r\n"
		+ "ILV";
		 Map<String,List<Float>> md= new HashMap<String,List<Float>>();
		 for(String ss:list.split("\r\n"))
		 {
		String sym=ss.trim()+"-USD";
		//sym="CHSB-USD";
		 String dir = System.getProperty("user.dir")+"\\src\\com\\zoubworld\\bourse\\simulator\\data\\";
	  System.out.println(dir);
    	Market m = new Market(dir,false);
//	m.download(m.getNow(), sym);
		Stock s= new Stock()
;/*
		s.load("src\\com\\zoubworld\\bourse\\simulator\\data\\ILD.PA.csv");
		s.strategyS();
		s.load("src\\com\\zoubworld\\bourse\\simulator\\data\\GBB.PA.csv");
		s.strategyS();
		s.load("src\\com\\zoubworld\\bourse\\simulator\\data\\FP.PA.csv");
		s.strategyS();

		s.load("src\\com\\zoubworld\\bourse\\simulator\\data\\^FCHI.csv");
		s.strategyS();
		*/
		//"src\\com\\zoubworld\\bourse\\simulator\\data\\
		s.load(dir+"yahoo.com\\"+sym+".csv");
	//	System.out.println(s.getPrices(30));
	//	System.out.println(s.getPricesNormalized(30));
		md.put(sym, s.getPricesNormalized(30));
		 }
		 System.out.println();
		 System.out.println(JavaUtils.Format( md,":","\r\n"));
	}
@Override
public Price get(Date d) {
		if (d==null)
			return data.get(getLastDate());
		return data.get(d);
	}
public Double from(Date d, double i) {
		try {
		return i/get(d).getClose();
		}
		catch (java.lang.NullPointerException e) {
			System.err.println("on "+this+" the date "+d+" is missing");
		}
		return null;
	}
@Override
public String getSymbol() {
	return symbol;
}

public List<Float> getClose(Date datebegin,Date datestop) {
	/*List<Float> lf=new ArrayList<Float>();
for(Date d:data.keySet())
	if(d.after(datebegin) &&d.before(datestop))
		{lf.add(get(d).getClose());
		}	
	return lf;*/
	return get( datebegin, datestop).stream().map(p -> p.getClose()).toList();
}
@Override
public List<Price> get(Date datebegin,Date datestop) {
	
List<Price> lf=new ArrayList<Price>();
for(Date d:data.keySet())
	if(d.after(datebegin) &&d.before(datestop))
		{lf.add(get(d));
		}	
	return lf;
}

@Override
public double getVolume(Date datebegin,Date datestop) {
/*	double sum=0.0;
	int count=0;
for(Date d:data.keySet())
	if(d.after(datebegin) &&d.before(datestop))
		{sum+=data.get(d).getClose();
		count++;}
	return sum/count;
	*/
	return SMath.sum(get( datebegin, datestop).stream().map(p -> p.getVolume()).toList());
}
public double getAverage(Date datebegin,Date datestop) {
/*	double sum=0.0;
	int count=0;
for(Date d:data.keySet())
	if(d.after(datebegin) &&d.before(datestop))
		{sum+=data.get(d).getClose();
		count++;}
	return sum/count;
	*/
	return SMath.average(getClose( datebegin, datestop));
}

public double getstd(Date datebegin,Date datestop) {
/*	double sum=0.0;
	int count=0;
for(Date d:data.keySet())
	if(d.after(datebegin) &&d.before(datestop))
		{sum+=data.get(d).getClose();
		count++;}
	return sum/count;
	*/
	return SMath.StandardDeviation(getClose( datebegin, datestop));
}

@Override
public void reload(IMarket m) {

	load(m.getFile(symbol));
	
}
public List<Float> getPrices(int Ndays)
{
	 List<Float> l=new ArrayList<Float> ();
	 Map<Date, Price> m = getData();
	 List<Date> ld = JavaUtils.asSortedSet( m.keySet());
	 for(int i=0;i<Ndays;i++)
		 l.add(m.get(ld.get(ld.size()-i-1)).getClose());
	 return l;
}
public List<Float> getPrices()
{	
	 Map<Date, Price> m = getData();	 
	 return getPrices(m.size());
} 

public Price getPrices(Date d)
{	
	return getData().get(d);
} 

public List<Float> getPricesNormalized(int Ndays)
{
List<Float> l=getPrices( Ndays);
float ref=l.get(0);
for(int i=0;i<l.size();i++)
l.add(i, l.remove(i)/ref*100);

return l;
}
}