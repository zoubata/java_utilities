package com.zoubworld.orthophoniste;

import java.util.HashMap;
import java.util.Map;

import com.zoubworld.utils.JavaUtils;
/*
 
 <?xml version="1.0" encoding="utf-8"?>
<svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="300" height="200">
  <title>Exemple simple de figure SVG</title>
  <desc>
    Cette figure est constitu�e d'un rectangle,
    d'un segment de droite et d'un cercle.
  </desc>
 
  <rect width="100" height="80" x="0" y="70" fill="green" />
  
  <circle cx="90" cy="80" r="50" fill="blue" />
  <line x1="180" y1="60" x2="180" y2="40" stroke="red" />
  <line x1="180" y1="60" x2="200" y2="60" stroke="green" />
  <text x="180" y="60">Un texte</text>
  <path  d="m 195,150 25,0 0,-25 30,0"    id="path7954"          />
  
  <path style="fill:none;stroke-width:0.1;stroke-linecap:butt;stroke-linejoin:miter;stroke:rgb(0%,0%,0%);stroke-opacity:1;stroke-miterlimit:10;" d="M 20.837413 -3.364131 L 20.837413 -5.386983 L 25.787413 -5.386983 L 25.787413 -4.336983 " transform="matrix(20,0,0,20,942.099404,1108.321686)"/>
  
</svg>

 */
public class BuilderTest {

	public BuilderTest() {
		// TODO Auto-generated constructor stub
	}
	static Map<Integer,Integer> history=new HashMap<Integer,Integer>();
		public static void main(String[] args) {
		

		StringBuffer datatoSave=new StringBuffer();
		int X=210*5;
		int Y=297*5;
		datatoSave.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
				+ "<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" width=\""+X+"\" height=\""+Y+"\">\n");
		int i;
		for(i=0;i<Y;i++)
		{
		int x=(int)(Math.random()*X);
		int y=(int)(Math.random()*Y);
		if (isvalide(x,y))
		{
		history.put(x, y);
		String test=Character.toString((char)('A'+(int)(Math.random()*6)));
		datatoSave.append("\t<text x=\""+x+"\"\ty=\""+y+"\" >"+test+"</text>\n");
		}
		
		}
		datatoSave.append("</svg>");
		JavaUtils.saveAs("c:\\temp\\test.svg", datatoSave.toString());
	}/*
y-
x+*/

		private static boolean isvalide(int x, int y) {


			for(Integer x2:history.keySet())
			{
				int y2=history.get(x2);
				double d=(Math.pow((x-x2),2)+Math.pow((y-y2),2));
		//		System.out.println("x="+x+"; y="+y+";x2="+x2+"; y2="+y2+"; d="+d+";");
				if (d<2200)
					return false;
			}
			return true;
		}

}
