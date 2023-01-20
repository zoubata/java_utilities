package com.zoubworld.bourse.simulator;

import java.util.Date;
import java.util.List;

public interface IWallet {

	String toString();

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	Double get(IToken key);

	public void Deposit(Date date, IToken tCurrency, double dGrossAmount, IToken tfee, double dFee,String Note);

	public void Buy(Date date, IToken tCurrency, double dGrossAmount, IToken tCurrency2, double dFee,String Note);

	public void Sell(Date date, IToken tCurrency, double dGrossAmount, IToken tCurrency2, double dFee,String Note);
	public void Withdrawal(Date date, IToken tCurrency, double dGrossAmount, IToken tfee, double dFee,String Note);
	/** return one Row(List<String>) of current status of wallet*/
	List<String>[] torowCsv();
	/** return IToken form a symbol String*/
	public IToken getToken(String tokenSymbol);

	void Payouts(Date date, IToken tCurrency, double dGrossAmount, IToken tfee, double dFee, String Note);
}