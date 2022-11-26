package com.zoubworld.Crypto.server.Peer.peerbase.Handler;

import java.util.List;

import com.zoubworld.Crypto.server.Peer.peerbase.IHandler;
import com.zoubworld.Crypto.server.Peer.peerbase.IPeerInfo;
import com.zoubworld.Crypto.server.Peer.peerbase.IPeerMessage;
import com.zoubworld.Crypto.server.Peer.peerbase.Node;
import com.zoubworld.Crypto.server.Peer.peerbase.PeerConnection;
import com.zoubworld.Crypto.server.Peer.peerbase.PeerMessage;
import com.zoubworld.Crypto.server.Peer.peerbase.sample.FileShareNode;

/* msg syntax: QUIT pid */
public class QuitHandler  extends Handler implements IHandler {
	
	public QuitHandler(Node peer) 
	{
		super(peer);
		type=PEERQUIT;
		 }
	
	public void handleMessage(PeerConnection peerconn, IPeerMessage msg) {
		String pid = msg.getMsgData().trim();
		if (peer.getPeer(pid) == null) {
			peerconn.sendData(new PeerMessage(ERROR, "Quit: peer not found: " + pid));
		} 
		else {
			peer.removePeer(pid);
			peerconn.sendData(new PeerMessage(REPLY, "Quit: peer removed: " + pid));
		}
	}
	
	static public boolean Request(Node n,IPeerInfo pid )
	{
		List<IPeerMessage> resplist = n.sendToPeer(pid.getId(), FileShareNode.PEERQUIT, n.getId(), true);
		if (resplist!=null && resplist.size()>0)
			if (resplist.get(0).getMsgType().equals(REPLY))
			{
	n.removePeer(pid.getId());
	return true;}
		return false;
	}
	
}