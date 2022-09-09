package com.zoubworld.java.utils.compress.binalgo;

import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.file.IBinaryReader;
import com.zoubworld.java.utils.compress.file.IBinaryWriter;

interface ICoder
{
	public void encodeSymbol(IModel model,IHistory history,ISymbol symbol,IBinaryWriter out);
	public ISymbol decodeSymbol(IModel model,IHistory history, IBinaryReader in);
	
}

