package com.zoubworld.robot.lidar.YD.sub;
//! A struct for returning configuration from the YDLIDAR
	public class LaserConfig {
	  //! Start angle for the laser scan [rad].  0 is forward and angles are measured clockwise when viewing YDLIDAR from the top.
	  public float min_angle;
	  //! Stop angle for the laser scan [rad].   0 is forward and angles are measured clockwise when viewing YDLIDAR from the top.
	  public float max_angle;
	  //! angle resoltuion [rad]
	  public float angle_increment;
	  //! Scan resoltuion [s]
	  public float time_increment;
	  //! Time between scans
	  public float scan_time;
	  //! Minimum range [m]
	  public float min_range;
	  //! Maximum range [m]
	  public float max_range;

	};