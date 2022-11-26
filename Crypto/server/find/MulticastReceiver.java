package com.zoubworld.Crypto.server.find;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

import com.zoubworld.electronic.logic.gates.One;


public class MulticastReceiver extends Thread {
    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[256];
    List<InetAddress> AddressFoundAlive=(List<InetAddress>) /*Collections.synchronizedCollection*/(new ArrayList<InetAddress>());
	static public boolean verbose=true;
    
    public static String Msg="Ping: corporate,application,port";
    public MulticastReceiver(String CorporateId,String applicationId,int port)
    {
    	Msg="Ping : "+CorporateId+", "+applicationId+", "+port;
    }
 
    public void run() {
        try {
        	if (verbose)
        	System.out.println("started ");
            
			socket = new MulticastSocket(MulticastPublisher.MulticastPort);
		
	    socket.setSoTimeout(15000);//150s
        InetAddress group = InetAddress.getByName(MulticastPublisher.MultiCastAddress);
        socket.joinGroup(group);
        while (true) {
        	try {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            InetAddress address = packet.getAddress();
            int port = packet.getPort();

            String received = new String(
              packet.getData(), 0, packet.getLength());
            if (verbose)
            System.out.println("received from "+address.getHostAddress()+":"+port+"  "+received);
            if (Msg.equals(received))
            {
            	
            	AddressFoundAlive.add(address);
                if (AddressFoundAlive.indexOf(address)!=AddressFoundAlive.lastIndexOf(address))
                { // flush all addresse before the old One. they should have more than 1 hour
                	
                	AddressFoundAlive=AddressFoundAlive.subList(AddressFoundAlive.indexOf(address)+1, AddressFoundAlive.size());
                	//AddressFoundAlive.add(address);
                	}
                if (verbose)
                System.out.println("\tadd "+address.getHostAddress()+":"+port+"  "+AddressFoundAlive);
            }
            else
            {
            	if (verbose)
            		System.out.println("\tignore "+address.getHostAddress()+":"+port+"  ");
            }
            	
            if ("end".equals(received)) {
                break;
            }
        	}
        	catch(java.net.SocketTimeoutException e)
        	{
        		//it is possible
        		System.out.print(".");
        	}
        }
        socket.leaveGroup(group);
        socket.close();
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public Collection<InetAddress> getAddressFoundAlive() {
		return AddressFoundAlive;
	}

	/**
  	 * @param args
  	 * @throws IOException 
  	 */
  	public static void main(String[] args) throws IOException {
  		MulticastReceiver e=new MulticastReceiver("www.perso.fr","myappWeb",8080);
  		e.run();
  		
  	}
}