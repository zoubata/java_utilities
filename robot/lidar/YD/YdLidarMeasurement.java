package com.zoubworld.robot.lidar.YD;

import com.zoubworld.robot.lidar.driver.ILidarMeasurement;

/**
 * Single measurement from LIDAR
 *
 * @author Peter Abeles
 */
public class YdLidarMeasurement implements ILidarMeasurement {
	public boolean start;
	public int quality;
	public int angle;
	public int distance;
	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarMeasurement#isStart()
	 */
	@Override
	public boolean isStart() {
		return start;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarMeasurement#setStart(boolean)
	 */
	@Override
	public void setStart(boolean start) {
		this.start = start;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarMeasurement#getQuality()
	 */
	@Override
	public int getQuality() {
		return quality;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarMeasurement#setQuality(int)
	 */
	@Override
	public void setQuality(int quality) {
		this.quality = quality;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarMeasurement#getAngle()
	 */
	@Override
	public int getAngle() {
		return angle;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarMeasurement#setAngle(int)
	 */
	@Override
	public void setAngle(int angle) {
		this.angle = angle;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarMeasurement#getDistance()
	 */
	@Override
	public int getDistance() {
		return distance;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarMeasurement#setDistance(int)
	 */
	@Override
	public void setDistance(int distance) {
		this.distance = distance;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarMeasurement#getTimeMilli()
	 */
	@Override
	public long getTimeMilli() {
		return timeMilli;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarMeasurement#setTimeMilli(long)
	 */
	@Override
	public void setTimeMilli(long timeMilli) {
		this.timeMilli = timeMilli;
	}

	/** System.currentTimeMillis() when the measurement arrived */
	public long timeMilli;

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarMeasurement#isInvalid()
	 */
	@Override
	public boolean isInvalid() {
		return distance == 0;
	}
}
/*
struct LaserPoint {
  //! lidar angle
  float angle;
  //! lidar range
  float range;
  //! lidar intensity
  float intensity;
  LaserPoint &operator = (const LaserPoint &data) {
    this->angle = data.angle;
    this->range = data.range;
    this->intensity = data.intensity;
    return *this;
  }
};*/