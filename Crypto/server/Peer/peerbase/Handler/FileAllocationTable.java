/**
 * 
 */
package com.zoubworld.Crypto.server.Peer.peerbase.Handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zoubworld.Crypto.server.Peer.peerbase.PeerMessage;



/**
 * @author zoubata
 *
 */
public class FileAllocationTable {
	List<AFile> table=new ArrayList<AFile>();
	Date lastRefresh=null;//last write on disk or date of last fetch
	
	@SuppressWarnings("serial")
	class AFile extends File
	{
		public AFile(File parent) {
			super(parent.getAbsoluteFile());
			// TODO Auto-generated constructor stub
		}
		public AFile Factory(byte[]  header, byte[]  data) {
			int i=0;
			byte len=header[i++];
			byte flag=header[i++];
			int length=PeerMessage.ByteToIntArray (header[i++],header[i++],header[i++],header[i++]);
			byte hash[]= {header[i++],header[i++],header[i++],header[i++]    ,header[i++],header[i++],header[i++],header[i++],
					header[i++],header[i++],header[i++],header[i++]    ,header[i++],header[i++],header[i++],header[i++],
					header[i++],header[i++],header[i++],header[i++]    ,header[i++],header[i++],header[i++],header[i++],
					header[i++],header[i++],header[i++],header[i++]    ,header[i++],header[i++],header[i++],header[i++]			
			};
			
			long filesize=PeerMessage.ByteToLongArray (header[i++],header[i++],header[i++],header[i++]    ,header[i++],header[i++],header[i++],header[i++]);
			long lastModified=PeerMessage.ByteToLongArray (header[i++],header[i++],header[i++],header[i++]    ,header[i++],header[i++],header[i++],header[i++]);
			String filepath="";
			for(;i<len+length;i++)
				filepath+=header[i];
			if ((flag&16)!=0)//exist/create
			{
			File f=new File(filepath);
			if ((flag&8)==0)//file ?
			Files.write(f.toPath(), data);
			else f.mkdirs();
			
			f.setExecutable((flag&1)!=0);
			f.setWritable((flag&2)!=0);
			f.setReadable((flag&4)!=0);
			f.setLastModified(lastModified);
			}
			
		}
		byte[]  Header() throws IOException, NoSuchAlgorithmException 
		{
			byte flag=(byte) ( (this.canExecute()?1:0)+
					(this.canWrite()?2:0) +
					(this.canRead()?4:0) +
					(this.isDirectory()?8:0) + 
					(this.exists()?16:0)
					);
			String filepath=this.getAbsolutePath();
			return concatenate(
					
			(byte)(256/8+8+8+1+1+4),//+len
			flag,
			PeerMessage.intToByteArray(filepath.length()),
			checksum(),
			PeerMessage.longToByteArray (this.length()),
			PeerMessage.longToByteArray (this.lastModified()),
			filepath.getBytes()
			);
						
		}
		byte[]  data() throws IOException
		{
			return Files.readAllBytes(this.toPath());
		}
		 private byte[] concatenate(byte b, byte flag, byte[] intToByteArray, byte[] checksum, byte[] longToByteArray,
				byte[] longToByteArray2, byte[] bytes) {

			 byte tmp[]=new byte[1+1+intToByteArray.length+checksum.length+longToByteArray.length+longToByteArray2.length+bytes.length];
			 int i=0;
			 tmp[i++]=b;
			 tmp[i++]=flag;
			 for(int j=0;j<intToByteArray.length;j++,i++)
				 tmp[i]=intToByteArray[j];
						 
			 for(int j=0;j<checksum.length;j++,i++)
				 tmp[i]=checksum[j];
						 
			 for(int j=0;j<longToByteArray.length;j++,i++)
				 tmp[i]=longToByteArray[j];
						 
			 for(int j=0;j<longToByteArray2.length;j++,i++)
				 tmp[i]=longToByteArray2[j];
						 
			 for(int j=0;j<bytes.length;j++,i++)
				 tmp[i]=bytes[j];			 
			return tmp;
		}
		 
		private byte[] checksum( ) throws IOException, NoSuchAlgorithmException {
			 MessageDigest md = MessageDigest.getInstance("SHA-256"); //SHA, MD2, MD5, SHA-256, SHA-384...
		        // file hashing with DigestInputStream
		        try (DigestInputStream dis = new DigestInputStream(new FileInputStream((File)this), md)) {
		            while (dis.read() != -1) ; //empty loop to clear the data
		            md = dis.getMessageDigest();
		        }		       
		        return md.digest();
		    }	
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
			
	}

}
