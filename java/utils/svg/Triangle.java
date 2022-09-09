package com.zoubworld.java.utils.svg;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.geometry.Point;

public class Triangle extends Polygone {

	public Triangle(Svg owner, List<Point> l) {
		super(owner, l);
		// TODO Auto-generated constructor stub
	}
	
	public Triangle(Svg owner, Point p1,Point p2,Point p3) {
		super(owner, new ArrayList());
		path.add(p1);
		path.add(p2);
		path.add(p3);
		path.add(p1);

	}

}
