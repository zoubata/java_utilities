/**
 * 
 */
package com.zoubworld.Crypto.Wallet;

import java.util.Date;

/**
 * @author zoubata
 *
 */
public class Operation implements IOperation {

	/**
	 * 
	 */
	public Operation() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public Operation(Date date, IToken tCurrency, double dGrossAmount, IToken tfee, double dFee,String Note) {
		super();
		this.date = date;
		this.tCurrency = tCurrency;
		this.dGrossAmount = dGrossAmount;
		this.tfee = tfee;
		this.dFee = dFee;
		this.Note = Note;		
	}
	String Note;
	Date date;
	IToken tCurrency;
	double dGrossAmount;
	IToken tfee;
	double dFee;
	String Type;
	
	final static public String Payouts="Payouts";
	final static public String Deposit="Deposit";
	final static public String CardDeposit="Deposit/Card";
	final static public String CardDepositInternational="Deposit/Card International";
	final static public String Withdrawal="Withdrawal";
	final static public String AutoInvest="Auto-Invest";
	
	/**
	 * @return the type
	 */
	public String getType() {
		return Type;
	}
	final static public String Exchange ="Exchange";
	final static public String Subscription ="Subscription";
	
	public static IOperation Payouts(Date date, IToken tCurrency, double dGrossAmount, IToken tfee, double dFee,String Note) {
		Operation o = new Operation( date,  tCurrency,  dGrossAmount,  tfee,  dFee, Note);
		o.Type=Payouts;
		return o;
	}
	public static IOperation Deposit(Date date, IToken tCurrency, double dGrossAmount, IToken tfee, double dFee,String Note) {
		Operation o = new Operation( date,  tCurrency,  dGrossAmount,  tfee,  dFee, Note);
		o.Type=Deposit;
		return o;
	}
	
	public static IOperation Withdrawal(Date date, IToken tCurrency, double dGrossAmount, IToken tfee, double dFee,String Note) {
		Operation o = new Operation( date,  tCurrency,  dGrossAmount,  tfee,  dFee, Note);
		o.Type=Withdrawal;
		return o;
	}public static IOperation Sell(Date date, IToken tCurrency, double dGrossAmount, IToken tfee, double dFee,String Note) {
		Operation o = new Operation( date,  tCurrency,  dGrossAmount,  tfee,  dFee, Note);
		o.Type="Sell";
		return o;
	}
	public static IOperation Buy(Date date, IToken tCurrency, double dGrossAmount, IToken tfee, double dFee,String Note) {
		Operation o = new Operation( date,  tCurrency,  dGrossAmount,  tfee,  dFee, Note);
		o.Type="Buy";
		return o;
	}

	public Date getDate() {
		return date;
	}

	public IToken gettCurrency() {
		return tCurrency;
	}

	public double getdGrossAmount() {
		return dGrossAmount;
	}

	public IToken getTfee() {
		return tfee;
	}

	public double getdFee() {
		return dFee;
	}

	public static IOperation Fee(Date date, IToken tfee, double dFee, String Note) {
		Operation o = new Operation( date,  null,  0.0,  tfee,  dFee, Note);
		o.Type="Fee";
		return o;
	}

}
