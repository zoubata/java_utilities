/**
 * 
 */
package com.zoubworld.Crypto.server.Utils;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.zoubworld.Crypto.server.PathService;
import com.zoubworld.utils.JavaUtils;

/**
 * @author zoubata
 *
 */
public class StorePoperty {
	protected  String toLine()
	{
			return "{\r\n"+JavaUtils.Format(JavaUtils.SortMapByKey(property),":","\r\n")+"\r\n}\r\n";
	}
	
	public void Parser(String file) {
		String[] lines = file.split("\\n");
		//List<T>
		property=JavaUtils.parseMapStringString(file, ":", "\r\n");
		
		//return peers;
		
	}
	/**
	 * 
	 */
	public StorePoperty() {
	//	load();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	/**
	 * 
	 */
	public void load() {
		load(getFileName());
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public String get(String key) {
		return getProperties().get(key);
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public String put(String key, String value) {
		return property.put(key, value);
	}

	/**
	 * 
	 * @see java.util.Map#clear()
	 */
	public void clear() {
		property.clear();
	}

	@Override
	public int hashCode() {
		return Objects.hash(property);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StorePoperty other = (StorePoperty) obj;
		return Objects.equals(property, other.property);
	}

	public String getPathName() {
		return PathService.getHomeDir();
	}
	
	public String getFileName() {
				
		String name = this.getClass().getSimpleName();
		return getPathName() + "." + name + File.separator + getId() + ".property";
	}

	public String getId() {
		
	String name = this.getClass().getSimpleName();
		return name;
	}
	public void load(String filename) {
		File f = new File(filename);
		String file = JavaUtils.read(f);
		
/*peers =*/
		if (file!=null)
		 Parser(file);
		else
			init();
	}

	/** initialise property for an missing file*/
	public  void init() {
	
	}
	public void save() {
		
		property.put("LastModified", ""+Calendar.getInstance().getTimeInMillis());
		File f=null;
		
		save(getFileName()	);

	}

	public void save(String filename) {
		JavaUtils.saveAs(filename, toLine());
	}

	protected Map<String,String> property=new HashMap<String,String>();

	public String getProperty(String key) {
		return property.get(key);
	}
	public Map<String,String> getProperties() {
		if(property==null)
		 property=new HashMap<String,String>();
		return property;
	}

}
