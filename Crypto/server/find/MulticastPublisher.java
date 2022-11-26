package com.zoubworld.Crypto.server.find;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Calendar;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;


public class MulticastPublisher extends TimerTask {
public static String MultiCastAddress="229.233.239.241";//RESERVED : https://www.rfc-editor.org/rfc/rfc5771.html#section-13
public static int MulticastPort=11587;

//1 once per hours, do not saturate the port, if 20 nodes are alive, the refresh is each 3 minutes
public static int frequency = 1000*60*60;

private DatagramSocket socket;
    private InetAddress group;
    private byte[] buf;
    static public boolean verbose=true;
    
    public static String Msg="Ping: corporate,application,port";
    public MulticastPublisher(String CorporateId,String applicationId,int port)
    {
    	Msg="Ping : "+CorporateId+", "+applicationId+", "+port;
    }
 
   
    
    
    public void multicast(
      String multicastMessage) throws IOException {
        socket = new DatagramSocket();
/* https://www.iana.org/assignments/multicast-addresses/multicast-addresses.xhtml#multicast-addresses-2
 *  Local Network Control Block (224.0.0.0 - 224.0.0.255 (224.0.0/24))
Internetwork Control Block (224.0.1.0 - 224.0.1.255 (224.0.1/24))
AD-HOC Block I (224.0.2.0 - 224.0.255.255)
RESERVED (224.1.0.0-224.1.255.255 (224.1/16))
SDP/SAP Block (224.2.0.0-224.2.255.255 (224.2/16))
AD-HOC Block II (224.3.0.0-224.4.255.255 (224.3/16, 224.4/16))
RESERVED (224.5.0.0-224.251.255.255 (251 /16s))
DIS Transient Groups 224.252.0.0-224.255.255.255 (224.252/14))
RESERVED (225.0.0.0-231.255.255.255 (7 /8s))
Source-Specific Multicast Block (232.0.0.0-232.255.255.255 (232/8))
GLOP Block
AD-HOC Block III (233.252.0.0-233.255.255.255 (233.252/14)) 
*/
        group = InetAddress.getByName(MultiCastAddress);
        buf = multicastMessage.getBytes();

        DatagramPacket packet 
          = new DatagramPacket(buf, buf.length, group, MulticastPort);
        socket.send(packet);
        if (verbose)
        System.out.println("send "+Msg);
        socket.close();
    }
    
    /**
	 * @param args
	 * @throws IOException 
	 */
	public static void main2(String[] args) throws IOException {
		MulticastPublisher e=new MulticastPublisher("www.perso.fr","myappWeb",8080);
		e.multicast(Msg);
	}

	/** start the monitoring service 
	 * get object with the list of netAddress alive 
	 * */
	public static Collection<InetAddress> startService(String site, String app)
	{
		Timer timer = new Timer();
        Calendar date = Calendar.getInstance();
        //Setting the date from when you want to activate scheduling
        System.out.println(date.getTime().toString());
        //execute every 3 seconds
        MulticastReceiver e=new MulticastReceiver(site,app,8080);
  		e.start();
  		
  		timer.schedule(new MulticastPublisher(site,app,8080), date.getTime(), MulticastPublisher.frequency);
  		return e.getAddressFoundAlive();
	}
	
	public static void main(String[] args) {
        Timer timer = new Timer();
        Calendar date = Calendar.getInstance();
        //Setting the date from when you want to activate scheduling
        System.out.println(date.getTime().toString());
        //execute every 3 seconds
        MulticastReceiver e=new MulticastReceiver("www.perso.fr","myappWeb",8080);
  		e.start();
  		
  		timer.schedule(new MulticastPublisher("www.perso.fr","myappWeb",8080), date.getTime(), MulticastPublisher.frequency);
        
  		while(true)
  		{
  			try {
				Thread.sleep(4000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
  			System.out.println(e.getAddressFoundAlive());}
  		
	}
	
	public void run() {
		
		try {
			multicast(Msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
/*
 * 5. Broadcast and IPv6

IPv4 supports three types of addressing: unicast, broadcast, and multicast. Broadcast, in theory, is a one-to-all communication, i.e. a packet sent from a device has the potential of reaching the entire internet.

As this is undesired for obvious reasons, the scope of the IPv4 broadcast was significantly reduced. Multicast, which also serves as a better alternative to broadcast, came in much later and hence lagged in adoption.

In IPv6, multicast support has been made mandatory, and there is no explicit concept of broadcasting. Multicast has been extended and improved so that all broadcasting features can now be implemented with some form of multicasting.

In IPv6, the left-most bits of an address are used to determine its type. For a multicast address, the first 8 bits are all ones, i.e. FF00::/8. Further, bit 113-116 represent the scope of the address, which can be either one of the following 4: Global, Site-local, Link-local, Node-local.
freestar
ADVERTISING

In addition to unicast and multicast, IPv6 also supports anycast, in which a packet can be sent to any member of the group, but need not be sent to all members.

 */