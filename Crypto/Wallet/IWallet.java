package com.zoubworld.Crypto.Wallet;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IWallet {

	String toString();

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	Double get(IToken key);

	/**
	 * operation on walet
	 */
	public void Deposit(Date date, IToken tCurrency, double dGrossAmount, IToken tfee, double dFee, String Note);

	/**
	 * operation on walet
	 */
	public void Buy(Date date, IToken tCurrency, double dGrossAmount, IToken tCurrency2, double dFee, String Note);

	/**
	 * operation on walet
	 */
	public void Sell(Date date, IToken tCurrency, double dGrossAmount, IToken tCurrency2, double dFee, String Note);

	/**
	 * operation on walet
	 */
	public void Withdrawal(Date date, IToken tCurrency, double dGrossAmount, IToken tfee, double dFee, String Note);

	/** return one Row(List<String>) of current status of wallet */
	List<String>[] torowCsv();

	/** return IToken form a symbol String */
	public IToken getToken(String tokenSymbol);

	/**
	 * operation on walet
	 */
	public void Payouts(Date date, IToken tCurrency, double dGrossAmount, IToken tfee, double dFee, String Note);

	/**
	 * asset inside the wallet (snapshot)
	 */
	public Map<IToken, Double> getAsset();

	/**
	 * date of last update of wallet
	 */
	public Date getActualDate();
	/**
	 * operation on walet
	 */
	void Fee(Date date, IToken tfee, double dFee, String Note);

}