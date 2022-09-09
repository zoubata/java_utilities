package com.zoubworld.java.utils.compress.binalgo;

import com.zoubworld.java.utils.compress.ISymbol;

interface IHistory
{
	/** update history symbol
	 * */
	public void incrementContexts(ISymbol symbol);
}
