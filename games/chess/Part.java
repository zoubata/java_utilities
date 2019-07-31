package com.zoubworld.games.chess;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.games.ILocation;
import com.zoubworld.games.IPart;

public abstract class Part extends com.zoubworld.games.Part implements IPart {

	@Override
	public IPart isEatAllow(List<ILocation> ll) {
		if (ll.size()<2)
			return null;
		int x1=ll.get(0).getX();
		int y1=ll.get(0).getY();
		int x2=ll.get(1).getX();
		int y2=ll.get(1).getY();
		if(isMoveAllow(ll))
		return getBoard().getPart(ll.get(1));
	return null;
	}
	@Override
	public boolean isMoveAllow(List<ILocation> ll) {
		if (getBoard()==null)
			return false;
		if (getBoard().getPart(ll.get(0))==null)
			return false;
		
	boolean b=(getBoard().getPart(ll.get(1))==null) // target is empty
			||	(getTeam()!=getBoard().getPart(ll.get(1)).getTeam());//OR end cell isn't on my part
	if (ll.size()!=2)//one movement at a time
		return false;
	int x1=ll.get(0).getX();
	int y1=ll.get(0).getY();
	int x2=ll.get(1).getX();
	int y2=ll.get(1).getY();
	b&=!((x1==x2)&&(y1==y2));// there is a move
	
	return b;
	}
}
