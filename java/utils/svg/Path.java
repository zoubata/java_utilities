package com.zoubworld.java.utils.svg;
import java.util.List;

import com.zoubworld.geometry.Point;
public class Path  extends BasicSvg  implements ISvgObject{
	List<Point> path;
	public Path(Svg owner,List<Point> l) {
		path=l;
		if(path.size()>0)
		{
		x=path.get(0).getX0();
		y=path.get(0).getY0();
		}
	}
	/**
	 * @return the attribute
	 */
	public Attribute getAttribute() {
		if(attribute==null)
			attribute = Attribute.Path; 			
		return attribute;
	}
	public String toSVG()
	{
		String s="";
		 
		  s+="<path d=\"";
		  for(Point p:path)
		  s+=" M "+BasicSvg.DoubletoSVG(p.getX0())+" "+BasicSvg.DoubletoSVG(p.getY0());
		  s+="\" />\n";
		  
		return s;
		
	
	}

}
