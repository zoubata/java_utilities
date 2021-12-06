/**
 * 
 */
package com.zoubworld.java.math;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zoubworld.utils.JavaUtils;

/**
 * @author pierre valleau
 * 
 * This library is able to perform factorial(2^15) or 32768!
 * This is optimized by saving part of computation in memory allowing next computation to be faster.
 * 
 * I used it to perform Ckn(262144,22752) for example
 */

public class statistic {
	static public Node buildTreeOfPossible(int sizeOfSet,int numberOfElement,boolean reput,boolean ordered )
	{
		Set<Integer> set=new HashSet<Integer>();
		for(int i=0;i<sizeOfSet;i++)
			set.add(Integer.valueOf(i));
		Node r=new Node(null);
	 buildTreeOfPossible(r,set , numberOfElement, reput, ordered );
	return r;
	
	}
	public static Node buildTreeOfPossible(Node n,Set<Integer> set, int numberOfElement, boolean reput, boolean ordered) {
		if(numberOfElement<=0)
			return n;
		numberOfElement--;
		Set<Integer> set2=new HashSet<Integer>(set);
		
		for(Integer I:set)
		{
			Node e=new Node(I);
			if(ordered)
			{
				set2=new HashSet<Integer>(set);
					if  (!reput)
				set2.remove(I);
			}
			else
			{
				if  (!reput)
					set2.remove(I);
				else
				{
					//@todo not implemented
				}
			}
			buildTreeOfPossible(e,set2,numberOfElement, reput, ordered);
			if(set2.size()>=numberOfElement)
				n.add(e);
			
		}
		
		return n;
	}
	static Map<BigInteger,BigInteger> cache=new HashMap<BigInteger,BigInteger>();
	
	/** Factorial of any number, it is just a question of cpu time.
	 * see https://en.wikipedia.org/wiki/Factorial
	 * 
	 * This function have some optimization, that eat memory to speed up lultiple computation of factorial.
	 * to disable it set cache to null.
	 * */
	static public BigInteger factoriel(BigInteger N)
	{
		
		BigInteger b=null;
		b=BigInteger.ONE;
		long i=1;
		if (cache!=null) 
		{
		b=cache.get(N);
		if (b!=null)
			return b;
		List<BigInteger> l = new ArrayList<BigInteger>(cache.keySet());
		l=JavaUtils.asSortedList(l);
		i=0;
		if (l.size()>0)
		{
			while((i<l.size()) && (N.compareTo(l.get((int) i))>0))
				i++;
			
			{
			
				   i--;
				   if (i>=0)
				{b=cache.get(l.get((int) i));i=l.get((int) i).longValue()+1;}
				   else
					{i=1;b=BigInteger.ONE;}
					  
			}
		}
		else
		{i=1;b=BigInteger.ONE;}
		}
		
		 for(;i<=N.longValueExact();i++){    
		      b=b.multiply(BigInteger.valueOf(i));
		      
		
		      if (i%256==0)
			cache.put(BigInteger.valueOf((long)i),b);
		  } 
		 if (cache!=null) 
			 cache.put(N,b);
		return b;
	}
	
	/** arrangement sans remise
	 * basic equation
	 * see https://fr.wikipedia.org/wiki/Arrangement , 
	 * https://en.wikipedia.org/wiki/Permutation#k-permutations_of_n
	 * */
	static public BigInteger Akn2(BigInteger k,BigInteger n)
	{
		if (k.compareTo(n)>0)
			return BigInteger.ZERO;
		BigInteger b=factoriel(n);
		BigInteger c=factoriel(n.subtract(k));		
		return b.divide(c);
		
	}
	/** arrangement sans remise
	 * optimized
	 * */
	static public BigInteger Akn(BigInteger k,BigInteger n)
	{	
		if (k.compareTo(n)>0)
			return BigInteger.ZERO;
	BigInteger b = BigInteger.ONE;
	BigInteger i=null;
	 for(i=n.subtract(k).add(BigInteger.ONE);n.compareTo(i)>=0;i=i.add(BigInteger.ONE)){    
	      b=b.multiply(i);
	  } 
	 return b;
	}
	static public BigInteger Ckn(long k,long n)
	{
		return Ckn(BigInteger.valueOf( k),BigInteger.valueOf(n));
	}
	/** 
	 * see https://en.wikipedia.org/wiki/Combination
	 * */
	static  public BigInteger Ckn(BigInteger k,BigInteger n)
	{
		BigInteger b=factoriel(k);
		return Akn( k, n).divide(b);
	}
	static  public BigInteger Pn(BigInteger n)
	{
		BigInteger b=factoriel(n);
		return (b);
	}
	
	/**
	 * 
	 */
	public statistic() {
		// TODO Auto-generated constructor stub
	}

