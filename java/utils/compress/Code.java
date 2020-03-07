package com.zoubworld.java.utils.compress;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.zoubworld.java.utils.compress.file.IBinaryReader;
import com.zoubworld.java.utils.compress.file.IBinaryWriter;
/*
import net.sourceforge.jaad.aac.tools.IS;
*/
public class Code implements ICode {

	/** return the total bit length of the list
	 * */
		static public Long length(List<ICode> lc)
		{ 
			if (lc==null)
			return null;
		return lc.stream().map(c->(c==null)?0:c.length()).collect(Collectors.summingLong(Integer::intValue));
	}
	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.Icode#length()
	 */
	@Override
	public int length()
	{ 
		return lenbit;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(Code.class.isInstance(obj))
		{
			Code c=(Code)obj;
			if (c.lenbit!=lenbit)
				return false;
			if(!c.getLong().equals(getLong()))
			return false;
			else
				return true;
		}
		return super.equals(obj);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int i=lenbit;
		
		for(char c:code)
		i^=c;
		return i;
	}
	protected char code[] = null;
	protected int lenbit = 0;
	protected ISymbol sym=null;
	public static List<ISymbol> toSymbol(List<ICode> lc)
	{
		List<ISymbol> ls= new ArrayList<ISymbol>();
		for(ICode c:lc)
			ls.add(c.getSymbol());
		return ls;
	}
	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.Icode#getSymbol()
	 */
	@Override
	public ISymbol getSymbol() {
		return sym;
	}
	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.Icode#setSymbol(net.zoubwolrd.java.utils.compress.Symbol)
	 */
	@Override
	public void setSymbol(ISymbol sym) {
		this.sym = sym;
	}
	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.Icode#toCode()
	 */
	@Override
	public char[] toCode() {
		return code;
	}
	// bit string "010101" for 0x15
	/* (non-Javadoc)
	 * @see net.zoubwolrd.java.utils.compress.Icode#toRaw()
	 */
	@Override
	public String toRaw() {
		String s = "";
		if (code!=null)
			for (int i = 0 ; i <lenbit; i++)
				
					s += ((code[i / 8] >> (7-(i % 8))) & 0x1) == 1 ? "1" : "0";
				
		return s;
	}
	



	public String toString() {
		String s="(0x";
				if(length()>64)
				for (int i = 0 ; i <code.length; i++)	
					s +=Integer.toHexString(code[i]);
				else
					s +=Long.toHexString(getLong());
				s+=" \t,"+length();
		s+= "),0b";
	
		s += toRaw() +"\t";
		return s;
	}

	public Code(char c) {
		 code= new char[1];
		 code[0]=c;
		 lenbit=8;
	}
	/** define a codee a a string of 0/1 : Code("00001101")=Code( (byte)0x0d )
	 * */
	public Code(String s) {
		if (s.replaceAll("1", "").replaceAll("0", "").length()!=0)
			System.exit(-1);
		 code= new char[(s.length()+7)/8];
	//	 int i=0;
		 for(char c:s.toCharArray())
		 {
			 huffmanAddBit(c);
	//		 i++;
		 }
		 
		 lenbit=s.length();
	}
	
	public Code(byte c) {
		 code= new char[1];
		 code[0]=(char)c;
		 lenbit=8;
	}
	public Code(short s) {
		 code= new char[2];
		 code[0]= (char) (s>>8);
		 code[1]= (char) (s&0xff);
		 lenbit=16;
	}
	public Code(int s) {
		 code= new char[4];
		 for(int i=0;i<4;i++)
		 code[i]= (char) ((s>>(8*(3-i)))&0xff);
		 lenbit=32;

	}
	public static Code Factory(long s, int len,boolean BigEndian) {
		if (BigEndian)
			return new Code(s,len);
		if (len%8==0)
		{
			long sr=0;
			for(int i=0;i<len;i+=8)
			{
				long tmp=((s>>i)&0xff);
				tmp=tmp<<(len-i-8);
				sr|=tmp;
			}
			return new Code(sr,len);
			
		}
		return null;
	}
	public Code(long s) {
		 code= new char[8];
		 for(int i=0;i<8;i++)
			 code[i]= (char) ((s>>(8*(7-i)))&0xff);
		 lenbit=64;
	}
	public static int readCode255(IBinaryReader bin)
	{
		int i=(int)bin.readLong(8,false);
		int s=0;
		while(i==255)
		{
			s+=i;
			i=(int)bin.readLong(8,false);
		}
		s+=i;
		return s;
	}
	public static Code FactoryCode255(int s) {
	int len=0;
	long x=0;
	while(s>255)
	{
		len+=8;
		x=x<<8L|255L;
		s-=255;
	}
	x=(x<<8)|s;
	len+=8;
	return new Code(x,len);
	}
	
