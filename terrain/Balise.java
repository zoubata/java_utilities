/**
 * 
 */
package com.zoubworld.terrain;

/**
 * @author M43507
 *
 */
public class Balise extends OObject implements IBalise {

	/**
	 * 
	 */
	public Balise() {
		// TODO Auto-generated constructor stub
	}

	public Balise(Double x, Double y, Double tetha) {
		super(x,y,tetha);
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.terrain.IObject#getProbaCoord(java.lang.Double)
	 */
	@Override
	public Double getProbaCoord(Double Radius) {
		Double p=super.getProbaCoord(Radius);
		return p;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
