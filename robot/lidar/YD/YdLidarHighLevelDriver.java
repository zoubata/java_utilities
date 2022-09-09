package com.zoubworld.robot.lidar.YD;
import java.util.Arrays;

import com.zoubworld.robot.lidar.YD.sub.LaserDebug;
import com.zoubworld.robot.lidar.YD.sub.node_info;
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
public class YdLidarHighLevelDriver implements ILidarListener, ILidarHighLevelDriver {

	volatile boolean ready = false;
	
	ILidarLowLevelDriver driver;
	node_info[] global_nodes ;
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
			driver = new YdLidarLowLevelDriver(device, this);
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
			YdLidarScan scan = new YdLidarScan();
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
	 * @see com.zoubworld.robot.lidar.RP.ILidarHighLevelDriver#handleMeasurement(com.zoubworld.robot.lidar.RP.YdLidarMeasurement)
	 */
	@Override
	public void handleMeasurement( ILidarMeasurement measurement ) {
		int which = measurement.getAngle();
		// ignore obviously bad packet
		if (which >= YdLidarScan.N) {
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
	 * @see com.zoubworld.robot.lidar.RP.ILidarHighLevelDriver#handleDeviceInfo(com.zoubworld.robot.lidar.RP.YdLidarDeviceInfo)
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
 
/*********************************************************************
* Software License Agreement (BSD License)
*
*  Copyright (c) 2018, EAIBOT, Inc.
*  All rights reserved.
*
*  Redistribution and use in source and binary forms, with or without
*  modification, are permitted provided that the following conditions
*  are met:
*
*   * Redistributions of source code must retain the above copyright
*     notice, this list of conditions and the following disclaimer.
*   * Redistributions in binary form must reproduce the above
*     copyright notice, this list of conditions and the following
*     disclaimer in the documentation and/or other materials provided
*     with the distribution.
*   * Neither the name of the Willow Garage nor the names of its
*     contributors may be used to endorse or promote products derived
*     from this software without specific prior written permission.
*
*  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
*  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
*  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
*  FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
*  COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
*  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
*  BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
*  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
*  CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
*  LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
*  ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
*  POSSIBILITY OF SUCH DAMAGE.
*********************************************************************/

//namespace ydlidar {

//enum YDLIDAR_MODLES {
	  static final int    YDLIDAR_F4      = 1;/**< F4雷达型号代号. */
		  static final int   YDLIDAR_T1      = 2;/**< T1雷达型号代号. */
		  static final int   YDLIDAR_F2      = 3;/**< F2雷达型号代号. */
		  static final int   YDLIDAR_S4      = 4;/**< S4雷达型号代号. */
		  static final int   YDLIDAR_G4      = 5;/**< G4雷达型号代号. */
		  static final int   YDLIDAR_X4      = 6;/**< X4雷达型号代号. */
		  static final int   YDLIDAR_G4PRO   = 7;/**< G4PRO雷达型号代号. */
		  static final int   YDLIDAR_F4PRO   = 8;/**< F4PRO雷达型号代号. */
		  static final int    YDLIDAR_R2      = 9;/**< R2雷达型号代号. */
		  static final int   YDLIDAR_G10     = 10;/**< G10雷达型号代号. */
		  static final int   YDLIDAR_S4B     = 11;/**< S4B雷达型号代号. */
		  static final int   YDLIDAR_S2      = 12;/**< S2雷达型号代号. */
		  static final int   YDLIDAR_G6      = 13;/**< G6雷达型号代号. */
		  static final int   YDLIDAR_G2A     = 14;/**< G2A雷达型号代号. */
		  static final int    YDLIDAR_G2B     = 15;/**< G2雷达型号代号. */
		  static final int    YDLIDAR_G2C     = 16;/**< G2C雷达型号代号. */
		  static final int    YDLIDAR_G4B     = 17;/**< G4B雷达型号代号. */
		  static final int    YDLIDAR_G4C     = 18;/**< G4C雷达型号代号. */
		  static final int   YDLIDAR_G1      = 19;/**< G1雷达型号代号. */

		  static final int    YDLIDAR_TG15    = 100;/**< TG15雷达型号代号. */
		  static final int    YDLIDAR_TG30    = 101;/**< T30雷达型号代号. */
		  static final int    YDLIDAR_TG50    = 102;/**< TG50雷达型号代号. */
 
//enum YDLIDAR_RATE {
		  static final int   YDLIDAR_RATE_4K = 0;
		  static final int    YDLIDAR_RATE_8K = 1;
		  static final int    YDLIDAR_RATE_9K = 2;
		  static final int    YDLIDAR_RATE_10K = 3;

	  static final int  TYPE_TOF = 0;
	  static final int 	  TYPE_TRIANGLE  = 1;
			  static final int 	  TYPE_Tail=2;
			  
	  boolean    isScanning;
	  int     m_FixedSize ;
	  float   m_AngleOffset;
	  boolean    m_isAngleOffsetCorrected;
	  float   frequencyOffset;
	  int   lidar_model;
	  byte Major;
	  byte Minjor;
	  YdLidarLowLevelDriver lidarPtr;
	  long m_PointTime;
	  long last_node_time;
//	  node_info *global_nodes;
//	  std::map<int, int> SampleRateMap;
	  boolean m_ParseSuccess;
	  String m_lidarSoftVer;
	  String m_lidarHardVer;
	  String m_lidarSerialNum;
	  int defalutSampleRate;
	  
	  String m_SerialPort        = "";
	 int  m_SerialBaudrate    = 230400;
	 boolean m_FixedResolution   = true;
	 boolean m_Reversion         = false;
	 boolean m_Inverted          = false;//
	 boolean m_AutoReconnect     = true;
	 boolean m_SingleChannel     = false;
	 int  m_LidarType         = TYPE_TRIANGLE;
	  float m_MaxAngle          = 180.f;
	  float  m_MinAngle          = -180.f;
	  float  m_MaxRange          = 64.f;
	  float  m_MinRange          = 0.01f;
	int  m_SampleRate        = 5;
	float  m_ScanFrequency     = 10;
	float m_AbnormalCheckCount=0;
	float m_OffsetTime=0;
/*-------------------------------------------------------------
						Constructor
-------------------------------------------------------------*/
	YdLidarHighLevelDriver( ) {
  m_SerialPort        = "";
  m_SerialBaudrate    = 230400;
  m_FixedResolution   = true;
  m_Reversion         = false;
  m_Inverted          = false;//
  m_AutoReconnect     = true;
  m_SingleChannel     = false;
  m_LidarType         = TYPE_TRIANGLE;
  m_MaxAngle          = 180.f;
  m_MinAngle          = -180.f;
  m_MaxRange          = 64.0f;
  m_MinRange          = 0.01f;
  m_SampleRate        = 5;
  defalutSampleRate   = 5;
  m_ScanFrequency     = 10;
  isScanning          = false;
  m_FixedSize         = 720;
  frequencyOffset     = 0.4f;
  m_AbnormalCheckCount  = 4;
  Major               = 0;
  Minjor              = 0;
//  m_IgnoreArray.clear();
  m_PointTime         = 1000000000L / 5000;
  m_OffsetTime        = 0.0f;
  m_AngleOffset       = 0.0f;
  lidar_model = YDLIDAR_G2B;
  last_node_time = System.nanoTime();
 // global_nodes = new node_info[YDlidarDriver::MAX_SCAN_NODES];
  m_ParseSuccess = false;
 
 }
/*-------------------------------------------------------------
                    ~CYdLidar
-------------------------------------------------------------*/
 

void  disconnecting() {
  if (lidarPtr!=null) {
    lidarPtr.disconnect();
     
    lidarPtr = null;
  }

  isScanning = false;
}

//get zero angle offset value
float  getAngleOffset()  {
  return m_AngleOffset;
}

boolean isAngleOffetCorrected()  {
  return m_isAngleOffsetCorrected;
}

String  getSoftVersion()  {
  return m_lidarSoftVer;
}

String  getHardwareVersion()  {
  return m_lidarHardVer;
}

String  getSerialNumber()  {
  return m_lidarSerialNum;
}

boolean isRangeValid(double reading)  {
  if (reading >= m_MinRange && reading <= m_MaxRange) {
    return true;
  }

  return false;
}
/*
boolean isRangeIgnore(double angle)  {
	boolean ret = false;

  for (short j = 0; j < m_IgnoreArray.size(); j = j + 2) {
    if ((angles::from_degrees(m_IgnoreArray[j]) <= angle) &&
        (angle <= angles::from_degrees(m_IgnoreArray[j + 1]))) {
      ret = true;
      break;
    }
  }

  return ret;
}*/
private static long getTime(){return System.nanoTime();}
private static int getms(){return (int) (System.nanoTime()/1000000L);}


private void delay(long i){}

/*-------------------------------------------------------------
                        checkHardware
-------------------------------------------------------------*/
boolean checkHardware() {
  if (lidarPtr!=null) {
    return false;
  }

  if (isScanning && lidarPtr.isscanning()) {
    return true;
  }

  return false;
}
/*!
 * \brief Convert degrees to radians
 */

static  double from_degrees(double degrees) {
  return degrees * Math.PI / 180.0;
}

/*!
 * \brief Convert radians to degrees
 */
static  double to_degrees(double radians) {
  return radians * 180.0 / Math.PI;
}
/*!
 * \brief normalize
 *
 * Normalizes the angle to be -M_PI circle to +M_PI circle
 * It takes and returns radians.
 *
 */
static  double normalize_angle(double angle) {
  double a = normalize_angle_positive(angle);

  if (a > Math.PI) {
    a -= 2.0 * Math.PI;
  }

  return a;
}

/*!
 * \brief normalize_angle_positive
 *
 *        Normalizes the angle to be 0 to 2*M_PI
 *        It takes and returns radians.
 */
static  double normalize_angle_positive(double angle) {
  return fmod(fmod(angle, 2.0 * Math.PI) + 2.0 * Math.PI, 2.0 * Math.PI);
}
static  double fmod(double a,double b)
{return a%b;
}


boolean isRangeIgnore(double angle)  {
	boolean ret = false;
/*
  for (int j = 0; j < m_IgnoreArray.size(); j = j + 2) {
    if ((from_degrees(m_IgnoreArray[j]) <= angle) &&
        (angle <= from_degrees(m_IgnoreArray[j + 1]))) {
      ret = true;
      break;
    }
  }*/

  return ret;
}

/*-------------------------------------------------------------
						doProcessSimple
-------------------------------------------------------------*/
boolean doProcessSimple(ILidarScan outscan2,
                                boolean hardwareError) {
  hardwareError			= false;
  YdLidarScan outscan=(YdLidarScan) outscan2;
  // Bound?
  if (!checkHardware()) {
    hardwareError = true;
    delay((long) (200 / m_ScanFrequency));
    return false;
  }

  int   count = YdLidarLowLevelDriver.MAX_SCAN_NODES;
  //wait Scan data:
  long tim_scan_start = getTime();
  long startTs = tim_scan_start;
  int op_result =  lidarPtr.grabScanData(global_nodes, count);
  long tim_scan_end = getTime();

  // Fill in scan data:
  if (YdLidarLowLevelDriver.IS_OK(op_result)) {
    long scan_time = m_PointTime * (count - 1);
    tim_scan_end += m_OffsetTime * 1e9;
    tim_scan_end -= m_PointTime;
    tim_scan_end -= global_nodes[0].stamp;
    tim_scan_start = tim_scan_end -  scan_time ;

    if (tim_scan_start < startTs) {
      tim_scan_start = startTs;
      tim_scan_end = tim_scan_start + scan_time;
    }

    if ((last_node_time + m_PointTime) >= tim_scan_start) {
      tim_scan_start = last_node_time + m_PointTime;
      tim_scan_end = tim_scan_start + scan_time;
    }

    last_node_time = tim_scan_end;

    if (m_MaxAngle < m_MinAngle) {
      float temp = m_MinAngle;
      m_MinAngle = m_MaxAngle;
      m_MaxAngle = temp;
    }

    int all_node_count = count;

    outscan.config.min_angle = (float) from_degrees(m_MinAngle);
    outscan.config.max_angle =  (float) from_degrees(m_MaxAngle);
    outscan.config.scan_time =   (float) (scan_time * 1.0 / 1000000000);
    outscan.config.time_increment = (float) (outscan.config.scan_time / (double)(count - 1));
    outscan.config.min_range = m_MinRange;
    outscan.config.max_range = m_MaxRange;
    outscan.stamp = tim_scan_start;
    outscan.points.clear();

    if (m_FixedResolution) {
      all_node_count = m_FixedSize;
    }

    outscan.config.angle_increment = (outscan.config.max_angle -
                                      outscan.config.min_angle) / (all_node_count - 1);

    double range = 0.0f;
    double intensity = 0.0f;
    double angle = 0.0f;

    for (int i = 0; i < count; i++) {
      angle = (float)((global_nodes[i].angle_q6_checkbit >>
                                  YdLidarLowLevelDriver.LIDAR_RESP_MEASUREMENT_ANGLE_SHIFT) / 64.0f) + m_AngleOffset;

      if (YdLidarLowLevelDriver.isTOFLidar(m_LidarType)) {
        if (YdLidarLowLevelDriver.isOldVersionTOFLidar(lidar_model, Major, Minjor)) {
          range =  (global_nodes[i].distance_q2 / 2000.f);
        } else {
          range =  (global_nodes[i].distance_q2 / 1000.f);
        }
      } else {
        if (YdLidarLowLevelDriver.isOctaveLidar(lidar_model)) {
          range =  (global_nodes[i].distance_q2 / 2000.f);
        } else {
          range =  (global_nodes[i].distance_q2 / 4000.f);
        }
      }

      intensity =  (global_nodes[i].sync_quality);
      angle =  from_degrees(angle);

      //Rotate 180 degrees or not
      if (m_Reversion) {
        angle = angle + Math.PI;
      }

      //Is it counter clockwise
      if (m_Inverted) {
        angle = 2 * Math.PI - angle;
      }

      angle = normalize_angle(angle);

      //ignore angle
      if (isRangeIgnore(angle)) {
        range = 0.0;
      }

      //valid range
      if (!isRangeValid(range)) {
        range = 0.0;
        intensity = 0.0;
      }

      if (angle >= outscan.config.min_angle &&
          angle <= outscan.config.max_angle) {
    	  YdLidarMeasurement point=new YdLidarMeasurement();
        point.angle = (int) angle;
        point.distance = (int) range;
        point.quality = (int) intensity;

        if (outscan.points.isEmpty()) {
          outscan.stamp = tim_scan_start + i * m_PointTime;
        }

        outscan.points.add(0,point);
      }

      handleDeviceInfoPackage(count);
    }

    return true;
  } else {
    if (YdLidarLowLevelDriver.IS_FAIL(op_result)) {
      // Error? Retry connection
    }
  }

  return false;

}


void parsePackageNode( node_info node, LaserDebug info) {
  switch (node.index) {
    case 0://W3F4CusMajor_W4F0CusMinor;
      info.W3F4CusMajor_W4F0CusMinor = node.debug_info[node.index];
      break;

    case 1://W4F3Model_W3F0DebugInfTranVer
      info.W4F3Model_W3F0DebugInfTranVer = node.debug_info[node.index];
      break;

    case 2://W3F4HardwareVer_W4F0FirewareMajor
      info.W3F4HardwareVer_W4F0FirewareMajor = node.debug_info[node.index];
      break;

    case 4://W3F4BoradHardVer_W4F0Moth
      info.W3F4BoradHardVer_W4F0Moth = node.debug_info[node.index];
      break;

    case 5://W2F5Output2K4K5K_W5F0Date
      info.W2F5Output2K4K5K_W5F0Date = node.debug_info[node.index];
      break;

    case 6://W1F6GNoise_W1F5SNoise_W1F4MotorCtl_W4F0SnYear
      info.W1F6GNoise_W1F5SNoise_W1F4MotorCtl_W4F0SnYear =
        node.debug_info[node.index];
	break;

    case 7://W7F0SnNumH
      info.W7F0SnNumH = node.debug_info[node.index];
      break;

    case 8://W7F0SnNumL
      info.W7F0SnNumL = node.debug_info[node.index];

      break;

    default:
      break;
  }

  if (node.index > info.MaxDebugIndex && node.index < 100) {
    info.MaxDebugIndex = (node.index);
  }
}

 boolean isVersionValid( LaserDebug info) {
	boolean ret = false;

  if (isValidValue(info.W3F4CusMajor_W4F0CusMinor) &&
      isValidValue(info.W4F3Model_W3F0DebugInfTranVer) &&
      isValidValue(info.W3F4HardwareVer_W4F0FirewareMajor) &&
      isValidValue(info.W3F4BoradHardVer_W4F0Moth)) {
    ret = true;
  }

  return ret;
}

 boolean isValidValue(byte value) {
  if ((value & 0x80)==0) {
    return false;
  }

  return true;
}

boolean isSerialNumbValid( LaserDebug info) {
  boolean ret = false;

  if (isValidValue(info.W2F5Output2K4K5K_W5F0Date) &&
      isValidValue(info.W1F6GNoise_W1F5SNoise_W1F4MotorCtl_W4F0SnYear) &&
      isValidValue(info.W7F0SnNumH) &&
      isValidValue(info.W7F0SnNumH)) {
    ret = true;
  }

  return ret;
}
boolean ParseLaserDebugInfo( LaserDebug info, YdLidarDeviceInfo value) {
	boolean ret = false;
  byte CustomVerMajor =  (byte) (
                            (info.W3F4CusMajor_W4F0CusMinor) >> 4);
  byte CustomVerMinor =  (byte) ((info.W3F4CusMajor_W4F0CusMinor) & 0x0F);
   byte lidarmodel = (byte) ((info.W4F3Model_W3F0DebugInfTranVer)
                        >> 3);
  byte hardwareVer =(byte)
                        ((info.W3F4HardwareVer_W4F0FirewareMajor) >> 4);
                        byte Moth = (byte) ((info.W3F4BoradHardVer_W4F0Moth) & 0x0F);

                        byte Date = (byte) ((info.W2F5Output2K4K5K_W5F0Date) & 0x1F);
                        byte Year = 
                 (byte) ((info.W1F6GNoise_W1F5SNoise_W1F4MotorCtl_W4F0SnYear) & 0x0F);
  int Number = (((info.W7F0SnNumH) << 7) |
                     (info.W7F0SnNumL));

  if (isVersionValid(info) && (info.MaxDebugIndex > 0) && Year!=0) {

    if (isSerialNumbValid(info) && info.MaxDebugIndex > 8) {
    	value.firmware_major = (CustomVerMajor);
    	value.firmware_minor = ( CustomVerMinor);
         value.hardware = hardwareVer;
      value.model = lidarmodel;
      int year = Year + 2015;
      value.serialNumber=(String.format( "%04d", year)+
    		  String.format( "%02d", Moth)+
      String.format( "%02d", Date)+
      String.format( "%08d", Number)).getBytes();

      for (int i = 0; i < 16; i++) {
        value.serialNumber[i] -= 48;
      }

      ret = true;
    }
  }

  return ret;
}



void  handleDeviceInfoPackage(int count) {
  if (m_ParseSuccess) {
    return;
  }

  LaserDebug debug = new LaserDebug();
  debug.MaxDebugIndex = 0;

  for (int i = 0; i < count; i++) {
    parsePackageNode(global_nodes[i], debug);
  }

  YdLidarDeviceInfo info=new YdLidarDeviceInfo();

  if (ParseLaserDebugInfo(debug, info)) {
    if (info.firmware_major != 0 ||
    		info.firmware_minor != 0 ||
            info.hardware != 0) {
      String serial_number="";

      for (int i = 0; i < 16; i++) {
        serial_number += ""+(char)(info.serialNumber[i] & 0xff);
      }

      Major = (byte)(info.firmware_major);
      Minjor = (byte)(info.firmware_minor);
      String softVer =  (Major & 0xff) + "." + (
                               Minjor & 0xff);
      String hardVer = ""+(char)(info.hardware & 0xff);

      m_lidarSerialNum = serial_number;
      m_lidarSoftVer = softVer;
      m_lidarHardVer = hardVer;

      if (!m_ParseSuccess) {
        printfVersionInfo(info);
      }
    }

  }
}


/*-------------------------------------------------------------
						turnOn
-------------------------------------------------------------*/
boolean turnOn() {
  if (isScanning && lidarPtr.isscanning()) {
    return true;
  }

  // start scan...
  int op_result = lidarPtr.startScan();

  if (!YdLidarLowLevelDriver.IS_OK(op_result)) {
    op_result = lidarPtr.startScan();

    if (!YdLidarLowLevelDriver.IS_OK(op_result)) {
      lidarPtr.stop();
      System.err.println(String.format( "[CYdLidar] Failed to start scan mode: %x\n", op_result));
      isScanning = false;
      return false;
    }
  }

  m_ParseSuccess = false;
  m_PointTime = lidarPtr.getPointTime();

  if (checkLidarAbnormal()) {
    lidarPtr.stop();
    System.err.println(String.format(
            "[CYdLidar] Failed to turn on the Lidar, because the lidar is blocked or the lidar hardware is faulty.\n"));
    isScanning = false;
    return false;
  }

  if (m_SingleChannel && !m_ParseSuccess) {
    handleSingleChannelDevice();
  }

  m_PointTime = lidarPtr.getPointTime();
  isScanning = true;
  lidarPtr.setAutoReconnect(m_AutoReconnect);
  System.out.println(String.format("[YDLIDAR INFO] Current Sampling Rate : %dK", m_SampleRate));
  System.out.println("[YDLIDAR INFO] Now YDLIDAR is scanning ......");
  
  return true;
}

/*-------------------------------------------------------------
						turnOff
-------------------------------------------------------------*/
boolean turnOff() {
  if (lidarPtr!=null) {
    lidarPtr.stop();
  }

  if (isScanning) {
	  System.out.println("[YDLIDAR INFO] Now YDLIDAR Scanning has stopped ......\n");
  }

  isScanning = false;
  return true;
}

/*-------------------------------------------------------------
checkLidarAbnormal
-------------------------------------------------------------*/
boolean checkLidarAbnormal() {

  int   count = YdLidarLowLevelDriver.MAX_SCAN_NODES;
  int check_abnormal_count = 0;

  if (m_AbnormalCheckCount < 2) {
    m_AbnormalCheckCount = 2;
  }

  int op_result = YdLidarLowLevelDriver.RESULT_FAIL;
//  std::vector<int> data;
  int buffer_count  = 0;

  while (check_abnormal_count < m_AbnormalCheckCount) {
    //Ensure that the voltage is insufficient or the motor resistance is high, causing an abnormality.
    if (check_abnormal_count > 0) {
      delay(check_abnormal_count * 1000);
    }

    float scan_time = 0.0f;
    int start_time = 0;
    int end_time = 0;
    op_result = YdLidarLowLevelDriver.RESULT_OK;

    while (buffer_count < 10 && (scan_time < 0.05 ||
                                 !lidarPtr.getSingleChannel()) && YdLidarLowLevelDriver.IS_OK(op_result)) {
      start_time = getms();
      count = YdLidarLowLevelDriver.MAX_SCAN_NODES;
      op_result =  lidarPtr.grabScanData(global_nodes, count);
      end_time = getms();
      scan_time = (float) (1.0 * (end_time - start_time) / 1e3);
      buffer_count++;

      if (YdLidarLowLevelDriver.IS_OK(op_result)) {
        handleDeviceInfoPackage(count);

        if (CalculateSampleRate(count, scan_time)) {
          if (!lidarPtr.getSingleChannel()) {
            return !YdLidarLowLevelDriver.IS_OK(op_result);
          }
        }
      }
    }

    if (YdLidarLowLevelDriver.IS_OK(op_result) && lidarPtr.getSingleChannel()) {
      data.push_back(count);
      int collection = 0;

      while (collection < 5) {
        count = YdLidarLowLevelDriver.MAX_SCAN_NODES;
        start_time = getms();
        op_result =  lidarPtr.grabScanData(global_nodes, count);
        end_time = getms();


        if (YdLidarLowLevelDriver.IS_OK(op_result)) {
          if (Math.abs( (data.front() - count)) > 10) {
            data.erase(data.begin());
          }

          handleDeviceInfoPackage(count);
          scan_time = (float) (1.0 * (end_time - start_time) / 1e3);
          data.add(0,count);

          if (CalculateSampleRate(count, scan_time)) {

          }

          if (scan_time > 0.05 && scan_time < 0.5 && lidarPtr.getSingleChannel()) {
            m_SampleRate =  (int) ((count / scan_time + 500) / 1000);
            m_PointTime = (long) (1e9 / (m_SampleRate * 1000));
            lidarPtr.setPointTime(m_PointTime);
          }

        }

        collection++;
      }

      if (data.size() > 1) {
        int total = accumulate(data.begin(), data.end(), 0);
        int mean =  total / data.size(); //mean value
        m_FixedSize = ( ((mean + 5) / 10)) * 10;
        System.out.print(String.format("[YDLIDAR]:Fixed Size: %d\n", m_FixedSize));
        System.out.print(String.format("[YDLIDAR]:Sample Rate: %dK\n", m_SampleRate));
        return false;
      }

    }

    check_abnormal_count++;
  }

  return !YdLidarLowLevelDriver.IS_OK(op_result);
}


/** Returns true if the device is connected & operative */
boolean getDeviceHealth() {
  if (null==lidarPtr) {
    return false;
  }

  lidarPtr.stop();
  int op_result;
  YdLidarHeath healthinfo;
  System.out.print(String.format("[YDLIDAR]:SDK Version: %s\n", YdLidarLowLevelDriver.getSDKVersion()));
  op_result = lidarPtr.getHealth(healthinfo);

  if (YdLidarLowLevelDriver.IS_OK(op_result)) {
	  System.out.print(String.format("[YDLIDAR]:Lidar running correctly ! The health status: %s\n",
           (int)healthinfo.status == 0 ? "good" : "bad"));

    if (healthinfo.status == 2) {
    	System.err.print(
              "Error, Yd Lidar internal error detected. Please reboot the device to retry.\n");
      return false;
    } else {
      return true;
    }

  } else {
	  System.err.print(String.format( "Error, cannot retrieve Yd Lidar health code: %x\n", op_result));
    return false;
  }

}

/*!
 * @brief lidarModelDefaultSampleRate
 * @param model
 * @return
 */
 int lidarModelDefaultSampleRate(int model) {
  int sample_rate = 4;

  switch (model) {
    case YDLIDAR_F4:
      break;

    case YDLIDAR_T1:
      break;

    case YDLIDAR_F2:
      break;

    case YDLIDAR_S4:
      break;

    case YDLIDAR_G4:
      sample_rate = 9;
      break;

    case YDLIDAR_X4:
      sample_rate = 5;
      break;

    case YDLIDAR_G4PRO:
      sample_rate = 9;
      break;

    case YDLIDAR_F4PRO:
      sample_rate = 4;
      break;

    case YDLIDAR_R2:
      sample_rate = 5;
      break;

    case YDLIDAR_G10:
      sample_rate = 10;
      break;

    case YDLIDAR_S4B:
      sample_rate = 4;
      break;

    case YDLIDAR_S2:
      sample_rate = 3;
      break;

    case YDLIDAR_G6:
      sample_rate = 18;
      break;

    case YDLIDAR_G2A:
      sample_rate = 5;
      break;

    case YDLIDAR_G2B:
      sample_rate = 5;
      break;

    case YDLIDAR_G2C:
      sample_rate = 4;
      break;

    case YDLIDAR_G4B:
      break;

    case YDLIDAR_G4C:
      break;

    case YDLIDAR_G1:
      sample_rate = 9;
      break;

    case YDLIDAR_TG15:
      sample_rate = 20;
      break;

    case YDLIDAR_TG30:
      sample_rate = 20;
      break;

    case YDLIDAR_TG50:
      sample_rate = 20;
      break;

    default:
      break;
  }

  return sample_rate ;
}

/*!
 * @brief isSupportLidar
 * @param model
 * @return
 */
 boolean isSupportLidar(int model) {
  boolean ret = true;

  if (model < YDLIDAR_F4 || (model > YDLIDAR_G1 &&
                             model < YDLIDAR_TG15) ||
      model > YDLIDAR_TG50) {
    ret = false;

  }

  return ret;
}
 

/*!
 * @brief hasIntensity
 * @param model
 * @return
 */
 boolean hasIntensity(int model) {
  boolean ret = false;

  if (model == YDLIDAR_G2B ||
      model == YDLIDAR_G4B ||
      model == YDLIDAR_S4B) {
    ret = true;
  }

  return ret;
}

/*!
 * @brief hasScanFrequencyCtrl
 * @param model
 * @return
 */
 boolean hasScanFrequencyCtrl(int model) {
	 boolean ret = true;

  if (model == YDLIDAR_S4 ||
      model == YDLIDAR_S4B ||
      model == YDLIDAR_S2 ||
      model == YDLIDAR_X4) {
    ret = false;
  }

  return ret;
}


/*!
 * @brief hasSampleRate
 * @param model
 * @return
 */

 boolean hasSampleRate(int model) {
	boolean ret = false;

  if (model == YDLIDAR_G4 ||
      model == YDLIDAR_G4PRO ||
      model == YDLIDAR_F4PRO ||
      model == YDLIDAR_G6 ||
      model == YDLIDAR_TG15 ||
      model == YDLIDAR_TG50 ||
      model == YDLIDAR_TG30) {
    ret = true;
  }

  return ret;
}
/*!
 * @brief hasZeroAngle
 * @param model
 * @return
 */

 boolean hasZeroAngle(int model) {
	boolean ret = false;

  if (model == YDLIDAR_R2 ||
      model == YDLIDAR_G2A ||
      model == YDLIDAR_G2B ||
      model == YDLIDAR_G2C ||
      model == YDLIDAR_G1) {
    ret = true;
  }

  return ret;
}

boolean getDeviceInfo() {
  if (lidarPtr==null) {
    return false;
  }

  YdLidarDeviceInfo devinfo=new YdLidarDeviceInfo();
  int op_result = lidarPtr.getDeviceInfo(devinfo);

  if (!YdLidarLowLevelDriver.IS_OK(op_result)) {
	  System.err.print( "get Device Information Error\n");
    return false;
  }

  if (!isSupportLidar(devinfo.model)) {
	  System.out.print(String.format("[YDLIDAR INFO] Current SDK does not support current lidar models[%s]\n",
           lidarModelToString(devinfo.model)));
    return false;
  }

  frequencyOffset     = 0.4f;
  String model = "G2";
  lidar_model = devinfo.model;
  model = lidarModelToString(devinfo.model);
  boolean intensity = hasIntensity(devinfo.model);
  defalutSampleRate = lidarModelDefaultSampleRate(devinfo.model);

  String serial_number;
  lidarPtr.setIntensities(intensity);
  printfVersionInfo(devinfo);

  for (int i = 0; i < 16; i++) {
    serial_number += (char)(devinfo.serialNumber[i] & 0xff);
  }

  if (devinfo.firmware_major != 0 ||
      devinfo.hardware != 0) {
    m_lidarSerialNum = serial_number;
    m_lidarSoftVer = (Major & 0xff) + "." + (
                       Minjor & 0xff);
    m_lidarHardVer = ""+(devinfo.hardware  & 0xff);
  }

  if (hasSampleRate(devinfo.model)) {
    checkSampleRate();
  } else {
    m_SampleRate = defalutSampleRate;
  }

  if (hasScanFrequencyCtrl(devinfo.model)) {
    checkScanFrequency();
  }

  if (hasZeroAngle(devinfo.model)) {
    checkCalibrationAngle(serial_number);
  }

  return true;
}

void handleSingleChannelDevice() {
  if (lidarPtr==null || !lidarPtr.getSingleChannel()) {
    return;
  }

  YdLidarDeviceInfo devinfo;
  int op_result = lidarPtr.getDeviceInfo(devinfo);

  if (!YdLidarLowLevelDriver.IS_OK(op_result)) {
    return;
  }

  printfVersionInfo(devinfo);
  return;
}

void printfVersionInfo( YdLidarDeviceInfo info) {
  if (info.firmware_major == 0 &&
      info.hardware == 0) {
    return;
  }

  m_ParseSuccess = true;
  lidar_model = info.model;
  Major = (byte)(info.firmware_major);
  Minjor = (byte)(info.firmware_minor);
  System.out.print(String.format("[YDLIDAR] Connection established in [%s][%d]:\n"
        + "Firmware version: %u.%u\n"
       +  "Hardware version: %u\n"
        + "Model: %s\n"
        +  "Serial: ",
         m_SerialPort ,
         m_SerialBaudrate,
         Major,
         Minjor,
         ( int)info.hardware ,
         lidarModelToString(lidar_model)));

  for (int i = 0; i < 16; i++) {
	  System.out.print(String.format("%01X", info.serialNumber[i] & 0xff));
  }

  System.out.print("\n");
}

/*!
 * @brief lidarModelToString
 * @param model
 * @return
 */
String lidarModelToString(int model) {
  String name = "unkown";

  switch (model) {
    case YDLIDAR_F4:
      name = "F4";
      break;

    case YDLIDAR_T1:
      name = "T1";

      break;

    case YDLIDAR_F2:
      name = "F2";

      break;

    case YDLIDAR_S4:
      name = "S4";

      break;

    case YDLIDAR_G4:
      name = "G4";

      break;

    case YDLIDAR_X4:
      name = "X4";

      break;

    case YDLIDAR_G4PRO:
      name = "G4PRO";

      break;

    case YDLIDAR_F4PRO:
      name = "F4PRO";

      break;

    case YDLIDAR_R2:
      name = "R2";

      break;

    case YDLIDAR_G10:
      name = "G10";

      break;

    case YDLIDAR_S4B:
      name = "S4B";

      break;

    case YDLIDAR_S2:
      name = "S2";

      break;

    case YDLIDAR_G6:
      name = "G6";

      break;

    case YDLIDAR_G2A:
      name = "G2A";

      break;

    case YDLIDAR_G2B:
      name = "G2B";

      break;

    case YDLIDAR_G2C:
      name = "G2C";

      break;

    case YDLIDAR_G4B:
      name = "G4B";

      break;

    case YDLIDAR_G4C:
      name = "G4C";
      break;

    case YDLIDAR_G1:
      name = "G1";

      break;

    case YDLIDAR_TG15:
      name = "TG15";

      break;

    case YDLIDAR_TG30:
      name = "TG30";

      break;

    case YDLIDAR_TG50:
      name = "TG50";
      break;

    default:
      name = "unkown";
      break;
  }

  return name;
}

/*!
 * @brief isOctaveLidar
 * @param model
 * @return
 */
boolean isOctaveLidar(int model) {
  boolean ret = false;

  if (model == YDLIDAR_G6 ||
      model == YDLIDAR_TG15 ||
      model == YDLIDAR_TG30 ||
      model == YDLIDAR_TG50) {
    ret = true;
  }

  return ret;
}
 int ConvertLidarToUserSmaple(int model, int rate) {
  int _samp_rate = 9;

  switch (rate) {
    case YDLIDAR_RATE_4K:
      _samp_rate = 10;

      if (!isOctaveLidar(model)) {
        _samp_rate = 4;
      }

      break;

    case YDLIDAR_RATE_8K:
      _samp_rate = 16;

      if (!isOctaveLidar(model)) {
        _samp_rate = 8;

        if (model == YDLIDAR_F4PRO) {
          _samp_rate = 6;
        }
      }

      break;

    case YDLIDAR_RATE_9K:
      _samp_rate = 18;

      if (!isOctaveLidar(model)) {
        _samp_rate = 9;
      }

      break;

    case YDLIDAR_RATE_10K:
      _samp_rate = 20;

      if (!isOctaveLidar(model)) {
        _samp_rate = 10;
      }

      break;

    default:
      _samp_rate = 9;

      if (!isOctaveLidar(model)) {
        _samp_rate = 18;
      }

      break;
  }

  return _samp_rate;
}

 int ConvertUserToLidarSmaple(int model, int m_SampleRate,
                                    int defaultRate) {
  int _samp_rate = 9;

  switch (m_SampleRate) {
    case 10:
      _samp_rate = YDLIDAR_RATE_4K;
      break;

    case 16:
      _samp_rate = YDLIDAR_RATE_8K;
      break;

    case 18:
      _samp_rate = YDLIDAR_RATE_9K;
      break;

    case 20:
      _samp_rate = YDLIDAR_RATE_10K;
      break;

    default:
      _samp_rate = defaultRate;
      break;
  }

  if (!isOctaveLidar(model)) {
    _samp_rate = 2;

    switch (m_SampleRate) {
      case 4:
        _samp_rate = YDLIDAR_RATE_4K;
        break;

      case 8:
        _samp_rate = YDLIDAR_RATE_8K;
        break;

      case 9:
        _samp_rate = YDLIDAR_RATE_9K;
        break;

      default:
        break;
    }

    if (model == YDLIDAR_F4PRO) {
      _samp_rate = 0;

      switch (m_SampleRate) {
        case 4:
          _samp_rate = YDLIDAR_RATE_4K;
          break;

        case 6:
          _samp_rate = YDLIDAR_RATE_8K;
          break;

        default:
          break;
      }

    }
  }

  return _samp_rate;
}


void  checkSampleRate() {
  int _rate;
  _rate  = 3;
  int _samp_rate = 9;
  int try_count = 0;
  m_FixedSize = 1440;
  int ans = lidarPtr.getSamplingRate(_rate);

  if (YdLidarLowLevelDriver.IS_OK(ans)) {
    _samp_rate = ConvertUserToLidarSmaple(lidar_model, m_SampleRate, _rate);

    while (_samp_rate != _rate) {
      ans = lidarPtr.setSamplingRate(_rate);
      try_count++;

      if (try_count > 6) {
        break;
      }
    }

    _samp_rate = ConvertLidarToUserSmaple(lidar_model, _rate);
  }

  m_SampleRate = _samp_rate;
  defalutSampleRate = m_SampleRate;
}


boolean CalculateSampleRate(int count, double scan_time) {
  if (count < 1) {
    return false;
  }

  if (global_nodes[0].scan_frequence != 0) {
    double scanfrequency  = global_nodes[0].scan_frequence / 10.0;

    if (YdLidarLowLevelDriver.isTOFLidar(m_LidarType)) {
      if (!YdLidarLowLevelDriver.isOldVersionTOFLidar(lidar_model, Major, Minjor)) {
        scanfrequency  = global_nodes[0].scan_frequence / 10.0 + 3.0;
      }
    }

    int samplerate = (int) ((count * scanfrequency + 500) / 1000);
    int cnt = 0;

    if (SampleRateMap.find(samplerate) != SampleRateMap.end()) {
      cnt = SampleRateMap[samplerate];
    }

    cnt++;
    SampleRateMap[samplerate] =  cnt;

    if (isValidSampleRate(SampleRateMap) || defalutSampleRate == samplerate) {
      m_SampleRate = samplerate;
      m_PointTime = (long) (1e9 / (m_SampleRate * 1000));
      lidarPtr.setPointTime(m_PointTime);

      if (!m_SingleChannel) {
        m_FixedSize = (int) (m_SampleRate * 1000 / (m_ScanFrequency - 0.1));
        System.out.print(String.format("[YDLIDAR]:Fixed Size: %d\n", m_FixedSize));
        System.out.print(String.format("[YDLIDAR]:Sample Rate: %dK\n", m_SampleRate));
      }

      return true;
    } else {
      if (SampleRateMap.size() > 1) {
        SampleRateMap.clear();
      }
    }
  } else {
    if (scan_time > 0.04 && scan_time < 0.4) {
      int samplerate = (int) ((count / scan_time + 500) / 1000);

      if (defalutSampleRate == samplerate) {
        m_SampleRate = samplerate;
        m_PointTime = (long) (1e9 / (m_SampleRate * 1000));
        lidarPtr.setPointTime(m_PointTime);
        return true;
      }
    }

  }


  return false;
}


/*!
 * @brief isSupportScanFrequency
 * @param model
 * @param frequency
 * @return
 */
 boolean isSupportScanFrequency(int model, double frequency) {
	 boolean ret = false;

  if (model >= YDLIDAR_TG15) {
    if (3 <= frequency && frequency <= 15.7) {
      ret = true;
    }
  } else {
    if (5 <= frequency && frequency <= 15.7) {
      ret = true;
    }
  }

  return ret;
}
/*-------------------------------------------------------------
                        checkScanFrequency
-------------------------------------------------------------*/
boolean checkScanFrequency() {
  float frequency = 7.4f;
  int _scan_frequency[]=new int[1];
  float hz = 0;
  int ans = YdLidarLowLevelDriver.RESULT_FAIL;

  if (isSupportScanFrequency(lidar_model, m_ScanFrequency)) {
    m_ScanFrequency += frequencyOffset;
    ans = lidarPtr.getScanFrequency(_scan_frequency) ;

    if (YdLidarLowLevelDriver.IS_OK(ans)) {
      frequency = _scan_frequency[0] / 100.f;
      hz = m_ScanFrequency - frequency;

      if (hz > 0) {
        while (hz > 0.95) {
          lidarPtr.setScanFrequencyAdd(_scan_frequency);
          hz = hz - 1.0f;
        }

        while (hz > 0.09) {
          lidarPtr.setScanFrequencyAddMic(_scan_frequency);
          hz = hz - 0.1f;
        }

        frequency = _scan_frequency[0] / 100.0f;
      } else {
        while (hz < -0.95) {
          lidarPtr.setScanFrequencyDis(_scan_frequency);
          hz = hz + 1.0f;
        }

        while (hz < -0.09) {
          lidarPtr.setScanFrequencyDisMic(_scan_frequency);
          hz = hz + 0.1f;
        }

        frequency = _scan_frequency[0] / 100.0f;
      }
    }
  } else {
    m_ScanFrequency += frequencyOffset;
    System.err.print(String.format("current scan frequency[%f] is out of range.",
            m_ScanFrequency - frequencyOffset));
  }

  ans = lidarPtr.getScanFrequency(_scan_frequency);

  if (YdLidarLowLevelDriver.IS_OK(ans)) {
    frequency = _scan_frequency[0] / 100.0f;
    m_ScanFrequency =  frequency;
  }

  m_ScanFrequency -= frequencyOffset;
  m_FixedSize = (int) (m_SampleRate * 1000 / (m_ScanFrequency - 0.1));
  System.out.print(String.format("[YDLIDAR INFO] Current Scan Frequency: %fHz\n", m_ScanFrequency));
  return true;
}

/*-------------------------------------------------------------
                        checkCalibrationAngle
-------------------------------------------------------------*/
void checkCalibrationAngle(String serialNumber) {
  m_AngleOffset = 0.0f;
  int ans = YdLidarLowLevelDriver.RESULT_FAIL;
  int angle[]=new int[1];
  int retry = 0;
  m_isAngleOffsetCorrected = false;

  while (retry < 2) {
    ans = lidarPtr.getZeroOffsetAngle(angle);

    if (YdLidarLowLevelDriver.IS_OK(ans)) {
      if (angle[0] > 720 || angle[0] < -720) {
        ans = lidarPtr.getZeroOffsetAngle(angle);

        if (!YdLidarLowLevelDriver.IS_OK(ans)) {
          
          //retry++;
          continue;
        }
      }

      m_isAngleOffsetCorrected = (angle[0] != 720);
      m_AngleOffset = (float) (angle[0] / 4.0);
      System.out.print(String.format("[YDLIDAR INFO] Successfully obtained the %s offset angle[%f] from the lidar[%s]\n"
             , m_isAngleOffsetCorrected ? "corrected" : "uncorrrected", m_AngleOffset,
             serialNumber));
      return;
    }

    retry++;
  }

  System.out.print(String.format("[YDLIDAR INFO] Current %s AngleOffset : %f°\n",
         m_isAngleOffsetCorrected ? "corrected" : "uncorrrected", m_AngleOffset));
}



/*-------------------------------------------------------------
						checkCOMMs
-------------------------------------------------------------*/
boolean checkCOMMs() {
  if (lidarPtr==null) {
    // create the driver instance
    lidarPtr = new YdLidarLowLevelDriver();

    if (lidarPtr==null) {
    	System.err.print( "Create Driver fail\n");
      return false;
    }
  }

  if (lidarPtr.isconnected()) {
    return true;
  }

  // Is it COMX, X>4? ->  "\\.\COMX"
  if (m_SerialPort.length() >= 3) {
    if (m_SerialPort.toLowerCase().startsWith("com")) {
      // Need to add "\\.\"?
      if (m_SerialPort.length() > 4 || m_SerialPort.charAt(3) > '4') {
        m_SerialPort = ("\\\\.\\") + m_SerialPort;
      }
    }
  }

  // make connection...
  int op_result = lidarPtr.connect(m_SerialPort, m_SerialBaudrate);

  if (!YdLidarLowLevelDriver.IS_OK(op_result)) {
	  System.err.print(String.format(
            "[CYdLidar] Error, cannot bind to the specified serial port[%s] and baudrate[%d]\n",
            m_SerialPort, m_SerialBaudrate));
    return false;
  }

  lidarPtr.setSingleChannel(m_SingleChannel);
  lidarPtr.setLidarType(m_LidarType);

  return true;
}

/*-------------------------------------------------------------
                        checkStatus
-------------------------------------------------------------*/
boolean checkStatus() {

  if (!checkCOMMs()) {
    return false;
  }

  boolean ret = getDeviceHealth();

  if (!ret) {
    delay(2000);
    ret = getDeviceHealth();

    if (!ret) {
      delay(1000);
    }
  }

  if (!getDeviceInfo()) {
    delay(2000);
    ret = getDeviceInfo();

    if (!ret) {
      return false;
    }
  }

  return true;
}

/*-------------------------------------------------------------
						initialize
-------------------------------------------------------------*/
boolean initialize() {
  if (!checkCOMMs()) {
	  System.err.print(
            "[CYdLidar::initialize] Error initializing YDLIDAR check Comms.\n");
   
    return false;
  }

  if (!checkStatus()) {
	  System.err.print(
            "[CYdLidar::initialize] Error initializing YDLIDAR check status.\n");
 
    return false;
  }

  return true;
}
}
