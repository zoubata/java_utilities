package com.zoubworld.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zoubworld.utils.IParsable;
import com.zoubworld.utils.JavaUtils;
import com.zoubworld.utils.ParsableDefine;
import com.zoubworld.utils.ParsableNuplet;


/** generics parsable class
 * 
 * used with ParseFile(List<IParsable> classList, String filename)
 *  the synthaxe is "key Symbol value"
 *  example "Total : 100"
 * */
public class ParsableNuplet implements IParsable {
	protected String match=null;//"\\s*[(.*)]\\s*";
	String separator=",";
	String begin="\\[";
	String stop="\\]";
	String multilineSeparator=null;//if null single line, if "\r\n" multiline parse
	
	public ParsableNuplet(String myseparator,
	String mybegin,
	String mystop,
	String myMultilineSeparator) {
		super();
		separator=myseparator;
		begin=mybegin;
		stop=mystop;
		multilineSeparator=myMultilineSeparator;
		match="\\s*"+begin+"\\s*(\\S+)\\s*"+separator+"\\s*(\\S.*\\s*"+separator+"?)\\s*"+stop+"\\s*";

	}
	public ParsableNuplet(ParsableNuplet ref) {
				super();
				separator=ref.separator;
				begin=ref.begin;
				stop=ref.stop;
				multilineSeparator=ref.multilineSeparator;
				match="\\s*"+begin+" (\\S+)\\s+"+separator+"\\s*(\\S.*\\s*"+separator+"?)\\s*"+stop+"\\s*";
				
			}
	protected Pattern p0=null;

	private ParsableNuplet(String myMatch) {
		super();
		
	}
	/**
	 * @return the p0
	 */
	public Pattern getP0() {
		if (p0==null)
		p0=Pattern.compile(match);
		return p0;
	}

	String value=null;
	String key=null;

    public String getvalue() {
		return value;
	}
    public String[] getvalues() {
		return value.split(separator);
	}
	public String getKey() {
		return key;
	}

	public static Map<String,String> GetMap(List<IParsable> classList)
	{
		if (classList==null)
			return null;
		Map<String,String> map=new HashMap<String,String>();
		for(IParsable ps:classList)
			if ( ps instanceof ParsableDefine) 
				if(((ParsableDefine)ps).define!=null)
					if(((ParsableDefine)ps).value!=null)
						map.put(((ParsableDefine)ps).define, ((ParsableDefine)ps).value);
		return map;
		
	}
	


	public static List<String> GeList(List<IParsable> classList)
	{
		if (classList==null)
			return null;
		List<String> map=new ArrayList<String>();
		for(IParsable ps:classList)
			map.add( ( ps).get());
		return map;
		
	}
	/** in case of possible multiple match pslit string by 1 items.*/
	public String[] Split(String lines)
	{
	
	lines=lines.replaceAll(multilineSeparator, getregExpVal(separator)); 
	String replace=multilineSeparator+getregExpVal(begin);
	lines=lines.replaceAll(separator+begin,replace );
	return lines.split(multilineSeparator);
	}
private String getregExpVal(String regexp) {
		
	if(regexp.equals("\\s")) 
		return " ";
	if(regexp.equals("\\.")) 
		return ".";
	if(regexp.equals("\\(")) 
		return "(";
	if(regexp.equals("\\)")) 
		return ")";
	if(regexp.equals("\\[")) 
		return "[";
	if(regexp.equals("\\]")) 
		return "]";
	if(regexp.equals("\\.")) 
		return ".";
	return regexp;
	}
public IParsable Parse(String line) {
	
		
	Matcher m=null;
	String separatorvalue=separator;

	separatorvalue=getregExpVal(separator);
	if(multilineSeparator!=null)
line=line.replaceAll(multilineSeparator, separatorvalue);
	 if ((m=getP0().matcher(line))!=null)
	 if ((m).find())    		 
 {
		 ParsableNuplet tc; 	
		 tc= new ParsableNuplet(this); 		
		 tc.key=m.group(1);
		 
		 tc.value=m.group(2);
		 for(int i=2;i<m.groupCount();i++)
		 tc.value+=separator+m.group(i);
		
	return tc;
	}
	return null;
}
public static void main(String[] args)
{/*
	ParsableNuplet test= new ParsableNuplet(",","\\[","\\]",null);
	System.out.println("Pat : "+test.getP0());
	System.out.println("test : "+test.toString());
	
	ParsableNuplet result=(ParsableNuplet) test.Parse("[ compt, info1, info2]");
	System.out.println("key : "+result.getKey());
	System.out.println("val : "+result.getvalue());
	*/
	/*
{
	ParsableNuplet test= new ParsableNuplet(", ","\\[","\\]","\n\r");
	System.out.println("Pat : "+test.getP0());
	System.out.println("test : "+test.toString());
	
	ParsableNuplet result=(ParsableNuplet) test.Parse("[\n\rD1200\n\rDIODE_SMA_SMB\n\rD Schottky\n\r\n\r\n\r\n\r]");
	System.out.println("key : "+result.getKey());
	System.out.println("val : "+result.getvalue());
}*/
	/*
{
	ParsableNuplet test= new ParsableNuplet("\\s","\\(","\\)","\n\r");
	System.out.println("Pat : "+test.getP0());
	System.out.println("test : "+test.toString());
	
	ParsableNuplet result=(ParsableNuplet) test.Parse("(\n\rNetP1000_5\n\rP1000-5\n\rP1100-1\n\rUA1100-8\n\r)");
	System.out.println("key : "+result.getKey());
	System.out.println("val : "+result.getvalue());
}
	*/
File filein=new File("c:\\temp\\toto.txt") ;
	
StringBuilder sb = new StringBuilder();
ParsableNuplet test= new ParsableNuplet("\\s","\\(","\\)","\r\n");
String lines=JavaUtils.read( filein);
String alline[]=test.Split(lines);



		
		 for(int i=1;i<alline.length;i++)
		 {
			 ParsableNuplet result= (ParsableNuplet) (test.Parse(alline[i]));
			 List<String> l=new ArrayList<String>();
			 for(int j=0;j<result.getvalues().length;j++)
			 l.add(result.getvalues()[j]);
			 Collections.sort(l);
		 System.out.println(result.getKey()+" :" +String.join("," , l));
		 }
		
	
	}

	
public String get() {
	return  getKey() ;
}

	public String toString()
	{
		return begin+" (.*) ("+separator+" (.* ))* "+stop;
	}	


	
	



}
