package com.zoubworld.java.utils.svg;

import com.zoubworld.geometry.Point;

public class Cross extends BasicSvg implements ISvgObject {

	double l=0.002;
	public Cross(Point p) {
		x=p.getX0();
		y=p.getY0();
		getAttribute().put("stroke", "green");
		getAttribute().put("stroke-width", "20%");
		
		         
		
	}
	public String toSVG()
	{
		String s="";
		 
		  s+="<line x1=\""+BasicSvg.DoubletoSVG(MToUnit(x-l))+getUnit()+"\" y1=\""+BasicSvg.DoubletoSVG(MToUnit(y-l))+getUnit()+"\" x2=\""+BasicSvg.DoubletoSVG(MToUnit(x+l))+getUnit()+"\" y2=\""+BasicSvg.DoubletoSVG(MToUnit(y+l))+getUnit()+"\" "+getAttribute().toSVG()+" />";
		  s+="<line x1=\""+BasicSvg.DoubletoSVG(MToUnit(x+l))+getUnit()+"\" y1=\""+BasicSvg.DoubletoSVG(MToUnit(y-l))+getUnit()+"\" x2=\""+BasicSvg.DoubletoSVG(MToUnit(x-l))+getUnit()+"\" y2=\""+BasicSvg.DoubletoSVG(MToUnit(y+l))+getUnit()+"\" "+getAttribute().toSVG()+" />\n";
				  
		return s;
		
	
	}
	
	}