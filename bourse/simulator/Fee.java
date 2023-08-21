package com.zoubworld.bourse.simulator;

import com.zoubworld.Crypto.Wallet.Operation;

/// https://swissborg.com/legal/swissborg-app-fees
public class Fee {

	/** proprotional part of fee */
	double rate=1/100;
	/** fix part of fee */
	double fix=0;
	/** minimum  of fee */
	double min=1;
	/** maximum  of fee */
	double max=100;
	public double getFree(Operation op)
	{
		if ((op.getType()==Operation.Exchange)
				||(op.getType()==Operation.AutoInvest) )
			return Math.max(Math.min( op.getdGrossAmount()*rate+fix,min),max);
		if ((op.getType()==Operation.Withdrawal) )
			return Math.max(Math.min( op.getdGrossAmount()*rate/10+fix,min),max);
			return 0;
	}
	public Fee() {
		// TODO Auto-generated constructor stub
	}

}
