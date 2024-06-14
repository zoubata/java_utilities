/**
 * 
 */
package com.zoubworld.insee;

import java.util.List;
import java.util.function.Function;

import com.zoubworld.utils.ExcelArray;

/**
 * 
 */
public class Deces {

	/**
	 * 
	 */
	public Deces() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExcelArray ea=new ExcelArray();
		ea.readTxt("C:\\temp\\Downloads\\deces\\csv\\Deces_2022.csv",";","\r\n","\"");
		
		
		System.out.println(ea.getHeader());
		System.out.println(ea.getRow(2));
		
		System.out.println(ea.getCell(2, 2));
		System.out.println(ea.getCell(2, 6));
		
		double a=age("19270407");
		
		Function<List<String>, String> fn = 
				  x ->  ("" +(age(x.get(6))-age(x.get(2))));
		ea.addColumn("age",fn );
		ea.saveAs("C:\\temp\\Downloads\\deces\\csv\\Deces_2022-new.csv");
		
	}

	private static double age(String string) {
		Long l=Long.parseUnsignedLong( string.substring(0, 4));
				double d=l;
				l=Long.parseUnsignedLong( string.substring(4, 6));
				d+=l/12.0;
				
				l=Long.parseUnsignedLong( string.substring(6, 8));
				d+=l/12.0/31;
				
		return d;
	}

}
