package com.zoubworld.games.dames.part;

import java.util.List;

import com.zoubworld.games.ILocation;
import com.zoubworld.games.IPart;
import com.zoubworld.games.Iboard;
import com.zoubworld.games.Location;
import com.zoubworld.games.Part;

public class Queen extends Part  implements IPart {

	public Queen(char myteam) {
		team=myteam;
		partid='Q';
	}
	@Override
	public boolean isMoveAllow(List<ILocation> ll) {
		
		int x1=ll.get(0).getX();
		int y1=ll.get(0).getY();
		int x2=ll.get(1).getX();
		int y2=ll.get(1).getY();
		boolean b= Math.abs(x1-x2)==Math.abs(y1-y2);
				b=b&& (getBoard().getPart(new Location((y2),(x2)))==null);
			// path vide on 1 sur 2.
				return b;
	}
	@Override
	public IPart isEatAllow(List<ILocation> ll) {
		// TODO Auto-generated method stub
		return null;
	}
}
