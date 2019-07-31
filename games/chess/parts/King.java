package com.zoubworld.games.chess.parts;

import java.util.List;

import com.zoubworld.games.ILocation;
import com.zoubworld.games.IPart;
import com.zoubworld.games.chess.Part;

public class King extends Part implements IPart {

	public King(char myteam) {
		team=myteam;

		partid='K';
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
		boolean b1=Math.abs(x1-x2)<=1;// move 1 cell
		b1=b1&&Math.abs(y1-y2)<=1;// move 1 cell
	
	/*	boolean b2=!((x1==x2)&&(y1==y2));// there is a move
		// mange pas de piece ou une du camp adverce
		boolean b3=	getBoard().getPart(ll.get(1))==null  ;
				b3=b3|| ( getBoard().getPart(ll.get(1))!=null  
					 && (getTeam()!=getBoard().getPart(ll.get(1)).getTeam()));//end cell isn't on my part*/
	
		return b&& b1 ;
	}
	@Override
	public IPart isEatAllow(List<ILocation> ll) {
		// TODO Auto-generated method stub
		return null;
	}

}
