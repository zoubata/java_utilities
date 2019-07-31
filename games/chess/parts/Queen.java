package com.zoubworld.games.chess.parts;

import java.util.List;

import com.zoubworld.games.ILocation;
import com.zoubworld.games.IPart;
import com.zoubworld.games.chess.Part;

public class Queen extends Part  implements IPart {

	public Queen(char myteam) {
		team=myteam;
		partid='Q';
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
		boolean br=(x1==x2) || (y1==y2);
		boolean bb= Math.abs(x1-x2)==Math.abs(y1-y2);
		/*
		 b=b&& ((getBoard().getPart(ll.get(1))==null) 
				||	(getTeam()!=getBoard().getPart(ll.get(1)).getTeam()));//end cell isn't on my part
		b&=!((x1==x2)&&(y1==y2));// there is a move
		*/
		return b && (bb || br);
	}

	@Override
	public IPart isEatAllow(List<ILocation> ll) {
		// TODO Auto-generated method stub
		return null;
	}
}
