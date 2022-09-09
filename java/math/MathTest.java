package com.zoubworld.java.math;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MathTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testIntValue() {

		BigRational r2 = new BigRational(314567, 100);

		assertEquals(r2.longValue(), 3145);
	}

	@Test
	public final void testStatistique() {

		assertEquals("1/139838160",
				new BigRational(BigInteger.ONE, statistic.Ckn(5, 50).multiply(statistic.Ckn(2, 12))).toString());

		assertEquals("1/25425120",
				new BigRational(BigInteger.ONE, statistic.Ckn(5, 50).multiply(statistic.Ckn(1, 12))).toString());
		assertEquals("1/2118760",
				new BigRational(BigInteger.ONE, statistic.Ckn(5, 50).multiply(statistic.Ckn(0, 12))).toString());
		assertEquals("1/15199800",
				new BigRational(BigInteger.ONE, statistic.Ckn(4, 50).multiply(statistic.Ckn(2, 12))).toString());
		assertEquals("1/2763600",
				new BigRational(BigInteger.ONE, statistic.Ckn(4, 50).multiply(statistic.Ckn(1, 12))).toString());
		assertEquals("1/1293600",
				new BigRational(BigInteger.ONE, statistic.Ckn(3, 50).multiply(statistic.Ckn(2, 12))).toString());
		assertEquals("1/230300",
				new BigRational(BigInteger.ONE, statistic.Ckn(4, 50).multiply(statistic.Ckn(0, 12))).toString());
		assertEquals("1/80850",
				new BigRational(BigInteger.ONE, statistic.Ckn(2, 50).multiply(statistic.Ckn(2, 12))).toString());
		assertEquals("1/235200",
				new BigRational(BigInteger.ONE, statistic.Ckn(3, 50).multiply(statistic.Ckn(1, 12))).toString());
		assertEquals("1/19600",
				new BigRational(BigInteger.ONE, statistic.Ckn(3, 50).multiply(statistic.Ckn(0, 12))).toString());
		assertEquals("1/3300",
				new BigRational(BigInteger.ONE, statistic.Ckn(1, 50).multiply(statistic.Ckn(2, 12))).toString());
		assertEquals("1/14700",
				new BigRational(BigInteger.ONE, statistic.Ckn(2, 50).multiply(statistic.Ckn(1, 12))).toString());
		assertEquals("1/1225",
				new BigRational(BigInteger.ONE, statistic.Ckn(2, 50).multiply(statistic.Ckn(0, 12))).toString());
	}
	@Test
	public final void testMatrix2() {
		double ar1[][]= {{0.1,0.2,0.3},{0.4,0.5,0.6},{0.7,0.8,0.9}};
		Double Ar1[][]= {{0.1,0.2,0.3},{0.4,0.5,0.6},{0.7,0.8,0.9}};
		Jama.Matrix M1=new Jama.Matrix(ar1);
		Matrix m1=new Matrix(Ar1);
		
		double ar2[][]= {{1.0,2.0,3.0},{4.0,5.0,6.0},{7.0,8.0,9.0}};
		Double Ar2[][]= {{1.0,2.0,3.0},{4.0,5.0,6.0},{7.0,8.0,9.0}};
		Jama.Matrix M2=new Jama.Matrix(ar2);
		Matrix m2=new Matrix(Ar2);
		
		Jama.Matrix Mr=M1.times(M2);		
		Matrix mr=m1.multiply(m2);
		
		assertArrayEquals(m1.getData(), M1.getArrayCopy());
		assertArrayEquals(m2.getData(), M2.getArrayCopy());
		assertArrayEquals(mr.getData(), Mr.getArrayCopy());
		assertEquals("{{3.000,3.600,4.200,},\r\n" + 
				"{6.600,8.100,9.600,},\r\n" + 
				"{10.200,12.600,15.000,},\r\n" + 
				"}",mr.toString());
		

		double ar3[][]= {{1.0,2.0,3.0}};
		Double Ar3[][]= {{1.0,2.0,3.0}};
		 M1=new Jama.Matrix(ar3);
		 m1=new Matrix(Ar3);
		
		 Mr=M1.times(M2);		
		 mr=m1.multiply(m2);
		
		assertArrayEquals(m1.getData(), M1.getArrayCopy());
		assertArrayEquals(m2.getData(), M2.getArrayCopy());
		assertArrayEquals(mr.getData(), Mr.getArrayCopy());
		assertEquals("{{30.000,36.000,42.000,},\r\n" + 
				"}",mr.toString());
		
		
		}
	@Test
	public final void testCkn() {
		
		BigInteger n=BigInteger.valueOf(52L);
		BigInteger k=BigInteger.valueOf(5L);
		assertEquals(statistic.Ckn(k, n),BigInteger.valueOf(2598960L));
		assertEquals(statistic.Ckn(5, 52),BigInteger.valueOf(2598960L));
		
		 n=BigInteger.valueOf(15);
		 k=BigInteger.valueOf(10);
		assertEquals(statistic.Ckn(k, n),BigInteger.valueOf(3003));
		 n=BigInteger.valueOf(4);
		 k=BigInteger.valueOf(3);
		assertEquals(statistic.Ckn(k, n),BigInteger.valueOf(4));
		 n=BigInteger.valueOf(36);
		 k=BigInteger.valueOf(9);
		assertEquals(statistic.Ckn(k, n),BigInteger.valueOf(94143280));
		
		 n=BigInteger.valueOf(1500);
		 k=BigInteger.valueOf(10);
		assertEquals(statistic.Ckn(k, n).toString(),"15420310928028718469107350");
		 n=BigInteger.valueOf(150);
		 k=BigInteger.valueOf(90);
		assertEquals(statistic.Ckn(k, n).toString(),"4621498022322629883980226171186143574727600");
		
		
	}
	@Test
	public final void testFactoriel() {
		assertEquals(statistic.factoriel(BigInteger.valueOf(4L)),BigInteger.valueOf(24L));
		
		assertEquals(statistic.factoriel(BigInteger.valueOf(5L)),BigInteger.valueOf(24*5L));
		assertEquals(statistic.factoriel(BigInteger.valueOf(16L)),BigInteger.valueOf(20922789888000L));
		long t0 = System.nanoTime();
		BigInteger n1 = statistic.factoriel(BigInteger.valueOf(1024L));
		long t1 = System.nanoTime();
		BigInteger n2 = statistic.factoriel(BigInteger.valueOf(1025L));
		long t2 = System.nanoTime();
		
		assertTrue((t2-t1)<(t1-t0)*0.1);//10 time faster
		System.out.println("f(1024)"+(t1-t0)+"ns");
		System.out.println("f(1025)"+(t2-t1)+"ns");
		assertEquals(n1.multiply(BigInteger.valueOf(1025)),n2);
			
		assertEquals(statistic.factoriel(BigInteger.valueOf(17)), 	BigInteger.valueOf(355687428096000L));
		assertEquals(statistic.factoriel(BigInteger.valueOf(16)), 	BigInteger.valueOf(20922789888000L));
		assertEquals(statistic.factoriel(BigInteger.valueOf(20)), 	BigInteger.valueOf(2432902008176640000L));
		assertEquals(statistic.factoriel(BigInteger.valueOf(19)), 	BigInteger.valueOf(121645100408832000L));
		assertEquals(statistic.factoriel(BigInteger.valueOf(18)), 	BigInteger.valueOf(6402373705728000L));
		
		assertEquals(statistic.factoriel(BigInteger.valueOf(15)), 	BigInteger.valueOf(1307674368000L));
		assertEquals(statistic.factoriel(BigInteger.valueOf(12)), 	BigInteger.valueOf(479001600L));
		
	}
	
	@Test
	public final void testPrime() {
		BigInteger n=BigInteger.valueOf(13L);
		assertTrue(statistic.isPrime(n));
		
		n=BigInteger.valueOf(97L);
		assertTrue(statistic.isPrime(n));
		
		 n=BigInteger.valueOf(65537L);
		assertTrue(statistic.isPrime(n));
		
		n=BigInteger.valueOf(257L);
		assertTrue(statistic.isPrime(n));
		n=BigInteger.valueOf(256L);
		assertFalse(statistic.isPrime(n));
		int prime[]= {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97,//Nombres premiers <100
		257,65537,//Nombres fermat
		127, 157, 211, 241, 307, 421, 463 ,601, 757, 1093, 1123, 1483, 1723, 2551, 2801, 2971, 3307, 3541, 3907, 4423, 4831, 5113, 5701, 6007, 6163, 6481, 8011, 8191, 9901, 10303, 11131, 12211, 12433, 13807, 14281, 17293, 19183, 19531, 20023//Nombres  premiers brésiliens
		};
		
		for(int N:prime)
			n=BigInteger.valueOf(N);
				assertTrue(statistic.isPrime(n));
		
	}
	@Test
	public final void testAkn() {
		
		BigInteger n=BigInteger.valueOf(4L);
		BigInteger k=BigInteger.valueOf(3L);
		assertEquals(statistic.Akn2(k,n),BigInteger.valueOf(24L));
		assertEquals(statistic.Akn(n,k),BigInteger.ZERO);
		assertEquals(statistic.Akn2(n,k),BigInteger.ZERO);
		assertEquals(statistic.Akn(k,n), statistic.Akn2(k,n));
		
		n=BigInteger.valueOf(4L);
		k=BigInteger.valueOf(2L);
		assertEquals(statistic.Akn(k,n), statistic.Akn2(k,n));
		n=BigInteger.valueOf(95L);
		k=BigInteger.valueOf(89L);
		assertEquals(statistic.Akn(k,n), statistic.Akn2(k,n));
		
	}
	@SuppressWarnings("unlikely-arg-type")
	@Test
	public final void testMatrix() {
		Matrix a = new Matrix(3, 2);
		assertEquals(3, a.sizeX());
		assertEquals(2, a.sizeY());
		Double data[][] = { { 2.0, 3.0, 4.0 }, { 1.0, 0.0, 0.0 } };
		a.setData(data);
		assertEquals(3, a.sizeX());
		assertEquals(2, a.sizeY());
		// a.transposition();
		Matrix b = new Matrix(2, 3);
		Double datb[][] = { { 0.0, 1000.0 }, { 1.0, 100.0 }, { 0.0, 10.0 } };
		b.setData(datb);
		// b.transposition();
		Matrix r = new Matrix(2, 2);
		Double datr[][] = { { 3.0, 2340.0 }, { 0.0, 1000.0 } };
		r.setData(datr);
		// r.transposition();
		assertEquals(r, a.multiply(b));
		Matrix m = new Matrix(2);
		Vector v = new Vector(2);

		assertEquals("{{0.000,0.000,},\r\n" + 
				"{0.000,0.000,},\r\n" + 
				"}", m.toString());
		assertEquals("{nul,nul,}", v.toString());
		v.init(0);
		m.zero();
		assertEquals(v.toDiagonalMatrix(), m);
		m.identity();
		assertEquals("{{1.000,0.000,},\r\n" + "{0.000,1.000,},\r\n" + "}", m.toString());
		assertEquals(m, m);
		assertNotEquals(m, (Matrix) null);
		assertNotEquals((Matrix) null, m);
		assertNotEquals(v, m);

		assertEquals(m, m.transposition());
		m=m.multiply(4);
		assertEquals("{{4.000,0.000,},\r\n" + "{0.000,4.000,},\r\n" + "}", m.toString());
		Matrix m2 = new Matrix(2);
		m2.identity();
		m2=m2.add(m2);
		m2=m2.add(m2);
		assertEquals(m2, m);
		m.zero();
		assertEquals("{{0.000,0.000,},\r\n" + "{0.000,0.000,},\r\n" + "}", m.toString());
		m.identity();
		m2.identity();
		assertEquals(true, m.equals(m2));
		m = new Matrix(2,3);
		m.identity();//impossible
		assertEquals("{{0.000,0.000,},\r\n" + 
				"{0.000,0.000,},\r\n" + 
				"{0.000,0.000,},\r\n" + 
				"}", m.toString());
		assertEquals(null, m.inverse());///@todo 
		assertEquals(true, m.equals(m));
		m2.zero();
		assertEquals(false, m.equals(m2));
		m = new Matrix(4,5);
		assertEquals(null, m.multiply(m2));
		m = new Matrix(2,2);
		m.identity();
		assertEquals(false, m.equals(m2));
		assertEquals(false, m.equals(v));
		assertEquals(31744, m2.hashCode());
		assertNotEquals(m2.hashCode(), m.hashCode());
		assertEquals("{0.000,0.000,}", m.multiply(v).toString());
		
		
		
		
	}

	@Test
	public final void testLongValue() {

		BigRational r2 = new BigRational(314567, 100);

		assertEquals(r2.longValue(), 3145);

	}

	@Test
	public final void testFloatValue() {
		BigRational r2 = new BigRational(5, 100);

		assertEquals(BigRational.getPI(1).doubleValue(), 4, 0.000000000001);
		assertEquals(BigRational.getPI(2).doubleValue(), 2.6666, 0.0001);
		assertEquals(BigRational.getPI(3).doubleValue(), 3.4666, 0.0001);
		assertEquals(BigRational.getPI(4).doubleValue(), 2.8952, 0.0001);
		assertEquals(BigRational.getPI(5).doubleValue(), 3.3396, 0.0001);

		assertEquals(BigRational.getPi(1).doubleValue(), 3, 0.000000000001);
		assertEquals(BigRational.getPi(2).doubleValue(), 3.1666, 0.0001);
		assertEquals(BigRational.getPi(3).doubleValue(), 3.1333, 0.0001);
		assertEquals(BigRational.getPi(4).doubleValue(), 3.1452, 0.0001);
		assertEquals(BigRational.getPi(5).doubleValue(), 3.1396, 0.0001);
		assertEquals(BigRational.PI.doubleValue(), 3.14159265358979, 0.000000001);
		int n = 100;
		System.out.println(BigRational.getPi(n));
		System.out.println(BigRational.getPi(n).doubleValue());

		System.out.println(BigRational.getPI(n));
		System.out.println(BigRational.getPI(n).doubleValue());

		assertEquals(r2.floatValue(), 0.05, 0.00001);
	}

	@Test
	public final void testDoubleValue() {
		BigRational r2 = new BigRational(5, 100);

		assertEquals(r2.doubleValue(), 0.05, 0.0);
	}

	@Test
	public final void testNumerator() {

		BigRational r2 = new BigRational(5, 7);

		assertEquals(r2.numerator().intValue(), 5);
	}

	@Test
	public final void testDenominator() {
		BigRational r2 = new BigRational(5, 7);

		assertEquals(r2.denominator().intValue(), 7);
	}

	@Test
	public final void testToString() {
		IBasicOperator r = new BigRational(314, 100);
		assertEquals(r.toString(), "157/50");
	}

	@Test
	public final void testCompareTo() {
		BigRational r = new BigRational(314, 100);
		BigRational r1 = new BigRational(157, 50);

		BigRational r2 = new BigRational(156, 50);
		BigRational r3 = new BigRational(158, 50);

		assertEquals(r, r1);
		assertTrue(r.compareTo(r1) == 0);
		assertTrue(r.compareTo(r2) > 0);
		assertTrue(r.compareTo(r3) < 0);

	}

	@Test
	public final void testPlus() {
		BigRational r = new BigRational(5, 6);
		BigRational r1 = new BigRational(1, 3);

		BigRational r2 = new BigRational(1, 2);
		BigRational r3 = new BigRational(6, 9);

		assertEquals(r1.add(r2), r);

		assertEquals(r3, BigRational.mediant(r1, r));
	}

	@Test
	public final void testNegate() {

		IBasicOperator r = new BigRational(1, 3);
		IBasicOperator r1 = new BigRational(-1, 3);

		assertEquals(r1.negate(), r);
	}

	@Test
	public final void testAbs() {

		IBasicOperator r = new BigRational(1, 3);
		IBasicOperator r1 = new BigRational(-1, 3);

		assertEquals(r1.abs(), r);
	}

	@Test
	public final void testTrigo() {

		assertEquals(0.0, ((BigRational) BigRational.sin(BigRational.zero)).doubleValue(), 0.00001);
		assertEquals(0.0, ((BigRational) BigRational.sin(BigRational.PI.multiply(BigRational.two))).doubleValue(),
				0.00001);
		assertEquals(0.0, ((BigRational) BigRational.sin(BigRational.PI)).doubleValue(), 0.00001);
		assertEquals(1.0, ((BigRational) BigRational.sin(BigRational.PI.divide(BigRational.two))).doubleValue(),
				0.00001);
		assertEquals(Math.sin(Math.PI / 6),
				((BigRational) BigRational.sin(BigRational.PI.divide(new BigRational(6)))).doubleValue(), 0.00001);
		assertEquals(Math.sin(Math.PI / 4),
				((BigRational) BigRational.sin(BigRational.PI.divide(new BigRational(4)))).doubleValue(), 0.00001);
		assertEquals(Math.sin(Math.PI / 3),
				((BigRational) BigRational.sin(BigRational.PI.divide(new BigRational(3)))).doubleValue(), 0.00001);

		assertEquals(0.0, ((BigRational) BigRational.tan(BigRational.zero)).doubleValue(), 0.00001);
		assertEquals(Math.tan(Math.PI / 4),
				((BigRational) BigRational.tan(BigRational.PI.divide(BigRational.two).divide(BigRational.two)))
						.doubleValue(),
				0.00001);

		assertEquals(Math.tan(Math.PI / 6),
				((BigRational) BigRational.tan(BigRational.PI.divide(new BigRational(6)))).doubleValue(), 0.00001);
		assertEquals(Math.tan(Math.PI / 4),
				((BigRational) BigRational.tan(BigRational.PI.divide(new BigRational(4)))).doubleValue(), 0.00001);
		assertEquals(Math.tan(Math.PI / 3),
				((BigRational) BigRational.tan(BigRational.PI.divide(new BigRational(3)))).doubleValue(), 0.00001);

		assertEquals(1.0, ((BigRational) BigRational.cos(BigRational.zero)).doubleValue(), 0.00001);
		assertEquals(-1.0, ((BigRational) BigRational.cos(BigRational.PI)).doubleValue(), 0.00001);
		assertEquals(0.0, ((BigRational) BigRational.cos(BigRational.PI.divide(BigRational.two))).doubleValue(),
				0.00001);
		assertEquals(Math.cos(Math.PI / 4),
				((BigRational) BigRational.cos(BigRational.PI.divide(BigRational.two).divide(BigRational.two)))
						.doubleValue(),
				0.00001);

		assertEquals(Math.cos(Math.PI / 6),
				((BigRational) BigRational.cos(BigRational.PI.divide(new BigRational(6)))).doubleValue(), 0.00001);
		assertEquals(Math.cos(Math.PI / 4),
				((BigRational) BigRational.cos(BigRational.PI.divide(new BigRational(4)))).doubleValue(), 0.00001);
		assertEquals(Math.cos(Math.PI / 3),
				((BigRational) BigRational.cos(BigRational.PI.divide(new BigRational(3)))).doubleValue(), 0.00001);
		// exp arccos arctan arcsin
		assertEquals(Math.exp(0), ((BigRational) BigRational.exp(BigRational.zero)).doubleValue(), 0.00001);
		assertEquals(Math.exp(0.5), ((BigRational) BigRational.exp(new BigRational(1, 2))).doubleValue(), 0.00001);
		assertEquals(Math.exp(1), ((BigRational) BigRational.exp(BigRational.one)).doubleValue(), 0.00001);
		// assertEquals(Math.exp(10),((BigRational) BigRational.exp(new
		// BigRational(10,1))).doubleValue(),0.00001);
		/*
		 * assertEquals(Math.atan(0),((BigRational) BigRational.arctan(
		 * BigRational.zero)).doubleValue(),0.00001);
		 * assertEquals(Math.atan(0.5),((BigRational) BigRational.arctan(
		 * BigRational.one.divide(new BigRational(2)))).doubleValue(),0.00001);
		 * assertEquals(Math.atan(1),((BigRational) BigRational.arctan(
		 * BigRational.one)).doubleValue(),0.00001);
		 * assertEquals(Math.atan(-1),((BigRational) BigRational.arctan(
		 * BigRational.one.negate())).doubleValue(),0.00001);
		 * assertEquals(Math.atan(1000),((BigRational) BigRational.arctan(new
		 * BigRational(1000))).doubleValue(),0.00001);
		 * 
		 * assertEquals(Math.acos(0),((BigRational) BigRational.arccos(
		 * BigRational.zero)).doubleValue(),0.00001);
		 * assertEquals(Math.acos(0.5),((BigRational) BigRational.arccos(
		 * BigRational.one.divide(new BigRational(2)))).doubleValue(),0.00001);
		 * assertEquals(Math.acos(1),((BigRational) BigRational.arccos(
		 * BigRational.one)).doubleValue(),0.00001);
		 * assertEquals(Math.acos(-1),((BigRational) BigRational.arccos(
		 * BigRational.one.negate())).doubleValue(),0.00001);
		 * 
		 * assertEquals(Math.asin(0),((BigRational) BigRational.arcsin(
		 * BigRational.zero)).doubleValue(),0.00001);
		 * assertEquals(Math.asin(0.5),((BigRational) BigRational.arcsin(
		 * BigRational.one.divide(new BigRational(2)))).doubleValue(),0.00001);
		 * assertEquals(Math.asin(1),((BigRational) BigRational.arcsin(
		 * BigRational.one)).doubleValue(),0.00001);
		 * assertEquals(Math.asin(-1),((BigRational) BigRational.arcsin(
		 * BigRational.one.negate())).doubleValue(),0.00001);
		 */
	}

	@Test
	public final void testNumber() {

		assertEquals(1, BigRational.pow(0, 0).longValue());
		assertEquals(1, BigRational.pow(1000, 0 + 0).longValue());
		assertEquals(1000, BigRational.pow(10, 3).longValue());

		assertEquals(1.61803398874, BigRational.getPHI(20).doubleValue(), 0.0000001);
		// 1,618 033 988 749 894 848 204 586 834 365 638 117 720 309 179 805 762 862 135
		// 448 622 705 260 462 189 024 497 072 072 041

		BigRational b = (BigRational) BigRational.PI.divide(BigRational.two);
		b = (BigRational) b.power(33);
		assertEquals(Math.pow(Math.PI / 2, 33.0), (b).doubleValue(), 0.0001);

		assertEquals(1000, ((BigRational) (new BigRational(10)).power(3)).longValue());
		assertEquals(1.0 / 27.0, ((BigRational) (new BigRational(1, 3)).power(3)).doubleValue(), 0.0001);

		assertEquals(1, BigRational.factorial(1).longValue());
		assertEquals(2 * 3 * 4, BigRational.factorial(4).longValue());

		assertEquals(1, BigRational.binomialCoefficient(4, 0).longValue());
		assertEquals(4, BigRational.binomialCoefficient(4, 1).longValue());
		assertEquals(6, BigRational.binomialCoefficient(4, 2).longValue());
		assertEquals(4, BigRational.binomialCoefficient(4, 3).longValue());
		assertEquals(1, BigRational.binomialCoefficient(4, 4).longValue());

		assertEquals(1, BigRational.binomialCoefficient(5, 0).longValue());
		assertEquals(5, BigRational.binomialCoefficient(5, 1).longValue());
		assertEquals(10, BigRational.binomialCoefficient(5, 2).longValue());
		assertEquals(10, BigRational.binomialCoefficient(5, 3).longValue());
		assertEquals(5, BigRational.binomialCoefficient(5, 4).longValue());
		assertEquals(1, BigRational.binomialCoefficient(5, 5).longValue());

		assertEquals(0, BigRational.StirlingNumber2(3, 0).longValue());
		assertEquals(1, BigRational.StirlingNumber2(3, 1).longValue());
		assertEquals(3, BigRational.StirlingNumber2(3, 2).longValue());
		assertEquals(1, BigRational.StirlingNumber2(3, 3).longValue());

		assertEquals(0, BigRational.StirlingNumber2(7, 0).longValue());
		assertEquals(1, BigRational.StirlingNumber2(7, 1).longValue());
		assertEquals(63, BigRational.StirlingNumber2(7, 2).longValue());
		assertEquals(301, BigRational.StirlingNumber2(7, 3).longValue());
		assertEquals(350, BigRational.StirlingNumber2(7, 4).longValue());
		assertEquals(140, BigRational.StirlingNumber2(7, 5).longValue());
		assertEquals(21, BigRational.StirlingNumber2(7, 6).longValue());

		assertEquals(0, BigRational.StirlingNumber2(10, 0).longValue());
		assertEquals(1, BigRational.StirlingNumber2(10, 1).longValue());
		assertEquals(511, BigRational.StirlingNumber2(10, 2).longValue());
		assertEquals(9330, BigRational.StirlingNumber2(10, 3).longValue());
		assertEquals(34105, BigRational.StirlingNumber2(10, 4).longValue());
		assertEquals(42525, BigRational.StirlingNumber2(10, 5).longValue());
		assertEquals(22827, BigRational.StirlingNumber2(10, 6).longValue());
		assertEquals(5880, BigRational.StirlingNumber2(10, 7).longValue());
		assertEquals(750, BigRational.StirlingNumber2(10, 8).longValue());
		assertEquals(45, BigRational.StirlingNumber2(10, 9).longValue());
		assertEquals(1, BigRational.StirlingNumber2(10, 10).longValue());
		assertEquals(15, BigRational.StirlingNumber2(6, 5).longValue());
		assertEquals(BigRational.StirlingNumber2(7, 2).longValue(), 63);
		assertEquals(BigRational.StirlingNumber2(10, 5).longValue(), 42525);
		assertEquals(BigRational.StirlingNumber2(9, 3).longValue(), 3025);

		assertEquals(1, BigRational.WorpitzkyNumber(0, 0).longValue());
		assertEquals(1, BigRational.WorpitzkyNumber(1, 0).longValue());
		assertEquals(1, BigRational.WorpitzkyNumber(1, 1).longValue());
		assertEquals(1, BigRational.WorpitzkyNumber(2, 0).longValue());
		assertEquals(3, BigRational.WorpitzkyNumber(2, 1).longValue());
		assertEquals(2, BigRational.WorpitzkyNumber(2, 2).longValue());
		assertEquals(1, BigRational.WorpitzkyNumber(3, 0).longValue());
		assertEquals(7, BigRational.WorpitzkyNumber(3, 1).longValue());
		assertEquals(12, BigRational.WorpitzkyNumber(3, 2).longValue());
		assertEquals(6, BigRational.WorpitzkyNumber(3, 3).longValue());

		assertEquals(1, BigRational.WorpitzkyNumber(4, 0).longValue());
		assertEquals(15, BigRational.WorpitzkyNumber(4, 1).longValue());
		assertEquals(50, BigRational.WorpitzkyNumber(4, 2).longValue());
		assertEquals(60, BigRational.WorpitzkyNumber(4, 3).longValue());
		assertEquals(24, BigRational.WorpitzkyNumber(4, 4).longValue());

		assertEquals(1, BigRational.WorpitzkyNumber(5, 0).longValue());
		assertEquals(31, BigRational.WorpitzkyNumber(5, 1).longValue());
		assertEquals(180, BigRational.WorpitzkyNumber(5, 2).longValue());
		assertEquals(390, BigRational.WorpitzkyNumber(5, 3).longValue());
		assertEquals(360, BigRational.WorpitzkyNumber(5, 4).longValue());
		assertEquals(120, BigRational.WorpitzkyNumber(5, 5).longValue());

		assertEquals(1, BigRational.WorpitzkyNumber(6, 0).longValue());
		assertEquals(63, BigRational.WorpitzkyNumber(6, 1).longValue());
		assertEquals(602, BigRational.WorpitzkyNumber(6, 2).longValue());
		assertEquals(2100, BigRational.WorpitzkyNumber(6, 3).longValue());
		assertEquals(3360, BigRational.WorpitzkyNumber(6, 4).longValue());
		assertEquals(2520, BigRational.WorpitzkyNumber(6, 5).longValue());
		assertEquals(720, BigRational.WorpitzkyNumber(6, 6).longValue());

		assertEquals(1, BigRational.WorpitzkyNumber(7, 0).longValue());
		assertEquals(127, BigRational.WorpitzkyNumber(7, 1).longValue());
		assertEquals(1932, BigRational.WorpitzkyNumber(7, 2).longValue());
		assertEquals(10206, BigRational.WorpitzkyNumber(7, 3).longValue());
		assertEquals(25200, BigRational.WorpitzkyNumber(7, 4).longValue());
		assertEquals(31920, BigRational.WorpitzkyNumber(7, 5).longValue());
		assertEquals(20160, BigRational.WorpitzkyNumber(7, 6).longValue());
		assertEquals(5040, BigRational.WorpitzkyNumber(7, 7).longValue());

		assertEquals(1, BigRational.WorpitzkyNumber(8, 0).longValue());
		assertEquals(255, BigRational.WorpitzkyNumber(8, 1).longValue());
		assertEquals(6050, BigRational.WorpitzkyNumber(8, 2).longValue());
		assertEquals(46620, BigRational.WorpitzkyNumber(8, 3).longValue());
		assertEquals(166824, BigRational.WorpitzkyNumber(8, 4).longValue());
		assertEquals(317520, BigRational.WorpitzkyNumber(8, 5).longValue());
		assertEquals(332640, BigRational.WorpitzkyNumber(8, 6).longValue());
		assertEquals(181440, BigRational.WorpitzkyNumber(8, 7).longValue());
		assertEquals(40320, BigRational.WorpitzkyNumber(8, 8).longValue());

		// 1, -511, 18660

		/*
		 * assertEquals(120,BigRational.WorpitzkyNumber(6,6).longValue());
		 * assertEquals(2520,BigRational.WorpitzkyNumber(7,6).longValue());
		 * assertEquals(31920,BigRational.WorpitzkyNumber(8,6).longValue());
		 * assertEquals(317520,BigRational.WorpitzkyNumber(9,6).longValue());
		 * assertEquals(40320,BigRational.WorpitzkyNumber(9,9).longValue());
		 * assertEquals(40320,BigRational.WorpitzkyNumber(9,9).longValue());
		 */
		assertEquals(BigRational.BernoulliNumber(0), new BigRational(1));
		assertEquals(BigRational.BernoulliNumber(1), new BigRational(1, 2));
		assertEquals(BigRational.BernoulliNumber(2), new BigRational(1, 6));
		assertEquals(BigRational.BernoulliNumber(3), new BigRational(0));
		assertEquals(BigRational.BernoulliNumber(4), new BigRational(-1, 30));
		assertEquals(BigRational.BernoulliNumber(5), new BigRational(0));
		assertEquals(BigRational.BernoulliNumber(6), new BigRational(1, 42));
		assertEquals(BigRational.BernoulliNumber(12), new BigRational(-691, 2730));
		assertEquals(BigRational.BernoulliNumber(14), new BigRational(7, 6));
		/*
		 * assertEquals(BigRational.BernoulliNumber(15),new
		 * BigRational(8615841276005L,14322));
		 * assertEquals(BigRational.BernoulliNumber(16),new BigRational(-3617,510));
		 * assertEquals(BigRational.BernoulliNumber(18),new BigRational(43867,798));
		 * assertEquals(BigRational.BernoulliNumber(20),new BigRational(-174611,330));
		 * assertEquals(BigRational.BernoulliNumber(17),new
		 * BigRational(2577687858367L,6));
		 * //assertEquals(BigRational.BernoulliNumber(20),new
		 * BigRational(261082718496449122051LL,13530));
		 */

	}

	@Test
	public final void testMinus() {
		IBasicOperator r = new BigRational(1, 6);
		IBasicOperator r1 = new BigRational(1, 3);

		IBasicOperator r2 = new BigRational(1, 2);

		assertEquals(r2.subtract(r1), r);
	}

	@Test
	public final void testDivides() {

		IBasicOperator r = new BigRational(2, 3);
		IBasicOperator r1 = new BigRational(1, 3);

		BigRational r2 = new BigRational(1, 2);

		assertEquals(r1.divide(r2), r);
	}

}
