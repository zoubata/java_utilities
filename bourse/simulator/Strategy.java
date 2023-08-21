package com.zoubworld.bourse.simulator;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.zoubworld.Crypto.Wallet.IToken;

public class Strategy {

	public Strategy() {
		// TODO Auto-generated constructor stub
	}
	/** buy decreasing one , sell rising one, 
	 * if delta >x% in a window of n days */
	public void arbitrate(Wallet w, Date theday)
	{
		int n=14;
		double x=10/100;
		
		Set<IToken> ltoken=w.getTokenList();
		 final Calendar cal = Calendar.getInstance();
			
		    cal.setTime(theday);
		    cal.add(Calendar.DATE, -n);
		    Date refDay=cal.getTime();
		    for(IToken a:ltoken)
		    {
		    	float ppast=a.get(refDay).getClose();
		    	float pnew=a.get(theday).getClose();
		    	float xt=(pnew-ppast)/(ppast);
		    	double q=w.get(a);
		    	if(xt>x)
		    	{
		    		IToken tCurrency, double dGrossAmount, IToken tCurrency2, double dFee,
		    		
		    		w.Sell(theday+1, w.getCurrency(), q, a, ....)}
		    	else if (xt<-x)
		    	{}
		    	
		    }
		    
	}
	public void backtest( Wallet w, Date begin, Date end)
	{
		  final Calendar cal = Calendar.getInstance();
	
		    cal.setTime(begin);

		    for(;cal.getTime().before(end);cal.add(Calendar.DATE, 1))
		    {
		    	arbitrate(w,cal.getTime());
		    }
		    
	}
	
	public static void main(String[] args) {
		 String dir = System.getProperty("user.dir")+"\\src\\com\\zoubworld\\bourse\\simulator\\data\\";

		  // directory from where the program was launched
		  // e.g /home/mkyong/projects/core-java/java-io
		  System.out.println(dir);
	Market m = new Market(dir);
	Wallet w=new Wallet();
	Strategy s=new Strategy();
	m.refresh(m.previousday(1));
	System.out.println(m);
	System.out.println(m.sumarize().replaceAll("\\.", ","));
	s.backtest(w, m.getFirstDate(), m.getLastDate());

	}

}
