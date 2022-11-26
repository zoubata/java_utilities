/**
 * 
 */
package com.zoubworld.Crypto.server;

import com.zoubworld.Crypto.server.account.Iwallet;

/**
 * @author zoubata
 * property of the current servver
 *
 */
public class ServerProperty {

	//ID
	Iwallet w;
	String ENS;//Ethereum Name Service alias
	//url
	String IP_DNS;// IP v4 or v6 or dns name
	Integer PORT;
	//perf
	Long AvailSpace;//df -h of /
	Long TotalSpace;
	Long UsedSpace;//du /home/xxx/ -sh
	
	Long SpeedNetworkUp;//bps
	Long SpeedNetworkDown;//bps
	Integer NbProcessor;//cat /proc/cpuinfo
	Long ProcessorSpeed;//cat /proc/cpuinfo
	Long MemTotal;//cat /proc/meminfo
	Long MemFree;//cat /proc/meminfo
	
	
	//location
	String COUNTRY;
	String REGION;
	String CITY;
	String InternetServiceProvider;// connection
	String HostServiceProvider;// hardware, v
	String Owner;// account/user/admin name
	String Email;// email of admin
	
	
	server.list
		->IP,PORT,ID
	ID.list
		->account ID
	Account.fat
		->Files
		
		
	
	
	
	
	
	
	/**
	 * 
	 */
	public ServerProperty() {
		// TODO Auto-generated constructor stub
	}

}
