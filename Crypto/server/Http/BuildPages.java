/**
 * 
 */
package com.zoubworld.Crypto.server.Http;

import java.io.File;

import com.zoubworld.Crypto.server.PathService;
import com.zoubworld.Crypto.server.account.Account;
import com.zoubworld.Crypto.server.account.Iwallet;
import com.zoubworld.Crypto.server.account.Wallet;
import com.zoubworld.utils.JavaUtils;

/**
 * @author zoubata
 *
 */
public class BuildPages {

	/**
	 * 
	 */
	public BuildPages() {
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Iwallet w1=new Wallet();
		Account a1=new Account(w1);
		PathService.setAccount(a1);
		APage p404=new APage("Not Found","Page not Found",PathService.getServerHttpPath(PathService.getAccount())+JavaHTTPServer.FILE_NOT_FOUND);
		APage phome=new APage("Home","Welcome!",PathService.getServerHttpPath(PathService.getAccount())+JavaHTTPServer.DEFAULT_FILE);
		APage punsupport=new APage("Sorry","sorry this basic server didn't support that",PathService.getServerHttpPath(PathService.getAccount())+JavaHTTPServer.METHOD_NOT_SUPPORTED);
	/*	p404.addUrl("go to home", "./index.html");
		punsupport.addUrl("go to home", "./index.html");
		phome.addUrl("to be build : Visit W3Schools.com!", "https://www.w3schools.com/");
		p404.addImg("not find freepik.com material", "https://img.freepik.com/free-vector/search-engine-marketing-business-copywriting-service-content-management_335657-3148.jpg?w=1380&t=st=1667665406~exp=1667666006~hmac=6947fba031cd18b128389c6a780e171d96d0077a7f5c5de637fe8324584156fc");
		punsupport.addImg("not find freepik.com material", "https://img.freepik.com/free-vector/page-found-with-people-connecting-plug-concept-illustration_114360-1927.jpg?w=1380&t=st=1667665328~exp=1667665928~hmac=c20f06fbe7efe89c5313f304e0900d59fa01650ba7a05adbb37a0cbbe031e620");
		phome.addImg("home pic, freepik.com material","https://img.freepik.com/premium-vector/set-web-browser-window_349999-104.jpg?w=1800");
		*/
		/*
		JavaUtils.saveAs(PathService.getServerHttpDir(PathService.getAccount())+JavaHTTPServer.FILE_NOT_FOUND, p404.toString());
		JavaUtils.saveAs(PathService.getServerHttpDir(PathService.getAccount())+JavaHTTPServer.METHOD_NOT_SUPPORTED, punsupport.toString());
		*/
		APage pserver=new ServerPage();
		//JavaUtils.saveAs(PathService.getServerHttpDir(PathService.getAccount())+pserver.getFilename(),pserver.toString());
		phome.addbody("Welcome to the web3 decetralized");
		phome.child.add(p404);
		phome.child.add(punsupport);
		phome.add(pserver);
		APage ptest=new APage("test page","page use for example and test",PathService.getServerHttpPath(PathService.getAccount())+"test.html");
		
		ptest.add(p404);
		ptest.add(punsupport);
		ptest.addEQUATION("E = {mc^2} ");
		
		ptest.addEQUATION("\\begin{pmatrix}\r\n"
				+ "   a & b \\\\\r\n"
				+ "   c & d\r\n"
				+ "\\end{pmatrix}");
		ptest.addEQUATION("\\sum_{\\mathclap{1\\le i\\le j\\le n}} x_{ij}");
		
		ptest.addEQUATION("\\cfrac{a}{1 + \\cfrac{1}{b}}");
				
     	ptest.addEQUATION("\\sqrt[3]{x} ");
		ptest.addEQUATION("E = {mc^2} ");
				
	
		ptest.addEQUATION("p(x) = x^8+x^7+x^6+x^5\\\\ \r\n"
				+ "	- x^4 - x^3 - x^2 - x ");
		
	
		ptest.addUML("graph TD;\r\n"
				+ "    A-->B;\r\n"
				+ "    A-->C;\r\n"
				+ "    B-->D;\r\n"
				+ "    C-->D;");
		
		
		ptest.addUML("sequenceDiagram\r\n"
				+ "    participant Alice\r\n"
				+ "    participant Bob\r\n"
				+ "    Alice->>John: Hello John, how are you?\r\n"
				+ "    loop Healthcheck\r\n"
				+ "        John->>John: Fight against hypochondria\r\n"
				+ "    end\r\n"
				+ "    Note right of John: Rational thoughts <br/>prevail!\r\n"
				+ "    John-->>Alice: Great!\r\n"
				+ "    John->>Bob: How about you?\r\n"
				+ "    Bob-->>John: Jolly good!");
		
		
		
		ptest.addUML("gantt\r\n"
				+ "dateFormat  YYYY-MM-DD\r\n"
				+ "title Adding GANTT diagram to mermaid\r\n"
				+ "excludes weekdays 2014-01-10\r\n"
				+ "\r\n"
				+ "section A section\r\n"
				+ "Completed task            :done,    des1, 2014-01-06,2014-01-08\r\n"
				+ "Active task               :active,  des2, 2014-01-09, 3d\r\n"
				+ "Future task               :         des3, after des2, 5d\r\n"
				+ "Future task2               :         des4, after des3, 5d");
		
		ptest.addUML("classDiagram\r\n"
				+ "Class01 <|-- AveryLongClass : Cool\r\n"
				+ "Class03 *-- Class04\r\n"
				+ "Class05 o-- Class06\r\n"
				+ "Class07 .. Class08\r\n"
				+ "Class09 --> C2 : Where am i?\r\n"
				+ "Class09 --* C3\r\n"
				+ "Class09 --|> Class07\r\n"
				+ "Class07 : equals()\r\n"
				+ "Class07 : Object[] elementData\r\n"
				+ "Class01 : size()\r\n"
				+ "Class01 : int chimp\r\n"
				+ "Class01 : int gorilla\r\n"
				+ "Class08 <--> C2: Cool label");
	
		
		
		phome.add(ptest);
		APage  phomeAccount=com.zoubworld.Crypto.server.account.BuildPages.getAccountPage();
		phome.add(phomeAccount);
		
		;
		
		APage p=new FilesPage(new File("C:\\temp\\Account\\"));
		phome.add(p);
		/*
		JavaUtils.saveAs(PathService.getServerHttpDir(PathService.getAccount())+p.getFilename(), p.toString());
		JavaUtils.saveAs(PathService.getServerHttpDir(PathService.getAccount())+JavaHTTPServer.DEFAULT_FILE, phome.toString());*/
		
		phome.save(PathService.getHomeDir());
		
	}

}
