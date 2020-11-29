package com.zoubworld.robot.lidar.RP;

import com.zoubworld.robot.lidar.driver.ILidarDeviceInfo;

/**
 * Contains information about the device
 *
 * @author Peter Abeles
 */
public class RpLidarDeviceInfo implements ILidarDeviceInfo {
	public int model;
	public int firmware_minor;
	public int firmware_major;
	public int hardware;
	public byte[] serialNumber = new byte[16];

	/**
	 * @return the model
	 */
	public int getModel() {
		return model;
	}

	/**
	 * @return the firmware_minor
	 */
	public int getFirmware_minor() {
		return firmware_minor;
	}

	/**
	 * @return the firmware_major
	 */
	public int getFirmware_major() {
		return firmware_major;
	}

	/**
	 * @return the hardware
	 */
	public int getHardware() {
		return hardware;
	}

	/**
	 * @return the serialNumber
	 */
	public byte[] getSerialNumber() {
		return serialNumber;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarDeviceInfo#print()
	 */
	@Override
	public void print() {
		System.out.println("DEVICE INFO");
		System.out.println("  model = " + model);
		System.out.println("  firmware_minor = " + firmware_minor);
		System.out.println("  firmware_major = " + firmware_major);
		System.out.println("  hardware = " + hardware);

		System.out.print("  Serial = ");
		for (int i = 0; i < serialNumber.length; i++) {
			System.out.printf("%02X", serialNumber[i]);
			if ((i + 1) % 4 == 0)
				System.out.print(" ");
		}
		System.out.println();
	}
}
