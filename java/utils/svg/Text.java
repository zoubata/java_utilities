package com.zoubworld.java.utils.svg;

public class Text  extends BasicSvg  implements ISvgObject{

	public Text() {
		// TODO Auto-generated constructor stub
	}
 
	String text=null;
	/**
	 * @return the attribute
	 */
	public Attribute getAttribute() {
		if(attribute==null)
			attribute = Attribute.Text; 			
		return attribute;
	}
	Double x0=0.0,y0=0.0,rot=0.0;
	public Text(Svg owner,Double my_x1,Double my_y1,String my_text) {
		x=my_x1;
		y=my_y1;
		text=my_text;		
	}
	public Text(Svg owner,Double my_x1,Double my_y1,Double my_x0,Double my_y0,Double my_rot,String my_text) {
		x=my_x1;
		y=my_y1;
		x0=my_x0;
		y0=my_y0;
		rot=my_rot;
		text=my_text;		
	}

	public String toSVG()
	{
		String s="";
		 if (rot==0.0)
		  s+="<text x=\""+BasicSvg.DoubletoSVG(x)+getUnit()+"\" y=\""+BasicSvg.DoubletoSVG(y)+getUnit()+"\" "+getAttribute().toSVG()+" >"+((text==null)?"":text)+"</text >\n";
		 else
		 {
			 s+="<g transform=\"translate("+x+" "+y+")\">";
			 s+="<g transform=\"rotate("+rot+")\">";
			  s+="<text x=\""+BasicSvg.DoubletoSVG(x0)+getUnit()+"\" y=\""+BasicSvg.DoubletoSVG(y0)+getUnit()+"\" "+getAttribute().toSVG();
			  
			/*  if (getAttribute()!=null)
				  s+=	 getAttribute().toSVG();*/
			  s+=" >"+((text==null)?"":text)+"</text >\n";
			  s+="</g>";
			  s+="</g>";
		 }	 
		return s;
		
	
	}
	
}
