/*
	File: NormalSocket.java
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
	Jan 29 2007	Nadeem Abdul Hamid	Created
 */


package com.zoubworld.Crypto.server.Peer.peerbase.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * Encapsulates the standard Socket object of the Java library
 * to fit the SocketInterface of the PeerBase system.
 * 
 * @author Nadeem Abdul Hamid
 *
 */
public class NormalSocket implements SocketInterface {

	private Socket s;
	private InputStream is;
	private OutputStream os;
	
	
	/**
	 * Creates a stream socket and connects it to the specified port number on the named host.
	 * 
	 * @param host the host name, or <code>null</code> for the loopback address
	 * @param port the port number
	 * @throws IOException if an I/O error occurs when creating the socket
	 * @throws UnknownHostException if the IP address of the host could not be determined
	 */
	public NormalSocket(String host, int port) 
	throws IOException, UnknownHostException {
		this(new Socket(host, port));
		//SSLSocket()
	}
	
	
	/**
	 * Encapsulates a normal Java API Socket object.
	 * @param socket an already-open socket connection
	 * @throws IOException
	 */
	public NormalSocket(Socket socket)
	throws IOException {
		s = socket;
		is = s.getInputStream();
		os = s.getOutputStream();		
	}
	
	
	/* (non-Javadoc)
	 * @see peerbase.SocketInterface#close()
	 */
	public void close() throws IOException {
		is.close();
		os.close();
		s.close();
	}

	/* (non-Javadoc)
	 * @see peerbase.SocketInterface#read()
	 */
	public int read() throws IOException {
		return is.read();
	}

	/* (non-Javadoc)
	 * @see peerbase.SocketInterface#read(byte[])
	 */
	public int read(byte[] b) throws IOException {
		return is.read(b);
	}

	/* (non-Javadoc)
	 * @see peerbase.SocketInterface#write(byte[])
	 */
	public void write(byte[] b) throws IOException {
		os.write(b);
		os.flush();
	}

}
