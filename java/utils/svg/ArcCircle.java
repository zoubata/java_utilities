package com.zoubworld.java.utils.svg;

import com.zoubworld.geometry.Point;
import com.zoubworld.geometry.Segment;
import com.zoubworld.geometry.Unit;

public class ArcCircle extends BasicSvg{
	Point centre;
	double rayon;
	double angle1;
	double angle2;
	public ArcCircle() {
		// TODO Auto-generated constructor stub
	}
	//<path d ="M10,50 A 50 20 0 1 1 110,50" fill ="none" stroke ="red" stroke-width ="4" stroke-dasharray ="5 5"/>
	//<path d ="Mx1,y1 A rx ry 0 1 1 x2,y2" fill ="none" stroke ="red" stroke-width ="4" stroke-dasharray ="5 5"/>
	@Override
	public String toSVG() {
		double x1=centre.getX0()+Math.sin(angle1)*rayon;
		double y1=centre.getY0()+Math.cos(angle1)*rayon;
		double x2=centre.getX0()+Math.sin(angle2)*rayon;
		double y2=centre.getY0()+Math.cos(angle2)*rayon;
		double l=0.1;
		l=((angle2-angle1)%(Math.PI*2))*rayon/10;
		l=Math.max(l, 0.012);
		Segment s1=new Segment(  l/2, (angle2-Math.PI/2+Math.PI/6)%(Math.PI*2),new Point(x2,y2));
		Segment s0=new Segment( l/2, (angle2-Math.PI/2-Math.PI/3)%(Math.PI*2),new Point(x2,y2));
		
		return s0.toSvg()+s1.toSvg()+"<path d =\"M"+Unit.MtoPx(x1)+","+Unit.MtoPx(y1)+" A "+Unit.MtoPx(rayon)+" "+Unit.MtoPx(rayon)+" 0 "+(((angle2-angle1)%(Math.PI*2))<Math.PI?0:1)+" "+(((angle2-angle1)%(Math.PI*2))<0?1:0)+" "+Unit.MtoPx(x2)+","+Unit.MtoPx(y2)+"\" fill =\"none\" stroke =\"red\" stroke-width =\"10\"/>";
	}
	/**
	 * @param centre
	 * @param rayon
	 * @param angle1
	 * @param angle2
	 */
	public ArcCircle(Point centre, double rayon, double angle1, double angle2) {
		super();
		this.centre = centre;
		this.rayon = rayon;
		this.angle1 = angle1;
		this.angle2 = angle2;
	}
	
}
