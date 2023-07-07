/**
 * 
 */
package com.zoubworld.Crypto.Wallet;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.zoubworld.bourse.simulator.Stock;
import com.zoubworld.utils.JavaUtils;

/**
 * @author zoubata
 *
 */
public class Wallet implements IWallet {
	Map<IToken,Double> asset=new HashMap<IToken,Double>();
	Date now;
	public Date getActualDate()
	{
		return now;
	}
	public Map<IToken,Double> getAsset()
	{
		Map<IToken,Double> asset2=new HashMap<IToken,Double>();
		for(Entry<IToken, Double> e:asset.entrySet())
			asset2.put(e.getKey(), e.getValue());
		return asset2;
	}
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
		Double som;
		if(tCurrency!=null)
		{
		 som=asset.get(tCurrency);
		if (som==null)
			som=0.0;
		som+=dGrossAmount;
		asset.put(tCurrency, som);
		}
		else 
			System.err.println("oups");
		if(tfee!=null)
		{		
		som=asset.get(tfee);
		if (som==null)
			som=0.0;
		som-=dFee;
		asset.put(tfee, som);
		}
		else 
			System.err.println("oups");
		now=date;

		
	}
	@Override
	public void Deposit(Date date, IToken tCurrency, double dGrossAmount, IToken tfee, double dFee,String Note) {
		IOperation  op=Operation.Deposit( date,  tCurrency,  dGrossAmount,  tfee,  dFee, Note);
		history.put(date,op);
		Double som;
		if(tCurrency!=null)
		{
			som=asset.get(tCurrency);
	
		if (som==null)
			som=0.0;
		som+=dGrossAmount;
		asset.put(tCurrency, som);
		}
		else 
			System.err.println("oups");
		if(tfee!=null)
		{
		som=asset.get(tfee);
		if (som==null)
			som=0.0;
		som-=dFee;
		asset.put(tfee, som);
		}
		else 
			System.err.println("oups");
		now=date;

		
	}

	@Override
	public void Buy(Date date, IToken tCurrency, double dGrossAmount, IToken tfee, double dFee,String Note) {
		IOperation  op=Operation.Buy( date,  tCurrency,  dGrossAmount,  tfee,  dFee, Note);
		history.put(date,op);
		Double som;
		if(tCurrency!=null)
		{
			som=asset.get(tCurrency);
	
		if (som==null)
			som=0.0;
		som+=dGrossAmount;
		asset.put(tCurrency, som);
		}
		else 
			System.err.println("oups");
		if(tfee!=null)
		{
		som=asset.get(tfee);
		if (som==null)
			som=0.0;
		som-=dFee;
		asset.put(tfee, som);
		}
		/*else it is free
			System.err.println("oups");*/
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
		if(tokenSymbol==null || tokenSymbol.isBlank())
			return null;
		for(IToken t:asset.keySet())
			if (tokenSymbol.equals(t.getSymbol()))
				return t;
				return null;
	}
	@Override
	public List<String>[] torowCsv() {
		return torowCsv(asset,now,this);
	}
	static 	public List<String>[] torowCsv(Map<IToken,Double> asset,Date now,IWallet w) {
				List<String> l = asset.keySet().stream().map(a->a.getSymbol()).collect(Collectors.toList());
		List<String>[] ex=new List[2];
		List<String> l2= new ArrayList<String>();
		
		l=JavaUtils.asSortedList(l);
		ex[0]=l;
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		 
		for(String s:l)
		{
			Double d = asset.get(w.getToken(s));
			if (d==null)
				d=0.0;
			l2.add(""+ new DecimalFormat("#####.########").format(d));
		}
		
		if(now!=null) {
			l2.add(0,formater.format(now));
		
			l.add(0,"Date ");}
		ex[1]=l2;
		
		return ex;
	}
	@Override
	public void Fee(Date date, IToken tfee, double dFee, String Note) {
		IOperation  op=Operation.Fee( date,    tfee,  dFee, Note);
		history.put(date,op);
		Double som=0.0;
		
		
		som=asset.get(tfee);
		if (som==null)
			som=0.0;
		som-=dFee;
		asset.put(tfee, som);
		now=date;
		
	}

}
