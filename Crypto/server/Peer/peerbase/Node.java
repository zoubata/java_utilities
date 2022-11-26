/*
	File: Node.java
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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import com.zoubworld.Crypto.server.Peer.peerbase.Handler.*;
import com.zoubworld.Crypto.server.Peer.peerbase.Router.*;

import com.zoubworld.Crypto.server.Peer.peerbase.socket.SocketFactory;
import com.zoubworld.Crypto.server.Peer.peerbase.socket.SocketInterface;

/**
 * This is the primary class for the PeerBase peer-to-peer development system.
 * It maintains this node's information (id, host, port), a list of known peers,
 * list of message handlers, and a handler for routing data through the 
 * P2P network.
 * 
 * @author Nadeem Abdul Hamid
 *
 */
public class Node {

	//********************************************************************
	// INNER CLASSES
	//
	
	/*
	 *  This class is used to respond to and handle incoming connections
	 * in a separate thread.
	 */
	private class PeerHandler extends Thread {
		private SocketInterface s;
		
		public PeerHandler(Socket socket) throws IOException {
			s = SocketFactory.getSocketFactory().makeSocket(socket);
			
		//	new PeerInfo(socket.getInetAddress().getHostAddress(), socket.getPort());
		
		}
		
		public void run() {
			LoggerUtil.getLogger().fine("New PeerHandler: " + s);
			
			PeerConnection peerconn = new PeerConnection(null, s);
			IPeerMessage peermsg = peerconn.recvData();
			if (!handlers.containsKey(peermsg.getMsgType())) {
				LoggerUtil.getLogger().fine("Not handled: " + peermsg);
			}
			else {
				LoggerUtil.getLogger().finer("Handling: " + peermsg);
				handlers.get(peermsg.getMsgType()).handleMessage(peerconn, 
																 peermsg);
			}
			LoggerUtil.getLogger().fine("Disconnecting incoming: " + peerconn);
			// NOTE: log message should indicate null peerconn host
			
			peerconn.close();
		}
	}
	
	
	/*
	 * This class is used to set up "stabilizer" functions to run at
	 * specified intervals
	 */
	private class StabilizerRunner extends Thread {
		private StabilizerInterface st;
		private int delay;   // milliseconds
		
		public StabilizerRunner(StabilizerInterface st, int delay) {
			this.st = st;
			this.delay = delay;
		}
		
