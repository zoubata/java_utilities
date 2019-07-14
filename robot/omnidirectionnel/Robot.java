/**
 * 
 */
package com.zoubworld.robot.omnidirectionnel;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.zoubworld.geometry.Point;
import com.zoubworld.geometry.Unit;
import com.zoubworld.geometry.iCoordTransformation;
import com.zoubworld.robot.CapteurDistance;
import com.zoubworld.robot.Ilocalisation;
import com.zoubworld.robot.Terrain;

import svg.ItoSvg;
import svg.SvgRender;

/**
 * @author zoubata
 *
 */
public class Robot  implements ItoSvg,Ilocalisation,iCoordTransformation {

	/**
	 * 
	 */
	public Robot() {
		// TODO Auto-generated constructor stub
		Capteur= new CapteurDistance[3];
		Capteur[0]=new CapteurDistance(Unit.mm(0),	Unit.mm(0),		Unit.degre(0));
		Capteur[1]=new CapteurDistance(Unit.mm(0),		Unit.mm(0),	Unit.degre(120));
		Capteur[2]=new CapteurDistance(Unit.mm(0),	Unit.mm(0),		Unit.degre(240));
		
	}

	double x0=0;
	double y0=0;
	double theta0=0;
	
	
	@Override
	public void rotate(double theta) {
		theta0=theta0+theta;
		Double x=x0;
		Double y=y0;
			/*
			x0=x*Math.cos(theta)+y*Math.sin(theta);
			y0=-x*Math.sin(theta)+y*Math.cos(theta);
		*/
		for(iCoordTransformation ct:Capteur)
		{	//	ct.translation(x0-x, y0-y);
			ct.rotate(theta);
	}
	}
	@Override
	public void translation(double x, double y) {
		x0=x0+x;
		y0=y0+y;
		for(iCoordTransformation ct:Capteur)
			ct.translation(x, y);
		
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
	/* (non-Javadoc)
	 * @see robot.Ilocalisation#getX0()
	 */
	@Override
	public Double getX0() {
		return x0;
	}
	/* (non-Javadoc)
	 * @see robot.Ilocalisation#setX0(double)
	 */
	@Override
	public void setX0(Double x0) {
		this.x0 = x0;
	}
	/* (non-Javadoc)
	 * @see robot.Ilocalisation#getY0()
	 */
	@Override
	public Double getY0() {
		return y0;
	}
	/* (non-Javadoc)
	 * @see robot.Ilocalisation#setY0(double)
	 */
	@Override
	public void setY0(Double y0) {
		this.y0 = y0;
	}
	/* (non-Javadoc)
	 * @see robot.Ilocalisation#getTheta0()
	 */
	@Override
	public Double getTheta0() {
		return theta0;
	}
	/* (non-Javadoc)
	 * @see robot.Ilocalisation#setTheta0(double)
	 */
	@Override
	public void setTheta0(Double theta0) {
		this.theta0 = theta0;
	}
	CapteurDistance Capteur[];
	@Override
	public String toSvg() {
		String s="<g id=\""+this.getClass()+"\" transform=\"translate("+Unit.toPx(x0)+","+Unit.toPx(y0)+") rotate("+Unit.toDegre(theta0)+")\" >\n";
		s+="<rect x=\""+(-200)+"mm\" y=\""+(-200)+"mm\" width=\""+(400)+"mm\" height=\""+(400)+"mm\" style=\"fill:rgb(0,0,255);stroke-width:3;stroke:rgb(0,0,0)\" />\n";
		s+="</g  >\n";
		for(CapteurDistance s1:Capteur)
		{
			s+="\t"+s1.toSvg()+"\n";
			if(s1.getPointObstacle()!=null)
			s+="\t"+s1.getPointObstacle().toSvg()+"\n";
			
		}
		
			return s;
	}
	
	public String toString() {
		String s="capteur robot\n";
		for(CapteurDistance s1:Capteur)
		{
			s+="\t"+s1.toString()+"\n";
		}

			return s;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Robot r=new Robot();
		Terrain terrain=new Terrain();
		System.out.println(r.toString());
		System.out.println(terrain.toString());
		r.translation((Unit.mm(500)), (Unit.mm(500)));
		
		SvgRender svg=new SvgRender();		
		svg.getObjects().add(terrain);
		svg.getObjects().add(r);
		try {
			PrintWriter csv = new PrintWriter("c:\\temp\\svg\\file.csv");
			csv.println("a;x;y;P1;d1;P2;d2;P3;d3");
			
		for(int angle=0;angle<361;angle++)
		{
			r.rotate(Unit.degre(1));
			String s=" "+Unit.toDegre(r.theta0)+" ; "+Unit.toMm(r.x0)+" ; "+Unit.toMm(r.y0)+" ; "+
					r.Capteur[0].getPointObstacle()+" ; "+
					r.Capteur[0].getDistance()+" ; "+
					r.Capteur[1].getPointObstacle()+" ; "+
					r.Capteur[1].getDistance()+" ; "+
					r.Capteur[2].getPointObstacle()+" ; "+
					r.Capteur[2].getDistance()+" ; "+"";
			s=s.replaceAll("\\.", ",");
					s=s.replaceAll("null", "");
		csv.println(s	);
				
			PrintWriter out;
		
				out = new PrintWriter("c:\\temp\\svg\\filename-"+angle+".svg");
				out.println(svg.toSvg());
				out.close();
			
		}
	
		csv.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
