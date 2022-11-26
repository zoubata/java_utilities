/**
 * 
 */
package com.zoubworld.Crypto.server.Http;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Map.Entry;

import org.tautua.markdownpapers.Markdown;
import org.tautua.markdownpapers.parser.ParseException;

import com.zoubworld.Crypto.server.GeoIP;
import com.zoubworld.Crypto.server.Server;
import com.zoubworld.Crypto.server.Peer.peerbase.IPeerInfo;
import com.zoubworld.Crypto.server.account.Iaccount;

/**
 * @author zoubata
 * build an html page for account info
 */
public class AccountPage extends APage {

	
	public AccountPage(List<Iaccount> lp ) {
		filename="Account/"+"index.html";
		title="Page of account : ";
		body="the account of server is :"+Server.getthis().getAcc().getId12s()+BR;
		
		child.add(new AccountPage(Server.getthis().getAcc()));
		body="there are "+lp.size()+" account on the host/server"+BR;
		for(Iaccount p: lp)
		{
		addUrl("account "+p.getId12s(), "Account/"+p.getId12s()+"html");
		body+=BR;
		child.add(new AccountPage(p));}
	}
	/**
	 * 
	 */
	public AccountPage(Iaccount p ) {
		title="Page of The account "+p.getId12s();
		filename="Account/"+p.getId12s()+".html";
		body="";
		body+=Header(1,"General information");
		body+="The account type is t  : "+p.getAccountType()+BR;
		body+="The porperty aret: "+BR;
		for ( Entry<String, String> e:p.getProperties().entrySet())
			body+="The porperty : "+e.getKey()+" is "+e.getValue()+BR;
			
		
		
	}

	/**
	 * @param title
	 * @param body
	 * @return 
	 */
	private AccountPage(String title, String body) {
		super(title, body,"account.html");
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args)  {
		Reader in;
		try {
			in = new FileReader("c:\\temp\\in.md");
		
		Writer out = new FileWriter("c:\\temp\\out.html");

		Markdown md = new Markdown();
		md.transform(in, out);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
