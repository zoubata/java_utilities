/**
 * 
 */
package com.zoubworld.chemistry;

/**
 * @author Pierre
 *
 */
public class Isotope extends Atom {

	static public Isotope Factory(Atom at, int neutron)
	{
		Isotope i=new Isotope(at);
		i.neutron+=neutron;
		return i;
	}
	/**
	 * 
	 */
	public Isotope() {
			super();
	}

	/**
	 * @param at
	 */
	public Isotope(Atom at) {
		super(at);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name2
	 * @param sym
	 * @param num
	 */
	public Isotope(String name2, String sym, int num) {
		super(name2, sym, num);
		// TODO Auto-generated constructor stub
	}

}
