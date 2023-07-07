package com.zoubworld.Crypto.Wallet;

import java.util.Date;
import java.util.List;

import com.zoubworld.bourse.simulator.Stock;

public interface IMarket {

	Stock get(String symbol);

	void load();

	/** will force the refresh
	 * */
	void refresh();

	Date getFirstDate();

	Date getLastDate();

	List<Date> getDates();

	IToken getToken(String currency);

	/** provide file or directoryof data of symbol*/
	String getFile(String symbol);

}