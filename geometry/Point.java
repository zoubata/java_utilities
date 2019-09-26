package com.zoubworld.geometry;

import com.zoubworld.java.svg.ItoSvg;
import com.zoubworld.robot.Ilocalisation;

public class Point implements ItoSvg, Ilocalisation,iCoordTransformation {

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (x0 != other.x0)
			return false;
		if (y0 != other.y0)
			return false;
		return true;
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
			return toSvg(3);		
		}
		public String toSvg(int r)
		{
			return "<circle  cx=\""+Unit.MtoMm(x0)+"mm\" cy=\""+Unit.MtoMm(y0)+"mm\" r=\""+r+"mm\" style=\"fill:rgb(255,0,0)\" />";		
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

	
}
