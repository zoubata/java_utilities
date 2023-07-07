package com.zoubworld.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;

/** A file : it contains a files or a folder
 * https://docs.oracle.com/en/java/javase/11/docs/specs/security/standard-names.html#securerandom-number-generation-algorithms
 * */
public class AFile {
	
	void toto()
	{
		 SecureRandom secureRandom;
		try {
			secureRandom = SecureRandom.getInstance("SHA1PRNG");
		
	        secureRandom.setSeed(12345678L);
	        System.out.println(secureRandom.nextLong());
	        } catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

public void loadSmallFile(String filepath)
{
		File f=new File(filepath);
		flatSize=f.length()
		 data = new byte[(int)flatSize];
		 InputStream in;
		try {
			in = new FileInputStream(f);
			   in.read(data);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		nonceHeader=new byte[32];
		datanonce=new byte[32];
		SecureRandom sr=new SecureRandom();
	
		sr.nextBytes(nonceHeader);
		
		sr.nextBytes(datanonce);
		
		FlatIntegrity
				
}
	
	public class Options {
		public final static long RESERVED_Value =     0x000000005A6F7562L;//"Zoub"
		public final static long COMPFORMAT_MASK =    0x0000000F00000000L;
		 public final static long ENCFORMAT_MASK =    0x000000F000000000L;
		 public final static long SCRAMBFORMAT_MASK = 0x00000F0000000000L;
		 public final static long SIGNFORMAT_MASK =   0x0000F00000000000L;
		 public final static long INTEGFORMAT_MASK =  0x000F000000000000L;
		 public final static long ACCESSFORMAT_MASK =   0x00F0000000000000L;
		
		 private long options=RESERVED_Value;
		 public Options( ) {
		 }
		 public void setOptions(final int options) {
		 this.options = options;
		 }
		 public long getOptions( ) {
		 return this.options;
		 }
		}
	//public header :
	Options options;//8 bytes
	long flatSize=0;//8 b
	long reserved1=0;//8 b
	long reserved=0x0;//8 b
	
	byte[] FlatIntegrity=new byte[32];
	
	//private header protected by RSA with public key
	byte[] nonceHeader=new byte[32];// true random number to prevent from hasking.it could be used as a salt for key generation from password
	byte[] signature=new byte[32];
	
	byte[] owner=new byte[32];// the address of the public crypto key of owner
	/**
	 * @return the options
	 */
	public Options getOptions() {
		return options;
	}

	/**
	 * @param options the options to set
	 */
	public void setOptions(Options options) {
		this.options = options;
	}

	/**
	 * @return the signature
	 */
	public byte[] getSignature() {
		return signature;
	}

	/**
	 * @param signature the signature to set
	 */
	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	/**
	 * @return the owner
	 */
	public byte[] getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(byte[] owner) {
		this.owner = owner;
	}

	/**
	 * @return the flatSize
	 */
	public long getFlatSize() {
		return flatSize;
	}

	/**
	 * @return the flatIntegrity
	 */
	public byte[] getFlatIntegrity() {
		return FlatIntegrity;
	}

	byte[] dataFileKey=new byte[32];//<key used to decript the file.
	// data encrypted by fast algo with dataFileKey :
	byte[] datanonce=new byte[32];//<true random number to prevent from hasking of data. It could be use as IV, initial vector for CFM
	
	byte[] datacompressedScrambedEncrypted=new byte[(int)flatSize];//<real data of file
	// compression is done by strandard algo.
	// Scramble is done by an Xor with multiple generation of random algo number, the algo is initialized by the nonceHeader and 100 run of rand() 
	// encyption is done by a standard algo, bu due to scramble the entropie is maximal.
	//end crypted section:
	byte[] ArchiveIntegrity=new byte[32];
	byte[] data;
	
	public static void saveAs(String fileName, InputStream in) {
		File fileOut;
// in = new GZIPInputStream(in);
		if (fileName != null) {
			fileOut = new File(fileName);
		} else {
			System.err.println("error");
			return;
		}
		String dir=JavaUtils.dirOfPath(fileName);
		if (!JavaUtils.fileExist(dir))
			JavaUtils.mkDir(dir);
					
			   // FileOutputStream  
	        OutputStream os;
			try {
				os = new FileOutputStream(fileOut);
		
	       
	        byte[] buf = new byte[8192*256];
	        int length;
	        while ((length = in.read(buf)) != -1) {
	            os.write(buf, 0, length);
	        }
	        // Close
	        os.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
}