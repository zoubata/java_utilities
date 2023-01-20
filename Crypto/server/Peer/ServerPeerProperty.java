package com.zoubworld.Crypto.server.Peer;


import com.zoubworld.Crypto.server.PathService;
import com.zoubworld.Crypto.server.Utils.StorePoperty;
import com.zoubworld.Crypto.server.account.Account;

public class ServerPeerProperty extends StorePoperty 
{
	
	
	@Override
	public  void init() {
		ServerPeerProperty sp=this;
		//sp.load();
		if (sp.get("PORT")==null)
			sp.put("PORT", ""+ServerPeer.PORT);
		if (sp.get("Host")==null)
			sp.put("Host", "localhost");
		
		
		
		//	getMyInfo().setId(a.getId12s());
		
		if (sp.get("ID")==null)
		{
			Account a=new Account();
			sp.put("ID", a.getId12s());
			}
	}

	
	String path;
	
	@Override
	public String getPathName() {
		if (path==null)
			path=  PathService.getHomeServerDir("Peer");
		return path;
	}
	
public void setPathName(String p) {		
		 path=p;
	}

	public int getPORT() {
		// TODO Auto-generated method stub
		return Integer.parseInt(get("PORT"));
	}

	public String getId12s() {
		// TODO Auto-generated method stub
		return get("ID");
	}

	public String getHost() {
		// TODO Auto-generated method stub
		return get("Host");
	}

	String id=null;
	@Override
	public String getId() {
		if( id==null)
			return "ServerPeerProperty";
	return id;
}
	public void setId(String id) {
		this.id=id;
		
	}
	}