/**
 * 
 */
package com.zoubworld.games.chess.parts;

import java.util.List;

import com.zoubworld.games.IPart;
import com.zoubworld.games.chess.Part;

/**
 * @author M43507
 *
 */
public class Rook extends Part  implements IPart {

	public Rook(char myteam) {
		team=myteam;
		partid='r';
	}
	
	@Override
	public boolean isMoveAllow(int x1, int y1, int x2, int y2) {
		if (getBoard()==null)
			return false;
		boolean b=(x1==x2) || (y1==y2);//dia
		//thing on the path
		for(int x=x1;x!=x2;x+=Math.signum(x2-x1))
			if(x!=x1)
			b&=getBoard().getPart(x, y1)==null;
		
		for(int y=y1;y!=y2;y+=Math.signum(y2-y1))
			if(y!=y1)
			b&=getBoard().getPart(x1, y)==null;
		
		
		b&=(getBoard().getPart(x2, y2)==null) 
				||	(getTeam()!=getBoard().getPart(x2, y2).getTeam());//end cell isn't on my part
		b&=!((x1==x2)&&(y1==y2));// there is a move
		
		return b;
	}

}
