package com.zoubworld.geometry;

import com.zoubworld.java.svg.ItoSvg;
import com.zoubworld.java.utils.svg.ISvgObject;

public class Circle extends SvgObject implements  ItoSvg {

	// equation : r²=(x-x0)²+(y-y0)²
	// x=x0+r*cos(theta);y=y0+r*sin(theta);
	
	Point center;
	double r;
	public Circle() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param center point
	 * @param r radius
	 */
	public Circle(Point center, double r) {
		super();
		this.center = center;
		this.r = r;
	}

	/**
	 * @return the center
	 */
	public Point getCenter() {
		return center;
	}

	/**
	 * @param center the center to set
	 */
	public void setCenter(Point center) {
		this.center = center;
	}

	/**
	 * @return the r
	 */
	public double getR() {
		return r;
	}

	/**
	 * @param r the r to set
	 */
	public void setR(double r) {
		this.r = r;
	}

	@Override
	public String toSvg() {
		if (center==null)
			return null;
		
		String s="<circle cx=\""+Unit.MtoMm(center.getX0())+"mm\" cy=\""+Unit.MtoMm(center.getY0())+"\" r=\""+Unit.MtoMm(r)+"\"/>";
		return s;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
