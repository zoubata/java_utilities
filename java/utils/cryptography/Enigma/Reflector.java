// This is a SUGGESTED skeleton file.  Throw it away if you don't use it.
package com.zoubworld.java.utils.cryptography.Enigma;

/** Class that represents a reflector in the enigma.
 *  @author
 */
class Reflector extends Rotor {
	int[] reflection;
	
	public static Reflector make(String str){
		char[] s = str.trim().replace(" ", "").toCharArray();
		int[] cipher = new int[Machine.CharSetSize];
		for (int i = 0; i< cipher.length; i++){
			cipher[i] = toIndex(s[i]);
		}
		return new Reflector(cipher);
	}
	
	public Reflector(int[] r){
		reflection = r;
	}
        
    int convertForward(int p) {
        return ((reflection[((p)%Machine.CharSetSize+Machine.CharSetSize)%Machine.CharSetSize])%Machine.CharSetSize+Machine.CharSetSize)%Machine.CharSetSize;
    }

    /** Returns a useless value; should never be called. */
    @Override
    int convertBackward(int unused) {
        throw new UnsupportedOperationException();
    }

    /** Reflectors do not advance. */
    @Override
    void advance() {
    }

}
