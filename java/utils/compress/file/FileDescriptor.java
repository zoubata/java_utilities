package com.zoubworld.java.utils.compress.file;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.zoubworld.java.utils.compress.HuffmanCode;
import com.zoubworld.java.utils.compress.ISymbol;
import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.utils.JavaUtils;

public class FileDescriptor {

		File file;
		String pathfilename;
		public Long getSize()
		{
			if(file==null)
				return 0L;
			return file.length();
		}
		CRC64 crc64=null;
		List<ISymbol> symList = null;
			Map<ISymbol, Long> freq = null;
			
		/**
			 * @return the symList
			 */
			public List<ISymbol> getSymList() {
				if (symList==null)
					symList = Symbol.from(file);				
				return symList;
			}
			
			public  Double getEntropie() 
			{
				return ISymbol.getEntropie( getFreq());
			}
			/**
			 * @return the freq
			 */
			public Map<ISymbol, Long> getFreq() {
				if (freq==null)
				freq = Symbol.Freq(getSymList());
				freq=JavaUtils.SortMapByValue(freq);
				return freq;
			}
			
			public Map<ISymbol, Double> getDFreq() {
				
				Map<ISymbol, Double> dfreq=new HashMap<ISymbol, Double>();
				double s=0;
				for(Entry<ISymbol, Long> e:getFreq().entrySet())
					s+=e.getValue();
					for(Entry<ISymbol, Long> e:getFreq().entrySet())
						{
					dfreq.put(e.getKey(),((double)e.getValue())/s);
				}		
					dfreq=JavaUtils.SortMapByValue(dfreq);
					return dfreq;
			}
			/**
			 * @return
			 * @see java.io.File#getName()
			 */
			public String getName() {
				return pathfilename;
			}

			public static Double norme1(FileDescriptor f1,FileDescriptor f2)
			{				return norme( f1, f2,1);			}
			public static Double norme2(FileDescriptor f1,FileDescriptor f2)
			{				return norme( f1, f2,2);			}
			
			public static Double norme(FileDescriptor f1,FileDescriptor f2,double p)
			{				
				Map<ISymbol, Double> d1=f1.getDFreq();
				Map<ISymbol, Double> d2=f2.getDFreq();
				
				Set<ISymbol> s=new HashSet<ISymbol>();
				s.addAll(d1.keySet());
				s.addAll(d2.keySet());
				double ds=0.0;
				for(ISymbol sym:s)
				{
					Double a1=d1.get(sym);
					Double a2=d2.get(sym);
					if(a1==null)
						a1=0.0;
					if(a2==null)
						a2=0.0;
					ds+=Math.pow(Math.abs(a1-a2),p);
					
				}
				return Math.pow(ds, 1.0/p);
				
			}
			
			Crc16 crc16=null;
			public Integer getCrc16()
			{
				if(file==null)
					return 0;
				if(crc16==null)
					try {
					//	System.out.println("#compute CRC of "+file);
						crc16=Crc16.fromFile(file);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				if(crc16==null)
					return 0;
					return crc16.getValue();
			}
			public Long getCrc64()
			{
				if(file==null)
					return 0L;
				if(crc64==null)
					try {
					//	System.out.println("#compute CRC of "+file);
						crc64=CRC64.fromFile(file);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				if(crc64==null)
					return 0L;
					return crc64.getValue();
			}
		public String getExtention()
		{
			if(file==null)
				return "";
			String n= file.getName();
			if (n.contains("."))
			return n.substring(n.lastIndexOf("."),n.length());
			return "";
		}
		public FileDescriptor(File f) {
			file=f;
			pathfilename=f.getAbsolutePath();
			
		}
		public FileDescriptor(String homepath,File f) {
			file=f;
			pathfilename=f.getAbsolutePath().replace(homepath, "");
			
		}

	public FileDescriptor(String path,String filename, long size, long crc, List<ISymbol> list) {
		symList=list;
		pathfilename	=path+filename;
	Symbol.toAFile(file=JavaUtils.createFile(pathfilename),  symList);
	}

}
