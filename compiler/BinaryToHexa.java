/**
 * 
 */
package com.zoubworld.compiler;

import java.util.HashMap;
import java.util.Map;

import com.zoubworld.utils.ArgsParser;
import com.zoubworld.utils.JavaUtils;

/**
 * @author Pierre Valleau
 *

 * the goal of this class is to convers binary number I.E. : 0b10 to hexa number 0x2 on C files to be compliant with morre compiler, 
 * example gcc support 0b, but iar support only 0x.
 * 
 */
public class BinaryToHexa {
	Map<String,String> optionparam= null;
	ArgsParser args=null;
	/**
	 * 
	 */
	public BinaryToHexa() {
		optionparam= new HashMap<String,String>();
		// parameter "="
			//	optionparam.put("filter="," regular expression to filter file to parse");
				
				// argument : ""
		optionparam.put("?","this app read filein and convert binary number to hexa number.");
		optionparam.put("Filein","path to files to read");
		optionparam.put("Fileout","path to files to read");
		//	optionparam.put("Action"," list of action: \n\t- SaveLot : save lot info\n\t- SaveAllCsv : save csv\n\t- SaveAll : save all");
				
					// option"+"
				optionparam.put("-help"," this help");
				
		args=new ArgsParser(BinaryToHexa.class,optionparam);		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// parse it
		BinaryToHexa tool=new BinaryToHexa();
					tool.args.parse(args);
					tool.args.check();
					
					System.out.println(tool.args.displayConfig());
					//use it
					String fin=tool.args.getArgument(1);
					String fout=tool.args.getArgument(2);
					String tab[]=JavaUtils.read(fin).split("\n");
					for(int line=0;line<tab.length;line++)
					{
						String s=tab[line];
					//	s="sercom->I2CS.CTRLA.bit.SPEED=0b10;";
						int beginIndex=0;
						int previousindex=-1;
						while (s.contains("0b") && (beginIndex>=0))
						{
							
							beginIndex=s.indexOf("0b",previousindex+1);
							if (beginIndex>=0)
							{
							int endIndex=beginIndex;
							int data=0;
							for(endIndex=beginIndex+2;endIndex<s.length() & (s.charAt(endIndex)>='0' &&  s.charAt(endIndex)<='1');endIndex++)
								if (s.charAt(endIndex)=='1')
									data=(int) ((data<<1L)+1L);
								else
									data=data<<1;
								;
								if(endIndex!=beginIndex+2)
							s=s.replaceFirst(s.substring(beginIndex, endIndex), String.format("0x%"+((endIndex-(beginIndex+2)-1)/4+1)+"x", data).replaceAll(" ", "0"));
								previousindex=beginIndex;
							}
						}
						tab[line]=s;
					}
					JavaUtils.saveAs(fout, tab);
					
	}

}
