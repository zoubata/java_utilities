package com.zoubworld.Crypto.server.Http;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zoubworld.Crypto.server.PathService;
import com.zoubworld.Crypto.server.account.Iaccount;
import com.zoubworld.utils.JavaUtils;

// The tutorial can be found just here on the SSaurel's Blog : 
// https://www.ssaurel.com/blog/create-a-simple-http-web-server-in-java
// Each Client Connection will be managed in a dedicated Thread
public class JavaHTTPServer implements Runnable{ 
	
	public static File WEB_ROOT1 = null;
	/**
	 * @return the webRoot
	 * like : "C:\\temp\\DvyZaJyYVyaw\\Server\\Http"
	 */
	public static File getWebRoot() {
		if (WEB_ROOT1!=null)
			return WEB_ROOT1;
		return WEB_ROOT1=new File(PathService.getServerHttpDir(PathService.getAccount()));
	}

	public static final String DEFAULT_FILE = "index.html";
	public static final String DEFAULT_FILE2 = "index.htm";
	public static final String FILE_NOT_FOUND = "404.html";
	public static final String METHOD_NOT_SUPPORTED = "not_supported.html";
	// port to listen connection
	static final int PORT = 8080;
	
	// verbose mode
	static final boolean verbose = true;
	
	// Client Connection via Socket Class
	private Socket connect;
	
	public JavaHTTPServer(Socket c) {
		connect = c;
	}
	
	public static void main(String[] args) {
		BuildPages.main(args);
		try {
			ServerSocket serverConnect = new ServerSocket(PORT);
			System.out.println("Server started.\nListening for connections on port : " + PORT + " ...\n");
			
			// we listen until user halts server execution
			while (true) {
				JavaHTTPServer myServer = new JavaHTTPServer(serverConnect.accept());
				
				if (verbose) {
					System.out.println("Connecton opened. (" + new Date() + ")");
				}
				
				// create dedicated thread to manage the client connection
				Thread thread = new Thread(myServer);
				thread.start();
			}
			
		} catch (IOException e) {
			System.err.println("Server Connection error : " + e.getMessage());
		}
	}

