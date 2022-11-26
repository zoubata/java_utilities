/**
 * 
 */
package com.zoubworld.Crypto.server.account;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.zoubworld.Crypto.server.PathService;
import com.zoubworld.utils.JavaUtils;

/**
 * @author zoubata
 *
 */
public class Account implements Iaccount {

	/**
	 * 
	 */
	public Account() {
		// TODO Auto-generated constructor stub
	}

	public Account(Iwallet w) {
		wallet=w;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Iwallet w=new Wallet();
		Account a=new Account(w);
		a.getProperties().put("Id12s", a.getId12s());
		a.getProperties().put("Wallet.key.public", a.getWallet().getPublicKey().toString());
		a.saveProperties();
		System.out.println(a.getId12s());
		System.out.println(System.getProperties().get("os.name"));

		
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
		String s = JavaUtils.read(getHomeDir()+"."+getAccountType()+File.separatorChar+"account.property");
		
		properties=JavaUtils.parseMapStringString(s,":","\r\n");
		} catch(NullPointerException e)
		{
			properties=new HashMap<String,String>();
		}
		return properties;
	}
	public void saveProperties(Map<String,String> p)
	{
		JavaUtils.saveAs(getHomeDir()+"."+getAccountType()+File.separatorChar+"account.property", JavaUtils.Format(getProperties(),":","\r\n"));		
	}
	
	public String getProperties(String key)
	{
		return getProperties().get(key);
	}
	
	Iwallet wallet=new Wallet();
	public Iwallet getWallet() {		
		return wallet;
	}

	@Override
	public String getHomeDir() {
		// TODO Auto-generated method stub
		return PathService.getHomeDir(this);
	}

	


}
