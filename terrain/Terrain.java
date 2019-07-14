/**
 * 
 */
package com.zoubworld.terrain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import com.zoubworld.geometry.*;
/**
 * @author pierre valleau
 *
 */
import com.zoubworld.robot.lidar.LidarData;
import com.zoubworld.utils.JavaUtils;

import svg.SvgRender;
public class Terrain implements ITerrain {

	/**
	 * 
	 */
	public Terrain() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Terrain t=new Terrain();
		Balise jg=new Balise(100.0/2,-100.0/2,90.0);	
		Balise jd=new Balise(2000-100.0/2,-100.0/2,90.0);	
		Balise jc=new Balise(2000.0/2,3000+100.0/2,270.0);	
		Balise vg=new Balise(100.0/2,3000+100.0/2,270.0);	
		Balise vd=new Balise(2000-100.0/2,3000+100.0/2,270.0);	
		Balise vc=new Balise(2000.0/2,-100.0/2,90.0);	
		
		Balise vr1=new Balise(null,null,null);	
		Balise vr2=new Balise(null,null,null);	
		Balise jr1=new Balise(null,null,null);	
		Balise jr2=new Balise(null,null,null);	
		t.add(jg);
		t.add(jc);
		t.add(jd);
		t.add(vg);
		t.add(vc);
		t.add(vd);
		t.add(vr1);
		t.add(vr2);
		t.add(jr1);
		t.add(jr2);
		
		int pitch=10;
		Double proba[][]= new Double[(int)((2000-1)/pitch)+1][];
		for(int x=0;x<proba.length;x++)
			proba[x]= new Double[(int)((3000-1)/pitch)+1];
		//for()
		/*
		BaliseDetection bd=null;
		{	
			Double d=bd.getDistance();
			Double td=bd.getThetad();
			Double x0=bd.getB().getX0();
			Double y0=bd.getB().getY0();
			Double t0=bd.getB().getT0();
			Double x1=x0+td*Math.cos(degreToRadian(td+t0));
			Double y1=y0+td*Math.cos(degreToRadian(td+t0));
			Double t1=null;
			if(proba[(int) (x1/pitch)][(int) (y1/pitch)]==null)
				proba[(int) (x1/pitch)][(int) (y1/pitch)]=(double) 1;
			else
				proba[(int) (x1/pitch)][(int) (y1/pitch)]+=(double) 1;
				
		}
		*/
		Unit.setAccuracy(30);
		List<LidarData> lld=LidarData.parse("C:\\Temp\\svg\\log.log") ;
		//for( LidarData ld:lld)
		LidarData ld=lld.get(2);
		{
			Unit.setAccuracy(90.0);
			List<Point> lp=ld.getPoints();
			
			List<Segment>		ls=Segment.convert(lp);
			SimpleRegression elt=new SimpleRegression();
			elt.addData(ld.getData());
			elt.regress();
			double r=elt.getR();
			boolean bt=elt.hasIntercept();
			double b=elt.getIntercept();
			double a=elt.getSlope();
			 	
			
			 for(Point p:lp)
			 {	
			 double y=elt.predict( p.getX0());	
				y=y-p.getY0();
				 System.out.println(p.toString()+"dy="+y);
							
			 }
			 System.out.println("\r\n");
					 for(Double p:ld.getRange())
				 System.out.println(p);
			 System.out.println("\r\n");
				
			 System.out.println(ls);
			 System.out.println(ls.size()+"+"+lp.size());
				SvgRender svg=new SvgRender();		
			svg.getObjects().addAll(ls);
		//	svg.getObjects().addAll(lp);
			JavaUtils.saveAs("C:\\Temp\\svg\\lidar-"+"--"+".svg", svg.toSvg());
		}
	}


	//static public List<Segment> convert(List<Point> lp)// lidar data converted (theta,d) => (x,y)/(0,0)
	
	//sort seg/size
	//=>3 +long = bord du terrain ==> list(x0/y0/t0) =>test() choise best proba
	


	private static double degreToRadian(double d) {
		// TODO Auto-generated method stub
		return d/360*2*Math.PI;
	}

	/**
	 * @param e
	 * @return
	 * @see java.util.Set#add(java.lang.Object)
	 */
	public boolean add(IObject e) {
		return getObject().add(e);
	}

	@Override
	public Double getX0() {
		// TODO Auto-generated method stub
		return 0.0;
	}

	@Override
	public Double getY0() {
		// TODO Auto-generated method stub
		return 0.0;
	}

	@Override
	public Double getT0() {
		// TODO Auto-generated method stub
		return 0.0;
	}

	@Override
	public Double getProbaCoord(Double Radius) {
		// TODO Auto-generated method stub
		return 1.0;
	}

	Set<IObject> objects=null;
	@Override
	public Set<IObject> getObject() {
		if (objects==null)
			objects=new HashSet();
		return objects;
	}

	@Override
	public Set<IRobot> getRobot() {
		Set<IObject> objs=getObject();
		Set<IRobot> rbts=new HashSet();
		for(IObject e:objs)
			if(IRobot.class.isInstance(e))
			rbts.add((IRobot)e);
		return rbts;
	}

	@Override
	public Set<IBalise> getBalise() {
		Set<IObject> objs=getObject();
		Set<IBalise> rbts=new HashSet();
		for(IObject e:objs)
			if(IBalise.class.isInstance(e))
			rbts.add((IBalise)e);
		return rbts;
	}


}
