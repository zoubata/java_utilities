package com.zoubworld.java.games.morpion;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.zoubworld.java.games.utils.Case;
import com.zoubworld.java.games.utils.Plateau;

public class Morpion {

	public Morpion() {
		plateau = new Plateau(3, 3);
	}

	Plateau plateau;

	public static void main(String[] args) {
		Morpion m = new Morpion();
		boolean gagnant = false;
		BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
		System.out.println(m.getPlateau().toString());
		while ((m.getPlateau().NbCaseFree() > 0) && (!gagnant)) {

			System.out.println("à " + m.joueurCourant.toString() + " de jouer !\n");

			try {
				if (!m.play(buffer.readLine()))
					System.out.println("coup non valide !\n Rejoue !");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			gagnant = m.isAgagnant();
			System.out.println(m.getPlateau().toString());

		}

		if (gagnant)
			System.out.println("il y a un gagnant : " + m.Getgagnant().toString() + ", \nPartie terminer!");
		else
			System.out.println("Match nul ! \nPartie terminer!");

	}

	private boolean isAgagnant() {
		return Getgagnant() != null;
	}

	private Case Getgagnant() {

		int x = 0;
		int y = 0;
		for (x = 0; x < 3; x++) {
			if ((plateau.getCase(x, 0) == plateau.getCase(x, 1)) && (plateau.getCase(x, 1) == plateau.getCase(x, 2))
					&& (plateau.getCase(x, 0) != null))
				return plateau.getCase(x, 0);
		}
		for (y = 0; y < 3; y++) {
			if ((plateau.getCase(0, y) == plateau.getCase(1, y)) && (plateau.getCase(1, y) == plateau.getCase(2, y))
					&& (plateau.getCase(0, y) != null))
				return plateau.getCase(0, y);
		}

		if ((plateau.getCase(0, 0) == plateau.getCase(1, 1)) && (plateau.getCase(1, 1) == plateau.getCase(2, 2))
				&& (plateau.getCase(0, 0) != null))
			return plateau.getCase(1, 1);

		if ((plateau.getCase(0, 2) == plateau.getCase(1, 1)) && (plateau.getCase(1, 1) == plateau.getCase(2, 0))
				&& (plateau.getCase(0, 2) != null))
			return plateau.getCase(1, 1);

		return null;
	}

	int count = 0;
	Case cX = new Case('X');
	Case cO = new Case('O');
	Case joueurCourant = cX;

	/**
	 * get ln to play on cell a1,c3,b3,....
	 * 
	 * @param readLine
	 */
	private boolean play(String readLine) {
		byte l = 0;
		if (readLine.length() > 1)
			l = readLine.getBytes()[0];
		byte n = 0;
		if (readLine.length() > 1)
			n = readLine.getBytes()[1];
		int x = -1;
		int y = -1;
		if (l == (byte) 'a')
			y = 0;
		if (l == (byte) 'A')
			y = 0;
		if (l == (byte) 'b')
			y = 1;
		if (l == (byte) 'B')
			y = 1;
		if (l == (byte) 'c')
			y = 2;
		if (l == (byte) 'C')
			y = 2;

		if (n == (byte) '1')
			x = 0;

		if (n == (byte) '2')
			x = 1;
		if (n == (byte) '3')
			x = 2;
		Case c = null;

		if ((x >= 0) && (y >= 0) && plateau.getCase(x, y) == null)// coup valid
		{
			if (count % 2 == 0)
				c = cX;
			else
				c = cO;
			plateau.setCase(x, y, (c));
			count++;
			if (count % 2 == 0)
				joueurCourant = cX;
			else
				joueurCourant = cO;
			return true;
		}
		return false;

	}

	/**
	 * @return the plateau
	 */
	public Plateau getPlateau() {
		return plateau;
	}

}
