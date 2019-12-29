/**
 * 
 */
package com.zoubworld.terrain.t2020;


import com.zoubworld.geometry.Unit;
import com.zoubworld.java.svg.ItoSvg;
/**
 * @author  Pierre Valleau
 *
 */
import com.zoubworld.terrain.IObject;

public class Wall implements IObject,ItoSvg {

	@Override
	public Double getX0() {
		// TODO Auto-generated method stub
		return 0.0;
	}

	@Override
	public Double getY0() {
		// TODO Auto-generated method stub
		return 0.0;
	}

	@Override
	public Double getT0() {
		// TODO Auto-generated method stub
		return 0.0;
	}

	@Override
	public Double getProbaCoord(Double Radius) {
		// TODO Auto-generated method stub
		return 0.0;
	}

	@Override
	public String toSvg() {
		// TODO Auto-generated method stub
		return "<circle cx=\""+Unit.MtoMm(getX0())+"mm\" cy=\""+Unit.MtoMm(getY0())+"\" r=\""+Unit.MtoMm(0.005)+"\" stroke=\"grey\"/>";
	}

}
