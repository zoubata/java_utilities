package com.zoubworld.Crypto.server.Peer.peerbase;

public interface IPeerMessage {

	/** 
	 * Returns the message type as a String.
	 * @return the message type (4-character String)
	 */
	String getMsgType();

	/**
	 * Returns the message type.
	 * @return the message type (4-byte array)
	 */
	byte[] getMsgTypeBytes();

	/**
	 * Returns the message data as a String.
	 * @return the message data
	 */
	String getMsgData();

	/**
	 * Returns the message data.
	 * @return the message data
	 */
	byte[] getMsgDataBytes();

	/**
	 * Returns a packed representation of this message as an
	 * array of bytes.
	 * @return byte array of message data
	 */
	byte[] toBytes();

	String toString();

}