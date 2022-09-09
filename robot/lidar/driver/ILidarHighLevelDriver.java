package com.zoubworld.robot.lidar.driver;

import com.zoubworld.robot.lidar.RP.RpLidarDeviceInfo;
import com.zoubworld.robot.lidar.RP.RpLidarMeasurement;

public interface ILidarHighLevelDriver {

	/**
	 * Connects to the LIDAR
	 * @param device Which device the lidar is connected to
	 * @param totalCollect How many measurements should it collect in a single scan.  If <= 0 it will automatically
	 *                     determine the number in a complete scan and use that.
	 * @return true if successful or false if not
	 */
	boolean initialize(String device, int totalCollect);

	/**
	 * Disconnects and shuts down the connection to the LIDAR
	 */
	void stop();

	/**
	 * Returns the most recent complete scan which has been returned by this function.
	 * @param scan (output) Where the complete scan is written to
	 * @param timeout If > 0 then it will wait at most that amount of time for a complete scan
	 * @return true if it has a complete scan or false if it timed out.
	 */
	boolean blockCollectScan(ILidarScan scan, long timeout);

	void handleMeasurement(ILidarMeasurement measurement);

	void handleDeviceHealth(ILidarHeath health);

	void handleDeviceInfo(ILidarDeviceInfo info);

	boolean isInitialized();

}