/**
 * 
 */
package com.zoubworld.bourse.simulator;

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
	int Volume;
	/**
	 * @throws ParseException 
	 * 
	 */
	public Price(String line) throws ParseException {		
		String tab[]=line.split(separator);
	//	try {
		Open=Float.parseFloat(tab[1]);
		High=Float.parseFloat(tab[2]);
		Low=Float.parseFloat(tab[3]);
		Close=Float.parseFloat(tab[4]);
		AdjClose=Float.parseFloat(tab[5]);
		Volume=Integer.parseInt(tab[6]);
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		
			date = simpleDateFormat.parse(tab[0]);
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

	public int getVolume() {
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

}
