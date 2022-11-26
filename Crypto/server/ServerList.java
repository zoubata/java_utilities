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

	Map<String,String> properties=null;
	public Map<String,String> getProperties()
	{
		if(properties==null)
			properties=loadProperties();
		if(properties==null)
			properties=new HashMap<String,String>();
		return properties;
	}
	
	public Map<String,String> loadProperties()
	{
		try {
		String s = JavaUtils.read(getHomeDir()+"."+getAccountType()+File.separatorChar+"servers.property");
		
		properties=JavaUtils.parseMapStringString(s,":","\r\n");
		} catch(NullPointerException e)
		{
			properties=new HashMap<String,String>();
		}
		return properties;
	}
	private String getAccountType() {
		// TODO Auto-generated method stub
		return ".";
	}

	private String getHomeDir() {
		// TODO Auto-generated method stub
		return Account.getHomeRoot();
	}

	public void saveProperties(Map<String,String> p)
	{
		JavaUtils.saveAs(getHomeDir()+"."+getAccountType()+File.separatorChar+"servers.property", JavaUtils.Format(getProperties(),":","\r\n"));		
	}
	
	public String getProperties(String key)
	{
		return getProperties().get(key);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
