package com.zoubworld.games.dames.part;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.games.ILocation;
import com.zoubworld.games.IPart;
import com.zoubworld.games.Location;
import com.zoubworld.games.Part;

public class Pown extends Part  implements IPart {

	public Pown(char myteam) {
		team=myteam;

		partid='p';
	}
	@Override
	public boolean isMoveAllow(List<ILocation> ll) {
		if (ll.size()<2)
			return false;
		int x1=ll.get(0).getX();
		int y1=ll.get(0).getY();
		int x2=ll.get(1).getX();
		int y2=ll.get(1).getY();
		boolean b=Math.abs(x1-x2)==Math.abs(y1-y2) ;// good axes
		b&=!((x1==x2)&&(y1==y2));//it must be a move to a different cell
		boolean b1=(//move	
				((y2-y1==1)&&(team=='W'))
				|| ((y2-y1==-1)&&(team=='B'))
		);// un pas
		boolean b2=(//eat
				(  (y2-y1==2)&&(team=='W'))
				|| ((y2-y1==-2)&&(team=='B'))
			)
			&& 
			(getBoard().getPart(new Location((x1+x2)/2,(y1+y2)/2))!=null) && getBoard().getPart(new Location((x1+x2)/2,(y1+y2)/2)).getTeam()!=getBoard().getPart(new Location((x1),(y1))).getTeam();
			boolean b3=(getBoard().getPart(new Location((x2),(y2)))==null);//case d'arriver vide
			
		;
		b=b&& (b1||b2) && b3;
				
		return b; 
	}
	public IPart isEatAllow(List<ILocation> ll) {
		
		int x1=ll.get(0).getX();
		int y1=ll.get(0).getY();
		int x2=ll.get(1).getX();
		int y2=ll.get(1).getY();
		boolean b=Math.abs(x1-x2)==Math.abs(y1-y2) ;// good axes
		boolean b1=(//move	
				((y2-y1==1)&&(team=='W'))
				|| ((y2-y1==-1)&&(team=='B'))
		);// un pas
		boolean b2=(//eat
				(  (y2-y1==2)&&(team=='W'))
				|| ((y2-y1==-2)&&(team=='B'))
			)
			&& 
			(getBoard().getPart(new Location((x1+x2)/2,(y1+y2)/2))!=null) && getBoard().getPart(new Location((x1+x2)/2,(y1+y2)/2)).getTeam()!=getBoard().getPart(new Location((x1),(y1))).getTeam();
			boolean b3=(getBoard().getPart(new Location((x2),(y2)))==null);//case d'arriver vide
			
		;
		b=b&& (b2) && b3;
		if (b)
			
		return getBoard().getPart(new Location((x1+x2)/2,(y1+y2)/2)); 
		return null;
	}
	/*
	@Override
	public List<String> getMoves() {
		int x1=getBoard().getX(this);
		int y1=getBoard().getY(this);
		List<String> ls=new ArrayList();
		int y2=y1+1;
		if (team=='B')
			 y2=y1-1;
		int x2=x1+1;
		
		
		if (getBoard().isMoveAllow(x1, y1, x2, y2))
			if (isMoveAllow(x1, y1, x2, y2))
				ls.add(getBoard().moveToString(x1, y1, x2, y2));
		x2=x1-1;
		if (getBoard().isMoveAllow(x1, y1, x2, y2))
			if (isMoveAllow(x1, y1, x2, y2))
				ls.add(getBoard().moveToString(x1, y1, x2, y2));
		
		y2=y1+2;
		if (team=='B')
			 y2=y1-2;
		x2=x1+2;
		if (getBoard().isMoveAllow(x1, y1, x2, y2))
			if (isMoveAllow(x1, y1, x2, y2))
				ls.add(getBoard().moveToString(x1, y1, x2, y2));
		
		x2=x1-2;
		if (getBoard().isMoveAllow(x1, y1, x2, y2))
			if (isMoveAllow(x1, y1, x2, y2))
				ls.add(getBoard().moveToString(x1, y1, x2, y2));
		
		return ls;
	}
	*/
}
