package com.zoubworld.games.chess.parts;

import java.util.List;

import com.zoubworld.games.IPart;
import com.zoubworld.games.chess.Part;

public class Queen extends Part  implements IPart {

	public Queen(char myteam) {
		team=myteam;
		partid='Q';
	}
	
	@Override
	public boolean isMoveAllow(int x1, int y1, int x2, int y2) {
		if (getBoard()==null)
			return false;
		boolean br=(x1==x2) || (y1==y2);
		boolean bb= Math.abs(x1-x2)==Math.abs(y1-y2);
		
		boolean b=(getBoard().getPart(x2, y2)==null) 
				||	(getTeam()!=getBoard().getPart(x2, y2).getTeam());//end cell isn't on my part
		b&=!((x1==x2)&&(y1==y2));// there is a move
		
		return b && (bb || br);
	}
}
