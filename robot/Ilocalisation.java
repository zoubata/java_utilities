package com.zoubworld.robot;

import com.zoubworld.geometry.Point;

public interface Ilocalisation {

	public abstract Double getX0();

	public abstract void setX0(Double x0);
	public abstract void setPoint(Point p0);

	public abstract Double getY0();
	public abstract Point getPoint0();

	public abstract void setY0(Double y0);

	public abstract Double getTheta0();

	public abstract void setTheta0(Double theta0);

}