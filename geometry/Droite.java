/**
 * 
 */
package com.zoubworld.geometry;

import com.zoubworld.java.svg.ItoSvg;
import com.zoubworld.java.svg.SvgObject;

/**
 * @author zoubata
 *
 * equation : y=ax+b
 * if a=infinity x=c+0*y
 */
public class Droite extends SvgObject implements ItoSvg, iCoordTransformation  {

	double a=0.0;
	Double c=null;
	
	public Double getA() {
		return a;
	}

	public void setA(Double a) {
		this.a = a;
	}

	public Double getB() {
		return b;
	}
	 public boolean contient(Point p) 
	 {
		 return contient( p.getX0(), p.getY0());
	 }
	
	 public boolean contient(Droite d) 
	 {
		 if (equals(d))
			 return true;
		 return contient( d,Unit.getAccuracy()) ;
	 }
	 /**
	  * error : in %
	  * */
	 public boolean contient(Droite d,Double error) 
	 {
		 
		boolean bo=((d.a==a) || (Math.abs(d.a-a)<=a*error) || (Double.isInfinite(a) && Double.isInfinite(d.a) ) );
		bo =bo && ((d.b==b) || (Math.abs(d.b-b)<=b*error));
		bo=bo && (c==d.c || (c!=null && d.c!=null && Math.abs(d.c-c)<=c*error)) ;
		 return bo;
		
				
				
	 }
	
	 
	 private boolean contient(double x, double y) {
		 if (c!=null)
			 return c==x;
		 
		 double d=Math.abs((y-(a*x+b))) ;

		 if (d>Unit.m(Unit.getAccuracy()))
	     	return false;
		return true;
	}
	 
	public static Double seCoupeEnX(Droite a,Droite b) 
	{
		Point p=seCoupe( a, b);
		if (p==null)
			return null;
		return p.getX0();
		/*
		// droite a : y=ax+b
				// droite b : y=Ax+B
				// point d'intersection : x=(B-b)/(a-A) ; y=A*x+B
				// distance = ((x0-x)�+(y+-y)�)^0.5
		
		if (a.getA().equals(b.getA()))
			if (a.getB().equals(b.getB()))
			{ return 0.0;}//(0,b)
			else
			return null;
		Double x=null;
		Double y=null;
		if(b.getA().isInfinite())
		{
			//y(b)=b(b);
			//y=b.getB();
		//	x=(y-a.getB())/a.getA();
			x=b.c;
			y=x*a.getA()+a.getB();
			
		}
		else if(a.getA().isInfinite())
		{
			//y(b)=b(b);
		//	y=a.getB();
		//	x=(y-b.getB())/b.getA();	
			
			
			x=a.c;
			y=x*b.getA()+b.getB();
		}
		else
		{
		 x=(b.getB()-a.getB())/(a.getA()-b.getA()) ;	
		
		 y=b.getA()*x+b.getB();
		if (b.getA().equals(0))
			y=b.getB();
		if (x.equals(0))
			y=b.getB();
		
		}
		return x;*/
	}
	public double getY(double x) {
		double y1=x*a+b;
		Double A=a;
		
		if (A.isInfinite())
			if (x!=c)
				return Double.NaN;
		return y1;
	}
	public String toSvg()
	{
		double x1=1000;
		double y1=getY(x1);
		double x0=-1000;
		double y0=getY(x0);
		
		return "<line x1=\""+Unit.MtoMm(x0)+"mm\" y1=\""+Unit.MtoMm(y0)+"mm\" x2=\""+Unit.MtoMm(x1)+"mm\" y2=\""+Unit.MtoMm(y1)+"mm\" style=\"stroke:rgb(255,0,0);stroke-width:2\" />";		
	}

	public static Double seCoupeEnY(Droite a,Droite b) 
	{
		Point p=seCoupe( a, b);
		if (p==null)
			return null;
		return p.getY0();
		/*
		// droite a : y=ax+b
				// droite b : y=Ax+B
				// point d'intersection : x=(B-b)/(a-A) ; y=A*x+B
				// distance = ((x0-x)�+(y+-y)�)^0.5
		if (a.getA().equals(b.getA()))
			{
			if (a.getB().equals(b.getB()))
			{ return b.getB();}//(0,b)
			else
			return null;
	}		
		Double x=null ; 
		Double y=null;
		if(b.getA().isInfinite())
		{
			//y(b)=b(b);
			//y=b.getB();
			//x=(y-a.getB())/a.getA();
			x=b.c;
			y=x*a.getA()+a.getB();
			
		}
		else if(a.getA().isInfinite())
		{
			//y(b)=b(b);
		//	y=a.getB();
		//	x=(y-b.getB())/b.getA();	
			
			
			x=a.c;
			y=x*b.getA()+b.getB();
		}
		else
		{
			 x=(b.getB()-a.getB())/(a.getA()-b.getA()) ;		
			 y=b.getA()*x+b.getB();
		if (b.getA().equals(0))
			y=b.getB();	
		if (x.equals(0))
			y=b.getB();
		}
		return y;
		*/
	}
	
