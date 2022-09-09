package com.zoubworld.chemistry;

import java.util.Collection;

public interface IMolecule {

	/**
	 * @return the atoms
	 */
	Collection<IAtom> getAtoms();

	String toDot();

	String toString();

}