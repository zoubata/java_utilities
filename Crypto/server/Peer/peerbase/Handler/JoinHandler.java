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

/* msg syntax: JOIN pid host port */
public class JoinHandler  extends Handler implements IHandler {
	
	public JoinHandler(Node peer) 
	{
		super(peer);
		type=INSERTPEER;
		 }
			
	public void handleMessage(PeerConnection peerconn, IPeerMessage msg) {
		if (peer.maxPeersReached()) {
			LoggerUtil.getLogger().fine("maxpeers reached " + 
								peer.getMaxPeers());
			peerconn.sendData(new PeerMessage(ERROR, "Join: " +
								"too many peers"));
			return;
		}
		
		// check for correct number of arguments
		String[] data = msg.getMsgData().split("\\s");
		if (data.length != 3) {
			peerconn.sendData(new PeerMessage(ERROR, "Join: " +
								"incorrect arguments"));
			return;
		}
		
		// parse arguments into PeerInfo structure
		IPeerInfo info = new PeerInfo(data[0], data[1],
									 Integer.parseInt(data[2]));
		
		if (peer.getPeer(info.getId()) != null) 
			peerconn.sendData(new PeerMessage(ERROR, "Join: " +
									"peer already inserted"));
		else if (info.getId().equals(peer.getId())) 
			peerconn.sendData(new PeerMessage(ERROR, "Join: " +
									"attempt to insert self"));
		else {
			peer.addPeer(info);
			peerconn.sendData(new PeerMessage(REPLY, "Join: " +
									"peer added: " + info.getId()));
		}
	}
	String type="";

	public static boolean Request(Node n,IPeerInfo pd )
	{
	
	List<IPeerMessage>  resplist= n.connectAndSend(pd, INSERTPEER,
			String.format("%s %s %d", n.getId(), n.getHost(), n.getPort()), true);
			
	String resp=resplist.get(0).getMsgType();
	if (!resp.equals(REPLY) || n.getPeerKeys().contains(pd.getId()))
		return false;
	n.addPeer(pd);
	
	return true;
	}
	
	@Override
	public String getMsgType() {
		// TODO Auto-generated method stub
		return type;
	}
}
