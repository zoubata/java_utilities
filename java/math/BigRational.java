package com.zoubworld.java.math;

import java.io.PrintStream;
import java.math.BigInteger;

/******************************************************************************
 *  Compilation:  javac Rational.java
 *  Execution:    java Rational
 *
 *  Immutable ADT for Rational numbers. 
 * 
 *  Invariants
 *  -----------
 *   - gcd(num, den) = 1, i.e, the rational number is in reduced form
 *   - den >= 1, the denominator is always a positive integer
 *   - 0/1 is the unique representation of 0
 *
 *  We employ some tricks to stave of overflow, but if you
 *  need arbitrary precision rationals, use BigRational.java.
 *
 *1st autor :
Copyright © 2000–2017, Robert Sedgewick and Kevin Wayne.
Last updated: Fri Oct
* updated: 2019, Pierre Valleau
 ******************************************************************************/

public class BigRational  extends Number implements Comparable<BigRational>, IBasicOperator {
	public static BigRational zero = new BigRational(0, 1);
	public static BigRational one = new BigRational(1, 1);
	public static BigRational two = new BigRational(2, 1);

    private BigInteger num;   // the numerator
    private BigInteger den;   // the denominator

    /** return the phi number(Golden mean) from serial  PHI(n)=1+1/(1+phi(n-1))
     *  up to n
     * 1,618 033 988 749 894 848 204 586 834 365 638 117 720 309 179 805 762 862 135 448 622 705 260 462 189 024 497 072 072 041
     * */
    static public BigRational getPHI(int n)
    {
    	BigRational r=BigRational.one;
    	if(n>0)
    		r=r.add(one.divide(getPHI(n-1)));
    	return r;
    }
    /** return the pi number from serial  PI=4/1-4/3+4/5-....
     * up to n*/
    static public BigRational getPI(int n)
    {
    	BigRational r=new BigRational(0,4);
    	for(int i=0;i<n;i++)
    	{
    		BigRational e=new BigRational(4*((i%2==0)?1:-1), 1+2*i);
    	//	System.out.print(e.toString() +"+");
    		r=r.add(e);
    	}//System.out.println();
    	return r;
    }
    /** precomputed approximation of PI 
     * accuracy : ~1E-30
     * */
    static public BigRational PI=new BigRational(
    		
    		BigInteger.valueOf(2646693125139304345L	),
    		BigInteger.valueOf( 	842468587426513207L )
    		
    		
    		
    	/*
    	 * 	BigInteger.valueOf(1146408	),
    		BigInteger.valueOf( 	364913)
    	
    	
    		BigInteger.valueOf(3126535	),
    		BigInteger.valueOf( 	995207)
    		*/
    		
    		 
//don't work : 	new BigInteger("314159265358979323846264338327950288419716939937510582097494459230781640628620899862803482534211706798214"),
//    		new BigInteger( "100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000")
    		);
    public static IBasicOperator cos(IBasicOperator radian)
    {
    	if (radian.compareTo(BigRational.PI)>0)
    		return cos( radian.subtract(BigRational.PI.multiply(two)));
    	if (radian.compareTo(BigRational.PI.negate())<0)
    		return cos( radian.add(BigRational.PI.multiply(two)));
    	//-PI<x<PI
    	
    	if (radian.compareTo(zero)<0)
    		return cos( radian.negate());
    	//0<x<PI
    	if (radian.compareTo(BigRational.PI.divide(two))>0)
    		return cos( BigRational.PI.subtract(radian.negate())).negate();
    	//0<x<PI/2
    	BigRational bgcos=zero;
    	for(int i=0;i<N_TaylorSeries;i++)
    		bgcos=bgcos.add(radian.power(2*i).multiply(new BigRational(i%2==0?BigInteger.ONE:BigInteger.ONE.negate(),factorial(2*i))));
    
    	
    	return bgcos;
    }   
    /*
    public BigRational tan(BigRational radian)
    {
    	return sin(radian).divide(cos(radian));
    } */  


    /**
     * see https://en.wikipedia.org/wiki/Bernoulli_number
     * */
    static public BigRational BernoulliNumber(int n)
    {
    	BigRational r=zero;
    	for(int k=0;k<=n;k++)
    	{
    		BigRational e=(new BigRational(k%2==0?1:-1,k+1));
    		e=(BigRational) e.multiply(WorpitzkyNumber(n,k));
    		r=r.add(e);
    	}
    	return r;
    }

