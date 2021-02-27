package com.zoubworld.robot.lidar.YD;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.zoubworld.robot.lidar.RP.RpLidarLowLevelDriver.ReadSerialThread;
import com.zoubworld.robot.lidar.YD.YdLidarHighLevelDriver.node_info;
import com.zoubworld.robot.lidar.driver.ILidarDeviceInfo;
import com.zoubworld.robot.lidar.driver.ILidarHeath;
import com.zoubworld.robot.lidar.driver.ILidarListener;
import com.zoubworld.robot.lidar.driver.ILidarLowLevelDriver;
import com.zoubworld.robot.lidar.driver.ILidarMeasurement;

/**
 * Low level driver for RP-LIDAR.  Just sends and receives packets.  Doesn't attempt to filter bad data or care about
 * timeouts.
 *
 * @author Peter Abeles
 */
// TODO wait for responce doesn't appear to be working
public class YdLidarLowLevelDriver implements ILidarLowLevelDriver {
	// out going packet types
/*	final static	byte SYNC_BYTE0 = (byte) 0xA5;
	final static	byte SYNC_BYTE1 = (byte) 0x5A;
	final static	byte STOP = (byte) 0x25;
	final static	byte RESET = (byte) 0x40;
	final static	byte SCAN = (byte) 0x20;
	final static	byte FORCE_SCAN = (byte) 0x21;
	final static	byte GET_INFO = (byte) 0x50;
	final static	byte GET_HEALTH = (byte) 0x52;
	final static	byte START_MOTOR = (byte) 0xF0;
		// in coming packet types
	final static	byte RCV_INFO = (byte) 0x04;
	final static	byte RCV_HEALTH = (byte) 0x06;
	final static	byte RCV_SCAN = (byte) 0x81;
	*/

	final static	byte  SUNNOISEINTENSITY= (byte) 0xff;
	final static	byte  GLASSNOISEINTENSITY= (byte) 0xfe;

	final static	byte  LIDAR_CMD_STOP   = (byte)                   0x65;
	final static	byte  LIDAR_CMD_SCAN   = (byte)                   0x60;
	final static	byte  LIDAR_CMD_FORCE_SCAN = (byte)              0x61;
	final static	byte  LIDAR_CMD_RESET     = (byte)                0x80;
	final static	byte  LIDAR_CMD_FORCE_STOP  = (byte)              0x00;
	final static	byte  LIDAR_CMD_GET_EAI      = (byte)             0x55;
	final static	byte  LIDAR_CMD_GET_DEVICE_INFO = (byte)          0x90;
	final static	byte  LIDAR_CMD_GET_DEVICE_HEALTH = (byte)        0x92;
	final static	byte  LIDAR_ANS_TYPE_DEVINFO    = (byte)          0x4;
	final static	byte  LIDAR_ANS_TYPE_DEVHEALTH   = (byte)         0x6;
	final static	byte  LIDAR_CMD_SYNC_BYTE       = (byte)          0xA5;
	final static	byte  LIDAR_CMDFLAG_HAS_PAYLOAD  = (byte)         0x80;
	final static	byte  LIDAR_ANS_SYNC_BYTE1     = (byte)           0xA5;
	final static	byte  LIDAR_ANS_SYNC_BYTE2     = (byte)           0x5A;
	final static	byte  LIDAR_ANS_TYPE_MEASUREMENT = (byte)         0x81;
	final static	byte  LIDAR_RESP_MEASUREMENT_SYNCBIT = (byte)       (0x1<<0);
	final static	byte  LIDAR_RESP_MEASUREMENT_QUALITY_SHIFT= (byte) 2;
	final static	byte  LIDAR_RESP_MEASUREMENT_CHECKBIT  = (byte)     (0x1<<0);
	final static	byte  LIDAR_RESP_MEASUREMENT_ANGLE_SHIFT = (byte)   1;
	final static	byte  LIDAR_RESP_MEASUREMENT_DISTANCE_SHIFT = (byte) 2;
	final static	byte  LIDAR_RESP_MEASUREMENT_ANGLE_SAMPLE_SHIFT= (byte) 8;

	final static	byte  LIDAR_CMD_RUN_POSITIVE        = (byte)     0x06;
	final static	byte  LIDAR_CMD_RUN_INVERSION       = (byte)     0x07;
	final static	byte  LIDAR_CMD_SET_AIMSPEED_ADDMIC = (byte)     0x09;
	final static	byte  LIDAR_CMD_SET_AIMSPEED_DISMIC = (byte)     0x0A;
	final static	byte  LIDAR_CMD_SET_AIMSPEED_ADD    = (byte)     0x0B;
	final static	byte  LIDAR_CMD_SET_AIMSPEED_DIS    = (byte)     0x0C;
	final static	byte  LIDAR_CMD_GET_AIMSPEED        = (byte)     0x0D;

	final static	byte  LIDAR_CMD_SET_SAMPLING_RATE   = (byte)     0xD0;
	final static	byte  LIDAR_CMD_GET_SAMPLING_RATE   = (byte)     0xD1;
	final static	byte  LIDAR_STATUS_OK               = (byte)     0x0;
	final static	byte  LIDAR_STATUS_WARNING          = (byte)     0x1;
	final static	byte  LIDAR_STATUS_ERROR            = (byte)     0x2;

	final static	byte  LIDAR_CMD_ENABLE_LOW_POWER    = (byte)     0x01;
	final static	byte  LIDAR_CMD_DISABLE_LOW_POWER   = (byte)     0x02;
	final static	byte  LIDAR_CMD_STATE_MODEL_MOTOR   = (byte)     0x05;
	final static	byte  LIDAR_CMD_ENABLE_CONST_FREQ    = (byte)    0x0E;
	final static	byte  LIDAR_CMD_DISABLE_CONST_FREQ  = (byte)     0x0F;

	final static	byte  LIDAR_CMD_GET_OFFSET_ANGLE    = (byte)      0x93;
	final static	byte  LIDAR_CMD_SAVE_SET_EXPOSURE    = (byte)     0x94;
	final static	byte  LIDAR_CMD_SET_LOW_EXPOSURE    = (byte)      0x95;
	final static	byte  LIDAR_CMD_ADD_EXPOSURE       	= (byte)    0x96;
	final static	byte  LIDAR_CMD_DIS_EXPOSURE       	= (byte)    0x97;

	
	final static	int DEFAULT_TIMEOUT = 2000;    /**< 默认超时时间. */
	final static	int	    DEFAULT_HEART_BEAT = 1000; /**< 默认检测掉电功能时间. */
			final static	int	    MAX_SCAN_NODES = 3600;	   /**< 最大扫描点数. */
					final static	int	    DEFAULT_TIMEOUT_COUNT = 1;
		public static			boolean IS_OK(int x)    {return  (x) == RESULT_OK ;}
		public	static		boolean  IS_TIMEOUT(int x)  {return  (x) == RESULT_TIMEOUT ;}
		public	static		boolean  IS_FAIL(int x)  {return  (x) == RESULT_FAIL ;}
	
		
		final static	int	 RESULT_OK    =  0;
		final static	int	 RESULT_TIMEOUT= -1;
		final static	int	 RESULT_FAIL  =  -2;

		
	SerialPort serialPort;
	InputStream in;
	OutputStream out;

	// buffer for out going data
	byte[] dataOut = new byte[1024];

	// flag to turn on and off verbose debugging output
	boolean verbose = false;

	// thread for reading serial data
	private ReadSerialThread readThread;

	// Storage for incoming packets
	YdLidarHeath health = new YdLidarHeath();
	YdLidarDeviceInfo deviceInfo = new YdLidarDeviceInfo();
	/**
	 * @return the serialPort
	 */
	public SerialPort getSerialPort() {
		return serialPort;
	}

	/**
	 * @param serialPort the serialPort to set
	 */
	public void setSerialPort(SerialPort serialPort) {
		this.serialPort = serialPort;
	}

	/**
	 * @return the verbose
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * @return the health
	 */
	public ILidarHeath getHealth() {
		return health;
	}

	/**
	 * @return the deviceInfo
	 */
	public ILidarDeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	/**
	 * @return the measurement
	 */
	public ILidarMeasurement getMeasurement() {
		return measurement;
	}

	/**
	 * @return the scanning
	 */
	public boolean isScanning() {
		return scanning;
	}

	YdLidarMeasurement measurement = new YdLidarMeasurement();
	ILidarListener listener;

	// if it is in scanning mode.  When in scanning mode it just parses measurement packets
	boolean scanning = false;

	// Type of packet last recieved
	int lastReceived = 0;

	
	

public static 	boolean isTOFLidar(int type) {
  bool ret = false;

  if (type == TYPE_TOF) {
    ret = true;
  }

  return ret;
}

/*!
 * @brief isOctaveLidar
 * @param model
 * @return
 */
public static boolean  isOctaveLidar(int model) {
  bool ret = false;

  if (model == YDLIDAR_G6 ||
      model == YDLIDAR_TG15 ||
      model == YDLIDAR_TG30 ||
      model == YDLIDAR_TG50) {
    ret = true;
  }

  return ret;
}
public static boolean isOldVersionTOFLidar(int model, int Major, int Minor) {
  bool ret = false;

  if (model == YDLIDAR_TG15 ||
      model == YDLIDAR_TG50 ||
      model == YDLIDAR_TG30)  {
    if (Major <= 1 && Minor <= 2) {
      ret = true;
    }

  }

  return ret;
}

