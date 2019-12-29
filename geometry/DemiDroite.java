/**
 * 
 */
package com.zoubworld.geometry;

/**
 * @author zoubata
 *
 */
public class DemiDroite  extends Droite{
	//double x0,y0;
	Point p0=new Point(0,0);
	protected Double theta=0.0;
	

	@Override
	public void rotate(double theta) {
		
		super.rotate(theta);
		this.theta-=theta;
		p0.rotate(theta);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		if( DemiDroite.class.isInstance(obj))
		{
		
		DemiDroite other = (DemiDroite) obj;
		if (!super.equals( other))
			return false;
		
		if (!getP0().equals( other.getP0()))
			return false;
		if (!theta.equals( other.theta))
			return false;
		
		return true;
		}
		return false;
	}
	@Override
	public void translation(double x, double y) {
		super.translation(x,y);	
		p0.translation(x, y);
	}
	
	public Double getX0() {
		return p0.getX0();
	}

	public void setX0(Double x0) {
		p0.setX0(x0); 
		
	}

	public Double getY0() {
		return p0.getY0();
	}

	public void setY0(Double y0) {
		p0.setY0(y0); 
		
	}
	public boolean contient(Point p)
	{
		
		int n=(int) (theta/(Math.PI*2));
		theta=theta-n*(Math.PI*2);
		if (theta<0)
		theta=theta+(Math.PI*2);
		if (
				((p.getX0()>=getX0()) && (Math.abs(theta)<=Unit.degreToRadian(90)))
			||
			    ((p.getX0()>=getX0()) && (Math.abs(theta)>=Unit.degreToRadian(360-90)))
			)
		return super.contient( p);
		else
			if (
					((p.getX0()<=getX0()) && (Math.abs(theta)>=Unit.degreToRadian(90)))
				&&
				    ((p.getX0()<=getX0()) && (Math.abs(theta)<=Unit.degreToRadian(360-90)))
				)
			return super.contient( p);
				
		return false;
		
				
	}
		public boolean contient(double x,double y)
		{
			return contient(new Point(x,y));
		
	}

		@SuppressWarnings("unused")
		private DemiDroite(double a, double b)
		{
			super(a,b,null);
			
		}
		@SuppressWarnings("unused")
		private DemiDroite(double a, double b,Double c)
		{
			super(a,b,null);
			
		}
		/**
	 * 
	 */
	public DemiDroite(Double x0,Double y0,Double theta) {
		super();
		p0=new Point(x0,y0);
		set(p0, theta);
		/*
		p0.setX0(x0);
		p0.setY0(y0);		
		if (theta==Math.PI)
			{a=Double.NEGATIVE_INFINITY;b=y0;}
		else
		{
			a=Math.tan(theta);
			
		b=y0-a*x0;
		}
		Double A=a;
		if (A.isInfinite())
			c=x0;
		
		this.theta=theta;
		*/
	}
	public DemiDroite(Point p,double theta) {
		super();
		 set(p, theta);
		 /*
		p0=new Point(p);		
		if (theta==Math.PI)
			{a=Double.NEGATIVE_INFINITY;b=getY0();}
		else
		{
			a=Math.tan(theta);
			
		b=getY0()-a*getX0();
		}
		Double A=a;
		if (A.isInfinite())
			c=getX0();
		
		this.theta=theta;
		*/
	}

	public Point getP0() {
		return p0;
	}

	public String toString()
	{
		return "DemiDroite("+getX0()+","+getY0()+", angle:"+(getTheta()/Math.PI*180)+"�)";
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public Double seCoupeEnX(DemiDroite s1) {
		Double x=Droite.seCoupeEnX(s1,this);
		if (x==null)
			return null;
		if ((getX0()<x) && (Math.abs(getTheta())<(Math.PI/2)))
		return x;
		if ((getX0()>x) && (Math.abs(getTheta())>(Math.PI/2)))
				return x;
		return null;
		
	}
	public String toSvg()
	{
		double x1=1000*Math.cos(theta);
		double y1=getY(x1);
		
		return "<line x1=\""+Unit.MtoMm(getX0())+"mm\" y1=\""+Unit.MtoMm(getY0())+"mm\" x2=\""+Unit.MtoMm(x1)+"mm\" y2=\""+Unit.MtoMm(y1)+"mm\" style=\"stroke:rgb(255,0,0);stroke-width:2\" />";		
	}

	public static Point seCoupe(DemiDroite a,DemiDroite b)
	{
		Point p=null;
		Double x=seCoupeEnX(a, b);
		Double y=seCoupeEnY(a, b);
		if(x!=null && y!=null)
			p=new Point(x,y);
		return p;
	}
	public Double seCoupeEnY(DemiDroite s1) {
		/*Double x=Droite.seCoupeEnX(s1,this);*/
		Double y=Droite.seCoupeEnY(s1,this);
		if (y==null)
			return null;
		if ((getY0()<y) && ((getTheta())<(Math.PI)))
			return y;
			if ((getY0()>y) && (Math.abs(getTheta())>(Math.PI)))
					return y;
			return null;
	}

	public double getTheta() {
		this.theta=theta-2*Math.PI*((int)(theta/(2*Math.PI)));
		return theta;
	}

	public void set(Point p02, Double angle) {
		p0=new Point(p02);
		theta=angle;
		double b=p0.getY0();
		//y=ax+b;				b=y-ax		
		b=p0.getY0()-p0.getX0()*Math.tan(angle);
		if(Math.abs(angle)==Math.PI/2)
			super.set(Double.NaN,p0.getY0(),p0.getX0());
		else
			super.set(Math.tan(angle),b,null);
		
	}

	

	


}
