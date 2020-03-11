package com.zoubworld.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author Pierre Valleau
 *
 */
public class GraphVizBuilder {

	/** data organisation :
	 *  -------------------   
	 *    map(key,list)
	 *    list(item1,item2,...) 
	 *  GRAPH
	 *  ------  
	 * key -> item1
	 *     \_> item2
	 *     ...
	 * 
	 * */
	 
	Map<String,List<String>> graph=null;
	
	/**
	 * 
	 */

	public GraphVizBuilder(Map<String, List<String>> data) {
		graph=data;
	}
	public String toStringCSV()
	{
		String tmp="";
		Set<String> setname=new HashSet<String>();
		for(Object key:graph.keySet())
		{
			
			if (setname.contains(key.toString()))
				{}
			else
				tmp+=key.toString()+":"+graph.get(key)+"\n";
			setname.add(key.toString());
			
		}
		
		return tmp;
	}
	public String toString()
	{
		int count=0;
		String s="digraph G {\n";
	//	s+="size =\"4000,4000\";";
		Set<String> setname=new HashSet<String>();
//		int i=1;
		for(Object key:graph.keySet())
		{
			if (setname.contains(key.toString()))
				
			setname.add(key.toString());
		}
		for(Object key:graph.keySet())
		{
			
			count++;
			String attrib1="";/*
			if (count==1)
				attrib1+="shape=box,style=filled";
			else
			if (tc.getType().startsWith("BIN"))
				if (tc.getTestblock().contains("Pass"))
					attrib1+="shape=box,style=filled;color=green";
				else
				attrib1+="shape=box,style=filled,color=red";
			*/
	//		s+=" "+key.toString() +(attrib1==""?"":"[" +attrib1+"]")+";\n";
			/*
			if (tc.getType().startsWith("CALL"))
			{
		s+=" "+tc.getTestblock() +"  " +" ["+"color=blue,shape=box"+"];\n";
		}	*/
			for(Object out:graph.get(key))
			{
				String attrib="";
				/*if (out.endsWith("_fl"))	
				{	attrib+="color=red,";
				s+=" "+ out+" ["+attrib+"];\n";	
				}
				Testcase tcnext=find(tc,out);*/
				/*
				if (out.endsWith("_lb"))	
						attrib+="shape=box,";*/
				/*if (out.endsWith("NEXT"))	
					attrib+="style=bold,";
				if(tcnext!=null)
				s+=" "+tc.getTestblock() +" -> " + tcnext.getTestblock()+" ["+attrib+"label=\""+out+"\"];\n";
				else*/
					s+=" \""+key.toString().replaceAll("\\\\", "_") .replaceAll(":", "_") +"\" -> \"" + out.toString().replaceAll("\\\\", "_").replaceAll(":", "_")+"\" ["+(attrib1==""?"":" " +attrib1+" ")+"label=\""+out.toString().replaceAll("\\\\", "_").replaceAll(":", "_")+"\"];\n";	
				
				/*	
				if(out.equalsIgnoreCase("NEXT"))	
					{
					s+=" "+tc.getTestblock() +" -> " + flow.get(flow.indexOf(tc)+1).getTestblock()+" ["+attrib+"label=\""+out+"\"];\n";
					}	
				
				else if (out.endsWith("_lb"))	
				s+=" "+tc.getTestblock() +" -> " + out+" ["+attrib+"label=\""+out+"\"];\n";	
				else 
				
						s+=" "+tc.getTestblock() +" -> " + out+" ["+attrib+"label=\""+out+"\"];\n";	
				*/
			}
	/*		if (tc.getType().startsWith("CALL"))
					{
				s+=" "+tc.getTestblock() +" -> " + flow.get(flow.indexOf(tc)+1).getTestblock()+" ["+"shape=box,style=bold,"+"label=\""+"auto next"+"\""+"];\n";
				}	*/
		/*	if (tc.getLabel()!=null)
				s+=tc.getLabel()+" [shape=box]; \n"+tc.getLabel() +" -> " + tc.getTestblock()+"\n";	*/
		}
		s+=" }";
		return s;
		
		
		}
	/*
	private Testcase find( Testcase tc, String out) {
		if(out.equals("NEXT"))
			return flow.get(flow.indexOf(tc)+1);
		for(Testcase mytc:flow)
			if(out.equalsIgnoreCase(mytc.getLabel()))
				return mytc;
		
		
		return null;
	}*/
}