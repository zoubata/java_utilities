/**
 * 
 */
package com.zoubworld.compiler;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.zoubworld.utils.ArgsParser;
import com.zoubworld.utils.JavaUtils;

/**
 * @author Pierre Valleau
 *
 */
public class LogParser {


	static String gitcmd="cvs";
	Map<String,String> optionparam= null;
	ArgsParser args=null;

	public LogParser() {
		
		optionparam= new HashMap<String,String>();
		// parameter "="
			//	optionparam.put("filter="," regular expression to filter file to parse");
				
				// argument : ""
				optionparam.put("File","path to files to read");
			//	optionparam.put("Action"," list of action: \n\t- SaveLot : save lot info\n\t- SaveAllCsv : save csv\n\t- SaveAll : save all");
				
					// option"+"
				optionparam.put("-help"," this help");
				
		args=new ArgsParser(optionparam);		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	
		LogParser tool=new LogParser();
			
	/******************  ARGUMENTS *************************/
			
			
		Map<String,List<String>> errors=new HashMap<String,List<String>>();
		Map<String,List<String>> warnings=new HashMap<String,List<String>>();
		
			
			// parse it
			tool.args.parse(args);
			tool.args.check();
			
			System.out.println(tool.args.displayConfig());
			//use it
			String dir=tool.args.getArgument(1);
			
			// dir=".";
			String filestrings[]=JavaUtils.read(dir).replaceAll("\r", "").split("\n");
			dir=dir.substring(0,dir.lastIndexOf(File.separator));
			System.setProperty( "user.dir", dir );
			
			for(String sfile: filestrings)
			{
				if (sfile.contains("warning:"))
				{
					String key=sfile.substring(sfile.indexOf("warning:")+"warning:".length());
					List<String> l=warnings.get(key);
					if (l==null)  
						l=new ArrayList<String>();
					l.add(sfile.substring(0,sfile.indexOf("warning")));
					warnings.put(key, l);

				}
				if (sfile.contains("error:"))
				{
					String key=sfile.substring(sfile.indexOf("error:")+"error:".length());
					List<String> l=errors.get(key);
					if (l==null)  
						l=new ArrayList<String>();
					l.add(sfile.substring(0,sfile.indexOf("error:")));
					errors.put(key, l);
				}
				if (sfile.contains("undefined reference to"))
				{
					String key=sfile.substring(sfile.indexOf("undefined reference to")+"undefined reference to".length());
					List<String> l=errors.get(key);
					if (l==null)  
						l=new ArrayList<String>();
					l.add(sfile.substring(0,sfile.indexOf("undefined reference to")));
					errors.put(key, l);
				}
				
			}
			{
			System.out.println("warnings:");
			System.out.println("===========================================================");
			List<String> recipes=new ArrayList<String>();
			recipes.addAll(warnings.keySet());
			class RecipeCompare implements Comparator<String> {

			    @Override
			    public int compare(String o1, String o2) {
			        // write comparison logic here like below , it's just a sample
			        if (warnings.get(o1).size()==warnings.get(o2).size())
			        	return 0;
			        if (warnings.get(o1).size()<warnings.get(o2).size())
			        	return 1;
			        else return -1;
			    }
			}
			
			Collections.sort(recipes,new RecipeCompare());
			int count=0;
			for(String elt:recipes)
			{
				System.out.println("("+warnings.get(elt).size()+")"+ elt);
				count+=warnings.get(elt).size();
				for(String note:warnings.get(elt))
				System.out.println("\t"+note);
				
				
			}
			System.out.println("Total Warning : "+count+" " );
			
	}
			
			System.out.println("errors:");
			System.out.println("===========================================================");
			//System.out.println(errors);
			
			{
			List<String> recipes=new ArrayList<String>();
			recipes.addAll(errors.keySet());
			class RecipeCompare implements Comparator<String> {

			    @Override
			    public int compare(String o1, String o2) {
			        // write comparison logic here like below , it's just a sample
			        if (errors.get(o1).size()==errors.get(o2).size())
			        	return 0;
			        if (errors.get(o1).size()<errors.get(o2).size())
			        	return 1;
			        else return -1;
			    }
			}
			
			Collections.sort(recipes,new RecipeCompare());
			int count=0;
			for(String elt:recipes)
			{
				System.out.println("("+errors.get(elt).size()+")"+ elt);
				count+=errors.get(elt).size();
				for(String note:errors.get(elt))
				System.out.println("\t"+note);
				
				
			}
			System.out.println("Total Error : "+count+" " );
	}
			
			
			System.out.println("===========================================================");
			
		}
	}



