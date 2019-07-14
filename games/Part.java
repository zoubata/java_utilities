package com.zoubworld.games;

import java.util.ArrayList;
import java.util.List;

public abstract class Part implements IPart {

	protected char partid=' ';
	protected char team=' ';

	@Override
	public List<List<ILocation>> getMoves() {
		List<List<ILocation>> lm=new ArrayList();
		ILocation l= getBoard().getLoc(this);
		ILocation l2=null;
			for(int x2=0;x2<8;x2++)
				for(int y2=0;y2<8;y2++)
						if(isMoveAllow(l, l2=new Location(x2, y2)))
							{ 
							List<ILocation> ll=new ArrayList();
							ll.add(l);
							ll.add(l2);
							lm.add(ll);
							}
							
		return lm;
	}
	@Override
	public boolean isMoveAllow(ILocation l1, ILocation l2) {
		List<ILocation> ll=new ArrayList();
		return isMoveAllow( ll);
	}
	public String toString()
	{
		return ""+team+partid;
	}
	@Override
	public char getTeam() {
		// TODO Auto-generated method stub
		return team;
	}
	
	Iboard board=null;
	@Override
	public Iboard getBoard() {
		return board;
	}
	@Override
	public void setBoard(Iboard b) {
		board=b;
	}
	@Override
	public boolean isMoveAllow(String move) {
		Location l=new Location();
		
		return isMoveAllow(l.parseMove(move));
	}
	
}
