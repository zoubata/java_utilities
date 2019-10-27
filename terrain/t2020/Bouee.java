package com.zoubworld.terrain.t2020;

import com.zoubworld.geometry.Unit;
import com.zoubworld.java.svg.ItoSvg;
import com.zoubworld.java.utils.svg.ISvgObject;
import com.zoubworld.terrain.IObject;

public class Bouee implements IObject,ItoSvg {

	String color;
	public Bouee(String color) {
		this.color=color;
	}

	@Override
	public Double getX0() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getY0() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getT0() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Double getProbaCoord(Double Radius) {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public String toSvg() {
		return "<circle cx=\""+Unit.MtoMm(getX0())+"mm\" cy=\""+Unit.MtoMm(getY0())+"\" r=\""+Unit.MtoMm(0.072/2)+"\" stroke=\""+color+"\"/>";
	
	}

}
