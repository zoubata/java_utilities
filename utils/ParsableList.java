package com.zoubworld.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zoubworld.utils.IParsable;
import com.zoubworld.utils.ParsableList;


/** generics parsable class
 * 
 * used with ParseFile(List<IParsable> classList, String filename)
 * 
 * example it can parse : 
 BIN   6 |    9|    9   6.4 |

        | Handler 'a'
SOCKET  | S0A | S0B | S1A | S1B | S2A | S2B | S3A | S3B | count     %  | Bin name
        |-----|-----|-----|-----|-----|-----|-----|-----|--------------|---------------
PASSED  |    5|    5|    5|    5|    0|    5|    5|    5|    35  25.0  |
FAILED  |    5|    5|    5|    5|    0|    5|    5|    5|    35  25.0  |
        |     |     |     |     |     |     |     |     |              |
BIN   1 |    5|    5|    5|    5|    0|    5|    5|    5|    35  25.0  | "Pass_pb"
BIN 467 |    0|    0|    0|    0|    0|    1|    0|    0|     1   0.7  | "Twi_fb"
BIN 494 |    0|    0|    0|    0|    0|    4|    0|    0|     4   2.9  | "Dac_fb"
BIN 502 |    0|    0|    0|    0|    0|    0|    0|    0|     0   0.0  | "Bandgap_fb"
BIN 901 |    5|    5|    5|    5|    0|    0|    5|    5|    30  21.4  | "Shorts_fb"
BIN 924 |    0|    0|    0|    0|    0|    0|    0|    0|     0   0.0  | "Pulldowns_fb"

        | Handler 'a'
SOCKET  | S4A | S4B | S5A | S5B | S6A | S6B | S7A | S7B | count     %  | Bin name
        |-----|-----|-----|-----|-----|-----|-----|-----|--------------|---------------
PASSED  |    0|    5|    5|    5|    5|    5|    5|    5|    35  25.0  |
FAILED  |    0|    5|    5|    5|    5|    5|    5|    5|    35  25.0  |
        |     |     |     |     |     |     |     |     |              |
BIN   1 |    0|    5|    5|    5|    5|    5|    5|    5|    35  25.0  | "Pass_pb"
BIN 467 |    0|    3|    0|    0|    0|    0|    0|    0|     3   2.1  | "Twi_fb"
BIN 494 |    0|    0|    0|    0|    0|    0|    0|    0|     0   0.0  | "Dac_fb"
BIN 502 |    0|    1|    0|    0|    0|    0|    0|    0|     1   0.7  | "Bandgap_fb"
BIN 901 |    0|    0|    5|    5|    5|    5|    5|    5|    30  21.4  | "Shorts_fb"
BIN 924 |    0|    1|    0|    0|    0|    0|    0|    0|     1   0.7  | "Pulldowns_fb"

 * from code :
 * ParsableList PSL_SOCKET=new ParsableList("\\|","SOCKET");
		ParsableList PSL_PASSED=new ParsableList("\\|","PASSED");
		ParsableList PSL_FAILED=new ParsableList("\\|","FAILED");
		ParsableList PSL_Bins=new ParsableList("\\|","BIN \\d+");
		  handlermap.add(PSL_SOCKET);
		  handlermap.add(PSL_PASSED);
		  handlermap.add(PSL_Bins);
		handlermap=JavaUtils.ParseFile(handlermap,filelines.split("\\n") ,(String)null); 

 * */
public class ParsableList implements IParsable {

	public ParsableList(String mySeparator,String  mykey) {
		super();
		separator=mySeparator;
		key=mykey;
	}
	public String toString()
	{
		return "ParsableList("+key+"("+separator+".*)*)";
	}
	/**
	 * @return the p0
	 */
	public Pattern getP0() {
		if (p0==null)
		p0=Pattern.compile("^\\s*"+key+"\\s*(\\s*"+separator+"\\s*.*)\\s*"+"$");
		return p0;
	}

	List<String> value=null;
	String separator=null;
	String key=null;
	private ParsableList()
	{
		super();
	}
	
	private Pattern p0=null;
	/**
	 * select item from ps that match this object.
	*/
	public Set<IParsable> select( Set<IParsable> ps)
	{
		Set<IParsable> set=new HashSet<IParsable>();
		for(IParsable s:ps)
			if ( s instanceof ParsableList) 
			if ( (((ParsableList)s).separator==separator) 
				&&(((ParsableList)s).key==key) )
				set.add(s);
		return set;
		
	}
	
		public static Map<String,List<String>> GetMap(List<IParsable> classList)
		{
			if (classList==null)
				return null;
			Map<String,List<String>> map=new HashMap<String,List<String>>();
			for(IParsable ps:classList)
				if ( ps instanceof ParsableList) 
					if(((ParsableList)ps).key!=null)
						if(((ParsableList)ps).value!=null)
							if(map.containsKey(((ParsableList)ps).key))
							{
								List<String> l=new ArrayList<String>();
								l.addAll(((ParsableList)ps).value);
								l.addAll(map.get(((ParsableList)ps).key));
								map.put(((ParsableList)ps).key, l);	
							}
							else
				map.put(((ParsableList)ps).key, ((ParsableList)ps).value);
			return map;
			
		}
	public IParsable Parse(String line) {
		
			
		Matcher m=null;

		 if ((m=getP0().matcher(line))!=null)
		 if ((m).find())    		 
	 {
			 String[] tab=line.split(separator);
			 ParsableList tc= new ParsableList(this.separator,tab[0].trim()); 	
			 if (tc.value==null)
			 	tc.value=new ArrayList<String>();
			
			 for(int i=1;i<tab.length;i++)
				 tc.value.add(tab[i].trim());
			
		return tc;
		}
		return null;
	}

	public List<String> getValue() {
		return value;
	}
/*	public String getSymbol() {
		return symbol;
	}*/
	public String getKey() {
		return key;
	}
	@Override
	public String get() {
		// TODO Auto-generated method stub
		return getValue().toString();
	}



}