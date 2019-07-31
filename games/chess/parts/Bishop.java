package com.zoubworld.games.chess.parts;

import java.util.List;

import com.zoubworld.games.ILocation;
import com.zoubworld.games.IPart;
import com.zoubworld.games.Location;
import com.zoubworld.games.chess.Part;

public class Bishop extends Part implements IPart {

	public Bishop(char myteam) {
		team=myteam;
		partid='b';
	}
	@Override
	public boolean isMoveAllow(List<ILocation> ll) {
		boolean b= super.isMoveAllow(ll);
		if (ll.size()<2)
			return false;
		int x1=ll.get(0).getX();
		int y1=ll.get(0).getY();
		int x2=ll.get(1).getX();
		int y2=ll.get(1).getY();
		if (getBoard()==null)
			return false;

		 b=b&& Math.abs(x1-x2)==Math.abs(y1-y2);
		for(int x=x1,y=y1;x!=x2 && y!=y2;y+=Math.signum(y2-y1),x+=Math.signum(x2-x1))
			if(x!=x1)
			b&=getBoard().getPart(new Location(x, y))==null;
	/*
		b&=!((x1==x2)&&(y1==y2));// there is a move
			b&=(getBoard().getPart(ll.get(1))==null) 
				||	(getTeam()!=getBoard().getPart(ll.get(1)).getTeam());//end cell isn't on my part
	*/
	/*	for(int x=Math.min(x1,x2)+1;x<Math.max(x1,x2);x++)
			b&=getBoard().getPart(x, y)==null;
		*/return b;
	}
	@Override
	public IPart isEatAllow(List<ILocation> ll) {
		// TODO Auto-generated method stub
		return null;
	}
	
		


}
