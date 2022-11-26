package com.zoubworld.Crypto.server.account;

import java.math.BigInteger;
import java.util.Map;
import org.apache.commons.codec.digest.DigestUtils;
public interface Iaccount {

	public Iwallet getWallet();
	public Map<String,String> getProperties();
	public Map<String,String> loadProperties();
	public  void saveProperties(Map<String,String> p);
	
	public String getHomeDir();
	/** return an Id string on 12 char , allowed char are a-z, A-Z,0-9 
	 * */
	public default String getId12s()
	{
		String s="";
		IKey k = getWallet().getPublicKey();
		byte[] b = DigestUtils.sha256(k.toString());
		BigInteger bi= new BigInteger(b);
		int alphabet=26+26+10;
		bi=bi.mod(BigInteger.valueOf(alphabet).pow(12));
		for(;bi.bitLength()>1;)
		{	s+=enc62(bi.mod(BigInteger.valueOf(alphabet)).intValue());
				bi=bi.divide(BigInteger.valueOf(alphabet));}
		return s;
	}
	default char enc62(int i)
	{
		if (i<26)
			return (char) ('a'+i);
		if (i<26*2)
			return (char) ('A'+i-26);
		if (i<26*2+10)
			return (char) ('0'+i-52);
		return (char) '_';
		
	}
	public default String getAccountType()
	{
	return  "NodeZoub";
	}
	public default  void saveProperties()
	{
		Map<String,String> p=getProperties();
		saveProperties(p);
	}
}
