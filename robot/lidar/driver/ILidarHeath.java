package com.zoubworld.robot.lidar.driver;

public interface ILidarHeath {

	void print();
	/**
	 * @return the status
	 */
	public int getStatus();

	/**
	 * @return the error_code
	 */
	public int getError_code();

}