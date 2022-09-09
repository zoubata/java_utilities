package com.zoubworld.robot.lidar.driver;

public interface ILidarMeasurement {

	/**
	 * @return the start
	 */
	boolean isStart();

	/**
	 * @param start the start to set
	 */
	void setStart(boolean start);

	/**
	 * @return the quality
	 */
	int getQuality();

	/**
	 * @param quality the quality to set
	 */
	void setQuality(int quality);

	/**
	 * @return the angle
	 */
	int getAngle();

	/**
	 * @param angle the angle to set
	 */
	void setAngle(int angle);

	/**
	 * @return the distance
	 */
	int getDistance();

	/**
	 * @param distance the distance to set
	 */
	void setDistance(int distance);

	/**
	 * @return the timeMilli
	 */
	long getTimeMilli();

	/**
	 * @param timeMilli the timeMilli to set
	 */
	void setTimeMilli(long timeMilli);

	boolean isInvalid();

}