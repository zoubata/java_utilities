/**
 * 
 */
package com.zoubworld.robot.lidar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.zoubworld.geometry.Point;
import com.zoubworld.geometry.Unit;
import com.zoubworld.java.svg.ItoSvg;
import com.zoubworld.java.svg.SvgRender;
import com.zoubworld.robot.Terrain;
import com.zoubworld.utils.IParsable;
import com.zoubworld.utils.JavaUtils;
import com.zoubworld.utils.ParsableList;
import com.zoubworld.utils.ParsableList2;
import com.zoubworld.utils.ParsableNuplet;
import com.zoubworld.utils.ParsableSymbol;
import com.zoubworld.utils.ParsableTitle;

/**
 * @author Pierre Valleau
 *
 */
public class LidarData implements ItoSvg{

	int data[];

	public int nbdata() {
		if (data != null)
			return data.length;
		return 0;
	}

	public double deltaAngle() {
		int nb = nbdata();
		if (nb != 0)
			return 360.0 / nb;
		return 0.0;
	}

	public int[] getRawData() {
		return data;
	}

	int data360[] = null;
	int delta360[] = null;
	Integer min = null;
	Integer max = null;
	List<Integer> minimumlocal=new ArrayList<>();
	List<Integer> sommetlocal=new ArrayList<>();
	public int[] get360Data() {
		if (data360 == null) {
			int nb = nbdata();
			if (nb != 0) {
				data360 = new int[360];
				delta360= new int[360];
				max = min = data360[0] = (data[0]+data[1])/2;
				for (int i = 1; i < 360; i++) {
					// data360[i]=(data[(i*360)/nb]);
					data360[i] = (data[(i * 360) / nb] + data[(i * 360) / nb + 1]) / 2;

					// 5000 point 6 a 12hz => 833/416point/t
					if (min > data360[i])
						min = data360[i];
					if (max < data360[i])
						max = data360[i];
					delta360[i]=data360[i] -data360[i-1]; 
				}
				delta360[0]=data360[0] -data360[359];
			}
			int zero=0;// if >0 then take a marge of error. in case of noise
			minimumlocal=new ArrayList<>();
			sommetlocal=new ArrayList<>();
			for (int i = 1; i < 360; i++)
			if ((delta360[i-1]<-zero && delta360[i]>zero) || (delta360[i-1]>zero && delta360[i]<-zero))
			{
				
				if (Math.abs(delta360[i-1]-delta360[i])>10)
				{
					sommetlocal.add(i);
				}
				else
					minimumlocal.add(i);
					
			}
				
		}
		return data360;
	}

	public void setRawData(int[] data) {
		this.data = data;
	}

	/**
	 * 
	 */
	public LidarData() {
		// TODO Auto-generated constructor stub
	}
	
	public List<Double> getRange() {
		return range;
	
	}
		public List<Point> getPoints() {
				Double a=0.0;
		List<Point> lp=new ArrayList();
		for(Double d:range)			
		{
			if(d!=null)
			{
			Point p=new Point(d*Math.cos(a),d*Math.sin(a));
			lp.add(p);
			}
			a+=dAngle;			
		}
		return lp;
	}
		/**
		 * */
	public double[][] getData() {
		Double a=0.0;
		double[][]  dat= new double[range.size()][];
	List<Point> lp=new ArrayList();
	int index=0;
	for(Double d:range)			
	{
		
		if(d!=null)
		{
			dat[index]=new double[2];
			dat[index][0]=d*Math.cos(a);
			dat[index][1]=d*Math.sin(a);
			index++;
		}
		a+=dAngle;	
				
	}
	dat = Arrays.copyOf(dat, index);
	return dat;
}