		public void run() {
			while (true) {
				st.stabilizer();
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					LoggerUtil.getLogger().fine(""+e);
				}
			}
		}
	}

	
	//********************************************************************
	// PEERNODE CLASS MEMBERS
	//
	
	private static final int SOCKETTIMEOUT = 2000; // milliseconds
	private static final int DEFAULTPORT = 7879; // milliseconds
	
	private IPeerInfo myInfo;
	
	private int maxPeers;  // maximum size of peers list; 0 means unlimited
	private Hashtable<String,IPeerInfo> peers;
	
	private Hashtable<String,IHandler> handlers;
	private IRouter router;
	
	private boolean shutdown;  // node is in shutdown mode?
	
	
	/**
	 * Initialize this node with the given info and the specified
	 * limit on the size of the peer list.
	 * 
	 * @param maxPeers the maximum size of the peer list (0 means 'no limit')
	 * @param info the id and host/port information for this node
	 */
	public Node(int maxPeers, IPeerInfo info) {
		
		if (info==null)
		info=new PeerInfo(null, DEFAULTPORT);
		if (info.getHost() == null)
			info.setHost(getHostname());
		if (info.getId() == null)
			info.setId(info.getHost() + ":" + info.getPort());
		
		this.myInfo = info;
		this.maxPeers = maxPeers;
		
		this.peers = new Hashtable<String,IPeerInfo>();
		this.handlers = new Hashtable<String,IHandler>();
		this.router = null;
		
		this.shutdown = false;
	}
	
	public void setDefaultHandler()
	{
	this.addRouter(new Router(this));
	
	this.addHandler( new JoinHandler(this));
	this.addHandler( new ListHandler(this));
	this.addHandler( new NameHandler(this));
	this.addHandler( new QuitHandler(this));
	this.addHandler( new PingHandler(this));
	
	}
	
	/**
	 * Initialize this node with no limit on the size of the peer
	 * list and set up to listen for incoming connections on 
	 * the specified port.
	 */
	public Node(int port) {
		this(0, new PeerInfo(port));
	}
	

	/*
	 * Attempt to determine the name or IP address of the machine
	 * this node is running on.
	 */
	private String getHostname() {
		String host = "";
		try {
			Socket s = new Socket("www.google.com", 80);
			host = s.getLocalAddress().getHostAddress();
		}
		catch (UnknownHostException e) {
			LoggerUtil.getLogger().warning("Could not determine host: " + e);
		}
		catch (IOException e) {
			LoggerUtil.getLogger().warning(e.toString());
		}
		
		LoggerUtil.getLogger().config("Determined host: " + host);
		return host;
	}
	
	
	/**
	 * Create a socket to listen for incoming connections.
	 * @param port the port number to listen on, or 0 to use any free port
	 * @return the server socket
	 * @throws IOException if error occurs
	 */
	public ServerSocket makeServerSocket(int port) throws IOException {
		return makeServerSocket(port, 5);
	}


	/**
	 * Create a socket to listen for incoming connections.
	 * @param port the port number to listen on, or 0 to use any free port
	 * @param backlog he maximum length of the queue
	 * @return the server socket
	 * @throws IOException if error occurs
	 */
	public ServerSocket makeServerSocket(int port, int backlog) 
	throws IOException {
		ServerSocket s = new ServerSocket(port, backlog);
		s.setReuseAddress(true);
		return s;
	}
	
	
	/**
	 * Attempts to route and send a message to the specified peer, optionally waiting
	 * and returning any replies. This method using the Node's routing function
	 * to decide the next immediate peer to actually send the message to, based on 
	 * the peer identifier of the final destination. If no router function (object)
	 *  has been registered, it will not work. 
	 * 
	 * @param peerid the destination peer identifier
	 * @param msgtype the type of the message being send
	 * @param msgdata the message data
	 * @param waitreply whether to wait for reply(ies)
	 * @return list of replies (may be empty if error occurred)
	 */
	public List<IPeerMessage> sendToPeer(String peerid, String msgtype, 
					String msgdata, boolean waitreply) {
		IPeerInfo pd = null;
		if (router != null)
			pd = router.route(peerid);
		if (pd == null) {
			LoggerUtil.getLogger().severe(
				String.format("Unable to route %s to %s", msgtype, peerid));
			return new ArrayList<IPeerMessage>();
		}
		
		return connectAndSend(pd, msgtype, msgdata, waitreply);
	}


	/**
	 * Connects to the specified peer and sends a message, optionally waiting
	 * and returning any replies.
	 * 
	 * @param pd the peer information
	 * @param msgtype the type of the message being send
	 * @param msgdata the message data
	 * @param waitreply whether to wait for reply(ies)
	 * @return list of replies (may be empty if error occurred)
	 */
	public List<IPeerMessage> connectAndSend(IPeerInfo pd, String msgtype, 
					String msgdata, boolean waitreply) {
		List<IPeerMessage> msgreply = new ArrayList<IPeerMessage>();
		try {
			PeerConnection peerconn = new PeerConnection(pd);
			IPeerMessage tosend = new PeerMessage(msgtype, msgdata);
			peerconn.sendData(tosend);
			LoggerUtil.getLogger().fine("Sent " + tosend + "/" + peerconn);
			
			if (waitreply) {
				IPeerMessage onereply = peerconn.recvData();
				while (onereply != null) {
					msgreply.add(onereply);
					LoggerUtil.getLogger().fine("Got reply " + onereply);
					onereply = peerconn.recvData();
				}
			}
			
			peerconn.close();
		}
		catch (IOException e) {
			LoggerUtil.getLogger().warning("Error: " + e + "/"
					+ pd + "/" + msgtype);
		}
		return msgreply;
	}
	
	
	/**
	 * Starts the loop which is the primary operation of the Node.
	 * The main loop opens a server socket, listens for incoming connections,
	 * and dispatches them to registered handlers appropriately.
	 */
	public void mainLoop() {
		try {
			ServerSocket s = makeServerSocket(myInfo.getPort());
			s.setSoTimeout(SOCKETTIMEOUT);

			while (!shutdown) {
				LoggerUtil.getLogger().fine("Listening...");
				try {
					Socket clientsock = s.accept();
					clientsock.setSoTimeout(0);
					
					PeerHandler ph = new PeerHandler(clientsock);
					ph.start();
				}
				catch (SocketTimeoutException e) {
					LoggerUtil.getLogger().fine("" + e);
					continue;
				}
			} // end while (!shutdown);
			
			s.close();
		}
		catch (SocketException e) {
			LoggerUtil.getLogger().severe("Stopping main loop (SocketExc): " + e);
		}
		catch (IOException e) {
			LoggerUtil.getLogger().severe("Stopping main loop (IOExc): " + e);
		}
		
		shutdown = true;
	}
	
	
	/**
	 * Starts a "stabilizer" function running at the specified
	 * interval.
	 * 
	 * @param st the stabilizer function object
	 * @param delay the delay (in milliseconds)
	 */
	public void startStabilizer(StabilizerInterface st, int delay) {
		StabilizerRunner sr = new StabilizerRunner(st, delay);
		sr.start();
	}
	
	
	public void addHandler(String msgtype, IHandler handler) {
		handlers.put(msgtype, handler);
	}
	public void addHandler( IHandler handler) {
		handlers.put(handler.getMsgType(), handler);
	}
	
	
	public void addRouter(IRouter router) {
		this.router = router;
	}
	
	
	public boolean addPeer(IPeerInfo pd) {
		return addPeer(pd.getId(), pd);
	}
	
	
	/**
	 * Add new peer information to the peer list, indexed by the given
	 * key.
	 * @param key the key associated with the peer
	 * @param pd the peer information
	 * @return true if successful; false if maxPeers is reached, or if the
	 * peer list already contains the given key.
	 */
	public boolean addPeer(String key, IPeerInfo pd) {
		if ((maxPeers == 0 || peers.size() < maxPeers) &&
				!peers.containsKey(key)) {
			peers.put(key, pd);
			return true;
		}
		return false;
	}
	
	
	public IPeerInfo getPeer(String key) {
		return peers.get(key);
	}
	
	
	public IPeerInfo removePeer(String key) {
		return peers.remove(key);
	}
	
	
	public Set<String> getPeerKeys() {
		return peers.keySet();
	}
	
	
	public int getNumberOfPeers() {
		return peers.size();
	}
	
	
	public int getMaxPeers() {
		return maxPeers;
	}
	
	
	public boolean maxPeersReached() {
		return maxPeers > 0 && peers.size() == maxPeers;
	}
	
	
	public String getId() {
		return myInfo.getId();
	}
	
	
	public String getHost() {
		return myInfo.getHost();
	}


	public int getPort() {
		return myInfo.getPort();
	}

}
