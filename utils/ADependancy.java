package com.zoubworld.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zoubworld.utils.ADependancy;
import com.zoubworld.utils.IParsable;


/**
 * @author Pierre Valleau
 *
 */
public class ADependancy implements IParsable {

	public ADependancy() {
		// TODO Auto-generated constructor stub
	}

	List<String> keys=new ArrayList<String>();
	List<String> dependances=new ArrayList<String>();

	
	Pattern p0=Pattern.compile("^.*:.*$");
	Pattern p1=Pattern.compile("^\\s*(\\S+\\s+)+\\s*$");
	Pattern p2=Pattern.compile("^\\s*(\\S+\\s+)+\\s*$");
		
	public IParsable Parse(String line) {
		
			
		Matcher m=null;

		 
		 if ((m=p0.matcher(line)).find())    		 
	 {
	
		
			 ADependancy tc= new ADependancy(); 
			 {
				 String[] tab=line.split(":");//replaceAll("\\S:", ".#").split("[^c:\\\\]:");
				 String subline=tab[0];
				 subline=subline.replaceAll("\\\\ ", "##");
				 for(String it:subline.split("\\s"))
					 if (it!=null) tc.keys.add(it.replaceAll("##", "\\\\ ").trim());
				 if (tab.length>1)
				 {
				  subline=tab[1].replaceAll("\\\\ ", "##");;
				  String[] tabslpit=subline.split("\\s");
				 for(String it:tabslpit)
					 if (it!=null) tc.dependances.add(it.replaceAll("##", "\\\\ ").trim());}
			 }
			/* tc.type=m.group(1);
			 if(!tc.getType().matches("CALLL|TESTL.*"))
			 {
			
			 tc.testblock=m.group(2).replace(",", "").trim();
			 
			 tc.out=new ArrayList<String>();
			 
	
		for(int i1=3;i1<=m.groupCount();i1++)
			if (m.group(i1)!=null)
			tc.out.add(m.group(i1).replace(",", "").trim());
			 }
			 else
			 {
				 tc.label=m.group(2).replace(",", "").trim();	
				 tc.testblock=m.group(3).replace(",", "").trim();
				 
				 tc.out=new ArrayList<String>();
				 
    	
    		for(int i1=4;i1<=m.groupCount();i1++)
    			if (m.group(i1)!=null)
    			tc.out.add(m.group(i1).replace(",", "").trim());
				 } 
		*/
		return tc;
		}
		return null;
	}

	public List<String> getKeys() {
		return keys;
	}

	public List<String> getList(String key) {
		
		return dependances;
	}

	@Override
	public String get() {
		
		return dependances.toString();
	}

	@Override
	public List<String> getList() {
		
		return dependances;
	}

}
