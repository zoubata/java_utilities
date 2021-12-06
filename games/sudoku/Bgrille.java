/**
 * 
 */
package com.zoubworld.games.sudoku;

/**
 * @author Pierre Valleau
 *
 */
public class Bgrille {

	boolean g[][];
	public String toString()
	{
		String s="";
		

		for(boolean [] l:g)
		{
			for(boolean c:l)
					s+=c?"1":"0";
			s+="\r\n";
		}
		return s;
		
		}
	/**
	 * 
	 */
	public Bgrille() {
		g= new boolean[Sudoku.N*Sudoku.N][Sudoku.N*Sudoku.N];
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public static Bgrille exclude(Cgrille cg, char c) {
		Bgrille b=new Bgrille();
		b.vtrue();
		for(int y=0;y<cg.g.length;y++)
			for(int x=0;x<cg.g[y].length;x++)
			{
				//exclue if already filled
				if (cg.g[y][x]!=' ')
					b.g[y][x]=false;
				
				if (cg.g[y][x]==c)
				{
					//exclue on same colunm
					for(int i=0;i<cg.g.length;i++)							
						b.g[i][x]=false;
					//exclue on same row
					for(int i=0;i<cg.g[y].length;i++)							
						b.g[y][i]=false;
					int xi=(x/Sudoku.N)*Sudoku.N;
					int yi=(y/Sudoku.N)*Sudoku.N;
					//exclue on same box
					for(int yj=0;yj<Sudoku.N;yj++)
						for(int xj=0;xj<Sudoku.N;xj++)
						{
							b.g[yi+yj][xi+xj]=false;	
						}
				}
			}
		return b;
	}
	public void vtrue() {
		for(int y=0;y<g.length;y++)
			for(int x=0;x<g[y].length;x++)
		g[y][x]=true;
	}

}
