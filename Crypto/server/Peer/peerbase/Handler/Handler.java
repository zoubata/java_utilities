package com.zoubworld.Crypto.server.Peer.peerbase.Handler;
import java.io.PrintStream;

import com.zoubworld.Crypto.server.Peer.peerbase.IHandler;
import com.zoubworld.Crypto.server.Peer.peerbase.IPeerMessage;
import com.zoubworld.Crypto.server.Peer.peerbase.Node;
import com.zoubworld.Crypto.server.Peer.peerbase.PeerConnection;
import com.zoubworld.Crypto.server.Peer.peerbase.PeerMessage;

/**
 * 
 * @author zoubata
 * 
 * basic handler squeleton
 *
 */
public class Handler implements IHandler {
	protected Node peer;
	static boolean verbose=true;
	static PrintStream outVerbose = System.out;
	
	public Handler(Node peer) { this.peer = peer; }
			
	public void handleMessage(PeerConnection peerconn, IPeerMessage msg) {
		if(verbose) outVerbose.println("Handle from "+peerconn.getPeerInfo().toString() +" message : "+msg.toString());
		// check for correct number of arguments
		String data = msg.getMsgData();
	 {
			peerconn.sendData(new PeerMessage(REPLY, data));
		}
	}
	String type=null;

	@Override
	public String getMsgType() {
		return type;
	}
}