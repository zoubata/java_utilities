package com.zoubworld.java.utils.svg;

public class Line  extends BasicSvg  implements ISvgObject{

	public Line(Svg owner,Double my_x1,Double my_y1,Double my_x2,Double my_y2) {
		x=my_x1;
		x2=my_x2;
		y=my_y1;
		y2=my_y2;
	}
	
	public Line(Svg owner,Double my_x1,Double my_y1,Double alpha) {
		x=my_x1+1*Math.cos(alpha+Math.PI);
		x2=my_x1+1*Math.cos(alpha);
		y=my_y1+1*Math.sin(alpha+Math.PI);
		y2=my_y1+1*Math.sin(alpha);
	}

	/**
	 * @return the attribute
	 */
	public Attribute getAttribute() {
		if(attribute==null)
			attribute = Attribute.Line; 			
		return attribute;
	}
	private Double x2,y2;
	public String toSVG()
	{
		String s="";
		 
		  s+="<line x1=\""+BasicSvg.DoubletoSVG(x)+getUnit()+"\" y1=\""+BasicSvg.DoubletoSVG(y)+getUnit()+"\" x2=\""+BasicSvg.DoubletoSVG(x2)+getUnit()+"\" y2=\""+BasicSvg.DoubletoSVG(y2)+getUnit()+"\" "+getAttribute().toSVG()+" />\n";
		  
		return s;
		
	
	}

}
