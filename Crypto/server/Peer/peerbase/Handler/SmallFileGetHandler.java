package com.zoubworld.Crypto.server.Peer.peerbase.Handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.zoubworld.Crypto.server.Peer.peerbase.IHandler;
import com.zoubworld.Crypto.server.Peer.peerbase.IPeerInfo;
import com.zoubworld.Crypto.server.Peer.peerbase.IPeerMessage;
import com.zoubworld.Crypto.server.Peer.peerbase.LoggerUtil;
import com.zoubworld.Crypto.server.Peer.peerbase.Node;
import com.zoubworld.Crypto.server.Peer.peerbase.PeerConnection;
import com.zoubworld.Crypto.server.Peer.peerbase.PeerInfo;
import com.zoubworld.Crypto.server.Peer.peerbase.PeerMessage;

/* msg syntax: FGET file-name */
public class SmallFileGetHandler extends Handler implements IHandler {
	
	
	public SmallFileGetHandler(Node peer) {
super(peer);
	type=SMALLFILEGET; }
	
	public void handleMessage(PeerConnection peerconn, IPeerMessage msg) {
		String filename = msg.getMsgData().trim();
		if(verbose) outVerbose.println("Handle FGET message : "+filename);
		
		if (!(new File(filename)).exists() )
		{
			peerconn.sendData(new PeerMessage(ERROR, type+": "
					+ "file not found " + filename));
			return;
		}
			
		byte filedata2[];
		try {
			FileInputStream infile = new FileInputStream(filename);
			int len = infile.available();
			filedata2 = new byte[len];
			infile.read(filedata2);
			infile.close();
		}
		catch (IOException e) {
			LoggerUtil.getLogger().info("Fget: error reading file: " + e);
			peerconn.sendData(new PeerMessage(ERROR, type+": "
					+ "error reading file " + filename));
			return;
		}
		
		peerconn.sendData(new PeerMessage(REPLY, filedata2));
	}

	public static File Request(Node n,IPeerInfo pd,String fileName )
	{
	
	List<IPeerMessage>  resplist= n.connectAndSend(pd, SMALLFILEGET,
			fileName, true);
			
	String resp=resplist.get(0).getMsgType();
	if (!resp.equals(REPLY) || n.getPeerKeys().contains(pd.getId()))
		return null;
	
	n.addPeer(pd);
	File f=new File(fileName);
	FileOutputStream outfile;
	try {
		outfile = new FileOutputStream(f);
		outfile.write(resplist.get(0).getMsgDataBytes());
	outfile.close();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return f;
	}
}
