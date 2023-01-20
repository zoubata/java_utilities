/**
 * 
 */
package com.zoubworld.Crypto;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zoubworld.utils.JavaUtils;

/**
 * @author zoubata
 *
 */
public class Uniswap {

	/**
	 * 
	 */
	public Uniswap() {
		// TODO Auto-generated constructor stub
	}

	
	static Set<String> parse_token(String path) {
		String pat="\\d+\\s+[\\s\\w]+[\\(PoS\\.\\)]*\\s+\\((\\w+)\\)\\s+\\$(\\d+\\.\\d+\\w?)\\s+.*\\s*(\\d+\\.\\d+\\w?)\\%\\s+\\$(\\d+\\.\\d+\\w?)\\s+\\$(\\d+\\.\\d+\\w?)\\s+";
		//"\\d+\\s+.+\\s+\\((\\w+)\\)\\s+\\$(\\d+\\.\\d+\\w?)\\s+.*\\s*(\\d+\\.\\d+\\w?)\\%\\s+\\$(\\d+\\.\\d+\\w?)\\s+\\$(\\d+\\.\\d+\\w?)\\s+";
		System.out.println(pat);
	Pattern p= Pattern.compile(pat
				//"\\s+.+\\s+\\((\\w+)\\)\\s+\\$(\\d+\\.\\d+)\\s+.\\s*(\\d+\\.\\d+)\\%\\s+\\$(\\d+\\.\\d+\\w)\\s+\\$(\\d+\\.\\d+\\w)\\s+\\d+"
				//"\\s+.+\\s+\\((\\w+)\\)\\s+\\$(\\d+\\.\\d+)\\s+(\\d+\\.\\d+)\\%\\s+\\$(\\d+\\.\\d+)\\w\\s+\\$(\\d+\\.\\d+)\\w\\s+(\\d)\\s+"
				);
	    Matcher m = p.matcher(path);
		Set<String> patterns = new LinkedHashSet<>();
		 while (m.find()) {
		  patterns.add(m.group(1));
		  
		  double TVL=parsedouble(m.group(5));
		  double vol=parsedouble(m.group(4));
		  double price=parsedouble(m.group(2));
		  
		  System.out.println(m.group(1)+";"+price+";"+m.group(3)+";"+vol+";"+TVL+";");
		 }
		 return patterns;
		}

	
	static Set<String> parse_pool(String path) {
		String pat="\\d+\\s+.+\\s+(\\w+\\/\\w+)\\s+(\\d+\\.?\\d*\\w?)\\%\\s+.?\\$(\\d+\\.?\\d*\\w?)\\s+\\$(\\d+\\.?\\d*\\w?)\\s+\\$(\\d+\\.?\\d*\\w?)";
		System.out.println(pat);
		Pattern p= Pattern.compile(pat
				//"\\d+\\s+.+\\s+(\\w+\\/\\w+)\\s+(\\d+\\.?\\d*\\w?)\\%\\s+.\\$(\\d+\\.\\d+\\w?)\\s+\\$(\\d+\\.\\d+\\w?)\\s+\\$(\\d+\\.\\d+\\w?)"
				);
	    Matcher m = p.matcher(path);
		Set<String> patterns = new LinkedHashSet<>();
		 while (m.find()) {
		  patterns.add(m.group(1));
		  
		  double TVL=parsedouble(m.group(3));
		  double vol=parsedouble(m.group(4));
		  double vol7d=parsedouble(m.group(5));
			  double taux=parsedouble(m.group(2));
		  String couplesym=m.group(1);
		  
		  System.out.println(couplesym+";"+taux+";"+TVL+";"+vol+";"+vol7d+";");
		 }
		 return patterns;
		}
	
	private static double parsedouble(String group) {
		double m=1;
		if (group.endsWith("k"))
		{m=1000;group=group.substring(0, group.length()-1);}
		if (group.endsWith("m"))
		{m=1000000;group=group.substring(0, group.length()-1);}
		if (group.endsWith("b"))
		{m=1000000000;group=group.substring(0, group.length()-1);}
	
		double d=Double.parseDouble(group);
		return d*m;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String f=JavaUtils.read(new File("C:\\Users\\M43507\\Downloads\\new 23.txt"));
		parse_token(f);
		
		 f=JavaUtils.read(new File("C:\\Users\\M43507\\Downloads\\new 24.txt"));
		parse_pool(f);
	}

}
