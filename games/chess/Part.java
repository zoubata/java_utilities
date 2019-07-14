package com.zoubworld.games.chess;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.games.IPart;

public abstract class Part extends com.zoubworld.games.Part implements IPart {

	@Override
	public IPart isEatAllow(int x1, int y1, int x2, int y2) {
		// TODO Auto-generated method stub
		if(isMoveAllow(x1, y1, x2, y2))
		return getBoard().getPart(x2, y2);
	return null;
	}

	
}
