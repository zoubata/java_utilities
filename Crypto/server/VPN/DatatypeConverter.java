/**
 * 
 */
package com.zoubworld.Crypto.server.VPN;

/**
 * @author zouabat
 *  manage compatibility issue since java 1.8 ->
 *
 */
public class DatatypeConverter {

	/**
	 * 
	 */
	public DatatypeConverter() {
		// TODO Auto-generated constructor stub
	}
	   private static final char[] hexCode = "0123456789ABCDEF".toCharArray();
//copy/paste :
// https://github.com/javaee/jaxb-spec/blob/master/jaxb-api/src/main/java/javax/xml/bind/DatatypeConverterImpl.java
//https://github.com/javaee/jaxb-spec/blob/master/jaxb-api/src/main/java/javax/xml/bind/DatatypeConverter.java
	 public static String printHexBinary(byte[] data) {
	        StringBuilder r = new StringBuilder(data.length * 2);
	        for (byte b : data) {
	            r.append(hexCode[(b >> 4) & 0xF]);
	            r.append(hexCode[(b & 0xF)]);
	        }
	        return r.toString();
	    }

}
