/**
 * 
 */
package com.zoubworld.Crypto.server.Http;


import com.zoubworld.Crypto.server.GeoIP;
import com.zoubworld.Crypto.server.Server;

/**
 * @author zoubata
 * build an html page for server info
 */
public class ServerPage extends APage {

	/**
	 * 
	 */
	public ServerPage() {
		title="Page of this Server";
		filename="server.html";
		body="";
		body+=Header(1,"General information");
		body+="The server is started since : "+dateformat.format((Server.getthis().getStarted()))+BR;
		body+="The local host name is "+Server.getthis().getLocalHost().getHostName()+BR;
		body+="The local host address is "+Server.getthis().getLocalHost().getHostAddress()+BR;
		body+="The Web host address is "+Server.getthis().getWebIp()+BR;
		GeoIP l = Server.getthis().getLocation();
		
		body+="The host is probbly located : "+l.getCountry()
				+','+l.getLeastSpecificSubdivision()
				+","+l.getCity()
				//+","+Server.getthis().getLocation().getPostal()
				+BR;
		
		addUrl("list of peer connected to the server", "peer/"+"index.html");
		
		
		
		
	}

	/**
	 * @param title
	 * @param body
	 */
	public ServerPage(String title, String body) {
		super(title, body,"server.html");
		// TODO Auto-generated constructor stub
	}

}
