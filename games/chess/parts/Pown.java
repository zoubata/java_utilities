package com.zoubworld.games.chess.parts;

import java.util.List;

import com.zoubworld.games.IPart;
import com.zoubworld.games.chess.Part;

public class Pown extends Part  implements IPart {

	public Pown(char myteam) {
		team=myteam;

		partid='p';
	}
	@Override
	public boolean isMoveAllow(int x1, int y1, int x2, int y2) {
		if (getBoard()==null)
			return false;
		boolean bw=getBoard().getPart(x1, y1).getTeam()=='W'
				&& (y1-y2)==1 && getBoard().getPart(x2, y2)==null;
		boolean bb=getBoard().getPart(x1, y1).getTeam()=='B'
				&& (y1-y2)==-1&&  getBoard().getPart(x2, y2)==null;
		
		boolean sw=getBoard().getPart(x1, y1).getTeam()=='W'
				&& (y1-y2)==2 && y1==7-2 && getBoard().getPart(x2, y2)==null;
		boolean sb=getBoard().getPart(x1, y1).getTeam()=='B'
				&& (y1-y2)==-2 && y1==2 && getBoard().getPart(x2, y2)==null;
		boolean beb=false;//eat...
		boolean bew=false;//eat...
		boolean b=(getBoard().getPart(x2, y2)==null) 
				||	(getTeam()!=getBoard().getPart(x2, y2).getTeam());//end cell isn't on my part
		b&=!((x1==x2)&&(y1==y2));// there is a move
		
		return b &&(bw|| bb ||sb||sw||beb||bew);
	}
}
