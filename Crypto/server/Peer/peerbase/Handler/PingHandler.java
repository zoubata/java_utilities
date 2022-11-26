package com.zoubworld.Crypto.server.Peer.peerbase.Handler;

import java.util.List;

import com.zoubworld.Crypto.server.Peer.peerbase.IHandler;
import com.zoubworld.Crypto.server.Peer.peerbase.IPeerInfo;
import com.zoubworld.Crypto.server.Peer.peerbase.IPeerMessage;
import com.zoubworld.Crypto.server.Peer.peerbase.LoggerUtil;
import com.zoubworld.Crypto.server.Peer.peerbase.Node;
import com.zoubworld.Crypto.server.Peer.peerbase.PeerConnection;
import com.zoubworld.Crypto.server.Peer.peerbase.PeerMessage;

public class PingHandler extends Handler implements IHandler {
	private Node peer;
	
	public PingHandler(Node peer) {
		super(peer);
		type=PING;
		 }
			
	public void handleMessage(PeerConnection peerconn, IPeerMessage msg) {
		// check for correct number of arguments
		String data = msg.getMsgData();
	 {
			peerconn.sendData(new PeerMessage(REPLY, data));
		}
	}
	@Override
	public String getMsgType() {
		// TODO Auto-generated method stub
		return type;
	}
	static public boolean Request(Node n,IPeerInfo pd )
	{
	List<IPeerMessage> resplist = n.connectAndSend(pd, PING, "", true);
	if (resplist == null || resplist.size() == 0)
		return false;
	return true;
	}
}