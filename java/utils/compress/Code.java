package com.zoubworld.java.utils.compress;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.zoubworld.java.utils.compress.file.IBinaryReader;
import com.zoubworld.java.utils.compress.file.IBinaryWriter;
import com.zoubworld.utils.JavaUtils;

/*
import net.sourceforge.jaad.aac.tools.IS;
*/
public class Code implements ICode {


	static public Code NULL=new Code(0,0);
	/** return the total bit length of the list
	 * */
		static public Long length(List<ICode> lc)
		{ 
			if (lc==null)
			return null;
		return lc.stream().map(c -> (c == null) ? 0 : c.length()).collect(Collectors.summingLong(Integer::intValue));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.Icode#length()
	 */
	@Override
	public int length() {
		return lenbit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (Code.class.isInstance(obj)) {
			Code c = (Code) obj;
			if (c.lenbit != lenbit)
				return false;
			// if(!c.getLong().equals(getLong()))
			for (int i = 0; i < code.length; i++)
				if (code[i] != c.code[i])
					return false;
			return true;
		}
		return super.equals(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int i = lenbit;

		for (char c : code)
			i ^= c;
		return i;
	}

	protected char code[] = null;
	protected int lenbit = 0;
	protected ISymbol sym = null;

	public static List<ISymbol> toSymbol(List<ICode> lc) {
		List<ISymbol> ls = new ArrayList<ISymbol>();
		for (ICode c : lc)
			ls.add(c.getSymbol());
		return ls;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.Icode#getSymbol()
	 */
	@Override
	public ISymbol getSymbol() {
		return sym;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.zoubwolrd.java.utils.compress.Icode#setSymbol(net.zoubwolrd.java.utils.
	 * compress.Symbol)
	 */
	@Override
	public void setSymbol(ISymbol sym) {
		this.sym = sym;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.Icode#toCode()
	 */
	@Override
	public char[] toCode() {
		return code;
	}

	// bit string "010101" for 0x15
	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.Icode#toRaw()
	 */
	@Override
	public String toRaw() {
		String s = "";
		if (code != null)
			for (int i = 0; i < lenbit; i++)

				s += ((code[i / 8] >> (7 - (i % 8))) & 0x1) == 1 ? "1" : "0";

		return s;
	}

	public String toString() {
		String s = "(0x";
		if (length() > 64)
			for (int i = 0; i < code.length; i++)
				s += Integer.toHexString(code[i]);
		else
			s += Long.toHexString(getLong());
		s += " \t," + length();
		s += "),0b";

		s += toRaw() + "\t";
		return s;
	}

	public Code(boolean c[]) {
		 code= new char[c.length/8+1];
		 lenbit=c.length;
		 int j=0;
		 while(j<lenbit)
		 {
			 code[j/8]=0;
			 for(int i=0;(i<8) &&(j<lenbit) ;i++)
		 code[j/8]|=c[j++]?(1<<i):0;
		 }
		 
	}

	public Code(BigInteger i) {
		//lenbit = i.bitLength()+i.signum()<0?1:0;
		lenbit = i.toByteArray().length*8;
		
			
		code = new char[(lenbit-7)/8+1];
		int index=0;
		for(byte b:i.toByteArray())
		code[index++] = (char)b;
		

	}

	/**
	 * define a codee a a string of 0/1 : Code("00001101")=Code( (byte)0x0d )
	 */
	public Code(String s) {
		if (s.replaceAll("1", "").replaceAll("0", "").length() != 0)
			System.exit(-1);
		code = new char[(s.length() + 7) / 8];
		// int i=0;
		for (char c : s.toCharArray()) {
			huffmanAddBit(c);
			// i++;
		}

		lenbit = s.length();
	}

	public Code(byte c) {
		code = new char[1];
		code[0] = (char) c;
		lenbit = 8;
	}

	public Code(short s) {
		code = new char[2];
		code[0] = (char) (s >> 8);
		code[1] = (char) (s & 0xff);
		lenbit = 16;
	}/*
	public Code(char s) {
		code = new char[2];
		code[0] = (char) (s >> 8);
		code[1] = (char) (s & 0xff);
		lenbit = 16;
	}*/
	
	public Code(char c) {
		 code= new char[1];
		 code[0]=c;
		 lenbit=8;
	}

	public Code(int s) {
		code = new char[4];
		for (int i = 0; i < 4; i++)
			code[i] = (char) ((s >> (8 * (3 - i))) & 0xff);
		lenbit = 32;

	}

	public static Code Factory(long s, int len, boolean BigEndian) {
		if (BigEndian)
			return new Code(s, len);
		if (len % 8 == 0) {
			long sr = 0;
			for (int i = 0; i < len; i += 8) {
				long tmp = ((s >> i) & 0xff);
				tmp = tmp << (len - i - 8);
				sr |= tmp;
			}
			return new Code(sr, len);

		}
		return null;
	}

	public Code(long s) {
		code = new char[8];
		for (int i = 0; i < 8; i++)
			code[i] = (char) ((s >> (8 * (7 - i))) & 0xff);
		lenbit = 64;
	}

	public static int readCode255(IBinaryReader bin) {
		int i = (int) bin.readLong(8, false);
		int s = 0;
		while (i == 255) {
			s += i;
			i = (int) bin.readLong(8, false);
		}
		s += i;
		return s;
	}

	public static Code FactoryCode255(int s) 
	 {
		final int rl=8;
		final long rc=(1L<<rl)-1L;		
		int len = rl;
		long x = 0;
		while (s >= rc) {
			len += rl;
			x = x | rc;
			x = x << rl ;
			s -= rc;
		}
		x = (x ) | s;
		//len += rl;
		return new Code(x, len);
	}
	/** build code for number : n
	 * the code is : "11"x((int)n/3)+(n%3)
	 * 00 : 0
	 * 01 : 1
	 * 10 : 2
	 * 1100 : 3
	 * 1101 : 4
	 * 1110 : 5
	 * 111100 : 6
	 * ...
	 * */
	public static Code FactoryCode3(int s) {
		final int rl=2;
		final long rc=(1L<<rl)-1L;		
		int len = rl;
		long x = 0;
		while (s >= rc) {
			len += rl;
			x = x | rc;
			x = x << rl ;
			s -= rc;
		}
		x = (x ) | s;
		//len += rl;
		return new Code(x, len);
	}
	/** Unary code
The simplest integer code is probably the unary code, which maps each positive integer n E N to a sequence of n zeros, 
followed by a one:
Integer  Code word  Implied probability
0           1            2^-1
1           01           2^-2
2           001          2^-3
3           0001         2^-4
4           00001        2^-5
n           0...n..01    2^(n+1)

It is easy to verify that this code satisfies the criteria from above: it has a unique mapping, its code words are 
self-delimiting, and it generates the entirespace of possible binary sequences.One can interpret the unary code as
 a series of binary questions �Is itk?� starting from k= 0 and incrementing k after each question. When the answer to 
 a question is no, a0is written;when the answer is yes, a1is written and the process terminates.
 
 
The unary code usesn+1 bits to encode any integern. This code is optimal for the distributionthat assigns to each integernthe probability mass of 2^(-n-1), 
i.e. a geometric distribution withsuccess parameter theta=1/2.

see also FactoryUnaryCode1, for convention with 111110 instead of 000001
*/
	public static Code FactoryUnaryCode(int s) {
		return new Code(StringUtils.repeat("0", s) + "1");
	}
	/** Unary coding
	 * https://fr.wikipedia.org/wiki/Codage_unaire
	 * https://en.wikipedia.org/wiki/Unary_coding
	 * */
	public static Code FactoryUnaryCode1(int s) {
		return new Code(StringUtils.repeat("1", s) + "0");
	}
	
	/** Elias gamma code
	 * https://en.wikipedia.org/wiki/Elias_gamma_coding
The perhaps simplest non-unary integer code is theF-code by Elias (1975). This code assignscode words to natural numbers greater than zero,n?{1,2,3, . . .}.
The code word for integernis formed by writingnin binary notation (without the leading1),prefixed by its string length written in unary, using the unary code 
from the previous section.This solves the termination problem, as the unary number canbe decoded first � this way,the decoder knows how many binary digits to 
read.For small integers, the EliasF-code generates the following code words:
Integer  Code word    Implied probability
1					  1	   								2^-1
2					 010									2^-3
3					 011									2^-3
4					00100									2^-5
5					00101									2^-5
6					00110									2^-5
7					00111									2^-5
8        0001000                2^-7
n        log2(n),n              2^(-2*log2(n)-1)

This method encodes each integer using 2?log2n?+ 1 bits. To encode zero, the code can beshifted down by 1. To encode all integersi?Z, a suitable bijection can
 be used; for example,i=?n2?�(-1)n mod 2 maps natural numbers (1,2,3,4,5, ...) onto integers (0,1,-1,2,-3, ...).The basic idea behind this coding technique is to
  augment thenatural binary representation of an integer with a length indicator, making the resulting code words uniquely decodable.The same basic construction 
  is used by many other integer codes, some of which are reviewedin the remainder of this section
	 * 
	 * */
	public static Code FactoryEliasGammaCode(long s) {
		int n=(int) (Math.log(s)/Math.log(2));
		long d=(long) (s-Math.pow(2, n));
		Code c = new Code((long) d,n);
		Code u = FactoryUnaryCode(n);
		return merge(u,c);
	}
	/**   
  
  Exponential Golomb codes
  As an example of integer codes which are widely used in practice, I will describe the family of exponential Golomb codes. This family is really just one code,
   parametrised by an integer k that determines the distribution of code word lengths. In particular, the k th code distributes the probability mass such that the 
   first 2kintegers have equal probability, and a joint mass of exactly12. An exponential Golomb code with parameter k encodes a natural number n as follows:
   1.    Compute m=(log2(n+ 2^k))-k, and encode m using the unary code 
   2. Write the binary representation of n+ 2^k, omitting the leading1. 
   This representation uses exactly k+m bits.The number m represents the number of additional binary digits needed to encode n, having made use of k (free digits). 
   So in total, an exponential Golomb code represents any positive integer n with exactly  (log2(n+ 2k))+ 1 bits.  Table 2.1 shows some of the code words generated
    by exponential Golomb codes with different settings of k.
    
    For example, in the case of k= 3, no additional digits are needed to represent the numbers 0 to 7 in
     binary, because the k= 3 free digits provide enough space. This means thatm= 0for these numbers, and the unary encoding of m= 0 is the single digit prefix1.
      Numbers 8to 15 require an additional 4th digit, som= 1, giving unary prefix01. This coding method can be used for signed integers, too, usually by alternating 
      the sign with the least significant digit of the code word, as shown in Table 
      
      https://en.wikipedia.org/wiki/Exponential-Golomb_coding
	 * */
	public static Code FactoryExponentialGolombCode(int k, int n) {
		int m=(int) (Math.log(n+Math.pow(2, k))/Math.log(2))-k;
		int d=0;
	/*	if (k==0)
			d=(int) (Math.pow(2, (m))-1);
		else if (k==1)
			d=(int) (Math.pow(2, (m+k))-2);
		else*/
			d=(int) (Math.pow(2, (m+k))-Math.pow(2,k));
		d=n-d;
		Code c = new Code((long) (d),(m+k));
		Code u = FactoryUnaryCode(m);
		
		return merge(u,c);
	}
	/** un ExponentialGolomb subversion, for j=1, it is like FactoryExponentialGolombCode(k,n)
	 * 
	 * n-> Unary(m)+ n[0..m*j+k], where m=(log(n)/log(2))/j
	 * */
	public static ICode FactoryExponentialGolombCode(int k,int j, int n) {
	
		/*int m=(int) (Math.log(n+Math.pow(2, k))/Math.log(2))-k;
		
		m=(m+j-1)/j;
		
		int m2=m*j;
		int d=0;
		
		
		
		int n2=n;
		m=0;
		while(n2>=1<<((m+1)*4+k))
			n2-=1<<((m+++1)*4+k);
		 m2=(m+1)*4;

			d=(int) (Math.pow(2, (m2+k))-Math.pow(2,k));
		d=n-d;
		Code c = new Code((long) (d),(m2+k));
		Code u = FactoryUnaryCode(m);
		
		return merge(u,c);*/
		return CodeNumber.getExpGolombkCode(k, n);
	}
	/** code a number n with FactoryCode3(l) and the bits stream of n on a length (l+1)*4
	 * where (l+1)*4=log2(n);
	 * 0 =000000
	 * 15=001111
	 * 16+255=01 11111111
	 * 
	 * */
	public static Code FactoryCodeN3k(int s) {
		int s2=s;
		int l=0;
		while(s2>=1<<((l+1)*4))
			s2-=1<<((l+++1)*4);
		int len=(l+1)*4;
		
		Code cl = FactoryCode3( l);//=(i+1)*4
		
		Code cd=new Code(s2,len);
				
		return merge(cl,cd);
	}
	/** merge 2 bit code 
	 * */
	public static Code merge(Code cl, Code cd) {
		long l=(cl.getLong().longValue()<<cd.length())+cd.getLong();
		return new Code(l,cd.length()+cl.length());
	}
	
	
	private static List<Long> fib=null;
	/** speed optimized Fibonacci 
	 * */
	private static long Fib(int n)
	{
		if (fib==null)
		fib=new ArrayList<Long>();
		int s=fib.size();
		if(s<=n)
		for(int i=s;i<=n;i++)
		{
			if(i<=2)
			{
			if (i<=0)
				fib.add( 0L);
			else
			fib.add( 1L);
			}
			else
			fib.add(Fib(i-2)+Fib(i-1))	;
		}
		return fib.get(n).longValue();
	}
	/** basic Fibonacci
	 * 
	private static long Fibonacci(long n)
	{
		if(n<=2)
		{
		if (n<=0)
			return 0;
		return 1;
		}
		return  Fibonacci( n-1)+ Fibonacci( n-2);
	}*/
	/** n>0
	 * https://en.wikipedia.org/wiki/Fibonacci_coding
	 * */
	public static Code FactoryFibonacciCode(long n) {
		int findN=2;
		Code r=new Code(0,0);
		while(n>Fib(findN))
		{
			findN++;
		}
		while(n>0)
		{
			if (Fib(findN)>n)
				findN--;
			else
				{
				n-=Fib(findN);
				findN--;
				r = ORL(r,FactoryUnaryCode(findN-1));
				}
		}
		r.huffmanAddBit('1');
		return r;
	//	return null;
	}
	/** OR with left alignment 2 codes A & C and return r.
	 * A=0baaaa
	 * C=0bcccccc
	 * r=0b(a|c)(a|c)(a|c)(a|c)cc
	 * example ORL(0b0011,0bb100011)=0b101111
	 * */
	public static Code ORL(Code a, Code c) {
		String OR="";
	 if (a.length()>c.length())
	 {//swap
		 Code b=a;
		 a=c;c=b;
	 }
	 OR=c.toRaw();
	 char[] ORb = OR.toCharArray();
	 String as = a.toRaw();
	 for(int i=0;i<as.length();i++)
		 if(as.charAt(i)=='1')
			 ORb[i]='1'; 
		OR=String.valueOf(ORb); 
		return new Code(OR);
	}

	public Code(ICode a,ICode b)
	{
		Code c=new Code(a.toRaw()+b.toRaw());
		code=c.code;
		lenbit=c.lenbit;
		sym=new CompositeSymbol(a.getSymbol(), b.getSymbol());
	}

	
	/**
	 * https://en.wikipedia.org/wiki/Levenshtein_coding
	 * */
	public static Code FactoryLevenshteinCode(long N) {
	if(N==0)
		return new Code(0,1);
	  int c = 0;
      String bits="";
      do {
          int m = 0;
          for (long temp = N; temp > 1; temp>>=1)  // calculate floor(log2(num))
              ++m;
          for (int i=0; i < m; ++i)
              bits=((((N >> i) & 1)==0)?'0':'1')+bits;
          N = m;
          ++c;
      } while (N > 0);
      String bitwriter="";
      for (int i=0; i < c; ++i)
          bitwriter+='1';
      bitwriter+='0';
      //while (bits.length() > 0)
          bitwriter+=bits;
	return new Code(bitwriter);
	}	
	/**
	 * https://en.wikipedia.org/wiki/Golomb_coding
	 * */
	public static Code FactoryGolombCode(int k,long N) {
		assert N>=0;
		assert k>0;

		 
	int d=(int)N/k;
	int r=(int) (N%k);
	assert d*k+r==N;
	int l=(int) (Math.log(k)/Math.log(2));
	assert r<Math.pow(2, l);
	assert k<=Math.pow(2, l);
	
	Code a=Code.FactoryUnaryCode1(d);
			Code b=new Code(r,l);
	return merge(a,b);		
	}
	
	/** Elias omega coding
	 * https://en.wikipedia.org/wiki/Elias_omega_coding
	 * */
	public static Code FactoryEliasOmegaCode(long N) {
		assert N>0;
	 String as="";
		Code a;
		while (N > 1) {
            int len = 0;
            for (long temp = N; temp > 0; temp >>= 1)  // calculate 1+floor(log2(num))
                len++;
            for (int i = 0; i < len; i++)
                as=((((N >> i) & 1)==0)?'0':'1')+as;
            N = len - 1;
        }
		a=new Code(as);
		Code z=new Code(0,1);
		return merge(a,z);	
	}
	/** Elias delta coding
	 * https://en.wikipedia.org/wiki/Elias_delta_coding
	 * 
	  To decode an Elias delta-coded integer:

    Read and count zeros from the stream until you reach the first one. Call this count of zeros L.
    Considering the one that was reached to be the first digit of an integer, with a value of 2L, read the remaining L digits of the integer. Call this integer N+1, and subtract one to get N.
    Put a one in the first place of our final output, representing the value 2N.
    Read and append the following N digits.
	 * */
	public static Code FactoryEliasDeltaCode(long X) {
	assert X>0;
		int N=(int) (Math.log(X)/Math.log(2));
		assert Math.pow(2, N)<=X;
		assert Math.pow(2, N+1)>X;
		/*int L=(int) (Math.log(N+1)/Math.log(2));
		assert Math.pow(2, L)<=N+1;
		assert Math.pow(2, L+1)>N+1;
		String s=StringUtils.repeat("0", L);*/
		Code a=FactoryEliasGammaCode(N+1);
		Code b=new Code((long)(X-Math.pow(2, N)),N);
		return merge(a,b);
		//	return null;
	}
	/**
	 * https://en.wikipedia.org/wiki/Variable-length_quantity
	 * */
	public static Code FactoryVLQZigzagCode(long N) {	
		return FactoryVariableLengthQuantityUnsignedCode((N<<1) ^ N>>63);
	}
	/**
	 * https://en.wikipedia.org/wiki/Variable-length_quantity
	 * */
	public static Code FactoryVariableLengthQuantitySignedCode(long N) {
		/* int len = 0;
         for (long temp = N; temp > 0; temp >>= 1)  // calculate 1+floor(log2(num))
             len++;*/
		String sig="0";
		if (N<0)
			{N=-N;sig="1";}
         if (N==0)
        	 return new Code(0,8);
         int l=6;
         String s="";
         String i="0";
         while(N>0)
         {
        	 String t=Long.toString((N & ((1<<(l+1))-1) ),2).replaceAll(" ", "0");
        	 t=StringUtils.repeat("0", l-t.length())+t;
        	 if(l==6)
        		 t=sig+t;
        	 s=i +t+s;
        	 N=N>>>l;
        	l=7;
         i="1";
        	 
         }
         return new Code(s);
	}
	/**
	 * https://en.wikipedia.org/wiki/Variable-length_quantity
	 * */
	public static Code FactoryVariableLengthQuantityUnsignedCode(long N) {
	assert N>=0;
		/* int len = 0;
         for (long temp = N; temp > 0; temp >>= 1)  // calculate 1+floor(log2(num))
             len++;*/
         if (N==0)
        	 return new Code(0,8);
         int l=7;
         String s="";
         String i="0";
         while(N>0)
         {
        	 String t=Long.toString((N & 0x7fL),2).replaceAll(" ", "0");
        	 t=StringUtils.repeat("0", 7-t.length())+t;
        	 s=i +t+s;
        	 N=N>>>7;
        	
         i="1";
        	 
         }
         return new Code(s);
	}
	/**
	 * https://en.wikipedia.org/wiki/Golomb_coding
	 * https://unix4lyfe.org/rice-coding/
	 * */
	public static Code FactoryRiceCode(int k,long N) {		
		return FactoryGolombCode((int) Math.pow(2, k) , N);
	}
	/**
	 * https://en.wikipedia.org/wiki/Even%E2%80%93Rodeh_coding
	 * 
	 * */
	public static Code FactoryEvenRodehCode(long N) {
		assert N>=0;
		String s="";
		//1
		if(N>=4)
			s="0";
		//2
		do
		{
		if (N<8)
			{
				String t=Long.toString((N & 0x7L),2).replaceAll(" ", "0");
       	 		t=StringUtils.repeat("0", 3-t.length())+t;
       	 		s=t+s;
       	 		return new Code(s);
			}
		else
		{
			//3
			N=N-7;
			//4
			N=N;
		}
		}//5
		while((N>=8));
		return null;
	}
	
	/**
	 * https://en.wikipedia.org/wiki/Shannon%E2%80%93Fano%E2%80%93Elias_coding
	 * */
	public static Code FactoryShannonFanoEliasCode(Map<ISymbol,Long> freq,ISymbol sym) {
//freq=JavaUtils.SortMapByKey(freq);
Map<ISymbol,Long> freqo = new TreeMap<>(
		new Comparator<ISymbol>() {

    public int compare(ISymbol o1, ISymbol o2) {
        return (int) (o1.getId()-(o2.getId()));
    }

});

freqo.putAll(freq);
double total=0;
for(ISymbol s:freqo.keySet())
total+=freqo.get(s);
int L=(int) Math.ceil(Math.log(total/freqo.get(sym))/Math.log(2))+1;
double d=0.0;


for(ISymbol s:freqo.keySet())
	if (s!=sym)
d+=freqo.get(s)/total;
	else
	{
		d+=freqo.get(s)/2.0/total;
		d=d*(1<<L);
		long l= (long) (d+1.0/(1<<L));
		
	//	l=l& ((1<<(L+1))-1);
		return new Code(l,L);
	}
return null;
		}
	
	/** funny example of morse coe, but not usefull
	 * '.' is code 0 and '-' is code 1
	 * https://fr.wikipedia.org/wiki/Code_Morse_international
	 * */
	public static Code FactoryCodeMorse(char c) {
		switch(c) {
		case 'A': return new Code(0b01,2);
		case 'B': return new Code(0b1000,4);
		case 'C': return new Code(0b1010,4);
		case 'D': return new Code(0b100,3);
		case 'E': return new Code(0b0,1);
		case 'F': return new Code(0b0010,4);
		case 'G': return new Code(0b110,3);
		case 'H': return new Code(0b0000,4);
		case 'I': return new Code(0b00,3);
		case 'J': return new Code(0b0111,4);
		case 'K': return new Code(0b101,3);
		case 'L': return new Code(0b0100,4);
		case 'M': return new Code(0b11,2);
		case 'N': return new Code(0b110,3);
		case 'O': return new Code(0b111,3);
		case 'P': return new Code(0b0110,4);
		case 'Q': return new Code(0b1101,4);
		case 'R': return new Code(0b010,3);
		case 'S': return new Code(0b000,3);
		case 'T': return new Code(0b1,1);
		case 'U': return new Code(0b001,3);
		case 'V': return new Code(0b0001,4);
		case 'W': return new Code(0b011,3);
		case 'X': return new Code(0b1001,4);
		case 'Y': return new Code(0b1011,4);
		case 'Z': return new Code(0b1100,4);
		case '1': return new Code(0b01111,5);
		case '2': return new Code(0b00111,5);
		case '3': return new Code(0b00011,5);
		case '4': return new Code(0b00001,5);
		case '5': return new Code(0b00000,5);
		case '6': return new Code(0b10000,5);
		case '7': return new Code(0b11000,5);
		case '8': return new Code(0b11100,5);
		case '9': return new Code(0b11110,5);
		case '0': return new Code(0b11111,5);
		case '.': return new Code(0b010101,6);
		case ',': return new Code(0b110011,6);
		case '?': return new Code(0b001100,6);
		case '\'': return new Code(0b011110,6);
		case '!': return new Code(0b011110,6);
		case '/': return new Code(0b101011,6);
		case '(': return new Code(0b10110,5);
		case ')': return new Code(0b101101,6);
		case '&': return new Code(0b01000,5);
		case ':': return new Code(0b111000,6);
		case ';': return new Code(0b101010,6);
		case '=': return new Code(0b10001,5);
		case '+': return new Code(0b01010,5);
		case '-': return new Code(0b100001,6);
		case '_': return new Code(0b001101,6);
		case '"': return new Code(0b010010,6);
		case '$': return new Code(0b0001001,7);
		case '@': return new Code(0b011010,6);
		case '�': return new Code(0b0101,4);
		case '�': return new Code(0b01101,5);
		case '�': return new Code(0b10100,5);
		case '\00': return new Code(0b1111,4);
		case '\01': return new Code(0b00110,5);
		case '�': return new Code(0b01001,5);
		case '�': return new Code(0b00100,5);
		case '\02': return new Code(0b11010,5);
		case '\03': return new Code(0b1111,4);
		case '\04': return new Code(0b01110,5);
		case '\05': return new Code(0b11011,5);
		case '�': return new Code(0b1110,4);
		case '\06': return new Code(0b00010,5);
		case '\07': return new Code(0b01100,5);
		case '�': return new Code(0b0011,4);
		case '\10': return new Code(0b011010,6);
		  default:
		    // code block
		}

		return null;
		
		
	}
	public Code(long s, int len) {

		if (len % 8 == 0) {
			code = new char[(len - 1) / 8 + 1];
			for (int i = 0; i < code.length; i++)
				code[i] = (char) ((s >> (8 * (code.length - 1 - i))) & 0xff);
			lenbit = len;
		} else
			for (int i = len - 1; i >= 0; i--)
				huffmanAddBit((((s >> i) & 0x1) == 0) ? '0' : '1');

	}

	/**
	 * return log2(N) just upper where N is the number of different symbol.
	 */
	public static int getDefaultLength() {

		int R = Symbol.getNbSymbol();
		int Nb = (int) (Math.log(R) / Math.log(2) + 1);

		return Nb;
	}

	/**
	 * convert symbol id on code of length len bits.
	 */
	public static void reworkCode(List<ISymbol> ls, int len) {
		for (ISymbol s : ls)
			if (s.getCode().length() != len)
				s.setCode(new Code(s.getId(), len));

	}

	/**
	 * remove zero at beginning
	 */
	public void trim() {
		int o = 0;
		for (int i = 0; (i < lenbit) && (((code[i / 8] >> (7 - (i % 8))) & 0x1) == 0); i++)
			o++;
		while (o >= 8) {
			for (int i = 0; i < lenbit / 8 - 1; i++)
				code[i] = code[i + 1];
			o -= 8;
			lenbit -= 8;
		}
		while (o > 0) {
			int i;
			for (i = 1; i * 8 < lenbit; i++)
				code[i - 1] = (char) ((code[i - 1] << 1) + ((code[i] >> 7) & 0x1));
			code[(lenbit - 1) / 8] = (char) ((code[(lenbit - 1) / 8] << 1) & 0xfe);
			o -= 1;
			lenbit -= 1;
		}

	}

	public Code() {
		// TODO Auto-generated constructor stub
	}

	// bit order
	// 76543210,FEDCBA98
	// code[0] code[1]
	// 0x1 =00000001
	// 0x10 =00010000
	// 0x100=00000000 00000001
	// 0x001=00000001 00000000
	// (011).huffmanAddBit(1)=(0111) code[0]=0b0110xxxx.

	public void huffmanAddBit(char code2) {
		if (code == null) {
			code = new char[1];
			lenbit = 0;
		}
		if (lenbit % 8 == 0) {
			char code3[] = new char[(lenbit / 8) + 1];
			if (lenbit != 0)
				for (int i = 0; i < lenbit / 8; i++)
					code3[i] = code[i];

			code3[(lenbit + 1) / 8] = 0;
			code = code3;
		}
		if (code2 == '1')
			code[lenbit / 8] = (char) ((code[lenbit / 8]) + (char) (1 << (7 - lenbit % 8)));
		// else
		// code[lenbit / 8] = (char) ((code[lenbit / 8]));
		lenbit += 1;

	}

	public static void main(String[] args) {
		/*
		 * {
		 * 
		 * Code c = new Code(); System.out.println(c.toString()); c.huffmanAddBit('0');
		 * System.out.println(c.toString()); c.huffmanAddBit('1');
		 * System.out.println(c.toString()); c.huffmanAddBit('0');
		 * System.out.println(c.toString()); c.huffmanAddBit('1');
		 * System.out.println(c.toString()); c.huffmanAddBit('0');
		 * System.out.println(c.toString()); c.huffmanAddBit('1');
		 * System.out.println(c.toString()); c.huffmanAddBit('1');
		 * System.out.println(c.toString()); c.huffmanAddBit('1');
		 * System.out.println(c.toString());
		 * 
		 * 
		 * ICode a = new Code((char) 1);
		 * System.out.println("(char) 1 : "+a.toString()+": "+a.toRaw()); ICode b = new
		 * Code((short) 2);
		 * System.out.println("(short) 2 : "+b.toString()+": "+b.toRaw()); ICode d = new
		 * Code((int) 4); System.out.println("(int) 4 : "+d.toString()+": "+c.toRaw());
		 * c = new Code((long) 0x123456789L);
		 * System.out.println("(long) 0x123456789 : "+c.toString() +": "+c.toRaw());
		 * System.out.print("0b"); for(int i=c.lenbit-1;i>=0;i--)
		 * System.out.print(c.getMsb(i)); System.out.println(); }
		 * 
		 * { Code a = new Code(0x80);
		 * System.out.println("() 0x80 : \t"+a.toString()+"\t: "+a.toRaw()); Code b =
		 * new Code(0x8F);
		 * System.out.println("() 0x8F : \t"+b.toString()+"\t: "+b.toRaw()); Code c =
		 * new Code(0x1007F); System.out.println("() 0x1007F : \t"+c.toString()
		 * +"\t: "+c.toRaw()); Code d = new Code((short)0x1);
		 * System.out.println("() 0x1 : \t"+d.toString() +"\t: "+d.toRaw());
		 * 
		 * System.out.println(a.toString()+".compareToCode("+b.toString()+")="+a.
		 * compareToCode(b));
		 * System.out.println(b.toString()+".compareToCode("+c.toString()+")="+b.
		 * compareToCode(c));
		 * System.out.println(c.toString()+".compareToCode("+a.toString()+")="+c.
		 * compareToCode(a));
		 * System.out.println(c.toString()+".compareToCode("+d.toString()+")="+c.
		 * compareToCode(d)); } {// test sort List<Code> lc=new ArrayList<Code>(); for
		 * (int i=1; i<25;i+=5) lc.add(new Code((char)i)); for (int i=1; i<25;i+=5)
		 * lc.add(new Code((short)(i))); for (int i=1; i<25;i+=5) lc.add(new
		 * Code((short)(i*256))); for (int i=1; i<30;i+=5) lc.add(new
		 * Code((short)(i*256+i)));
		 * 
		 * Collections.sort(lc, new CodeComparator());
		 * System.out.println("\nCodeComparator\n========="); for(ICode n:lc) {
		 * System.out.println(n.toString() + "\t=\t" +n.toString() );
		 * 
		 * }
		 * 
		 * Collections.sort(lc, new CodeComparatorInteger());
		 * System.out.println("\nCodeComparatorInteger\n========="); for(ICode n:lc) {
		 * System.out.println(n.toString() + "\t=\t" +n.toString() );
		 * 
		 * }
		 * 
		 * }
		 * 
		 * { Code a = new Code();
		 * a.huffmanAddBit('0');a.huffmanAddBit('0');a.huffmanAddBit('0');//char(' ')=
		 * 0b000 : System.out.println("(char) 1 : "+a.toString()+": "+a.toRaw()); Code b
		 * = new Code();
		 * b.huffmanAddBit('1');b.huffmanAddBit('0');b.huffmanAddBit('1');b.
		 * huffmanAddBit('0');b.huffmanAddBit('0');//char('r')= 0b10100
		 * System.out.println("(short) 2 : "+b.toString()+": "+b.toRaw()); Code c = new
		 * Code(); c.huffmanAddBit('0');c.huffmanAddBit('1');c.huffmanAddBit('1');c.
		 * huffmanAddBit('0');c.huffmanAddBit('0');//char('i')= 0b01100
		 * System.out.println("(long) 0x123456789 : "+c.toString() +": "+c.toRaw());
		 * 
		 * System.out.println("a.compareToCode(b)="+a.compareToCode(b));
		 * System.out.println("b.compareToCode(c)="+b.compareToCode(c));
		 * System.out.println("c.compareToCode(a)="+c.compareToCode(a));
		 * 
		 * 
		 * 
		 * 
		 * 
		 * 
		 * }
		 * 
		 * 
		 */

	}

	/**
	 * integer compare 0b1 bigger than 0b01 0b1 smaller than 0b10 0b011 bigger then
	 * 0b10 0b11 smaller then 0b100
	 * 
	 */
	public int compareToInt(ICode s2) {

		int MSBindex = Math.max(s2.length(), lenbit);
		for (int i = MSBindex - 1; i >= 0; i--) {
			Integer msb1 = getMsb(i);
			Integer msb2 = s2.getMsb(i);
			if (msb1 == null)
				msb1 = 0;
			if (msb2 == null)
				msb2 = 0;
			if (msb1 - msb2 != 0)
				return (msb1 - msb2);
		}

		if (lenbit != s2.length())
			return -(lenbit - s2.length());
		return 0;

	}

	/**
	 * TBD
	 */
	public int compareToCode2(ICode s2) {
		for (int i = 0; i < lenbit; i++) {
			if (i == s2.length())// (lenbit>s2.lenbit)
				return -1;
			if (code[i / 8] != s2.toCode()[i / 8])
				if (((code[i / 8] >> (7 - (i % 8))) - (s2.toCode()[i / 8] >> (7 - (i % 8)))) != 0)
					return -((code[i / 8] >> (7 - (i % 8))) - (s2.toCode()[i / 8] >> (7 - (i % 8))));
		}
		if (lenbit < s2.length())
			return 1;
		return 0;

	}

	public List<Code> ByteArrayToListCode(byte[] datas) {
		List<Code> l = new ArrayList<Code>();
		for (byte c : datas)
			l.add(Code.find(c));
		return l;
	}

	static Code tabByte[] = new Code[256];;

	private static Code find(byte c) {

		if (tabByte[c] == null)
			tabByte[c] = new Code(c);
		return tabByte[c];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.Icode#getMsb(int)
	 */
	@Override
	public Integer getMsb(int index) {
		if (index >= lenbit)
			return null;
		return (code[(lenbit - 1 - index) / 8] >> (7 - (index % 8)) & 0x1);
	}

	/**
	 * gray compare 0b1 smaller than 0b10 0b011 smaller then 0b1
	 */
	public int compareToCode(ICode s2) {
		for (int i = 0; i < lenbit; i++) {
			if (s2.getMsb(i) == null)
				return 1;
			int x = (getMsb(i) - s2.getMsb(i));
			if (x != 0)
				return x;
		}
		if (s2.length() > lenbit)
			return -1;
		return 0;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.Icode#write(net.zoubwolrd.java.utils.
	 * compress.BinaryStdOut)
	 */
	@Override
	public void write(IBinaryWriter o) {
		int i = 7;
		for (; i < lenbit; i += 8)
			o.write((byte) code[i / 8]);
		int mod = lenbit % 8;
		if (mod != 0)

			o.write((byte) code[lenbit / 8] >> (8 - mod), mod);
	}

public static String toRaw(List<ICode> lc) {
	StringBuffer ss=new StringBuffer ();
	for(ICode c:lc)
		ss.append(c.toRaw());
	return ss.toString();
}


	@Override
	public void write(FileOutputStream out) throws IOException {

		if (lenbit < 64) {
			long d = getLong().longValue();
			out.write((int) ((d >> 0) & 0xff));
			out.write((int) ((d >> 8) & 0xff));
			out.write((int) ((d >> 16) & 0xff));
			out.write((int) ((d >> 24) & 0xff));
			out.write((int) ((d >> 32) & 0xff));
			out.write((int) ((d >> 40) & 0xff));
			out.write((int) ((d >> 48) & 0xff));
			out.write((int) ((d >> 56) & 0xff));

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.zoubwolrd.java.utils.compress.Icode#getLong()
	 */
	@Override
	public Long getLong() {
		long tmp = 0;
		if (lenbit > 64)
			throw new IllegalArgumentException("bit stream to big for getLong = " + this.toString());
		for (int i = 0; i < lenbit; i += 8) {
			long c = (long) (this.code[i / 8] & 0xff);// 1234567890123L;//0x11F71FB04CB
			tmp = (tmp << 8L);
			tmp += (long) (c);
		}
		if (lenbit % 8 != 0)
			tmp = tmp >> (Math.abs(8 - lenbit % 8));
		return tmp;
	}

}
