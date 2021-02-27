package com.zoubworld.robot.lidar.driver;

import com.zoubworld.robot.lidar.RP.RpLidarDeviceInfo;
import com.zoubworld.robot.lidar.RP.RpLidarMeasurement;

/**
 * Listener for client of {@link roboticinception.rplidar.RpLidarLowLevelDriver}
 *
 * @author Peter Abeles
 */
public interface ILidarListener {

	public void handleMeasurement(ILidarMeasurement measurement);

	public void handleDeviceHealth(ILidarHeath health);

	public void handleDeviceInfo(ILidarDeviceInfo info);
}
