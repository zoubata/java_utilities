package com.zoubworld.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.util.Comparator;

public class ContainsFileComparator<T> implements Comparator<T> {

	public ContainsFileComparator() {
		
	}


	@Override
	public int compare(T o1, T o2) {
		 InputStream i1;
		 int d1=0,d2=0;
			
		try {
			i1 = new FileInputStream(((File)  o1));
			
		 InputStream i2 = new FileInputStream(((File)  o2));
		
			while (((d1=i1.read()) != -1)
					 &&
					( (d2=i2.read()) != -1)
					 ) {
			     if(d1!=d2)
			     {
			    	
			    	 return d2-d1;
			    	 }
			    
			 }
			 
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 if (d1==d2) return 0;
			if(d1==-1)
			     return 1;
			else
			 if(d2==-1)
				  return -1;
			    		
		 return 0;
	}

}
