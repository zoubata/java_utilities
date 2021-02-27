package com.zoubworld.robot.lidar.driver;

public interface ILidarDeviceInfo {

	void print();

	/**
	 * @return the model
	 */
	public int getModel() ;

	/**
	 * @return the firmware_minor
	 */
	public int getFirmware_minor();

	/**
	 * @return the firmware_major
	 */
	public int getFirmware_major();

	/**
	 * @return the hardware
	 */
	public int getHardware() ;

	/**
	 * @return the serialNumber
	 */
	public byte[] getSerialNumber();

}