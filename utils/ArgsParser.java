/**
 * 
 */
package com.zoubworld.utils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


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
	public ArgsParser( @SuppressWarnings("rawtypes") Class mainclass,Map<String, String> myOptionsAvailablehelp) {
		optionsavailablehelp = myOptionsAvailablehelp;
		if (optionsavailablehelp==null)
			optionsavailablehelp=new HashMap<String, String> ();
		if (	!optionsavailablehelp.keySet().contains("--help")
			||	!optionsavailablehelp.keySet().contains("-help")
			||	!optionsavailablehelp.keySet().contains("+help")
				)
			optionsavailablehelp.put("--help"," display the help,");
		//optionsavailablehelp.put("-gui"," display a windows interface,");
		if (	!optionsavailablehelp.keySet().contains("--interactive")
				||	!optionsavailablehelp.keySet().contains("-interactive")
				||	!optionsavailablehelp.keySet().contains("+interactive")
					)
			optionsavailablehelp.put("-interactive"," display a commandline shell to configure the tool,");
		
	//	optionsavailablehelp.put("SeparatorForParameter=,", " separator used inside parameter  for Tuple and Map; Tuple synthase is \"(a,b,c,d)\" and for map the synthaxe is \"{{a,b},{c,d},{e,f}} or {}\" where \",\" is the SeparatorForParameter");
		init(optionsavailablehelp.keySet());
		main=mainclass;
		if (ArgsParser.class==main)
			System.err.println("Error : on parameter of constructor, class should be different from ArgsParser.class");
	}

	@SuppressWarnings("unused")
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
	public void setArgument(int index,String text) {
		if (index<=0)
			return ;
		while (arguments.size()<argumentscount)
			arguments.add(null);
			
		if (arguments.size()>=index)
		 arguments.set(index-1,text);
		
	}
	/* getter */
	public String getParam(String paramName) {
		return parameter.get(paramName);
	}
	public Map<String, String> getParams() {
		return parameter;
	}
	public List< String> getArguments() {
		List<String> l=new ArrayList();
		for (String argmnt : optionsavailablehelp.keySet())
			if (getQualifier(argmnt) == null)
				if (getValueParam(argmnt) == null) {
					l.add(argmnt);
				}
		return l;
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
	public void setOption(String optionName,Boolean b) {
		 options.remove(optionName);
		 options.put(optionName,b);
	}
	public Map<String, Boolean> getOptions( ) {
		return options;
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
		option=option.trim();
		if (option.startsWith("@"))
			return option.substring(1,option.length());
		/*
		if (option.startsWith("#"))
			return false;
		if (option.startsWith("&"))
			return false;*/
		return null;
	}/**
	 * return the state of the option if none it should be a parameter
	 * */
	private String getMultiConfigFile(String option) {
		if(option==null)
			return null;
		option=option.trim();
		if (option.startsWith("@@"))
			return option.substring(2,option.length());
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

	public boolean interactivemode()
	{
		Scanner myInput = new Scanner( System.in );
		System.out.println("Entering in interractive mode.");
		System.out.println("you can enter a config file like : @c:\\path\\file.jconfig, or skip it by press return or exit to quit");
		
		String option=myInput.nextLine();
		
		if (getMultiConfigFile( option)!=null)
		{	
			for(String line:JavaUtils.read(getMultiConfigFile( option)).split("\\n+"))
			if (!line.isBlank() && !line.startsWith("//")){
				run(line.split("\\n"));
			}
			System.exit(0);
			/***/
		}
		else if (getConfigFile( option)!=null)
		{
			loadConfig(getConfigFile( option));
			return true;
		}
		if("exit".equals(option))
			return false;
		for(String key:options.keySet())
		{

			System.out.println("Option :"+key+" : \r\n"+getDescrition(key));
			System.out.println("Default value"+" : "+getDefaultQualifier(key));
			System.out.println("please enter : true or false or default to keep default value, or exit?");

			Boolean b=null;
			do
			{
			try {
 			b=myInput.nextBoolean();
			}
			catch (java.util.InputMismatchException e) {
				String s=myInput.nextLine();
				if("exit".equals(s))
					return false;
				else
					if ("default".equals(s.trim()))
						b=getDefaultQualifier(key);
					else
				if ("*".equals(s))
					b=getDefaultQualifier(key);
				else
					System.out.println("true or false ?");				
			}
			}
			while(b==null);
			options.put(key,b);			
		}
		for(String key:parameter.keySet())
		{
			System.out.println("Parameter : "+key+" : \r\n"+getDescrition(key));
			System.out.println("default value"+" : '"+getDefaultParam(key)+"'");

			System.out.println("please enter : a value ? or default(*) to keep default value, or exit ?");
			String v="";
			boolean defaut=false;
			do
			{
			v=myInput.nextLine();
			if("exit".equals(v))
				return false;
			else
				if ("*".equals(v))
				{
					   v=getDefaultParam(key);
					   parameter.put(key,v);
					   defaut=true;
					}
			else if("default".equals(v))
			{
				   v=getDefaultParam(key);
				   parameter.put(key,v.trim());	
				   defaut=true;
				}
			else if("blank".equals(v))
			{
				   v="";
				   parameter.put(key,v.trim());	
				   defaut=true;
				}
			else
			parameter.put(key,v);
			}
			while((v==null || v.equals(""))&&!defaut);
		}
		for(int i=0;i<argumentscount;i++)
		{
			System.out.println("Argument "+(i+1)+" : "+argumentslist.get(i)+" : "+
		getDescrition(argumentslist.get(i)));
			System.out.println("a value ?");
			String v="";
			while(v==null || v.equals(""))
			v=myInput.nextLine();
			if("exit".equals(v))
				return false;
			arguments.add(v.trim());			
		}
		return true;
		
	}
	public void loadConfig(String configFile) {
		arguments.clear();
		parse(JavaUtils.read(configFile).split("\\n+"));		
	}

	public String getDescrition(String key) {
		String des=optionsavailablehelp.get(key);
		if (des==null)
			des=optionsavailablehelp.get("+"+key);
		if (des==null)
			des=optionsavailablehelp.get("-"+key);
		if (des==null)
			des=optionsavailablehelp.get("--"+key);
		if (des==null)
			des=optionsavailablehelp.get(key+"=");
		if (des==null)
			for(String k:optionsavailablehelp.keySet())
				if (k.startsWith(key+"="))
					return optionsavailablehelp.get(k);
		return des;
	}
	
	private String getDefaultParam(String key) {
		for(String k:optionsavailablehelp.keySet())
			if (k.startsWith(key+"="))
				return getValueParam(k);
		return null;
	}
	private Boolean getDefaultQualifier(String key) {
		String des=optionsavailablehelp.get(key);
		if (des==null)
			des=optionsavailablehelp.get("+"+key);
		else
			return null;
		if (des==null)
			des=optionsavailablehelp.get("-"+key);
		else
			return getQualifier("+"+key);
		if (des==null)
			des=optionsavailablehelp.get("--"+key);		
		else
			return getQualifier("-"+key);
		if (des!=null)
			return getQualifier("--"+key);
		return null;
	}
	
	String BriefHelp="";///< one line short sumary
	String DescriptionHelp="";///< one chapter general information
	
	public String getBriefHelp() {
		return BriefHelp;
	}

	public void setBriefHelp(String briefHelp) {
		BriefHelp = briefHelp;
	}

	public String getDescriptionHelp() {
		return DescriptionHelp;
	}

	public void setDescriptionHelp(String descriptionHelp) {
		DescriptionHelp = descriptionHelp;
	}

	Map<String, Boolean> options = new HashMap<String, Boolean>();// optional,
																	// default
																	// behaviour
																	// defined
	Map<String, String> parameter = new HashMap<String, String>();// optional,
																	// default
																	// behaviour
																	// defined
	List<String> arguments = new ArrayList<String>(); // mandatory on cmd line values apply on current command line
	List<String> argumentslist = new ArrayList<String>(); // mandatory on cmd line, list of arg values mandatory
	
	int argumentscount = 0;

	/**
	 * initialise option and parameter from a Collection
	 * */
	protected void init(Collection<String> optionsparamList) {
		options = new HashMap<String, Boolean>();
		parameter = new HashMap<String, String>();
		parse(optionsparamList);
		argumentscount = arguments.size();
		argumentslist=arguments;
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
	
		if (optionsparamList==null || optionsparamList.length==0)
		{
			if (((getOption("gui")!=null)  && !getOption("gui") )
				|| ( (getOption("interactive")!=null) &&  getOption("interactive"))
					)
			interactivemode();
			return;
		}
		List<String> t = new ArrayList<String>();
		if (optionsparamList!=null)
		for (String s : optionsparamList)
			if (s!=null && !s.isBlank()  && !s.trim().startsWith("//"))
			t.add(s.trim());
		parse(t);
		
	}

	public void parse(Collection<String> optionsparamList) {
		for (String option : optionsparamList) 
		{
			if(option.startsWith("//"))
			{}
			else
			if (getMultiConfigFile( option)!=null)
			{	
				for(String line:JavaUtils.read(getMultiConfigFile( option)).split("\\n+"))
				{
					run(line.split("\\n"));
				}
				System.exit(0);
				/***/
			}
			else if (getConfigFile( option)!=null)
			{	
				parse(JavaUtils.read(getConfigFile( option)).split("\\n+"));
				/***/
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
	

	


	private void run(String[] args) {
		  Class cls = this.main;
	      System.out.println("Run : "+cls.getName());
	      

	      Method meth;
		try {
			meth = cls.getMethod("main", String[].class);
			  String[] params = args; // init params accordingly
		      meth.invoke(null, (Object) params); // static method doesn't have an instance
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		for(String key:this.parameter.keySet())
			if (parameter.get(key)==null  || "".equals(parameter.get(key).trim()))
			{
				System.out.println("bad command line : you haven't provide value for " + key);
				System.out.println(help());
				System.out.println("your config is :\n");
				System.out.println(displayConfig());
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
		tmp+=getBriefHelp();
		
		
		// init(optionsavailablehelp.keySet());
		for (String argmnt : optionsavailablehelp.keySet())
		
			if (getHelper(argmnt) != null)// if some help text :
					tmp += " " + argmnt.substring(1);
		
		tmp += "Usage : exe [(qualifier)options]  [(qualifier)options[=value]] parameter[=value] Argument1 argumentn\r\n";
		tmp += "  or  : exe @file.config\r\n";
	
		tmp+=getDescriptionHelp();
		
		tmp+="Where exe="+executable()+"\r\n";
		
		for (String argmnt : optionsavailablehelp.keySet())
			if (getQualifier(argmnt) == null)
				if (getHelper(argmnt) == null)
						if (getValueParam(argmnt) == null)
					tmp += "\t" + argmnt;
		tmp += "\n\n";
		int i = 0;
		
		String tmp2 =  "\t* qualifier :\n";
		tmp2 += "\t\t'--'\t: true/enable : use the option \n";
		tmp2 += "\t\t'-'\t: false/disable this option\n";
		tmp2 += "\t\t'+'\t: true/enable this option\n";
		tmp2 += "\t* options list:\n";
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
		 tmp2 = "\t* parameter list:\n";
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
		tmp += "\t\tValues can be according to the need/context :\n"
				+ "\t\t- a value : 'toto','true','55',... : a simple string without space\n"
				+ "\t\t- a list : '[toto,titi]' : a string without space starting with [ finsishing with ] and element are separated by ','\n"
				+ "\t\t- a map : '{key=value,toto=1,titi=alpha}' : a string without space starting with { finsishing with } and element are separated by a ',' key is followed by a '=' and the value\n"
				+ "";
		
		 tmp2 = "\t* Argument list : mandatory items in the good order:\n";
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
		tmp += "\tConfiguration file:\n\t\t@file.config : where 'file.config' is the path to a text file that contains arguments,options and qualifier";
		
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
		// argument : "" "string"
		optionparam.put("Action"," list of action: \n\t- merge : merge several file\n\t- filtercol : perform filtering in file process\n\t- FILTERCOL : perform filtering in RAM process\n\t- header : extrat header");
		optionparam.put("Dir","path to file");
		optionparam.put("outputfile","file to save");
		// option"+" "-" "--"
		optionparam.put("--help"," this help");
		myargs=new ArgsParser(ArgsParser.class,optionparam);
		// parse it
		myargs.parse(args);
		if (!myargs.check())
			if(!myargs.interactivemode())
				{
				System.out.println(myargs.help());
				System.exit(-1);
				}
	
		//use it
		myargs.getArgument(1);
		myargs.getParam("Separator");
		
		//...
		//...
		
		
		myargs.SaveConfigFile();
		
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
	@SuppressWarnings("rawtypes")
	Class main;
	/**
	 * @return the main
	 */
	public Class getClassMain() {
		return main;
	}

	public String toConfigFile() 
	{return toConfigFile("");
	}
public String toConfigFile(String filename) {
	
	String tmp = "";
	// init(optionsavailablehelp.keySet());
	if(filename==null)
		filename="";
	tmp+="// "+executable()+" @"+filename+"\r\n";
	//tmp+="// @file.cmd"+"\r\n";
	
//	int i=1;
	if(arguments.size()!=0)
	{
	for (String argmnt : arguments)
		
				tmp += "\t\t"+ argmnt+"\n";
	//tmp += "\n\n";
	}
	
	if(options.keySet().size()!=0)
	{
	
	for (String option : options.keySet()) {
		if(getOption(option))
			tmp += "\t\t+"+option+"\n";
		else
			tmp += "\t\t-"+option+"\n";
		
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
private String executable() {
	return "java  -cp JavaTool.jar "+main.getCanonicalName();
	
}

/** do a back a the command line needed to reproduce the same result
 * you can relaod it by doing 'java  -cp JavaTool.jar class.name @thefilename'
 * */
public void SaveConfigFile(String thefilename) {

	JavaUtils.saveAs(thefilename, toConfigFile(thefilename));
	
}
public void SaveConfigFile() 
{
	 SaveConfigFile(".jconfig"); 
	}
/** build a array of String for a main() function
 * */
public String[] toArgs() {
String tmp="";
	if(arguments.size()!=0)
	{
	for (String argmnt : arguments)
		
				tmp += ""+ argmnt.trim()+"\n";
	}
	
	if(options.keySet().size()!=0)
	{
	
	for (String option : options.keySet()) {
		if(getOption(option))
			tmp += "+"+option.trim()+"\n";
		else
			tmp += "-"+option.trim()+"\n";
		
	}
	}
	if(parameter.keySet().size()!=0)
	{
	for (String param : parameter.keySet()) {
		tmp +=  param.trim() + "=" + parameter.get(param).trim() + "\n";
	}
	}
	
	return tmp.split("\n");
}

public int getParamInt(String key) {
	
	return Integer.parseInt(getParam(key));
}
public long getParamLong(String key) {
	
	return Long.parseLong(getParam(key));
}
public int getParamIntHex(String key) {
	String data=getParam(key);
	if (data.startsWith("0x"))
		data=data.substring(2);
	return Integer.parseInt(data,16);
}
public Byte[] getParamByteArray(String key)
{
	key=getParam(key);
if (key.startsWith("0x"))
	key=key.substring(2);
if (key.startsWith("none") || key.isBlank())
	return new Byte[0];

	Byte b[]=new Byte[key.length()/2];
	for(int i=0;i<b.length;i++)
	{
		b[i]=(byte) (Integer.parseInt(key.substring(i*2,i*2+2),16) & 0xff);
	}
	return b;
}	

public double getParamDouble(String key) {
	
	return Double.parseDouble(getParam(key));
}
}
