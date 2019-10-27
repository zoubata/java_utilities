/**
 * 
 */
package com.zoubworld.geometry;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.java.svg.ItoSvg;

/**
 * @author zoubata
 *
 */
public class Segment extends DemiDroite  implements ItoSvg {

	
	//double x1,y1;
	Point p1=new Point(0,0);
	
	private Segment(double x0,double y0,double theta)
	{
		super(x0,y0,theta);
	
	}
	public Point seCoupe(Droite capteur)
	{
		if (capteur==null)
			return null;
		Point p=Droite.seCoupe(this, capteur);
		if (p!=null)
		if (contient( p) && capteur.contient(p))
			return p;
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if( Segment.class.isInstance(obj))
		{
		Segment other = (Segment) obj;
		if (other.getP0().equals(getP0()) && other.getP1().equals(getP1()) )
			return true;
		if (other.getP1().equals(getP0()) && other.getP0().equals(getP1()) )
			return true;
		}
		return false;
	}
	@Override
	public void rotate(double theta) {
		
		super.rotate(theta);
		p1.rotate(theta);
	}

	@Override
	public void translation(double x, double y) {
		super.translation(x,y);	
		p1.translation(x, y);
	}
	/**
	 * 
	 */
	public Segment(Double x0,Double y0, Double x1,Double y1) {
		super(x0,y0,0.0);
		//this.x1 = x1;
//		this.y1 = y1;
		p1.setX0(x1);
		p1.setY0(y1);
		
		a=(((y1-y0)/(x1-x0)));
		if(x1.equals(x0))
		 b=0;
		else
		b=(y1-a*x1);
		if (x0.equals(x1))
			c=x0;
		
		theta=Math.atan(a);
		if(x0>x1)
			theta+=Math.PI;
	}
	
	public Segment(Point p0, Point myp1)
	{
		super(p0,Point.angle(myp1, p0, new Point(p0.getX0(),myp1.getY0())));
		
		Set( p0,  myp1) ;
	}
	/** return the angle between the segment(p0,p1) and axes x
	 * */
	public double getTheta()
	{
		return getTheta( p0, p1);
	}
	static protected double getTheta(Point p0,Point p1) {
		if ((double)p1.getX0()!=(double)p0.getX0())
		return Math.atan((p1.getY0()-p0.getY0())/(p1.getX0()-p0.getX0()));
		else
			if (p1.getY0()-p0.getY0()>0)
				return Math.PI/2;
			else
				return Math.PI/2;
			
	}
	public void Set(Point p0, Point myp1) 
	{
		super.set(p0,Point.angle(myp1, p0, new Point(p0.getX0(),myp1.getY0())));

		this.p1=new Point(myp1);
		/*
		//this.x1 = x1;
//		this.y1 = y1;
		p1.setX0(myp1.getX0());
		p1.setY0(myp1.getY0());
		
		a=(((myp1.getY0()-p0.getY0())/(myp1.getX0()-p0.getX0())));
		if(p1.getX0().equals(p0.getX0()))
		 b=0;
		else
		b=(myp1.getY0()-a*myp1.getX0());
		if (p0.getX0().equals(myp1.getX0()))
			c=p0.getX0();
		
		theta=Math.atan(a);
		if(p0.getX0()>p1.getX0())
			theta+=Math.PI;
			*/
	}
	/** define a segment by the middle : point, and the length, and the angle : theta.
	 * if theta=0, the segment is parallel to x axes.
	 * */
	public Segment(Point middlePoint, Double length, double theta) {
		super(middlePoint, theta);
		Point Myp1=new Point(middlePoint.getX0()+length*Math.cos(theta)/2,middlePoint.getY0()+length*Math.sin(theta)/2);
		Point Myp0=new Point(middlePoint.getX0()-length*Math.cos(theta)/2,middlePoint.getY0()-length*Math.sin(theta)/2);
		Set( Myp0,  Myp1) ;
	}
	public boolean contient(Point p)
	{
		if(p==null)
			return false;
		

	
		if (p.getX0().isNaN())
			return false;
		if (p.getY0().isNaN())
			return false;
		
		if ((Math.min(getX0(),  getX1())>p.getX0()) || (Math.max(getX0(), getX1())<p.getX0()))
			return false;
		if ((Math.min(getY0(),  getY1())>p.getY0()) || (Math.max(getY0(), getY1())<p.getY0()))
			return false;
		
		if ((p.getX0().equals(getX0())) && (p.getX0().equals(getX1())))
			return true;
		
		return super.contient( p);
	
	}
	public boolean contient(Double x,Double y)
	{
		
		
		return super.contient( new Point(x, y));
		
	}
	
	public boolean contient(Segment s)
	{
		if(s==null)
			return false;
		
		
		boolean bo=contient( s.getP0());
				bo=bo && contient( s.getP1());
		return bo;
		
	}


	

	public void setX0(Double x0) {
		super.setX0(x0);
		setA((( getY1()-getY0())/( getX1()-x0)));
		setB( getY1()-a* getX1());
	}



	public Point getP1() {
		return p1;
	}

	public void setY0(Double y0) {
		super.setY0(y0);
		setA((( getY1() -y0)/( getX1()-getX0())));
		setB( getY1() -a* getX1() );
	}

	public Double getX1() {
		return p1.getX0();
	}

	public void setX1(Double x1) {
		//this.x1 = x1;
		p1.setX0(x1);
		setA(((getY1()-getY0())/(x1-getX0())));
		setB(getY1()-a*x1);
	}

	public Double getY1() {
		return p1.getY0();
	}