	static public List<LidarData> parse(String filename)
	{
		List<LidarData> lld=new ArrayList();
		  ParsableTitle OneLidarMsg=new ParsableTitle("^\\s*(header)\\s*:\\s*$");
			
		List<IParsable> chapterList=new ArrayList<IParsable>();
		chapterList.add(OneLidarMsg);
		String filelines=JavaUtils.read(filename);
		Map<IParsable,String[]> map=JavaUtils.ExtractChapterFile( chapterList,  filelines.split("\n"));
		for(IParsable key:map.keySet())
			if(key!=null)
		{String t[]=(map.get(key));
		//System.out.println(key.toString()+"=>"+t);
		}
		int index=0;
		for(String lines []:map.values())
		{
			LidarData d=LidarData.parse(lines);
			if (!(d.range==null || d.range.size()<=1))
			{
				lld.add(d);
			index++;}
		}
	return lld;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		  ParsableTitle OneLidarMsg=new ParsableTitle("^\\s*(header)\\s*:\\s*$");
		  String fileName="C:\\Temp\\svg\\log.log";
		  
		List<IParsable> chapterList=new ArrayList<IParsable>();
		chapterList.add(OneLidarMsg);
		String filelines=JavaUtils.read(fileName);
		Map<IParsable,String[]> map=JavaUtils.ExtractChapterFile( chapterList,  filelines.split("\n"));
		for(IParsable key:map.keySet())
			if(key!=null)
		{String t[]=(map.get(key));
		//System.out.println(key.toString()+"=>"+t);
		}
		int index=0;
		for(String lines []:map.values())
		{
			LidarData d=LidarData.parse(lines);
			if (!(d.range==null || d.range.size()<=1))
			{
		//	System.out.println(d.toSvg(1,1,45));
			Terrain terrain=new Terrain();
			
			d.positione((Unit.mmtoM(1500)), (Unit.mmtoM(1000)),Unit.degreToRadian(0));
			
			SvgRender svg=new SvgRender();		
			svg.getObjects().add(terrain);
			svg.getObjects().add( d);
			JavaUtils.saveAs("C:\\Temp\\svg\\lidar-"+(index)+".svg", svg.toSvg());
			SvgRender.convert("C:\\Temp\\svg\\lidar-"+(index)+".svg","C:\\Temp\\svg\\lidar-"+(index)+".jpg");
			
			index++;}
		}
	}
	double x0; double y0; double theta0;
private void positione(double myx0, double myy0, double d) {
	x0=myx0;
			y0=myy0;
			theta0=d;
	}

Double dAngle;// from ros topic "angle_increment: 0.00872664619237
Double anguleMin;// from ros topic "angle_min: -3.14159274101"
Double anguleMax;// from ros topic "angle_max: +3.14159274101"
List<Double> range;
static  IParsable ps_dAngle=new ParsableSymbol(":","angle_increment");
static IParsable ps_anguleMin=new ParsableSymbol(":","angle_min");
static IParsable ps_anguleMax=new ParsableSymbol(":","angle_max");
static IParsable ps_range=new ParsableList2(",","ranges:","\\[","\\]");
static IParsable ps_intensities=new ParsableList2(",","intensities:","\\[","\\]");


	public static LidarData parse(String[] lines) {
		LidarData ld=new LidarData();
	List<IParsable> classList=new ArrayList<IParsable>();
	classList.add(ps_dAngle);
	classList.add(ps_anguleMin);
	classList.add(ps_anguleMax);
	classList.add(ps_range);
	classList.add(ps_intensities);
	
	List<IParsable> result=JavaUtils.ParseFile(classList, lines, null);
	Map <String,String> map=ParsableSymbol.GetMap(result);		
	ld.dAngle = ParsableSymbol.MapKeyToDouble(map, "angle_increment");
	ld.anguleMin = ParsableSymbol.MapKeyToDouble(map, "angle_min");
	ld.anguleMax = ParsableSymbol.MapKeyToDouble(map, "angle_max");
	Map<String,List<String>>  map2=ParsableList2.GetMap(result);
	ld.range=ParsableList2.MapKeyToListDouble(map2, "ranges:");
	List<Double> i=ParsableList2.MapKeyToListDouble(map2, "intensities:");
	if (i!=null)
	for(int j=0;j<i.size();j++)
		if(i.get(j)!=null && i.get(j)==0)
		{ld.range.remove(j);ld.range.add(j,null);}
		return ld;
		
	}
	public String toSvg()
	{
		return toSvg(x0,y0,theta0);
	}
	public String toSvg(double xm,double ym, double tetha)
	{
		String s="<g id=\""+this.getClass()+"\">\n";
		int index=0;
		if (anguleMin!=null && anguleMax!=null && dAngle!=null )
		for(double a=anguleMin;a<anguleMax;a+=dAngle)
			if(range.size()>index && range.get(index)!=null)
		{
			double x0=xm;
			double y0=ym;
			double x1=xm+range.get(index)*Math.cos(a+tetha);
			double y1=ym+range.get(index)*Math.sin(a+tetha);
			index++;
		s+= "\t<line x1=\""+Unit.MtoMm(x0)+"mm\" y1=\""+Unit.MtoMm(y0)+"mm\" x2=\""+Unit.MtoMm(x1)+"mm\" y2=\""+Unit.MtoMm(y1)+"mm\" style=\"stroke:rgb(255,0,0);stroke-width:2\" /> "+"\r\n";
		}//("\r\n// angle="+Unit.toDegre(a)+" degre")+
			else
				index++;
		s+="</g>\n"+"";
		return s;
	}
}
