package com.zoubworld.Crypto.server.Peer.peerbase.Router;

import com.zoubworld.Crypto.server.Peer.peerbase.IPeerInfo;
import com.zoubworld.Crypto.server.Peer.peerbase.IRouter;
import com.zoubworld.Crypto.server.Peer.peerbase.Node;
/** Basic router strategy
 * 
 * */
public class Router implements IRouter {
		private Node peer;
		
		public Router(Node peer) {
			this.peer = peer;
		}
		
		public IPeerInfo route(String peerid) {
			if (peer.getPeerKeys().contains(peerid)) return peer.getPeer(peerid);
			else return null;
		}
	}
