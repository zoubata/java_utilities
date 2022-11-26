package com.zoubworld.bourse.simulator;

import java.util.Date;
import java.util.List;

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

}