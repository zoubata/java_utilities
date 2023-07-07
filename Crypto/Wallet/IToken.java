package com.zoubworld.Crypto.Wallet;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.zoubworld.Crypto.Wallet.IMarket;
public interface IToken {

	Map<Date, Price> getData();

	List<Date> getDates();

	Date getFirstDate();

	Date getLastDate();

	void reload(IMarket m);

	Price get(Date d);

	String getSymbol();

	List<Price> get(Date datebegin, Date datestop);

	double getVolume(Date datebegin, Date datestop);


}