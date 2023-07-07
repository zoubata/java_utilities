package com.zoubworld.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

class JavaUtilsTest {


	@Test
	void testGetCommunPath() {
		assertEquals("C:\\temp\\", JavaUtils.getCommunPath("C:\\temp\\index.html", "C:\\temp\\out.html"));
		assertEquals("C:\\temp\\", JavaUtils.getCommunPath("C:\\temp\\4DQtIZGZ8j9Y\\.NodeZoub\\account.property", "C:\\temp\\PEwInAc33GiX\\.NodeZoub\\account.property"));
		assertEquals("C:\\temp\\", JavaUtils.getCommunPath("C:\\temp\\4DQtIZGZ8j9Y\\.NodeZoub\\account.property", "C:\\temp\\out.html"));
			
	}

	@Test
	void testGetRelativePathFromTo() {
		assertEquals(".\\..\\..\\PEwInAc33GiX\\.NodeZoub\\account.property", JavaUtils.getRelativePathFromTo("C:\\temp\\4DQtIZGZ8j9Y\\.NodeZoub\\", "C:\\temp\\PEwInAc33GiX\\.NodeZoub\\account.property"));
		assertEquals(".\\..\\..\\out.html", JavaUtils.getRelativePathFromTo("C:\\temp\\4DQtIZGZ8j9Y\\.NodeZoub\\", "C:\\temp\\out.html"));
		assertEquals(".\\4DQtIZGZ8j9Y\\.NodeZoub\\out.html", JavaUtils.getRelativePathFromTo( "C:\\temp\\","C:\\temp\\4DQtIZGZ8j9Y\\.NodeZoub\\out.html"));
		assertEquals(".\\out.html", JavaUtils.getRelativePathFromTo("C:\\temp\\", "C:\\temp\\out.html"));
		assertEquals(".\\4DQtIZGZ8j9Y\\.NodeZoub", JavaUtils.getRelativePathFromTo( "C:\\temp\\","C:\\temp\\4DQtIZGZ8j9Y\\.NodeZoub"));
		assertEquals(".\\4DQtIZGZ8j9Y\\.NodeZoub\\", JavaUtils.getRelativePathFromTo( "C:\\temp\\","C:\\temp\\4DQtIZGZ8j9Y\\.NodeZoub\\"));
		
	}

	@Test
	void Max() {

		Double[] ad= new Double[] {Double.valueOf(0.0),(double) 1, (double)5.000001,(double) 2,(double) 3, (double)4, (double)5,Math.PI,Math.E};
		List<Double> ld	=	Arrays.asList(ad);
		 
		 assertEquals(JavaUtils.Max(ad),JavaUtils.Max(ld));
		 assertEquals((double)5.000001,JavaUtils.Max(ld));
			 
		 
		 
	}
	
	@Test
	void Min() {
		Double[] ad= new Double[] {Double.valueOf(0.01),(double) 1, (double)5.000001,(double) 2,(double) 3, (double)4, (double)5,Math.PI,Math.E};
		List<Double> ld	=	Arrays.asList(ad);
		 
		 assertEquals(JavaUtils.Min(ad),JavaUtils.Min(ld));
		 assertEquals((double)0.01,JavaUtils.Min(ld));
			 
		 
		 
	}
	
	@Test
	void HistoBins() {
		Double[] ad= new Double[] {0.0,1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0};
		List<Double> ld	=	Arrays.asList(ad);
		
		List<Double> ld2=JavaUtils.HistoBins(0.0,10.0);
		 assertEquals(ld2,ld);
		 
	}
	
	@Test
	void Histo() {
		List<Double> bin=new ArrayList<Double>();
		Double[] ad= new Double[] {0.0,1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0};
		List<Double> ld	=	Arrays.asList(ad);
         Map<Double, Long> h=JavaUtils.Histo(ld, bin) ;
         assertEquals(bin,ld);
         assertEquals(h.get(0.0),1L);
         assertEquals(h.get(1.0),1L);
         assertEquals(h.get(2.0),1L);
         assertEquals(h.get(3.0),1L);
         assertEquals(h.get(4.0),1L);

         Double[] ad2= new Double[] {0.0,1.0,1.1,3.0,Math.PI,Math.E,6.0,7.0,8.0,9.0,10.0};
 		List<Double> ld2	=	Arrays.asList(ad2);
        
         h=JavaUtils.Histo(ld2, bin) ;
         assertEquals(bin,ld);
         assertEquals(h.get(0.0),1L);
         assertEquals(h.get(1.0),2L);
         assertEquals(h.get(2.0),1L);
         assertEquals(h.get(3.0),2L);
         assertEquals(h.get(4.0),0L);
         assertEquals(h.get(5.0),0L);
         assertEquals(h.get(6.0),1L);
         
	}
		
}
