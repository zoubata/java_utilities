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
	  public void testnear() {
		 
		 Point p0=new Point(0,0);
		 Point p05=new Point(0,5);
		 Point p0m5=new Point(0,-5);
		 Point p55=new Point(5,5);
		 Point pm55=new Point(-5,5);
		 Point pm5m5=new Point(-5,-5);
					Point p1=new Point(0,10);
			Point p2=new Point(10,10);
			Point p3=new Point(10,0);
			Point pm105=new Point(-10,10*Math.sin(Math.PI/6));
			Point p105=new Point(10,10*Math.sin(Math.PI/6));
			Point p10m5=new Point(10,-10*Math.sin(Math.PI/6));
			 
			//y=ax+b;x=c
	assertEquals(0.0,Segment.getTheta(p0,p3),0.01);
	assertEquals(Math.PI/2,Segment.getTheta(p0,p1),0.01);
	assertEquals(-Math.PI/2,Segment.getTheta(p0,p0m5),0.01);
	assertEquals(Math.PI/4,Segment.getTheta(p0,p55),0.01);
	assertEquals(Math.PI*3/4,Segment.getTheta(p0,pm55),0.01);
	assertEquals(-Math.PI/6,Segment.getTheta(p0,p10m5),0.1);
		assertEquals(Math.PI/6,Segment.getTheta(p0,p105),0.1);
assertEquals(Math.PI*5/6,Segment.getTheta(p0,pm105),0.1);
	
	Segment s=new Segment(p0, p2);
	Droite d=new Droite(1,0);
	assertEquals(d.getA(),s.getDroite().getA(),0.01);
	assertEquals(d.getB(),s.getDroite().getB(),0.01);
	
	assertEquals(p0,s.getDroite().getPointofX(0));
	assertEquals(p0,s.getDroite().getPointofY(0));
	
	assertEquals(p2,s.getDroite().getPointofX(10));
	assertEquals(p2,s.getDroite().getPointofY(10));
	assertEquals(p55,s.getDroite().getPointofX(5));
	assertEquals(p55,s.getDroite().getPointofY(5));
	assertEquals(pm5m5,s.getDroite().getPointofX(-5));
	assertEquals(pm5m5,s.getDroite().getPointofY(-5));
	
	
	assertEquals(0.0,Point.angle(		p1,p0,p1),0.01);
	assertEquals(Math.PI/2,Point.angle(		p3,p0,p1),0.01);
	assertEquals(-Math.PI/2,Point.angle(		p1,p0,p3),0.01);
	assertEquals(Math.PI,Point.angle(		p0,p05,p1),0.01);
	
	System.out.print(s.toSvg()+"\r\n"+s.getDroite().toSvg());
	assertEquals(1,s.getDroite().getA(),0.01);
	assertEquals(0,s.getDroite().getB(),0.01);
	assertEquals((Double)null,s.getDroite().getC());
	assertEquals(true,s.getDroite().near(p0, 0.01));
	assertEquals(true,s.getDroite().near(p2, 0.01));
	assertEquals(true,s.getDroite().near(p55, 0.01));
	assertEquals(false,s.getDroite().near(p3, 0.01));
	assertEquals(false,s.getDroite().near(p1, 0.01));
	assertEquals(false,s.getDroite().near(p05, 0.01));

	s=new Segment(p0, p1);
	assertEquals(Double.NaN,s.getDroite().getA(),0.1);
	assertEquals(0.0,s.getDroite().getB(),0.1);
	assertEquals(0.0,s.getDroite().getC(),0.01);
	Point p30=new Point(100,0);
	Point pm30=new Point(-100,0);
	assertEquals(true,s.getDroite().near(p05, 0.01));
	assertEquals(false,s.getDroite().near(p55, 0.01));
	assertEquals(false,s.getDroite().near(p3, 0.01));
	assertEquals(false,s.getDroite().near(p30, 0.01));
	assertEquals(false,s.getDroite().near(pm30, 0.01));
	s=new Segment(p0, p3);
	assertEquals(true,s.getDroite().near(p30, 0.01));
	assertEquals(true,s.getDroite().near(pm30, 0.01));

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
			Segment sh=new Segment(p0,p3);
			Segment sh3=new Segment(p3,p0);
			Segment sh2=new Segment(p1,p2);
			Segment sv=new Segment(p0,p1);
			Segment s45=new Segment(p0,p2);
			
			assertEquals(sh,sh);
			assertEquals(sh,sh3);
			assertNotEquals(sh2,sh);
			assertNotEquals(sh2,null);
			assertNotEquals(sh2,p0);
			
		//	assertEquals(sh,new Segment());
			assertEquals(p0,sv.seCoupe(sh));
			assertEquals(null,sh2.seCoupe(sh));
			assertEquals(null,sv.seCoupe(null));
			
			assertEquals(10,s.longeur(),0.0001);

			assertEquals(Math.PI/2,s.getTheta(),0.0001);
			assertEquals(Math.PI/2,sv.getTheta(),0.0001);
			assertEquals(0,sh.getTheta(),0.0001);
			assertEquals(Math.PI/4,s45.getTheta(),0.0001);
			
			assertEquals(10,s.longeur(),0.0001);
			
			 assertTrue(s.contient(p05.getX0()
					 ,p05.getY0()));
		 assertTrue(s.contient(p05));
		 assertTrue(s.contient(p0));
		 assertTrue(s.contient(p1));
		 assertTrue(!s.contient(p2));
		 assertTrue(!s.contient(p3));
		 assertTrue(sa.contient(0.0, 0.0));
		 assertTrue(!sa.contient(0.0, 10.0));
		 assertTrue(!sa.contient((Segment)sb));
		 assertTrue(!sa.contient((Segment)null));
		 assertTrue(!sa.contient((Point)null));
				// assertTrue(sa.contient((Droite)sb));
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
	  public void testDemiDroite() {
		 Point p=new Point(0,0);
		 Point p10=new Point(1,0);
		 DemiDroite ddxy=new DemiDroite( p.getX0(),p.getY0(),0.0);
		 DemiDroite ddp=new DemiDroite( p,0.0);
		 DemiDroite ddph=new DemiDroite( p,Math.PI/2);
		 DemiDroite ddp10=new DemiDroite( p10,0);
		 
		 assertEquals(ddp,ddp);
		 assertEquals(ddxy,ddp);
		 assertNotEquals(ddph,ddp);
		 assertNotEquals(ddp10,ddp);
		 assertNotEquals(ddph,null);
		 assertNotEquals(ddp,p);
		 assertEquals(ddp.getTheta(),0.0,0);
		 assertEquals(ddph.getTheta(),Math.PI/2,0);
		 assertEquals(p,DemiDroite.seCoupe(ddph,ddp));
			 
			 
		 
	 }
	 @Test
		  public void testDroite() {
		 Droite dh=new Droite(0,1.0);
	 //y=ax+b;x=c;
	 Droite d=new Droite(Double.NaN,1.0,2.0);
			 assertEquals(d,d);
			 assertEquals(d,new Droite(Double.NaN,2.0,2.0));
			 assertNotEquals(d,null);
			 
			 assertEquals(new Droite(1,1.0,null),new Droite(1,1.0,null));
	 
	 assertEquals(new Droite(0,1.0,null),new Droite(0,1.0,null));
	 assertEquals((double)dh.getA(), 0.0,0.0);
	 assertEquals((double)dh.getB(), 1.0,0.0);
	 assertEquals(dh.getC(), null);
	 Droite dv=new Droite(Double.NaN,0,1.0);
	 assertEquals(dv.getC(), 1.0,0);
	 assertTrue(!dv.equals(dh));
	 
	 assertTrue( dv.contient(new Point(1,1)) );
	 assertTrue( !dv.contient(dh) );
	 assertTrue( dv.contient(new Droite(Double.NaN,110,1.0)) );
	 assertTrue( dv.contient(new Point(1,0)) );
	 assertTrue( !dv.contient(new Point(0,0)) );
	 
	 assertEquals(dv.getTheta(), Math.PI/2,0.0001);
	 assertEquals(dh.getTheta(), 0,0.0001);
		 
	 Droite d45=new Droite(1,0,Double.NaN);
	
	 Droite dm45=new Droite(1,0,Double.NaN);
	 assertTrue(dm45.equals(d45));
	 assertTrue(dm45.equals(dm45));
	 assertTrue(!dm45.equals(null));
	 assertTrue(d45.equals(dm45));
	 assertTrue(!d45.equals(new Segment(0.0,0.0,1.0,1.0)));
	 d=(new Segment(0.0,0.0,1.0,1.0)).getDroite();
	 assertTrue(d45.equals(d));
	 
	 Point p1=Droite.seCoupe(dh, d45);
	 assertEquals(p1, new Point(1,1));
	 
	 Point p2=Droite.seCoupe(dv, d45);
	 assertEquals(p2, new Point(1,1));
	 
	 Point p=Droite.seCoupe(dh, dv);
	 assertEquals(p, new Point(1,1));
	 assertEquals(Droite.seCoupeEnX(dh, dv),p.getX0());
	 assertEquals(Droite.seCoupeEnY(dh, dv),p.getY0());
	 
	 assertEquals(d.toSvg(), "<line x1=\"-1000000.0mm\" y1=\"-1000000.0mm\" x2=\"1000000.0mm\" y2=\"1000000.0mm\" style=\"stroke:rgb(255,0,0);stroke-width:2\" />");
	 assertEquals(d.toString(), "Droite(y=1.0*x+0.0)");
	 
	 d.setA(1.0);
	 d.setB(0.0);
	 assertEquals(d, d45);
	 d45.rotate(-Math.PI/4);
	// assertEquals(dh, d45);
	// assertEquals(dv, d45);
	 dv.translation(-1.0, 0);
	 dh.translation(0, -1.0);
		
	 
	 assertEquals(Droite.seCoupe(dh, dv), new Point(0,0));
	 Droite.main(null);
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
}		/// @todo	 assertEquals(1,ls.size());
	
	 }
	 

	 @Test
	 public void testPoint() {
		 Point p00=new Point(0,0);
		 Point p01=new Point(0,1);
		 Point p10=new Point(1,0);
		 Point p11=new Point(1,1);
		 
		 Segment s0=new Segment(p00,p01);
		 Segment s0bis=new Segment(new Point(p01),new Point(p00));
		 
		 assertTrue(s0.contient(0,0));
		 assertTrue(s0.contient(0,0.5));
		 assertTrue(s0.contient(0,1));
		 assertTrue(!s0.contient(0,1.0001));
		 assertTrue(!s0.contient(1,1));
		 assertTrue(!s0.contient((Point)null));
		 assertTrue(!s0.contient((Point)p11));
		 assertTrue(s0.contient((Point)p00));
		
		 
		 Point p050=new Point(0.5,0);
		 Segment s1=new Segment(p050, 1.0, 0.0) ;
		 assertEquals(s0 , s0bis);
		 //assertEquals(s0 , s1);
			 
		 s0.setX0(1.0);
		 s0.setY0(1.0);
		 assertEquals(s0.getP0(), p11);
		 
		 s0.setX1(1.0);
		 s0.setY1(0.0);
		 assertEquals(s0.getP1(), p10);
		
	 }
	 @Test
	 public void testPointSeg() {
		 Point p00=new Point(0,0);
		 Point p01=new Point(0,1);
		 Point p11=new Point(1,1);
		 Point p10=new Point(1,0);
		 Point p=new Point(-1,-1);
		 p.setPoint(p00);
		 assertEquals(p, p00);
		 p= new Point(p00);
		 assertEquals(p, p00);
		 p.setX0(1.0);
		 assertEquals(p, p10);
		 p.setY0(1.0);
		 assertEquals(p, p11);
		 
		 assertEquals(p10, Point.MaxX(p00, p10));
		 assertEquals(p00, Point.MinX(p00, p10));
		 
		 assertEquals(p01, Point.MaxY(p01, p10));
		 assertEquals(p10, Point.MinY(p01, p10));
		 
		 
		 assertEquals(p10, Point.MaxX(p10, p00));
		 assertEquals(p00, Point.MinX(p10, p00));
		 
		 assertEquals(p01, Point.MaxY(p10, p01));
		 assertEquals(p10, Point.MinY(p10, p01));
		 
		 
		 assertEquals(p10, Point.MaxX(p10, null));
		 assertEquals(p10, Point.MinX(p10, null));		 
		 assertEquals(p10, Point.MaxY(p10, null));
		 assertEquals(p10, Point.MinY(p10, null));
		 
		 assertEquals(p10, Point.MaxX(null, p10));
		 assertEquals(p10, Point.MinX(null, p10));		 
		 assertEquals(p10, Point.MaxY(null, p10));
		 assertEquals(p10, Point.MinY(null, p10));
		 
		 assertNotEquals(p10, null);
		 assertNotEquals(p10, new Segment(p10,p11));
		 assertEquals(p10,  p10);
		 assertNotEquals(p10,  p11);
		 assertNotEquals(p00,  p11);
		 assertNotEquals(p00,  p01);
		 assertNotEquals(p10,  p01);
		 assertEquals(p10,  p10.getPoint0());
		 
		 p.setPoint(p00);p.translation(1.0,1.0);
		 assertEquals(p11,  p); 
		 
		 assertEquals((double)p10.getTheta0(), 0.0,0.0);
		 p.setPoint(p11);
		 assertEquals(p.toSvg(),"<circle  cx=\"1000.0mm\" cy=\"1000.0mm\" r=\"3mm\" style=\"fill:rgb(255,0,0)\" />");
		 assertEquals(p.toString(),"Point(1.000000,1.000000)");
	
		 
		  p.setPoint(p10);
		  p.rotate(-Math.PI/2);
		  assertEquals(p01.getX0(),p.getX0(),0.001);
		  assertEquals(p01.getY0(),p.getY0(),0.001);
			 
		 
		 System.out.println("#######"+p.toSvg());
		 System.out.println("#######"+p.toString());
		 
		 		 
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
			 if(p!=null)
			 System.out.println(p.toString()+"\r\n");
	 
		 for(Segment p:ls)
			 if(p!=null)
			 System.out.println(p.toString()+"\r\n");
		 }
	// @todo	 assertEquals(1,ls.size());
		
	 }
	 @Test
		public void testUnit() {
			assertEquals(Unit.mmtoM(1), 0.001,0);//1M=>1000mm
			assertEquals(Unit.RadiantoDegre(Math.PI), 180,0.0001);//PI radian => 180°
			assertEquals(Unit.MtoPx(1), 3779.527559,0.0001);//1M=>pixel
				
			}
	@Test
	public void test() {
		//fail("Not yet implemented");
	}

}