	@Override
	public void run() {
		// we manage our particular client connection
		BufferedReader in = null; PrintWriter out = null; BufferedOutputStream dataOut = null;
		String fileRequested = null;
		
		try {
			// we read characters from the client via input stream on the socket
			in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
			// we get character output stream to client (for headers)
			out = new PrintWriter(connect.getOutputStream());
			// get binary output stream to client (for requested data)
			dataOut = new BufferedOutputStream(connect.getOutputStream());
			// get first line of the request from the client
			String input = in.readLine();
			if (verbose)
				System.out.println(">" + input + " ");
			
			
			if (input==null)
			{
				if (verbose)
					System.out.println("oups : '" + input + "' .");
					
			}
			else
			{
			// we parse the request with a string tokenizer
			StringTokenizer parse = new StringTokenizer(input);
			String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client : POST, GET
			// we get file requested
			fileRequested = parse.nextToken();
			String standard = parse.nextToken();//HTTP/1.1
			
			Map<String,String> httpQuery=new HashMap<String,String>();
			
			Map<String,String> query=new HashMap<String,String>();
			
			if (method.equals("GET")  &&  fileRequested.contains("?"))
				{
				query=processInputGetQuery(fileRequested);
				fileRequested=fileRequested.split("\\?")[0];
				}
			if (method.equals("GET")  ||  method.equals("POST"))
			   httpQuery=processInputHttp(in);
			
			if (httpQuery.get("Content-Type")!=null
					&& httpQuery.get("Content-Type").equals("application/json") 
					&&  method.equals("POST"))
				query=processInputPostJson(in);
			
			
			if (httpQuery.get("Content-Type")!=null && httpQuery.get("Content-Type").startsWith("multipart/form-data;")
					&&  method.equals("POST"))
					{

			char[] datafile = processInputPostformdata( query, in);
					
					}
			
			
		
		
			File file=getFile(fileRequested);
		
			
			if (verbose)
				System.out.println("fileRequested : '" + fileRequested + "' .");
			
			// we support only GET and HEAD methods, we check
			if (method.equals("GET")  || method.equals("POST")  ||  method.equals("HEAD"))
				{
				// GET or HEAD method
			
				String content = getContentType(fileRequested);
				
				if (method.equals("GET") || method.equals("POST") ) 
				{ // GET method so we return content
					byte[] fileData =null;
					String p=null;
					
					
					 if ((file!=null) && file.exists())
					 {
							
							fileData= readFileData(file);
							content = getContentType(file.getAbsolutePath());
						// send HTTP Headers
						out.println("HTTP/1.1 200 OK");
						out.println("Server: Java HTTP Server from zoubworld.server.Http : 0.1");
						
						out.println("Allow: GET, POST, HEAD");//Allow: GET, POST, HEAD, PUT, DELETE, CONNECT, OPTIONS, TRACE, PATCH
						
						out.println("Date: " + new Date());
						out.println("Content-type: " + content);
						out.println("Content-length: " + fileData.length);
						
						out.println(); // blank line between headers and content, very important !
						out.flush(); // flush character output stream buffer
						
						dataOut.write(fileData, 0, fileData.length);
						dataOut.flush();
						
					}
					else
					{
						p=PathService.findalias("http://localhost"+fileRequested);
						
					
						// send HTTP Headers
						//out.println("HTTP/1.1 301 Moved Permanently");//https://developer.mozilla.org/fr/docs/Web/HTTP/Status/301
						out.println("HTTP/1.1 302 Found");//https://developer.mozilla.org/fr/docs/Web/HTTP/Status/302
						
						out.println("Location: "+p+" ");//
						//https://developer.mozilla.org/fr/docs/Web/HTTP/Headers/Location
						
						//out.println("Allow: GET ; HEAD");//Allow: GET, POST, HEAD
					
						out.flush(); // flush character output stream buffer
						
						if (verbose) {
							System.out.println("redirect to " + p + ". ");
						}
					//	dataOut.write(fileData, 0, fileData.length);
						dataOut.flush();
						}
				
				if (verbose) {
					System.out.println("File " + fileRequested + " of type " + content + " returned");
					}
				}
				
			}
			else
			if (!method.equals("GET")  &&  !method.equals("HEAD")  &&  !method.equals("POST")) 
			{
				
				
				if (verbose) {
					System.out.println("501 Not Implemented : " + method + " method.");
				}
				if (verbose) {
					System.out.println("405 Method Not Allowed " + method + " method.");
					//https://developer.mozilla.org/fr/docs/Web/HTTP/Status/405
				}
				// we return the not supported file to the client
				file = new File(getWebRoot(), METHOD_NOT_SUPPORTED);
				String contentMimeType = "text/html";
				//read content to return to client
				byte[] fileData = readFileData(file);
					
				// we send HTTP Headers with data to client
				out.println("HTTP/1.1 501 Not Implemented");
				out.println("Server: Java HTTP Server from zoubworld.server.Http : 0.1");
				out.println("Date: " + new Date());
				out.println("Content-type: " + contentMimeType);
				out.println("Content-length: " + fileData.length);
				out.println(); // blank line between headers and content, very important !
				out.flush(); // flush character output stream buffer
				// file
				dataOut.write(fileData, 0, fileData.length);
				dataOut.flush();
				System.out.println("unsupported : "+input);	
			} 
				else
					System.err.println("oups unsupported : "+input);	
			}	
		} catch (FileNotFoundException fnfe) {
			try {
				fileNotFound(out, dataOut, fileRequested);
			} catch (IOException ioe) {
				System.err.println("Error with file not found exception : " + ioe.getMessage());
			}
			
		} catch (IOException ioe) {
			System.err.println("Server error : " + ioe);
		} finally {
			try {
				in.close();
				out.close();
				dataOut.close();
				connect.close(); // we close socket connection
			} catch (Exception e) {
				System.err.println("Error closing stream : " + e.getMessage());
			} 
			
			if (verbose) {
				System.out.println("Connection closed.\n");
			}
		}
		
		
	}
	
