package com.zoubworld.robot.lidar.YD.sub;
public class node_info {
	public  byte    sync_flag;  //sync flag
		  public  int   sync_quality;//!????
		  public int   angle_q6_checkbit; //!?????
		  public int   distance_q2; //! ???????
		  public long   stamp; //! ???
		  public byte    scan_frequence;//! ?????????,????0
		  public byte    debug_info[];//12
		  public byte    index;
		}