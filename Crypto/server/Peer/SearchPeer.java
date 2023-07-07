package com.zoubworld.Crypto.server.Peer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimerTask;

import com.zoubworld.Crypto.server.Peer.peerbase.IPeerInfo;
import com.zoubworld.Crypto.server.Peer.peerbase.PeerInfo;
import com.zoubworld.Crypto.server.Peer.peerbase.PeerList;

public class SearchPeer extends TimerTask {
	PeerList pl=null;
	ServerPeer snpeer=null;
	public SearchPeer(ServerPeer snpeer2, PeerList pl2) {
		pl=pl2;
		snpeer=snpeer2;
	}

	@Override
    public void run() {
        System.out.println("SearchPeer at : " 
          + LocalDateTime.ofInstant(Instant.ofEpochMilli(scheduledExecutionTime()), 
          ZoneId.systemDefault()));
        
        for(IPeerInfo p:snpeer.connectToNetwork())
			if(PeerInfo.class.isInstance(p))
				if(!pl.getPeers().contains((PeerInfo)p))
		pl.getPeers().add((PeerInfo)p);
		
		if (pl.getPeers().size()==0)
			pl.getPeers().add((PeerInfo)snpeer.getMyInfo());
		
		pl.setId(snpeer.getId());pl.save();
		
		for(IPeerInfo i:pl.getList())
		snpeer.addPeer(i);
    }
}
