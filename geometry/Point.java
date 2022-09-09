package com.zoubworld.geometry;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.java.svg.ItoSvg;
import com.zoubworld.java.svg.SvgObject;
import com.zoubworld.robot.Ilocalisation;

public class Point extends SvgObject implements ItoSvg, Ilocalisation,iCoordTransformation {

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (Math.abs(x0 - other.x0)>(Math.abs(x0)/10000.0))
			return false;
		if  (Math.abs(y0 - other.y0)>(Math.abs(y0)/10000.0))
			return false;
		return true;
	}
	
	static public List<Point> getWindows(List<Point> ls)
	{
		List<Point> lp=new ArrayList<Point>();
		double xm,xM,ym,yM;
		int i=0;
		while(ls.get(i)==null)
			i++;
			xm=ls.get(i).getX0();
		
			xM=ls.get(i).getX0();
		
			ym=ls.get(i).getY0();
		
			yM=ls.get(i).getY0();
		for(Point p:ls)
			if (p!=null)
		{
			if(xm>p.getX0())
				xm=p.getX0();
			if(xM<p.getX0())
				xM=p.getX0();
			if(ym>p.getY0())
				ym=p.getY0();
			if(yM<p.getY0())
				yM=p.getY0();
			
		}
		lp.add(new Point(xm,ym));
		lp.add(new Point(xM,ym));
		lp.add(new Point(xM,yM));
		lp.add(new Point(xm,yM));
		
		/*Point pmm,pmM,pMm,pMM;
		pmm=pMM=pMm=pmM=ls.get(0);
		for(Point p:ls)
		{
			if ((pmM.getX0()>p.getX0())
					 &&(pmM.getY0()<p.getY0()))
						pmM=p;
			
			if ((pMm.getX0()<p.getX0())
					 &&(pMm.getY0()>p.getY0()))
						pMm=p;

			if ((pmm.getX0()>=p.getX0())
					 &&(pmm.getY0()>=p.getY0()))
						pmm=p;

			if ((pMM.getX0()<=p.getX0())
					 &&(pMM.getY0()<=p.getY0()))
						pMM=p;
					
			
			
		}
		lp.add(pmm);
		lp.add(pmM);
		lp.add(pMM);
		lp.add(pMm);
*/
		return lp;
		
	}
	/**  angle in b made by a,b,c
	 * */
	static public Double angle(Point a,Point b,Point c)
	{
		System.out.print("angle("+a+","+b+","+c+")");
	//	Segment s0=new Segment(b,a);
		//Segment s1=new Segment(b,c);
		Double d= Segment.getTheta(b,c)-Segment.getTheta(b,a);
		System.out.println("="+d);
		return d;
	}
	public Point(double x,	double y) {
		x0=x;
		y0=y;
	}
	public String toString()
	{
		return "Point("+String.format("%3.6f", x0)+","+String.format("%3.6f", y0)+")";
	}
	public Point(Point p) {
		x0=p.getX0();
		y0=p.getY0();
	}
	@Override
	public void setPoint(Point p0) {
		setX0(p0.getX0());
		setY0(p0.getY0());
		
	}

	@Override
	public Point getPoint0() {
		// TODO Auto-generated method stub
		return new Point(getX0(),getY0());
	}
	public Double getX0() {
		return x0;
	}
	public void setX0(Double x0) {
		this.x0 = x0;
	}
	public Double getY0() {
		return y0;
	}
	public void setY0(Double y0) {
		this.y0 = y0;
	}

		double x0;
		double y0;
		
		
		
		/* (non-Javadoc)
		 * @see geometrie.iCoordTransformation#rotate(double)
		 */
		@Override
		public void rotate(double theta)
		{
			double x1=x0*Math.cos(theta)+y0*Math.sin(theta);
			double y1=-x0*Math.sin(theta)+y0*Math.cos(theta);
			x0=x1;
			y0=y1;
		}
		/* (non-Javadoc)
		 * @see geometrie.iCoordTransformation#translation(double, double)
		 */
		@Override
		public void translation(double x,double y)
		{
			x0=x+x0;
			y0=y+y0;
		}
		/** return the rotation of an angle of point this according to reference point ref*/
		public Point rotation(Point ref,double angle)
		{
			Segment s=new Segment(ref,this);
			Double n=s.longeur();
			Double a=s.getTheta();
			double x=ref.getX0()+n*Math.cos(a+angle);
			
			double y=ref.getY0()+n*Math.sin(a+angle);
			return new Point(x,y);
		}
		
		/*
		x'=x+x0
		y'=y+y0
		
		rotation
		x'=x*cos(tetha)+y*sin(theta)
		y'=-x*sin(tetha)+y*cos(theta)
		
	}*/

		@Override
		public String toSvg()
		{
			return toSvg(size);		
		}
		static public String style="style=\"fill:rgb(255,0,0)\"";
		static public int size=3;
				
		public String toSvg(int r)
		{
			return "<circle  cx=\""+Unit.MtoMm(x0)+"mm\" cy=\""+Unit.MtoMm(y0)+"mm\" r=\""+r+"mm\" "+style+" />";		
		}

		public Point toReferentiel(Ilocalisation loc ) {
			Double x=this.x0-loc.getX0();
			Double y=this.y0-loc.getY0();
			Double r=Math.pow(x*x+y*y,1/2);
			
			Double theta=Unit.degreToRadian(90);
			if (x!=0)
			{
				theta=Math.atan(y/x);
				if(x<0)
					theta+=Unit.degreToRadian(180);
			}
			else
			{
				if (y>0)
					theta=Unit.degreToRadian(90);
				else
					theta=Unit.degreToRadian(-90);
			}
			
			 x=r*Math.cos(theta-loc.getTheta0());
			 y=r*Math.sin(theta-loc.getTheta0());	
			
			return new Point(x,y);
		}

		

		@Override
		public Double getTheta0() {

			return 0.0;
		}

		@Override
		public void setTheta0(Double theta0) {
			
			
		}

		public static Point MinX(Point p0, Point p1) {
			if(p0==null)
				return p1;
			if(p1==null)
				return p0;
			
			return (p0.getX0()<p1.getX0())?p0:p1;
		}
		public static Point MaxX(Point p0, Point p1) {
			if(p0==null)
				return p1;
			if(p1==null)
				return p0;
			
			return (p0.getX0()>p1.getX0())?p0:p1;
		}
		
		public static Point MinY(Point p0, Point p1) {
			if(p0==null)
				return p1;
			if(p1==null)
				return p0;
			
			return (p0.getY0()<p1.getY0())?p0:p1;
		}
		public static Point MaxY(Point p0, Point p1) {
			if(p0==null)
				return p1;
			if(p1==null)
				return p0;
			
			return (p0.getY0()>p1.getY0())?p0:p1;
		}

		public static String toSvg(List<Point> lp) {
			String s="";
			for(Point p:lp)
				if(p!=null)
				s+=p.toSvg();
			return s;
		}
		/** use euclidienne coordinate angle distance at origin (0,0)
		 * */
		public static Point factoryOal(double angle, double distance) {			
			return new Point (distance*Math.cos(angle),distance*Math.sin(angle));
		}
		/** use euclidienne coordinate angle distance at point p (0,0)
		 * */
		public static Point factoryOal(Point p,double angle, double distance) {			
			return new Point (distance*Math.cos(angle)+p.getX0(),distance*Math.sin(angle)+p.getY0());
		}

		public static Point intersection(Droite a, Droite b) {
		
			return Droite.seCoupe( a, b) ;
		}

	
}
