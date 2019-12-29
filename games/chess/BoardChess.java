/**
 * 
 */
package com.zoubworld.games.chess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.zoubworld.games.ILocation;
import com.zoubworld.games.IPart;
import com.zoubworld.games.Iboard;
import com.zoubworld.games.Location;
import com.zoubworld.games.chess.parts.Bishop;
import com.zoubworld.games.chess.parts.King;
import com.zoubworld.games.chess.parts.Knight;
import com.zoubworld.games.chess.parts.Pown;
import com.zoubworld.games.chess.parts.Queen;
import com.zoubworld.games.chess.parts.Rook;

/**
 * @author Pierre Valleau
 *
 */
public class BoardChess implements Iboard {


	public void put(ILocation l, IPart part) {
		int x=l.getX();
		int y=l.getY();
array[y][x]=part;		
	}
	static final int size=8;
	IPart array[][];
	public IPart getpart(String location)
	{
		int x=location.codePointAt(1)-'1';
		int y=location.codePointAt(0)-'A';
		return array[y][x];
	}
	public void init()
	{

		array=new IPart[size][];
		for(int y=0;y<size;y++)
		array[y]=new IPart[size];
		for(int x=0;x<size;x++)
			array[1][x]=new Pown('W');
		array[0][0]=new Rook('W');
		array[0][7]=new Rook('W');
		array[0][1]=new Knight('W');
		array[0][6]=new Knight('W');
		array[0][2]=new Bishop('W');
		array[0][5]=new Bishop('W');
		array[0][3]=new Queen('W');
		array[0][4]=new King('W');
		
		for(int x=0;x<size;x++)
			array[6][x]=new Pown('B');
		array[7][0]=new Rook('B');
		array[7][7]=new Rook('B');
		array[7][1]=new Knight('B');
		array[7][6]=new Knight('B');
		array[7][2]=new Bishop('B');
		array[7][5]=new Bishop('B');
		array[7][3]=new Queen('B');
		array[7][4]=new King('B');
	}
	
	public String toString()
	{
		String s="";
		if (array!=null)
			s=" 1 2 3 4 5 6 7 8\r\n";
		for(int y=0;y<size;y++)
		{
			s+=""+(char)('A'+(char)y);
			if (array[y]!=null)
			for(int x=0;x<size;x++)
		{
				if (array[y][x]!=null)
				s+=array[y][x].toString();
				else
				s+="  ";
		}
			s+="\r\n";
		}		
		return s;		
	}
	
	
	/** display the list of move.
	 *   .  XXXXXX
	 *        X*X
	 start point : *
	 end point #
	 * */
	public String toString(List<List<ILocation>> move2)
	{
		char move[][];
		move=new char[size][];
		for(int y=0;y<size;y++)
		{
			move[y]=new char[size];
			for(int x=0;x<size;x++)
		{move[y][x]=' ';
				
		}}
		for(List<ILocation> amv:move2)
		{
			int i=0;
			for(ILocation l:amv)
			{
				int x=l.getX();
				int y=l.getY();
				if(i==0)
					move[y][x]='*';
				else
					move[y][x]='+';
				i++;
			}
		}
		String s="";
		if (array!=null)
			s=" 1 2 3 4 5 6 7 8\r\n";
		for(int y=0;y<size;y++)
		{
			s+=""+(char)('A'+(char)y);
			if (array[y]!=null)
			for(int x=0;x<size;x++)
		{
				if (array[y][x]!=null)
					s+=array[y][x].toString();
					else
					s+=" "+move[y][x];
		}
			s+="\r\n";
		}		
		return s;		
	}

	public void clear() {
		for(int x=0;x<size;x+=1)
			for(int y=0;y<size;y+=1)
				array[y][x]=null;
		
	}
	/**
	 * 
	 */
	public BoardChess() {
		// TODO Auto-generated constructor stub
	}
	/**
	move : "A1B1", 
	 * */
	public boolean move(String move)
	{
		String loc1=move.substring(0, 2);
				String loc2=move.substring(2, 4);
		return move(loc1,loc2);
	}
	/**
	 loc1 : "A1", 
	 * */
	public boolean move(String loc1,String loc2)
	{
		int x1=loc1.codePointAt(1)-'1';
		int y1=loc1.codePointAt(0)-'A';
		int x2=loc2.codePointAt(1)-'1';
		int y2=loc2.codePointAt(0)-'A';

		array[y2][x2]=array[y1][x1];
		array[y1][x1]=null;
		return true;
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		BoardChess b=new BoardChess();
		b.init();
		BufferedReader readerin = new BufferedReader(new InputStreamReader(System.in));
		System.out.println(b.toString());
		
		while(b.move(readerin.readLine()))
			System.out.println(b.toString());
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
		List<ILocation> ll=new ArrayList<ILocation>();
		ll.add(l1);
		ll.add(l2);
		return isMoveAllow( ll);
	}

	public boolean isMoveAllow(List<ILocation> ll) {
		
		int x1=ll.get(0).getX();
		int y1=ll.get(0).getY();
		int x2=ll.get(1).getX();
		int y2=ll.get(1).getY();
		return x1>=0 && x2>=0 && y1>=0 && y2>=0 && x1<size && x2<size && y1<size && y2<size;
	}
	
	@Override
	public IPart getPart(ILocation l) {
		int x=l.getX();
		int y=l.getY();
		
		return array[y][x];
	}
	@Override
	public int sizeY() {
		// TODO Auto-generated method stub
		return size;
	}
	@Override
	public int sizeX() {
		// TODO Auto-generated method stub
		return size;
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


}
