/**
 * 
 */
package com.zoubworld.java.utils.compress;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import com.zoubworld.java.utils.compress.file.IBinaryReader;
import com.zoubworld.java.utils.compress.file.IBinaryWriter;
import com.zoubworld.utils.JavaUtils;

/**
 * @author Pierre Valleau
 * based on https://en.wikipedia.org/wiki/Shannon%E2%80%93Fano%E2%80%93Elias_coding
 *
 */
public class ShannonFanoEliasCode implements ICodingRule {

	/**
	 * 
	 */
	public ShannonFanoEliasCode() {
		// TODO Auto-generated constructor stub
	}





	public ShannonFanoEliasCode(long id, IBinaryReader binaryStdin) {
		// TODO Auto-generated constructor stub
	}





	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.ICodingRule#getCode(com.zoubworld.java.utils.compress.file.IBinaryReader)
	 */
	@Override
	public ICode getCode(IBinaryReader binaryStdIn) {

		ISymbol s = decodeASymbol( binaryStdIn);
		return get(s);
	}

	/**
	 * supposed : trie build and code also
	 * */
	public ISymbol decodeASymbol(IBinaryReader binaryStdIn2) {
		
		ICode c = getGenericCode(binaryStdIn2);
		if(c==null) return null;
		 ISymbol sym = c.getSymbol();
		 
			if (sym.getId() > 256)// complex symbol
			{
				 sym = Symbol.decode(sym, binaryStdIn2);
				
			} /*else
				sym=sym;*/
			
		
		return sym;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.ICodingRule#getGenericCode(com.zoubworld.java.utils.compress.file.IBinaryReader)
	 */
	@Override
	public ICode getGenericCode(IBinaryReader binaryStdIn) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.ICodingRule#getSymbol(com.zoubworld.java.utils.compress.file.IBinaryReader)
	 */
	@Override
	public ISymbol getSymbol(IBinaryReader binaryStdIn) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.java.utils.compress.ICodingRule#writeCodingRule(com.zoubworld.java.utils.compress.file.IBinaryWriter)
	 */
	@Override
	public void writeCodingRule(IBinaryWriter binaryStdOut) {
		// TODO Auto-generated method stub

	}
	Map<ISymbol, Long> freq;
	BidiMap<ISymbol, ICode> m;
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zoubworld.java.utils.compress.ICodingRule#get(com.zoubworld.java.utils.
	 * compress.ISymbol)
	 */
	@Override
	public ICode get(ISymbol sym) {
		if (CompositeSymbol.class.isInstance(sym)) {
			CompositeSymbol cs = (CompositeSymbol) sym;
			ISymbol sa = cs.getS1();
			ICode a = get(sa);
			ISymbol sb = cs.getS2();
			ICode b = sb.getCode();
			/*
			 * ICode code=new CompositeCode(a,b);
			 */
			sa.setCode(a);
			sb.setCode(b);
			
			CompositeCode cc=new CompositeCode(cs);
			
			cs.setCode(cc);

			// ICode code=new CompositeCode(cs);
			// cs.setCode(code);
			ICode code = cs.getCode();
			
			/**/
			return code;

		}

		ICode code = m.get(sym);
			return code;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zoubworld.java.utils.compress.ICodingRule#get(com.zoubworld.java.utils.
	 * compress.ICode)
	 */
	@Override
	public ISymbol get(ICode code) {
		/*
		 * for(Entry<ISymbol, ICode> e:m.entrySet()) if(e.getValue()==code) return
		 * e.getKey(); return null;
		 */
		return m.getKey(code);
	}
	/** build the coding set form a frequency table
	 * */
	static public ShannonFanoEliasCode buildCode(Map<ISymbol, Long> freq)
	{
		ShannonFanoEliasCode n=new ShannonFanoEliasCode();
		n.freq=freq;
		n.build();
		
		return n;
	}
	void build()
	{
		Comparator<ISymbol> byRanking = 
				  (ISymbol player1, ISymbol player2) -> Integer.compare(freq.get(player2).intValue(), freq.get(player1).intValue());
				  
		Ordered=JavaUtils.asSortedSet(freq.keySet(),byRanking);
	Ordered=JavaUtils.asSortedSet(freq.keySet());
		sum=0L;
		for(Long v:freq.values())
			sum+=v;
		double f=0.0;
		double pxi=0.0;
		m = new DualHashBidiMap<>();
		for(ISymbol xi:Ordered)
		 {
			double F =f+(pxi=p(xi))/2;
					F*=256.0*256.0*256.0*256.0;
			F *=256.0*256.0*256.0*1.0;
			f+=(pxi);//F(xi)
			
			long z=Math.round(F);//Z(xi)
			int l=(int)Math.round(Math.log(1/pxi)/Math.log(2)+1);//L(xi);
			z=z>>(56-l);
			m.put(xi, new Code(z,l));
		 }
	}
	Long sum=null;
	List<ISymbol> Ordered=null;
	private double F(ISymbol x)
	{
		
		double f=0.0;
		for(ISymbol xi:Ordered)
			if(xi!=x)
		f+=p(xi);
			else
			{
				f+=p(x)/2;
				return f;
			}
		//oups, ya un pb
		return f;
		
	}
	private double p(ISymbol xi) {
		Long l=freq.get(xi);
		return l.doubleValue()/sum;
	}
	private int L(ISymbol xi) {
		return (int)Math.round(Math.log(1/p(xi))/Math.log(2)+1);
	}
	private long Z(ISymbol xi) 
	{
		Double f=F(xi);
		f*=256.0*256.0*256.0*256.0;
		f*=256.0*256.0*256.0;
		long l=f.longValue();
		return l;		
	}
	private ICode Code(ISymbol xi) 
	{
		long z=Z(xi);
		int l=Math.round(L(xi));
		return new Code(z>>(56-l),l);		
	}
	

	public static void main(String[] args) {
		
		List<ISymbol> ls = Symbol.from("aaaaaaaabbbbbbccccdddddd");
		System.out.println(ls);
		ShannonFanoEliasCode cc=new ShannonFanoEliasCode();
		cc.freq=(Symbol.FreqId(ls));
		System.out.println(cc.freq);
		cc.build();
		System.out.println("F(a) ="+cc.F(Symbol.from('a'))
		+" p(a) ="+cc.p(Symbol.from('a'))
		+" L(a) ="+cc.L(Symbol.from('a'))
		+" Z(a) ="+cc.Z(Symbol.from('a'))+" =0b"+Long.toBinaryString(cc.Z(Symbol.from('a')))
		);
		System.out.println(cc.m);
		System.out.println("a : "+cc.p(Symbol.from('a'))+" : "+cc.get(Symbol.from('a')).toRaw());
		System.out.println("b : "+cc.p(Symbol.from('b'))
		+" : "+cc.get(Symbol.from('b')).toRaw());
		System.out.println("c : "+cc.p(Symbol.from('c'))+" : "+cc.get(Symbol.from('c')).toRaw());
		System.out.println("d : "+cc.p(Symbol.from('d'))+" : "+cc.get(Symbol.from('d')).toRaw());
			
		
	}
}
