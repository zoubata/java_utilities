package com.zoubworld.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*** a parser class for Javautils.parse(...) that manage a reg exp and return a list<String>
	 * */
public class ParsableRegExp implements IParsable
{
	List<String> data;
	@Override
	public IParsable Parse(String line) {
		Matcher m=null;
		m=p.matcher(line);
		if(m==null)
			return null;
		ParsableRegExp out=null;
		if(!p.pattern().startsWith("("))
		{
			if (m.find( )) {
		
			 out=new ParsableRegExp(this);
		for(int i=1;i<=m.groupCount();i++)
			out.data.add(m.group(i));
			}
		}
		else
		{
		if ((m)!=null)
		 while (m.find()) {	
			 if(out==null) out=new ParsableRegExp(this);
	            String e=line.substring(m.start(), m.end());
	            e=m.group();
	            out.data.add(e);       
	           
	        } 
		}
		 if(out==null)
			return null;
	 
		return out;
	}

	@Override
	public String get() {
		if( data==null || data.size()==0)
			return null;
		return data.get(0);
	}
	Pattern p=null;
	/** the regExp form is "blabla(match)blabla(match)bla..."
	 *  the regExp form is "(match)"
	 * */
	public ParsableRegExp(String RegExp)
	{
		p=Pattern.compile(RegExp);
		data=new ArrayList<String>();
	}

	public ParsableRegExp(ParsableRegExp parsableRegExp) {
		this.p=parsableRegExp.p;
		this.data=new ArrayList<String>();		
	}

	@Override
	public List<String> getList() {
	
		return data;
	}
	}