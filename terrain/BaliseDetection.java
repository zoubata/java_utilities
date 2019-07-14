/**
 * 
 */
package com.zoubworld.terrain;

import java.util.Calendar;
import java.util.Date;

/**
 * @author  pierre valleau
 *
 */
public class BaliseDetection {

	IBalise b;
	Double distance;
	Double Thetad;
	Date time;
	
	/**
	 * 
	 */
	public BaliseDetection(IBalise b1,Double d,Double t) {
		b=b1;
		distance=d;
		Thetad=t;
		time= Calendar.getInstance().getTime();
	}

	/**
	 * @return the b
	 */
	public IBalise getB() {
		return b;
	}

	/**
	 * @return the distance
	 */
	public Double getDistance() {
		return distance;
	}

	/**
	 * @return the thetad
	 */
	public Double getThetad() {
		return Thetad;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
