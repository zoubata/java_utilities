/**
 * 
 */
package com.zoubworld.java.games.utils;

/**
 * @author Pierre valleau
 *
 */
public class Plateau {
	
	Case data[][];

	/**
	 * 
	 */
	public Plateau() {
		data=null;
	}
	
	public Plateau(int x,int y) {
   data=new Case[y][x];   
   for(int i=0;i<y;i++)
	   data[i]=new Case[x];
	}

	/**
	 * @return the data
	 */
	public Case[][] getData() {
		return data;
	}
	
	public Case getCase(int x,int y) {
		if (data==null)
			return null;
		if (data.length<=y || data[y]==null)
			return null;
		
		if (data[y].length<=x )
			return null;

		return data[y][x];
	}
	public void setCase(int x,int y, Case c) {
		data[y][x]=c;
	}
	public int getXsize()
	{
		return data[0].length;
	}
	public int getYsize()
	{
		return data.length;
	}
	public String toString()
	{
		String s=" ";
		for(int x=0;x<getXsize();x++)
			s+="\t"+(x+1);
		s+="\r\n";
		for(int y=0;y<getYsize();y++)
		{
			s+=""+(char)('a'+(char)y);
			for(int x=0;x<getXsize();x++)
					s+="\t"+((getCase(x, y)==null)?"":getCase(x, y).toString());
			s+="\r\n";
		}
		return s;
	}

	/**
	 * @param data the data to set
	 */
	protected void setData(Case[][] data) {
		this.data = data;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public int NbCaseFree() {
		int count=0;
		for(int y=0;y<getYsize();y++)
		{
			for(int x=0;x<getXsize();x++)
				if(getCase(x, y)==null)
					count++;
				else
					if(getCase(x, y).getValue()==null)
						count++;
			
		}
		return count;
	}

}
