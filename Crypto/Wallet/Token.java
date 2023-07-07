/**
 * 
 */
package com.zoubworld.Crypto.Wallet;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zoubworld.bourse.simulator.Market;
import com.zoubworld.bourse.simulator.Stock;
import com.zoubworld.utils.JavaUtils;

/**
 * @author zoubata
 *
 */
public class Token implements IToken {

	/**
	 * 
	 */
	public Token() {
		// TODO Auto-generated constructor stub
	}
	String Symbol=null;
	Map<Date,Price> data=new HashMap<Date,Price>();
	
	public Token(String currency) {
		Symbol=currency;
	}

	@Override
	public Map<Date, Price> getData() {
		return data;
	}

	@Override
	public List<Date> getDates() {
		return JavaUtils.asSortedSet(data.keySet());
	}

	@Override
	public Date getFirstDate() {
		return getDates().get(0);
	}

	@Override
	public Date getLastDate() {
		return getDates().get(getDates().size()-1);
	}


	@Override
	public Price get(Date d) {
		return data.get(d);
	}

	@Override
	public String getSymbol() {
		return Symbol;
	}

	@Override
	public List<Price> get(Date datebegin, Date datestop) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getVolume(Date datebegin, Date datestop) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Token s= new Token()
;
		s.load("E:\\crypto\\yahoo\\USDT-BTC.csv");
		System.out.print(s);
	}

	
	
	public void load(String file) {
		String l=JavaUtils.read(file);
		String ref=null;
		for(String line:l.split("\n"))
		{
			if (ref==null)
				ref=line;
				else
				{
			Price p=Price.parse(ref,line);
			if (p!=null)
			data.put(p.getDate(), p);
				}
		}
		
	}

	@Override
	public String toString() {
		return "Token [" + Symbol + "]";
	}

	@Override
	public void reload(IMarket m) {

		load(m.getFile(Symbol));
		
	}
	
}
