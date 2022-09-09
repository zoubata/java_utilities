/**
 * 
 */
package com.zoubworld.utils;
import static java.util.stream.Collectors.toMap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;

import SevenZip.Compression.LZMA.Encoder;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import print.color.Ansi.BColor;
import print.color.Ansi.FColor;
import print.color.ColoredPrinterNIX;
import print.color.ColoredPrinterTemplate;
import print.color.ColoredPrinterWIN;
/**
 * @author Pierre Valleau
 *
 */
public final class JavaUtils {
	static public int debug_level = 0;
	static public String RegEx_EndOfLine = "\\n";

	private static ColoredPrinterTemplate getPrinter(FColor frontColor, BColor backColor) {

		String os = System.getProperty("os.name");
		// System.out.println("DETECTED OS: " + os);

		if (os.toLowerCase().startsWith("win")) {
			return new ColoredPrinterWIN.Builder(1, false).foreground(frontColor).background(backColor).build();
		} else {
			return new ColoredPrinterNIX.Builder(1, false).foreground(frontColor).background(backColor).build();
		}

	}
	public static String asSortedString(String s,String separator)
	{
		String t[]=s.split(separator);
		List<String> l=new ArrayList<String>();
		for(String e:t)
		l.add(e);
		l=asSortedList(l);
		String ss="";
		for(String e:l)
			ss+=e+separator;
		return ss;
	}
	
	/** convert a Set on a List sorted and remove null object
	 * */
	public static
	<T extends Comparable<? super T>> List<T> asSortedSet(Collection<T> c) {
	  List<T> list = new ArrayList<T>();
	  list.addAll(c);
	  while(list.contains(null))
	  {list.remove(null);
	  System.err.println("Warning null object inside collection dropped !");}
	  
	  Collections.sort(list);
	  return list;
	}
	