	public static Point seCoupe(Droite a,Droite b) 
	{
		// droite a : y=ax+b
				// droite b : y=Ax+B
				// point d'intersection : x=(B-b)/(a-A) ; y=A*x+B
				// distance = ((x0-x)�+(y+-y)�)^0.5
		if (a.getA().equals(b.getA()))
			{
			if (a.getB().equals(b.getB()))
			{ return new Point(0.0, b.getB());}//(0,b)
			else
			return null;
	}		
		Double x=null ; 
		Double y=null;
		if(b.getA().isInfinite() || b.getA().isNaN())
		{/*
			//y(b)=b(b);
			y=b.getB();
			x=(y-a.getB())/a.getA();*/
			x=b.c;
			y=x*a.getA()+a.getB();
			
		}
		else if(a.getA().isInfinite()|| a.getA().isNaN())
		{/*
			//y(b)=b(b);
			y=a.getB();
			x=(y-b.getB())/b.getA();	
			*/
			
			x=a.c;
			y=x*b.getA()+b.getB();
		}
		else
		{
			 x=(b.getB()-a.getB())/(a.getA()-b.getA()) ;		
			 y=b.getA()*x+b.getB();
		if (b.getA().equals(0))
			y=b.getB();	
		if (x.equals(0))
			y=b.getB();
		}
		return new Point(x, y);
	}
	
	public String toString()
	{
		if (c==null)
		return "Droite(y="+String.format("%3.6f", a)+"*x+"+String.format("%3.6f", b)+")";
		return "Droite(x="+String.format("%3.6f", c)+")";
		
	}
	public void setB(Double b) {
		this.b = b;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		{
			/*
			Droite d1=new Droite(99999999999.0,0.0);
		
		Droite d2=new Droite(0.0,0.0);
		
		System.out.println(d1+" et " +d2 + " se coupe en "+Droite.seCoupeEnX(d1,d2)+","+Droite.seCoupeEnY(d1,d2)+"");
		}
		{
			Droite d1=new Droite(2.0,1.0);
			Droite d2=new Droite(-0.5,2.0);
			
			System.out.println(d1+" et " +d2 + " se coupe en "+Droite.seCoupeEnX(d1,d2)+","+Droite.seCoupeEnY(d1,d2)+"");
		
		}
		{
			Droite d1=new Droite(2.0,1.0);
			Droite d2=new Droite(2.0,2.0);
			
			System.out.println(d1+" et " +d2 + " se ne coupe pas en "+Droite.seCoupeEnX(d1,d2)+","+Droite.seCoupeEnY(d1,d2)+"");
		
		}
		
		{
			Droite d1=new Droite(2.0,1.0);
			Droite d2=new Droite(2.0,1.0);
			
			System.out.println(d1+" et " +d2 + " se  coupe partout dont en "+Droite.seCoupeEnX(d1,d2)+","+Droite.seCoupeEnY(d1,d2)+"");
		
		}
		{
			Droite d1=new Droite(0.0,0.0);
			Droite d2=new Droite(0.0,0.0);
			
			System.out.println(d1+" et " +d2 + " se  coupe partout dont en "+Droite.seCoupeEnX(d1,d2)+","+Droite.seCoupeEnY(d1,d2)+"");
		*/
		}

	}
	double b=0.0;
	
	/**
	 * 
	 */
	public Droite(double a, double b) {
		super();
		set(a,  b, null);		
	}

	/**
	 * @return the where X is cross when a=infinity
	 */
	public Double getC() {
		return c;
	}

	/** when x=c+y*0, a=infiny */
	public Droite(double a, double b,Double c) {
		super();
		set(a, b, c);
	}
	public Point getPointofX(double x)
	{
		Point p=null;
		Double y=a*x+b;
		if (c==null || c.isNaN())
		 p= new Point(x,y);		
		return p;		
	}
	public Point getPointofY(double y)
	{
		Point p=null;
		Double x=(y-b)/a;
		if (c==null || c.isNaN())
		 p= new Point(x,y);		
		else
			if(y==c)
				p= new Point(x,y);		
		return p;
		
	}
	
	/**
	 * y=a*x+b;x=c
	 * a result equal to null, or Nan, mean all value are valid.
	 * */
	public void set(double a, double b, Double c) {
		
		if(c!=null)
		{
			
		if (c.isNaN() ||			c.isInfinite())
			c=null;
		
		}
		
		if(c!=null)
			this.a=Double.NaN;
		else
			this.a=a;
		this.b=b;
		this.c=c;
		
	}
	/** do not use it
	 * */
	protected Droite() {
		
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		if( Droite.class.isInstance(obj))
		{
		Droite other = (Droite) obj;
		if (c==null)
		{
			if (a!= other.a)
				return false;
			if (b!= other.b)
				return false;
		}
		else
		if (c.equals(other.c))
			return true;
		else
			return false;
		if(a==Double.NaN)			
		return false;
		return true;
		}
		return false;
	}
	@Override
	public void rotate(double theta) {
		
		a=Math.tan(Math.atan(a)+theta);
		
	}

	@Override
	public void translation(double x, double y) {
		Double A=a;
		if (A.isInfinite()|| A.isNaN())
		{// x=c y=* A=infinity
			c=c+x;
		}
		else
		{//y=b+a.x
		b=b+y-a*x;
		}		
	}
	/** return the angle between the droite and axes x
	 * */
	public double getTheta() {
		if (c==null)
		return Math.atan(a);
		else
			return Math.PI/2;
	}

	public boolean near(Point p, double tolerance) {
		double x=p.getX0(),
				y=p.getY0();
		
		 if (c!=null)
			 return c==x;
		 double y2=(a*x+b);
		 double d=Math.abs((y-y2)) ;

		 if (d>tolerance)
	     	return false;
		return true;
		
	}

	

}
