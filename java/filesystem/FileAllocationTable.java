/**
 * 
 */
package com.zoubworld.java.filesystem;

/**
 * 
 */
public class FileAllocationTable {

	Byte table[];
	//0xf...f7 : bad cluster
	//0xf...f8 : end of disk
	// 0x0 : free;
	// different is ref for next cluster to read.
	
	
	
	/**
	 * 
	 */
	public FileAllocationTable() {
		// TODO Auto-generated constructor stub
	}

}
