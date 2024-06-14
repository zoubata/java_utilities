/**
 * 
 */
package com.zoubworld.java.filesystem.myFS;

import java.util.Date;

/**
 * 
 */
public class DirectoryEntry {
	Long size=null;
	Date creation=null;
	char type=' ';//File,Folder,SystemFileElement 
	Long HashId=null;
	Long previousHashId=null;
	Long penultimateHashId=null;
	Long AntepenultimateHashId=null;
	
	String FileName=null;
	
	
	/*    
	Niveau N : environ 0 % de redondance
    Niveau L : environ 7 % de redondance
    Niveau M : environ 15 %
    Niveau Q : environ 25 %
    Niveau H : environ 30 %
    */
	/**
	 * 
	 */
	public DirectoryEntry() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
