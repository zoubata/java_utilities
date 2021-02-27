package com.zoubworld.robot.lidar.RP;

import com.zoubworld.robot.lidar.driver.ILidarHeath;

/**
 * Packet which describes the sensor's health
 *
 * @author Peter Abeles
 */
public class RpLidarHeath implements ILidarHeath {
	public int status;
	public int error_code;

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @return the error_code
	 */
	public int getError_code() {
		return error_code;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarHeath#print()
	 */
	@Override
	public void print() {
		System.out.println("HEALTH:");
		switch (status) {
			case 0:
				System.out.println("  Good");
				break;
			case 1:
				System.out.println("  Warning");
				break;
			case 2:
				System.out.println("  Error");
				break;
			default:
				System.out.println("  unknown = " + status);
		}
		System.out.println("  error_code = " + error_code);
	}
}
