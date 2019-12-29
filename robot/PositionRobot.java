/**
 * 
 */
package com.zoubworld.robot;

import com.zoubworld.geometry.DemiDroite;
import com.zoubworld.geometry.Point;
import com.zoubworld.geometry.Unit;
import com.zoubworld.java.svg.ItoSvg;

/**
 * @author zoubata
 *
 */
public class PositionRobot implements ItoSvg, Ilocalisation {

	/**
	 * 
	 */
	public PositionRobot() {
		// TODO Auto-generated constructor stub
		Capteur= new CapteurDistance[4];
		Capteur[0]=new CapteurDistance(Unit.mmtoM(200),	Unit.mmtoM(0),		Unit.degreToRadian(0));
		Capteur[1]=new CapteurDistance(Unit.mmtoM(0),		Unit.mmtoM(200),	Unit.degreToRadian(90));
		Capteur[2]=new CapteurDistance(Unit.mmtoM(-200),	Unit.mmtoM(0),		Unit.degreToRadian(180));
		Capteur[3]=new CapteurDistance(Unit.mmtoM(0),		Unit.mmtoM(-200),	Unit.degreToRadian(270));
		
	}
/* simulation du capteur */
	public Double getDistance(int capteur)
{
		
			DemiDroite capteurc=new DemiDroite(Capteur[capteur].getX0(),Capteur[capteur].getY0(),Capteur[capteur].getTheta0());
			
			
			capteurc.rotate(getTheta0());
			capteurc.translation(getX0(),getY0());		
		
			 p=Terrain.getInstance().getPointObstacle(capteurc);
		return Terrain.getInstance().getDistanceObstacle( capteurc);
}
	Point p;
	public void test()
	{
		for(int i=0;i<Capteur.length;i++)
		{
		 Double d=getDistance(i);
		 System.out.print("distance d="+d+" en "+p);
		}
	}
	
	double x0=0;
	double y0=0;
	double theta0=0;
	CapteurDistance Capteur[];
	/**
	 * @param args
	 */
	public static void main(String[] args) {



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
	@Override
	public String toSvg() {
		String s="<g id=\""+this.getClass()+"\" transform=\"translate("+Unit.MtoPx(x0)+","+Unit.MtoPx(y0)+") rotate("+Unit.RadiantoDegre(theta0)+")\" >\n";
		s+="<rect x=\""+(-200)+"mm\" y=\""+(-200)+"mm\" width=\""+(400)+"mm\" height=\""+(400)+"mm\" style=\"fill:rgb(0,0,255);stroke-width:3;stroke:rgb(0,0,0)\" />\n";
		for(CapteurDistance s1:Capteur)
		{
			s+="\t"+s1.toSvg()+"\n";
		}
		s+="</g  >\n";
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

}
