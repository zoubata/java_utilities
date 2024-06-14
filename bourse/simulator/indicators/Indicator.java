package com.zoubworld.bourse.simulator.indicators;
import java.util.List;
import java.util.stream.Collectors;

import com.zoubworld.bourse.simulator.Stock;

/** come from https://github.com/markusaksli/TradeBot/tree/master
 * modified the 27/08/2023 by zoubata
 * */
import java.util.List;

public interface Indicator {

    //Used to get the latest indicator value updated with closed candle
    double get();

    //Used to get value of indicator simulated with the latest non-closed price
    double getTemp(double newPrice);

    //Used in constructor to set initial value
    void init(List<Double> closingPrices);
    
	public default void init(Stock action) {
		List<Double> closingPrices=action.getPrices().stream().map(p->(double)p).collect(Collectors.toList());
		init(closingPrices);
		
	}	  
    //Used to update value with latest closed candle closing price
    void update(double newPrice);

    //Used to check for buy signal
    int check(double newPrice);

    String getExplanation();
}
