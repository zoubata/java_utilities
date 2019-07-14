/**
 * 
 */
package com.zoubworld.games;

import java.util.List;

/**
 * @author Pierre Valleau
 *
 */
public interface IPart {

	public Iboard getBoard();
	public void setBoard(Iboard  b);
	
	//public IPart(char team);
	/**
	 * return the possible movement.
	 * */
	public List<List<ILocation>> getMoves();
	public String toString();
	public boolean isMoveAllow(List<ILocation> ll);
	public boolean isMoveAllow(ILocation l1,ILocation l2);
	public boolean isMoveAllow(String move);
	public char getTeam();
	public IPart isEatAllow(List<ILocation> ll);
}
