/**
 * 
 */
package com.zoubworld.Crypto;

import java.util.Date;

import com.zoubworld.bourse.simulator.IToken;

/**
 * @author zoubata
 *
 */
public interface IOperation {
	//public IOperation Deposit(Date date, IToken tCurrency, double dGrossAmount, IToken tfee, double dFee);
	
	

	public Date getDate() ;

	public IToken gettCurrency();

	public double getdGrossAmount();

	public IToken getTfee();

	public double getdFee();
}
