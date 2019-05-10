package com.zoubworld.java.utils.svg;

public class Translate extends ComplexSvg implements ISvgObject{

	// 
		Double x=null;
		Double y=null;
	public Translate(Double my_x,Double my_y)
	{
		super();
		x=my_x;
		y=my_y;
	}
	
	
	public String toSVG()
	{
		String s="<g transform=\"translate("+x+getUnit()+","+y+getUnit()+")\">\n";
		s+=super.toSVG();
		s+="</g>\n";
		return s;
	}
}
