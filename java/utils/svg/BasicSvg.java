package com.zoubwolrd.java.utils.svg;

public class BasicSvg implements ISvgObject{

	Attribute attribute;
	/**
	 * @return the attribute
	 */
	public Attribute getAttribute() {
		if(attribute==null)
			attribute = new Attribute(); 
			
		return attribute;
	}
	static public String DoubletoSVG(Double d) {
	return String.format("%3f",d); 
	}	
	public String getUnit() {
		// TODO Auto-generated method stub
		return "mm";
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
		// TODO Auto-generated method stub
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
