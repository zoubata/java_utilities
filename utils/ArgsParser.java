/**
 * 
 */
package com.zoubworld.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zoubworld.utils.ArgsParser;
import com.zoubworld.utils.JavaUtils;


/** an class that manage command line arguments.
 * @author Pierre Valleau
 <P>
 <li>
 <br> usages : ArgsParser args=new ArgsParser(optionsavailablehelp);
 <br> where Map<String, String> optionsavailablehelp;
 <br> optionsavailable help <= "keywords","help on it"
 <br> keyword can be a ordered argument : "argument"  args.getArgument(1)
 <br> keyword can be an parameter  : "myparam=toto" (defaultly "toto")  args.getParam("myparam")
 <br> keyword can be an option  : "+myoption" or "--myoption" (defaultly on) ; or "-myoption" defaultly off args.getOption("myoption")
 <br> keyword can be an config file  : "@file"  this file will be parse as command line arguments
 <br> keyword can be an help  : "?" it is just additionnal text display on help
 </P>  
 <br> Detail usage :
 <br> 
 <br> <li> a declaration step : 
 <br> {@code 
  HashMap<String, String> optionparam = new HashMap<String,String>();
  optionparam.put("option/arg/param","help of option");
  ...
  ArgsParser arg=new ArgsParser(optionparam);}
 <br> }
 <br><li> a process input of main():
 <br> {@code
  arg.parse(args);
  arg.check();
  		if(args.length==0) arg.help();
 }
  		
 <br> <li> get data :
 *<br> {@code
 		String param1=arg.getArgument(1);
 		
 		String filenamec=arg.getParam("param");
 		
 		List<String> filenames=arg.getParamAsList("paramlist");
 		
 		Map<String,Sting> filenames=arg.getParamAsMap("paramlist");
 		
 		bool enabled=arg.getOption("option");
 		
 *		}
 */
public class ArgsParser {
	/**
	 * as key option with qualifier as value help on it
	 * */
	Map<String, String> optionsavailablehelp;

	/**
	 * 
	 */
	public ArgsParser(Map<String, String> myOptionsAvailablehelp) {
		optionsavailablehelp = myOptionsAvailablehelp;
	//	optionsavailablehelp.put("SeparatorForParameter=,", " separator used inside parameter  for Tuple and Map; Tuple synthase is \"(a,b,c,d)\" and for map the synthaxe is \"{{a,b},{c,d},{e,f}} or {}\" where \",\" is the SeparatorForParameter");
		init(optionsavailablehelp.keySet());
	}

	private ArgsParser() {

	}

	/* getter */
	public String getArgument(int index) {
		if (index<=0)
			return null;
		if (arguments.size()>=index)
		return arguments.get(index-1);
		return null;
	}

	/* getter */
	public String getParam(String paramName) {
		return parameter.get(paramName);
	}
	
	/* setter to change the value
	 * */
	public void setParam(String paramName,String value) {
		 parameter.put(paramName,value);
	}

	/* getter */
	public List<String> getParamAsList(String paramName) {
		return JavaUtils.parseListString(getParam( paramName));
	}
	
	/* getter */
	public Map<String,String> getParamAsMap(String paramName) {
		return JavaUtils.parseMapStringString(getParam( paramName));
	}

	/* getter */
	public Boolean getOption(String optionName) {
		return options.get(optionName);
	}

	private String getValueParam(String param) {
		if(param==null)
			return null;
		if (param.contains("=")) {
			return param.substring(param.indexOf("=") + 1, param.length());
		}
		return null;
	}	
	private String getParamName(String param) {
		if (param.contains("=")) {
			return param.substring(0, param.indexOf("="));
		}
		return null;
	}