	static List<BigInteger> prime=new ArrayList<BigInteger>();
	public static boolean isPrime(BigInteger n) 
	{
		if (prime==null)
			prime=new ArrayList<BigInteger>();
		if(prime.size()==0)
			prime.add(BigInteger.ONE.add(BigInteger.ONE));
		int index=prime.size()-1;
		BigInteger last = prime.get(index);
		for(BigInteger i=last.add(BigInteger.ONE);n.compareTo(i)>=0;i=i.add(BigInteger.ONE))
		{
			prime.add(i);
		}
		if(n.compareTo(last)>=0)
		for(int j=0;j<prime.size();j++) 
		{
		BigInteger kk=BigInteger.ZERO;
			
		
			for(BigInteger k=BigInteger.ONE.add(BigInteger.ONE);(n.compareTo(kk)>0)&&(n.compareTo(k)>0);k=k.add(BigInteger.ONE))
			prime.remove(kk=prime.get(j).multiply(k));	
	}
		return prime.indexOf(n)>=0;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		/*
		BigRational euromillionProba[]= {
				new BigRational(BigInteger.ONE,Ckn(5,50).multiply(Ckn(2,12)))
				,new BigRational(BigInteger.ONE,Ckn(5,50).multiply(Ckn(1,12)))
				,new BigRational(BigInteger.ONE,Ckn(5,50).multiply(Ckn(0,12)))
				,new BigRational(BigInteger.ONE,Ckn(4,50).multiply(Ckn(2,12)))
				,new BigRational(BigInteger.ONE,Ckn(4,50).multiply(Ckn(1,12)))
				,new BigRational(BigInteger.ONE,Ckn(3,50).multiply(Ckn(2,12)))
				,new BigRational(BigInteger.ONE,Ckn(4,50).multiply(Ckn(0,12)))
				,new BigRational(BigInteger.ONE,Ckn(2,50).multiply(Ckn(2,12)))
				,new BigRational(BigInteger.ONE,Ckn(3,50).multiply(Ckn(1,12)))
				,new BigRational(BigInteger.ONE,Ckn(3,50).multiply(Ckn(0,12)))
				,new BigRational(BigInteger.ONE,Ckn(1,50).multiply(Ckn(2,12)))
				,new BigRational(BigInteger.ONE,Ckn(2,50).multiply(Ckn(1,12)))
				,new BigRational(BigInteger.ONE,Ckn(2,50).multiply(Ckn(0,12)))
		}; 
		BigRational gain[]= {
				new BigRational(13983816000L,100)
				,new BigRational(699190800L,100)
				,new BigRational(310751467L,100)
				,new BigRational(62150293L,100)
				,new BigRational(3107515L,100)
				,new BigRational(1412507L,100)
				,new BigRational(1381118L,100)
				,new BigRational(98547L,100)
				,new BigRational(70625L,100)
				,new BigRational(31389L,100)
				,new BigRational(18771L,100)
				,new BigRational(4027L,100)
				,new BigRational(2190L,100)
		};
		

		BigRational gain2[]= {
				new BigRational(190000000L,100)
				,new BigRational(469817180L,100)
				,new BigRational(6982740L,100)
				,new BigRational(325280L,100)
				,new BigRational(21730L,100)
				,new BigRational(11450L,100)
				,new BigRational(7510L,100)
				,new BigRational(1730L,100)
				,new BigRational(1950L,100)
				,new BigRational(1460L,100)
				,new BigRational(910L,100)
				,new BigRational(920L,100)
				,new BigRational(500L,100)
				};
		for(int i=0;i<euromillionProba.length;i++)
		System.out.println(euromillionProba[i].toString());
		for(int i=0;i<gain.length;i++)
			System.out.println(gain2[i].divide(gain[i]).doubleValue() );
		System.out.println(13983816000L/699190800L);
		System.out.println("Ckn(2,12)"+Ckn(2,12));
		System.out.println("Ckn(1,12)"+Ckn(1,12));
				
		System.out.println(Ckn(2,3));
		System.out.println(Ckn(1,3));
	
		Node n=buildTreeOfPossible(12,2,false,false );
		System.out.println("Ckn(12,2)"+n.countEndLeaf()+"\n"+n.toString());
		*/
		
		BigRational rs =new BigRational(0);
		
		
		long m=400;//bit fail
		long k=1;//bit fail on apps
			long n=604*8;
		long N=32*1024*8;
		//for( m=1;m<=30;m++)
		{ rs =new BigRational(0);
	for( k=1;k<=m;k++)
		{
		IBasicOperator r=BigRational.zero;
		
		BigInteger rl = statistic.Ckn(k,m);
		r=r.add(new BigRational(rl));
	//	System.out.println("Ckn(k,m)="+rl+"\n");
		rl = statistic.Ckn(n-k,N-m);
		r=r.multiply(new BigRational(rl));
	//	System.out.println("Ckn(n-k,N-m)="+rl+"\n");
		rl=statistic.Ckn(n,N);
	//	System.out.println("Ckn(n,N)="+rl+"\n");
		r=r.divide(new BigRational(rl));
	//	System.out.println("p(n="+n+",N="+N+",m="+m+",k="+k+")= "+r.doubleValue()*100 +" %");
		rs=rs.add(r);
	}
		System.out.println("Rused(n="+n+",N="+N+",m="+m+",k=1.."+m+")= "+rs.doubleValue()*100 +" %");
	}
	}
	/*
ABCD
1+2+3=6
AB CD
AB AC AD
BC BD
CD*/
}
