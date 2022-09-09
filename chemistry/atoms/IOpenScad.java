package com.zoubworld.chemistry.atoms;

public interface IOpenScad {

	
	static double Factor=1e15;
	
	
	/**
	 * @return the color
	 */
	public String getColor();

	/**
	 * @return the radius
	 */
	public Double getRadius() ;

	default public String toOpenSCad()
	{
	return " color( c = \""+getColor()+"\" ) {\r\n" + 
		"     sphere(r = "+Factor*getRadius()+");\r\n"
		+ "}\r\n";
	}
}