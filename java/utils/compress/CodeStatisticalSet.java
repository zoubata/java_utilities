/**
 * 
 */
package com.zoubworld.java.utils.compress;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleToIntFunction;
import java.util.function.Function;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import com.zoubworld.java.utils.compress.file.IBinaryReader;
import com.zoubworld.java.utils.compress.file.IBinaryWriter;
import com.zoubworld.utils.JavaUtils;

/**
 * @author Pierre Valleau inspired from statistic, this coding allow to save it
 *         with a small footprint for various kind of distribution of code.
 *         regarding coding symbol it is less efficient than huffman, but
 *         regarding the coding of the codingset, it is more efficient. huffman
 *         is efficient for large symbol list(>25k) with a small alphabet(256)
 *         Statistical approch is interesting for size of symbol's list n>3000
 *         with a large alphabet(n/k with k=1..5) if the distribution can be
 *         approch by a
 * 
 *         used distribution are listed here :
 * 
 *         https://fr.wikipedia.org/wiki/Loi_de_probabilit%C3%A9
 * 
 *         https://fr.wikipedia.org/wiki/Loi_scalante
 * 
 *         ++
 *         https://fr.wikipedia.org/wiki/Loi_de_Benford#Loi_d'un_bloc_de_k_chiffres
 *         p(n/N)=ln(1+1/n)/ln(N);==> bit stream.
 * 
 * 
 *         https://fr.wikipedia.org/wiki/Loi_de_Zipf
 *         https://fr.wikipedia.org/wiki/Loi_de_Pareto
 * 
 *         https://fr.wikipedia.org/wiki/Loi_d%27extremum_g%C3%A9n%C3%A9ralis%C3%A9e
 *         https://fr.wikipedia.org/wiki/Loi_de_Gumbel
 *         https://fr.wikipedia.org/wiki/Loi_de_Fr%C3%A9chet
 *         https://fr.wikipedia.org/wiki/Loi_de_Weibull
 *         https://fr.wikipedia.org/wiki/Longue_tra%C3%AEne
 *         https://fr.wikipedia.org/wiki/Loi_de_L%C3%A9vy
 *         https://fr.wikipedia.org/wiki/Loi_binomiale
 *         https://fr.wikipedia.org/wiki/Loi_g%C3%A9om%C3%A9trique
 *         https://fr.wikipedia.org/wiki/Loi_exponentielle
 *         https://fr.wikipedia.org/wiki/Loi_de_Bernoulli
 *         https://fr.wikipedia.org/wiki/Loi_hyperg%C3%A9om%C3%A9trique
 * 
 *         https://fr.wikipedia.org/wiki/Loi_de_Tukey-lambda
 *         https://fr.wikipedia.org/wiki/Loi_de_Cauchy_(probabilit%C3%A9s)
 *         https://fr.wikipedia.org/wiki/Loi_logistique
 *         https://fr.wikipedia.org/wiki/Loi_normale
 *         https://fr.wikipedia.org/wiki/Loi_de_Poisson
 *         https://fr.wikipedia.org/wiki/Loi_uniforme_continue
 *         https://fr.wikipedia.org/wiki/Loi_uniforme_discr%C3%A8te
 * 
 *         https://fr.wikipedia.org/wiki/Loi_hyperg%C3%A9om%C3%A9trique
 *         https://fr.wikipedia.org/wiki/Loi_du_%CF%87%C2%B2
 * 
 */
public class CodeStatisticalSet implements ICodingRule {

	/**
	 * 
	 */
	public CodeStatisticalSet(int min, int max, Function<Long, Double> law, ISymbol mysprout) {
		this.m = new DualHashBidiMap<>();
		sprout = mysprout;
		this.current=law;
		this.min=min;
		this.max=max;
	}
	public CodeStatisticalSet(int min, int max, int law, ISymbol mysprout,double param1,double param2,double param3,double param4) {
		this.m = new DualHashBidiMap<>();
		sprout = mysprout;
		this.current=proba.get(law);
		this.min=min;
		this.max=max;
		this.lawParam1=param1;
		this.lawParam2=param2;
		this.lawParam3=param3;
		this.lawParam4=param4;
		
	}

