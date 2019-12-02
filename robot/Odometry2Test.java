package com.zoubworld.robot;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.zoubworld.java.math.Matrix;

public class Odometry2Test {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}
	Matrix pos=new Matrix(3,1);
	Matrix speed=new Matrix(3,1);
	double time=Calendar.getInstance().getTimeInMillis();
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	public void update()
	{
		double newtime=Calendar.getInstance().getTimeInMillis();
		pos=pos.add(speed.multiply(newtime-time));
		time=newtime;
	}
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testInit() {
		Matrix whellLinearSpeed = new 	Matrix(3,1);
		Double d[]= {1.0,1.0,1.0};
		whellLinearSpeed.setData(d);
		whellLinearSpeed.transposition();
		Odometry2 od=new Odometry2();
		od.init();
		assertEquals("{{1.000,1.000,1.000,},\r\n" + 
				"}",(whellLinearSpeed).toString());

		assertEquals("{{0.110,},\r\n" + 
				"{0.000,},\r\n" + 
				"{0.000,},\r\n" + 
				"}",od.whellRotationSpeed(whellLinearSpeed).toString());

		assertEquals("{{0.110,},\r\n" + 
				"{0.000,},\r\n" + 
				"{0.000,},\r\n" + 
				"}",od.robotLinearSpeed(whellLinearSpeed).toString());
			
		Double d2[]= {1.0,-1.0,0.0};
		whellLinearSpeed.setData(d2);
		whellLinearSpeed.transposition();
		assertEquals("{{0.110,},\r\n" + 
				"{0.000,},\r\n" + 
				"{0.000,},\r\n" + 
				"},\r\n" + 
				"}",od.robotLinearSpeed(whellLinearSpeed).toString());
			
	//	fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testWhellLinearSpeed2() {
		fail("Not yet implemented"); // TODO
	}

}