	public void setY1(Double y1) {
		p1.setY0(y1);
	//this.y1 = y1;
		setA(((y1-getY0())/(getX1()-getX0())));
		setB(y1-a*getX1());
	}
	public String toString()
	{
		return "Segment( ("+getX0()+","+getY0()+")"+", ("+getX1()+","+getY1()+") issue de "+super.toString()+")";
	}
	
	public String toSvg()
	{
		return "<line x1=\""+Unit.MtoMm(getX0())+"mm\" y1=\""+Unit.MtoMm(getY0())+"mm\" x2=\""+Unit.MtoMm(getX1())+"mm\" y2=\""+Unit.MtoMm(getY1())+"mm\" style=\"stroke:rgb(255,0,0);stroke-width:4\" />"
				+ getP0().toSvg(6)+getP1().toSvg(6);		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Segment( (3.0,2.0), (0.0,2.0) issue de DemiDroite(3.0,2.0, angle:-0.0�)) et DemiDroite(1.5,1.5, angle:120.0�) se croise en Point(1.2113248654051876,2.0)
		
		
		Segment s2=new Segment(Unit.m(3),Unit.m(2),Unit.m(0),Unit.m(2));
		DemiDroite d=new DemiDroite(Unit.m(1.5),Unit.m(1.5),Unit.degreToRadian(120));
		System.out.println(s2+" a une longeur de "+s2.longeur()+" m");
		Point p= Droite.seCoupe(d, s2);
		s2.contient(p);
		d.contient(p);
		
		Segment s1=new Segment(Unit.mmtoM(0),Unit.mmtoM(0),Unit.mmtoM(0),Unit.mmtoM(1000));
		System.out.println(s1+" a une longeur de "+s1.longeur()+" m");
		
		s1=new Segment(Unit.mmtoM(0),Unit.mmtoM(0),Unit.mmtoM(1000),Unit.mmtoM(1000));
		System.out.println(s1+" a une longeur de "+s1.longeur()+" m");
		

	}
	public Double longeur() {		
		return Math.pow((getX0()-getX1())*(getX0()-getX1())+(getY0()-getY1())*(getY0()-getY1()),0.5);
	}
	public static Segment Merge(Segment sold, Segment s) {
		if (s==null)
			return sold;
		if (sold==null)
			return s;
		if (!((Droite)s).contient(sold))
			return null;
		
		Point p1=Point.MinX(s.getP0(),s.getP1());
				Point p2=Point.MaxX(s.getP0(),s.getP1());
				if(p1==p2)
				{
					 p1=Point.MinY(s.getP0(),s.getP1());
					 p2=Point.MaxY(s.getP0(),s.getP1());
				}
				
				if (sold.contient(p1))
					p1=null;
				if (sold.contient(p2))
					p2=null;
				
				Point p3=Point.MinX(sold.getP0(),sold.getP1());
				Point p4=Point.MaxX(sold.getP0(),sold.getP1());
				
				
						
						
				if(p3==p4)
				{
					 p3=Point.MinY(sold.getP0(),sold.getP1());
					 p4=Point.MaxY(sold.getP0(),sold.getP1());
					 p1=Point.MinY(p1,p3);
					 p2=Point.MaxY(p2,p4);
				}
				else
				{
					p1=Point.MinX(p1,p3);
					p2=Point.MaxX(p2,p4);
				}
				/*
				if (s.contient(p4))
					p4=null;
				if (s.contient(p3))
					p3=null;
			if (p1==null)
				p1=p4;
			if (p1==null)
				p1=p3;
			if (p2==null)
				p2=p3;
			if (p2==null)
				p2=p4;*/
			return new Segment(p1,p2);	
	}

	public Point getMilieu()
	{
		Point p0=getP0();
		Point p1=getP1();
		
		return new Point((p0.getX0()+p1.getX0())/2,(p0.getY0()+p1.getY0())/2);
	}
	static public List<Segment> convert(List<Point> lp)// lidar data converted (theta,d) => (x,y)/(0,0)
	{
	//List<Point> lp=new ArrayList();
		 List<Segment> 	ls=new ArrayList();
	Point pold=null;
	Segment sold=null;
	for(Point p:lp)
		 {
		if (pold!=null)
		{
			Segment s=new Segment(p, pold); 
			if (sold==null)
				sold=s; 
			else
				if (   ((s).contient(sold.getP0()) || (s).contient(sold.getP1()) )
					&&   ((Droite)s).contient(sold) 
					)
				{
					sold=Segment.Merge(sold,s);
				}
				else
					{ls.add(sold);sold=s;}
		}
		pold=p;
		 }
	ls.add(sold);
	return ls;//list segment	
	}
	public Droite getDroite() {
		
		return new Droite(getA(),getB(),getC());
	}
	/** return a point on the droite
	 *  but outside the segment, and at a distance of r from extremite point
	 *  */
	public Point getPointFromOutSide(Point extremite, double r) {
	//	p1
		double x0=extremite.getX0();
		double y0=a*x0+b;
				if (extremite.getY0()!=y0)
		return null;
				//(x0-x)�+(y0-y)�=r�	
				//(x0-x)�+(y0-a*x+b)�=r�	
				//
				//math : equation 2nd degre
				//x�+x0�+2*x*x0+(y0+b)�+a�*x�-2a(y0+b)x=r�
				//x�+(a+b)x+ab=0=(x+a)(x+b)
				theta+=180;
				 double x=x0+r*Math.cos(theta);
				 double y=y0+r*Math.sin(theta);
				 
				return new Point(x,y);
	}
}