    /**
     * see https://en.wikipedia.org/wiki/Bernoulli_number
     * */
    static public BigRational StirlingNumber2(long n,long k)
    {
    	IBasicOperator r=zero;
    //	System.out.println("StirlingNumber2 "+n+","+k);
    	for(long v=0;v<=k;v++)
    	{
    		BigRational e=new BigRational(pow(k-v,n).multiply(BigInteger.valueOf((((v%2)==0)?1:-1))),BigInteger.ONE);
    		e=(BigRational) e.multiply(binomialCoefficient(k,v));
    //		System.out.println("* "+v+"="+e);
    		r=r.add(e);
    	}
  //  	System.out.println("+=> "+r);
    	
    	return (BigRational) r.multiply(new BigRational(BigInteger.ONE,factorial(k)));
    }
    /**
     *  (n)
     *  (k)
     *  http://mathworld.wolfram.com/BinomialCoefficient.html
     *  https://en.wikipedia.org/wiki/Binomial_coefficient
     * */
    static public BigRational binomialCoefficient(long n,long k)
    {
    	return new BigRational(factorial(n),(factorial(k).multiply(factorial(n-k))));
    }

    /**
     * see https://en.wikipedia.org/wiki/Stirling_numbers_of_the_second_kind
     * */
    static public BigRational WorpitzkyNumber(long n,long k)
    {
    	BigRational r=zero;
    	/*IBasicOperator r=StirlingNumber2( n+1, k+1);
    	
    	return (BigRational) r.multiply(new BigRational(factorial(k),BigInteger.ONE));
    	*/
    	for(long v=0;v<=k;v++)
    	{
    		BigRational e=new BigRational(factorial(k).multiply(pow(v+1,n)),factorial(v).multiply(factorial(k-v)));
    		if (((v+k)%2)==1)
    			e=e.negate();
    		r=r.add(e);
    	}
    		return r;
    }

    protected static BigInteger factorial(long n)
    {
    	return factorial( BigInteger.valueOf( n));
    }
    protected static BigInteger factorial(BigInteger n){    
  	  if (n.equals(BigInteger.ZERO) )    
  	    return BigInteger.ONE;    
  	  else    
  	    return(n.multiply(factorial(n.subtract(BigInteger.ONE))));    
  	 } 
    protected static BigInteger   pow(long x,long n)
    {return pow(BigInteger.valueOf(x),n) ;
    }
    
    protected static BigInteger   pow(BigInteger x,long n){    
    	BigInteger r= x;//BigInteger.valueOf(x);
    	r=r.pow((int) n);
  /*	  for(int i=1;i<=n;i++)
  		  r*=x;
  	*/  
  	    return(r);    
  	 }    
    
  /**
     * 
     * Taylor Series approximation of Arctangent
     * */
    static public IBasicOperator tan(IBasicOperator radian)
    {
    	
    	if (radian.compareTo(BigRational.PI)>0)
    		return tan( radian.subtract(BigRational.PI.multiply(two)));
    	if (radian.compareTo(BigRational.PI.negate())<0)
    		return tan( radian.add(BigRational.PI.multiply(two)));
    	//-PI<x<PI
    	
    	if (radian.compareTo(zero)<0)
    		return tan( radian.negate()).negate();
    	//0<x<PI
    	if (radian.compareTo(BigRational.PI.divide(two))>0)
    		return tan( radian.subtract(BigRational.PI));
    	//0<x<PI/2
    	
    	BigRational r=zero;
    	
    	for(int n=1;n<N_TaylorSeries;n++)
    	{ 
    		r=r.add(radian.power(2*n-1).multiply(BernoulliNumber(2*n)).multiply(new BigRational(pow(-4,n).multiply(BigInteger.ONE.subtract(pow(4,n))),factorial(n*2))));
    	}
    	return r;
    }
    
    
    
