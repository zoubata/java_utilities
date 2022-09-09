package com.zoubworld.robot.lidar.RP;
import java.util.Arrays;

import org.ddogleg.struct.GrowQueue_I32;

import com.zoubworld.robot.lidar.driver.ILidarScan;

/**
 * Storage for a complete scan
 *
 * @author Peter Abeles
 */
public class RpLidarScan implements ILidarScan {

	public static final int N = 360*64-1;

	public int quality[] = new int[N];
	public int distance[] = new int[N];
	/** System.currentTimeMillis() when a new observation arrived */
	public long time[] = new long[N];
	/** index of elements which were written to */
	public GrowQueue_I32 used = new GrowQueue_I32();


	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarScan#set(com.zoubworld.robot.lidar.RP.RpLidarScan)
	 */
	@Override
	public void set( ILidarScan scan2 ) {
		RpLidarScan scan=(RpLidarScan)scan2;
		System.arraycopy(scan.quality,0,quality,0,N);
		System.arraycopy(scan.distance,0,distance,0,N);
		System.arraycopy(scan.time,0,time,0,N);
		used.resize(scan.used.size);
		for (int i = 0; i < scan.used.size; i++) {
			used.data[i] = scan.used.data[i];
		}
	}

	public void convertMeters( double meters[] ) {

		for (int i = 0; i < N; i++) {
			meters[i] = distance[i] / 4000.0;
		}
	}

	public void convertMilliMeters( double meters[] ) {

		for (int i = 0; i < N; i++) {
			meters[i] = distance[i] / 4.0;
		}
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarScan#reset()
	 */
	@Override
	public void reset() {
		// mark them all as invalid
		Arrays.fill(distance,0);
		used.reset();
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarScan#isInvalid(int)
	 */
	@Override
	public boolean isInvalid( int which ) {
		return distance[which] == 0;
	}


}
