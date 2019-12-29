package com.zoubworld.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CPreprocessor {

	public CPreprocessor() {
		super();
		defines = new HashMap<String, String>();
		typedefs = new HashSet<String>();

	}
	
	static public String removeComment(String filecontains)
	{
 		String code=filecontains;
		// code=code.replaceAll("//.*\n", "").replaceAll("\\/\\*.*\\*\\/", "");
		while (code.contains("/*"))
			code=code.substring(0,code.indexOf("/*"))+code.substring(code.indexOf("*/")+2,code.length());
		while (code.contains("//"))
			{
			String code2=code.substring(code.indexOf("//"),code.length());
			code2=code2.substring(code2.indexOf("\n"),code2.length());
			code=code.substring(0,code.indexOf("//"))+code2;
			}
		return code;
	}
	Map<String, String> defines = null;

	/* retur fuse value without comments. */
	public String getValue(String def) {
		String s = null;
		if (defines != null) {
			s = defines.get(def);
			if (s != null) {
				if (s.contains("//"))
					s = s.substring(0, s.indexOf("//"));
				if (s.contains("/*") && (s.contains("*/")))
					s = s.replace(s.substring(s.indexOf("/*"), s.indexOf("*/")+2), "");
			}
		}
		return s;

	}

	public Set<String> getDefines() {
		return defines.keySet();
	}

	public Map<String, String> getDefineMap() {
		return defines;
	}

	public Set<String> getTypedefs() {
		return typedefs;
	}

	public Set<String> getIncludes() {
		return includes;
	}

	Set<String> typedefs = null;
	Set<String> includes = null;

	String key = "";

	public void saveTo(String filename) {

		PrintStream out;
		try {
			out = new PrintStream(new File(filename));
			saveTo(out);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void saveTo(PrintStream out) {

		// ArrayList<String> ldefines=new ArrayList<String>(defines);

		out.println();
		for (String str : getDefines()) {
			String st = str.replaceFirst(key.toUpperCase(), "");
			String Space = " ";
			for (int i = 0; (0 < 40 - st.length()) && (i < 40 - st.length()); i++)
				Space += " ";
			if (st.compareTo(str) != 0) {
				{
					out.println("#if defined( " + str + " )");
					out.println("#define " + st + Space + str);
					out.println("#endif");
					out.println();
				}
			} else
				out.println("//" + str);

		}
		for (String str : typedefs) {
			String st = str.replaceFirst(key.toLowerCase(), "");
			String Space = " ";
			for (int i = 0; (0 < 40 - st.length()) && (i < 40 - st.length()); i++)
				Space += " ";

			if (st.compareTo(str) != 0) {
				out.println("//#if defined( " + str + " )");
				out.println("#define " + st + Space + str);
				out.println("//#endif");
				out.println();
			} else
				out.println("//" + str);
		}

	}

	static private Pattern p1 = Pattern.compile("^\\s*#\\s*define\\s+(\\w+)\\s+(.+)$");

	/**
	 * This function verify there isn't line with only writespace
	 * 
	 * @param aFilename
	 *            name of file tested
	 * @return boolean that say if the file is good.
	 */
	public void readFile(String fullFileName) {
		System.out.println(">readFile( '" + fullFileName + "')");
	//	int lineCount = 0;
		File aFile = null;
		aFile = new File(fullFileName);
		// HashSet<String,String> content;

		if (!(aFile.exists() && aFile.isFile())) {
			System.out.println("file not find : " + fullFileName);
			return;
		}
		// declared here only to make visible to finally clause
		BufferedReader input = null;
		try {
			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!
			input = new BufferedReader(new FileReader(aFile));
			Matcher m;
			String line = null; // not declared within while loop
			/*
			 * readLine is a bit quirky : it returns the content of a line MINUS the
			 * newline. it returns null only for the END of the stream. it returns an empty
			 * String if two newlines appear in a row.
			 */
			while ((line = input.readLine()) != null) {

				if (line.matches("^[\t ]*$")) {
					// System.out.println( "line ignored : '"+line +"'");
				} else {
				//	lineCount++;

					if //
					((m = p1.matcher(line)).find())// #define TCC4 ((Tcc *)0x43001000U) /**< \brief (TCC4) APB Base
													// Address */
					{

						String def = m.group(1);
						String val = m.group(2);

						defines.put(def, val);

					} else if (line.matches("^[\t ]*typedef[\t ]+.*$"))

					{
						String[] tab = line.split("[\t ]");
						if (tab.length > 1)
							if (tab[1].contains("struct") && (tab.length > 2)) {
								if (tab[2].compareTo("") != 0)
									typedefs.add(tab[2]);
							} else {
								if (tab[1].compareTo("") != 0)
									typedefs.add(tab[1]);
							}

					}

				}

				// debugDisplay(50,"'"+line+"'");

			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (input != null) {
					// flush and close both "input" and its underlying FileReader
					input.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/** read a H file contain*/
	public void readFile(String filecontains[]) {
	//	int lineCount = 0;
		
			
			Matcher m;
			/*
			 * readLine is a bit quirky : it returns the content of a line MINUS the
			 * newline. it returns null only for the END of the stream. it returns an empty
			 * String if two newlines appear in a row.
			 */
			for( String line : filecontains) {

				if (line.matches("^[\t ]*$")) {
					// System.out.println( "line ignored : '"+line +"'");
				} else {
				//	lineCount++;

					if //
					((m = p1.matcher(line)).find())// #define TCC4 ((Tcc *)0x43001000U) /**< \brief (TCC4) APB Base
													// Address */
					{

						String def = m.group(1);
						String val = m.group(2);

						defines.put(def, val);

					} else if (line.matches("^[\t ]*typedef[\t ]+.*$"))

					{
						String[] tab = line.split("[\t ]");
						if (tab.length > 1)
							if (tab[1].contains("struct") && (tab.length > 2)) {
								if (tab[2].compareTo("") != 0)
									typedefs.add(tab[2]);
							} else {
								if (tab[1].compareTo("") != 0)
									typedefs.add(tab[1]);
							}

					}

				}

				// debugDisplay(50,"'"+line+"'");

			}
		
	}

	public static void main(final String[] args) {
		CPreprocessor c = new CPreprocessor();
		// c.readFile("D:\\Users\\Pierre\\CVS_Home\\micros_01\\Annapurna\\Testprogram\\selftest\\SAM\\gpio_110.h");
		// c.setKey("ARM_");
		// c.saveTo("D:\\Users\\Pierre\\CVS_Home\\micros_01\\Annapurna\\Testprogram\\selftest\\SAM2\\gpio_110.h");

		c.readDir("D:\\Users\\Pierre\\CVS_Home\\micros_01\\Annapurna\\Testprogram\\selftest\\avr32");
		c.readDir("D:\\Users\\Pierre\\CVS_Home\\micros_01\\Annapurna\\Testprogram\\selftest\\sam");
		c.setKey("avr32_");
		c.saveTo("D:\\Users\\Pierre\\CVS_Home\\micros_01\\Annapurna\\Testprogram\\selftest\\avr32\\io_avr32conv.h");
		c.setKey("arm_");
		c.saveTo("D:\\Users\\Pierre\\CVS_Home\\micros_01\\Annapurna\\Testprogram\\selftest\\SAM\\io_samconv.h");

	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@SuppressWarnings("unused")
	public String[] listFileNames(String directory) {
		File f = new File(directory);
		String[] sa = f.list(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				boolean result = filename.endsWith(".h");
				// result&=filename.contains( "setup_" );
				// TODO Auto-generated method stub
				return result;
			}
		});
		return (sa);
	}

	public void readDir(String directory) {
		String[] filenames = listFileNames(directory);
		for (String filename : filenames) {
			// System.out.println("process : "+filename);
			this.readFile(directory + "\\" + filename);
		}

	}

}
