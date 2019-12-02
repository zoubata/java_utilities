/**
 * 
 */
package com.zoubworld.geometry;

/**
 * @author valleau
 *
 *the is a segment oriented with a base and arrow.
 */
public class Vector extends Segment {

	public Vector(Double x0, Double y0, Double x1, Double y1) {
		super(x0, y0, x1, y1);
		// TODO Auto-generated constructor stub
	}
	public Vector(Point pBase,Point pArrow) {
		super(pBase, pArrow);
		// TODO Auto-generated constructor stub
	}

	public Vector(Point p, Double angle, Double len) {
		super(p.getX0(), p.getY0(), p.getX0()+len*Math.cos(angle), p.getY0()+len*Math.sin(angle));
		// TODO Auto-generated constructor stub
	}

	
	public Point getArrow() {
		return getP1();
	}
	public String toSvg()
	{
		double l=0.1;
		l=this.longeur()/10;
		l=Math.max(l, 0.012);
		Segment s1=new Segment( l, getTheta()+Math.PI*5/6,getArrow());
		Segment s0=new Segment(getBase(), l/2, getTheta()+Math.PI/2);
				Segment s2=new Segment( l, getTheta()-Math.PI*5/6,getArrow());
		return "<line x1=\""+Unit.MtoMm(getX0())+"mm\" y1=\""+Unit.MtoMm(getY0())+"mm\" x2=\""+Unit.MtoMm(getX1())+"mm\" y2=\""+Unit.MtoMm(getY1())+"mm\" "+style+" />"
				+ s0.toSvg()+s1.toSvg()+s2.toSvg();		
				
	}
	public Point getBase() {
		return getP0();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		Vector v=new Vector(0.0, 0.0, 3.0, 3.0);
System.out.println(v.toSvg());
*/
	}

}
