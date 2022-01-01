/**
 * 
 */
package com.zoubworld.chemistry;

/**
 * @author Pierre Valleau
 *
 */
public class Ion extends Atom {

	/**
	 * 
	 */
	public Ion() {
		
	}
	static public Ion Factory(Atom at, int charge)
	{
		Ion i=new Ion(at);
		i.electron+=charge;
		return i;
	}
	/**
	 * @param name2
	 * @param sym
	 * @param num
	 */
	public Ion(String name2, String sym, int num) {
		super(name2, sym, num);
		// TODO Auto-generated constructor stub
	}
	public Ion(Atom at) {
		super(at);
	}

}
