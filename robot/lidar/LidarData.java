/**
 * 
 */
package com.zoubworld.robot.lidar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.zoubworld.geometry.Point;
import com.zoubworld.geometry.Segment;
import com.zoubworld.geometry.Unit;
import com.zoubworld.java.svg.ItoSvg;
import com.zoubworld.java.svg.SvgObject;
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
public class LidarData implements ItoSvg {

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
	List<Integer> minimumlocal = new ArrayList<>();
	List<Integer> sommetlocal = new ArrayList<>();

	public int[] get360Data() {
		if (data360 == null) {
			int nb = nbdata();
			if (nb != 0) {
				data360 = new int[360];
				delta360 = new int[360];
				max = min = data360[0] = (data[0] + data[1]) / 2;
				for (int i = 1; i < 360; i++) {
					// data360[i]=(data[(i*360)/nb]);
					data360[i] = (data[(i * 360) / nb] + data[(i * 360) / nb + 1]) / 2;

					// 5000 point 6 a 12hz => 833/416point/t
					if (min > data360[i])
						min = data360[i];
					if (max < data360[i])
						max = data360[i];
					delta360[i] = data360[i] - data360[i - 1];
				}
				delta360[0] = data360[0] - data360[359];
			}
			int zero = 0;// if >0 then take a marge of error. in case of noise
			minimumlocal = new ArrayList<>();
			sommetlocal = new ArrayList<>();
			for (int i = 1; i < 360; i++)
				if ((delta360[i - 1] < -zero && delta360[i] > zero)
						|| (delta360[i - 1] > zero && delta360[i] < -zero)) {

					if (Math.abs(delta360[i - 1] - delta360[i]) > 10) {
						sommetlocal.add(i);
					} else
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

	public List<Point> getPoints(double x, double y, double theta) {
		Double a = 0.0;
		List<Point> lp = new ArrayList();
		for (Double d : range) {
			if (d != null) {
				Point p = new Point(x+d * Math.cos(a+theta), y+d * Math.sin(a+theta));
				lp.add(p);
			}
			a += dAngle;
		}
		return lp;
	}

	/**
	 * */
	public double[][] getData() {
		Double a = 0.0;
		double[][] dat = new double[range.size()][];
		List<Point> lp = new ArrayList();
		int index = 0;
		for (Double d : range) {

			if (d != null) {
				dat[index] = new double[2];
				dat[index][0] = d * Math.cos(a);
				dat[index][1] = d * Math.sin(a);
				index++;
			}
			a += dAngle;

		}
		dat = Arrays.copyOf(dat, index);
		return dat;
	}

	static public List<LidarData> parse(String filename) {
		List<LidarData> lld = new ArrayList();
		ParsableTitle OneLidarMsg = new ParsableTitle("^\\s*(header)\\s*:\\s*$");

		List<IParsable> chapterList = new ArrayList<IParsable>();
		chapterList.add(OneLidarMsg);
		String filelines = JavaUtils.read(filename);
		Map<IParsable, String[]> map = JavaUtils.ExtractChapterFile(chapterList, filelines.split("\n"));
		for (IParsable key : map.keySet())
			if (key != null) {
				String t[] = (map.get(key));
				// System.out.println(key.toString()+"=>"+t);
			}
		int index = 0;
		for (String lines[] : map.values()) {
			LidarData d = LidarData.parse(lines);
			if (!(d.range == null || d.range.size() <= 1)) {
				lld.add(d);
				index++;
			}
		}
		return lld;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ParsableTitle OneLidarMsg = new ParsableTitle("^\\s*(header)\\s*:\\s*$");
		String fileName = "C:\\Temp\\svg\\log.log";

		List<IParsable> chapterList = new ArrayList<IParsable>();
		chapterList.add(OneLidarMsg);
		String filelines = JavaUtils.read(fileName);
		Map<IParsable, String[]> map = JavaUtils.ExtractChapterFile(chapterList, filelines.split("\n"));
		for (IParsable key : map.keySet())
			if (key != null) {
				String t[] = (map.get(key));
				// System.out.println(key.toString()+"=>"+t);
			}
		int index = 0;
		for (String lines[] : map.values()) {
			LidarData d = LidarData.parse(lines);
			if (!(d.range == null || d.range.size() <= 1)) {
				// System.out.println(d.toSvg(1,1,45));
				Terrain terrain = new Terrain();
				
				
				List<Point> lp = d.getPoints(1.5,1,0);
				
				d.improvedata();
				List<Point> lp2 = d.getPoints(1.51,1,0);
				lp2=filter(lp2,1.5,1);
				SvgRender svg = new SvgRender();
				svg.getObjects().add(terrain);
				Point.size=3;
				Point.style= "style=\"fill:" + SvgObject.black + "\"";;
				svg.getObjects().add(new SvgObject(Point.toSvg(lp)));
				
				Point.style= "style=\"fill:" + SvgObject.red + "\"";;
				svg.getObjects().add(new SvgObject(Point.toSvg(lp2)));
				
				lp2 = d.getPoints(1.52,1.0,0);
				lp2=filter(lp2,1.52,1);
				List<Segment> ls = d.reduce2(lp2);
				Segment.style = "style=\"stroke:" + SvgObject.Green + ";stroke-width:20\"";
				Point.style = "style=\"fill:" + SvgObject.Green + "\"";
				Point.size = 30;
				svg.getObjects().add(new SvgObject(Segment.toSvg(ls)));
				
				Point.size=50;
				svg.getObjects().add(new Point(1.5,1.0));
				JavaUtils.saveAs("C:\\Temp\\svg\\lidar.svg", svg.toSvg());
				
				d.positione((Unit.mmtoM(1500)), (Unit.mmtoM(1000)), Unit.degreToRadian(0));

				 svg = new SvgRender();
				svg.getObjects().add(terrain);
				svg.getObjects().add(d);
				JavaUtils.saveAs("C:\\Temp\\svg\\lidar-" + (index) + ".svg", svg.toSvg());
				// SvgRender.convert("C:\\Temp\\svg\\lidar-"+(index)+".svg","C:\\Temp\\svg\\lidar-"+(index)+".jpg");

				index++;
			}
		}
	}

	private static List<Point> filter(List<Point> lp,double x0,double y0) {
		
		List<Point> lp2= new ArrayList();
		for(int i=1;i<lp.size()-1;i++)
			if ((lp.get(i-1)!=null && lp.get(i)!=null && lp.get(i+1)!=null)
				&&	(new Segment(lp.get(i-1),lp.get(i+1))).longeur()
					<(new Segment(lp.get(i),lp.get(i+1)).longeur()/2)
				)
			{}
			else if ((lp.get(i-1)!=null && lp.get(i)!=null && lp.get(i+1)!=null)
				&&	(new Segment(lp.get(i-1),lp.get(i+1))).longeur()
					<(new Segment(lp.get(i),lp.get(i-1)).longeur()/2)
				)
			{}
			else if (lp.get(i-1)==null || lp.get(i)==null || lp.get(i+1)==null)
					{
				lp2.add(null);
					}
			else if((new Segment(lp.get(i-1),new Point(x0,y0))).longeur()<0.400)
			{}
			else
				lp2.add(lp.get(i));
		return lp2;
	}

	private void improvedata() {
		Double d0 = null, d1 = null, d2 = null, d3 = null;
		for (int i = 0; i < range.size(); i++) {
			d0 = range.get(i);
			if (d3 != null && d2 == null && d1 != null) {
				d2 = (d1 + d3) / 2;
				range.remove(i - 2);
				range.add(i - 2, d2);

			} else if (d3 != null && d2 == null && d1 == null && d0 != null) {
				d2 = (d0 + 2 * d3) / 3;
				d1 = (d0 * 2 + d3) / 3;

				range.remove(i - 1);
				range.add(i - 1, d1);
				range.remove(i - 2);
				range.add(i - 2, d2);

			}
			d3 = d2;
			d2 = d1;
			d1 = d0;
		}
	}

	double x0;
	double y0;
	double theta0;

	private void positione(double myx0, double myy0, double d) {
		x0 = myx0;
		y0 = myy0;
		theta0 = d;
	}

	Double dAngle;// from ros topic "angle_increment: 0.00872664619237
	Double anguleMin;// from ros topic "angle_min: -3.14159274101"
	Double anguleMax;// from ros topic "angle_max: +3.14159274101"
	List<Double> range;
	static IParsable ps_dAngle = new ParsableSymbol(":", "angle_increment");
	static IParsable ps_anguleMin = new ParsableSymbol(":", "angle_min");
	static IParsable ps_anguleMax = new ParsableSymbol(":", "angle_max");
	static IParsable ps_range = new ParsableList2(",", "ranges:", "\\[", "\\]");
	static IParsable ps_intensities = new ParsableList2(",", "intensities:", "\\[", "\\]");

	public static LidarData parse(String[] lines) {
		LidarData ld = new LidarData();
		List<IParsable> classList = new ArrayList<IParsable>();
		classList.add(ps_dAngle);
		classList.add(ps_anguleMin);
		classList.add(ps_anguleMax);
		classList.add(ps_range);
		classList.add(ps_intensities);

		List<IParsable> result = JavaUtils.ParseFile(classList, lines, null);
		Map<String, String> map = ParsableSymbol.GetMap(result);
		ld.dAngle = ParsableSymbol.MapKeyToDouble(map, "angle_increment");
		ld.anguleMin = ParsableSymbol.MapKeyToDouble(map, "angle_min");
		ld.anguleMax = ParsableSymbol.MapKeyToDouble(map, "angle_max");
		Map<String, List<String>> map2 = ParsableList2.GetMap(result);
		ld.range = ParsableList2.MapKeyToListDouble(map2, "ranges:");
		List<Double> i = ParsableList2.MapKeyToListDouble(map2, "intensities:");
		if (i != null)
			for (int j = 0; j < i.size(); j++)
				if (i.get(j) != null && i.get(j) == 0) {
					ld.range.remove(j);
					ld.range.add(j, null);
				}
		return ld;

	}

	public String toSvg() {
		theta0=0;
		x0=1.5;
		y0=1;
		/*
		String s = "";
		Segment.style = "style=\"stroke:" + SvgObject.Red + ";stroke-width:20\"";
		Point.style = "style=\"fill:" + SvgObject.Green + "\"";
		Point.size = 5;
		List<Segment> ls1 = toSegmentList(x0, y0, 0);
		// System.out.println(Segment.tosvg(ls1));
		SvgObject s1 = new SvgObject(Segment.tosvg(ls1));
		Point.style = "style=\"fill:" + SvgObject.Black + "\"";
		Point.size = 6;
		Segment.style = "style=\"stroke:" + SvgObject.Blue + ";stroke-width:40\"";
		List<Segment> ls = reduce(ls1);
		SvgObject s2 = new SvgObject("<g id=\"small1\" transform=\"translate(000,300)\">" + Segment.tosvg(ls) + "</g>");

		Point.style = "style=\"fill:" + SvgObject.Lime + "\"";
		Point.size = 6;
		Segment.style = "style=\"stroke:" + SvgObject.Yellow + ";stroke-width:40\"";
		List<Segment> ls2 = reduce2(toPointList(x0, y0, 0));
		SvgObject s3 = new SvgObject(
				"<g id=\"small2\" transform=\"translate(050,050)\">" + Segment.tosvg(ls2) + "</g>");
		SvgRender svg = new SvgRender();
		svg.getObjects().add(s1);
		svg.getObjects().add(s2);
		svg.getObjects().add(s3);

		JavaUtils.saveAs("C:\\Temp\\svg\\try.svg", svg.toSvg());

		
		Segment.style = "style=\"stroke:" + SvgObject.red + ";stroke-width:40\"";
		ls=ls2;
		// System.out.println(Segment.tosvg(ls));
		for (Segment seg : ls)
			if (seg != null)
				s += seg.toSvg();

		Segment.style = "style=\"stroke:rgb(0,0,0);stroke-width:40\"";
		Segment seg2 = choiceTheta(ls);
		if (seg2 != null)
			s += seg2.toSvg();
		theta0 = -seg2.getTheta();
		Segment.style = "style=\"stroke:rgb(55,55,55);stroke-width:100\"";
		for (Segment seg : reduce(toSegmentList(x0, y0, theta0)))
			if (seg != null)
				s += seg.toSvg();*/
		//compute rotation at 90°
		List<Segment> ls2 = reduce2(toPointList(x0, y0, 0));
		Segment seg2 = choiceTheta(ls2);
		theta0=-seg2.getTheta();
        Segment.style = "style=\"stroke:" + SvgObject.black + ";stroke-width:40\"";
		String s=seg2.toSvg();
		
		//compute rotation at 180°
		ls2=reduce2(toPointList(x0, y0, theta0));
		List<Point> lp=Point.getWindows(Segment.getPoints(ls2));
		if ((new Segment(lp.get(0),lp.get(1))).longeur()>(new Segment(lp.get(0),lp.get(2))).longeur())
		{	theta0+=Math.PI/2;	
		//compute x/y translation
		}
		List<Point> lpw = Point.getWindows(lp=toPointList(x0, y0, theta0));
		x0+=-lpw.get(0).getX0();
		y0+=-lpw.get(0).getY0();
		Point.size=50;
		s+=Point.toSvg(lpw);
		Point.size=3;
		Point.style="style=\"fill:" + SvgObject.Black + "\"";
		s+=Point.toSvg(lp);
		Point.style="style=\"fill:" + SvgObject.red + "\"";
		
		Segment.style = "style=\"stroke:" + SvgObject.red + ";stroke-width:40\"";
		for (Segment seg : ls2=reduce2(toPointList(x0, y0, theta0)))
			if (seg != null)
				s += seg.toSvg();
		
		
				
		Segment.style = "style=\"stroke:rgb(0,0,255);stroke-width:4\"";
		
		return toSvg2(x0, y0, 0) /*+ toSvg2(x0, y0, theta0) *//*+ s*/;
	}

	private Segment choiceTheta(List<Segment> ls) {
		while(ls.contains(null))
		ls.remove(null);
		ls.sort(Comparator.comparing(Segment::longeur).reversed());

		List<Double> lt = ls.stream().map(s -> s.getTheta()).collect(Collectors.toList());
		List<Integer> li = lt.stream().map(d -> (int) (d * 90)).collect(Collectors.toList());
		// histogram(li).get(0)*** todo
		return ls.get(0);
	}

	public String toSvg2(double xm, double ym, double tetha) {
		String s = "<g id=\"" + this.getClass() + "\">\n";
		int index = 0;
		if (anguleMin != null && anguleMax != null && dAngle != null)
			for (double a = anguleMin; a < anguleMax; a += dAngle)
				if (range.size() > index && range.get(index) != null) {
					double x0 = xm;
					double y0 = ym;
					double x1 = xm + range.get(index) * Math.cos(a + tetha);
					double y1 = ym + range.get(index) * Math.sin(a + tetha);
					index++;
					s += "\t<line x1=\"" + Unit.MtoMm(x0) + "mm\" y1=\"" + Unit.MtoMm(y0) + "mm\" x2=\""
							+ Unit.MtoMm(x1) + "mm\" y2=\"" + Unit.MtoMm(y1)
							+ "mm\" style=\"stroke:rgb(255,0,0);stroke-width:2\" /> " + "\r\n";
				} // ("\r\n// angle="+Unit.toDegre(a)+" degre")+
				else
					index++;
		s += "</g>\n" + "";
		return s;
	}

	public List<Segment> reduce2(List<Point> lp) {
		List<Segment> ls = new ArrayList();
		Point pprevious = null;
		Point pn_1=null;
		Segment s = null;
		double error=0.03;
		for (Point p : lp) {
			// if point to far, there is an obstacle
			if(p!=null & pn_1!=null)
			if ((new Segment(pn_1, p)).longeur()>error)
			{
				ls.add(s);
				pprevious = p;
				s=null;
			}//if new seg or on it
			if (s == null || (p != null && s.getDroite().near(p, error))) {
				if (p != null && pprevious != null)
					s = new Segment(pprevious, p);
				else
					pprevious = p;
			} else// new seg
			{
				ls.add(s);
				pprevious = p;
				s=null;
			}
			pn_1=p;
		}
		return ls;

	}

	public List<Point> toPointList(double xm, double ym, double tetha) {
		List<Point> lp = new ArrayList();
		int index = 0;
		Double x0 = 0.0;
		Double y0 = 0.0;
		double x1 = 0;
		double y1 = 0;
		if (anguleMin != null && anguleMax != null && dAngle != null)
			for (double a = anguleMin; a < anguleMax; a += dAngle)
				if (range.size() > index && range.get(index) != null)

				{

					x1 = xm + range.get(index) * Math.cos(a + tetha);
					y1 = ym + range.get(index) * Math.sin(a + tetha);

					x0 = x1;
					y0 = y1;
					lp.add(new Point(x1, y1));
					index++;
				} // ("\r\n// angle="+Unit.toDegre(a)+" degre")+
				else {
					index++;
					x0 = y0 = null;
					lp.add(null);
				}
		return lp;
	}

	public List<Segment> reduce(List<Segment> ls) {
		List<Segment> lrs = new ArrayList();
		double t0 = 100000;
		double t00 = 100000;
		Point p0 = null;
		Point p1 = null, p1old = null;
		for (Segment s : ls)
			if (s != null) {
				double t1 = s.getTheta();
				p1 = s.getP1();
				double d = Math.abs(t1 - t0);
				if (d > Math.PI / 4) {
					t00 = t1;
					if (p0 != null)
						lrs.add(new Segment(p0, p1old));
					p0 = s.getP0();
				}
				p1old = p1;
				double t05 = (t1 + t0) / 2;
				t0 = t1;
			} else {
				p0 = p1 = p1old = null;
			}
		if (p0 != null && p1 != null)
			lrs.add(new Segment(p0, p1));
		return lrs;
	}

	public List<Segment> toSegmentList(double xm, double ym, double tetha) {
		List<Segment> lp = new ArrayList();
		int index = 0;
		Double x0 = 0.0;
		Double y0 = 0.0;
		double x1 = 0;
		double y1 = 0;
		if (anguleMin != null && anguleMax != null && dAngle != null)
			for (double a = anguleMin; a < anguleMax; a += dAngle)
				if (range.size() > index && range.get(index) != null && range.get(index - 1) != null
						&& (0.1 > Math.abs(range.get(index) - range.get(index - 1))))

				{

					x1 = xm + range.get(index) * Math.cos(a + tetha);
					y1 = ym + range.get(index) * Math.sin(a + tetha);
					if (x0 != null && y0 != null)
						lp.add(new Segment(x0, y0, x1, y1));
					x0 = x1;
					y0 = y1;

					index++;
				} // ("\r\n// angle="+Unit.toDegre(a)+" degre")+
				else {
					index++;
					x0 = y0 = null;
					lp.add(null);
				}

		return lp;
	}

	public String toSvg(double xm, double ym, double tetha) {
		String s = "<g id=\"" + this.getClass() + "\">\n";
		int index = 0;
		Double x0 = 0.0;
		Double y0 = 0.0;
		double x1 = 0;
		double y1 = 0;
		if (anguleMin != null && anguleMax != null && dAngle != null)
			for (double a = anguleMin; a < anguleMax; a += dAngle)
				if (range.size() > index && range.get(index) != null) {

					x1 = xm + range.get(index) * Math.cos(a + tetha);
					y1 = ym + range.get(index) * Math.sin(a + tetha);
					if (x0 != null && y0 != null)
						s += "\t<line x1=\"" + Unit.MtoMm(x0) + "mm\" y1=\"" + Unit.MtoMm(y0) + "mm\" x2=\""
								+ Unit.MtoMm(x1) + "mm\" y2=\"" + Unit.MtoMm(y1)
								+ "mm\" style=\"stroke:rgb(255,255,0);stroke-width:25\" /> " + "\r\n";

					x0 = x1;
					y0 = y1;
					index++;
				} // ("\r\n// angle="+Unit.toDegre(a)+" degre")+
				else {
					index++;
					x0 = y0 = null;
				}
		s += "</g>\n" + "";
		return s;
	}
}