    /**
     * 
     * Taylor Series 
     * */
    static public IBasicOperator sin(IBasicOperator radian)
    {
    	
    	if (radian.compareTo(BigRational.PI )>0)
    		return sin( radian.subtract(BigRational.PI)).negate();
    	//x<PI
    	if (radian.compareTo(BigRational.PI.negate())<0)
    		return sin( radian.add(BigRational.PI)).negate();
    	//-PI<x<PI
    	if (radian.compareTo(zero)<0)
    		return sin( radian.negate()).negate();
    	//0<x<PI
    	if (radian.compareTo(BigRational.PI.divide(two))>0)
        	return sin(BigRational.PI.subtract(radian));
    	//0<x<PI/2
    	
    	BigRational sin=zero;
    	for(int i=0;i<N_TaylorSeries;i++)
    		sin=sin.add(radian.power(2*i+1).multiply(new BigRational(i%2==0?BigInteger.ONE:BigInteger.ONE.negate(),factorial(2*i+1))));
    	
    	/*
    	BigRational sin=radian;
    	BigRational r2=radian.multiply(radian);
    	BigRational r3=r2.multiply(radian);
    	sin.add((new BigRational(-1,2*3)).multiply(r3));
    	sin.add((new BigRational(-1,2*3*4*5)).multiply(r3).multiply(r2));
    	sin.add((new BigRational(-1,2*3*4*5*6*7)).multiply(r3).multiply(r3).multiply(radian));
    	sin.add((new BigRational(-1,2*3*4*5*6*7*8*9)).multiply(r3).multiply(r3).multiply(r3));
    	sin.add((new BigRational(-1,2*3*4*5*6*7*8*9*10*11)).multiply(r3).multiply(r3).multiply(r3).multiply(r2));
    	*/
    	return sin;
    }
    
    
    /* (non-Javadoc)
	 * @see com.zoubworld.java.math.IBasicOperator#power(int)
	 */
    @Override
	public IBasicOperator power(int n)
    {
    	IBasicOperator e;
    	if (n==1)
    		return this;
    	if (n==0)
    		return one;
    	if (n==2)
    		return this.multiply(this);
    	int i=n/2;
    	
    	if (n%2==0)
    		e=one;
    	else
    		e=this;
    	BigRational e2=(BigRational) this.power(i);
    	
    	return (BigRational) e.multiply(e2.multiply(e2));
    }
    /**
     * the number of iteration on the taylor series for exp(),sin(),cos(),tan(),arctan(),...
     * */
    static int N_TaylorSeries=20;
    //complexity : 100:445s;//80:170s;//50:21s;//20:1s
    /**
     * 
     * Taylor Series approximation of Exponent
     * */
    static public IBasicOperator exp(IBasicOperator x)
    {
    	
    	IBasicOperator r=one;
    	BigInteger n=BigInteger.valueOf(1);
    	for(int i=1;i<N_TaylorSeries;i++)
    	{
    		n=n.multiply(BigInteger.valueOf(i));
    		r=r.add( x.power(i).divide(new BigRational(n,BigInteger.ONE)));
    	}
    	return r;
    }
    
    /**
     * 
     * Taylor Series approximation of Arctangent
     * */
    static public IBasicOperator arctan(IBasicOperator x)
    {
    	
    	IBasicOperator r=zero;
    	/*
    	for(int i=1;i<N_TaylorSeries;i+=2)
    	{ 
    		r=r.add(x.power(i).divide(new BigRational(((i/2)%2==0)?i:-i)));
    	}
    	*/
    	for(int n=1;n<N_TaylorSeries;n++)
        	r=r.add(x.power(2*n+1).divide(new BigRational(2*n+1)));
    	return r;
    }
    
    /**
     * 
     * Taylor Series approximation of Arcsinus
     * */
    static public IBasicOperator arccos(IBasicOperator x)
    {
    	return PI.divide(two).subtract(arcsin(x));
    }
    /**
     * 
     * Taylor Series approximation of Arcsinus
     * */
    static public IBasicOperator arcsin(IBasicOperator x)
    {
    	
    	IBasicOperator r=zero;
    	if (x.abs().compareTo(one)<0)
    	for(int n=1;n<N_TaylorSeries;n+=2)
    	{ 
    		r=r.add( x.power(2*n+1)
    				 .multiply(new BigRational((factorial(n*2))
	    						 		,pow(4,n)
	    						 			.multiply(pow(factorial(n),2))
	    						 			.multiply(BigInteger.valueOf((2*n+1))))));
    	}
    	else
    		return null;
    	return r;
    }
    
