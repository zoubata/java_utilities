package com.zoubworld.chemistry;

import java.util.Map;

public interface IAtom {

	/**
	 * @return the electron
	 */
	int getNumberOfElectron();

	int getNumberOfNucleus();

	/**
	 * @param electron the electron to set
	 */
	void setElectron(int electron);

	/**
	 * @return the symbol
	 */
	String getSymbol();

	/**
	 * @return the proton
	 */
	int getNumberOfProton();

	/**
	 * @return the neutron
	 */
	Integer getNumberOfNeutron();

	String getEletronStructure();

	// https://simple.wikipedia.org/wiki/Electron_shell
	Integer getEletronsLastShell();

	Map<String, String> getProperty();

}