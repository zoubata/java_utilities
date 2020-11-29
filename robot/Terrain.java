package com.zoubworld.robot;

import com.zoubworld.geometry.DemiDroite;
import com.zoubworld.geometry.Droite;
import com.zoubworld.geometry.Point;
import com.zoubworld.geometry.Segment;
import com.zoubworld.geometry.Unit;
import com.zoubworld.java.svg.ItoSvg;

public class Terrain implements ItoSvg {

	public Terrain() {
		obstacle=new  Segment[4];
		obstacle[0]=new Segment(Unit.mmtoM(0.0), 		Unit.mmtoM(0.0), 		Unit.mmtoM(3000.0),	 	Unit.mmtoM(0.0		));
		obstacle[1]=new Segment(Unit.mmtoM(0.0), 		Unit.mmtoM(0.0), 		Unit.mmtoM(0.0), 		Unit.mmtoM(2000.0	));
		obstacle[2]=new Segment(Unit.mmtoM(3000.0), 	Unit.mmtoM(2000.0), 	Unit.mmtoM(0.0),  		Unit.mmtoM(2000.0	));
		obstacle[3]=new Segment(Unit.mmtoM(3000.0), 	Unit.mmtoM(2000.0), 	Unit.mmtoM(3000.0), 	Unit.mmtoM(0.0		));
			
		instance=this;	
	}
	
	public String toString() {
		String s="Terrain\n";
		for(Segment s1:obstacle)
		{
			s+="\t"+s1.toString()+"\n";
		}

			return s;
	}
	
	Segment obstacle[]=null;
	
	public String toSvg() {
	String s="<g id=\""+this.getClass()+"\">\n";
	for(Segment s1:obstacle)
	{
		
		s+="\t"+s1.toSvg()+"\n";
	}
	s+="</g>\n"+"";
		return s;
	}
	public Double getDistanceObstacle(double x0, double y0, double theta0)
	{
		DemiDroite capteur=new DemiDroite(x0,y0,theta0);
		return  getDistanceObstacle( capteur);
	}
	public Double getDistanceObstacle(DemiDroite capteur)
	{
		// droite capteur : y=ax+b
		// droite obstacle : y=Ax+B
		// point d'intersection : x=(B-b)/(a-A) ; y=A*x+B
		// distance = ((x0-x)�+(y+-y)�)^0.5
		Double distance=null;
		
		for(Segment s1:obstacle)
		{
			Double x=capteur.seCoupeEnX(s1);
			Double y=capteur.seCoupeEnY(s1);
			
			if (s1.contient(x, y))
			{
				double d= (new Segment(x,y,capteur.getX0(),capteur.getY0())).longeur();
				System.out.println("\t-"+s1+" et "+capteur+" se croise en ("+x+","+y+") a une distance "+d);
				
				if (distance==null)
					distance= d;
				else
					distance= Math.min(d,distance);
			}
			else
				System.out.println("\t-"+s1+" et "+capteur+" ne se croise pas("+x+","+y+").");
		}
		
		return distance;
	}
	
	public Point getPointObstacle(DemiDroite capteur)
	{
	
		
		// droite capteur : y=ax+b
		// droite obstacle : y=Ax+B
		// point d'intersection : x=(B-b)/(a-A) ; y=A*x+B
		// distance = ((x0-x)�+(y+-y)�)^0.5
//		Double distance=null;
		//Point p=null;
		for(Segment s1:obstacle)
		{
			
			Point ps= Droite.seCoupe(s1, capteur);
			System.out.println("\t-"+s1+" et "+capteur+" se croise en "+ps);
			
			
			if ( s1.contient(ps)
				&&	capteur.contient(ps)
				)
			{
				System.out.println("\t-"+ps+" est dans "+capteur+" et "+s1);
				return ps;
			}
			
			/*
			Double x=capteur.seCoupeEnX(s1);
			Double y=capteur.seCoupeEnY(s1);
			
			if (s1.contient(x, y))
			{
				double d= (new Segment(x,y,capteur.getX0(),capteur.getY0())).longeur();
				System.out.println("\t-"+s1+" et "+capteur+" se croise en ("+x+","+y+") a une distance "+d);
				
				if (distance==null)
				{
					distance= d;
					p=new Point(x,y);
					}
				else
				{
					if (distance>d);
					{
					p=new Point(x,y);
					distance=d;
					}
				}
			}
			else
				System.out.println("\t-"+s1+" et "+capteur+" ne se croise pas("+x+","+y+").");*/
		}
		
		return null;
	}
	/*
	public Point getPointObstacle(DemiDroite capteur)
	{
		// droite capteur : y=ax+b
		// droite obstacle : y=Ax+B
		// point d'intersection : x=(B-b)/(a-A) ; y=A*x+B
		// distance = ((x0-x)�+(y+-y)�)^0.5
		Double distance=null;
		Point p=null;
		for(Segment s1:obstacle)
		{
			Double x=capteur.seCoupeEnX(s1);
			Double y=capteur.seCoupeEnY(s1);
			
			if (s1.estDansLeSegment(x, y))
			{
				double d= (new Segment(x,y,capteur.getX0(),capteur.getY0())).longeur();
				System.out.println("\t-"+s1+" et "+capteur+" se croise en ("+x+","+y+") a une distance "+d);
				
				if (distance==null)
				{
					distance= d;
					p=new Point(x,y);
					}
				else
				{
					if (distance>d);
					{
					p=new Point(x,y);
					distance=d;
					}
				}
			}
			else
				System.out.println("\t-"+s1+" et "+capteur+" ne se croise pas("+x+","+y+").");
		}
		
		return p;
	}
	*/
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Terrain t=new Terrain ();
		double x=Unit.mmtoM(1500);
		double y=Unit.mmtoM(1000);
		double theta=Unit.degreToRadian(0);
		Double d=t.getDistanceObstacle(x, y,theta );
		System.out.println("le point("+x+","+y+") sous l'angle "+theta+" radian est a la distance "+d+" m.");
		PositionRobot robot=new PositionRobot();
		 x=Unit.mmtoM(1);
		 y=Unit.mmtoM(1);
		 theta=Unit.degreToRadian(180);
		 d=t.getDistanceObstacle(x, y,theta );
			System.out.println("le point("+x+","+y+") sous l'angle "+theta+" radian est a la distance "+d+" m.");
			
			 theta=Unit.degreToRadian(0);
			 d=t.getDistanceObstacle(x, y,theta );
				System.out.println("le point("+x+","+y+") sous l'angle "+theta+" radian est a la distance "+d+" m.");
				robot.setX0(Unit.mmtoM(1500));
				robot.setY0(Unit.mmtoM(1000));
				robot.setTheta0(Unit.degreToRadian(0));
				robot.test();
				
				System.out.print(t.toSvg());
				System.out.print(robot.toSvg());
				System.out.print(robot);
				
		
	}
	static Terrain instance=null;
	public static Terrain getInstance() {
		
		return instance;
	}
	

}
