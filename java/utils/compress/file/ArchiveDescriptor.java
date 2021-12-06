/**
 * 
 */
package com.zoubworld.java.utils.compress.file;

import java.io.File;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zoubworld.java.utils.compress.Symbol;
import com.zoubworld.utils.JavaUtils;

/**
 * @author pierre valleau
 *
 */
public class ArchiveDescriptor {

	List<FileDescriptor> files;

	/**
	 * 
	 */
	public ArchiveDescriptor(File dir) {
		files = new ArrayList<FileDescriptor>();
		addfiles(dir);
	}
	public ArchiveDescriptor( ) {
		files = new ArrayList<FileDescriptor>();
	}

	public void addfiles(File dir) {
		addfiles(dir.getAbsolutePath(), dir);
	}
	public void addfiles(String homePath,File dir) {
				for (File f : dir.listFiles())
			if (f.isFile())
				addfile(new FileDescriptor(homePath,f));
			else if (f.isDirectory())
				addfiles(homePath,f);
			else
				System.err.println("not managed " + f);
	}
	public void addfile(File f) {	
		addfile(new FileDescriptor(f));		
}
	public void addfile(FileDescriptor f) {	
		files.add(f);		
}

	Map<Long, List<FileDescriptor>> redondant = null;

	public void optimize() {
		
		Comparator<FileDescriptor> compareBySize = (FileDescriptor o1, FileDescriptor o2) -> o1.getSize()
				.compareTo(o2.getSize());

		Collections.sort(files, compareBySize);

		Collections.sort(files, Comparator.comparing(FileDescriptor::getExtention)
				//.thenComparing(FileDescriptor::getSize)
				.thenComparing(FileDescriptor::getEntropie).thenComparing(FileDescriptor::getCrc64));

		/*
		redondant = new HashMap<Long, List<FileDescriptor>>();
		Long crc64old = null;
		Long sizeold = -1L;
		for (int i = 0; i < files.size(); i++) {
			long size = files.get(i).getSize();
			if (size == sizeold) {
				Long crc64 = files.get(i).getCrc64();
				if (crc64old == null && i > 0)
					crc64old = files.get(i - 1).getCrc64();

				if (crc64.equals(crc64old)) {
					List<FileDescriptor> l = redondant.get(crc64);
					if (l == null)
						redondant.put(crc64, l = new ArrayList<FileDescriptor>());
					if (!l.contains(files.get(i - 1)))
						l.add(files.get(i - 1));
					l.add(files.get(i));
				}
				crc64old = crc64;
			} else
				crc64old = null;
			sizeold = size;
		}
		for (List<FileDescriptor> l : redondant.values())
			files.removeAll(l);
			*/
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArchiveDescriptor a = new ArchiveDescriptor(new File("C:\\home_user\\work\\acpnetapp05\\prober_vision_volume\\data\\661A7_N04U"));

		System.out.println("arch size " + a.getSize());
		System.out.println("arch nb file " + a.getNumberElement());
		a.optimize();
		if (a.files != null)
			for (FileDescriptor f : a.files) {
				System.out.println("\t+ " + f.getSize() + "\t" + Long.toHexString(f.getCrc64()) +" "+ String.format("%1.2f", f.getEntropie())+"\t" + f.file);
			//	System.out.println(JavaUtils.Format(f.getDFreq(),"->",", ",s->s.toString(),z->String.format("%2.2f", z*100)));
			}

		if (a.redondant != null)
			for (List<FileDescriptor> l : a.redondant.values()) {
				System.out.println("\t-redondant");
				for (FileDescriptor f : l)
					System.out.println("\t\t+" + f.getSize() + "\t" + Long.toHexString(f.getCrc64()) + "\t" + f.file);
			}
	}

	public long getNumberElement() {
		if (files != null)
			return files.size() + 0L;
		return 0;
	}

	public Long getSize() {
		long l = 0;
		if (files != null)
			for (FileDescriptor f : files)
				l += f.getSize();
		return l;
	}

}