	private File getFile(String fileRequested) {
		File file = new File(getWebRoot(), fileRequested);
		if (fileRequested.endsWith("/")) {			
			file = new File(getWebRoot(), fileRequested + DEFAULT_FILE);
			if (file.exists())
				return file;
			file = new File(getWebRoot(), fileRequested + DEFAULT_FILE2);
			if (file.exists())
				return file;
			fileRequested += DEFAULT_FILE;
		}
		if (verbose) 
			System.out.println("try " + file.getAbsolutePath());
		
		if (!file.exists())
		{
			file = new File(PathService.getHomeDir(), fileRequested);
			if (verbose) 
				System.out.println("try " + file.getAbsolutePath());
		}
		if (!file.exists())
		{		
		 fileRequested=PathService.resolve("http://localhost"+fileRequested);
		 file=new File(fileRequested);
			 if (verbose) {
					System.out.println("try " + file.getAbsolutePath());
			 }
		}
		if (!file.exists())
			return null;
		return file;
	}

	Pattern pquery=Pattern.compile("([^\\?]*)\\?([^\\?]+)");
	Pattern pparam2=Pattern.compile("([\\w\\d]+)=([\\w\\d]*)&?");
	Pattern pparam1=Pattern.compile("([\\w\\d]+)");
	
	/** parse input 1st String from a http request for GET method */
	private Map<String, String> processInputGetQuery(String  fileRequested) {
		Map<String,String> query=new HashMap<String,String>();		
		Matcher m;
		if (fileRequested.contains("?"))				
		if ((m=pquery.matcher(fileRequested)).find())
		{
			String param=m.group(2);
			 Matcher m2 = Pattern.compile("([^&=]+)=([^&=]+)")
			     .matcher(param);
			 while (m2.find()) {				
			   query.put(m2.group(1),m2.group(2));
			 }
		}
		return query;
	}

	/** parse input stream data from a http request  Post with Json*/
	private Map<String, String> processInputPostJson(BufferedReader in) throws IOException {
		String line="";
		while(in.ready() && !line.contains("}"))
			line+=(char)in.read();
		return JavaUtils.parseMapStringString(line, ":", ",");
	}

	/*  -----------------------------233717507785802449472151535
	    Content-Disposition: form-data; name="file"; filename="0A00E000.hex.jconfig"
        Content-Type: application/octet-stream
*/
	/** process in data from http request
	 * 
	 associatd to :
  <input id="fileupload" type="file" name="fileupload" /> 
  <button id="upload-button" onclick="uploadFile()"> Upload </button>
	  <script>
  async function uploadFile() {
  let formData = new FormData(); 
  formData.append("file", fileupload.files[0]);
  await fetch('/upload.php', {
    method: "POST",
    body: formData
  }); 
  alert('The file has been uploaded successfully.');
  }
  </script>
  */
	private char [] processInputPostformdata( Map<String, String>  http,BufferedReader in) throws IOException {
		
		String line="";
		String Mark=null;
		while(in.ready() && !(line=in.readLine()).isBlank())
		{
			if (verbose) 
				System.out.println(">"+line);
			if (line.isBlank())
				break;
			Matcher ma;
			if ((ma=pparamh.matcher(line)).find())
			{
				http.put(ma.group(1).trim(), ma.group(2).trim());
			}
			else
				Mark=line;
		}
		if (http.get("Content-Disposition")!=null)
		{
		String[] t = http.get("Content-Disposition").split(";");
		for(String s:t)
			if (s.contains("="))
				http.put(s.split("=")[0].trim(), s.split("=")[1].trim());
			else
				http.put(s,"");
		}	
		char buf[]=new char[1024*1024*256];
		int len=in.read(buf);
		int marklen=+"\r\n--".length()+Mark.length()+"\r\n".length();
		char bufo[]=new char[len-marklen];
		System.arraycopy(buf,0,bufo,0,len-marklen);
		return bufo;
	}

