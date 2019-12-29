// This is a SUGGESTED skeleton file.  Throw it away if you don't use it.
package com.zoubworld.java.utils.cryptography.Enigma;

import java.util.ArrayList;
import java.util.List;

/** Class that represents a rotor in the enigma machine.
 *  @author
 */
class Rotor {
    // This needs other methods, fields, and constructors.
	public static Rotor rotor(String str, String notches){
		char[] s = str.trim().replace(" ", "").toCharArray();
		int[] cipher = new int[Machine.CharSetSize];
		for (int i = 0; i< cipher.length; i++){
			cipher[i] = toIndex(s[i]);
		}
		s = notches.trim().replace(" and ", "").toCharArray();
		if (s.length == 2){
			return new Rotor(cipher, toIndex(s[0]), toIndex(s[1]));
		} else {
			return new Rotor(cipher, toIndex(s[0]));
		}
		
	}
	public static Rotor rotor(int isize, int germe, int pos) {
		int[] cipher = new int[isize];
		List<Integer> list=new ArrayList<Integer>();
		for (int i = 0; i< cipher.length; i++)
			list.add(i);	
		for (int i = 0; i< cipher.length; i++){
			cipher[i] = list.remove((i*germe)%list.size());
		}
		return new Rotor(cipher, toIndex(pos));
		
	}
	public Rotor(int[] c, int notch1, int notch2) {
		this.notch1 = notch1;
		this.notch2 = notch2;
		cipher = c;
		createBCipher();
	}
	public Rotor(int[] c, int notch1) {
		this.notch1 = notch1;
		cipher = c;
		createBCipher();
	}
	public Rotor(){
		
	}

	private void createBCipher() {
		for(int i =0; i<cipher.length; i++)
			bcipher[cipher[i]] = i;
	}
    /** Assuming that P is an integer in the range 0..25, returns the
     *  corresponding upper-case letter in the range A..Z. */
    static char toLetter(int p) {
    	if (Machine.CharSetSize==26)
        return (char)(p + 'A');
    	return (char)p;
    }

    /** Assuming that C is an upper-case letter in the range A-Z, return the
     *  corresponding index in the range 0..25. Inverse of toLetter. */
    static int toIndex(int c) {
        if (Machine.CharSetSize==26)
        	return ((char)c) - 'A';
        return c%Machine.CharSetSize;
    }

    /** Return my current rotational position as an integer between 0
     *  and 25 (corresponding to letters 'A' to 'Z').  */
    int getPosition() {
        return position;
    }

    /** Set getPosition() to POSN.  */
    void setPosition(int posn) {
        position = posn;
    }

    /** Return the conversion of P (an integer in the range 0..25)
     *  according to my permutation. */
    int convertForward(int p) {
        return ((cipher[((p+position)%Machine.CharSetSize+Machine.CharSetSize)%Machine.CharSetSize]-position)%Machine.CharSetSize+Machine.CharSetSize)%Machine.CharSetSize;
    }

    /** Return the conversion of E (an integer in the range 0..25)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        return ((bcipher[((e+position)%Machine.CharSetSize+Machine.CharSetSize)%Machine.CharSetSize]-position)%Machine.CharSetSize+Machine.CharSetSize)%Machine.CharSetSize;
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return (position == notch1 || position == notch2);
    }

    /** Advance me one position. */
    void advance() {
        position = (position+1) % Machine.CharSetSize;
    }

    /** My current position (index 0..25, with 0 indicating that 'A'
     *  is showing). */
    private int position;
    private int[] cipher = new int[Machine.CharSetSize];
    private int[] bcipher = new int[Machine.CharSetSize];
    private int notch1 = -1;
    private int notch2 = -1;
	

}
