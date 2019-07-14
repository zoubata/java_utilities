package com.zoubworld.games.chess.parts;

import java.util.List;

import com.zoubworld.games.IPart;
import com.zoubworld.games.chess.Part;

public class Bishop extends Part implements IPart {

	public Bishop(char myteam) {
		team=myteam;
		partid='b';
	}
	@Override
	public boolean isMoveAllow(int x1, int y1, int x2, int y2) {
		if (getBoard()==null)
			return false;
		// TODO Auto-generated method stub
		boolean b= Math.abs(x1-x2)==Math.abs(y1-y2);
		for(int x=x1,y=y1;x!=x2 && y!=y2;y+=Math.signum(y2-y1),x+=Math.signum(x2-x1))
			if(x!=x1)
			b&=getBoard().getPart(x, y)==null;
	
		b&=!((x1==x2)&&(y1==y2));// there is a move
			b&=(getBoard().getPart(x2, y2)==null) 
				||	(getTeam()!=getBoard().getPart(x2, y2).getTeam());//end cell isn't on my part
	
	/*	for(int x=Math.min(x1,x2)+1;x<Math.max(x1,x2);x++)
			b&=getBoard().getPart(x, y)==null;
		*/return b;
	}
	
		


}
