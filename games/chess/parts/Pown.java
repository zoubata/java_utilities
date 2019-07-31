package com.zoubworld.games.chess.parts;

import java.util.List;

import com.zoubworld.games.ILocation;
import com.zoubworld.games.IPart;
import com.zoubworld.games.Location;
import com.zoubworld.games.chess.Part;

public class Pown extends Part  implements IPart {

	public Pown(char myteam) {
		team=myteam;

		partid='p';
	}
	@Override
	public boolean isMoveAllow(List<ILocation> ll) {
		boolean b= super.isMoveAllow(ll);		
		if (!b)
			return false;
		int x1=ll.get(0).getX();
		int y1=ll.get(0).getY();
		int x2=ll.get(1).getX();
		int y2=ll.get(1).getY();
		/*if (getBoard()==null)
			return false;
		if (getBoard().getPart(ll.get(0))==null)
			return false;*/
		// one step
		boolean pw=getBoard().getPart(ll.get(0)).getTeam()=='W';//part white
		boolean bw=	pw	&& (y1-y2)==1 ;
				bw=bw && x1==x2 && getBoard().getPart(ll.get(1))==null;
		boolean pb=getBoard().getPart(ll.get(0)).getTeam()=='B';//part black
		boolean bb=	pb	&& (y1-y2)==-1 &&  x1==x2 && getBoard().getPart(ll.get(1))==null;
		
		//2 step
		boolean sw=pw	&& (y1-y2)==2 &&  x1==x2 ;// move of 2
		sw=sw && (y1==(6))&& getBoard().getPart(ll.get(1))==null && getBoard().getPart(new Location(x1,y1-1))==null;//good start point, path free
		boolean sb=pb	&& (y1-y2)==-2 &&  x1==x2 ; // move of 2
				sb=sb && y1==1 && getBoard().getPart(ll.get(1))==null && getBoard().getPart(new Location(x1,y1+1))==null;//good start point, path free
		boolean beb=false;//eat...
		boolean bew=false;//eat...
		
		b&=!((x1==x2)&&(y1==y2));// there is a move
		
		return b &&(((bw|| bb) ||sb||sw||beb||bew));
	}
	@Override
	public IPart isEatAllow(List<ILocation> ll) {
		// TODO Auto-generated method stub
		return null;
	}
}
