package com.zoubworld.Crypto.server.Peer.peerbase.Handler;

import java.util.ArrayList;
import java.util.List;

import com.zoubworld.Crypto.server.Peer.peerbase.IHandler;
import com.zoubworld.Crypto.server.Peer.peerbase.IPeerInfo;
import com.zoubworld.Crypto.server.Peer.peerbase.IPeerMessage;
import com.zoubworld.Crypto.server.Peer.peerbase.Node;
import com.zoubworld.Crypto.server.Peer.peerbase.PeerConnection;
import com.zoubworld.Crypto.server.Peer.peerbase.PeerInfo;
import com.zoubworld.Crypto.server.Peer.peerbase.PeerMessage;


/* msg syntax: LIST */
public class ListHandler  extends Handler implements IHandler {

	
	public ListHandler(Node peer) 
	{
		super(peer);
		type=LISTPEER;
		 }
	
	public void handleMessage(PeerConnection peerconn, IPeerMessage msg) {
		if (verbose) outVerbose.println("Answer "+type+" From:" +peerconn.getPeerInfo().toString());
		
		peerconn.sendData(new PeerMessage(REPLY, 
				String.format("%d", peer.getNumberOfPeers())));
		for (String pid : peer.getPeerKeys()) {
			peerconn.sendData(new PeerMessage(REPLY, 
					String.format("%s %s %d", pid, peer.getPeer(pid).getHost(),
							peer.getPeer(pid).getPort())));
			if (verbose) outVerbose.println("\t-"+peer.getPeer(pid).toString());
		}
	}
	
	/** search Peer until reach max number or when a deep is reached
	 * the function can return more Peer,
	 * */
	static public List<IPeerInfo> Request(Node n,IPeerInfo pd, int deep,int max )
	{
		

		List<IPeerInfo> l= Request( n, pd );
		int i=0;
		while(deep>=0)
		{	
			int ls=l.size();
			while (max< l.size() && i<ls)
			{
				List<IPeerInfo> l2 = Request( n, l.get(i) );
				for(IPeerInfo le:l2)
				if (!l.contains(le))//keep only new one
						l.addAll(l2);
				i++;
			}
			if(max<=l.size())
				return l;
			deep--;//next level
		}
			return l;
	}
	
	static public List<IPeerInfo> Request(Node n,IPeerInfo pd )
	{
		if (verbose) outVerbose.println("Request "+LISTPEER+" From:" +pd.toString());
	// do recursive depth first search to add more peers
		List<IPeerMessage>  resplist = n.connectAndSend(pd, LISTPEER, "", true);
		List<IPeerInfo> l=new ArrayList<IPeerInfo> ();
		if (resplist.size() > 1) {
			resplist.remove(0);
			for (IPeerMessage pm : resplist) {
				String[] data = pm.getMsgData().split("\\s");
				String nextpid = data[0];
				String nexthost = data[1];
				int nextport = Integer.parseInt(data[2]);
				PeerInfo pi = new PeerInfo(nextpid,nexthost,nextport);
				if (verbose) outVerbose.println("Listed :" +pi.toString());
			/*	if (!nextpid.equals(n.getId()))
					if (!l.contains(pi))*/
					l.add(pi);
			}
		}
		return l;	
	}
}