    /** return the pi number from serial  PI=3+4/(2*3*4)-4/(4*5*6)+....
     * up to n*/
    static public BigRational getPi(int n)
    {
    	BigRational r=new BigRational(3,1);
    	for(int i=1;i<n;i++)
    	{
    		BigRational e=new BigRational(4*((i%2==1)?1:-1), (2*i)*(2*i+1)*(2*i+2));
    	//	System.out.print(e.toString() +"+");
    		r=r.add(e);
    	}//System.out.println();
    	return r;
    }
    // create and initialize a new Rational object
    public BigRational(long numerator2, long denominator2) {
    	BigInteger numerator=new BigInteger(""+numerator2);
    	BigInteger denominator=new BigInteger(""+denominator2);
    	
        if (denominator2 == 0) {
            throw new ArithmeticException("denominator is zero");
        }

        // reduce fraction
        BigInteger g = gcd(numerator, denominator);
        num = numerator.divide(g);
        den = denominator.divide(g);

        // needed only for negative numbers
        if (den.compareTo(BigInteger.ZERO) < 0) { den = den.negate(); num = num.negate(); }
    }
    public BigRational(long numerator2) {
    	long denominator2=1;
    	BigInteger numerator=new BigInteger(""+numerator2);
    	BigInteger denominator=new BigInteger(""+denominator2);
    	
        if (denominator2 == 0) {
            throw new ArithmeticException("denominator is zero");
        }

        // reduce fraction
        BigInteger g = gcd(numerator, denominator);
        num = numerator.divide(g);
        den = denominator.divide(g);

        // needed only for negative numbers
        if (den.compareTo(BigInteger.ZERO) < 0) { den = den.negate(); num = num.negate(); }
    }
 // create and initialize a new Rational object
    public BigRational(BigInteger numerator, BigInteger denominator) {
    	
        if (denominator.compareTo(BigInteger.ZERO) == 0) {
            throw new ArithmeticException("denominator is zero "+numerator+"/"+denominator);
        }

        // reduce fraction
        if(numerator.compareTo(BigInteger.ZERO)!=0)
        {
        	BigInteger g = gcd(numerator, denominator);
            num = numerator.divide(g);
        den = denominator.divide(g);
        }
        else
        {
            num = numerator;//==0
            den = BigInteger.ONE;//denominator;
            //System.err.print("???");
        }
        // needed only for negative numbers
        if (den.compareTo(BigInteger.ZERO) < 0) { den = den.negate(); num = num.negate(); }
    }
   
	// return the numerator and denominator of (this)
    public BigInteger numerator()   { return num; }
    public BigInteger denominator() { return den; }

    // return double precision representation of (this)
    public double doubleValue() {
    	Double d=(double)Long.MAX_VALUE;
    	BigInteger b=new BigInteger(""+Long.MAX_VALUE);
        BigInteger r[]=num.multiply(b).divideAndRemainder(den);
    	return (double) r[0].doubleValue()/d;
    }

    // return string representation of (this)
    public String toString() { 
        if (den.compareTo(BigInteger.ONE) == 0) return num + "";
        else          return num.toString() + "/" + den.toString();
    }

    // return { -1, 0, +1 } if a < b, a = b, or a > b
    /* (non-Javadoc)
	 * @see com.zoubworld.java.math.IBasicOperator#compareTo(com.zoubworld.java.math.BigRational)
	 */
    @Override
	public int compareTo(BigRational b) {
        BigRational a = this;
        BigInteger lhs = a.num.multiply( b.den);
        BigInteger rhs = a.den.multiply( b.num);
        if (lhs.compareTo(rhs) < 0) return -1;
        if (lhs.compareTo(rhs) > 0) return +1;
        return 0;
    }

