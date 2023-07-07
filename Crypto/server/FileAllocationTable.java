/**
 * 
 */
package com.zoubworld.Crypto.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zoubworld.Crypto.server.Peer.peerbase.PeerInfo;
import com.zoubworld.Crypto.server.Peer.peerbase.PeerMessage;
import com.zoubworld.Crypto.server.Utils.IStoreObject;
import com.zoubworld.Crypto.server.Utils.StoreList;



/**
 * @author zoubata
 *
 */
public class FileAllocationTable  extends StoreList<AFile>{
//	List<AFile> table=new ArrayList<AFile>();
	Date lastRefresh=null;//last write on disk or date of last fetch
	
	
		/**
	 * @param args
	 */
	public static void main(String[] args) {
		
			
	}
	public String getFileName() {
		AFile t=getSprout();
String name = t.getClass().getSimpleName();
return getPathName() + "." + name + File.separator + name + ".list";
}

	String path;
@Override
	public String getPathName() {
		// TODO Auto-generated method stub
		return path;
	}
		@Override
		protected AFile getSprout() {
			// TODO Auto-generated method stub
			return new AFile(null);
		}

}
