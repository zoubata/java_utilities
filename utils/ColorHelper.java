package com.zoubworld.utils;

import java.awt.Color;

public class ColorHelper {

	public ColorHelper() {
		// TODO Auto-generated constructor stub
	}
	static public String JumpColorBlue_to_Red(int k,int n)
	{
		 double value=k;
		 value=value/n;
		Color c = de.unikassel.ann.util.ColorHelper.numberToColorPercentage(value);
		
		return "{"+c.getRed()+","+c.getGreen()+","+c.getBlue()+"}";
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