	Pattern pparamh=Pattern.compile("([^:]+):(.+)");
	/** parse input strean from a http request */
	private Map<String, String> processInputHttp(BufferedReader in) throws IOException {
		Map<String, String> m=new HashMap<String, String>();
		String l="";
		Matcher ma;
		int i=0;
		for(i=130;i>0;i--)
		{
			l=in.readLine();
			if (verbose) {
				System.out.println(">"+l);
			}
			if (l.isBlank())
				break;
			if ((ma=pparamh.matcher(l)).find())
			{
				m.put(ma.group(1).trim(), ma.group(2).trim());
			}
			
		}
		return m;
	}

	private byte[] readFileData(File file) throws IOException {
		System.out.println("readFileData : " + file.getAbsolutePath() + " .");
		
		FileInputStream fileIn = null;
		byte[] fileData = new byte[(int) file.length()];
		
		try {
			fileIn = new FileInputStream(file);
			fileIn.read(fileData);
		} finally {
			if (fileIn != null) 
				fileIn.close();
		}
		
		return fileData;
	}
	
	// return supported MIME Types
	private String getContentType(String fileRequested) {
		if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html"))
			return "text/html";
		else
			if (fileRequested.endsWith(".csv") )
				return "text/csv";
			else
				if (fileRequested.endsWith(".xml") )
					return "text/xml";
				else
					if (fileRequested.endsWith(".css") )
						return "text/css";
					else
						if (fileRequested.endsWith(".mpeg") )
							return "video/mpeg";
						else
							if (fileRequested.endsWith(".mp4") )
								return "video/mp4";
		/*
		  Video	video/mpeg
video/mp4
video/quicktime
video/x-ms-wmv
video/x-msvideo
video/x-flv
video/webm*/
						else
							/*
Image	
image/gif
image/jpeg
image/png
image/tiff
image/vnd.microsoft.icon
image/x-icon
image/vnd.djvu
image/svg+xml
							 */
							if (fileRequested.endsWith(".gif") )
								
								return "image/gif";
							else
								if (fileRequested.endsWith(".jpg") || fileRequested.endsWith(".jpeg") )
									
									return "image/jpeg";
								else
									if (fileRequested.endsWith(".png") )
										
										return "image/png";
									else
										if (fileRequested.endsWith(".tiff") )
											
											return "image/tiff";
										else
											if (fileRequested.endsWith(".svg") )
												
												return "image/svg+xml";
	else
		if (fileRequested.endsWith(".mpeg") ||fileRequested.endsWith(".mp3") ||fileRequested.endsWith(".mp2") )
			return "audio/mpeg";
		else
			if (fileRequested.endsWith(".wma") )
				return "audio/x-ms-wma";
			else
				/*
				 Application	application/EDI-X12
application/EDIFACT
application/javascript
application/octet-stream
application/ogg
application/pdf
application/xhtml+xml
application/x-shockwave-flash
application/json
application/ld+json
application/xml
application/zip
application/x-www-form-urlencoded
*/						
				if (fileRequested.endsWith(".js") )
					return "application/javascript";
				else
					if (fileRequested.endsWith(".json") )
						return "application/json";
					else
						if (fileRequested.endsWith(".pdf") )
						return "application/pdf";
					else
						if (fileRequested.endsWith(".zip") )
							return "application/zip";
						else
													
					
					
												
			return "text/plain";
		
	}
	
	private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
		File file = new File(getWebRoot(), FILE_NOT_FOUND);
		String content = "text/html";
		byte[] fileData = readFileData(file);
		
		out.println("HTTP/1.1 404 File Not Found");
		out.println("Server: Java HTTP Server from SSaurel : 1.0");
		out.println("Date: " + new Date());
		out.println("Content-type: " + content);
		out.println("Content-length: " + fileData.length);
		out.println(); // blank line between headers and content, very important !
		out.flush(); // flush character output stream buffer
		
		dataOut.write(fileData, 0, fileData.length);
		dataOut.flush();
		
		if (verbose) {
			System.out.println("File " + fileRequested + " not found");
		}
	}
	
}