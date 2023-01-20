/**
 * 
 */
package com.zoubworld.Crypto.server.Peer.peerbase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.zoubworld.Crypto.server.PathService;
import com.zoubworld.Crypto.server.Server;
import com.zoubworld.Crypto.server.Peer.ServerPeerProperty;
import com.zoubworld.Crypto.server.Utils.StoreList;
import com.zoubworld.utils.JavaUtils;

/**
 * @author zoubata
 *
 */
public class PeerList extends StoreList<PeerInfo>{


//	List<PeerInfo> peers;
	public List<PeerInfo> getPeers() {
		if (peers==null)
			peers=new ArrayList<PeerInfo>();	
		return peers;
	}
	
	String path;
	
	@Override
	public String getPathName() {
		if (path==null)
		path= PathService.getHomeDir();
		return path;
	}
	
public void setPathName(String p) {		
		 path=p;
	}
	
	/**
	 * 
	 *
	public PeerList() {
		load();
	}

	/**
	 * 
	 *
	public void load() {
	//load(PathService.getHomeRoot()+".peer/Peers.list");
	}
		public void load(String filename) {
			File f=new File(filename);
			String file=JavaUtils.read(f);
			
				peers=Parser(file);
	}
		public void save() {
			save(PathService.getHomeRoot()+".peer/Peers.list");
			
		}
			public void save(String filename) {
					JavaUtils.saveAs(filename, toLine(peers));
	}

	public static List<PeerInfo> Parser(String file)
	{
		String[] lines=file.split("\\n");
		List<PeerInfo> peers=new ArrayList<PeerInfo> ();
		for(String line:lines)
			peers.add(PeerInfo.Parser(line));
		 return peers;
	}
	public static String toLine(List<PeerInfo> lp)
	{
		String s="";
		for(PeerInfo a:lp)
			s+=PeerInfo.toLine(a)+"\n";
		return s;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
/*
	@Override
	public int hashCode() {
		return Objects.hash(peers);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PeerList other = (PeerList) obj;
		return Objects.equals(peers, other.peers);
	}
*/

	String id=super.getId();
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {		
	return id;
}
	@Override
	protected PeerInfo getSprout() {
		return new PeerInfo(0);
	}
}
