package com.zoubworld.java.utils.svg;

import com.zoubworld.geometry.Unit;

public class BasicSvg implements ISvgObject{

	Attribute attribute;
	/**
	 * @return the attribute
	 */
	public Attribute getAttribute() {
		if(attribute==null)
			attribute = Attribute.Basic; 			
		return attribute;
	}
	static public String DoubletoSVG(Double d) {
	return String.format("%6f",d).replace(",", "."); 
	}	
	public String getUnit() {
		// TODO Auto-generated method stub
		return "";
	}
	public double MToUnit(double meter) {
		// TODO Auto-generated method stub
		return Unit.MtoMm(meter);
	}

	/**
	 * @param attribute the attribute to set
	 */
	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}


	public BasicSvg() {
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public String toSVG() {
		if (getAttribute()!=null)
			return "<g "+getAttribute().toSVG()+"</g>";
		return "";
	}
	protected Double x=null;
	protected Double y=null;
	public void move(Double dx, Double dy) {
		if (x==null ) x=0.0;
		if (y==null ) y=0.0;
		x+=dx;
		y+=dy;
		
	}

}
