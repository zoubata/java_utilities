/**
 * 
 */
package com.zoubworld.terrain;

/**
 * @author pierre valleau
 *
 */
public interface IObject {
	/** return the must probable X0
	 * */
	public Double getX0();
	/** return the must probable Y0
	 * */
	public Double getY0();
	/** return the must probable Theta0
	 * */
	public Double getT0();
	/** return the probability of the coord on a circle of Radius
	 * 1.0 sure
	 * 0 fully unknown
	 * 
	 * */
	public Double getProbaCoord(Double Radius);

}
