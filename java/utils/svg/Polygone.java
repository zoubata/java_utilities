package com.zoubworld.java.utils.svg;

import java.util.List;

import com.zoubworld.geometry.Point;

public class Polygone extends Path {

	/**
	 * @return the attribute
	 */
	public Attribute getAttribute() {
		if(attribute==null)
			attribute = Attribute.Polygone; 			
		return attribute;
	}
	public Polygone(Svg owner, List<Point> l) {
		super(owner, l);
		if(path.size()>0)
		path.add(path.get(0));
	}

}
