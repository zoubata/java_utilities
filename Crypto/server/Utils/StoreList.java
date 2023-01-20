
/**
 * 
 */
package com.zoubworld.Crypto.server.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.zoubworld.Crypto.server.PathService;
import com.zoubworld.Crypto.server.Server;
import com.zoubworld.utils.JavaUtils;

/**
 * @author zoubata
 *
 */
public abstract class StoreList<T extends IStoreObject> {

	protected List<T> peers=new ArrayList<T>();

	public List<T> getList() {
		return peers;
	}

	/**
	 * 
	 */
	public StoreList() {
	//	load();
	}

	/**
	 * 
	 */
	public void load() {
		load(getFileName());
	}

	public String getPathName() {
		return PathService.getHomeDir();
	}
	
	public String getFileName() {
				T t=getSprout();
		String name = t.getClass().getSimpleName();
		return getPathName() + "." + name + File.separator + getId() + ".list";
	}

	public String getId() {
		
			T t=getSprout();
	String name = t.getClass().getSimpleName();
		return name;
	}

	public void load(String filename) {
		File f = new File(filename);
		String file = JavaUtils.read(f);
/*peers =*/
		 Parser(file);
	}

	public void save() {
		save(getFileName()	);

	}

	public void save(String filename) {
		JavaUtils.saveAs(filename, toLine());
	}

	protected abstract T getSprout();

	public void Parser(String file) {
		if (file==null)
			return ;
		String[] lines = file.split("\\n");
		//List<T>
		peers = new ArrayList<T>();
		for (String line : lines)
		{
			T t = getSprout();
			if(  line!=null && !line.isBlank())
			peers.add((T) (t.Parser(line)));
		}
		//return peers;
		
	}

	public String toLine() {
		String s = "";
		for (T a : peers)
			s += (a.toLine()) + "\n";
		return s;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public int hashCode() {
		return Objects.hash(peers);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StoreList other = (StoreList) obj;
		return Objects.equals(getList(), other.getList());
	}

}
