/**
 * 
 */
package com.zoubworld.robot;

import com.zoubworld.geometry.Point;
import com.zoubworld.geometry.Segment;
import com.zoubworld.java.svg.ItoSvg;

/**
 * @author pierre valleau
 *
 */
public class Wheel  implements ItoSvg {

	Double x0;
	Double y0;
	Double theta0;
	Double radius;
	Integer tickPerRotation;
	/**
	 * @return the tickPerRotation
	 */
	public Integer getTickPerRotation() {
		return tickPerRotation;
	}

	/**
	 * @param tickPerRotation the tickPerRotation to set
	 */
	public void setTickPerRotation(Integer tickPerRotation) {
		this.tickPerRotation = tickPerRotation;
	}

	public Wheel(Double x0, Double y0, Double theta0, Double radius) {
		super();
		this.x0 = x0;
		this.y0 = y0;
		this.theta0 = theta0;
		this.radius = radius;
	}

	/**
	 * @return the x0
	 */
	public Double getX0() {
		return x0;
	}

	/**
	 * @param x0 the x0 to set
	 */
	public void setX0(Double x0) {
		this.x0 = x0;
	}

	/**
	 * @return the y0
	 */
	public Double getY0() {
		return y0;
	}

	/**
	 * @param y0 the y0 to set
	 */
	public void setY0(Double y0) {
		this.y0 = y0;
	}

	/**
	 * @return the theta0
	 */
	public Double getTheta0() {
		return theta0;
	}

	/**
	 * @param theta0 the theta0 to set
	 */
	public void setTheta0(Double theta0) {
		this.theta0 = theta0;
	}

	/**
	 * @return the radius
	 */
	public Double getRadius() {
		return radius;
	}

	/**
	 * @param radius the radius to set
	 */
	public void setRadius(Double radius) {
		this.radius = radius;
	}

	/**
	 * 
	 */
	public Wheel() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public double getDistance(int d0) {
		if(tickPerRotation==null)
		return 0.0;
		return (d0*radius*Math.PI*2.0)/tickPerRotation;
	}

	public double getDistanceFromCenter() {
		if(x0==null || y0==null)
		return 0;
		return Math.sqrt(x0*x0+y0*y0);
	}

	@Override
	public String toSvg() {
		Point p=getCenter();
		Segment s=getSegment() ;
		return s.toSvg()+p.toSvg();
	}

	public Point getCenter() {
		Point p=new Point(x0,y0);
		return p;
		
	}
		public Segment getSegment() {
				Point p=getCenter();
		return new Segment(p,radius*2,theta0);
		
	}

}
