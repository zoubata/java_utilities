//package com.EECE412A3;
package com.zoubworld.Crypto.server.VPN;

public class Helpers {

	public static String ByteToString(byte[] arr)
	{
		/*String s = "";
		for(int i = 0; i < arr.length; i++ )
		{
			s += ((int)arr[i]) + ", ";
		}
		return s;*/
		return DatatypeConverter.printHexBinary(arr);
	}
}
