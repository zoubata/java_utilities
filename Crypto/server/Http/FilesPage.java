/**
 * 
 */
package com.zoubworld.Crypto.server.Http;

import java.io.File;
import java.util.Set;

import com.zoubworld.Crypto.server.GeoIP;
import com.zoubworld.Crypto.server.Server;
import com.zoubworld.utils.JavaUtils;

/**
 * @author zoubata
 * build a html pages to links local files to allow download from http protocol
 */
public class FilesPage extends APage {

	/**
	 * 
	 */
	public FilesPage(File root) {
		title="Page of this Server : List of Files";
		filename="Files.html";
		Set<String> l = JavaUtils.listFileNames(root.getAbsolutePath(), "", false, false, true);
		body="";
		body+=Header(1,"Download Page");
		
		body+="\t<ul>\r\n";
				for(String s:l)
					body+="<li>"
				+" <a href=\""+"../../"+s+"\">"+s+"</a> "
							+"</li>\r\n";
		body+="</ul>\r\n";
						
			
	}

	/**
	 * @param title
	 * @param body
	 * @param filename
	 */
	private FilesPage(String title, String body, String filename) {
		super(title, body, filename);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		APage p=new FilesPage(new File("C:\\temp\\Account\\"));
				System.out.print(p.toString());
				JavaUtils.saveAs(p.getFilename(), p.toString());

	}

}
