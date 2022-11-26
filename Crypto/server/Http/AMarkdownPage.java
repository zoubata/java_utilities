package com.zoubworld.Crypto.server.Http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import org.tautua.markdownpapers.Markdown;
import org.tautua.markdownpapers.parser.ParseException;

/**
 * @author zoubata
 * build an html page from arkDwn Format
 * example : https://stackedit.io/app# support Latex equation, but not mermaid uml/graph
 * https://fr.wikipedia.org/wiki/Markdown
 */
public class AMarkdownPage extends APage {

	String mdFile="";
	public AMarkdownPage(String path,String mdFile ) 
	{
		super();
		this.mdFile=path+mdFile;
		setFilename(mdFile.replace(".md", ".html"));
	}
	
	/** return the body of the page
	 * */
	private String getBody() {
		Reader in;
		try {
			in = new FileReader(mdFile);
		
	//	Writer out = new FileWriter("c:\\temp\\out.html");
		 Writer out = new StringWriter();
		Markdown md = new Markdown();
		md.transform(in, out);
		body = out.toString(); // now it works fine
		
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
		return body;
	}
}