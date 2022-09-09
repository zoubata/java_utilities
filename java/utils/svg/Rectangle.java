package com.zoubworld.java.utils.svg;

public class Rectangle  extends BasicSvg implements ISvgObject {

	public Rectangle() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the attribute
	 */
	public Attribute getAttribute() {
		if(attribute==null)
			attribute = Attribute.Rectangle; 			
		return attribute;
	}
	public Rectangle(Svg owner,Double my_x1,Double my_y1,Double my_x2,Double my_y2) {
		x=my_x1;
		y=my_y1;
		width=Math.abs(my_x2-my_x1);
		height=Math.abs(my_y2-my_y1);
	}
	private Double width,height;
	public String toSVG()
	{
		String s="";
		 
		  s+="<rect width=\""+BasicSvg.DoubletoSVG(width)+getUnit()+"\" height=\""+BasicSvg.DoubletoSVG(height)+getUnit()+"\" x=\""+BasicSvg.DoubletoSVG(x)+getUnit()+"\" y=\""+BasicSvg.DoubletoSVG(y)+getUnit()+"\" "+getAttribute().toSVG()+" />\n";//fill=\"green\""
		  
		return s;
		
	
	}
}
