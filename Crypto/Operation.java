/**
 * 
 */
package com.zoubworld.Crypto;

import java.util.Date;

import com.zoubworld.bourse.simulator.IToken;

/**
 * @author M43507
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
	

	public static IOperation Payouts(Date date, IToken tCurrency, double dGrossAmount, IToken tfee, double dFee,String Note) {
		Operation o = new Operation( date,  tCurrency,  dGrossAmount,  tfee,  dFee, Note);
		o.Type="Payouts";
		return o;
	}
	public static IOperation Deposit(Date date, IToken tCurrency, double dGrossAmount, IToken tfee, double dFee,String Note) {
		Operation o = new Operation( date,  tCurrency,  dGrossAmount,  tfee,  dFee, Note);
		o.Type="Deposit";
		return o;
	}
	
	public static IOperation Withdrawal(Date date, IToken tCurrency, double dGrossAmount, IToken tfee, double dFee,String Note) {
		Operation o = new Operation( date,  tCurrency,  dGrossAmount,  tfee,  dFee, Note);
		o.Type="Withdrawal";
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

}
