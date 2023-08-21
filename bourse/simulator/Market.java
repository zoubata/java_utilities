package com.zoubworld.bourse.simulator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zoubworld.Crypto.Wallet.IMarket;
import com.zoubworld.Crypto.Wallet.IToken;
import com.zoubworld.utils.JavaUtils;

public class Market implements IMarket {

	Map<String,Stock> listing=new HashMap<String,Stock>();
	public static void main(String[] args) {
		
		 String dir = System.getProperty("user.dir")+"\\src\\com\\zoubworld\\bourse\\simulator\\data\\";

		  // directory from where the program was launched
		  // e.g /home/mkyong/projects/core-java/java-io
		  System.out.println(dir);
	Market m = new Market(dir);
	m.refresh(previousday(1));
	System.out.println(m);
	System.out.println(m.sumarize().replaceAll("\\.", ","));
	
	}
	
public String sumarize() {
		
		Date d0 = getNow();
		Date d1 = previousday(1);
		Date d7=previousday(7);
		Date d30=previousday(30);
		Date d90=previousday(91);
		Date d365=previousday(365);
		String separator=";";
		String LR="\r\n";
		String ss="";
	
		ss+="sym"+separator+"average D"+separator+"average W"+separator+"average M"+separator+"average T"+separator+"average Y"+separator+"Standard dev M"
				+separator+"% var W"+separator+"% var M"+separator+"% var T"+separator+"% var Y"+separator
				+"Volume D"+separator+"Volume WD"+separator+"Volume M"+separator+"Volume T"+separator+"Volume Y"+separator+LR;
		for(Stock s:listing.values())
		{
			String sym=s.getSymbol();
			double a1=s.getAverage(d1,d0 );
			double a7=s.getAverage(d7,d0 );
			double a30=s.getAverage(d30,d0 );
			double a90=s.getAverage(d90,d0 );
			double aa=s.getAverage(d365,d0 );
			double dv30=s.getstd(d30,d0 );
			double v7,v30,v90,va;
			v7=(a1/a7-1)*100;
			v30=(a1/a30-1)*100;
			v90=(a1/a90-1)*100;
			va=(a1/aa-1)*100;
			double vol1=s.getVolume(d1,d0 );
			double vol7=s.getVolume(d7,d0 );
			double vol30=s.getVolume(d30,d0 );
			double vol90=s.getVolume(d90,d0 );
			double vola=s.getVolume(d365,d0 );
			
			ss+=sym+separator+a1+separator+a7+separator+a30+separator+a90+separator+aa+separator+dv30+separator
					+v7+separator+v30+separator+v90+separator+va+separator
					+vol1+separator+vol7+separator+vol30+separator+vol90+separator+va+separator+LR;
			
		}
		return ss;
	}

	@Override
	public Stock get(String symbol)
	{
		Stock s= listing.get(symbol);
		if (s==null)
			{
			download(getLastDate(),symbol);
			load(dir);
			}
		s= listing.get(symbol);
			
			return s;
		
	}
public void download(Date lastdate,String symbol) {
	//
	
	symbol=symbol.trim();
String urlStr=null;
if ("yahoo.com".equals(marketName))
urlStr="https://query1.finance.yahoo.com/v7/finance/download/"+symbol+"?period1=1629668159&period2="+(lastdate.getTime()/1000)+"&interval=1d&events=history&includeAdjustedClose=true";
String file =getFile( symbol);
        URL url;
		try {
			url = new URL(urlStr);
		 System.out.println("Download:"+symbol+" : " + url );
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
       
 } catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
		
	}
public static	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

public void load(String dir)
{
	File d=new File(dir+marketName+File.separator);
	if(!d.exists())
		d.mkdirs();
	Set<String> l = JavaUtils.listFileNames(dir+marketName+File.separator, null,".csv", false, true,true);
	for(String f:l)
	{
		listing.put(JavaUtils.fileWithoutExtOfPath(f), new Stock(new File(dir+marketName+File.separator+f))) ;
	}
}

@Override
public void load()
{
	String[] l = JavaUtils.read(dir+"list.txt").split("\r");
	Date d=Calendar.getInstance().getTime();
	for(String f:l)
	{
	download(d, f.trim());
	}
	load( dir);
}
/** will force the refresh
 * */
@Override
public void refresh()
{
	refresh(getNow());
	}
/** refresh if older than date d;
 * */
public void refresh(Date dateToUpdate)
{
		Date d=getNow();
		for(IToken s:listing.values())
			if(s.getLastDate().before(dateToUpdate))
			{
			download(d, s.getSymbol());
			s.reload(this);
			}
	}
public Market(String dir) {
	 this( dir, true);
}
		public Market(String dir,boolean load) {
				marketName="yahoo.com";
		this.dir=dir;
		if (!dir.endsWith(File.separator))
			dir+=File.separator;
		if(load)
		load(dir);
	}
 String dir = System.getProperty("user.dir")+"\\src\\com\\zoubworld\\bourse\\simulator\\data\\";
		
 String marketName;
	public Market() {
		marketName="yahoo.com";
		 load(dir);
	}
	public Market(String marketname,String dir) {
		marketName=marketname;
		this.dir=dir;		
		 load(dir);
	}
	@Override
	public Date getFirstDate() {
		return getDates().get(0);
	}
	@Override
	public Date getLastDate() {
		return getDates().get(getDates().size()-1);
	}
	static public Date getNow() {
		return new Date();
	}
	
	/** return a Date n day in the past
	 * */
	static public Date previousday(int n) {
	    final Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DATE, -n);
	    return cal.getTime();
	}
	
	@Override
	public List<Date> getDates() {
		List<Date> date=new ArrayList<Date>();
		Set<Date> data=new HashSet<Date>();
		for( IToken s:listing.values())
			data.addAll(s.getDates());
		date.addAll(data);
		Collections.sort(date); 
		return date;
	}

	@Override
	public IToken getToken(String currency) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFile(String symbol) {
	
			return dir+marketName+File.separator+symbol+".csv";
		
	}

}
