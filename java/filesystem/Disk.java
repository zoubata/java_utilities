/**
 * 
 */
package com.zoubworld.java.filesystem;

/**
 * 
 */
public class Disk {
	FileAllocationTable table;
	Cluster data[]=new Cluster[99];
	Cluster bootDir=data[0];
	Cluster rootDir=data[0];
	
	
	
	/**
	 * 
	 */
	public Disk() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
