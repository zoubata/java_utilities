package com.zoubworld.games.chess.parts;

import java.util.List;

import com.zoubworld.games.IPart;
import com.zoubworld.games.chess.Part;

public class King extends Part implements IPart {

	public King(char myteam) {
		team=myteam;

		partid='K';
	}
	@Override
	public boolean isMoveAllow(int x1, int y1, int x2, int y2) {
		if (getBoard()==null)
			return false;
		boolean b=Math.abs(x1-x2)<=1;
		b=b&&Math.abs(y1-y2)<=1;
	
		b&=!((x1==x2)&&(y1==y2));// there is a move
		b&=(getBoard().getPart(x2, y2)==null) 
				||	(getTeam()!=getBoard().getPart(x2, y2).getTeam());//end cell isn't on my part
	
		return b;
	}

}
