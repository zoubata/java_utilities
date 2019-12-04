/**
 * 
 */
package com.zoubworld.java.utils.compress;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Pierre Valleau
 *
 */
public class CodingSet {

	Map<ISymbol,ICode> m;
	
	/**
	 * 
	 */
	public CodingSet() {
		m=new HashMap();
		for (byte c=0;c<256;c++)
			m.put(new Symbol(c), new Code( c));
		for (short c=256;c<Symbol.getNbSymbol();c++)
			m.put(new Symbol(c), new Code( c));
			
	}

}
