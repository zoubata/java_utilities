/**
 * 
 */
package com.zoubworld.games;

import java.util.List;

/**
 * @author M43507
 *
 */
public interface ILocation {

	public ILocation parse(String loc);
	public ILocation Factory(Integer x,Integer y,Integer z);
	public  List<ILocation> parseMove(String move);
	public String getSLoc();
	public Integer getX();
	public Integer getY();
	public Integer getZ();
	
	
	
}
