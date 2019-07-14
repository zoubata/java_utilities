package com.zoubworld.geometry;

public class Unit {

	static double accuracy=0;
	
	/**
	 * @return the accuracy
	 * 0 mean 0 error allow
	 * the value is considered as mm on x/y/theta or as % on coefficient 
	 */
	static public double getAccuracy() {
		return accuracy;
	}

	/**
	 * @param accuracy the accuracy to set
	 */
	static public void setAccuracy(double laccuracy) {
		accuracy = laccuracy;
	}

	public Unit() {
		// TODO Auto-generated constructor stub
	}

	static public double mm(double d)
	{
		return d*0.001;
	}
	static public double toMm(double d)
	{
		return d*1000;
	}
	
	static public double m(double d)
	{
		return d*1;
	}
	static public double degre(double d)
	{
		return d/360*2*Math.PI;
	}

	public static double toDegre(double theta0) {
		
		return theta0/Math.PI*180;
	}

	public static double toPx(double d) {
		// TODO Auto-generated method stub
		return toMm(d)*3.543307;
	}
}
