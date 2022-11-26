package com.zoubworld.Crypto.server.Peer.peerbase;

public interface IPeerInfo {

	/**
	 * @return the host
	 */
	String getHost();

	/**
	 * @return the id
	 */
	String getId();

	/**
	 * @return the port
	 */
	int getPort();

	/**
	 * @param host the host to set
	 */
	void setHost(String host);

	/**
	 * @param id the id to set
	 */
	void setId(String id);

	/**
	 * @param port the port to set
	 */
	void setPort(int port);

}