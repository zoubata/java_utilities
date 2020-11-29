package com.zoubworld.robot.lidar.driver;

import com.zoubworld.robot.lidar.driver.ILidarDeviceInfo;
import com.zoubworld.robot.lidar.driver.ILidarHeath;
import com.zoubworld.robot.lidar.driver.ILidarMeasurement;

import gnu.io.SerialPort;

public interface ILidarLowLevelDriver {
/*
	
*/
	/**
	 * Pauses for the specified number of milliseconds
	 */
	void pause(long milli);

	/**
	 * Shuts down the serial connection and threads
	 */
	void shutdown();

	/**
	 * Request that it enter scan mode
	 *
	 * @param timeout Blocking time.  Resends packet periodically.  <= 0 means no blocking.
	 * @return true if successful
	 */
	boolean sendScan(long timeout);

	/**
	 * Sends a STOP packet
	 */
	void sendStop();

	/**
	 * Sends a reset packet which will put it into its initial state
	 */
	void sendReset();

	/**
	 * Requests that a sensor info packet be sent
	 *
	 * @param timeout Blocking time.  Resends packet periodically.  <= 0 means no blocking.
	 * @return true if successful
	 */
	boolean sendGetInfo(long timeout);

	/**
	 * Requests that a sensor health packet be sent
	 *
	 * @param timeout Blocking time.  Resends packet periodically.  <= 0 means no blocking.
	 * @return true if successful
	 */
	boolean sendGetHealth(long timeout);

	/**
	 * Sends a start motor command
	 */
	void sendStartMotor(int speed);

	void setVerbose(boolean verbose);

	
	
	/**
	 * @return the serialPort
	 */
	public SerialPort getSerialPort();

	/**
	 * @param serialPort the serialPort to set
	 */
	public void setSerialPort(SerialPort serialPort);

	/**
	 * @return the verbose
	 */
	public boolean isVerbose();

	/**
	 * @return the health
	 */
	public ILidarHeath getHealth();

	/**
	 * @return the deviceInfo
	 */
	public ILidarDeviceInfo getDeviceInfo();

	/**
	 * @return the measurement
	 */
	public ILidarMeasurement getMeasurement();

	/**
	 * @return the scanning
	 */
	public boolean isScanning();
}