	public static <T,Number extends Comparable<Number>> Map<T, Number> SortMapByValue(Map<T, Number> map) {
		Map<T, Number> sorted = map
		        .entrySet()
		        .stream()
		        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
		        .collect(
		            toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
		                LinkedHashMap::new));
		return sorted;
	}
	public static <Object extends Comparable<Object>,V> Map<Object, V> SortMapByKey(Map<Object, V> map) {
		Map<Object, V> sorted = map
		        .entrySet()
		        .stream()
		        .sorted()
		        .collect(
		            toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
		                LinkedHashMap::new));
		return sorted;
	}

	public static
	<T extends Comparable<? super T>> List<T> asSortedList(List<T> c) {
	  List<T> list = new ArrayList<T>();
	  list.addAll(c);
	  Collections.sort(list);
	  return list;
	}
	static int DEBUG_Main = 1;
	static int DEBUG_Value = 2;
	static int DEBUG_Comment = 4;
	static int DEBUG_Detail = 8;
	static int DEBUG_loop = 16;

	/**
	 * level : how deep we dump comment, naturefilter :
	 */
	static public void debug(int level, int naturefilter, String comment) {
		switch (level) {
		case 0:
			debug(comment);
			break;
		default:
			debug(comment);

		}
	}

	static public void debug(String comment) {
		Date date = Calendar.getInstance().getTime();
		DateFormat writeFormat = new SimpleDateFormat(" HH:mm:ss.Ms");
		System.out.println("[" + writeFormat.format(date) + "]" + comment);
	}

	static public void newDir(String location, String NewDir) {
		File theDir ;
		if (location==null || location.equals(""))
			theDir = new File( NewDir);

		else
			theDir = new File(location + File.separator + NewDir);

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			System.out.println("creating directory: " + theDir.getName());
			String aDir=theDir.getAbsolutePath().substring(0,theDir.getAbsolutePath().lastIndexOf(File.separator));
			newDir("", aDir); 
			boolean result = false;

			try {
				theDir.mkdir();
				result = true;
			} catch (SecurityException se) {
				// handle it
			}
			if (result) {
				System.out.println("DIR created");
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * System.out.print("totot no color");
		 * System.out.println("\033[0m BLABLA \033[0m\n"); System.out.println((char)27 +
		 * "[31mThis text would show up red" + (char)27 + "[0m");
		 * 
		 * // for i in {1..255}; do printf "\e[0m$i: \e[38;05;${i}mCOLOR TEST 123\n";
		 * done; System.out.println((char)27 +
		 * "[34;43mBlue text with yellow background");
		 * 
		 * 
		 * AnsiConsole.systemInstall();
		 * 
		 * System.out.println(ansi().fg(RED).a("Hello World").reset());
		 * System.out.println("My Name is Raman");
		 * 
		 * AnsiConsole.systemUninstall();
		 */

		String command = "cat C:\\Home_Users\\pierre\\CVS_Home\\micros_01\\Jubilee\\Testprogram\\selftest\\test\\softMemBist\\toto";
		executeCommandColor(command);
		ColoredPrinterTemplate cp = getPrinter(FColor.WHITE, BColor.BLUE);
		cp.clear();
		cp.print("Bye!");
	}

	public static void executeCommandColor(String command) {
		/*
		ColoredPrinterTemplate cp = getPrinter(FColor.WHITE, BColor.BLACK);

		// printing according to that format
		// cp.println(cp);
		System.out.println("\t Run : '" + command + "'");
		
		 // cp.setAttribute(Attribute.REVERSE);
		 // cp.println("This is a normal message (with format reversed)."); //reseting
		 // the terminal to its default colors
		 
		cp.clear();
		cp.print(cp.getDateTime(), Attribute.NONE, FColor.CYAN, BColor.BLACK);

		String HighLigth = cp.generateCode(Attribute.NONE, FColor.RED, BColor.BLACK);
		String Error = cp.generateCode(Attribute.NONE, FColor.YELLOW, BColor.BLACK);
		String Warning = cp.generateCode(Attribute.NONE, FColor.CYAN, BColor.BLACK);
		String Normal = cp.generateCode(Attribute.NONE, FColor.WHITE, BColor.BLACK);

		Process p;
		try {

			p = Runtime.getRuntime().exec(command);
			// p.waitFor();

			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			BufferedReader readerErr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			BufferedReader readerIn = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(p.getOutputStream())));
			String line = "";

			while (p.isAlive()) {
				if ((reader.ready())) {
					if ((line = reader.readLine()) != null)
						cp.println(Normal + line);
				}

				if ((readerErr.ready())) {
					if ((line = readerErr.readLine()) != null) {
						//
						 // if ((line.contains("error"))||(line.contains("Error"))) cp.errorPrint(line ,
						 // Attribute.NONE, FColor.CYAN, BColor.BLACK);
						 // 
						 // else if ((line.contains("warning"))||(line.contains("Warning")))
						 // cp.errorPrint(line , Attribute.NONE, FColor.YELLOW, BColor.BLACK); else
						 // cp.errorPrint(line , Attribute.NONE, FColor.RED, BColor.BLACK);
						 //

						line = line.replaceAll("error", HighLigth + "error" + Error);
						line = line.replaceAll("Error", HighLigth + "Error" + Error);
						line = line.replaceAll("Warning", Warning + "Warning" + Error);
						line = line.replaceAll("warning", Warning + "warning" + Error);
						cp.errorPrintln(line, Attribute.NONE, FColor.YELLOW, BColor.BLACK);

					}
				}

				if ((readerIn.ready())) {
					if ((line = readerErr.readLine()) != null)

						out.write(line + "\n");
				}
				delay(20, TimeUnit.MILLISECONDS);// keep other process run an progress eyes can see only 50Hz activity.

			}
			;
			System.out.println("Process no longuer alive");
			p.waitFor();
			System.out.println("...");

			line = "";
			while ((line = reader.readLine()) != null) {

				cp.println(Normal + line);

			}
			cp.clear();
			line = "";
			while ((line = readerErr.readLine()) != null) {
				//
				 // if ((line.contains("error"))||(line.contains("Error"))) cp.errorPrint(line ,
				 // Attribute.NONE, FColor.BLUE, BColor.BLACK); else if
				 // ((line.contains("warning"))||(line.contains("Warning"))) cp.errorPrint(line ,
				 // Attribute.NONE, FColor.YELLOW, BColor.BLACK); else cp.errorPrint(line ,
				 // Attribute.NONE, FColor.RED, BColor.BLACK);
				 //
				line = line.replaceAll("error", HighLigth + "error" + Error);
				line = line.replaceAll("Error", HighLigth + "Error" + Error);
				line = line.replaceAll("Warning", Warning + "Warning" + Error);
				line = line.replaceAll("warning", Warning + "warning" + Error);
				cp.errorPrintln(Error + line);

			}

			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("error on '" + command + "'");
		}

		cp.clear();
*/
	}

	private static void delay(int time, TimeUnit unit) {
		try {
			unit.sleep(time);
			// TimeUnit.NANOSECONDS.sleep(100);
			// TimeUnit.MICROSECONDS.sleep(100);
			// TimeUnit.MILLISECONDS.sleep(20);
			/*
			 * TimeUnit.SECONDS.sleep(100); TimeUnit.MINUTES.sleep(100);
			 * TimeUnit.HOURS.sleep(100); TimeUnit.DAYS.sleep(100);
			 */
		} catch (InterruptedException e) {
			// Handle exception
		}
	}

	public static String executeCommand(String command) {
		return executeCommand(command, null);

	}
	public static String executeCommand(String command, String dir) {
	return executeCommand( command,  dir, null);
	}
/** execute 'command' in 'dir'  and provide 'script' as input of command.
 * */
	public static String executeCommand(String command, String dir, String script) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			if (dir != null)
				p = Runtime.getRuntime().exec(command, null, new File(dir));
			else
				p = Runtime.getRuntime().exec(command);

			System.out.println("\t Run : '" + command + "'" + (dir == null ? "" : "in " + dir));
			
			if (script!=null)
			{
				OutputStream  stdin = p.getOutputStream ();  
				for(String line:script.split("\r\n"))
				{
				  stdin.write(line.getBytes() );  
			      stdin.flush(); 
			    } 
				stdin.close();  
				p.waitFor();
				 /*
			PrintWriter out = new PrintWriter(p.getOutputStream());
			out.print(script);
			out.flush();
			p.waitFor();
			out.close();*/
			}
			else 
				p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));

			line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("error on '" + command + "'");
		}

		return output.toString();

	}

	/**
	 * parse a file and return a list of object from classList
	 */
	public static List<IParsable> ParseFile(List<IParsable> classList, String filename) {
		return ParseFile(classList, new File(filename), MultiLine);
	}

	static String MultiLine = null;

	/**
	 * parse a file and return a list of object from classList
	 * 
	 * if MyMultiLine!=null than line was merge if last sequence is equal to
	 * MyMultiLine
	 */
	public static List<IParsable> ParseFile(List<IParsable> classList, String filename, String MyMultiLine) {

		return ParseFile(classList, new File(filename), MyMultiLine);
	}

	/** convert a string from list.toString() into a list<string>
	 * so string s like that "[pa01,pa02,pa03]"
	 * */
	static public List<String> parseListString(String s)
	{
		if(s==null) 
			return null;
		s=s.trim();
		if (!(s.endsWith("]") && s.startsWith("[")))
			return null;
		s=s.substring(1,s.length()-1);
		List<String> l=new ArrayList<String>();
		for(String e:s.split(","))
			l.add(e.trim());
		return l;
	}
	
	static Pattern pMapList=Pattern.compile("(\\S+=\\[[^\\[^\\]]+\\])+");
	/** convert a string from (Map<String,List<String>>).toString() into a Map string/list(string)
	 * so string s like that "{toto=[pa01,pa02,pa03],titi=[pb11,pb12,pb13]}"
	 * */
	static public Map<String,List<String>> parseMapStringListString(String s)
	{
	//	System.out.println("\t:"+s);
		if (!(s.trim().endsWith("}") && s.trim().startsWith("{")))
			return null;
		Map<String,List<String>> map=new HashMap<String,List<String>> ();
		s=s.substring(1,s.length()-1);
		Matcher m=pMapList.matcher(s);
		while(	m.find())
		{
		//	System.out.println("Found at: "+ m.start()+ " - " + m.end()+s.substring(m.start(),m.end()));
		String ss=s.substring(m.start(),m.end());
		map.put(ss.split("=")[0].trim(),parseListString(ss.split("=")[1]));
		}
		
		return map;		
	}
	static Pattern pMap=Pattern.compile("([a-zA-Z0-9_]+=[^,]+)+");
	/** convert a string from map.toString() into a Map<string,string>
	 * so string s like that "{toto=momo,titi=mimi}"
	 * */
	static public Map<String,String> parseMapStringString(String s)
	{
	//	System.out.println("\t:"+s);
		if (!(s.trim().endsWith("}") && s.trim().startsWith("{")))
			return null;
		Map<String,String> map=new HashMap<String,String>();
		s=s.substring(1,s.length()-1);
		Matcher m=pMap.matcher(s);
		while(	m.find())
		{
		//	System.out.println("Found at: "+ m.start()+ " - " + m.end()+s.substring(m.start(),m.end()));
		String ss=s.substring(m.start(),m.end());
		map.put(ss.split("=")[0],(ss.split("=")[1]));
		}
		
		return map;		
	}
	/**
	 * parse a file and return a list of object from classList
	 */
	public static List<IParsable> ParseFile(List<IParsable> classList, File aFile, String MyMultiLine) {
		if (!(aFile.exists() && aFile.isFile())) {
			System.out.print("\t\tfile not find : '" + aFile.getAbsolutePath() + "'");
			return null;
		}

	//	String s = ("\t\tReading : '" + aFile.getAbsolutePath() + "'");
		String lines = read(aFile);
		if (lines != null)
			if (lines.contains("\r\n"))
				if (!JavaUtils.RegEx_EndOfLine.equals("\\r\\n")) {
					JavaUtils.RegEx_EndOfLine = "\\r\\n";
					System.out.println("Automatic update : JavaUtils.EndOfLine change to " + JavaUtils.RegEx_EndOfLine);
				}

		return ParseFile(classList, lines.split(JavaUtils.RegEx_EndOfLine), MyMultiLine);
	}

	public static List<IParsable> ParseFile(List<IParsable> classList, String[] Filelines, String MyMultiLine) {
		MultiLine = MyMultiLine;
		List<IParsable> listobj = new ArrayList<IParsable>();

		// HashSet<String,String> content;

		// declared here only to make visible to finally clause
		// BufferedReader input = null;
		String line = null; // not declared within while loop

		// use buffering, reading one line at a time
		// FileReader always assumes default encoding is OK!
		// input = new BufferedReader( new FileReader(aFile) );
		int countline = 0;

		if ((debug_level > 1)) {
			for (IParsable myclass : classList) {
				System.out.println("IParsable " + myclass.toString() + "  ");
			}
		}

		while (countline < Filelines.length) {
			line = Filelines[countline];
			countline++;
			if (MultiLine != null) {

				StringBuffer line2 = new StringBuffer("");
				if (line.endsWith(MultiLine))
					line2.append(line.substring(0, line.length() - MultiLine.length()));

				while (line.endsWith(MultiLine)) {

					if (countline < Filelines.length) {
						line = Filelines[countline];
						countline++;
						line2.append(line.substring(0, line.length() - MultiLine.length()));
					}

				}
				line = line2.toString();
			}

			boolean matched = false;
			for (IParsable myclass : classList) {
				Object obj = myclass.Parse(line);
				/*
				 * for(Class myclass:classList) { Method m; try { Class[] cArg = new Class[1];
				 * cArg[0] = String.class;
				 * 
				 * Class c=myclass; m=c.getDeclaredMethod("Parse", cArg); Object t =
				 * c.newInstance(); Object obj= m.invoke(t,line);
				 */
				if (obj != null) {
					listobj.add((IParsable) obj);
					matched = true;
					break;
				}
				/*
				 * } catch (NoSuchMethodException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); } catch (SecurityException e) { // TODO Auto-generated
				 * catch block e.printStackTrace(); } catch (IllegalAccessException e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); } catch
				 * (IllegalArgumentException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); } catch (InvocationTargetException e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); } catch
				 * (InstantiationException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); }
				 */

			}
			if ((debug_level > 0)) {
				if (!matched)
					System.out.println("line " + countline + " not understood : '" + line + "'");
				else
					System.out.println("line " + countline + " MATCHED        : '" + line + "'");
			}
		}
		return listobj;
	}

	/**
	 * parse a file and return a list of chapter from classList
	 */
	public static Map<IParsable, String[]> ExtractChapterFile(List<IParsable> classList, File aFile) {
		if (!(aFile.exists() && aFile.isFile())) {
			System.out.print("\t\tfile not find : '" + aFile.getAbsolutePath() + "'");
			return null;
		}

//		String s = ("\t\tReading : '" + aFile.getAbsolutePath() + "'");
		String lines = read(aFile);
		return ExtractChapterFile(classList, lines.split("\\n"));
	}

	public static Map<IParsable, String[]> ExtractChapterFile(List<IParsable> classList, String[] Filelines) {
		List<IParsable> listobj = new ArrayList<IParsable>();
		Map<IParsable, String[]> map = new HashMap<IParsable, String[]>();
		String line = null; // not declared within while loop

		int countline = 0;
		List<String> chapterlines = new ArrayList<String>();
		Object obj = null;
		Object objold = null;
		while (countline < Filelines.length) {
			// \s*[().*)]\s*
			// BIN CODE KEY:
			// P O S T B A K E
			// | Handler 'a'
			line = Filelines[countline];
			countline++;
			boolean matched = false;
			obj = null;
			for (IParsable myclass : classList) {
				obj = myclass.Parse(line);

				if (obj != null) {
					listobj.add((IParsable) obj);
					matched = true;
					break;
				}

			}

			if (matched) {
				map.put(((IParsable) objold), chapterlines.toArray(new String[0]));
				matched = false;
				chapterlines.clear();
				objold = obj;
			} else
				chapterlines.add(line);

		}

		{
			map.put(((IParsable) objold), chapterlines.toArray(new String[0]));

			chapterlines.clear();
		}
		return map;
	}

	public static int intValueOf(BufferedReader input) throws IOException {
		int i = Integer.MAX_VALUE;
		do {
			String line = input.readLine();
			try {
				i = Integer.valueOf(line);
			} catch (java.lang.NumberFormatException e) {
				System.out.println("bad synthaxe, not an int");
			}

		} while (i == Integer.MAX_VALUE);
		return i;
	}

	public static double doubleValueOf(BufferedReader input) throws IOException {
		double i = Double.NaN;
		do {
			try {
				i = Double.valueOf(input.readLine());
			} catch (java.lang.NumberFormatException e) {
				System.out.println("bad synthaxe, not an float");
			}

		} while (i == Double.NaN);
		return i;
	}

	public static int Max(Integer[] ys) {
		if (ys == null)
			return -1;
		if (ys.length == 0)
			return -1;

		int max = ys[0];
		for (int i : ys)
			if (i > max)
				max = i;
		return max;
	}

	public static int Min(Integer[] ys) {
		if (ys == null)
			return -1;
		if (ys.length == 0)
			return -1;

		int min = ys[0];
		for (int i : ys)
			if (i < min)
				min = i;
		return min;
	}

	public static double Max(Double[] ys) {
		if (ys == null)
			return -1;
		if (ys.length == 0)
			return -1;

		double max = ys[0];
		for (Double i : ys)
			if (i > max)
				max = i;
		return max;
	}

	public static double Min(Double[] ys) {
		if (ys == null)
			return -1;
		if (ys.length == 0)
			return -1;

		double min = ys[0];
		for (Double i : ys)
			if (i < min)
				min = i;
		return min;
	}
	public static void saveAs(String fileName, Collection<String> datatoSave)
	{
		saveAs( fileName,  String.join(",\n", datatoSave));
	}
	static boolean backup=false;
	/** save the datas datatoSave into a file called fileName
	 * it support natively the "xxx.gz" so it automaticaly compress the data.
	 * */
	public static void saveAs(String fileName, String datatoSave) {
		File fileOut;
// in = new GZIPInputStream(in);
		if (fileName != null) {
			fileOut = new File(fileName);
		} else {
			System.err.println("error");
			return;
		}
		String dir=dirOfPath(fileName);
		if (!fileExist(dir))
			mkDir(dir);
		try {
			System.out.println("\t-  :save File As : " + fileOut.getAbsolutePath());
			if (fileOut.exists())
				
					backup(fileOut);
				
	PrintWriter out=null; 
	if(fileName.endsWith(".zip"))
		out= new PrintWriter(new OutputStreamWriter(new ZipArchiveOutputStream(new FileOutputStream(fileOut)), "UTF-8"));
	else if(fileName.endsWith(".bz2"))
		out= new PrintWriter(new OutputStreamWriter(new BZip2CompressorOutputStream(new FileOutputStream(fileOut)), "UTF-8"));
		else if(fileName.endsWith(".gz"))
	out= new PrintWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(fileOut)), "UTF-8"));
	else
		out= new PrintWriter(new FileWriter(fileOut));
		
			out.print(datatoSave);

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);

		}

	}

	private static void backup(File fileOut) {
		if(!backup)
			return;
		String path=fileOut.getAbsolutePath();
		
			
		File f=new File(path);
		if (!path.endsWith(".bak"))
			path+=".bak";
		File f2=new File(path);
		int count=0;
		while(f2.exists())
			f2=new File(path+"."+count++);
			f.renameTo(f2);
		
	}
	/** save information to build the wafer */
	public static void saveAs(String fileName, String datatoSave[]) {
		File fileOut;

		if (fileName != null) {
			fileOut = new File(fileName);
		} else {
			System.err.println("error");
			return;
		}

		try {
			PrintWriter out=null; 
			 
			if(fileName.endsWith(".zip"))
				out= new PrintWriter(new OutputStreamWriter(new ZipArchiveOutputStream(new FileOutputStream(fileOut)), "UTF-8"));
			else if(fileName.endsWith(".bz2"))
				out= new PrintWriter(new OutputStreamWriter(new BZip2CompressorOutputStream(new FileOutputStream(fileOut)), "UTF-8"));
			else if(fileName.endsWith(".gz"))
			out= new PrintWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(fileOut)), "UTF-8"));
			else
				out= new PrintWriter(new FileWriter(fileOut));
			System.out.println("\t-  :save File As : " + fileOut.getAbsolutePath());
			for (String data : datatoSave)
				out.println(data);

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);

		}

	}

	/** read a 'small file' and return it into a string
	 * @see read(File filein)
	 *  */
	public static String read(String fileName) {
		File filein;
		if (fileName != null) {
			filein = new File(fileName);
		} else {
			System.err.println("error");
			return null;
		}
		
		return read(filein);
	}
	/** read a 'small file' and return it into a string
	 * it support yyy.xxx.gz files natively as a simple read of yy.xxx
	 *  */
	public static String read(File filein) {
		 if (!filein.exists())
		 {
			 System.err.println("\t-  :read File : " + filein.getAbsolutePath()+"fails, file doensn't exist");
				
			 return null;
		 }
		 if (!filein.isFile())
		 {
			 System.err.println("\t-  :read File : " + filein.getAbsolutePath()+"fails, it isn't a file");
				
			 return null;
		 }
		 System.out.println("\t-  :read File : " + filein.getAbsolutePath());
			
		/*
		 * StringBuilder sb = new StringBuilder();
		 * 
		 * 
		 * try { BufferedReader br = new BufferedReader(new FileReader(filein));
		 * 
		 * System.out.println("\t-  :read File : "+filein.getAbsolutePath());
		 * 
		 * String line = br.readLine();
		 * 
		 * while (line != null) { sb.append(line); sb.append(System.lineSeparator());
		 * line = br.readLine(); }
		 * 
		 * 
		 * br.close(); } catch (IOException e) { e.printStackTrace(); System.exit(-1);
		 * 
		 * } return sb.toString();
		 */
		
		 if(filein.getAbsolutePath().endsWith(".zip"))
			{
				BufferedInputStream isb;
				try {
					isb = new BufferedInputStream(new FileInputStream(filein));
				
					ZipArchiveInputStream  in = new ZipArchiveInputStream (isb);
				 byte[] encoded = new byte[65536];
			      int noRead;
			      StringBuffer s=new StringBuffer();
			      while ((noRead = in.read(encoded)) != -1) {
			    	  String tmp;
			    	  if (noRead<=0)
			    			  tmp="";
			    	  else
			    		  tmp=new String(encoded, Charset.defaultCharset());
			    	  //adjust the size
			    	  if( (noRead>0) && (noRead<65536))
			    			  tmp=tmp.substring(0, noRead);
			    	  
			    	s.append(tmp  );
			        
			      }
			      in.close();
			      isb.close();
			      return s.toString();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return null;
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
			else if(filein.getAbsolutePath().endsWith(".bz2"))
			{
				BufferedInputStream isb;
				try {
					isb = new BufferedInputStream(new FileInputStream(filein));
				
					BZip2CompressorInputStream in = new BZip2CompressorInputStream(isb);
				 byte[] encoded = new byte[65536];
			      int noRead;
			      StringBuffer s=new StringBuffer();
			      while ((noRead = in.read(encoded)) != -1) {
			    	  String tmp;
			    	  if (noRead<=0)
			    			  tmp="";
			    	  else
			    		  tmp=new String(encoded, Charset.defaultCharset());
			    	  //adjust the size
			    	  if( (noRead>0) && (noRead<65536))
			    			  tmp=tmp.substring(0, noRead);
			    	  
			    	s.append(tmp  );
			        
			      }
			      in.close();
			      return s.toString();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return null;
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
			else if(filein.getAbsolutePath().endsWith(".gz"))
		{
			BufferedInputStream isb;
			try {
				isb = new BufferedInputStream(new FileInputStream(filein));
			
			GZIPInputStream in = new GZIPInputStream(isb);
			 byte[] encoded = new byte[65536];
		      int noRead;
		      StringBuffer s=new StringBuffer();
		      while ((noRead = in.read(encoded)) != -1) {
		    	  String tmp;
		    	  if (noRead<=0)
		    			  tmp="";
		    	  else
		    		  tmp=new String(encoded, Charset.defaultCharset());
		    	  //adjust the size
		    	  if( (noRead>0) && (noRead<65536))
		    			  tmp=tmp.substring(0, noRead);
		    	  
		    	s.append(tmp  );
		        
		      }
		      in.close();
		      return s.toString();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		else
		{
		byte[] encoded;
		try {
			encoded = Files.readAllBytes(filein.toPath());
			return new String(encoded, Charset.defaultCharset());
		} catch (FileNotFoundException e) {

			e.printStackTrace();
			return null;
		}
		catch (IOException e) {

			e.printStackTrace();
			return null;
		}}

		
	}

	/** save information to build the wafer */
	public static void AppendAs(String fileName, String datatoSave) {
		File fileOut;

		if (fileName != null) {
			fileOut = new File(fileName);
		} else {
			System.err.println("error");
			return;
		}

		try {
			PrintWriter out = new PrintWriter(new FileWriter(fileOut, true));

			System.out.println("\t-  :Append File As : " + fileOut.getAbsolutePath());

			out.print(datatoSave);

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);

		}

	}

	public static void JavaexecuteCommand(String className, String cmdlineparams) {
		try {
			Class<?> cls = Class.forName(className);
			Method meth = cls.getMethod("main", String[].class);
			String[] params = cmdlineparams.split("\\s+"); // init params accordingly
			meth.invoke(null, (Object) params); // static method doesn't have an instance
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
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

	// for lists
	public static <T, U> List<U> convertList(List<T> from, Function<T, U> func) {
		return from.stream().map(func).collect(Collectors.toList());
	}

	// for arrays
	public static <T, U> U[] convertArray(T[] from, Function<T, U> func, IntFunction<U[]> generator) {
		return Arrays.stream(from).map(func).toArray(generator);
	}

	/**
	 * example
	 * 
	 * //for lists List<String> stringList = Arrays.asList("1","2","3");
	 * List<Integer> integerList = convertList(stringList, s ->
	 * Integer.parseInt(s));
	 * 
	 * //for arrays String[] stringArr = {"1","2","3"}; Double[] doubleArr =
	 * convertArray(stringArr, Double::parseDouble, Double[]::new);
	 */

	/**
	 * return the string set of file
	 */
	static public Set<String> listFileNames(String dir, String filterstring, boolean onlyDir, boolean onlyFile)
	{
		return listFileNames( dir,  filterstring,null,  onlyDir,  onlyFile,  false) ;
	}
	static public Set<String> listFileNames(String dir, String filterstring, boolean onlyDir, boolean onlyFile, boolean recursive)
	{
		return listFileNames( dir,  filterstring,null,  onlyDir,  onlyFile,  recursive) ;
	}
	static public Set<String> listFileNames(String dir, String filterstring,String extention, boolean onlyDir, boolean onlyFile, boolean recursive) {
				Set<String> setupFileNames = new HashSet<String>();
		if (!dir.endsWith(File.separator))
			dir+=File.separator;
		File f = new File(dir);
		String[] sa = f.list(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				boolean result = true;// filename.endsWith(Extention);
				File f = new File(dir.getAbsoluteFile() + File.separator + filename);
				if (onlyDir)
					result &= (f.isDirectory());
				if (onlyFile)
					result &= f.isFile();
				if(filterstring!=null)
					result &= filename.contains(filterstring);
				if(extention!=null)
				result &= filename.endsWith(extention);
				
				// TODO Auto-generated method stub
				return result;
			}
		});
		if (sa == null) {
		//	System.out.println("Error on file in '" + dir + "'");
		} else if (sa.length == 0) {
		//	System.out.println("No  file in '" + dir + "'");
		} else

			for (String s : sa) {
				setupFileNames.add(s);

			}
		if(recursive)
		{
			Set<String> dirs=listFileNames( dir,  "", true, false, false);
			for(String ldir:dirs)
			{	Set<String> ls=listFileNames( dir+ldir+File.separator,  filterstring,extention, onlyDir, onlyFile, recursive);
			for(String fs:ls)
			setupFileNames.add(ldir+File.separator+fs);
			}
		}
		return (setupFileNames);
	}

	
	public static void unzip(String zipFilePath, String destDir) {
		File dir = new File(destDir);
		JavaUtils.debug("\tunzip file " + zipFilePath + " to " + destDir);
		// create output directory if it doesn't exist
		if (!dir.exists())
			dir.mkdirs();
		FileInputStream fis;
		// buffer for read and write data to file
		byte[] buffer = new byte[1024];
		try {
			fis = new FileInputStream(zipFilePath);
			ZipInputStream zis = new ZipInputStream(fis);
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				String fileName = ze.getName();
				File newFile = new File(destDir + File.separator + fileName);
				System.out.println("Unzipping to " + newFile.getAbsolutePath());
				// create directories for sub directories in zip
				new File(newFile.getParent()).mkdirs();
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				// close this ZipEntry
				zis.closeEntry();
				ze = zis.getNextEntry();
			}
			// close last ZipEntry
			zis.closeEntry();
			zis.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JavaUtils.debug("\tzip finished " + zipFilePath);

	}

	public static void zipFiles(String[] srcFiles, String zipFile) {

		/*
		 * String zipFile = "C:/archive.zip";
		 * 
		 * String[] srcFiles = { "C:/srcfile1.txt", "C:/srcfile2.txt",
		 * "C:/srcfile3.txt"};
		 */
		try {

			// create byte buffer
			byte[] buffer = new byte[1024];

			FileOutputStream fos = new FileOutputStream(zipFile);

			ZipOutputStream zos = new ZipOutputStream(fos);

			for (int i = 0; i < srcFiles.length; i++) {

				File srcFile = new File(srcFiles[i]);

				FileInputStream fis = new FileInputStream(srcFile);

				// begin writing a new ZIP entry, positions the stream to the start of the entry
				// data
				ZipEntry ze = new ZipEntry(srcFile.getName());
				// ze.setMethod(ZipEntry.DEFLATED);
				zos.putNextEntry(ze);

				int length;

				while ((length = fis.read(buffer)) > 0) {
					zos.write(buffer, 0, length);
				}

				zos.closeEntry();

				// close the InputStream
				fis.close();

			}

			// close the ZipOutputStream
			zos.close();

		} catch (IOException ioe) {
			System.out.println("Error creating zip file: " + ioe);
		}

	}

	public static void sevenZip(String SourceFilePath, String destZipFile) throws Exception {
		/* Read the input file to be compressed */
		File inputToCompress = new File(SourceFilePath);
		BufferedInputStream inStream = new BufferedInputStream(new java.io.FileInputStream(inputToCompress));
		/* Create output file 7z File */
		File compressedOutput = new File(destZipFile);
		BufferedOutputStream outStream = new BufferedOutputStream(new java.io.FileOutputStream(compressedOutput));
		/* Create LZMA Encoder Object / Write Header Information */
		Encoder encoder = new Encoder();
		encoder.SetAlgorithm(5);// 2
		encoder.SetDictionarySize(1024 * 1024 * 1024);// 8388608
		encoder.SetNumFastBytes(273);// 128
		encoder.SetMatchFinder(1);
		encoder.SetLcLpPb(3, 0, 2);
		encoder.SetEndMarkerMode(false);
		encoder.WriteCoderProperties(outStream);
		long fileSize;
		fileSize = inputToCompress.length();
		for (int i = 0; i < 8; i++) {
			outStream.write((int) (fileSize >>> (8 * i)) & 0xFF);
		}
		/* Write Compressed Data to File */
		encoder.Code(inStream, outStream, -1, -1, null);
		/* Close Output Streams */
		outStream.flush();
		outStream.close();
		inStream.close();
	}

	private static String OS = System.getProperty("os.name").toLowerCase();

	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	public static boolean isMac() {

		return (OS.indexOf("mac") >= 0);

	}

	public static boolean isUnix() {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);

	}

	public static boolean isSolaris() {

		return (OS.indexOf("sunos") >= 0);

	}

	public static void sevenZipDir(String SourceFilePaths, String destZipFile) throws Exception {
		JavaUtils.debug("\t7zip " + destZipFile + " from " + SourceFilePaths);
		/*
		 * Set<String> list=listFileNames(SourceFilePaths, "", false, false); String
		 * listfull[]=new String[list.size()]; int i=0; for(String s:list) {
		 * 
		 * s=SourceFilePaths+File.separator+s; listfull[i++]=(s); }
		 * 
		 * sevenZip(listfull, destZipFile);
		 */
		String cmd = "7z";
		if (isWindows())
			cmd += ".exe";
		JavaUtils.executeCommand(cmd + " a -r -y -t7z -m0=LZMA:d128m:fb273 -mx9 -ms=on -mmt=32 -v1g " + destZipFile
				+ " " + SourceFilePaths);
		JavaUtils.debug("\t7zip finished " + destZipFile);

	}

	public static void sevenZip(String SourceFilePaths[], String destZipFile) throws Exception {
		/* Create output file 7z File */
		File compressedOutput = new File(destZipFile);

		BufferedOutputStream outStream = new BufferedOutputStream(new java.io.FileOutputStream(compressedOutput));
		/* Create LZMA Encoder Object / Write Header Information */
		Encoder encoder = new Encoder();
		encoder.SetAlgorithm(1);// 2
		encoder.SetDictionarySize(1024 * 1024 * 1024);// 8388608
		encoder.SetNumFastBytes(273);// 128
		encoder.SetMatchFinder(32);// 1
		encoder.SetLcLpPb(3, 0, 2);
		encoder.SetEndMarkerMode(false);
		encoder.WriteCoderProperties(outStream);
		long fileSize;

		if (SourceFilePaths != null)
			for (String SourceFilePath : SourceFilePaths) {
				JavaUtils.debug("\t\t- " + SourceFilePath);
				File inputToCompress = new File(SourceFilePath);
				/* Read the input file to be compressed */
				BufferedInputStream inStream = new BufferedInputStream(new java.io.FileInputStream(inputToCompress));
				fileSize = inputToCompress.length();
				for (int i = 0; i < 8; i++) {
					outStream.write((int) (fileSize >>> (8 * i)) & 0xFF);
				}
				/* Write Compressed Data to File */
				encoder.Code(inStream, outStream, -1, -1, null);
				inStream.close();
			}

		/* Close Output Streams */
		outStream.flush();
		outStream.close();

	}

	public static void zipDir(String SourceFilePaths, String destZipFile) throws Exception {
		JavaUtils.debug("\tzip " + destZipFile + " from " + SourceFilePaths);
		Set<String> list = listFileNames(SourceFilePaths, "", false, false);
		String listfull[] = new String[list.size()];
		int i = 0;
		for (String s : list) {

			s = SourceFilePaths + File.separator + s;
			listfull[i++] = (s);
		}

		zipFiles(listfull, destZipFile);
		JavaUtils.debug("\tzip finished " + destZipFile);
	}

	public static void DirDelete(String dir) {

		for (String f : listFileNames(dir, "", false, false)) {

			File myFile = new File(dir+f);
			if (myFile.isDirectory())
				DirDelete(dir + File.separator + f);
			else
				JavaUtils.FileDelete(myFile);
		}
		File mydir = new File(dir);
		JavaUtils.FileDelete(mydir);

	}
	public static void FileDelete(String f) {
		FileDelete(new File(f));
	}
		private static void FileDelete(File myFile) {
				myFile.delete();
		
	}
	/** Fastest way to Copy file in Java
	 * 
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static void fileCopy( File in, File out ) throws IOException
    {
        FileChannel inChannel = new FileInputStream( in ).getChannel();
        FileChannel outChannel = new FileOutputStream( out ).getChannel();
        try
        {
            // Try to change this but this is the number I tried.. for Windows, 64Mb - 32Kb)
            int maxCount = (64 * 1024 * 1024) - (32 * 1024);
            long size = inChannel.size();
            long position = 0;
            while ( position < size )
            {
               position += inChannel.transferTo( position, maxCount, outChannel );
            }
            System.out.println("File Successfully Copied..");
        }
        finally
        {
            if ( inChannel != null )
            {
               inChannel.close();
            }
            if ( outChannel != null )
            {
                outChannel.close();
            }
        }
    }
	/**
	 *   String doc_dir = "\\\\GVSSQLVM\\Lieferscheine\\20011023\\";
      
      */
	public static boolean createCopyOnNetwork(String domain,String username,String password,String src, String dest) throws Exception
	{
	    //FileInputStream in = null;
	    SmbFileOutputStream out = null;
	     BufferedInputStream inBuf = null;
	    try{
	        //jcifs.Config.setProperty("jcifs.smb.client.disablePlainTextPasswords","true");
	        NtlmPasswordAuthentication authentication = new NtlmPasswordAuthentication(domain,username,password); // replace with actual values  
	        SmbFile file = new SmbFile(dest, authentication); // note the different format
	        //in = new FileInputStream(src);
	          inBuf = new BufferedInputStream(new FileInputStream(src));
	        out = (SmbFileOutputStream)file.getOutputStream();
	        byte[] buf = new byte[5242880];
	        int len;
	        while ((len = inBuf.read(buf)) > 0){
	            out.write(buf, 0, len);
	        }
	    }
	    catch(Exception ex)
	    {
	        throw ex;
	    }
	    finally{
	        try{
	            if(inBuf!=null)
	                inBuf.close();
	            if(out!=null)
	                out.close();
	        }
	        catch(Exception ex)
	        {}
	    }
	    System.out.print("\n File copied to destination");
	        return true;
	}
	static long previousfreeMemory = 0;

	/**
	 * take care of memory if not enougth try to free than.
	 */
	public static void freeMemory(Float freefactor/* 0.2 */, Integer freeAbsoluteInByte/* 10MB : 10*1024*1024 */) {

		long freeMemory = Runtime.getRuntime().freeMemory();
		long maxMemory = Runtime.getRuntime().maxMemory();
		long UsedMemory = Runtime.getRuntime().totalMemory();
		if (UsedMemory <= 0)
			UsedMemory = 1;
		if (maxMemory <= 0)
			maxMemory = 1;

		double load = (double) UsedMemory / (double) maxMemory;
		double loadfree = (double) freeMemory / (double) UsedMemory;

		Float loadfactor = (float) 0.0;
		if (freefactor != null)
			loadfactor = 1 - freefactor;

		if (((freefactor != null && loadfactor != null) && ((load > loadfactor) && (loadfree < freefactor)))
				|| ((freeAbsoluteInByte != null) && ((freeAbsoluteInByte > freeMemory))))

		{

			memory();
			System.gc();
			System.out.println("== Cleaning Memory, result : ===");
			memory();

			long newfreeMemory = Runtime.getRuntime().freeMemory();

			long deltamemUsed = previousfreeMemory - freeMemory;
			long deltamemfree = newfreeMemory - freeMemory;
			if (deltamemUsed <= 0)
				deltamemUsed = 1;
			float efficiency = deltamemfree / deltamemUsed;
			synchronized (JavaUtils.class) {
				System.out
						.println("since last run " + deltamemUsed + " used and " + deltamemfree + " memory recovery ");

				previousfreeMemory = freeMemory;
			}

		}
	}

	/**
	 * @param args
	 */
	public static void memory() {
		/* Total number of processors or cores available to the JVM */
		System.out.println("----------------");
		System.out.println("Begin of Memory");

		System.out.println("Available processors (cores): " + Runtime.getRuntime().availableProcessors());

		long freeMemory = Runtime.getRuntime().freeMemory();
		long maxMemory = Runtime.getRuntime().maxMemory();
		long UsedMemory = Runtime.getRuntime().totalMemory();
		double load = (double) UsedMemory / (double) maxMemory;
		double loadfree = (double) freeMemory / (double) UsedMemory;
		/*
		 * Total amount of free memory available to the JVM
		 */
		System.out.println("Free memory (free memory available to the JVM): " + freeMemory + " or "
				+ (freeMemory / 1024 / 1024) + "Mo " + String.format("%3.3f", loadfree) + " %");
		/*
		 * This will return Long.MAX_VALUE if there is no preset limit
		 */
		/*
		 * Maximum amount of memory the JVM will attempt to use
		 */
		System.out.println("Maximum memory(the JVM will attempt to use)  : "
				+ (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory + " or " + (maxMemory / 1024 / 1024) + "Mo"));
		/*
		 * Total memory currently in use by the JVM
		 */
		System.out.println("Total memory used (by the JVM)               : " + Runtime.getRuntime().totalMemory()
				+ " or " + (Runtime.getRuntime().totalMemory() / 1024 / 1024) + "Mo " + String.format("%3.3f", load)
				+ " %");
		final int LIMIT_COUNTER = 1000000;

		/*
		 * //System.out.println("Testing Only for print...");
		 * System.out.println("Testing for Collection inside Loop...");
		 * //System.out.println("Testing for Collection outside Loop...");
		 * //ArrayList<String> arr; for (int i = 0; i < LIMIT_COUNTER; ++i) { //arr =
		 * new ArrayList<String>(); ArrayList<String> arr = new ArrayList<String>();
		 * System.out.println("" + i + ". Occupied(OldFree - currentFree): "+
		 * (freeMemory - Runtime.getRuntime().freeMemory())); }
		 * System.out.println("Occupied At the End: "+ (freeMemory -
		 * Runtime.getRuntime().freeMemory()));
		 */

		System.out.println("End of Memory");
		System.out.println("-------------");

	}

	public static int readAndCount(String filename, String string) {
		String s = JavaUtils.read(filename);
		int count = 0;
		for (int i = 0; i < s.length() - string.length(); i++)
			if (s.substring(i, i + string.length()).equals(string))
				count++;
		return count;
	}

	/** count element and map it on a map (kind of histogram)
	 * */
	public static Map<String, Integer> CountElementInList(List<String> list) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		for (String e : list) {
			if (result.get(e) != null)
				result.put(e, result.get(e) + 1);
			else
				result.put(e, 1);
		}
		return result;
	}

	public static void mkDir(String string) {
		newDir("", string);
		
	}

	public static boolean fileExist(String fileName) {
		File filein = new File(fileName);
		
		return filein.exists();
	}
	static public Set<String> getelementFromFormula(String e2) {
		Set<String> s=new HashSet<String>();
		String t[]=e2.split("[;:\\-\\\\*\\-\\^\\/+?()|]");
		
		for(String e:t)
			if (!e.trim().equals(""))
			s.add(e.trim());		
		return s;
	}

	/** remove doublons object that have the same contains
	 * */
	public static void cleanupDoublons(Set<String> specs) {
		Set<String> nodoubles=removeDoublons(specs);
		specs.clear();
		
		specs.addAll(nodoubles);
		
	}
	public static Set<String> removeDoublons(Set<String> specs) {
		Set<String> 	es=new HashSet<String>();
		/*Set<SpecDoxygenOutput> 	eo=new HashSet();
		eo.addAll(SpecDoxygenOutput.getExisting());
		List<SpecDoxygenOutput> eol=JavaUtils.asSortedSet(eo);*/
		for (String e:specs)
		{
			boolean exist=false;
			for(String e2:es)
				if(e2.trim().equalsIgnoreCase(e.trim()))
					exist=true;
			if (!exist)
					es.add(e);
		}
		
		return es;
	}

	public static Set<String>  getlistofDir(String path) {
		return listFileNames(path, "", true, false);
		
	}
    /** return the folder/ of a full path : folder/file.ext
     * */
	public static String dirOfPath(String filepathname) {
		String s=filepathname.substring(0,filepathname.lastIndexOf(File.separatorChar)+1);
		if (s.equals(""))
			s=filepathname.substring(0,filepathname.lastIndexOf('/')+1);
		return s;
	}
	/** return the 'file' of a full path : folder/file.ext
     * */
	public static String fileWithoutExtOfPath(String filepathname) {
		String s=fileWithExtOfPath( filepathname);
		if(s.contains("."))
		s=s.substring(0, s.lastIndexOf('.'));
		return s;
		
	}
	/** return the 'file.ext' of a full path : folder/file.ext
     * */
	public static String fileWithExtOfPath(String filepathname) {
		String s=filepathname.substring(filepathname.lastIndexOf(File.separatorChar)+1,filepathname.length());
		return s;
		
	}	
	/** return the 'file.ext' of a full path : folder/file.ext
     * */
	public static String ExtensionOfPath(String filepathname) {
		String s=fileWithExtOfPath(filepathname);
		s=s.substring(s.lastIndexOf('.')+1,s.length());
		return s;
		
	}
	/** split a list on a list of sub list like String.split() but
	 * note that sub list keep separator element at end
	 * */
	public static <T> List<List<T>> split(List<T> ls, T separator) {
		int toIndex=0;
		int old=0;
		List<List<T>>  list=new ArrayList<List<T>>();
		while(toIndex<ls.size())
		{
			if (ls.get(toIndex).equals(separator))
				{ list.add(ls.subList(old, toIndex+1));old=toIndex+1;}
			toIndex++;
			}
		if (old<ls.size())
		list.add(ls.subList(old, ls.size()-1));
		
		return list;
	}

	
	/** split input into a list of string based on regexp match
	 * */
	public static List<String> stringSplit(String input, String regexp) {
		List<String> l=new ArrayList<String>();
		Pattern p= Pattern.compile(regexp);
		Matcher matcher=p.matcher(input);
	//	matcher=p.matcher(input.replaceAll("\n", "\r\n"));
		/*
		 while(matcher.find()) {
			 for(int i=0;i<matcher.groupCount();i++)
	            System.out.println("found"+i+": "  + matcher.group(1)+":"  + matcher.group(i) );}
*/
	//	int count = 0;
//		if (matcher.matches())
			  while(matcher.find()) {
				  l.add(matcher.group(0));
			  }
		return l;
	}
	

/** Arround a double to a number of digit :
 * arround(0.8999999,1)=0.9
 * arround(8999999.0,1)=9000000.0
 * @param roundmode :BigDecimal.ROUND_UP(9.1=>10), BigDecimal.ROUND_DOWN(9.9=>9), BigDecimal.ROUND_HALF_EVEN(9.51=>10; 9.49=>9)
 * 
 *  */
	public static double arround(double value,int size, int roundmode)
	{
		BigDecimal bd2 = new BigDecimal(value);
		int p=(int)Math.log10(Math.abs(bd2.doubleValue()));
		if(p>0)
		{
		bd2 = bd2.scaleByPowerOfTen(-p-1);
		bd2 = bd2.setScale(size, roundmode);
		bd2 = bd2.scaleByPowerOfTen(p+1);
		}
		bd2 = bd2.setScale(size, roundmode);
		return  bd2.doubleValue();
		}

	/** convert a string :
	 * "aA\nbbB\nccC" into " bc\nabc\nABC"
	 * **/
public static String transpose(String s, String separator) {
	String[] tab=s.split(separator);
	int max=0;
	for(String e:tab)
		max=Math.max(max, e.length());
	String[] out=new String[max];
	for(int i=0;i<max;i++)
		out[i]="";
for(String e:tab)
		for(int i=0;i<max;i++)
			if(i>=e.length())
				out[max-1-i]+=" ";
			else
			out[max-1-i]+=""+e.charAt(e.length()-i-1);
	return String.join(separator, out);
}
	/** convert a map into string
	 * */
	public static <T,V> String Format(Map<T, V> m)
	{
		return  "{"+Format(m, "->",",")+"}";
	}
	/** convert a map into string
	 * with specific separator and link between key and values, 
	 * 
	 * */
	public static <T,V> String Format(Map<T, V> m, String link, String separator)
	{
		return  Format(m, link, separator,s->s.toString(),s->s.toString());
	}
	/** convert a map into string 
	 * with specific separator and link between key and values, 
	 * the data display is define by fk for the key and fv for the value
	 * */
public static <T,V> String Format(Map<T, V> m, String link, String separator,Function<T, String> fk,Function<V, String> fv) {
	StringBuffer s=new StringBuffer();
	for(Entry<T, V> e:m.entrySet())
		s.append(fk.apply(e.getKey())+link+fv.apply(e.getValue())+separator);
	return s.toString();

}
	public static String UpperdirOfPath(String dirOfPath) {
		if (dirOfPath.endsWith(File.separator))
			dirOfPath=dirOfPath.substring(0,dirOfPath.length()-1);
		String s=dirOfPath.substring(0,dirOfPath.lastIndexOf(File.separatorChar)+1);
		if (s.equals(""))
			s=dirOfPath.substring(0,dirOfPath.lastIndexOf('/')+1);
		return s;
		
	}
	public static String toString(byte[] byteArray) {
		String s="(";
		
		for(byte b:byteArray)
			s+=b+", ";
			s+=")";
		return s;
	}
}
