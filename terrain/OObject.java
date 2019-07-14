/**
 * 
 */
package com.zoubworld.terrain;

/**
 * @author pierre valleau
 *
 */
public class OObject implements IObject {

	/**
	 * 
	 */
	public OObject() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 */
	public OObject(Double x, Double y,Double tetha) {
		X0=x;
		Y0=y;
		T0=tetha;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.terrain.IObject#getX0()
	 */
	@Override
	public Double getX0() {
		
		return X0;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.terrain.IObject#getY0()
	 */
	@Override
	public Double getY0() {
		
		return Y0;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.terrain.IObject#getT0()
	 */
	@Override
	public Double getT0() {
		
		return T0;
	}
	Double X0=null;
	Double Y0=null;
	Double T0=null;
	
	/* (non-Javadoc)
	 * @see com.zoubworld.terrain.IObject#getProbaCoord(java.lang.Double)
	 */
	@Override
	public Double getProbaCoord(Double Radius) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