    // is this Rational object equal to y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        BigRational b = (BigRational) y;
        return compareTo(b) == 0;
    }

    // hashCode consistent with equals() and compareTo()
    // (better to hash the numerator and denominator and combine)
    public int hashCode() {
        return this.toString().hashCode();
    }


    // create and return a new rational (r.num + s.num) / (r.den + s.den)
    public static BigRational mediant(BigRational r, BigRational s) {
        return new BigRational(r.num.add( s.num), r.den.add( s.den));
    }

    // return gcd(|m|, |n|)
    private static BigInteger gcd(BigInteger m, BigInteger n) {
         m = m.abs();
         n = n.abs();
         if (n.compareTo(BigInteger.ZERO)==0) return m;
         if (n.compareTo(BigInteger.ONE)==0) return n;
     /*
        else return gcd(n, m.mod(n));*/
         return m.gcd(n);
    }
    
    // return lcm(|m|, |n|)
    private static BigInteger lcm(BigInteger m, BigInteger n) {
    	  m = m.abs();
          n = n.abs();
        return m.multiply((n.divide( gcd(m, n))));    // parentheses important to avoid overflow
    }

    // return a * b, staving off overflow as much as possible by cross-cancellation
    /* (non-Javadoc)
	 * @see com.zoubworld.java.math.IBasicOperator#multiply(com.zoubworld.java.math.BigRational)
	 */
    @Override
	public IBasicOperator multiply(IBasicOperator b1) {
        BigRational a = this;

        BigRational b = (BigRational) b1;
        // reduce p1/q2 and p2/q1, then multiply, where a = p1/q1 and b = p2/q2
        BigRational c = new BigRational(a.num, b.den);
        BigRational d = new BigRational(b.num, a.den);
        return new BigRational(c.num.multiply( d.num), c.den.multiply( d.den));
    }


    // return a + b, staving off overflow
    /* (non-Javadoc)
	 * @see com.zoubworld.java.math.IBasicOperator#add(com.zoubworld.java.math.BigRational)
	 */
    @Override
	public BigRational add(IBasicOperator b1) {
        BigRational a = this;
        BigRational b = (BigRational) b1;
        // special cases
        if (a.compareTo(zero) == 0) return b;
        if (b.compareTo(zero) == 0) return a;

        // Find gcd of numerators and denominators
        BigInteger f = gcd(a.num, b.num);
        BigInteger g = gcd(a.den, b.den);

        // add cross-product terms for numerator
        BigRational s = new BigRational((a.num.divide(f)).multiply(b.den.divide(g)).add((b.num.divide(f)).multiply(a.den.divide(g) )),
                                  lcm(a.den, b.den));

        // multiply back in
        s.num=s.num.multiply(f);
        return s;
    }

    // return -a
    /* (non-Javadoc)
	 * @see com.zoubworld.java.math.IBasicOperator#negate()
	 */
    @Override
	public BigRational negate() {
        return new BigRational(num.negate(), den);
    }

    // return |a|
    /* (non-Javadoc)
	 * @see com.zoubworld.java.math.IBasicOperator#abs()
	 */
    @Override
	public BigRational abs() {
        if (num.compareTo(BigInteger.ZERO) >= 0) return this;
        else return negate();
    }

    // return a - b
    /* (non-Javadoc)
	 * @see com.zoubworld.java.math.IBasicOperator#subtract(com.zoubworld.java.math.IBasicOperator)
	 */
    @Override
	public IBasicOperator subtract(IBasicOperator b1) {
        IBasicOperator a = this;

        BigRational b = (BigRational) b1;
        return a.add(b.negate());
    }


    public BigRational reciprocal() { return new BigRational(den, num);  }

    // return a / b
    /* (non-Javadoc)
	 * @see com.zoubworld.java.math.IBasicOperator#divide(com.zoubworld.java.math.BigRational)
	 */
    @Override
	public IBasicOperator divide(IBasicOperator b1) {
        IBasicOperator a = this;
        BigRational b=(BigRational)b1;
        return a.multiply(b.reciprocal());
    }


    // test client
    public static void main(String[] args) {
        BigRational x, y, z;
PrintStream StdOut=System.out;
        // 1/2 + 1/3 = 5/6
        x = new BigRational(1, 2);
        y = new BigRational(1, 3);
        z = x.add(y);
        StdOut.println(z);

        // 8/9 + 1/9 = 1
        x = new BigRational(8, 9);
        y = new BigRational(1, 9);
        z = x.add(y);
        StdOut.println(z);

        // 1/200000000 + 1/300000000 = 1/120000000
        x = new BigRational(1, 200000000);
        y = new BigRational(1, 300000000);
        z = x.add(y);
        StdOut.println(z);

        // 1073741789/20 + 1073741789/30 = 1073741789/12
        x = new BigRational(1073741789, 20);
        y = new BigRational(1073741789, 30);
        z = x.add(y);
        StdOut.println(z);

        //  4/17 * 17/4 = 1
        x = new BigRational(4, 17);
        y = new BigRational(17, 4);
        z = (BigRational) x.multiply(y);
        StdOut.println(z);

        // 3037141/3247033 * 3037547/3246599 = 841/961 
        x = new BigRational(3037141, 3247033);
        y = new BigRational(3037547, 3246599);
        z = (BigRational) x.multiply(y);
        StdOut.println(z);

        // 1/6 - -4/-8 = -1/3
        x = new BigRational( 1,  6);
        y = new BigRational(-4, -8);
        z = (BigRational) x.subtract(y);
        StdOut.println(z);
    }



@Override
public float floatValue() {
	return (float) doubleValue();
}

@Override
public int intValue() {
	return num.divide(den).intValue();
}

@Override
public long longValue() {
	return num.divide(den).longValue();
}


@Override
public int compareTo(IBasicOperator b) {

	return compareTo((BigRational) b);
} 
}