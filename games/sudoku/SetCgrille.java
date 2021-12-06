/**
 * 
 */
package com.zoubworld.games.sudoku;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Pierre Valleau
 *
 */
public class SetCgrille {

	Set<String> g[][];
	/**
	 * 
	 */
	public SetCgrille() {
		g= new Set[Sudoku.N*Sudoku.N][Sudoku.N*Sudoku.N];
		for(int y=0;y<g.length;y++)
			for(int x=0;x<g[y].length;x++)
		{
				g[y][x]=new HashSet<String>();
		for(char i=0;i<Sudoku.N*Sudoku.N;i++)
			g[y][x].add(""+(char)('1'+i));
		}	
	}
	public boolean isComplete( )
	{
		boolean b=true;
		for(int y=0;y<g.length;y++)
			for(int x=0;x<g[y].length;x++)
	if(g[y][x].size()!=1)
		b=false;
		return b;
	}
	public SetCgrille solve()
	{
		SetCgrille r=null;
		if(!isvalid())
			return null;
		if(isComplete())
			return this;
		for(int y=0;y<g.length;y++)
			for(int x=0;x<g[y].length;x++)
			{
				if (g[y][x].size()==0)
					return null;
				else
					if (g[y][x].size()>1)
					{
						for(String s:g[y][x])
						{
							SetCgrille cg=doAClone();
							cg.g[y][x]=new HashSet<String>();
							cg.g[y][x].add(s);
							cg.filter();
							if(!cg.isvalid())
								return null;
//	   						  System.out.println(cg);
							cg=cg.solve();
							if(cg!=null && cg.isvalid())
							  System.out.println(r=cg);
						}
					}
				
			}
		return r;
	}
	private SetCgrille doAClone() {
		SetCgrille r=new SetCgrille();

		r.g= new Set[g.length][g[0].length];
		for(int y=0;y<r.g.length;y++)
			for(int x=0;x<r.g[y].length;x++)
		{
				r.g[y][x]=new HashSet<String>();
				r.g[y][x].addAll(g[y][x]);
		}
		return r;
	}
	public boolean isvalid( )
	{
		boolean b=true;
		for(int y=0;y<g.length;y++)
			for(int x=0;x<g[y].length;x++)
	if(g[y][x].size()==0)
		b=false;
	
		for(int y=0;y<g.length;y++)
		{
			Set<String> s=new HashSet();
			for(int x=0;x<g[y].length;x++)
				if (s.contains(g[y][x]))
				return false;
				else
					if (g[y][x].size()==1)
					s.addAll(g[y][x]);
		}
	
			for(int x=0;x<g[0].length;x++)
				
		{
			Set<String> s=new HashSet();
			for(int y=0;y<g.length;y++)
						if (s.contains(g[y][x]))
				return false;
				else
					if (g[y][x].size()==1)
					s.addAll(g[y][x]);
		}
		return b;
	}
	
	public void filter()
	{
		Cgrille cg=new Cgrille();
		for(int y=0;y<g.length;y++)
			for(int x=0;x<g[y].length;x++)
			{
				if (g[y][x].size()==1)
					for(String s:g[y][x])
					cg.g[y][x]=s.charAt(0);
				else
				cg.g[y][x]=' ';
						
			}
		filter(cg);
	}
	public void filter(Cgrille cg)
			{
				for(int y=0;y<cg.g.length;y++)
					for(int x=0;x<cg.g[y].length;x++)
			{
				if(cg.g[y][x]!=' ')
					{
					g[y][x].clear();
					String s=""+(char)(cg.g[y][x]);
									
					//exclue on same colunm
					for(int i=0;i<cg.g.length;i++)							
						g[i][x].remove(s);
					//exclue on same row
					for(int i=0;i<cg.g[y].length;i++)							
						g[y][i].remove(s);
					int xi=(x/Sudoku.N)*Sudoku.N;
					int yi=(y/Sudoku.N)*Sudoku.N;
					//exclue on same box
					for(int yj=0;yj<Sudoku.N;yj++)
						for(int xj=0;xj<Sudoku.N;xj++)
						{
							g[yi+yj][xi+xj].remove(s);	
						}
					g[y][x].add(s);		
					
					
					}
				}
	}
class count
{
Map<String,Integer> m=new HashMap<String,Integer> ();
public void add(SetCgrille g)
{
	for(Set<String>[] l:g.g)
		add(l);
}
public void add(SetCgrille g, int x)
{
	for(Set<String>[] l:g.g)
		add(l[x]);
}
	public void add(Set<String>[] l)
	{
		for(Set<String> e:l)
			{
			add(e);		
	}
}

public Integer get(String e) {
	return m.get(e);
}
public void add(Set<String> s)
{
for(String e:s)
{
Integer i=m.get(e);
if (i==null)
	i=0;
i++;
m.put(e,i);

}
}
}
public String toString()
{
	String s="";
	

	for(Set [] l:g)
	{
		for(Set<String> c:l)
		{
			if(c.size()==1)
				for(String e:c)
				s+="  "+e+"  ";
			else
				if (c.isEmpty())
					s+=".....";
				else
					
					if(c.size()==2)
						{s+="---";for(String e:c)
							s+=e;}
					else						
						if(c.size()==3)
							{s+="--";for(String e:c)
								s+=e;}
						else						
							if(c.size()==4)
								{s+="-";
								for(String e:c)
									s+=e;}
							else						
								if(c.size()==5)
									{for(String e:c)
										s+=e;}
							else						
						s+="*****";
		s+=" ";
		}
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
s=		" 17   34 \r\n" + 
		"6  3 4178\r\n" + 
		"  3      \r\n" + 
		"      58 \r\n" + 
		" 4  6    \r\n" + 
		"3 2      \r\n" + 
		"    28  3\r\n" + 
		"   6 5  1\r\n" + 
		" 7    2  ";
      s="8   7 952\r\n" + 
		"   2  461\r\n" + 
		"96241587 \r\n" + 
		"69 3  14 \r\n" + 
		"1 7  6  8\r\n" + 
		"  38  7 6\r\n" + 
		"        4\r\n" + 
		"  17  3  \r\n" + 
		" 3  52 8 ";
      s="   9  48 \r\n" + 
    			"6 3     9\r\n" + 
    			"     6   \r\n" + 
    			"        4\r\n" + 
    			" 7 6     \r\n" + 
    			"54  78   \r\n" + 
    			"  2 54 1 \r\n" + 
    			"36       \r\n" + 
    			"      85 ";

s="   9  48 \r\n" + 
		"6 3     9\r\n" + 
		"     6   \r\n" + 
		"        4\r\n" + 
		" 7 6     \r\n" + 
		"54  78   \r\n" + 
		"  2 5461 \r\n" + 
		"36       \r\n" + 
		"      85 ";

          
   
   Cgrille cg=new Cgrille(s);

		System.out.println(cg);
		SetCgrille scg=new SetCgrille();
		scg.filter(cg);
		System.out.println(scg);
	scg.solve();

	}
	}
