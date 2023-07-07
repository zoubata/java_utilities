/**
 * 
 */
package com.zoubworld.Crypto.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.TimeZone;

import com.zoubworld.Crypto.server.Utils.StorePoperty;
import com.zoubworld.Crypto.server.account.Account;
import com.zoubworld.Crypto.server.account.Iwallet;
import com.zoubworld.utils.JavaUtils;

/**
 * @author zoubata
 * property of the current servver
 *
 */
public class ServerProperty extends StorePoperty{

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
	private Map<String, String> m;
	
	/*
	server.list
		->IP,PORT,ID
	ID.list
		->account ID
	Account.fat
		->Files
		*/
/*
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
	}*/
	private String getAccountType() {
		// TODO Auto-generated method stub
		return ".";
	}
/*
	private String getHomeDir() {
		// TODO Auto-generated method stub
		return PathService.getHomeRoot();
	}*/
/*
	public void saveProperties(Map<String,String> p)
	{
		JavaUtils.saveAs(getHomeDir()+"."+getAccountType()+File.separatorChar+"servers.property", JavaUtils.Format(getProperties(),":","\r\n"));		
	}
	
	public String getProperties(String key)
	{
		return getProperties().get(key);
	}
	
		*/
	
	Map<String, String> m2 =null;
	public Map<String, String> getIPinfo()
	{
		if(m2!=null)
			return m2;
		URL url;
		try {
			
			{	url = new URL("https://api.myip.com");
		
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setRequestMethod("GET");

		InputStream responseStream = httpConn.getResponseCode() / 100 == 2
				? httpConn.getInputStream()
				: httpConn.getErrorStream();
		Scanner s = new Scanner(responseStream).useDelimiter("\\A");
		String response = s.hasNext() ? s.next() : "";
		m2 = JavaUtils.parseMapStringString(response, ":", ",");
		}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			//http://ip-api.com/json/82.64.44.123?fields=status,message,continent,continentCode,country,countryCode,region,regionName,city,district,zip,lat,lon,timezone,offset,currency,isp,org,as,asname,reverse,mobile,proxy,hosting,query
			/*	
status	"success"
continent	"Europe"
continentCode	"EU"
country	"France"
countryCode	"FR"
region	"IDF"
regionName	"Île-de-France"
city	"Paris"
district	""
zip	"75017"
lat	48.8323
lon	2.4075
timezone	"Europe/Paris"
offset	3600
currency	"EUR"
isp	"Proxad / Free SAS"
org	"ProXad network / Free SA"
as	"AS12322 Free SAS"
asname	"PROXAD"
reverse	"82-64-44-123.subs.proxad.net"
mobile	false
proxy	false
hosting	false
query	"82.64.44.123"
*/
		if (m2==null)
				m2=new HashMap<String, String>();
		try {
		{//https://ipv6-test.com/api/
			//http://v4.ipv6-test.com/api/myip.php
			url = new URL("http://v6.ipv6-test.com/api/myip.php");
		
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setRequestMethod("GET");

		InputStream responseStream = httpConn.getResponseCode() / 100 == 2
				? httpConn.getInputStream()
				: httpConn.getErrorStream();
		Scanner s = new Scanner(responseStream).useDelimiter("\\A");
		String response = s.hasNext() ? s.next() : "";
		if (response!=null)
		m2.put("netIpV6",""+response);
		
		}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//check ping
		try
		{
		  String ipAddress = "www.google.com";
		    InetAddress inet = InetAddress.getByName(ipAddress);
		     long t1 = System.nanoTime();
		     boolean b = inet.isReachable(5000);
		     long t2 = System.nanoTime();
		     m2.put("netPing",""+(t2-t1));
		
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		//check download speed
		     //http://xcal1.vodafone.co.uk/
		try
		     {
			    	url = new URL("https://www.google.com/");//14kb,
			    	 long speed = testSpeed(url);
			    	//	url = new URL("http://212.183.159.230/50MB.zip");
			    	 if (speed>30000)//377kb, 
			    	 speed = testSpeed(new URL("https://upload.wikimedia.org/wikipedia/commons/5/52/Currency_South_Korea.jpg"));
			    	 if (speed>5000000)
					     speed = testSpeed(new URL("http://212.183.159.230/5MB.zip"));
					  
						
				 if (speed>    2500000000L)
				     speed = testSpeed(new URL("http://212.183.159.230/1GB.zip"));
				 else if (speed>250000000)
				     speed = testSpeed(new URL("http://212.183.159.230/100MB.zip"));
				 else if (speed>50000000)
				     speed = testSpeed(new URL("http://212.183.159.230/20MB.zip"));
				 
				
		     m2.put("netDownload",""+speed);
		     
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		     
		return m2;
		
		
		}
	/**
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private long testSpeed(URL url) throws IOException {
		long t1;
		long t2;
		byte[] buf = new byte[1024*1024*8];//8MB
			 
			int i =0;
			  int bytesRead;
			  t1 = System.nanoTime();
			BufferedInputStream cis = new BufferedInputStream( url.openStream());
			
			while ((bytesRead = cis.read(buf, 0, buf.length)) != -1) {
		       i+=bytesRead;
		    }
		    t2 = System.nanoTime();
		  //    System.out.println(".."+i+"o. "+(t2-t1)+"ns "+i*8*1e9/1024/1024/(t2-t1)+"Mbps/s");
		      buf=null;
		      cis.close();
		      long speed=(long) (i*8*1e9/(t2-t1));
		      
		      System.out.println(i/1024+ "kB\t, speed "+speed+" bps, t="+(t2-t1)*1e-6+" ms");
				
		return speed;
	}
	public String getSpeed()
	{
		if (getIPinfo()!=null)
		return getIPinfo().get("netDownload").replaceAll("\\\"", "");		
		return null;
	}
	public String getPing()
	{
		if (getIPinfo()!=null)
		return getIPinfo().get("netPing").replaceAll("\\\"", "");		
		return null;
	}
	public String getIP()
	{
		if (getIPinfo()!=null)
		return getIPinfo().get("\"ip\"").replaceAll("\\\"", "");		
		return null;
	}
	public String getcountry()
	{
		if (getIPinfo()!=null)
		return getIPinfo().get("\"country\"").replaceAll("\\\"", "");		
		return null;
	}
	public String getCC()
	{
		Map<String, String> m2 = getIPinfo();
		if (m2!=null)
		return m2.get("\"cc\"").replaceAll("\\\"", "");		
		return null;
	}
	public String getHostName() 
	{
		if (getIP()==null)
			return null;
	InetAddress ia;
	try {
		ia = InetAddress.getByName(getIP());
		return ia.getCanonicalHostName();
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
//	System.out.println(ia.getHostAddress()+":"+ia.getCanonicalHostName());
	return null;
	}
	/***
	 * 
	 * 
	 * Property list :
	 * - origin, and restriction , this section is to manage local law, and prevent not autorized material
	 * * State : the state where the server is localized
	 * * StateLawAllowed : the list of state allowed to send data receive data, or manage account
	 * * StateLawForbidden : the list of state Forbidden to send data receive data, or manage account
	 * 
	 * * Accounts : list accounts
	 * * Services : list service provided
	 * 
	 */
	@Override
	public  void init() {
		ServerProperty sp=this;
		//sp.load();
		if (sp.get("State")==null)
			sp.put("State", "fr");
		
		if (sp.get("NetState")==null && getCC()!=null)
			sp.put("NetState", getCC()+":"+getcountry());
		if (sp.get("NetIp")==null && getIP()!=null)
			sp.put("NetIp", getIP());
		if (sp.get("NetName")==null && getHostName()!=null)
			sp.put("NetName", getHostName());
		
		if (sp.get("NetPing")==null && getPing()!=null)
			sp.put("NetPing", getPing());//ns
		
		if (sp.get("NetDownload")==null && getSpeed()!=null)
			sp.put("NetDownload", getSpeed());//bps
		if (sp.get("NetUpload")==null && getIPinfo().get("NetUpload")!=null)
			sp.put("NetUpload", getIPinfo().get("NetUpload"));//bps
		
		if (sp.get("NetIpV6")==null && getIPinfo().get("netIpV6")!=null)
			sp.put("NetIpV6", getIPinfo().get("netIpV6"));
		
		
		
		
		if (sp.get("StateLawAllowed")==null)
			sp.put("StateLawAllowed", "default");
		if (sp.get("StateLawForbidden")==null)
			sp.put("StateLawForbidden", "default,");		
		if (sp.get("Accounts")==null)
			sp.put("Accounts", "");		
		if (sp.get("Services")==null)
			sp.put("Services", "default");		
		
		
		
		if (sp.get("os.name")==null)
			sp.put("os.name", System.getProperty("os.name"));		
		
		if (sp.get("PROCESSOR_IDENTIFIER")==null)
			sp.put("PROCESSOR_IDENTIFIER", System.getenv("PROCESSOR_IDENTIFIER"));		
		
		if (sp.get("PROCESSOR_ARCHITECTURE")==null)
			if (System.getenv("PROCESSOR_ARCHITECTURE")==null)
				sp.put("PROCESSOR_ARCHITECTURE", System.getProperty("os.arch"));		
			
			else
			sp.put("PROCESSOR_ARCHITECTURE", System.getenv("PROCESSOR_ARCHITECTURE"));		
		
		if (sp.get("NUMBER_OF_PROCESSORS")==null)
			sp.put("NUMBER_OF_PROCESSORS", System.getenv("NUMBER_OF_PROCESSORS"));		
		
	//	if (sp.get("RamSizeFree")==null)
			sp.put("RamSizeFree", ""+Runtime.getRuntime().freeMemory());		
			if (sp.get("RamSizeTotal")==null)
				sp.put("RamSizeTotal", ""+Runtime.getRuntime().maxMemory());	
			
			if (sp.get("Account")==null)
				sp.put("Account", ""+account.getId12s());	
			
			if (sp.get("WalletPublicKey")==null)
				sp.put("WalletPublicKey", ""+account.getWallet().getPublicKey());	
			
			if (sp.get("TimeZone")==null)
			sp.put("TimeZone", ""+TimeZone.getDefault().getID());		
		
		if (sp.get("TimeZone")==null)
			sp.put("TimeZone", ""+TimeZone.getDefault().getID());		
		
		if (sp.get("Country")==null)
			sp.put("Country", ""+Locale.getDefault().getISO3Country()+"/"+Locale.getDefault().getDisplayCountry());		
		if (sp.get("Language")==null)
			sp.put("Language", ""+Locale.getDefault().getISO3Language()+"/"+Locale.getDefault().getDisplayLanguage());		
		
		
		File cDrive = new File("C:");
		File root = new File("/");
		cDrive= File.listRoots()[0];
	/*	System.out.println(String.format("Total space: %.2f GB",
		  (double)cDrive.getTotalSpace() /1073741824));
		System.out.println(String.format("Free space: %.2f GB", 
		  (double)cDrive.getFreeSpace() /1073741824));
		System.out.println(String.format("Usable space: %.2f GB", 
		  (double)cDrive.getUsableSpace() /1073741824));
		*/
	//	if (sp.get("DiskSizeFree")==null)
			sp.put("DiskSizeFree", ""+cDrive.getFreeSpace());		
		
		if (sp.get("DiskSizeTotal")==null)
			sp.put("DiskSizeTotal", ""+cDrive.getTotalSpace());		
		
		
		    
	//	sp.save();

	}
	Account account=new Account();
	@Override
	public String getFileName() {
		
		String name = this.getClass().getSimpleName();
		String id=name;
		if (account!=null)
			id=account.getId12s();
		return getPathName() + "." + name + File.separator + id + ".property";
	}
	public static void main(String[] args) {
		ServerProperty sp=new ServerProperty();
		sp.load();
		sp.init();
		sp.save();
/*
        InetAddress ip=null;
    	try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		  
		System.out.println("Current host name : " + ip.getHostName());
              System.out.println("Current IP address : " + ip.getHostAddress());
              String osType= System.getProperty("os.arch");
              System.out.println("Operating system type =>"+ osType);
              String osVersion= System.getProperty("os.version");
              System.out.println("Operating system version =>"+ osVersion);
               
              String nameOS= System.getProperty("os.name");
              System.out.println("Operating system Name=>"+ nameOS);
              System.out.println(System.getenv("PROCESSOR_IDENTIFIER"));
              System.out.println(System.getenv("PROCESSOR_ARCHITECTURE"));
              System.out.println(System.getenv("NUMBER_OF_PROCESSORS"));
           
          System.out.println("Available processors (cores): " + 
              Runtime.getRuntime().availableProcessors());
       
          System.out.println("Free memory (bytes): " + 
              Runtime.getRuntime().freeMemory());
       
          long maxMemory = Runtime.getRuntime().maxMemory();
          System.out.println("Maximum memory (bytes): " + 
              (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory));
       
          System.out.println("Total memory (bytes): " + 
              Runtime.getRuntime().totalMemory());
               
         
              try {
			    
              NetworkInterface network = NetworkInterface.getByInetAddress(ip);
        
              byte[] mac = network.getHardwareAddress();
        
              System.out.print("Current MAC address : ");
        
              StringBuilder sb = new StringBuilder();
              for (int i = 0; i < mac.length; i++) {
                  sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));     
              }
              System.out.println(sb.toString());
              
          	} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  */
          }
	
	/**
	 * 
	 */
	public ServerProperty() {
		// TODO Auto-generated constructor stub
	}

}
