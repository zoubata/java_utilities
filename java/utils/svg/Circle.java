package com.zoubwolrd.java.utils.svg;

public class Circle  extends BasicSvg implements ISvgObject {

	
	
	public Circle(Svg owner,Double my_x1,Double my_y1,Double my_r) {
		x=my_x1;
		y=my_y1;
		r=my_r;
		
	}
	protected Double r;
	public String toSVG()
	{
		String s="";
		 
		  s+="<circle cx=\""+BasicSvg.DoubletoSVG(x)+getUnit()+"\" cy=\""+BasicSvg.DoubletoSVG(y)+getUnit()+"\" r=\""+BasicSvg.DoubletoSVG(r)+getUnit()+"\" "+getAttribute().toSVG()+"/>\n";//fill=\"red\" stroke="none"
		  
		return s;
		
	
	}
}
//stroke=\"red\" fill=\"none\"