	private boolean isArgument(String option) {
		if(option==null)
			return false;
		if (option.startsWith("--"))
			return false;
		if (option.startsWith("-"))
			return false;
		if (option.startsWith("+"))
			return false;
		if (option.contains("="))
			return false;
		return true;
	}
	
	
	/**
	 * return the state of the help if none it should be something else
	 * */
	private Boolean getHelper(String option) {
		if(option==null)
			return null;
		if (option.startsWith("?"))
			return true;	
		return null;
	}
	/**
	 * return the state of the option if none it should be a parameter
	 * */
	private Boolean getQualifier(String option) {
		if(option==null)
			return null;
		if (option.startsWith("--"))
			return true;
		if (option.startsWith("-"))
			return false;
		if (option.startsWith("+"))
			return true;
		return null;
	}
	/**
	 * return the state of the option if none it should be a parameter
	 * */
	private String getConfigFile(String option) {
		if(option==null)
			return null;
		if (option.startsWith("@"))
			return option.substring(1,option.length());
		/*
		if (option.startsWith("#"))
			return false;
		if (option.startsWith("&"))
			return false;*/
		return null;
	}

	private String getOptionName(String option) {
		if (option.startsWith("--"))
			return option.substring(2,option.length());
		if (option.startsWith("-"))
			return option.substring(1,option.length());
		if (option.startsWith("+"))
			return option.substring(1,option.length());
		
		return  option;
	}

	Map<String, Boolean> options = new HashMap<String, Boolean>();// optional,
																	// default
																	// behaviour
																	// defined
	Map<String, String> parameter = new HashMap<String, String>();// optional,
																	// default
																	// behaviour
																	// defined
	List<String> arguments = new ArrayList<String>(); // mandatory on cmd line
	int argumentscount = 0;

	/**
	 * initialise option and parameter from a Collection
	 * */
	protected void init(Collection<String> optionsparamList) {
		options = new HashMap<String, Boolean>();
		parameter = new HashMap<String, String>();
		parse(optionsparamList);
		argumentscount = arguments.size();
		arguments = new ArrayList<String>();
		/*
		for (String argmnt : optionsparamList.keySet())
			if (getQualifier(argmnt) == null)
				if (getValueParam(argmnt) == null) {
					argumentscount++;
				}*/
	}

	/* setter */
	public void parse(String optionsparamList[]) {
		
		List<String> t = new ArrayList<String>();
		if (optionsparamList!=null)
		for (String s : optionsparamList)
			t.add(s);
		parse(t);
	}

	public void parse(Collection<String> optionsparamList) {
		for (String option : optionsparamList) {
			if (getConfigFile( option)!=null)
			{
				parse(JavaUtils.read(getConfigFile( option)).split("\\s"));
			}
			else if (getQualifier(option) != null) {
				options.put(getOptionName(option), getQualifier(option));
			}
			else if (getValueParam(option) != null) {
				parameter.put(getParamName(option), getValueParam(option));
			} 
			else if (getHelper(option) != null) {}
			else if (isArgument(option) )  {
				arguments.add(option);
			}
			else
			{
				System.err.println("parameter '"+option+"' not understood");
			}
		}		
	}
	

	


	public boolean check()
	{
		if (this.argumentscount != this.arguments.size()) {
			System.out.println(help());
			System.out.println("bad command line : you have provide "+this.arguments.size()+" arguments when "+argumentscount+" is expected\n");
			System.out.println("your config is :\n");
			System.out.println(displayConfig());
			//System.exit(-1);
			return false;

		}
		if ((this.getOption("help") != null) && (this.getOption("help"))) {
			System.out.println(help());
			return true;
			
		}
		return true;
	}

	public String displayConfig() {
		String tmp = "";
		// init(optionsavailablehelp.keySet());
		tmp += "Configuration Apply :\n";
		tmp += "---------------------\n";
		tmp += "Arguments List :\n";
		int i=1;
		if(arguments.size()!=0)
		{
		for (String argmnt : arguments)
			
					tmp += "\t\t"+(i++)+" : " + argmnt+"\n";
		tmp += "\n\n";
		}
		else
			tmp += "\t- none\n\n";	
		if(options.keySet().size()!=0)
		{
		tmp += "Options List :\n";
		for (String option : options.keySet()) {
			tmp += "\t\t "+option+" is '" + getOption(option) + "'\n";
		}
		}
		if(parameter.keySet().size()!=0)
		{
		tmp += "\tparameters list:\n";
		for (String param : parameter.keySet()) {
			tmp += "\t\t'" + param + "' : '" + parameter.get(param) + "'\n";
		}
		}
		return tmp;
	}