	public Code(long s,int len) {
		 
		if (len%8==0)
		{
			 code= new char[(len-1)/8+1];
			 for(int i=0;i<code.length;i++)
				 code[i]= (char) ((s>>(8*(code.length-1-i)))&0xff);
			 lenbit=len;}
		else
		for(int i=len-1;i>=0;i--)
		 huffmanAddBit((((s>>i)&0x1)==0)?'0':'1');
		
	}
	/** return log2(N) just upper where N is the number of different symbol.
	 * */
	public static int getDefaultLength()
	{
		
		int R = Symbol.getNbSymbol();
		int Nb = (int) (Math.log(R) / Math.log(2) + 1);
		
		return Nb;
	}
	/** convert symbol id on code of length len bits.
	 * */
	public static void reworkCode(List<ISymbol> ls, int len)
	{
		for(ISymbol s:ls)
			if (s.getCode().length()!=len)
			s.setCode(new Code(s.getId(),len));
		
	}
	/** remove zero at beginning
	 * */
	public void trim()
	{
	int o=0;
	for(int i=0;(i<lenbit) && (((code[i/8]>>(7-(i%8)))&0x1)==0);i++)
		o++;
		while (o>=8)			
		{
	 for(int i=0;i<lenbit/8-1;i++)
		 code[i]= code[i+1];
	 o-=8;
		 lenbit-=8;
		 }
		while (o>0)			
		{
			int i;
	 for(i=1;i*8<lenbit;i++)
		 code[i-1]=(char) ((code[i-1]<<1)+ ((code[i]>>7) &0x1));
	 code[(lenbit-1)/8]=(char) ((code[(lenbit-1)/8]<<1) & 0xfe);
	 o-=1;
		 lenbit-=1;
		 }
		
	}
	public Code() {
		// TODO Auto-generated constructor stub
	}

	// bit order
	// 76543210,FEDCBA98
	// code[0]  code[1]
	// 0x1 =00000001
	//0x10 =00010000
	//0x100=00000000 00000001
	//0x001=00000001 00000000
	//(011).huffmanAddBit(1)=(0111) code[0]=0b0110xxxx.
	
