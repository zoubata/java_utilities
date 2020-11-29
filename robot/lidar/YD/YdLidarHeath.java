package com.zoubworld.robot.lidar.YD;

import com.zoubworld.robot.lidar.driver.ILidarHeath;

/**
 * Packet which describes the sensor's health
 *
 * @author Peter Abeles
 */
public class YdLidarHeath implements ILidarHeath {
	public int status;
	public int error_code;

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @return the error_code
	 */
	public int getError_code() {
		return error_code;
	}

	/* (non-Javadoc)
	 * @see com.zoubworld.robot.lidar.RP.ILidarHeath#print()
	 */
	@Override
	public void print() {
		System.out.println("HEALTH:");
		switch (status) {
			case 0:
				System.out.println("  Good");
				break;
			case 1:
				System.out.println("  Warning");
				break;
			case 2:
				System.out.println("  Error");
				break;
			default:
				System.out.println("  unknown = " + status);
		}
		System.out.println("  error_code = " + error_code);
	}
}
/*
struct device_health {
  uint8_t   status; ///< 健康状体
  uint16_t  error_code; ///< 错误代码
} __attribute__((packed))  ;
*/
/*
struct lidar_ans_header {
  uint8_t  syncByte1;
  uint8_t  syncByte2;
  uint32_t size: 30;
  uint32_t subType: 2;
  uint8_t  type;
} __attribute__((packed));
*/