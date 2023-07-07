/**
 * 
 */
package com.zoubworld.Crypto.server;

import java.io.File;

import com.zoubworld.Crypto.server.account.Account;
import com.zoubworld.Crypto.server.account.INode;
import com.zoubworld.Crypto.server.account.IService;
import com.zoubworld.Crypto.server.account.Iaccount;

/**
 * @author zoubata
 * manage all path of server
 */
public class PathService {

	/**
	 * 
	 */
	public PathService() {
		// TODO Auto-generated constructor stub
	}
	static Iaccount a=null; 
	/**
	 * @return the a
	 */
	public static Iaccount getAccount() {
		return a;
	}


	/**
	 * @param a the a to set
	 */
	public static void setAccount(Iaccount a) {
		PathService.a = a;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


	/** return the home  path of a user account 
	 * */
	public static String getHomeDir(Iaccount a) {
		return getHomeDir()+"."+getHomePath(a);

	}
	public static String getHomePath(Iaccount a) {
		return "/Account"+File.separatorChar+a.getId12s()+File.separatorChar;

	}
	
	/** return the home  path of a node 
	 * */
	public static String getHomeDir(INode a) {
		return getHomeDir()+"Node"+File.separatorChar+a.getId12s()+File.separatorChar;

	}
	/** return the home  path of a Service/server
	 * */
	public static String getHomeDir(IService a) {
		return getHomeDir()+"Node"+File.separatorChar+"Service"+File.separatorChar+a.getId12s()+File.separatorChar;

	}
	/** location of executable for the server/service
	 * */
	public static String getJarDir(IService a) {
		return getHomeDir( a)+"Jar"+File.separatorChar;

	}
	/** return the property file path
	 * the property file contains the field used by the system
	 * */
	public static String getPropertyFile(IService a) {
		return getHomeDir( a)+".property";

	}
	/** return the local path to file from url
	 * the url are organise like this :
	 * 
	 * http://localhost/Node/####/ is the HttpDir of Node ####
	 * http://localhost/Account/####/ is the HttpDir of account ####
	 * http://localhost/Service/####/ is the HttpDir of Service ####
	 * http://localhost/ is the getHomeRoot() /http
	 * 
	 * */
	public static String resolve(String uri) {

		String protocol=getprotocol(uri);
		String path=getpath(uri);
		String serve=getserverof(uri);
		String name="";
		if(path.split("/").length>2)
		name=path.split("/")[2];

		String pathf=path;
		if (File.separatorChar=='\\')
			pathf=path.replaceAll("/", "\\\\");

		if (path.startsWith("/Node/"))
		{
			pathf=pathf.substring((File.separatorChar+"Node"+File.separatorChar+name).length());
			if (pathf.isBlank())
				pathf=File.separatorChar+"index.html";
			String dir= getHomeDir()+""
					+ "Node"+File.separatorChar+name+File.separatorChar+protocol;
			if("http".equalsIgnoreCase(protocol))
			{
				if (pathf.isBlank())
					pathf=File.separatorChar+"index.html";
				if (!(new File(dir+pathf).exists()) && (new File(dir).exists()))
					pathf=File.separatorChar+"404.html";
			}
			return dir+pathf;
		}
		else
			if (path.startsWith("/Account/"))
			{
				pathf=pathf.substring((File.separatorChar+"Account"+File.separatorChar+name).length());
				String dir=getHomeDir()+"Account"+File.separatorChar+name+File.separatorChar+protocol;
				if("http".equalsIgnoreCase(protocol))
				{
					if (pathf.isBlank())
						pathf=File.separatorChar+"index.html";
					if (!(new File(dir+pathf).exists()) && (new File(dir).exists()))
						pathf=File.separatorChar+"404.html";
				}
				return dir+pathf;
			}
			else

				if (path.startsWith("/Service/"))
				{
					pathf=pathf.substring((File.separatorChar+"Service"+File.separatorChar+name).length());
					String dir=getHomeDir()+"Node"+File.separatorChar+"Service"+File.separatorChar+name+File.separatorChar+protocol;
					if("http".equalsIgnoreCase(protocol))
					{
						if (pathf.isBlank())
							pathf=File.separatorChar+"index.html";
						
						if (!(new File(dir+pathf).exists()) && (new File(dir).exists()))
							pathf=File.separatorChar+"404.html";
					}

					return dir+pathf;
				}
				else

				{
					String dir=getHomeDir( )+protocol;
					if("http".equalsIgnoreCase(protocol))
					{
						if (pathf.isBlank())
							pathf=File.separatorChar+"index.html";
						
						if (!(new File(dir+pathf).exists()) && (new File(dir).exists()))
							pathf=File.separatorChar+"404.html";
					}

					return dir+pathf;
					}

	}

	private static String getserverof(String uri) {
		// TODO Auto-generated method stub
		return uri.split("/")[2];
	}

	private static String getpath(String uri) {
		// TODO Auto-generated method stub
		return uri.substring(uri.indexOf("://")+3).replace(getserverof( uri), "");
	}

	private static String getprotocol(String uri) {

		return uri.split("://")[0];
	}

	/** location of executable for the node
	 * */
	public static String getJarDir(INode a) {
		return getHomeDir( a)+"Jar"+File.separatorChar;

	}
	/** return the property file path
	 * the property file contains the field used by the system
	 * */
	public static String getPropertyFile(INode a) {
		return getHomeDir( a)+".property";

	}

	public static String getListNodes(INode a) {
		return getHomeDir( a)+".ListNodes";
	}
	public static String getListAccounts(INode a) {
		return getHomeDir( a)+".ListAccounts";
	}

	public static String getDataDir(Iaccount a) {
		return getHomeDir( a)+"Data"+File.separatorChar;

	}
	/** return the config file path
	 * the config file contains the feild defined by the user
	 * */
	public static String getConfigFile(Iaccount a) {
		return getHomeDir( a)+".config";

	}
	/** return the property file path
	 * the property file contains the feild used by the system
	 * */
	public static String getPropertyFile(Iaccount a) {
		return getHomeDir( a)+".property";

	}
	public static String getFtpDir(Iaccount a) {
		return getHomeDir( a)+"ftp"+File.separatorChar;

	}
	public static String getHttpDir(Iaccount a) {
		return getHomeDir( )+getHttpPath( a);

	}
	public static String getHttpPath(Iaccount a) {
		return getHomePath( a)+"Http"+File.separatorChar;

	}
	public static String getServerHttpDir() {
		return getHomeServerDir("Http");

	}
	public static String getServerHttpPath() {
		return getHomeServerPath("Http" )+"Server"+File.separatorChar+"Http"+File.separatorChar;

	}
	public static String getHomeServerDir(String string) {
		if (string==null)
			return getHomeDir( )+"Server"+File.separatorChar;	
		return getHomeDir( )+"Server"+File.separatorChar+string+File.separatorChar;
	}
	public static String getHomeServerPath(String string) {
		// TODO Auto-generated method stub
		return getHomePath( a)+"Server"+File.separatorChar+string+File.separatorChar;
	}
	
	public static  String getHomeDir() {
		if (rootDir!=null)
			return rootDir;
		String osName = System.getProperty("os.name");
		/*
		 * 
		String currentUserDirectory = System.getProperty("user.dir");
		String userHome = System.getProperty("user.home");
		System.out.println("Operating System : "+osName);
		System.out.println("Current User Directory : "+currentUserDirectory);
		System.out.println("User Home : "+userHome);
		 */
		if(osName.contains("Windows")||osName.contains("windows"))
			return "c:\\temp\\";
		return "/tmp/";
		//		return "/home/"+getId12s()+File.separatorChar;
	}
	static private String rootDir=null;
	public static void setHomeDir(String root) 
	{
		if (rootDir==null)
		rootDir=root;
	}
	
	public static String findalias(String uri) {
		String protocol=getprotocol(uri);
		String path=getpath(uri);
		String serve=getserverof(uri);
		String name=path.split("/")[2];
		//need to search on peers
		return protocol+"://"+"newlocalhost"+path;
	}


	

}
