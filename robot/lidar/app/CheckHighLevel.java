package com.zoubworld.robot.lidar.app;

import com.zoubworld.robot.lidar.RP.RpLidarHighLevelDriver;
import com.zoubworld.robot.lidar.RP.RpLidarScan;
import com.zoubworld.robot.lidar.driver.ILidarHighLevelDriver;

/**
 * Prints out raw scans from the low level driver
 *
 * @author Peter Abeles
 */
public class CheckHighLevel {

	public static void main(String[] args) throws Exception {
		ILidarHighLevelDriver driver = new RpLidarHighLevelDriver();

		double mm[] = new double[ RpLidarScan.N ];

		if (!driver.initialize("/dev/ttyUSB0",0)) {
			System.out.println("Failed to initialize");
		} else {
			RpLidarScan scan = new RpLidarScan();
			for (int i = 0; i < 1000; i++) {

				if (!driver.blockCollectScan(scan, 10000))
					System.out.println("Couldn't collect a complete scan");
				else {
					scan.convertMilliMeters(mm);
					int total = 0;
					for (int j = 0; j < RpLidarScan.N; j++) {
						if( scan.distance[j] != 0 ) {
//							System.out.printf("%3f r = %f\n",(j/64.0),mm[j]);
//						System.out.printf("%4d = %4d\n", i, scan.distance[i]);
							total++;
						}
					}
					System.out.println("total with range = "+total);
				}

			}
		}
	}
}
