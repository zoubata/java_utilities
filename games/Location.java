/**
 * 
 */
package com.zoubworld.games;

import java.util.ArrayList;
import java.util.List;

/**
 * @author  Pierre Valleau
 *
 */
public class Location implements ILocation {

	/**
	 * 
	 */
	public Location() {
		// TODO Auto-generated constructor stub
	}

	public Location(int x2, int y2) {
		x=x2;y=y2;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	Integer x=null;
	Integer y=null;
	@Override
	public ILocation parse(String loc) {
		
		 x=Integer.parseInt(loc.substring(1, loc.length()))-1;
		 y=loc.codePointAt(0)-'A';
	
		return this;
	}

	@Override
	public List<ILocation> parseMove(String move) {
		List<ILocation> ll=new ArrayList();
		for(String loc:splitMove(move))
		{
			ILocation l=new Location();
		l=l.parse(loc);
		ll.add(l);}
		return ll;
	}

	public String toString()
	{
		return /*"loc("+*/getSLoc()/*+")"*/;
	}
	private List<String> splitMove(String move) {
		List<String> ls=new ArrayList<>();
		int i=move.charAt(2)>'@'?2:3;
		String loc1=move.substring(0, i);
		String loc2=move.substring(i, move.length());
		ls.add(loc1);
		ls.add(loc2);
		
		return ls;
	}

	@Override
	public String getSLoc() {
		// TODO Auto-generated method stub
		return ""+(char)('A'+y)+(int)(1+x);
	}


	@Override
	public Integer getX() {
		return x;
	}

	@Override
	public Integer getY() {
		return y;
	}

	@Override
	public Integer getZ() {
		return null;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(Integer x) {
		this.x = x;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(Integer y) {
		this.y = y;
	}

	@Override
	public ILocation Factory(Integer x, Integer y, Integer z) {
		Location l=new Location();
		l.x=x;
		l.y=y;
		
		return l;
	}

}
