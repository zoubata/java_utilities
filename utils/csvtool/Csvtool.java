package com.zoubworld.utils.csvtool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.zoubworld.utils.ArgsParser;
import com.zoubworld.utils.IStringFormater;
import com.zoubworld.utils.JavaUtils;

public class Csvtool {


//	String Extention=".csv";
//	String patternFile=".*";//"^(?!.*SPEC.*).*CP1.*$";
	Map<String,String> optionparam= null;
	ArgsParser args=null;

	public String[] listSetupFileNames(String dir)
    {
		Set<String> setupFileNames=new HashSet<String>();
		System.out.println("reading "+dir+" ...");
    	File f=new File(dir);
    	String[] sa=f.list(new FilenameFilter(){
    		String PatternFile=args.getParam("PatternFile");
    		String Extention=args.getParam("Extention"); 
						public boolean accept(File dir, String filename) {
						  boolean result=filename.endsWith(Extention);
						  result&=filename.matches(PatternFile);
							// TODO Auto-generated method stub
							return result;
						}});
    	if (sa==null)
    	{
    		System.out.println("Error on "+args.getParam("Extention")+" file in '"+dir+"'");
    	}
    	else		    	if (sa.length==0)
    	{
    		System.out.println("No "+args.getParam("Extention")+" file in '"+dir+"'");
    	}
    	else	

    	for(String s:sa)
    	{
    	   setupFileNames.add(s);
    	   
    	}
	return (sa);
    }
	 public void help()
	{
		System.out.println("Synthaxe :");
		System.out.println("Cmd Action ");
		System.out.println("");
		System.out.println("Result : the result file in .CSV");
		System.out.println("");
		System.out.println("Description :");
		System.out.println("=============");
		System.out.println("merge csv file, the result is result.csv");
		System.out.println("Detail :");
		System.out.println("========");	
		System.out.println("\t-merge file and put in 1st colon the file name ");
		System.out.println("Action :");
		System.out.println("========");	
		System.out.println("This action are :");
		System.out.println("\t'merge DirIn' : perform the merge");
		System.out.println("\t\tDirIn : the folder should be a full path description or ended by \\ or / according to OS");
		System.out.println("\t'header FileIn' : display the header with number");
		System.out.println("\t\tFileIn : the file should be a full path description");
		System.out.println("\t'filtercol list' : filter some column, list is the list of number of collum, example: '1,2,3,4,5,6,7,8,9,10,12,45,46'");		
		
		System.out.println("Option :");
		
		System.out.println("========");	
		System.out.println("This option are :");
		System.out.println("\t'-none' :no option");
		
		System.out.println(args.help()); 
		
		System.out.println("your config is :\n");
		System.out.println(args.displayConfig());
		System.out.println("");
		}
public static void main(String[] args) {
		
	
	String pathfile="C:\\temp\\assy\\lotflat\\00000H00_AAAA5\\58S05H05_PMHP5_10_1099.csv";
	ACsvFile acf= readCvsfile(pathfile);
	stranspose(acf);
	saveCvsfile( pathfile+".new",  acf);
	stranspose(acf);
	saveCvsfile( pathfile+".tcsv",  acf);
		/*
		// compilation de la regex
		Pattern p = Pattern.compile("a(b)(c)");
		// cr�ation d'un moteur de recherche
		Matcher m = p.matcher("abc");
		// lancement de la recherche de toutes les occurrences
		boolean b = m.find();
		// si recherche fructueuse
		if(b) {
		    // pour chaque groupe
		    for(int i=0; i <= m.groupCount(); i++) {
		        // affichage de la sous-cha�ne captur�e
		        System.out.println("Groupe " + i + " : " + m.group(i));
		    }
		}

		*/
	//filtercol U:\SVN_Home\svnavr32\ATSAM0+\Exodus\trunk\validation\661A1-7003-scan\WaferSort\result4.csv 1,2,3,4,5,6,7,8,9,10,11,43,44,45,46
	// merge U:\SVN_Home\svnavr32\ATSAM0+\Exodus\trunk\validation\661A1-7003-scan\WaferSort
	/*
	Csvtool mc=null;
	mc=new Csvtool();
	mc.args.parse(args);
	mc.args.check();
		String	Dir=mc.args.getArgument(2);			
		
		
		

		if ("merge".equalsIgnoreCase(mc.args.getArgument(1)))

			mc.read(mc.args.getArgument(3),Dir);
		else if ("header".equalsIgnoreCase(mc.args.getArgument(1)))

			mc.header(Dir);
		else if ("FILTERCOL".equals(mc.args.getArgument(1)))

			mc.filtercol2(Dir,mc.args.getArgument(3), mc.args.getParam("filtercol"));
		else if ("filtercol".equals(mc.args.getArgument(1)))

			mc.filtercol(Dir,mc.args.getArgument(3), mc.args.getParam("filtercol"));
		else if ("STREAM".equals(mc.args.getArgument(1)))

			mc.streamProcessing(mc.args.getArgument(2),mc.args.getArgument(3));
		
		
		
		else
			mc.help();
			/*
		for(int i=0;i<args.length;i++)
		{
		if (args[i].equalsIgnoreCase("-splitRun"));
		da.doSplitRun=true;
		if (args[i].equalsIgnoreCase("-SplitTestBlock"));
		da.doSplitTestBlock=true;
		}
		da.readFile();		
		da.writeFile(result);*/
	}

public static List<String[]> fileToArray(String filename, String separator)
{
File f=new File(filename);
return fileToArray(f,separator);

}
public static void FilterRowKeep(List<String[]> list, String colunm,Pattern  p)
{
	String[] header=list.get(0);
	int index=-1;
	for(int i=1; i<header.length;i++)
		if (header[i].equals(colunm))
			index=i;	
	if(index>=0)
	for(int i=list.size()-1; i>0;i--)		
	{
		String line[]=list.get(i);
		if (line.length>index)
		{
		//if(line[index].trim().equals(value.trim()))
		if((p.matcher(line[index].trim())).find())
		{
				
		}
		else
		{
			list.remove(i);
		}
		}
		else
		{
			list.remove(i);
		}
	}
	else
		System.err.println("Warning '"+colunm+"' not find !" );
}
public static void ReplaceColunm(List<String[]> list, String colunm,IStringFormater replacer)
{	
	String[] header=list.get(0);
	int index=-1;
	for(int i=1; i<header.length;i++)
		if (header[i].equals(colunm))
			index=i;	

	if(index>=0)
	for(int i=list.size()-1; i>0;i--)
	{
		String line[]=list.get(i);	
		if(line.length>index)
		line[index]=replacer.convert(line[index]);		
	}	
	else
		System.err.println("Warning '"+colunm+"' not find !" );
}

/** it keep only row where "colunm" is equal to "value"
 * 
 * */
public static void FilterRowKeep(List<String[]> list, String colunm,String value)
{	
	String val=value.replace((CharSequence) ".", (CharSequence) "\\.").replace((CharSequence) "\\", (CharSequence) "\\\\");	
	Pattern pi=Pattern.compile("^"+val+"$");
	FilterRowKeep( list,  colunm,pi );
}


public static void MapToStream(PrintStream out, Map<String,List<String>> map)
{
	for(String key: map.keySet())
	{
		List<String> list=map.get(key);
		String tab[]=(String[])list.toArray();	
	    out.println(key+" : " + String.join(",",list));
	}
}

public static void ArrayToStream(PrintStream out, List<String[]> array)
{
	for(String tab[]: array)
	{
		out.println( String.join(",",tab));
	}
}

public static  PrintStream FileToStream(String filename)
{
	File fo=new File(filename);
	PrintStream out=null;
	try {
		out = new PrintStream(fo);
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
	return out;
}

/** extract a map from an array, the colunm key become the key of the map and colunm value become the list of value
 * */
public static Map<String,List<String>> ExtractMapFromArray(List<String[]> list,String key,String value)
{
	 Map<String,List<String>> map=new  HashMap<String,List<String>>();
	 String[] header=list.get(0);
	 
	 int indexkey=-1;
		for(int i=1; i<header.length;i++)
			if (header[i].equals(key))
				indexkey=i;	
		int indexvalue=-1;
		for(int i=1; i<header.length;i++)
			if (header[i].equals(value))
			indexvalue=i;
		if ((indexvalue==-1)|| (indexkey==-1))
		{
			System.err.println("indexkey("+key+")="+indexkey+" indexvalue("+value+")=" +indexvalue);
			return map;
		}
		for(int i=1; i<list.size();i++)
		
		{
			String line[]=list.get(i);
			if(line.length>indexkey)
					{
			List<String> valuelist=map.get(line[indexkey]);
			if (valuelist==null)
					 valuelist=new ArrayList<String>();
			valuelist.add(line[indexvalue]);
			map.put(line[indexkey],valuelist);
			}
		}	
		return map;	 
}
/** it keeps colunm listed on "colunmlist" 
 * if a colunm is missing, it will be remove 
 * */
public static List<String[]> ExtractCollunm(List<String[]> array, String[] colunmlist)
{
	String[] header=array.get(0);
	int count=0;
	for (String col: colunmlist)
	{
	for(int i=1; i<header.length;i++)
		if (header[i].equals(col))
		{count++;}
		else header[i]=null;				
	}
	List<String[]> newlist=new ArrayList<String[]>(array.size());
	int index=0;
	
	for(int i=0; i<array.size();i++)
	{
		String line[]=array.get(i);
		String newline[]=new String[count];
		index=0;
		for(int j=0; j<header.length;j++)
			if (header[j]!=null) 
				newline[index++]=line[j];
		
		newlist.add(newline);		
		
	}	
	return newlist;
}
/** it keeps colunm listed on "colunmlist" 
 * if a colunm is missing, it will be fill with empty data 
 * */
public static List<String[]> ExtractColunmEmpty(List<String[]> array, String[] colunmlist)
{
	String[] header=array.get(0);
	Integer[] newToOldIndex=new Integer[colunmlist.length];
	System.out.println("Extract colunm");
	
/*
	for (int j=0;j< colunmlist.length;j++)
	{
		newToOldIndex[j]=-1;
	for(int i=0; i<header.length;i++)
	{
		String col=colunmlist[j].trim();
		String hdr=header[i];
		if ((j==12))
		{
			if ((j==12))
			System.out.println("");
		}
		if (hdr.trim().matches(col.replace('(', '.').replace(')', '.')))
		{
			newToOldIndex[j]=i;
			i=header.length;
		}
		}
				
	}*/
	for (int j=0;j< colunmlist.length;j++)	
	newToOldIndex[j]=-1;
	for (int j=0;j< colunmlist.length;j++)
	{
		for(int i=0; i<header.length;i++)
		{
			String col=colunmlist[j].trim();
			String hdr=header[i];
			if (hdr.trim().matches(col.replace('(', '.').replace(')', '.')))
			{
				newToOldIndex[j]=i;				
			}
		}
		
	}
	List<String[]> newlist=new ArrayList<String[]>(array.size());

	
	for(int i=0; i<array.size();i++)
	{
		String line[]=array.get(i);
		String newline[]=new String[colunmlist.length];

		for(int j=0; j<colunmlist.length;j++)
		{
			int index=newToOldIndex[j];
			if (index>=0)
				newline[j]=line[index];
			else
				newline[j]="";
		}
		newlist.add(newline);		
		
	}	
	return newlist;
}




//####################################################################################
//####################################################################################
//####################################################################################


public Stream<String> fileInToSteam(String filename)
{
	try {
		return Files.lines(Paths.get(filename));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
	}


private List<String[]> SplitColTranspose(String[] arr) {
	// TODO Auto-generated method stub
	List<String[]> la=new ArrayList<String[]>();
	
	if(args.getParam("ColNTranspose").equals(""))
		la.add(arr);
	else
	{
	
		Set<String> list=new HashSet<String>();
		for(String e:args.getParam("ColNTranspose").split(","))
			list.add(e.trim());
		
		String[] arr2=new String[arr.length-list.size()+2];//prepare line model
		int i1=0,i2=0;
		for(i1=0,i2=0;i1<arr.length;i1++)
			if(!list.contains(""+i1))
			arr2[i2++]=arr[i1];
		int ititle=i2;
		int ivalue=i2+1;		
		arr2[ititle]="Title";
		arr2[ivalue]="Value";
		//skip if 1st line :????
		if (false)
			la.add(arr2);
		else
		for(String e:list)// generate several ones
		{
			String[] arr3=new String[arr.length-list.size()+2];//copy
			for(i1=0,i2=0;i1<arr2.length;)
				arr3[i2++]=arr2[i1++];
			arr3[ititle]=Headers[Integer.parseInt(e)];
			arr3[ivalue]=arr[Integer.parseInt(e)];//set value
			la.add(arr3);//add
			}
	}
	return la;
}


private void streamProcessing(String filename, String outputfilename)   {
	
	streamProcessHeader(fileInToSteam(filename),  outputfilename) ;
	streamProcessing(fileInToSteam(filename),  outputfilename) ;
}
private void streamProcessHeader(Stream<String> lines, String outputfilename)   {
Optional<String> OptionalHeaderline =lines.findFirst();
if (OptionalHeaderline.isPresent())
	 Headers=OptionalHeaderline.get().split(args.getParam("Separator"));
/*
FileWriter fw;
fw = new FileWriter(outputfilename);
 writeToFile(fw, l));
//@formatter:on
fw.close();*/
}
String[] Headers;
private void streamProcessing(Stream<String> lines, String outputfilename)   {
    FileWriter fw;
	try {
		fw = new FileWriter(outputfilename);
		
	
     
    //@formatter:off
    lines
    .filter(line -> LineFilter(line))
    .map(line -> complete(line,args.getParam("Separator")))    
    .map(line -> replace(line))// line to array
    .map(line -> (args.getParam("Split").length()==0)?line:line.replaceAll(args.getParam("Split"), args.getParam("Separator")))// split some cell    
    .map(line -> line.split(args.getParam("Separator")))// line to array
    .map(line -> complete(line,args.getParam("Separator")))    
    .map(arr -> ColNDelete(arr))    

		
    .map(arr -> ColNFilter(arr))    
    .map(arr -> Extract(arr))    
    
    // after here number are lost.
    .map(arr -> SplitColTranspose(arr))// split, nd ..
    .flatMap(pList -> pList.stream())    //concatenate into the flux
    .map(arr -> String.join(args.getParam("Separator"), arr))    // array to line
    
    .forEach(l -> writeToFile(fw, l));
    //@formatter:on
    fw.close();
    lines.close();
    
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return;
	}
}

public static ACsvFile readCvsfile(String pathfile)
{
List<String> columns=null;
List<List<String>> values=null;
try(BufferedReader br=Files.newBufferedReader((Path)Paths.get(pathfile))) {
    String firstLine=br.readLine();
    if(firstLine==null) throw new IOException("empty file");
    columns=Arrays.asList(firstLine.split(CellDelimiter));
    values = br.lines()
        .map(line -> Arrays.asList(line.split(CellDelimiter)))
        .collect(Collectors.toList());
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
ACsvFile  acf= new ACsvFile(null);
acf.columns=columns;
acf.values=values;
acf.separator=CellDelimiter;

return acf;
}
/** transpose a table
 * */
public static void stranspose(ACsvFile acf)
{
	List<List<String>> values2=new ArrayList<List<String>>();
	values2.add(acf.columns);
	values2.addAll(acf.values);
	acf.values=new ArrayList<List<String>>();
	int size=acf.columns.size();
	for(int i=0;i<size;i++)
		acf.values.add(new ArrayList<String>());	

	values2.stream().forEachOrdered(
			(List<String> l) -> { for(int i=0;i<size;i++) 
				if(l.size()>i)
										acf.values.get(i).add(l.get(i));
				else 
					acf.values.get(i).add(null);
								}
	);
	acf.columns=acf.values.get(0);
	acf.values.remove(0);
	
}



static String CellDelimiter=",";
public static void saveCvsfile(String pathfile, ACsvFile acf)
{
List<String> columns=acf.columns;
List<List<String>> values=acf.values;

	 PrintWriter pw;
	try {
		pw = new PrintWriter(Files.newBufferedWriter(
			        Paths.get(pathfile)));
	 pw.println(String.join(CellDelimiter, columns));
	 values.stream().map((List<String> line)->String.join(CellDelimiter, line)).forEach(pw::println);
	 pw.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

/*
//A bit hackish
 static Function<String, String[]> mapLineToArray = (line) -> {
String[] p = line.split(", ");
return p;
};

*/
private Pattern piExtract=null;

private String[] Extract(String[] arr) {
	
	if (piExtract==null)
		piExtract=Pattern.compile(args.getParam("Extract"));
	List l=null;


	Matcher m=null;
	int i=0;
	if ((m=(piExtract.matcher(arr[i]))).find())
	{
		String s="";
		for(int j=0;j<m.groupCount();j++)
		{
			s+=m.group(j)+args.getParam("Separator");
		}
		if (s.length()>args.getParam("Separator").length())
		arr[i]=s.substring(0, s.length()-args.getParam("Separator").length());
	}
	return arr;
}

private String  replace(String line) {
	String l=line;
	if (args.isTuple("Replace"))
		return line.replaceAll(args.getTuple("Replace")[0],args.getTuple("Replace")[1]);
	if (args.isMap("Replace"))
	{
		Map<String ,String> m=args.getParamAsMap("Replace");
		for(String key:m.keySet())
			l=l.replaceAll(key,m.get(key));
	}
	return l;
}
private String[] complete(String[] line, String separator) {
	int count=line.length;
	count-=Headers.length;
	if (count<0)
	{
		String[] line2= new String[Headers.length];
		int i=0;
		for(;i<line.length;i++)
			line2[i]=line[i];
		for(;i<line2.length;i++)
			line2[i]="";
		return line2;			
	}
	return line;
}
private String complete(String line, String separator) {
	
	
	int count=line.split(separator).length;
	count-=Headers.length;
	int i;
	for(i=count;i<0;i++)
		line+=separator;
	return line;
}
private Pattern piRowFilter=null;
public boolean LineFilter(String line)
{
	if(args.getParam("LineFilter").equals(".*"))
	return true;
	
	else if (piRowFilter==null)
		piRowFilter=Pattern.compile(args.getParam("LineFilter"));
	
	return ((piRowFilter.matcher(line)).find());		

}

private String[] ColNDelete(String[] arr) {
	if(args.getParam("ColNDelete").equals(""))
	return arr;
	
	Set<String> list=new HashSet<String>();
	for(String e:args.getParam("ColNDelete").split(","))
		list.add(e.trim());
	
	String[] arr2=new String[arr.length-list.size()];
	int i1=0,i2=0;
	for(i1=0,i2=0;i1<arr.length;i1++)
		if(!list.contains(""+i1))
		arr2[i2++]=arr[i1];
		
	return arr2;
}

private String[] ColNFilter(String[] arr) {
	if(args.getParam("ColNFilter").equals(".*"))
	return arr;
	
	Set<String> list=new HashSet<String>();
	for(String e:args.getParam("ColNFilter").split(","))
		list.add(e.trim());
	
	String[] arr2=new String[list.size()];
	int i1=0,i2=0;
	for(i1=0,i2=0;i1<arr.length;i1++)
		if(list.contains(""+i1))
		arr2[i2++]=arr[i1];
		
	return arr2;
}



private void writeToFile(FileWriter fw, String l){
    try {
        fw.write(String.format("%s%n", l));
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}
//####################################################################################
//####################################################################################
//####################################################################################

/** it remove row where "colunm" is equal to "value"
 * 
 * */
public static void FilterRowRemove(List<String[]> list, String colunm,String value)
{
	String val=value.replace((CharSequence) ".", (CharSequence) "\\.").replace((CharSequence) "\\", (CharSequence) "\\\\");
	Pattern pi=Pattern.compile("^"+val+"$");
	FilterRowRemove( list,  colunm,pi );
}
public static void FilterRowRemove(List<String[]> list, String colunm,Pattern p)
	{
	String[] header=list.get(0);
	int index=-1;
	for(int i=1; i<header.length;i++)
		if (header[i].trim().equals(colunm.trim()))
			index=i;	
	if(index>=0)
	{
	for(int i=list.size()-1; i>0;i--)
	{
		String line[]=list.get(i);
	//	if(line[index].trim().equals(value.trim()))
		if(line.length>index)
		if((p.matcher(line[index].trim())).find())
	//	if(line[index].trim().matches(valregExp))
		{
			list.remove(i);
		}
		else
		{
			
		}
	}
	}
	else
		System.err.println("Warning '"+colunm+"' not find !" );
}

public static List<String[]> fileToArray(File aFile, String separator)
{
	List<String[]>  list=new ArrayList<String[]> ();
	  BufferedReader input=null;
	  int	 lineCount=0;
	  int	 abslineCount=0;
	    String line=null;
	  try {
		     //use buffering, reading one line at a time
		     //FileReader always assumes default encoding is OK!
		  input = new BufferedReader( new FileReader(aFile) );
		     System.out.println("Reading "+aFile.getAbsolutePath()+" ...");
		     /*
		     * readLine is a bit quirky :
		     * it returns the content of a line MINUS the newline.
		     * it returns null only for the END of the stream.
		     * it returns an empty String if two newlines appear in a row.
		     */
		   

		     	//    Matcher m = p.matcher(input);
		//	    return m.matches();
		     boolean bnew=false;
		     String name="result";
		   
				 bnew=true;
			
			
		     while (( line = input.readLine()) != null)
		     {
		    	 abslineCount++;
		    	 list.add(line.split(separator)) ;
		    		 
	    	 }
		     if (input!= null) {
		         //flush and close both "input" and its underlying FileReader
		         input.close();
		        
		       }
		    
		   }
		   catch (FileNotFoundException ex) {
			   System.err.println( " : FAIL in file "+aFile.getAbsolutePath() );
		     ex.printStackTrace();
		     
		   }
		   catch (IOException ex){
			   System.err.println( " : FAIL in file "+aFile.getAbsolutePath() );
		     ex.printStackTrace();
		   }
		   catch (Exception ex){
			   System.err.println( "  bad line "+abslineCount+" '"+line+"' in file "+aFile.getAbsolutePath() );
		     ex.printStackTrace();
		   }
		   finally {
		     try {
		    	   if (input!= null) {
				         //flush and close both "input" and its underlying FileReader
				         input.close();
				        
				         
				       }		    	 
				      System.gc(); 
		     }
		     catch (IOException ex) {
		    	 System.err.println(" : FAIL in file "+aFile.getAbsolutePath() );
		       ex.printStackTrace();
		     }
		   }
	
	return list;
}
private void filtercol(String filename,String filenameout,String filter) {

	dir=filename.substring(0, filename.lastIndexOf(File.separatorChar));
	List<String> filterlist;
	header=readHeader( filename);
	String [] headers=header.split(",");
	filterlist=new ArrayList<String>();
	for(String num:filter.split(","))
	{
		int inum=Integer.parseInt(num);
		filterlist.add(headers[inum]);
	}
	
	filtercol( filename,filenameout, filterlist);
}



private void filtercol2(String dir,String filenameout,String filter) {
	
	boolean removeheader=false;
	JavaUtils.saveAs(filenameout, "");//reset the file
	for(String filenamein:listSetupFileNames( dir))
	{
		filtercol2(dir+File.separatorChar+filenamein, filenameout, filter, removeheader);
		removeheader=true;
		JavaUtils.freeMemory((float) 0.2,10*124*1204 );// 20% and 10Mb 
		}
	}
private void filtercol2(String filenamein,String filenameout,String filter,boolean removeheader) {


	
	List<String[]> array=fileToArray(filenamein, (args.getParam("Separator")));
	array=ExtractColunmEmpty(array, args.getParam("filtercol").split(","));	
	File fileOut;
	

	if (filenameout != null) {
		fileOut = new File(filenameout);
	} else {
		System.err.println("error");
		return;
	}

	try {
		PrintWriter out = new PrintWriter(new FileWriter(fileOut, true));
		int count=0;
		System.out.println("\t-  :save File As : "+fileOut.getAbsolutePath());
		for(String[] l:array)
		{count++;
		if(count==1)
		{
			if(removeheader)
		{}
		else //update header
			out.println(String.join(args.getParam("Separator"), args.getParam("filtercol").replaceAll("\\\\s", " ").split(",")));
			}
		else
			out.println(String.join(args.getParam("Separator"), l));
		}
		out.close();
	} catch (IOException e) {
		e.printStackTrace();
		System.exit(-1);

	}
	//filtercol( filename, filterlist);
}

	 private void filtercol(String filename,String filenameout,List<String> filterlist) {
		//fileName=aFilename;

	      System.gc(); 
	
		int abslineCount=0;//absolute line
		File aFile =null;
	
		{
		  aFile = new File(filename);
		  if (!(aFile.exists() && aFile.isFile()))
		   {
		     System.out.println( "\t\tfile not find : '"+filename+"'" );
			   return  ;
		   }
		}
		String s=( "\t\tloading : '"+filename+"'" );
		 System.out.println( s);
		 //HashSet<String,String> content;

	   //declared here only to make visible to finally clause
	   BufferedReader input = null;
	    String line = null; //not declared within while loop
	  try {
	     //use buffering, reading one line at a time
	     //FileReader always assumes default encoding is OK!
	     input = new BufferedReader( new FileReader(aFile) );
	     
	     /*
	     * readLine is a bit quirky :
	     * it returns the content of a line MINUS the newline.
	     * it returns null only for the END of the stream.
	     * it returns an empty String if two newlines appear in a row.
	     */
	   

	     	//    Matcher m = p.matcher(input);
	//	    return m.matches();
	     boolean bnew=false;
	     String name="result";
	   
			 bnew=true;
			 lineCount=0;
			 if (out==null)
		     {
				 //dir+File.separator+name+args.getParam("Extention")
				 fileOut= new File(filenameout);
				 if (fileOut.exists())
				 {
					 fileOut.delete();
					 fileOut= new File(filenameout);
			     }
				 out= new PrintWriter(new FileWriter(fileOut,true));
				 System.out.println("create '"+fileOut.getAbsolutePath()+"'");
				 bnew=true;
				 lineCount=0;
		     }
			
		
	     while (( line = input.readLine()) != null)
	     {
	    	 abslineCount++;
	    	 if ( (abslineCount==1))
	    	 {
	    		
	    			 header=line;
			    	
	    	 }
	    	 if (out!=null)
	    	 {
	    		 String newline="";
	    	
	    			 
	    			 String [] headers=header.split(",");
	    			 String [] lines=line.split(",");
	    			
	    			 for(int i=0;i< headers.length;i++)
	    				 if (filterlist.contains(headers[i]))
	    						 newline+=lines[i]+",";
	    			 
	    		
	    		 
	    		 out.println(newline); 
	    	 }
	    	 
	    	
	    		 
   	 }
	     if (input!= null) {
	         //flush and close both "input" and its underlying FileReader
	         input.close();
	        
	       }
	    
	   }
	   catch (FileNotFoundException ex) {
		   System.err.println( " : FAIL in file "+filename );
	     ex.printStackTrace();
	     
	   }
	   catch (IOException ex){
		   System.err.println( " : FAIL in file "+filename );
	     ex.printStackTrace();
	   }
	   catch (Exception ex){
		   System.err.println( "  bad line "+abslineCount+" '"+line+"' in file "+filename );
	     ex.printStackTrace();
	   }
	   finally {
	     try {
	    	   if (input!= null) {
			         //flush and close both "input" and its underlying FileReader
			         input.close();
			        
			         
			       }		    	 
			      System.gc(); 
	     }
	     catch (IOException ex) {
	    	 System.err.println(" : FAIL in file "+filename );
	       ex.printStackTrace();
	     }
	   }
	  if (out!= null) {
	         //flush and close both "input" and its underlying FileReader
	         out.close();
	        
	         
	       }		
	   System.out.println(s+ " :"+abslineCount+" lines" );
	//   CaractDirectory.memory();

	//   CaractDirectory.memory();
	 
	
}
	private void header(String dir2) {
	// TODO Auto-generated method stub
		String header= readHeader( dir2);
		String [] headers=header.split(args.getParam("Separator"));
		int count=0;
		for(String e:headers)
			System.out.println(""+(count++)+" "+e);
	/*	List<String> hdrs=new ArrayList<String>();
		for(String e:headers)
		hdrs.add(e);
		
		class RecipeCompare implements Comparator<String> {

		    @Override
		    public int compare(String o1, String o2) {
		        // write comparison logic here like below , it's just a sample
		        return o1.compareTo(o2);
		    }
		}
	

		Collections.sort(hdrs,new RecipeCompare());
		for(String e:hdrs)
			System.out.println(e);*/
}
	PrintWriter out=null;
	 File fileOut=null;
	 String header=null;
	 String dir="";
	 public void read(String dir1,String filenameout)
	 {
		 dir=dir1;
		  out=null;
		  fileOut=null;
		  header=null;
		 for(String filename:listSetupFileNames( dir))
		 {
			 readFile(dir+ File.separator+filename,filenameout);
		 }
		 if (out!=null)
			 out.close();
		  System.out.println("result size :"+lineCount+" lines" );
		   System.gc(); 
			
	 }
		/** This function verify there isn't line with only writespace
		 * @param aFilename name of file tested
		 * @return boolean that say if the file is good.
		 */
		int lineCount=0;//line with contain
		private void readFile(String filename,String filenameout) {
			//fileName=aFilename;
			//String name="result";
		     
		      System.gc(); 
		
			int abslineCount=0;//absolute line
			File aFile =null;
		
			{
			  aFile = new File(filename);
			  if (!(aFile.exists() && aFile.isFile()))
			   {
			     System.out.println( "\t\tfile not find : '"+filename+"'" );
				   return  ;
			   }
			}
			String s=( "\t\tloading : '"+filename+"'" );
			 System.out.println( s);
			 //HashSet<String,String> content;

		   //declared here only to make visible to finally clause
		   BufferedReader input = null;
		    String line = null; //not declared within while loop
		  try {
		     //use buffering, reading one line at a time
		     //FileReader always assumes default encoding is OK!
		     input = new BufferedReader( new FileReader(aFile) );
		     
		     /*
		     * readLine is a bit quirky :
		     * it returns the content of a line MINUS the newline.
		     * it returns null only for the END of the stream.
		     * it returns an empty String if two newlines appear in a row.
		     */
		   

		     	//    Matcher m = p.matcher(input);
		//	    return m.matches();
		     boolean bnew=false;
		     if (out==null)
		     {
		    	 //dir+File.separator+name+args.getParam("Extention")
				 fileOut= new File(filenameout);
				 if (fileOut.exists())
				 {
					 fileOut.delete();//dir+File.separator+name+args.getParam("Extention")
					 fileOut= new File(filenameout);
			     }
				 out= new PrintWriter(new FileWriter(fileOut,true));
				 System.out.println("create '"+fileOut.getAbsolutePath()+"'");
				 bnew=true;
				 lineCount=0;
		     }
			
		     while (( line = input.readLine()) != null)
		     {
		    	 abslineCount++;
		    	 if ( (abslineCount==1))
		    	 {
		    		 if (bnew)
		    		 {
		    			 header=line;
				    	 if (out!=null)
				    	 {	 out.println("file,"+line);lineCount++;}
		    		 }
		    		 else
		    		 {
		    			 if (!line.equalsIgnoreCase(header))
		    			 {
		    				 System.out.println("Warning file "+filename+" Skip, not the same header : ");
		    				// System.out.println("\t Fref-  "+header);
		    				// System.out.println("\t Fcur-  "+line);
		    				 
		    				 input.close();
		    				 return;
		    			 }
		    			 
		    		 }
		    	 }
		    	 else
		    	 { if (out!=null)
		    	 { out.println(filename+","+line);lineCount++;}
		    	 }
		    		 
	    	 }
		     if (input!= null) {
		         //flush and close both "input" and its underlying FileReader
		         input.close();
		        
		       }
		    
		   }
		   catch (FileNotFoundException ex) {
			   System.err.println( " : FAIL in file "+filename );
		     ex.printStackTrace();
		     
		   }
		   catch (IOException ex){
			   System.err.println( " : FAIL in file "+filename );
		     ex.printStackTrace();
		   }
		   catch (Exception ex){
			   System.err.println( "  bad line "+abslineCount+" '"+line+"' in file "+filename );
		     ex.printStackTrace();
		   }
		   finally {
		     try {
		    	   if (input!= null) {
				         //flush and close both "input" and its underlying FileReader
				         input.close();
				        
				         
				       }		    	 
				      System.gc(); 
		     }
		     catch (IOException ex) {
		    	 System.err.println(" : FAIL in file "+filename );
		       ex.printStackTrace();
		     }
		   }
		   System.out.println(s+ " :"+abslineCount+" lines" );
		//   CaractDirectory.memory();
		//   CaractDirectory.memory();
		}
		
	public Csvtool() {
		optionparam= new HashMap<String,String>();
		optionparam.put("PatternFile=.*"," regular expression to filter file to parse");
		optionparam.put("Extention=.csv"," Extention to filter file to parse");
		optionparam.put("Separator=,"," separator string on file between field");
		optionparam.put("filtercol=1,2,3,4,5,6,7,8"," if action is filtercol, the list of colum number, if action is FILTERCOL, list of colunm name where space char is replace by \\s");
		optionparam.put("Action"," list of action: \n\t- merge : merge several file\n\t- filtercol : perform filtering in file process\n\t- FILTERCOL : perform filtering in RAM process\n\t- STREAM : perform filtering in stream process(heavy file)\n\t- header : extrat header to get colunm number ");
		optionparam.put("Dir","path to file(s)");
		optionparam.put("outputfile","file to save");
		
		
		//optionparam.put("Separator=,"," separator string on file between field");
		optionparam.put("LineFilter=.*"," (STREAM) regular expression on the string line to filter some row");
		optionparam.put("ColNDelete="," (STREAM) list of Colunm number to delete (can't be use with ColNTranspose or ColNFilter)");
		//optionparam.put("ColDelete="," list of Colunm name to delete");
		//optionparam.put("ColFilter=.*"," list of Colunm name to keep, example \"ColFilter=toto,titi,tu tu,ta\"");
		optionparam.put("ColNFilter=.*"," (STREAM) list of Colunm number(1st=0) to keep, example ColNFilter=0,1,2,3 (can't be use with ColNDelete or ColNTranspose)");
		optionparam.put("ColNTranspose="," (STREAM) list of Colunm number(1st=0) to keep, example ColNFilter=0,1,2,3(can't be use with ColNDelete or ColNFilter)");
		optionparam.put("Replace={}"," (STREAM) Replace=(a,b) will replace all a by b; Replace={{a,b},{a1,b1},{a2,b2}} will replace all a by b, all a1 by b1, all a2 by b2; a can be a regular expression ");
		optionparam.put("Split="," (STREAM) Split=separator Split a cell into several one based on a separator");
		optionparam.put("Extract="," (STREAM) Extract=regEx Split a cell into several one based on a regular expression");
		
		args=new ArgsParser(Csvtool.class,optionparam);		
	}

	private String readHeader(String filename) {
		//fileName=aFilename;

	      System.gc(); 
	
		int abslineCount=0;//absolute line
		File aFile =null;
	
		{
		  aFile = new File(filename);
		  if (!(aFile.exists() && aFile.isFile()))
		   {
		     System.out.println( "\t\tfile not find : '"+filename+"'" );
			   return  null;
		   }
		}
		String s=( "\t\tloading : '"+filename+"'" );
		 System.out.println( s);
		 //HashSet<String,String> content;

	   //declared here only to make visible to finally clause
	   BufferedReader input = null;
	    String line = null; //not declared within while loop
	  try {
	     //use buffering, reading one line at a time
	     //FileReader always assumes default encoding is OK!
	     input = new BufferedReader( new FileReader(aFile) );
	     
	     /*
	     * readLine is a bit quirky :
	     * it returns the content of a line MINUS the newline.
	     * it returns null only for the END of the stream.
	     * it returns an empty String if two newlines appear in a row.
	     */
	   

	     	//    Matcher m = p.matcher(input);
	//	    return m.matches();
	     boolean bnew=false;
	     String name="result";
	   
			 bnew=true;
			 lineCount=0;
	    
		
	     while (( line = input.readLine()) != null)
	     {
	    	 abslineCount++;
	    	 if ( (abslineCount==1))
	    	 {
	    			 header=line;
	    			  input.close();
	    			  return header;
	    	 }
	    	 else
	    	 {
	    	 }
    	 }
	     if (input!= null) {
	         //flush and close both "input" and its underlying FileReader
	         input.close();
	       }
	   }
	   catch (FileNotFoundException ex) {
		   System.err.println( " : FAIL in file "+filename );
	     ex.printStackTrace();
	     
	   }
	   catch (IOException ex){
		   System.err.println( " : FAIL in file "+filename );
	     ex.printStackTrace();
	   }
	   catch (Exception ex){
		   System.err.println( "  bad line "+abslineCount+" '"+line+"' in file "+filename );
	     ex.printStackTrace();
	   }
	   finally {
	     try {
	    	   if (input!= null) {
			         //flush and close both "input" and its underlying FileReader
			         input.close();
			        
			         
			       }		    	 
			      System.gc(); 
	     }
	     catch (IOException ex) {
	    	 System.err.println(" : FAIL in file "+filename );
	       ex.printStackTrace();
	     }
	   }
	  
	   System.out.println(s+ " :"+abslineCount+" lines" );
	//   CaractDirectory.memory();
return header;
	//   CaractDirectory.memory();
	 
	   
	}

}
