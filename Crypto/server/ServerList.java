/**
 * 
 */
package com.zoubworld.Crypto.server;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.zoubworld.Crypto.server.account.Account;
import com.zoubworld.utils.JavaUtils;

/**
 * @author zoubata
 * manage the list of servers
 */
public class ServerList {

	/**
	 * 
	 */
	public ServerList() {
		// TODO Auto-generated constructor stub
	}
	private String getHomeDir() {
		// TODO Auto-generated method stub
		return Account.getHomeDir();
	}
	public String getFilename()
	{
		return getHomeDir()+".server/server.list";		
	}
	public void load()
	{
		
	}
	public void save()
	{
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
