package com.zoubworld.robot.lidar.driver;

import com.zoubworld.robot.lidar.RP.RpLidarScan;

public interface ILidarScan {

	/**
	 * Copies 'scan' into this scan.
	 *
	 * @param scan The scan which is to be copied.
	 */
	void set(ILidarScan scan);

	void reset();

	boolean isInvalid(int which);

}