package com.zoubworld.java.utils.compress.binalgo;

import com.zoubworld.java.utils.compress.ISymbol;

interface IModel
{
	public void incrementContexts(IHistory history, ISymbol symbol);
	public IHistory getHistory();
}