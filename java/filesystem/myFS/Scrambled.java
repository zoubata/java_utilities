/**
 * 
 */
package com.zoubworld.java.filesystem.myFS;

/**
 * 
 */
public class Scrambled {

	
	Long key[]=null;// encripted key to scramble and enscramble the file
	
	Byte data[];
	
	public Long getdata(Long address)
	{
		return scrambleData(data[scrambleAdd(address)],address);
	}
	private int scrambleAdd(Long address) {
		// TODO Auto-generated method stub
		return (int) (address.longValue()%data.length);
	}
	private Long scrambleData(Byte data, Long address) {
		// TODO Auto-generated method stub
		return data;
	}
	/**
	 * 
	 */
	public Scrambled() {
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
