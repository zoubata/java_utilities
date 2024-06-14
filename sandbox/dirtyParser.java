/**
 * 
 */
package com.zoubworld.sandbox;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zoubworld.utils.JavaUtils;

/**
 * @author zoubata
 *
 */
public class dirtyParser {

	/**
	 * 
	 */
	public dirtyParser() {
		// TODO Auto-generated constructor stub
	}
                                                 	//SG801                 ,9999-9999-9999        ,                      ,                      ,                      ,Wed Jun 14 17:55:37 2023,CharacSwLeakageLowHigh_pad3v3_tg,                      ,4                     ,3.224                 , 3.224                ,V                     ,3.3                   , 3.3                  ,V                     ,3                     ,V                     ,1                     ,                      ,pd06                  
	static private Pattern pSG801 = Pattern.compile("SG801                 ,9999-9999-9999        ,                      ,                      ,                      ,.+,CharacSwLeakageLowHigh_pad3v3_tg,                      ,\\s*(\\S+)\\s+,\\s*(\\S+)\\s+,\\s*(\\S+)\\s+,V                     ,\\s*(\\S+)\\s+ ,\\s*(\\S+)\\s+ ,V                     ,\\s*(\\S+)\\s+,V                     ,..\\s+,                      ,....\\s+  ");
	//                                                      203000009   P      3   -100.00000nA      3.32642nA    100.00000nA  pa04                   pad_iil_high/pa04
	static private Pattern ppad_iil_high = Pattern.compile("203000...\\s+\\S+\\s+(\\S+)\\s+-\\S+A(.+)A\\s+\\S+A\\s+(.+)\\s+pad_iil_high/(.+)");
	//                                                      203000107   P      4   -100.00000nA      5.06592nA    100.00000nA  pd04                   pad_iil_high/pd04
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
String f=JavaUtils.read//("C:\\temp\\Downloads\\test.txt");
("C:\\temp\\Downloads\\dlog2");

String DUT=""; 
String v1=""; 
String v2=""; 
String v3=""; 
String v4=""; 
String v5=""; 
String pin="";
String pad_iil_high="";
Matcher matcher;
for(String line:f.split("\n"))
{
	
	
	if ((matcher = pSG801.matcher(line)).find()) {
			 DUT = matcher.group(1).trim();///
			 v1 = matcher.group(2).trim();///
			 v2 = matcher.group(3).trim();///
			 v3 = matcher.group(4).trim();///
			 v4 = matcher.group(5).trim();///
			 v5 = matcher.group(6).trim();///
		//	 System.out.print("."); 
	 }
	else
	
	if ((matcher = ppad_iil_high.matcher(line)).find()) {
		 DUT = matcher.group(1).trim();///
		 pad_iil_high = matcher.group(2).trim();///
		 pin = matcher.group(3).trim();///
		// v3 = matcher.group(4).trim();///
		 if (pad_iil_high.endsWith("n"))
		 pad_iil_high="="+pad_iil_high.substring(0,pad_iil_high.length()-1)+"*1e-9";
		 if (pad_iil_high.endsWith("u"))
			 pad_iil_high="="+pad_iil_high.substring(0,pad_iil_high.length()-1)+"*1e-6";
		 
		 
		 System.out.println(pin+", " +DUT+", " +v1+", " +v2+", " +v3+", " +v4+", " +v5+"," +pad_iil_high);
}
	else if (line.contains("evxio"))
	{}
	else if (line.contains("Failure"))
	{}
	else
		{
	//	System.out.println(">"+line);
		
		}




	
	
	}
	}

}
