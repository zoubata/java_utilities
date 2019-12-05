/**
 * 
 */
package com.zoubworld.robot;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.geometry.Droite;
import com.zoubworld.geometry.Point;
import com.zoubworld.geometry.Segment;
import com.zoubworld.geometry.Unit;
import com.zoubworld.java.svg.ItoSvg;
import com.zoubworld.java.svg.SvgRender;
import com.zoubworld.utils.JavaUtils;

/**
 * @author pierre valleau
 *
 */
public class Odometry  implements ItoSvg , Cloneable{

	List<Wheel> wheels;
	
	Double x0=0.0;
	Double y0=0.0;
	Double theta0=0.0;
	
	
	 public Object clone() throws
     CloneNotSupportedException 
{ 
		 // Assign the shallow copy to new reference variable t 
		 Odometry t = (Odometry)super.clone(); 
		 t.x0=(Double) x0+0;
		 t.y0=(Double) y0+0;
		  t.theta0=theta0+0;
		  t.wheels=new ArrayList();
		  t.wheels.addAll(wheels);
	  
	        return t; 
} 
	 
	
	 public Point get3WCi(Wheel w)
	 {
		 if (wheels.size()==3)
		 {
			 List<Wheel> ws=new ArrayList();
						 ws.addAll(wheels);
						 ws.remove(w);
						 Point c=Droite.seCoupe(ws.get(0).getSegment().getDroite(), ws.get(1).getSegment().getDroite());
						 return c;
		 }
		 return null;
	 }
	/**
	 * 
	 */
	public Odometry(Wheel wr,Wheel wl) {
		wheels=new ArrayList();
		wheels.add(wr);
		wheels.add(wl);
		
	}

	/**
	 * @param args
	 * @throws CloneNotSupportedException 
	 */
	public static void main(String[] args) throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Wheel wr=new Wheel(45.0/1000,0.0,90.0/180*Math.PI,19.0/1000);
		Wheel wl=new Wheel(-45.0/1000,0.0,-90.0/180*Math.PI,19.0/1000);
		wr.setTickPerRotation(230*4);
		wl.setTickPerRotation(230*4);
		
		Double x0=0.0;
		Double y0=0.0;
		Double theta0=0.0;
		Odometry odom = new Odometry(wr, wl);
		odom.init(x0,y0,theta0);
		Odometry odom2 =(Odometry) odom.clone();
		int move[]= {-230*4,230*4};
		System.out.println(odom.toSvg());
         SvgRender svgr= new SvgRender();
         svgr.addObject(odom);
            
		JavaUtils.saveAs("c:\\temp\\1.svg", svgr.toSvg());
		odom.compute(odom.tickToM( move));
		odom.get2WCi(odom.tickToM( move));
        svgr.addObject(odom2);
		System.out.println(odom.toSvg());
		JavaUtils.saveAs("c:\\temp\\2.svg", svgr.toSvg());
	}

	public double[] tickToM(int[] move) {
		double moved[]=new double[move.length]; 
		for(int i=0;i<move.length;i++)
			moved[i]=wheels.get(i).getDistance(move[i]);
		return moved;
		
	}
	private void compute(double[] move) {

		if (move.length==2)
			
		{
		double dd=(move[0]+move[1])/2.0;
		y0=y0+dd*Math.cos(theta0);
		x0=x0+dd*Math.sin(theta0);
		theta0=((move[0])*Math.sin(wheels.get(0).getTheta0())/wheels.get(0).getDistanceFromCenter()+(move[1]*Math.sin(wheels.get(1).getTheta0())/wheels.get(1).getDistanceFromCenter()));
		}
		else
			System.err.print("compute(move) not supported"); 
		
	}
/*
compute 2
pr=a*rr
pl=a*rl=a*(dlr+rr)=a*dlr+a*rr=a*dlr+pr
a=(pl-pr)/dlr;

a=angle de rotation du mouvement du robot.
pr= distance parcourru par roue r
pl= distance parcourru par roue l
rr: distance entre roue r et le centre de rotation du mouvement du robot.
rl: distance entre roue l et le centre de rotation du mouvement du robot.
*/
public Point get2WCi(double move[])
{
	 double delta=move[0]-move[1];
	 Segment s=new Segment(wheels.get(0).getCenter(),
	 wheels.get(1).getCenter());
double angle=delta/s.longeur();
double r=move[1]/angle;
if (Math.abs(move[1])>Math.abs(move[0]))
return s.getPointFromOutSide(wheels.get(0).getCenter(), r);
else
	return s.getPointFromOutSide(wheels.get(1).getCenter(), r);
}

	private void init(Double x0, Double y0, Double theta0) {
		this.x0 = x0;
		this.y0 = y0;
		this.theta0 = theta0;
	}

	@Override
	public String toSvg() {
		String s="<g "
				+ "transform=\"rotate("+Unit.RadiantoDegre(theta0)+", "+Unit.MtoPx(x0)+","+Unit.MtoPx(y0)+") translate("+Unit.MtoPx(x0)+","+Unit.MtoPx(y0)+")\""
				+ ">\r\n";
		
		for(Wheel w: wheels)
		s+="\t"+ w.toSvg()+"\r\n";
		s+="\t"+(new Point(0,0).toSvg());
		s+="\r\n</g>\r\n";
		return s;
	}

}
