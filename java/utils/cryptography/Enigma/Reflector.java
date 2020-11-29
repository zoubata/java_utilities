// This is a SUGGESTED skeleton file.  Throw it away if you don't use it.
package com.zoubworld.java.utils.cryptography.Enigma;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a reflector in the enigma.
 * 
 * @author
 */
public class Reflector extends Rotor {
	int[] reflection;

	public static Reflector make(String str) {
		char[] s = str.trim().replace(" ", "").toCharArray();
		int[] cipher = new int[Machine.CharSetSize];
		for (int i = 0; i < cipher.length; i++) {
			cipher[i] = toIndex(s[i]);
		}
		return new Reflector(cipher);
	}

	public static Reflector make(int isize, int germe) {
		int[] cipher = new int[isize];
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < cipher.length; i++)
			list.add(i);
		for (int i = 0; i < cipher.length; i++) {
			cipher[i] = list.remove((i * germe) % list.size());
		}
		return new Reflector(cipher);

	}

	public Reflector(int[] r) {
		reflection = r;
	}

	int convertForward(int p) {
		return ((reflection[((p) % Machine.CharSetSize + Machine.CharSetSize) % Machine.CharSetSize])
				% Machine.CharSetSize + Machine.CharSetSize) % Machine.CharSetSize;
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
