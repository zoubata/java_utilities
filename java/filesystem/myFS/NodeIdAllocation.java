/**
 * 
 */
package com.zoubworld.java.filesystem.myFS;



/**
 * 
 */
public class NodeIdAllocation {
    // cluster ID
	//  00000000-11111111-22222222-33333333
	// -44444444-55555555-66666666-77777777
	
	Integer NodeRelativeId=null;//name of the node
	Integer NodeIdSize=null;
	NodeIdAllocation IdTable[]=null;//2^NodeIdSize;
	
	/**
	 * 
	 */
	public NodeIdAllocation() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