	public String toString() {
		return help();
	}

	/**
	 * optionsavailable is a map that list all option, the value is the comment
	 * about the options. if qualifier is define then it is the default value.
	 * */
	public String help() {
		String tmp = "";
		// init(optionsavailablehelp.keySet());
		for (String argmnt : optionsavailablehelp.keySet())
		
			if (getHelper(argmnt) != null)// if some help text :
					tmp += " " + argmnt.substring(1);
		
		tmp += "synthaxe is [(qualifier)options[=value]] [(qualifier)options] parameter[=value] ";
		
		for (String argmnt : optionsavailablehelp.keySet())
			if (getQualifier(argmnt) == null)
				if (getHelper(argmnt) == null)
						if (getValueParam(argmnt) == null)
					tmp += " " + argmnt;
		tmp += "\n\n";
		int i = 0;
		
		String tmp2 =  "\tqualifier :\n";
		tmp2 += "\t\t'--' : enable/use the option \n";
		tmp2 += "\t\t'-' : disable this option\n";
		tmp2 += "\t\t'+' : enable this option\n";
		tmp2 += "\toptions list:\n";
		for (String option : optionsavailablehelp.keySet())
			if (getHelper(option) == null)
			if (getQualifier(option) != null) {
				tmp2 += "\t\t'" + option + "' : "
						+ optionsavailablehelp.get(option).replaceAll("\n", "\n\t\t\t") + "\n";

				tmp2 += "\t\t\t default value is " + getQualifier(option) + "\n";
				i++;
			}
		if(i!=0)
			tmp+=tmp2;
		
		 i = 0;
		 tmp2 = "\tparameter list:\n";
		for (String param : optionsavailablehelp.keySet())
			if (getHelper(param) == null)
			if (getQualifier(param) == null)
				if (getValueParam(param) != null) {
					tmp2 += "\t\t'" + param + "' : "
							+ optionsavailablehelp.get(param).replaceAll("\n", "\n\t\t\t") + "\n";
					tmp2 += "\t\t\t default value is '" + getValueParam(param)
							+ "'\n";
					i++;
				}
		if (i!=0) 
		{tmp+=tmp2;}
		tmp += "\tValues can be according to the need/context :\n"
				+ "\t\t- a value : 'toto' : a simple string without space\n"
				+ "\t\t- a list : '[toto,titi]' : a string without space starting with [ finsishing with ] and element are separated by ,\n"
				+ "\t\t- a map : '{key=value,toto=1,titi=alpha}' : a string without space starting with { finsishing with } and element are separated by a ',' key is followed by a '=' and the value\n"
				+ "";
		
		 tmp2 = "\tArgument list : mandatory items in the good order:\n";
		 i = 0;
		for (String argmnt : optionsavailablehelp.keySet())
			if (getQualifier(argmnt) == null)
				if (getValueParam(argmnt) == null) {
					i++;
					tmp2 += "\t\t'arg[" + i + "]:" + argmnt + "' : "
							+ optionsavailablehelp.get(argmnt).replaceAll("\n", "\n\t\t\t") + "\n";
				}
		if (i!=0)
			tmp+=tmp2;
		tmp += "\tConfiguration file:\n\t\t@configfile : where 'configfile' is the path to a text file that contains arguments,options and qualifier";
		
		return tmp;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/// create it
		Map<String,String> optionparam= null;
		ArgsParser myargs=null;
		
		optionparam= new HashMap<String,String>();
		// parameter "="
		optionparam.put("PatternFile=.*"," regular expression to filter file to parse");
		optionparam.put("Extention=.csv"," Extention to filter file to parse");
		optionparam.put("Separator=,"," separator string on file between field");
		optionparam.put("filtercol=1,2,3,4,5,6,7,8"," if action is filtercol, the list of colum number, if action is FILTERCOL, list of colunm name where space char is replace by \\s");
		// argument : ""
		optionparam.put("Action"," list of action: \n\t- merge : merge several file\n\t- filtercol : perform filtering in file process\n\t- FILTERCOL : perform filtering in RAM process\n\t- header : extrat header");
		optionparam.put("Dir","path to file");
		optionparam.put("outputfile","file to save");
		// option"+"
		optionparam.put("--help"," this help");
		myargs=new ArgsParser(optionparam);
		// parse it
		myargs.parse(args);
		myargs.check();
		
		//use it
		myargs.getArgument(1);
		myargs.getParam("Separator");
		
	}

