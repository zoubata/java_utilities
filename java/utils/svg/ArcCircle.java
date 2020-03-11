package com.zoubworld.java.utils.svg;

import com.zoubworld.geometry.Circle;
import com.zoubworld.geometry.Point;
import com.zoubworld.geometry.Segment;
import com.zoubworld.geometry.Unit;
import com.zoubworld.java.svg.ItoSvg;
import com.zoubworld.java.svg.SvgRender;
import com.zoubworld.utils.JavaUtils;

public class ArcCircle extends BasicSvg implements ItoSvg{
	Point centre;
	double rayon;
	double angle1;
	double angle2;
	public ArcCircle() {
		// TODO Auto-generated constructor stub
	}
	public Point getP0() {
		double x1=centre.getX0()+Math.sin(angle1)*rayon;
		double y1=centre.getY0()+Math.cos(angle1)*rayon;
		
		return new Point(x1,y1);
	}

	public Point getP1() {
		double x2=centre.getX0()+Math.sin(angle2)*rayon;
		double y2=centre.getY0()+Math.cos(angle2)*rayon;
		return new Point(x2,y2);
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
		l=((angle2-angle1)%(Math.PI*2))*rayon*0.2;
		l=Math.max(l, 0.012);
		double a=(new Segment(centre,getP1())).getTheta()+Math.PI/2;
		Segment s1=new Segment(  l/2, (a)%(Math.PI*2)+Math.PI/4,getP1());
		Segment s0=new Segment( l/2, (a)%(Math.PI*2)-Math.PI/4,getP1());
		
		return s0.toSvg()+s1.toSvg()+"<path d =\"M"+Unit.MtoPx(x1)+","+Unit.MtoPx(y1)+" A "+Unit.MtoPx(rayon)+" "+Unit.MtoPx(rayon)+" 0 "+(((angle2-angle1)%(Math.PI*2))<Math.PI?0:1)+" "+(((angle2-angle1)%(Math.PI*2))<0?1:0)+" "+Unit.MtoPx(x2)+","+Unit.MtoPx(y2)+"\" "+style+" />";
	}
	public static String style="fill =\"none\" stroke =\"blue\" stroke-width =\"4\"";
	public static void main(String[] args) 
	{
		Circle.style= " style=\"stroke:rgb(255,0,0);stroke-width:2\"  fill=\"none\"  " ;
		ArcCircle.style= " style=\"stroke:rgb(255,0,0);stroke-width:2\"  fill=\"none\"  " ;
		SvgRender sr= new SvgRender();
	/*	for(int i=10;i<115;i+=5)
			sr.addObject(new Circle(new Point(0.105,0.150),((double)i)/1000.0));
	*/	
		for(int i=10;i<110;i+=10)
		{
		sr.addObject(new ArcCircle(new Point(0.105,0.150),((double)i)/1000.0,Math.PI*0.02,Math.PI*0.98));
		sr.addObject(new ArcCircle(new Point(0.105,0.150),((double)i)/1000.0,-Math.PI*0.02,-Math.PI*0.98));
		}
	for(int i=15;i<110;i+=10)
	{
	sr.addObject(new ArcCircle(new Point(0.105,0.150),((double)i)/1000.0,Math.PI*0.52,Math.PI*1.48));
	sr.addObject(new ArcCircle(new Point(0.105,0.150),((double)i)/1000.0,Math.PI*0.48,-Math.PI*0.48));
	}
	
	for(int i=10;i<110;i+=10)
	{
	sr.addObject(new ArcCircle(new Point(0.105*3,0.150),((double)i)/1000.0,Math.PI*0.02,Math.PI*0.98));
	sr.addObject(new ArcCircle(new Point(0.105*3,0.150),((double)i)/1000.0,-Math.PI*0.02,-Math.PI*0.98));
	}
for(int i=15;i<110;i+=10)
{
sr.addObject(new ArcCircle(new Point(0.105*3,0.150),((double)i)/1000.0,Math.PI*0.52,Math.PI*1.48));
sr.addObject(new ArcCircle(new Point(0.105*3,0.150),((double)i)/1000.0,Math.PI*0.48,-Math.PI*0.48));
}

		JavaUtils.saveAs("C:\\Temp\\svg\\cercle.svg", sr.toSvg());
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
	@Override
	public String toSvg() {
		// TODO Auto-generated method stub
		return toSVG();
	}
	
}
