/**
 * 
 */
package com.zoubworld.java.math;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author pierre valleau
 *
 */

public class statistique {
	static public Node buildTreeOfPossible(int sizeOfSet,int numberOfElement,boolean reput,boolean ordered )
	{
		Set<Integer> set=new HashSet();
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
		Set<Integer> set2=new HashSet(set);
		
		for(Integer I:set)
		{
			Node e=new Node(I);
			if(ordered)
			{
				set2=new HashSet(set);
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
	static public BigInteger factoriel(BigInteger N)
	{
		BigInteger b=BigInteger.ONE;
		 for(long i=1;i<=N.longValueExact();i++){    
		      b=b.multiply(BigInteger.valueOf(i));    
		  }    
		return b;
	}
	
	/** arrangemeent sans remise
	 * */
	static public BigInteger Akn(BigInteger k,BigInteger n)
	{
		BigInteger b=factoriel(n);
		BigInteger c=factoriel(n.subtract(k));		
		return b.divide(c);
	}
	static public BigInteger Ckn(long k,long n)
	{
		return Ckn(BigInteger.valueOf( k),BigInteger.valueOf(n));
	}
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
	public statistique() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
		
	}
	/*
ABCD
1+2+3=6
AB CD
AB AC AD
BC BD
CD*/
}