	public boolean isFileList(String filein) {
		if (filein==null)
		return false;
		return filein.startsWith("@");
	}

	public List<String> getFileList(String filein) {
		List<String> lf=new ArrayList<String>();
		for(String s:JavaUtils.read(filein.substring(1, filein.length())).split(System.lineSeparator()))
			lf.add(filein.substring(1,filein.lastIndexOf(File.separator)+1)+ s);
		return lf;
	}
	public List<String> getList(String paramName) {
		return JavaUtils.parseListString(getParam( paramName));
		
	}
    /** add a argument */
	public void set(int index, String element) {
		index=index-1;
		if (this.arguments.size()>index)
		{
		if (this.arguments.get(index)!=null)
			this.arguments.remove(index);		
		else		
			this.argumentscount++;
		this.arguments.add(index, element);
		}		
	}
	
/**
 * (a,b)
 * */
	public String[] getTuple(String paramName) {
		if(!getParam( paramName).contains("("))
			return null;
		if(!getParam( paramName).contains(")"))
			return null;
		String t=getParam( paramName);
		t=t.replaceFirst("\\(", "");
		
		t=t.replace(")", "");
		
		String[] c=t.split(getParam( "SeparatorForParameter"));
		
		return c;
	}
	public boolean isTuple(String paramName)
	{
		return getTuple(paramName)!=null;
	}
	public boolean isMap(String paramName)
	{
		return getParamAsMap(paramName)!=null;
	}
	/** deprecated used : getParamAsMap(
public Map<String ,String> getMap(String paramName) {
	if(!getParam( paramName).contains("{"))
		return null;
	if(!getParam( paramName).contains("}"))
		return null;
	String ss=getParam("SeparatorForParameter");
	if (ss==null)
			 ss=",";
	String l=getParam( paramName);
	String t[]=l.split("\\}\\s*"+ss+"\\s*\\{");
	 Map<String ,String> m= new HashMap<String ,String> ();
	 
	 for(String tt:t)
	 {
		
		
		 String[] ttt = tt.split(ss);
	    m.put(ttt[0].replaceAll("\\{", ""), ttt[1].replaceAll("\\}", ""));
	 }
	 return m;
	}*/

public String toConfigFile() {
	
	String tmp = "";
	// init(optionsavailablehelp.keySet());
	
	int i=1;
	if(arguments.size()!=0)
	{
	for (String argmnt : arguments)
		
				tmp += "\t\t"+ argmnt+"\n";
	tmp += "\n\n";
	}
	
	if(options.keySet().size()!=0)
	{
	
	for (String option : options.keySet()) {
		if(getOption(option))
			tmp += "\t\t --"+option+"\n";
		else
			tmp += "\t\t -"+option+"\n";
		
	}
	}
	if(parameter.keySet().size()!=0)
	{
	for (String param : parameter.keySet()) {
		tmp += "\t\t" + param + "=" + parameter.get(param) + "\n";
	}
	}
	return tmp;
}

}
