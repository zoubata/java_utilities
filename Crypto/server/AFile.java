package com.zoubworld.Crypto.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import com.zoubworld.Crypto.server.Peer.peerbase.PeerInfo;
import com.zoubworld.Crypto.server.Peer.peerbase.PeerMessage;
import com.zoubworld.Crypto.server.Utils.IStoreObject;
import com.zoubworld.Crypto.server.Utils.StoreList;


class AFile extends File implements IStoreObject
	{
		public AFile(File parent) {
			super(parent.getAbsoluteFile());
			// TODO Auto-generated constructor stub
		}

		static long ByteArrayToLong(byte b1, byte b2, byte b3,byte b4,
				byte b5, byte b6, byte b7,byte b8
				)
		{
			byte[] bytes= {b1,b2,b3,b4,b5,b6,b7,b8};
			return ByteArrayToLong( bytes);
		}
		static long ByteArrayToLong(byte[] bytes)
		 {
		 long value = 0l;

		// Iterating through for loop
		for (byte b : bytes) {
		// Shifting previous value 8 bits to right and
		// add it with next value
		value = (value << 8) + (b & 255);
		}
		return value;
		}
		static int ByteArrayToInt(byte b1, byte b2, byte b3,byte b4)
		{
		byte[] bytes= {b1,b2,b3,b4};
		return ByteArrayToInt(bytes);
	}
static int ByteArrayToInt(byte[] bytes)
		 {
		 int value = 0;

		// Iterating through for loop
		for (byte b : bytes) {
		// Shifting previous value 8 bits to right and
		// add it with next value
		value = (value << 8) + (b & 255);
		}
		return value;
		}
		
		public AFile Factory(byte[]  header, byte[]  data) {
			int i=0;
			byte len=header[i++];
			byte flag=header[i++];
			int length=ByteArrayToInt (header[i++],header[i++],header[i++],header[i++]);
			byte hash[]= {header[i++],header[i++],header[i++],header[i++]    ,header[i++],header[i++],header[i++],header[i++],
					header[i++],header[i++],header[i++],header[i++]    ,header[i++],header[i++],header[i++],header[i++],
					header[i++],header[i++],header[i++],header[i++]    ,header[i++],header[i++],header[i++],header[i++],
					header[i++],header[i++],header[i++],header[i++]    ,header[i++],header[i++],header[i++],header[i++]			
			};
			
			long filesize=ByteArrayToLong (header[i++],header[i++],header[i++],header[i++]    ,header[i++],header[i++],header[i++],header[i++]);
			long lastModified=ByteArrayToLong (header[i++],header[i++],header[i++],header[i++]    ,header[i++],header[i++],header[i++],header[i++]);
			String filepath="";
			for(;i<len+length;i++)
				filepath+=header[i];
			if ((flag&16)!=0)//exist/create
			{
			File f=new File(filepath);
			if ((flag&8)==0)
				try {
					Files.write(f.toPath(), data);
					f.setExecutable((flag&1)!=0);
					f.setWritable((flag&2)!=0);
					f.setReadable((flag&4)!=0);
					f.setLastModified(lastModified);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			else f.mkdirs();
			
			
			}
			return null;
			
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
		@Override
		public IStoreObject Parser(String line) {
			String[] t = line.split(",");
			String filename=t[0];
			long lastModified=Long.parseLong(t[1]);
			long length=Long.parseLong(t[2]);			
			byte[] checksum = Base64.getDecoder().decode(t[3]);
			
			return null;
		}
		@Override
		public String toLine() {
			
			try {
				return getAbsolutePath().replace(getRootDir(), "./")
						+" , "+lastModified()
						+" , "+this.length()
						+" , "+Base64.getEncoder().encodeToString(this.checksum());
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		private String getRootDir() {
			return "";
		}	
	}