	public void huffmanAddBit(char code2) {
		if (code == null) {
			code = new char[1];
			lenbit = 0;
		}
		if (lenbit % 8 == 0) {
			char code3[] = new char[(lenbit /8) + 1];
			if (lenbit != 0)
				for (int i = 0; i < lenbit / 8; i++)
					code3[i] = code[i];

			code3[(lenbit + 1) / 8] = 0;
			code = code3;
		}
		if (code2 == '1')
			code[lenbit / 8] = (char) ((code[lenbit / 8]) + (char) (1 <<(7-lenbit%8)));
		//else
		//	code[lenbit / 8] = (char) ((code[lenbit / 8]));
		lenbit += 1;

	}
	

public static void main(String[] args) {
/*	{
		
		Code c = new Code();
	System.out.println(c.toString());
	c.huffmanAddBit('0');	System.out.println(c.toString());
	c.huffmanAddBit('1');	System.out.println(c.toString());
	c.huffmanAddBit('0');	System.out.println(c.toString());
	c.huffmanAddBit('1');	System.out.println(c.toString());
	c.huffmanAddBit('0');	System.out.println(c.toString());
	c.huffmanAddBit('1');	System.out.println(c.toString());
	c.huffmanAddBit('1');	System.out.println(c.toString());
	c.huffmanAddBit('1');	System.out.println(c.toString());
	
	
	ICode a = new Code((char) 1);
	System.out.println("(char) 1 : "+a.toString()+": "+a.toRaw());
	ICode b = new Code((short) 2);
	System.out.println("(short) 2 : "+b.toString()+": "+b.toRaw());
	ICode d = new Code((int) 4);
	System.out.println("(int) 4 : "+d.toString()+": "+c.toRaw());
	c = new Code((long) 0x123456789L);
	System.out.println("(long) 0x123456789 : "+c.toString() +": "+c.toRaw());
	System.out.print("0b");
	for(int i=c.lenbit-1;i>=0;i--)
		System.out.print(c.getMsb(i));
		System.out.println();
}
	
	{
		Code a = new Code(0x80);
		System.out.println("() 0x80 : \t"+a.toString()+"\t: "+a.toRaw());
		Code b = new Code(0x8F);
		System.out.println("() 0x8F : \t"+b.toString()+"\t: "+b.toRaw());
		Code c = new Code(0x1007F);
		System.out.println("() 0x1007F : \t"+c.toString() +"\t: "+c.toRaw());
		Code d = new Code((short)0x1);
		System.out.println("() 0x1 : \t"+d.toString() +"\t: "+d.toRaw());

		System.out.println(a.toString()+".compareToCode("+b.toString()+")="+a.compareToCode(b));
		System.out.println(b.toString()+".compareToCode("+c.toString()+")="+b.compareToCode(c));
		System.out.println(c.toString()+".compareToCode("+a.toString()+")="+c.compareToCode(a));
		System.out.println(c.toString()+".compareToCode("+d.toString()+")="+c.compareToCode(d));
	}
	{// test sort
		List<Code> lc=new  ArrayList<Code>();
		for (int i=1; i<25;i+=5)
			lc.add(new Code((char)i));
		for (int i=1; i<25;i+=5)
			lc.add(new Code((short)(i)));
		for (int i=1; i<25;i+=5)
			lc.add(new Code((short)(i*256)));
		for (int i=1; i<30;i+=5)
			lc.add(new Code((short)(i*256+i)));
		
			  Collections.sort(lc, new CodeComparator());
			  System.out.println("\nCodeComparator\n=========");
			  for(ICode n:lc)
			  {
				    System.out.println(n.toString() + "\t=\t" +n.toString() );
					
			  }
			  
			  Collections.sort(lc, new CodeComparatorInteger());
			  System.out.println("\nCodeComparatorInteger\n=========");
			  for(ICode n:lc)
			  {
				    System.out.println(n.toString() + "\t=\t" +n.toString() );
					
			  }
			  
	}
	
	{
		Code a = new Code();
		a.huffmanAddBit('0');a.huffmanAddBit('0');a.huffmanAddBit('0');//char(' ')= 0b000 : 
		System.out.println("(char) 1 : "+a.toString()+": "+a.toRaw());
		Code b = new Code();
		b.huffmanAddBit('1');b.huffmanAddBit('0');b.huffmanAddBit('1');b.huffmanAddBit('0');b.huffmanAddBit('0');//char('r')= 0b10100 
		System.out.println("(short) 2 : "+b.toString()+": "+b.toRaw());
		Code c = new Code();
		c.huffmanAddBit('0');c.huffmanAddBit('1');c.huffmanAddBit('1');c.huffmanAddBit('0');c.huffmanAddBit('0');//char('i')= 0b01100 
		System.out.println("(long) 0x123456789 : "+c.toString() +": "+c.toRaw());

		System.out.println("a.compareToCode(b)="+a.compareToCode(b));
		System.out.println("b.compareToCode(c)="+b.compareToCode(c));
		System.out.println("c.compareToCode(a)="+c.compareToCode(a));
		
	
	
		
		
	
	}
	
	
*/

}
/** integer compare 
 * 0b1 bigger than 0b01
 * 0b1 smaller than 0b10
 * 0b011 bigger then 0b10
 * 0b11 smaller then 0b100
 * 
 * */
public int compareToInt(ICode s2) {

	int MSBindex=Math.max(s2.length(), lenbit);
	for(int i=MSBindex-1;i>=0;i--)
	{
		Integer msb1=getMsb(i);
		Integer msb2=s2.getMsb(i);
		if(msb1==null)
			msb1=0;
		if(msb2==null)
			msb2=0;		
		if(msb1-msb2!=0)
			return (msb1-msb2);
	}
	
	if (lenbit!=s2.length())
		return -(lenbit-s2.length());
	return 0;
	
}

/** TBD
 * */
public int compareToCode2(ICode s2) {
		for(int i=0;i<lenbit;i++)
		{
			if (i==s2.length())//(lenbit>s2.lenbit)
				return -1;
		if(code[i/8]!=s2.toCode()[i/8])
			if (((code[i/8]>>(7-(i%8)))-(s2.toCode()[i/8]>>(7-(i%8))))!=0)
			return -((code[i/8]>>(7-(i%8)))-(s2.toCode()[i/8]>>(7-(i%8))));
		}
		if (lenbit<s2.length())
			return 1; 
	return 0;
	
}
public List<Code> ByteArrayToListCode(byte[] datas)
{
	List<Code> l=new ArrayList<Code>();
	for(byte c: datas)
		l.add(Code.find(c));
	return l;
	}
static Code tabByte[]= new Code[256];;
private static Code find(byte c) {

	if (tabByte[c]==null)
		tabByte[c]=new Code(c);
	return tabByte[c];
}
/* (non-Javadoc)
 * @see net.zoubwolrd.java.utils.compress.Icode#getMsb(int)
 */
@Override
public Integer getMsb(int index)
{
	if (index>=lenbit)
		return null;
	return (code[(lenbit-1-index)/8]>>(7-(index%8)) & 0x1);
}
/** gray compare 
 * 0b1 smaller than 0b10
 * 0b011 smaller then 0b1
 * */
public int compareToCode(ICode s2) {
		for(int i=0;i<lenbit;i++)
		{
		if(s2.getMsb( i)==null)
			return 1;
			int x=( getMsb( i)-s2.getMsb( i) );
			if (x!=0)
				return x;			
		}	
		if (s2.length()>lenbit)
			return -1;
	return 0;
	
}
/* (non-Javadoc)
 * @see net.zoubwolrd.java.utils.compress.Icode#write(net.zoubwolrd.java.utils.compress.BinaryStdOut)
 */
@Override
public void write(IBinaryWriter o) {
	int i = 7;
	for ( ; i <lenbit; i+=8)
			o.write((byte)code[i / 8]);
	int mod=lenbit%8;
	if(mod!=0)
		
			o.write((byte)code[lenbit / 8]>>(8-mod),mod);	
}

@Override
public void write(FileOutputStream out) throws IOException {
	
	if( lenbit<64)
	{
		long d=getLong().longValue();
	out.write((int) ((d>>0)&0xff));
	out.write((int) ((d>>8)&0xff));
	out.write((int) ((d>>16)&0xff));
	out.write((int) ((d>>24)&0xff));
	out.write((int) ((d>>32)&0xff));
	out.write((int) ((d>>40)&0xff));
	out.write((int) ((d>>48)&0xff));
	out.write((int) ((d>>56)&0xff));
	
	}
}
/* (non-Javadoc)
 * @see net.zoubwolrd.java.utils.compress.Icode#getLong()
 */
@Override
public Long getLong() {
	long tmp=0;
	if (lenbit>64)
		 throw new IllegalArgumentException("bit stream to big for getLong = " + this.toString());
	for(int i=0;i<lenbit;i+=8)
	{
		long c=(long)(this.code[i/8] & 0xff);//1234567890123L;//0x11F71FB04CB
		tmp=(tmp<<8L);
		tmp+=(long)(c);
	}
	if(lenbit%8!=0)
	tmp=tmp>>(Math.abs(8-lenbit%8));
	return tmp;
}



}
