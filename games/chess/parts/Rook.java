/**
 * 
 */
package com.zoubworld.games.chess.parts;

import java.util.List;

import com.zoubworld.games.ILocation;
import com.zoubworld.games.IPart;
import com.zoubworld.games.Location;
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
		 b=b&& ((x1==x2) || (y1==y2));//line/colunm
		//thing on the path
		for(int x=x1;x!=x2;x+=Math.signum(x2-x1))
			if(x!=x1)
			b&=getBoard().getPart(new Location(x, y1))==null;
		
		for(int y=y1;y!=y2;y+=Math.signum(y2-y1))
			if(y!=y1)
			b&=getBoard().getPart(new Location(x1, y))==null;
		
		/*
		b&=(getBoard().getPart(ll.get(1))==null) 
				||	(getTeam()!=getBoard().getPart(ll.get(1)).getTeam());//end cell isn't on my part
		b&=!((x1==x2)&&(y1==y2));// there is a move
		*/
		return b;
	}

	@Override
	public IPart isEatAllow(List<ILocation> ll) {
		// TODO Auto-generated method stub
		return null;
	}

}
