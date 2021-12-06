/**
 * 
 */
package com.zoubworld.games.sudoku;

/**
 * @author Pierre Valleau
 
 *
 */
public class Cgrille {

	char g[][];
	/**
	 * 
	 */
	public Cgrille() {
		g= new char[Sudoku.N*Sudoku.N][Sudoku.N*Sudoku.N];
	}
	public Cgrille(String grille) {
		g= new char[Sudoku.N*Sudoku.N][Sudoku.N*Sudoku.N];
		parse(grille);
	}
public void parse(String grille)
{
	int i=0;
	for(String line:grille.split("\r\n"))
	{
		if(line!=null)
		g[i++]=line.toCharArray();
	}
}
public String toString()
{
	String s="";
	

	for(char [] l:g)
	{
		for(char c:l)
				s+=c;
		s+="\r\n";
	}
	return s;
	
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String s="671 9 35 \r\n"
				+ "   7   6 \r\n"
				+ " 25436917\r\n"
				+ " 93681472\r\n"
				+ "218374695\r\n"
				+ "7    9   \r\n"
				+ "    23  6\r\n"
				+ " 4  6  31\r\n"
				+ "356 48729\r\n";

	}
	public int missing()
	{
		int count=0;
		for(char [] l:g)
		{
			for(char c:l)
				if(c!=' ')
					count++;
		}
		return count;
	}
	public void solveExclude(Bgrille[] b) {
		for(char i=0;i<b.length;i++)
		{
			System.out.print((char)('1'+i)+":\r\n"+b[i]);
		
		for(int y=0;y<b[i].g.length;y++)
			for(int x=0;x<b[i].g[y].length;x++)
			{
				int count=0;
				for(int j=0;j<b[i].g.length;j++)							
					count+=b[i].g[j][x]?1:0;
				if (count==1)
				{
					for(int j=0;j<b[i].g.length;j++)							
						if(b[i].g[j][x])	
							g[j][x]=(char)('1'+i);
				}
				count=0;
				for(int j=0;j<b[i].g[y].length;j++)							
					count+=b[i].g[y][j]?1:0;
				if (count==1)
				{
					for(int j=0;j<b[i].g[y].length;j++)							
						if(b[i].g[y][j])	
							g[y][j]=(char)('1'+i);
				}
				count=0;
				int xi=(x/Sudoku.N)*Sudoku.N;
				int yi=(y/Sudoku.N)*Sudoku.N;
				//exclue on same box
				for(int yj=0;yj<Sudoku.N;yj++)
					for(int xj=0;xj<Sudoku.N;xj++)
					{
						count+=b[i].g[yi+yj][xi+xj]?1:0;	
					}

				if (count==1)
				{
					for(int yj=0;yj<Sudoku.N;yj++)
						for(int xj=0;xj<Sudoku.N;xj++)
							if(b[i].g[yi+yj][xi+xj])	
							g[yi+yj][xi+xj]=(char)('1'+i);
				}
				
			
			}
		}
	}
	public void apply(SetCgrille scg) {
		for(int y=0;y<scg.g.length;y++)
			for(int x=0;x<scg.g[y].length;x++)
			{
				if(scg.g[y][x].size()==1)
					for(String s:scg.g[y][x])
					g[y][x]=s.charAt(0);
			}
	}

}
