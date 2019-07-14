package com.zoubworld.games.dames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.zoubworld.games.ILocation;
import com.zoubworld.games.IPart;
import com.zoubworld.games.Iboard;
import com.zoubworld.games.Location;
import com.zoubworld.games.dames.part.Pown;
import com.zoubworld.games.dames.part.Queen;

/**
 * @author M43507
 *
 */
public class BoardDames implements Iboard{

	static final int size=10;
	IPart array[][];
	List<IPart> white=new ArrayList();
	List<IPart> Black=new ArrayList();
	
	public IPart getpart(String location)
	{
		int x=Integer.parseInt(location.substring(1, location.length()))-1;
		int y=location.codePointAt(0)-'A';
		
		return array[y][x];
	}
	public void init()
	{

		array=new IPart[size][];
		for(int y=0;y<size;y++)
		array[y]=new IPart[size];
		

		white=new ArrayList();
		Black=new ArrayList();
		
		for(int x=0;x<size;x+=2)
		{
			array[0][x]=new Pown('W');
			array[1][x+1]=new Pown('W');
			array[2][x]=new Pown('W');
			array[3][x+1]=new Pown('W');
			
		}
		
		for(int x=0;x<size;x+=2)
		{
			array[size-1][x+1]=new Pown('B');
			array[size-2][x]=new Pown('B');
			array[size-3][x+1]=new Pown('B');
			array[size-4][x]=new Pown('B');		
		}
		
		for(int x=0;x<size;x+=1)
			for(int y=0;y<size;y+=1)
			{
				if (array[y][x]!=null)
					array[y][x].setBoard(this);
			}
		
	}
	
	public String toString()
	{
		String s="";
		if (array!=null)
		{
		
		for(int y=size-1;y>=0;y--)
		{
			s+=""+(char)('A'+(char)y);
			if (array[y]!=null)
			for(int x=0;x<size;x++)
		{
				if (array[y][x]!=null)
				s+=array[y][x].toString();
				else
					if(y%2==x%2)
				s+="  ";
					else
						s+="--";
		}
			s+="\r\n";
		}
		s+=" 1 2 3 4 5 6 7 8 9 1\r\n";
		s+="                   0\r\n";
		
		}
		return s;
		
	}
	/**
	 * 
	 */
	public BoardDames() {
		// TODO Auto-generated constructor stub
	}
	/**
	move : "A1B1", 
	 * */
	public boolean move(String move)
	{		
		if (move.length()<4)
			return false;
		Location l= new Location();
		
		return move(l.parseMove(move));
	}
		/**
	 loc1 : "A1", 
	 * */
	public boolean move(List<ILocation> ll)
	{
		int x1=ll.get(0).getX();
		int y1=ll.get(0).getY();
		
		int x2=ll.get(1).getX();
		int y2=ll.get(1).getY();
		
		if (null==array[y1][x1])
			return false;
		if (!isMoveAllow(ll))
			return false;
		IPart p=array[y1][x1].isEatAllow(ll);
		if (!array[y1][x1].isMoveAllow(ll))
			return false;
		if(p!=null)
		{	remove(p);
		//black/white++
		}
		array[y2][x2]=array[y1][x1];
		
		array[y1][x1]=null;
		return true;
	}
	
	private void remove(IPart p) {
		for(int x=0;x<size;x+=1)
			for(int y=0;y<size;y+=1)
				if (array[y][x]==p)
					array[y][x]=null;
	}
	
	public List<String> getMoves(char team)
	{
		List<String> ls=new ArrayList();
		for(int x=0;x<size;x+=1)
			for(int y=0;y<size;y+=1)
				if (array[y][x]!=null && array[y][x].getTeam()==team)
					for(List<ILocation> ll :array[y][x].getMoves())
					ls.add(moveToString(ll));
		return ls;
	}

	public boolean isMoveAllow(List<ILocation> ll) {
		
		int x1=ll.get(0).getX();
		int y1=ll.get(0).getY();
		int x2=ll.get(1).getX();
		int y2=ll.get(1).getY();
		return x1>=0 && x2>=0 && y1>=0 && y2>=0 && x1<size && x2<size && y1<size && y2<size;
	}
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		BoardDames b=new BoardDames();
		b.init();
		BufferedReader readerin = new BufferedReader(new InputStreamReader(System.in));
		System.out.println(b.toString());
		/*
		 System.out.println(b.getpart("D2").getMoves().toString());//[D2E3, D2E1]
		System.out.println("C1"+b.getpart("C1").getMoves().toString());//[]
		System.out.println(b.getpart("D10").getMoves().toString());//D10E9
		System.out.println(b.getMoves('W'));
		 */
		while(b.getListPart('B').size()>0 &&b.getListPart('W').size()>0)
		{
			while(!b.move(readerin.readLine()));
			System.out.println(b.toString());
			System.out.println(b.getMoves('B'));
			String m=b.choiceMove('B');
			System.out.println("=>"+m);
				b.move(m);
			System.out.println(b.toString());
					
		}
	}
	private List<IPart> getListPart(char team) {
		List<IPart> ls=new ArrayList();
		for(int x=0;x<size;x+=1)
			for(int y=0;y<size;y+=1)
				if (array[y][x]!=null && array[y][x].getTeam()==team)
					ls.add(array[y][x]);
		return ls;
	}
	private String choiceMove(char c) {
		List<String>  ls=getMoves(c);
		int i=(int)(Math.random()*ls.size());
		
		return ls.get(i);
	}
	@Override
	public IPart getPart(ILocation l) {
		int x=l.getX();
		int y=l.getY();
		
		return array[y][x];
	}

	@Override
	public ILocation getLoc(IPart pown) {

		for(int x=0;x<size;x+=1)
			for(int y=0;y<size;y+=1)
				if (array[y][x]==pown)
				{
					 Location l=new Location();
					 l.setX(x);l.setY(y);
					 return l;
				}
				return null;
			}
	@Override
	public void clear() {
		for(int x=0;x<size;x+=1)
			for(int y=0;y<size;y+=1)
				array[y][x]=null;
		
	}
	@Override
	public void put(ILocation l, IPart part) {
		int x=l.getX();
		int y=l.getY();
array[y][x]=part;		
	}
	@Override
	public String moveToString(List<ILocation> ll) {
		String s="";
		for(ILocation l:ll)
			s+=l.getSLoc();
		return s;
	}
	@Override
	public boolean isMoveAllow(ILocation l1, ILocation l2) {
		List<ILocation> ll=new ArrayList();
		return isMoveAllow( ll);
	}

}
