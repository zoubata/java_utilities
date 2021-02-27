package com.zoubworld.robot.lidar.YD;
import java.util.Arrays;
import java.util.List;

import org.ddogleg.struct.GrowQueue_I32;

import com.zoubworld.robot.lidar.YD.sub.LaserConfig;
import com.zoubworld.robot.lidar.driver.ILidarScan;

/**
 * Storage for a complete scan
 *
 * @author Peter Abeles
 */
public class YdLidarScan implements ILidarScan {

	/*public static final int N = 360*64-1;

	public int quality[] = new int[N];
	public int distance[] = new int[N];
	/ ** System.currentTimeMillis() when a new observation arrived * /
	public long time[] = new long[N];
	/ ** index of elements which were written to * /
	public GrowQueue_I32 used = new GrowQueue_I32();
*/
	
	LaserConfig config;
	List<YdLidarMeasurement> points;
	  long stamp;
	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarScan#set(com.zoubworld.robot.lidar.RP.YdLidarScan)
	 */
	@Override
	public void set( ILidarScan scan2 ) {
		YdLidarScan scan=(YdLidarScan)scan2;	
		points.clear();
		points.addAll(scan.points);
		stamp=scan.stamp;
		config=scan.config;		
	}

	public void convertMeters( double meters[] ) {

		for (int i = 0; i < points.size(); i++) {
			meters[i] =points.get(i).distance / 4000.0;
		}
	}

	public void convertMilliMeters( double meters[] ) {

		for (int i = 0; i < points.size(); i++) {
			meters[i] = points.get(i).distance / 4.0;
		}
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarScan#reset()
	 */
	@Override
	public void reset() {
		// mark them all as invalid
		points.clear();
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarScan#isInvalid(int)
	 */
	@Override
	public boolean isInvalid( int which ) {
		return points.get(which).distance == 0;
	}


}