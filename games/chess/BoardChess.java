/**
 * 
 */
package com.zoubworld.games.chess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.zoubworld.games.IPart;
import com.zoubworld.games.chess.parts.Bishop;
import com.zoubworld.games.chess.parts.King;
import com.zoubworld.games.chess.parts.Knight;
import com.zoubworld.games.chess.parts.Pown;
import com.zoubworld.games.chess.parts.Queen;
import com.zoubworld.games.chess.parts.Rook;

/**
 * @author M43507
 *
 */
public class BoardChess {

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
			s=" 12345678\r\n";
		for(int y=0;y<size;y++)
		{
			s+=""+(char)('A'+(char)y);
			if (array[y]!=null)
			for(int x=0;x<size;x++)
		{
				if (array[y][x]!=null)
				s+=array[y][x].toString();
				else
				s+=" ";
		}
			s+="\r\n";
		}
		
		return s;
		
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

}
