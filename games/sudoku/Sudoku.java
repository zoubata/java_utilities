/**
 * 
 */
package com.zoubworld.games.sudoku;

/**
 * @author Pierre Valleau
 *
 */
public class Sudoku {

	static int N=3;
	/**
	 * 
	 */
	public Sudoku() {
		// TODO Auto-generated constructor stub
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
		s="72    9 3\r\n" + 
				"185  64 7\r\n" + 
				"   7 25 8\r\n" + 
				" 4625    \r\n" + 
				" 3 5 9   \r\n" + 
				"        5\r\n" + 
				"452   836\r\n" + 
				"67982 1  \r\n" + 
				"     4  2";
      s=" 9 274863\r\n" + 
		"7 6  1294\r\n" + 
		"428  9 7 \r\n" + 
		"  3715   \r\n" + 
		" 578 231 \r\n" + 
		"841  6725\r\n" + 
		"5  4 713 \r\n" + 
		" 74 23  8\r\n" + 
		" 1 958  7";
s="79352  4 \r\n" + 
		"64 9   85\r\n" + 
		"2  461739\r\n" + 
		"47  92 53\r\n" + 
		"5 8 4 9 7\r\n" + 
		"1  73 4  \r\n" + 
		"81  573  \r\n" + 
		"3  2 9514\r\n" + 
		"95  148 6";

s=      " 1    4  \r\n" + 
		"46   7 1 \r\n" + 
		" 59    6 \r\n" + 
		" 4675  81\r\n" + 
		" 7  94356\r\n" + 
		"       74\r\n" + 
		"  7  65  \r\n" + 
		"89       \r\n" + 
		"6       3";
s="87  63495\r\n" + 
		" 5   76  \r\n" + 
		"   9  817\r\n" + 
		"7185 42  \r\n" + 
		" 9    75 \r\n" + 
		" 653 1 8 \r\n" + 
		" 81 3    \r\n" + 
		"3 7458169\r\n" + 
		"5   1   8";

      s="  2   3  \r\n" + 
		"     7  5\r\n" + 
		"  74 5   \r\n" + 
		"15    7  \r\n" + 
		"3  29   6\r\n" + 
		"9      8 \r\n" + 
		"   5     \r\n" + 
		"     9  7\r\n" + 
		" 3    19 ";

		Cgrille cg=new Cgrille(s);
		System.out.print("Grille:\r\n"+cg);
		Bgrille b[]=new Bgrille[N*N];
		int m=N*N*N*N;
		while(!(m==cg.missing()))
		{
			m=cg.missing();	
			//try by exclude
			for(char i=0;i<b.length;i++)
				b[i]=Bgrille.exclude(cg,(char)('1'+i));
			cg.solveExclude(b);
			
			//try by possible at 1
			System.out.print("Grille:\r\n"+cg);
			SetCgrille scg=new SetCgrille();
			scg.filter(cg);
			cg.apply(scg);
			System.out.print("Grille Possible:\r\n"+scg);
			
			//try to by luck.
			
		}
		 
		
		
	}

}
