package com.zoubworld.robot.lidar.app;

import com.zoubworld.robot.lidar.RP.RpLidarHighLevelDriver;
import com.zoubworld.robot.lidar.RP.RpLidarLowLevelDriver;
import com.zoubworld.robot.lidar.RP.RpLidarMeasurement;
import com.zoubworld.robot.lidar.RP.RpLidarScan;
import com.zoubworld.robot.lidar.driver.ILidarDeviceInfo;
import com.zoubworld.robot.lidar.driver.ILidarHeath;
import com.zoubworld.robot.lidar.driver.ILidarListener;
import com.zoubworld.robot.lidar.driver.ILidarLowLevelDriver;
import com.zoubworld.robot.lidar.driver.ILidarMeasurement;

/**
 * Prints out raw scans from the low level driver
 *
 * @author Peter Abeles
 */
public class SpewOutMeasurements implements ILidarListener {

	@Override
	public void handleMeasurement(ILidarMeasurement measurement) {
		double deg = measurement.getAngle() / 64.0;
		double r = measurement.getDistance() / 4.0;
		if (measurement.isStart())
			System.out.println();
		if( deg > 350 )
			System.out.printf(measurement.isStart() + " %3d   theta = %6.2f r = %10.2f\n", measurement.getQuality(), deg, r);
//		System.out.printf(measurement.start + " %3d   theta = %4d r = %5d\n", measurement.quality, measurement.angle, measurement.distance);
	}

	@Override
	public void handleDeviceHealth(ILidarHeath health) {
		health.print();
	}

	@Override
	public void handleDeviceInfo(ILidarDeviceInfo info) {
		System.out.println("Got device info packet");
		info.print();
	}
	

	public static void main(String[] args) throws Exception {
		ILidarLowLevelDriver driver = new RpLidarLowLevelDriver("/dev/ttyUSB0", new SpewOutMeasurements());
		driver.setVerbose(false);

		driver.sendReset();
		driver.pause(100);

		driver.sendGetInfo(1000);
		driver.sendGetHealth(1000);
		
		//for v2 only - I guess this command is ignored by v1
		driver.sendStartMotor(660);

		driver.sendScan(500);
		driver.pause(10000);
		driver.sendReset();
		driver.pause(100);
		driver.shutdown();
		driver.pause(100);
		System.exit(0);
	}

	

}
