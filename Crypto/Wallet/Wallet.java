/**
 * 
 */
package com.zoubworld.Crypto;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.zoubworld.bourse.simulator.IToken;
import com.zoubworld.bourse.simulator.IWallet;
import com.zoubworld.bourse.simulator.Stock;
import com.zoubworld.utils.JavaUtils;

/**
 * @author zoubata
 *
 */
public class Wallet implements IWallet {
	Map<IToken,Double> asset=new HashMap<IToken,Double>();
	Date now;
	private Map<Date, IOperation> history=new HashMap<Date,IOperation>();
	
	/**
	 * 
	 */
	public Wallet() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Double get(IToken key) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void Payouts(Date date, IToken tCurrency, double dGrossAmount, IToken tfee, double dFee,String Note) {
		IOperation  op=Operation.Payouts( date,  tCurrency,  dGrossAmount,  tfee,  dFee, Note);
		history.put(date,op);
		Double som=asset.get(tCurrency);
		if (som==null)
			som=0.0;
		som+=dGrossAmount;
		asset.put(tCurrency, som);
		
		som=asset.get(tfee);
		if (som==null)
			som=0.0;
		som-=dFee;
		asset.put(tfee, som);
		now=date;

		
	}
	@Override
	public void Deposit(Date date, IToken tCurrency, double dGrossAmount, IToken tfee, double dFee,String Note) {
		IOperation  op=Operation.Deposit( date,  tCurrency,  dGrossAmount,  tfee,  dFee, Note);
		history.put(date,op);
		Double som=asset.get(tCurrency);
		if (som==null)
			som=0.0;
		som+=dGrossAmount;
		asset.put(tCurrency, som);
		
		som=asset.get(tfee);
		if (som==null)
			som=0.0;
		som-=dFee;
		asset.put(tfee, som);
		now=date;

		
	}

	@Override
	public void Buy(Date date, IToken tCurrency, double dGrossAmount, IToken tfee, double dFee,String Note) {
		IOperation  op=Operation.Buy( date,  tCurrency,  dGrossAmount,  tfee,  dFee, Note);
		history.put(date,op);
		Double som=asset.get(tCurrency);
		if (som==null)
			som=0.0;
		som+=dGrossAmount;
		asset.put(tCurrency, som);
		
		som=asset.get(tfee);
		if (som==null)
			som=0.0;
		som-=dFee;
		asset.put(tfee, som);	
		now=date;
	}

	@Override
	public void Sell(Date date, IToken tCurrency, double dGrossAmount, IToken tfee, double dFee,String Note) {
		IOperation  op=Operation.Sell( date,  tCurrency,  dGrossAmount,  tfee,  dFee, Note);
		history.put(date,op);
		Double som=asset.get(tCurrency);
		if (som==null)
			som=0.0;
		som-=dGrossAmount;
		asset.put(tCurrency, som);
		
		som=asset.get(tfee);
		if (som==null)
			som=0.0;
		som-=dFee;
		asset.put(tfee, som);
		now=date;
	}
	public void Withdrawal(Date date, IToken tCurrency, double dGrossAmount, IToken tfee, double dFee,String Note) {
		IOperation  op=Operation.Withdrawal( date,  tCurrency,  dGrossAmount,  tfee,  dFee, Note);
		history.put(date,op);
		Double som=asset.get(tCurrency);
		if (som==null)
			som=0.0;
		som-=dGrossAmount;
		asset.put(tCurrency, som);
		
		som=asset.get(tfee);
		if (som==null)
			som=0.0;
		som-=dFee;
		asset.put(tfee, som);
		now=date;
	}
	public IToken getToken(String tokenSymbol)
	{
		for(IToken t:asset.keySet())
			if (tokenSymbol.equals(t.getSymbol()))
				return t;
				return null;
	}
	@Override
	public List<String>[] torowCsv() {
		List<String> l = asset.keySet().stream().map(a->a.getSymbol()).collect(Collectors.toList());
		List<String>[] ex=new List[2];
		List<String> l2= new ArrayList<String>();
		
		l=JavaUtils.asSortedList(l);
		ex[0]=l;
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		 
		l2.add(formater.format(now));
		for(String s:l)
		{
			Double d = asset.get(getToken(s));
			if (d==null)
				d=0.0;
			l2.add(""+ new DecimalFormat("#####.########").format(d));
		}
		
		l.add(0,"Date ");
		ex[1]=l2;
		
		return ex;
	}

}
