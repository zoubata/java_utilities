package com.zoubworld.games;

import java.util.List;

import com.zoubworld.games.dames.part.Pown;

public interface Iboard {

	public IPart getPart(ILocation loc);

	public boolean isMoveAllow(List<ILocation> ll);
	public boolean isMoveAllow(ILocation l1,ILocation l2);

	public String moveToString(List<ILocation> ll);
	public String toString();
	public String toString(List<List<ILocation>> move2);
	public ILocation getLoc(IPart pown);
	
	public void clear();
	public void put(ILocation loc,IPart part);

	public int sizeY();

	public int sizeX();
}
