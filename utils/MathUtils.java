package com.zoubworld.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
/**
 * @author Pierre Valleau
 *
 */
public class MathUtils {

	public MathUtils() {
		// TODO Auto-generated constructor stub
	}

	public static Double sum(List<Double> ld)
	{ 
		Double d=0.0;
		for( Double dd:ld)
			d+=dd;
		return d;
	}
	public static Double min(List<Double> ld)
	{
		Double d=ld.get(0);
		for( Double dd:ld)
			d=Math.min(dd,d);
		return d;
	}
	
	public static Double max(List<Double> ld)
	{
		Double d=ld.get(0);
		for( Double dd:ld)
			d=Math.max(dd,d);
		return d;
	}
	
	public static Double R(List<Double> ld)
	{
		return max(ld)-min( ld);
	}
	public static Double USL(List<Double> ld,Double cpk)
	{
		Double av=average( ld);
		Double std=stdDev( ld);
		Double d=av-3*cpk*std;
		return d;
	}
	public static Double USH(List<Double> ld,Double cpk)
	{
		Double av=average( ld);
		Double std=stdDev( ld);
		Double d=av+3*cpk*std;
		return d;
	}
	public static List<Double> outlayer(List<Double> ld,Double cpk)
	{
		Double av=average( ld);
		Double std=stdDev( ld);
		Double ush=av+3*cpk*std;
		Double usl=av-3*cpk*std;
		List<Double> ldd=new ArrayList<>(ld.size());
		for(Double d:ld)
			if (!((d>=usl)&&(d<=ush)))
		ldd.add(d);
		return ldd;
	}
/*
	private static Double Repeatabilityint(List<Double> lStandardDeviation)
	{
	
		Double d=lStandardDeviation.get(0);
		for( Double dd:lStandardDeviation)
			d+=dd*dd;
		d=d/lStandardDeviation.size();
		d=Math.sqrt(d);
		d=d*6;
		return d;
	}
	/** Repeatability: Equipment Variation (EV)

This is the "within appraiser" variation. It measures the variation one appraiser has when measuring the same part (and the same characteristic) using the same gage more than one time. The calculation is given below.

	 * //6*(sum(x�)/N)^0.5
	 * where x=std(list)
	 * *
	public static Double Repeatability(List<List<Double>> lld)
	{	
		List<Double> lStandardDeviation=new ArrayList();
		for(List<Double> ld:lld)
			lStandardDeviation.add(stdDev(ld));
		return Repeatabilityint(lStandardDeviation);
		
}*/
	public Double SigmaTV(List<List<Double>> lld)
	{
		Double SigmaEV=SigmaEV(lld);;// �repeatability� Equipment Gage Variation (EV)
		Double SigmaAV=SigmaAV(lld);//The standard deviation for reproducibility or Appraisal variability (AV).
		Double SigmaPV=0.0;		
		return Math.sqrt(SigmaEV*SigmaEV+SigmaAV*SigmaAV+SigmaPV*SigmaPV);		
	}
	public static Double SigmaEV(List<List<Double>> lld)
	{
		List<Double> r=R2(lld);
		Double Rb=average(r);
		Double SigmaAV=Rb/ getD2( lld.size());
		return SigmaAV;
	}
	/**
	 * lld is a list of different site, that contain a list of measure for the same part.*/
	public static Double SigmaAV(List<List<Double>> lld)
	{
		List<Double> av=average2(lld);
	Double Rxb=max(av)-max(av);
		Double SigmaAV=Rxb/ getD2( lld.size());
		return SigmaAV;
	}
	
	/*able for finding d2 in Gage R&R Studies*
	*/
	private static double getD2(int n)
	{
		double d2;
		double d[]={1.0,1.128,1.693,2.059,2.326,2.534,2.704,7.847,2.970,3.078,3.173,3.258,3.336,3.407,3.472};
		if(n<1)
			d2=d[0];
		else
		if(n<16) d2=d[n];
		else
			d2=d[15];
		return d2;
	}
	
	/**
	 * Reproducibility: Appraiser Variation (AV)

This is the "between appraisers" variation. It is the variation in the average of the measurements made by the different appraisers when measuring the same characteristic on the same part. The calculation is given below.

AV equation
AV=((Xbdiff*K2)�-EV�/nr)^0.5



where K2 is a constant that depends on the number of appraisers. For 2 appraisers, K2 is 0.7071. For three appraisers, K2 is 0.5231 For this example:
AV Calculation

	 * *
	public static Double Reproduceability(List<List<Double>> lld)
	{
		Double d=0.0;
		Double EV=0.0;
		Double K2=1.0;
		Double Xbdiff=0.0;
		if(lld.size()==2)
			K2=0.7071;
		else
		if(lld.size()==3)
			K2=0.5231;
		else
		
		d=K2*K2*Xbdiff*Xbdiff;
		d=d-(EV*EV/lld.size());
		return Math.sqrt(d);
	}
	/** Repeatability and Reproducibility (GRR) 
	 * the next calculation combines the two above to determine GRR, which is given by: GRR=(EV�+AV�)^ 0.5
	 * */
	public static Double R2R(List<List<Double>> lld)
	{
		Double Repr=SigmaEV(lld);
		Double Repe=SigmaAV(lld);
		return Math.sqrt(Repr*Repr+Repe*Repe);
	}
	
	/*
	 * Part Variation (PV)

The part variation is determined by multiplying the range of the part averages (Rp) by a constant K3. K3 depends on the number of parts. For 5 parts, K3 = 0.4030. The part variation is then given by:

Part Variation calculation

	 Parts
	K3
2	0.7071
3	0.5231
4	0.4467
5	0.4030
6	0.3742
7	0.3534
8	0.3375
9	0.3249
10	0.3146
	  
	 */
	/** Repeatability and Reproducibility (GRR)  %
	 * GRR % result goal is to be below 15%
	 * 
	 * 
	 *  */
	public static Double GR2R(List<List<Double>> lld,Double USL,Double USH)
	{
		Double rr=R2R( lld);
		
		return rr/(USH-USL);
	}
	public static Double average(List<Double> ld)
	{
		Double d=0.0;
		d=sum(ld)/ ld.size();
		return d;
	}
	public static List<Double> average2(List<List<Double>> lld)
	{
		List<Double> ld=new ArrayList<Double>(lld.size());
		for(List<Double> l:lld)
			ld.add(average(l));
		return ld;
	}
	public static List<Double> R2(List<List<Double>> lld)
	{
		List<Double> ld=new ArrayList<Double>(lld.size());
		for(List<Double> l:lld)
			ld.add(R(l));
		return ld;
	}
	public static Double median(List<Double> ld)
	{
		List<Double> ldd=new ArrayList<>(ld.size());
		ldd.addAll(ld);
		Collections.sort(ldd);		
		return ldd.get(ldd.size()/2);
	}
	
	public static Double stdDev(List<Double> ld)
	{
		int size=ld.size();
		Double mean=average( ld);
		Double deviationSum=0.0;
        double array[] = new double [(int) size];
		  //STANDARD DEVIATION CALCULATION
        for (int i = 0; i < size; i++)
            {
                array[i] = (Math.pow((array[i] - mean), 2));
            }
        for (int i = 0; i < size; i++)
        {
            deviationSum += array[i];
        }

        double variance = ((deviationSum / size));

        double standardDeviation = Math.sqrt(variance);

		return standardDeviation;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static <T> double norm(Map<T, Long> m, int e) {
		double d=0;
		for(Long l:m.values())
			d+=Math.pow(l, e);
		d=Math.pow(d, 1.0/e);
		
		return d;
	}

}