	ISymbol sprout = new Symbol();
	int min;
	int max;
	int probabilityLaw;
	private Double lawParam1;// k,µ
	private Double lawParam2;// a,
	private Double lawParam3;// b
	private Double lawParam4;// c
	Function<Long, Double> current = null;
	
	
	Function<Long, Double> BenfordOp = (Long x) -> Math.log(1.0 + 1.0 / n(x)) / Math.log(N());
	/** https://fr.wikipedia.org/wiki/Loi_uniforme_discr%C3%A8te
	 * */
	Function<Long, Double> uniforme  = (Long x) -> 1.0/N();
	Function<Long, Double> ZipfOp1 = (
			Long k) -> (Sum(1, N(), a -> 1.0 / Math.pow(a, lawParam1)) / Math.pow(n(k), lawParam1));
	Function<Long, Double> Mandelbrot4 = (Long k) -> Math.pow((lawParam1 / (lawParam2 + n(k) * lawParam3)), lawParam4);
	Function<Long, Double> Poisson1 = (Long k) -> Math.pow(lawParam1, n(k)) / fact(n(k)) * Math.exp(-lawParam1);
	Function<Long, Double> normal2 = (Long k) -> Math.exp(-0.5 * Math.pow((n(k) - lawParam1) / lawParam2, 2.0))
			/ (lawParam2 * Math.pow(2 * Math.PI, 0.5));
	Function<Long, Double> exponentielle1 = (Long k) -> lawParam1 * Math.exp(-lawParam1 * n(k));
	Function<Long, Double> geometrique1 = (Long k) -> Math.pow((1.0 - lawParam1), n(k) - 1.0) * lawParam1;
	Function<Long, Double> XX1 = (Long k) -> Math.exp(-n(k) / 2.0) * Math.pow(n(k), lawParam1 / 2.0 - 1.0)
			/ (Math.pow(2.0, lawParam1 / 2.0) * Gamma(lawParam1 / 2.0));
	List<Function<Long, Double>> proba = Arrays.asList(
			BenfordOp, 
			uniforme,
			ZipfOp1
			);

	static long fact(long n) {
		if (n == 0)
			return 1;
		else
			return (n * fact(n - 1));
	}

	public double st_gamma(double x) {
		return Math.sqrt(2 * Math.PI / x) * Math.pow((x / Math.E), x);
	}

	static public double Gamma(double x) {
		double[] p = { 0.99999999999980993, 676.5203681218851, -1259.1392167224028, 771.32342877765313,
				-176.61502916214059, 12.507343278686905, -0.13857109526572012, 9.9843695780195716e-6,
				1.5056327351493116e-7 };
		int g = 7;
		if (x < 0.5)
			return Math.PI / (Math.sin(Math.PI * x) * Gamma(1 - x));

		x -= 1;
		double a = p[0];
		double t = x + g + 0.5;
		for (int i = 1; i < p.length; i++) {
			a += p[i] / (x + i);
		}

		return Math.sqrt(2 * Math.PI) * Math.pow(t, x + 0.5) * Math.exp(-t) * a;
	}

	private Double Sum(long begin, long end, Function<Long, Double> func) {
		Double sum = 0.0;
		for (long i = begin; i < end; i++)
			sum += func.apply(i);
		return sum;
	}

	long N() {
		return max - min;
	}

	long n(long k) {
		return k - min;
	}

	Double proba( long x) {
		return current.apply(x);
	}
	public Map<ISymbol, Double> freq( )
	{
		Map<ISymbol, Double> mfreq=new  HashMap<ISymbol, Double> ();
		for(long i=min;i<max;i++)
		{
			mfreq.put(sprout.Factory(i), current.apply(i));
		}
		return mfreq;
		
	}
	public CodeStatisticalSet(List<ISymbol> ls) {
		Map<ISymbol, Long> m = ISymbol.Freq(ls);
		getConfig(m);
	}

	private void getConfig(Map<ISymbol, Long> m2) {
		
	}

	public CodeStatisticalSet(Map<ISymbol, Long> m) {
		getConfig(m);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (int i = 1; i < 19; i++)
			System.out.println("" + i + "\t\t" + Gamma(i));
		CodeStatisticalSet cr=new CodeStatisticalSet(0,32,0,new Number(),0.0,0.0,0.0,0.0);
		cr.build();
		
		for (int i = cr.min; i < cr.max; i++)
			System.out.println(i+"\t->\t" +cr.current.apply((long)i));
		System.out.println("" +cr.freq());
		Map<ISymbol, Double> fm = cr.freq();
		System.out.println("" +ISymbol.getEntropied(fm));
		System.out.println("n"+cr.N());
		
		System.out.println(
		cr.get(new Number(3)));
	}

	public void build() {
		ISymbol sym;
		for (long i = min; i < max; i++)
		m.put(sym=sprout.Factory(i), sym.getCode());
		//HuffmanCode.buildCode(freq());
	}

	static BidiMap<ISymbol, ICode> m;

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

			CompositeCode cc = new CompositeCode(cs);

			cs.setCode(cc);

			// ICode code=new CompositeCode(cs);
			// cs.setCode(code);
			ICode code = cs.getCode();

			/**/
			return code;
		}

		ICode code = m.get(sym);
		if (code == null)
			code = m.get(sprout.Factory((long) sym.getId()));
		return code;
	}

	@Override
	public ISymbol get(ICode code) {
		return m.getKey(code);
	}

	@Override
	public void setSprout(ISymbol sprout) {
		// TODO Auto-generated method stub

	}

	@Override
	public ICode getCode(IBinaryReader binaryStdIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ICode getGenericCode(IBinaryReader binaryStdIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISymbol getSymbol(IBinaryReader binaryStdIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeCodingRule(IBinaryWriter binaryStdOut) {
		// TODO Auto-generated method stub

	}
/**
 
 1 2 5 9 -> 1 2 3 4   enc : N,mask:4,110010001
 1: 5%
 2: 50%
 3: 20%
 4: 25%
reorder : [n]




 * */
}
