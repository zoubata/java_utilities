/**
 * 
 */
package com.zoubworld.Crypto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zoubworld.bourse.simulator.IToken;
import com.zoubworld.bourse.simulator.Market;
import com.zoubworld.bourse.simulator.Price;

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
	public Token(String currency) {
		Symbol=currency;
	}

	@Override
	public Map<Date, Price> getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Date> getDates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getFirstDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getLastDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reload(Market m) {
		// TODO Auto-generated method stub

	}

	@Override
	public Price get(Date d) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub

	}

}
