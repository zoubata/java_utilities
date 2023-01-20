/**
 * 
 */
package com.zoubworld.Crypto.server.Peer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.zoubworld.Crypto.server.PathService;
import com.zoubworld.Crypto.server.Peer.peerbase.*;
import com.zoubworld.Crypto.server.Peer.peerbase.Handler.ListHandler;
import com.zoubworld.Crypto.server.Peer.peerbase.Handler.PingHandler;
import com.zoubworld.Crypto.server.account.Account;
/**
 * @author Zoubata
 *
 */
public class ServerPeer extends Node {

	static final int PORT = 8077;
	static final int PeriodSearchPeer = 1*3600;//in second
	
	ServerPeerProperty property;
	/**
	 * 
	 */
	public ServerPeer() {
		this(new ServerPeerProperty());
	
	}
	public ServerPeer(ServerPeerProperty property) {
		super(PORT);
		this.property=property;
	
		setMyInfo(new PeerInfo(property.getId12s(),property.getHost(),property.getPORT()));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if(args.length>0)
		PathService.setHomeDir(args [0]) ;
		 
		ServerPeerProperty property=new ServerPeerProperty();
		if(args.length>0)
		property.setPathName(args [0]+"Server\\Peer\\");
		property.load();
	
		
		ServerPeer snpeer=new ServerPeer(property);
		PeerList pl=new PeerList();
		if(args.length>0)
		pl.setPathName(args [0]+"Server\\Peer\\");
		pl.setId(snpeer.getMyInfo().getId());
		pl.load();
	//	pl.getPeers().add(new PeerInfo("toto", "localhost", 8077));
		if (pl.getList()!=null)
			for(IPeerInfo pd:pl.getList())
				if(pd!=null)
					snpeer.addPeer( pd); 
		
		(new Thread() { public void run() { snpeer.mainLoop(); }}).start();
		
		new Timer().schedule(new SearchPeer(snpeer,pl), 1000, PeriodSearchPeer*1000);
		
		snpeer.property.save();
		snpeer.property.setId(snpeer.getId());
		snpeer.property.save();
		
		
	}
	List<IPeerInfo> connectToNetwork() {
		List<IPeerInfo> l=new ArrayList<IPeerInfo>();
		for (IPeerInfo p : getPeers())
		{
			List<IPeerInfo> l1=connect(p);
			if (l1!=null)
			 l.addAll( l1);
		}	
		return l;
	}

	private List<IPeerInfo> connect(IPeerInfo p) {
		boolean r = PingHandler.Request(this, p );
		List<IPeerInfo> l=null;
		if (r)
			l=ListHandler.Request(this, p );
	
		PeerList pl=new PeerList();	
		pl.setPathName(property.getPathName());
		pl.setId(p.getId());
		
		if (r)
		for(IPeerInfo i:l)
		pl.getPeers().add((PeerInfo)i);
		
		pl.save();
		
		return l;
	}

	

}
