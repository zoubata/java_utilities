package com.zoubworld.Crypto.server.Peer.peerbase.Handler;

import java.util.List;

import com.zoubworld.Crypto.server.Peer.peerbase.IHandler;
import com.zoubworld.Crypto.server.Peer.peerbase.IPeerInfo;
import com.zoubworld.Crypto.server.Peer.peerbase.IPeerMessage;
import com.zoubworld.Crypto.server.Peer.peerbase.LoggerUtil;
import com.zoubworld.Crypto.server.Peer.peerbase.Node;
import com.zoubworld.Crypto.server.Peer.peerbase.PeerConnection;
import com.zoubworld.Crypto.server.Peer.peerbase.PeerInfo;
import com.zoubworld.Crypto.server.Peer.peerbase.PeerMessage;


/* msg syntax: NAME */
public class NameHandler extends Handler implements IHandler {
	
	public NameHandler(Node peer) {
		super(peer);
		type=PEERINFO;
		 }
	
	public void handleMessage(PeerConnection peerconn, IPeerMessage msg) {
		
		peerconn.sendData(new PeerMessage(REPLY, peer.getId()));
	}

	static public boolean Request(Node n,IPeerInfo pd )
	{
	List<IPeerMessage> resplist = n.connectAndSend(pd, PEERNAME, "", true);
	if (resplist == null || resplist.size() == 0)
		return false;
	String peerid = resplist.get(0).getMsgData();
	LoggerUtil.getLogger().fine("contacted " + peerid);
	pd.setId(peerid);
	return true;
	}
}
