/*
	File: PeerInfo.java
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
	Jan 25 2007	Nadeem Abdul Hamid	Created
 */

package com.zoubworld.Crypto.server.Peer.peerbase;

import java.util.Objects;

import com.zoubworld.Crypto.server.Utils.IStoreObject;

/**
 * Maintains information related to the location of a peer node in 
 * the system, along with the peer's (unique) identifier in the peer-to-
 * peer network.
 * 
 * @author Nadeem Abdul Hamid
 *
 **/
public class PeerInfo implements IPeerInfo, IStoreObject {
	@Override
	public int hashCode() {
		return Objects.hash(host, id, port);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PeerInfo other = (PeerInfo) obj;
		return Objects.equals(host, other.host) && Objects.equals(id, other.id) && port == other.port;
	}

	private String id;
	private String host;
	private int port;
	
	@Override
	public  PeerInfo Parser(String line)
	{
		String id1=null;
		String host1=null;
		int port1=0;
		try {
		String[] t = line.split(",");
		  id1=t[0].trim();
		 host1=t[1].trim();
		 port1=Integer.parseInt(t[2].trim());
		}
		catch(java.lang.ArrayIndexOutOfBoundsException e)
		{
			return null;
		}
		 return new PeerInfo(id1,host1,port1);
	}
	@Override
	public  String toLine()
	{/*
		if(!PeerInfo.class.isInstance(o) )
			return null;
		PeerInfo p=(PeerInfo)o;*/
		return id+" , "+host+" , "+port;
	}
	
	/**
	 * Creates and initializes a new PeerInfo object.
	 * 
	 * @param id this peer's (unique) identifier in the peer-to-peer system
	 * @param host the IP address
	 * @param port the TCP port number
	 */
	public PeerInfo(String id, String host, int port) {
		this.id = id;
		this.host = host;
		this.port = port;
	}
	
	/**
	 * Creates and initializes a new PeerInfo object, with the peer's
	 * identifier set to "host:port".
	 * @param host the IP address
	 * @param port the TCP port number
	 */
	public PeerInfo(String host, int port) {
		this(host + ":" + port, host, port);
	}
	
	/**
	 * Creates a PeerInfo object storing only the TCP port number.
	 */
	public PeerInfo(int port) {
		this(null, port);
	}

	/**
	 * @return the host
	 */
	@Override
	public String getHost() {
		return host;
	}

	/**
	 * @return the id
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * @return the port
	 */
	@Override
	public int getPort() {
		return port;
	}

	/**
	 * @param host the host to set
	 */
	@Override
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @param id the id to set
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param port the port to set
	 */
	@Override
	public void setPort(int port) {
		this.port = port;
	}
	
	public String toString() {
		return id + " (" + host + ":" + port + ")";
	}
}
