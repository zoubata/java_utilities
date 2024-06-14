/**
 * 
 */
package com.zoubworld.Crypto.Wallet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Pierre Valleau
 *
 *  Date,Open,High,Low,Close,Adj Close,Volume
 */
public class Price {
static String separator=",";
	Date date;
	float Open,High,Low,Close,AdjClose;
	float Volume;
	int timeframe=24*60*60;//1d 1h 1m 1s ,...
	
	public static	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * @throws ParseException 
	 * 
	 */
	public Price(String line)  {		
		parse( line);
		
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public float getOpen() {
		return Open;
	}

	public void setOpen(float open) {
		Open = open;
	}

	public float getHigh() {
		return High;
	}
	/** return (High+Low+Open+Close)/4
	 * */
	public float getAverage() {
		return (High+Low+Open+Close)/4;
	}

	public void setHigh(float high) {
		High = high;
	}

	public float getLow() {
		return Low;
	}

	public void setLow(float low) {
		Low = low;
	}

	public float getClose() {
		return Close;
	}

	public void setClose(float close) {
		Close = close;
	}

	public float getAdjClose() {
		return AdjClose;
	}

	public void setAdjClose(float adjClose) {
		AdjClose = adjClose;
	}

	public double getVolume() {
		return Volume;
	}

	public void setVolume(int volume) {
		Volume = volume;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	static public Price parse(String ref,String line) {
		Price p=new Price(null);
		separator=";";
		if (ref!=null)
		{
		int rcount=count(ref,';');
	if (count(ref,',')>rcount)	
		{separator=",";rcount=count(ref,',');}
	
	if (count(ref,'\t')>rcount)	
	{separator="\t";rcount=count(ref,'\t');}
		}
		
		String[] col = ref.split(separator);
		String tab[]=line.split(separator);
		int index=-1;
		try {
		index=find(col,"open");
		if (index>=0)
		p.Open=Float.parseFloat(tab[index].trim());
		 index=find(col,"High");
		if (index>=0)
			p.High=Float.parseFloat(tab[index].trim());
		 index=find(col,"Low");
		if (index>=0)
			p.Low=Float.parseFloat(tab[index].trim());
		 index=find(col,"Close");
		if (index>=0)
			p.Close=Float.parseFloat(tab[index].trim());
		 index=find(col,"Adj Close");
		if (index>=0)
			p.AdjClose=Float.parseFloat(tab[index].trim());
		 index=find(col,"Volume");
		if (index>=0)
			p.Volume=Float.parseFloat(tab[index].trim());
		
		}
		catch (java.lang.NumberFormatException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
			return null;
		}
		 index=find(col,"Date");		 
		if (index>=0)
			try {
				p.date = simpleDateFormat.parse(tab[0]);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		return p;
}
		
	static private int find(String[] tab, String col) {
		int count=0;
		for(String s:tab)
			if(tab[count].toLowerCase().equals(col.toLowerCase()))
				return count;
			else count++;
		return 0;
	}

	static private int count(String ref, char c) {
		int count=0;
		for(char a:ref.toCharArray())
			if(a==c)
				count++;
		return count;
	}

	public void parse(String line) {
		if(line==null)
			return ;
		String tab[]=line.split(separator);
		//	try {
			Open=Float.parseFloat(tab[1].trim());
			High=Float.parseFloat(tab[2].trim());
			Low=Float.parseFloat(tab[3].trim());
			Close=Float.parseFloat(tab[4].trim());
			AdjClose=Float.parseFloat(tab[5].trim());
			Volume=Float.parseFloat(tab[6].trim());
			
			
				try {
					date = simpleDateFormat.parse(tab[0]);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			/*} catch (ParseException e) {
				// TODO Auto-generated catch block
				System.err.println(line);
					e.printStackTrace();
			}catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				System.err.println("\""+line+"\"");
					e.printStackTrace();
			}*/
			//date=new Date(Date.parse(tab[0]));
	}

}
