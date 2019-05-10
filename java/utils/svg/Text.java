package com.zoubwolrd.java.utils.svg;

public class Text  extends BasicSvg  implements ISvgObject{

	public Text() {
		// TODO Auto-generated constructor stub
	}
 
	String text=null;
	
	public Text(Svg owner,Double my_x1,Double my_y1,String my_text) {
		x=my_x1;
		y=my_y1;
		text=my_text;
		
	}

	public String toSVG()
	{
		String s="";
		 
		  s+="<text x=\""+BasicSvg.DoubletoSVG(x)+getUnit()+"\" y=\""+BasicSvg.DoubletoSVG(y)+getUnit()+"\" "+getAttribute().toSVG()+" >"+((text==null)?"":text)+"</text >\n";
		  
		return s;
		
	
	}
	
}
