/**
 * 
 */
package com.zoubworld.Crypto.server.Peer.peerbase;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Zoubata
 *
 */
public class PeerListTest {


String tmp="c:\\temp\\l.list";
	/**
	 * Test method for {@link com.zoubworld.Crypto.server.Peer.peerbase.PeerList#save()}.
	 * Test method for {@link com.zoubworld.Crypto.server.Peer.peerbase.PeerList#load()}.
	 */
	@Test
	public void testSave() {
		PeerList pl=new PeerList();
		pl.Parser("azertyuioplk , 123.245.167.189 , 9087\r\n"
				+ "QsdfghjlmPlj,localhost,8080\n"
		    + "    NBVCXW123456 , www.google.com   , 65535");
		pl.save(tmp);
		PeerList pl2=new PeerList();
		pl2.load(tmp);
		assertEquals(pl.getList(),pl2.getList());
		assertEquals(pl.toLine(),pl2.toLine());
		assertEquals(pl,pl2);
		
	}
	@Test
	public void testgetFileName() {
		PeerList pl=new PeerList();
		assertEquals(pl.getFileName(),"c:\\temp\\.PeerInfo\\PeerInfo.list");
	}
	/**
	 * Test method for {@link com.zoubworld.Crypto.server.Peer.peerbase.PeerList#Parser(java.io.File)}.
	 */
	@Test
	public void testParser() {
	PeerList pl=new PeerList();
	pl.Parser("azertyuioplk , 123.245.167.189 , 9087\r\n"
			+ "QsdfghjlmPlj,localhost,8080\n"
	    + "    NBVCXW123456 , www.google.com   , 65535");
	assertEquals(pl.getList().get(0).getId(),"azertyuioplk");
	assertEquals(pl.getList().get(1).getId(),"QsdfghjlmPlj");
	assertEquals(pl.getList().get(2).getId(),"NBVCXW123456");
	assertEquals(pl.getList().get(0).getHost(),"123.245.167.189");
	assertEquals(pl.getList().get(2).getHost(),"www.google.com");
	assertEquals(pl.getList().get(0).getPort(),9087);
	assertEquals(pl.getList().get(1).getPort(),8080);
	assertEquals(pl.getList().get(2).getPort(),65535);
	
	
	
	}

	/**
	 * Test method for {@link com.zoubworld.Crypto.server.Peer.peerbase.PeerList#toLine(java.util.List)}.
	 */
	@Test
	public void testToLine() 
		{
			PeerList pl=new PeerList();
			pl.Parser("azertyuioplk , 123.245.167.189 , 9087\r\n"
					+ "QsdfghjlmPlj,localhost,8080\n"
				    + "    NBVCXW123456 , www.google.com   , 65535");
			
			assertEquals(pl.toLine(),"azertyuioplk , 123.245.167.189 , 9087\n"
					+ "QsdfghjlmPlj , localhost , 8080\n"
					+ "NBVCXW123456 , www.google.com , 65535\n"
					+ "");
		}
	

}
