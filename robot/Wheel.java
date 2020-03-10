/**
 * 
 */
package com.zoubworld.robot;

import com.zoubworld.geometry.Point;
import com.zoubworld.geometry.Segment;
import com.zoubworld.geometry.Vector;
import com.zoubworld.java.svg.ItoSvg;

/**
 * @author pierre valleau
 *
 */
public class Wheel  implements ItoSvg {

	Double x0;// x location of the wheel, the center of ronot is at (x=0,y=0,0°)
	Double y0;// y location of the wheel, the center of ronot is at (x=0,y=0,0°)
	Double theta0;// theta0 location of the wheel, the center of ronot is at (x=0,y=0,0°)
	Double radius;// the radius of the wheel
	Integer tickPerRotation;// the number of tick provide by encoder in one round
	/**
	 * @return the tickPerRotation
	 */
	public Integer getTickPerRotation() {
		return tickPerRotation;
	}
	public String toString()
	{
		return "Wheel(r="+radius+"m,enc="+tickPerRotation+"=tick/s,x0="+x0+"m,y0="+y0+"m,theta0="+theta0+"rad,thetaC="+getThetaC()+"rad)";
	}

	/**
	 * @param tickPerRotation the tickPerRotation to set
	 */
	public void setTickPerRotation(Integer tickPerRotation) {
		this.tickPerRotation = tickPerRotation;
	}

	public Wheel(Double x0, Double y0, Double theta0, Double radius,Integer tickPerRotation) {
		super();
		this.x0 = x0;
		this.y0 = y0;
		this.theta0 = theta0;
		this.radius = radius;
		this.tickPerRotation=tickPerRotation;
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
		while(theta0>Math.PI)
			theta0-=Math.PI*2;
		while(theta0<-Math.PI)
			theta0+=Math.PI*2;
		
		return theta0;
	}
	public Double getThetaC() {
		return (new Segment(0.0,0.0,x0,y0)).getTheta();
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
/** return the distance in meter done by the wheel after do tick of encoder
 * 
 * @param d0
 * @return
 */
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
		Segment ss=getSegment() ;
		
		String s="";
	/*	Segment sb = new Segment(ss.getP0(),radius*0.2,theta0+Math.PI/2);
		Segment sp1 = new Segment(ss.getP1(),radius*0.2,theta0+Math.PI*3/4);
		Segment sp2 = new Segment(ss.getP1(),radius*0.2,theta0-Math.PI*3/4);*/
		s+="<g id=\""+this.getClass().getName()+"\">\r\n\t";
		s+= (new Vector(ss)).toSvg()/*sb.toSvg()+sp1.toSvg()+sp2.toSvg()+ss.toSvg()*/+p.toSvg();
		s+="\r\n</g>\r\n";
		return s;
	}
	/** display the speed vector of the wheel
	 * */
	public String toSvg(Double linearSpeed) {
		String s="";
		Point p=getCenter();
		Segment sb = new Segment(linearSpeed,theta0,p);
		
	/*	Segment sp1 = new Segment(sb.getP1(),radius*0.2,theta0+Math.PI*3/4);
		Segment sp2 = new Segment(sb.getP1(),radius*0.2,theta0-Math.PI*3/4);
	*/	
		s+="<g id=\""+this.getClass().getName()+"_Speed\">\r\n\t";
		s+= (new Vector(sb)).toSvg()/*+sp1.toSvg()+sp2.toSvg()*/;
		s+="\r\n</g>\r\n";
		return s;
	}
	public Point getCenter() {
		Point p=new Point(x0,y0);
		return p;
		
	}
		public Segment getSegment() {
				Point p=getCenter();
		return new Segment(p,radius*2,theta0);
		
	}

		public double getPerimeter() {
			return radius*2*Math.PI;
		}
		

}
