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
	
	public Double convert(double value,String fromUnit,String toUnits)
	{
		return null;
		/*Amount<ElectricCurrent> m2 = Amount.valueOf("234 mA").to(MICRO(AMPERE)); // Exact conversion.
		
		 Unit<?> from = Unit.valueOf(fromUnit);
		 Unit<?> to = Unit.valueOf(toUnits);
			 
	      UnitConverter from = new UnitConverter(fromUnit);
	      UnitConverter to = new UnitConverter(toUnit);
	      double meters = from.toMeters(value);
	      double converted = to.fromMeters(meters);
	      
		return value;*/}
	/**
	 * @param accuracy the accuracy to set
	 */
	static public void setAccuracy(double laccuracy) {
		accuracy = laccuracy;
	}

	public Unit() {
		// TODO Auto-generated constructor stub
	}
/**
 * convert meter to milli meter*/


	static public double mmtoM(double d)
	{
		return d*0.001;
	}
	static public double MtoMm(double d)
	{
		return d*1000;
	}
	
	static public double m(double d)
	{
		return d*1;
	}
	static public double degreToRadian(double d)
	{
		return d/360*2*Math.PI;
	}

	public static double RadiantoDegre(double theta0) {
		
		return theta0/Math.PI*180;
	}

	public static double MtoPx(double d) {

		return MtoMm(d)*3.779527559;//3.543307;
		//377952755.90551
		
	}

	public static String mainUnit(String unitlocal) {
		if (unitlocal.endsWith("V"))
			return "V";
		if (unitlocal.endsWith("C"))
			return "C";
		if (unitlocal.endsWith("A"))
			return "A";
		if (unitlocal.endsWith("Hz"))
			return "Hz";
		if (unitlocal.endsWith("Ohm"))
			return "Ohm";
		if (unitlocal.endsWith("m"))
			return "m";
		
		return null;
	}

}
