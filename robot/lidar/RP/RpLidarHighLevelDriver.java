package com.zoubworld.robot.lidar.RP;
import java.util.Arrays;

import com.zoubworld.robot.lidar.driver.ILidarDeviceInfo;
import com.zoubworld.robot.lidar.driver.ILidarHeath;
import com.zoubworld.robot.lidar.driver.ILidarHighLevelDriver;
import com.zoubworld.robot.lidar.driver.ILidarListener;
import com.zoubworld.robot.lidar.driver.ILidarLowLevelDriver;
import com.zoubworld.robot.lidar.driver.ILidarMeasurement;
import com.zoubworld.robot.lidar.driver.ILidarScan;

/**
 * High level task which performs intelligent filtering to remove bad data and correctly starts up the sensor
 *
 * @author Peter Abeles
 */
// TODO filter a scan if the angle is out of order
public class RpLidarHighLevelDriver implements ILidarListener, ILidarHighLevelDriver {

	final RpLidarScan work = new RpLidarScan();
	final RpLidarScan complete = new RpLidarScan();
	volatile boolean ready = false;

	ILidarLowLevelDriver driver;

	int expectedCount;
	volatile boolean initialized;

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarHighLevelDriver#initialize(java.lang.String, int)
	 */
	@Override
	public boolean initialize( String device , int totalCollect ) {
		if( driver != null )
			throw new RuntimeException("Already initialized");
		initialized = false;

		try {
			driver = new RpLidarLowLevelDriver(device, this);
		} catch (Exception e) {
			return false;
		}
		driver.setVerbose(false);

		driver.sendReset();
		driver.pause(1000);

//		driver.sendGetInfo(1000);
//		driver.sendGetHealth(1000);
		if( !driver.sendScan(500) ) {
//			System.err.println("scan request failed");
//			return false;
		}

		if( totalCollect <= 0 ) {
			if( !autoSetCollectionToScan() )
				return false;
		} else {
			expectedCount = totalCollect;
		}

		initialized = true;
//		System.out.println(" expected count = "+expectedCount);

		return true;
	}

	/**
	 * Determine the number of measurements it needs to collect to approximately read in an entire scan using
	 *
	 * @return true if no errors
	 */
	private boolean autoSetCollectionToScan() {
		int numberOfAttempts = 0;
		while( true ) {
			int N = 10;
			int totalUsed[] = new int[N];
			expectedCount = 0;
			RpLidarScan scan = new RpLidarScan();
			driver.pause(400);
			if (!blockCollectScan(scan, 500))
				return false;

			for (int i = 0; i < N; i++) {
				if (!blockCollectScan(scan, 500)) {
//					System.err.println("Learning scan failed!");
					return false;
				}
//				System.out.println("Learning from scan " + i + "   count = " + scan.used.size());
				totalUsed[i] = scan.used.size();
			}

			Arrays.sort(totalUsed);
			expectedCount = totalUsed[N / 2];

			int agreement = 0;
			for (int i = 0; i < N; i++) {
				if (Math.abs(expectedCount - totalUsed[i]) <= 5) {
					agreement++;
				}
			}

			if (agreement >= N / 2) {
				break;
			} else if( ++numberOfAttempts > 4 ) {
				throw new RuntimeException("Data is too noisy.  High variance in number of measurements per scan");
			}
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarHighLevelDriver#stop()
	 */
	@Override
	public void stop() {
		if( driver != null ) {
			driver.sendReset();
			driver.pause(100);
			driver.shutdown();
			driver.pause(100);
			driver = null;
			initialized = false;
		}
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarHighLevelDriver#blockCollectScan(com.zoubworld.robot.lidar.driver.ILidarScan, long)
	 */
	@Override
	public boolean blockCollectScan( ILidarScan scan , long timeout ) {
		long end = System.currentTimeMillis() + timeout;
		if( timeout <= 0 )
			end = Long.MAX_VALUE;

		while( end >= System.currentTimeMillis() ) {
			// needs to have an unread complete scan and can't be the first one since that might be incomplete
			if( ready ) {
				synchronized (complete) {
					scan.set(complete);
					ready = false;
					return true;
				}
			}
			Thread.yield();
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarHighLevelDriver#handleMeasurement(com.zoubworld.robot.lidar.RP.RpLidarMeasurement)
	 */
	@Override
	public void handleMeasurement( ILidarMeasurement measurement ) {
		int which = measurement.getAngle();
		// ignore obviously bad packet
		if (which >= RpLidarScan.N) {
			return;
		}

		boolean copyScan = false;
		if ( expectedCount == 0  ) {
			if( measurement.isStart() ) {
				copyScan = true;
			}
		} else if( expectedCount <= work.used.size() ) {
			copyScan = true;
		}

		if( copyScan ) {
			synchronized (complete) {
				complete.set(work);
				ready = true;
			}
			work.reset();
		}

		work.used.add(which);
		work.time[which] = measurement.getTimeMilli();
		work.distance[which] = measurement.getDistance();
		work.quality[which] = measurement.getQuality();
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarHighLevelDriver#handleDeviceHealth(com.zoubworld.robot.lidar.driver.ILidarHeath)
	 */
	@Override
	public void handleDeviceHealth( ILidarHeath health ) {

	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarHighLevelDriver#handleDeviceInfo(com.zoubworld.robot.lidar.RP.RpLidarDeviceInfo)
	 */
	@Override
	public void handleDeviceInfo( ILidarDeviceInfo info ) {

	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarHighLevelDriver#isInitialized()
	 */
	@Override
	public boolean isInitialized() {
		return initialized;
	}
}
