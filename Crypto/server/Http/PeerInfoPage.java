/**
 * 
 */
package com.zoubworld.Crypto.server.Http;


import java.util.List;

import com.zoubworld.Crypto.server.GeoIP;
import com.zoubworld.Crypto.server.Server;
import com.zoubworld.Crypto.server.Peer.peerbase.IPeerInfo;

/**
 * @author zoubata
 * build an html page for peer info
 */
public class PeerInfoPage extends APage {

	
	public PeerInfoPage(List<IPeerInfo> lp ) {
		filename="peer/"+"index.html";
		title="Page of Peers : ";
		
		body="there are "+lp.size()+" peers connected to the host/server";
		for(IPeerInfo p: lp)
		{
		addUrl("peer "+p.getId(), "peer/"+p.getId()+"html");
		
		child.add(new PeerInfoPage(p));}
	}
	/**
	 * 
	 */
	public PeerInfoPage(IPeerInfo p ) {
		title="Page of The Peer "+p.getId();
		filename="peer/"+p.getId()+".html";
		body="";
		body+=Header(1,"General information");
		body+="The Peer is on port  : "+p.getPort()+BR;
		body+="The Peer is on ip/host: "+p.getHost()+BR;
		GeoIP l = Server.getthis().getLocation();
		
		body+="The Peer is probbly located : "+l.getCountry()
				+','+l.getLeastSpecificSubdivision()
				+","+l.getCity()
				//+","+Server.getthis().getLocation().getPostal()
				+BR;	
		
	}

	/**
	 * @param title
	 * @param body
	 * @return 
	 */
	private PeerInfoPage(String title, String body) {
		super(title, body,"server.html");
		// TODO Auto-generated constructor stub
	}

}