	/**
	 * Initializes serial connection
	 *
	 * @param portName Path to serial port
	 * @param listener Listener for in comming packets
	 * @throws Exception
	 */
	public YdLidarLowLevelDriver(String portName, ILidarListener listener) throws Exception {
		System.out.println("Opening port " + portName);

		this.listener = listener;
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		CommPort commPort = portIdentifier.open("FOO", 2000);
		serialPort = (SerialPort) commPort;
		serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
		serialPort.setDTR(false); // lovely undocumented feature where if true the motor stops spinning

		in = serialPort.getInputStream();
		out = serialPort.getOutputStream();

		readThread = new ReadSerialThread();
		new Thread(readThread).start();
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarLowLevelDriver#pause(long)
	 */
	@Override
	public void pause(long milli) {
		synchronized (this) {
			try {
				wait(milli);
			} catch (InterruptedException e) {
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarLowLevelDriver#shutdown()
	 */
	@Override
	public void shutdown() {

		serialPort.close();

		if (readThread != null) {
			readThread.requestStop();
			readThread = null;
		}
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarLowLevelDriver#sendScan(long)
	 */
	@Override
	public boolean sendScan(long timeout) {
		return sendBlocking(SCAN, RCV_SCAN, timeout);
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarLowLevelDriver#sendStop()
	 */
	@Override
	public void sendStop() {
		scanning = false;
		sendNoPayLoad(STOP);
		
		/*  ROS_DEBUG("Stop scan");
  return laser.turnOff();*/
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarLowLevelDriver#sendReset()
	 */
	@Override
	public void sendReset() {
	/*	scanning = false;
		sendNoPayLoad(RESET);*/
		reset(10);
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarLowLevelDriver#sendGetInfo(long)
	 */
	@Override
	public boolean sendGetInfo(long timeout) {
		return sendBlocking(GET_INFO, RCV_INFO, timeout);
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarLowLevelDriver#sendGetHealth(long)
	 */
	@Override
	public boolean sendGetHealth(long timeout) {
		return sendBlocking(GET_HEALTH, RCV_HEALTH, timeout);
	}

	/**
	 * Low level blocking packet send routine
	 */
	protected boolean sendBlocking(byte command, byte expected, long timeout) {
		if (timeout <= 0) {
			sendNoPayLoad(command);
			return true;
		} else {
			lastReceived = 0;
			long endTime = System.currentTimeMillis() + timeout;
			do {
				sendNoPayLoad(command);
				pause(20);
			} while (endTime >= System.currentTimeMillis() && lastReceived != expected);
			return lastReceived == expected;
		}
	}

	/**
	 * Sends a command with no data payload
	 */
	protected void sendNoPayLoad(byte command) {
		if (verbose) {
			System.out.printf("Sending command 0x%02x\n", command & 0xFF);
		}

		dataOut[0] = SYNC_BYTE0;
		dataOut[1] = command;

		try {
			out.write(dataOut, 0, 2);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a command with data payload
	 *
	protected void sendPayLoad(byte command, byte[] payLoad) {
		if (verbose) {
			System.out.printf("Sending command 0x%02x\n", command & 0xFF);
		}

		dataOut[0] = SYNC_BYTE0;
		dataOut[1] = command;
		
		//add payLoad and calculate checksum
		dataOut[2] = (byte) payLoad.length;
		int checksum = 0 ^ dataOut[0] ^ dataOut[1] ^ (dataOut[2] & 0xFF);
		
		for(int i=0; i<payLoad.length; i++){
			dataOut[3 + i] = payLoad[i];
			checksum ^= dataOut[3 + i];
		}
		
		//add checksum - now total length is 3 + payLoad.length + 1
		dataOut[3 + payLoad.length] = (byte) checksum;

		try {
			out.write(dataOut, 0, 3 + payLoad.length + 1);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends a command with data payload - int
	 */
	protected void sendPayLoad(byte command, int payLoadInt) {
		byte[] payLoad = new byte[2];
		
		//load payload little Endian
		payLoad[0] = (byte) payLoadInt;
		payLoad[1] = (byte) (payLoadInt >> 8);
		
		sendPayLoad(command, payLoad);
	}
	
	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarLowLevelDriver#sendStartMotor(int)
	 */
	@Override
	public void sendStartMotor(int speed) {
	//	sendPayLoad(START_MOTOR, speed);
		startMotor();
	}

	/**
	 * Searches for and parses all complete packets inside data
	 */
	protected int parseData(byte[] data, int length) {

		int offset = 0;

		if (verbose) {
			System.out.println("parseData length = " + length);
			for (int i = 0; i < length; i++) {
				System.out.printf("%02x ", (data[i] & 0xFF));
			}
			System.out.println();
		}

		// search for the first good packet it can find
		while (true) {
			if (scanning) {
				if (offset + 5 > length) {
					return length;
				}

				if (parseScan(data, offset, 5)) {
					offset += 5;
				} else {
					if( verbose )
						System.out.println("--- Bad Packet ---");
					offset += 1;
				}
			} else {
				// see if it has consumed all the data
				if (offset + 1 + 4 + 1 > length) {
					return length;
				}

				byte start0 = data[offset];
				byte start1 = data[offset + 1];

				if (start0 == SYNC_BYTE0 && start1 == SYNC_BYTE1) {
					int info = ((data[offset + 2] & 0xFF)) | ((data[offset + 3] & 0xFF) << 8) | ((data[offset + 4] & 0xFF) << 16) | ((data[offset + 5] & 0xFF) << 24);

					int packetLength = info & 0x3FFFFFFF;
					int sendMode = (info >> 30) & 0xFF;
					byte dataType = data[offset + 6];

					if (verbose) {
						System.out.printf("packet 0x%02x length = %d\n", dataType, packetLength);
					}
					// see if it has received the entire packet
					if (offset + 2 + 5 + packetLength > length) {
						if (verbose) {
							System.out.println("  waiting for rest of the packet");
						}
						return offset;
					}

					if (parsePacket(data, offset + 2 + 4 + 1, packetLength, dataType)) {
						lastReceived = dataType & 0xFF;
						offset += 2 + 5 + packetLength;
					} else {
						offset += 2;
					}
				} else {
					offset++;
				}
			}
		}
	}

	protected boolean parsePacket(byte[] data, int offset, int length, byte type) {
		switch (type) {
			case (byte) YdLidarLowLevelDriver.RCV_INFO: // INFO
				return parseDeviceInfo(data, offset, length);

			case (byte) YdLidarLowLevelDriver.RCV_HEALTH: // health
				return parseHealth(data, offset, length);

			case (byte) YdLidarLowLevelDriver.RCV_SCAN: // scan and force-scan
				if (parseScan(data, offset, length)) {
					scanning = true;
					return true;
				}
				break;
			default:
				System.out.printf("Unknown packet type = 0x%02x\n", type);
		}
		return false;
	}

	protected boolean parseHealth(byte[] data, int offset, int length) {
		if (length != 3) {
			System.out.println("  bad health packet");
			return false;
		}

		health.status = data[offset] & 0xFF;
		health.error_code = (data[offset + 1] & 0xFF) | ((data[offset + 2] & 0xFF) << 8);

		listener.handleDeviceHealth(health);
		return true;
	}

	protected boolean parseDeviceInfo(byte[] data, int offset, int length) {
		if (length != 20) {
			System.out.println("  bad device info");
			return false;
		}

		deviceInfo.model = data[offset] & 0xFF;
		deviceInfo.firmware_minor = data[offset + 1] & 0xFF;
		deviceInfo.firmware_major = data[offset + 2] & 0xFF;
		deviceInfo.hardware = data[offset + 3] & 0xFF;

		for (int i = 0; i < 16; i++) {
			deviceInfo.serialNumber[i] = data[offset + 4 + i];
		}

		listener.handleDeviceInfo(deviceInfo);
		return true;
	}

	protected boolean parseScan(byte[] data, int offset, int length) {

		if (length != 5)
			return false;

		byte b0 = data[offset];
		byte b1 = data[offset + 1];

		boolean start0 = (b0 & 0x01) != 0;
		boolean start1 = (b0 & 0x02) != 0;

		if (start0 == start1)
			return false;

		if ((b1 & 0x01) != 1)
			return false;

		measurement.timeMilli = System.currentTimeMillis();
		measurement.start = start0;
		measurement.quality = (b0 & 0xFF) >> 2;
		measurement.angle = ((b1 & 0xFF)  | ((data[offset + 2] & 0xFF) << 8)) >> 1;
		measurement.distance = ((data[offset + 3] & 0xFF) | ((data[offset + 4] & 0xFF) << 8));

		listener.handleMeasurement(measurement);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarLowLevelDriver#setVerbose(boolean)
	 */
	@Override
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * Thread which reads in coming serial data from the LIDAR
	 */
	public class ReadSerialThread implements Runnable {

		byte data[] = new byte[1024 * 2];
		int size = 0;
		volatile boolean run = true;

		public void requestStop() {
			run = false;
		}

		@Override
		public void run() {
			while (run) {
				try {
					if (in.available() > 0) {
						int totalRead = in.read(data, size, data.length - size);

						size += totalRead;

						int used = parseData(data, size);

						// shift the buffer over by the amount read
						for (int i = 0; i < size - used; i++) {
							data[i] = data[i + used];
						}
						size -= used;

					}

					Thread.sleep(5);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	long m_PointTime;
/**
	 * @return the m_PointTime
	 */
	public long getPointTime() {
		return m_PointTime;
	}
	/**
	 * @param m_PointTime the m_PointTime to set
	 */
	public void setPointTime(long m_PointTime) {
		this.m_PointTime = m_PointTime;
	}


boolean m_SingleChannel     ;
int m_LidarType         ;



/**
 * @return the m_LidarType
 */
public int getLidarType() {
	return m_LidarType;
}
/**
 * @param m_LidarType the m_LidarType to set
 */
public void setLidarType(int m_LidarType) {
	this.m_LidarType = m_LidarType;
}
/**
 * @return the m_SingleChannel
 */
public boolean getSingleChannel() {
	return m_SingleChannel;
}
/**
 * @param m_SingleChannel the m_SingleChannel to set
 */
public void setSingleChannel(boolean m_SingleChannel) {
	this.m_SingleChannel = m_SingleChannel;
}

/************************************************************************/
/* get the current scan frequency of lidar                              */
/************************************************************************/
public int getScanFrequency(int frequency[])
{return getScanFrequency( frequency,10);}
		public int getScanFrequency(int frequency[],
    int  timeout) {
  int   ans;

  if (!isConnected) {
    return RESULT_FAIL;
  }

  disableDataGrabbing();
  flushSerial();
  {
   
    if ((ans = sendCommand(LIDAR_CMD_GET_AIMSPEED)) != RESULT_OK) {
      return ans;
    }

    lidar_ans_header response_header;

    if ((ans = waitResponseHeader(response_header, timeout)) != RESULT_OK) {
      return ans;
    }

    if (response_header.type != LIDAR_ANS_TYPE_DEVINFO) {
      return RESULT_FAIL;
    }

    if (response_header.size != 4) {
      return RESULT_FAIL;
    }

    if (waitForData(response_header.size, timeout) != RESULT_OK) {
      return RESULT_FAIL;
    }

byte data[]=new byte[sizeof_frequency];
    getData(data);
    frequency[0]=data[0]+data[1]<<8+data[2]<<16+data[3]<<24;
  }
  return RESULT_OK;

}
int sizeof_frequency=4;

/************************************************************************/
/* add the scan frequency by 1Hz each time                              */
/************************************************************************/
public int setScanFrequencyAdd(int frequency[]
	     ) {return setScanFrequencyAdd( frequency);}

	    	public int setScanFrequencyAdd(int frequency[],
	    
	    	    	    int timeout) {
  int  ans;

  if (!isConnected) {
    return RESULT_FAIL;
  }

  disableDataGrabbing();
  flushSerial();
  {
   // ScopedLocker l(_lock);

    if ((ans = sendCommand(LIDAR_CMD_SET_AIMSPEED_ADD)) != RESULT_OK) {
      return ans;
    }

    lidar_ans_header response_header;

    if ((ans = waitResponseHeader(&response_header, timeout)) != RESULT_OK) {
      return ans;
    }

    if (response_header.type != LIDAR_ANS_TYPE_DEVINFO) {
      return RESULT_FAIL;
    }

    if (response_header.size != 4) {
      return RESULT_FAIL;
    }

    if (waitForData(response_header.size, timeout) != RESULT_OK) {
      return RESULT_FAIL;
    }
byte data[]=new byte[4];
    getData(data);
    frequency[0]=data[0]+data[1]<<8+ data[2]<<16+ data[3]<<24;
  }
  return RESULT_OK;
}

/************************************************************************/
/* add the scan frequency by 0.1Hz each time                            */
/************************************************************************/
public int setScanFrequencyAddMic(int frequency[]/*,
    int timeout*/) {
  result_t  ans;

  if (!isConnected) {
    return RESULT_FAIL;
  }

  disableDataGrabbing();
  flushSerial();
  {
    //ScopedLocker l(_lock);

    if ((ans = sendCommand(LIDAR_CMD_SET_AIMSPEED_ADDMIC)) != RESULT_OK) {
      return ans;
    }

    lidar_ans_header response_header;

    if ((ans = waitResponseHeader(&response_header, timeout)) != RESULT_OK) {
      return ans;
    }

    if (response_header.type != LIDAR_ANS_TYPE_DEVINFO) {
      return RESULT_FAIL;
    }

    if (response_header.size != 4) {
      return RESULT_FAIL;
    }

    if (waitForData(response_header.size, timeout) != RESULT_OK) {
      return RESULT_FAIL;
    }


byte data[]=new byte[4];
    getData(data);
    frequency[0]=data[0]+data[1]<<8+ data[2]<<16+ data[3]<<24;
  }
  return RESULT_OK;
}

/************************************************************************/
/* decrease the scan frequency by 0.1Hz each time                       */
/************************************************************************/
public int setScanFrequencyDisMic(int frequency[]/*,
    int timeout*/) {
  result_t  ans;

  if (!isConnected) {
    return RESULT_FAIL;
  }

  disableDataGrabbing();
  flushSerial();
  {
  //  ScopedLocker l(_lock);

    if ((ans = sendCommand(LIDAR_CMD_SET_AIMSPEED_DISMIC)) != RESULT_OK) {
      return ans;
    }

    lidar_ans_header response_header;

    if ((ans = waitResponseHeader(response_header, timeout)) != RESULT_OK) {
      return ans;
    }

    if (response_header.type != LIDAR_ANS_TYPE_DEVINFO) {
      return RESULT_FAIL;
    }

    if (response_header.size != 4) {
      return RESULT_FAIL;
    }

    if (waitForData(response_header.size, timeout) != RESULT_OK) {
      return RESULT_FAIL;
    }


byte data[]=new byte[4];
    getData(data);
    frequency[0]=data[0]+data[1]<<8+ data[2]<<16+ data[3]<<24;
  }
  return RESULT_OK;
}


/************************************************************************/
/* decrease the scan frequency by 1Hz each time                         */
/************************************************************************/
public int setScanFrequencyDis(int frequency[]/*,
	    int timeout*/)
 {
  result_t  ans;

  if (!isConnected) {
    return RESULT_FAIL;
  }

  disableDataGrabbing();
  flushSerial();
  {
  //  ScopedLocker l(_lock);

    if ((ans = sendCommand(LIDAR_CMD_SET_AIMSPEED_DIS)) != RESULT_OK) {
      return ans;
    }

    lidar_ans_header response_header;

    if ((ans = waitResponseHeader(&response_header, timeout)) != RESULT_OK) {
      return ans;
    }

    if (response_header.type != LIDAR_ANS_TYPE_DEVINFO) {
      return RESULT_FAIL;
    }

    if (response_header.size != 4) {
      return RESULT_FAIL;
    }

    if (waitForData(response_header.size, timeout) != RESULT_OK) {
      return RESULT_FAIL;
    }

byte data[]=new byte[4];
    getData(data);
    frequency[0]=data[0]+data[1]<<8+ data[2]<<16+ data[3]<<24;
  }
  return RESULT_OK;
}	    	
	    	

/************************************************************************/
/*  the get to zero offset angle                                        */
/************************************************************************/
public int getZeroOffsetAngle(int angle[]/*,
    int timeout*/) {
  result_t  ans;

  if (!isConnected) {
    return RESULT_FAIL;
  }

  disableDataGrabbing();
  flushSerial();
  {
 //   ScopedLocker l(_lock);

    if ((ans = sendCommand(LIDAR_CMD_GET_OFFSET_ANGLE)) != RESULT_OK) {
      return ans;
    }

    lidar_ans_header response_header;

    if ((ans = waitResponseHeader(response_header, timeout)) != RESULT_OK) {
      return ans;
    }

    if (response_header.type != LIDAR_ANS_TYPE_DEVINFO) {
      return RESULT_FAIL;
    }

    if (response_header.size < sizeof(offset_angle)) {
      return RESULT_FAIL;
    }

    if (waitForData(response_header.size, timeout) != RESULT_OK) {
      return RESULT_FAIL;
    }
    byte data[]=new byte[4];
    getData(data);
    angle[0]=data[0]+data[1]<<8+ data[2]<<16+ data[3]<<24;
   // getData(reinterpret_cast<uint8_t *>(&angle), sizeof(angle));
  }
  return RESULT_OK;
}

	    	
	    	
	    	
result_t YDlidarDriver::connect(const char *port_path, int baudrate) {
  ScopedLocker lk(_serial_lock);
  m_baudrate = baudrate;
  serial_port = string(port_path);

  if (!_serial) {
    _serial = new serial::Serial(port_path, m_baudrate,
                                 serial::Timeout::simpleTimeout(DEFAULT_TIMEOUT));
  }

  {
  //  ScopedLocker l(_lock);

    if (!_serial->open()) {
      return RESULT_FAIL;
    }

    isConnected = true;

  }

  stopScan();
  delay(100);
  clearDTR();

  return RESULT_OK;
}



void YDlidarDriver::disableDataGrabbing() {
  {
    if (isScanning) {
      isScanning = false;
      _dataEvent.set();
    }
  }
  _thread.join();
}

bool YDlidarDriver::isscanning() const {
  return isScanning;
}
bool YDlidarDriver::isconnected() const {
  return isConnected;
}

result_t YDlidarDriver::sendCommand(uint8_t cmd, const void *payload,
                                    size_t payloadsize) {
  uint8_t pkt_header[10];
  cmd_packet *header = reinterpret_cast<cmd_packet * >(pkt_header);
  uint8_t checksum = 0;

  if (!isConnected) {
    return RESULT_FAIL;
  }

  if (payloadsize && payload) {
    cmd |= LIDAR_CMDFLAG_HAS_PAYLOAD;
  }

  header->syncByte = LIDAR_CMD_SYNC_BYTE;
  header->cmd_flag = cmd;
  sendData(pkt_header, 2) ;

  if ((cmd & LIDAR_CMDFLAG_HAS_PAYLOAD) && payloadsize && payload) {
    checksum ^= LIDAR_CMD_SYNC_BYTE;
    checksum ^= cmd;
    checksum ^= (payloadsize & 0xFF);

    for (size_t pos = 0; pos < payloadsize; ++pos) {
      checksum ^= ((uint8_t *)payload)[pos];
    }

    uint8_t sizebyte = (uint8_t)(payloadsize);
    sendData(&sizebyte, 1);

    sendData((const uint8_t *)payload, sizebyte);

    sendData(&checksum, 1);
  }

  return RESULT_OK;
}

result_t YDlidarDriver::sendData(const uint8_t *data, size_t size) {
  if (!isConnected) {
    return RESULT_FAIL;
  }

  if (data == NULL || size == 0) {
    return RESULT_FAIL;
  }

  size_t r;

  while (size) {
    r = _serial->writeData(data, size);

    if (r < 1) {
      return RESULT_FAIL;
    }

    size -= r;
    data += r;
  }

  return RESULT_OK;
}

result_t YDlidarDriver::waitForData(size_t data_count, int timeout,
                                    size_t *returned_size) {
  size_t length = 0;

  if (returned_size == NULL) {
    returned_size = (size_t *)&length;
  }

  return (result_t)_serial->waitfordata(data_count, timeout, returned_size);
}

int checkAutoConnecting() {
  result_t ans = RESULT_FAIL;
  isAutoconnting = true;

  while (isAutoReconnect && isAutoconnting) {
    {
    //  ScopedLocker l(_serial_lock);

      if (this.serialPort!=null) {
        if (serialPort->isOpen() || isConnected) {
          isConnected = false;
          serialPort.close();
          serialPort = NULL;
        }
      }
    }
    retryCount++;

    if (retryCount > 100) {
      retryCount = 100;
    }

    delay(100 * retryCount);
    int retryConnect = 0;

    while (isAutoReconnect &&
           connect(serial_port.c_str(), m_baudrate) != RESULT_OK) {
      retryConnect++;//@todo voir connect

      if (retryConnect > 25) {
        retryConnect = 25;
      }

      delay(200 * retryConnect);
    }

    if (!isAutoReconnect) {
      isScanning = false;
      return RESULT_FAIL;
    }

    if (isconnected()) {
      delay(100);
      {
     //   ScopedLocker l(_serial_lock);
        ans = startAutoScan();

        if (!IS_OK(ans)) {
          ans = startAutoScan();
        }
      }

      if (IS_OK(ans)) {
        isAutoconnting = false;
        return ans;
      }
    }
  }

  return RESULT_FAIL;

}



int YDlidarDriver::cacheScanData() {
  node_info      local_buf[128];
  size_t         count = 128;
  node_info      local_scan[MAX_SCAN_NODES];
  size_t         scan_count = 0;
  result_t       ans = RESULT_FAIL;
  memset(local_scan, 0, sizeof(local_scan));

  if (m_SingleChannel) {
    waitDevicePackage();
  }

  flushSerial();
  waitScanData(local_buf, count);

  int timeout_count   = 0;
  retryCount = 0;

  while (isScanning) {
    count = 128;
    ans = waitScanData(local_buf, count);

    if (!IS_OK(ans)) {
      if (IS_FAIL(ans) || timeout_count > DEFAULT_TIMEOUT_COUNT) {
        if (!isAutoReconnect) {
          fprintf(stderr, "exit scanning thread!!\n");
          fflush(stderr);
          {
            isScanning = false;
          }
          return RESULT_FAIL;
        } else {
          ans = checkAutoConnecting();

          if (IS_OK(ans)) {
            timeout_count = 0;
            local_scan[0].sync_flag = Node_NotSync;
          } else {
            isScanning = false;
            return RESULT_FAIL;
          }
        }
      } else {
        timeout_count++;
        local_scan[0].sync_flag = Node_NotSync;
        fprintf(stderr, "timout count: %d\n", timeout_count);
        fflush(stderr);
      }
    } else {
      timeout_count = 0;
      retryCount = 0;
    }


    for (size_t pos = 0; pos < count; ++pos) {
      if (local_buf[pos].sync_flag & LIDAR_RESP_MEASUREMENT_SYNCBIT) {
        if ((local_scan[0].sync_flag & LIDAR_RESP_MEASUREMENT_SYNCBIT)) {
          _lock.lock();//timeout lock, wait resource copy
          local_scan[0].stamp = local_buf[pos].stamp;
          local_scan[0].scan_frequence = local_buf[pos].scan_frequence;
          memcpy(scan_node_buf, local_scan, scan_count * sizeof(node_info));
          scan_node_count = scan_count;
          _dataEvent.set();
          _lock.unlock();
        }

        scan_count = 0;
      }

      local_scan[scan_count++] = local_buf[pos];

      if (scan_count == _countof(local_scan)) {
        scan_count -= 1;
      }
    }
  }

  isScanning = false;

  return RESULT_OK;
}

result_t YDlidarDriver::checkDeviceInfo(uint8_t *recvBuffer, uint8_t byte,
                                        int recvPos, int recvSize, int pos) {
  if (asyncRecvPos == sizeof(lidar_ans_header)) {
    if ((((pos < recvSize - 1) && byte == LIDAR_ANS_SYNC_BYTE1) ||
         (last_device_byte == LIDAR_ANS_SYNC_BYTE1 && byte == LIDAR_ANS_SYNC_BYTE2)) &&
        recvPos == 0) {
      if ((last_device_byte == LIDAR_ANS_SYNC_BYTE1 &&
           byte == LIDAR_ANS_SYNC_BYTE2)) {
        asyncRecvPos = 0;
        async_size = 0;
        headerBuffer[asyncRecvPos] = last_device_byte;
        asyncRecvPos++;
        headerBuffer[asyncRecvPos] = byte;
        asyncRecvPos++;
        last_device_byte = byte;
        return RESULT_OK;
      } else {
        if (pos < recvSize - 1) {
          if (recvBuffer[pos + 1] == LIDAR_ANS_SYNC_BYTE2) {
            asyncRecvPos = 0;
            async_size = 0;
            headerBuffer[asyncRecvPos] = byte;
            asyncRecvPos++;
            last_device_byte = byte;
            return RESULT_OK;
          }
        }

      }

    }

    last_device_byte = byte;

    if (header_.type == LIDAR_ANS_TYPE_DEVINFO ||
        header_.type == LIDAR_ANS_TYPE_DEVHEALTH) {
      if (header_.size < 1) {
        asyncRecvPos = 0;
        async_size = 0;
      } else {

        if (header_.type == LIDAR_ANS_TYPE_DEVHEALTH) {
          if (async_size < sizeof(health_)) {
            healthBuffer[async_size] = byte;
            async_size++;

            if (async_size == sizeof(health_)) {
              asyncRecvPos = 0;
              async_size = 0;
              get_device_health_success = true;
              last_device_byte = byte;
              return RESULT_OK;
            }

          } else {
            asyncRecvPos = 0;
            async_size = 0;
          }

        } else {

          if (async_size < sizeof(info_)) {
            infoBuffer[async_size] = byte;
            async_size++;

            if (async_size == sizeof(info_)) {
              asyncRecvPos = 0;
              async_size = 0;
              get_device_info_success = true;

              last_device_byte = byte;
              return RESULT_OK;
            }

          } else {
            asyncRecvPos = 0;
            async_size = 0;
          }
        }
      }
    } else if (header_.type == LIDAR_ANS_TYPE_MEASUREMENT) {
      asyncRecvPos = 0;
      async_size = 0;

    }

  } else {

    switch (asyncRecvPos) {
      case 0:
        if (byte == LIDAR_ANS_SYNC_BYTE1 && recvPos == 0) {
          headerBuffer[asyncRecvPos] = byte;
          last_device_byte = byte;
          asyncRecvPos++;
        }

        break;

      case 1:
        if (byte == LIDAR_ANS_SYNC_BYTE2 && recvPos == 0) {
          headerBuffer[asyncRecvPos] = byte;
          asyncRecvPos++;
          last_device_byte = byte;
          return RESULT_OK;
        } else {
          asyncRecvPos = 0;
        }

        break;

      default:
        break;
    }

    if (asyncRecvPos >= 2) {
      if (((pos < recvSize - 1 && byte == LIDAR_ANS_SYNC_BYTE1) ||
           (last_device_byte == LIDAR_ANS_SYNC_BYTE1 && byte == LIDAR_ANS_SYNC_BYTE2)) &&
          recvPos == 0) {
        if ((last_device_byte == LIDAR_ANS_SYNC_BYTE1 &&
             byte == LIDAR_ANS_SYNC_BYTE2)) {
          asyncRecvPos = 0;
          async_size = 0;
          headerBuffer[asyncRecvPos] = last_device_byte;
          asyncRecvPos++;
        } else {
          if (pos < recvSize - 2) {
            if (recvBuffer[pos + 1] == LIDAR_ANS_SYNC_BYTE2) {
              asyncRecvPos = 0;
            }
          }
        }
      }

      headerBuffer[asyncRecvPos] = byte;
      asyncRecvPos++;
      last_device_byte = byte;
      return RESULT_OK;
    }
  }

  return RESULT_FAIL;

}

result_t YDlidarDriver::waitDevicePackage(int timeout) {
  int recvPos = 0;
  asyncRecvPos = 0;
  int startTs = getms();
  int waitTime = 0;
  result_t ans = RESULT_FAIL;

  while ((waitTime = getms() - startTs) <= timeout) {
    size_t remainSize = PackagePaidBytes - recvPos;
    size_t recvSize = 0;
    result_t ans = waitForData(remainSize, timeout - waitTime, &recvSize);

    if (!IS_OK(ans)) {
      return ans;
    }

    ans = RESULT_FAIL;

    if (recvSize > remainSize) {
      recvSize = remainSize;
    }

    getData(globalRecvBuffer, recvSize);

    for (size_t pos = 0; pos < recvSize; ++pos) {
      uint8_t currentByte = globalRecvBuffer[pos];

      if (checkDeviceInfo(globalRecvBuffer, currentByte, recvPos, recvSize,
                          pos) == RESULT_OK) {
        continue;
      }
    }

    if (get_device_info_success) {
      ans = RESULT_OK;
      break;
    }
  }

  flushSerial();
  return ans;

}

result_t YDlidarDriver::waitPackage(node_info *node, int timeout) {
  int recvPos         = 0;
  int startTs    = getms();
  int waitTime   = 0;
  uint8_t  *packageBuffer = (m_intensities) ? (uint8_t *)&package.package_Head :
                            (uint8_t *)&packages.package_Head;
  uint8_t  package_Sample_Num         = 0;
  int32_t  AngleCorrectForDistance    = 0;
  int  package_recvPos    = 0;
  uint8_t package_type    = 0;

  if (package_Sample_Index == 0) {
    recvPos = 0;

    while ((waitTime = getms() - startTs) <= timeout) {
      size_t remainSize   = PackagePaidBytes - recvPos;
      size_t recvSize     = 0;
      result_t ans = waitForData(remainSize, timeout - waitTime, &recvSize);

      if (!IS_OK(ans)) {
        return ans;
      }

      if (recvSize > remainSize) {
        recvSize = remainSize;
      }

      getData(globalRecvBuffer, recvSize);

      for (size_t pos = 0; pos < recvSize; ++pos) {
        uint8_t currentByte = globalRecvBuffer[pos];

        switch (recvPos) {
          case 0:
            if (currentByte == (PH & 0xFF)) {

            } else {
              continue;
            }

            break;

          case 1:
            CheckSumCal = PH;

            if (currentByte == (PH >> 8)) {

            } else {
              recvPos = 0;
              continue;
            }

            break;

          case 2:
            SampleNumlAndCTCal = currentByte;
            package_type = currentByte & 0x01;

            if ((package_type == CT_Normal) || (package_type == CT_RingStart)) {
              if (package_type == CT_RingStart) {
                scan_frequence = (currentByte & 0xFE) >> 1;
              }
            } else {
              has_package_error = true;
              recvPos = 0;
              continue;
            }

            break;

          case 3:
            SampleNumlAndCTCal += (currentByte * 0x100);
            package_Sample_Num = currentByte;
            break;

          case 4:
            if (currentByte & LIDAR_RESP_MEASUREMENT_CHECKBIT) {
              FirstSampleAngle = currentByte;
            } else {
              has_package_error = true;
              recvPos = 0;
              continue;
            }

            break;

          case 5:
            FirstSampleAngle += currentByte * 0x100;
            CheckSumCal ^= FirstSampleAngle;
            FirstSampleAngle = FirstSampleAngle >> 1;
            break;

          case 6:
            if (currentByte & LIDAR_RESP_MEASUREMENT_CHECKBIT) {
              LastSampleAngle = currentByte;
            } else {
              has_package_error = true;
              recvPos = 0;
              continue;
            }

            break;

          case 7:
            LastSampleAngle = currentByte * 0x100 + LastSampleAngle;
            LastSampleAngleCal = LastSampleAngle;
            LastSampleAngle = LastSampleAngle >> 1;

            if (package_Sample_Num == 1) {
              IntervalSampleAngle = 0;
            } else {
              if (LastSampleAngle < FirstSampleAngle) {
                if ((FirstSampleAngle > 270 * 64) && (LastSampleAngle < 90 * 64)) {
                  IntervalSampleAngle = (float)((360 * 64 + LastSampleAngle -
                                                 FirstSampleAngle) / ((
                                                       package_Sample_Num - 1) * 1.0));
                  IntervalSampleAngle_LastPackage = IntervalSampleAngle;
                } else {
                  IntervalSampleAngle = IntervalSampleAngle_LastPackage;
                }
              } else {
                IntervalSampleAngle = (float)((LastSampleAngle - FirstSampleAngle) / ((
                                                package_Sample_Num - 1) * 1.0));
                IntervalSampleAngle_LastPackage = IntervalSampleAngle;
              }
            }

            break;

          case 8:
            CheckSum = currentByte;
            break;

          case 9:
            CheckSum += (currentByte * 0x100);
            break;
        }

        packageBuffer[recvPos++] = currentByte;
      }

      if (recvPos  == PackagePaidBytes) {
        package_recvPos = recvPos;
        break;
      }
    }

    if (PackagePaidBytes == recvPos) {
      startTs = getms();
      recvPos = 0;

      while ((waitTime = getms() - startTs) <= timeout) {
        size_t remainSize = package_Sample_Num * PackageSampleBytes - recvPos;
        size_t recvSize = 0;
        result_t ans = waitForData(remainSize, timeout - waitTime, &recvSize);

        if (!IS_OK(ans)) {
          return ans;
        }

        if (recvSize > remainSize) {
          recvSize = remainSize;
        }

        getData(globalRecvBuffer, recvSize);

        for (size_t pos = 0; pos < recvSize; ++pos) {
          if (m_intensities) {
            if (recvPos % 3 == 2) {
              Valu8Tou16 += globalRecvBuffer[pos] * 0x100;
              CheckSumCal ^= Valu8Tou16;
            } else if (recvPos % 3 == 1) {
              Valu8Tou16 = globalRecvBuffer[pos];
            } else {
              CheckSumCal ^= globalRecvBuffer[pos];
            }
          } else {
            if (recvPos % 2 == 1) {
              Valu8Tou16 += globalRecvBuffer[pos] * 0x100;
              CheckSumCal ^= Valu8Tou16;
            } else {
              Valu8Tou16 = globalRecvBuffer[pos];
            }
          }

          packageBuffer[package_recvPos + recvPos] = globalRecvBuffer[pos];
          recvPos++;
        }

        if (package_Sample_Num * PackageSampleBytes == recvPos) {
          package_recvPos += recvPos;
          break;
        }
      }

      if (package_Sample_Num * PackageSampleBytes != recvPos) {
        return RESULT_FAIL;
      }
    } else {
      return RESULT_FAIL;
    }

    CheckSumCal ^= SampleNumlAndCTCal;
    CheckSumCal ^= LastSampleAngleCal;

    if (CheckSumCal != CheckSum) {
      CheckSumResult = false;
      has_package_error = true;
    } else {
      CheckSumResult = true;
    }

  }

  uint8_t package_CT;

  if (m_intensities) {
    package_CT = package.package_CT;
  } else {
    package_CT = packages.package_CT;
  }

  (*node).scan_frequence  = 0;

  if ((package_CT & 0x01) == CT_Normal) {
    (*node).sync_flag = Node_NotSync;
    memset((*node).debug_info, 0xff, sizeof((*node).debug_info));

    if (!has_package_error) {
      if (package_index < 10) {
        (*node).debug_info[package_index] = (package_CT >> 1);
        (*node).index = package_index;
      } else {
        (*node).index = 0xff;
      }

      if (package_Sample_Index == 0) {
        package_index++;
      }
    } else {
      (*node).index = 255;
      package_index = 0;
    }
  } else {
    (*node).sync_flag = Node_Sync;
    (*node).index = 255;
    package_index = 0;

    if (CheckSumResult) {
      has_package_error = false;
      (*node).scan_frequence  = scan_frequence;
    }
  }

  (*node).sync_quality = Node_Default_Quality;
  (*node).stamp = 0;

  if (CheckSumResult) {
    if (m_intensities) {
      (*node).sync_quality = ((uint16_t)((
                                           package.packageSample[package_Sample_Index].PakageSampleDistance
                                           & 0x03) << LIDAR_RESP_MEASUREMENT_ANGLE_SAMPLE_SHIFT) |
                              (package.packageSample[package_Sample_Index].PakageSampleQuality));
      (*node).distance_q2 =
        package.packageSample[package_Sample_Index].PakageSampleDistance & 0xfffc;
    } else {
      (*node).distance_q2 = packages.packageSampleDistance[package_Sample_Index];
      (*node).sync_quality = ((uint16_t)(0xfc |
                                         packages.packageSampleDistance[package_Sample_Index] &
                                         0x0003)) << LIDAR_RESP_MEASUREMENT_QUALITY_SHIFT;

    }

    if ((*node).distance_q2 != 0) {
      if (!isTOFLidar(m_LidarType)) {
        if (isOctaveLidar(model)) {
          AngleCorrectForDistance = (int32_t)(((atan(((21.8 * (155.3 - ((
                                                  *node).distance_q2 / 2.0))) / 155.3) / ((
                                                      *node).distance_q2 / 2.0))) * 180.0 / 3.1415) * 64.0);
        } else  {
          AngleCorrectForDistance = (int32_t)(((atan(((21.8 * (155.3 - ((
                                                  *node).distance_q2 / 4.0))) / 155.3) / ((
                                                      *node).distance_q2 / 4.0))) * 180.0 / 3.1415) * 64.0);
        }
      }



    } else {
      AngleCorrectForDistance = 0;
    }

    float sampleAngle = IntervalSampleAngle * package_Sample_Index;

    if ((FirstSampleAngle + sampleAngle +
         AngleCorrectForDistance) < 0) {
      (*node).angle_q6_checkbit = (((uint16_t)(FirstSampleAngle + sampleAngle +
                                    AngleCorrectForDistance + 23040)) << LIDAR_RESP_MEASUREMENT_ANGLE_SHIFT) +
                                  LIDAR_RESP_MEASUREMENT_CHECKBIT;
    } else {
      if ((FirstSampleAngle + sampleAngle + AngleCorrectForDistance) > 23040) {
        (*node).angle_q6_checkbit = (((uint16_t)(FirstSampleAngle + sampleAngle +
                                      AngleCorrectForDistance - 23040)) << LIDAR_RESP_MEASUREMENT_ANGLE_SHIFT) +
                                    LIDAR_RESP_MEASUREMENT_CHECKBIT;
      } else {
        (*node).angle_q6_checkbit = (((uint16_t)(FirstSampleAngle + sampleAngle +
                                      AngleCorrectForDistance)) << LIDAR_RESP_MEASUREMENT_ANGLE_SHIFT) +
                                    LIDAR_RESP_MEASUREMENT_CHECKBIT;
      }
    }
  } else {
    (*node).sync_flag       = Node_NotSync;
    (*node).sync_quality    = Node_Default_Quality;
    (*node).angle_q6_checkbit = LIDAR_RESP_MEASUREMENT_CHECKBIT;
    (*node).distance_q2      = 0;
    (*node).scan_frequence  = 0;
  }


  uint8_t nowPackageNum;

  if (m_intensities) {
    nowPackageNum = package.nowPackageNum;
  } else {
    nowPackageNum = packages.nowPackageNum;
  }

  package_Sample_Index++;

  if (package_Sample_Index >= nowPackageNum) {
    package_Sample_Index = 0;
    CheckSumResult = false;
  }

  return RESULT_OK;
}

result_t YDlidarDriver::waitScanData(node_info *nodebuffer, size_t &count,
                                     int timeout) {
  if (!isConnected) {
    count = 0;
    return RESULT_FAIL;
  }

  size_t     recvNodeCount    =  0;
  int   startTs          = getms();
  int   waitTime         = 0;
  result_t   ans              = RESULT_FAIL;

  while ((waitTime = getms() - startTs) <= timeout && recvNodeCount < count) {
    node_info node;
    ans = waitPackage(&node, timeout - waitTime);

    if (!IS_OK(ans)) {
      count = recvNodeCount;
      return ans;
    }

    nodebuffer[recvNodeCount++] = node;

    if (node.sync_flag & LIDAR_RESP_MEASUREMENT_SYNCBIT) {
      size_t size = _serial->available();
      uint64_t delayTime = 0;
      size_t PackageSize = (m_intensities ? INTENSITY_NORMAL_PACKAGE_SIZE :
                            NORMAL_PACKAGE_SIZE);

      if (size > PackagePaidBytes && size < PackagePaidBytes * PackageSize) {
        size_t packageNum = size / PackageSize;
        size_t Number = size % PackageSize;
        delayTime = packageNum * m_PointTime * PackageSize / 2;

        if (Number > PackagePaidBytes) {
          delayTime += m_PointTime * ((Number - PackagePaidBytes) / 2);
        }

        size = Number;

        if (packageNum > 0 && Number == 0) {
          size = PackageSize;
        }
      }

      nodebuffer[recvNodeCount - 1].stamp = size * trans_delay + delayTime;
      nodebuffer[recvNodeCount - 1].scan_frequence = node.scan_frequence;
      count = recvNodeCount;
      return RESULT_OK;
    }

    if (recvNodeCount == count) {
      return RESULT_OK;
    }
  }

  count = recvNodeCount;
  return RESULT_FAIL;
}


result_t YDlidarDriver::grabScanData(node_info *nodebuffer, size_t &count,
                                     int timeout) {
  switch (_dataEvent.wait(timeout)) {
    case Event::EVENT_TIMEOUT:
      count = 0;
      return RESULT_TIMEOUT;

    case Event::EVENT_OK: {
      if (scan_node_count == 0) {
        return RESULT_FAIL;
      }

      ScopedLocker l(_lock);
      size_t size_to_copy = min(count, scan_node_count);
      memcpy(nodebuffer, scan_node_buf, size_to_copy * sizeof(node_info));
      count = size_to_copy;
      scan_node_count = 0;
    }

    return RESULT_OK;

    default:
      count = 0;
      return RESULT_FAIL;
  }

}


result_t YDlidarDriver::ascendScanData(node_info *nodebuffer, size_t count) {
  float inc_origin_angle = (float)360.0 / count;
  int i = 0;

  for (i = 0; i < (int)count; i++) {
    if (nodebuffer[i].distance_q2 == 0) {
      continue;
    } else {
      while (i != 0) {
        i--;
        float expect_angle = (nodebuffer[i + 1].angle_q6_checkbit >>
                              LIDAR_RESP_MEASUREMENT_ANGLE_SHIFT) /
                             64.0f - inc_origin_angle;

        if (expect_angle < 0.0f) {
          expect_angle = 0.0f;
        }

        uint16_t checkbit = nodebuffer[i].angle_q6_checkbit &
                            LIDAR_RESP_MEASUREMENT_CHECKBIT;
        nodebuffer[i].angle_q6_checkbit = (((uint16_t)(expect_angle * 64.0f)) <<
                                           LIDAR_RESP_MEASUREMENT_ANGLE_SHIFT) + checkbit;
      }

      break;
    }
  }

  if (i == (int)count) {
    return RESULT_FAIL;
  }

  for (i = (int)count - 1; i >= 0; i--) {
    if (nodebuffer[i].distance_q2 == 0) {
      continue;
    } else {
      while (i != ((int)count - 1)) {
        i++;
        float expect_angle = (nodebuffer[i - 1].angle_q6_checkbit >>
                              LIDAR_RESP_MEASUREMENT_ANGLE_SHIFT) /
                             64.0f + inc_origin_angle;

        if (expect_angle > 360.0f) {
          expect_angle -= 360.0f;
        }

        uint16_t checkbit = nodebuffer[i].angle_q6_checkbit &
                            LIDAR_RESP_MEASUREMENT_CHECKBIT;
        nodebuffer[i].angle_q6_checkbit = (((uint16_t)(expect_angle * 64.0f)) <<
                                           LIDAR_RESP_MEASUREMENT_ANGLE_SHIFT) + checkbit;
      }

      break;
    }
  }

  float frontAngle = (nodebuffer[0].angle_q6_checkbit >>
                      LIDAR_RESP_MEASUREMENT_ANGLE_SHIFT) / 64.0f;

  for (i = 1; i < (int)count; i++) {
    if (nodebuffer[i].distance_q2 == 0) {
      float expect_angle =  frontAngle + i * inc_origin_angle;

      if (expect_angle > 360.0f) {
        expect_angle -= 360.0f;
      }

      uint16_t checkbit = nodebuffer[i].angle_q6_checkbit &
                          LIDAR_RESP_MEASUREMENT_CHECKBIT;
      nodebuffer[i].angle_q6_checkbit = (((uint16_t)(expect_angle * 64.0f)) <<
                                         LIDAR_RESP_MEASUREMENT_ANGLE_SHIFT) + checkbit;
    }
  }

  size_t zero_pos = 0;
  float pre_degree = (nodebuffer[0].angle_q6_checkbit >>
                      LIDAR_RESP_MEASUREMENT_ANGLE_SHIFT) / 64.0f;

  for (i = 1; i < (int)count ; ++i) {
    float degree = (nodebuffer[i].angle_q6_checkbit >>
                    LIDAR_RESP_MEASUREMENT_ANGLE_SHIFT) / 64.0f;

    if (zero_pos == 0 && (pre_degree - degree > 180)) {
      zero_pos = i;
      break;
    }

    pre_degree = degree;
  }

  node_info *tmpbuffer = new node_info[count];

  for (i = (int)zero_pos; i < (int)count; i++) {
    tmpbuffer[i - zero_pos] = nodebuffer[i];
  }

  for (i = 0; i < (int)zero_pos; i++) {
    tmpbuffer[i + (int)count - zero_pos] = nodebuffer[i];
  }

  memcpy(nodebuffer, tmpbuffer, count * sizeof(node_info));
  delete[] tmpbuffer;

  return RESULT_OK;
}

/************************************************************************/
/* get health state of lidar                                            */
/************************************************************************/
result_t YDlidarDriver::getHealth(device_health &health, int timeout) {
  result_t ans;

  if (!isConnected) {
    return RESULT_FAIL;
  }

  if (m_SingleChannel) {
    if (get_device_health_success) {
      health = this->health_;
      return RESULT_OK;
    }

    health.error_code = 0;
    health.status = 0;
    return RESULT_OK;
  }

  disableDataGrabbing();
  flushSerial();
  {
    ScopedLocker l(_lock);

    if ((ans = sendCommand(LIDAR_CMD_GET_DEVICE_HEALTH)) != RESULT_OK) {
      return ans;
    }

    lidar_ans_header response_header;

    if ((ans = waitResponseHeader(&response_header, timeout)) != RESULT_OK) {
      return ans;
    }

    if (response_header.type != LIDAR_ANS_TYPE_DEVHEALTH) {
      return RESULT_FAIL;
    }

    if (response_header.size < sizeof(device_health)) {
      return RESULT_FAIL;
    }

    if (waitForData(response_header.size, timeout) != RESULT_OK) {
      return RESULT_FAIL;
    }

    getData(reinterpret_cast<uint8_t *>(&health), sizeof(health));
  }
  return RESULT_OK;
}

/************************************************************************/
/* get device info of lidar                                             */
/************************************************************************
result_t YDlidarDriver::getDeviceInfo(device_info &info, int timeout) {
  result_t  ans;

  if (!isConnected) {
    return RESULT_FAIL;
  }

  if (m_SingleChannel) {
    if (get_device_info_success) {
      info = this->info_;
      return RESULT_OK;
    }

    info.model = YDLIDAR_S2;
    info.firmware_version = 0;
    info.hardware_version = 0;
    return RESULT_OK;
  }

  disableDataGrabbing();
  flushSerial();
  {
    ScopedLocker l(_lock);

    if ((ans = sendCommand(LIDAR_CMD_GET_DEVICE_INFO)) != RESULT_OK) {
      return ans;
    }

    lidar_ans_header response_header;

    if ((ans = waitResponseHeader(&response_header, timeout)) != RESULT_OK) {
      return ans;
    }

    if (response_header.type != LIDAR_ANS_TYPE_DEVINFO) {
      return RESULT_FAIL;
    }

    if (response_header.size < sizeof(lidar_ans_header)) {
      return RESULT_FAIL;
    }

    if (waitForData(response_header.size, timeout) != RESULT_OK) {
      return RESULT_FAIL;
    }

    getData(reinterpret_cast<uint8_t *>(&info), sizeof(info));
    model = info.model;
  }

  return RESULT_OK;
}

/************************************************************************/
/* the set to signal quality                                            */
/************************************************************************/
void YDlidarDriver::setIntensities(const bool &isintensities) {
  if (m_intensities != isintensities) {
    if (globalRecvBuffer) {
      delete[] globalRecvBuffer;
      globalRecvBuffer = NULL;
    }

    globalRecvBuffer = new uint8_t[isintensities ? sizeof(node_package) : sizeof(
                                                   node_packages)];
  }

  m_intensities = isintensities;

  if (m_intensities) {
    PackageSampleBytes = 3;
  } else {
    PackageSampleBytes = 2;
  }
}
/**
* @brief 设置雷达异常自动重新连接 \n
* @param[in] enable    是否开启自动重连:
*     true	开启
*	  false 关闭
*/
void YDlidarDriver::setAutoReconnect(const bool &enable) {
  isAutoReconnect = enable;
}


void YDlidarDriver::checkTransDelay() {
  //calc stamp
  trans_delay = _serial->getByteTime();
  sample_rate = lidarModelDefaultSampleRate(model) * 1000;

  switch (model) {
    case YDLIDAR_G4://g4
    case YDLIDAR_G4PRO:
    case YDLIDAR_F4PRO:
    case YDLIDAR_G6://g6
    case YDLIDAR_TG15:
    case YDLIDAR_TG30:
    case YDLIDAR_TG50:
      if (m_sampling_rate == -1) {
        sampling_rate _rate;
        _rate.rate = 0;
        getSamplingRate(_rate);
        m_sampling_rate = _rate.rate;
      }

      sample_rate = ConvertLidarToUserSmaple(model, m_sampling_rate);
      sample_rate *= 1000;

      break;

    case YDLIDAR_G2C:
      sample_rate = 4000;
      break;

    case YDLIDAR_G1:
      sample_rate = 9000;
      break;

    case YDLIDAR_G4C:
      sample_rate = 4000;
      break;

    default:
      break;
  }

  m_PointTime = 1e9 / sample_rate;
}

result_t YDlidarDriver::stopScan(int timeout) {
  UNUSED(timeout);

  if (!isConnected) {
    return RESULT_FAIL;
  }

  ScopedLocker l(_lock);
  sendCommand(LIDAR_CMD_FORCE_STOP);
  delay(5);
  sendCommand(LIDAR_CMD_STOP);
  delay(5);
  return RESULT_OK;
}

result_t YDlidarDriver::createThread() {
  _thread = CLASS_THREAD(YDlidarDriver, cacheScanData);

  if (_thread.getHandle() == 0) {
    isScanning = false;
    return RESULT_FAIL;
  }

  isScanning = true;
  return RESULT_OK;
}


result_t YDlidarDriver::startAutoScan(bool force, int timeout) {
  result_t ans;

  if (!isConnected) {
    return RESULT_FAIL;
  }

  flushSerial();
  delay(10);
  {

    ScopedLocker l(_lock);

    if ((ans = sendCommand(force ? LIDAR_CMD_FORCE_SCAN : LIDAR_CMD_SCAN)) !=
        RESULT_OK) {
      return ans;
    }

    if (!m_SingleChannel) {
      lidar_ans_header response_header;

      if ((ans = waitResponseHeader(&response_header, timeout)) != RESULT_OK) {
        return ans;
      }

      if (response_header.type != LIDAR_ANS_TYPE_MEASUREMENT) {
        return RESULT_FAIL;
      }

      if (response_header.size < 5) {
        return RESULT_FAIL;
      }
    }

  }

  if (isSupportMotorCtrl(model)) {
    startMotor();
  }

  return RESULT_OK;
}

/************************************************************************/
/*  start to scan                                                       */
/************************************************************************/
result_t YDlidarDriver::startScan(bool force, int timeout) {
  result_t ans;

  if (!isConnected) {
    return RESULT_FAIL;
  }

  if (isScanning) {
    return RESULT_OK;
  }

  stop();
  checkTransDelay();
  flushSerial();
  delay(30);
  {
    ScopedLocker l(_lock);

    if ((ans = sendCommand(force ? LIDAR_CMD_FORCE_SCAN : LIDAR_CMD_SCAN)) !=
        RESULT_OK) {
      return ans;
    }

    if (!m_SingleChannel) {

      lidar_ans_header response_header;

      if ((ans = waitResponseHeader(&response_header, timeout)) != RESULT_OK) {
        return ans;
      }

      if (response_header.type != LIDAR_ANS_TYPE_MEASUREMENT) {
        return RESULT_FAIL;
      }

      if (response_header.size < 5) {
        return RESULT_FAIL;
      }
    }

    ans = this->createThread();
  }

  if (isSupportMotorCtrl(model)) {
    startMotor();
  }

  return ans;
}



/************************************************************************/
/*  stopMotor                                                           */
/************************************************************************/
result_t YDlidarDriver::stopMotor() {
  ScopedLocker l(_lock);

  if (isSupportMotorDtrCtrl) {
    clearDTR();
    delay(500);
  } else {
    setDTR();
    delay(500);
  }

  return RESULT_OK;
}



/************************************************************************/
/*  get the sampling rate of lidar                                      */
/************************************************************************/
result_t YDlidarDriver::getSamplingRate(sampling_rate &rate, int timeout) {
  result_t  ans;

  if (!isConnected) {
    return RESULT_FAIL;
  }

  disableDataGrabbing();
  flushSerial();
  {
    ScopedLocker l(_lock);

    if ((ans = sendCommand(LIDAR_CMD_GET_SAMPLING_RATE)) != RESULT_OK) {
      return ans;
    }

    lidar_ans_header response_header;

    if ((ans = waitResponseHeader(&response_header, timeout)) != RESULT_OK) {
      return ans;
    }

    if (response_header.type != LIDAR_ANS_TYPE_DEVINFO) {
      return RESULT_FAIL;
    }

    if (response_header.size != 1) {
      return RESULT_FAIL;
    }

    if (waitForData(response_header.size, timeout) != RESULT_OK) {
      return RESULT_FAIL;
    }

    getData(reinterpret_cast<uint8_t *>(&rate), sizeof(rate));
    m_sampling_rate = rate.rate;
  }
  return RESULT_OK;
}

/************************************************************************/
/*  the set to sampling rate                                            */
/************************************************************************/
result_t YDlidarDriver::setSamplingRate(sampling_rate &rate, int timeout) {
  result_t  ans;

  if (!isConnected) {
    return RESULT_FAIL;
  }

  disableDataGrabbing();
  flushSerial();
  {
    ScopedLocker l(_lock);

    if ((ans = sendCommand(LIDAR_CMD_SET_SAMPLING_RATE)) != RESULT_OK) {
      return ans;
    }

    lidar_ans_header response_header;

    if ((ans = waitResponseHeader(&response_header, timeout)) != RESULT_OK) {
      return ans;
    }

    if (response_header.type != LIDAR_ANS_TYPE_DEVINFO) {
      return RESULT_FAIL;
    }

    if (response_header.size != 1) {
      return RESULT_FAIL;
    }

    if (waitForData(response_header.size, timeout) != RESULT_OK) {
      return RESULT_FAIL;
    }

    getData(reinterpret_cast<uint8_t *>(&rate), sizeof(rate));
  }
  return RESULT_OK;
}

/************************************************************************/
/*  the get to zero offset angle                                        */
/************************************************************************/
result_t YDlidarDriver::getZeroOffsetAngle(offset_angle &angle,
    int timeout) {
  result_t  ans;

  if (!isConnected) {
    return RESULT_FAIL;
  }

  disableDataGrabbing();
  flushSerial();
  {
    ScopedLocker l(_lock);

    if ((ans = sendCommand(LIDAR_CMD_GET_OFFSET_ANGLE)) != RESULT_OK) {
      return ans;
    }

    lidar_ans_header response_header;

    if ((ans = waitResponseHeader(&response_header, timeout)) != RESULT_OK) {
      return ans;
    }

    if (response_header.type != LIDAR_ANS_TYPE_DEVINFO) {
      return RESULT_FAIL;
    }

    if (response_header.size < sizeof(offset_angle)) {
      return RESULT_FAIL;
    }

    if (waitForData(response_header.size, timeout) != RESULT_OK) {
      return RESULT_FAIL;
    }

    getData(reinterpret_cast<uint8_t *>(&angle), sizeof(angle));
  }
  return RESULT_OK;
}



std::string YDlidarDriver::getSDKVersion() {
  return SDKVerision;
}
std::map<std::string, std::string>  YDlidarDriver::lidarPortList() {
  std::vector<PortInfo> lst = list_ports();
  std::map<std::string, std::string> ports;

  for (std::vector<PortInfo>::iterator it = lst.begin(); it != lst.end(); it++) {
    std::string port = "ydlidar" + (*it).device_id;
    ports[port] = (*it).port;
  }

  return ports;
}

}


/**
 * Sends a command with data payload
 */
protected int sendPayLoad(byte command, byte[] payLoad) {
	if (verbose) {
		System.out.printf("Sending command 0x%02x\n", command & 0xFF);
	}
	byte header[]=new byte[2];
	header[0]=LIDAR_CMD_SYNC_BYTE;
	header[1]=command;
	  sendData(header) ;
	  byte checksum = 0; 
	  if ((cmd & LIDAR_CMDFLAG_HAS_PAYLOAD) && payloadsize && payload) {
		    checksum ^= LIDAR_CMD_SYNC_BYTE;
		    checksum ^= cmd;
		    checksum ^= (payloadsize & 0xFF);

		    for (byte pos = 0; pos < payLoad.length; ++pos) {
		      checksum ^= payload[pos];
		    }

		    char sizebyte= (byte)(payLoad.length);
		    sendData(sizebyte);

		    sendData(payload);

		    sendData(checksum);
		  }
	  return RESULT_OK;
}
/** send data : a byte
 * */
int sendData(byte data)
{
	datas[]=new char[1];
	datas[0]=data;
	return sendData(datas);
}
/** send data array of byte
 * */
int sendData(byte dataOut[])
{
	if (!isConnected) {
    return RESULT_FAIL;
  }

  if (data == NULL || size == 0) {
    return RESULT_FAIL;
  }
	try {
		out.write(dataOut, 0,  dataOut.length );
		out.flush();
	} catch (IOException e) {
		e.printStackTrace();
		  return RESULT_FAIL;
	}

	  return RESULT_OK;
}


int getData(byte data[]) {
  if (!isConnected) {
    return RESULT_FAIL;
  }

  int size=0;

  while (size!=data.length) {
		if (in.available() > 0) {
			int totalRead = in.read(data, size, data.length - size);

			size += totalRead;
			}

  }

  return RESULT_OK;
}
boolean isscanning()  {
  return isScanning;
}
boolean isconnected()  {
  return isConnected;
}

void flushSerial() {
  if (!isConnected) {
    return;
  }
  while(in.available()>0)
in.skip(in.available());
  
}


void disconnect() {
  isAutoReconnect = false;

  if (!isConnected) {
    return ;
  }

  stop();
 // delay(10);
 /* ScopedLocker l(_serial_lock);

  if (_serial) {
    if (_serial->isOpen()) {
      _serial->closePort();
    }
  }*/
  if (in!=null) {	
	  in.close();
	  }
  if (serialPort!=null) {	
	  serialPort.close();
	  }
  
  
  isConnected = false;

}

private connect(const char *port_path, int baudrate) {
	System.out.println("Opening port " + portName);

	this.listener = listener;
	CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
	CommPort commPort = portIdentifier.open("FOO", 2000);
	serialPort = (SerialPort) commPort;
	serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
	serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
	serialPort.setDTR(false); // lovely undocumented feature where if true the motor stops spinning

	in = serialPort.getInputStream();
	out = serialPort.getOutputStream();

	readThread = new ReadSerialThread();
	new Thread(readThread).start();

 
  //  if (!serialPort.is->open())      return RESULT_FAIL;
   

    isConnected = true;

  

  stopScan();
  delay(100);
  serialPort.setDTR(false);

  return RESULT_OK;
}
public YdLidarLowLevelDriver()
{
	serialPort=null;
	
		  isConnected         = false;
		  isScanning          = false;
		 
		  m_intensities       = false;
		  isAutoReconnect     = true;
		  isAutoconnting      = false;
		  m_baudrate          = 230400;
		  isSupportMotorDtrCtrl  = true;
		  scan_node_count     = 0;
		  sample_rate         = 5000;
		  m_PointTime         = 1e9 / 5000;
		  trans_delay         = 0;
		  scan_frequence      = 0;
		  m_sampling_rate     = -1;
		  model               = -1;
		  retryCount          = 0;
		  has_device_header   = false;
		  m_SingleChannel     = false;
		  m_LidarType         = TYPE_TOF;

		
		  PackageSampleBytes  = 2;
		  IntervalSampleAngle = 0.0;
		  FirstSampleAngle    = 0;
		  LastSampleAngle     = 0;
		  CheckSum            = 0;
		  CheckSumCal         = 0;
		  SampleNumlAndCTCal  = 0;
		  LastSampleAngleCal  = 0;
		  CheckSumResult      = true;
		  Valu8Tou16          = 0;

		  last_device_byte    = 0x00;
		  asyncRecvPos        = 0;
		  async_size          = 0;
		  
		   infoBuffer = new YdLidarDeviceInfo();
		  healthBuffer = new YdLidarHeath();
		  
		  get_device_health_success = false;
		  get_device_info_success = false;

		  package_Sample_Index = 0;
		  IntervalSampleAngle_LastPackage = 0.0;
		  package_index = 0;
		  has_package_error = false;
}
private int grabScanData(byte nodebuffer[],
        int timeout) {
	if (scan_node_count == 0) {
	return RESULT_FAIL;
	}
	in.read(nodebuffer);
	scan_node_count = 0;
	
	return RESULT_OK;
	
	
}

/************************************************************************/
/* get health state of lidar                                            */
/************************************************************************/
int getHealth(YdLidarHeath ahealth, int timeout) {
  result_t ans;

  if (!isConnected) {
    return RESULT_FAIL;
  }

  if (m_SingleChannel) {
    if (get_device_health_success) {
      ahealth.Set(health);
      return RESULT_OK;
    }

    health.error_code = 0;
    health.status = 0;
    return RESULT_OK;
  }

  disableDataGrabbing();
  flushSerial();
  {
   
    if ((ans = sendCommand(LIDAR_CMD_GET_DEVICE_HEALTH)) != RESULT_OK) {
      return ans;
    }

   
    if ((ans = waitResponseHeader(ahealth, timeout)) != RESULT_OK) {
      return ans;
    }

    if (ahealth.type != LIDAR_ANS_TYPE_DEVHEALTH) {
      return RESULT_FAIL;
    }

    if (ahealth.size < sizeof(ahealth)) {
      return RESULT_FAIL;
    }

    if (waitForData(ahealth.size, timeout) != RESULT_OK) {
      return RESULT_FAIL;
    }
    
    byte data=new byte[3];
    getData(data);
    health.status=data[0];
    health.error_code=data[1]+((int)data[2])<<8;    
  }
  return RESULT_OK;
}

/************************************************************************/
/* get device info of lidar                                             */
/************************************************************************/
int getDeviceInfo(YdLidarDeviceInfo info, int timeout) {
 int ans;
  if (!isConnected) {
    return RESULT_FAIL;
  }

  if (m_SingleChannel) {
    if (get_device_info_success) {
    	deviceInfo = this.deviceInfo;
      return RESULT_OK;
    }

    deviceInfo.model = YDLIDAR_S2;
    deviceInfo.firmware_version = 0;
    deviceInfo.hardware_version = 0;
    return RESULT_OK;
  }

  disableDataGrabbing();
  flushSerial();
  {
    ScopedLocker l(_lock);

    if ((ans = sendCommand(LIDAR_CMD_GET_DEVICE_INFO)) != RESULT_OK) {
      return ans;
    }

    lidar_ans_header response_header;

    if ((ans = waitResponseHeader(&response_header, timeout)) != RESULT_OK) {
      return ans;
    }

    if (response_header.type != LIDAR_ANS_TYPE_DEVINFO) {
      return RESULT_FAIL;
    }
 int sizeof_info=(1+2+1+16);
   
    if (response_header.length < sizeof_info) {
      return RESULT_FAIL;
    }

    if (waitForData((1+1+4+1), timeout) != RESULT_OK) {
      return RESULT_FAIL;
    }

  
    byte data=new byte[sizeof_info];
    getData(data);
    deviceInfo.model=data[0];
    deviceInfo.firmware_minor=data[1];
    deviceInfo.firmware_major=data[2];
    deviceInfo.hardware_version=data[3]; 
    for(int i=0;i<16;i++)
    deviceInfo.serialnum[i]=data[4+i];   
     model = deviceInfo.model;
  }

  return RESULT_OK;
}


/************************************************************************/
/* the set to signal quality                                            */
/************************************************************************/
void setIntensities( boolean isintensities) {
  if (m_intensities != isintensities) {
    if (globalRecvBuffer) {
      delete[] globalRecvBuffer;
      globalRecvBuffer = NULL;
    }

    globalRecvBuffer = new byte[isintensities ? sizeof_node_package : sizeof_node_packages];
  }
  m_intensities = isintensities;

  if (m_intensities) {
    PackageSampleBytes = 3;
  } else {
    PackageSampleBytes = 2;
  }
}
  int sizeof_node_package =2+1+1+2+2+2+sizeof_PackageNode*PackageSampleMaxLngth;
	int	  sizeof_node_packages=2+1+1+2+2+2+2*PackageSampleMaxLngth;
	int			  sizeof_PackageNode=1+2;
				  int PackageSampleMaxLngth =0x100;

void  setAutoReconnect( boolean enable) {
  isAutoReconnect = enable;
}

/************************************************************************/
/*  stopMotor                                                           */
/************************************************************************/
int stopMotor() {
 
  if (isSupportMotorDtrCtrl) {
    this.serialPort.setDTR(false);
    delay(500);
  } else {
	  this.serialPort.setDTR(true);
    delay(500);
  }
  
  return RESULT_OK;
}

* lidarPortList() {
  
	* ports=CommPortIdentifier.getPortIdentifiers();
	/*
	std::vector<PortInfo> lst = list_ports();
  std::map<std::string, std::string> ports;

  for (std::vector<PortInfo>::iterator it = lst.begin(); it != lst.end(); it++) {
    std::string port = "ydlidar" + (*it).device_id;
    ports[port] = (*it).port;
  }
*/
  return ports;
}
String SDKVerision="1.4.5";
static String getSDKVersion() {
  return SDKVerision;
}

int stopScan(int timeout) {
  
  if (!isConnected) {
    return RESULT_FAIL;
  }

  sendCommand(LIDAR_CMD_FORCE_STOP);
  delay(5);
  sendCommand(LIDAR_CMD_STOP);
  delay(5);
  return RESULT_OK;
}


/************************************************************************/
/*   stop scan                                                   */
/************************************************************************/
int stop() {
  if (isAutoconnting) {
    isAutoReconnect = false;
    isScanning = false;
  }

  disableDataGrabbing();
  stopScan();

  if (isSupportMotorCtrl(model)) {
    stopMotor();
  }

  return RESULT_OK;
}

/************************************************************************/
/*  reset device                                                        */
/************************************************************************/
int reset(int timeout) {
  int ans;

  if (!isConnected) {
    return RESULT_FAIL;
  }


  if ((ans = sendCommand(LIDAR_CMD_RESET)) != RESULT_OK) {
    return ans;
  }

  return RESULT_OK;
}

/************************************************************************/
/*  startMotor                                                          */
/************************************************************************/
int startMotor() {
  
  if (isSupportMotorDtrCtrl) {
    serialPort.setDTR(true);
    delay(500);
  } else {
	  serialPort.setDTR(false);
    delay(500);
  }

  return RESULT_OK;
}



void disableDataGrabbing() {
  {
    if (isScanning) {
      isScanning = false;
    //  _dataEvent.set();
    }
  }
//  _thread.join();
}




int waitResponseHeader(byte header[sizeof_lidar_ans_header],
    int timeout) {
  int  recvPos     = 0;
  int startTs = System.nanoTime()/1E6;
  byte  recvBuffer[sizeof_lidar_ans_header];
 // uint8_t  *headerBuffer = reinterpret_cast<uint8_t *>(header);
  uint32_t waitTime = 0;
  has_device_header = false;
  last_device_byte = 0x00;

  while ((waitTime = (System.nanoTime()/1E6) - startTs) <= timeout) {
    size_t remainSize = sizeof(lidar_ans_header) - recvPos;
    size_t recvSize = 0;
    int ans = waitForData(remainSize, timeout - waitTime, &recvSize);

    if (!IS_OK(ans)) {
      return ans;
    }

    if (recvSize > remainSize) {
      recvSize = remainSize;
    }

    ans = getData(recvBuffer, recvSize);

    if (IS_FAIL(ans)) {
      return RESULT_FAIL;
    }

    for (int pos = 0; pos < recvSize; ++pos) {
      byte currentByte = recvBuffer[pos];

      switch (recvPos) {
        case 0:
          if (currentByte != LIDAR_ANS_SYNC_BYTE1) {
            if (last_device_byte == (PH & 0xFF) && currentByte == (PH >> 8)) {
              has_device_header = true;
            }

            last_device_byte = currentByte;
            continue;
          }

          break;

        case 1:
          if (currentByte != LIDAR_ANS_SYNC_BYTE2) {
            last_device_byte = currentByte;
            recvPos = 0;
            continue;
          }

          break;
      }

      headerBuffer[recvPos++] = currentByte;
      last_device_byte = currentByte;

      if (recvPos == sizeof_lidar_ans_header) {
        return RESULT_OK;
      }
    }
  }

  return RESULT_FAIL;
}

int waitForData(int data_count, int timeout,
                                    size_t *returned_size) {
  size_t length = 0;
  int startTs = System.nanoTime()/1E6;

  if (returned_size == NULL) {
    returned_size = (size_t *)&length;
  }
while(in.available()<data_count && (( (System.nanoTime()/1E6) - startTs) <= timeout));

 // return (result_t)_serial->waitfordata(data_count, timeout, returned_size);
return in.available();
}

int PH=0x55AA;
int NORMAL_PACKAGE_SIZE=90;
int INTENSITY_NORMAL_PACKAGE_SIZE=130;



/************************************************************************/
/*  start to scan                                                       */
/************************************************************************/
int startScan(bool force, int timeout) {
  int ans;

  if (!isConnected) {
    return RESULT_FAIL;
  }

  if (isScanning) {
    return RESULT_OK;
  }

  stop();
  checkTransDelay();
  flushSerial();
 // delay(30);
  {
  //  ScopedLocker l(_lock);

    if ((ans = sendCommand(force ? LIDAR_CMD_FORCE_SCAN : LIDAR_CMD_SCAN)) !=
        RESULT_OK) {
      return ans;
    }

    if (!m_SingleChannel) {

      byte response_header[sizeof_lidar_ans_header];

      if ((ans = waitResponseHeader(response_header, timeout)) != RESULT_OK) {
        return ans;
      }

      if (response_header_type(response_header) != LIDAR_ANS_TYPE_MEASUREMENT) {
        return RESULT_FAIL;
      }

      if (response_header_size(response_header) < 5) {
        return RESULT_FAIL;
      }
    }

    ans = this->createThread();
  }

  if (isSupportMotorCtrl(model)) {
    startMotor();
  }

  return ans;
}
int response_header_size(byte response_header[])
{
	return response_header[0];
	}
int response_header_type(byte response_header[])
{
	return response_header[0]+response_header[1]<<8+response_header[2]<<16+(response_header[3]&0x3f)<<24;
	}
}



int createThread() {
 // _thread = CLASS_THREAD(YDlidarDriver, cacheScanData);
	readThread = new ReadSerialThread();
	new Thread(readThread).start();

  if (readThread!=null) {
  
    isScanning = false;
    return RESULT_FAIL;
  }

  isScanning = true;
  return RESULT_OK;
}
public int grabScanData(node_info[] global_nodes, int count) {
	// TODO Auto-generated method stub
	return 0;
}