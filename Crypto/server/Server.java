/**
 * 
 */
package com.zoubworld.Crypto.server;

import java.time.LocalDateTime;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.zoubworld.Crypto.server.account.Account;
import com.zoubworld.Crypto.server.account.Iaccount;
import com.zoubworld.Crypto.server.find.MulticastPublisher;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.Enumeration;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

import com.maxmind.db.CHMCache;
import com.maxmind.db.Reader.FileMode;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.AsnResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.model.EnterpriseResponse;
import com.maxmind.geoip2.model.IspResponse;
import com.maxmind.geoip2.record.Location;

//import static com.maxmind.geoip2.json.File.readJsonFile;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
/**
 * @author zoubata
 * https://dzone.com/articles/remote-debugging-java-applications-with-jdwp
 */
public class Server {

	/** singleton pattern
	 * 
	 */
	static public Server factory() {
		if (instance==null)
		return instance=new Server();
		return instance;
	}
	private Server() {
		 instance=this;
		 started = LocalDateTime.now();
		 // publish its id
		 alive = MulticastPublisher.startService("Node Crypto", this.getClass().getCanonicalName());
		 
	}
	Collection<InetAddress> alive;
	/** return the list of IP of over node server
	 * */
	public Collection<InetAddress> getAliveIp() {
		return alive;
	}
	LocalDateTime started;
	public LocalDateTime getStarted() {
		return started;
	}
	Iaccount acc=new Account();
	
	public Iaccount getAcc() {
		return acc;
	}
	public void setAcc(Iaccount acc) {
		this.acc = acc;
	}
	static Server instance=null;

	public static Server getthis()
	{
		if(instance==null)
			instance=new Server();
		return instance;
	}
	public InetAddress getLocalHost()
	{
	try {
		return java.net.InetAddress.getLocalHost();
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
	}
	static public String getWebIp()
	{
URL whatismyip;
String ip=null;
try {
	whatismyip = new URL("http://checkip.amazonaws.com");

BufferedReader in = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));

 ip = in.readLine(); //you get the IP as a String
//System.out.println(ip);
} catch (MalformedURLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
return ip;
}
	public GeoIP getLocation()
	{
		try {
			GeoIP l = getLocationOfIp(getWebIp());
	/*	System.out.println(	"the host is probbly located : "+l.getCountry()
			+','+l.getLeastSpecificSubdivision()
			+","+l.getCity());*/
			//+","+Server.getthis().getLocation().getPostal()
			
			
			return l;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GeoIp2Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/*
	public class GeoIP {
	    public GeoIP(String ip, String cityName, String latitude2, String longitude2) {
			this.ipAddress=ip;
			this.city=cityName;
			this.latitude=latitude2;
			this.longitude=longitude2;
		}
		private String ipAddress;
	    private String city;
	    private String latitude;
	    private String longitude;
	    // constructors, getters and setters... 
	}*/
	public static void givenIP_whenFetchingCity_thenReturnsCityData(    String ip ,
		    String dbLocation ) 
			
			
			  throws IOException, GeoIp2Exception {
			//    String ip = "your-ip-address";
			    //String dbLocation = "your-path-to-mmdb";
String userHomeDir = System.getProperty("user.home");
		
File database = new File(userHomeDir+"\\Downloads\\GeoLite2-City_20221104\\GeoLite2-City.mmdb");
//DatabaseReader dbReader = new DatabaseReader.Builder(database).fileMode(FileMode.MEMORY_MAPPED).withCache(new CHMCache()).build();


		 
		//	    File database = new File(dbLocation);
			    DatabaseReader dbReader = new DatabaseReader.Builder(database)
			      .build();
			        
			    InetAddress ipAddress = InetAddress.getByName(ip);
			    CityResponse response = dbReader.city(ipAddress);
			        
			    String countryName = response.getCountry().getName();
			    String cityName = response.getCity().getName();
			    String postal = response.getPostal().getCode();
			    String state = response.getLeastSpecificSubdivision().getName();
			    System.out.print(countryName+','+cityName+","+postal+","+state);
			}

	public static  GeoIP getLocationOfIp(    String ip 		    ) 
			throws IOException, GeoIp2Exception 
				 {


		
		 System.out.println("ip :"+ip);
    File database = new File(PathService.getHomeDir()+"\\.ServerHttp\\GeoLite2-City_20221104\\GeoLite2-City.mmdb");
  //DatabaseReader dbReader = new DatabaseReader.Builder(database).fileMode(FileMode.MEMORY_MAPPED).withCache(new CHMCache()).build();



    DatabaseReader dbReader = new DatabaseReader.Builder(database)
      .build();
        
    InetAddress ipAddress = InetAddress.getByName(ip);
    CityResponse response = dbReader.city(ipAddress);
    String countryName = response.getCountry().getName();
    String cityName = response.getCity().getName();
    String postal = response.getPostal().getCode();
    String state = response.getLeastSpecificSubdivision().getName();
			  	        	
			    return new GeoIP(ip,cityName,countryName,postal,state);
			}
	/**
	 * @param args
	 * @throws SocketException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws SocketException, UnknownHostException {

		  try {
		      InetAddress address = InetAddress.getByName("www.oreilly.com");
		      System.out.println(address);
		    } catch (UnknownHostException ex) {
		      System.out.println("Could not find www.oreilly.com");
		    }
		
		  try {
		      InetAddress me = InetAddress.getLocalHost();
		      String dottedQuad = me.getHostAddress();
		      System.out.println("My address is " + dottedQuad);
		    } catch (UnknownHostException ex) {
		      System.out.println("I'm sorry. I don't know my own address.");
		    }
		  
	
		String aa = java.net.InetAddress.getLoopbackAddress().getCanonicalHostName();
		System.out.println("getHostAddress="+aa);
	//.....
		/*
	    Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
	    while (e.hasMoreElements()){
	    	 NetworkInterface  ani=e.nextElement();
	    	 System.out.println("Interface : "+ani.getDisplayName());
	      Enumeration<InetAddress> i = ani.getInetAddresses();
	      while (i.hasMoreElements()){
	        InetAddress a = i.nextElement();
	        System.out.println(a.getHostName()+" -> "+a.getHostAddress()+
	           "\n\t isloopback? "+a.isLoopbackAddress()+
	           "\n\t isSiteLocalAddress? "+a.isSiteLocalAddress()+
	           "\n\t isIPV6? "+(a instanceof Inet6Address)
	         );
	      }
	      System.out.println("---------------");
	    }*/
	    try {
			givenIP_whenFetchingCity_thenReturnsCityData("82.64.44.123","");
			GeoIP response = getLocationOfIp("82.64.44.123");
		    String countryName = response.getCountry();
		    String cityName = response.getCity();
		    String postal = response.getPostal();
		    String state = response.getLeastSpecificSubdivision();
		    System.out.print(countryName+','+cityName+","+postal+","+state);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (GeoIp2Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    }
}
