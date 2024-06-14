/**
 * 
 */
package com.zoubworld.java.filesystem.myFS;

/**
 * 
 */
public class LocalClusterTable extends Cluster{
//..
	byte SplitSize=8;//1..2^32 ~8-16 <<rsq(sizeCluster/8)
	//sizeCluster/(2^SplitSize)>(2^SplitSize*8+32)
	
	Long Clusterstate[/*2^SplitSize*/];//state of each cluster
	//2^SplitSize-1 free , 0 bad , 1 full.
	/*
	256 : 0..255 ; 0:free, 1 partialy used, 254 full, 255 bad

	   /256
	        /256   */
	/**
	 * 
	 */
	public LocalClusterTable() {
		// TODO Auto-generated constructor stub
	}

}
