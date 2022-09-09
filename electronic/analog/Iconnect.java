/**
 * 
 */
package com.zoubworld.electronic.analog;

/**
 * @author Pierre Valleau
 *
 */
public interface Iconnect {
	/**
	 * @return the pa
	 */
	public Point getPa();

	/**
	 * @param pa the pa to set
	 */
	public void setPa(Point pa);

	/** return the potential against the reference
	 * @throws Exception */
	public Double getPotential(Point ref,Point measurepoint) throws Exception;

	public Double getCurrent(Point point) throws Exception;
	
}
