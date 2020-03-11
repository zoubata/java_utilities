package com.zoubworld.java.utils.svg;

import com.zoubworld.java.svg.ItoSvg;

/** class that contain a svg string 
 * */
public class StringSvg  extends BasicSvg  implements ItoSvg{

	public StringSvg() {
		// TODO Auto-generated constructor stub
	}
 
	String text=null;
	
	public StringSvg(String my_text) {
	
		text=my_text;
		
	}

	@Override
	public String toSvg()
	{
			return text;	
	}

	
	
}
