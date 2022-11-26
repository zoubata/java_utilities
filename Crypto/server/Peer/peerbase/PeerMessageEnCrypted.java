/*
	File: PeerMessage.java
	Copyright 2007 by Nadeem Abdul Hamid
	
	Permission to use, copy, modify, and distribute this software and its
	documentation for any purpose and without fee is hereby granted, provided
	that the above copyright notice appear in all copies and that both the
	copyright notice and this permission notice and warranty disclaimer appear
	in supporting documentation, and that the names of the authors or their
	employers not be used in advertising or publicity pertaining to distri-
	bution of the software without specific, written prior permission.

	The authors and their employers disclaim all warranties with regard to
	this software, including all implied warranties of merchantability and
	fitness. In no event shall the authors or their employers be liable for 
	any special, indirect or consequential damages or any damages whatsoever 
	resulting from loss of use, data or profits, whether in an action of 
	contract, negligence or other tortious action, arising out of or in 
	connection with the use or performance of this software, even if 
	advised of the possibility of such damage.

	Date		Author				Changes
	Jan 31 2007	Nadeem Abdul Hamid	Created
 */

package com.zoubworld.Crypto.server.Peer.peerbase;

import java.io.IOException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.zoubworld.Crypto.server.Peer.peerbase.socket.SocketInterface;

/**
 * Represents a message, composed of type and data fields, in the PeerBase
 * system. Also provides functionality for converting messages to/from
 * byte arrays in a portable manner. The type of every message is a 4-byte
 * value (i.e. 4-character string).
 * 
 * @author Nadeem Abdul Hamid
 * 
 * 
 * message format : 
 * byte[4] : byte[4] : byte[n] : byte[32]
 * TYPE    :  LEN    : DATA    : HMAC-SHA256
 * TYPE : is link to the PeerHandler to define an action per Type into an object HandlerInterface
 * the value of type is the own of Node app class. 
 * LEN in byte of data.
 */
public class PeerMessageEnCrypted implements IPeerMessage {

	private byte[] type;
	private byte[] data;
	IPeerInfo owner;
	/**
	 * Constructs a new PeerMessage object.
	 * @param type the message type (4 bytes)
	 * @param data the message data
	 */
	public PeerMessageEnCrypted(byte[] type, byte[] data) {
		this.type = (byte[])type.clone();
		this.data = (byte[])data.clone();
	}
	static public IPeerMessage Factory(byte[] type, byte[] data) {
		return new PeerMessage((byte[])type.clone(), (byte[])data.clone());
	}
	
	
	/** 
	 * Constructs a new PeerMessage object.
	 * @param type the message type (4 characters)
	 * @param data the message data
	 */
	public PeerMessageEnCrypted(String type, String data) {
		this(type.getBytes(), data.getBytes());
	}
	static public IPeerMessage Factory(String type, String data) {
		return new PeerMessage(type.getBytes(), data.getBytes());
	}
	
	
	/**
	 * Constructs a new PeerMessage object.
	 * @param type the message type (4 characters)
	 * @param data the message data
	 */
	public PeerMessageEnCrypted(String type, byte[] data) {
		this(type.getBytes(), data);
	}
	static public IPeerMessage Factory(String type, byte[] data) {
		return new PeerMessage(type.getBytes(), data);
	}
	
	
	protected PeerMessageEnCrypted() {
		// TODO Auto-generated constructor stub
	}


	/**
	 * Constructs a new PeerMessage object by reading data
	 * from the given socket connection.
	 * @param s a socket connection object
	 * @throws IOException if I/O error occurs
	 */
	public PeerMessageEnCrypted(SocketInterface s) throws IOException {
		
		type = new byte[4];
		byte[] thelen = new byte[4]; // for reading length of message data
		if (s.read(type) != 4)
			throw new IOException("EOF in PeerMessage constructor: type");
		if (s.read(thelen) != 4)
			throw new IOException("EOF in PeerMessage constructor: thelen");
		
		int len = PeerMessage.byteArrayToInt(thelen);
		data = new byte[len];
		
		if (s.read(data) != len)
			throw new IOException("EOF in PeerMessage constructor: " +
									"Unexpected message data length");
		byte[]  hash = new byte[32];
		if (s.read(hash) != len)
			throw new IOException("EOF in PeerMessage constructor: " +
									"Unexpected message data length miss signature");
		//AES
		byte[] hmacSha256 = null;
		 try {
		      Mac mac = Mac.getInstance("HmacSHA256");
		      SecretKeySpec secretKeySpec = new SecretKeySpec(getSecretKey(), "HmacSHA256");
		      mac.init(secretKeySpec);
		      mac.update(type);
		      hmacSha256 = mac.doFinal(data);
		    } catch (Exception e) {
		      throw new RuntimeException("Failed to calculate hmac-sha256", e);
		    }
		if (!Arrays.equals(hmacSha256,hash))
			throw new IOException("bad signature");
		 
		
	}
	
	static public IPeerMessage Factory(SocketInterface s) throws IOException {
		
			IPeerMessage a=new PeerMessage(s);			
			return a;
			
		}
		
	
	/** 
	 * Returns the message type as a String.
	 * @return the message type (4-character String)
	 */
	@Override
	public String getMsgType() {
		return new String(type);
	}
	
	
	/**
	 * Returns the message type.
	 * @return the message type (4-byte array)
	 */
	@Override
	public byte[] getMsgTypeBytes() {
		return (byte[])type.clone();
	}
	
	
	/**
	 * Returns the message data as a String.
	 * @return the message data
	 */
	@Override
	public String getMsgData() {
		return new String(data);
	}

	
	/**
	 * Returns the message data.
	 * @return the message data
	 */
	@Override
	public byte[] getMsgDataBytes() {
		return (byte[])data.clone();
	}

	
	/**
	 * Returns a packed representation of this message as an
	 * array of bytes.
	 * @return byte array of message data
	 */
	@Override
	public byte[] toBytes() {
		byte[] bytes = new byte[4 + 4 + data.length+32];
		byte[] lenbytes = PeerMessage.intToByteArray(data.length);
		
		for (int i=0; i<4; i++) bytes[i] = type[i];
		for (int i=0; i<4; i++) bytes[i+4] = lenbytes[i];
		for (int i=0; i<data.length; i++) bytes[i+8] = data[i];
		

		byte[] hmacSha256 = null;
		 try {
		      Mac mac = Mac.getInstance("HmacSHA256");
		      SecretKeySpec secretKeySpec = new SecretKeySpec(getSecretKey(), "HmacSHA256");
		      mac.init(secretKeySpec);
		      mac.update(type);
		      hmacSha256 = mac.doFinal(data);
		    } catch (Exception e) {
		      throw new RuntimeException("Failed to calculate hmac-sha256", e);
		    }
		
		for (int i=0; i<32; i++) bytes[i+8+data.length] = hmacSha256[i];
		
		return bytes;
	}
	
	
	@Override
	public String toString() {
		return "PeerMessage[" + getMsgType() + ":" + getMsgData() + "]";
	}
	
	
	
}
