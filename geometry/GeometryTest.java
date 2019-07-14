/**
 * 
 */
package com.zoubworld.geometry;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author M43507
 *
 */
public class GeometryTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	
	 @Test
	  public void testSegment() {
		 
		 Point p0=new Point(0,0);
			Point p05=new Point(0,5);
			Point p1=new Point(0,10);
			Point p2=new Point(10,10);
			Point p3=new Point(10,0);
			Segment s=new Segment(0.0, 0.0, 0.0, 10.0);
			Segment sa=new Segment(p0,p05);
			Segment sb=new Segment(p1,p05);
			Segment sai=new Segment(p05,p0);
			
			
		 assertTrue(s.contient(p05));
		 assertTrue(s.contient(p0));
		 assertTrue(s.contient(p1));
		 assertTrue(!s.contient(p2));
		 assertTrue(!s.contient(p3));
		 assertTrue(sa.contient(0.0, 0.0));
		 assertTrue(!sa.contient(0.0, 10.0));
		 assertTrue(!sa.contient((Segment)sb));
		 assertTrue(sa.contient((Droite)sb));
		 assertTrue(s.contient(s.getMilieu()));
		 Double m=s.getMilieu().getX0();
		 assertTrue((m==0.0));
		 Double my=s.getMilieu().getY0();
		 assertTrue((my==5.0));		 
		 assertTrue(sa.contient(sa));
		 Segment ss=Segment.Merge(sa, sb);
		 Segment ssi=Segment.Merge(sai, sb);
		 assertTrue(ss.contient(ssi));
		 assertTrue(ssi.contient(ss));
		 
		 assertTrue(ss.contient(sb));
		 assertTrue(ss.contient(sa));
		 assertTrue(ss.contient(p0));
		 assertTrue(ss.contient(p05));
		 assertTrue(ss.contient(p1));
		 assertTrue(ss.longeur()==sa.longeur()+sb.longeur());
		 List<Point> lp=new ArrayList<Point>();
		 lp.add(p0);
		 lp.add(p05);
		 lp.add(p1);
		 lp.add(p2);
		 lp.add(p3);
		 lp.add(p0);
		 List<Segment> ls=	 Segment.convert(lp);

		 assertTrue(ls.size()==4);
	 }
	 
	 public double r()
	 {
		 return Math.random()*5;
	 }
	 @Test
	  public void testSegment2() {
		 Double x=0.0;
		 Double a=Math.random();
		 Double b=Math.random();
		 
		 Point p0=new Point(x,x*a+b);x=Math.random()*5;		
			Point p1=new Point(x,x*a+b);x=Math.random()*50;
			Point p2=new Point(x,x*a+b);x=Math.random()*20;
			Point p3=new Point(x,x*a+b);x=Math.random()*155;
			Point p4=new Point(x,x*a+b);
		 List<Point> lp=new ArrayList<Point>();
		 lp.add(p0);
		 lp.add(p1);
		 lp.add(p2);
		 lp.add(p3);
		 lp.add(p4);
		 Unit.setAccuracy(5.0*2);
		 List<Segment> ls=	 Segment.convert(lp);
	 for(Point p:lp)
		 System.out.println(p.toString()+"\r\n");
		 assertEquals(1,ls.size());
	
	 } @Test
	  public void testSegment4() {
		 Double x=0.0;
		 Double a=Math.random();
		 Double b=Math.random();
		 
		 Point p0=new Point(x,x*a+b+1);x=Math.random()*5;		
			Point p1=new Point(x,x*a+b+2);x=Math.random()*50;
			Point p2=new Point(x,x*a+b+4);x=Math.random()*20;
			Point p3=new Point(x,x*a+b+0);x=Math.random()*155;
			Point p4=new Point(x,x*a+b+1);
		 List<Point> lp=new ArrayList<Point>();
		 lp.add(p0);
		 lp.add(p1);
		 lp.add(p2);
		 lp.add(p3);
		 lp.add(p4);
		 Unit.setAccuracy(5.0*2);
		 List<Segment> ls=	 Segment.convert(lp);
		 {	 for(Point p:lp)
			 System.out.println(p.toString()+"\r\n");
		 for(Segment p:ls)
			 System.out.println(p.toString()+"\r\n");
}			 assertEquals(1,ls.size());
	
	 }
	 @Test
	 public void testSegment3() {
		 Double x=0.0;
		 Double a=5.0;
		 Double b=-9.0;
		 
		 Point p0=new Point(x,x*a+b+r());x=5.0;		
			Point p1=new Point(x,x*a+b+r());x=50.0;
			Point p2=new Point(x,x*a+b+r());x=20.0;
			Point p3=new Point(x,x*a+b+r());x=155.0;
			Point p4=new Point(x,x*a+b+r());
		 List<Point> lp=new ArrayList<Point>();
		 lp.add(p0);
		 lp.add(p1);
		 lp.add(p2);
		 lp.add(p3);
		 lp.add(p4);
		 Unit.setAccuracy(5.0*2);
		 List<Segment> ls=	 Segment.convert(lp);
		 if(1==ls.size())
		 {
		 for(Point p:lp)
			 System.out.println(p.toString()+"\r\n");
	 
		 for(Segment p:ls)
			 System.out.println(p.toString()+"\r\n");
		 }
		 assertEquals(1,ls.size());
		
	 }
	 
	@Test
	public void test() {
		//fail("Not yet implemented");
	}

}
