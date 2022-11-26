/**
 * 
 */
package com.zoubworld.Crypto.server.Http;

import java.io.File;

import com.zoubworld.utils.JavaUtils;

/**
 * @author zoubata
 *
 * here is the generation of pages http for the root site pointing to services and account and nodes
 */
public class RootPages {

	/**
	 * 
	 */
	public RootPages() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		APage p404=new APage("Not Found","Page not Found", JavaHTTPServer.DEFAULT_FILE);
		APage phome=new APage("Home","Welcome!",JavaHTTPServer.FILE_NOT_FOUND);
		APage punsupport=new APage("Sorry","sorry this basic server didn't support that",JavaHTTPServer.METHOD_NOT_SUPPORTED);
		p404.addUrl("go to home", "./index.html");
		punsupport.addUrl("go to home", "./index.html");
		phome.addUrl("to be build : Visit W3Schools.com!", "https://www.w3schools.com/");
		p404.addImg("not find freepik.com material", "https://img.freepik.com/free-vector/search-engine-marketing-business-copywriting-service-content-management_335657-3148.jpg?w=1380&t=st=1667665406~exp=1667666006~hmac=6947fba031cd18b128389c6a780e171d96d0077a7f5c5de637fe8324584156fc");
		punsupport.addImg("not find freepik.com material", "https://img.freepik.com/free-vector/page-found-with-people-connecting-plug-concept-illustration_114360-1927.jpg?w=1380&t=st=1667665328~exp=1667665928~hmac=c20f06fbe7efe89c5313f304e0900d59fa01650ba7a05adbb37a0cbbe031e620");
		phome.addImg("home pic, freepik.com material","https://img.freepik.com/premium-vector/set-web-browser-window_349999-104.jpg?w=1800");
		JavaUtils.saveAs(JavaHTTPServer.WEB_ROOT.getAbsolutePath()+File.separatorChar+JavaHTTPServer.DEFAULT_FILE, phome.toString());
		JavaUtils.saveAs(JavaHTTPServer.WEB_ROOT.getAbsolutePath()+File.separatorChar+JavaHTTPServer.FILE_NOT_FOUND, p404.toString());
		JavaUtils.saveAs(JavaHTTPServer.WEB_ROOT.getAbsolutePath()+File.separatorChar+JavaHTTPServer.METHOD_NOT_SUPPORTED, punsupport.toString());
		

	}

}
