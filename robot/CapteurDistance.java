/**
 * 
 */
package com.zoubworld.robot;

import com.zoubworld.geometry.DemiDroite;
import com.zoubworld.geometry.Point;
import com.zoubworld.geometry.Segment;
import com.zoubworld.geometry.iCoordTransformation;
import com.zoubworld.java.svg.ItoSvg;

/**
 * @author zoubata
 *
 */
public class CapteurDistance implements ItoSvg  , Ilocalisation,iCoordTransformation {

	//position dans le robot, le cente en 0,0; theta=0°(face avant); 
	Double x0=0.0;
	Double y0=0.0;	
	Double theta0=0.0;
	// distance obstacle capter
	Double distance=0.0;
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


	public Double getTheta0() {
		return theta0;
	}

	public void setTheta0(Double theta) {
		this.theta0 = theta;
	}

	public Double getDistance() {
		if (getPointObstacle()==null)
			return null;
		Double distance=(new Segment(getPointObstacle(),getPoint0())).longeur();
		return distance;
	}
	
	@Override
	public void rotate(double theta) {
		theta0=theta0+theta;
		Double x=x0;
		Double y=y0;
		/*
		x0=x*Math.cos(theta)+y*Math.sin(theta);
		y0=-x*Math.sin(theta)+y*Math.cos(theta);*/
	}
	@Override
	public void translation(double x, double y) {
		x0=x0+x;
		y0=y0+y;
	
		
	}
	
	public Double getDistance(double xRobot,double yRobot, double thetaRobot) {
		//M= cos(t) 	-sin(t) 	x
		//   sin(t) 	cos(t)  	y
		//	 0 			0			1
		//pc'=M*pc
		//tc'=tc+tr
		
		
		
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	/**
	 * 
	 */
	public CapteurDistance() {
		
	}
	public Point getPointObstacle() {
		return Terrain.getInstance().getPointObstacle(this.getDemiDroite());
	}
	private DemiDroite getDemiDroite() {
		DemiDroite capteur=new DemiDroite(x0,y0,theta0);
		return capteur;
	}

	public CapteurDistance(double x, double y, double angle) {
		x0=x;
		y0=y;
		theta0=angle;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public String toSvg() {
		String s="<g id=\""+this.getClass()+"\">\n";
		
			s+=	getDemiDroite().toSvg()+"\n";
/*		if(getPointObstacle()!=null)
		s+=getPointObstacle().toReferentiel(this).toSvg()+"\n";*/
		s+="</g>";
		 return s;
	}

	public String toString() {
		DemiDroite capteur=new DemiDroite(x0,y0,theta0);
		return capteur.toString();
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

	

}
