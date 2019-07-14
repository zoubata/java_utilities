package com.zoubworld.geometry;

public interface iCoordTransformation {

	public abstract void rotate(double theta);

	public abstract void translation(double x, double y);
	/*
	x'=x+x0
	y'=y+y0
	
	rotation
	x'=x*cos(tetha)+y*sin(theta)
	y'=-x*sin(tetha)+y*cos(theta)
	
	}*/

}